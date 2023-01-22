package com.maukaim.assistant.intellij.plugin.ui.settings;

import com.intellij.openapi.components.Service;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.project.Project;
import com.maukaim.assistant.intellij.plugin.shared.aware.AssistantConfigurationAware;
import com.maukaim.assistant.intellij.plugin.core.AssistantConfigurationService;

import javax.swing.*;

@Service
public final class BuloAssistantSettings implements Configurable, AssistantConfigurationAware {
    private final Project ctx;
    private JTextField apiKeyField;
    public BuloAssistantSettings(Project project) {
        this.ctx = project;
    }

    @Override
    public String getDisplayName() {
        return "Maukaim Assistant";
    }

    @Override
    public JComponent createComponent() {
        AssistantConfigurationService.AssistantConfiguration assistantConfiguration = getAssistantConfiguration();
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        apiKeyField = new JTextField();
        apiKeyField.setText(assistantConfiguration.getApiKey());
        apiKeyField.addActionListener(e-> apiKeyField.getText());
        panel.add(new JLabel("API Key:"));
        panel.add(apiKeyField);

        return panel;
    }

    @Override
    public boolean isModified() {
        return !apiKeyField.getText().equals(getAssistantConfiguration().getApiKey());
    }

    @Override
    public void apply() {
        ctx.getService(AssistantConfigurationService.class).setApiKey(apiKeyField.getText());
    }

    @Override
    public void reset() {
        apiKeyField.setText(getAssistantConfiguration().getApiKey());
    }

    @Override
    public Project getProject() {
        return ctx;
    }
}