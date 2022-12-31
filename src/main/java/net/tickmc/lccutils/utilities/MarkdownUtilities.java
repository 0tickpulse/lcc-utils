package net.tickmc.lccutils.utilities;

import java.util.Arrays;

/**
 * A class containing utilities for markdown.
 *
 * @author 0TickPulse
 */
public class MarkdownUtilities {
    /**
     * The character used to indicate a first-level header. This should only be used once per document.
     */
    public static final String h1 = "# ";
    /**
     * The character used to indicate a second-level header.
     */
    public static final String h2 = "## ";
    /**
     * The character used to indicate a third-level header.
     */
    public static final String h3 = "### ";
    /**
     * The character used to indicate a fourth-level header.
     */
    public static final String h4 = "#### ";
    /**
     * The character used to indicate a fifth-level header.
     */
    public static final String h5 = "##### ";
    /**
     * The character used to indicate a sixth-level header.
     */
    public static final String h6 = "###### ";

    public static final String horizontalRule = "----------";

    /** System-dependent line separator. */
    public static final String SEPARATOR = System.lineSeparator().repeat(2);

    /**
     * Returns a string with the specified text in bold.
     *
     * @param text The text to make bold.
     */
    public static String bold(String text) {
        return "**" + text + "**";
    }

    /**
     * Returns a string with the specified text in italics.
     *
     * @param text The text to italicize.
     */
    public static String italic(String text) {
        return "_" + text + "_";
    }

    /**
     * Returns a string with the specified text in strikethrough.
     *
     * @param text The text to strikethrough.
     */
    public static String strikethrough(String text) {
        return "~~" + text + "~~";
    }

    /**
     * Returns a string with the specified text in inline code.
     *
     * @param text The text to make as inline code.
     */
    public static String code(String text) {
        return "`" + text + "`";
    }

    /**
     * Returns a string with the specified text in a code block.
     *
     * @param text The text to make as a code block.
     * @see #codeBlock(String, String)
     */
    public static String codeBlock(String text) {
        return codeBlock(text, "plaintext");
    }

    /**
     * Returns a string with the specified text in a code block with the specified language.
     *
     * @param text     The text to make as a code block.
     * @param language The language of the code block.
     */
    public static String codeBlock(String text, String language) {
        return "```" + language + System.lineSeparator() + text + System.lineSeparator() + "```";
    }

    /**
     * Returns a string with a link to the specified URL with the specified text.
     *
     * @param text The text to display as the link.
     * @param url  The URL to link to.
     */
    public static String link(String text, String url) {
        return "[" + text + "](" + url + ")";
    }

    /**
     * Returns a string with a link to the specified image with the specified alt text.
     *
     * @param text The text to display as the alt text.
     * @param url  The URL of the image to link to.
     */
    public static String image(String text, String url) {
        return "!" + link(text, url);
    }

    /**
     * Returns a string with the specified text in a quote.
     *
     * @param text The text to make as a quote.
     */
    public static String quote(String text) {
        return "> " + text;
    }

    /**
     * Returns a string with the specified text in a quote with an author.
     *
     * @param text   The text to make as a quote.
     * @param author The author of the quote.
     */
    public static String quote(String text, String author) {
        return quote(text) + " - " + author;
    }

    /**
     * Returns an ordered list with the specified items.
     *
     * @param items The items to include in the list.
     */
    public static String orderedList(String[] items) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < items.length; i++) {
            builder.append(i + 1).append(". ").append(items[i]).append(System.lineSeparator());
        }
        return builder.toString();
    }

    /**
     * Returns an unordered list with the specified items.
     *
     * @param items The items to include in the list.
     */
    public static String unorderedList(String[] items) {
        return Arrays.stream(items).map(item -> "- " + item + System.lineSeparator()).reduce("", String::concat);
    }

    /**
     * In markdown, a single newline is ignored. This method joins the specified strings with two newlines,
     * which will be interpreted by markdown as separate paragraphs.
     * In addition, it filters out strings that are empty.
     *
     * @param strings The strings to join.
     */
    public static String join(String... strings) {
        return Arrays.stream(strings).filter(s -> !s.isEmpty()).reduce("", (s1, s2) -> s1 + SEPARATOR + s2);
    }
}
