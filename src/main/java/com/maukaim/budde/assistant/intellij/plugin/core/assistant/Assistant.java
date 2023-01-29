package com.maukaim.budde.assistant.intellij.plugin.core.assistant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

public class Assistant {
    private String id;
    private String name;
    private String description;
    private String externalServiceModelId;
    private String b64FaceImage;
    private List<String> modulePaths;
    private float creativityLevel;
    private Instant createInstant;

    @JsonCreator
    public Assistant(@JsonProperty("id") String id,
                     @JsonProperty("name") String name,
                     @JsonProperty("description") String description,
                     @JsonProperty("externalServiceModelId") String externalServiceModelId,
                     @JsonProperty("b64FaceImage") String b64FaceImage,
                     @JsonProperty("modulePaths") List<String> modulePaths,
                     @JsonProperty("creativityLevel") float creativityLevel,
                     @JsonProperty("createInstant") Instant createInstant) {
        this.name = name;
        this.id = id;
        this.createInstant = createInstant;
        this.description =description;
        this.externalServiceModelId = externalServiceModelId;
        this.b64FaceImage = b64FaceImage;
        this.modulePaths = modulePaths;
        this.creativityLevel = creativityLevel;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getExternalServiceModelId() {
        return externalServiceModelId;
    }

    public String getB64FaceImage() {
        return b64FaceImage;
    }

    public List<String> getModulePaths() {
        return Objects.requireNonNullElse(modulePaths, List.of());
    }

    public float getCreativityLevel() {
        return creativityLevel;
    }

    public Instant getCreateInstant() {
        return createInstant;
    }

    @Override
    public String toString() {
        return "Assistant{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", externalServiceModelId='" + externalServiceModelId + '\'' +
                ", b64FaceImage='" + b64FaceImage + '\'' +
                ", modulePaths=" + modulePaths +
                ", creativityLevel=" + creativityLevel +
                ", createInstant=" + createInstant +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Assistant assistant = (Assistant) o;
        return Float.compare(assistant.creativityLevel, creativityLevel) == 0 && id.equals(assistant.id) && name.equals(assistant.name) && Objects.equals(description, assistant.description) && Objects.equals(externalServiceModelId, assistant.externalServiceModelId) && Objects.equals(b64FaceImage, assistant.b64FaceImage) && Objects.equals(modulePaths, assistant.modulePaths) && createInstant.equals(assistant.createInstant);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, externalServiceModelId, b64FaceImage, modulePaths, creativityLevel, createInstant);
    }
}
