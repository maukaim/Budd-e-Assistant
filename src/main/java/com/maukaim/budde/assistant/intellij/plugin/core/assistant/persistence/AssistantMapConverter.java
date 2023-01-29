package com.maukaim.budde.assistant.intellij.plugin.core.assistant.persistence;

import com.fasterxml.jackson.core.type.TypeReference;
import com.intellij.util.xmlb.Converter;
import com.maukaim.budde.assistant.intellij.plugin.core.assistant.model.Assistant;
import com.maukaim.budde.assistant.intellij.plugin.core.marshall.JacksonMarshaller;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class AssistantMapConverter extends Converter<Map<String,Assistant>> {
    private JacksonMarshaller jacksonMarshaller;

    public AssistantMapConverter() {
        jacksonMarshaller = new JacksonMarshaller();
    }

    @Override
    public @Nullable Map<String, Assistant> fromString(@NotNull String value) {
        TypeReference<Map<String, Assistant>> typeReference = new TypeReference<>() {
        };
        return jacksonMarshaller.unMarshallMap(value, typeReference);
    }

    @Override
    public @Nullable String toString(@NotNull Map<String, Assistant> value) {
        return jacksonMarshaller.marshall(value);
    }

//    @Override
//    public @Nullable Assistant fromString(@NotNull String value) {
//        return jacksonMarshaller.unMarshall(value, Assistant.class);
//    }
//
//    @Override
//    public @Nullable String toString(@NotNull Assistant value) {
//        return jacksonMarshaller.marshall(value);
//    }
}
