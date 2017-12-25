package moge.rpg.ai;

import java.io.IOException;
import java.util.Arrays;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.SimpleFormatter;

public class Logger {

    private static final String file = "/home/masashi/PeerCast/mogeRPGserver/log/logger_" + System.currentTimeMillis() + ".log";

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

    public void info(String msg) {
        logger.fine(msg);
    }

    public void close() {
        Arrays.stream(logger.getHandlers()).forEach(h -> h.close());
    }
}
