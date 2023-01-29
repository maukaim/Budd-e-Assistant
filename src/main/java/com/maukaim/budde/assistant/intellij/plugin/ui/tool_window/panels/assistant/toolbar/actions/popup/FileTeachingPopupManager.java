package com.maukaim.budde.assistant.intellij.plugin.ui.tool_window.panels.assistant.toolbar.actions.popup;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectFileIndex;
import com.intellij.openapi.ui.popup.JBPopup;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.util.Computable;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFileSystemItem;
import com.intellij.psi.PsiJavaFile;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.util.ui.FormBuilder;
import com.intellij.util.ui.JBUI;
import com.maukaim.budde.assistant.intellij.plugin.core.assistant.AssistantService;
import com.maukaim.budde.assistant.intellij.plugin.core.assistant.model.Assistant;
import com.maukaim.budde.assistant.intellij.plugin.core.chat.model.JavaFileIdentifier;
import com.maukaim.budde.assistant.intellij.plugin.ui.tool_window.panels.assistant.toolbar.actions.popup.component.JavaFileCheckBox;
import com.maukaim.budde.assistant.intellij.plugin.ui.utils.PsiJavaFileLoader;
import org.jetbrains.annotations.ApiStatus;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

@ApiStatus.Experimental
public class FileTeachingPopupManager {

    public static void createAndShow(Project ctx) {
        JBScrollPane mainPanel = new JBScrollPane() {{
            setViewportView(new JPanel(new BorderLayout()) {{
                add(new JLabel("Loading Java files..."), BorderLayout.NORTH);
            }});
        }};

        Assistant currentAssistant = ctx.getService(AssistantService.class).getCurrentAssistant();
        JBPopup popup = JBPopupFactory.getInstance().createComponentPopupBuilder(mainPanel, null)
                .setTitle("What should " + currentAssistant.getName() + "Know?")
                .setModalContext(true)
                .setResizable(true)
                .setShowBorder(true)
                .setAdText("Loading all the Java files available in the project...")
                .setMovable(true)
                .createPopup();

        popup.setRequestFocus(true);
        popup.showInFocusCenter();

        new Thread(() -> fillPopup(popup, mainPanel, ctx)).start();
    }

    private static void fillPopup(JBPopup popup, JBScrollPane mainPanel, Project ctx) {
        JButton submitButton = generateSubmitButton();
        Map<JavaFileIdentifier, PsiJavaFile> fileMap = mapFileToCustomName(ctx);
        List<JavaFileCheckBox> fileCheckBoxes = buildFilesCheckBoxes(fileMap.keySet());

        FormBuilder formBuilder = FormBuilder.createFormBuilder()
                .addTooltip("Select below all the files to send to your assistant")
                .addVerticalGap(12);

        Map<String, List<JavaFileCheckBox>> boxMap = buildBoxesToPackageNameMap(fileCheckBoxes);
        for (String packageName : boxMap.keySet()) {
            formBuilder.addComponent(new JLabel(packageName))
                    .addSeparator();
            List<JavaFileCheckBox> javaFileCheckBoxes = boxMap.get(packageName);
            javaFileCheckBoxes.forEach(formBuilder::addComponent);
        }

        JPanel content = formBuilder
                .addVerticalGap(16).addComponentToRightColumn(submitButton)
                .getPanel();

        setBehaviorOnSubmit(ctx, submitButton, fileCheckBoxes, fileMap, popup);

        JPanel contentWrapper = new JPanel(new BorderLayout());
        contentWrapper.setBorder(JBUI.Borders.empty(12, 24));
        contentWrapper.add(content, BorderLayout.NORTH);
        mainPanel.setViewportView(contentWrapper);

        popup.setAdText("Choose files your assistant should know and click Submit", SwingConstants.LEFT);

        mainPanel.repaint();
        mainPanel.revalidate();
        popup.pack(true, true);
    }

    private static Map<String, List<JavaFileCheckBox>> buildBoxesToPackageNameMap(List<JavaFileCheckBox> fileCheckBoxes) {
        Map<String, List<JavaFileCheckBox>> result = new HashMap<>();
        for (JavaFileCheckBox fileCheckBox : fileCheckBoxes) {
            result.putIfAbsent(fileCheckBox.getPackageName(), new ArrayList<>());
            result.compute(fileCheckBox.getPackageName(), (key, val) -> {
                val.add(fileCheckBox);
                return val;
            });
        }
        return result;
    }

    private static void setBehaviorOnSubmit(Project ctx,
                                            JButton submitButton,
                                            List<JavaFileCheckBox> fileCheckBoxes,
                                            Map<JavaFileIdentifier, PsiJavaFile> fileMap,
                                            JBPopup popup) {
        submitButton.addActionListener(e -> {
            Map<JavaFileIdentifier, String> contentToIdentifier = new HashMap<>();
            for (JavaFileCheckBox box : fileCheckBoxes) {
                if (box.isSelected()) {
                    JavaFileIdentifier javaFileIdentifier = new JavaFileIdentifier(box.getFileName(), box.getPackageName());
                    String fileContent = ApplicationManager.getApplication().runReadAction((Computable<String>) () -> {
                        PsiJavaFile psiJavaFile = fileMap.get(javaFileIdentifier);
                        VirtualFile virtualFile = psiJavaFile.getVirtualFile();
                        try {
                            return new String(virtualFile.contentsToByteArray(), StandardCharsets.UTF_8);
                        } catch (IOException ex) {
                            return null;
                        }
                    });
                    if(fileContent != null){
                        contentToIdentifier.put(javaFileIdentifier, fileContent);
                    }
                }
            }
            AssistantService assistantService = ctx.getService(AssistantService.class);
            assistantService.teachesCurrentAssistant(contentToIdentifier);

            popup.cancel();
        });

    }

    private static String transformLightWeight(String javaFileContent) {
        int lastImportStatementStartIndex = javaFileContent.lastIndexOf("import");
        int firstLineBreakAfterImports = javaFileContent.indexOf("\n", lastImportStatementStartIndex);
        String usableContent = javaFileContent.substring(firstLineBreakAfterImports);
        return usableContent.replaceAll("\n","")
                .replaceAll(" {2}","")
                .replaceAll(" {2}", "");
    }

    private static List<JavaFileCheckBox> buildFilesCheckBoxes(Set<JavaFileIdentifier> javaFileIdentifiers) {
        return javaFileIdentifiers.stream()
                .map(fileIdentifier -> new JavaFileCheckBox(fileIdentifier.getClassName(),
                        fileIdentifier.getPackageName(),
                        false))
                .collect(Collectors.toList());
    }

    private static Map<JavaFileIdentifier, PsiJavaFile> mapFileToCustomName(Project ctx) {
        PsiJavaFileLoader psiJavaFileLoader = new PsiJavaFileLoader(ctx);
        ProjectFileIndex.SERVICE.getInstance(ctx).iterateContent(psiJavaFileLoader);
        List<PsiJavaFile> psiJavaFiles = psiJavaFileLoader.getPsiFiles();

        return psiJavaFiles.stream()
                .collect(Collectors.toMap(
                        FileTeachingPopupManager::buildFileName,
                        module -> module
                ));

    }

    private static JavaFileIdentifier buildFileName(PsiJavaFile javaFile) {
        return ApplicationManager.getApplication().runReadAction((Computable<JavaFileIdentifier>) () -> new JavaFileIdentifier(javaFile.getName(), javaFile.getPackageName()));
    }

    private static JButton generateSubmitButton() {
        return new JButton("Send To Assistant");
    }

}
