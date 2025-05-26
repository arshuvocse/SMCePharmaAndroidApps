package com.creatrix.salessolution.Activity.Expense.Model;

import com.creatrix.salessolution.Model.Expense.ADetailListDAO;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ExpListTeam {
    @SerializedName("ExpenseClaimID")
    @Expose
    private Integer expenseClaimID;
    @SerializedName("ExpenseTypeId")
    @Expose
    private Integer expenseTypeId;
    @SerializedName("ExpenseDate")
    @Expose
    private String expenseDate;
    @SerializedName("EmpInfoId")
    @Expose
    private Integer empInfoId;
    @SerializedName("Amount")
    @Expose
    private Double amount;
    @SerializedName("Remarks")
    @Expose
    private String remarks;
    @SerializedName("ImageName")
    @Expose
    private String imageName;
    @SerializedName("ImagePath")
    @Expose
    private String imagePath;
    @SerializedName("EntryBy")
    @Expose
    private Integer entryBy;
    @SerializedName("EntryDate")
    @Expose
    private String entryDate;
    @SerializedName("ApprovalStatus")
    @Expose
    private String approvalStatus;

    @SerializedName("ExpenseTypeName")
    @Expose
    private String ExpenseTypeName;

    @SerializedName("IsFromApp")
    @Expose
    private Integer isFromApp;
    @SerializedName("ApprovedBy")
    @Expose
    private Integer approvedBy;
    @SerializedName("ApprovedDate")
    @Expose
    private String approvedDate;
    @SerializedName("UpdateBy")
    @Expose
    private Integer updateBy;
    @SerializedName("UpdateDate")
    @Expose
    private String updateDate;
    @SerializedName("ExpanseApprovalId")
    @Expose
    private Integer expanseApprovalId;
    @SerializedName("MenuId")
    @Expose
    private Integer menuId;
    @SerializedName("Date")
    @Expose
    private String date;
    @SerializedName("FromEmpId")
    @Expose
    private Integer fromEmpId;
    @SerializedName("ToEmpId")
    @Expose
    private Integer toEmpId;
    @SerializedName("TableId")
    @Expose
    private Integer tableId;
    @SerializedName("Status")
    @Expose
    private String status;
    @SerializedName("Comments")
    @Expose
    private String comments;
    @SerializedName("Type")
    @Expose
    private String type;
    @SerializedName("Step")
    @Expose
    private Integer step;
    @SerializedName("GroupId")
    @Expose
    private Integer groupId;
    @SerializedName("RegionId")
    @Expose
    private Integer regionId;
    @SerializedName("AreaId")
    @Expose
    private Integer areaId;
    @SerializedName("TerritoryId")
    @Expose
    private Integer territoryId;
    @SerializedName("ToGroupId")
    @Expose
    private Integer toGroupId;
    @SerializedName("ToRegionId")
    @Expose
    private Integer toRegionId;
    @SerializedName("ToAreaId")
    @Expose
    private Integer toAreaId;
    @SerializedName("ToTerritoryId")
    @Expose
    private Integer toTerritoryId;
    @SerializedName("EntryByS")
    @Expose
    private Integer entryByS;
    @SerializedName("EntryDateS")
    @Expose
    private String entryDateS;
    @SerializedName("EntryTimeS")
    @Expose
    private String entryTimeS;
    @SerializedName("ApproveByS")
    @Expose
    private Integer approveByS;
    @SerializedName("ApproveDateS")
    @Expose
    private String approveDateS;
    @SerializedName("ApproveTimeS")
    @Expose
    private String approveTimeS;
    @SerializedName("EntryByApp")
    @Expose
    private Integer entryByApp;
    @SerializedName("EntryDateApp")
    @Expose
    private String entryDateApp;
    @SerializedName("EntryTimeApp")
    @Expose
    private String entryTimeApp;
    @SerializedName("ApproveByApp")
    @Expose
    private Integer approveByApp;
    @SerializedName("ApproveDateApp")
    @Expose
    private String approveDateApp;
    @SerializedName("ApproveTimeApp")
    @Expose
    private String approveTimeApp;
    @SerializedName("RoleTypeId")
    @Expose
    private Integer roleTypeId;
    @SerializedName("ToRoleTypeId")
    @Expose
    private Integer toRoleTypeId;
    @SerializedName("EmpMasterCode")
    @Expose
    private String empMasterCode;
    @SerializedName("EmpName")
    @Expose
    private String empName;
    @SerializedName("TerritoryName")
    @Expose
    private String territoryName;
    @SerializedName("TerritoryCode")
    @Expose
    private String territoryCode;
    @SerializedName("AreaCode")
    @Expose
    private String areaCode;
    @SerializedName("AreaName")
    @Expose
    private String areaName;
    @SerializedName("RegionCode")
    @Expose
    private String regionCode;
    @SerializedName("RegionName")
    @Expose
    private String regionName;
    @SerializedName("ImageString")
    @Expose
    private String imageString;
    @SerializedName("ImagePreName")
    @Expose
    private String imagePreName;
    @SerializedName("MIOEmpId")
    @Expose
    private Integer mIOEmpId;
    @SerializedName("ASMEMPId")
    @Expose
    private Integer aSMEMPId;
    @SerializedName("RSMEMPId")
    @Expose
    private Integer rSMEMPId;
    @SerializedName("NSMEMPId")
    @Expose
    private Integer nSMEMPId;

    List<ADetailListDAO>aDetailListDAO;

    public Integer getExpenseClaimID() {
        return expenseClaimID;
    }

    public void setExpenseClaimID(Integer expenseClaimID) {
        this.expenseClaimID = expenseClaimID;
    }

    public Integer getExpenseTypeId() {
        return expenseTypeId;
    }

    public void setExpenseTypeId(Integer expenseTypeId) {
        this.expenseTypeId = expenseTypeId;
    }

    public String getExpenseDate() {
        return expenseDate;
    }

    public void setExpenseDate(String expenseDate) {
        this.expenseDate = expenseDate;
    }

    public Integer getEmpInfoId() {
        return empInfoId;
    }

    public void setEmpInfoId(Integer empInfoId) {
        this.empInfoId = empInfoId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public Integer getEntryBy() {
        return entryBy;
    }

    public void setEntryBy(Integer entryBy) {
        this.entryBy = entryBy;
    }

    public String getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(String entryDate) {
        this.entryDate = entryDate;
    }

    public String getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(String approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    public String getExpenseTypeName() {
        return ExpenseTypeName;
    }

    public void setExpenseTypeName(String expenseTypeName) {
        ExpenseTypeName = expenseTypeName;
    }

    public Integer getIsFromApp() {
        return isFromApp;
    }

    public void setIsFromApp(Integer isFromApp) {
        this.isFromApp = isFromApp;
    }

    public Integer getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(Integer approvedBy) {
        this.approvedBy = approvedBy;
    }

    public String getApprovedDate() {
        return approvedDate;
    }

    public void setApprovedDate(String approvedDate) {
        this.approvedDate = approvedDate;
    }

    public Integer getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(Integer updateBy) {
        this.updateBy = updateBy;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public Integer getExpanseApprovalId() {
        return expanseApprovalId;
    }

    public void setExpanseApprovalId(Integer expanseApprovalId) {
        this.expanseApprovalId = expanseApprovalId;
    }

    public Integer getMenuId() {
        return menuId;
    }

    public void setMenuId(Integer menuId) {
        this.menuId = menuId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getFromEmpId() {
        return fromEmpId;
    }

    public void setFromEmpId(Integer fromEmpId) {
        this.fromEmpId = fromEmpId;
    }

    public Integer getToEmpId() {
        return toEmpId;
    }

    public void setToEmpId(Integer toEmpId) {
        this.toEmpId = toEmpId;
    }

    public Integer getTableId() {
        return tableId;
    }

    public void setTableId(Integer tableId) {
        this.tableId = tableId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getStep() {
        return step;
    }

    public void setStep(Integer step) {
        this.step = step;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public Integer getRegionId() {
        return regionId;
    }

    public void setRegionId(Integer regionId) {
        this.regionId = regionId;
    }

    public Integer getAreaId() {
        return areaId;
    }

    public void setAreaId(Integer areaId) {
        this.areaId = areaId;
    }

    public Integer getTerritoryId() {
        return territoryId;
    }

    public void setTerritoryId(Integer territoryId) {
        this.territoryId = territoryId;
    }

    public Integer getToGroupId() {
        return toGroupId;
    }

    public void setToGroupId(Integer toGroupId) {
        this.toGroupId = toGroupId;
    }

    public Integer getToRegionId() {
        return toRegionId;
    }

    public void setToRegionId(Integer toRegionId) {
        this.toRegionId = toRegionId;
    }

    public Integer getToAreaId() {
        return toAreaId;
    }

    public void setToAreaId(Integer toAreaId) {
        this.toAreaId = toAreaId;
    }

    public Integer getToTerritoryId() {
        return toTerritoryId;
    }

    public void setToTerritoryId(Integer toTerritoryId) {
        this.toTerritoryId = toTerritoryId;
    }

    public Integer getEntryByS() {
        return entryByS;
    }

    public void setEntryByS(Integer entryByS) {
        this.entryByS = entryByS;
    }

    public String getEntryDateS() {
        return entryDateS;
    }

    public void setEntryDateS(String entryDateS) {
        this.entryDateS = entryDateS;
    }

    public String getEntryTimeS() {
        return entryTimeS;
    }

    public void setEntryTimeS(String entryTimeS) {
        this.entryTimeS = entryTimeS;
    }

    public Integer getApproveByS() {
        return approveByS;
    }

    public void setApproveByS(Integer approveByS) {
        this.approveByS = approveByS;
    }

    public String getApproveDateS() {
        return approveDateS;
    }

    public void setApproveDateS(String approveDateS) {
        this.approveDateS = approveDateS;
    }

    public String getApproveTimeS() {
        return approveTimeS;
    }

    public void setApproveTimeS(String approveTimeS) {
        this.approveTimeS = approveTimeS;
    }

    public Integer getEntryByApp() {
        return entryByApp;
    }

    public void setEntryByApp(Integer entryByApp) {
        this.entryByApp = entryByApp;
    }

    public String getEntryDateApp() {
        return entryDateApp;
    }

    public void setEntryDateApp(String entryDateApp) {
        this.entryDateApp = entryDateApp;
    }

    public String getEntryTimeApp() {
        return entryTimeApp;
    }

    public void setEntryTimeApp(String entryTimeApp) {
        this.entryTimeApp = entryTimeApp;
    }

    public Integer getApproveByApp() {
        return approveByApp;
    }

    public void setApproveByApp(Integer approveByApp) {
        this.approveByApp = approveByApp;
    }

    public String getApproveDateApp() {
        return approveDateApp;
    }

    public void setApproveDateApp(String approveDateApp) {
        this.approveDateApp = approveDateApp;
    }

    public String getApproveTimeApp() {
        return approveTimeApp;
    }

    public void setApproveTimeApp(String approveTimeApp) {
        this.approveTimeApp = approveTimeApp;
    }

    public Integer getRoleTypeId() {
        return roleTypeId;
    }

    public void setRoleTypeId(Integer roleTypeId) {
        this.roleTypeId = roleTypeId;
    }

    public Integer getToRoleTypeId() {
        return toRoleTypeId;
    }

    public void setToRoleTypeId(Integer toRoleTypeId) {
        this.toRoleTypeId = toRoleTypeId;
    }

    public String getEmpMasterCode() {
        return empMasterCode;
    }

    public void setEmpMasterCode(String empMasterCode) {
        this.empMasterCode = empMasterCode;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public String getTerritoryName() {
        return territoryName;
    }

    public void setTerritoryName(String territoryName) {
        this.territoryName = territoryName;
    }

    public String getTerritoryCode() {
        return territoryCode;
    }

    public void setTerritoryCode(String territoryCode) {
        this.territoryCode = territoryCode;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getRegionCode() {
        return regionCode;
    }

    public void setRegionCode(String regionCode) {
        this.regionCode = regionCode;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public String getImageString() {
        return imageString;
    }

    public void setImageString(String imageString) {
        this.imageString = imageString;
    }

    public String getImagePreName() {
        return imagePreName;
    }

    public void setImagePreName(String imagePreName) {
        this.imagePreName = imagePreName;
    }

    public Integer getMIOEmpId() {
        return mIOEmpId;
    }

    public void setMIOEmpId(Integer mIOEmpId) {
        this.mIOEmpId = mIOEmpId;
    }

    public Integer getASMEMPId() {
        return aSMEMPId;
    }

    public void setASMEMPId(Integer aSMEMPId) {
        this.aSMEMPId = aSMEMPId;
    }

    public Integer getRSMEMPId() {
        return rSMEMPId;
    }

    public void setRSMEMPId(Integer rSMEMPId) {
        this.rSMEMPId = rSMEMPId;
    }

    public Integer getNSMEMPId() {
        return nSMEMPId;
    }

    public void setNSMEMPId(Integer nSMEMPId) {
        this.nSMEMPId = nSMEMPId;
    }

    public List<ADetailListDAO> getaDetailListDAO() {
        return aDetailListDAO;
    }

    public void setaDetailListDAO(List<ADetailListDAO> aDetailListDAO) {
        this.aDetailListDAO = aDetailListDAO;
    }
}
