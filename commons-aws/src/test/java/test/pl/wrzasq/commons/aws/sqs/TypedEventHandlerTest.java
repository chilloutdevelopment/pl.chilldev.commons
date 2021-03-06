/*
 * This file is part of the pl.wrzasq.commons.
 *
 * @license http://mit-license.org/ The MIT license
 * @copyright 2019 © by Rafał Wrzeszcz - Wrzasq.pl.
 */

package test.pl.wrzasq.commons.aws.sqs;

import java.util.Collections;
import java.util.UUID;
import java.util.function.Consumer;

import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.wrzasq.commons.aws.sqs.TypedEventHandler;
import test.pl.wrzasq.commons.aws.GenericMessage;

@ExtendWith(MockitoExtension.class)
public class TypedEventHandlerTest {
    @Mock
    private Consumer<String> messageHandler;

    @Mock
    private Consumer<GenericMessage> genericMessageHandler;

    @Captor
    private ArgumentCaptor<GenericMessage> genericMessage;

    @Test
    public void process() throws JsonProcessingException {
        var objectMapper = new ObjectMapper();

        var content = "test";

        var message = new SQSEvent.SQSMessage();
        message.setBody(objectMapper.writeValueAsString(content));

        var event = new SQSEvent();
        event.setRecords(Collections.singletonList(message));

        var handler = new TypedEventHandler(
            objectMapper,
            this.messageHandler,
            String.class
        );
        handler.process(event);

        Mockito.verify(this.messageHandler).accept(content);
    }

    @Test
    public void processGeneric() {
        var objectMapper = new ObjectMapper();

        var id0 = UUID.randomUUID();
        var id1 = UUID.randomUUID();

        var content = String.format(
            "{"
                + "\"ids\":["
                + "\"%s\","
                + "\"%s\""
                + "]}",
            id0.toString(),
            id1.toString()
        );

        var message = new SQSEvent.SQSMessage();
        message.setBody(content);

        var event = new SQSEvent();
        event.setRecords(Collections.singletonList(message));

        var handler = new TypedEventHandler(
            objectMapper,
            this.genericMessageHandler,
            GenericMessage.class
        );
        handler.process(event);

        Mockito.verify(this.genericMessageHandler).accept(this.genericMessage.capture());
        var genericMessage = this.genericMessage.getValue();

        Assertions.assertEquals(
            id0,
            genericMessage.getIds().get(0),
            "TypedEventHandler.process() should deserialize typed message."
        );
        Assertions.assertEquals(
            id1,
            genericMessage.getIds().get(1),
            "TypedEventHandler.process() should deserialize typed message."
        );
    }
}
