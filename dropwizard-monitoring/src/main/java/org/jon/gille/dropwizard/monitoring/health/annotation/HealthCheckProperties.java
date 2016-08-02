package org.jon.gille.dropwizard.monitoring.health.annotation;

import org.jon.gille.dropwizard.monitoring.health.domain.Level;
import org.jon.gille.dropwizard.monitoring.health.domain.Type;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface HealthCheckProperties {

    Level level();

    Type type();

    String description() default "";

    String dependentOn() default "";

    String link() default "";

}
