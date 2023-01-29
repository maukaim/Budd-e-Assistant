package com.maukaim.budde.assistant.intellij.plugin.core.assistant.persistence;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.Service;
import com.intellij.openapi.components.State;
import com.intellij.util.xmlb.XmlSerializerUtil;
import com.intellij.util.xmlb.annotations.OptionTag;
import com.maukaim.budde.assistant.intellij.plugin.core.assistant.model.Assistant;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

@Service
//@State(name="newPlace")
@State(name="BarbieLand")
public final class AssistantRepository implements PersistentStateComponent<AssistantRepository> {
    @OptionTag(converter = AssistantMapConverter.class)
    public Map<String, Assistant> allAssistantCache = new HashMap<>();
    public String currentAssistantId = "";

    @Override
    public @Nullable AssistantRepository getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull AssistantRepository state) {
        XmlSerializerUtil.copyBean(state, this);
    }

    public void put(Assistant assistant){
        this.allAssistantCache.put(assistant.getId(), assistant);
    }

    public Assistant getCurrent(){
         return getById(currentAssistantId);
    }

    public void setCurrent(String newCurrentAssistantId){
         currentAssistantId = newCurrentAssistantId;
    }

    public Assistant getById(String assistantId){
        return allAssistantCache.get(assistantId);
    }

    public Assistant remove(String assistantId){
        return allAssistantCache.remove(assistantId);
    }

    public boolean isEmpty(){
        return allAssistantCache.isEmpty();
    }

    public List<Assistant> getAll(){
        return allAssistantCache.values().stream().collect(Collectors.toList());
    }
}
