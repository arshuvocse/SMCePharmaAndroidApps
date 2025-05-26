package com.creatrix.salessolution.Activity.Doctor.VisitPlan;

import com.creatrix.salessolution.Model.Doctor.DoctorListViewModel;

import java.util.List;

public interface ChkedItemListener {
    void ckdItemName(List<DoctorListViewModel> st,int Pos);
    void unckdItemName(List<DoctorListViewModel> st,int Pos);
}
