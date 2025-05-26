package com.creatrix.salessolution.Interface;

import com.creatrix.salessolution.Activity.MileageClaim.Model.ApproveMilRQ;
import com.creatrix.salessolution.Activity.MileageClaim.Model.MileageListTeam;


import java.util.List;
import java.util.Map;

public interface IMileageTeam {
        interface Presenter{
            void SaveTeamMileageClaim(ApproveMilRQ aInfo);
            void GetTeamMileageList(String param, Map<String, String> filter);
        }
        interface View{
            void onTeamMileageList(List<MileageListTeam> aList);
            void onSaveSuccess(String message);
            void onSaveError(String message);

        }
    }

