package com.creatrix.salessolution.Activity.Approval.DCR;

import androidx.annotation.Keep;

@Keep public class DcrApplogProduct {
    int ProductId;
    String ProductName;
    int ProductQty;

    public DcrApplogProduct() {
    }

    public DcrApplogProduct(String productName, int productQty) {
        ProductName = productName;
        ProductQty = productQty;
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

    public int getProductQty() {
        return ProductQty;
    }

    public void setProductQty(int productQty) {
        ProductQty = productQty;
    }

    @Override
    public String toString() {
        return "DcrApplogProduct{" +
                "ProductName='" + ProductName + '\'' +
                ", ProductQty=" + ProductQty +
                '}';
    }
}
