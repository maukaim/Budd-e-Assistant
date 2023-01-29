package com.maukaim.budde.assistant.intellij.plugin.core.chat;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserMessage extends RawMessage {
    private Double cost;

    public UserMessage(@JsonProperty("cost") Double cost,
                       @JsonProperty("message") String message) {
        super(message, MessageType.USER);
        this.cost = cost;
    }

    public Double getCost() {
        return cost;
    }

    @Override
    public String toString() {
        return "UserMessage{" +
                "cost=" + cost +
                ", message='" + message + '\'' +
                ", messageType=" + messageType +
                '}';
    }
}
