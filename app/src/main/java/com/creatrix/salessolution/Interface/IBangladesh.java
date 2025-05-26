package com.creatrix.salessolution.Interface;

import com.creatrix.salessolution.Model.DistrictVM;
import com.creatrix.salessolution.Model.DivisionVM;
import com.creatrix.salessolution.Model.MarketStructure.StructureTable.Area;
import com.creatrix.salessolution.Model.MarketStructure.StructureTable.Group;
import com.creatrix.salessolution.Model.MarketStructure.StructureTable.Market;
import com.creatrix.salessolution.Model.MarketStructure.StructureTable.Region;
import com.creatrix.salessolution.Model.MarketStructure.StructureTable.SubTeritorry;
import com.creatrix.salessolution.Model.MarketStructure.StructureTable.Teritorry;
import com.creatrix.salessolution.Model.ThanaVM;

import java.util.List;

public interface IBangladesh {
    interface  Presenter{
        void GetDivisionLocal(/*List<DivisionVM> divList*/);
        void GetDistrictLocal(int divid);
        void GetThanaLocal(int disid);

    }
    interface View{
        void vDivL(List<DivisionVM> divList);
        void vDisL(List<DistrictVM> disList);
        void vThanaL(List<ThanaVM> thanaList);


    }
}
