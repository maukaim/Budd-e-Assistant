package com.maukaim.budde.assistant.intellij.plugin.listeners;


import com.maukaim.budde.assistant.intellij.plugin.core.assistant.Assistant;

public interface AssistantCreatedListener {
    void onCreated(Assistant newAssistant);
}
