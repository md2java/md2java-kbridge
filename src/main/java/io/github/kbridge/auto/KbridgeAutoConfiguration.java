package io.github.kbridge.auto;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan("io.github.kbridge")
@PropertySource(value = "classpath:kbridge.properties")
public class KbridgeAutoConfiguration {

}
