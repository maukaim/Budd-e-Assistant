package com.maukaim.budde.assistant.intellij.plugin.ui.tool_window.panels.assistant.toolbar.actions;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.maukaim.budde.assistant.intellij.plugin.core.assistant.Assistant;
import com.maukaim.budde.assistant.intellij.plugin.core.assistant.AssistantService;
import com.maukaim.budde.assistant.intellij.plugin.ui.tool_window.panels.assistant.toolbar.actions.parent.AssistantDependentAction;
import com.maukaim.budde.assistant.intellij.plugin.ui.tool_window.panels.assistant.toolbar.actions.popup.CreativityManagementPopupManager;
import org.jetbrains.annotations.NotNull;

public class CreativityLevelAction extends AssistantDependentAction {

    @Override
    public void update(@NotNull AnActionEvent e) {
        super.update(e);
        Assistant currentAssistant = e.getProject().getService(AssistantService.class).getCurrentAssistant();
        float creativityLevel = currentAssistant.getCreativityLevel();
        if(creativityLevel == 0){
            e.getPresentation().setIcon(AllIcons.Actions.IntentionBulbGrey);
        }else if(creativityLevel < 0.5){
            e.getPresentation().setIcon(AllIcons.Actions.QuickfixOffBulb);
        }else if(creativityLevel < 1){
            e.getPresentation().setIcon(AllIcons.Actions.IntentionBulb);
        }else{
            e.getPresentation().setIcon(AllIcons.Actions.QuickfixBulb);
        }
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project ctx = e.getProject();
        CreativityManagementPopupManager.createAndShow(ctx);
    }

}
