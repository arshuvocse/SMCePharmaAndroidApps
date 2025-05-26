package com.creatrix.salessolution.Activity.Approval.VisitPlan.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AdoctorVisitplnDetails {
    @SerializedName("DayValue")
    @Expose
    private String dayValue;
    @SerializedName("DayName")
    @Expose
    private String dayName;
    @SerializedName("VisitPlanDate")
    @Expose
    private String visitPlanDate;
    @SerializedName("aVisitDtlsDAO")
    @Expose
    private List<AVisitDtlsDAO> aVisitDtlsDAO = null;

    public String getDayValue() {
        return dayValue;
    }

    public void setDayValue(String dayValue) {
        this.dayValue = dayValue;
    }

    public String getDayName() {
        return dayName;
    }

    public void setDayName(String dayName) {
        this.dayName = dayName;
    }

    public String getVisitPlanDate() {
        return visitPlanDate;
    }

    public void setVisitPlanDate(String visitPlanDate) {
        this.visitPlanDate = visitPlanDate;
    }

    public List<AVisitDtlsDAO> getaVisitDtlsDAO() {
        return aVisitDtlsDAO;
    }

    public void setaVisitDtlsDAO(List<AVisitDtlsDAO> aVisitDtlsDAO) {
        this.aVisitDtlsDAO = aVisitDtlsDAO;
    }
}
