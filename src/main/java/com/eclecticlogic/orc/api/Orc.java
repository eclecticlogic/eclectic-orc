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

package com.eclecticlogic.orc.api;

import java.lang.annotation.*;

/**
 * Annotates additional properties of a column for type definition.
 * Created by kabram
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Inherited
public @interface Orc {

    /**
     * @return Length of the field. Applicable only to string. If left empty, the output column will be of type
     * string. Otherwise the output column will be of type varchar(length).
     */
    int length() default 0;


    /**
     * @return Precision for BigDecimal type
     */
    int precision() default 0;


    /**
     * @return Scale for BigDecimal type.
     */
    int scale() default 0;
}
