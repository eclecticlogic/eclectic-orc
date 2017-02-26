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
package com.eclecticlogic.orc

import com.eclecticlogic.orc.api.Orc

/**
 * Created by kabram
 */
class Student {

    String name
    int age
    BigDecimal score
    boolean resident


    @Orc(length = 20)
    String getName() {
        return name
    }


    @Orc
    int getAge() {
        return age
    }


    @Orc(precision = 10, scale = 2)
    BigDecimal getScore() {
        return score
    }

    void setName(String name) {
        this.name = name
    }

    void setAge(int age) {
        this.age = age
    }

    void setScore(BigDecimal score) {
        this.score = score
    }

}
