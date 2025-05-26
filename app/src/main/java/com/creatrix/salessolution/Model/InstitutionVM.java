package com.creatrix.salessolution.Model;

public class InstitutionVM {
    int InstitutionId;
    String Institution;

    public int getInstitutionId() {
        return InstitutionId;
    }

    public void setInstitutionId(int institutionId) {
        InstitutionId = institutionId;
    }

    public String getInstitution() {
        return Institution;
    }

    public void setInstitution(String institution) {
        Institution = institution;
    }

    @Override
    public String toString() {
        return Institution ;
    }
}
