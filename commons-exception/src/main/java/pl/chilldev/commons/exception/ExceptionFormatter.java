/**
 * This file is part of the ChillDev-Commons.
 *
 * @license http://mit-license.org/ The MIT license
 * @copyright 2014 © by Rafał Wrzeszcz - Wrzasq.pl.
 */

package pl.chilldev.commons.exception;

import java.text.MessageFormat;

/**
 * Exception dumper signature.
 *
 * @version 0.0.1
 * @since 0.0.1
 */
public interface ExceptionFormatter
{
    /**
     * Simple formatter that prints one-line exception info (it's type and message).
     */
    ExceptionFormatter SIMPLE_FORMAT = new ExceptionFormatter() {
        /**
         * Messages formatter.
         */
        protected MessageFormat formatter = new MessageFormat("{0}: {1}");

        /**
         * {@inheritDoc}
         * @version 0.0.1
         * @since 0.0.1
         */
        @Override
        public String format(Throwable exception)
        {
            return this.formatter.format(new Object[] {
                    exception.getClass().getName(),
                    exception.getMessage()
            });
        }
    };

    /**
     * Formats exception to printable representation.
     *
     * @param exception Exception to be displayed.
     * @return String representation.
     * @since 0.0.1
     */
    String format(Throwable exception);
}