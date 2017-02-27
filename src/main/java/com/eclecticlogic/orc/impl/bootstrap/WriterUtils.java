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

package com.eclecticlogic.orc.impl.bootstrap;

import org.apache.hadoop.hive.ql.exec.vector.BytesColumnVector;
import org.apache.hadoop.hive.ql.exec.vector.DecimalColumnVector;
import org.apache.hadoop.hive.ql.exec.vector.DoubleColumnVector;
import org.apache.hadoop.hive.ql.exec.vector.ListColumnVector;
import org.apache.hadoop.hive.ql.exec.vector.LongColumnVector;
import org.apache.hadoop.hive.ql.exec.vector.MapColumnVector;
import org.apache.hadoop.hive.ql.exec.vector.StructColumnVector;
import org.apache.hadoop.hive.ql.exec.vector.TimestampColumnVector;
import org.apache.hadoop.hive.ql.exec.vector.UnionColumnVector;
import org.apache.orc.TypeDescription;
import org.apache.orc.TypeDescription.Category;

/**
 * Created by kabram
 */
public class WriterUtils {

    public static String getTypeDescriptionCreator(Category category) {
        switch (category) {
            case BINARY:
                return "createBinary";
            case BOOLEAN:
                return "createBoolean";
            case BYTE:
                return "createByte";
            case CHAR:
                // TODO Defined as varchar because athena doesn't seem to support char.
                return "createVarchar";
            case DATE:
                return "createDate";
            case DECIMAL:
                return "createDecimal";
            case DOUBLE:
                return "createDouble";
            case FLOAT:
                return "createFloat";
            case INT:
                return "createInt";
            case LIST:
                return "createList";
            case LONG:
                return "createLong";
            case MAP:
                return "createMap";
            case SHORT:
                return "createShort";
            case STRING:
                return "createVarchar";
            case STRUCT:
                return "createStruct";
            case TIMESTAMP:
                return "createTimestamp";
            case UNION:
                return "createUnion";
            case VARCHAR:
                return "createVarchar";
            default:
                return null;
        }
    }


    public static String getVectorClassName(Category category) {
        switch (category) {
            case BINARY:
            case CHAR:
            case VARCHAR:
            case STRING:
                return BytesColumnVector.class.getName();
            case BOOLEAN:
            case BYTE:
            case SHORT:
            case INT:
            case LONG:
            case DATE:
                return LongColumnVector.class.getName();
            case DECIMAL:
                return DecimalColumnVector.class.getName();
            case FLOAT:
            case DOUBLE:
                return DoubleColumnVector.class.getName();
            case LIST:
                return ListColumnVector.class.getName();
            case MAP:
                return MapColumnVector.class.getName();
            case STRUCT:
                return StructColumnVector.class.getName();
            case TIMESTAMP:
                return TimestampColumnVector.class.getName();
            case UNION:
                return UnionColumnVector.class.getName();
            default:
                return null;
        }
    }
}
