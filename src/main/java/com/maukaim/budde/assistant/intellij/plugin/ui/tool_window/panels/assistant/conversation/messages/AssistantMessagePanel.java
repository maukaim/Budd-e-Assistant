package com.maukaim.budde.assistant.intellij.plugin.ui.tool_window.panels.assistant.conversation.messages;

import com.intellij.util.ui.UIUtil;
import com.maukaim.budde.assistant.intellij.plugin.core.assistant.Assistant;
import com.maukaim.budde.assistant.intellij.plugin.shared.ImageUtil;

import javax.swing.*;
import java.awt.*;

public class AssistantMessagePanel extends MessagePanel {

    public AssistantMessagePanel(String message, Assistant assistant) {
        super(message);
        buildUi(message, assistant);
    }

    @Override
    public boolean isUserMessage() {
        return false;
    }

    @Override
    protected Color getBgColor() {
        return UIUtil.getPanelBackground().darker();
    }

    protected void buildUi(String messageToDisplay, Assistant currentAssistant) {
        this.removeAll();

        JPanel iconPanel = createIconPanel(new ImageIcon(ImageUtil.getRoundedImage(currentAssistant.getB64FaceImage(), 2)));
        iconPanel.setMaximumSize(new Dimension(iconPanel.getPreferredSize().width, Integer.MAX_VALUE));
        JPanel textPanel = createTextPanel(messageToDisplay);
        iconPanel.setToolTipText(currentAssistant.getName());
        this.add(iconPanel);
        this.add(textPanel);

        revalidate();
        repaint();
    }


}
