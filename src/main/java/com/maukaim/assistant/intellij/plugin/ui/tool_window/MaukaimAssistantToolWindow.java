package com.maukaim.assistant.intellij.plugin.ui.tool_window;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.SimpleToolWindowPanel;
import com.maukaim.assistant.intellij.plugin.shared.aware.AssistantConfigurationAware;
import com.maukaim.assistant.intellij.plugin.listeners.ApiKeyChangedListener;
import com.maukaim.assistant.intellij.plugin.shared.MaukaimAssistantTopics;
import com.maukaim.assistant.intellij.plugin.ui.tool_window.panels.MaukaimAssistantsPanel;
import com.maukaim.assistant.intellij.plugin.ui.tool_window.panels.MissingApiKeyPanel;
import org.apache.commons.lang.StringUtils;


public class MaukaimAssistantToolWindow extends SimpleToolWindowPanel implements ApiKeyChangedListener, AssistantConfigurationAware {
    private MissingApiKeyPanel missingApiKeyPanel;
    private MaukaimAssistantsPanel mainPanel;
    private final Project ctx;

    public MaukaimAssistantToolWindow(MissingApiKeyPanel missingApiKeyPanel,
                                      MaukaimAssistantsPanel mainPanel,
                                      Project project) {
        super(true, true);
        this.ctx = project;
        this.mainPanel = mainPanel;
        this.missingApiKeyPanel = missingApiKeyPanel;

        displayRelevantPanel(getAssistantConfiguration().getApiKey());
        ctx.getMessageBus().connect().subscribe(MaukaimAssistantTopics.API_KEY_CHANGE, this::displayRelevantPanel);
    }

    private void displayRelevantPanel(String apiKey) {
        this.removeAll();
        if (StringUtils.isEmpty(apiKey)) {
            this.add(missingApiKeyPanel);
        } else {
            this.add(mainPanel);
        }
        repaint();
    }

    @Override
    public void onApiKeyChanged(String newApiKey) {
        displayRelevantPanel(newApiKey);
    }

    @Override
    public Project getProject() {
        return ctx;
    }
}