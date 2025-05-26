package com.creatrix.salessolution.Model;

public class ModelSMCType {
    Integer SMCTypeId;
    String SMCType;
    Integer forCustomer;
    Integer forDoctor;
    public Integer getSMCTypeId() {
        return SMCTypeId;
    }

    public void setSMCTypeId(Integer SMCTypeId) {
        this.SMCTypeId = SMCTypeId;
    }

    public String getSMCType() {
        return SMCType;
    }

    public void setSMCType(String SMCType) {
        this.SMCType = SMCType;
    }

    public Integer getForCustomer() {
        return forCustomer;
    }

    public void setForCustomer(Integer forCustomer) {
        this.forCustomer = forCustomer;
    }

    public Integer getForDoctor() {
        return forDoctor;
    }

    public void setForDoctor(Integer forDoctor) {
        this.forDoctor = forDoctor;
    }

    @Override
    public String toString() {
        return  SMCType;
    }
}
