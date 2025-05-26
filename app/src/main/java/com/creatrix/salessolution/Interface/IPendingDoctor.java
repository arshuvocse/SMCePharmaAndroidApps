package com.creatrix.salessolution.Interface;

import com.creatrix.salessolution.Activity.Customer.Pending.CustomerARModel;
import com.creatrix.salessolution.Activity.Doctor.Pending.DoctorARModel;
import com.creatrix.salessolution.Model.Customer;

import java.util.List;

public interface IPendingDoctor {
    interface Presenter{
        void GetDoctorReport(int empId);
    }

    interface View{
        void OnSuccessDoctorReport(List<DoctorARModel> aList);
        void  OnErrorReport(String msg);
    }
}
