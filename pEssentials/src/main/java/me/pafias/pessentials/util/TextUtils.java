package me.pafias.pessentials.util;

import java.text.Normalizer;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class TextUtils {

    public record BlacklistedWord(
            String original,
            String normalized
    ) {
    }

    private static final Map<Character, Character> CHAR_MAP = Map.ofEntries(
            Map.entry('@', 'a'),
            Map.entry('4', 'a'),

            Map.entry('3', 'e'),

            Map.entry('0', 'o'),

            Map.entry('5', 's'),
            Map.entry('$', 's'),

            Map.entry('7', 't'),
            Map.entry('+', 't'),

            Map.entry('8', 'b'),

            // Cyrillic homoglyphs
            Map.entry('а', 'a'),
            Map.entry('е', 'e'),
            Map.entry('о', 'o'),
            Map.entry('р', 'p'),
            Map.entry('с', 'c'),
            Map.entry('х', 'x'),
            Map.entry('у', 'y'),
            Map.entry('к', 'k'),
            Map.entry('м', 'm'),
            Map.entry('т', 't'),
            Map.entry('н', 'h'),
            Map.entry('в', 'b'),

            // Greek homoglyphs
            Map.entry('α', 'a'),
            Map.entry('ο', 'o'),
            Map.entry('ρ', 'p'),
            Map.entry('χ', 'x'),
            Map.entry('υ', 'y'),
            Map.entry('κ', 'k'),
            Map.entry('τ', 't'),
            Map.entry('ν', 'v')
    );

    private static final Map<Character, char[]> AMBIGUOUS_CHAR_MAP = Map.of(
            '1', new char[]{'i', 'l', 't'},
            '!', new char[]{'i', 'l'},
            '|', new char[]{'i', 'l'}
    );

    public static String normalize(String input) {
        return normalizeVariants(input).stream().findFirst().orElse("");
    }

    public static Set<String> normalizeVariants(String input) {
        if (input == null || input.isBlank()) {
            return Set.of("");
        }

        String text = input.toLowerCase(Locale.ROOT);

        text = Normalizer.normalize(text, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "");

        Set<String> variants = new LinkedHashSet<>();
        variants.add("");

        for (char current : text.toCharArray()) {
            char[] replacements = getReplacements(current);

            if (replacements.length == 0) {
                continue;
            }

            Set<String> nextVariants = new LinkedHashSet<>();

            for (String variant : variants) {
                for (char replacement : replacements) {
                    nextVariants.add(variant + replacement);
                }
            }

            variants = nextVariants;

            if (variants.size() > 64) {
                variants = new LinkedHashSet<>(variants.stream().limit(64).toList());
            }
        }

        Set<String> collapsed = new LinkedHashSet<>();

        for (String variant : variants) {
            collapsed.add(collapseRepeatedCharacters(variant));
        }

        return collapsed;
    }

    private static char[] getReplacements(char current) {
        if (AMBIGUOUS_CHAR_MAP.containsKey(current)) {
            return AMBIGUOUS_CHAR_MAP.get(current);
        }

        if (CHAR_MAP.containsKey(current)) {
            return new char[]{CHAR_MAP.get(current)};
        }

        if (current >= 'a' && current <= 'z') {
            return new char[]{current};
        }

        if (current >= '0' && current <= '9') {
            return new char[]{current};
        }

        return new char[0];
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