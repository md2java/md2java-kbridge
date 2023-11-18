package io.github.kbridge.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jms.activemq.ActiveMQAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@ConditionalOnProperty(name = "app.mq.provider.name",havingValue = "activemq")
@Configuration
@Import(ActiveMQAutoConfiguration.class)
public class ActiveMQActivator {

}
