package com.creatrix.salessolution.Activity.DWSP.Model;

import androidx.annotation.Keep;

@Keep public class SaveDWSP {
      int DWSPMasterId;
      int MonthValue;
      int YearValue;
      int EmpInfoId;
      Double FCBAmount;
      Double GeneralAmount;
      Double CampaignAmount;
      String DWSPDate;

    public int getDWSPMasterId() {
        return DWSPMasterId;
    }

    public void setDWSPMasterId(int DWSPMasterId) {
        this.DWSPMasterId = DWSPMasterId;
    }

    public int getMonthValue() {
        return MonthValue;
    }

    public void setMonthValue(int monthValue) {
        MonthValue = monthValue;
    }

    public int getYearValue() {
        return YearValue;
    }

    public void setYearValue(int yearValue) {
        YearValue = yearValue;
    }

    public int getEmpInfoId() {
        return EmpInfoId;
    }

    public void setEmpInfoId(int empInfoId) {
        EmpInfoId = empInfoId;
    }

    public Double getFCBAmount() {
        return FCBAmount;
    }

    public void setFCBAmount(Double FCBAmount) {
        this.FCBAmount = FCBAmount;
    }

    public Double getGeneralAmount() {
        return GeneralAmount;
    }

    public void setGeneralAmount(Double generalAmount) {
        GeneralAmount = generalAmount;
    }

    public Double getCampaignAmount() {
        return CampaignAmount;
    }

    public void setCampaignAmount(Double campaignAmount) {
        CampaignAmount = campaignAmount;
    }

    public String getDWSPDate() {
        return DWSPDate;
    }

    public void setDWSPDate(String DWSPDate) {
        this.DWSPDate = DWSPDate;
    }
}
