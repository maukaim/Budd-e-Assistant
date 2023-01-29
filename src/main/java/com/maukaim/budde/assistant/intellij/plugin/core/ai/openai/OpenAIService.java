package com.maukaim.budde.assistant.intellij.plugin.core.ai.openai;

import com.intellij.openapi.components.Service;
import com.intellij.openapi.project.Project;
import com.maukaim.budde.assistant.intellij.plugin.core.ai.ExternalAIService;
import com.maukaim.budde.assistant.intellij.plugin.core.ai.openai.model.requests.CompletionRequest;
import com.maukaim.budde.assistant.intellij.plugin.core.ai.openai.model.requests.FaceRequest;
import com.maukaim.budde.assistant.intellij.plugin.core.ai.openai.model.responses.*;
import com.maukaim.budde.assistant.intellij.plugin.core.assistant.ConfigurationService;
import com.maukaim.budde.assistant.intellij.plugin.core.chat.model.MessageType;
import com.maukaim.budde.assistant.intellij.plugin.core.chat.model.RawMessage;
import com.maukaim.budde.assistant.intellij.plugin.core.marshall.JacksonMarshaller;
import org.jetbrains.annotations.ApiStatus;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public final class OpenAIService implements ExternalAIService {

    private static final String OPENAI_ROOT_ADDRESS = "https://api.openai.com";
    private static final String OPENAI_ALL_BASIC_MODELS_SERVICE_PATH = "/v1/models";
    private static final String OPEN_AI_COMPLETIONS_SERVICE_PATH = "/v1/completions";
    private static final String OPEN_AI_FACE_GENERATOR_SERVICE_PATH = "/v1/images/generations";
    private static final Integer DEFAULT_RESPONSE_MAX_TOKENS = 767;
    private static final Integer COOL_DOWN_BASE_MODELS_QUERY_SECONDS = 120;
    @ApiStatus.Experimental
    private static final String OPEN_AI_ALL_EXISTING_FINE_TUNED_MODELS_SERVICE_PATH = "/v1/fine-tunes";

    private static final String ROUGH_TOKENIZER_REGEX_INCLUDE = "( ?\\!)|( ?\\?)|( ?\\.*)|( ?\\w{1,6})|( \\n)";
    private static final String ROUGH_TOKENIZER_REGEX_EXCLUDE = String.format("([^(%s)])*", ROUGH_TOKENIZER_REGEX_INCLUDE);
    private static final String ROUGH_TOKENIZER_REGEX = String.format("%s|%s", ROUGH_TOKENIZER_REGEX_INCLUDE, ROUGH_TOKENIZER_REGEX_EXCLUDE);
    private static final Pattern ROUGH_TOKENIZER = Pattern.compile(ROUGH_TOKENIZER_REGEX);
    private static final String DEFAULT_MODEL_ID = "text-davinci-003";

    private final Project ctx;

    private List<String> cachedBaseModels = List.of();
    private Instant lastBaseModelsCall = Instant.EPOCH;

    public OpenAIService(Project project) {
        this.ctx = project;
    }

    @Override
    public List<String> getAllBaseModel() {
        return Stream.concat(
                        getBaseModels().stream().filter(id -> id.contains("text")),
                        getExistingAssistantModelIds().stream())
                .collect(Collectors.toList());
    }

    @Override
    @ApiStatus.Experimental
    public List<String> getExistingAssistantModelIds() {
        return List.of();
//        HttpRequest request = buildGetRequest(OPEN_AI_ALL_EXISTING_FINE_TUNED_MODELS_SERVICE_PATH);
//        FineTunedModelsResponse response = sendRequest(request, FineTunedModelsResponse.class);
//        return response.getData() == null ?
//                List.of()
//                : response.getData().stream()
//                .map(FineTunedModelDetails::getId)
//                .collect(Collectors.toList());
    }

    @Override
    public String prompt(String modelId, double creativityLevel, String question, List<RawMessage> history) {
        String promptToSend = buildPrompt(modelId, question, history);
        String modelIdToUse = Objects.requireNonNullElse(modelId, DEFAULT_MODEL_ID);
        System.out.println("Will use model: " + modelIdToUse);
        HttpRequest request = buildPostRequest(OPEN_AI_COMPLETIONS_SERVICE_PATH,
                new CompletionRequest(modelIdToUse, promptToSend, DEFAULT_RESPONSE_MAX_TOKENS, creativityLevel));
        CompletionResponse completionResponse = sendRequest(request, CompletionResponse.class);

        return completionResponse.getChoices().stream()
                .filter(choice -> choice.getIndex() == 0)
                .findFirst()
                .orElseGet(() -> new CompletionChoice() {{
                    this.setText("API problem, please talk with plugin dev.");
                }}).getText();
    }

    private String buildPrompt(String modelId, String rawQuestion, List<RawMessage> history) {
        Integer maxTokens = resolveMaxTokensToSend(modelId);
        String question = prependQuestion(rawQuestion);

        int totalTokens = computeRoughTokens(question);
        List<String> messagesToAdd = new ArrayList<>() {{
            add(question);
        }};
        for (int i = history.size() - 1; i >= 0; i--) {
            RawMessage rawMessage = history.get(i);

            String transformedMessage;
            if (rawMessage.getMessageType() == MessageType.ASSISTANT) {
                transformedMessage = prependAnswer(rawMessage.getMessage());
            } else {
                transformedMessage = prependQuestion(rawMessage.getMessage());
            }

            int messageSize = computeRoughTokens(transformedMessage);
            if (totalTokens + messageSize <= maxTokens) {
                totalTokens += messageSize;
                messagesToAdd.add(transformedMessage);
            } else {
                break;
            }
        }

        StringBuilder promptBuilder = new StringBuilder();
        for (int i = messagesToAdd.size() - 1; i >= 0; i--) {
            promptBuilder.append(messagesToAdd.get(i));
        }

        return promptBuilder.append(prependAnswer("")).toString();
    }

    private String prependQuestion(String message) {
        return ("\nQ: " + message).stripTrailing();
    }

    private String prependAnswer(String message) {
        return ("\nA: " + message).stripTrailing();
    }

    private int computeRoughTokens(String question) {
        return ROUGH_TOKENIZER.matcher(question).groupCount();
    }

    private Integer resolveMaxTokensToSend(String modelId) {
        int buffer = 256;
        int base;
        if (modelId.endsWith("3")) {
            base = 4096;
        } else {
            base = 2048;
        }
        return Math.max(0, base - buffer - DEFAULT_RESPONSE_MAX_TOKENS);
    }

    @Override
    public String generateAssistantFace(String assistantName, String... customizationItems) {
        String prompt = String.format("A portrait photo of a smiling developer named %s. %s", assistantName,
                String.join(", ", customizationItems));
        HttpRequest request = buildPostRequest(OPEN_AI_FACE_GENERATOR_SERVICE_PATH,
                new FaceRequest(prompt));
        try {
            FaceImageResponse faceImageResponse = sendRequest(request, FaceImageResponse.class);
            return faceImageResponse.getData().get(0).getB64_json();
        } catch (Exception e) {
            System.out.println("Fall back to defaulted face");
            return ExternalAIService.super.generateAssistantFace(assistantName, customizationItems);
        }
    }

    @ApiStatus.Experimental
    @Override
    public String createFineTunedModel(String baseId, String... trainingFiles) {
        return null;
    }

    private <T> T sendRequest(HttpRequest request, Class<T> clazz) {
        HttpClient httpClient = HttpClient.newHttpClient();
        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                System.out.println("Received ! " + response.body());
                return ctx.getService(JacksonMarshaller.class).unMarshall(response.body(), clazz);
            }
            System.out.println("Ah... not good status code: " + response.body());
            throw new RuntimeException("Bad status code");
        } catch (IOException e) {
            System.out.println("IO issue: " + e.getMessage());
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            System.out.println("Got interrupted! " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private List<String> getBaseModels() {
        Instant now = Instant.now();
        if (lastBaseModelsCall.until(now, ChronoUnit.SECONDS) > COOL_DOWN_BASE_MODELS_QUERY_SECONDS) {
            HttpRequest request = buildGetRequest(OPENAI_ALL_BASIC_MODELS_SERVICE_PATH);
            BaseModelsResponse response = sendRequest(request, BaseModelsResponse.class);
            lastBaseModelsCall = now;
            cachedBaseModels = response.getData() == null ?
                    List.of()
                    : response.getData().stream()
                    .map(BaseModelDetails::getId)
                    .sorted()
                    .collect(Collectors.toList());
        }

        return cachedBaseModels;
    }

    private HttpRequest buildGetRequest(String servicePath) {
        return newRequestBuilder()
                .uri(URI.create(OPENAI_ROOT_ADDRESS + servicePath))
                .GET()
                .build();
    }

    private HttpRequest buildPostRequest(String uri, Object bodyObject) {
        return newRequestBuilder()
                .uri(URI.create(OPENAI_ROOT_ADDRESS + uri))
                .POST(HttpRequest.BodyPublishers.ofString(ctx.getService(JacksonMarshaller.class).marshall(bodyObject))) //TODO: Declare as Marshaller service. Better for underlying impl switch
                .build();
    }

    private HttpRequest.Builder newRequestBuilder() {
        ConfigurationService service = ctx.getService(ConfigurationService.class);
        ConfigurationService.AssistantConfiguration assistantConfiguration = service.getAssistantConfiguration();
        return HttpRequest.newBuilder()
                .setHeader("Content-Type", "application/json")
                .setHeader("Authorization", "Bearer " + assistantConfiguration.getApiKey())
                .version(HttpClient.Version.HTTP_2);
    }
}
