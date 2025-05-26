package com.creatrix.salessolution.Activity.Doctor.VisitPlan;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.creatrix.salessolution.Activity.Doctor.TourePlan.TourePlanAC;
import com.creatrix.salessolution.Activity.Fragment.HomeFragment;
import com.creatrix.salessolution.Activity.Fragment.MoreFragment;
import com.creatrix.salessolution.Activity.Fragment.ReportFragment;
import com.creatrix.salessolution.Model.ResultInfo;
import com.creatrix.salessolution.Network.ApiDoctorCall;
import com.creatrix.salessolution.Network.RetrofitClientInstance;
import com.creatrix.salessolution.Network.TourApiCall;
import com.creatrix.salessolution.NormalAdapter.TourStatusBottomSheetDialog;
import com.creatrix.salessolution.R;
import com.creatrix.salessolution.Services.Constants;
import com.creatrix.salessolution.UtilityHelper.SnackBarManagement;
import com.creatrix.salessolution.databinding.ActivityDoctorVisitPlanACBinding;

import java.net.SocketTimeoutException;

import okhttp3.HttpUrl;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DoctorVisitPlanAC extends AppCompatActivity implements TourStatusBottomSheetDialog.BottomSheetListener {
    ActivityDoctorVisitPlanACBinding binding;
    FragmentManager fragmentManager;
    ProgressDialog progressDoalog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDoctorVisitPlanACBinding.inflate(getLayoutInflater());
        setContentView(R.layout.activity_doctor_visit_plan_a_c);
        setContentView(binding.getRoot());
        Constants.WHO="DoctorVisitPlanAC";
        if (savedInstanceState == null) {
            fragmentManager = getSupportFragmentManager();
            VisitPlanListFragment vplFragment = new VisitPlanListFragment();
            fragmentManager.beginTransaction().setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left).replace(R.id.fragmentContaineplanr, vplFragment).commit();
        }



    }

    @Override
    public void FinalSubmitClick(int month, int year, int empId, String remarks) {
        try {
            progressDoalog = new ProgressDialog(DoctorVisitPlanAC.this);
            progressDoalog.setMessage("Final Submit Processing....");
            progressDoalog.show();
            progressDoalog.setCanceledOnTouchOutside(false);
            ApiDoctorCall service = RetrofitClientInstance.getRetrofitInstance().create(ApiDoctorCall.class);
            Call<ResultInfo> call = service.UpdateDoctorTourPlan(empId,month,year,remarks);
            HttpUrl ds = call.request().url();
            call.enqueue(new Callback<ResultInfo>() {
                @Override
                public void onResponse(Call<ResultInfo> call, Response<ResultInfo> response) {
                    progressDoalog.dismiss();
                    ResultInfo info = response.body();
                    if (info.getSuccess() == true) {
                        new AlertDialog.Builder(DoctorVisitPlanAC.this)
                                .setTitle("Success")
                                .setMessage("Final Visit Plan submission done for selected month")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent mIntent = getIntent();
                                        startActivity(mIntent);
                                        finish();

                                    }
                                }).setCancelable(false).show();
                    }
                }

                @Override
                public void onFailure(Call<ResultInfo> call, Throwable t) {
                    progressDoalog.dismiss();
                    if (t instanceof SocketTimeoutException) {
                        SnackBarManagement._error_CustomMessage(binding.master, "Some error occurred. Please try again");
                    } else {
                        SnackBarManagement._error_CustomMessage(binding.master, "Some error occurred. Please try again");
                    }
                }
            });

        } catch (Exception ex) {
            progressDoalog.dismiss();
            SnackBarManagement._error_CustomMessage(binding.master, "Some error occurred. Please try again");
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}