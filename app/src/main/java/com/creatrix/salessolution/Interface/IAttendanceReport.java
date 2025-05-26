package com.creatrix.salessolution.Interface;

import com.creatrix.salessolution.Model.Customer;
import com.creatrix.salessolution.Model.Report_AttendanceViewModel;

import java.util.List;

public interface IAttendanceReport {
    interface Presenter{
        void GetAttendanceData(int empId,String startDate,String endDate);

    }

    interface View{
        void OnSuccess(List<Report_AttendanceViewModel> aList);
        void  OnError(String msg);
    }
}
