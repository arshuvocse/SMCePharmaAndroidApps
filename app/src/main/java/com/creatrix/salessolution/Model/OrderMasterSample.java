package com.creatrix.salessolution.Model;

import java.util.List;

public class OrderMasterSample {
    int pk;
    public int OrderId ;
    public int EmpId ;
    public String EmpName ;
    int DoctorId;
    String DoctorCode;
    String DoctorName;
    String ChamberAddress;
    public String CreatedBy;
    public String CreatedDate ;
    public String Status;
    public String Remarks;
    public int IsPending;
    public int IsConfirm;
    public int IsDelivered;
    public List<OrderDetailSample> OrderDetailSampleMain ;
    public List<ProductSample> OrderSampleDetails ;

    public OrderMasterSample() {
    }

    public int getOrderId() {
        return OrderId;
    }

    public void setOrderId(int orderId) {
        OrderId = orderId;
    }

    public int getEmpId() {
        return EmpId;
    }

    public void setEmpId(int empId) {
        EmpId = empId;
    }

    public String getCreatedBy() {
        return CreatedBy;
    }

    public void setCreatedBy(String createdBy) {
        CreatedBy = createdBy;
    }

    public String getCreatedDate() {
        return CreatedDate;
    }

    public void setCreatedDate(String createdDate) {
        CreatedDate = createdDate;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getRemarks() {
        return Remarks;
    }

    public void setRemarks(String remarks) {
        Remarks = remarks;
    }

    public int getIsPending() {
        return IsPending;
    }

    public void setIsPending(int isPending) {
        IsPending = isPending;
    }

    public int getIsConfirm() {
        return IsConfirm;
    }

    public void setIsConfirm(int isConfirm) {
        IsConfirm = isConfirm;
    }

    public int getIsDelivered() {
        return IsDelivered;
    }

    public void setIsDelivered(int isDelivered) {
        IsDelivered = isDelivered;
    }

    public List<OrderDetailSample> getOrderDetailSampleMain() {
        return OrderDetailSampleMain;
    }

    public void setOrderDetailSampleMain(List<OrderDetailSample> orderDetailSampleMain) {
        OrderDetailSampleMain = orderDetailSampleMain;
    }

    public List<ProductSample> getOrderSampleDetails() {
        return OrderSampleDetails;
    }

    public void setOrderSampleDetails(List<ProductSample> orderSampleDetails) {
        OrderSampleDetails = orderSampleDetails;
    }
}
