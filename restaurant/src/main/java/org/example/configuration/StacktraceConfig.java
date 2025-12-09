package org.example.configuration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class StacktraceConfig {

    private static final Logger log = LogManager.getLogger(StacktraceConfig.class);

    // It would be nice if it looked more like a spring error stacktrace?
    public static void logStackTraceFromThread(Exception e) {
        StringBuilder message = new StringBuilder();
        for(StackTraceElement stackTraceElement : e.getStackTrace()) {
            message.append(System.lineSeparator()).append(e).append(" ").append(stackTraceElement.toString());
        }
        log.error(message.toString());
    }

}
