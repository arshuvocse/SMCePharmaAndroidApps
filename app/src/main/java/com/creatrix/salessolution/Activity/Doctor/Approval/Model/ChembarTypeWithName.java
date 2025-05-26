package com.creatrix.salessolution.Activity.Doctor.Approval.Model;

import androidx.annotation.Keep;

@Keep
public class ChembarTypeWithName {
    int ChamberTypeId;
    String ChamberTypeName;
    String ChamberName;


    public int getChamberTypeId() {
        return ChamberTypeId;
    }

    public void setChamberTypeId(int chamberTypeId) {
        ChamberTypeId = chamberTypeId;
    }

    public String getChamberTypeName() {
        return ChamberTypeName;
    }

    public void setChamberTypeName(String chamberTypeName) {
        ChamberTypeName = chamberTypeName;
    }

    public String getChamberName() {
        return ChamberName;
    }

    public void setChamberName(String chamberName) {
        ChamberName = chamberName;
    }
}
