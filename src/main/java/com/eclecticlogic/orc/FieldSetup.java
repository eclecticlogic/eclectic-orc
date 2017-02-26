package com.eclecticlogic.orc;

import com.eclecticlogic.orc.api.Factory;
import jdk.nashorn.internal.runtime.regexp.JoniRegExp;

/**
 * Created by kabram
 */
public class FieldSetup {

    void foo() {
        Factory.createSchema(Rtc.class)
                .column(Rtc::getRoyaltyItem)
                .column(Rtc::getParticipantId)
                .column(Rtc::getAllocation)
                .column(it -> it.getAllocation().getClass());

    }
}
