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
import com.eclecticlogic.orc.OrcTemporal;
import com.eclecticlogic.orc.OrcTemporalType;
import com.eclecticlogic.orc.impl.bootstrap.WriterUtils;
import org.apache.orc.TypeDescription.Category;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by kabram on 2/27/17.
 */
public class GeneratorSchemaType {
    private final static Map<Class<?>, Category> createInstructionsByType;

    static {
        HashMap map = new HashMap();
        map.put(Boolean.TYPE, Category.BOOLEAN);
        map.put(Character.TYPE, Category.CHAR);
        map.put(Byte.TYPE, Category.BYTE);
        map.put(Short.TYPE, Category.SHORT);
        map.put(Integer.TYPE, Category.INT);
        map.put(Long.TYPE, Category.LONG);
        map.put(Float.TYPE, Category.FLOAT);
        map.put(Double.TYPE, Category.DOUBLE);
        createInstructionsByType = Collections.unmodifiableMap(map);
    }

    protected final List<Method> accessorMethods = new ArrayList<>();
    protected String lastAccessedProperty;
    protected int columnIndex; // Column columnIndex for this if the entire structure were to be flattened.
    private Category _category; // lazy computed cateogry


    public List<Method> getAccessorMethods() {
        return accessorMethods;
    }


    public String getLastAccessedProperty() {
        return lastAccessedProperty;
    }


    public void setLastAccessedProperty(String lastAccessedProperty) {
        this.lastAccessedProperty = lastAccessedProperty;
    }


    public int getColumnIndex() {
        return columnIndex;
    }


    public void setColumnIndex(int columnIndex) {
        this.columnIndex = columnIndex;
    }


    public boolean isPrimitive() {
        return getLastAccessorMethod().getReturnType().isPrimitive();
    }


    protected Method getLastAccessorMethod() {
        return accessorMethods.isEmpty() ? null : accessorMethods.get(accessorMethods.size() - 1);
    }


    protected Orc getAnnotation() {
        Method method = getLastAccessorMethod();
        if (method == null) {
            return null;
        }
        return method.isAnnotationPresent(Orc.class) ? method.getDeclaredAnnotation(Orc.class) : null;
    }


    public Category getCategory() {
        if (_category == null) {
            _category = _getCategory();
        }
        return _category;
    }


    private Category _getCategory() {
        // TODO: Support converter definition.
        if (getLastAccessorMethod() == null) {
            return Category.STRUCT;
        }
        Class<?> clz = getLastAccessorMethod().getReturnType();
        if (clz.isPrimitive()) {
            return createInstructionsByType.get(clz);
        } else if (String.class.isAssignableFrom(clz)) {
            return Category.VARCHAR;
        } else if (BigDecimal.class.isAssignableFrom(clz)) {
            return Category.DECIMAL;
        } else if (LocalDate.class.isAssignableFrom(clz)) {
            return Category.DATE;
        } else if (ZonedDateTime.class.isAssignableFrom(clz) || LocalDateTime.class.isAssignableFrom(clz)) {
            return Category.TIMESTAMP;
        } else if (Date.class.isAssignableFrom(clz)) {
            if (getLastAccessorMethod().isAnnotationPresent(OrcTemporal.class)) {
                OrcTemporalType temporalType = getLastAccessorMethod().getDeclaredAnnotation(OrcTemporal.class).value();
                switch (temporalType) {
                    case DATE:
                        return Category.DATE;
                    case TIMESTAMP:
                        return Category.TIMESTAMP;
                }
            }
            return Category.TIMESTAMP;
        } else if (List.class.isAssignableFrom(clz)) {
            return Category.LIST;
        }
        return Category.STRUCT;
    }


    public String getCreateCategory() {
        return WriterUtils.getTypeDescriptionCreator(getCategory());
    }


    public String getVectorClassName() {
        return WriterUtils.getVectorClassName(getCategory());
    }


    public String getPropertyAccess() {
        StringBuilder builder = new StringBuilder();
        for (Method accessorMethod : accessorMethods) {
            builder.append(".").append(accessorMethod.getName()).append("()");
        }
        return builder.toString();
    }


    public boolean isTypeBinary() {
        return getCategory() == Category.BINARY;
    }


    public boolean isTypeChar() {
        return getCategory() == Category.CHAR;
    }


    public boolean isTypeVarchar() {
        return getCategory() == Category.VARCHAR;
    }


    public boolean isTypeString() {
        return getCategory() == Category.STRING;
    }


    public boolean isTypeBoolean() {
        return getCategory() == Category.BOOLEAN;
    }


    public boolean isTypeByte() {
        return getCategory() == Category.BYTE;
    }


    public boolean isTypeShort() {
        return getCategory() == Category.SHORT;
    }


    public boolean isTypeInt() {
        return getCategory() == Category.INT;
    }


    public boolean isTypeLong() {
        return getCategory() == Category.LONG;
    }


    public boolean isTypeDate() {
        return getCategory() == Category.DATE;
    }


    public boolean isTypeDecimal() {
        return getCategory() == Category.DECIMAL;
    }


    public boolean isTypeFloat() {
        return getCategory() == Category.FLOAT;
    }


    public boolean isTypeDouble() {
        return getCategory() == Category.DOUBLE;
    }


    public boolean isTypeList() {
        return getCategory() == Category.LIST;
    }


    public boolean isTypeMap() {
        return getCategory() == Category.MAP;
    }


    public boolean isTypeStruct() {
        return getCategory() == Category.STRUCT;
    }


    public boolean isTypeTimestamp() {
        return getCategory() == Category.TIMESTAMP;
    }


    public boolean isTypeUnion() {
        return getCategory() == Category.UNION;
    }
}
