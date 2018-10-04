/*
 * Copyright (c) 2017 Eclectic Logic LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.eclecticlogic.orc;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotates a list (strictly, any derivative of java.lang.Iterable) return type to denote child data type and average collection size.
 * Created by kabram
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Inherited
public @interface OrcList {

    /**
     * @return Type of the elements. Due to type-erasure, this information is lost at runtime in code. Therefore we attempt to explicitly
     * capture it.
     */
    Class<?> entryType();


    /**
     * @return Average size in bytes of the elements.
     */
    int elementSize() default 1;


    /**
     * @return Average size of elements in the collection.
     */
    int averageSize() default 1;


    /**
     * @return A converter for each element of the class. T
     */
    Class<? extends Converter<?, ?>> converter() default DEFAULT.class;


    /**
     * An elaborate workaround for a vexing issue with not being able to use null as the default value.
     * Refer to http://stackoverflow.com/questions/1178104/error-setting-a-default-null-value-for-an-annotations-field
     */
    static abstract class DEFAULT implements Converter<String, String> {}
}
