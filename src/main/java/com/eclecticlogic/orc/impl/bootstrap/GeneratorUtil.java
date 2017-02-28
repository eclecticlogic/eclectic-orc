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

package com.eclecticlogic.orc.impl.bootstrap;

import org.apache.hadoop.hive.ql.exec.vector.BytesColumnVector;
import org.apache.hadoop.hive.ql.exec.vector.ColumnVector;
import org.apache.hadoop.hive.ql.exec.vector.DecimalColumnVector;
import org.apache.hadoop.hive.ql.exec.vector.DoubleColumnVector;
import org.apache.hadoop.hive.ql.exec.vector.ListColumnVector;
import org.apache.hadoop.hive.ql.exec.vector.LongColumnVector;
import org.apache.hadoop.hive.ql.exec.vector.MapColumnVector;
import org.apache.hadoop.hive.ql.exec.vector.StructColumnVector;
import org.apache.hadoop.hive.ql.exec.vector.TimestampColumnVector;
import org.apache.hadoop.hive.ql.exec.vector.UnionColumnVector;
import org.apache.orc.TypeDescription.Category;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.apache.orc.TypeDescription.Category.*;

/**
 * Created by kabram
 */
public class GeneratorUtil {

    private final static Map<Class<?>, Category> categoriesByType;
    private final static Map<Class<?>, String> primitiveAccessorByType;
    private final static Map<Class<? extends ColumnVector>, String> templateNameClassReinitByType;

    static {
        {
            Map<Class<?>, Category> map = new HashMap();
            map.put(Boolean.TYPE, Category.BOOLEAN);
            map.put(Boolean.class, Category.BOOLEAN);
            map.put(Character.TYPE, Category.CHAR);
            map.put(Character.class, Category.CHAR);
            map.put(Byte.TYPE, Category.BYTE);
            map.put(Byte.class, Category.BYTE);
            map.put(Short.TYPE, Category.SHORT);
            map.put(Short.class, Category.SHORT);
            map.put(Integer.TYPE, Category.INT);
            map.put(Integer.class, Category.INT);
            map.put(Long.TYPE, Category.LONG);
            map.put(Long.class, Category.LONG);
            map.put(Float.TYPE, Category.FLOAT);
            map.put(Float.class, Category.FLOAT);
            map.put(Double.TYPE, Category.DOUBLE);
            map.put(Double.class, Category.DOUBLE);
            categoriesByType = Collections.unmodifiableMap(map);
        }
        {
            Map<Class<?>, String> map = new HashMap();
            map.put(Boolean.class, "booleanValue()");
            map.put(Byte.class, "byteValue()");
            map.put(Character.class, "charValue()");
            map.put(Short.class, "shortValue()");
            map.put(Integer.class, "intValue()");
            map.put(Long.class, "longValue()");
            map.put(Float.class, "floatValue()");
            map.put(Double.class, "doubleValue()");
            primitiveAccessorByType = Collections.unmodifiableMap(map);
        }
        {
            Map<Class<? extends ColumnVector>, String> map = new HashMap();
            map.put(BytesColumnVector.class, "initBytesList");
            map.put(LongColumnVector.class, "initLongList");
            map.put(DoubleColumnVector.class, "initDoubleList");
            map.put(DecimalColumnVector.class, "initDecimalList");
            map.put(TimestampColumnVector.class, "initTimestampList");
            templateNameClassReinitByType = Collections.unmodifiableMap(map);
        }
    }

    public static Map<Class<?>, Category> getCategoryByType() {
        return categoriesByType;
    }


    public static Map<Class<?>, String> getPrimitiveAccessorByType() {
        return primitiveAccessorByType;
    }


    public static String getTypeDescriptionCreator(Category category) {
        switch (category) {
            case BINARY:
                return "createBinary";
            case BOOLEAN:
                return "createBoolean";
            case BYTE:
                return "createByte";
            case CHAR:
                // TODO Defined as varchar because athena doesn't seem to support char.
                return "createVarchar";
            case DATE:
                return "createDate";
            case DECIMAL:
                return "createDecimal";
            case DOUBLE:
                return "createDouble";
            case FLOAT:
                return "createFloat";
            case INT:
                return "createInt";
            case LIST:
                return "createList";
            case LONG:
                return "createLong";
            case MAP:
                return "createMap";
            case SHORT:
                return "createShort";
            case STRING:
                return "createVarchar";
            case STRUCT:
                return "createStruct";
            case TIMESTAMP:
                return "createTimestamp";
            case UNION:
                return "createUnion";
            case VARCHAR:
                return "createVarchar";
            default:
                return null;
        }
    }


    public static Class<? extends ColumnVector> getVectorClassName(Category category) {
        switch (category) {
            case BINARY:
            case CHAR:
            case VARCHAR:
            case STRING:
                return BytesColumnVector.class;
            case BOOLEAN:
            case BYTE:
            case SHORT:
            case INT:
            case LONG:
            case DATE:
                return LongColumnVector.class;
            case DECIMAL:
                return DecimalColumnVector.class;
            case FLOAT:
            case DOUBLE:
                return DoubleColumnVector.class;
            case LIST:
                return ListColumnVector.class;
            case MAP:
                return MapColumnVector.class;
            case STRUCT:
                return StructColumnVector.class;
            case TIMESTAMP:
                return TimestampColumnVector.class;
            case UNION:
                return UnionColumnVector.class;
            default:
                return null;
        }
    }


    public static String getTemplateNameColumnSetter(Category category) {
        switch (category) {
            case BINARY:
                return "columnBinary";
            case BOOLEAN:
                return "columnBoolean";
            case BYTE:
                return "columnByte";
            case CHAR:
                return "columnChar";
            case DATE:
                return "columnDate";
            case DECIMAL:
                return "columnDecimal";
            case DOUBLE:
                return "columnDouble";
            case FLOAT:
                return "columnFloat";
            case INT:
                return "columnInt";
            case LIST:
                return "columnList";
            case LONG:
                return "columnLong";
            case MAP:
                return "columnMap";
            case SHORT:
                return "columnShort";
            case STRING:
                return "columnVarchar";
            case STRUCT:
                return "columnStruct";
            case TIMESTAMP:
                return "columnTimestamp";
            case UNION:
                return "columnUnion";
            case VARCHAR:
                return "columnVarchar";
            default:
                return null;
        }
    }


    public static boolean isSupportsLengthSpecification(Category category) {
        return Arrays.stream(new Category[]{CHAR, VARCHAR, STRING}).anyMatch(it -> it == category);
    }


    public static String getTemplateNameListReinit(Category category) {
        return templateNameClassReinitByType.get(getVectorClassName(category));
    }


    /**
     * @param method method reference
     * @return Javabean property name for a getter or the method name itself turned into camel case.
     */
    public static String getPropertyName(Method method) {
        int index = 0;
        if (method.getName().startsWith("get") && //
                method.getName().length() > 3 && //
                Character.isUpperCase(method.getName().charAt(3))) {
            index = 3;
        } else if (method.getName().startsWith("is") && //
                method.getName().length() > 2 && //
                Character.isUpperCase(method.getName().charAt(2))) {
            index = 2;
        }
        String propertyName = method.getName().substring(index);
        if (Character.isUpperCase(propertyName.charAt(0))) {
            propertyName = Character.toLowerCase(propertyName.charAt(0)) + propertyName.substring(1);
        }
        return propertyName;
    }
}
