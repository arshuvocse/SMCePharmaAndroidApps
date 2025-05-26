package com.creatrix.salessolution.Model.Doctor;

public class SpecialDay {
    int pk;
    int SpecialDayId;
    String SpecialDay;
    String SpeciaDateStr;


    public SpecialDay() {
    }

    public SpecialDay(int specialDayId, String speciaDateStr, String specialDay) {
        SpecialDayId = specialDayId;
        SpeciaDateStr = speciaDateStr;
        SpecialDay = specialDay;
    }

    public int getPk() {
        return pk;
    }

    public void setPk(int pk) {
        this.pk = pk;
    }

    public int getSpecialDayId() {
        return SpecialDayId;
    }

    public void setSpecialDayId(int specialDayId) {
        SpecialDayId = specialDayId;
    }

    public String getSpecialDay() {
        return SpecialDay;
    }

    public void setSpecialDay(String specialDay) {
        SpecialDay = specialDay;
    }

    public String getSpeciaDateStr() {
        return SpeciaDateStr;
    }

    public void setSpeciaDateStr(String speciaDateStr) {
        SpeciaDateStr = speciaDateStr;
    }


    @Override
    public String toString() {
        return SpecialDay;
    }
}
