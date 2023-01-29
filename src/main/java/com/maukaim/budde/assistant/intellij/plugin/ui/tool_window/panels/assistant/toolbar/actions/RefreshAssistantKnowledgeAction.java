package com.maukaim.budde.assistant.intellij.plugin.ui.tool_window.panels.assistant.toolbar.actions;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.maukaim.budde.assistant.intellij.plugin.ui.tool_window.panels.assistant.toolbar.actions.parent.AssistantDependentAction;
import org.jetbrains.annotations.NotNull;

public class RefreshAssistantKnowledgeAction extends AssistantDependentAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project ctx = e.getProject();
        System.out.println("Here, we will refresh the assistant knowledge base ! Fine tuning etc");
    }
}
