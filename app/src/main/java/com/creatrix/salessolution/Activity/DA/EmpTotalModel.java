package com.creatrix.salessolution.Activity.DA;

import androidx.annotation.Keep;

@Keep
public class EmpTotalModel {
  String TotalOrder;
  String totalDcr;
  String TotalRX;

    public String getTotalOrder() {
        return TotalOrder;
    }

    public void setTotalOrder(String totalOrder) {
        TotalOrder = totalOrder;
    }

    public String getTotalDcr() {
        return totalDcr;
    }

    public void setTotalDcr(String totalDcr) {
        this.totalDcr = totalDcr;
    }

    public String getTotalRX() {
        return TotalRX;
    }

    public void setTotalRX(String totalRX) {
        TotalRX = totalRX;
    }
}
