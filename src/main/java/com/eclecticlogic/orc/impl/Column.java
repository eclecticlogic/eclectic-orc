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

package com.eclecticlogic.orc.impl;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Captures the elements of a schema column - name, column accessor function and sub-schema.
 * Created by kabram
 */
public class Column<T> {
    Supplier<String> nameFunction;
    Function<Object, Object> columnFunction;
    boolean delegated;
    SchemaSpi subSchema;

    public Supplier<String> getNameFunction() {
        return nameFunction;
    }

    public void setNameFunction(Supplier<String> nameFunction) {
        this.nameFunction = nameFunction;
    }

    public Function<Object, Object> getColumnFunction() {
        return columnFunction;
    }

    public void setColumnFunction(Function<Object, Object> columnFunction) {
        this.columnFunction = columnFunction;
    }

    public boolean isDelegated() {
        return delegated;
    }

    public void setDelegated(boolean delegated) {
        this.delegated = delegated;
    }

    public SchemaSpi getSubSchema() {
        return subSchema;
    }

    public void setSubSchema(SchemaSpi subSchema) {
        this.subSchema = subSchema;
    }
}
