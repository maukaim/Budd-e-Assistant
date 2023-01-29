package com.maukaim.budde.assistant.intellij.plugin.core.ai;

import com.maukaim.budde.assistant.intellij.plugin.core.chat.model.RawMessage;
import com.maukaim.budde.assistant.intellij.plugin.shared.ImageUtil;
import org.jetbrains.annotations.ApiStatus;

import java.util.List;

public interface ExternalAIService {

    List<String> getAllBaseModel();

    String prompt(String modelId, double creativityLevel, String question, List<RawMessage> history);

    default String generateAssistantFace(String assistantName, String... customizationItems) {
        return ImageUtil.getDefaultBase64AssistantFace();
    }

    @ApiStatus.Experimental
    List<String> getExistingAssistantModelIds();

    @ApiStatus.Experimental
    String createFineTunedModel(String modelId, String... trainingFiles);

}
