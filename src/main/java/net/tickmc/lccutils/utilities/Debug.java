package net.tickmc.lccutils.utilities;

import net.tickmc.lccutils.LccUtils;

import java.awt.*;
import java.util.Arrays;
import java.util.logging.Level;

public class Debug {

    /**
     * Returns a string that sets the foreground color of console text.
     *
     * @param color The hex color to set the foreground to.
     */
    public static String foregroundColor(Color color) {
        return "\u001B[38;2;" + (color.getRed()) + ";" + (color.getGreen()) + ";" + (color.getBlue()) + "m";
    }

    /**
     * Returns a string that sets the background color of console text.
     *
     * @param color The hex color to set the background to.
     */
    public static String backgroundColor(Color color) {
        return "\u001B[48;2;" + (color.getRed()) + ";" + (color.getGreen()) + ";" + (color.getBlue()) + "m";
    }

    public static String formatError(String message) {
        return foregroundColor(new Color(0xFF0000))
            + "An unexpected error occured!"
            + foregroundColor(new Color(0xFFFFFF))
            + System.lineSeparator()
            + message;
    }

    public static String formatException(Throwable e) {
        return foregroundColor(new Color(0xFF0000))
            + "An internal exception occured!"
            + foregroundColor(new Color(0xFFFFFF))
            + System.lineSeparator()
            + e.getLocalizedMessage()
            + System.lineSeparator()
            + Arrays.stream(e.getStackTrace()).map(StackTraceElement::toString).reduce("", (a, b) -> a + System.lineSeparator() + b);
    }

    public static void error(String message) {
        LccUtils.getPlugin().getLogger().log(
            Level.SEVERE,
            formatError(message)
        );
    }

    public static void error(Throwable e) {
        LccUtils.getPlugin().getLogger().log(
            Level.SEVERE,
            formatException(e)
        );
    }

    public static void log(String message) {
        LccUtils.getPlugin().getLogger().log(
            Level.INFO,
            message
        );
    }

    public static void devLog(String message) {
        if (LccUtils.debug) {
            LccUtils.getPlugin().getLogger().log(
                Level.INFO,
                foregroundColor(new Color(0x00FF00))
                    + "[DEV] "
                    + foregroundColor(new Color(0xFFFFFF))
                    + message
            );
        }
    }
}
