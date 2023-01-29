package com.maukaim.budde.assistant.intellij.plugin.core.ai.openai.model.responses;

import org.jetbrains.annotations.ApiStatus;

import java.util.List;
@ApiStatus.Experimental
public class FineTunedModelsResponse {
    List<FineTunedModelDetails> data;

    public List<FineTunedModelDetails> getData() {
        return data;
    }

    public void setData(List<FineTunedModelDetails> data) {
        this.data = data;
    }
}
