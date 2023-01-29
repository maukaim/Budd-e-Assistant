package com.maukaim.budde.assistant.intellij.plugin.ui.tool_window.panels.assistant.conversation;

import javax.swing.*;
import java.util.Timer;
import java.util.TimerTask;

public class WaitResponseComponent extends JLabel {
    private final Timer timer;

    public WaitResponseComponent() {
        this.setText(".");
        TimerTask task = new TimerTask() {
            int i = 1;

            public void run() {
                setText("." + new String(new char[i % 3]).replace("\0", "."));
                i++;
                if (i == 12) {
                    i = 1;
                }
            }
        };
        timer = new Timer();
        timer.scheduleAtFixedRate(task, 0, 500);
    }

    public void cancel() {
        timer.cancel();
    }
}
