package com.creatrix.salessolution.Activity.Doctor.Approval.Model;

import androidx.annotation.Keep;

import com.creatrix.salessolution.Model.Doctor.ChembarList;
import com.creatrix.salessolution.Model.Doctor.DoctorContact;
import com.creatrix.salessolution.Model.Doctor.DoctorSM;
import com.creatrix.salessolution.Model.Doctor.SpecialDay;

import java.util.List;
@Keep
public class DoctorApproveModel {
    int DoctorId;
    String DoctorName;
    String DoctorAddress;
    String DoctorCode;
    String DesignationName;
    String DoctorTypeName;
    String CategoryName;
    String ProgramTypeName;
    String SMCTypeName;
    String SpecialDay;
    String RegionName;
    String AreaName;
    String TerritoryName;
    String SubTerritoryName;
    String MarketName;
    String Remarks;
    String DegreeStr;
    String BrandStr;
    String SpecialityStr;
    String DoctorTypeStr;
    String InstitutionSTr;
    int ContactTypeId;
    String Contact;
    String ContactType;
    String SessionUser;
    int SubMarketId;
    int DoctorTypeId;
    int MarketId;
    int TerritoryId;
    int SubTerritoryId;
    String UnionName;
    int SpecialDayId;
    String SpeciaDateStr;
    int DoctorCategoryId;
    String StationTypeId;
    int ProgramTypeId;
    int DesignationId;
    List<ChembarTypeWithName> aChemberListDAO;
    List<DoctorContact> aDoctorContactDAO;
    List<SpecialDay> aDoctorSpecialDAO;
    public int getDoctorId() {
        return DoctorId;
    }

    public void setDoctorId(int doctorId) {
        DoctorId = doctorId;
    }

    public String getDoctorName() {
        return DoctorName;
    }

    public void setDoctorName(String doctorName) {
        DoctorName = doctorName;
    }

    public String getDoctorAddress() {
        return DoctorAddress;
    }

    public void setDoctorAddress(String doctorAddress) {
        DoctorAddress = doctorAddress;
    }

    public String getDoctorCode() {
        return DoctorCode;
    }

    public void setDoctorCode(String doctorCode) {
        DoctorCode = doctorCode;
    }

    public String getDesignationName() {
        return DesignationName;
    }

    public void setDesignationName(String designationName) {
        DesignationName = designationName;
    }

    public String getDoctorTypeName() {
        return DoctorTypeName;
    }

    public void setDoctorTypeName(String doctorTypeName) {
        DoctorTypeName = doctorTypeName;
    }

    public String getCategoryName() {
        return CategoryName;
    }

    public void setCategoryName(String categoryName) {
        CategoryName = categoryName;
    }

    public String getProgramTypeName() {
        return ProgramTypeName;
    }

    public void setProgramTypeName(String programTypeName) {
        ProgramTypeName = programTypeName;
    }

    public String getSMCTypeName() {
        return SMCTypeName;
    }

    public void setSMCTypeName(String SMCTypeName) {
        this.SMCTypeName = SMCTypeName;
    }

    public String getSpecialDay() {
        return SpecialDay;
    }

    public void setSpecialDay(String specialDay) {
        SpecialDay = specialDay;
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

    public String getRemarks() {
        return Remarks;
    }

    public void setRemarks(String remarks) {
        Remarks = remarks;
    }

    public String getDegreeStr() {
        return DegreeStr;
    }

    public void setDegreeStr(String degreeStr) {
        DegreeStr = degreeStr;
    }

    public String getBrandStr() {
        return BrandStr;
    }

    public void setBrandStr(String brandStr) {
        BrandStr = brandStr;
    }

    public String getSpecialityStr() {
        return SpecialityStr;
    }

    public void setSpecialityStr(String specialityStr) {
        SpecialityStr = specialityStr;
    }

    public String getDoctorTypeStr() {
        return DoctorTypeStr;
    }

    public void setDoctorTypeStr(String doctorTypeStr) {
        DoctorTypeStr = doctorTypeStr;
    }

    public String getInstitutionSTr() {
        return InstitutionSTr;
    }

    public void setInstitutionSTr(String institutionSTr) {
        InstitutionSTr = institutionSTr;
    }

    public int getContactTypeId() {
        return ContactTypeId;
    }

    public void setContactTypeId(int contactTypeId) {
        ContactTypeId = contactTypeId;
    }

    public String getContact() {
        return Contact;
    }

    public void setContact(String contact) {
        Contact = contact;
    }

    public String getSessionUser() {
        return SessionUser;
    }

    public void setSessionUser(String sessionUser) {
        SessionUser = sessionUser;
    }

    public int getSubMarketId() {
        return SubMarketId;
    }

    public void setSubMarketId(int subMarketId) {
        SubMarketId = subMarketId;
    }

    public int getDoctorTypeId() {
        return DoctorTypeId;
    }

    public void setDoctorTypeId(int doctorTypeId) {
        DoctorTypeId = doctorTypeId;
    }

    public int getMarketId() {
        return MarketId;
    }

    public void setMarketId(int marketId) {
        MarketId = marketId;
    }

    public int getTerritoryId() {
        return TerritoryId;
    }

    public void setTerritoryId(int territoryId) {
        TerritoryId = territoryId;
    }

    public int getSubTerritoryId() {
        return SubTerritoryId;
    }

    public void setSubTerritoryId(int subTerritoryId) {
        SubTerritoryId = subTerritoryId;
    }

    public String getUnionName() {
        return UnionName;
    }

    public void setUnionName(String unionName) {
        UnionName = unionName;
    }

    public int getSpecialDayId() {
        return SpecialDayId;
    }

    public void setSpecialDayId(int specialDayId) {
        SpecialDayId = specialDayId;
    }

    public String getSpeciaDateStr() {
        return SpeciaDateStr;
    }

    public void setSpeciaDateStr(String speciaDateStr) {
        SpeciaDateStr = speciaDateStr;
    }

    public int getDoctorCategoryId() {
        return DoctorCategoryId;
    }

    public void setDoctorCategoryId(int doctorCategoryId) {
        DoctorCategoryId = doctorCategoryId;
    }

    public String getStationTypeId() {
        return StationTypeId;
    }

    public void setStationTypeId(String stationTypeId) {
        StationTypeId = stationTypeId;
    }

    public int getProgramTypeId() {
        return ProgramTypeId;
    }

    public void setProgramTypeId(int programTypeId) {
        ProgramTypeId = programTypeId;
    }

    public int getDesignationId() {
        return DesignationId;
    }

    public void setDesignationId(int designationId) {
        DesignationId = designationId;
    }

    public String getRegionName() {
        return RegionName;
    }

    public void setRegionName(String regionName) {
        RegionName = regionName;
    }

    public String getContactType() {
        return ContactType;
    }

    public void setContactType(String contactType) {
        ContactType = contactType;
    }

    public List<ChembarTypeWithName> getaChemberListDAO() {
        return aChemberListDAO;
    }

    public void setaChemberListDAO(List<ChembarTypeWithName> aChemberListDAO) {
        this.aChemberListDAO = aChemberListDAO;
    }

    public List<DoctorContact> getaDoctorContactDAO() {
        return aDoctorContactDAO;
    }

    public void setaDoctorContactDAO(List<DoctorContact> aDoctorContactDAO) {
        this.aDoctorContactDAO = aDoctorContactDAO;
    }

    public List<com.creatrix.salessolution.Model.Doctor.SpecialDay> getaDoctorSpecialDAO() {
        return aDoctorSpecialDAO;
    }

    public void setaDoctorSpecialDAO(List<com.creatrix.salessolution.Model.Doctor.SpecialDay> aDoctorSpecialDAO) {
        this.aDoctorSpecialDAO = aDoctorSpecialDAO;
    }
}
