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

    private final TypeDesc typeDescription = new TypeDesc(this);
    private final TypeInfo typeInfo = new TypeInfo(this);
    private final ComplexType complexType = new ComplexType(this);
    private final Template template = new Template(this);
    private Class<?> delegateClass;
    private boolean needsDelegate;

    public boolean isNeedsDelegate() {
        return needsDelegate;
    }

    public void setNeedsDelegate(boolean needsDelegate) {
        this.needsDelegate = needsDelegate;
    }

    public Class<?> getDelegateClass() {
        return delegateClass;
    }

    public void setDelegateClass(Class<?> delegateClass) {
        this.delegateClass = delegateClass;
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


    public Template getTemplate() {
        return template;
    }


    /**
     * @return true if the column vector (not the datatype) holds primitive values (e.g. LongVectorColumn holds primitive values, but
     * there is not a vector that holds primitive chars).
     */
    public boolean isPrimitiveVector() {
        return GeneratorUtil.getPrimitiveAccessorByType(getColumnClassType()) != null;
    }


    /**
     * @return Fully qualified class name of the Orc ColumnVector that implements this column.
     */
    public String getVectorClassName() {
        return GeneratorUtil.getVectorClassName(getCategory()).getName();
    }


}
