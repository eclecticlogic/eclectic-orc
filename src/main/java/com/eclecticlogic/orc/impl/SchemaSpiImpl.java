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

import com.eclecticlogic.orc.api.Schema;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Created by kabram
 */
public class SchemaSpiImpl<T> implements SchemaSpi<T> {

    final Class<T> schemaClz;
    final T proxy;
    final ProxyManager<T> proxyManager = new ProxyManager<>(this);
    final List<Column> schemaColumns = new ArrayList<>();
    SchemaType currentSchemaType;


    public SchemaSpiImpl(Class<T> clz) {
        schemaClz = clz;
        proxy = proxyManager.generate(clz);
    }


    @Override
    public Class<T> getSchemaClass() {
        return schemaClz;
    }

    @Override
    public Schema<T> column(Function<T, Object> columnFunction) {
        return column(() -> currentSchemaType.lastAccessorProperty, columnFunction);
    }


    @Override
    public Schema<T> column(String name, Function<T, Object> columnFunction) {
        return column(() -> name, columnFunction);
    }


    Schema<T> column(Supplier<String> nameFunction, Function<T, Object> columnFunction) {
        Column column = new Column();
        column.setNameFunction(nameFunction);
        column.setColumnFunction(columnFunction);
        schemaColumns.add(column);
        return this;
    }

    public SchemaType compile() {
        SchemaType struct = new SchemaType();
        struct.setCreateInstruction(getCreateInstruction(schemaClz));
        for (Column<T> column: schemaColumns) {
            currentSchemaType = new SchemaType();
            column.getColumnFunction().apply(proxy);
            currentSchemaType.setName(column.getNameFunction().get());
            struct.getStructChildren().add(currentSchemaType);
        }
        return struct;
    }


    @Override
    public void add(Method accessor) {
        currentSchemaType.getAccessorMethods().add(accessor);
        currentSchemaType.setLastAccessorProperty(proxyManager.getPropertyFor(accessor).getName());
    }

    String getCreateInstruction(Class<?> clz) {
        return "createStruct";
    }
}
