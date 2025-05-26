package com.creatrix.salessolution.Model;

public class mCompanyUnit {
    public  int ComUnitId;
    public String ComUnitName;

    public int getComUnitId() {
        return ComUnitId;
    }

    public void setComUnitId(int comUnitId) {
        ComUnitId = comUnitId;
    }

    public String getComUnitName() {
        return ComUnitName;
    }

    public void setComUnitName(String comUnitName) {
        ComUnitName = comUnitName;
    }

    @Override
    public String toString() {
        return getComUnitName();
    }
}
