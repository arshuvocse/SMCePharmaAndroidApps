package com.creatrix.salessolution.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.creatrix.salessolution.Model.DcpCcpData;
import com.creatrix.salessolution.Model.Doctor.DocPlanInfo;
import com.creatrix.salessolution.Model.Section;
import com.creatrix.salessolution.Model.TourDetailForTADA;
import com.creatrix.salessolution.Network.RetrofitClientInstance;
import com.creatrix.salessolution.Network.ApiDoctorCall;
import com.creatrix.salessolution.R;
import com.creatrix.salessolution.RecyclerAdapter.DcpCcpDynamicSectionAdapter;
import com.creatrix.salessolution.RecyclerAdapter.DcpCcpSectionAdapter;
import com.creatrix.salessolution.RecyclerAdapter._dcpccpdaysTask_Recycler;
import com.creatrix.salessolution.RecyclerAdapter._doctorplanTodaysTask_Recycler;
import com.creatrix.salessolution.UtilityHelper.SessionManagement;
import com.creatrix.salessolution.UtilityHelper.UtilityHelper;
import com.creatrix.salessolution.databinding.ActivityTodaysTaskBinding;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import okhttp3.HttpUrl;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TodaysTaskActivity extends AppCompatActivity {
    ActivityTodaysTaskBinding binding;
    ProgressDialog progressDoalog;
    private _doctorplanTodaysTask_Recycler mAdapter;
    private _dcpccpdaysTask_Recycler mAdapterDcpCcp;

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

   //     GetReportData(cDate,empId);
        GetReportDataDCPCCP(empId);
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

    public void GetReportDataDCPCCP(int empId) {
        progressDoalog = new ProgressDialog(TodaysTaskActivity.this);
        progressDoalog.setMessage("Loading....");
        progressDoalog.setCanceledOnTouchOutside(false);
        progressDoalog.show();

        try {
            ApiDoctorCall service = RetrofitClientInstance
                    .getRetrofitInstance()
                    .create(ApiDoctorCall.class);

            Call<List<DcpCcpData>> call = service.GetTodaysTaskDCPCCP(empId);

            call.enqueue(new Callback<List<DcpCcpData>>() {
                @Override
                public void onResponse(Call<List<DcpCcpData>> call, Response<List<DcpCcpData>> response) {
                    // activity মারা গেলে dialog dismiss করতে গেলে আবার ক্র্যাশ হতে পারে
                    if (!isFinishing() && progressDoalog != null && progressDoalog.isShowing()) {
                        progressDoalog.dismiss();
                    }

                    // 1) response successful নাও হতে পারে
                    if (!response.isSuccessful()) {
                        ErrorView("Server error: " + response.code());
                        LoadinViewDcpCCp(null);
                        return;
                    }

                    // 2) body null হতে পারে
                    List<DcpCcpData> data = response.body();
                    LoadinViewDcpCCp(data);
                }

                @Override
                public void onFailure(Call<List<DcpCcpData>> call, Throwable t) {
                    if (!isFinishing() && progressDoalog != null && progressDoalog.isShowing()) {
                        progressDoalog.dismiss();
                    }

                    if (t instanceof java.net.SocketTimeoutException) {
                        ErrorView("Slow Connection Detected");
                    } else {
                        ErrorView("Some Error Occurred");
                    }

                    // view clean করে দেই
                    LoadinViewDcpCCp(null);
                }
            });

        } catch (Exception ex) {
            if (!isFinishing() && progressDoalog != null && progressDoalog.isShowing()) {
                progressDoalog.dismiss();
            }
            ErrorView("Some Error Occurred");
            LoadinViewDcpCCp(null);
        }
    }

    public void LoadinViewDcpCCp(List<DcpCcpData> aList) {
        // null / empty হলে safe
        if (aList == null || aList.isEmpty()) {
            binding.docTaskCount.setText("0");
            binding.recyclerView.setAdapter(null);
            return;
        }

        binding.docTaskCount.setText(String.valueOf(aList.size()));

        // 🚫 getApplicationContext() না, activity context ব্যবহার করি
        LinearLayoutManager lm = new LinearLayoutManager(TodaysTaskActivity.this);
        binding.recyclerView.setLayoutManager(lm);
        binding.recyclerView.setItemAnimator(new DefaultItemAnimator());

        // section বানানোর আগে try-catch দিলে আরেক লেভেলের সেফটি
        List<Section> sections;
        try {
            sections = buildSections(aList);
        } catch (Exception e) {
            // কোনো কারণে section build ফেইল করলে পুরো app যেন না বের হয়
            sections = new ArrayList<>();
        }

        DcpCcpDynamicSectionAdapter adapter =
                new DcpCcpDynamicSectionAdapter(sections, (typeName, item) -> {
                    if ("DCP".equalsIgnoreCase(typeName)) {
                        // TODO: call Make DCR API
                    } else if ("CVP".equalsIgnoreCase(typeName) || "CCP".equalsIgnoreCase(typeName)) {
                        // TODO: call Make CCP API
                    } else {
                        // TODO: generic flow
                    }
                });

        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.scrollToPosition(0);
    }


    private List<Section> buildSections(List<DcpCcpData> all) {
        if (all == null || all.isEmpty()) return new ArrayList<>();

        // Preserve insertion order
        Map<String, List<DcpCcpData>> map = new LinkedHashMap<>();

        for (DcpCcpData x : all) {
            String raw = (x.getTypeName() == null ? "" : x.getTypeName()).trim();
            String key = raw.isEmpty() ? "Others" : raw.toUpperCase(); // normalize
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                map.computeIfAbsent(key, k -> new ArrayList<>()).add(x);
            }
        }

        // Custom order: DCP first, CCP second, then rest alphabetically
        List<String> keys = new ArrayList<>(map.keySet());
        Collections.sort(keys, (a, b) -> {
            if (a.equals("DCP") && !b.equals("DCP")) return -1;
            if (b.equals("DCP") && !a.equals("DCP")) return 1;
            if (a.equals("CVP") && !b.equals("CVP")) return -1;
            if (b.equals("CVP") && !a.equals("CVP")) return 1;
            return a.compareTo(b);
        });

        List<Section> sections = new ArrayList<>();
        for (String k : keys) sections.add(new Section(k, map.get(k)));
        return sections;
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