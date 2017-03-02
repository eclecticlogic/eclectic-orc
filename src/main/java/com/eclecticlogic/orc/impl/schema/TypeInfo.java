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

package com.eclecticlogic.orc.impl.schema;

import com.eclecticlogic.orc.impl.bootstrap.GeneratorUtil;
import org.apache.orc.TypeDescription;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * Created by kabram on 2/28/17.
 */
public class TypeInfo {

    private final GenInfo genInfo;


    public TypeInfo(GenInfo genInfo) {
        this.genInfo = genInfo;
    }


    public boolean isPrimitive() {
        return genInfo.getLastAccessorMethod().getReturnType().isPrimitive();
    }


    public boolean isTypeBinary() {
        return genInfo.getCategory() == TypeDescription.Category.BINARY;
    }


    public boolean isTypeChar() {
        return genInfo.getCategory() == TypeDescription.Category.CHAR;
    }


    public boolean isTypeVarchar() {
        return genInfo.getCategory() == TypeDescription.Category.VARCHAR;
    }


    public boolean isTypeString() {
        return genInfo.getCategory() == TypeDescription.Category.STRING;
    }


    public boolean isTypeBoolean() {
        return genInfo.getCategory() == TypeDescription.Category.BOOLEAN;
    }


    public boolean isTypeByte() {
        return genInfo.getCategory() == TypeDescription.Category.BYTE;
    }


    public boolean isTypeShort() {
        return genInfo.getCategory() == TypeDescription.Category.SHORT;
    }


    public boolean isTypeInt() {
        return genInfo.getCategory() == TypeDescription.Category.INT;
    }


    public boolean isTypeLong() {
        return genInfo.getCategory() == TypeDescription.Category.LONG;
    }


    public boolean isTypeDate() {
        return genInfo.getCategory() == TypeDescription.Category.DATE;
    }


    public boolean isTypeDecimal() {
        return genInfo.getCategory() == TypeDescription.Category.DECIMAL;
    }


    public boolean isTypeFloat() {
        return genInfo.getCategory() == TypeDescription.Category.FLOAT;
    }


    public boolean isTypeDouble() {
        return genInfo.getCategory() == TypeDescription.Category.DOUBLE;
    }


    public boolean isTypeList() {
        return genInfo.getCategory() == TypeDescription.Category.LIST;
    }


    public boolean isTypeMap() {
        return genInfo.getCategory() == TypeDescription.Category.MAP;
    }


    public boolean isTypeStruct() {
        return genInfo.getCategory() == TypeDescription.Category.STRUCT;
    }


    public boolean isTypeTimestamp() {
        return genInfo.getCategory() == TypeDescription.Category.TIMESTAMP;
    }


    public boolean isTypeUnion() {
        return genInfo.getCategory() == TypeDescription.Category.UNION;
    }


    public boolean isTypeLocalDate() {
        return LocalDate.class.isAssignableFrom(genInfo.getColumnClassType());
    }


    public boolean isTypeZonedDateTime() {
        return ZonedDateTime.class.isAssignableFrom(genInfo.getColumnClassType());
    }


    public boolean isTypeDateTime() {
        return Date.class.isAssignableFrom(genInfo.getColumnClassType());
    }


    /**
     * @return true if the underlying type is a java util List derivative.
     */
    public boolean isTypeJavaList() { return List.class.isAssignableFrom(genInfo.getColumnClassType()); }


    /**
     * @return true if the underlying type is a java util Collection derivative.
     */
    public boolean isTypeJavaCollection() { return Collection.class.isAssignableFrom(genInfo.getColumnClassType()); }


    /**
     * @return true if the underlying type is an Enum derivative.
     */
    public boolean isEnum() { return Enum.class.isAssignableFrom(genInfo.getColumnClassType()); }


    /**
     * @return Returns the method that can be used on a boxed type to get the primitive type.
     */
    public String getPrimitiveConversionMethod() {
        return GeneratorUtil.getPrimitiveAccessorByType(genInfo.getColumnClassType());
    }


}
