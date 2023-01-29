package com.maukaim.budde.assistant.intellij.plugin.ui.tool_window;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.SimpleToolWindowPanel;
import com.maukaim.budde.assistant.intellij.plugin.shared.aware.AssistantConfigurationAware;
import com.maukaim.budde.assistant.intellij.plugin.shared.BuddeAssistantTopics;
import com.maukaim.budde.assistant.intellij.plugin.ui.tool_window.panels.assistant.AssistantsPanel;
import com.maukaim.budde.assistant.intellij.plugin.ui.tool_window.panels.MissingApiKeyPanel;
import org.apache.commons.lang.StringUtils;


public class BuddeAssistantToolWindow extends SimpleToolWindowPanel implements AssistantConfigurationAware {
    private MissingApiKeyPanel missingApiKeyPanel;
    private AssistantsPanel mainPanel;
    private final Project ctx;

    public BuddeAssistantToolWindow(MissingApiKeyPanel missingApiKeyPanel,
                                    AssistantsPanel mainPanel,
                                    Project project) {
        super(true, true);
        this.ctx = project;
        this.mainPanel = mainPanel;
        this.missingApiKeyPanel = missingApiKeyPanel;

        displayRelevantPanel(getAssistantConfiguration().getApiKey());
        ctx.getMessageBus().connect().subscribe(BuddeAssistantTopics.API_KEY_CHANGE, this::displayRelevantPanel);
    }

    private void displayRelevantPanel(String apiKey) {
        this.removeAll();
        if (StringUtils.isEmpty(apiKey)) {
            this.add(missingApiKeyPanel);
        } else {
            this.add(mainPanel);
        }
        revalidate();
        repaint();
    }

    @Override
    public Project getProject() {
        return ctx;
    }
}