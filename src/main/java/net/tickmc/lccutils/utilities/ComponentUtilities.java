package net.tickmc.lccutils.utilities;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.jetbrains.annotations.Nullable;

public class ComponentUtilities {
    public static Component hline = Component.text(" ".repeat(80)).color(TextColor.color(0x232323)).decorate(TextDecoration.STRIKETHROUGH);

    public static Component joinComponents(@Nullable Component... components) {
        Component component = Component.empty();
        for (Component c : components) {
            if (c != null) {
                component = component.append(c);
            }
        }
        return component;
    }

    public static Component joinComponentsAndCompress(@Nullable Component... components) {
        Component component = Component.empty();
        for (Component c : components) {
            if (c == null) {
                continue;
            }
            if (component.children().size() < 2) {
                component = component.append(c);
                continue;
            }
            if (component.children().get(component.children().size() - 1).equals(Component.newline()) &&
                    component.children().get(component.children().size() - 2).equals(Component.newline())) {
                if (c.equals(Component.newline())) {
                    continue;
                }
            }

            component = component.append(c);
        }
        return component;
    }

    /**
     * Compresses a component, replacing any >2 line breaks in a row with two line breaks.
     *
     * @param component The component to compress
     */
    public static Component compress(Component component) {
        return component.replaceText(TextReplacementConfig.builder().match("\n{3,}").replacement("\n\n").build());
    }
}
