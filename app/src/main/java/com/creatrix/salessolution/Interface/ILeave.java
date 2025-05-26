package com.creatrix.salessolution.Interface;

import com.creatrix.salessolution.Model.LeaveSM;
import com.creatrix.salessolution.Model.LeaveTypeInfo;
import com.creatrix.salessolution.Model.LeaveVM;

import java.util.List;

public interface ILeave {
    interface Presenter{
        void GetLeaveTyep(int empId,int year);
        void SaveLeave(LeaveSM aInfo);
        void GetLeaveRecords(int empId,String Month,int year);
    }

    interface View{
        void onLeaveTypeGet(List<LeaveTypeInfo> aList);
        void onSaveSuccess(String message);
        void onSaveError(String message);
        void onLeaveRecordsGet(List<LeaveVM> aList);
    }
}
