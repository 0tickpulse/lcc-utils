package net.tickmc.lccutils.components;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.tickmc.lccutils.utilities.ComponentUtilities;
import net.tickmc.lccutils.utilities.GeneralUtilities;
import net.tickmc.lccutils.utilities.MarkdownUtilities;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Optional;

/**
 * Represents a component of the plugin. A component is a part of the plugin that can be enabled or disabled.
 * This should be extended by most applicable parts of the plugin.
 * In the constructor of your new component, you should set the metadata of the component, such as the name and description.
 * To make the workflow easier, you can create other abstract classes that extend this class but have some metadata already filled out.
 *
 * @author 0TickPulse
 */
public abstract class LccComponent<T extends LccComponent<?>> {
    /**
     * The names of the component.
     * This will be displayed in the {@code /components} command, the documentation, and more.
     * Keep in mind that in some places, the first element of this array will be used as the main name,
     * and the rest will be used as aliases.
     */
    public @NotNull String[] names = new String[0];
    /**
     * A short description of the component.
     * This will be displayed in the {@code /components} command, the documentation, and more.
     * It should display information on what the component does, and any other important information.
     */
    public String description = "";
    /**
     * The author of the component. You should write your name here.
     * This will be displayed in the {@code /components} command, the documentation, and more.
     */
    public String author = "";
    /**
     * Any examples of how to use the component. This should be in the form of a code snippet.
     * This will be displayed in the {@code /components} command, the documentation, and more.
     */
    public String[] examples = new String[0];
    /**
     * If set to true, the component will not generate documentation.
     */
    public boolean ignoreDocumentation = false;

    /**
     * Sets the names of the component. This will override any previous names. If you don't want to override the previous names, use {@link #addNames(String...)}.
     *
     * @param names The names to set.
     * @see #names
     */
    public T setNames(String... names) {
        this.names = names;
        return (T) this;
    }

    /**
     * Adds names to the component. This will not override any previous names. If you want to override the previous names, use {@link #setNames(String[])}.
     *
     * @param names The names to add.
     * @see #names
     */
    public T addNames(String... names) {
        this.names = GeneralUtilities.include(this.names, names);
        return (T) this;
    }

    /**
     * Sets the description of the component.
     *
     * @param description The description to set.
     * @see #description
     */
    public T setDescription(String description) {
        this.description = description;
        return (T) this;
    }

    /**
     * Sets the author of the component.
     *
     * @param author The author to set.
     * @see #author
     */
    public T setAuthor(String author) {
        this.author = author;
        return (T) this;
    }

    /**
     * Sets the examples of the component. This will override any previous examples. If you don't want to override the previous examples, use {@link #addExamples(String...)}.
     *
     * @param examples The examples to set.
     * @see #examples
     */
    public T setExamples(String[] examples) {
        this.examples = examples;
        return (T) this;
    }

    /**
     * Adds examples to the component. This will not override any previous examples. If you want to override the previous examples, use {@link #setExamples(String[])}.
     *
     * @param examples The examples to add.
     * @see #examples
     */
    public T addExamples(String... examples) {
        this.examples = GeneralUtilities.include(this.examples, examples);
        return (T) this;
    }

    /**
     * Sets whether the component should be ignored when generating documentation.
     *
     * @param ignoreDocumentation Whether the component should be ignored when generating documentation.
     * @see #ignoreDocumentation
     */
    public T setIgnoreDocumentation(boolean ignoreDocumentation) {
        this.ignoreDocumentation = ignoreDocumentation;
        return (T) this;
    }

    /**
     * This should return a string containing the documentation for the component.
     * This will be displayed in the {@code /components} command, the documentation, and more.
     */
    public abstract @NotNull String getCategory();

    /**
     * Returns the first name of the component. As mentioned in the documentation for {@link #names}, the first element of this array will be used as the main name.
     *
     * @see #names
     */
    public String getName() {
        return names[0];
    }

    /**
     * Returns the aliases of the component. As mentioned in the documentation for {@link #names}, the elements of this array, excluding the first, will be used as aliases.
     *
     * @see #names
     */
    public String[] getAliases() {
        return Arrays.copyOfRange(names, 1, names.length);
    }

    /**
     * Returns a markdown-formatted string containing the documentation for the component. In most cases, this should not be overridden, but if you want extra control, it is possible.
     */
    public String generateMarkdownEntry() {
        return MarkdownUtilities.join(
                generateMarkdownName(),
                generateMarkdownAliases(),
                generateMarkdownDescription(),
                generateMarkdownAuthor(),
                generateMarkdownExamples()
        );
    }

    public Component generateMinecraftEntry() {
        return ComponentUtilities.joinComponentsAndCompress(
                ComponentUtilities.hline,
                Component.newline(),
                generateMinecraftName(),
                Component.newline(),
                generateMinecraftAliases(),
                Component.newline(),
                Component.newline(),
                generateMinecraftDescription(),
                Component.newline(),
                Component.newline(),
                generateMinecraftExamples(),
                Component.newline(),
                Component.newline(),
                generateMinecraftAuthor(),
                Component.newline(),
                ComponentUtilities.hline);
    }

    public String generateMarkdownName() {
        return MarkdownUtilities.h3 + getCategory() + ": `" + getName() + "`";
    }

    public @Nullable Component generateMinecraftName() {
        return Component.text(getCategory() + ": " + getName()).color(TextColor.color(0x0080FF));
    }

    public String generateMarkdownAliases() {
        String[] aliases = getAliases();
        if (aliases.length > 0) {
            return "Aliases: " + Arrays.stream(aliases).map(s -> "`" + s + "`").reduce((s, s2) -> s + ", " + s2).orElse("");
        }
        return "";
    }

    public @Nullable Component generateMinecraftAliases() {
        String[] aliases = getAliases();
        if (aliases.length > 0) {
            return Component.text("Aliases: " + Arrays.stream(aliases).reduce((s, s2) -> s + ", " + s2).orElse("")).color(TextColor.color(0x6E6E6E));
        }
        return null;
    }

    public String generateMarkdownDescription() {
        return description;
    }

    public @Nullable Component generateMinecraftDescription() {
        return Component.text(description).color(TextColor.color(0xC1C1C1));
    }

    public String generateMarkdownExamples() {
        if (examples.length > 0) {
            return MarkdownUtilities.h4 + "Examples" + MarkdownUtilities.SEPARATOR + Arrays.stream(examples).map(s -> MarkdownUtilities.codeBlock(s, "yaml")).reduce((s, s2) -> s + System.lineSeparator() + System.lineSeparator() + s2).orElse("");
        }
        return "";
    }

    public @Nullable Component generateMinecraftExamples() {
        if (examples.length > 0) {
            TextComponent component = Component.text("Examples").decoration(TextDecoration.BOLD, TextDecoration.State.TRUE).color(TextColor.color(0x0080FF)).append(Component.newline()).append(Component.newline());
            Optional<TextComponent> exampleText = Arrays.stream(examples).map(s ->
                    Component.text(s).decoration(TextDecoration.BOLD, TextDecoration.State.FALSE).color(TextColor.color(0x87BBBB))
            ).reduce((s, s2) -> s.append(Component.newline()).append(Component.newline()).append(s2));
            return exampleText.map(component::append).orElse(component);
        }
        return null;
    }

    public String generateMarkdownAuthor() {
        return "Author: " + author;
    }

    public @Nullable Component generateMinecraftAuthor() {
        return Component.text("Author: ").color(TextColor.color(0x6E6E6E))
                .append(Component.text(author).color(TextColor.color(0x0080FF)));
    }

    /**
     * Generates a link to the documentation page for this component for use in markdown.
     */
    public static String getMarkdownLink(LccComponent<?> component) {
        String title = component.generateMarkdownName().replaceFirst(MarkdownUtilities.h3, "");
        return MarkdownUtilities.link(title, "#" + GeneralUtilities.trimToCharacterSet(title.toLowerCase().replace(' ', '-'), "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ123456789_-#"));
    }

    public String getMarkdownLink() {
        return getMarkdownLink(this);
    }

    /**
     * A method that is called when the component is registered.
     */
    public abstract void onEnable();

    /**
     * A method that is called when the component is unregistered.
     */
    public abstract void onDisable();
}
