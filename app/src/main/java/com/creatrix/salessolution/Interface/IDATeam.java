package com.creatrix.salessolution.Interface;

import com.creatrix.salessolution.Activity.Approval.DA.ApproveDARQ;
import com.creatrix.salessolution.Activity.Approval.DA.DAListData;
import com.creatrix.salessolution.Activity.MileageClaim.Model.ApproveMilRQ;

import java.util.List;
import java.util.Map;

public interface IDATeam {
    interface Presenter{
        void SaveTeamDA(ApproveDARQ aInfo);
        void GetTeamDAList(String param, Map<String, String> filter);
    }
    interface View{
        void onTeamDAList(List<DAListData> aList);
        void onSaveSuccess(String message);
        void onSaveError(String message);

    }
}
