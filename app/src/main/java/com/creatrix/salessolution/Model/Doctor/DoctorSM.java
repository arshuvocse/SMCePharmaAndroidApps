package com.creatrix.salessolution.Model.Doctor;

import android.provider.ContactsContract;

import java.util.List;

public class DoctorSM {

    int DoctorId;
    String DoctorName;
    String DoctorAddress;
    String Remarks;
    String DegreeStr;
    String BrandStr;
    String SpecialityStr;
    String DoctorTypeStr;
    String InstitutionSTr;
    int ContactTypeId;
    String Contact;
    int SessionUser;
    int MarketId;
    int AreaId;
    int TerritoryId;
    int SubTerritoryId;
    String UnionName;
    int SpecialDayId;
    String SpeciaDateStr;
    int DoctorCategoryId;
    int StationTypeId;
    int ProgramTypeId;
    int SMCTypeId;
    int DoctorTypeId;
    int DesignationId;
    List<ChembarList> aChemberListDAO;
    List<DoctorContact> aDoctorContactDAO;
    List<SpecialDay> aDoctorSpecialDAO;
    public DoctorSM() {
    }

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

    public String getRemarks() {
        return Remarks;
    }

    public void setRemarks(String remarks) {
        Remarks = remarks;
    }


    public int getContactTypeId() {
        return ContactTypeId;
    }

    public void setContactTypeId(int contactTypeId) {
        ContactTypeId = contactTypeId;
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


    public String getContact() {
        return Contact;
    }

    public void setContact(String contact) {
        Contact = contact;
    }

    public int getSessionUser() {
        return SessionUser;
    }

    public void setSessionUser(int sessionUser) {
        SessionUser = sessionUser;
    }


    public int getMarketId() {
        return MarketId;
    }

    public void setMarketId(int marketId) {
        MarketId = marketId;
    }

    public int getDesignationId() {
        return DesignationId;
    }

    public void setDesignationId(int designationId) {
        DesignationId = designationId;
    }

    public int getAreaId() {
        return AreaId;
    }

    public void setAreaId(int areaId) {
        AreaId = areaId;
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

    public int getDoctorCategoryId() {
        return DoctorCategoryId;
    }

    public void setDoctorCategoryId(int doctorCategoryId) {
        DoctorCategoryId = doctorCategoryId;
    }

    public int getStationTypeId() {
        return StationTypeId;
    }

    public void setStationTypeId(int stationTypeId) {
        StationTypeId = stationTypeId;
    }

    public int getProgramTypeId() {
        return ProgramTypeId;
    }

    public void setProgramTypeId(int programTypeId) {
        ProgramTypeId = programTypeId;
    }

    public int getSMCTypeId() {
        return SMCTypeId;
    }

    public void setSMCTypeId(int SMCTypeId) {
        this.SMCTypeId = SMCTypeId;
    }

    public String getUnionName() {
        return UnionName;
    }

    public void setUnionName(String unionName) {
        UnionName = unionName;
    }

    public List<ChembarList> getaChemberListDAO() {
        return aChemberListDAO;
    }

    public void setaChemberListDAO(List<ChembarList> aChemberListDAO) {
        this.aChemberListDAO = aChemberListDAO;
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

    public int getDoctorTypeId() {
        return DoctorTypeId;
    }

    public void setDoctorTypeId(int doctorTypeId) {
        DoctorTypeId = doctorTypeId;
    }

    public List<DoctorContact> getaDoctorContactDAO() {
        return aDoctorContactDAO;
    }

    public void setaDoctorContactDAO(List<DoctorContact> aDoctorContactDAO) {
        this.aDoctorContactDAO = aDoctorContactDAO;
    }

    public List<SpecialDay> getaDoctorSpecialDAO() {
        return aDoctorSpecialDAO;
    }

    public void setaDoctorSpecialDAO(List<SpecialDay> aDoctorSpecialDAO) {
        this.aDoctorSpecialDAO = aDoctorSpecialDAO;
    }
}
