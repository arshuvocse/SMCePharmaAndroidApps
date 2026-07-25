package com.creatrix.salessolution.Network;

import com.creatrix.salessolution.Activity.Approval.VisitPlan.Model.ModelPending;
import com.creatrix.salessolution.Activity.Customer.Approval.Model.CustomerApprovalList;
import com.creatrix.salessolution.Activity.Customer.Approval.Model.CustomerApprovalRQ;
import com.creatrix.salessolution.Activity.Customer.Pending.BtnModel;
import com.creatrix.salessolution.Activity.Customer.Pending.UpdateCustMaket;
import com.creatrix.salessolution.Activity.Customer.CustomerSvModel;
import com.creatrix.salessolution.Activity.Customer.Pending.CustomerARModel;
import com.creatrix.salessolution.Activity.Doctor.Pending.DoctorARModel;
import com.creatrix.salessolution.Activity.Doctor.UpdateDocMarket;
import com.creatrix.salessolution.Activity.PersonInfoDAO;
import com.creatrix.salessolution.Model.Customer;
import com.creatrix.salessolution.Model.EmpTotalCountModel;
import com.creatrix.salessolution.Model.ResultInfo;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface ApiCustomerCall {
    @Headers({"Accept: application/json"})
    @GET("/api/Customer")
    Call<List<Customer>> GetCustomerByUser(@Query("empId") int empId);
    @GET("/api/Customer/GetCustomerByUserByMobileNo")
    Call<Customer> GetCustomerByUserByMobileNo(@Query("empId") int empId, @Query("CellNo") String CellNo);

    @POST("/api/Customer/SaveCustomer")
    Call<ResultInfo> SaveCustomer(@Body CustomerSvModel aInfo);

    @POST("/api/Customer/UpdateCustomerBSP")
    Call<ResultInfo> UpdateCustomerBSP(@Body CustomerSvModel aInfo);

    @GET("/api/Reports/GetEmployeeWiseTotalCount")
    Call<EmpTotalCountModel> GetEmployeeWiseTotalCount(@Query("empId") String empId, @Query("Date")   String Date);
    @GET("/api/Customer/GetPersonByDivisionDistrict")
  Call<List<PersonInfoDAO>> GetPersonByDivisionDistrict(@Query("DivisionId") int DivisionId, @Query("DistrictId")   int DistrictId, @Query("ThanaId")   int ThanaId, @Query("FromWhom")   String FromWhom);
    @Headers({"Accept: text/plain"})
    @GET("/api/IntrigrationAPI/GetProviderInfoIntrigration")
    Call<List<PersonInfoDAO>> GetProviderInfoIntrigration(@Query("upazila") String upazila, @Query("userId") String userId, @Query("pass") String pass);
    //Approve/reject
    @GET("/api/Reports/GetCustomerPendingRejectList")
    Call<List<CustomerARModel>> GetCustomerApproveRejList(@Query("empId") int empId,@Query("ApprovalStatus") String status);
    //Approve/reject
    @GET("/api/Reports/GetCustomerPendingRejectLists")
    Call<List<CustomerARModel>> GetCustomerApproveRejLists(@Query("empId") int empId);

    //Approval
    @GET("/api/Customer/GetCustomer_Applog")
    Call<List<CustomerApprovalList>> GetCustomerApproval(@Query("pram") String pram, @QueryMap Map<String, String> mapparam);

    @POST("/api/Customer/SaveCustomer_Applog")
    Call<ResultInfo> SaveCustomerApproval(@Body CustomerApprovalRQ aInfo);

    //New Apis
    @GET("/api/Reports/GetCustomerUpdateInfo")
    Call<List<CustomerARModel>> GetCustomerUpdate(@Query("empId") int empId, @QueryMap Map<String, String> mapparam);

    @GET("/api/Reports/GetDoctorUpdateInfo")
    Call<List<DoctorARModel>> GetDoctorUpdate(@Query("empId") int empId, @QueryMap Map<String, String> mapparam);

    @GET("/api/Reports/GetOnOffButtonForCustomerChange")
    Call<BtnModel> GetCustomerbtnOnOFF();

/*    @GET("/api/Customer/GetCustomerInfo_Change")
    Call<CustomerInfoChangeModel> GetCustomerInfoChange(@Query("empId") int empId);*/


    @POST("/api/Customer/UpdateCustomerProvider")
    //Call<ResultInfo> UpdateCustomerProviderType(@Field("CustomerId") int CustomerId,@Field("EmpId") int EmpId,@Field("ProviderTypeId") int ProviderTypeId);
    Call<ResultInfo> UpdateCustomerProviderType(@QueryMap HashMap<String,String> data);

    @POST("/api/Doctor/UpdateDoctorProvider")
    Call<ResultInfo> UpdateDoctorProviderType(@QueryMap HashMap<String,String> data);

    @POST("/api/Customer/UpdateCustomerMarket")
    Call<ResultInfo> UpdateCustomerMarket(@Body UpdateCustMaket updateCustMaket);

    @POST("/api/Doctor/UpdateDoctorMarket")
    Call<ResultInfo> UpdateDoctorMarket(@Body UpdateDocMarket updateDocMarket);


    @GET("/api/Home/GetAllApprovalPendingInfo")
    Call<List<ModelPending>> GetDash(@Query("pram") String pram, @Query("Role") String Role);

}
