package net.tickmc.lccutils.managers.text.formatter;

public final class Tests {
    public static void main(String[] args) {
        String test = "&cHello &bWorld with &[font=a]a random font! No more &[/color] color!";
        System.out.println("Input: " + test);
        var tokens = new Tokenizer(test).scanTokens();
        System.out.println("Tokens: " + tokens);
        var nodes = new Parser(tokens).parse();
        System.out.println("Parsed: " + new Printer().print(nodes));
        var interpreter = new ComponentGenerator(nodes);
        System.out.println("Interpreted: " + interpreter.interpret());
    }
}
