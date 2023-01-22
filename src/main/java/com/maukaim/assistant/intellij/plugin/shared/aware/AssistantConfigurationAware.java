package com.maukaim.assistant.intellij.plugin.shared.aware;

import com.maukaim.assistant.intellij.plugin.core.AssistantConfigurationService;

public interface AssistantConfigurationAware extends ProjectAware{
    default AssistantConfigurationService.AssistantConfiguration getAssistantConfiguration(){
        return getProject().getService(AssistantConfigurationService.class).getAssistantConfiguration();
    }
}
