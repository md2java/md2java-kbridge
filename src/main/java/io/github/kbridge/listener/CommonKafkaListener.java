package io.github.kbridge.listener;

import java.util.HashMap;
import java.util.List;

import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.core.MessagingTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Component;

import io.github.kbridge.props.AppConstants;
import io.github.kbridge.props.ChannelNames;
import io.github.kbridge.transform.KafkaPayloadTransformer;
import io.github.kbridge.util.TransformerUtil;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class CommonKafkaListener {

	@Autowired
	private TransformerUtil transformerUtil;

	@Autowired
	private MessagingTemplate messagingTemplate;

	@KafkaListener(topics = "${app.kafka.topics}", groupId = "ami-topic-id", autoStartup = "${app.kafka.enabled:false}", concurrency = "${app.kafka.threds:3}")
	public void fromKafka(ConsumerRecord<String, GenericRecord> consumerRecord) {
		String topic = consumerRecord.topic();
		List<KafkaPayloadTransformer> transformers = transformerUtil.findKafkaTransformer(topic);
		for (KafkaPayloadTransformer transformer : transformers) {
			Object tranformToMq = transformer.tranformToMq(consumerRecord);
			HashMap<String, Object> headers = new HashMap<String, Object>();
			headers.put(AppConstants.KAFKA_TOPIC, topic);
			headers.put(AppConstants.MQ_TOPIC, transformer.mqTopic());
			GenericMessage<Object> genericMessage = new GenericMessage<>(tranformToMq, headers);
			messagingTemplate.send(ChannelNames.TO_MQ.bean(), genericMessage);
			log.info("sent to channel: {} ", ChannelNames.TO_MQ.bean());
		}
	}

}
