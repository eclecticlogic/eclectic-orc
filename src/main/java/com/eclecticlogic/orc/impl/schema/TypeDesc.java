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
import com.eclecticlogic.orc.impl.bootstrap.GeneratorUtil;
import org.apache.orc.TypeDescription;

import javax.persistence.Column;

/**
 * Models attributes for org.apache.orc.TypeDescription
 * Created by kabram
 */
public class TypeDesc {

    private String name;
    private final GenInfo genInfo;


    public TypeDesc(GenInfo genInfo) {
        this.genInfo = genInfo;
    }


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


    public String getCreateMethod() {
        return GeneratorUtil.getTypeDescriptionCreator(genInfo.getCategory());
    }



    /**
     * @return Length to be used for varchar (or string) data type.
     */
    public Integer getLength() {
        if (genInfo.getCategory() == TypeDescription.Category.CHAR) {
            return 1;
        } else if (!GeneratorUtil.isSupportsLengthSpecification(genInfo.getCategory())) {
            return null;
        }
        Orc orc = genInfo.getAnnotation(Orc.class);
        if (orc == null) {
            // Check if jpa column annotation is present.
            Column col = genInfo.getAnnotation(Column.class);
            if (col == null || col.length() == 0) {
                return null;
            }
            return col.length();
        }
        return orc.length();
    }


    /**
     * @return precision to be used for Decimal data type.
     */
    public int getPrecision() {
        Orc orc = genInfo.getAnnotation(Orc.class);
        if (orc == null) {
            Column col = genInfo.getAnnotation(Column.class);
            return col == null ? 0 : col.precision();
        }
        return orc.precision();
    }


    /**
     * @return scale to be used for Decimal data type.
     */
    public int getScale() {
        Orc orc = genInfo.getAnnotation(Orc.class);
        if (orc == null) {
            Column col = genInfo.getAnnotation(Column.class);
            return col == null ? 0 : col.scale();
        }
        return orc.scale();
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
