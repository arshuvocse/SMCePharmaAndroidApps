package com.creatrix.salessolution.Interface;

import com.creatrix.salessolution.Activity.Approval.Prescription.PrescApprovalData;
import com.creatrix.salessolution.Activity.Approval.Prescription.PrescApprovalRQ;


import java.util.List;
import java.util.Map;

public interface IPrescApproval {
    interface  Presenter{
        void getPrescApprovalList(String pram, Map<String,String> mapparam);
        void SavePrescApproval(PrescApprovalRQ aInfo);
    }
    interface  View{
        void onGetPrescApprovalList(List<PrescApprovalData> aList);
        void onSaveSuccess(String message);
        void onError(String message);
    }
}
