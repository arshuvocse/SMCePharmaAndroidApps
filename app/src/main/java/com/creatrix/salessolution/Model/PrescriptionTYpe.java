package com.creatrix.salessolution.Model;

public class PrescriptionTYpe {

    int PrescriptionTypeId;
    String PrescriptionType;

    public int getPrescriptionTypeId() {
        return PrescriptionTypeId;
    }

    public void setPrescriptionTypeId(int prescriptionTypeId) {
        PrescriptionTypeId = prescriptionTypeId;
    }

    public String getPrescriptionType() {
        return PrescriptionType;
    }

    public void setPrescriptionType(String prescriptionType) {
        PrescriptionType = prescriptionType;
    }

    @Override
    public String toString() {
        return PrescriptionType;
    }
}

