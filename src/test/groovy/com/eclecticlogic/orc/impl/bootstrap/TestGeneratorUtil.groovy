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

package com.eclecticlogic.orc.impl.bootstrap

import com.eclecticlogic.orc.Orc
import org.testng.annotations.Test

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZonedDateTime

import static org.apache.orc.TypeDescription.Category.*
import static org.testng.Assert.*

/**
 * Created by kabram
 */
@Test
class TestGeneratorUtil {

    class Money extends BigDecimal {
        Money(String var1) {
            super(var1)
        }
    }

    class MyDate extends Date {}

    class FunnyProperties {
        int getMyScore() { 0 }
        int notMyScore() { 0 }
        int CanThisBeTrue() { 0 }
        boolean isThisTrue() { 0 }
    }

    enum WarType {
        LOCAL, GLOBAL

        @Orc
        int getIntensity() { 0 }
    }

    enum GameType {
        RPG, MMORG
    }

    void testPrimitiveDefaultValues() {
        assertEquals GeneratorUtil.getDefaultValueForPrimitiveType(Boolean.TYPE), false
        assertEquals GeneratorUtil.getDefaultValueForPrimitiveType(Character.TYPE), '\u0000' as char
        assertEquals GeneratorUtil.getDefaultValueForPrimitiveType(Byte.TYPE), 0
        assertEquals GeneratorUtil.getDefaultValueForPrimitiveType(Short.TYPE), 0
        assertEquals GeneratorUtil.getDefaultValueForPrimitiveType(Integer.TYPE), 0
        assertEquals GeneratorUtil.getDefaultValueForPrimitiveType(Long.TYPE), 0
        assertEquals GeneratorUtil.getDefaultValueForPrimitiveType(Float.TYPE), 0.0f
        assertEquals GeneratorUtil.getDefaultValueForPrimitiveType(Double.TYPE), 0.0d
    }

    void testCategoriesByBasicTypes() {
        assertEquals GeneratorUtil.getCategoryByBasicType(Boolean.TYPE), BOOLEAN
        assertEquals GeneratorUtil.getCategoryByBasicType(Character.TYPE), CHAR
        assertEquals GeneratorUtil.getCategoryByBasicType(Byte.TYPE), BYTE
        assertEquals GeneratorUtil.getCategoryByBasicType(Short.TYPE), SHORT
        assertEquals GeneratorUtil.getCategoryByBasicType(Integer.TYPE), INT
        assertEquals GeneratorUtil.getCategoryByBasicType(Long.TYPE), LONG
        assertEquals GeneratorUtil.getCategoryByBasicType(Float.TYPE), FLOAT
        assertEquals GeneratorUtil.getCategoryByBasicType(Double.TYPE), DOUBLE
    }

    void testCategoriesByAssignableTypes() {
        assertEquals GeneratorUtil.getCategoryByAssignableType(Money), DECIMAL
        assertEquals GeneratorUtil.getCategoryByAssignableType(Set), LIST
        assertEquals GeneratorUtil.getCategoryByAssignableType(Date), TIMESTAMP
        assertEquals GeneratorUtil.getCategoryByAssignableType(LocalDate), DATE
        assertEquals GeneratorUtil.getCategoryByAssignableType(LocalDateTime), TIMESTAMP
        assertEquals GeneratorUtil.getCategoryByAssignableType(ZonedDateTime), TIMESTAMP
        assertEquals GeneratorUtil.getCategoryByAssignableType(MyDate), TIMESTAMP
    }

    void testPrimitiveAccessorByType() {
        assertEquals GeneratorUtil.getPrimitiveAccessorByType(Boolean), 'booleanValue()'
        assertEquals GeneratorUtil.getPrimitiveAccessorByType(Byte), 'byteValue()'
        assertEquals GeneratorUtil.getPrimitiveAccessorByType(Character), 'charValue()'
        assertEquals GeneratorUtil.getPrimitiveAccessorByType(Short), 'shortValue()'
        assertEquals GeneratorUtil.getPrimitiveAccessorByType(Integer), 'intValue()'
        assertEquals GeneratorUtil.getPrimitiveAccessorByType(Long), 'longValue()'
        assertEquals GeneratorUtil.getPrimitiveAccessorByType(Float), 'floatValue()'
        assertEquals GeneratorUtil.getPrimitiveAccessorByType(Double), 'doubleValue()'
    }

    void testPropertyName() {
        assertEquals GeneratorUtil.getPropertyName(FunnyProperties.getMethod('getMyScore')), 'myScore'
        assertEquals GeneratorUtil.getPropertyName(FunnyProperties.getMethod('notMyScore')), 'notMyScore'
        assertEquals GeneratorUtil.getPropertyName(FunnyProperties.getMethod('CanThisBeTrue')), 'canThisBeTrue'
        assertEquals GeneratorUtil.getPropertyName(FunnyProperties.getMethod('isThisTrue')), 'thisTrue'
    }

    void testGetAnnotatedMethodInEnum() {
        assertTrue GeneratorUtil.getAnnotatedMethodInEnum(WarType).present
        assertEquals GeneratorUtil.getAnnotatedMethodInEnum(WarType).get(), WarType.getMethod('getIntensity')
        assertFalse GeneratorUtil.getAnnotatedMethodInEnum(GameType).present
    }
}