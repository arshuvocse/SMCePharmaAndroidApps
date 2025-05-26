package com.creatrix.salessolution.Activity.Customer.Pending;

import androidx.annotation.Keep;

@Keep public class UpdateCustMaket {
    int GroupId;
    int ZoneId;
    int AeaId;
    int TeritoryId;
    int STeritoryId;
    int MarketId;
    int EmpId;
    int CustomerId;

    public int getAeaId() {
        return AeaId;
    }

    public void setAeaId(int aeaId) {
        AeaId = aeaId;
    }

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

    public int getEmpId() {
        return EmpId;
    }

    public void setEmpId(int empId) {
        EmpId = empId;
    }

    public int getCustomerId() {
        return CustomerId;
    }

    public void setCustomerId(int customerId) {
        CustomerId = customerId;
    }
}
