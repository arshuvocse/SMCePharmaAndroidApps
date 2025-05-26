package com.creatrix.salessolution.Interface;

import com.creatrix.salessolution.Activity.Approval.DCR.DcrApprovalData;
import com.creatrix.salessolution.Activity.Approval.DCR.DcrApprovalRQ;

import java.util.List;
import java.util.Map;

public interface IDcrApproval {
    interface  Presenter{
        void getDcrApprovalList(String pram, Map<String,String> mapparam);
        void SaveDcrApproval(DcrApprovalRQ aInfo);
    }
    interface  View{
        void onGetDcrApprovalList(List<DcrApprovalData> aList);
        void onSaveSuccess(String message);
        void onError(String message);
    }
}

