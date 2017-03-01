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

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by kabram on 3/1/17.
 */
public class Template {

    private static final AtomicInteger variableCounter = new AtomicInteger();
    private final GenInfo genInfo;
    private ThreadLocal<Integer> currentVariable = new ThreadLocal<>();


    public Template(GenInfo genInfo) {
        this.genInfo = genInfo;
    }


    public String getTemplateNameColumnSetter() {
        return GeneratorUtil.getTemplateNameColumnSetter(genInfo.getCategory());
    }


    /**
     * @return Creates a new temporary variable name and returns it.
     */
    public String getNewTempVariable() {
        currentVariable.set(variableCounter.getAndIncrement());
        return "v" + currentVariable.get();
    }


    /**
     * @return Returns the current temporary variable. At least one call to getNewTempVariable() from current thread should precede this
     * call.
     */
    public String getTempVariable() {
        return "v" + currentVariable.get();
    }


}
