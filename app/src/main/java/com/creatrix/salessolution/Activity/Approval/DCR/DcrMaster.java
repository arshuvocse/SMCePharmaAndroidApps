package com.creatrix.salessolution.Activity.Approval.DCR;

import androidx.annotation.Keep;

import com.creatrix.salessolution.Model.Doctor.DoctorBrand;
import com.creatrix.salessolution.Model.UserByRole;

import java.util.List;
@Keep public class DcrMaster {
    int DcrId;
    String DcrDate;
    String DoctorName;
    String VisitTypeName;
    String ChemberName;
    String EntryBy;
    String EntryDate;
    String UpdateBy;
    String UpdateDate;
    String Remarks;
    String ReasonName;
    String GroupName;
    String RegionName;
    String AreaName;
    String TerritoryName;
    String SubTerritoryName;
    String MarketName;

    String ApprovalStatus;
    String EntryDate_Apps;
    List<DcrApplogProduct> aDCRProductDAOList;
    List<DoctorBrand> doctorBrand;
    List<UserByRole> VisitedWithDAO;

    String Latitude;
    String Longitude;
    String StreetAddress;
    public int getDcrId() {
        return DcrId;
    }

    public void setDcrId(int dcrId) {
        DcrId = dcrId;
    }

    public String getDcrDate() {
        return DcrDate;
    }

    public void setDcrDate(String dcrDate) {
        DcrDate = dcrDate;
    }

    public String getDoctorName() {
        return DoctorName;
    }

    public void setDoctorName(String doctorName) {
        DoctorName = doctorName;
    }

    public String getVisitTypeName() {
        return VisitTypeName;
    }

    public void setVisitTypeName(String visitTypeName) {
        VisitTypeName = visitTypeName;
    }

    public String getChemberName() {
        return ChemberName;
    }

    public void setChemberName(String chemberName) {
        ChemberName = chemberName;
    }

    public String getEntryBy() {
        return EntryBy;
    }

    public void setEntryBy(String entryBy) {
        EntryBy = entryBy;
    }

    public String getEntryDate() {
        return EntryDate;
    }

    public void setEntryDate(String entryDate) {
        EntryDate = entryDate;
    }

    public String getUpdateBy() {
        return UpdateBy;
    }

    public void setUpdateBy(String updateBy) {
        UpdateBy = updateBy;
    }

    public String getUpdateDate() {
        return UpdateDate;
    }

    public void setUpdateDate(String updateDate) {
        UpdateDate = updateDate;
    }

    public String getRemarks() {
        return Remarks;
    }

    public void setRemarks(String remarks) {
        Remarks = remarks;
    }

    public String getReasonName() {
        return ReasonName;
    }

    public void setReasonName(String reasonName) {
        ReasonName = reasonName;
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

    public String getMarketName() {
        return MarketName;
    }

    public void setMarketName(String marketName) {
        MarketName = marketName;
    }

    public String getApprovalStatus() {
        return ApprovalStatus;
    }

    public void setApprovalStatus(String approvalStatus) {
        ApprovalStatus = approvalStatus;
    }

    public String getEntryDate_Apps() {
        return EntryDate_Apps;
    }

    public void setEntryDate_Apps(String entryDate_Apps) {
        EntryDate_Apps = entryDate_Apps;
    }

    public List<DcrApplogProduct> getaDCRProductDAOList() {
        return aDCRProductDAOList;
    }

    public void setaDCRProductDAOList(List<DcrApplogProduct> aDCRProductDAOList) {
        this.aDCRProductDAOList = aDCRProductDAOList;
    }

    public List<DoctorBrand> getDoctorBrand() {
        return doctorBrand;
    }

    public void setDoctorBrand(List<DoctorBrand> doctorBrand) {
        this.doctorBrand = doctorBrand;
    }

    public List<UserByRole> getVisitedWithDAO() {
        return VisitedWithDAO;
    }

    public void setVisitedWithDAO(List<UserByRole> visitedWithDAO) {
        VisitedWithDAO = visitedWithDAO;
    }

    public String getLatitude() {
        return Latitude;
    }

    public void setLatitude(String latitude) {
        Latitude = latitude;
    }

    public String getLongitude() {
        return Longitude;
    }

    public void setLongitude(String longitude) {
        Longitude = longitude;
    }

    public String getStreetAddress() {
        return StreetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        StreetAddress = streetAddress;
    }
}
