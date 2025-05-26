package com.creatrix.salessolution.Network;
import com.creatrix.salessolution.Activity.Approval.DCR.DcrApprovalData;
import com.creatrix.salessolution.Activity.Approval.DCR.DcrApprovalRQ;
import com.creatrix.salessolution.Activity.Approval.Leave.LeaveApprovalData;
import com.creatrix.salessolution.Activity.Approval.Leave.LeaveApprovalRQ;
import com.creatrix.salessolution.Activity.Reports.Model.ModelDWSPReport;
import com.creatrix.salessolution.Activity.Team.Model.Team;
import com.creatrix.salessolution.Model.LeaveSM;
import com.creatrix.salessolution.Model.LeaveTypeInfo;
import com.creatrix.salessolution.Model.LeaveVM;
import com.creatrix.salessolution.Model.ResultInfo;


import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface UserProcessAPI {
    @Headers({"Accept: application/json"})
    @GET("/api/Leave/GetLeaveType")
    Call<List<LeaveTypeInfo>> GetLeaveType(@Query("empId") int empId, @Query("year") int year);

    @POST("/api/Leave/SaveLeave")
    Call<ResultInfo> SaveLeave(@Body LeaveSM aInfo);

    @GET("/api/Leave/GetLeaveRecords")
    Call<List<LeaveVM>> GetLeaveRecords(@Query("empId") int empId,@Query("Month") String Month, @Query("year") int year);

    /*http://45.64.134.85:570/api/StructureSync/GetEmpTeamList?role=AM&id=357&aparam=*/
    @GET("/api/StructureSync/GetEmpTeamList")
    Call<List<Team>> GetTeamList(@Query("id") int id,@QueryMap Map<String,String> mapparam);

    //Approval Leave
    @GET("/api/Leave/GetLeave_AppLog")
    Call<List<LeaveApprovalData>> GetLeaveListApproval(@Query("pram") String pram, @QueryMap Map<String, String> mapparam);

    @POST("/api/Leave/SaveLeave_AppLog")
    Call<ResultInfo> SaveLeaveApproval(@Body LeaveApprovalRQ aInfo);

    @POST("/api/Reports/ChangePassword")
    Call<ResultInfo> ChangePass(@QueryMap Map<String,String> mapparam);

    @GET("/api/Reports/GetDWSPReport_Emp")
    Call<List<ModelDWSPReport>> GetDWSPReport(@QueryMap Map<String, String> mapparam);



}
