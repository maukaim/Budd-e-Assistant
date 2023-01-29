package com.maukaim.budde.assistant.intellij.plugin.ui.tool_window.panels.assistant.toolbar.actions.popup;

import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.JBPopup;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.util.PsiUtil;
import com.intellij.ui.components.JBCheckBox;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.util.ui.FormBuilder;
import com.intellij.util.ui.JBUI;
import com.maukaim.budde.assistant.intellij.plugin.core.assistant.model.Assistant;
import com.maukaim.budde.assistant.intellij.plugin.core.assistant.AssistantService;
import org.jetbrains.annotations.ApiStatus;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@ApiStatus.Experimental
public class ModuleTrackedPopupManager {

    public static void createAndShow(Project ctx) {
        JBScrollPane mainPanel = new JBScrollPane() {{
            setViewportView(new JPanel(new BorderLayout()) {{
                add(new JLabel("Loading modules..."), BorderLayout.NORTH);
            }});
        }};

        AssistantService assistantService = ctx.getService(AssistantService.class);
        Assistant currentAssistant = assistantService.getCurrentAssistant();
        JBPopup popup = JBPopupFactory.getInstance().createComponentPopupBuilder(mainPanel, null)
                .setTitle(currentAssistant.getName() + "'s Tracked Modules")
                .setModalContext(true)
                .setResizable(true)
                .setAdText("Loading tracked modules for current assistant...")
                .setMovable(true)
                .createPopup();

        popup.setRequestFocus(true);
        popup.showInFocusCenter();

        new Thread(() -> fillPopup(currentAssistant, popup, mainPanel, ctx)).start();
    }

    private static void fillPopup(Assistant currentAssistant, JBPopup popup, JBScrollPane mainPanel, Project ctx) {
        JButton submitButton = generateSubmitButton();
        Map<String, Module> moduleMap = mapModuleToCustomName(ctx);
        List<JBCheckBox> moduleCheckBoxes = buildModulesCheckBoxes(currentAssistant, moduleMap, ctx);
        FormBuilder formBuilder = FormBuilder.createFormBuilder()
                .addComponent(new JLabel("Tracked modules: "))
                .addSeparator()
                .addVerticalGap(8);

        moduleCheckBoxes.forEach(formBuilder::addComponent);

        JPanel content = formBuilder.addComponentToRightColumn(submitButton)
                .getPanel();

        setBehaviorOnSubmit(ctx, submitButton, moduleCheckBoxes, moduleMap, popup);

        JPanel contentWrapper = new JPanel(new BorderLayout());
        contentWrapper.setBorder(JBUI.Borders.empty(12, 24));
        contentWrapper.add(content, BorderLayout.NORTH);
        mainPanel.setViewportView(contentWrapper);

        popup.setAdText("Add/Remove tracked module(s) and click Submit", SwingConstants.LEFT);

        mainPanel.repaint();
        mainPanel.revalidate();
        popup.pack(true, true);
    }

    private static void setBehaviorOnSubmit(Project ctx,
                                            JButton submitButton,
                                            List<JBCheckBox> modulesCheckBoxes,
                                            Map<String,Module> moduleMap,
                                            JBPopup popup) {
        submitButton.addActionListener(e -> {
            AssistantService service = ctx.getService(AssistantService.class);
            List<String> newModuleSelection = modulesCheckBoxes.stream()
                    .filter(AbstractButton::isSelected)
                    .map(checkBox -> moduleMap.get(checkBox.getText()))
                    .filter(Objects::nonNull)
                    .map(Module::getModuleFilePath)
                    .collect(Collectors.toList());

            service.updateCurrentAssistantTrackedModule(newModuleSelection);

            popup.cancel();
        });

    }

    private static List<JBCheckBox> buildModulesCheckBoxes(Assistant currentAssistant,
                                                           Map<String, Module> moduleMap,
                                                           Project ctx) {
        List<String> modulePaths = currentAssistant.getModulePaths();
        return moduleMap.entrySet().stream()
                .map(entry -> {
                    String customModuleName = entry.getKey();
                    Module module = entry.getValue();
                    return new JBCheckBox(customModuleName,
                            modulePaths.contains(module.getModuleFilePath()));
                })
                .collect(Collectors.toList());
    }

    private static Map<String, Module> mapModuleToCustomName(Project ctx) {
        ModuleManager moduleManager = ModuleManager.getInstance(ctx);

        return Arrays.stream(moduleManager.getModules())
                .collect(Collectors.toMap(
                        module -> buildModuleName(module, ctx),
                        module -> module
                ));

    }

    private static String buildModuleName(Module module, Project ctx) {
        StringBuilder nameBuilder = new StringBuilder();
        VirtualFile moduleFile = module.getModuleFile();
        if(moduleFile != null){
            Module parentModule = ModuleUtil.findModuleForFile(moduleFile, ctx);
            if (parentModule != null) {
                nameBuilder.append(buildModuleName(parentModule, ctx))
                        .append(" > ");
            }
        }
        return nameBuilder.append(module.getName()).toString();
    }

    private static JButton generateSubmitButton() {
        return new JButton("Submit");
    }
}
