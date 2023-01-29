package com.maukaim.budde.assistant.intellij.plugin.core.filetracker;


import com.fasterxml.jackson.core.type.TypeReference;
import com.intellij.util.xmlb.Converter;
import com.maukaim.budde.assistant.intellij.plugin.core.marshall.JacksonMarshaller;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Set;

public class PendingFileUpdateMapConverter extends Converter<Map<String, Set<String>>> {
    private JacksonMarshaller jacksonMarshaller;

    public PendingFileUpdateMapConverter() {
        jacksonMarshaller = new JacksonMarshaller();
    }

    @Override
    public @Nullable Map<String, Set<String>> fromString(@NotNull String value) {
        TypeReference<Map<String, Set<String>>> typeReference = new TypeReference<>() {
        };
        return jacksonMarshaller.unMarshallMap(value, typeReference);
    }

    @Override
    public @Nullable String toString(@NotNull Map<String, Set<String>> value) {
        return jacksonMarshaller.marshall(value);
    }

}
