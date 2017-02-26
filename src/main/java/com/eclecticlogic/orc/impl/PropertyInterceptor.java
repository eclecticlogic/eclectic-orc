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

import com.google.common.base.Defaults;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.stream.Stream;

/**
 * Created by kabram
 */
public class PropertyInterceptor<T> implements MethodInterceptor {

    private final SchemaSpi<T> schema;
    private final ProxyManager proxyManager;

    public PropertyInterceptor(ProxyManager proxyManager, SchemaSpi<T> schema) {
        this.schema = schema;
        this.proxyManager = proxyManager;
    }

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        schema.add(method);
        return getReturnValue(method);
    }


    Object getReturnValue(Method method) {
        Class<?> returnType = method.getReturnType();
        if (returnType.isPrimitive()) {
            return Defaults.defaultValue(method.getReturnType());
        } else if (Modifier.isFinal(returnType.getModifiers())
                || isDefaultConstructable(returnType)) {
            // Cannot sub-class a final class or a class with no default constructor and therefore cannot make a proxy
            return null;
        } else {
            return proxyManager.generate(returnType);
        }
    }

    /**
     * Attribution: http://stackoverflow.com/questions/27810634/how-can-i-check-a-class-has-no-arguments-constructor
     * @param clazz
     * @return true if clazz has a contructor with 0 parameters.
     */
    private boolean isDefaultConstructable(Class<?> clazz) {
        return Stream.of(clazz.getConstructors())
                .noneMatch((c) -> c.getParameterCount() == 0);
    }
}
