package net.tickmc.lccutils.utilities;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * A class containing general utilities that can be used in any part of the plugin.
 */
public class GeneralUtilities {

    public static <T> T fallback(@Nullable T value, T fallback) {
        return value == null ? fallback : value;
    }

    /**
     * Returns a copy of the array with the specified value added to the end.
     *
     * @param array    The array to add to.
     * @param newItems The items to add to the array.
     */
    public static <T> T[] include(@NonNull T[] array, @NonNull T... newItems) {
        List<T> list = new ArrayList<>(Arrays.asList(array));
        list.addAll(Arrays.asList(newItems));
        return list.toArray(array);
    }

    /**
     * Returns whether the array contains the specified value.
     *
     * @param array The array to search.
     * @param item  The item to search for.
     */
    public static <T> boolean contains(T[] array, T item) {
        return Arrays.asList(array).contains(item);
    }

    /**
     * Returns whether the array contains the specified value, case-insensitive.
     *
     * @param array The array to search.
     * @param item  The item to search for.
     */
    public static boolean containsIgnoreCase(String[] array, String item) {
        for (String string : array) {
            if (string.equalsIgnoreCase(item)) {
                return true;
            }
        }
        return false;
    }

    public static boolean containsIgnoreCase(Collection<String> list, String item) {
        for (String string : list) {
            if (string.equalsIgnoreCase(item)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Indents a string by a specified number of spaces.
     *
     * @param text   The text to indent.
     * @param indent The number of spaces to indent by.
     */
    public static String indent(String text, int indent) {
        return Arrays.stream(text.split(System.lineSeparator())).map(line -> " ".repeat(indent) + line).reduce("", String::concat);
    }

    /**
     * Returns a copy of the string that, if its length is greater than the specified length, is truncated to the specified length and concatenated with "...".
     *
     * @param text      The text to truncate.
     * @param maxLength The maximum length of the string.
     */
    public static String overflow(String text, int maxLength) {
        return text.length() > maxLength ? text.substring(0, maxLength - 3) + "..." : text;
    }

    /**
     * Trims a string to only have the characters from a certain character set.
     *
     * @param string       The string to trim.
     * @param characterSet The character set to trim to.
     */
    public static String trimToCharacterSet(String string, String characterSet) {
        StringBuilder builder = new StringBuilder();
        for (char character : string.toCharArray()) {
            if (characterSet.contains(String.valueOf(character))) {
                builder.append(character);
            }
        }
        return builder.toString();
    }
}
