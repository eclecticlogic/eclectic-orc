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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kabram
 */
public class SchemaType {
    String name;
    String createInstruction;
    Integer length;
    int precision, scale;
    List<SchemaType> structChildren = new ArrayList<>();
    SchemaType listChild;
    SchemaType mapKey;
    SchemaType mapValue;

    public boolean isStructType() {
        return !structChildren.isEmpty();
    }

    public boolean isListType() {
        return listChild != null;
    }

    public boolean isMapType() {
        return mapKey != null;
    }

    public List<SchemaType> getStructChildren() {
        return structChildren;
    }

    public SchemaType getListChild() {
        return listChild;
    }

    public SchemaType getMapKey() {
        return mapKey;
    }

    public SchemaType getMapValue() {
        return mapValue;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreateInstruction() {
        if (isStructType()) {
            return "createStruct";
        } else if (isListType()) {
            return "createList";
        } else if (isMapType()) {
            return "createMap";
        }
        return createInstruction;
    }

    public void setCreateInstruction(String createInstruction) {
        this.createInstruction = createInstruction;
    }


    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public int getPrecision() {
        return precision;
    }

    public void setPrecision(int precision) {
        this.precision = precision;
    }

    public int getScale() {
        return scale;
    }

    public void setScale(int scale) {
        this.scale = scale;
    }


    public boolean isPrecisionFirst() {
        return precision != 0 && precision > 10;
    }


    public boolean isPrecisionLast() {
        return precision != 0 && precision <= 10;
    }


    public boolean isScaleFirst() {
        return scale != 0 && isPrecisionLast();
    }

    public boolean isScaleLast() {
        return scale != 0 && isPrecisionFirst();
    }
}
