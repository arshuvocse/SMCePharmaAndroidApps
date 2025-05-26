package com.creatrix.salessolution.Interface;

import android.widget.RelativeLayout;

import com.creatrix.salessolution.Activity.Attendance.Model.ApproveRQ;
import com.creatrix.salessolution.Activity.Attendance.Model.AttenApproval;
import com.creatrix.salessolution.Activity.Attendance.Model.ButtonRP;
import com.creatrix.salessolution.Model.Attendance;

import java.util.List;
import java.util.Map;

public interface IAttendance {
    interface  Presenter{
        void doSavePuncINInfo(Attendance attendance, RelativeLayout masterLayout);
        //void teamAttendanceList(List<TeamAtten> teamlist);
        void teamAttendanceList(String list,String param,String role);
        void teamAttendanceListFilter(String list, String param, Map<String,String> filter);
        void teamAttendanceApprove(ApproveRQ approveRQ);
        void teamAttendanceApproveAll(ApproveRQ approveRQ);
        void button(int empid);

    }
    interface View{
        void onSuccessapprove(boolean t);
        void onSuccessapproveAll(String t);
        void onSuccess(String message,boolean status,int type);
        void onSuccessTeamAtten(List<AttenApproval> teamlist);
        void onError(String message,int type);
        void onButtonView(ButtonRP buttonRP);
    }
}
