package io.github.kbridge.transform;

import javax.jms.Message;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.messaging.support.GenericMessage;

public interface MqPayloadTransformer extends PayloadTransformer{
	Object transformToKafka(Message message);
	default void handleOnFail(Exception ex, GenericMessage<ProducerRecord<String, Object>> message) {}
}
