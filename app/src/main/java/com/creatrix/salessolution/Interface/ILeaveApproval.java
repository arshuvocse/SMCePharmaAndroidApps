package com.creatrix.salessolution.Interface;

import com.creatrix.salessolution.Activity.Approval.Leave.LeaveApprovalData;
import com.creatrix.salessolution.Activity.Approval.Leave.LeaveApprovalRQ;
import java.util.List;
import java.util.Map;

public interface ILeaveApproval {
    interface Presenter{
        void getLeaveApprovalList(String pram, Map<String,String> mapparam);
        void SaveLeaveApproval(LeaveApprovalRQ aInfo);
    }
    interface  View{
        void onGetLeaveApprovalList(List<LeaveApprovalData> aList);
        void onSaveSuccess(String message,String what);
        void onError(String message);
    }
}