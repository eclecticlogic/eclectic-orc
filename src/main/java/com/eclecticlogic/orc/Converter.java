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

/**
 * Converts from a user-type U to an orc-compatible type T. Use this with the @OrcConverter annotation to convert custom data-types to
 * orc-compatible types.
 * @param <U> user-data type
 * @param <T> converted orc-compatible type
 */
public interface Converter<U, T> {

    /**
     * @return Class of the orc-compatible type.
     */
    Class<T> getConvertedClass();

    /**
     * @param instance Instance of your domain specific type.
     * @return Converted value.
     */
    T convert(U instance);
}
