package com.maukaim.budde.assistant.intellij.plugin.core.ai.openai.model.responses;

public class OpenAiPhotoEncoded {
    private String b64_json;

    public String getB64_json() {
        return b64_json;
    }

    public void setB64_json(String b64_json) {
        this.b64_json = b64_json;
    }

    @Override
    public String toString() {
        return "OpenAiPhotoEncoded{" +
                "b64_json='" + b64_json + '\'' +
                '}';
    }
}
