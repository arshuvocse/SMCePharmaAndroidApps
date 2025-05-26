package com.creatrix.salessolution.Activity.Reports.Model;

public class ModelGiftBonus {
    String OrderNo;
    String InvoiceNo;
    String InvoiceDate;
    String BounsQty;
    String GiftQty;
    String Amount;

    public ModelGiftBonus() {
    }

    public String getOrderNo() {
        return OrderNo;
    }

    public void setOrderNo(String orderNo) {
        OrderNo = orderNo;
    }

    public String getInvoiceNo() {
        return InvoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        InvoiceNo = invoiceNo;
    }

    public String getInvoiceDate() {
        return InvoiceDate;
    }

    public void setInvoiceDate(String invoiceDate) {
        InvoiceDate = invoiceDate;
    }

    public String getBounsQty() {
        return BounsQty;
    }

    public void setBounsQty(String bounsQty) {
        BounsQty = bounsQty;
    }

    public String getGiftQty() {
        return GiftQty;
    }

    public void setGiftQty(String giftQty) {
        GiftQty = giftQty;
    }

    public String getAmount() {
        return Amount;
    }

    public void setAmount(String amount) {
        Amount = amount;
    }
}
