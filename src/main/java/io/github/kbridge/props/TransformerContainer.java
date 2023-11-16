package io.github.kbridge.props;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;

import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;

import io.github.kbridge.transform.KafkaPayloadTransformer;
import io.github.kbridge.transform.MqPayloadTransformer;

@Component
public class TransformerContainer {

	private Map<String, MqPayloadTransformer> mqTransformerMap;

	private Map<String, KafkaPayloadTransformer> kafkaTransformerMap;

	@PostConstruct
	public void init() {
		this.mqTransformerMap = new ConcurrentHashMap<>();
		this.kafkaTransformerMap = new ConcurrentHashMap<>();
	}

	public void registerTransformer(MqPayloadTransformer s) {
		String key = buildKey(s.mqTopic(), s.kafkaTopic());
		mqTransformerMap.put(key, s);
	}

	private String buildKey(String from, String to) {
		return String.format("%s->%s", from, to);
	}

	public void registerTransformer(KafkaPayloadTransformer s) {
		String key = buildKey(s.kafkaTopic(), s.mqTopic());
		kafkaTransformerMap.put(key, s);
	}

	public MqPayloadTransformer findMqTransformer(String mqtopic, String kafkaTopic) {
		String key = buildKey(mqtopic, kafkaTopic);
		return mqTransformerMap.get(key);
	}
	
	public MqPayloadTransformer findMqTransformer(MessageHeaders headers) {
		String mqTopic = (String) headers.get(AppConstants.MQ_TOPIC);
		String kafkaTopic = (String) headers.get(AppConstants.KAFKA_TOPIC);
		return findMqTransformer(mqTopic,kafkaTopic);
	}

	public KafkaPayloadTransformer findKafkaTransformer(String kafkaTopic,String mqtopic) {
		String key = buildKey(kafkaTopic, mqtopic);
		return kafkaTransformerMap.get(key);
	}
	
	public KafkaPayloadTransformer findKafkaTransformer(MessageHeaders headers) {
		String mqTopic = (String) headers.get(AppConstants.MQ_TOPIC);
		String kafkaTopic = (String) headers.get(AppConstants.KAFKA_TOPIC);
		return findKafkaTransformer(kafkaTopic,mqTopic);
	}
	
}
