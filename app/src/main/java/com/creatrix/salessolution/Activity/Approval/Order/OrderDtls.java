package com.creatrix.salessolution.Activity.Approval.Order;

import androidx.annotation.Keep;

@Keep public class OrderDtls {
    int ProductId;
    String ProductCode;
    String ProductName;
    int Quantity;
    double TotalTradePrice;
    double TotalVatAmount;
    double UnitPrice;

    public int getProductId() {
        return ProductId;
    }

    public void setProductId(int productId) {
        ProductId = productId;
    }

    public String getProductCode() {
        return ProductCode;
    }

    public void setProductCode(String productCode) {
        ProductCode = productCode;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public int getQuantity() {
        return Quantity;
    }

    public void setQuantity(int quantity) {
        Quantity = quantity;
    }

    public double getTotalTradePrice() {
        return TotalTradePrice;
    }

    public void setTotalTradePrice(double totalTradePrice) {
        TotalTradePrice = totalTradePrice;
    }

    public double getTotalVatAmount() {
        return TotalVatAmount;
    }

    public void setTotalVatAmount(double totalVatAmount) {
        TotalVatAmount = totalVatAmount;
    }

    public double getUnitPrice() {
        return UnitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        UnitPrice = unitPrice;
    }
}
