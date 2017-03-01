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
enum Power {

    LOW(45.0), MEDIUM(77.4), HIGH(89.9);

    Power(BigDecimal value) {
        this.value = value
    }
    private BigDecimal value

    @Orc(precision = 23, scale = 7)
    BigDecimal getValue() {
        return value
    }
}