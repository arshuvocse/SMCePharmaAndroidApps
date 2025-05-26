package com.creatrix.salessolution.Network;

import com.creatrix.salessolution.Activity.Approval.DA.DAListData;
import com.creatrix.salessolution.Activity.Approval.Order.OrderApprovalData;
import com.creatrix.salessolution.Activity.Approval.Order.OrderApprovalSaveBody;
import com.creatrix.salessolution.Activity.OrderProcess.Model.CampaignCalModel;
import com.creatrix.salessolution.Activity.OrderProcess.Model.CampaignModel;
import com.creatrix.salessolution.Activity.OrderProcess.Model.CampaignGetReq;
import com.creatrix.salessolution.Activity.OrderProcess.Model.CampaignPostReq;
import com.creatrix.salessolution.Activity.OrderProcess.Model.OrderMasterModel;
import com.creatrix.salessolution.Activity.SelfReports.SalesReport.Model.CWSaleReportModel;
import com.creatrix.salessolution.Activity.SelfReports.SalesReport.Model.PWSaleReportModel;
import com.creatrix.salessolution.Model.CampaignMasterNew;
import com.creatrix.salessolution.Model.CordinateUpdate;
import com.creatrix.salessolution.Model.OrderMaster;
import com.creatrix.salessolution.Model.OrderMasterNew;
import com.creatrix.salessolution.Model.QuotedPrice;
import com.creatrix.salessolution.Model.ResultInfo;
import com.creatrix.salessolution.ResposeModel.ResponseInfo;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface OrderProcessAPICALL {
    @Headers({"Accept: application/json"})

    @GET("/api/QuotedPrice/GetQuotedPrice")
    Call<List<QuotedPrice>> GetQuotedPrice();
    @GET("/api/Campaign/GetCurrentCampaign")
    Call<List<CampaignMasterNew>> GetBonusCampaign(@Query("customerId") int  customerId);
    //Call<List<CampaignMasterNew>> GetBonusCampaign(@Query("empId") int empId,@Query("customerId") int  customerId);

    @POST("/api/Campaign/GetCustomerWiseCampaign")
    Call<List<CampaignModel>> GetCampaign(@Body List<CampaignGetReq> campaignGetReq);

    @POST("/api/Campaign/GetOrderProductWiseCampaign")
    Call<List<CampaignCalModel>> GetCampaignProductWise(@Body CampaignPostReq campaignPosttReq);

    @POST("/api/Location/UpdateCustomerLocation")
    Call<ResponseInfo> UpdateLocation(@Body CordinateUpdate location);

    @POST("/api/OrderProcess/MakeOrder")
   // Call<ResultInfo> SubmitOrder2(@Body OrderMasterNew aOrder);
    Call<ResultInfo> SubmitOrder2(@Body OrderMasterModel aOrder);

    @GET("/api/OrderProcess/MakeOrder")
    Call<ResultInfo> SubmitOrder(@Body OrderMaster aOrder);

    @GET("/api/Order/GetOrder_Applog")
    Call<List<OrderApprovalData>> GetOrderApproval(@Query("pram") String pram, @QueryMap Map<String,String> mapparam);

    @POST("/api/Order/SaveOrder_Applog")
    Call<ResultInfo> SaveOrderApproval(@Body OrderApprovalSaveBody aOrder);


    //Report
    @GET("/api/Reports/GetCWSR")
    Call<CWSaleReportModel> GetCustWiseOrderReport(@QueryMap Map<String,String> mapparam);

    @GET("/api/Reports/GetPWSR")
    Call<PWSaleReportModel> GetProductWiseOrderReport(@QueryMap Map<String,String> mapparam);

}
