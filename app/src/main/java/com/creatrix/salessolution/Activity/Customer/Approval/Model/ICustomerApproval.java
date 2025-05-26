package com.creatrix.salessolution.Activity.Customer.Approval.Model;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.creatrix.salessolution.Activity.Doctor.Approval.Model.DoctorApprovalList;
import com.creatrix.salessolution.Activity.Doctor.Approval.Model.DoctorApprovalRQ;

import java.util.List;
import java.util.Map;

public interface ICustomerApproval {
    interface Presenter{
        void GetCustomerApprovalList(String param, Map<String, String> filter, ConstraintLayout layout);
        void SaveCustomerApprovalList(CustomerApprovalRQ customerApprovalRQ);

    }

    interface View{
        void OnRevieveCustomerApproval(List<CustomerApprovalList> aList);
        void OnSuccess(String msg);
        void  OnError(int type);
    }
}
