package com.creatrix.salessolution.Activity.Approval.DWSP.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ADWSPDate {
    @SerializedName("DayValue")
    @Expose
    private String dayValue;
    @SerializedName("DayName")
    @Expose
    private String dayName;
    @SerializedName("FCBAmount")
    @Expose
    private String fCBAmount;
    @SerializedName("GeneralAmount")
    @Expose
    private String generalAmount;
    @SerializedName("CampaignAmount")
    @Expose
    private String campaignAmount;

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
}
