package com.creatrix.salessolution.Network;


import com.creatrix.salessolution.Model.AppVersionModel;
import com.creatrix.salessolution.Model.Dashboard_SummeryVM;
import com.creatrix.salessolution.Model.EmpTotalCountModel;
import com.creatrix.salessolution.Model.NotificationViewModel;
import com.creatrix.salessolution.Model.Order.OrderTrackigMaster;
import com.creatrix.salessolution.Model.OrderMaster;
import com.creatrix.salessolution.Model.OrderViewModel;
import com.creatrix.salessolution.Model.Report_AttendanceViewModel;
import com.creatrix.salessolution.Model.ResultInfo;
import com.creatrix.salessolution.Model.Training;
import com.creatrix.salessolution.Model.User;
import com.creatrix.salessolution.Model.UserTracking;
import com.creatrix.salessolution.Model.mCompanyUnit;
import com.creatrix.salessolution.Model.Notice;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface GetDataService {
    @Headers({"Accept: application/json"})
    @GET("/api/User")
    Call<User> LoginRequest(@Query("UserName") String username, @Query("password") String password,@Query("Imei") String Imei,@Query("DeviceToken") String DeviceToken,@Query("Device") String Device,@Query("AppVersion") String AppVersion,@Query("OS") String OS,@Query("OS_Version") String OS_Version);

    @POST("/api/OrderManagement")
    Call<ResultInfo> MakeOrder(@Body OrderMaster aInfo);

    @POST("/api/Order/SaveDoctorrequirement")
    Call<ResultInfo> MakeSampleOrder(@Body OrderMaster aInfo);

    @GET("/api/OrderManagement")
    Call<List<mCompanyUnit>> GetCompanyUnit(@Query("empId") int empId);

    //For Notice Done
    @GET("/api/Notice")
    Call<List<Notice>> GetUpdatedNotice(@Query("empId") int empId);

    @GET("/api/Notice/UpdateNoticeBoardReadByEmpId")
    Call<Void> postSeenNotice(@Query("id") int id,@Query("EmpId") int EmpId,@Query("AppSeenDate") String AppSeenDate);

    //For Training
    @GET("/api/Training")
    Call<List<Training>> GetTraining(@Query("empId") int empId);
    @GET("/api/Training/UpdateTrainingReadByEmpId")
    Call<Void> postSeenTraining(@Query("id") int id,@Query("EmpId") int EmpId);

    // For Customer Demo
    @GET("/api/CusDemo/GetOrderInfo")
    Call<OrderMaster> GetOrder_CustomerST(@Query("orderId") int orderId);

    @GET("/api/CusDemo")
    Call<List<OrderViewModel>> GetCustomer_OrderList(@Query("customerCode") String customerCode);

    @GET("/api/OrderTracking/GetMioOrderList")
    Call<List<OrderTrackigMaster>>GetMio_OrderList(@QueryMap Map<String,String> mapparam);

    @GET("/api/CusDemo/ApproveOrderMio")
    Call<ResultInfo> ApproveOrder_fromMio(@Query("orderId") int orderId);


    @GET("/api/CusDemo/RejectOrderMio")
    Call<ResultInfo> RejectOrder_fromMio(@Query("orderId") int orderId);


    //

    @GET("/api/Order/GetMioDashboardQuickSummery")
    Call<Dashboard_SummeryVM> GetMioDashboardSummeryData(@Query("empId") int empId, @Query("currenDate") String currenDate);
    @GET("/api/Order/GetMioDashboardQuickSummery_2")
    Call<Dashboard_SummeryVM> GetTodaySummery(@Query("empId") int empId, @Query("currenDate") String currenDate);


    @GET("/api/CusDemo/GetAttendanceData")
    Call<List<Report_AttendanceViewModel>> GetAttendanceData(@Query("empId") int empId,@Query("startDate") String startDate,@Query("endDate") String endDate);

    @POST("/api/AppPrimary")
    Call<AppVersionModel> GetAppVersionInfo();


    @GET("/api/Notification")
    Call<Integer> GetNotificationCount(@Query("id") int id);

    @GET("/api/Notification/GetNotification")
    Call<List<NotificationViewModel>> GetNotification(@Query("empId") int empId);

    @GET("/api/Notification/UpdateNotificationRead")
    Call<ResultInfo> UpdateNotificationIsRead(@Query("id") int id);

    @POST("/api/User")
    Call<ResultInfo> SaveTracking(@Body UserTracking aInfo);


}
