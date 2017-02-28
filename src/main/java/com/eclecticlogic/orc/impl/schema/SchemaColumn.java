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

package com.eclecticlogic.orc.impl.schema;

import com.eclecticlogic.orc.impl.bootstrap.GeneratorUtil;

import java.lang.reflect.Method;

/**
 * Created by kabram
 */
public class SchemaColumn extends AbstractSchemaColumn {

    private final TypeDesc typeDescription;
    private final TypeInfo typeInfo;
    private final ComplexType complexType;


    public SchemaColumn() {
        this.typeDescription = new TypeDesc(this);
        this.typeInfo = new TypeInfo(this);
        this.complexType = new ComplexType(this);
    }


    public ComplexType getComplexType() {
        return complexType;
    }


    public TypeDesc getTypeDescription() {
        return typeDescription;
    }


    public TypeInfo getTypeInfo() {
        return typeInfo;
    }


    public String getPropertyAccess() {
        StringBuilder builder = new StringBuilder();
        for (Method accessorMethod : getAccessorMethods()) {
            builder.append(".").append(accessorMethod.getName()).append("()");
        }
        return builder.toString();
    }


    /**
     * @return true if the column vector (not the datatype) holds primitive values (e.g. LongVectorColumn holds primitive values, but
     * there is not a vector that holds primitive chars).
     */
    public boolean isPrimitiveVector() {
        return GeneratorUtil.getPrimitiveAccessorByType().containsKey(getColumnClassType());
    }


    /**
     * @return Fully qualified class name of the Orc ColumnVector that implements this column.
     */
    public String getVectorClassName() {
        return GeneratorUtil.getVectorClassName(getCategory()).getName();
    }


}
