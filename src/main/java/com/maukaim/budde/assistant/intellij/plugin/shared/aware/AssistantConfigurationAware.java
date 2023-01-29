package com.maukaim.budde.assistant.intellij.plugin.shared.aware;

import com.maukaim.budde.assistant.intellij.plugin.core.assistant.ConfigurationService;
import com.maukaim.budde.assistant.intellij.plugin.core.assistant.model.AssistantConfiguration;

public interface AssistantConfigurationAware extends ProjectAware{
    default AssistantConfiguration getAssistantConfiguration(){
        return getProject().getService(ConfigurationService.class).getAssistantConfiguration();
    }
}
