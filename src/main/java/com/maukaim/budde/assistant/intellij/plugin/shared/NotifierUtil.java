package com.maukaim.budde.assistant.intellij.plugin.shared;

import com.intellij.notification.NotificationGroupManager;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.project.Project;

public class NotifierUtil {
    public static final String NOTIFIER_GROUP = "Budd-e Notification Group";

    public static void notifyError(Project project,
                                   String title,
                                   String content) {
        NotificationGroupManager.getInstance()
                .getNotificationGroup(NOTIFIER_GROUP)
                .createNotification(title, content, NotificationType.ERROR)
                .notify(project);
    }

    public static void notifyWarning(Project project,
                                   String title,
                                   String content) {
        NotificationGroupManager.getInstance()
                .getNotificationGroup(NOTIFIER_GROUP)
                .createNotification(title, content, NotificationType.WARNING)
                .notify(project);
    }
}
