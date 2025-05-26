package com.creatrix.salessolution.Model;

public class UserRole {
    int pk;
    int UserRoleID;
    String  RoleName;

    public int getPk() {
        return pk;
    }

    public void setPk(int pk) {
        this.pk = pk;
    }

    public int getUserRoleID() {
        return UserRoleID;
    }

    public void setUserRoleID(int userRoleID) {
        UserRoleID = userRoleID;
    }

    public String getRoleName() {
        return RoleName;
    }

    public void setRoleName(String roleName) {
        RoleName = roleName;
    }
}
