package com.maukaim.budde.assistant.intellij.plugin.core.ai.openai.model.responses;

import java.util.List;

public class FaceImageResponse {
    private List<OpenAiPhotoEncoded> data;

    public List<OpenAiPhotoEncoded> getData() {
        return data;
    }

    public void setData(List<OpenAiPhotoEncoded> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "FaceImageResponse{" +
                "data=" + data +
                '}';
    }
}
