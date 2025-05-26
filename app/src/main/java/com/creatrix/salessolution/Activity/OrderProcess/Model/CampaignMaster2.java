package com.creatrix.salessolution.Activity.OrderProcess.Model;
public class CampaignMaster2 {
    int CampgainMasterId;
    public int getCampgainMasterId() {
        return CampgainMasterId;
    }
    public void setCampgainMasterId(int campgainMasterId)
    {
        CampgainMasterId = campgainMasterId;
    }

    @Override
    public String toString() {
        return "CampgainMasterId=" + CampgainMasterId;
    }
}
