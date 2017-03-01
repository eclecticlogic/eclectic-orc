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

import java.io.Closeable;
import java.io.IOException;
import java.util.function.Consumer;

/**
 * This is the interface to write your data and close the orc file.
 * Created by kabram
 */
public interface OrcWriter<T> extends Closeable {

    /**
     * This method will throw a wrapped IOException if underlying API throws an IO exception. This may be called multiple times
     * to write data to the same file.
     * @param data Data to write.
     */
    OrcWriter<T> write(Iterable<T> data);


    /**
     * A variant of the Closeable.close() method that calls the supplied exception handler instead of throwing an exception.
     * useful in cases where you want to simply ignore the exception and not make your code verbose.
     * @param exceptionHandler
     */
    void close(Consumer<IOException> exceptionHandler);
}
