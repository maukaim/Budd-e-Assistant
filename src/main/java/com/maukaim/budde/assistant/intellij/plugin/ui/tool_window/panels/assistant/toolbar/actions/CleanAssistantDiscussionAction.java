package com.maukaim.budde.assistant.intellij.plugin.ui.tool_window.panels.assistant.toolbar.actions;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.maukaim.budde.assistant.intellij.plugin.core.assistant.AssistantService;
import com.maukaim.budde.assistant.intellij.plugin.core.assistant.model.Assistant;
import com.maukaim.budde.assistant.intellij.plugin.core.chat.ChatHistoryRepository;
import com.maukaim.budde.assistant.intellij.plugin.ui.tool_window.panels.assistant.toolbar.actions.parent.AssistantDependentAction;
import org.jetbrains.annotations.NotNull;

public class CleanAssistantDiscussionAction extends AssistantDependentAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project ctx = e.getProject();
        AssistantService assistantService = ctx.getService(AssistantService.class);
        Assistant currentAssistant = assistantService.getCurrentAssistant();
        int result = Messages.showOkCancelDialog(
                "Are you sure you want to erase the existing discussion?\n" +
                        "There is no coming back.",
                "Delete Discussion with " + currentAssistant.getName(),
                "I want to forget.",
                "Cancel",
                Messages.getWarningIcon()
        );
        if (result == Messages.OK) {
            assistantService.cleanDiscussion();
        }
    }

    @Override
    public void setDefaultIcon(boolean isDefaultIconSet) {
        super.setDefaultIcon(isDefaultIconSet);
    }
}
