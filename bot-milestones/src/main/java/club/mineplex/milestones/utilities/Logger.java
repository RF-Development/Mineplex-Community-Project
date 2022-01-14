package club.mineplex.milestones.utilities;

import java.lang.management.ManagementFactory;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {

    public static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    /**
     * Get the class that called this one
     * Via https://stackoverflow.com/questions/11306811/how-to-get-the-caller-class-in-java
     *
     * @return Class name called from
     */
    public static String getCallerCallerClassName() {
        final StackTraceElement[] stElements = Thread.currentThread().getStackTrace();
        String callerClassName = null;
        for (int i = 1; i < stElements.length; i++) {
            final StackTraceElement ste = stElements[i];
            if (ste.getClassName().indexOf("java.lang.Thread") != 0) {
                if (callerClassName == null) {
                    callerClassName = ste.getClassName();
                } else if (!callerClassName.equals(ste.getClassName())) {
                    return ste.getClassName();
                }
            }
        }
        return null;
    }

    /**
     * Create a basic info log
     *
     * @param text   Text for the log
     * @param values Any values to format text with
     */
    public static void log(final String text, final String... values) {
        log(text, LogLevel.INFO, values);
    }

    /**
     * Create a log with a custom log level
     *
     * @param text   Text to log
     * @param level  The logging level
     * @param values Values to format the text with
     */
    public static void log(final String text, final LogLevel level, final String... values) {
        final String process = ManagementFactory.getRuntimeMXBean().getName();
        final String pid = process.substring(0, process.indexOf("@"));

        System.out.printf(
                "%s %s %s --- %s %s%n",
                dtf.format(LocalDateTime.now()),
                level.colour + level.label + "\u001B[0m",
                "\u001B[35m" + pid + "\u001B[0m",
                "\u001B[36m" + getCallerCallerClassName() + "\u001B[0m\t:",
                String.format(text, values)
        );

    }

    /**
     * Various logging levels
     */
    public enum LogLevel {

        INFO("INFO", "\u001B[32m"),
        DEBUG("DEBUG", "\u001B[32m"),
        WARN("WARN", "\u001B[33m"),
        ERROR("ERROR", "\u001B[31m");

        public String label;
        public String colour;

        LogLevel(final String label, final String colour) {
            this.label = label;
            this.colour = colour;
        }
    }

}
