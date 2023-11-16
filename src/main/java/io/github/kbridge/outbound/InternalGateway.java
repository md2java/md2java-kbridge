package io.github.kbridge.outbound;

import javax.annotation.PostConstruct;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import io.github.kbridge.props.AppConstants;
import io.github.kbridge.props.TransformerContainer;
import io.github.kbridge.transform.KafkaPayloadTransformer;
import io.github.kbridge.transform.MqPayloadTransformer;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class InternalGateway {

	@Autowired
	private KafkaTemplate<String, Object> kafkaTemplate;
	@Autowired
	private TransformerContainer transformerContainer;
	
	@Autowired
	private JmsTemplate jmsTemplate;
	
	private JmsTemplate queueJmsTemplate;

	private JmsTemplate jmsProducer = null;


	@PostConstruct
	public void init() {
		queueJmsTemplate = new JmsTemplate(jmsTemplate.getConnectionFactory());
		queueJmsTemplate.setPubSubDomain(false);
	}
	
	@Retryable(maxAttemptsExpression = "${kbridge.kafka.max-try:3}",recover = "recoverKafka")
	public void sendToKafka(GenericMessage<ProducerRecord<String, Object>> message) {
		try {
			kafkaTemplate.send(message.getPayload());			
		} catch (Exception e) {
			log.error("something went wrong: {} ",e.toString());
			throw e;
		}
	}

	@Recover
	public void recoverKafka(Exception ex,GenericMessage<ProducerRecord<String, Object>> message) {
		String mqTopicName = (String) message.getHeaders().get(AppConstants.MQ_TOPIC);
		log.error("failed to send from mq: {}  to kafka: {} - after all tried: {} ",mqTopicName,message.getPayload().topic(),ex.toString());
		MqPayloadTransformer transformer = transformerContainer.findMqTransformer(message.getHeaders());
		transformer.handleOnFail(ex,message);
	}


	@Retryable(maxAttemptsExpression = "${kbridge.mq.max-try:3}",recover = "recoverMq")
	public void sendToMq(GenericMessage<Object> message) {
		try {
			KafkaPayloadTransformer transformer = transformerContainer.findKafkaTransformer(message.getHeaders());
			jmsProducer = transformer.isPubSubDomain() ? jmsTemplate : queueJmsTemplate;
			jmsProducer.convertAndSend(transformer.mqTopic(), message.getPayload());
		} catch (Exception e) {
			log.error("something went wrong: {} ",e.toString());
			throw e;
		}
	}
	
	@Recover
	public void recoverMq(Exception ex,GenericMessage<Object> message) {
		String kafkaTopic = (String) message.getHeaders().get(AppConstants.KAFKA_TOPIC);
		KafkaPayloadTransformer transformer = transformerContainer.findKafkaTransformer(message.getHeaders());
		log.error("failed to send from kafka: {}  to mq: {} - after all tried: {} ",kafkaTopic,transformer.mqTopic(),ex.toString());
		 transformer.handleOnFail(ex,message);
	}
	
	
	
	
	
}
