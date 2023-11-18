package io.github.kbridge.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.ibm.mq.spring.boot.MQAutoConfiguration;

@ConditionalOnProperty(name = "app.mq.provider.name",havingValue = "ibmmq",matchIfMissing = true)
@Configuration
@Import(MQAutoConfiguration.class)
public class IbmMQActivator {

}
