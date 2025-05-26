package com.creatrix.salessolution.Activity.Doctor.TourePlan.Model;

import com.creatrix.salessolution.Model.TourPlanViewModel;

import java.util.List;

public class TourPlanReq {
    List<TourPlanViewModel> aTourPlanInfo;

    public List<TourPlanViewModel> getaTourPlanInfo() {
        return aTourPlanInfo;
    }

    public void setaTourPlanInfo(List<TourPlanViewModel> aTourPlanInfo) {
        this.aTourPlanInfo = aTourPlanInfo;
    }
}
