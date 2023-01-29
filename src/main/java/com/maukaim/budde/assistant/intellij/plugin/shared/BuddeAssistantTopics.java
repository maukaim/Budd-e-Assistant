package com.maukaim.budde.assistant.intellij.plugin.shared;

import com.intellij.util.messages.Topic;
import com.maukaim.budde.assistant.intellij.plugin.listeners.*;

public class BuddeAssistantTopics {
    public static final Topic<ApiKeyChangedListener> API_KEY_CHANGE = Topic.create("Open AI's API Key Change", ApiKeyChangedListener.class);
    public static final Topic<UserPromptCreatedListener> USER_PROMPT_SENT = Topic.create("User Prompt Created", UserPromptCreatedListener.class);
    public static final Topic<FileMessageInsertedInConversationListener> FILE_MESSAGE_IN_CONVERSATION = Topic.create("Added File in conversation", FileMessageInsertedInConversationListener.class);
    public static final Topic<IAAnswerListener> IA_ANSWER_RECEIVED = Topic.create("IA replied", IAAnswerListener.class);
    public static final Topic<PromptProcessingListener> PROMPT_PROCESSING = Topic.create("Prompt in process.", PromptProcessingListener.class);

    //Assistant
    public static final Topic<AssistantSelectedListener> ASSISTANT_SELECTED = Topic.create("An Assistant is selected", AssistantSelectedListener.class);
    public static final Topic<AssistantCreatedListener> ASSISTANT_CREATED = Topic.create("New Assistant has been created", AssistantCreatedListener.class);
    public static final Topic<AssistantDeletedListener> ASSISTANT_DELETED = Topic.create("Deleted assistant", AssistantDeletedListener.class);
}
