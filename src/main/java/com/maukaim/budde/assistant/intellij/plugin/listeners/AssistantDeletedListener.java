package com.maukaim.budde.assistant.intellij.plugin.listeners;

import com.maukaim.budde.assistant.intellij.plugin.core.assistant.Assistant;

public interface AssistantDeletedListener {
    void onDelete(Assistant oldAssistantRecord);
}
