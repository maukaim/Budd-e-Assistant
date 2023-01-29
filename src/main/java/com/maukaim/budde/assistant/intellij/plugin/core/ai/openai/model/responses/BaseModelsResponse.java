package com.maukaim.budde.assistant.intellij.plugin.core.ai.openai.model.responses;

import java.util.List;

public class BaseModelsResponse {
    List<BaseModelDetails> data;

    public List<BaseModelDetails> getData() {
        return data;
    }

    public void setData(List<BaseModelDetails> data) {
        this.data = data;
    }
}
