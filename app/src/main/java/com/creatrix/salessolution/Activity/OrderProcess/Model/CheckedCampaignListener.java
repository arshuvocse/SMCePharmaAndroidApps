package com.creatrix.salessolution.Activity.OrderProcess.Model;

import com.creatrix.salessolution.Model.Doctor.DoctorListViewModel;

import java.util.List;

public interface CheckedCampaignListener {
    void ckdItem(List<CampaignModel> camp, int Pos);
    void ckdItemid2(List<CampaignMaster2> ids2, int Pos);
    void ckdItemId(List<CampaignMasters> ids, int Pos);
}
