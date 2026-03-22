package utils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.*;

public class LoggingManager {

    private static final String LOGS_DIR = System.getProperty("user.dir") + File.separator +
            "Reports" + File.separator + "Logs";

    public static void configureLogging() {
        File logDir = new File(LOGS_DIR);
        if (!logDir.exists())
            logDir.mkdirs();

        Logger rootLogger = Logger.getLogger("");

        // Remove default console handlers
        for (Handler handler : rootLogger.getHandlers()) {
            rootLogger.removeHandler(handler);
        }

        // Custom formatter
        Formatter oneLineFormatter = new Formatter() {
            private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            @Override
            public String format(LogRecord record) {
                String time = sdf.format(new Date(record.getMillis()));
                String className = record.getSourceClassName();
                String method = record.getSourceMethodName();
                String message = formatMessage(record);

                return String.format("[%s] [%s %s] %s%n", time, className, method, message);
            }
        };

        try {
            String timestamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
            FileHandler fileHandler = new FileHandler(
                    LOGS_DIR + File.separator + "execution_" + timestamp + ".log", true);
            fileHandler.setFormatter(oneLineFormatter);

            rootLogger.addHandler(fileHandler);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        // Optional console logging (only your logs)
        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setFormatter(new SimpleFormatter());
        rootLogger.addHandler(consoleHandler);
    }

}
