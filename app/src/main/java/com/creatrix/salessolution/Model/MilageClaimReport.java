package com.creatrix.salessolution.Model;

public class MilageClaimReport {
    public  int MileageClaimId;
    public  String MileageDate;
    public  String TransportName;
    public  String EmpName;
    public  double MileageInKM;
    public  double MeterReading;
    public  String MarketName;
    public  String ApprovalStatus;
    public  String ImageString;
    public  String Comments;

    public int getMileageClaimId() {
        return MileageClaimId;
    }

    public void setMileageClaimId(int mileageClaimId) {
        MileageClaimId = mileageClaimId;
    }

    public String getMileageDate() {
        return MileageDate;
    }

    public void setMileageDate(String mileageDate) {
        MileageDate = mileageDate;
    }

    public String getTransportName() {
        return TransportName;
    }

    public void setTransportName(String transportName) {
        TransportName = transportName;
    }

    public String getEmpName() {
        return EmpName;
    }

    public void setEmpName(String empName) {
        EmpName = empName;
    }

    public double getMileageInKM() {
        return MileageInKM;
    }

    public void setMileageInKM(double mileageInKM) {
        MileageInKM = mileageInKM;
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

    public String getImageString() {
        return ImageString;
    }

    public void setImageString(String imageString) {
        ImageString = imageString;
    }

    public double getMeterReading() {
        return MeterReading;
    }

    public void setMeterReading(double meterReading) {
        MeterReading = meterReading;
    }

    public String getComments() {
        return Comments;
    }

    public void setComments(String comments) {
        Comments = comments;
    }
}
