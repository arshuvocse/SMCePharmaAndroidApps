package com.creatrix.salessolution.Activity.Approval.Prescription;

import androidx.annotation.Keep;

import com.creatrix.salessolution.Activity.Approval.DCR.DcrMaster;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
@Keep public class PrescApprovalData {
    @SerializedName("PrescriptionApprovalId")
    @Expose
    private Integer prescriptionApprovalId;
    @SerializedName("PrescriptionId")
    @Expose
    private Integer prescriptionId;
    @SerializedName("EmpInfoId")
    @Expose
    private Integer empInfoId;
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
    @SerializedName("RoleTypeId")
    @Expose
    private Integer roleTypeId;
    @SerializedName("ToRoleTypeId")
    @Expose
    private Integer toRoleTypeId;
    @SerializedName("MenuId")
    @Expose
    private Integer menuId;
    @SerializedName("TerritoryName")
    @Expose
    private String territoryName;
    @SerializedName("TerritoryCode")
    @Expose
    private String territoryCode;
    @SerializedName("AreaCode")
    @Expose
    private String areaCode;
    @SerializedName("ApprovalStatus")
    @Expose
    private String approvalStatus;
    @SerializedName("EmpMasterCode")
    @Expose
    private String empMasterCode;
    @SerializedName("EmpName")
    @Expose
    private String empName;
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
    @SerializedName("aPrescriptionRMasterDAO")
    @Expose
    private PrescriptionMaster aPrescriptionRMasterDAO;

    public Integer getPrescriptionApprovalId() {
        return prescriptionApprovalId;
    }

    public void setPrescriptionApprovalId(Integer prescriptionApprovalId) {
        this.prescriptionApprovalId = prescriptionApprovalId;
    }

    public Integer getPrescriptionId() {
        return prescriptionId;
    }

    public void setPrescriptionId(Integer prescriptionId) {
        this.prescriptionId = prescriptionId;
    }

    public Integer getEmpInfoId() {
        return empInfoId;
    }

    public void setEmpInfoId(Integer empInfoId) {
        this.empInfoId = empInfoId;
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

    public Integer getMenuId() {
        return menuId;
    }

    public void setMenuId(Integer menuId) {
        this.menuId = menuId;
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

    public String getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(String approvalStatus) {
        this.approvalStatus = approvalStatus;
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

    public PrescriptionMaster getaPrescriptionRMasterDAO() {
        return aPrescriptionRMasterDAO;
    }

    public void setaPrescriptionRMasterDAO(PrescriptionMaster aPrescriptionRMasterDAO) {
        this.aPrescriptionRMasterDAO = aPrescriptionRMasterDAO;
    }

}
