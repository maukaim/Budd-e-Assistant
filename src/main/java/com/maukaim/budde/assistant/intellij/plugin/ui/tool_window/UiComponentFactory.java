package com.maukaim.budde.assistant.intellij.plugin.ui.tool_window;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.SimpleToolWindowPanel;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import com.maukaim.budde.assistant.intellij.plugin.ui.tool_window.panels.assistant.AssistantsPanel;
import com.maukaim.budde.assistant.intellij.plugin.ui.tool_window.panels.MissingApiKeyPanel;
import org.jetbrains.annotations.NotNull;

public class UiComponentFactory implements ToolWindowFactory {

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        SimpleToolWindowPanel panel = new BuddeAssistantToolWindow(new MissingApiKeyPanel(project),
                new AssistantsPanel(project),
                project);

        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        Content content = contentFactory.createContent(panel, "", false);
        toolWindow.getContentManager().addContent(content);
    }
}
