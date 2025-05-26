package com.creatrix.salessolution.Activity.Customer.Approval.Model;

import androidx.annotation.Keep;

import com.creatrix.salessolution.Activity.Customer.CustomerSvModel;
@Keep public class CustomerApprovalList {
    int CustomerApprovalId;
    int CustomerMasterId;
    int EmpInfoId;
    String Date;
    int FromEmpId;
    int ToEmpId;
    int TableId;
    String Status;
    String Comments;
    String Type;
    int Step;
    int GroupId;
    int RegionId;
    int AreaId;
    int TerritoryId;
    int RoleTypeId;
    int ToRoleTypeId;
    int MenuId;
    String TerritoryName;
    String TerritoryCode;
    String AreaCode;
    String ApprovalStatus;
    String EmpMasterCode;
    String EmpName;
    int MIOEmpId;
    int ASMEMPId;
    int RSMEMPId;
    int NSMEMPId;
    CustomerSvModel CustomerSMListDao;

    public int getCustomerApprovalId() {
        return CustomerApprovalId;
    }

    public void setCustomerApprovalId(int customerApprovalId) {
        CustomerApprovalId = customerApprovalId;
    }

    public int getCustomerMasterId() {
        return CustomerMasterId;
    }

    public void setCustomerMasterId(int customerMasterId) {
        CustomerMasterId = customerMasterId;
    }

    public int getEmpInfoId() {
        return EmpInfoId;
    }

    public void setEmpInfoId(int empInfoId) {
        EmpInfoId = empInfoId;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public int getFromEmpId() {
        return FromEmpId;
    }

    public void setFromEmpId(int fromEmpId) {
        FromEmpId = fromEmpId;
    }

    public int getToEmpId() {
        return ToEmpId;
    }

    public void setToEmpId(int toEmpId) {
        ToEmpId = toEmpId;
    }

    public int getTableId() {
        return TableId;
    }

    public void setTableId(int tableId) {
        TableId = tableId;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getComments() {
        return Comments;
    }

    public void setComments(String comments) {
        Comments = comments;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public int getStep() {
        return Step;
    }

    public void setStep(int step) {
        Step = step;
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

    public int getRoleTypeId() {
        return RoleTypeId;
    }

    public void setRoleTypeId(int roleTypeId) {
        RoleTypeId = roleTypeId;
    }

    public int getToRoleTypeId() {
        return ToRoleTypeId;
    }

    public void setToRoleTypeId(int toRoleTypeId) {
        ToRoleTypeId = toRoleTypeId;
    }

    public int getMenuId() {
        return MenuId;
    }

    public void setMenuId(int menuId) {
        MenuId = menuId;
    }

    public String getTerritoryName() {
        return TerritoryName;
    }

    public void setTerritoryName(String territoryName) {
        TerritoryName = territoryName;
    }

    public String getTerritoryCode() {
        return TerritoryCode;
    }

    public void setTerritoryCode(String territoryCode) {
        TerritoryCode = territoryCode;
    }

    public String getAreaCode() {
        return AreaCode;
    }

    public void setAreaCode(String areaCode) {
        AreaCode = areaCode;
    }

    public String getApprovalStatus() {
        return ApprovalStatus;
    }

    public void setApprovalStatus(String approvalStatus) {
        ApprovalStatus = approvalStatus;
    }

    public String getEmpMasterCode() {
        return EmpMasterCode;
    }

    public void setEmpMasterCode(String empMasterCode) {
        EmpMasterCode = empMasterCode;
    }

    public String getEmpName() {
        return EmpName;
    }

    public void setEmpName(String empName) {
        EmpName = empName;
    }

    public int getMIOEmpId() {
        return MIOEmpId;
    }

    public void setMIOEmpId(int MIOEmpId) {
        this.MIOEmpId = MIOEmpId;
    }

    public int getASMEMPId() {
        return ASMEMPId;
    }

    public void setASMEMPId(int ASMEMPId) {
        this.ASMEMPId = ASMEMPId;
    }

    public int getRSMEMPId() {
        return RSMEMPId;
    }

    public void setRSMEMPId(int RSMEMPId) {
        this.RSMEMPId = RSMEMPId;
    }

    public int getNSMEMPId() {
        return NSMEMPId;
    }

    public void setNSMEMPId(int NSMEMPId) {
        this.NSMEMPId = NSMEMPId;
    }

    public CustomerSvModel getCustomerSMListDao() {
        return CustomerSMListDao;
    }

    public void setCustomerSMListDao(CustomerSvModel customerSMListDao) {
        CustomerSMListDao = customerSMListDao;
    }
    /* {
        int CustomerMasterId;
        String CustomerName;
        String CustomerCode;
        String Address;
        String MarketName;
        int MarketId;
        String AreaName;
        String CellNo;
        int CustomerTypeId;
        boolean IsVatApplicable;
        String VoterID;
        String CustomerType;
        String Reamrks;
        String Latitude;
        String Longitude;
        String StreetAddress;
        int ProgramTypeId;
        String ProgramTypeName;
        String TermOfPayment;
        String OwnerName;
        String CustomerImagePreName;
        String CustomerImageString;
    }*/
}
