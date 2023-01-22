package com.maukaim.assistant.intellij.plugin.ui.tool_window.panels.tabs;

import com.intellij.icons.AllIcons;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class CloseableTab extends JPanel {
    private JLabel label;

    public CloseableTab(final Component component, String title) {
        super(new FlowLayout(FlowLayout.LEFT, 4, 2));
        setOpaque(false);

        label = new JLabel(title);
        add(label);
        JButton button = new JButton();
        button.setIcon(AllIcons.Actions.Close);
        button.setOpaque(false);
        button.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                button.setIcon(AllIcons.Actions.CloseHovered);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                button.setIcon(AllIcons.Actions.CloseHovered);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                button.setIcon(AllIcons.Actions.Close);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                button.setIcon(AllIcons.Actions.CloseHovered);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setIcon(AllIcons.Actions.Close);
            }
        });
        button.setPreferredSize(new Dimension(18, 18));
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JTabbedPane tabbedPane = (JTabbedPane) getParent().getParent();
                tabbedPane.remove(component);
            }
        });
        add(button);
    }
}
