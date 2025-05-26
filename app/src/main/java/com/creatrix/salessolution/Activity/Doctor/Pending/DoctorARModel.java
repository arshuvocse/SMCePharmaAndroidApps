package com.creatrix.salessolution.Activity.Doctor.Pending;

import androidx.annotation.Keep;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Keep
public class DoctorARModel {

    @SerializedName("createdAt")
    @Expose
    private String createdAt;
    @SerializedName("DoctorCode")
    @Expose
    private String doctorCode;
    @SerializedName("MarketName")
    @Expose
    private String marketName;
    @SerializedName("ActionStatus")
    @Expose
    private String actionStatus;
    @SerializedName("WaitingRole")
    @Expose
    private String waitingRole;
    @SerializedName("WatingEmployee")
    @Expose
    private String watingEmployee;

    @SerializedName("GroupId")
    @Expose
    private String GroupId;

    @SerializedName("RegionId")
    @Expose
    private String RegionId;
    @SerializedName("AreaId")
    @Expose
    private String AreaId;
    @SerializedName("TerritoryId")
    @Expose
    private String TerritoryId;
    @SerializedName("SubTerritoryId")
    @Expose
    private String SubTerritoryId;
    @SerializedName("MarketId")
    @Expose
    private String MarketId;
    String DoctorTypeName;
    int DoctorTypeId;

    String ProgramTypeName;
    int ProgramTypeId;
    String SMCType;
    int SMCTypeId;

    int DoctorId;
    String GroupName;
    String RegionName;
    String AreaName;
    String TerritoryName;
    String SubTerritoryName;
    String Status;


    public String getDoctorTypeName() {
        return DoctorTypeName;
    }

    public void setDoctorTypeName(String doctorTypeName) {
        DoctorTypeName = doctorTypeName;
    }

    public int getDoctorTypeId() {
        return DoctorTypeId;
    }

    public void setDoctorTypeId(int doctorTypeId) {
        DoctorTypeId = doctorTypeId;
    }

    public int getDoctorId() {
        return DoctorId;
    }

    public void setDoctorId(int doctorId) {
        DoctorId = doctorId;
    }

    public String getGroupName() {
        return GroupName;
    }

    public void setGroupName(String groupName) {
        GroupName = groupName;
    }

    public String getRegionName() {
        return RegionName;
    }

    public void setRegionName(String regionName) {
        RegionName = regionName;
    }

    public String getAreaName() {
        return AreaName;
    }

    public void setAreaName(String areaName) {
        AreaName = areaName;
    }

    public String getTerritoryName() {
        return TerritoryName;
    }

    public void setTerritoryName(String territoryName) {
        TerritoryName = territoryName;
    }

    public String getSubTerritoryName() {
        return SubTerritoryName;
    }

    public void setSubTerritoryName(String subTerritoryName) {
        SubTerritoryName = subTerritoryName;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getDoctorCode() {
        return doctorCode;
    }

    public void setDoctorCode(String doctorCode) {
        this.doctorCode = doctorCode;
    }

    public String getMarketName() {
        return marketName;
    }

    public void setMarketName(String marketName) {
        this.marketName = marketName;
    }

    public String getActionStatus() {
        return actionStatus;
    }

    public void setActionStatus(String actionStatus) {
        this.actionStatus = actionStatus;
    }

    public String getWaitingRole() {
        return waitingRole;
    }

    public void setWaitingRole(String waitingRole) {
        this.waitingRole = waitingRole;
    }

    public String getWatingEmployee() {
        return watingEmployee;
    }

    public void setWatingEmployee(String watingEmployee) {
        this.watingEmployee = watingEmployee;
    }

    public String getGroupId() {
        return GroupId;
    }

    public void setGroupId(String groupId) {
        GroupId = groupId;
    }

    public String getRegionId() {
        return RegionId;
    }

    public void setRegionId(String regionId) {
        RegionId = regionId;
    }

    public String getAreaId() {
        return AreaId;
    }

    public void setAreaId(String areaId) {
        AreaId = areaId;
    }

    public String getTerritoryId() {
        return TerritoryId;
    }

    public void setTerritoryId(String territoryId) {
        TerritoryId = territoryId;
    }

    public String getSubTerritoryId() {
        return SubTerritoryId;
    }

    public void setSubTerritoryId(String subTerritoryId) {
        SubTerritoryId = subTerritoryId;
    }

    public String getMarketId() {
        return MarketId;
    }

    public void setMarketId(String marketId) {
        MarketId = marketId;
    }

    public String getProgramTypeName() {
        return ProgramTypeName;
    }

    public void setProgramTypeName(String programTypeName) {
        ProgramTypeName = programTypeName;
    }

    public int getProgramTypeId() {
        return ProgramTypeId;
    }

    public void setProgramTypeId(int programTypeId) {
        ProgramTypeId = programTypeId;
    }

    public String getSMCType() {
        return SMCType;
    }

    public void setSMCType(String SMCType) {
        this.SMCType = SMCType;
    }

    public int getSMCTypeId() {
        return SMCTypeId;
    }

    public void setSMCTypeId(int SMCTypeId) {
        this.SMCTypeId = SMCTypeId;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }
}
