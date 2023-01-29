package com.maukaim.budde.assistant.intellij.plugin.listeners;

import com.maukaim.budde.assistant.intellij.plugin.core.chat.model.FileMessage;

public interface FileMessageInsertedInConversationListener {
    void onFileMessageInserted(FileMessage fileMessage);
}
