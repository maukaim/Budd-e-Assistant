package com.maukaim.budde.assistant.intellij.plugin.core.assistant;

import com.intellij.openapi.components.Service;
import com.intellij.openapi.project.Project;
import com.maukaim.budde.assistant.intellij.plugin.listeners.ApiKeyChangedListener;
import com.maukaim.budde.assistant.intellij.plugin.shared.BuddeAssistantTopics;

@Service
public final class ConfigurationService {
    private final Project project;
    private String apiKey = "YOUR_API_KEY";

    public ConfigurationService(Project project) {
        this.project = project;
    }

    public AssistantConfiguration getAssistantConfiguration() {
        return new AssistantConfiguration(apiKey);
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
        notifyNewApiKey();
    }

    private void notifyNewApiKey() {
        ApiKeyChangedListener publisher = project.getMessageBus().syncPublisher(BuddeAssistantTopics.API_KEY_CHANGE);
        publisher.onApiKeyChanged(apiKey);
    }

    public class AssistantConfiguration{
        private final String apiKey;

        public AssistantConfiguration(String apiKey) {
            this.apiKey = apiKey;
        }

        public String getApiKey() {
            return apiKey;
        }
    }
}
