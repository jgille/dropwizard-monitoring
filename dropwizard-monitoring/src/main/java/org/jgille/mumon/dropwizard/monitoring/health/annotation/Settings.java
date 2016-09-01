package org.jgille.mumon.dropwizard.monitoring.health.annotation;

import org.jgille.mumon.dropwizard.monitoring.health.domain.Level;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Settings {

    Level level();

    String type() default "";

    String description() default "";

    String dependentOn() default "";

    String link() default "";

}
