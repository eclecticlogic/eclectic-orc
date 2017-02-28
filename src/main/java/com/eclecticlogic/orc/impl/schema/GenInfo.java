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
import com.eclecticlogic.orc.OrcConverter;
import org.apache.orc.TypeDescription.Category;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;

/**
 * Created by kabram on 2/28/17.
 */
public interface GenInfo {

    default <A extends Annotation> A getAnnotation(Class<? extends A> clz) {
        return getLastAccessorMethod().isAnnotationPresent(clz) ? getLastAccessorMethod().getDeclaredAnnotation(clz) : null;
    }


    default Converter getConverter() {
        Method method = getLastAccessorMethod();
        if (method.isAnnotationPresent(OrcConverter.class)) {
            try {
                return method.getDeclaredAnnotation(OrcConverter.class).value().newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }


    /**
     * @return Category of concern.
     */
    Category getCategory();


    /**
     * @return Accessor methods
     */
    List<Method> getAccessorMethods();


    /**
     * @return The last accessor method in a chain of invocations for getting the value for the column.
     */
    default Method getLastAccessorMethod() {
        try {
            return getAccessorMethods().isEmpty() ? //
                    Object.class.getMethod("getClass") : getAccessorMethods().get(getAccessorMethods().size() - 1);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * @return Type of the column class, applying converters if necessary.
     */
    Class<?> getColumnClassType();
}
