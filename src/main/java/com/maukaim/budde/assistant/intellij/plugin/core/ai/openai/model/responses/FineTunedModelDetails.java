package com.maukaim.budde.assistant.intellij.plugin.core.ai.openai.model.responses;

import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Experimental
public class FineTunedModelDetails {
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
