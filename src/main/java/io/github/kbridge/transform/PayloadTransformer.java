package io.github.kbridge.transform;

public interface PayloadTransformer {
	String mqTopic();
	String kafkaTopic();
	boolean isPubSubDomain();
}
