package com.maukaim.budde.assistant.intellij.plugin.core.chat.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maukaim.budde.assistant.intellij.plugin.core.chat.model.MessageType;
import com.maukaim.budde.assistant.intellij.plugin.core.chat.model.RawMessage;

public class AssistantMessage extends RawMessage {
    private Double cost;

    public AssistantMessage(@JsonProperty("cost") Double cost,
                            @JsonProperty("message") String message) {
        super(message, MessageType.ASSISTANT);
        this.cost = cost;
    }

    public Double getCost() {
        return cost;
    }

    @Override
    public String toString() {
        return "AssistantMessage{" +
                "cost=" + cost +
                ", message='" + message + '\'' +
                ", messageType=" + messageType +
                '}';
    }
}
