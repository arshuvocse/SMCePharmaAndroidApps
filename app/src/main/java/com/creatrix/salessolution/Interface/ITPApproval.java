package com.creatrix.salessolution.Interface;


import com.creatrix.salessolution.Activity.Approval.TourPlan.Model.TPApprovalSaveBody;
import com.creatrix.salessolution.Activity.Approval.TourPlan.Model.TourPlanApprovalData;


import java.util.List;
import java.util.Map;

public interface ITPApproval {
    interface Presenter{
        void getTPApprovalList(String pram, Map<String,String> mapparam);
        void SaveTPApproval(TPApprovalSaveBody aInfo);
    }
    interface  View{
        void onGetTPApprovalList(List<TourPlanApprovalData> aList);
        void onSaveSuccess(String message);
        void onError(String message);
    }
}