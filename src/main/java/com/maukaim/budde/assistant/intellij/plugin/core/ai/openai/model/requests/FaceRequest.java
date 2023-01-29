package com.maukaim.budde.assistant.intellij.plugin.core.ai.openai.model.requests;

public class FaceRequest {
    private final String prompt;
    private final String response_format = "b64_json";
    private final String size = "256x256";
    private final Integer n = 1;

    public FaceRequest(String prompt) {
        this.prompt = prompt;
    }

    public String getPrompt() {
        return prompt;
    }

    public String getResponse_format() {
        return response_format;
    }

    public String getSize() {
        return size;
    }

    public Integer getN(){
        return n;
    }

    @Override
    public String toString() {
        return "FaceRequest{" +
                "prompt='" + prompt + '\'' +
                ", response_format='" + response_format + '\'' +
                ", size='" + size + '\'' +
                ", n=" + n +
                '}';
    }
}
