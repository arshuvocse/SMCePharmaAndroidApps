package com.creatrix.salessolution.Activity.Approval.DWSP.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ADWSPMasterDAO {
    @SerializedName("DWSPMasterId")
    @Expose
    private Integer dWSPMasterId;
    @SerializedName("MonthValue")
    @Expose
    private Integer monthValue;
    @SerializedName("YearValue")
    @Expose
    private Integer yearValue;
    @SerializedName("EmpInfoId")
    @Expose
    private Integer empInfoId;
    @SerializedName("FCBAmount")
    @Expose
    private String fCBAmount;
    @SerializedName("GeneralAmount")
    @Expose
    private String generalAmount;
    @SerializedName("CampaignAmount")
    @Expose
    private String campaignAmount;
    @SerializedName("TargetAmount")
    @Expose
    private String TargetAmount;

    @SerializedName("DWSPDate")
    @Expose
    private String dWSPDate;
    @SerializedName("MonthName")
    @Expose
    private String monthName;
    @SerializedName("IsFinalSubmit")
    @Expose
    private Boolean isFinalSubmit;
    @SerializedName("ApprovalStatus")
    @Expose
    private String approvalStatus;
    @SerializedName("aDWSPDate")
    @Expose
    private List<ADWSPDate> aDWSPDate = null;

    public Integer getdWSPMasterId() {
        return dWSPMasterId;
    }

    public void setdWSPMasterId(Integer dWSPMasterId) {
        this.dWSPMasterId = dWSPMasterId;
    }

    public Integer getMonthValue() {
        return monthValue;
    }

    public void setMonthValue(Integer monthValue) {
        this.monthValue = monthValue;
    }

    public Integer getYearValue() {
        return yearValue;
    }

    public void setYearValue(Integer yearValue) {
        this.yearValue = yearValue;
    }

    public Integer getEmpInfoId() {
        return empInfoId;
    }

    public void setEmpInfoId(Integer empInfoId) {
        this.empInfoId = empInfoId;
    }

    public String getfCBAmount() {
        return fCBAmount;
    }

    public void setfCBAmount(String fCBAmount) {
        this.fCBAmount = fCBAmount;
    }

    public String getGeneralAmount() {
        return generalAmount;
    }

    public void setGeneralAmount(String generalAmount) {
        this.generalAmount = generalAmount;
    }

    public String getCampaignAmount() {
        return campaignAmount;
    }

    public void setCampaignAmount(String campaignAmount) {
        this.campaignAmount = campaignAmount;
    }

    public String getdWSPDate() {
        return dWSPDate;
    }

    public void setdWSPDate(String dWSPDate) {
        this.dWSPDate = dWSPDate;
    }

    public String getMonthName() {
        return monthName;
    }

    public void setMonthName(String monthName) {
        this.monthName = monthName;
    }

    public Boolean getFinalSubmit() {
        return isFinalSubmit;
    }

    public void setFinalSubmit(Boolean finalSubmit) {
        isFinalSubmit = finalSubmit;
    }

    public String getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(String approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    public List<ADWSPDate> getaDWSPDate() {
        return aDWSPDate;
    }

    public void setaDWSPDate(List<ADWSPDate> aDWSPDate) {
        this.aDWSPDate = aDWSPDate;
    }

    public String getTargetAmount() {
        return TargetAmount;
    }

    public void setTargetAmount(String targetAmount) {
        TargetAmount = targetAmount;
    }
}
