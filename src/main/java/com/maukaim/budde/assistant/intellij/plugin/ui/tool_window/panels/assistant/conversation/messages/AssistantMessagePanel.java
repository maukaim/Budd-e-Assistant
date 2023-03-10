package com.maukaim.budde.assistant.intellij.plugin.ui.tool_window.panels.assistant.conversation.messages;

import com.intellij.openapi.project.Project;
import com.intellij.util.ui.UIUtil;
import com.maukaim.budde.assistant.intellij.plugin.core.assistant.model.Assistant;
import com.maukaim.budde.assistant.intellij.plugin.shared.ImageUtil;

import javax.swing.*;
import java.awt.*;

public class AssistantMessagePanel extends MessagePanel<String> {
    private final Project ctx;
    public AssistantMessagePanel(String message, Assistant assistant, Project project) {
        super(message);
        this.ctx = project;
        buildUi(message, assistant);
    }

    @Override
    protected Color getBgColor() {
        return UIUtil.getPanelBackground().darker();
    }

    private void buildUi(String messageToDisplay, Assistant currentAssistant) {
        this.removeAll();

        JPanel iconPanel = createIconPanel(new ImageIcon(ImageUtil.getRoundedImage(currentAssistant.getB64FaceImage(), 2, ctx)), true);
        iconPanel.setMaximumSize(new Dimension(iconPanel.getPreferredSize().width, Integer.MAX_VALUE));
        JPanel textPanel = createTextPanel(messageToDisplay);
        iconPanel.setToolTipText(currentAssistant.getName());
        this.add(iconPanel);
        this.add(textPanel);

        revalidate();
        repaint();
    }
}
