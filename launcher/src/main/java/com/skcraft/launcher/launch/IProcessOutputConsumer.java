package com.skcraft.launcher.launch;

import java.io.InputStream;

/**
 * Created by octavian on 26.04.15.
 */
public interface IProcessOutputConsumer {
    /**
     * Consume an input stream and print it to the dialog. The consumer
     * will be in a separate daemon thread.
     *
     * @param from stream to read
     */
    public void consume(InputStream from);
}
