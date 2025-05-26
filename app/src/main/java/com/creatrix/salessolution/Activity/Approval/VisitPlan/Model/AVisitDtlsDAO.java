package com.creatrix.salessolution.Activity.Approval.VisitPlan.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AVisitDtlsDAO {
    @SerializedName("DoctorName")
    @Expose
    private String doctorName;

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }
}
