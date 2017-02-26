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

package com.eclecticlogic.orc;

import java.math.BigDecimal;

/**
 * Created by kabram
 */
public class Rtc {

    String royaltyItem;
    String participantId;
    BigDecimal allocation;

    public String getRoyaltyItem() {
        return royaltyItem;
    }

    public void setRoyaltyItem(String royaltyItem) {
        this.royaltyItem = royaltyItem;
    }

    public String getParticipantId() {
        return participantId;
    }

    public void setParticipantId(String participantId) {
        this.participantId = participantId;
    }

    public BigDecimal getAllocation() {
        return allocation;
    }

    public void setAllocation(BigDecimal allocation) {
        this.allocation = allocation;
    }
}
