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

import com.eclecticlogic.orc.OrcHandle;
import com.eclecticlogic.orc.OrcWriter;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hive.ql.exec.vector.ColumnVector;
import org.apache.hadoop.hive.ql.exec.vector.VectorizedRowBatch;
import org.apache.orc.CompressionKind;
import org.apache.orc.OrcFile;
import org.apache.orc.TypeDescription;
import org.apache.orc.Writer;

import java.io.IOException;
import java.util.function.Consumer;

/**
 * Created by kabram
 */
public abstract class AbstractOrcWriter<T> implements OrcHandle<T>, OrcWriter<T> {

    private Configuration configuration = new Configuration();
    private OrcFile.WriterOptions writerOptions;
    private CompressionKind compressionKind;
    private int bufferSize = 10 * 1024;
    private int batchSize = 1024;
    private TypeDescription _typeDescription;
    protected VectorizedRowBatch vectorizedRowBatch;
    private Writer writer;


    @Override
    public OrcHandle<T> withConfiguration(Configuration configuration) {
        this.configuration = configuration;
        return this;
    }

    @Override
    public OrcHandle<T> withOptions(OrcFile.WriterOptions writerOptions) {
        this.writerOptions = writerOptions;
        return null;
    }

    @Override
    public OrcHandle<T> withCompression(CompressionKind compressionKind) {
        this.compressionKind = compressionKind;
        return this;
    }

    @Override
    public OrcHandle<T> withBufferSize(int size) {
        this.bufferSize = size;
        return this;
    }

    @Override
    public OrcHandle<T> withBatchSize(int batchSize) {
        this.batchSize = batchSize;
        return this;
    }


    @Override
    public OrcWriter<T> open(Path path) {
        if (writerOptions == null) {
            writerOptions = OrcFile.writerOptions(configuration);
        }
        if (compressionKind != null) {
            writerOptions.compress(compressionKind);
        }
        if (bufferSize != 0) {
            writerOptions.bufferSize(bufferSize);
        }
        // Add the schema to the writer options.
        TypeDescription schema = getTypeDescription();
        writerOptions.setSchema(schema);
        try {
            writer = OrcFile.createWriter(path, writerOptions);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        vectorizedRowBatch = schema.createRowBatch(batchSize);
        specialCaseSetup();
        return this;
    }


    @Override
    public OrcWriter<T> write(Iterable<T> data) {
        try {
            for (T datum : data) {
                if (vectorizedRowBatch.size == vectorizedRowBatch.getMaxSize()) {
                    writer.addRowBatch(vectorizedRowBatch);
                    vectorizedRowBatch.reset();
                }
                // Write the datum to the column vectors.
                write(datum);
                vectorizedRowBatch.size++;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return this;
    }


    @Override
    public void close() throws IOException {
        if (vectorizedRowBatch != null) {
            writer.addRowBatch(vectorizedRowBatch);
            vectorizedRowBatch = null;
        }
        if (writer != null) {
            writer.close();
            writer = null;
        }
    }


    @Override
    public void close(Consumer<IOException> exceptionHandler) {
        try {
            close();
        } catch (IOException e) {
            exceptionHandler.accept(e);
        }
    }


    protected TypeDescription getTypeDescription() {
        if (_typeDescription == null) {
            _typeDescription = createTypeDescription();
        }
        return _typeDescription;
    }


    /**
     * Helper utility to set the value of the current property to null in the vector.
     * @param vector
     */
    protected void setNull(ColumnVector vector) {
        vector.isNull[vectorizedRowBatch.size] = true;
        vector.noNulls = false;
    }


    /**
     * @return The schema for the orc file as computed by the property access definitions. The implementation is generated dynamically at
     * runtime using javassist.
     */
    protected abstract TypeDescription createTypeDescription();


    /**
     * Hook to setup special cases such as the modification of list child to support the full flattened size
     * (rows x average list column size per row)
     */
    protected abstract void specialCaseSetup();


    /**
     * Routine that actually populates one row of the list into the vectorized row batch. The implementation of this is generated
     * dynamically at runtime using javassist.
     * @param datum Object instance to write.
     */
    protected abstract void write(T datum);


    @Override
    public String toString() {
        return getTypeDescription().toString();
    }
}
