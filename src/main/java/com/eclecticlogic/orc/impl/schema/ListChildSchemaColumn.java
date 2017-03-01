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

import com.eclecticlogic.orc.OrcCollection;

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


    @Override
    public Class<?> getColumnClassType() {
        OrcCollection orcCollection = getLastAccessorMethod().getDeclaredAnnotation(OrcCollection.class);
        if (orcCollection == null) {
            throw new RuntimeException("@OrcCollection annotation must be present for list column types.");
        }
        return orcCollection.entryType();
    }
}
