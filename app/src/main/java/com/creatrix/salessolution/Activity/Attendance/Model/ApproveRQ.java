package com.creatrix.salessolution.Activity.Attendance.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ApproveRQ {
    @SerializedName("ApprovalId")
    @Expose
    private Integer approvalId;
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
    private String entryByS;
    @SerializedName("EntryDateS")
    @Expose
    private String entryDateS;
    @SerializedName("EntryTimeS")
    @Expose
    private String entryTimeS;
    @SerializedName("ApproveByS")
    @Expose
    private String approveByS;
    @SerializedName("ApproveDateS")
    @Expose
    private String approveDateS;
    @SerializedName("ApproveTimeS")
    @Expose
    private String approveTimeS;
    @SerializedName("EntryByApp")
    @Expose
    private String entryByApp;
    @SerializedName("EntryDateApp")
    @Expose
    private String entryDateApp;
    @SerializedName("EntryTimeApp")
    @Expose
    private String entryTimeApp;
    @SerializedName("ApproveByApp")
    @Expose
    private String approveByApp;
    @SerializedName("ApproveDateApp")
    @Expose
    private String approveDateApp;
    @SerializedName("ApproveTimeApp")
    @Expose
    private String approveTimeApp;
    @SerializedName("MenuId")
    @Expose
    private Integer menuId;

    public Integer getApprovalId() {
        return approvalId;
    }

    public void setApprovalId(Integer approvalId) {
        this.approvalId = approvalId;
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

    public String getEntryByS() {
        return entryByS;
    }

    public void setEntryByS(String entryByS) {
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

    public String getApproveByS() {
        return approveByS;
    }

    public void setApproveByS(String approveByS) {
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

    public String getEntryByApp() {
        return entryByApp;
    }

    public void setEntryByApp(String entryByApp) {
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

    public String getApproveByApp() {
        return approveByApp;
    }

    public void setApproveByApp(String approveByApp) {
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

    public Integer getMenuId() {
        return menuId;
    }

    public void setMenuId(Integer menuId) {
        this.menuId = menuId;
    }

}

