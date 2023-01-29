package net.tickmc.lccutils.managers.text.formatter;

public class Printer implements Parser.Visitor<String> {

    public String print(Parser.Node node) {
        return node.accept(this);
    }

    @Override
    public String visitText(Parser.Node.Text node) {
        return node.tokens.stream().map(Tokenizer.Token::value).reduce("", String::concat);
    }

    @Override
    public String visitParsedText(Parser.Node.ParsedText node) {
        return "(parsed " + node.nodes.stream().map(this::print).reduce("", String::concat) + ")";
    }

    @Override
    public String visitSingularColorCode(Parser.Node.SingularColorCode node) {
        return "(&" + node.code.value() + ")";
    }

    @Override
    public String visitHexColorCode(Parser.Node.HexColorCode node) {
        return "(&#" + node.getHex().asHexString() + ")";
    }

    @Override
    public String visitMapColorCode(Parser.Node.MapColorCode node) {
        return "(&map " + node.map.entrySet().stream().map(entry -> entry.getKey() + "=" + entry.getValue().stream().map(this::print).toList()).reduce("", (s1, s2) -> s1 + "," + s2) + ")";
    }

    @Override
    public String visitEnd(Parser.Node.End node) {
        return "(&end " + print(node.node) + ")";
    }
}
