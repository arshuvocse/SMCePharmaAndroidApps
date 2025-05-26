package com.creatrix.salessolution.Interface;

import com.creatrix.salessolution.Model.Training;

import java.util.List;

public interface ITraining {

    interface Presenter{
        void getTraining(int empid);
        void seenTraining(int id,int empid);
    }
    interface View{
        void onSuccess(List<Training> tList);
        void onError(String msg);
    }
}
