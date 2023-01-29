package com.maukaim.budde.assistant.intellij.plugin.ui.tool_window.panels.assistant.conversation.messages;

import com.intellij.ui.BadgeIcon;
import com.intellij.ui.components.JBTextArea;
import com.intellij.util.ui.JBUI;

import javax.swing.*;
import java.awt.*;

public abstract class MessagePanel extends JPanel {
    private final String rawMessage;

    public MessagePanel(String rawMessage) {
        this.rawMessage = rawMessage;
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.setBorder(JBUI.Borders.empty(JBUI.insets(18, 24)));
        this.setBackground(getBgColor());
    }

    public String getRawMessage() {
        return rawMessage;
    }

    public abstract boolean isUserMessage();

    protected abstract Color getBgColor();

    protected JPanel createTextPanel(String messageToDisplay) {
        String cleanedMessage = strip(messageToDisplay);

        JBTextArea jTextArea = new JBTextArea();
        jTextArea.setLineWrap(true);
        jTextArea.setWrapStyleWord(true);
        jTextArea.setEditable(false);
        jTextArea.setText(cleanedMessage);
        jTextArea.setOpaque(false);
        jTextArea.setMinimumSize(new Dimension(80, 40));

        JPanel textPanel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.fill = GridBagConstraints.BOTH;

        textPanel.add(jTextArea, constraints);
        textPanel.setOpaque(false);

        textPanel.setBorder(JBUI.Borders.empty(JBUI.insets(0, 12)));

        return textPanel;
    }

    private String strip(String message) {
        return message.strip()
                .replaceAll("(\\A(\\n)+)|((\\n)+\\z)", "")
                .replaceAll("\n ", "\n")
                .strip();
    }

    protected JPanel createIconPanel(Icon icon) {
        JPanel iconPanel = new JPanel(new BorderLayout());
        iconPanel.setOpaque(false);
        BadgeIcon badgeIcon = new BadgeIcon(icon, new Color(128, 255, 128));
        iconPanel.add(new JLabel(badgeIcon), BorderLayout.NORTH);
        iconPanel.setBorder(JBUI.Borders.empty(JBUI.insets(0, 12)));

        return iconPanel;
    }
}
