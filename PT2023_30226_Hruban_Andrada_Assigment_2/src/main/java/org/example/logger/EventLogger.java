package org.example.logger;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class EventLogger {
    private String logFileName;

    public EventLogger(String logFileName) {
        this.logFileName = logFileName;
    }

    public synchronized void logEvent(String event) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(logFileName, true))) {
            writer.write(event + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
