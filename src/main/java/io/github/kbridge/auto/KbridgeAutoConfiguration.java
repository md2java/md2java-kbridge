package io.github.kbridge.auto;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jms.activemq.ActiveMQAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import com.ibm.mq.spring.boot.MQAutoConfiguration;

@Configuration
@EnableAutoConfiguration(exclude = {MQAutoConfiguration.class,ActiveMQAutoConfiguration.class})
@ComponentScan("io.github.kbridge")
@PropertySource(value = "classpath:kbridge.properties")
public class KbridgeAutoConfiguration {

}
