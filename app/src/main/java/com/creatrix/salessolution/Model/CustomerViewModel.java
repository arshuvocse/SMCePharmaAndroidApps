package com.creatrix.salessolution.Model;

public class CustomerViewModel {

    private int CustomerMasterId;
    private String CustomerName;
    private String Address;

    @Override
    public String toString() {
        return CustomerName;
    }

    public int getCustomerMasterId() {
        return CustomerMasterId;
    }

    public void setCustomerMasterId(int customerMasterId) {
        CustomerMasterId = customerMasterId;
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
}
