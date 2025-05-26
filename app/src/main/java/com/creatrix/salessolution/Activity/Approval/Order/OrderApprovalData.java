package com.creatrix.salessolution.Activity.Approval.Order;

import androidx.annotation.Keep;

import com.creatrix.salessolution.Model.Customer;
@Keep public class OrderApprovalData {
    int OrderApprovalId;
    String EntryDate;
    int EmpInfoId;
    String ApprovalStatus;
    int MenuId;
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
    int MarketId;
    String MarketName;
    int ToGroupId;
    int ToRegionId;
    int ToAreaId;
    int ToTerritoryId;
    String EntryByS;
    String EntryDateS;
    String EntryTimeS;
    String ApproveByS;
    String ApproveDateS;
    String ApproveTimeS;
    String EntryByApp;
    String EntryDateApp;
    String EntryTimeApp;
    String ApproveByApp;
    String ApproveDateApp;
    String ApproveTimeApp;
    int RoleTypeId;
    int ToRoleTypeId;
    String EmpMasterCode;
    String EmpName;
    String TerritoryName;
    String TerritoryCode;
    String AreaCode;
    String AreaName;
    String RegionCode;
    String RegionName;
    int MIOEmpId;
    int ASMEMPId;
    int RSMEMPId;
    int NSMEMPId;
    OrderMasterDAO aOrderMasterDAO;
    Customer aCustMasterDAO;

    public int getOrderApprovalId() {
        return OrderApprovalId;
    }

    public void setOrderApprovalId(int orderApprovalId) {
        OrderApprovalId = orderApprovalId;
    }

    public String getEntryDate() {
        return EntryDate;
    }

    public void setEntryDate(String entryDate) {
        EntryDate = entryDate;
    }

    public int getEmpInfoId() {
        return EmpInfoId;
    }

    public void setEmpInfoId(int empInfoId) {
        EmpInfoId = empInfoId;
    }

    public String getApprovalStatus() {
        return ApprovalStatus;
    }

    public void setApprovalStatus(String approvalStatus) {
        ApprovalStatus = approvalStatus;
    }

    public int getMenuId() {
        return MenuId;
    }

    public void setMenuId(int menuId) {
        MenuId = menuId;
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

    public int getToGroupId() {
        return ToGroupId;
    }

    public void setToGroupId(int toGroupId) {
        ToGroupId = toGroupId;
    }

    public int getToRegionId() {
        return ToRegionId;
    }

    public void setToRegionId(int toRegionId) {
        ToRegionId = toRegionId;
    }

    public int getToAreaId() {
        return ToAreaId;
    }

    public void setToAreaId(int toAreaId) {
        ToAreaId = toAreaId;
    }

    public int getToTerritoryId() {
        return ToTerritoryId;
    }

    public void setToTerritoryId(int toTerritoryId) {
        ToTerritoryId = toTerritoryId;
    }

    public String getEntryByS() {
        return EntryByS;
    }

    public void setEntryByS(String entryByS) {
        EntryByS = entryByS;
    }

    public String getEntryDateS() {
        return EntryDateS;
    }

    public void setEntryDateS(String entryDateS) {
        EntryDateS = entryDateS;
    }

    public String getEntryTimeS() {
        return EntryTimeS;
    }

    public void setEntryTimeS(String entryTimeS) {
        EntryTimeS = entryTimeS;
    }

    public String getApproveByS() {
        return ApproveByS;
    }

    public void setApproveByS(String approveByS) {
        ApproveByS = approveByS;
    }

    public String getApproveDateS() {
        return ApproveDateS;
    }

    public void setApproveDateS(String approveDateS) {
        ApproveDateS = approveDateS;
    }

    public String getApproveTimeS() {
        return ApproveTimeS;
    }

    public void setApproveTimeS(String approveTimeS) {
        ApproveTimeS = approveTimeS;
    }

    public String getEntryByApp() {
        return EntryByApp;
    }

    public void setEntryByApp(String entryByApp) {
        EntryByApp = entryByApp;
    }

    public String getEntryDateApp() {
        return EntryDateApp;
    }

    public void setEntryDateApp(String entryDateApp) {
        EntryDateApp = entryDateApp;
    }

    public String getEntryTimeApp() {
        return EntryTimeApp;
    }

    public void setEntryTimeApp(String entryTimeApp) {
        EntryTimeApp = entryTimeApp;
    }

    public String getApproveByApp() {
        return ApproveByApp;
    }

    public void setApproveByApp(String approveByApp) {
        ApproveByApp = approveByApp;
    }

    public String getApproveDateApp() {
        return ApproveDateApp;
    }

    public void setApproveDateApp(String approveDateApp) {
        ApproveDateApp = approveDateApp;
    }

    public String getApproveTimeApp() {
        return ApproveTimeApp;
    }

    public void setApproveTimeApp(String approveTimeApp) {
        ApproveTimeApp = approveTimeApp;
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

    public String getAreaName() {
        return AreaName;
    }

    public void setAreaName(String areaName) {
        AreaName = areaName;
    }

    public String getRegionCode() {
        return RegionCode;
    }

    public void setRegionCode(String regionCode) {
        RegionCode = regionCode;
    }

    public String getRegionName() {
        return RegionName;
    }

    public void setRegionName(String regionName) {
        RegionName = regionName;
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

    public OrderMasterDAO getaOrderMasterDAO() {
        return aOrderMasterDAO;
    }

    public void setaOrderMasterDAO(OrderMasterDAO aOrderMasterDAO) {
        this.aOrderMasterDAO = aOrderMasterDAO;
    }

    public int getMarketId() {
        return MarketId;
    }

    public void setMarketId(int marketId) {
        MarketId = marketId;
    }

    public String getMarketName() {
        return MarketName;
    }

    public void setMarketName(String marketName) {
        MarketName = marketName;
    }

    public Customer getaCustMasterDAO() {
        return aCustMasterDAO;
    }

    public void setaCustMasterDAO(Customer aCustMasterDAO) {
        this.aCustMasterDAO = aCustMasterDAO;
    }
}
