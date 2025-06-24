package com.supershop.Service;

import com.supershop.Entity.AppLog;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

@Service
public class LogService {
    private static final String LOG_FILE = "../Log/app.log";

    public synchronized void saveLog(AppLog log) {
        try (FileWriter fw = new FileWriter(LOG_FILE, true);
             PrintWriter pw = new PrintWriter(fw)) {

            pw.println(String.format("[%s] USER: %s - METHOD: %s",
                    log.getLogTimestamp().toString(),
                    log.getUsername(),
                    log.getAccessedMethod()));

        } catch (IOException e) {
            System.err.println("Error writing to log file: " + e.getMessage());
        }
    }
}
