/*
 * This file is part of the pl.wrzasq.commons.
 *
 * @license http://mit-license.org/ The MIT license
 * @copyright 2017 - 2019 © by Rafał Wrzeszcz - Wrzasq.pl.
 */

package test.pl.wrzasq.commons.aws.sns;

import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.model.PublishResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.wrzasq.commons.aws.sns.TopicClient;

@ExtendWith(MockitoExtension.class)
public class TopicClientTest {
    @Mock
    private AmazonSNS sns;

    @Mock
    private ObjectMapper objectMapper;

    @Test
    public void publish() throws JsonProcessingException {
        // just for code coverage
        new TopicClient(this.objectMapper, null);

        var topic = "arn:test";
        var input = new Object();
        var message = "{}";
        var result = new PublishResult();

        var client = new TopicClient(this.sns, this.objectMapper, topic);

        Mockito.when(this.objectMapper.writeValueAsString(input)).thenReturn(message);
        Mockito.when(this.sns.publish(topic, message)).thenReturn(result);

        Assertions.assertSame(
            result,
            client.publish(input),
            "TopicClient.publish() should send serialized message to configured topic."
        );
    }
}
