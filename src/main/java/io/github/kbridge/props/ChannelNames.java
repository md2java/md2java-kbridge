package io.github.kbridge.props;

public enum ChannelNames {

	TO_MQ("toMqChannel"), TO_KAFKA("toKafkaChannel");

	private String bean;

	ChannelNames(String bean) {
		this.bean = bean;
	}

	public String bean() {
		return bean;
	}

}
