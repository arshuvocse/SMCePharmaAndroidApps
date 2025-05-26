package com.creatrix.salessolution.Model.Doctor;

public class ContactTypeVM {
    int pk;
    int ContactTypeId;
    String ContactType;
    public int getPk() {
        return pk;
    }

    public void setPk(int pk) {
        this.pk = pk;
    }

    public int getContactTypeId() {
        return ContactTypeId;
    }

    public void setContactTypeId(int contactTypeId) {
        ContactTypeId = contactTypeId;
    }

    public String getContactType() {
        return ContactType;
    }

    public void setContactType(String contactType) {
        ContactType = contactType;
    }

    @Override
    public String toString() {
        return ContactType;
    }
}
