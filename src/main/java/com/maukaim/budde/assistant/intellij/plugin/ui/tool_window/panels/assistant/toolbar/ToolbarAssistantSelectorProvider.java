package com.maukaim.budde.assistant.intellij.plugin.ui.tool_window.panels.assistant.toolbar;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.util.messages.MessageBusConnection;
import com.maukaim.budde.assistant.intellij.plugin.core.assistant.Assistant;
import com.maukaim.budde.assistant.intellij.plugin.core.assistant.AssistantService;
import com.maukaim.budde.assistant.intellij.plugin.shared.BuddeAssistantTopics;
import com.maukaim.budde.assistant.intellij.plugin.ui.tool_window.panels.assistant.toolbar.selector.AssistantComboBoxRenderer;

import javax.swing.*;
import java.awt.event.ItemEvent;
import java.util.List;

import static com.maukaim.budde.assistant.intellij.plugin.core.assistant.AssistantService.PLACEHOLDER_ASSISTANT;

public class ToolbarAssistantSelectorProvider {

    public static JComponent get(Project ctx) {
        JComboBox<Assistant> comboBox = buildComboBox(ctx);

        comboBox.addItemListener((e) -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                Assistant assistant = (Assistant) e.getItem();
                ctx.getService(AssistantService.class).selectAssistant(assistant.getId());
            }
        });
        MessageBusConnection topicSubscriber = ctx.getMessageBus().connect();
        topicSubscriber.subscribe(BuddeAssistantTopics.ASSISTANT_SELECTED,(newAssistant)->{
            comboBox.setSelectedItem(ctx.getService(AssistantService.class).getCurrentAssistant());
        });
        topicSubscriber.subscribe(BuddeAssistantTopics.ASSISTANT_CREATED,(newAssistant)->{
            updateComboBox(comboBox, ctx);
        });
        topicSubscriber.subscribe(BuddeAssistantTopics.ASSISTANT_DELETED,(newAssistant)->{
            updateComboBox(comboBox, ctx);
        });

        return comboBox;
    }

    private static JComboBox<Assistant> buildComboBox(Project ctx) {
        JComboBox<Assistant> comboBox = new ComboBox<>();
        updateComboBox(comboBox, ctx);

        comboBox.setRenderer(new AssistantComboBoxRenderer());
        comboBox.setPrototypeDisplayValue(PLACEHOLDER_ASSISTANT);
        return comboBox;
    }

    private static void updateComboBox(JComboBox<Assistant> comboBox, Project ctx) {
        AssistantService assistantService = ctx.getService(AssistantService.class);
        List<Assistant> availableAssistants = assistantService.getAll();

        comboBox.removeAllItems();
        if (!availableAssistants.isEmpty()) {
            availableAssistants.stream().forEach(comboBox::addItem);
        }
        comboBox.setSelectedItem(assistantService.getCurrentAssistant());
    }
}
