package com.creatrix.salessolution.Model;

public class MileageClaimSM {
    public int MileageClaimId;
    public String MileageDate;
    public int TransportId;
    public int MarketId;
    public double MileageInKM;
    public double MeterReading;
    public String  Remarks;
    public String  ImageBase64String;
    public int EmpInfoId;

    public MileageClaimSM() {
    }

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

    public int getTransportId() {
        return TransportId;
    }

    public void setTransportId(int transportId) {
        TransportId = transportId;
    }

    public int getMarketId() {
        return MarketId;
    }

    public void setMarketId(int marketId) {
        MarketId = marketId;
    }

    public double getMileageInKM() {
        return MileageInKM;
    }

    public void setMileageInKM(double mileageInKM) {
        MileageInKM = mileageInKM;
    }

    public double getMeterReading() {
        return MeterReading;
    }

    public void setMeterReading(double meterReading) {
        MeterReading = meterReading;
    }

    public String getRemarks() {
        return Remarks;
    }

    public void setRemarks(String remarks) {
        Remarks = remarks;
    }

    public String getImageBase64String() {
        return ImageBase64String;
    }

    public void setImageBase64String(String imageBase64String) {
        ImageBase64String = imageBase64String;
    }

    public int getEmpInfoId() {
        return EmpInfoId;
    }

    public void setEmpInfoId(int empInfoId) {
        EmpInfoId = empInfoId;
    }
}
