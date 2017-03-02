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
import java.util.Date;
import java.util.List;
import java.util.Optional;

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
            _category = _getCategory(getColumnClassType());
        }
        return _category;
    }


    @SuppressWarnings("unchecked")
    private Category _getCategory(Class<?> clz) {
        if (clz == null) {
            return Category.STRUCT;
        } else if (GeneratorUtil.getCategoryByBasicType(clz) != null) {
            return GeneratorUtil.getCategoryByBasicType(clz);
        } else if (String.class.isAssignableFrom(clz)) {
            // Return STRING vs VARCHAR based on whether size is specified or not.
            Orc orc = getAnnotation(Orc.class);
            if (orc == null) {
                // Check if jpa column annotation is present.
                Column col = getAnnotation(Column.class);
                if (col == null || col.length() == 0) {
                    return Category.STRING;
                }
                return Category.VARCHAR;
            }
            return orc.length() == 0 ? Category.STRING : Category.VARCHAR;
        } else if (GeneratorUtil.getCategoryByAssignableType(clz) != null) {
            Category category = GeneratorUtil.getCategoryByAssignableType(clz);
            return category == Category.TIMESTAMP ? getAnnotationBasedCategory() : category;
        } else if (Date.class.isAssignableFrom(clz)) {

        } else if (Enum.class.isAssignableFrom(clz)) {
            return getEnumCategory((Class<? extends Enum<?>>) clz);
        }
        return Category.STRUCT;
    }


    /**
     * @return DATE or TIMESTAMP based on presence of @OrcTemporal annotation.
     */
    protected Category getAnnotationBasedCategory() {
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
        return Category.TIMESTAMP;
    }


    /**
     * Look for orc annotation in any of the methods. If found, return a category based on the return type of that method.
     * Otherwise we will call name() and therefore the Category is STRING.
     * @param clz
     * @return
     */
    protected Category getEnumCategory(Class<? extends Enum<?>> clz) {
        Optional<Method> annotatedMethod = GeneratorUtil.getAnnotatedMethodInEnum(clz);
        // Assumed to be a call to name() and therefore a string
        return annotatedMethod.map(m -> _getCategory(m.getReturnType())).orElse(Category.STRING);
    }

}
