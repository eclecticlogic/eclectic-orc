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

package com.eclecticlogic.orc.impl.schema

import com.eclecticlogic.orc.Converter
import com.eclecticlogic.orc.Orc
import com.eclecticlogic.orc.OrcTemporal
import com.eclecticlogic.orc.OrcTemporalType
import org.apache.orc.TypeDescription
import org.testng.Assert
import org.testng.annotations.Test

import javax.persistence.Temporal
import javax.persistence.TemporalType
import java.lang.annotation.Annotation
import java.lang.reflect.Method
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZonedDateTime

import static org.apache.orc.TypeDescription.Category.*
import static org.testng.Assert.assertEquals

/**
 * Created by kabram
 */
@Test
class TestAbstractSchemaColumn {

    class SampleClass {
        String getName() { return null }
    }

    void testGetColumnClassType() {
        AbstractSchemaColumn underTest1 = new AbstractSchemaColumn() {
            @Override
            Converter getConverter() {
                return null
            }

            @Override
            Method getLastAccessorMethod() {
                return SampleClass.getMethod('getName')
            }
        }

        assertEquals(underTest1.columnClassType, String)

        AbstractSchemaColumn underTest2 = new AbstractSchemaColumn() {
            @Override
            Converter getConverter() {
                return new Converter() {
                    @Override
                    Class getConvertedClass() {
                        return BigDecimal
                    }

                    @Override
                    Object convert(Object instance) {
                        return null
                    }
                }
            }
        }

        assertEquals(underTest2.columnClassType, BigDecimal)
    }

    class Dollar extends BigDecimal {
        Dollar(String s) { super (s)}
    }

    void testGetCategory() {
        AbstractSchemaColumn column = new AbstractSchemaColumn() {
            def <A extends Annotation> A getAnnotation(Class<? extends A> clz) {
                return null
            }
        }
        AbstractSchemaColumn column2 = new AbstractSchemaColumn() {
            def <A extends Annotation> A getAnnotation(Class<? extends A> clz) {
                return new Orc() {
                    @Override
                    int length() {
                        return 10
                    }

                    @Override
                    int precision() {
                        return 0
                    }

                    @Override
                    int scale() {
                        return 0
                    }

                    @Override
                    Class<? extends Annotation> annotationType() {
                        return null
                    }
                }
            }
        }
        assertEquals(column._getCategory(null), STRUCT)
        assertEquals(column._getCategory(SampleClass), STRUCT)
        assertEquals(column._getCategory(String), STRING)
        assertEquals(column._getCategory(BigDecimal), DECIMAL)
        assertEquals(column._getCategory(Dollar), DECIMAL)
        assertEquals(column._getCategory(Boolean.TYPE), BOOLEAN)
        assertEquals(column2._getCategory(String), VARCHAR)
        assertEquals(column._getCategory(LocalDate), DATE)
        assertEquals(column._getCategory(LocalDateTime), TIMESTAMP)
        assertEquals(column._getCategory(Date), TIMESTAMP)
        assertEquals(column._getCategory(ZonedDateTime), TIMESTAMP)

        AbstractSchemaColumn column3 = new AbstractSchemaColumn() {
            def <A extends Annotation> A getAnnotation(Class<? extends A> clz) {
                return new OrcTemporal() {
                    @Override
                    OrcTemporalType value() {
                        return OrcTemporalType.DATE
                    }

                    @Override
                    Class<? extends Annotation> annotationType() {
                        return null
                    }
                }
            }
        }
        assertEquals(column3._getCategory(LocalDateTime), DATE)
        assertEquals(column3._getCategory(Date), DATE)
        assertEquals(column3._getCategory(ZonedDateTime), DATE)
    }


    void testGetAnnotationBasedDateCategory() {
        AbstractSchemaColumn column = new AbstractSchemaColumn() {
            Annotation returnValue
            def <A extends Annotation> A getAnnotation(Class<? extends A> clz) {
                if (returnValue == null) {
                    return null
                }
                if (clz.isAssignableFrom(returnValue.class)) {
                    return returnValue
                }
                return null
            }
        }
        assertEquals(column.annotationBasedDateCategory, TIMESTAMP)
        column.returnValue = new Temporal() {
            @Override
            TemporalType value() {
                return TemporalType.TIMESTAMP
            }

            @Override
            Class<? extends Annotation> annotationType() {
                return null
            }
        }
        assertEquals(column.annotationBasedDateCategory, TIMESTAMP)
        column.returnValue = new Temporal() {
            @Override
            TemporalType value() {
                return TemporalType.DATE
            }

            @Override
            Class<? extends Annotation> annotationType() {
                return null
            }
        }
        assertEquals(column.annotationBasedDateCategory, DATE)
        column.returnValue = new OrcTemporal() {
            @Override
            OrcTemporalType value() {
                return OrcTemporalType.DATE
            }

            @Override
            Class<? extends Annotation> annotationType() {
                return null
            }
        }
        assertEquals(column.annotationBasedDateCategory, DATE)
        column.returnValue = new OrcTemporal() {
            @Override
            OrcTemporalType value() {
                return OrcTemporalType.TIMESTAMP
            }

            @Override
            Class<? extends Annotation> annotationType() {
                return null
            }
        }
        assertEquals(column.annotationBasedDateCategory, TIMESTAMP)
    }

    enum Month {
        JAN, FEB, MAR
    }

    enum Animal {
        DOG('D' as char), CAT('C' as char)

        char code

        Animal(char code) {
            this.code = code
        }

        @Orc
        char getCode() {
            return code
        }
    }

    void testGetEnumCategory() {
        AbstractSchemaColumn column = new AbstractSchemaColumn()
        assertEquals(column.getEnumCategory(Month), STRING)
        assertEquals(column.getEnumCategory(Animal), CHAR)
    }
}
