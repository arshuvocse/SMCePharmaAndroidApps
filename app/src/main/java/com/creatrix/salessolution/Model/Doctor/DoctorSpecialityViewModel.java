package com.creatrix.salessolution.Model.Doctor;

public class DoctorSpecialityViewModel {

  int  SpecialityId;
  String SpecialityName;

    public int getSpecialityId() {
        return SpecialityId;
    }

    public void setSpecialityId(int specialityId) {
        SpecialityId = specialityId;
    }

    public String getSpecialityName() {
        return SpecialityName;
    }

    public void setSpecialityName(String specialityName) {
        SpecialityName = specialityName;
    }

    @Override
    public String toString() {
        return SpecialityName;
    }
}
