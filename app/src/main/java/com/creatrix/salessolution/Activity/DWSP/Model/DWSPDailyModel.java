package com.creatrix.salessolution.Activity.DWSP.Model;

import androidx.annotation.Keep;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Keep public class DWSPDailyModel {
    @SerializedName("DWSPMasterId")
    @Expose
    private Integer dWSPMasterId;
    @SerializedName("FCBAmount")
    @Expose
    private Double fCBAmount;
    @SerializedName("GeneralAmount")
    @Expose
    private Double generalAmount;
    @SerializedName("CampaignAmount")
    @Expose
    private Double campaignAmount;
    @SerializedName("DWSPDetailId")
    @Expose
    private Integer dWSPDetailId;
    @SerializedName("MonthValue")
    @Expose
    private Integer monthValue;
    @SerializedName("YearValue")
    @Expose
    private Integer yearValue;
    @SerializedName("DayValue")
    @Expose
    private Integer dayValue;
    @SerializedName("DWSPDate")
    @Expose
    private String dWSPDate;
    @SerializedName("IsFinalSubmit")
    @Expose
    private boolean isFinalSubmit;

    public Integer getDWSPMasterId() {
        return dWSPMasterId;
    }

    public void setDWSPMasterId(Integer dWSPMasterId) {
        this.dWSPMasterId = dWSPMasterId;
    }

    public Double getFCBAmount() {
        return fCBAmount;
    }

    public void setFCBAmount(Double fCBAmount) {
        this.fCBAmount = fCBAmount;
    }

    public Double getGeneralAmount() {
        return generalAmount;
    }

    public void setGeneralAmount(Double generalAmount) {
        this.generalAmount = generalAmount;
    }

    public Double getCampaignAmount() {
        return campaignAmount;
    }

    public void setCampaignAmount(Double campaignAmount) {
        this.campaignAmount = campaignAmount;
    }

    public Integer getDWSPDetailId() {
        return dWSPDetailId;
    }

    public void setDWSPDetailId(Integer dWSPDetailId) {
        this.dWSPDetailId = dWSPDetailId;
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

    public Integer getDayValue() {
        return dayValue;
    }

    public void setDayValue(Integer dayValue) {
        this.dayValue = dayValue;
    }

    public String getDWSPDate() {
        return dWSPDate;
    }

    public void setDWSPDate(String dWSPDate) {
        this.dWSPDate = dWSPDate;
    }

    public boolean getIsFinalSubmit() {
        return isFinalSubmit;
    }

    public void setIsFinalSubmit(boolean isFinalSubmit) {
        this.isFinalSubmit = isFinalSubmit;
    }
}
