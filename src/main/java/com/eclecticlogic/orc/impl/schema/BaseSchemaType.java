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

import com.eclecticlogic.orc.Orc;
import org.apache.orc.TypeDescription.Category;

/**
 * Created by kabram on 2/27/17.
 */
public class BaseSchemaType extends GeneratorSchemaType {
    protected String name;

    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


    public Integer getLength() {
        if (getCategory() == Category.CHAR) {
            return 1;
        }
        Orc orc = getAnnotation();
        if (orc == null || orc.length() == 0) {
            return null;
        }
        return orc.length();
    }


    public int getPrecision() {
        Orc orc = getAnnotation();
        return orc == null ? 0 : orc.precision();
    }


    public int getScale() {
        Orc orc = getAnnotation();
        return orc == null ? 0 : orc.scale();
    }


    public boolean isPrecisionFirst() {
        return getPrecision() != 0 && getPrecision() > 10;
    }


    public boolean isPrecisionLast() {
        return getPrecision() != 0 && getPrecision() <= 10;
    }


    public boolean isScaleFirst() {
        return isPrecisionLast();
    }


    public boolean isScaleLast() {
        return isPrecisionFirst();
    }

}
