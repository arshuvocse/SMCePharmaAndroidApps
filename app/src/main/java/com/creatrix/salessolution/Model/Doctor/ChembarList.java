package com.creatrix.salessolution.Model.Doctor;

public class ChembarList {
    int ChamberTypeId;
    String Name;



    public int getChamberTypeId() {
        return ChamberTypeId;
    }

    public void setChamberTypeId(int chamberTypeId) {
        ChamberTypeId = chamberTypeId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }



    @Override
    public String toString() {
        return Name ;
    }
}
