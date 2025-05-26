package com.creatrix.salessolution.Network;

import com.creatrix.salessolution.Activity.Attendance.Model.ApproveRQ;
import com.creatrix.salessolution.Activity.Attendance.Model.AttenApproval;
import com.creatrix.salessolution.Activity.Attendance.Model.ButtonRP;
import com.creatrix.salessolution.Model.Attendance;
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

public interface AttendanceApi {
    @Headers({"Accept: application/json"})
    @POST("/api/Attendance/SavePunch")
    Call<ResultInfo> SavePunInfo(@Body Attendance aInfo);
   // http://45.64.134.85:570/api/AttendanceApi/GetAttendanceInformation_New?pram=AND View_Webapi_EmployeeFieldForceInfo.EmpRegionId=5&Role=RSM&AppStatus=&AttType=2&FromDt=&ToDt=&EmpId=7
    @GET("/api/AttendanceApi/GetAttendanceInformation_New")
    Call<List<AttenApproval>> GetAttenInfoNew(@Query("pram") String pram, @QueryMap Map<String,String> mapparam);

    @GET("/api/AttendanceApi/GetAttendanceInformation")
    Call<List<AttenApproval>> GetAttenInfo(@Query("pram") String pram, @Query("emprole") String emprole);//tblApprovalLog.AreaId='17'

    @POST("/api/AttendanceApi/SaveAttendanceLog")
    Call<ResultInfo> ApproveApi(@Body ApproveRQ approveRQ);

    @POST("/api/Attendance/GetPunchInOutInfo")
    Call<ButtonRP> GetButtonStatus(@Query("EmpInfoId") int EmpInfoId);

}
