package com.creatrix.salessolution.Interface;

import android.widget.RelativeLayout;

import com.creatrix.salessolution.Activity.Attendance.Model.ApproveRQ;
import com.creatrix.salessolution.Activity.Attendance.Model.AttenApproval;
import com.creatrix.salessolution.Activity.Attendance.Model.ButtonRP;
import com.creatrix.salessolution.Model.Attendance;

import java.util.List;
import java.util.Map;

public interface ISyncMaster {
    interface  Presenter{
        void cllNSM(String empcode, String emproleempid);
        void cllDZSM(String empcode, String emprole);
        void cllAM(String empcode, String emprole);
        void cllMIO(String empcode, String emprole);
        //Area
        void cllGroup(String empcode, String emprole);
        void cllZone(String empcode, String emprole);
        void cllArea(String empcode, String emprole);
        void cllTeritory(String empcode, String emprole);
        void cllSTeritory(String empcode, String emprole);
        void cllMarket(String empcode, String emprole);
        //getCustomerInfo
        void cllCustomer(int empid);
        void cllCustomerStation(int empid);
        //Doctor
        void cllDoctor(int empid);
        void cllDesignation(int empid);
        void cllDegree(int empid);
        void cllSpeciality(int empid);
        void cllSpecialday(int empid);
        void cllInstitution(int empid);
        void cllChamberType(int empid);
        void cllChamberName(int empid);
        void cllBrand(int empid);
        void cllDoccategory(int empid);
        void cllProgramtypey(int empid);
        //getRoleWiseUser
        void cllUserRole(int empid);
        void cllUserByRole(int empid);
        //Product
        void cllProduct(int empid);
        void cllProductSample(int empid);
        void cllProductGift(int empid);
        void cllQuotedPrice(int empid);
       // getTypeInfo
        void cllDoctorType(int empId, int year);
        void cllCustomerType(int empId, int year);
        void cllDoctorContactType(int empId, int yeard);
        void cllExpenseType(int empId, int year,String role);
        void cllLeaveType(int empId, int year);
        void cllPrescriptionType(int empId, int year);

        void cllDivision();

        void cllDistrict();

        void cllThana();

        void cllNonEffectivereason(int empId, int year);
        void cllTransportList(int empId, int year);
        void cllTourPurpose(int empId, int year);
        void cllVisitType(int empId, int year);
        void callProviderType();
        void callSMCType();
    }
    interface View{
        void onGetNSM(String a,boolean t);
        void onGetDZSM(String a,boolean t);
        void onGetAM(String a,boolean t);
        void onGetMIO(String a,boolean t);

        void onGetGroup(String a,boolean t);
        void onGetZone(String a,boolean t);
        void onGetArea(String a,boolean t);
        void onGetTeritory(String a,boolean t);
        void onGetSTeritory(String a,boolean t);
        void onGetMarket(String a,boolean t);

        void onGetCustomer(String a,boolean t);
        void onGetCustomerStation(String a,boolean t);

        void onGetDoctor(String a,boolean t);
        void onGetDesignation(String a,boolean t);
        void onGetDegree(String a,boolean t);
        void onGetSpeciality(String a,boolean t);
        void onGetSpecialday(String a,boolean t);
        void onGetInstitution(String a,boolean t);
        void onGetChamberType(String a,boolean t);
        void onGetChamberName(String a,boolean t);
        void onGetBrand(String a,boolean t);
        void onGetDoccategory(String a,boolean t);
        void onGetProgramtypey(String a,boolean t);
        void onGetUserRole(String a,boolean t);
        void onGetUserByRole(String a,boolean t);
        //Product
        void onGetProduct(String a,boolean t);
        void onGetProductSample(String a,boolean t);
        void onGetProductGift(String a,boolean t);
        void onGetQuotedPrice(String a,boolean t);
        // getTypeInfo
        void onGetDoctorType(String a,boolean t);
        void onGetCustomerType(String a,boolean t);
        void onGetDoctorContactType(String a,boolean t);
        void onGetExpenseType(String a,boolean t);
        void onGetLeaveType(String a,boolean t);
        void onGetPrescriptionType(String a,boolean t);
        void onGetNonEffectivereason(String a,boolean t);
        void onGetTransportList(String a,boolean t);
        void onGetTourPurpose(String a,boolean t);
        void onGetVisitType(String a,boolean t);

        void onGetProviderType(String a,boolean t);
        void onGetSMCType(String a,boolean t);

    }
}
