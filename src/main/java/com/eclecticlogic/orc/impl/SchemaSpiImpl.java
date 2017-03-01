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

import com.eclecticlogic.orc.Schema;
import com.eclecticlogic.orc.impl.bootstrap.GeneratorUtil;
import com.eclecticlogic.orc.impl.schema.ListChildSchemaColumn;
import com.eclecticlogic.orc.impl.schema.SchemaColumn;
import org.apache.orc.TypeDescription.Category;

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
    final List<Column> columns = new ArrayList<>();
    SchemaColumn currentSchemaColumn;
    String lastAccessedProperty;

    public SchemaSpiImpl(Class<T> clz) {
        schemaClz = clz;
        proxy = new ProxyManager<>(this).generate(clz);
    }


    @Override
    public Class<T> getSchemaClass() {
        return schemaClz;
    }


    @Override
    public Schema<T> column(Function<T, Object> columnFunction) {
        return column(() -> lastAccessedProperty, columnFunction);
    }


    @Override
    public Schema<T> column(String name, Function<T, Object> columnFunction) {
        return column(() -> name, columnFunction);
    }


    Schema<T> column(Supplier<String> nameFunction, Function<T, Object> columnFunction) {
        Column column = new Column();
        column.setNameFunction(nameFunction);
        column.setColumnFunction(columnFunction);
        columns.add(column);
        return this;
    }


    @Override
    public SchemaColumn compile() {
        SchemaColumn struct = new SchemaColumn();
        for (Column<T> column : columns) {
            lastAccessedProperty = null;
            currentSchemaColumn = new SchemaColumn();
            column.getColumnFunction().apply(proxy);
            currentSchemaColumn.getTypeDescription().setName(column.getNameFunction().get());
            struct.getComplexType().getStructChildren().add(currentSchemaColumn);
            // Special types.
             if (currentSchemaColumn.getCategory() == Category.LIST) {
                currentSchemaColumn.getComplexType().setListChild(new ListChildSchemaColumn(currentSchemaColumn));
            }
        }
        computeColumnIndices(struct.getComplexType().getStructChildren(), 0);
        return struct;
    }


    int computeColumnIndices(List<SchemaColumn> schemaColumns, int index) {
        for (SchemaColumn column : schemaColumns) {
            if (column.getCategory() == Category.STRUCT) {
                index = computeColumnIndices(column.getComplexType().getStructChildren(), index);
            } else {
                column.setColumnIndex(index++);
            }
        }
        return index;
    }


    @Override
    public void add(Method accessor) {
        currentSchemaColumn.getAccessorMethods().add(accessor);
        lastAccessedProperty = GeneratorUtil.getPropertyName(accessor);
    }


}
