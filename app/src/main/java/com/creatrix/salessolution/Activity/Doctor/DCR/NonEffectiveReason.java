package com.creatrix.salessolution.Activity.Doctor.DCR;

import androidx.annotation.Keep;

@Keep public class NonEffectiveReason {
    int ReasonId;
    String ReasonName;

    public NonEffectiveReason() {
    }

    public int getReasonId() {
        return ReasonId;
    }

    public void setReasonId(int reasonId) {
        ReasonId = reasonId;
    }

    public String getReasonName() {
        return ReasonName;
    }

    public void setReasonName(String reasonName) {
        ReasonName = reasonName;
    }

    @Override
    public String toString() {
        return ReasonName;
    }
}
