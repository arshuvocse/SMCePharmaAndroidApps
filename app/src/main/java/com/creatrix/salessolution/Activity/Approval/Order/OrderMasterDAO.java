package com.creatrix.salessolution.Activity.Approval.Order;

import androidx.annotation.Keep;

import java.util.List;
@Keep public class OrderMasterDAO {
    int OrderId;
    String OrderCode;
    String CustomerCode;
    String CustomerName;
    String Address;
    double TotalAmount;
    double TotalTP;
    double TotalVAT;
    int ApprovalStatus;
    List<OrderDtls> aOrderDtls;
    public int getOrderId() {
        return OrderId;
    }

    public void setOrderId(int orderId) {
        OrderId = orderId;
    }

    public String getOrderCode() {
        return OrderCode;
    }

    public void setOrderCode(String orderCode) {
        OrderCode = orderCode;
    }

    public String getCustomerCode() {
        return CustomerCode;
    }

    public void setCustomerCode(String customerCode) {
        CustomerCode = customerCode;
    }

    public String getCustomerName() {
        return CustomerName;
    }

    public void setCustomerName(String customerName) {
        CustomerName = customerName;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public double getTotalAmount() {
        return TotalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        TotalAmount = totalAmount;
    }

    public double getTotalTP() {
        return TotalTP;
    }

    public void setTotalTP(double totalTP) {
        TotalTP = totalTP;
    }

    public double getTotalVAT() {
        return TotalVAT;
    }

    public void setTotalVAT(double totalVAT) {
        TotalVAT = totalVAT;
    }

    public int getApprovalStatus() {
        return ApprovalStatus;
    }

    public void setApprovalStatus(int approvalStatus) {
        ApprovalStatus = approvalStatus;
    }

    public List<OrderDtls> getaOrderDtls() {
        return aOrderDtls;
    }

    public void setaOrderDtls(List<OrderDtls> aOrderDtls) {
        this.aOrderDtls = aOrderDtls;
    }
}
