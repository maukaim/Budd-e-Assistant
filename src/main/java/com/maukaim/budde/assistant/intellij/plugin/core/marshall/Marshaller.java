package com.maukaim.budde.assistant.intellij.plugin.core.marshall;

public interface Marshaller {
    <T> T unMarshall(String json, Class<T> clazz);
    String marshall(Object object);

}
