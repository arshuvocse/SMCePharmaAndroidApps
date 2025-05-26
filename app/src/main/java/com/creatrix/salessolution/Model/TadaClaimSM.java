package com.creatrix.salessolution.Model;

public class TadaClaimSM {
    String TadaDate;
    String Remarks;
    int EmpInfoId;
    double TaAmt;
    double DaAmt;
    int id;
    String TourType;
    String ImageString;
    String HotelName;
    String HotelPhone;
    int NewTourTypeId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTourType() {
        return TourType;
    }

    public void setTourType(String tourType) {
        TourType = tourType;
    }

    public String getTadaDate() {
        return TadaDate;
    }

    public void setTadaDate(String tadaDate) {
        TadaDate = tadaDate;
    }

    public String getRemarks() {
        return Remarks;
    }

    public void setRemarks(String remarks) {
        Remarks = remarks;
    }

    public int getEmpInfoId() {
        return EmpInfoId;
    }

    public void setEmpInfoId(int empInfoId) {
        EmpInfoId = empInfoId;
    }

    public double getTaAmt() {
        return TaAmt;
    }

    public void setTaAmt(double taAmt) {
        TaAmt = taAmt;
    }

    public double getDaAmt() {
        return DaAmt;
    }

    public void setDaAmt(double daAmt) {
        DaAmt = daAmt;
    }

    public String getImageString() {
        return ImageString;
    }

    public void setImageString(String imageString) {
        ImageString = imageString;
    }

    public String getHotelName() {
        return HotelName;
    }

    public void setHotelName(String hotelName) {
        HotelName = hotelName;
    }

    public String getHotelPhone() {
        return HotelPhone;
    }

    public void setHotelPhone(String hotelPhone) {
        HotelPhone = hotelPhone;
    }

    public int getNewTourTypeId() {
        return NewTourTypeId;
    }

    public void setNewTourTypeId(int newTourTypeId) {
        NewTourTypeId = newTourTypeId;
    }
}
