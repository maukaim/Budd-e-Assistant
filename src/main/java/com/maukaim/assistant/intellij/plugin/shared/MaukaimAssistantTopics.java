package com.maukaim.assistant.intellij.plugin.shared;

import com.intellij.util.messages.Topic;
import com.maukaim.assistant.intellij.plugin.listeners.ApiKeyChangedListener;

public class MaukaimAssistantTopics {
    public static final Topic<ApiKeyChangedListener> API_KEY_CHANGE = Topic.create("Open AI's API Key Change", ApiKeyChangedListener.class);
}
