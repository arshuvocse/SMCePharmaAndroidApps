package com.creatrix.salessolution.Interface;

import com.creatrix.salessolution.Activity.Doctor.DCR.NonEffectiveReason;
import com.creatrix.salessolution.Model.DcrSM;
import com.creatrix.salessolution.Model.Doctor.DoctorBrand;
import com.creatrix.salessolution.Model.Doctor.DoctorChamberName;
import com.creatrix.salessolution.Model.Doctor.DoctorChamberTypeVM;
import com.creatrix.salessolution.Model.Gift;
import com.creatrix.salessolution.Model.Product;
import com.creatrix.salessolution.Model.ProductSample;
import com.creatrix.salessolution.Model.TourTypeViewModel;
import com.creatrix.salessolution.Model.UserByRole;
import com.creatrix.salessolution.Model.UserRole;

import java.util.List;

public interface IDCR {
    interface Presenter{
        void GetVisitType();
        void GetChamber(int doctorId);
        void GetGiftProduct(String empId);
        void GetDoctorBrand(int empId);
        void GetSampleProduct(String empId);
        void SaveDCR(DcrSM aInfo);
        void GetUserRole();
        void GetUserByRoleId(int roleid/*,int empId*/);
        void GetNoneffective();
    }

    interface View{
        void OnVisitTypeGet(List<TourTypeViewModel> aInfo);
       // void OnChamberGet(List<DoctorChamberTypeVM> aInfo);
        void OnChamberGet(List<DoctorChamberName> aInfo);
        void OnGiftProductGet(List<Gift> aInfo);
        void OnSampleProductGet(List<ProductSample> aInfo);
        void OnDoctorBrandGet(List<DoctorBrand> aInfo);
        void OnDcrSaveSuccess(String message);
        void OnDcrSaveError(String message);
        void onUserRoleGet(List<UserRole> aList);
        void onUserGet(List<UserByRole> aList);
        void onNoneffectiveGet(List<NonEffectiveReason> aList);
    }
}
