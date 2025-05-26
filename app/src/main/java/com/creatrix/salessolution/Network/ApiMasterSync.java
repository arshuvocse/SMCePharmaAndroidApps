package com.creatrix.salessolution.Network;

import com.creatrix.salessolution.Model.MarketStructure.AssignEmpTable.ASM;
import com.creatrix.salessolution.Model.MarketStructure.AssignEmpTable.MIO;
import com.creatrix.salessolution.Model.MarketStructure.AssignEmpTable.NSM;
import com.creatrix.salessolution.Model.MarketStructure.AssignEmpTable.RSM;
import com.creatrix.salessolution.Model.MarketStructure.StructureTable.Area;
import com.creatrix.salessolution.Model.MarketStructure.StructureTable.Group;
import com.creatrix.salessolution.Model.MarketStructure.StructureTable.Market;
import com.creatrix.salessolution.Model.MarketStructure.StructureTable.Region;
import com.creatrix.salessolution.Model.MarketStructure.StructureTable.SubTeritorry;
import com.creatrix.salessolution.Model.MarketStructure.StructureTable.Teritorry;
import com.creatrix.salessolution.Model.ResultInfo;


import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiMasterSync {
    @Headers({"Accept: application/json"})
    @POST("/api/StructureSync/LastSynchronizationInfo")
    Call<ResultInfo> LastSync(@Body int EmpInfoId);


    @GET("/api/StructureSync/GetFieldForceMarket")
    Call<List<Market>> GetMarketByUser(@Query("empcode") String empcode,
                                       @Query("emprole") String emprole);
    @GET("/api/StructureSync/GetFieldForceSubTerritory")
    Call<List<SubTeritorry>> GetSubTeritoryByUser(@Query("empcode") String empcode,
                                                  @Query("emprole") String emprole);
    @GET("/api/StructureSync/GetFieldForceTerritory")
    Call<List<Teritorry>> GetTeritoryByUser(@Query("empcode") String empcode,
                                            @Query("emprole") String emprole);

    @GET("/api/StructureSync/GetFieldForceArea")
    Call<List<Area>> GetAreaByUser(@Query("empcode") String empcode,
                                   @Query("emprole") String emprole);

    @GET("/api/StructureSync/GetFieldForceRegion")
    Call<List<Region>> GetRegionByUser(@Query("empcode") String empcode,
                                       @Query("emprole") String emprole);
    @GET("/api/StructureSync/GetFieldForceGroup")
    Call<List<Group>> GetGroupByUser(@Query("empcode") String empcode,
                                     @Query("emprole") String emprole);

    @GET("/api/StructureSync/GetFieldForceMio")
    Call<List<MIO>> GetMio(@Query("empcode") String empcode,
                           @Query("emprole") String emprole);

    @GET("/api/StructureSync/GetFieldForceAsm")
    Call<List<ASM>> GetASM(@Query("empcode") String empcode,
                           @Query("emprole") String emprole);

    @GET("/api/StructureSync/GetFieldForceRsm")
    Call<List<RSM>> GetRSM(@Query("empcode") String empcode,
                           @Query("emprole") String emprole);

    @GET("/api/StructureSync/GetFieldForceNsm")
    Call<List<NSM>> GetNSM(@Query("empcode") String empcode,
                           @Query("emprole") String emprole);


}
