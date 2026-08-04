package me.pafias.pessentials.util;

import java.text.Normalizer;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public final class TextUtils {

    private static final int MAX_VARIANTS = 64;

    private TextUtils() {
    }

    public enum MatchMode {
        EXACT,
        CONTAINS
    }

    public record BlacklistedWord(
            String original,
            String normalized,
            MatchMode matchMode
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
            Map.entry('ё', 'e'),
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
            Map.entry('і', 'i'),
            Map.entry('ј', 'j'),
            Map.entry('ѕ', 's'),
            Map.entry('ԁ', 'd'),
            Map.entry('ԛ', 'q'),

            // Greek homoglyphs
            Map.entry('α', 'a'),
            Map.entry('β', 'b'),
            Map.entry('ε', 'e'),
            Map.entry('ι', 'i'),
            Map.entry('κ', 'k'),
            Map.entry('ο', 'o'),
            Map.entry('ρ', 'p'),
            Map.entry('τ', 't'),
            Map.entry('υ', 'y'),
            Map.entry('χ', 'x'),
            Map.entry('ν', 'v')
    );

    private static final Map<Character, char[]> AMBIGUOUS_CHAR_MAP = Map.of(
            '1', new char[]{'i', 'l', 't'},
            '!', new char[]{'i', 'l'},
            '|', new char[]{'i', 'l'}
    );

    public static String normalize(String input) {
        return normalizeVariants(input)
                .stream()
                .findFirst()
                .orElse("");
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

        for (final char current : text.toCharArray()) {
            final char[] replacements = getReplacements(current);

            if (replacements.length == 0) {
                continue;
            }

            final Set<String> nextVariants = new LinkedHashSet<>();

            for (final String variant : variants) {
                for (final char replacement : replacements) {
                    nextVariants.add(variant + replacement);

                    if (nextVariants.size() >= MAX_VARIANTS) {
                        break;
                    }
                }

                if (nextVariants.size() >= MAX_VARIANTS) {
                    break;
                }
            }

            variants = nextVariants;
        }

        final Set<String> collapsed = new LinkedHashSet<>();

        for (final String variant : variants) {
            collapsed.add(collapseRepeatedCharacters(variant));
        }

        return collapsed;
    }

    public static Set<String> createFilterCandidates(String input) {
        if (input == null || input.isBlank()) {
            return Set.of();
        }

        final String[] tokens = input
                .toLowerCase(Locale.ROOT)
                .trim()
                .split("\\s+");

        final Set<String> candidates = new LinkedHashSet<>();

        for (final String token : tokens) {
            addNonBlankVariants(candidates, token);
        }

        final StringBuilder fragmentedRun = new StringBuilder();
        int fragmentedTokenCount = 0;

        for (final String token : tokens) {
            final String cleanedToken = normalize(token);

            if (!cleanedToken.isBlank() && cleanedToken.length() <= 2) {
                fragmentedRun.append(token);
                fragmentedTokenCount++;
                continue;
            }

            addFragmentedCandidate(
                    candidates,
                    fragmentedRun,
                    fragmentedTokenCount
            );

            fragmentedRun.setLength(0);
            fragmentedTokenCount = 0;
        }

        addFragmentedCandidate(
                candidates,
                fragmentedRun,
                fragmentedTokenCount
        );

        candidates.remove("");

        return candidates;
    }

    private static void addNonBlankVariants(
            Set<String> candidates,
            String input
    ) {
        for (final String variant : normalizeVariants(input)) {
            if (!variant.isBlank()) {
                candidates.add(variant);
            }
        }
    }

    private static void addFragmentedCandidate(
            Set<String> candidates,
            StringBuilder fragmentedRun,
            int tokenCount
    ) {
        if (tokenCount < 2) {
            return;
        }

        addNonBlankVariants(candidates, fragmentedRun.toString());
    }

    private static char[] getReplacements(char current) {
        final char[] ambiguous = AMBIGUOUS_CHAR_MAP.get(current);

        if (ambiguous != null) {
            return ambiguous;
        }

        final Character replacement = CHAR_MAP.get(current);

        if (replacement != null) {
            return new char[]{replacement};
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