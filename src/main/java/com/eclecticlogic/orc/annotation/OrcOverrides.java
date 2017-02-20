package com.eclecticlogic.orc.annotation;

import java.lang.annotation.*;

/**
 * Annotates a method as providing a field for ORC output.
 * Created by kabram on 2/20/17.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Inherited
public @interface OrcOverrides {
    /**
     * Name of the field. This will be used to populate the ORC fieldname in the schema.
     */
    String name();


    /**
     * @return Length of the field. Applicable only to string. If left empty, the output column will be of type
     * string. Otherwise the output column will be of type varchar(length).
     */
    int length() default 0;


    /**
     * @return The ordering of the column in the ORC schema.
     */
    int order();
}
