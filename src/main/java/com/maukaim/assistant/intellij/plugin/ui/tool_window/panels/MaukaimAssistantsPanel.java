package com.maukaim.assistant.intellij.plugin.ui.tool_window.panels;

import com.intellij.icons.AllIcons;
import com.intellij.ide.highlighter.JavaFileType;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.IconButton;
import com.intellij.ui.EditorTextField;
import com.intellij.ui.JBSplitter;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.components.JBTabbedPane;
import com.intellij.util.ui.TimedDeadzone;
import com.maukaim.assistant.intellij.plugin.ui.tool_window.panels.tabs.CloseActionButton;
import com.maukaim.assistant.intellij.plugin.ui.tool_window.panels.tabs.CloseableTab;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;


public class MaukaimAssistantsPanel extends JBTabbedPane {
    private static final String ADD_TAB_HEADER = "+";
    private final Project ctx;

    public MaukaimAssistantsPanel(Project project) {
        this.ctx = project;
        this.addChangeListener(e -> {
            System.out.println(this.getSelectedComponent());
            System.out.println(this.getTabComponentAt(getSelectedIndex()));
            if (getTitleAt(getSelectedIndex()) == ADD_TAB_HEADER) {
                this.remove(getSelectedIndex());
                int newTabIndex = this.getTabCount();
                this.addTab("Tab " + (newTabIndex + 1), new JLabel("Content: " + Math.random()));
                this.setSelectedIndex(newTabIndex);

                this.addTab(ADD_TAB_HEADER, new JLabel());
            }
        });
        this.add("The Conversation", getPanel());
        this.addTab(ADD_TAB_HEADER, new JLabel());
    }

    @Override
    public void addTab(String title, Component component) {
        super.addTab(title, component);
        if(title != ADD_TAB_HEADER){
            int count = this.getTabCount() - 1;
            setTabComponentAt(count, new CloseableTab(component, title));
        }
    }

    private Component getPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        JTextArea textArea = new JTextArea();

        textArea.setMinimumSize(new Dimension(0, 36));

        JScrollPane scrollPane = new JBScrollPane(textArea);
        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));

        for (int i = 0; i < 30; i++) {
            if (i == 14) {
                EditorFactory editorFactory = EditorFactory.getInstance();
                Document document = editorFactory.createDocument("public class UneClasseCool {\npublic void essaye(){ return;}\n}");

                EditorTextField editorTextField = new EditorTextField(document, ctx, JavaFileType.INSTANCE, true);
                container.add(editorTextField);
                continue;
            }
            container.add(new JLabel("Hehe " + i));
        }

        scrollPane.setViewportView(container);
        JBSplitter splitter = new JBSplitter(true, 1);

        splitter.setFirstComponent(scrollPane);
        splitter.setSecondComponent(textArea);

        return splitter;
    }
}

