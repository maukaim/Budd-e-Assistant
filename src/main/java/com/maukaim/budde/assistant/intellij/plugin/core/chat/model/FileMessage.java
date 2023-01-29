package com.maukaim.budde.assistant.intellij.plugin.core.chat.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FileMessage extends RawMessage {
    private Double cost;
    private JavaFileIdentifier javaFileIdentifier;

    public FileMessage(@JsonProperty("cost") Double cost,
                       @JsonProperty("message") String message,
                       @JsonProperty("javaFileIdentifier") JavaFileIdentifier javaFileIdentifier) {
        super(message, MessageType.FILE);
        this.cost = cost;
        this.javaFileIdentifier = javaFileIdentifier;
    }

    public Double getCost() {
        return cost;
    }

    public JavaFileIdentifier getJavaFileIdentifier() {
        return javaFileIdentifier;
    }

    @Override
    public String toString() {
        return "FileMessage{" +
                "cost=" + cost +
                ", javaFileIdentifier=" + javaFileIdentifier +
                ", message='" + message + '\'' +
                ", messageType=" + messageType +
                '}';
    }
}
