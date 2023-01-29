package com.maukaim.budde.assistant.intellij.plugin.core.ai.openai.model.requests;

public class CompletionRequest {

    private final String model;
    private final String prompt;
    private final Integer max_tokens;
    private final double temperature;
    private final String user = "Budde Assistant IntelliJ Plugin";

    public CompletionRequest(String model, String prompt, Integer max_tokens, double temperature) {
        this.model = model;
        this.prompt = prompt;
        this.max_tokens = max_tokens;
        this.temperature = temperature;
    }

    public String getModel() {
        return model;
    }

    public String getPrompt() {
        return prompt;
    }

    public Integer getMax_tokens() {
        return max_tokens;
    }

    public double getTemperature() {
        return temperature;
    }

    public String getUser() {
        return user;
    }

    @Override
    public String toString() {
        return "PromptRequestBody{" +
                "model='" + model + '\'' +
                ", prompt='" + prompt + '\'' +
                ", max_tokens=" + max_tokens +
                ", temperature=" + temperature +
                ", user='" + user + '\'' +
                '}';
    }
}
