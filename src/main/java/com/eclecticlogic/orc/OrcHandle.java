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

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.orc.CompressionKind;
import org.apache.orc.OrcFile.WriterOptions;

/**
 * This is the interface to configure and open an orc file. Get an instance of this from the Factory by passing in a Schema definition.
 * Created by kabram
 */
public interface OrcHandle<T> {

    /**
     * @param configuration Configuration to use. This is optional.
     * @return self reference for fluent interface.
     */
    OrcHandle<T> withConfiguration(Configuration configuration);

    /**
     * @param writerOptions Writer options to use. Note: if you pass in an explicit writerOptions object, this value will not be used.
     * @return self reference for fluent interface.
     */
    OrcHandle<T> withOptions(WriterOptions writerOptions);

    /**
     * @param compressionKind Compression to use. This value will overwrite any setting passed in WriterOptions.
     * @return self reference for fluent interface.
     */
    OrcHandle<T> withCompression(CompressionKind compressionKind);

    /**
     * @param size Buffer size to use. This value will overwrite any setting passed in WriterOptions.
     * @return self reference for fluent interface.
     */
    OrcHandle<T> withBufferSize(int size);

    /**
     * @param batchSize Vector batch size to use.
     * @return self reference for fluent interface.
     */
    OrcHandle<T> withBatchSize(int batchSize);


    /**
     * @param path Path to write orc file to.
     * @return self reference for fluent interface.
     */
    OrcWriter<T> open(Path path);

}
