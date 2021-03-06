/*
 * This file is part of the pl.wrzasq.commons.
 *
 * @license http://mit-license.org/ The MIT license
 * @copyright 2017 - 2019 © by Rafał Wrzeszcz - Wrzasq.pl.
 */

package pl.wrzasq.commons.aws.sns;

import java.util.function.Consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import pl.wrzasq.commons.aws.MessageHandler;

/**
 * SNS notifications handler that processes typed message.
 */
public class TypedNotificationHandler extends SimpleNotificationHandler {
    /**
     * Initializes SNS handler.
     *
     * @param objectMapper JSON handler.
     * @param messageHandler Single message consumer.
     * @param type Message content type.
     * @param <Type> Message type.
     */
    public <Type> TypedNotificationHandler(
        ObjectMapper objectMapper,
        Consumer<Type> messageHandler,
        Class<Type> type
    ) {
        super(new MessageHandler<>(objectMapper, messageHandler, type)::handle);
    }
}
