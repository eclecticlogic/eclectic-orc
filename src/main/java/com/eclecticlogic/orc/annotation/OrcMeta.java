package com.eclecticlogic.orc.annotation;

import java.lang.annotation.*;

/**
 * Meta-orc annotation that allows a user-defined annotation to serve as a container for OrcOverrides.
 * Created by kabram on 2/20/17.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
@Inherited
public @interface OrcMeta {

    String value() default "";
}
