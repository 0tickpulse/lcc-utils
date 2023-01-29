package net.tickmc.lccutils.documentation;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.tickmc.lccutils.components.LccComponent;
import net.tickmc.lccutils.utilities.ComponentUtilities;
import net.tickmc.lccutils.utilities.Debug;
import net.tickmc.lccutils.utilities.GeneralUtilities;
import net.tickmc.lccutils.utilities.MarkdownUtilities;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

public abstract class DocumentationGenerator<T> {

    public static String linkMarkdown(LccComponent<?> component) {
        return Markdown.getInstance().link(component);
    }

    public static class Markdown extends DocumentationGenerator<String> {

        private static Markdown instance;

        public static Markdown getInstance() {
            if (instance == null) {
                instance = new Markdown();
            }
            return instance;
        }

        private Markdown() {
        }

        @Override
        public String generate(LccComponent<?> component) {
            return MarkdownUtilities.join(
                title(component),
                aliases(component),
                description(component),
                examples(component),
                seeAlso(component),
                authors(component)
            );
        }

        public String typeAndName(LccComponent<?> component) {
            return component.getCategory().getReadableName() + ": `" + component.getName() + "`";
        }

        @Override
        public String title(LccComponent<?> component) {
            return MarkdownUtilities.h3 + typeAndName(component);
        }

        @Override
        public String aliases(LccComponent<?> component) {
            String[] aliases = component.getAliases();
            if (aliases.length > 0) {
                return "Aliases: " + Arrays.stream(aliases).map(s -> "`" + s + "`").reduce((s, s2) -> s + ", " + s2).orElse("");
            }
            return "";
        }

        @Override
        public String description(LccComponent<?> component) {
            return component.getMarkdownDescription();
        }

        @Override
        public String examples(LccComponent<?> component) {
            if (component.getExamples().size() > 0) {
                return MarkdownUtilities.h4 + "Examples" + MarkdownUtilities.SEPARATOR + component.getExamples().stream().map(s -> MarkdownUtilities.codeBlock(s, "yaml")).reduce((s, s2) -> s + System.lineSeparator() + System.lineSeparator() + s2).orElse("");
            }
            return "";
        }

        @Override
        public String authors(LccComponent<?> component) {
            if (component.getAuthors().isEmpty()) {
                return "";
            }
            return "Author(s): " + String.join(", ", component.getAuthors());
        }

        @Override
        public String seeAlso(LccComponent<?> component) {
            if (component.getSeeAlso().size() == 0) {
                return "";
            }
            String[] seeAlso = component.getSeeAlso().stream().map(clazz -> {
                try {
                    return link(clazz.getConstructor().newInstance());
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                         NoSuchMethodException e) {
                    Debug.error("Failed to instantiate component " + clazz.getName() + " for see also list!");
                    return "";
                }
            }).filter(Objects::nonNull).toArray(String[]::new);
            if (seeAlso.length == 0) {
                return "";
            }
            return MarkdownUtilities.h4 + "See also" + MarkdownUtilities.SEPARATOR + MarkdownUtilities.unorderedList(seeAlso);
        }

        @Override
        public String link(LccComponent<?> component) {
            String title = typeAndName(component);
            return MarkdownUtilities.link(title, "#" + GeneralUtilities.trimToCharacterSet(title.toLowerCase().replace(' ', '-'), "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ123456789_-#"));
        }
    }

    public static class Minecraft extends DocumentationGenerator<Component> {

        private static Minecraft instance;

        public static Minecraft getInstance() {
            if (instance == null) {
                instance = new Minecraft();
            }
            return instance;
        }

        private Minecraft() {
        }

        @Override
        public Component generate(LccComponent<?> component) {
            return ComponentUtilities.joinComponentsAndCompress(
                ComponentUtilities.hline,
                Component.newline(),
                title(component),
                Component.newline(),
                aliases(component),
                Component.newline(),
                Component.newline(),
                description(component),
                Component.newline(),
                Component.newline(),
                examples(component),
                Component.newline(),
                Component.newline(),
                seeAlso(component),
                Component.newline(),
                Component.newline(),
                authors(component),
                Component.newline(),
                ComponentUtilities.hline);
        }

        @Override
        public Component title(LccComponent<?> component) {
            return ComponentUtilities.formatTitle(component.getCategory().getReadableName() + ": " + component.getName());
        }

        @Override
        public Component aliases(LccComponent<?> component) {
            String[] aliases = component.getAliases();
            if (aliases.length > 0) {
                return ComponentUtilities.formatUnimportant("Aliases: " + Arrays.stream(aliases).reduce((s, s2) -> s + ", " + s2).orElse(""));
            }
            return null;
        }

        @Override
        public Component description(LccComponent<?> component) {
            return ComponentUtilities.formatBody(component.getMarkdownDescription());
        }

        @Override
        public Component examples(LccComponent<?> component) {
            if (component.getExamples().size() == 0) {
                return null;
            }
            TextComponent textComponent = (TextComponent) ComponentUtilities.formatTitle("Examples").append(Component.newline()).append(Component.newline());
            Optional<TextComponent> exampleText = component.getExamples().stream().map(s ->
                Component.text(s).decoration(TextDecoration.BOLD, TextDecoration.State.FALSE).color(TextColor.color(0x87BBBB))
            ).reduce((s, s2) -> s.append(Component.newline()).append(Component.newline()).append(s2));
            return exampleText.map(textComponent::append).orElse(textComponent);
        }

        @Override
        public Component authors(LccComponent<?> component) {
            return Component.text("Author: ").color(TextColor.color(0x6E6E6E))
                .append(ComponentUtilities.formatTitle(String.join(", ", component.getAuthors()), false));
        }

        @Override
        public Component seeAlso(LccComponent<?> component) {
            if (component.getSeeAlso().size() == 0) {
                return null;
            }
            TextComponent textComponent = (TextComponent) ComponentUtilities.formatTitle("See also:").append(Component.newline()).append(Component.newline());
            Optional<Component> seeAlsoText = component.getSeeAlso().stream().map(
                    clazz -> {
                        try {
                            return link(clazz.getConstructor().newInstance());
                        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                                 NoSuchMethodException e) {
                            Debug.error("Failed to instantiate component " + clazz.getName() + " for see also list!");
                            return null;
                        }
                    }
                )
                .reduce((s, s2) -> {
                    if (s2 == null) {
                        return s;
                    }
                    return s.append(Component.newline()).append(Component.newline()).append(s2);
                });
            return seeAlsoText.map(textComponent::append).orElse(textComponent);
        }

        @Override
        public Component link(LccComponent<?> component) {
            Component title = Minecraft.getInstance().title(component);
            return title
                .color(TextColor.color(0x87BBBB))
                .hoverEvent(HoverEvent.showText(Component.text(GeneralUtilities.overflow(component.getMarkdownDescription(), 350))))
                .clickEvent(ClickEvent.suggestCommand("/lccutils component " + component.getCategory().getSimpleReadableName() + " " + component.getName().replace(" ", "_")));
        }
    }

    public abstract T generate(LccComponent<?> component);

    public abstract T title(LccComponent<?> component);

    public abstract T aliases(LccComponent<?> component);

    public abstract T description(LccComponent<?> component);

    public abstract T examples(LccComponent<?> component);

    public abstract T authors(LccComponent<?> component);

    public abstract T seeAlso(LccComponent<?> component);

    public abstract T link(LccComponent<?> component);
}
