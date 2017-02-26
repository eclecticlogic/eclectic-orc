package com.eclecticlogic.orc;

import com.eclecticlogic.orc.api.Factory;
import jdk.nashorn.internal.runtime.regexp.JoniRegExp;

/**
 * Created by kabram
 */
public class FieldSetup {

    void foo() {
        Factory.createSchema(Rtc.class)
                .field(Rtc::getRoyaltyItem)
                .field(Rtc::getParticipantId)
                .field(Rtc::getAllocation)
                .field(it -> it.getAllocation().getClass());

    }
}
