package com.maukaim.budde.assistant.intellij.plugin.core.chat;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.Service;
import com.intellij.openapi.components.State;
import com.intellij.util.xmlb.XmlSerializerUtil;
import com.intellij.util.xmlb.annotations.OptionTag;
import com.maukaim.budde.assistant.intellij.plugin.core.chat.model.RawMessage;
import com.maukaim.budde.assistant.intellij.plugin.core.chat.model.UserMessage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@State(name="historyRepository2")
public final class ChatHistoryRepository implements PersistentStateComponent<ChatHistoryRepository> {
    @OptionTag(converter = ChatHistoryMapConverter.class)
    public Map<String, List<RawMessage>> chatHistory = new HashMap<>();

    @Override
    public @Nullable ChatHistoryRepository getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull ChatHistoryRepository state) {
        XmlSerializerUtil.copyBean(state, this);
    }

    public List<RawMessage> getPreviousDiscussion(String assistantId){
        return List.copyOf(chatHistory.getOrDefault(assistantId, List.of()));
    }

    public void addMessage(String assistantId, RawMessage rawMessage){
        List<RawMessage> existingChat = this.chatHistory.get(assistantId);
        if(existingChat != null){
            existingChat.add(rawMessage);
        }else{
            List<RawMessage> chat = new ArrayList<>(){{add(rawMessage);}};
            this.chatHistory.put(assistantId, chat);
        }
    }

    public void cleanDiscussion(String assistantId){
        chatHistory.remove(assistantId);
    }
}
