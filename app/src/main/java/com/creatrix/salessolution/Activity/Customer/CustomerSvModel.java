package com.creatrix.salessolution.Activity.Customer;

import androidx.annotation.Keep;


@Keep public class CustomerSvModel {
    int CustomerMasterId;
    String CustomerName;

    public String getCustomerBSPCode() {
        return CustomerBSPCode;
    }

    public void setCustomerBSPCode(String customerBSPCode) {
        CustomerBSPCode = customerBSPCode;
    }

    String CustomerBSPCode;
    String ImageBase64String;
    String TradeLicenseImg;
    String Address;
    String CellNo;
    String ConPerson; //Owner name
    String TermOfPayment;
    boolean IsFromApp;
    int CustomerTypeId;
    int ProgramTypeId;
    int SMCTypeId;
    int EmpId;
    String VoterID;
    String TradeLicense;
    String Latitude;
    String Longitude;
    String StreetAddress;
    String Reamrks;
    String ZoneName;
    String AreaName;
    String Subterritory;
    String territory;
    String MarketName;
    String MarketCode;
    String ProgramTypeName; //Provider type
    String SMCTypeName;
    int GroupId;
    int RegionId;
    int AreaId;
    int TerritoryId;
    int SubTerritoryId;
    int MarketId;



    public int getCustomerMasterId() {
        return CustomerMasterId;
    }

    public void setCustomerMasterId(int customerMasterId) {
        CustomerMasterId = customerMasterId;
    }

    public String getCustomerName() {
        return CustomerName;
    }

    public void setCustomerName(String customerName) {
        CustomerName = customerName;
    }

    public String getImageBase64String() {
        return ImageBase64String;
    }

    public void setImageBase64String(String imageBase64String) {
        ImageBase64String = imageBase64String;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getCellNo() {
        return CellNo;
    }

    public void setCellNo(String cellNo) {
        CellNo = cellNo;
    }

    public String getConPerson() {
        return ConPerson;
    }

    public void setConPerson(String conPerson) {
        ConPerson = conPerson;
    }

    public String getTermOfPayment() {
        return TermOfPayment;
    }

    public void setTermOfPayment(String termOfPayment) {
        TermOfPayment = termOfPayment;
    }

    public boolean isFromApp() {
        return IsFromApp;
    }

    public void setFromApp(boolean fromApp) {
        IsFromApp = fromApp;
    }

    public int getMarketId() {
        return MarketId;
    }

    public void setMarketId(int marketId) {
        MarketId = marketId;
    }

    public int getCustomerTypeId() {
        return CustomerTypeId;
    }

    public void setCustomerTypeId(int customerTypeId) {
        CustomerTypeId = customerTypeId;
    }


    public int getEmpId() {
        return EmpId;
    }

    public void setEmpId(int empId) {
        EmpId = empId;
    }

    public String getVoterID() {
        return VoterID;
    }

    public void setVoterID(String voterID) {
        VoterID = voterID;
    }

    public String getTradeLicense() {
        return TradeLicense;
    }

    public void setTradeLicense(String tradeLicense) {
        TradeLicense = tradeLicense;
    }

    public String getReamrks() {
        return Reamrks;
    }

    public void setReamrks(String reamrks) {
        Reamrks = reamrks;
    }

    public String getTradeLicenseImg() {
        return TradeLicenseImg;
    }

    public void setTradeLicenseImg(String tradeLicenseImg) {
        TradeLicenseImg = tradeLicenseImg;
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

    public String getMarketName() {
        return MarketName;
    }

    public void setMarketName(String marketName) {
        MarketName = marketName;
    }

    public String getMarketCode() {
        return MarketCode;
    }

    public void setMarketCode(String marketCode) {
        MarketCode = marketCode;
    }

    public String getProgramTypeName() {
        return ProgramTypeName;
    }

    public void setProgramTypeName(String programTypeName) {
        ProgramTypeName = programTypeName;
    }

    public int getGroupId() {
        return GroupId;
    }

    public void setGroupId(int groupId) {
        GroupId = groupId;
    }

    public int getRegionId() {
        return RegionId;
    }

    public void setRegionId(int regionId) {
        RegionId = regionId;
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

    public String getSMCTypeName() {
        return SMCTypeName;
    }

    public void setSMCTypeName(String SMCTypeName) {
        this.SMCTypeName = SMCTypeName;
    }

    public String getZoneName() {
        return ZoneName;
    }

    public void setZoneName(String zoneName) {
        ZoneName = zoneName;
    }

    public String getAreaName() {
        return AreaName;
    }

    public void setAreaName(String areaName) {
        AreaName = areaName;
    }

    public String getSubterritory() {
        return Subterritory;
    }

    public void setSubterritory(String subterritory) {
        Subterritory = subterritory;
    }

    public String getTerritory() {
        return territory;
    }

    public void setTerritory(String territory) {
        this.territory = territory;
    }
}
