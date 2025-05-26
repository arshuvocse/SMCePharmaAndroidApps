package com.creatrix.salessolution.Interface;

import android.content.Context;

import com.creatrix.salessolution.Activity.Customer.Pending.CustomerARModel;
import com.creatrix.salessolution.Model.Customer;

import java.util.List;

public interface ICustomer {
    interface Presenter{
        void GetCustomerByUser(int empId);
        void GetCustomerReport(int empId);

    }

    interface View{
        void OnSuccess(List<Customer> aList);
        void OnSuccessCustomerReport(List<CustomerARModel> aList);
        void  OnError(int type);
        void  OnErrorReport(String msg);
    }
}
