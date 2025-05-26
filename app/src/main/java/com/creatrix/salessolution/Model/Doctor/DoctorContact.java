package com.creatrix.salessolution.Model.Doctor;

public class DoctorContact {
    int ContactTypeId;
    String Contact;
    String ContactType;

    public DoctorContact(int contactTypeId, String contact,String contactType) {
        ContactTypeId = contactTypeId;
        Contact = contact;
        ContactType = contactType;
    }

    public DoctorContact() {
    }

    public int getContactTypeId() {
        return ContactTypeId;
    }

    public void setContactTypeId(int contactTypeId) {
        ContactTypeId = contactTypeId;
    }

    public String getContact() {
        return Contact;
    }

    public void setContact(String contact) {
        Contact = contact;
    }

    public String getContactType() {
        return ContactType;
    }

    public void setContactType(String contactType) {
        ContactType = contactType;
    }
}
