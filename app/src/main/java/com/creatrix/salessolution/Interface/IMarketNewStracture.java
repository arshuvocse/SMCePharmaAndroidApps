package com.creatrix.salessolution.Interface;

import com.creatrix.salessolution.Model.MarketStructure.StructureTable.Area;
import com.creatrix.salessolution.Model.MarketStructure.StructureTable.Group;
import com.creatrix.salessolution.Model.MarketStructure.StructureTable.Market;
import com.creatrix.salessolution.Model.MarketStructure.StructureTable.Region;
import com.creatrix.salessolution.Model.MarketStructure.StructureTable.SubTeritorry;
import com.creatrix.salessolution.Model.MarketStructure.StructureTable.Teritorry;

import java.util.List;

public interface IMarketNewStracture {
    interface  Presenter{
        void GetGroupLocal();
        void GetRegionLocalEdit(int grpid);
        void GetRegionLocal(int grpid);
        void GetRegionLocalEnd(int grpid);
        void GetRegionLocalEndEdit(int grpid);
        void GetAreaLocal(int regid);
        void GetAreaLocalEdit(int regid);
        void GetAreaLocalEnd(int regid);
        void GetAreaLocalEndEdit(int regid);
        void GetTeritoryLocal(int areaid);
        void GetTeritoryLocalEdit(int areaid);
        void GetTeritoryLocalEditEnd(int areaid);
        void GetTeritoryLocalEditEndEdit(int areaid);
        void GetTeritoryLocalEnd(int areaid);
        void GetSTeritoryLocal(int terid);
        void GetSTeritoryLocalEdit(int terid);
        void GetSTeritoryLocalEditEnd(int terid);
        void GetSTeritoryLocalEnd(int terid);
        void GetMarketLocal(int sterid);
        void GetMarketLocalEdit(int sterid);
        void GetMarketLocalEditEnd(int sterid);
        void GetMarketLocalEnd(int sterid);
        void GetMarketLocalEndEdit(int sterid);
    }
    interface View{
        void vGroup(List<Group> groupList);
        void vRegion(List<Region> regionList);
        void vRegionEdit(List<Region> regionList);
        void vRegionEnd(List<Region> regionList);
        void vRegionEndEdit(List<Region> regionList);
        void vArea(List<Area> areaList);
        void vAreaEnd(List<Area> areaList);
        void vAreaEndEdit(List<Area> areaList);
        void vTeritory(List<Teritorry> teritoryList);
        void vTeritoryEnd(List<Teritorry> teritoryList);
        void vTeritoryEndEdit(List<Teritorry> teritoryList);
        void vSTeritory(List<SubTeritorry> steritoryList);
        void vSTeritoryEnd(List<SubTeritorry> steritoryList);
        void vSTeritoryEndEdit(List<SubTeritorry> steritoryList);
        void vMarket(List<Market> marketList);
        void vMarketEnd(List<Market> marketList);
        void vMarketEndEdit(List<Market> marketList);

    }

}
