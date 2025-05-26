package com.creatrix.salessolution.Interface;

public interface IMore {
    interface Presenter{

        void doCustomerSync(int empId);
        void doProductSync(int empId);
        void doDoctorSync(int empId);
        void doOtherSync(String empcode,String emprole);

    }
    interface View{
        void onSuccess(String Message);
        void onError(String Message);
        void onCustomerSync(String Message);
        void onProductSync(String Message);
        void onDoctorSync(String Message,boolean a);
        void onOtherSync(String Message);
    }
}

