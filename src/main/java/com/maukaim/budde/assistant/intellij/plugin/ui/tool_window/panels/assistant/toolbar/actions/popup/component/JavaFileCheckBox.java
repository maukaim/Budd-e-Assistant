package com.maukaim.budde.assistant.intellij.plugin.ui.tool_window.panels.assistant.toolbar.actions.popup.component;

import com.intellij.openapi.util.NlsContexts;
import com.intellij.ui.components.JBCheckBox;
import org.jetbrains.annotations.Nullable;

public class JavaFileCheckBox extends JBCheckBox {
    private final String packageName;

    public JavaFileCheckBox(@Nullable @NlsContexts.Checkbox String fileName, String packageName, boolean selected) {
        super(fileName, selected);
        this.packageName = packageName;
    }

    public String getPackageName() {
        return packageName;
    }

    public String getFileName(){
        return getText();
    }
}
