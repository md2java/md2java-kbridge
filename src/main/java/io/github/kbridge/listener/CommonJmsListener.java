package io.github.kbridge.listener;

import java.util.HashMap;
import java.util.List;

import javax.jms.Message;
import javax.jms.MessageListener;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.core.MessagingTemplate;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Component;

import io.github.kbridge.props.AppConstants;
import io.github.kbridge.props.ChannelNames;
import io.github.kbridge.transform.MqPayloadTransformer;
import io.github.kbridge.util.TransformerUtil;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class CommonJmsListener implements MessageListener {
	
	
	@Autowired
	private MessagingTemplate messagingTemplate;
	
	@Autowired
	private TransformerUtil transformerUtil;

	@Override
	public void onMessage(Message message) {
		log.info("MessageListener-received from mq ");
		String mqTopic = transformerUtil.extractTopic(message);
		List<MqPayloadTransformer> transformers = transformerUtil.findMqTransformer(message);
		for (MqPayloadTransformer transformer : transformers) {
			Object kafkaPayload = transformer.transformToKafka(message);
			ProducerRecord<String, Object> producerRecord = new ProducerRecord<>(transformer.kafkaTopic(),
					kafkaPayload);
			HashMap<String, Object> headers = new HashMap<String, Object>();
			headers.put(AppConstants.MQ_TOPIC, mqTopic);
			headers.put(AppConstants.KAFKA_TOPIC, transformer.kafkaTopic());
			GenericMessage<ProducerRecord<String, Object>> genericMessage = new GenericMessage<>(producerRecord,
					headers);
			messagingTemplate.send(ChannelNames.TO_KAFKA.bean(), genericMessage);
			log.info("sent to : {} ", ChannelNames.TO_KAFKA.bean());
		}
	}


}
