/*
 * SK's Minecraft Launcher
 * Copyright (C) 2010-2014 Albert Pham <http://www.sk89q.com> and contributors
 * Please see LICENSE.txt for license information.
 */

package com.skcraft.launcher.launch;

import java.io.IOException;
import java.io.InputStream;

import static org.apache.commons.io.IOUtils.closeQuietly;

/**
 * A simple process output consumer which will pass everything it consumes to System.out.
 */
public class ProcessOutputConsumerPassThrough implements IProcessOutputConsumer {
    /**
     * Consume an input stream and print it to the dialog. The consumer
     * will be in a separate daemon thread.
     *
     * @param from stream to read
     */
    public void consume(InputStream from) {
        final InputStream in = from;
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                byte[] buffer = new byte[1024];
                try {
                    int len;
                    while ((len = in.read(buffer)) != -1) {
                        String s = new String(buffer, 0, len);
                        System.out.print(s);
                    }
                } catch (IOException e) {
                } finally {
                    closeQuietly(in);
                }
            }
        });
        thread.setDaemon(true);
        thread.start();
    }
}
