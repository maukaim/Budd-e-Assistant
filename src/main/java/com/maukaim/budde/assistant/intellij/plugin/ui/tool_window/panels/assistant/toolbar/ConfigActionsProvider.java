package com.maukaim.budde.assistant.intellij.plugin.ui.tool_window.panels.assistant.toolbar;

import com.intellij.openapi.actionSystem.*;
import com.intellij.util.ui.JBUI;

import javax.swing.*;

public class ConfigActionsProvider {
    private static final String BUDD_E_TOOLBAR_ACTION_GROUP = "budd-e_toolbar_action-group"; // Matches plugin.xml
    private static final String TOOLBAR_ACTIONS_ID = "TOOLBAR_ACTIONS_ID";

    public static JComponent get(JComponent parent) {
        ActionManager actionManager = ActionManager.getInstance();

        ActionToolbar toolbar = actionManager.createActionToolbar(TOOLBAR_ACTIONS_ID,
                (ActionGroup) actionManager.getAction(BUDD_E_TOOLBAR_ACTION_GROUP),
                true);

        toolbar.setTargetComponent(parent);
        JComponent toolbarComponent = toolbar.getComponent();
        toolbarComponent.setBorder(JBUI.Borders.empty(2, 2));

        return toolbarComponent;
    }
}
