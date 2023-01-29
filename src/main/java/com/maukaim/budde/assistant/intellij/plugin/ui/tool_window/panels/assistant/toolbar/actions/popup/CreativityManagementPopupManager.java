package com.maukaim.budde.assistant.intellij.plugin.ui.tool_window.panels.assistant.toolbar.actions.popup;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.JBPopup;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.components.JBSlider;
import com.intellij.util.ui.FormBuilder;
import com.intellij.util.ui.JBUI;
import com.maukaim.budde.assistant.intellij.plugin.core.assistant.Assistant;
import com.maukaim.budde.assistant.intellij.plugin.core.assistant.AssistantService;

import javax.swing.*;
import java.awt.*;

public class CreativityManagementPopupManager {

    public static void createAndShow(Project ctx) {
        JBScrollPane mainPanel = new JBScrollPane(){{
            setViewportView(new JPanel(new BorderLayout()) {{
                add(new JLabel("load creativity level..."), BorderLayout.NORTH);
            }});
        }};


        JBPopup popup = JBPopupFactory.getInstance().createComponentPopupBuilder(mainPanel, null)
                .setTitle("Creativity Level Used for Answers")
                .setModalContext(true)
                .setResizable(true)
                .setAdText("Wait while initializing the pop window...")
                .setMovable(true)
                .createPopup();

        popup.setRequestFocus(true);
        popup.showInFocusCenter();

        new Thread(() -> fillPopup(popup, mainPanel, ctx)).start();
    }

    private static void fillPopup(JBPopup popup, JBScrollPane mainPanel, Project ctx) {
        AssistantService assistantService = ctx.getService(AssistantService.class);
        Assistant currentAssistant = assistantService.getCurrentAssistant();

        JButton submitButton = generateSubmitButton();
        JBSlider slider = buildCreativityLevelSlider(currentAssistant);
        JPanel content = FormBuilder.createFormBuilder()
                .addLabeledComponent(currentAssistant.getName() +  "'s Creativity Level: ", slider)
                .addTooltip("Higher value means more creative.")
                .addSeparator()
                .addVerticalGap(8)
                .addComponentToRightColumn(submitButton)
                .getPanel();

        setBehaviorOnSubmit(ctx, submitButton, slider, popup);

        JPanel contentWrapper = new JPanel(new BorderLayout());
        contentWrapper.setBorder(JBUI.Borders.empty(12, 24));
        contentWrapper.add(content, BorderLayout.NORTH);
        mainPanel.setViewportView(contentWrapper);

        popup.setAdText("Set the creativity level for current assistant & Submit", SwingConstants.LEFT);

        mainPanel.repaint();
        mainPanel.revalidate();
        popup.pack(true, true);
    }

    private static JBSlider buildCreativityLevelSlider(Assistant currentAssistant) {
        return new JBSlider(JBSlider.HORIZONTAL, 0, 100, (int)(currentAssistant.getCreativityLevel()*100));
    }

    private static void setBehaviorOnSubmit(Project ctx,
                                            JButton submitButton,
                                            JBSlider slider,
                                            JBPopup popup) {
        submitButton.addActionListener(e -> {
            ctx.getService(AssistantService.class).updateCurrentAssistantCreativityLevel(slider.getValue());
            popup.cancel();
        });

    }

    private static JButton generateSubmitButton() {
        return new JButton("Submit");
    }
}
