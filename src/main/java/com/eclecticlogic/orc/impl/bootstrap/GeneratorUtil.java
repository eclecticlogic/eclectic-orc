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

import com.eclecticlogic.orc.Orc;
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
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.apache.orc.TypeDescription.Category.*;

/**
 * A dumping ground for various static mappings!
 * Created by kabram
 */
public class GeneratorUtil {

    private final static Map<Class<?>, Category> categoriesByBasicType;
    private final static Map<Class<?>, Category> categoriesByAssignableType;
    private final static Map<Class<?>, String> primitiveAccessorByType;
    private final static Map<Class<? extends ColumnVector>, String> templateNameClassReinitByType;
    private final static Map<Class<?>, Object> defaultsByPrimitiveType;
    private final static Map<Category, String> typeDescriptionCreatorByCategory;
    private final static Map<Category, String> templateNameColumnSetterByCategory;
    private final static Map<Category, Class<? extends ColumnVector>> vectorClassesByCategory;

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
            categoriesByBasicType = Collections.unmodifiableMap(map);
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
        {
            Map<Class<?>, Object> map = new HashMap();
            map.put(Boolean.TYPE, Boolean.valueOf(false));
            map.put(Character.TYPE, Character.valueOf('\u0000'));
            map.put(Byte.TYPE, Byte.valueOf((byte)0));
            map.put(Short.TYPE, Short.valueOf((short)0));
            map.put(Integer.TYPE, Integer.valueOf(0));
            map.put(Long.TYPE, Long.valueOf(0L));
            map.put(Float.TYPE, Float.valueOf(0.0F));
            map.put(Double.TYPE, Double.valueOf(0.0D));
            defaultsByPrimitiveType = Collections.unmodifiableMap(map);
        }
        {
            Map<Class<?>, Category> map = new HashMap<>();
            map.put(BigDecimal.class, Category.DECIMAL);
            map.put(LocalDate.class, Category.DATE);
            map.put(LocalDateTime.class, Category.TIMESTAMP);
            map.put(ZonedDateTime.class, Category.TIMESTAMP);
            map.put(Date.class, Category.TIMESTAMP);
            map.put(Iterable.class, Category.LIST);
            categoriesByAssignableType = Collections.unmodifiableMap(map);
        }
        {
            Map<Category, String> map = new HashMap<>();
            map.put(Category.BINARY, "createBinary");
            map.put(Category.BOOLEAN, "createBoolean");
            map.put(Category.BYTE, "createByte");
            map.put(Category.CHAR, "createVarchar"); // AWS Athena doesn't seem to support char.
            map.put(Category.DATE, "createDate");
            map.put(Category.DECIMAL, "createDecimal");
            map.put(Category.DOUBLE, "createDouble");
            map.put(Category.FLOAT, "createFloat");
            map.put(Category.INT, "createInt");
            map.put(Category.LIST, "createList");
            map.put(Category.LONG, "createLong");
            map.put(Category.MAP, "createMap");
            map.put(Category.SHORT, "createShort");
            map.put(Category.STRING, "createString");
            map.put(Category.STRUCT, "createStruct");
            map.put(Category.TIMESTAMP, "createTimestamp");
            map.put(Category.UNION, "createUnion");
            map.put(Category.VARCHAR, "createVarchar");
            typeDescriptionCreatorByCategory = Collections.unmodifiableMap(map);
        }
        {
            Map<Category, Class<? extends ColumnVector>> map = new HashMap<>();
            map.put(Category.BINARY, BytesColumnVector.class);
            map.put(Category.BOOLEAN, LongColumnVector.class);
            map.put(Category.BYTE, LongColumnVector.class);
            map.put(Category.CHAR, BytesColumnVector.class);
            map.put(Category.DATE, LongColumnVector.class);
            map.put(Category.DECIMAL, DecimalColumnVector.class);
            map.put(Category.DOUBLE, DoubleColumnVector.class);
            map.put(Category.FLOAT, DoubleColumnVector.class);
            map.put(Category.INT, LongColumnVector.class);
            map.put(Category.LIST, ListColumnVector.class);
            map.put(Category.LONG, LongColumnVector.class);
            map.put(Category.MAP, MapColumnVector.class);
            map.put(Category.SHORT, LongColumnVector.class);
            map.put(Category.STRING, BytesColumnVector.class);
            map.put(Category.STRUCT, StructColumnVector.class);
            map.put(Category.TIMESTAMP, TimestampColumnVector.class);
            map.put(Category.UNION, UnionColumnVector.class);
            map.put(Category.VARCHAR, BytesColumnVector.class);
            vectorClassesByCategory = Collections.unmodifiableMap(map);
        }
        {
            Map<Category, String> map = new HashMap<>();
            map.put(Category.BINARY, "columnBinary");
            map.put(Category.BOOLEAN, "columnBoolean");
            map.put(Category.BYTE, "columnByte");
            map.put(Category.CHAR, "columnChar"); // AWS Athena doesn't seem to support char.
            map.put(Category.DATE, "columnDate");
            map.put(Category.DECIMAL, "columnDecimal");
            map.put(Category.DOUBLE, "columnDouble");
            map.put(Category.FLOAT, "columnFloat");
            map.put(Category.INT, "columnInt");
            map.put(Category.LIST, "columnList");
            map.put(Category.LONG, "columnLong");
            map.put(Category.MAP, "columnMap");
            map.put(Category.SHORT, "columnShort");
            map.put(Category.STRING, "columnVarchar");
            map.put(Category.STRUCT, "columnStruct");
            map.put(Category.TIMESTAMP, "columnTimestamp");
            map.put(Category.UNION, "columnUnion");
            map.put(Category.VARCHAR, "columnVarchar");
            templateNameColumnSetterByCategory = Collections.unmodifiableMap(map);
        }
    }

    public static Object getDefaultValueForPrimitiveType(Class<?> primitiveType) {
        return defaultsByPrimitiveType.get(primitiveType);
    }

    public static Category getCategoryByBasicType(Class<?> clz) {
        return categoriesByBasicType.get(clz);
    }


    public static Category getCategoryByAssignableType(Class<?> clz) {
        for (Class<?> aClz : categoriesByAssignableType.keySet()) {
            if (aClz.isAssignableFrom(clz)) {
                return categoriesByAssignableType.get(aClz);
            }
        }
        return null;
    }


    public static String getPrimitiveAccessorByType(Class<?> clz) {
        return primitiveAccessorByType.get(clz);
    }


    public static String getTypeDescriptionCreator(Category category) {
        return typeDescriptionCreatorByCategory.get(category);
    }


    public static Class<? extends ColumnVector> getVectorClassName(Category category) {
        return vectorClassesByCategory.get(category);
    }


    public static String getTemplateNameColumnSetter(Category category) {
        return templateNameColumnSetterByCategory.get(category);
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


    public static Optional<Method> getAnnotatedMethodInEnum(Class<? extends Enum<?>> clz) {
        return Arrays.stream(clz.getDeclaredMethods()) //
                .filter(it -> it.isAnnotationPresent(Orc.class)) //
                .filter(it -> it.getParameterCount() == 0) //
                .filter(it -> !Void.TYPE.equals(it.getReturnType())) //
                .findFirst();
    }
}
