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
import com.eclecticlogic.orc.Orc;
import com.eclecticlogic.orc.OrcTemporal;
import com.eclecticlogic.orc.impl.bootstrap.GeneratorUtil;
import org.apache.orc.TypeDescription.Category;

import javax.persistence.Column;
import javax.persistence.Temporal;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Created by kabram on 2/27/17.
 */
public class AbstractSchemaColumn implements GenInfo {

    private final List<Method> accessorMethods = new ArrayList<>();
    private int columnIndex; // Column columnIndex for this if the entire structure were to be flattened.
    private Category _category; // lazy computed cateogry


    @Override
    public List<Method> getAccessorMethods() {
        return accessorMethods;
    }


    public int getColumnIndex() {
        return columnIndex;
    }


    public void setColumnIndex(int columnIndex) {
        this.columnIndex = columnIndex;
    }


    @Override
    public Class<?> getColumnClassType() {
        Method method = getLastAccessorMethod();
        Converter c = getConverter();
        return c == null ? method.getReturnType() : c.getConvertedClass();
    }


    @Override
    public Category getCategory() {
        if (_category == null) {
            _category = _getCategory();
        }
        return _category;
    }


    private Category _getCategory() {
        // TODO: Support converter definition.
        Class<?> clz = getColumnClassType();
        if (clz == null) {
            return Category.STRUCT;
        } else if (GeneratorUtil.getCategoryByType().containsKey(clz)) {
            return GeneratorUtil.getCategoryByType().get(clz);
        } else if (String.class.isAssignableFrom(clz)) {
            // Return STRING vs VARCHAR based on whether size is specified or not.
            Orc orc = getAnnotation(Orc.class);
            if (orc == null || orc.length() == 0) {
                // Check if jpa column annotation is present.
                Column col = getAnnotation(Column.class);
                if (col == null || col.length() == 0) {
                    return Category.STRING;
                }
                return Category.VARCHAR;
            }
            return Category.VARCHAR;
        } else if (BigDecimal.class.isAssignableFrom(clz)) {
            return Category.DECIMAL;
        } else if (LocalDate.class.isAssignableFrom(clz)) {
            return Category.DATE;
        } else if (ZonedDateTime.class.isAssignableFrom(clz) || LocalDateTime.class.isAssignableFrom(clz)) {
            return Category.TIMESTAMP;
        } else if (Date.class.isAssignableFrom(clz)) {
            OrcTemporal orcTemporal = getAnnotation(OrcTemporal.class);
            if (orcTemporal == null) {
                Temporal jpaTemporal = getAnnotation(Temporal.class);
                if (jpaTemporal == null) {
                    return Category.TIMESTAMP;
                }
                switch (jpaTemporal.value()) {
                    case DATE:
                        return Category.DATE;
                    case TIME:
                    case TIMESTAMP:
                        return Category.TIMESTAMP;
                }
            }
            switch (orcTemporal.value()) {
                case DATE:
                    return Category.DATE;
                case TIMESTAMP:
                    return Category.TIMESTAMP;
            }
        } else if (Iterable.class.isAssignableFrom(clz)) {
            return Category.LIST;
        }
        return Category.STRUCT;
    }


}
