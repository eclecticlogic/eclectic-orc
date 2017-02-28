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

import net.sf.cglib.proxy.CallbackFilter;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static java.util.stream.Collectors.*;

/**
 * Filter to decide whether to intercept the method or not.
 * Created by kabram
 */
public class SchemaFilter<T> implements CallbackFilter {

    private final Class<T> clz;
    private final Set<Method> allowedMethods;
    private static final Set<String> disallowed = new HashSet(Arrays.asList("getClass", "notify", "wait", "notifyAll"));

    public SchemaFilter(Class<T> clz) {
        this.clz = clz;
        allowedMethods = Arrays.stream(clz.getMethods()) //
                // No parameters
                .filter(it -> it.getParameterCount() == 0) //
                // Not void return type
                .filter(it -> !it.getReturnType().isAssignableFrom(Void.class)) //
                .filter(it -> !disallowed.contains(it.getName())) //
                // Filter out groovy specific meta-methods. Primitives and arrays don't have a package name!
                .filter(it -> it.getReturnType().isPrimitive() || //
                        it.getReturnType().isArray() || //
                        !it.getReturnType().getPackage().getName().contains("groovy"))
                .collect(toSet());
    }


    @Override
    public int accept(Method method) {
        return allowedMethods.contains(method) ? 0 : 1;
    }
}
