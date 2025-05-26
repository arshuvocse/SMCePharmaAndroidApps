package com.creatrix.salessolution.Model.Doctor;

public class DoctorChamberName {
    int pk;
    int ChemberId;
    String ChemberName;
    int DoctorId;

    public DoctorChamberName() {
    }

    public int getChemberId() {
        return ChemberId;
    }

    public void setChemberId(int chemberId) {
        ChemberId = chemberId;
    }

    public String getChemberName() {
        return ChemberName;
    }

    public void setChemberName(String chemberName) {
        ChemberName = chemberName;
    }

    public int getDoctorId() {
        return DoctorId;
    }

    public void setDoctorId(int doctorId) {
        DoctorId = doctorId;
    }

    public int getPk() {
        return pk;
    }

    @Override
    public String toString() {
        return ChemberName;
    }
}
