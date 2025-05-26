package com.creatrix.salessolution.Network;

import com.creatrix.salessolution.Model.Product;
import com.creatrix.salessolution.Model.ProductSample;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ProductApi {
    //Product
    @GET("/api/Product")
    Call<List<Product>> GetProductList(@Query("empId") int empId);


    @GET("/api/Product/GetSampleProducts")
    Call<List<ProductSample>> GetSampleProductsList(@Query("empId") int empId);
}
