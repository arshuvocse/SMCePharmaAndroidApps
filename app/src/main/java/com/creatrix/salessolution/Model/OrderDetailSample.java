package com.creatrix.salessolution.Model;

public class OrderDetailSample {
    int pk;
    public int ODSampleId ;
    public int OrderId ;
    public int ProductId ;
    public String ProductName ;
    public String ProductCode ;
    public int Quantity;

    public OrderDetailSample() {
    }

    public int getPk() {
        return pk;
    }

    public int getODSampleId() {
        return ODSampleId;
    }

    public void setODSampleId(int ODSampleId) {
        this.ODSampleId = ODSampleId;
    }

    public int getOrderId() {
        return OrderId;
    }

    public void setOrderId(int orderId) {
        OrderId = orderId;
    }

    public int getProductId() {
        return ProductId;
    }

    public void setProductId(int productId) {
        ProductId = productId;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public String getProductCode() {
        return ProductCode;
    }

    public void setProductCode(String productCode) {
        ProductCode = productCode;
    }

    public int getQuantity() {
        return Quantity;
    }

    public void setQuantity(int quantity) {
        Quantity = quantity;
    }
}
