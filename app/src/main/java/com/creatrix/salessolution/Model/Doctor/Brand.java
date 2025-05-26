package com.creatrix.salessolution.Model.Doctor;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Brand {
    @SerializedName("ProductBrandId")
    @Expose
    private Integer productBrandId;
    @SerializedName("IngridentsId")
    @Expose
    private Integer ingridentsId;
    @SerializedName("ProductSQName")
    @Expose
    private String productSQName;
    private int MaxValue;

    public Brand() {
    }

    public Integer getProductBrandId() {
        return productBrandId;
    }

    public void setProductBrandId(Integer productBrandId) {
        this.productBrandId = productBrandId;
    }

    public Integer getIngridentsId() {
        return ingridentsId;
    }

    public void setIngridentsId(Integer ingridentsId) {
        this.ingridentsId = ingridentsId;
    }

    public String getProductSQName() {
        return productSQName;
    }

    public void setProductSQName(String productSQName) {
        this.productSQName = productSQName;
    }

    public int getMaxValue() {
        return MaxValue;
    }

    public void setMaxValue(int maxValue) {
        MaxValue = maxValue;
    }
}
