/*
 * This file is part of the pl.wrzasq.commons.
 *
 * @license http://mit-license.org/ The MIT license
 * @copyright 2016, 2019 © by Rafał Wrzeszcz - Wrzasq.pl.
 */

package pl.wrzasq.commons.text.formatter;

/**
 * (X)HTML format handler.
 */
public class HtmlFormatter implements FormatterInterface {
    /**
     * {@inheritDoc}
     */
    @Override
    public String transform(String text) {
        // this is already our desired format
        return text;
    }
}
