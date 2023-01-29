package com.maukaim.budde.assistant.intellij.plugin.shared;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.project.Project;
import com.intellij.util.ui.UIUtil;
import org.apache.commons.io.IOUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Objects;

public class ImageUtil {
    private static final String DEFAULT_ASSISTANT_BASE64_FILE_PATH = "assets/default_assistant_b64.txt";

    public static String getDefaultBase64AssistantFace() {
        try {
            return IOUtils.toString(Objects.requireNonNull(ImageUtil.class.getClassLoader()
                    .getResourceAsStream(DEFAULT_ASSISTANT_BASE64_FILE_PATH)), StandardCharsets.UTF_8);
        } catch (IOException e) {
            Notifications.Bus.notify(new Notification(NotifierUtil.NOTIFIER_GROUP, "Problem to get default assistant face", "An error occurred: " + e.getMessage(), NotificationType.ERROR));
            throw new RuntimeException(e);
        }
    }

    private static Image getDefaultAssistantRounded(int scale, Project ctx) {
        BufferedImage originalImage;
        try (InputStream inputStream = ImageUtil.class.getClassLoader().getResourceAsStream("assets/default_assistant.png")) {
            originalImage = ImageIO.read(inputStream);
        } catch (IOException e) {
            NotifierUtil.notifyError(ctx, "Problem To Get Default Assistant Face", "Issue: " + e.getMessage());
            throw new RuntimeException(e);
        }

        return getRoundedImage(originalImage, scale);
    }

    public static Image getRoundedImage(String base64Image, int scale, Project ctx){
        if(base64Image == null){
            return getDefaultAssistantRounded(scale, ctx);
        }
        byte[] imageBytes = Base64.getDecoder().decode(base64Image);
        BufferedImage originalImage;
        try {
            originalImage = ImageIO.read(new ByteArrayInputStream(imageBytes));
        }catch (Exception e){
            NotifierUtil.notifyError(ctx, "Problem To Get Assistant Face", "Issue: " + e.getMessage());
            throw new RuntimeException(e);
        }

        return getRoundedImage(originalImage, scale);
    }

    private static Image getRoundedImage(BufferedImage originalImage, int scale){
        int size = (int) (UIUtil.getFontSize(UIUtil.FontSize.NORMAL) * scale);

        Image scaledImage = originalImage.getScaledInstance(size, size, Image.SCALE_SMOOTH);
        BufferedImage roundedImage = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = roundedImage.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setClip(new RoundRectangle2D.Double(0, 0, size, size, size, size));
        g2d.drawImage(scaledImage, 0, 0, null);
        g2d.dispose();

        return roundedImage;
    }
}
