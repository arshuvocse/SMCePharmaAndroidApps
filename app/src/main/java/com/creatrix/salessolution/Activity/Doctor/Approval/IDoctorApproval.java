package com.creatrix.salessolution.Activity.Doctor.Approval;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.creatrix.salessolution.Activity.Doctor.Approval.Model.DoctorApprovalList;
import com.creatrix.salessolution.Activity.Doctor.Approval.Model.DoctorApprovalRQ;
import com.creatrix.salessolution.Model.Customer;

import java.util.List;
import java.util.Map;

public interface IDoctorApproval {
    interface Presenter{
        void GetDoctorApprovalList(String param, Map<String, String> filter, ConstraintLayout layout);
        void SaveDoctorApprovalList(DoctorApprovalRQ doctorApprovalRQ);

    }

    interface View{
        void OnRevieveDoctorApproval(List<DoctorApprovalList> aList);
        void OnSuccess(String msg);
        void  OnError(int type);
    }
}
