package org.example.configuration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class StacktraceFromThread {

    private static final Logger log = LogManager.getLogger(StacktraceFromThread.class);

    public static void logStackTraceFromThread() {
        StringBuilder message = new StringBuilder();
        for(StackTraceElement stackTraceElement : Thread.currentThread().getStackTrace()) {
            message.append(System.lineSeparator()).append(stackTraceElement.toString());
        }
        log.error(message.toString());
    }

}
