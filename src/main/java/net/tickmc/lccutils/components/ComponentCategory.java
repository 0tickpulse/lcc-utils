package net.tickmc.lccutils.components;

import org.jetbrains.annotations.Nullable;

/**
 * Categories for components.
 * <p>
 * Contributers are free to add their own categories.
 */
public enum ComponentCategory {

    /**
     * Default category for components.
     * Represents a component that cannot conform to any other category and thus is placed in the Miscellaneous category.
     * In other words, the Miscellaneous category can be considered a catch-all category or a fallback category.
     * Use this if:
     * <ul>
     *     <li>You are unsure of which category your component should be placed in.</li>
     *     <li>Your component does not fit into any other category.</li>
     *     <li>Your component can fit into a category, but will be the only component in that category.</li>
     * </ul>
     * Note that components that are in this category are generally recommended to have clearer documentation.
     * The category title does not indicate what the component is, what it does, or what it is used for.
     * Therefore, it is recommended that the component title and documentation be as descriptive as possible,
     * at least more than components of other categories.
     *
     * @see net.tickmc.lccutils.components.miscellaneous.MiscellaneousComponent
     */
    MISCELLANEOUS("Miscellaneous"),

    /**
     * Category for components that are Mythic mechanics.
     * Mechanics are a part of the Mythic skill system and allow the user to execute certain actions.
     *
     * @see net.tickmc.lccutils.components.mechanics.MechanicComponent
     * @see <a href="https://git.lumine.io/mythiccraft/MythicMobs/-/wikis/Skills/Mechanics">Mythic wiki - Mechanics</a>
     */
    MYTHIC_MECHANIC("Mythic Mechanic"),
    /**
     * Category for components that are Mythic conditions.
     * Conditions are a part of the Mythic skill system and allow the user to check for certain conditions before skills are run.
     *
     * @see net.tickmc.lccutils.components.conditions.ConditionComponent
     * @see <a href="https://git.lumine.io/mythiccraft/MythicMobs/-/wikis/Skills/Conditions">Mythic wiki - Conditions</a>
     */
    MYTHIC_CONDITION("Mythic Condition"),
    /**
     * Category for components that are Mythic targeters.
     * Targeters are a part of the Mythic skill system and allow the user to target certain entities or locations.
     *
     * @see net.tickmc.lccutils.components.targeters.TargeterComponent
     * @see <a href="https://git.lumine.io/mythiccraft/MythicMobs/-/wikis/Skills/Targeters">Mythic wiki - Targeters</a>
     */
    MYTHIC_TARGETER("Mythic Targeter"),
    /**
     * Category for components that are Mythic triggers.
     * Triggers are a part of the Mythic skill system and allow the user to denote when a skill occurs.
     * I am currently unsure how to add triggers using the MythicMobs API.
     */
    MYTHIC_TRIGGER("Mythic Trigger"),
    /**
     * Category for components that are Mythic placeholders.
     * Placeholders are a part of the Mythic skill system and allow the user to insert certain values into a string.
     * These placeholders will be replaced with dynamic values at runtime.
     *
     * @see net.tickmc.lccutils.components.mythicplaceholders.MythicPlaceholderComponent
     * @see <a href="https://git.lumine.io/mythiccraft/MythicMobs/-/wikis/Skills/Placeholders">Mythic wiki - Placeholders</a>
     */
    MYTHIC_PLACEHOLDER("Mythic Placeholder"),
    /**
     * Category for components that are PlaceholderAPI placeholders.
     * Placeholders are a part of the PlaceholderAPI system and allow the user to insert certain values into a string.
     * These placeholders will be replaced with dynamic values at runtime.
     *
     * @see net.tickmc.lccutils.components.papi.PAPIPlaceholderComponent
     */
    PAPI_PLACEHOLDER("PAPI Placeholder"),
    /**
     * Category for components that are Minecraft commands.
     * Commands are a part of the Minecraft command system and allow users, consoles, and command blocks to execute certain actions.
     * This plugin uses CommandAPI to register commands.
     *
     * @see net.tickmc.lccutils.components.commands.CommandComponent
     * @see <a href="https://commandapi.jorel.dev/">CommandAPI</a>
     */
    MINECRAFT_COMMAND("Minecraft Command"),

    /**
     * Category for components that are bridges.
     * Bridges are components that allow other plugins to interact with this plugin, essentially creating a "bridge" between the plugins.
     * Not to be confused with the Bridge design pattern.
     *
     * @see net.tickmc.lccutils.components.bridges.BridgeComponent
     */
    BRIDGE("Bridge");

    /**
     * A readable title of the category to be used in documentation.
     */
    private final String readableName;

    public String getReadableName() {
        return readableName;
    }

    /**
     * Returns the readable title, but lowercased and without spaces. For use in file names, commands, etc.
     */
    public String getSimpleReadableName() {
        return readableName.replace(" ", "").toLowerCase();
    }

    /**
     * Gets a category from this enum based on its {@link #readableName}. Returns null if not present.
     *
     * @param name The readable title of the category to search for.
     */
    public @Nullable ComponentCategory get(String name) {
        for (ComponentCategory category : values()) {
            if (category.getReadableName().equalsIgnoreCase(name)) {
                return category;
            }
        }
        return null;
    }

    ComponentCategory(String readableName) {
        this.readableName = readableName;
    }
}
