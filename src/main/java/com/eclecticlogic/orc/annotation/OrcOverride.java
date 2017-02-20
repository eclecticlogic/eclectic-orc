package com.eclecticlogic.orc.annotation;

import java.lang.annotation.*;

/**
 * Annotates a method as providing a field for ORC output.
 * Created by kabram on 2/20/17.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Inherited
public @interface OrcOverride {
    /**
     * @return Name of the field to override. This will be used to populate the ORC fieldname in the schema.
     */
    String name() default "";


    /**
     * @return Java bean property to override (if name is the same).
     */
    String property() default "";


    /**
     * @return The new orc column definition. It is not necessary to repeat any attribute you don't wish to override.
     */
    Orc orc();
}
