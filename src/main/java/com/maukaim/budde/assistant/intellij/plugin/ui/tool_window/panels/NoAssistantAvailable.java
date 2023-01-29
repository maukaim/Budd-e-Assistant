package com.maukaim.budde.assistant.intellij.plugin.ui.tool_window.panels;

import com.intellij.openapi.project.Project;

import javax.swing.*;
import java.awt.*;


public final class NoAssistantAvailable extends JPanel {
    private JTextField apiKeyField;
    private JLabel hint;
    private final Project ctx;

    public NoAssistantAvailable(Project project) {
        super(new FlowLayout());
        this.ctx = project;
        setUp();
    }

    private void setUp() {
        this.add(new JLabel("No assistant available. Create a new one !", FlowLayout.CENTER));
    }
}
