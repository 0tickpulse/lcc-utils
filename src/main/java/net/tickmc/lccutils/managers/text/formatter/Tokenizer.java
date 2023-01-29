package net.tickmc.lccutils.managers.text.formatter;

import java.util.ArrayList;
import java.util.List;

public class Tokenizer {

    public enum TokenType {
        AND('&'), // @
        HASH('#'), // #
        EQUALS('='), // =
        COMMA(','), // ,
        LEFT_BRACKET('['), // [
        RIGHT_BRACKET(']'), // ]
        FORWARD_SLASH('/'), // /
        BACK_SLASH('\\'), // \
        SEMICOLON(';'), // ;
        // for everything else
        CHARACTER(null),
        EOF(null);

        TokenType(Character character) {
            this.character = character;
        }

        private final Character character;

        public static List<Character> getAllCharacters() {
            List<Character> characters = new ArrayList<>();
            for (TokenType type : values()) {
                if (type.character != null) {
                    characters.add(type.character);
                }
                characters.add(type.character);
            }
            return characters;
        }
    }

    public record Token(TokenType type, String value) {
    }

    public String source;
    private final List<Token> tokens = new ArrayList<>();
    private int start = 0;
    private int current = 0;
    private final int line = 1;

    public Tokenizer(String source) {
        this.source = source;
    }

    public List<Token> scanTokens() {
        while (!isAtEnd()) {
            start = current;
            scanToken();
        }

        tokens.add(new Token(TokenType.EOF, ""));
        return tokens;
    }

    private void scanToken() {
        char c = advance();
        for (TokenType type : TokenType.values()) {
            if (type.character != null && c == type.character) {
                addToken(type);
                return;
            }
        }
        addToken(TokenType.CHARACTER);
    }

    private char advance() {
        current++;
        return source.charAt(current - 1);
    }

    private boolean isAtEnd() {
        return current >= source.length();
    }

    private void addToken(TokenType type) {
        String text = source.substring(start, current);
        tokens.add(new Token(type, text));
    }

    private char peek() {
        if (isAtEnd()) {
            return '\0';
        }
        return source.charAt(current);
    }
}
