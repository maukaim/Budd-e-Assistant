package com.maukaim.budde.assistant.intellij.plugin.ui.utils;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ContentIterator;
import com.intellij.openapi.util.Computable;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiJavaFile;
import com.intellij.psi.PsiManager;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class PsiJavaFileLoader implements ContentIterator {
    private final List<PsiJavaFile> loadedFiles = new ArrayList<>();
    private final Project ctx;

    public PsiJavaFileLoader(Project project) {
        this.ctx = project;
    }

    @Override
    public boolean processFile(@NotNull VirtualFile fileOrDir) {
        PsiManager psiManager = PsiManager.getInstance(ctx);
        PsiFile file = ApplicationManager.getApplication().runReadAction((Computable<PsiFile>) () -> psiManager.findFile(fileOrDir));
        if (file != null && file instanceof PsiJavaFile) {
            loadedFiles.add((PsiJavaFile) file);
        }
        return true;
    }

    public List<PsiJavaFile> getPsiFiles() {
        return List.copyOf(loadedFiles);
    }
}
