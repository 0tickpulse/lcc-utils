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

    public static class MarkdownDocumentationGenerator extends DocumentationGenerator<String> {

        private static MarkdownDocumentationGenerator instance;

        public static MarkdownDocumentationGenerator getInstance() {
            if (instance == null) {
                instance = new MarkdownDocumentationGenerator();
            }
            return instance;
        }

        private MarkdownDocumentationGenerator() {
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

        @Override
        public String title(LccComponent<?> component) {
            return MarkdownUtilities.h3 + component.getCategory().getReadableName() + ": `" + component.getName() + "`";
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
            return component.getDescription();
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
            return "Author(s): " + String.join(", ", component.getAuthors());
        }

        @Override
        public String seeAlso(LccComponent<?> component) {
            if (component.getSeeAlso().size() == 0) {
                return "";
            }
            return MarkdownUtilities.h4 + "See also" + MarkdownUtilities.SEPARATOR + MarkdownUtilities.unorderedList(component.getSeeAlso().stream().map(clazz -> {
                try {
                    return link(clazz.getConstructor().newInstance());
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                         NoSuchMethodException e) {
                    Debug.error("Failed to instantiate component " + clazz.getName() + " for see also list!");
                    return "";
                }
            }).filter(Objects::nonNull).toArray(String[]::new));
        }

        @Override
        public String link(LccComponent<?> component) {
            String title = title(component).replaceFirst(MarkdownUtilities.h3, "");
            return MarkdownUtilities.link(title, "#" + GeneralUtilities.trimToCharacterSet(title.toLowerCase().replace(' ', '-'), "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ123456789_-#"));
        }
    }

    public static class MinecraftDocumentationGenerator extends DocumentationGenerator<Component> {

        private static MinecraftDocumentationGenerator instance;

        public static MinecraftDocumentationGenerator getInstance() {
            if (instance == null) {
                instance = new MinecraftDocumentationGenerator();
            }
            return instance;
        }

        private MinecraftDocumentationGenerator() {
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
            return Component.text(component.getCategory().getReadableName() + ": " + component.getName()).color(TextColor.color(0x0080FF));
        }

        @Override
        public Component aliases(LccComponent<?> component) {
            String[] aliases = component.getAliases();
            if (aliases.length > 0) {
                return Component.text("Aliases: " + Arrays.stream(aliases).reduce((s, s2) -> s + ", " + s2).orElse("")).color(TextColor.color(0x6E6E6E));
            }
            return null;
        }

        @Override
        public Component description(LccComponent<?> component) {
            return Component.text(component.getDescription()).color(TextColor.color(0xC1C1C1));
        }

        @Override
        public Component examples(LccComponent<?> component) {
            if (component.getExamples().size() == 0) {
                return null;
            }
            TextComponent textComponent = Component.text("Examples").decoration(TextDecoration.BOLD, TextDecoration.State.TRUE).color(TextColor.color(0x0080FF)).append(Component.newline()).append(Component.newline());
            Optional<TextComponent> exampleText = component.getExamples().stream().map(s ->
                Component.text(s).decoration(TextDecoration.BOLD, TextDecoration.State.FALSE).color(TextColor.color(0x87BBBB))
            ).reduce((s, s2) -> s.append(Component.newline()).append(Component.newline()).append(s2));
            return exampleText.map(textComponent::append).orElse(textComponent);
        }

        @Override
        public Component authors(LccComponent<?> component) {
            return Component.text("Author: ").color(TextColor.color(0x6E6E6E))
                .append(Component.text(String.join(", ", component.getAuthors())).color(TextColor.color(0x0080FF)));
        }

        @Override
        public Component seeAlso(LccComponent<?> component) {
            if (component.getSeeAlso().size() == 0) {
                return null;
            }
            TextComponent textComponent = Component.text("See also").decoration(TextDecoration.BOLD, TextDecoration.State.TRUE).color(TextColor.color(0x0080FF)).append(Component.newline()).append(Component.newline());
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
            Component title = MinecraftDocumentationGenerator.getInstance().title(component);
            return title
                .color(TextColor.color(0x87BBBB))
                .hoverEvent(HoverEvent.showText(Component.text(GeneralUtilities.overflow(component.getDescription(), 350))))
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
