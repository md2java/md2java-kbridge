package io.github.kbridge.config;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.ExecutorChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.core.MessagingTemplate;
import org.springframework.messaging.MessageChannel;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;

@Configuration
@EnableIntegration
@EnableRetry
public class EipConfig {

	@Bean
	protected MessageChannel toKafkaChannel() {
		Executor executor = Executors.newFixedThreadPool(2, new CustomizableThreadFactory("kafka-out-"));
		return new ExecutorChannel(executor);
	}

	@Bean
	protected MessageChannel toMqChannel() {
		Executor executor = Executors.newFixedThreadPool(2, new CustomizableThreadFactory("mq-out-"));
		return new ExecutorChannel(executor);
	}

	@Bean
	protected MessagingTemplate messagingTemplate() {
		return new MessagingTemplate();
	}

	
	
}
