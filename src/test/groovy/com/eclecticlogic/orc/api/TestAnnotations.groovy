/*
 * Copyright (c) 2017 Eclectic Logic LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.eclecticlogic.orc.api

import com.eclecticlogic.orc.Graduate
import com.eclecticlogic.orc.Orc
import org.testng.annotations.Test

import java.beans.BeanInfo
import java.beans.Introspector

/**
 * Created by kabram
 */
@Test
class TestAnnotations {

    void testOrc() {
        BeanInfo studentInfo = Introspector.getBeanInfo(Graduate)
        studentInfo.propertyDescriptors.each {
            switch (it.name) {
                case 'name':
                    Orc orc = it.readMethod.getAnnotation(Orc)
                    org.testng.Assert.assertNotNull(orc)
                    org.testng.Assert.assertEquals(orc.length(), 20)
                    break
                case 'age':
                    Orc orc = it.readMethod.getAnnotation(Orc)
                    org.testng.Assert.assertNotNull(orc)
                    org.testng.Assert.assertEquals(orc.length(), 0)
                    break
                case 'score':
                    Orc orc = it.readMethod.getAnnotation(Orc)
                    org.testng.Assert.assertNotNull(orc)
                    org.testng.Assert.assertEquals(orc.length(), 0)
                    org.testng.Assert.assertEquals(orc.precision(), 10)
                    org.testng.Assert.assertEquals(orc.scale(), 2)
                    break
                default:
                    break
            }
        }
    }
}
