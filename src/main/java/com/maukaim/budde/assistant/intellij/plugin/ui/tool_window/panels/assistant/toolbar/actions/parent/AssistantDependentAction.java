package com.maukaim.budde.assistant.intellij.plugin.ui.tool_window.panels.assistant.toolbar.actions.parent;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.maukaim.budde.assistant.intellij.plugin.core.assistant.AssistantService;
import org.jetbrains.annotations.NotNull;

public abstract class AssistantDependentAction extends AnAction {
    @Override
    public void update(@NotNull AnActionEvent e) {
        AssistantService service = e.getProject().getService(AssistantService.class);
        if(service.hasAssistants()){
            e.getPresentation().setEnabledAndVisible(true);
        }else{
            e.getPresentation().setEnabledAndVisible(false);
        }
    }
}
