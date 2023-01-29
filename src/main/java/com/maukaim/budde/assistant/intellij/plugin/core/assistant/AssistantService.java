package com.maukaim.budde.assistant.intellij.plugin.core.assistant;

import com.intellij.openapi.components.Service;
import com.intellij.openapi.project.Project;
import com.maukaim.budde.assistant.intellij.plugin.core.ai.openai.OpenAIService;
import com.maukaim.budde.assistant.intellij.plugin.core.assistant.model.Assistant;
import com.maukaim.budde.assistant.intellij.plugin.core.assistant.model.AssistantFactory;
import com.maukaim.budde.assistant.intellij.plugin.core.assistant.persistence.AssistantRepository;
import com.maukaim.budde.assistant.intellij.plugin.core.chat.ChatHistoryRepository;
import com.maukaim.budde.assistant.intellij.plugin.core.chat.model.*;
import com.maukaim.budde.assistant.intellij.plugin.listeners.*;
import com.maukaim.budde.assistant.intellij.plugin.shared.BuddeAssistantTopics;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public final class AssistantService {
    public static final Assistant PLACEHOLDER_ASSISTANT = AssistantFactory.build("",
            "None Available",
            "PlaceHolder for smoother bug.",
            null,
            null,
            List.of(),
            0.5F,
            Instant.EPOCH);

    private final Project ctx;
    private volatile boolean promptCouldBeSent = true;
    private final ExecutorService executorService;

    public AssistantService(Project project) {
        this.ctx = project;
        executorService = Executors.newFixedThreadPool(3);
    }

    public void sendPrompt(String prompt) {
        setInPromptProcess();

        executorService.submit(() -> {
            try {
                OpenAIService openAiService = ctx.getService(OpenAIService.class);
                ChatHistoryRepository historyRepository = ctx.getService(ChatHistoryRepository.class);
                Assistant currentAssistant = getCurrentAssistant();
                List<RawMessage> history = historyRepository.getPreviousDiscussion(currentAssistant.getId());
                historyRepository.addMessage(currentAssistant.getId(), new UserMessage(0.0, prompt));
                String response = openAiService.prompt(currentAssistant.getExternalServiceModelId(), currentAssistant.getCreativityLevel(), prompt, history);
                historyRepository.addMessage(currentAssistant.getId(), new AssistantMessage(0.0, response));
                IAAnswerListener iaAnswerListener = ctx.getMessageBus().syncPublisher(BuddeAssistantTopics.IA_ANSWER_RECEIVED);
                iaAnswerListener.onNewAnswer(response);
            } finally {
                finishPromptProcess();
            }
        });

    }

    public boolean couldSendPrompt() {
        return promptCouldBeSent;
    }

    public void createAssistant(String assistantName,
                                String assistantDescription,
                                boolean shouldHaveACustomFace,
                                String externalServiceModelId,
                                String contextForCustomFace) {
        //TODO: In future, will create a fine tune.
        OpenAIService iaService = ctx.getService(OpenAIService.class);
        String base64Face = null;
        if (shouldHaveACustomFace) {
            base64Face = iaService.generateAssistantFace(assistantName, contextForCustomFace);
        }

        Assistant newAssistant = new Assistant(
                generateNewAssistantId(),
                assistantName,
                assistantDescription,
                externalServiceModelId,
                base64Face,
                List.of(),
                0.5F,
                Instant.now());

        ctx.getService(AssistantRepository.class).put(newAssistant);
        AssistantCreatedListener publisher = ctx.getMessageBus().syncPublisher(BuddeAssistantTopics.ASSISTANT_CREATED);
        publisher.onCreated(newAssistant);
        selectAssistant(newAssistant.getId());
    }

    public boolean hasAssistants() {
        AssistantRepository repository = ctx.getService(AssistantRepository.class);
        return !repository.isEmpty();
    }

    public void selectAssistant(String selectedAssistantId) {
        AssistantRepository repository = ctx.getService(AssistantRepository.class);
        Assistant newAssistantToSelect = Objects.requireNonNullElse(repository.getById(selectedAssistantId), PLACEHOLDER_ASSISTANT);
        repository.setCurrent(newAssistantToSelect.getId());

        AssistantSelectedListener publisher = ctx.getMessageBus().syncPublisher(BuddeAssistantTopics.ASSISTANT_SELECTED);
        publisher.onSelect(newAssistantToSelect);
    }

    public List<Assistant> getAll() {
        List<Assistant> all = ctx.getService(AssistantRepository.class).getAll();
        all.sort(Comparator.comparing(Assistant::getId));
        return all;
    }

    public Assistant getCurrentAssistant() {
        Assistant current = ctx.getService(AssistantRepository.class).getCurrent();
        return current == null ? PLACEHOLDER_ASSISTANT : current;
    }

    public void deleteAssistant(String assistantId) {
        List<Assistant> allAssistant = getAll();
        AssistantRepository assistantRepository = ctx.getService(AssistantRepository.class);
        int indexAssistantToBeDeleted = allAssistant.indexOf(assistantRepository.getById(assistantId));

        String newAssistantSelectedId = "";
        if (!(allAssistant.size() == 1)) {
            Assistant nextAssistant = allAssistant.get((indexAssistantToBeDeleted + 1) % allAssistant.size());
            newAssistantSelectedId = nextAssistant.getId();
        }

        Assistant removedAssistant = assistantRepository.remove(assistantId);
        ctx.getService(ChatHistoryRepository.class).cleanDiscussion(removedAssistant.getId());
        AssistantDeletedListener publisher = ctx.getMessageBus().syncPublisher(BuddeAssistantTopics.ASSISTANT_DELETED);
        publisher.onDelete(removedAssistant);

        selectAssistant(newAssistantSelectedId);
    }

    public void updateCurrentAssistantTrackedModule(List<String> moduleNames) {
        Assistant currentAssistant = getCurrentAssistant();
        Assistant newVersion = AssistantFactory.buildWithModulePaths(currentAssistant, moduleNames);
        ctx.getService(AssistantRepository.class).put(newVersion);

    }

    public void updateCurrentAssistantCreativityLevel(float creativityLevelBase100) {
        Assistant currentAssistant = getCurrentAssistant();
        Assistant newVersion = AssistantFactory.buildWithCreativityLevel(currentAssistant, creativityLevelBase100 / 100);
        ctx.getService(AssistantRepository.class).put(newVersion);
    }

    public void teachesCurrentAssistant(Map<JavaFileIdentifier, String> filesContent) {
        ChatHistoryRepository historyRepository = ctx.getService(ChatHistoryRepository.class);
        Assistant currentAssistant = getCurrentAssistant();
        for (JavaFileIdentifier identifier : filesContent.keySet()) {
            String fileContent = filesContent.get(identifier);
            String fileMessageContent = wrapJavaLearnMessage(fileContent);
            FileMessage fileMessage = new FileMessage(0.0,fileMessageContent , identifier);
            historyRepository.addMessage(currentAssistant.getId(),fileMessage);
            FileMessageInsertedInConversationListener publisher = ctx.getMessageBus().syncPublisher(BuddeAssistantTopics.FILE_MESSAGE_IN_CONVERSATION);
            publisher.onFileMessageInserted(fileMessage);
        }
    }

    private String wrapJavaLearnMessage(String fileContent) {
        return String.format("Learn this Java Class:```%s```", fileContent);
    }

    public void cleanDiscussion() {
        ChatHistoryRepository service = ctx.getService(ChatHistoryRepository.class);
        service.cleanDiscussion(getCurrentAssistant().getId());
        selectAssistant(getCurrentAssistant().getId());
    }

    private void setInPromptProcess() {
        promptCouldBeSent = false;
        PromptProcessingListener listener = ctx.getMessageBus().syncPublisher(BuddeAssistantTopics.PROMPT_PROCESSING);
        listener.onStart();
    }

    private void finishPromptProcess() {
        promptCouldBeSent = true;
        PromptProcessingListener listener = ctx.getMessageBus().syncPublisher(BuddeAssistantTopics.PROMPT_PROCESSING);
        listener.onFinish();
    }

    private String generateNewAssistantId() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString().replaceAll("-", "");
    }
}
