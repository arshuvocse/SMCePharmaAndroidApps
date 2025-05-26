package com.creatrix.salessolution.Model;

public class DcrVM {
    int DcrId;
    String DcrDate;
    String TourTypeName;
    String Name;
    String EmpName;
    Boolean IsApproved;
    String ApprovalStatus;


    public int getDcrId() {
        return DcrId;
    }

    public void setDcrId(int dcrId) {
        DcrId = dcrId;
    }

    public String getDcrDate() {
        return DcrDate;
    }

    public void setDcrDate(String dcrDate) {
        DcrDate = dcrDate;
    }

    public String getTourTypeName() {
        return TourTypeName;
    }

    public void setTourTypeName(String tourTypeName) {
        TourTypeName = tourTypeName;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getEmpName() {
        return EmpName;
    }

    public void setEmpName(String empName) {
        EmpName = empName;
    }

    public Boolean getApproved() {
        return IsApproved;
    }

    public void setApproved(Boolean approved) {
        IsApproved = approved;
    }

    public String getApprovalStatus() {
        return ApprovalStatus;
    }

    public void setApprovalStatus(String approvalStatus) {
        ApprovalStatus = approvalStatus;
    }
}
