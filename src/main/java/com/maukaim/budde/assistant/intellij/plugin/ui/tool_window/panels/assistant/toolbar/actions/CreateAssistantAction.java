package com.maukaim.budde.assistant.intellij.plugin.ui.tool_window.panels.assistant.toolbar.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.maukaim.budde.assistant.intellij.plugin.ui.tool_window.panels.assistant.toolbar.actions.popup.AssistantCreationPopupManager;
import org.jetbrains.annotations.NotNull;

public class CreateAssistantAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project ctx = e.getProject();
        AssistantCreationPopupManager.createAndShow(ctx);
    }

}
