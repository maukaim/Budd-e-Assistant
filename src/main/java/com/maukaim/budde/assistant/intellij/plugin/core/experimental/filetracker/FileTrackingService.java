package com.maukaim.budde.assistant.intellij.plugin.core.experimental.filetracker;

import com.intellij.openapi.components.Service;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiUtil;
import com.maukaim.budde.assistant.intellij.plugin.core.assistant.model.Assistant;
import com.maukaim.budde.assistant.intellij.plugin.core.assistant.AssistantService;
import org.jetbrains.annotations.ApiStatus;

import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@ApiStatus.Experimental
public final class FileTrackingService {
    private final Project ctx;

    public FileTrackingService(Project project) {
        ctx = project;
    }

    public void updatePendingTrackFileLists(PsiFile psiFile) {
        Module fileModule = ModuleUtil.findModuleForFile(psiFile);
        String modulePath = fileModule.getModuleFilePath();
        String filePath = psiFile.getVirtualFile().getCanonicalPath();

        AssistantService assistantService = ctx.getService(AssistantService.class);
        PendingFileUpdateRepository repository = ctx.getService(PendingFileUpdateRepository.class);
        assistantService.getAll().stream()
                .filter(assistant -> assistant.getModulePaths().contains(modulePath))
                .map(Assistant::getId)
                .forEach(assistantId -> repository.addTrackedFilePath(assistantId, filePath));

    }

    public List<PsiFile> retrieveAndPopAllFilesTracked(String assistantId) {
        Set<String> pendingFilePath = cleanPendingUpdates(assistantId);

        return pendingFilePath.stream()
                .map(filePath -> VfsUtil.findFile(Path.of(filePath), true))
                .filter(Objects::nonNull)
                .map(vf -> PsiUtil.getPsiFile(ctx, vf))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public Set<String> cleanPendingUpdates(String assistantId) {
        PendingFileUpdateRepository pendingFileUpdateRepository = ctx.getService(PendingFileUpdateRepository.class);
        return pendingFileUpdateRepository.popPendingFilePathByAssistantId(assistantId);
    }
}
