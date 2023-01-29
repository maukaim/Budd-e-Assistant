package com.maukaim.budde.assistant.intellij.plugin.ui.tool_window.panels.assistant.toolbar.actions;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.maukaim.budde.assistant.intellij.plugin.core.assistant.model.Assistant;
import com.maukaim.budde.assistant.intellij.plugin.core.assistant.AssistantService;
import com.maukaim.budde.assistant.intellij.plugin.ui.tool_window.panels.assistant.toolbar.actions.parent.AssistantDependentAction;
import org.jetbrains.annotations.NotNull;

public class DeleteAssistantAction extends AssistantDependentAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project ctx = e.getProject();
        System.out.println("Delete Assistant button clicked!");
        AssistantService service = ctx.getService(AssistantService.class);
        Assistant currentAssistant = service.getCurrentAssistant();
        int result = Messages.showOkCancelDialog(
                "Are you sure you want to delete the current Assistant?\n" +
                        "There is no coming back.",
                "Assistant Deletion",
                "Delete " + currentAssistant.getName() + " for good",
                "Cancel",
                Messages.getWarningIcon()
        );
        if (result == Messages.OK) {
            service.deleteAssistant(currentAssistant.getId());
        }
    }
}
