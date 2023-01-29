package com.maukaim.budde.assistant.intellij.plugin.ui.tool_window.panels.assistant.conversation.messages;

import com.intellij.icons.AllIcons;
import com.intellij.util.ui.UIUtil;

import javax.swing.*;
import java.awt.*;

public class UserMessagePanel extends MessagePanel<String> {
    private static final Icon USER_ICON = AllIcons.General.User;

    public UserMessagePanel(String message) {
        super(message);
        buildUi(message);
    }

    @Override
    protected Color getBgColor() {
        return UIUtil.getPanelBackground();
    }

    private void buildUi(String messageToDisplay) {
        this.removeAll();

        JPanel iconPanel = createIconPanel(USER_ICON, false);
        iconPanel.setMaximumSize(new Dimension(iconPanel.getPreferredSize().width, iconPanel.getMaximumSize().width));
        JPanel textPanel = createTextPanel(messageToDisplay);
        this.add(textPanel);
        this.add(iconPanel);

        revalidate();
        repaint();
    }
}
