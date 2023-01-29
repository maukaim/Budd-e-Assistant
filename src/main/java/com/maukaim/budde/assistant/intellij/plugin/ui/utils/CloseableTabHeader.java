package com.maukaim.budde.assistant.intellij.plugin.ui.utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CloseableTabHeader extends JPanel {

    public CloseableTabHeader(JTabbedPane tabbedPane, String title) {
        super(new FlowLayout(FlowLayout.LEFT, 0, 0));
        setOpaque(false);

        JLabel label = new JLabel(title + "ici");
        add(label);
        label.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));
        JButton button = new JButton("x");
        add(button);
        setUpButton(button, tabbedPane);

    }

    private void setUpButton(JButton button, JTabbedPane tabbedPane) {
        button.setBorder(BorderFactory.createEmptyBorder());
        button.addActionListener(e -> {
            int index = tabbedPane.indexOfTabComponent(CloseableTabHeader.this);
            if (index != -1) {
                tabbedPane.remove(index);
            }
        });

        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setText("OK");
            }
            public void mouseExited(MouseEvent e) {
                button.setText("NOK");

//                button.setVisible(false);
            }
        });
    }
}