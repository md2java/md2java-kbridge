package io.github.kbridge.props;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.github.kbridge.transform.KafkaPayloadTransformer;
import io.github.kbridge.transform.MqPayloadTransformer;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@Getter
public class EtlProperties {

	private Map<String, Set<String>> targetDestination;

	private Map<String, List<MqPayloadTransformer>> mqMap;

	private Map<String, List<KafkaPayloadTransformer>> kafkaMap;

	@Autowired(required = false)
	private List<MqPayloadTransformer> mqPayloadTransformers;
	
	@Autowired(required = false)
	private List<KafkaPayloadTransformer> kafkaMqPayloadTransformers;

	@Autowired
	private TransformerContainer transformerContainer;
	
	@PostConstruct
	public void init() {
		mqMap = new ConcurrentHashMap<String, List<MqPayloadTransformer>>();
		kafkaMap = new ConcurrentHashMap<String, List<KafkaPayloadTransformer>>();
		targetDestination = new ConcurrentHashMap<>();

		if (Objects.nonNull(mqPayloadTransformers)) {
			mqPayloadTransformers.stream().forEach(s -> {
				Set<String> list = targetDestination.get(s.mqTopic());
				if (Objects.isNull(list)) {
					list = new HashSet<>();
					targetDestination.put(s.mqTopic(), list);
				}
				list.add(s.kafkaTopic());
				addMqTransformer(s);
			});
		}

		if (Objects.nonNull(kafkaMqPayloadTransformers)) {
			kafkaMqPayloadTransformers.stream().forEach(s -> {
				Set<String> list = targetDestination.get(s.kafkaTopic());
				if (Objects.isNull(list)) {
					list = new HashSet<>();
					targetDestination.put(s.kafkaTopic(), list);
				}
				list.add(s.kafkaTopic());
				addKafkaTransformer(s);
			});
		}
		
		String kafkaTopicList = kafkaMap.keySet().stream().collect(Collectors.joining(","));
		System.setProperty("app.kafka.topics", "default-Topic");
		if(StringUtils.isNotBlank(kafkaTopicList)) {
			System.setProperty("app.kafka.enabled", "true");
			System.setProperty("app.kafka.topics", kafkaTopicList);
		}
		log.info("topic target mapping: {} ", targetDestination);

	}

	private void addKafkaTransformer(KafkaPayloadTransformer s) {
		List<KafkaPayloadTransformer> list = kafkaMap.get(s.kafkaTopic());
		if(Objects.isNull(list)) {
			list = new ArrayList<KafkaPayloadTransformer>();
			kafkaMap.put(s.kafkaTopic(), list);
		}
		list.add(s);
		transformerContainer.registerTransformer(s);
	}

	private void addMqTransformer(MqPayloadTransformer s) {
		List<MqPayloadTransformer> list = mqMap.get(s.mqTopic());
		if(Objects.isNull(list)) {
			list = new ArrayList<MqPayloadTransformer>();
			mqMap.put(s.mqTopic(), list);
		}
		list.add(s);
		transformerContainer.registerTransformer(s);
	}

}
