package io.github.kbridge;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Import;

import io.github.kbridge.auto.KbridgeAutoConfiguration;

@Documented
@Retention(RUNTIME)
@Target(TYPE)
@Import(value = KbridgeAutoConfiguration.class)
public @interface EnableKbridge {

}
