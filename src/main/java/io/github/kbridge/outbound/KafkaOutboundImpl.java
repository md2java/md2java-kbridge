package io.github.kbridge.outbound;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.Poller;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class KafkaOutboundImpl {
	
	@Autowired
	private InternalGateway internalGateway;
	
	@ServiceActivator(inputChannel = "toKafkaChannel",poller = @Poller(fixedDelay = "${ko.fixed.delay:100}",taskExecutor = "kafkaOutboundExecutor"))
	public void toKafkaHandler(GenericMessage<ProducerRecord<String, Object>> message) {
		log.debug("received from toKafkaChannel: {} ", message.getPayload());
		internalGateway.sendToKafka(message);
		log.debug("sent to kafka: {} ", message.getPayload().topic());
	}
}
