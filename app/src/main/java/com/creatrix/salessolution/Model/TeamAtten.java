package com.creatrix.salessolution.Model;

public class TeamAtten {
    public int UserId;
    public int EmpInfoId;
    public String UserName;
    public String UserType;
    public String Password;
    public String UserEmail;
    public String ContactInfo;
    public String RoleName;
    public int Role;
    Attendance userAttendance;
    ExpenseClaimMaster userExpClaim;

    public int getUserId() {
        return UserId;
    }

    public void setUserId(int userId) {
        UserId = userId;
    }

    public int getEmpInfoId() {
        return EmpInfoId;
    }

    public void setEmpInfoId(int empInfoId) {
        EmpInfoId = empInfoId;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getUserType() {
        return UserType;
    }

    public void setUserType(String userType) {
        UserType = userType;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getUserEmail() {
        return UserEmail;
    }

    public void setUserEmail(String userEmail) {
        UserEmail = userEmail;
    }

    public String getContactInfo() {
        return ContactInfo;
    }

    public void setContactInfo(String contactInfo) {
        ContactInfo = contactInfo;
    }

    public String getRoleName() {
        return RoleName;
    }

    public void setRoleName(String roleName) {
        RoleName = roleName;
    }

    public int getRole() {
        return Role;
    }

    public void setRole(int role) {
        Role = role;
    }

    public Attendance getUserAttendance() {
        return userAttendance;
    }

    public void setUserAttendance(Attendance userAttendance) {
        this.userAttendance = userAttendance;
    }

    public ExpenseClaimMaster getUserExpClaim() {
        return userExpClaim;
    }

    public void setUserExpClaim(ExpenseClaimMaster userExpClaim) {
        this.userExpClaim = userExpClaim;
    }
}
