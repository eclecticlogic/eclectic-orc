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

import java.util.function.Function;

/**
 * Interface to define your orc file schema. The columns are defined in the order in which you call the column functions.
 *
 * Created by kabram
 */
public interface Schema<T> {

    /**
     * @param fieldFunction An instance of the type T will be passed to this function and you are expected to call the appropriate
     *                      method to define the column. You are not restricted to just java-bean getters. Any method that takes no
     *                      parameter and returns a non-void type can be called. You can also chain method calls (e.g. getXyz().getPqr())
     *                      to get at sub-attributes. The name of the column is derived from the last method to be invoked.
     *
     * @return Self-reference for fluent interface buildout.
     */
    Schema<T> column(Function<T, Object> fieldFunction);

    /**
     * @param name An explicit name to be used for the column.
     * @param columnFunction Same as above
     * @return self-reference for fluent interface.
     */
    Schema<T> column(String name, Function<T, Object> columnFunction);
}
