package net.tickmc.lccutils.managers.text.formatter;

import net.kyori.adventure.text.format.TextColor;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class Parser {
    public static class ParseError extends RuntimeException {
        public Tokenizer.Token token;

        public ParseError(String message, Tokenizer.Token token) {
            super(message);
            this.token = token;
        }
    }

    public static abstract class Node {
        public abstract <T> T accept(Visitor<T> visitor);

        public static class Text extends Node {
            public List<Tokenizer.Token> tokens;

            public Text(List<Tokenizer.Token> tokens) {
                this.tokens = tokens;
            }

            @Override
            public <T> T accept(Visitor<T> visitor) {
                return visitor.visitText(this);
            }

            public String stringify() {
                return tokens.stream().map(Tokenizer.Token::value).reduce("", String::concat);
            }
        }

        public static class ParsedText extends Node {
            public List<Node> nodes;

            public ParsedText(List<Node> components) {
                this.nodes = components;
                this.nodes = mergeSingularCharacters();
            }

            @Override
            public <T> T accept(Visitor<T> visitor) {
                return visitor.visitParsedText(this);
            }

            public List<Node> mergeSingularCharacters() {
                List<Node> nodes = new ArrayList<>();
                int i = 0;
                while (i < this.nodes.size()) {
                    Node node = this.nodes.get(i);
                    if (node instanceof Text) {
                        List<Tokenizer.Token> tokens = new ArrayList<>(((Text) node).tokens);
                        while (i + 1 < this.nodes.size() && this.nodes.get(i + 1) instanceof Text) {
                            tokens.addAll(((Text) this.nodes.get(i + 1)).tokens);
                            i++;
                        }
                        nodes.add(new Text(tokens));
                    } else {
                        nodes.add(node);
                    }
                    i++;
                }
                return nodes;
            }

            public List<Node> toSingularCharacters() {
                List<Node> nodes = new ArrayList<>();
                for (Node node : this.nodes) {
                    if (node instanceof Text) {
                        for (Tokenizer.Token token : ((Text) node).tokens) {
                            nodes.add(new Text(List.of(token)));
                        }
                    } else {
                        nodes.add(node);
                    }
                }
                return nodes;
            }
        }

        public static class SingularColorCode extends Node {
            public Tokenizer.Token code;

            public SingularColorCode(Tokenizer.Token token) {
                this.code = token;
            }

            @Override
            public <T> T accept(Visitor<T> visitor) {
                return visitor.visitSingularColorCode(this);
            }
        }

        public static class HexColorCode extends Node {
            public Tokenizer.Token[] codes;

            public HexColorCode(Tokenizer.Token[] codes) {
                this.codes = codes;
            }

            @Override
            public <T> T accept(Visitor<T> visitor) {
                return visitor.visitHexColorCode(this);
            }

            public TextColor getHex() {
                return TextColor.fromHexString("#" + codes[0].value() + codes[1].value() + codes[2].value() + codes[3].value() + codes[4].value() + codes[5].value());
            }
        }

        public static class MapColorCode extends Node {
            public LinkedHashMap<String, List<ParsedText>> map;

            public MapColorCode(LinkedHashMap<String, List<ParsedText>> map) {
                this.map = map;
            }

            @Override
            public <T> T accept(Visitor<T> visitor) {
                return visitor.visitMapColorCode(this);
            }
        }

        public static class End extends Node {
            public Text node;
            public End(Text node) {
                this.node = node;
            }
            @Override
            public <T> T accept(Visitor<T> visitor) {
                return visitor.visitEnd(this);
            }
        }
    }

    public interface Visitor<R> {
        R visitText(Node.Text node);
        R visitParsedText(Node.ParsedText node);
        R visitSingularColorCode(Node.SingularColorCode node);
        R visitHexColorCode(Node.HexColorCode node);
        R visitMapColorCode(Node.MapColorCode node);
        R visitEnd(Node.End node);
    }

    private final List<Tokenizer.Token> tokens;
    public final List<Character> COLOR_CODES = List.of('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'k', 'l', 'm', 'n', 'o', 'r');
    private int current = 0;

    public Parser(List<Tokenizer.Token> tokens) {
        this.tokens = tokens;
    }

    public Node.ParsedText parse() {
        List<Node> nodes = new ArrayList<>();
        while (!isAtEnd()) {
            nodes.add(parseSingle());
        }
        return new Node.ParsedText(nodes);
    }

    private Node parseSingle() {
        if (matchUnlessEscaped(Tokenizer.TokenType.BACK_SLASH)) {
            // return the next token
            return new Node.Text(List.of(advance()));
        }
        if (matchUnlessEscaped(Tokenizer.TokenType.AND)) {
            if (matchUnlessEscaped(Tokenizer.TokenType.LEFT_BRACKET)) {
                return map();
            }
            if (check(Tokenizer.TokenType.CHARACTER) && COLOR_CODES.contains(peek().value().charAt(0))) {
                return new Node.SingularColorCode(advance());
            }
            if (matchUnlessEscaped(Tokenizer.TokenType.HASH)) {
                return new Node.HexColorCode(consumeHex());
            }
            return new Node.Text(List.of(previous()));
        }
        return new Node.Text(List.of(advance()));
    }

    private Node map() {
        if (match(Tokenizer.TokenType.FORWARD_SLASH)) {
            Node.Text key = consumeUntil(Tokenizer.TokenType.EQUALS, Tokenizer.TokenType.RIGHT_BRACKET);
            consume(Tokenizer.TokenType.RIGHT_BRACKET, "Expected ']' after end value!");
            return new Node.End(key);
        }
        LinkedHashMap<String, List<Node.ParsedText>> map = new LinkedHashMap<>();
        do {
            Node.Text key = consumeUntil(Tokenizer.TokenType.EQUALS);
            consume(Tokenizer.TokenType.EQUALS, "Expected '=' after key!");
            List< Node.ParsedText > values = new ArrayList<>();
            do {
                values.add(mapValue(Tokenizer.TokenType.COMMA, Tokenizer.TokenType.SEMICOLON, Tokenizer.TokenType.RIGHT_BRACKET));
            } while (matchUnlessEscaped(Tokenizer.TokenType.COMMA));
            map.put(key.stringify(), values);
        } while (match(Tokenizer.TokenType.SEMICOLON));
        consume(Tokenizer.TokenType.RIGHT_BRACKET, "Expected ']' after map!");
        return new Node.MapColorCode(map);
    }

    private Node.Text consumeUntil(Tokenizer.TokenType... types) {
        List<Tokenizer.Token> textTokens = new ArrayList<>();
        while (!isAtEnd()) {
            if (match(Tokenizer.TokenType.BACK_SLASH)) {
                advance();
                textTokens.add(advance());
                continue;
            }
            if (checkUnlessEscaped(types)) {
                break;
            }
            textTokens.add(peek());
            advance();
        }
        if (textTokens.isEmpty()) {
            throw error(peek(), "Expected text!");
        }
        return new Node.Text(textTokens);
    }

    private Node.ParsedText mapValue(Tokenizer.TokenType... types) {
        List<Node> nodes = new ArrayList<>();
        while (!isAtEnd()) {
            if (checkUnlessEscaped(types)) {
                break;
            }
            nodes.add(parseSingle());
        }
        if (nodes.isEmpty()) {
            throw error(peek(), "Expected text!");
        }
        return new Node.ParsedText(nodes);
    }

    private Tokenizer.Token consume(Tokenizer.TokenType type, String message) {
        if (check(type)) {
            return advance();
        }
        throw error(peek(), message);
    }

    private Tokenizer.Token[] consumeHex() {
        Tokenizer.Token[] tokens = new Tokenizer.Token[6];
        List<Character> hexCodes = List.of('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f');
        for (int i = 0; i < 6; i++) {
            if (!check(Tokenizer.TokenType.CHARACTER)) {
                throw error(peek(), "Expected " + 6 + " digits or hex letters!");
            }
            if (!hexCodes.contains(peek().value().charAt(0))) {
                throw error(peek(), "Expected " + 6 + " digits or hex letters!");
            }
            tokens[i] = advance();
        }
        return tokens;
    }

    private ParseError error(Tokenizer.Token token, String message) {
        return new ParseError(message, token);
    }

    private boolean match(Tokenizer.TokenType type) {
        if (check(type)) {
            advance();
            return true;
        }
        return false;
    }

    private boolean matchUnlessEscaped(Tokenizer.TokenType type) {
        if (check(type) && isEscaped()) {
            advance();
            return true;
        }
        return false;
    }

    private boolean check(Tokenizer.TokenType... types) {
        if (isAtEnd()) {
            return false;
        }
        for (Tokenizer.TokenType type : types) {
            if (peek().type() == type) {
                return true;
            }
        }
        return false;
    }

    private boolean checkUnlessEscaped(Tokenizer.TokenType... types) {
        if (isAtEnd() || isAtStart()) {
            return false;
        }
        for (Tokenizer.TokenType type : types) {
            if (peek().type() == type && isEscaped()) {
                return true;
            }
        }
        return false;
    }

    private Tokenizer.Token peek() {
        return tokens.get(current);
    }

    private Tokenizer.Token previous() {
        return tokens.get(current - 1);
    }

    private boolean isAtEnd() {
        return peek().type() == Tokenizer.TokenType.EOF;
    }
    private boolean isAtStart() {
        return current == 0;
    }

    private boolean isEscaped() {
        return isAtStart() || previous().type() != Tokenizer.TokenType.BACK_SLASH;
    }

    private Tokenizer.Token advance() {
        if (!isAtEnd()) {
            current++;
        }
        return previous();
    }
}
