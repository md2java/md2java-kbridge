package io.github.kbridge.listener;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.PostConstruct;
import javax.jms.ConnectionFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.jms.listener.DefaultMessageListenerContainer;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;
import org.springframework.stereotype.Component;

import io.github.kbridge.transform.MqPayloadTransformer;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@DependsOn("kafkaOutboundImpl")
public class TopicSubscriber {

	@Autowired
	private ConnectionFactory connectionFactory;
	@Autowired
	private CommonJmsListener commonJmsListener;

	@Autowired(required = false)
	private List<MqPayloadTransformer> mqPayloadTransformers;

	@PostConstruct
	public void init() {
		ExecutorService executor = Executors.newFixedThreadPool(4, new CustomizableThreadFactory("mq-in-"));

		if (Objects.isNull(mqPayloadTransformers)) {
			log.info("no mq topic cofigured");
			return;
		}
		mqPayloadTransformers.stream().forEach(s -> {
			buildListner(executor, commonJmsListener, s);
		});
	}

	private void buildListner(ExecutorService executor, CommonJmsListener messageListener, MqPayloadTransformer mqPTF) {
		DefaultMessageListenerContainer container = new DefaultMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		container.setDestinationName(mqPTF.mqTopic());
		container.setMessageListener(messageListener);
		container.setPubSubDomain(mqPTF.isPubSubDomain());
		container.setTaskExecutor(executor);
		container.initialize();
		container.start();
		log.info("topic: {} subscribed ispublish: {} ", mqPTF.mqTopic(),mqPTF.isPubSubDomain());
	}

}
