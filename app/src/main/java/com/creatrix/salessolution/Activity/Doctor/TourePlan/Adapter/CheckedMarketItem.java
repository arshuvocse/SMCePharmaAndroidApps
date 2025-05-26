package com.creatrix.salessolution.Activity.Doctor.TourePlan.Adapter;

import com.creatrix.salessolution.Model.Customer;
import com.creatrix.salessolution.Model.MarketStructure.StructureTable.Market;

import java.util.List;

public interface CheckedMarketItem {
    void ckdItemMarketName(List<Market> st, int Pos);
    void unckdItemMarketName(List<Market> st,int Pos);
}
