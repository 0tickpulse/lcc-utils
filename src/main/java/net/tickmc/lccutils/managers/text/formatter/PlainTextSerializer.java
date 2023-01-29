package net.tickmc.lccutils.managers.text.formatter;

import java.util.List;
import java.util.Map;

public class PlainTextSerializer implements Parser.Visitor<String> {

    public String serialize(Parser.Node node) {
        return node.accept(this);
    }

    @Override
    public String visitText(Parser.Node.Text node) {
        return node.stringify();
    }

    @Override
    public String visitParsedText(Parser.Node.ParsedText node) {
        return node.nodes.stream().map(this::serialize).reduce("", String::concat);
    }

    @Override
    public String visitSingularColorCode(Parser.Node.SingularColorCode node) {
        return "";
    }

    @Override
    public String visitHexColorCode(Parser.Node.HexColorCode node) {
        return "";
    }

    @Override
    public String visitMapColorCode(Parser.Node.MapColorCode node) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, List<Parser.Node.ParsedText>> entry : node.map.entrySet()) {
            String key = entry.getKey();
            List<Parser.Node.ParsedText> value = entry.getValue();
            List<String> stringValues = value.stream().map(this::serialize).toList();
            switch (key) {
                case "color", "decoration", "font", "click", "hover" -> {
                }
                case "translate" -> {
                    sb.append(stringValues.get(0));
                }
                case "keybind" -> {
                    sb.append(stringValues.get(0));
                }
                case "insertion" -> {

                }
            }
        }
        return sb.toString();
    }

    @Override
    public String visitEnd(Parser.Node.End node) {
        return "";
    }
}
