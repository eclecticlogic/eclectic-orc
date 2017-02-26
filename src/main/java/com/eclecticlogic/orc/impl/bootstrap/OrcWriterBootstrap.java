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

import com.eclecticlogic.orc.api.OrcWriter;
import com.eclecticlogic.orc.api.Schema;
import com.eclecticlogic.orc.impl.AbstractOrcWriter;
import com.eclecticlogic.orc.impl.SchemaSpi;
import com.eclecticlogic.orc.impl.SchemaType;
import javassist.*;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupDir;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

/**
 * Created by kabram
 */
public class OrcWriterBootstrap {

    private static final String ORC_WRITER_PACKAGE = "com.eclecticlogic.eclectic.orc.impl.writer.";
    // This is used to prevent linkage error due to concurrent creation of classes.
    private static AtomicInteger extractorNameSuffix = new AtomicInteger();


    public static <T> OrcWriter<T> create(Supplier<Schema<T>> schemaSupplier) {
        return null;
    }


    static <T> OrcWriter<T> createWriter(Supplier<Schema<T>> schemaSupplier) {
        SchemaSpi<T> schema = (SchemaSpi<T>)schemaSupplier.get();
        ClassPool pool = ClassPool.getDefault();
        pool.insertClassPath(new ClassClassPath(AbstractOrcWriter.class));
        CtClass cc = pool.makeClass(ORC_WRITER_PACKAGE + schema.getSchemaClass().getSimpleName()
                + "$OrcWriter_" + extractorNameSuffix.incrementAndGet());
        try {
            cc.setSuperclass(pool.get(AbstractOrcWriter.class.getName()));

            cc.addMethod(CtNewMethod.make(getTypeDescriptionBody(schema), cc));
        } catch (CannotCompileException | NotFoundException e) {
            throw new RuntimeException(e);
        }

        try {
            return (OrcWriter<T>) cc.toClass().newInstance();
        } catch (InstantiationException | IllegalAccessException | CannotCompileException e) {
            throw new RuntimeException(e);
        }
    }


    static <T> String getTypeDescriptionBody(SchemaSpi<T> schema) {
        STGroup group = new STGroupDir("eclectic/orc/template");
        ST st = group.getInstanceOf("schemaMethod");
        SchemaType struct = new SchemaType();

        SchemaType f = new SchemaType();f.setName("score");f.setCreateInstruction("createVarchar");
        struct.getStructChildren().add(f);
        st.add("schemaType", struct);
        return st.render();
    }
}
