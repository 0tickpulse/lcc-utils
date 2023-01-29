package net.tickmc.lccutils.managers.text.formatter;

import net.kyori.adventure.text.format.TextColor;

import java.util.ArrayList;
import java.util.List;

public class ColorUtilities {
    public static List<TextColor> gradient(TextColor color1, TextColor color2, int steps) {
        List<TextColor> gradient = new ArrayList<>();
        int redStep = (color2.red() - color1.red()) / steps;
        int greenStep = (color2.green() - color1.green()) / steps;
        int blueStep = (color2.blue() - color1.blue()) / steps;
        for (int i = 0; i < steps; i++) {
            int red = color1.red() + (i * redStep);
            int green = color1.green() + (i * greenStep);
            int blue = color1.blue() + (i * blueStep);
            gradient.add(TextColor.color(red, green, blue));
        }
        return gradient;
    }

    public static List<TextColor> multiGradient(int length, TextColor... colors) {
        List<TextColor> gradient = new ArrayList<>();
        if (colors.length < 2) {
            throw new IllegalArgumentException("At least two colors must be provided");
        }
        if (length < 1) {
            throw new IllegalArgumentException("length must be greater than 0");
        }
        int segmentLength = length / (colors.length - 1);
        for (int i = 0; i < colors.length - 1; i++) {
            List<TextColor> segment = gradient(colors[i], colors[i + 1], segmentLength);
            gradient.addAll(segment);
        }
        if (gradient.size() < length) {
            gradient.add(colors[colors.length - 1]);
        }
        return gradient;
    }
}
