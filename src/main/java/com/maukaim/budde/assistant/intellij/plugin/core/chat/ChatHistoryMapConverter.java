package com.maukaim.budde.assistant.intellij.plugin.core.chat;

import com.fasterxml.jackson.core.type.TypeReference;
import com.intellij.util.xmlb.Converter;
import com.maukaim.budde.assistant.intellij.plugin.core.chat.model.RawMessage;
import com.maukaim.budde.assistant.intellij.plugin.core.marshall.JacksonMarshaller;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public class ChatHistoryMapConverter extends Converter<Map<String, List<RawMessage>>> {
    private JacksonMarshaller jacksonMarshaller;

    public ChatHistoryMapConverter() {
        jacksonMarshaller = new JacksonMarshaller();
    }

    @Override
    public @Nullable Map<String, List<RawMessage>> fromString(@NotNull String value) {
        TypeReference<Map<String, List<RawMessage>>> typeReference = new TypeReference<>() {
        };
        return jacksonMarshaller.unMarshallMap(value, typeReference);
    }

    @Override
    public @Nullable String toString(@NotNull Map<String, List<RawMessage>> value) {
        return jacksonMarshaller.marshall(value);
    }
}
