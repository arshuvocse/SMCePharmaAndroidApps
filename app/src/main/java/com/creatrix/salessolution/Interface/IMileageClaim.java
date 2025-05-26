package com.creatrix.salessolution.Interface;

import com.creatrix.salessolution.Model.MilageClaimReport;
import com.creatrix.salessolution.Model.MileageClaimSM;
import com.creatrix.salessolution.Model.Transport;

import java.util.List;

public interface IMileageClaim {
    interface Presenter{
        void GetTransportList();
        void SaveMileageClaim(MileageClaimSM aInfo);
        void GetMileageList(int empId);
    }
    interface View{
        void onTransportListGet(List<Transport> aList);
        void onSaveSuccess(String message);
        void onSaveError(String message);
        void onMileageListGet(List<MilageClaimReport> aList);
    }
}
