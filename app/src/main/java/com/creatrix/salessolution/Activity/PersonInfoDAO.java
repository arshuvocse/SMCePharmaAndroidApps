package com.creatrix.salessolution.Activity;

import com.google.gson.annotations.SerializedName;

public class PersonInfoDAO {
    @SerializedName(value = "Name", alternate = {"providerName"})
    public String Name;
    @SerializedName(value = "Address", alternate = {"address"})
    public String Address;
    @SerializedName(value = "Mobile", alternate = {"mobile"})
    public String Mobile;
    @SerializedName(value = "Upazila", alternate = {"upazila"})
    public String Upazila;
    @SerializedName(value = "BSPCode", alternate = {"providerCode"})
    public String BSPCode;
    @SerializedName(value = "ProviderType", alternate = {"programName"})
    public String ProviderType;
    @SerializedName(value = "Division", alternate = {"division"})
    public String Division;
    @SerializedName(value = "District", alternate = {"district"})
    public String District;
    @SerializedName(value = "OwnerName", alternate = {"ownerName"})
    public String OwnerName;

    public String oneLine() {
        return BSPCode + " : " + Name + " : " + Address + " : " + Mobile + " : " + Upazila;
    }
}
