package io.github.kbridge.util;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.jms.JMSException;
import javax.jms.Message;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.github.kbridge.props.EtlProperties;
import io.github.kbridge.transform.KafkaPayloadTransformer;
import io.github.kbridge.transform.MqPayloadTransformer;

@Component
public class TransformerUtil {
	
	
	@Autowired
	private EtlProperties etlProperties;
	
	@Value("${app.mq.protocol.pre://}")
	private String mqProtocolPrefix;

	public List<KafkaPayloadTransformer>  findKafkaTransformer(String topic) {
		List<KafkaPayloadTransformer> kafkaPayloadTransformer = etlProperties.getKafkaMap().get(topic);
		if (Objects.isNull(kafkaPayloadTransformer)) {
			throw new RuntimeException(
					String.format("transformer not found for kafka topic : %s", kafkaPayloadTransformer));
		}
		return kafkaPayloadTransformer;
	}

	public List<MqPayloadTransformer> findMqTransformer(Message message) {
		List<MqPayloadTransformer> mqPayloadTransformer = null;
		String mqTopicName = null;
		try {
			mqTopicName = extractTopic(message);
			mqPayloadTransformer = findMqTransformer(mqTopicName);
		} catch (Exception e) {
		}
		
		return mqPayloadTransformer;
	}

	public List<MqPayloadTransformer> findMqTransformer(String mqTopicName) {
		List<MqPayloadTransformer> mqPayloadTransformer = null;
		Map<String, List<MqPayloadTransformer>> mqMap = etlProperties.getMqMap();
		mqPayloadTransformer = mqMap.get(mqTopicName);
		if (Objects.isNull(mqPayloadTransformer)) {
			throw new RuntimeException(String.format("transformer not found for mq topic : %s", mqTopicName));
		}
		return mqPayloadTransformer;
	}

	public String extractTopic(Message message){
		String mqTopicName = null;
		try {
			mqTopicName = message.getJMSDestination().toString();
			mqTopicName  = StringUtils.substringAfterLast(mqTopicName, mqProtocolPrefix);
		} catch (JMSException e) {
			
		}
		return mqTopicName;
	}
	
	
}
