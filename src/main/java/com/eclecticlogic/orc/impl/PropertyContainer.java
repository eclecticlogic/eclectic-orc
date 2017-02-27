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

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

/**
 * Created by kabram
 */
public class PropertyContainer<T> {

    private final Map<Method, PropertyDescriptor> propertiesByReadMethod;

    public PropertyContainer(Class<T> schemaClass) {
        Class<T> clz = schemaClass;
        BeanInfo info = null;
        try {
            info = Introspector.getBeanInfo(schemaClass);
        } catch (IntrospectionException e) {
            throw new RuntimeException(e);
        }
        PropertyDescriptor[] propertyDescriptors = info.getPropertyDescriptors();
        propertiesByReadMethod = Arrays.stream(propertyDescriptors) //
                .filter(it -> it.getReadMethod() != null) //
                .filter(it -> !it.getReadMethod().getName().equals("getClass"))
                // Filter out groovy specific meta-methods
                .filter(it -> it.getReadMethod().getReturnType().isPrimitive() || //
                        it.getReadMethod().getReturnType().isArray() || //
                        !it.getReadMethod().getReturnType().getPackage().getName().contains("groovy"))
                .collect(toMap(PropertyDescriptor::getReadMethod, it -> it));
    }


    public boolean isGetter(Method method) {
        return propertiesByReadMethod.containsKey(method);
    }


    public PropertyDescriptor getPropertyFor(Method method) {
        return propertiesByReadMethod.get(method);
    }

}
