package com.maukaim.budde.assistant.intellij.plugin.ui.tool_window.panels.assistant.toolbar.selector;

import com.maukaim.budde.assistant.intellij.plugin.core.assistant.model.Assistant;

import javax.swing.*;
import java.awt.*;

public class AssistantComboBoxRenderer extends DefaultListCellRenderer {

    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        Component component = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        if(list.isSelectionEmpty()){
            setText("None Available");
        }else if (value instanceof Assistant) {
            Assistant assistant = (Assistant) value;
            setText(assistant.getName());
        }
        return component;
    }
}
