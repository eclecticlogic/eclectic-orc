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

import com.eclecticlogic.orc.OrcWriter;
import com.eclecticlogic.orc.impl.AbstractOrcWriter;
import com.eclecticlogic.orc.impl.SchemaSpi;
import com.eclecticlogic.orc.impl.schema.SchemaType;
import javassist.*;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupDir;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by kabram
 */
public class OrcWriterBootstrap {

    private static final String ORC_WRITER_PACKAGE = "com.eclecticlogic.eclectic.orc.impl.writer.";

    private final static ConcurrentHashMap<Class<?>, OrcWriter<?>> writersByClass = new ConcurrentHashMap<>();
    // This is used to prevent linkage error due to concurrent creation of classes.
    private static AtomicInteger extractorNameSuffix = new AtomicInteger();


    @SuppressWarnings("unchecked")
    public static <T> OrcWriter<T> create(SchemaSpi<T> schema) {
        Class<T> clz = schema.getSchemaClass();
        writersByClass.computeIfAbsent(clz, (cz) -> createWriter(schema));
        return (OrcWriter<T>)writersByClass.get(clz);
    }


    @SuppressWarnings("unchecked")
    static <T> OrcWriter<T> createWriter(SchemaSpi<T> schema) {
        ClassPool pool = ClassPool.getDefault();
        pool.insertClassPath(new ClassClassPath(AbstractOrcWriter.class));
        CtClass cc = pool.makeClass(ORC_WRITER_PACKAGE + schema.getSchemaClass().getSimpleName()
                + "$OrcWriter_" + extractorNameSuffix.incrementAndGet());
        SchemaType schemaType = schema.compile();
        try {
            cc.setSuperclass(pool.get(AbstractOrcWriter.class.getName()));

            cc.addMethod(CtNewMethod.make(getTypeDescriptionBody(schemaType), cc));
            cc.addMethod(CtNewMethod.make(getWriteBody(schemaType, schema.getSchemaClass()), cc));
        } catch (CannotCompileException | NotFoundException e) {
            throw new RuntimeException(e);
        }

        try {
            return (OrcWriter<T>) cc.toClass().newInstance();
        } catch (InstantiationException | IllegalAccessException | CannotCompileException e) {
            throw new RuntimeException(e);
        }
    }


    static String getTypeDescriptionBody(SchemaType schemaType) {
        STGroup group = new STGroupDir("eclectic/orc/template");
        ST st = group.getInstanceOf("schemaMethod");

        st.add("schemaType", schemaType);
        String s = st.render();
        System.out.println(s);
        return s;
    }


    static String getWriteBody(SchemaType schemaType, Class<?> schemaClass) {
        STGroup group = new STGroupDir("eclectic/orc/template");
        ST st = group.getInstanceOf("writeMethod");
        st.add("schemaType", schemaType);
        st.add("sclass", schemaClass);
        String s = st.render();
        System.out.println(s);
        return s;
    }
}
