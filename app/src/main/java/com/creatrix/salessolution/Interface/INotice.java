package com.creatrix.salessolution.Interface;

import com.creatrix.salessolution.Model.Notice;

import java.util.ArrayList;
import java.util.List;

public interface INotice {
    interface  Presenter{
        void getNotices(int empId);
        void getNoticesforPop(int empId);
        void postSeenNotice(int id,int EmpId,String AppSeenDate);

    }
    interface  View{
        void onSuccess(List<Notice> arrayList);
        void onError(String message);
    }

}
