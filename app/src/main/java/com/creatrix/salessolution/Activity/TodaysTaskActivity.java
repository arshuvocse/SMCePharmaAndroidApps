package com.creatrix.salessolution.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.creatrix.salessolution.Model.Doctor.DocPlanInfo;
import com.creatrix.salessolution.Model.TourDetailForTADA;
import com.creatrix.salessolution.Network.RetrofitClientInstance;
import com.creatrix.salessolution.Network.ApiDoctorCall;
import com.creatrix.salessolution.R;
import com.creatrix.salessolution.RecyclerAdapter._doctorplanTodaysTask_Recycler;
import com.creatrix.salessolution.UtilityHelper.SessionManagement;
import com.creatrix.salessolution.UtilityHelper.UtilityHelper;
import com.creatrix.salessolution.databinding.ActivityTodaysTaskBinding;

import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.List;

import okhttp3.HttpUrl;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TodaysTaskActivity extends AppCompatActivity {
    ActivityTodaysTaskBinding binding;
    ProgressDialog progressDoalog;
    private _doctorplanTodaysTask_Recycler mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityTodaysTaskBinding.inflate(getLayoutInflater());
        //setContentView(R.layout.activity_todays_task);
        setContentView(binding.getRoot());
        binding.toolbarCustom.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        SessionManagement session = new SessionManagement(getApplicationContext());
        session.checkLogin();
        HashMap<String, String> user = session.getUserDetails();
        int empId = Integer.parseInt(user.get(SessionManagement.KEY_EmpId));
        String cDate = UtilityHelper._GetCurrentDate();

        GetReportData(cDate,empId);
    }


    public void GetReportData(String cDate,int empId) {
        progressDoalog = new ProgressDialog(TodaysTaskActivity.this);
        progressDoalog.setMessage("Loading....");
        progressDoalog.show();
        progressDoalog.setCanceledOnTouchOutside(false);
        try{
            ApiDoctorCall service = RetrofitClientInstance.getRetrofitInstance().create(ApiDoctorCall.class);
          //  Call<List<DocPlanInfo>> call = service.GetTodayTask(empId);
            Call<List<TourDetailForTADA>> call = service.GetTodayTask(empId);
            HttpUrl ds = call.request().url();
            call.enqueue(new Callback<List<TourDetailForTADA>>() {
                @Override
                public void onResponse(Call<List<TourDetailForTADA>> call, Response<List<TourDetailForTADA>> response) {
                    progressDoalog.dismiss();
                    LoadinView(response.body());
                }
                @Override
                public void onFailure(Call<List<TourDetailForTADA>> call, Throwable t) {
                    progressDoalog.dismiss();
                    if(t instanceof SocketTimeoutException){
                        ErrorView("Slow Connection Detected");
                    }else{
                        ErrorView("Some Error Occurred");
                    }
                }
            });

        }catch (Exception ex){
            progressDoalog.dismiss();
            ErrorView("Some Error Occurred");
        }
    }
    public void LoadinView(List<TourDetailForTADA> aList){
        if(aList != null){
            int countS = aList.size();
            binding.docTaskCount.setText(Integer.toString(countS));


            mAdapter = new _doctorplanTodaysTask_Recycler(aList);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            binding.recyclerView.setLayoutManager(mLayoutManager);
            binding.recyclerView.setItemAnimator(new DefaultItemAnimator());
            binding.recyclerView.setAdapter(mAdapter);
            binding.recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(),
                    DividerItemDecoration.VERTICAL));
            binding.recyclerView.setItemAnimator(null);
            binding.recyclerView.scrollToPosition(0);
            mAdapter.notifyDataSetChanged();
        }
    }



    public  void ErrorView(String msg){
        Toast.makeText(TodaysTaskActivity.this,msg,Toast.LENGTH_LONG).show();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left,
                R.anim.slide_out_right);
    }
}