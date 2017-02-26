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
    final SchemaType schemaType;
    final List<String> fieldNames = new ArrayList<>();
    final List<String> accessorNames = new ArrayList<>();
    final List<String> currentFields = new ArrayList<>();


    public SchemaSpiImpl(Class<T> clz) {
        schemaClz = clz;
        proxy = proxyManager.generate(clz);
        schemaType = new SchemaType();
        schemaType.setCreateInstruction(getCreateInstruction(clz));
    }


    @Override
    public Class<T> getSchemaClass() {
        return schemaClz;
    }

    @Override
    public Schema<T> field(Function<T, Object> fieldFunction) {
        return field(fieldFunction, () -> currentFields.get(currentFields.size() - 1));
    }


    @Override
    public Schema<T> field(String name, Function<T, Object> fieldFunction) {
        return field(fieldFunction, () -> name);
    }


    Schema<T> field(Function<T, Object> fieldFunction, Supplier<String> nameFunction) {
        fieldFunction.apply(proxy);
        accessorNames.add(String.join(".", currentFields));
        fieldNames.add(nameFunction.get());
        currentFields.clear();
        return this;
    }


    @Override
    public void add(Method accessor) {
        currentFields.add(proxyManager.getPropertyFor(accessor).getName());
    }

    String getCreateInstruction(Class<?> clz) {
        return "createStruct";
    }
}
