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

import com.eclecticlogic.orc.Converter;
import com.eclecticlogic.orc.OrcList;

import java.lang.reflect.Method;

/**
 * Created by kabram
 */
public class ListChildSchemaColumn extends SchemaColumn {

    private final SchemaColumn parent;


    public ListChildSchemaColumn(SchemaColumn parent) {
        this.parent = parent;
    }


    @Override
    public Method getLastAccessorMethod() {
        return parent.getLastAccessorMethod();
    }


    public Class<?> getListEntryType() {
        OrcList orcCollection = getLastAccessorMethod().getDeclaredAnnotation(OrcList.class);
        if (orcCollection == null) {
            throw new RuntimeException("@OrcList annotation must be present for list column types.");
        }
        return orcCollection.entryType();
    }


    @Override
    public Class<?> getColumnClassType() {
        OrcList orcCollection = getLastAccessorMethod().getDeclaredAnnotation(OrcList.class);
        if (orcCollection == null) {
            throw new RuntimeException("@OrcList annotation must be present for list column types.");
        }
        Converter c = getConverter();
        if (c == null) {
            return orcCollection.entryType();
        } else {
            return c.getConvertedClass();
        }
    }


    /**
     * @return Check if the parent @OrcList has a converter defined.
     */
    @Override
    public Converter getConverter() {
        OrcList orcCollection = getLastAccessorMethod().getDeclaredAnnotation(OrcList.class);
        if (orcCollection.converter().equals(OrcList.DEFAULT.class)) {
            return null;
        } else {
            try {
                return orcCollection.converter().newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
