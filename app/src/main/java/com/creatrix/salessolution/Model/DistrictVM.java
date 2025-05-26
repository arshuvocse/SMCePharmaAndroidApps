package com.creatrix.salessolution.Model;

public class DistrictVM {
    int DistrictId;
    String DistrictName;
    int DivisionId;

    public int getDistrictId() {
        return DistrictId;
    }

    public void setDistrictId(int districtId) {
        DistrictId = districtId;
    }

    public String getDistrictName() {
        return DistrictName;
    }

    public void setDistrictName(String districtName) {
        DistrictName = districtName;
    }

    public int getDivisionId() {
        return DivisionId;
    }

    public void setDivisionId(int divisionId) {
        DivisionId = divisionId;
    }

    @Override
    public String toString() {
        return DistrictName;
    }
}
