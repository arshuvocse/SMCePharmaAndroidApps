package com.creatrix.salessolution.Model.Doctor;

public class DoctorTypeVM {
    int pk;
    int DoctorTypeId;
    String DoctorTypeName;

    public DoctorTypeVM() {
    }

    public int getPk() {
        return pk;
    }

    public void setPk(int pk) {
        this.pk = pk;
    }

    public int getDoctorTypeId() {
        return DoctorTypeId;
    }

    public void setDoctorTypeId(int doctorTypeId) {
        DoctorTypeId = doctorTypeId;
    }

    public String getDoctorTypeName() {
        return DoctorTypeName;
    }

    public void setDoctorTypeName(String doctorTypeName) {
        DoctorTypeName = doctorTypeName;
    }

    @Override
    public String toString() {
        return DoctorTypeName;
    }
}
