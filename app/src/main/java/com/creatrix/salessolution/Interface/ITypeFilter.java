package com.creatrix.salessolution.Interface;

import com.creatrix.salessolution.Model.CustomerType;
import com.creatrix.salessolution.Model.Doctor.ContactTypeVM;
import com.creatrix.salessolution.Model.Doctor.DoctorTypeVM;
import com.creatrix.salessolution.Model.Doctor.SpecialDay;
import com.creatrix.salessolution.Model.ModelProviderType;
import com.creatrix.salessolution.Model.ModelSMCType;

import java.util.List;

public interface ITypeFilter {
    interface Presenter{
        void GetDoctorType(int id);
        void GetCustomerType(int id);
        void GetContactType(int id);
        void GetProviderType(int id);
        void GetSMCType(int id,String who);
        void GetSpecialType(int id);
    }

    interface View{
        void onDoctorTypeReceived(List<DoctorTypeVM> aList);
        void onCustomerTypeReceived(List<CustomerType> aList);
        void onContactTypeReceived(List<ContactTypeVM> aList);
        void onProviderTypeReceived(List<ModelProviderType> aList);
        void onSMCTypeReceived(List<ModelSMCType> aList);
        void onSpecialTypeReceived(List<SpecialDay> aList);


    }


}
