package moge.rpg.ai;

import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.SimpleFormatter;

public class Logger {

    private static final String file = "/home/masashi/Desktop/log.log";

    private static Logger selfInstance;

    private static java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Logger.class.getSimpleName());

    private Logger() {
        FileHandler fileHandler;
        try {
            fileHandler = new FileHandler(file, false);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        fileHandler.setFormatter(new SimpleFormatter());
        logger.addHandler(fileHandler);
        logger.setLevel(Level.FINE);

        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setLevel(Level.CONFIG);
        logger.addHandler(consoleHandler);

        logger.setUseParentHandlers(false);
    }

    public static Logger getLogger() {
        if (selfInstance == null) {
            selfInstance = new Logger();
        }
        return selfInstance;
    }

    public void log(String msg) {
        logger.fine(msg);
    }
}
