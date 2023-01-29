package com.maukaim.budde.assistant.intellij.plugin.core.chat.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class JavaFileIdentifier {
    private final String className;
    private final String packageName;

    public JavaFileIdentifier(@JsonProperty("className") String className,
                              @JsonProperty("packageName") String packageName) {
        this.className = className;
        this.packageName = packageName;
    }

    public String getClassName() {
        return className;
    }

    public String getPackageName() {
        return packageName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JavaFileIdentifier that = (JavaFileIdentifier) o;
        return className.equals(that.className) && Objects.equals(packageName, that.packageName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(className, packageName);
    }
}