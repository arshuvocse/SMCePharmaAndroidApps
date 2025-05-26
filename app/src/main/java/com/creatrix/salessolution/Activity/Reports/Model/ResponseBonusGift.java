package com.creatrix.salessolution.Activity.Reports.Model;

import java.util.List;

public class ResponseBonusGift {
    String TotalBouns;
    String TotalGift;
    String TotalAmount;
    List<ModelGiftBonus> Lists;

    public ResponseBonusGift() {
    }

    public String getTotalAmount() {
        return TotalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        TotalAmount = totalAmount;
    }

    public String getTotalBouns() {
        return TotalBouns;
    }

    public void setTotalBouns(String totalBouns) {
        TotalBouns = totalBouns;
    }

    public String getTotalGift() {
        return TotalGift;
    }

    public void setTotalGift(String totalGift) {
        TotalGift = totalGift;
    }

    public List<ModelGiftBonus> getLists() {
        return Lists;
    }

    public void setLists(List<ModelGiftBonus> lists) {
        Lists = lists;
    }
}
