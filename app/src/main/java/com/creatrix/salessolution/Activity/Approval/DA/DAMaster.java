package com.creatrix.salessolution.Activity.Approval.DA;

import androidx.annotation.Keep;

@Keep public class DAMaster {
    int TadaID;
    int EmpInfoId;
    String TadaDate;
    String EmpName;
    String ImageString;
    Double DaAmt;
    String Remarks;
    String MarketName;
    String ApprovalStatus;
    String TadaDateNewFormat;

    public String getTotalEmpRslt() {
        return TotalEmpRslt;
    }

    public void setTotalEmpRslt(String totalEmpRslt) {
        TotalEmpRslt = totalEmpRslt;
    }

    String TotalEmpRslt;

    public String getTadaDateNewFormat() {
        return TadaDateNewFormat;
    }

    public void setTadaDateNewFormat(String tadaDateNewFormat) {
        TadaDateNewFormat = tadaDateNewFormat;
    }

    public int getTadaID() {
        return TadaID;
    }

    public void setTadaID(int tadaID) {
        TadaID = tadaID;
    }

    public int getEmpInfoId() {
        return EmpInfoId;
    }

    public void setEmpInfoId(int empInfoId) {
        EmpInfoId = empInfoId;
    }

    public String getTadaDate() {
        return TadaDate;
    }

    public void setTadaDate(String tadaDate) {
        TadaDate = tadaDate;
    }

    public String getEmpName() {
        return EmpName;
    }

    public void setEmpName(String empName) {
        EmpName = empName;
    }

    public String getImageString() {
        return ImageString;
    }

    public void setImageString(String imageString) {
        ImageString = imageString;
    }

    public Double getDaAmt() {
        return DaAmt;
    }

    public void setDaAmt(Double daAmt) {
        DaAmt = daAmt;
    }

    public String getRemarks() {
        return Remarks;
    }

    public void setRemarks(String remarks) {
        Remarks = remarks;
    }

    public String getMarketName() {
        return MarketName;
    }

    public void setMarketName(String marketName) {
        MarketName = marketName;
    }

    public String getApprovalStatus() {
        return ApprovalStatus;
    }

    public void setApprovalStatus(String approvalStatus) {
        ApprovalStatus = approvalStatus;
    }
}
