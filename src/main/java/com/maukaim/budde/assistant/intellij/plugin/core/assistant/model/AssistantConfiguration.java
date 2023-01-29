package com.maukaim.budde.assistant.intellij.plugin.core.assistant.model;

public class AssistantConfiguration {
    private final String apiKey;

    public AssistantConfiguration(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getApiKey() {
        return apiKey;
    }

}
