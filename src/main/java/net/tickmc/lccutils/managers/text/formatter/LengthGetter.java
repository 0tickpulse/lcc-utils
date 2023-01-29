package net.tickmc.lccutils.managers.text.formatter;

import java.util.List;
import java.util.Map;

public class LengthGetter implements Parser.Visitor<Integer> {

    public int getLength(Parser.Node node) {
        return node.accept(this);
    }

    @Override
    public Integer visitText(Parser.Node.Text node) {
        return node.stringify().length();
    }

    @Override
    public Integer visitParsedText(Parser.Node.ParsedText node) {
        return node.nodes.stream().map(this::getLength).reduce(0, Integer::sum);
    }

    @Override
    public Integer visitSingularColorCode(Parser.Node.SingularColorCode node) {
        return 0;
    }

    @Override
    public Integer visitHexColorCode(Parser.Node.HexColorCode node) {
        return 0;
    }

    @Override
    public Integer visitMapColorCode(Parser.Node.MapColorCode node) {
        int length = 0;
        for (Map.Entry<String, List<Parser.Node.ParsedText>> entry : node.map.entrySet()) {
            String key = entry.getKey();
            List<Parser.Node.ParsedText> value = entry.getValue();
            List<Integer> stringValues = value.stream().map(this::getLength).toList();
            switch (key) {
                case "color", "decoration", "font", "click", "hover" -> {
                }
                case "translate" -> {
                    length += stringValues.get(0);
                }
                case "keybind" -> {
                    length += stringValues.get(0);
                }
                case "insertion" -> {
                }
            }
        }
        return length;
    }

    @Override
    public Integer visitEnd(Parser.Node.End node) {
        return 0;
    }
}
