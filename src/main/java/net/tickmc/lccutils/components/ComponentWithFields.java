package net.tickmc.lccutils.components;

import net.tickmc.lccutils.utilities.GeneralUtilities;
import net.tickmc.lccutils.utilities.MarkdownUtilities;

import java.util.Arrays;

/**
 * A component with fields. Generally Mythic mechanics, conditions, or targeters.
 * In Mythic, fields are a properties of MythicLineConfigs, which are a map of strings to strings, where the key is the title of the field and the value is the value of the field.
 */
public abstract class ComponentWithFields extends LccComponent<ComponentWithFields> {
    /**
     * Represents a field in a {@link ComponentWithFields}.
     * When constructing a field, do not set the examples, category, or author. They will be ignored.
     * Unlike {@link LccComponent}s, fields have a default value.
     */
    public static class ComponentField extends LccComponent<ComponentField> {

        @Override
        public void onEnable() {
        }

        @Override
        public void onDisable() {
        }

        /**
         * The default value of the field.
         */
        private String defaultValue;

        /**
         * Gets the default value of the field.
         *
         * @see #defaultValue
         */
        public String getDefaultValue() {
            return defaultValue;
        }

        /**
         * Sets the default value of the field.
         *
         * @param defaultValue The default value of the field.
         */
        public ComponentField setDefaultValue(String defaultValue) {
            this.defaultValue = defaultValue;
            return this;
        }
    }

    /**
     * A list of fields that this component has.
     */
    public ComponentField[] fields = new ComponentField[0];

    /**
     * Sets the fields of the component. This will override any previous examples. If you don't want to override the previous examples, use {@link #addFields(ComponentField...)}.
     *
     * @param fields The fields to set.
     * @see #fields
     */
    public void setFields(ComponentField[] fields) {
        this.fields = fields;
    }

    /**
     * Adds fields to the component. This will not override any previous fields. If you want to override the previous fields, use {@link #setFields(ComponentField[])}.
     *
     * @param fields The fields to add.
     * @see #fields
     */
    public void addFields(ComponentField... fields) {
        this.fields = GeneralUtilities.include(this.fields, fields);
    }

    @Override
    public String generateMarkdownEntry() {
        return super.generateMarkdownEntry() + System.lineSeparator() + System.lineSeparator() + this.generateDocsFields();
    }

    private String generateDocsFields() {
        return MarkdownUtilities.h3 + "Fields" + MarkdownUtilities.SEPARATOR +
            MarkdownUtilities.unorderedList(Arrays.stream(this.fields).map(field -> {
                    StringBuilder fieldBuilder = new StringBuilder();
                    fieldBuilder.append(field.getNames().stream().map(MarkdownUtilities::code).reduce((a, b) -> a + ", " + b).orElse(""))
                        .append(" - ")
                        .append(field.getDescription());
                    if (field.defaultValue != null) {
                        fieldBuilder.append(System.lineSeparator())
                            .append("  Default value: ")
                            .append(MarkdownUtilities.code(field.defaultValue));
                    }
                    return fieldBuilder.toString();
                }
            ).toArray(String[]::new));
    }
}
