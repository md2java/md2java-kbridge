package io.github.kbridge.transform;

import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.messaging.support.GenericMessage;

public interface KafkaPayloadTransformer {
	Object tranformToMq(ConsumerRecord<String, GenericRecord> records);
	String mqTopic();
	String kafkaTopic();
	boolean isPubSubDomain();
	default void handleOnFail(Exception ex, GenericMessage<Object> message) {}

}
