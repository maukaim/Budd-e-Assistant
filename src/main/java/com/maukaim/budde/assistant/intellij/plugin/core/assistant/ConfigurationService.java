package com.maukaim.budde.assistant.intellij.plugin.core.assistant;

import com.intellij.credentialStore.CredentialAttributes;
import com.intellij.credentialStore.CredentialAttributesKt;
import com.intellij.credentialStore.Credentials;
import com.intellij.ide.passwordSafe.PasswordSafe;
import com.intellij.openapi.components.Service;
import com.intellij.openapi.project.Project;
import com.intellij.util.PlatformUtils;
import com.intellij.vcs.log.util.VcsUserUtil;
import com.maukaim.budde.assistant.intellij.plugin.core.assistant.model.AssistantConfiguration;
import com.maukaim.budde.assistant.intellij.plugin.listeners.ApiKeyChangedListener;
import com.maukaim.budde.assistant.intellij.plugin.shared.BuddeAssistantTopics;

@Service
public final class ConfigurationService {
    private final Project project;
    private static final String BUDDE_SYSTEM = "Budde_Assistant";
    private static final String OPENAI_API_KEY = "OpenAIApiKey";

    public ConfigurationService(Project project) {
        this.project = project;
    }

    public AssistantConfiguration getAssistantConfiguration() {
        return new AssistantConfiguration(getApiKey());
    }

    public void setApiKey(String apiKey) {
        storeApiKey(apiKey);
        notifyNewApiKey(apiKey);
    }

    private void storeApiKey(String apiKey) {
        CredentialAttributes credentialAttributes = createCredentialAttributes(OPENAI_API_KEY);
        Credentials credentials = new Credentials(PlatformUtils.getPlatformPrefix(), apiKey);
        PasswordSafe.getInstance().set(credentialAttributes, credentials);
    }

    private String getApiKey(){
        CredentialAttributes credentialAttributes = createCredentialAttributes(OPENAI_API_KEY);
        return PasswordSafe.getInstance().getPassword(credentialAttributes);
    }

    private void notifyNewApiKey(String apiKey) {
        ApiKeyChangedListener publisher = project.getMessageBus().syncPublisher(BuddeAssistantTopics.API_KEY_CHANGE);
        publisher.onApiKeyChanged(apiKey);
    }

    private CredentialAttributes createCredentialAttributes(String key) {
        return new CredentialAttributes(
                CredentialAttributesKt.generateServiceName(BUDDE_SYSTEM, key)
        );
    }
}
