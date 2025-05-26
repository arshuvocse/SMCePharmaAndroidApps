package com.creatrix.salessolution.Model;

public class Report_AttendanceViewModel {

    private  String AttendanceDate;
    private String AttendanceType;
    private String AttendanceStatus;
    private String AttendanceText;
    private String AttendanceTime;


    private String PunchInTime;
    private String PunchOutTime;
    private String PInLat;
    private String PInLog;
    private String PunchOutTime1;
    private String POutLat;
    private String POutLong;


    public String getPunchInTime() {
        return PunchInTime;
    }

    public void setPunchInTime(String punchInTime) {
        PunchInTime = punchInTime;
    }

    public String getPunchOutTime() {
        return PunchOutTime;
    }

    public void setPunchOutTime(String punchOutTime) {
        PunchOutTime = punchOutTime;
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

    public String getPunchOutTime1() {
        return PunchOutTime1;
    }

    public void setPunchOutTime1(String punchOutTime1) {
        PunchOutTime1 = punchOutTime1;
    }

    public String getPOutLat() {
        return POutLat;
    }

    public void setPOutLat(String POutLat) {
        this.POutLat = POutLat;
    }

    public String getPOutLong() {
        return POutLong;
    }

    public void setPOutLong(String POutLong) {
        this.POutLong = POutLong;
    }

    public String getAttendanceDate() {
        return AttendanceDate;
    }

    public void setAttendanceDate(String attendanceDate) {
        AttendanceDate = attendanceDate;
    }

    public String getAttendanceType() {
        return AttendanceType;
    }

    public void setAttendanceType(String attendanceType) {
        AttendanceType = attendanceType;
    }

    public String getAttendanceStatus() {
        return AttendanceStatus;
    }

    public void setAttendanceStatus(String attendanceStatus) {
        AttendanceStatus = attendanceStatus;
    }

    public String getAttendanceText() {
        return AttendanceText;
    }

    public void setAttendanceText(String attendanceText) {
        AttendanceText = attendanceText;
    }

    public String getAttendanceTime() {
        return AttendanceTime;
    }

    public void setAttendanceTime(String attendanceTime) {
        AttendanceTime = attendanceTime;
    }
}
