package com.maukaim.budde.assistant.intellij.plugin.listeners.impl;

import com.intellij.codeInsight.daemon.impl.DaemonCodeAnalyzerImpl;
import com.intellij.lang.annotation.HighlightSeverity;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.newvfs.BulkFileListener;
import com.intellij.openapi.vfs.newvfs.events.VFileEvent;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiJavaFile;
import com.intellij.psi.PsiManager;
import com.maukaim.budde.assistant.intellij.plugin.core.filetracker.FileTrackingService;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class JavaFileChangeListener implements BulkFileListener {
    private final Project ctx;
    private final PsiManager psiManager;
    private final PsiDocumentManager psiDocumentManager;
    private final ModuleManager moduleManager;

    public JavaFileChangeListener(Project project) {
        this.ctx = project;
        psiManager = PsiManager.getInstance(ctx);
        psiDocumentManager = PsiDocumentManager.getInstance(ctx);
        moduleManager = ModuleManager.getInstance(ctx);
    }

    @Override
    public void after(@NotNull List<? extends @NotNull VFileEvent> events) {
        FileTrackingService service = ctx.getService(FileTrackingService.class);

        for (VFileEvent event : events) {
            VirtualFile virtualFile = event.getFile();
            if (virtualFile != null) {
                PsiFile psiFile = psiManager.findFile(virtualFile);
                if (isValidForTracking(psiFile)) {
                    System.out.println("On peut le suivre !");
                    service.updatePendingTrackFileLists(psiFile);
                }
            }
        }
    }

    private boolean isValidForTracking(PsiFile psiFile) {
        if (psiFile instanceof PsiJavaFile) {
            Document document = psiDocumentManager.getDocument(psiFile);
            return document != null
                    && (DaemonCodeAnalyzerImpl.getHighlights(document, HighlightSeverity.ERROR, ctx).isEmpty())
                    && ModuleUtil.findModuleForFile(psiFile) != null;
        }

        return false;
    }
}
