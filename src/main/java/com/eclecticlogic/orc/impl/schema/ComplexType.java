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

import com.eclecticlogic.orc.OrcList;
import com.eclecticlogic.orc.impl.bootstrap.GeneratorUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Complex column types.
 * Created by kabram
 */
public class ComplexType {

    private final GenInfo genInfo;
    private List<SchemaColumn> structChildren = new ArrayList<>();
    private SchemaColumn listChild;
    private SchemaColumn mapKey;
    private SchemaColumn mapValue;


    public ComplexType(GenInfo genInfo) {
        this.genInfo = genInfo;
    }


    public List<SchemaColumn> getStructChildren() {
        return structChildren;
    }


    public void setStructChildren(List<SchemaColumn> structChildren) {
        this.structChildren = structChildren;
    }


    public SchemaColumn getListChild() {
        return listChild;
    }


    public void setListChild(SchemaColumn listChild) {
        this.listChild = listChild;
    }


    public SchemaColumn getMapKey() {
        return mapKey;
    }


    public void setMapKey(SchemaColumn mapKey) {
        this.mapKey = mapKey;
    }


    public SchemaColumn getMapValue() {
        return mapValue;
    }


    public void setMapValue(SchemaColumn mapValue) {
        this.mapValue = mapValue;
    }


    /**
     * @return Average size of list for list column types.
     */
    public int getAverageNullSize() {
        OrcList orcCollection = genInfo.getAnnotation(OrcList.class);
        if (orcCollection == null) {
            throw new RuntimeException("@OrcList annotation must be present for list column types.");
        }
        return orcCollection.averageSize() ;
    }


    /**
     * @return Average size of list for list column types.
     */
    public int getAverageSize() {
        OrcList orcCollection = genInfo.getAnnotation(OrcList.class);
        if (orcCollection == null) {
            throw new RuntimeException("@OrcList annotation must be present for list column types.");
        }
        return orcCollection.averageSize() * orcCollection.elementSize();
    }


    public String getTemplateNameListReinit() {
        return GeneratorUtil.getTemplateNameListReinit(genInfo.getCategory());
    }

}
