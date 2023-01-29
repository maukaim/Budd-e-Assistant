package com.maukaim.budde.assistant.intellij.plugin.core.chat;

import com.fasterxml.jackson.annotation.JsonProperty;

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
