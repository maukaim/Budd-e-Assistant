package com.maukaim.budde.assistant.intellij.plugin.core.assistant.model;

import com.maukaim.budde.assistant.intellij.plugin.core.assistant.Assistant;

import java.time.Instant;
import java.util.List;

public class AssistantFactory {

    public static Assistant buildWithModulePaths(Assistant assistant, List<String> modulePaths){
        return build(assistant.getId(),
                assistant.getName(),
                assistant.getDescription(),
                assistant.getExternalServiceModelId(),
                assistant.getB64FaceImage(),
                modulePaths,
                assistant.getCreativityLevel(),
                assistant.getCreateInstant());
    }

    public static Assistant buildWithCreativityLevel(Assistant assistant, float creativityLevel){
        return build(assistant.getId(),
                assistant.getName(),
                assistant.getDescription(),
                assistant.getExternalServiceModelId(),
                assistant.getB64FaceImage(),
                assistant.getModulePaths(),
                creativityLevel,
                assistant.getCreateInstant());
    }

    public static Assistant build(String id,
                           String name,
                           String description,
                           String externalModelId,
                           String b64FaceImage,
                           List<String> modulePaths,
                           float creativityLevel,
                           Instant createInstant){
        return new Assistant(id, name, description, externalModelId, b64FaceImage,modulePaths, creativityLevel, createInstant);
    }
}
