package com.maukaim.budde.assistant.intellij.plugin.ui.tool_window.panels.assistant.conversation;

import com.intellij.openapi.project.Project;
import com.maukaim.budde.assistant.intellij.plugin.core.assistant.AssistantService;
import com.maukaim.budde.assistant.intellij.plugin.core.chat.model.FileMessage;
import com.maukaim.budde.assistant.intellij.plugin.core.chat.model.JavaFileIdentifier;
import com.maukaim.budde.assistant.intellij.plugin.ui.tool_window.panels.assistant.conversation.messages.AssistantMessagePanel;
import com.maukaim.budde.assistant.intellij.plugin.ui.tool_window.panels.assistant.conversation.messages.FileMessagePanel;
import com.maukaim.budde.assistant.intellij.plugin.ui.tool_window.panels.assistant.conversation.messages.MessagePanel;
import com.maukaim.budde.assistant.intellij.plugin.ui.tool_window.panels.assistant.conversation.messages.UserMessagePanel;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class AssistantConversation extends JPanel {
    private final List<MessagePanel> messagePanels;
    private final Project ctx;

    public AssistantConversation(Project project, List<MessagePanel<?>> initialMessages) {
        this.ctx = project;
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.messagePanels = new ArrayList<>() {{
            addAll(initialMessages);
        }};
        messagePanels.forEach(this::add);
    }

    public void replaceAllMessage(List<MessagePanel<?>> newMessages){
        this.messagePanels.clear();
        this.messagePanels.addAll(newMessages);
        this.removeAll();
        newMessages.forEach(this::addMessage);
        revalidate();
        repaint();
    }

    public void addUserMessage(String message){
        addMessage(new UserMessagePanel(message));
    }

    public void addFileMessage(JavaFileIdentifier javaFileIdentifier) {
        addMessage(new FileMessagePanel(javaFileIdentifier));
    }

    public void addAssistantMessage(String message){
        AssistantService service = ctx.getService(AssistantService.class);
        addMessage(new AssistantMessagePanel(message,service.getCurrentAssistant(), ctx));
    }

    private void addMessage(MessagePanel<?> messagePanel) {
        this.messagePanels.add(messagePanel);
        this.add(messagePanel);
        revalidate();
        repaint();
    }
}
