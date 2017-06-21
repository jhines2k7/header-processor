package com.hines.james.headerprocessor;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.cloud.stream.test.binder.MessageCollector;
import org.springframework.integration.annotation.Transformer;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class HeaderProcessorApplicationTests {

	@Autowired
	private Processor processor;

	@Autowired
	private MessageCollector messageCollector;

	@Test
	@SuppressWarnings("unchecked")
	public void testAddingNewHeaders() {
		processor.input().send(MessageBuilder.withPayload("hello").setHeader("key1", "value1").setHeader("key2", "value2").build());

		Message<String> received = (Message<String>) messageCollector.forChannel(processor.output()).poll();
		assertThat(received.getPayload(), equalTo("hello"));
	}

	@SpringBootApplication
	@EnableBinding(Processor.class)
	public static class MyProcessor {

		@Autowired
		private Processor channels;

		@Transformer(inputChannel = Processor.INPUT, outputChannel = Processor.OUTPUT)
		public Message<?> transform(String in, MessageHeaders headers) {
            Map newHeaders = new HashMap();

            newHeaders.putAll(headers);
            newHeaders.put("foo", "bar");
            newHeaders.put("fizz", "buzz");

            return  new GenericMessage(in, newHeaders);
		}
	}
}
