package com.creatrix.salessolution.Model;

public class PrescriptionVM {
    int PrescriptionId;
    String PrescriptionDate;
    String PrescriptionType;
    String DoctorName;
    String ProductName;

    String Createdby;
    String ApprovalStatus;

    public String getCreatedby() {
        return Createdby;
    }

    public void setCreatedby(String createdby) {
        Createdby = createdby;
    }

    public String getApprovalStatus() {
        return ApprovalStatus;
    }

    public void setApprovalStatus(String approvalStatus) {
        ApprovalStatus = approvalStatus;
    }

    public int getPrescriptionId() {
        return PrescriptionId;
    }

    public void setPrescriptionId(int prescriptionId) {
        PrescriptionId = prescriptionId;
    }

    public String getPrescriptionDate() {
        return PrescriptionDate;
    }

    public void setPrescriptionDate(String prescriptionDate) {
        PrescriptionDate = prescriptionDate;
    }

    public String getPrescriptionType() {
        return PrescriptionType;
    }

    public void setPrescriptionType(String prescriptionType) {
        PrescriptionType = prescriptionType;
    }

    public String getDoctorName() {
        return DoctorName;
    }

    public void setDoctorName(String doctorName) {
        DoctorName = doctorName;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }
}
