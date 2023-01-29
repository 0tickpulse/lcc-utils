package net.tickmc.lccutils.managers.text.formatter;

import net.kyori.adventure.text.Component;

public class Formatter {
    public static Component deserialize(String message) {
        Tokenizer tokenizer = new Tokenizer(message);
        Parser parser = new Parser(tokenizer.scanTokens());
        ComponentGenerator interpreter = new ComponentGenerator(parser.parse());
        return interpreter.interpret();
    }
}
