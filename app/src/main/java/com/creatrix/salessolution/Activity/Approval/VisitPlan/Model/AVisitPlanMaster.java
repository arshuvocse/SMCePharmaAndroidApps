package com.creatrix.salessolution.Activity.Approval.VisitPlan.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AVisitPlanMaster {
    @SerializedName("DocTPMaster")
    @Expose
    private Integer docTPMaster;
    @SerializedName("EmpInfoId")
    @Expose
    private Integer empInfoId;
    @SerializedName("MonthValue")
    @Expose
    private String monthValue;
    @SerializedName("YearValue")
    @Expose
    private String yearValue;
    @SerializedName("MonthName")
    @Expose
    private String monthName;
    @SerializedName("ApprovalStatus")
    @Expose
    private String approvalStatus;
    @SerializedName("IsFinalSubmit")
    @Expose
    private Boolean isFinalSubmit;
    @SerializedName("aDoctorVisitPlanDetail")
    @Expose
    private List<AdoctorVisitplnDetails> aDoctorVisitPlanDetail = null;

    public Integer getDocTPMaster() {
        return docTPMaster;
    }

    public void setDocTPMaster(Integer docTPMaster) {
        this.docTPMaster = docTPMaster;
    }

    public Integer getEmpInfoId() {
        return empInfoId;
    }

    public void setEmpInfoId(Integer empInfoId) {
        this.empInfoId = empInfoId;
    }

    public String getMonthValue() {
        return monthValue;
    }

    public void setMonthValue(String monthValue) {
        this.monthValue = monthValue;
    }

    public String getYearValue() {
        return yearValue;
    }

    public void setYearValue(String yearValue) {
        this.yearValue = yearValue;
    }

    public String getMonthName() {
        return monthName;
    }

    public void setMonthName(String monthName) {
        this.monthName = monthName;
    }

    public String getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(String approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    public Boolean getIsFinalSubmit() {
        return isFinalSubmit;
    }

    public void setIsFinalSubmit(Boolean isFinalSubmit) {
        this.isFinalSubmit = isFinalSubmit;
    }

    public Boolean getFinalSubmit() {
        return isFinalSubmit;
    }

    public void setFinalSubmit(Boolean finalSubmit) {
        isFinalSubmit = finalSubmit;
    }

    public List<AdoctorVisitplnDetails> getaDoctorVisitPlanDetail() {
        return aDoctorVisitPlanDetail;
    }

    public void setaDoctorVisitPlanDetail(List<AdoctorVisitplnDetails> aDoctorVisitPlanDetail) {
        this.aDoctorVisitPlanDetail = aDoctorVisitPlanDetail;
    }
}
