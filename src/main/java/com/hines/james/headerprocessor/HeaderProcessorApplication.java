package com.hines.james.headerprocessor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.integration.annotation.Transformer;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.GenericMessage;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

@EnableBinding(Processor.class)
@SpringBootApplication
public class HeaderProcessorApplication {

	@Transformer(inputChannel = Processor.INPUT, outputChannel = Processor.OUTPUT)
	public Message<?> transform(String in, MessageHeaders headers) {
		Map newHeaders = new HashMap();

		newHeaders.putAll(headers);
		newHeaders.put("foo", "bar");
		newHeaders.put("fizz", "buzz");

		return  new GenericMessage(in, newHeaders);
	}

	public static void main(String[] args) {
		SpringApplication.run(HeaderProcessorApplication.class, args);
	}
}
