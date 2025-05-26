package com.creatrix.salessolution.Interface;

import android.content.Context;

import com.creatrix.salessolution.Model.MarketStructure.StructureTable.Area;
import com.creatrix.salessolution.Model.MarketStructure.StructureTable.Group;
import com.creatrix.salessolution.Model.MarketStructure.StructureTable.Market;
import com.creatrix.salessolution.Model.MarketStructure.StructureTable.Region;
import com.creatrix.salessolution.Model.MarketStructure.StructureTable.SubTeritorry;
import com.creatrix.salessolution.Model.MarketStructure.StructureTable.Teritorry;

import java.util.List;

public interface IMarketStracture {
    interface  Presenter{
        void GetGroupLocal();
        void GetRegionLocal(int grpid);
        void GetAreaLocal(int regid);
        void GetTeritoryLocal(int areaid);
        void GetSTeritoryLocal(int terid);
        void GetMarketLocal(int sterid);
    }
    interface View{
        void vGroup(List<Group> groupList);
        void vRegion(List<Region> regionList);
        void vArea(List<Area> areaList);
        void vTeritory(List<Teritorry> teritoryList);
        void vSTeritory(List<SubTeritorry> steritoryList);
        void vMarket(List<Market> marketList);

    }

}
