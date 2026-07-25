package com.creatrix.salessolution.Model;

public class DcpCcpData {
    private int DoctorId;
    private String DoctorName;
    private String TypeName; // "DCP" or "CCP"

    private String  DocContact;
    private String  DoctorTypeName;
    private String  ProgramTypeName;
    private String  ChemberName;

    public void setDoctorId(int doctorId) {
        DoctorId = doctorId;
    }

    public void setDoctorName(String doctorName) {
        DoctorName = doctorName;
    }

    public void setTypeName(String typeName) {
        TypeName = typeName;
    }

    public String getDocContact() {
        return DocContact;
    }

    public void setDocContact(String docContact) {
        DocContact = docContact;
    }

    public String getDoctorTypeName() {
        return DoctorTypeName;
    }

    public void setDoctorTypeName(String doctorTypeName) {
        DoctorTypeName = doctorTypeName;
    }

    public String getProgramTypeName() {
        return ProgramTypeName;
    }

    public void setProgramTypeName(String programTypeName) {
        ProgramTypeName = programTypeName;
    }

    public String getChemberName() {
        return ChemberName;
    }

    public void setChemberName(String chemberName) {
        ChemberName = chemberName;
    }

    public int getDoctorId() { return DoctorId; }
    public String getDoctorName() { return DoctorName == null ? "" : DoctorName; }
    public String getTypeName() { return TypeName == null ? "" : TypeName; }
}
