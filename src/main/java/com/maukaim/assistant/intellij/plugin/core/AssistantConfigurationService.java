package com.maukaim.assistant.intellij.plugin.core;

import com.intellij.openapi.components.Service;
import com.intellij.openapi.project.Project;
import com.maukaim.assistant.intellij.plugin.listeners.ApiKeyChangedListener;
import com.maukaim.assistant.intellij.plugin.shared.MaukaimAssistantTopics;

@Service
public final class AssistantConfigurationService {
    private final Project project;
    private String apiKey;

    public AssistantConfigurationService(Project project) {
        this.project = project;
    }

    public AssistantConfiguration getAssistantConfiguration() {
        return new AssistantConfiguration(apiKey);
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
        //check if apiKey is valid
        notifyNewApiKey();
    }

    private void notifyNewApiKey() {
        ApiKeyChangedListener publisher = project.getMessageBus().syncPublisher(MaukaimAssistantTopics.API_KEY_CHANGE);
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
