package com.maukaim.budde.assistant.intellij.plugin.core.ai;

import org.jetbrains.annotations.ApiStatus;

/**
 * Used by connectors to set parameters as the epochs
 */
@ApiStatus.Experimental
public enum TrainingPriority {
    LOW,
    MEDIUM,
    HIGH
}
