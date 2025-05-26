package com.creatrix.salessolution.Activity.Attendance.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AttenApproval {
    @SerializedName("AttendanceId")
    @Expose
    private Integer attendanceId;
    @SerializedName("MIOId")
    @Expose
    private Integer mIOId;
    @SerializedName("EmpInfoId")
    @Expose
    private Integer empInfoId;
    @SerializedName("PunchInTime")
    @Expose
    private String punchInTime;
    @SerializedName("PInLat")
    @Expose
    private String pInLat;
    @SerializedName("PInLog")
    @Expose
    private String pInLog;
    @SerializedName("PunchOutTime")
    @Expose
    private String punchOutTime;
    @SerializedName("POutLat")
    @Expose
    private String pOutLat;
    @SerializedName("POutLong")
    @Expose
    private String pOutLong;
    @SerializedName("EntryDate")
    @Expose
    private String entryDate;
    @SerializedName("POutRemarks")
    @Expose
    private String pOutRemarks;
    @SerializedName("AttendanceDate")
    @Expose
    private String attendanceDate;
    @SerializedName("PINCreatedDateTime")
    @Expose
    private String pINCreatedDateTime;
    @SerializedName("POUTCreatedDateTime")
    @Expose
    private String pOUTCreatedDateTime;
    @SerializedName("Status")
    @Expose
    private String status;
    @SerializedName("ShiftId")
    @Expose
    private Integer shiftId;
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
    @SerializedName("Comments")
    @Expose
    private String comments;
    @SerializedName("Type")
    @Expose
    private String type;
    @SerializedName("Step")
    @Expose
    private String step;
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
    @SerializedName("AttType")
    @Expose
    private Integer attType;
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
    @SerializedName("ImageString")
    @Expose
    private String imageString;
    @SerializedName("ImagePreName")
    @Expose
    private String imagePreName;
    @SerializedName("ToRoleTypeId")
    @Expose
    private Integer toRoleTypeId;
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

    private String ApprovalStatus;
    private String AttAddress;

    public String getApprovalStatus() {
        return ApprovalStatus;
    }

    public void setApprovalStatus(String approvalStatus) {
        ApprovalStatus = approvalStatus;
    }

    public String getAttAddress() {
        return AttAddress;
    }

    public void setAttAddress(String attAddress) {
        AttAddress = attAddress;
    }

    public Integer getAttendanceId() {
        return attendanceId;
    }

    public void setAttendanceId(Integer attendanceId) {
        this.attendanceId = attendanceId;
    }

    public Integer getMIOId() {
        return mIOId;
    }

    public void setMIOId(Integer mIOId) {
        this.mIOId = mIOId;
    }

    public Integer getEmpInfoId() {
        return empInfoId;
    }

    public void setEmpInfoId(Integer empInfoId) {
        this.empInfoId = empInfoId;
    }

    public String getPunchInTime() {
        return punchInTime;
    }

    public void setPunchInTime(String punchInTime) {
        this.punchInTime = punchInTime;
    }

    public String getPInLat() {
        return pInLat;
    }

    public void setPInLat(String pInLat) {
        this.pInLat = pInLat;
    }

    public String getPInLog() {
        return pInLog;
    }

    public void setPInLog(String pInLog) {
        this.pInLog = pInLog;
    }

    public String getPunchOutTime() {
        return punchOutTime;
    }

    public void setPunchOutTime(String punchOutTime) {
        this.punchOutTime = punchOutTime;
    }

    public String getPOutLat() {
        return pOutLat;
    }

    public void setPOutLat(String pOutLat) {
        this.pOutLat = pOutLat;
    }

    public String getPOutLong() {
        return pOutLong;
    }

    public void setPOutLong(String pOutLong) {
        this.pOutLong = pOutLong;
    }

    public String getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(String entryDate) {
        this.entryDate = entryDate;
    }

    public String getPOutRemarks() {
        return pOutRemarks;
    }

    public void setPOutRemarks(String pOutRemarks) {
        this.pOutRemarks = pOutRemarks;
    }

    public String getAttendanceDate() {
        return attendanceDate;
    }

    public void setAttendanceDate(String attendanceDate) {
        this.attendanceDate = attendanceDate;
    }

    public String getPINCreatedDateTime() {
        return pINCreatedDateTime;
    }

    public void setPINCreatedDateTime(String pINCreatedDateTime) {
        this.pINCreatedDateTime = pINCreatedDateTime;
    }

    public String getPOUTCreatedDateTime() {
        return pOUTCreatedDateTime;
    }

    public void setPOUTCreatedDateTime(String pOUTCreatedDateTime) {
        this.pOUTCreatedDateTime = pOUTCreatedDateTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getShiftId() {
        return shiftId;
    }

    public void setShiftId(Integer shiftId) {
        this.shiftId = shiftId;
    }

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

    public String getStep() {
        return step;
    }

    public void setStep(String step) {
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

    public Integer getAttType() {
        return attType;
    }

    public void setAttType(Integer attType) {
        this.attType = attType;
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

    public Integer getToRoleTypeId() {
        return toRoleTypeId;
    }

    public void setToRoleTypeId(Integer toRoleTypeId) {
        this.toRoleTypeId = toRoleTypeId;
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

}

