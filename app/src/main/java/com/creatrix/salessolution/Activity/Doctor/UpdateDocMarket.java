package com.creatrix.salessolution.Activity.Doctor;

import androidx.annotation.Keep;

@Keep public class UpdateDocMarket {
    int GroupId;
    int ZoneId;
    int AreaId;
    int TeritoryId;
    int STeritoryId;
    int MarketId;
    String EmpId;
    String DoctorId;

    public int getGroupId() {
        return GroupId;
    }

    public void setGroupId(int groupId) {
        GroupId = groupId;
    }

    public int getZoneId() {
        return ZoneId;
    }

    public void setZoneId(int zoneId) {
        ZoneId = zoneId;
    }

    public int getAreaId() {
        return AreaId;
    }

    public void setAreaId(int areaId) {
        AreaId = areaId;
    }

    public int getTeritoryId() {
        return TeritoryId;
    }

    public void setTeritoryId(int teritoryId) {
        TeritoryId = teritoryId;
    }

    public int getSTeritoryId() {
        return STeritoryId;
    }

    public void setSTeritoryId(int STeritoryId) {
        this.STeritoryId = STeritoryId;
    }

    public int getMarketId() {
        return MarketId;
    }

    public void setMarketId(int marketId) {
        MarketId = marketId;
    }

    public String getEmpId() {
        return EmpId;
    }

    public void setEmpId(String empId) {
        EmpId = empId;
    }

    public String getDoctorId() {
        return DoctorId;
    }

    public void setDoctorId(String doctorId) {
        DoctorId = doctorId;
    }
}
