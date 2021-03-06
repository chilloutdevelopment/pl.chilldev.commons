/*
 * This file is part of the pl.wrzasq.commons.
 *
 * @license http://mit-license.org/ The MIT license
 * @copyright 2015 - 2016, 2018 - 2019 © by Rafał Wrzeszcz - Wrzasq.pl.
 */

package pl.wrzasq.commons.text.html;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

import pl.wrzasq.commons.text.Formatter;
import pl.wrzasq.commons.text.TextProcessingException;

/**
 * Various HTML text processing utilities.
 */
public class Utils {
    /**
     * Default suffix.
     */
    public static final String SUFFIX_DEFAULT = "…";

    /**
     * Word bound matchind regexp.
     */
    public static final Pattern WORDBOUND_PATTERN = Pattern.compile("\\S\\s+\\S*?$", Pattern.UNICODE_CASE);

    /**
     * First paragraph pattern.
     */
    public static final Pattern REGEX_FIRSTPARAGRAPH = Pattern.compile(
        "<p(?: [^>]*)?>(.*?)</p>",
        Pattern.DOTALL
    );

    /**
     * Text formatter (dummy default instance to prevent NPE).
     */
    private static Formatter formatter = new Formatter();

    /**
     * Fetches first paragraph of text.
     *
     * @param text HTML snippet.
     * @return First paragrapth.
     */
    public static String firstParagraph(String text) {
        var match = Utils.REGEX_FIRSTPARAGRAPH.matcher(text);

        return match.find() ? match.group(1) : "";
    }

    /**
     * Truncates text.
     *
     * @param text Text to be truncated.
     * @param length Maximum text length.
     * @param suffix Suffix to be used at the end of truncated text.
     * @param wordBounds Whether to look for word end or not.
     * @return Translated message.
     */
    public static String truncate(String text, int length, String suffix, boolean wordBounds) {
        // nothing to do here
        if (text.length() <= length) {
            return text;
        }

        // look for last possible word
        if (wordBounds) {
            // look for last word-break
            var part = text.substring(0, length + 2);
            var matcher = Utils.WORDBOUND_PATTERN.matcher(part);
            if (matcher.find()) {
                // we add 1 as second parameter is exclusive
                length = matcher.start() + 1;
            }
        }

        return text.substring(0, length).trim() + suffix;
    }

    /**
     * Truncates text.
     *
     * @param text Text to be truncated.
     * @param length Maximum text length.
     * @param suffix Suffix to be used at the end of truncated text.
     * @return Translated message.
     */
    public static String truncate(String text, int length, String suffix) {
        return Utils.truncate(text, length, suffix, true);
    }

    /**
     * Truncates text.
     *
     * @param text Text to be truncated.
     * @param length Maximum text length.
     * @param wordBounds Whether to look for word end or not.
     * @return Trucnated text.
     */
    public static String truncate(String text, int length, boolean wordBounds) {
        return Utils.truncate(text, length, Utils.SUFFIX_DEFAULT, wordBounds);
    }

    /**
     * Truncates text.
     *
     * @param text Text to be truncated.
     * @param length Maximum text length.
     * @return Trucnated text.
     */
    public static String truncate(String text, int length) {
        return Utils.truncate(text, length, Utils.SUFFIX_DEFAULT, true);
    }

    /**
     * Registers new text formatting handler.
     *
     * @param formatter Formatter.
     */
    public static void setFormatter(Formatter formatter) {
        Utils.formatter = formatter;
    }

    /**
     * Formats the text.
     *
     * @param format Format name.
     * @param text Source text.
     * @return Formatted text.
     * @throws TextProcessingException When text processing fails.
     */
    public static String format(String format, String text)
        throws TextProcessingException {
        return Utils.formatter.transform(format, text);
    }

    /**
     * Wrapper function that encodes URLs using UTF-8 encoding.
     *
     * @param value URL part.
     * @return URL-encoded part.
     */
    public static String urlEncode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }
}
