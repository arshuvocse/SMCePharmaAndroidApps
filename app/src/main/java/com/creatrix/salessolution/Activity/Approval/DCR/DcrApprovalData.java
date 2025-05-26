package com.creatrix.salessolution.Activity.Approval.DCR;

import androidx.annotation.Keep;

import com.creatrix.salessolution.Model.Doctor.DoctorBrand;
import com.creatrix.salessolution.Model.UserByRole;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;
@Keep
public class DcrApprovalData {
    @SerializedName("DCRApprovalId")
    @Expose
    private Integer dCRApprovalId;
    @SerializedName("DcrId")
    @Expose
    private Integer dcrId;
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
    private Object menuId;
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
    @SerializedName("aDCRMasterDAO")
    @Expose
    private DcrMaster aDCRMasterDAO;

    public Integer getDCRApprovalId() {
        return dCRApprovalId;
    }

    public void setDCRApprovalId(Integer dCRApprovalId) {
        this.dCRApprovalId = dCRApprovalId;
    }

    public Integer getDcrId() {
        return dcrId;
    }

    public void setDcrId(Integer dcrId) {
        this.dcrId = dcrId;
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

    public Object getMenuId() {
        return menuId;
    }

    public void setMenuId(Object menuId) {
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

    public DcrMaster getaDCRMasterDAO() {
        return aDCRMasterDAO;
    }

    public void setaDCRMasterDAO(DcrMaster aDCRMasterDAO) {
        this.aDCRMasterDAO = aDCRMasterDAO;
    }

    @Override
    public String toString() {
        return "DcrApprovalData{" +
                "dCRApprovalId=" + dCRApprovalId +
                ", dcrId=" + dcrId +
                ", empInfoId=" + empInfoId +
                ", date='" + date + '\'' +
                ", fromEmpId=" + fromEmpId +
                ", toEmpId=" + toEmpId +
                ", tableId=" + tableId +
                ", status='" + status + '\'' +
                ", comments='" + comments + '\'' +
                ", type='" + type + '\'' +
                ", step=" + step +
                ", groupId=" + groupId +
                ", regionId=" + regionId +
                ", areaId=" + areaId +
                ", territoryId=" + territoryId +
                ", roleTypeId=" + roleTypeId +
                ", toRoleTypeId=" + toRoleTypeId +
                ", menuId=" + menuId +
                ", territoryName='" + territoryName + '\'' +
                ", territoryCode='" + territoryCode + '\'' +
                ", areaCode='" + areaCode + '\'' +
                ", approvalStatus='" + approvalStatus + '\'' +
                ", empMasterCode='" + empMasterCode + '\'' +
                ", empName='" + empName + '\'' +
                ", mIOEmpId=" + mIOEmpId +
                ", aSMEMPId=" + aSMEMPId +
                ", rSMEMPId=" + rSMEMPId +
                ", nSMEMPId=" + nSMEMPId +
                '}';
    }
/* int DCRApprovalId;
    int DcrId;
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
    List<DcrMaster> aDCRMasterDAO;


    public int getDCRApprovalId() {
        return DCRApprovalId;
    }

    public void setDCRApprovalId(int DCRApprovalId) {
        this.DCRApprovalId = DCRApprovalId;
    }

    public int getDcrId() {
        return DcrId;
    }

    public void setDcrId(int dcrId) {
        DcrId = dcrId;
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

    public List<DcrMaster> getaDCRMasterDAO() {
        return aDCRMasterDAO;
    }

    public void setaDCRMasterDAO(List<DcrMaster> aDCRMasterDAO) {
        this.aDCRMasterDAO = aDCRMasterDAO;
    }
*/
}
