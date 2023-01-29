package com.maukaim.budde.assistant.intellij.plugin.ui.tool_window.panels.assistant.conversation.messages;

import com.intellij.ui.BadgeIcon;
import com.intellij.ui.components.JBTextArea;
import com.intellij.util.ui.JBUI;

import javax.swing.*;
import java.awt.*;

public abstract class MessagePanel<T> extends JPanel {
    protected final T rawContent;

    public MessagePanel(T rawContent) {
        this.rawContent = rawContent;
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.setBorder(JBUI.Borders.empty(JBUI.insets(18, 24)));
        this.setBackground(getBgColor());
    }

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

    protected JPanel createIconPanel(Icon icon, boolean badged) {
        JPanel iconPanel = new JPanel(new BorderLayout());
        iconPanel.setOpaque(false);
        Icon iconToDisplay;
        if (badged) {
            iconToDisplay = new BadgeIcon(icon, new Color(128, 255, 128));
        } else {
            iconToDisplay = icon;
        }
        iconPanel.add(new JLabel(iconToDisplay), BorderLayout.NORTH);
        iconPanel.setBorder(JBUI.Borders.empty(JBUI.insets(0, 8)));

        return iconPanel;
    }
}
