package util;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
/**
 * Utility class to log messages to the console with colored output and timestamps.
 * 
 * <p>Supports different log levels: INFO, WARN, ERROR, and DEBUG.
 * Each message is printed with the current time and color-coded for clarity.</p>
 *
 * <p>Example output: {@code [14:23:01] [INFO ] Application started}</p>
 *
 * @author ResQ360
 */
public class Logger {
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm:ss");
    private static final String RESET = "\u001B[0m";
    /**
     * Enum representing ANSI color codes for terminal output.
     */
    public enum Color {
        BLACK("\u001B[30m"),
        RED("\u001B[31m"),
        GREEN("\u001B[32m"),
        YELLOW("\u001B[33m"),
        BLUE("\u001B[34m"),
        PURPLE("\u001B[35m"),
        CYAN("\u001B[36m"),
        WHITE("\u001B[37m"),

        BRIGHT_BLACK("\u001B[90m"),
        BRIGHT_RED("\u001B[91m"),
        BRIGHT_GREEN("\u001B[92m"),
        BRIGHT_YELLOW("\u001B[93m"),
        BRIGHT_BLUE("\u001B[94m"),
        BRIGHT_PURPLE("\u001B[95m"),
        BRIGHT_CYAN("\u001B[96m"),
        BRIGHT_WHITE("\u001B[97m");

        private final String code;
        /**
         * Constructor for the color enum.
         *
         * @param code ANSI escape code
         */
        Color(String code) {
            this.code = code;
        }
        /**
         * Returns the ANSI code as a string.
         *
         * @return the ANSI escape code
         */
        @Override
        public String toString() {
            return code;
        }
    }
    /**
     * Enum representing the log level (INFO, WARN, ERROR, DEBUG).
     * Each level has a label and an associated color.
     */
    public enum Level {
        INFO("INFO", Color.WHITE),
        WARN("WARN", Color.YELLOW),
        ERROR("ERROR", Color.RED),
        DEBUG("DEBUG", Color.CYAN);

        final String label;
        final Color color;
         /**
         * Constructor for log level.
         *
         * @param label label shown in the log
         * @param color color used for this level
         */
        Level(String label, Color color) {
            this.label = label;
            this.color = color;
        }
    }
    /**
     * Logs a message with the specified level, timestamp, and color.
     *
     * @param level   the log level
     * @param message the message to display
     */
    private static void log(Level level, String message) {
        String timestamp = LocalTime.now().format(TIME_FORMAT);
        String paddedLabel = String.format("%-5s", level.label);

        System.out.printf(
                "%s[%s] [%s] %s%s%n",
                level.color, timestamp, paddedLabel, message, RESET
        );
    }

    /**
     * Logs an informational message.
     *
     * @param message the message to log
     */
    public static void info(String message)  { 
        log(Level.INFO, message); 
    }
    /**
     * Logs a warning message.
     *
     * @param message the message to log
     */
    public static void warn(String message)  { 
        log(Level.WARN, message); 
    }
    /**
     * Logs an error message.
     *
     * @param message the message to log
     */
    public static void error(String message) { 
        log(Level.ERROR, message); 
    }
    /**
     * Logs an error message and prints the stack trace.
     *
     * @param message the message to log
     * @param e       the exception to print
     */
    public static void error(String message, Throwable e) {
        log(Level.ERROR, message);
        e.printStackTrace(System.err);
    }
    /**
     * Logs a debug message.
     *
     * @param message the message to log
     */
    public static void debug(String message) { 
        log(Level.DEBUG, message); 
    }
}
