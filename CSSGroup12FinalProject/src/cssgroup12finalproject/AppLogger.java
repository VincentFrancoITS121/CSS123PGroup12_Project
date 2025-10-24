/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cssgroup12finalproject;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AppLogger {
    private static final String LOG_FILE = "manhwa_link.log";
    private static PrintWriter writer;
    
    static {
        try {
            writer = new PrintWriter(new FileWriter(LOG_FILE, true));
        } catch (IOException e) {
            System.err.println("Logger init failed: " + e.getMessage());
        }
    }
    
    public static void log(String message) {
        String timestamp = LocalDateTime.now()
            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String entry = "[" + timestamp + "] " + message;
        
        System.out.println(entry);
        if (writer != null) {
            writer.println(entry);
            writer.flush();
        }
    }
    
    public static void error(String message, Exception e) {
        log("ERROR: " + message);
        if (e != null && writer != null) {
            e.printStackTrace(writer);
            writer.flush();
        }
    }
    
    public static void info(String message) {
        log("INFO: " + message);
    }
    
    private AppLogger() {} // Prevent instantiation
}
