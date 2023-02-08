package com.maukaim.budde.assistant.intellij.plugin.ui.tool_window.panels.assistant.conversation.messages;

import com.intellij.icons.AllIcons;
import com.intellij.util.ui.UIUtil;
import com.maukaim.budde.assistant.intellij.plugin.core.assistant.model.Assistant;
import com.maukaim.budde.assistant.intellij.plugin.core.chat.model.JavaFileIdentifier;
import com.maukaim.budde.assistant.intellij.plugin.shared.ImageUtil;

import javax.swing.*;
import java.awt.*;

public class FileMessagePanel extends MessagePanel<JavaFileIdentifier> {

    public FileMessagePanel(JavaFileIdentifier content) {
        super(content);
        buildUi(content);
    }

    @Override
    protected Color getBgColor() {
        return UIUtil.getPanelBackground();
    }

    private void buildUi(JavaFileIdentifier messageToDisplay) {
        this.removeAll();

        JPanel iconPanel = createIconPanel(AllIcons.Actions.AddFile, false);
        iconPanel.setMaximumSize(new Dimension(iconPanel.getPreferredSize().width, Integer.MAX_VALUE));
        JPanel textPanel = createTextPanel(messageToDisplay.getClassName());

        this.add(iconPanel);
        this.add(textPanel);

        revalidate();
        repaint();
    }
}
