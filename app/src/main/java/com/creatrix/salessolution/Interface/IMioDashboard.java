package com.creatrix.salessolution.Interface;

import com.creatrix.salessolution.Model.Dashboard_SummeryVM;
import com.creatrix.salessolution.Model.Notice;

import java.util.List;

public interface IMioDashboard {

    interface  Presenter{
        void getDashboardSummeryData(int empId,String currentDate);
        void getTodaySummery(int empId,String currentDate);

    }
    interface  View{
        void onDashboardSummeryDataBind(Dashboard_SummeryVM aData);
        void onTodaySummeryDataBind(Dashboard_SummeryVM aData);
        void onError(String message);
    }
}
