package io.github.kbridge.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.integration.channel.PriorityChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.core.MessagingTemplate;
import org.springframework.messaging.MessageChannel;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableIntegration
@EnableRetry
public class EipConfig {

	@Bean
	protected MessageChannel toKafkaChannel() {
		PriorityChannel priorityChannel = new PriorityChannel();
		return priorityChannel;
	}

	@Bean
	protected MessageChannel toMqChannel() {
		PriorityChannel priorityChannel = new PriorityChannel();
		return priorityChannel;
	}

	@Bean
	protected MessagingTemplate messagingTemplate() {
		return new MessagingTemplate();
	}
	
	@Bean
	protected TaskExecutor kafkaOutboundExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2); 
        executor.setMaxPoolSize(10); 
        executor.setThreadNamePrefix("kafka-out-");
        executor.initialize();
        return executor;
    }
	
	
	@Bean
	protected TaskExecutor mqOutboundExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2); 
        executor.setMaxPoolSize(10); 
        executor.setThreadNamePrefix("mq-out-"); 
        executor.initialize();
        return executor;
    }
	
	
	
}
