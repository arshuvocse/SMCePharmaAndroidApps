package com.creatrix.salessolution.Interface;

import com.creatrix.salessolution.Model.Doctor.DoctorChamberName;
import com.creatrix.salessolution.Model.PrescriptionSM;
import com.creatrix.salessolution.Model.PrescriptionTYpe;
import com.creatrix.salessolution.Model.Product;

import java.util.List;

public interface IPrescription {
    interface Presenter{
        void GetPrescriptionType(int empId);
        void GetProducts(int empId);
        void GetChamberId(int docId);
        void SavePrescription(PrescriptionSM aPres);
        void SubmitPrescription(PrescriptionSM aPres);

    }
    interface View{
        void onPrescriptionTypeGet(List<PrescriptionTYpe> aList);
        void onProductGet(List<Product> aList);
        void onChamberGet(List<DoctorChamberName> cList);
        void onSaveSuccess(String message);
        void onSaveError(String message);
    }
}
