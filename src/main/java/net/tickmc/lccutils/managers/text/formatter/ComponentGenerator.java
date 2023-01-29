package net.tickmc.lccutils.managers.text.formatter;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ComponentGenerator implements Parser.Visitor<Void> {

    @FunctionalInterface
    public interface StyleTransformer {
        Style transform(Style style);
    }

    private static final Map<String, TextColor> colorMap = new HashMap<>();
    private static final Map<String, StyleTransformer> styleMap = new HashMap<>();

    static {
        colorMap.put("0", TextColor.color(0, 0, 0));
        colorMap.put("black", TextColor.color(0, 0, 0));
        colorMap.put("1", TextColor.color(0, 0, 170));
        colorMap.put("dark_blue", TextColor.color(0, 0, 170));
        colorMap.put("2", TextColor.color(0, 170, 0));
        colorMap.put("dark_green", TextColor.color(0, 170, 0));
        colorMap.put("3", TextColor.color(0, 170, 170));
        colorMap.put("dark_aqua", TextColor.color(0, 170, 170));
        colorMap.put("4", TextColor.color(170, 0, 0));
        colorMap.put("dark_red", TextColor.color(170, 0, 0));
        colorMap.put("5", TextColor.color(170, 0, 170));
        colorMap.put("dark_purple", TextColor.color(170, 0, 170));
        colorMap.put("6", TextColor.color(255, 170, 0));
        colorMap.put("gold", TextColor.color(255, 170, 0));
        colorMap.put("7", TextColor.color(170, 170, 170));
        colorMap.put("gray", TextColor.color(170, 170, 170));
        colorMap.put("8", TextColor.color(85, 85, 85));
        colorMap.put("dark_gray", TextColor.color(85, 85, 85));
        colorMap.put("9", TextColor.color(85, 85, 255));
        colorMap.put("blue", TextColor.color(85, 85, 255));
        colorMap.put("a", TextColor.color(85, 255, 85));
        colorMap.put("green", TextColor.color(85, 255, 85));
        colorMap.put("b", TextColor.color(85, 255, 255));
        colorMap.put("aqua", TextColor.color(85, 255, 255));
        colorMap.put("c", TextColor.color(255, 85, 85));
        colorMap.put("red", TextColor.color(255, 85, 85));
        colorMap.put("d", TextColor.color(255, 85, 255));
        colorMap.put("light_purple", TextColor.color(255, 85, 255));
        colorMap.put("e", TextColor.color(255, 255, 85));
        colorMap.put("yellow", TextColor.color(255, 255, 85));
        colorMap.put("f", TextColor.color(255, 255, 255));
        colorMap.put("white", TextColor.color(255, 255, 255));
        styleMap.put("k", (style -> style.decorate(TextDecoration.OBFUSCATED)));
        styleMap.put("obfuscated", (style -> style.decorate(TextDecoration.OBFUSCATED)));
        styleMap.put("l", (style -> style.decorate(TextDecoration.BOLD)));
        styleMap.put("bold", (style -> style.decorate(TextDecoration.BOLD)));
        styleMap.put("m", (style -> style.decorate(TextDecoration.STRIKETHROUGH)));
        styleMap.put("strikethrough", (style -> style.decorate(TextDecoration.STRIKETHROUGH)));
        styleMap.put("n", (style -> style.decorate(TextDecoration.UNDERLINED)));
        styleMap.put("underline", (style -> style.decorate(TextDecoration.UNDERLINED)));
        styleMap.put("o", (style -> style.decorate(TextDecoration.ITALIC)));
        styleMap.put("italic", (style -> style.decorate(TextDecoration.ITALIC)));
    }

    private final Parser.Node.ParsedText parsed;
    private Component builder = Component.empty();
    private List<TextDecoration> currentDecorations;
    private List<TextDecoration> previousDecorations;
    private Style currentStyle = Style.empty();
    private Style previousStyle = Style.empty();
    private List<TextColor> gradientColors = null;

    public ComponentGenerator(Parser.Node.ParsedText node) {
        this.parsed = node;
    }

    public Component interpret() {
        int i = 0;
        while (i < parsed.nodes.size()) {
            Parser.Node node = parsed.nodes.get(i);

            if (node instanceof Parser.Node.MapColorCode colorNode && colorNode.map.containsKey("gradient")) {
                int distance = distanceToEndGradient(i);
                int nodeDistance = nodeDistanceToEndGradient(i);
                gradientColors = ColorUtilities.multiGradient(
                    distance,
                    colorNode.map.get("gradient").stream().map(text -> color(new PlainTextSerializer().serialize(text))).toArray(TextColor[]::new)
                );
                // pls dont touch
                // i dont know why it works but it does
                // this code is as unstable as the economy
                int k = 1;
                for (int j = 1; j < distance; k++) {
                    Parser.Node gradientNode = parsed.toSingularCharacters().get(i + k - 1);
                    TextColor color = gradientColors.get(Math.min(k - 1, gradientColors.size() - 1));
                    currentStyle = currentStyle.color(color);
                    gradientNode.accept(this);
                    j += new LengthGetter().getLength(gradientNode);
                }
                i += nodeDistance - 1;
                continue;
            }

            node.accept(this);
            i++;

        }
        return builder;
    }

    @Override
    public Void visitText(Parser.Node.Text node) {
        append(Component.text(node.stringify()));
        return null;
    }

    @Override
    public Void visitParsedText(Parser.Node.ParsedText node) {
        return null;
    }

    @Override
    public Void visitSingularColorCode(Parser.Node.SingularColorCode node) {
        String c = node.code.value();
        if (Objects.equals(c, "r")) {
            currentStyle = Style.empty();
            currentDecorations = null;
        } else if (colorMap.containsKey(c)) {
            currentStyle = currentStyle.color(colorMap.get(c));
        } else if (styleMap.containsKey(c)) {
            currentStyle = styleMap.get(c).transform(currentStyle);
        }
        return null;
    }

    @Override
    public Void visitHexColorCode(Parser.Node.HexColorCode node) {
        currentStyle = currentStyle.color(node.getHex());
        return null;
    }

    @Override
    public Void visitMapColorCode(Parser.Node.MapColorCode node) {
        for (Map.Entry<String, List<Parser.Node.ParsedText>> entry : node.map.entrySet()) {
            String key = entry.getKey();
            List<Parser.Node.ParsedText> value = entry.getValue();
            List<String> stringValues = value.stream().map(n -> new PlainTextSerializer().serialize(n)).toList();
            switch (key) {
                case "color" -> setStyle(currentStyle.color(color(stringValues.get(0))));
                case "decoration" -> currentStyle = styleMap.get(stringValues.get(0)).transform(currentStyle);

                case "font" -> setStyle(currentStyle.font(Key.key(stringValues.get(0))));
                case "click" -> {
                    String action = stringValues.get(0);
                    String value1 = stringValues.get(1);
                    setStyle(currentStyle.clickEvent(ClickEvent.clickEvent(ClickEvent.Action.valueOf(action.toUpperCase()), value1)));
                }

                case "hover" -> {
                    String value1 = stringValues.get(0);
                    setStyle(currentStyle.hoverEvent(HoverEvent.hoverEvent(HoverEvent.Action.SHOW_TEXT, Component.text(value1))));
                }

                case "translate" -> {
                    String key1 = stringValues.get(0);
                    append(Component.translatable(key1));
                }

                case "keybind" -> {
                    String key1 = stringValues.get(0);
                    append(Component.keybind(key1));
                }

                case "insertion" -> {
                    String value1 = stringValues.get(0);
                    setStyle(currentStyle.insertion(value1));
                }
            }
        }

        return null;
    }

    @Override
    public Void visitEnd(Parser.Node.End node) {
        switch (node.node.stringify()) {
            case "color" -> setStyle(currentStyle.color(null));
            case "decoration" -> currentDecorations = null;
            case "font" -> setStyle(currentStyle.font(null));
            case "click" -> setStyle(currentStyle.clickEvent(null));
        }
        return null;
    }

    private Component decorate(Component component) {
        if (currentDecorations != null) {
            for (TextDecoration decoration : currentDecorations) {
                component = component.decoration(decoration, TextDecoration.State.TRUE);
            }
        }
        if (currentStyle != null) {
            component = component.style(currentStyle);
        }
        return component;
    }

    private void addDecoration(TextDecoration decoration) {
        if (currentDecorations == null) {
            currentDecorations = List.of(decoration);
        } else {
            currentDecorations.add(decoration);
        }
    }

    private void setStyle(Style style) {
        previousStyle = currentStyle;
        currentStyle = style;
    }

    private void append(Component component) {
        builder = builder.append(decorate(component));
    }

    private TextColor color(String color) {
        TextColor color1 = colorMap.get(color);
        if (color1 != null) {
            return color1;
        }
        if (color.startsWith("#")) {
            color = color.substring(1);
            int r = Integer.parseInt(color.substring(0, 2), 16);
            int g = Integer.parseInt(color.substring(2, 4), 16);
            int b = Integer.parseInt(color.substring(4, 6), 16);
            return TextColor.color(r, g, b);
        }
        return null;
    }

    private int distanceToEndGradient(int index) {
        int distance = 1;
        for (int i = index; i < parsed.nodes.size(); i++) {
            Parser.Node node = parsed.nodes.get(i);
            if (endGradient(node)) {
                return distance;
            }
            distance += new LengthGetter().getLength(node);
        }
        return distance;
    }

    private boolean endGradient(Parser.Node node) {
        return node instanceof Parser.Node.End end && Objects.equals(end.node.stringify(), "gradient")
            || node instanceof Parser.Node.SingularColorCode colorCode && (ComponentGenerator.colorMap.containsKey(colorCode.code.value()) || Objects.equals(colorCode.code.value(), "r"))
            || node instanceof Parser.Node.HexColorCode;
    }

    private int nodeDistanceToEndGradient(int index) {
        int distance = 1;
        for (int i = index; i < parsed.nodes.size(); i++) {
            Parser.Node node = parsed.nodes.get(i);
            if (endGradient(node)) {
                return distance;
            }
            distance++;
        }
        return distance;
    }
}
