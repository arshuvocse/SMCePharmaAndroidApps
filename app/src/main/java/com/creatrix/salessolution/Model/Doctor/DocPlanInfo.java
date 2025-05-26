package com.creatrix.salessolution.Model.Doctor;

public class DocPlanInfo {
    int DocTPDetailsId;
    int DoctorId;
    String TpDate;
    String DoctorName;
    String DoctorCode;

    public int getDoctorId() {
        return DoctorId;
    }

    public void setDoctorId(int doctorId) {
        DoctorId = doctorId;
    }

    public String getDoctorCode() {
        return DoctorCode;
    }

    public void setDoctorCode(String doctorCode) {
        DoctorCode = doctorCode;
    }

    public int getDocTPDetailsId() {
        return DocTPDetailsId;
    }

    public void setDocTPDetailsId(int docTPDetailsId) {
        DocTPDetailsId = docTPDetailsId;
    }

    public String getTpDate() {
        return TpDate;
    }

    public void setTpDate(String tpDate) {
        TpDate = tpDate;
    }

    public String getDoctorName() {
        return DoctorName;
    }

    public void setDoctorName(String doctorName) {
        DoctorName = doctorName;
    }
}
