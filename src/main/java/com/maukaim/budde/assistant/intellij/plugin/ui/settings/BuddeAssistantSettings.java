package com.maukaim.budde.assistant.intellij.plugin.ui.settings;

import com.intellij.openapi.components.Service;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.project.Project;
import com.maukaim.budde.assistant.intellij.plugin.shared.aware.AssistantConfigurationAware;
import com.maukaim.budde.assistant.intellij.plugin.core.assistant.ConfigurationService;

import javax.swing.*;

@Service
public final class BuddeAssistantSettings implements Configurable, AssistantConfigurationAware {
    private final Project ctx;
    private JTextField apiKeyField;
    public BuddeAssistantSettings(Project project) {
        this.ctx = project;
    }

    @Override
    public String getDisplayName() {
        return "Budde Assistant";
    }

    @Override
    public JComponent createComponent() {
        ConfigurationService.AssistantConfiguration assistantConfiguration = getAssistantConfiguration();
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
        ctx.getService(ConfigurationService.class).setApiKey(apiKeyField.getText());
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