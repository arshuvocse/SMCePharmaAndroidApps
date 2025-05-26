package com.creatrix.salessolution.Interface;

import com.creatrix.salessolution.Activity.Doctor.TourePlan.Model.TourPlanReq;
import com.creatrix.salessolution.Activity.Doctor.VisitPlan.Model.VisitplanModel;
import com.creatrix.salessolution.Model.MonthDate;
import com.creatrix.salessolution.Model.TourPlanViewModel;

import java.util.List;

public interface IVisitPlan {
    interface  Presenter{
        void getVisitPlanDataByEmpId(List<MonthDate> aMondateList,boolean is_Entry,int month, int year, int empId);
        void saveVisitPlanPerdayByEmpId(List<VisitplanModel> vpl);

    }
    interface View{
       // void OnVisitPlanDataGet(List<TourPlanViewModel> aList);
        void OnArreangList(List<MonthDate> aMondateList,boolean is_Entry, List<VisitplanModel> aTpLIst);
        void OnFailour(String msg);
        void OnSuccessVPPDay(String msg);

    }
}
