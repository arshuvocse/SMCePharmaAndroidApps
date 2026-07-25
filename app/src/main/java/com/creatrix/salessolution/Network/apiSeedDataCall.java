package com.creatrix.salessolution.Network;
import com.creatrix.salessolution.Activity.OrderProcess.Model.DepoStockModel;
import com.creatrix.salessolution.Model.CustomerType;
import com.creatrix.salessolution.Model.DistrictVM;
import com.creatrix.salessolution.Model.DivisionVM;
import com.creatrix.salessolution.Model.StationType;
import com.creatrix.salessolution.Model.ThanaVM;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface apiSeedDataCall {
    @Headers({"Accept: application/json"})

    @GET("/api/SeedData/GetBSPDivisionAll")
    Call<List<DivisionVM>> GetDivisionAll();

    @GET("/api/SeedData/GetBSPDistrict")
    Call<List<DistrictVM>> GetDistrict();

    @GET("/api/SeedData/GetBSPThana")
   // Call<List<ThanaVM>> GetThana(@Query("id") int id);
    Call<List<ThanaVM>> GetThana();

    @GET("/api/SeedData/GetStation")
    Call<List<StationType>> GetStation();
    //Stock
    @GET("/api/SeedData/GetDCStoreStockList")
    Call<List<DepoStockModel>> GetDepoStock(@Query("CustomerMasterId") String CustomerMasterId);

    //Customer
    @GET("/api/Customer/GetCustomerType")
    Call<List<CustomerType>> GetCustomerType();



}
