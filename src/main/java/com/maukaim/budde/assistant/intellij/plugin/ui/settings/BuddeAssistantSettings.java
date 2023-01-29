package com.maukaim.budde.assistant.intellij.plugin.ui.settings;

import com.intellij.openapi.components.Service;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.project.Project;
import com.intellij.util.ui.FormBuilder;
import com.maukaim.budde.assistant.intellij.plugin.shared.aware.AssistantConfigurationAware;
import com.maukaim.budde.assistant.intellij.plugin.core.assistant.ConfigurationService;
import com.maukaim.budde.assistant.intellij.plugin.core.assistant.model.AssistantConfiguration;

import javax.swing.*;
import java.awt.*;

@Service
public final class BuddeAssistantSettings implements Configurable, AssistantConfigurationAware {
    private final Project ctx;
    private JTextField apiKeyField;
    public BuddeAssistantSettings(Project project) {
        this.ctx = project;
    }

    @Override
    public String getDisplayName() {
        return "Budd-e Settings";
    }

    @Override
    public JComponent createComponent() {
        JPanel mainSettingsPanel = new JPanel(new BorderLayout());

        AssistantConfiguration assistantConfiguration = getAssistantConfiguration();
        apiKeyField = new JTextField();
        apiKeyField.setText(assistantConfiguration.getApiKey());
        JPanel content = FormBuilder.createFormBuilder()
                .addComponent(new JLabel("OpenAI Settings"))
                .addSeparator()
                .addLabeledComponent("API Key", apiKeyField)
                .addTooltip("This value will be stored as Secret Credential in IntelliJ. Please never share to anyone.")
                .addVerticalGap(8)
                .getPanel();


        mainSettingsPanel.add(content, BorderLayout.NORTH);

        return mainSettingsPanel;
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