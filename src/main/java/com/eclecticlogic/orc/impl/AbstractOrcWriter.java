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

import com.eclecticlogic.orc.api.OrcWriter;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hive.ql.exec.vector.ColumnVector;
import org.apache.hadoop.hive.ql.exec.vector.VectorizedRowBatch;
import org.apache.orc.CompressionKind;
import org.apache.orc.OrcFile;
import org.apache.orc.TypeDescription;
import org.apache.orc.Writer;

import java.io.IOException;

/**
 * Created by kabram
 */
public abstract class AbstractOrcWriter<T> implements OrcWriter<T> {

    private Configuration configuration = new Configuration();
    private OrcFile.WriterOptions writerOptions;
    private CompressionKind compressionKind;
    private int bufferSize;
    private int batchSize;
    protected VectorizedRowBatch vectorizedRowBatch;

    @Override
    public OrcWriter<T> withConfiguration(Configuration configuration) {
        this.configuration = configuration;
        return this;
    }

    @Override
    public OrcWriter<T> withOptions(OrcFile.WriterOptions writerOptions) {
        this.writerOptions = writerOptions;
        return null;
    }

    @Override
    public OrcWriter<T> withCompression(CompressionKind compressionKind) {
        this.compressionKind = compressionKind;
        return this;
    }

    @Override
    public OrcWriter<T> withBufferSize(int size) {
        this.bufferSize = size;
        return this;
    }

    @Override
    public OrcWriter<T> withBatchSize(int batchSize) {
        this.batchSize = batchSize;
        return this;
    }

    @Override
    public void write(Path path, Iterable<T> data) throws IOException {
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
        Writer writer = OrcFile.createWriter(path, writerOptions);
        vectorizedRowBatch = schema.createRowBatch(batchSize);
        for (T datum : data) {
            if (vectorizedRowBatch.size == vectorizedRowBatch.getMaxSize()) {
                writer.addRowBatch(vectorizedRowBatch);
                vectorizedRowBatch.reset();
            }
            // Write the datum to the column vectors.
            write(datum);
            vectorizedRowBatch.size++;
        }
        writer.addRowBatch(vectorizedRowBatch);
        writer.close();
    }


    protected abstract TypeDescription getTypeDescription();


    protected void setNull(ColumnVector vector) {
        vector.isNull[vectorizedRowBatch.size] = true;
        vector.noNulls = false;
    }


    protected abstract void write(T datum);
}
