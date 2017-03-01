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

package com.eclecticlogic.orc

/**
 * Created by kabram on 3/1/17.
 */
enum Level {

    FRESHMEN('F' as char), SOPHOMORE('S' as char), JUNIOR('J' as char), SENIOR('N' as char);

    Level(char code) {
        this.code = code
    }
    private char code;

    @Orc
    char getCode() {
        return code
    }
}