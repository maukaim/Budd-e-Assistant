package com.maukaim.budde.assistant.intellij.plugin.ui.tool_window.panels.assistant;

import com.intellij.openapi.project.Project;
import com.intellij.ui.JBSplitter;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.util.messages.MessageBusConnection;
import com.maukaim.budde.assistant.intellij.plugin.core.assistant.model.Assistant;
import com.maukaim.budde.assistant.intellij.plugin.core.assistant.AssistantService;
import com.maukaim.budde.assistant.intellij.plugin.core.chat.ChatHistoryRepository;
import com.maukaim.budde.assistant.intellij.plugin.core.chat.model.FileMessage;
import com.maukaim.budde.assistant.intellij.plugin.core.chat.model.RawMessage;
import com.maukaim.budde.assistant.intellij.plugin.listeners.PromptProcessingListener;
import com.maukaim.budde.assistant.intellij.plugin.shared.BuddeAssistantTopics;
import com.maukaim.budde.assistant.intellij.plugin.ui.tool_window.panels.assistant.conversation.AssistantConversation;
import com.maukaim.budde.assistant.intellij.plugin.ui.tool_window.panels.assistant.conversation.WaitResponseComponent;
import com.maukaim.budde.assistant.intellij.plugin.ui.tool_window.panels.assistant.conversation.messages.AssistantMessagePanel;
import com.maukaim.budde.assistant.intellij.plugin.ui.tool_window.panels.assistant.conversation.messages.FileMessagePanel;
import com.maukaim.budde.assistant.intellij.plugin.ui.tool_window.panels.assistant.conversation.messages.MessagePanel;
import com.maukaim.budde.assistant.intellij.plugin.ui.tool_window.panels.assistant.conversation.messages.UserMessagePanel;
import com.maukaim.budde.assistant.intellij.plugin.ui.tool_window.panels.assistant.prompt.PromptArea;
import com.maukaim.budde.assistant.intellij.plugin.ui.tool_window.panels.assistant.toolbar.ToolbarProvider;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class AssistantsPanel extends JPanel {
    private final Project ctx;
    private WaitResponseComponent waitResponsePanel;

    public AssistantsPanel(Project project) {
        this.ctx = project;
        this.setLayout(new BorderLayout());

        JComponent toolBar = ToolbarProvider.get(ctx, this);
        this.add(toolBar, BorderLayout.NORTH);
        this.add(getMainPanel(), BorderLayout.CENTER);
    }

    private Component getMainPanel() {
        JBScrollPane scrollPane = new JBScrollPane();
        ChatHistoryRepository chatHistoryRepository = ctx.getService(ChatHistoryRepository.class);
        AssistantService assistantService = ctx.getService(AssistantService.class);
        Assistant currentAssistant = assistantService.getCurrentAssistant();
        List<RawMessage> previousDiscussion = chatHistoryRepository.getPreviousDiscussion(currentAssistant.getId());

        AssistantConversation conversationPanel = new AssistantConversation(ctx, buildMessagePanels(previousDiscussion, currentAssistant));
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.add(conversationPanel, BorderLayout.NORTH);
        scrollPane.setViewportView(wrapper);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        JBSplitter splitter = new JBSplitter(true, 1);
        splitter.setFirstComponent(scrollPane);
        splitter.setSecondComponent(buildPromptArea());

        watchAssistantChangeAndUpdateConversation(conversationPanel);

        ctx.getMessageBus().connect().subscribe(BuddeAssistantTopics.USER_PROMPT_SENT,
                (prompt) -> {
                    conversationPanel.addUserMessage(prompt);
                    autoScrollDown(scrollPane);
                }
        );

        ctx.getMessageBus().connect().subscribe(BuddeAssistantTopics.IA_ANSWER_RECEIVED,
                (answer) -> {
                    conversationPanel.addAssistantMessage(answer);
                    autoScrollDown(scrollPane);
                }
        );

        ctx.getMessageBus().connect().subscribe(BuddeAssistantTopics.FILE_MESSAGE_IN_CONVERSATION,
                (fileMessage) -> {
                    conversationPanel.addFileMessage(fileMessage.getJavaFileIdentifier());
                    autoScrollDown(scrollPane);
                }
        );

        ctx.getMessageBus().connect().subscribe(BuddeAssistantTopics.PROMPT_PROCESSING, new PromptProcessingListener() {
            @Override
            public void onStart() {
                waitResponsePanel = new WaitResponseComponent();
                conversationPanel.add(waitResponsePanel);
                autoScrollDown(scrollPane);
            }

            @Override
            public void onFinish() {
                if (waitResponsePanel != null) {
                    waitResponsePanel.cancel();
                    conversationPanel.remove(waitResponsePanel);
                    waitResponsePanel = null;
                }
            }
        });

        return splitter;
    }

    private void watchAssistantChangeAndUpdateConversation(AssistantConversation conversationPanel) {
        MessageBusConnection topicSubscriber = ctx.getMessageBus().connect();
        topicSubscriber.subscribe(BuddeAssistantTopics.ASSISTANT_SELECTED, (newAssistant) -> {
            AssistantService assistantService = ctx.getService(AssistantService.class);
            ChatHistoryRepository chatHistoryRepository = ctx.getService(ChatHistoryRepository.class);
            Assistant currentAssistant = assistantService.getCurrentAssistant();

            List<RawMessage> historic = chatHistoryRepository.getPreviousDiscussion(currentAssistant.getId());
            conversationPanel.replaceAllMessage(buildMessagePanels(historic, currentAssistant));
        });
    }

    private List<MessagePanel<?>> buildMessagePanels(List<RawMessage> previousDiscussion, Assistant currentAssistant) {
        return previousDiscussion.stream()
                .map(message -> {
                    switch (message.getMessageType()) {
                        case ASSISTANT:
                            return new AssistantMessagePanel(message.getMessage(), currentAssistant, ctx);
                        case USER:
                            return new UserMessagePanel(message.getMessage());
                        case FILE:
                            return new FileMessagePanel(((FileMessage)message).getJavaFileIdentifier());
                        default:
                            return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private void autoScrollDown(JBScrollPane scrollPane) {
        scrollPane.validate();
        scrollPane.repaint();
        JScrollBar vertical = scrollPane.getVerticalScrollBar();
        vertical.setValue(vertical.getMaximum());
    }

    private JComponent buildPromptArea() {
        return new PromptArea(ctx);
    }
}