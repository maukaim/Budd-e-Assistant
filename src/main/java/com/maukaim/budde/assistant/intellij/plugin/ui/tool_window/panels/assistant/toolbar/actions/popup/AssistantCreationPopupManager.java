package com.maukaim.budde.assistant.intellij.plugin.ui.tool_window.panels.assistant.toolbar.actions.popup;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.openapi.ui.popup.JBPopup;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.ui.components.JBCheckBox;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.components.JBTextArea;
import com.intellij.util.ui.FormBuilder;
import com.intellij.util.ui.JBUI;
import com.intellij.util.ui.UIUtil;
import com.maukaim.budde.assistant.intellij.plugin.core.ai.openai.OpenAIService;
import com.maukaim.budde.assistant.intellij.plugin.core.assistant.AssistantService;
import com.maukaim.budde.assistant.intellij.plugin.shared.NameProvider;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.List;
import java.util.Random;

public class AssistantCreationPopupManager {

    public static void createAndShow(Project ctx) {
        JBScrollPane mainPanel = new JBScrollPane() {{
            setViewportView(new JPanel(new BorderLayout()) {{
                add(new JLabel("Loading data..."), BorderLayout.NORTH);
            }});
        }};

        JBPopup popup = JBPopupFactory.getInstance().createComponentPopupBuilder(mainPanel, null)
                .setTitle("New Assistant")
                .setModalContext(true)
                .setResizable(true)
                .setAdText("Loading base models from external service...")
                .setMovable(true)
                .createPopup();

        popup.setRequestFocus(true);
        popup.showInFocusCenter();

        new Thread(() -> fillPopup(popup, mainPanel, ctx)).start();
    }

    private static void fillPopup(JBPopup popup, JBScrollPane mainPanel, Project ctx) {
        JTextField nameField = generateNameField();
        JTextArea descriptionField = generateDescriptionField();

        JBCheckBox generateFaceChoice = generateFaceChoiceBox();
        JLabel optionalContextLabel = generateOptionalFaceContextLabel(generateFaceChoice);
        JBTextArea optionalContextField = generateOptionalFaceContextField(generateFaceChoice);
        JBLabel optionalContextTooltip = generateOptionalFaceContextTip(generateFaceChoice);

        JButton submitButton = generateSubmitButton();
        constraintButtonOnNoBlankField(submitButton, nameField);

        JComboBox<String> modelIdComboBox = buildComboBox(ctx);

        JPanel content = FormBuilder.createFormBuilder()
                .addLabeledComponent("Name: ", nameField)
                .addLabeledComponent("Will help you on: ", descriptionField, true)
                .addVerticalGap(8)
                .addLabeledComponent("Base model: ", modelIdComboBox)
                .addVerticalGap(8)
                .addComponent(new JLabel("Advanced Settings"))
                .addSeparator()
                .addComponent(generateFaceChoice)
                .addComponent(optionalContextLabel)
                .addComponent(optionalContextField)
                .addComponentToRightColumn(optionalContextTooltip, 1)
                .addVerticalGap(8)
                .addComponentToRightColumn(submitButton)
                .getPanel();

        generateFaceChoice.addItemListener(e -> {
            optionalContextField.setVisible(generateFaceChoice.isSelected());
            optionalContextLabel.setVisible(generateFaceChoice.isSelected());
            optionalContextTooltip.setVisible(generateFaceChoice.isSelected());
            content.revalidate();
            content.repaint();
            popup.pack(true, true);

        });

        setBehaviorOnSubmit(ctx, submitButton, nameField, descriptionField, generateFaceChoice, optionalContextField, modelIdComboBox, popup);

        JPanel contentWrapper = new JPanel(new BorderLayout());
        contentWrapper.setBorder(JBUI.Borders.empty(12, 24));
        contentWrapper.add(content, BorderLayout.NORTH);
        mainPanel.setViewportView(contentWrapper);

        popup.setAdText("Parameter your assistant and click Submit", SwingConstants.LEFT);

        mainPanel.repaint();
        mainPanel.revalidate();
        popup.pack(true, true);
    }

    private static JComboBox<String> buildComboBox(Project ctx) {
        OpenAIService assistantService = ctx.getService(OpenAIService.class);
        JComboBox<String> comboBox = new ComboBox<>();

        List<String> iaServiceModelIds = assistantService.getAllBaseModel();
        iaServiceModelIds.stream().forEach(comboBox::addItem);
        comboBox.setSelectedItem(iaServiceModelIds.get(new Random().nextInt(800) % iaServiceModelIds.size()));
        return comboBox;
    }

    private static void setBehaviorOnSubmit(Project ctx,
                                            JButton submitButton,
                                            JTextField nameField,
                                            JTextArea descriptionField,
                                            JBCheckBox generateFaceChoice,
                                            JBTextArea optionalContextField,
                                            JComboBox<String> comboBox,
                                            JBPopup popup) {
        submitButton.addActionListener(e -> {
            AssistantService service = ctx.getService(AssistantService.class);
            service.createAssistant(nameField.getText(),
                    descriptionField.getText(),
                    generateFaceChoice.isSelected(),
                    comboBox.getItemAt(comboBox.getSelectedIndex()),
                    optionalContextField.getText());
            popup.cancel();
        });

    }

    private static void constraintButtonOnNoBlankField(JButton submitButton, JTextField nameField) {
        nameField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                if (!(nameField.getText() == null) && !nameField.getText().isBlank()) {
                    submitButton.setEnabled(true);
                } else {
                    submitButton.setEnabled(false);
                }
            }
        });
    }

    private static JButton generateSubmitButton() {
        return new JButton("Submit");
    }

    private static JBLabel generateOptionalFaceContextTip(JBCheckBox generateFaceChoice) {
        JBLabel toolTip = new JBLabel("better work as a short sentence.", UIUtil.ComponentStyle.SMALL, UIUtil.FontColor.BRIGHTER) {{
            setBorder(JBUI.Borders.emptyLeft(10));
        }};
        toolTip.setVisible(generateFaceChoice.isSelected());
        return toolTip;
    }

    private static JBTextArea generateOptionalFaceContextField(JBCheckBox generateFaceChoice) {
        JBTextArea textArea = new JBTextArea() {{
            setLineWrap(true);
        }};
        textArea.setVisible(generateFaceChoice.isSelected());
        return textArea;
    }

    private static JLabel generateOptionalFaceContextLabel(JBCheckBox generateFaceChoice) {
        JLabel label = new JLabel("The Assistant context on the photo: ");
        label.setVisible(generateFaceChoice.isSelected());
        return label;
    }

    private static JBCheckBox generateFaceChoiceBox() {
        return new JBCheckBox("Generate a custom face");
    }

    private static JTextArea generateDescriptionField() {
        return new JBTextArea() {{
            setLineWrap(true);
        }};
    }

    private static JTextField generateNameField() {
        return new JTextField(NameProvider.provideRandom());
    }
}
