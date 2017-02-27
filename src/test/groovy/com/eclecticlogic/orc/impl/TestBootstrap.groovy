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

package com.eclecticlogic.orc.impl

import com.eclecticlogic.orc.Graduate
import com.eclecticlogic.orc.Factory
import com.eclecticlogic.orc.OrcWriter
import com.eclecticlogic.orc.Schema
import org.apache.hadoop.fs.Path
import org.testng.annotations.Test

/**
 * Created by kabram
 */
@Test
class TestBootstrap {

    void testStringTemplate() {
        Schema schema = Factory.createSchema(Graduate)
                .column() {it.name}
                .column() {it.age}
                .column('money') {it.allowance }
        OrcWriter writer = Factory.createWriter(schema)
        List<Graduate> list = []
        list << new Graduate(name: 'abc', age: 10, allowance: 150.0)
        list << new Graduate(name: 'def', age: 20, allowance: 250.0)
        list << new Graduate(name: 'aaa', age: 30, allowance: 350.0)
        Path path = new Path('/home/kabram/temp/dp/graduate.orc')
        writer.write(path, list)
    }
}
