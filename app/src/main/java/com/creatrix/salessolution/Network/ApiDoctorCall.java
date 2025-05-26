package com.creatrix.salessolution.Network;

import com.creatrix.salessolution.Activity.Approval.DCR.DcrApprovalData;
import com.creatrix.salessolution.Activity.Approval.DCR.DcrApprovalRQ;
import com.creatrix.salessolution.Activity.Approval.Prescription.PrescApprovalData;
import com.creatrix.salessolution.Activity.Approval.Prescription.PrescApprovalRQ;
import com.creatrix.salessolution.Activity.Approval.TourPlan.Model.TPApprovalSaveBody;
import com.creatrix.salessolution.Activity.Approval.TourPlan.Model.TourPlanApprovalData;
import com.creatrix.salessolution.Activity.Approval.VisitPlan.Model.VPApprovalSaveBody;
import com.creatrix.salessolution.Activity.Approval.VisitPlan.Model.VisitPlanApprovalData;
import com.creatrix.salessolution.Activity.Doctor.Approval.Model.DoctorApprovalList;
import com.creatrix.salessolution.Activity.Doctor.Approval.Model.DoctorApprovalRQ;
import com.creatrix.salessolution.Activity.Doctor.DCR.NonEffectiveReason;
import com.creatrix.salessolution.Activity.Doctor.Pending.DoctorARModel;
import com.creatrix.salessolution.Activity.Doctor.VisitPlan.Model.VisitPlanMaster;
import com.creatrix.salessolution.Activity.Doctor.VisitPlan.Model.VisitplanModel;
import com.creatrix.salessolution.Model.DcrSM;
import com.creatrix.salessolution.Model.DcrVM;
import com.creatrix.salessolution.Model.Doctor.Brand;
import com.creatrix.salessolution.Model.Doctor.ContactTypeVM;
import com.creatrix.salessolution.Model.Doctor.DoctorCategory;
import com.creatrix.salessolution.Model.Doctor.DoctorChamberName;
import com.creatrix.salessolution.Model.Doctor.DoctorChamberTypeVM;
import com.creatrix.salessolution.Model.Doctor.DoctorDegreeViewModel;
import com.creatrix.salessolution.Model.Doctor.DoctorDesignation;
import com.creatrix.salessolution.Model.Doctor.DoctorListViewModel;
import com.creatrix.salessolution.Model.Doctor.DoctorSM;
import com.creatrix.salessolution.Model.Doctor.DoctorSpecialityViewModel;
import com.creatrix.salessolution.Model.Doctor.DoctorTypeVM;
import com.creatrix.salessolution.Model.Doctor.ProgramType;
import com.creatrix.salessolution.Model.Doctor.SpecialDay;
import com.creatrix.salessolution.Model.Gift;
import com.creatrix.salessolution.Model.InstitutionVM;
import com.creatrix.salessolution.Model.ModelProviderType;
import com.creatrix.salessolution.Model.ModelSMCType;
import com.creatrix.salessolution.Model.PrescriptionSM;
import com.creatrix.salessolution.Model.PrescriptionTYpe;
import com.creatrix.salessolution.Model.ResultInfo;
import com.creatrix.salessolution.Model.TourDetailForTADA;
import com.creatrix.salessolution.Model.TourPlanMasterViewModel;
import com.creatrix.salessolution.Model.TourPlanViewModel;
import com.creatrix.salessolution.Model.TourTypeViewModel;
import com.creatrix.salessolution.Model.UserByRole;
import com.creatrix.salessolution.Model.UserRole;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface ApiDoctorCall {
    @Headers({"Accept: application/json"})

    @GET("/api/Doctor/GetDoctorDegree")
    Call<List<DoctorDegreeViewModel>> GetDoctorDegree();

    @GET("/api/Doctor/GetDoctorSpeciality")
    Call<List<DoctorSpecialityViewModel>> GetDoctorSpeciality();

    @GET("/api/Doctor/GetDoctorSpecialDay")
    Call<List<SpecialDay>> GetDoctorSpecialday();

    @GET("/api/Doctor/GetDoctorType")
    Call<List<DoctorTypeVM>> GetDoctorType();

    @GET("/api/Doctor/GetDoctorInstitution")
    Call<List<InstitutionVM>> GetDoctorInstitution();

    @GET("/api/Doctor/GetDoctorChemberType")
    Call<List<DoctorChamberTypeVM>> GetDoctorChamber();

    @GET("/api/Doctor/GetDoctorChemberName")
    Call<List<DoctorChamberName>> GetChemberName(@Query("empId") int empId);

    @GET("/api/Doctor/GetDoctorContactType")
    Call<List<ContactTypeVM>> GetDoctorContactType();

    @GET("/api/Doctor/GetDoctorBrand")
    Call<List<Brand>> GetDoctorBradType();


    @GET("/api/Doctor/GetDoctorProgramType")
    Call<List<ProgramType>> GetDoctorProgramType();

    @GET("/api/Doctor/GeDoctorCategory")
    Call<List<DoctorCategory>> GetDoctorCategory();

    @POST("/api/Doctor")
    Call<ResultInfo> Save(@Body DoctorSM aDoc);

    @GET("/api/Doctor/GetDoctorList")
    Call<List<DoctorListViewModel>> GetDoctorList(@Query("empId") int empId);

    //Approve reject
    @GET("/api/Reports/GetDoctorPendingReject")
    Call<List<DoctorARModel>> GetDoctorApproveRejList(@Query("empId") int empId,@Query("ApprovalStatus") String Status);
    //Approve reject
    @GET("/api/Reports/GetDoctorPendingRejectS")
    Call<List<DoctorARModel>> GetDoctorApproveRejLists(@Query("empId") int empId);


    @GET("/api/Doctor/GetPrescriptionType")
    Call<List<PrescriptionTYpe>> GetPrescriptionType(@Query("empId") int empId);


    @POST("api/Doctor/SavePrescription")
    Call<ResultInfo> SavePrescription(@Body PrescriptionSM aPres);

    @GET("/api/Doctor/GetPrescriptionList")
    Call<List<PrescriptionSM>> GetPrescriptionList(@QueryMap Map<String, String> mapparam);

    @GET("/api/Doctor/GetDoctorDesignation")
    Call<List<DoctorDesignation>> GetDoctorDesignation();

    //DCR
    @GET("/api/Doctor/GetVisitType")
    Call<List<TourTypeViewModel>> GetVisitType();

    //Provider
    @GET("/api/Doctor/GetProviderType")
    Call<List<ModelProviderType>> GetProviderType();

    @GET("/api/Doctor/GetSMCType")
    Call<List<ModelSMCType>> GetSMCType();

    @GET("/api/Doctor/GetDoctorChamberForDCR")
    Call<List<DoctorChamberTypeVM>> GetDoctorChamberDCR(@Query("doctorId") int doctorId);

    //Gift,Product,Sample
    @GET("/api/Doctor/GetProductForDcrByType")
    Call<List<Gift>> GetProductForDcrByType(@Query("empId") int empId, @Query("type") String type);


    @POST("/api/Doctor/SaveDcr")
    Call<ResultInfo> SaveDcr(@Body DcrSM aInfo);

    @GET("/api/Doctor/GetDcrList")
    Call<List<DcrVM>> GetDcrList(@QueryMap Map<String, String> mapparam);//FromDt

    //Approval DCR
    @GET("/api/Doctor/GetDCR_Applog")
    Call<List<DcrApprovalData>> GetDcrListApproval(@Query("pram") String pram, @QueryMap Map<String, String> mapparam);

    @POST("/api/Doctor/SaveDCR_Applog")
    Call<ResultInfo> SaveDcrApproval(@Body DcrApprovalRQ aInfo);

    //Approval Prescription
    @GET("/api/Doctor/GetPrescription_Applog")
    Call<List<PrescApprovalData>> GetPrescListApproval(@Query("pram") String pram, @QueryMap Map<String, String> mapparam);
    @POST("/api/Doctor/SavePrescription_Applog")
    Call<ResultInfo> SavePrescApproval(@Body PrescApprovalRQ aInfo);


    @GET("/api/Doctor/GetNonEffectiveReason")
    Call<List<NonEffectiveReason>> GetNonEffectiveReason();

    @GET("/api/Doctor/GetUserRole")
    Call<List<UserRole>> GetUserRole();

    @GET("/api/Doctor/GetEmpInfoRoleID")
    Call<List<UserByRole>> GetUserByRole(@Query("empId") int empId);

    @GET("/api/Doctor/GetUserRoleByRoleId")
    Call<List<UserByRole>> GetUserRoleByRoleId(@Query("id") int id, @Query("empId") int empId);

    //Doc Tour PLan
    @GET("/api/Doctor/GetDoctorTourPlanDetail")
    Call<List<TourPlanViewModel>> GetDoctorTourPlanData(@Query("month") int month, @Query("year") int year, @Query("empId") int empId);

    //Doctor visitplan data from server-done
    @GET("/api/Doctor/GetDoctorVisitPlanDetail")
    Call<List<VisitplanModel>> GetDoctorVisitPlanData(@Query("month") int month, @Query("year") int year, @Query("empId") int empId);


    @POST("/api/Doctor/SaveDoctorVisitPlan")
    Call<ResultInfo> SaveDoctorVisitPlan(@Body List<VisitplanModel> aInfo);

    @GET("/api/Doctor/GetDoctorVisitPlanMaster")
        // Call<List<TourPlanMasterViewModel>> GetDoctorVisitPlanMaster(@Query("month") int month, @Query("year") int year, @Query("empId") int empId);
    Call<List<VisitPlanMaster>> GetDoctorVisitPlanMaster(@Query("month") int month, @Query("year") int year, @Query("empId") int empId);

    @GET("/api/TourPlan/GetTourPlanMasterData")
    Call<List<TourPlanMasterViewModel>> GetTourPlanMaster(@Query("month") int month, @Query("year") int year, @Query("empId") int empId);

    //Visitplan final submit
    @POST("/api/Doctor/UpdateDoctorTourPlan")
    Call<ResultInfo> UpdateDoctorTourPlan(@Query("empId") int empId, @Query("month") int month, @Query("year") int year, @Query("remarks") String remarks);

    //Delete Doctorvisite Plan
    @POST("/api/Doctor/DeleteTourPlan")
    Call<ResultInfo> DeleteTourPlanData(@Query("id") int id);

/*    @GET("/api/Doctor/GetDoctorTourPlanByDate")
    Call<List<DocPlanInfo>> GetDoctorTourPlanByDate(@Query("tourPlanDate") String tourPlanDate, @Query("empId") int empId);*/

    //TodayTask
    @GET("/api/Doctor/TodaysTask")
    Call<List<TourDetailForTADA>> GetTodayTask(@Query("empId") int empId);

    //Approval
    @GET("/api/Doctor/GetDoctor_Applog")
    Call<List<DoctorApprovalList>> GetDoctorApproval(@Query("pram") String pram, @QueryMap Map<String, String> mapparam);

    @POST("/api/Doctor/SaveDoctor_Applog")
    Call<ResultInfo> SaveDoctorApproval(@Body DoctorApprovalRQ aInfo);



    //Approval Visitplan
    @GET("/api/Doctor/GetDoctorVisitPlan_Applog")
    Call<List<VisitPlanApprovalData>> GetVPApproval(@Query("pram") String pram, @QueryMap Map<String,String> mapparam);

    @POST("/api/Doctor/SaveDoctorVisit_Applog")
    Call<ResultInfo> SaveVPApproval(@Body VPApprovalSaveBody approvalSaveBody);




}
