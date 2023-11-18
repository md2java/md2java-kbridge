package io.github.kbridge.transform;

import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.messaging.support.GenericMessage;

public interface KafkaPayloadTransformer extends PayloadTransformer {
	Object tranformToMq(ConsumerRecord<String, GenericRecord> records);
	default void handleOnFail(Exception ex, GenericMessage<Object> message) {}

}
