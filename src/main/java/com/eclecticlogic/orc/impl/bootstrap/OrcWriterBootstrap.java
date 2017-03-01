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

import com.eclecticlogic.orc.OrcHandle;
import com.eclecticlogic.orc.impl.AbstractOrcWriter;
import com.eclecticlogic.orc.impl.SchemaSpi;
import com.eclecticlogic.orc.impl.schema.SchemaColumn;
import javassist.CannotCompileException;
import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtNewMethod;
import javassist.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by kabram
 */
public class OrcWriterBootstrap {

    private static final String ORC_WRITER_PACKAGE = "com.eclecticlogic.eclectic.orc.impl.writer.";

    private final static ConcurrentHashMap<Class<?>, OrcHandle<?>> writersByClass = new ConcurrentHashMap<>();
    // This is used to prevent linkage error due to concurrent creation of classes.
    private static AtomicInteger extractorNameSuffix = new AtomicInteger();

    private static Logger logger = LoggerFactory.getLogger(OrcWriterBootstrap.class);


    @SuppressWarnings("unchecked")
    public static <T> OrcHandle<T> create(SchemaSpi<T> schema) {
        Class<T> clz = schema.getSchemaClass();
        writersByClass.computeIfAbsent(clz, (cz) -> createWriter(schema));
        return (OrcHandle<T>) writersByClass.get(clz);
    }


    @SuppressWarnings("unchecked")
    static <T> OrcHandle<T> createWriter(SchemaSpi<T> schema) {
        ClassPool pool = ClassPool.getDefault();
        pool.insertClassPath(new ClassClassPath(AbstractOrcWriter.class));
        CtClass cc = pool.makeClass(ORC_WRITER_PACKAGE + schema.getSchemaClass().getSimpleName() + "$OrcWriter_" + extractorNameSuffix
                .incrementAndGet());
        SchemaColumn schemaColumn = schema.compile();
        try {
            cc.setSuperclass(pool.get(AbstractOrcWriter.class.getName()));

            cc.addMethod(CtNewMethod.make(createTypeDescriptionBody(schemaColumn), cc));
            cc.addMethod(CtNewMethod.make(getSpecialCaseSetupBody(schemaColumn), cc));
            cc.addMethod(CtNewMethod.make(getWriteBody(schemaColumn, schema.getSchemaClass()), cc));
        } catch (CannotCompileException | NotFoundException e) {
            throw new RuntimeException(e);
        }

        try {
            return (OrcHandle<T>) cc.toClass().newInstance();
        } catch (InstantiationException | IllegalAccessException | CannotCompileException e) {
            throw new RuntimeException(e);
        }
    }


    static String createTypeDescriptionBody(SchemaColumn schemaColumn) {
        STGroup group = new STGroupFile("eclectic/orc/template/methodCreateTypeDescription.stg");
        ST st = group.getInstanceOf("methodGetTypeDescription");
        st.add("schemaColumn", schemaColumn);
        String s = st.render();
        logger.debug(s);
        return s;
    }


    static String getSpecialCaseSetupBody(SchemaColumn schemaColumn) {
        STGroup group = new STGroupFile("eclectic/orc/template/methodSpecialCaseSetup.stg");
        // Special case setup is needed for lists to adjust list child vector size.
        List<SchemaColumn> listSchemaTypes = new ArrayList<>();
        Stack<SchemaColumn> structTypes = new Stack<>();
        structTypes.push(schemaColumn);
        while (!structTypes.isEmpty()) {
            for (SchemaColumn child : structTypes.pop().getComplexType().getStructChildren()) {
                if (child.getTypeInfo().isTypeStruct()) {
                    structTypes.push(child);
                } else if (child.getTypeInfo().isTypeList()) {
                    listSchemaTypes.add(child);
                }
            }
        }
        ST st = group.getInstanceOf("methodSpecialCaseSetup");
        st.add("list", listSchemaTypes);
        String s = st.render();
        logger.debug(s);
        return s;
    }


    static String getWriteBody(SchemaColumn schemaColumn, Class<?> schemaClass) {
        STGroup group = new STGroupFile("eclectic/orc/template/methodWrite.stg");
        ST st = group.getInstanceOf("methodWrite");
        st.add("schemaColumn", schemaColumn);
        st.add("sclass", schemaClass);
        String s = st.render();
        logger.debug(s);
        return s;
    }
}
