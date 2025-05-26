package com.creatrix.salessolution.Model;

public class Dashboard_SummeryVM {
    String PunchInTime;
    String WeeklySaleAmount;
    String MonthlySaleAmount;
    String OrderSubmitedToday;
    String OrderTodayAmt;
    String ProductTotalOrder;
    String TotalDcr;
    String TotalPrescription;
    String Colection;
    String BonusGift;

    public String getTotalDcr() {
        return TotalDcr;
    }

    public void setTotalDcr(String totalDcr) {
        TotalDcr = totalDcr;
    }

    public String getOrderTodayAmt() {
        return OrderTodayAmt;
    }

    public void setOrderTodayAmt(String orderTodayAmt) {
        OrderTodayAmt = orderTodayAmt;
    }

    public String getProductTotalOrder() {
        return ProductTotalOrder;
    }

    public void setProductTotalOrder(String productTotalOrder) {
        ProductTotalOrder = productTotalOrder;
    }

    public String getPunchInTime() {
        return PunchInTime;
    }

    public void setPunchInTime(String punchInTime) {
        PunchInTime = punchInTime;
    }

    public String getWeeklySaleAmount() {
        return WeeklySaleAmount;
    }

    public void setWeeklySaleAmount(String weeklySaleAmount) {
        WeeklySaleAmount = weeklySaleAmount;
    }

    public String getMonthlySaleAmount() {
        return MonthlySaleAmount;
    }

    public void setMonthlySaleAmount(String monthlySaleAmount) {
        MonthlySaleAmount = monthlySaleAmount;
    }

    public String getOrderSubmitedToday() {
        return OrderSubmitedToday;
    }

    public void setOrderSubmitedToday(String orderSubmitedToday) {
        OrderSubmitedToday = orderSubmitedToday;
    }

    public String getTotalPrescription() {
        return TotalPrescription;
    }

    public void setTotalPrescription(String totalPrescription) {
        TotalPrescription = totalPrescription;
    }

    public String getColection() {
        return Colection;
    }

    public void setColection(String colection) {
        Colection = colection;
    }

    public String getBonusGift() {
        return BonusGift;
    }

    public void setBonusGift(String bonusGift) {
        BonusGift = bonusGift;
    }
}
