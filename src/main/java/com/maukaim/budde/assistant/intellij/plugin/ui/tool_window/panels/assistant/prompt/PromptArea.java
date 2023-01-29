package com.maukaim.budde.assistant.intellij.plugin.ui.tool_window.panels.assistant.prompt;

import com.intellij.openapi.project.Project;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.components.JBTextArea;
import com.intellij.util.ui.JBUI;
import com.intellij.util.ui.UIUtil;
import com.maukaim.budde.assistant.intellij.plugin.core.assistant.AssistantService;
import com.maukaim.budde.assistant.intellij.plugin.listeners.UserPromptCreatedListener;
import com.maukaim.budde.assistant.intellij.plugin.shared.BuddeAssistantTopics;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class PromptArea extends JPanel {
    private final Project ctx;

    public PromptArea(Project project) {
        this.ctx = project;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(JBUI.Borders.empty(12,24));
        setBackground(UIUtil.getPanelBackground().brighter());

        add(wrapAsScrollable(getScrollableTextArea()));
    }

    private Component wrapAsScrollable(JBTextArea scrollableTextArea) {
        JBScrollPane scrollPane = new JBScrollPane();
        scrollPane.setViewportView(scrollableTextArea);
        scrollPane.setMinimumSize(new Dimension(0, 28));
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        return scrollPane;
    }

    private JBTextArea getScrollableTextArea() {
        JBTextArea textArea = new JBTextArea();
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setAutoscrolls(true);
        textArea.setOpaque(false);

        InputMap input = textArea.getInputMap();
        KeyStroke enter = KeyStroke.getKeyStroke("ENTER");
        KeyStroke shiftEnter = KeyStroke.getKeyStroke("shift ENTER");
        input.put(shiftEnter, input.get(enter));
        input.put(enter,"prompt-submit");

        ActionMap actions = textArea.getActionMap();
        actions.put("prompt-submit", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AssistantService assistantService = ctx.getService(AssistantService.class);
                if(textArea.getText().length() > 3 && assistantService.couldSendPrompt()){
                    String prompt = textArea.getText().strip();
                    UserPromptCreatedListener publisher = ctx.getMessageBus().syncPublisher(BuddeAssistantTopics.USER_PROMPT_SENT);
                    publisher.onNewUserPrompt(prompt);
                    assistantService.sendPrompt(prompt);

                    textArea.setText("");
                }
            }
        });

        return textArea;
    }
}
