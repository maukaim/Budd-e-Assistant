package com.maukaim.budde.assistant.intellij.plugin.ui.tool_window.panels.assistant.toolbar;

import com.intellij.openapi.project.Project;
import com.intellij.ui.JBColor;
import com.intellij.util.ui.JBUI;

import javax.swing.*;
import java.awt.*;

public class ToolbarProvider {

    public static JComponent get(Project ctx, JComponent parent) {
        JPanel toolbarWrapper = new JPanel(new BorderLayout());

        toolbarWrapper.add(ConfigActionsProvider.get(parent), BorderLayout.WEST);
        toolbarWrapper.add(ToolbarAssistantSelectorProvider.get(ctx), BorderLayout.EAST);
        toolbarWrapper.setBorder(JBUI.Borders.customLineBottom(JBColor.border()));
        return toolbarWrapper;
    }
}
