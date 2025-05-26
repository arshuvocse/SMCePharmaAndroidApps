package com.creatrix.salessolution.Model;

public class Attendance {

    private Integer AttendanceId;
    private Integer EmpInfoId;
    private String PunchInTime;
    private String PInLat;
    private String PInLog;
    private String EntryDate;
    private String POutRemarks;
    private String AttendanceDate;
    private Integer AttType;
    private String AttImg;
    private String AttAddress;

    public Attendance() {
    }

    public Integer getAttendanceId() {
        return AttendanceId;
    }

    public void setAttendanceId(Integer attendanceId) {
        AttendanceId = attendanceId;
    }

    public Integer getEmpInfoId() {
        return EmpInfoId;
    }

    public void setEmpInfoId(Integer empInfoId) {
        EmpInfoId = empInfoId;
    }

    public String getPunchInTime() {
        return PunchInTime;
    }

    public void setPunchInTime(String punchInTime) {
        PunchInTime = punchInTime;
    }

    public String getPInLat() {
        return PInLat;
    }

    public void setPInLat(String PInLat) {
        this.PInLat = PInLat;
    }

    public String getPInLog() {
        return PInLog;
    }

    public void setPInLog(String PInLog) {
        this.PInLog = PInLog;
    }

    public String getEntryDate() {
        return EntryDate;
    }

    public void setEntryDate(String entryDate) {
        EntryDate = entryDate;
    }

    public String getPOutRemarks() {
        return POutRemarks;
    }

    public void setPOutRemarks(String POutRemarks) {
        this.POutRemarks = POutRemarks;
    }

    public String getAttendanceDate() {
        return AttendanceDate;
    }

    public void setAttendanceDate(String attendanceDate) {
        AttendanceDate = attendanceDate;
    }

    public Integer getAttType() {
        return AttType;
    }

    public void setAttType(Integer attType) {
        AttType = attType;
    }

    public String getAttImg() {
        return AttImg;
    }

    public void setAttImg(String attImg) {
        AttImg = attImg;
    }

    public String getAttAddress() {
        return AttAddress;
    }

    public void setAttAddress(String attAddress) {
        AttAddress = attAddress;
    }
}
