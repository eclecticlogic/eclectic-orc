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

package com.eclecticlogic.orc.api;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.orc.CompressionKind;
import org.apache.orc.OrcFile;

import java.io.IOException;

/**
 * Created by kabram
 */
public interface OrcWriter<T> {

    OrcWriter<T> withConfiguration(Configuration configuration);

    OrcWriter<T> withOptions(OrcFile.WriterOptions writerOptions);

    OrcWriter<T> withCompression(CompressionKind compressionKind);

    OrcWriter<T> withBufferSize(int size);

    OrcWriter<T> withBatchSize(int batchSize);

    void write(Path path, Iterable<T> data) throws IOException;
}
