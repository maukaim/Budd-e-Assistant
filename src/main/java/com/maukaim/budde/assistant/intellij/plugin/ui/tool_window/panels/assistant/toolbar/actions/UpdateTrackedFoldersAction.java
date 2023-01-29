package com.maukaim.budde.assistant.intellij.plugin.ui.tool_window.panels.assistant.toolbar.actions;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.maukaim.budde.assistant.intellij.plugin.ui.tool_window.panels.assistant.toolbar.actions.parent.AssistantDependentAction;
import com.maukaim.budde.assistant.intellij.plugin.ui.tool_window.panels.assistant.toolbar.actions.popup.ModuleTrackedPopupManager;
import org.jetbrains.annotations.NotNull;

public class UpdateTrackedFoldersAction extends AssistantDependentAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project ctx = e.getProject();
        ModuleTrackedPopupManager.createAndShow(ctx);
    }
}
