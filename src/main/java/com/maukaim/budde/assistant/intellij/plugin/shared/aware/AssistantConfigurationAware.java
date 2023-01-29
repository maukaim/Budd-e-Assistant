package com.maukaim.budde.assistant.intellij.plugin.shared.aware;

import com.maukaim.budde.assistant.intellij.plugin.core.assistant.ConfigurationService;

public interface AssistantConfigurationAware extends ProjectAware{
    default ConfigurationService.AssistantConfiguration getAssistantConfiguration(){
        return getProject().getService(ConfigurationService.class).getAssistantConfiguration();
    }
}
