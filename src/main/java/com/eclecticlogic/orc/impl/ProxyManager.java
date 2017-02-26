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

import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.NoOp;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Stack;

/**
 * Created by kabram
 */
public class ProxyManager<T> {
    final SchemaSpi<T> schema;
    final Stack<PropertyContainer> containers = new Stack<>();

    public ProxyManager(SchemaSpi<T> schema) {
        this.schema = schema;
    }

    public T generate(Class<T> clz) {
        containers.push(new PropertyContainer(clz));
        return enhance(clz);
    }


    public T enhance(Class<T> clz) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(clz);
        enhancer.setCallbacks(new Callback[]{new PropertyInterceptor<>(this, schema), NoOp.INSTANCE});
        enhancer.setCallbackFilter(new SchemaFilter<>(this));
        return (T) enhancer.create();
    }


    public boolean isGetter(Method method) {
        return containers.peek().isGetter(method);
    }


    public PropertyDescriptor getPropertyFor(Method method) {
        return containers.peek().getPropertyFor(method);
    }
}
