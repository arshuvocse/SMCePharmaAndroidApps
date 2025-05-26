package com.creatrix.salessolution.Model.Doctor;

public class DoctorChamberTypeVM {
    int pk;
    int ChamberTypeId;
    String ChamberTypeName;
    String ChamberName;

    public int getPk() {
        return pk;
    }

    public void setPk(int pk) {
        this.pk = pk;
    }

/* public DoctorChamberTypeVM(int chamberTypeId, String chamberTypeName,String ChamberName) {
        ChamberTypeId = chamberTypeId;
        ChamberTypeName = chamberTypeName;
        ChamberName = ChamberName;
    }*/

    public DoctorChamberTypeVM(int chamberTypeId, String chamberTypeName, String chamberName) {
        ChamberTypeId = chamberTypeId;
        ChamberTypeName = chamberTypeName;
        ChamberName = chamberName;
    }

    public DoctorChamberTypeVM() {
    }

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
/* public int getChamberTypeId() {
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
    }*/

    @Override
    public String toString() {
        return ChamberTypeName;
    }
}

