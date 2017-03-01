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

package com.eclecticlogic.orc;

import com.eclecticlogic.orc.impl.SchemaSpi;
import com.eclecticlogic.orc.impl.bootstrap.OrcWriterBootstrap;
import com.eclecticlogic.orc.impl.SchemaSpiImpl;

/**
 * This is the main class to interact with the eclectic-orc library.
 * Created by kabram
 */
public class Factory {

    /**
     * @param clz The class of objects you want to write to your orc file.
     * @param <T> The type of objects you want to write.
     * @return Schema creator to specify the orc file schema.
     */
    public static <T> Schema<T> createSchema(Class<T> clz) {
        return new SchemaSpiImpl<T>(clz);
    }


    /**
     * @param schema The schema for the orc file you want to create.
     * @param <T> The type of the object you are working with.
     * @return Instance that allows you to configure, open and write to your data.
     */
    public static <T> OrcHandle<T> createWriter(Schema<T> schema) {
        return OrcWriterBootstrap.create((SchemaSpi<T>)schema);
    }
}
