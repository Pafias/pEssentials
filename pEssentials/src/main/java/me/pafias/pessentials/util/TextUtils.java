package me.pafias.pessentials.util;

import java.text.Normalizer;
import java.util.Locale;

public class TextUtils {

    public record BlacklistedWord(
            String original,
            String normalized
    ) {}

    public static String normalize(String input) {
        if (input == null || input.isBlank())
            return "";

        String text = input.toLowerCase(Locale.ROOT);

        text = Normalizer.normalize(text, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "");

        text = text
                .replace('@', 'a')
                .replace('4', 'a')
                .replace('3', 'e')
                .replace('1', 'i')
                .replace('!', 'i')
                .replace('|', 'i')
                .replace('0', 'o')
                .replace('5', 's')
                .replace('$', 's')
                .replace('7', 't')
                .replace('+', 't')
                .replace('8', 'b');

        text = text.replaceAll("[^a-z0-9]", "");

        text = collapseRepeatedCharacters(text);

        return text;
    }

    private static String collapseRepeatedCharacters(String input) {
        final StringBuilder builder = new StringBuilder(input.length());

        char previous = 0;

        for (final char current : input.toCharArray()) {
            if (current != previous) {
                builder.append(current);
                previous = current;
            }
        }

        return builder.toString();
    }

}
