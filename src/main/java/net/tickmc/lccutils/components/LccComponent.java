package net.tickmc.lccutils.components;

import net.kyori.adventure.text.Component;
import net.tickmc.lccutils.documentation.DocumentationGenerator;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Represents a component of the plugin. A component is a part of the plugin that can be enabled or disabled.
 * This should be extended by most applicable parts of the plugin.
 * In the constructor of your new component, you should set the metadata of the component, such as the title and description.
 * To make the workflow easier, you can create other abstract classes that extend this class but have some metadata already filled out.
 *
 * @author 0TickPulse
 */
public abstract class LccComponent<T extends LccComponent<?>> {
    /**
     * The names of the component.
     * This will be displayed in the {@code /components} command, the documentation, and more.
     * Keep in mind that in some places, the first element of this set will be used as the main title,
     * and the rest will be used as aliases.
     *
     * @apiNote Should be ordered.
     */
    private @NotNull Set<String> names = new LinkedHashSet<>();
    /**
     * A short description of the component to be displayed in Markdown.
     * This will be displayed in the documentation, and more.
     * It should display information on what the component does, and any other important information.
     */
    private String markdownDescription = "";
    /**
     * A short description of the component to be displayed in Minecraft.
     * This will be displayed in the {@code /components} command, and more.
     * It should display information on what the component does, and any other important information.
     *
     * @apiNote Methods that use this field should support MiniMessage.
     */
    private String minecraftDescription = "";

    /**
     * The author of the component. You should write your title here.
     * This will be displayed in the {@code /components} command, the documentation, and more.
     */
    private Set<String> authors = new LinkedHashSet<>();
    /**
     * Any examples of how to use the component. This should be in the form of a code snippet.
     * This will be displayed in the {@code /components} command, the documentation, and more.
     */
    private Set<String> examples = new LinkedHashSet<>();
    /**
     * Any additional components that provides additional information about the component.
     * This will be displayed in the {@code /components} command, the documentation, and more.
     */
    private Set<Class<? extends LccComponent<?>>> seeAlso = new LinkedHashSet<>();
    /**
     * If set to true, the component will not generate documentation.
     */
    private boolean ignoreDocumentation = false;
    /**
     * The category of the component. This will be used for sorting as well as documentation sections.
     */
    private @NotNull ComponentCategory category = ComponentCategory.MISCELLANEOUS;

    /**
     * Gets the names of the component.
     *
     * @see #names
     */
    @NotNull
    public Set<String> getNames() {
        return names;
    }

    /**
     * Sets the names of the component. This will override any previous names. If you don't want to override the previous names, use {@link #addNames(String...)}.
     *
     * @param names The names to set.
     * @see #names
     */
    public T setNames(String... names) {
        this.names = new LinkedHashSet<>(Arrays.asList(names));
        return (T) this;
    }

    /**
     * Adds names to the component. This will not override any previous names. If you want to override the previous names, use {@link #setNames(String[])}.
     *
     * @param names The names to add.
     * @see #names
     */
    public T addNames(String... names) {
        this.names.addAll(Arrays.asList(names));
        return (T) this;
    }

    /**
     * Gets the Markdown description of the component.
     *
     * @see #markdownDescription
     */
    public String getMarkdownDescription() {
        return markdownDescription;
    }

    /**
     * Sets the Markdown description of the component.
     *
     * @param markdownDescription The description to set.
     * @see #markdownDescription
     */
    public T setMarkdownDescription(String markdownDescription) {
        this.markdownDescription = markdownDescription;
        return (T) this;
    }

    /**
     * Gets the Minecraft description of the component.
     *
     * @see #minecraftDescription
     */
    public String getMinecraftDescription() {
        return minecraftDescription;
    }

    /**
     * Sets the Minecraft description of the component.
     *
     * @param minecraftDescription The description to set.
     * @see #minecraftDescription
     */
    public T setMinecraftDescription(String minecraftDescription) {
        this.minecraftDescription = minecraftDescription;
        return (T) this;
    }

    /**
     * Sets both the Minecraft and Markdown descriptions of the component.
     *
     * @param description The description to set.
     * @see #minecraftDescription
     * @see #markdownDescription
     */
    public T setDescription(String description) {
        this.minecraftDescription = description;
        this.markdownDescription = description;
        return (T) this;
    }

    /**
     * Gets the authors of the component.
     *
     * @see #authors
     */
    public Set<String> getAuthors() {
        return authors;
    }

    /**
     * Sets the author of the component. This will override any previous authors. If you don't want to override the previous authors, use {@link #addAuthors(String...)}.
     *
     * @param authors The author to set.
     * @see #authors
     */
    public T setAuthors(Set<String> authors) {
        this.authors = authors;
        return (T) this;
    }

    /**
     * Adds authors to the component. This will not override any previous authors. If you want to override the previous authors (please don't), use .
     *
     * @param authors The authors to add.
     * @see #authors
     */
    public T addAuthors(String... authors) {
        this.authors.addAll(Arrays.asList(authors));
        return (T) this;
    }

    /**
     * Gets the examples of the component.
     *
     * @see #examples
     */
    public Set<String> getExamples() {
        return examples;
    }

    /**
     * Sets the examples of the component. This will override any previous examples. If you don't want to override the previous examples, use {@link #addExamples(String...)}.
     *
     * @param examples The examples to set.
     * @see #examples
     */
    public T setExamples(String[] examples) {
        this.examples = new LinkedHashSet<>(Arrays.asList(examples));
        return (T) this;
    }

    /**
     * Adds examples to the component. This will not override any previous examples. If you want to override the previous examples, use {@link #setExamples(String[])}.
     *
     * @param examples The examples to add.
     * @see #examples
     */
    public T addExamples(String... examples) {
        this.examples.addAll(Arrays.asList(examples));
        return (T) this;
    }

    /**
     * Gets the additional see-also of the component.
     *
     * @see #seeAlso
     */
    public Set<Class<? extends LccComponent<?>>> getSeeAlso() {
        return seeAlso;
    }

    /**
     * Sets the see also components of the component. This will override any previous see also components. If you don't want to override the previous see also components, use {@link #addSeeAlso(Class...)}.
     *
     * @param seeAlso The see also components to set.
     * @see #seeAlso
     */
    public T setSeeAlso(Class<? extends LccComponent<?>>[] seeAlso) {
        this.seeAlso = new LinkedHashSet<>(Arrays.asList(seeAlso));
        return (T) this;
    }

    /**
     * Adds see also components to the component. This will not override any previous see also components. If you want to override the previous see also components, use {@link #setSeeAlso(Class[])}.
     *
     * @param seeAlso The see also components to add.
     * @see #seeAlso
     */
    @SafeVarargs
    public final T addSeeAlso(Class<? extends LccComponent<?>>... seeAlso) {
        this.seeAlso.addAll(Arrays.asList(seeAlso));
        return (T) this;
    }

    /**
     * Gets whether the component should be ignored by the documentation generator.
     *
     * @see #ignoreDocumentation
     */
    public boolean isIgnoreDocumentation() {
        return ignoreDocumentation;
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
     * Gets the category of this component.
     *
     * @see #category
     */
    @NotNull
    public ComponentCategory getCategory() {
        return category;
    }

    /**
     * Sets the category and returns the component.
     *
     * @param category The category to set.
     * @see #category
     */
    public T setCategory(ComponentCategory category) {
        this.category = category;
        return (T) this;
    }

    /**
     * Returns the first title of the component. As mentioned in the documentation for {@link #names}, the first element of this array will be used as the main title.
     *
     * @see #names
     */
    public String getName() {
        return names.iterator().next();
    }

    /**
     * Returns the aliases of the component. As mentioned in the documentation for {@link #names}, the elements of this array, excluding the first, will be used as aliases.
     *
     * @see #names
     */
    public String[] getAliases() {
        return names.stream().skip(1).toArray(String[]::new);
    }

    /**
     * Generates a comma-separated string of authors.
     */
    public String getAuthorsString() {
        return String.join(", ", authors);
    }

    /**
     * A method that is called when the component is registered.
     */
    public abstract void onEnable();

    /**
     * A method that is called when the component is unregistered.
     */
    public abstract void onDisable();

    public String generateMarkdownEntry() {
        return DocumentationGenerator.Markdown.getInstance().generate(this);
    }

    public Component generateMinecraftEntry() {
        return DocumentationGenerator.Minecraft.getInstance().generate(this);
    }
}
