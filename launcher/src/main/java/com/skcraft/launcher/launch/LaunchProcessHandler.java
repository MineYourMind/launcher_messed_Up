/*
 * SK's Minecraft Launcher
 * Copyright (C) 2010-2014 Albert Pham <http://www.sk89q.com> and contributors
 * Please see LICENSE.txt for license information.
 */

package com.skcraft.launcher.launch;

import com.google.common.base.Function;
import com.skcraft.launcher.Launcher;
import com.skcraft.launcher.dialog.ProcessConsoleFrame;
import lombok.NonNull;
import lombok.extern.java.Log;

import javax.swing.*;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;

/**
 * Handles post-process creation during launch.
 */
@Log
public class LaunchProcessHandler implements Function<Process, ProcessConsoleFrame> {

    private static final int CONSOLE_NUM_LINES = 10000;

    private final Launcher launcher;
    private final boolean showProcessConsole;
    private ProcessConsoleFrame consoleFrame;

    public LaunchProcessHandler(@NonNull Launcher launcher, boolean showProcessConsole) {
        this.launcher = launcher;
        this.showProcessConsole = showProcessConsole;
    }

    @Override
    public ProcessConsoleFrame apply(final Process process) {
        log.info("Watching process " + process);

        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                    IProcessOutputConsumer messageLog;
                    if (showProcessConsole) {
                        consoleFrame = new ProcessConsoleFrame(CONSOLE_NUM_LINES, true);
                        consoleFrame.setProcess(process);
                        consoleFrame.setVisible(true);
                        messageLog = consoleFrame.getMessageLog();
                    } else {
                        messageLog = new ProcessOutputConsumerPassThrough();
                    }
                    messageLog.consume(process.getInputStream());
                    messageLog.consume(process.getErrorStream());
                }
            });

            // Wait for the process to end
            process.waitFor();
        } catch (InterruptedException e) {
            // Orphan process
        } catch (InvocationTargetException e) {
            log.log(Level.WARNING, "Unexpected failure", e);
        }

        log.info("Process ended, re-showing launcher...");

        // Restore the launcher
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                if (consoleFrame != null) {
                    consoleFrame.setProcess(null);
                    consoleFrame.requestFocus();
                }
            }
        });

        return consoleFrame;
    }

}
