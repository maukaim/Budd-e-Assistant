package com.maukaim.budde.assistant.intellij.plugin.ui.tool_window.panels;

import com.intellij.openapi.project.Project;
import com.maukaim.budde.assistant.intellij.plugin.core.assistant.ConfigurationService;

import javax.swing.*;

import java.awt.*;

public final class MissingApiKeyPanel extends JPanel {
    private JTextField apiKeyField;
    private JLabel hint;
    private final Project ctx;

    public MissingApiKeyPanel(Project project) {
        super(new FlowLayout());
        this.ctx = project;
        setUp();
    }

    private void setUp() {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setAlignmentY(Component.CENTER_ALIGNMENT);

        this.hint= new JLabel("No API KEY Detected, please provide your OpenAI API key below:");
        this.add(hint);

        this.apiKeyField = new JTextField();
        this.apiKeyField.setMaximumSize(new Dimension(400,this.apiKeyField.getFont().getSize() + 24 ));
        this.add(this.apiKeyField);
        this.apiKeyField.addActionListener(action  -> getConfig().setApiKey(apiKeyField.getText().strip()));

        JButton validateButton = new JButton("Validate");
        validateButton.addActionListener(action -> getConfig().setApiKey(apiKeyField.getText().strip()));
        this.add(validateButton);

    }

    private ConfigurationService getConfig(){
        return this.ctx.getService(ConfigurationService.class);
    }
}
