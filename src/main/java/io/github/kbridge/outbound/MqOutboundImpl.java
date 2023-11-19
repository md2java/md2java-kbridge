package io.github.kbridge.outbound;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Component;

import io.github.kbridge.props.AppConstants;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class MqOutboundImpl {
	
	@Autowired
	private InternalGateway internalGateway;

	
	@ServiceActivator(inputChannel = "toMqChannel")
	public void toMqHandler(GenericMessage<Object> message) {
		log.debug("received from toMqChannel: {} ", message.getPayload());
		internalGateway.sendToMq(message);
		log.debug("sent to mq {} ",message.getHeaders().get(AppConstants.MQ_TOPIC));

	}

}
