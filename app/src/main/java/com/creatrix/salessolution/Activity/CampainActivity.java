package com.creatrix.salessolution.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.creatrix.salessolution.Model.Rp_CampainViewModel;
import com.creatrix.salessolution.Model.Rp_StockViewModel;
import com.creatrix.salessolution.Network.APICall_Report_i;
import com.creatrix.salessolution.Network.RetrofitClientInstance;
import com.creatrix.salessolution.R;
import com.creatrix.salessolution.RecyclerAdapter._campaing_DataRecyclerview;
import com.creatrix.salessolution.RecyclerAdapter._stock_details_RecyclerAdapter;
import com.creatrix.salessolution.UtilityHelper.SessionManagement;

import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.List;

import okhttp3.HttpUrl;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CampainActivity extends AppCompatActivity {
    ProgressDialog progressDoalog;
    private RecyclerView recyclerView;
    private _campaing_DataRecyclerview mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_campain);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_custom);
        SwipeRefreshLayout swip_camp = (SwipeRefreshLayout) findViewById(R.id.swip_camp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        SessionManagement session = new SessionManagement(getApplicationContext());
        session.checkLogin();
        HashMap<String, String> user = session.getUserDetails();
        String userName = user.get(SessionManagement.KEY_LoginName);
        int empId = Integer.parseInt(user.get(SessionManagement.KEY_EmpId));
        GetReportData(empId);


        ImageView attRefresh = findViewById(R.id.attRefresh);
        attRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetReportData(empId);
            }
        });
        swip_camp.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swip_camp.setRefreshing(false);
                GetReportData(empId);
            }
        });
    }

    public void GetReportData(int empId) {
        progressDoalog = new ProgressDialog(CampainActivity.this);
        progressDoalog.setIcon(R.drawable.ic_email);
        progressDoalog.setMessage("Campaign is Loading....");
        progressDoalog.show();
        progressDoalog.setCanceledOnTouchOutside(false);
        try {
            APICall_Report_i service = RetrofitClientInstance.getRetrofitInstance().create(APICall_Report_i.class);
            Call<List<Rp_CampainViewModel>> call = service.GetCampaingData(empId);

            HttpUrl ds = call.request().url();
            call.enqueue(new Callback<List<Rp_CampainViewModel>>() {
                @Override
                public void onResponse(Call<List<Rp_CampainViewModel>> call, Response<List<Rp_CampainViewModel>> response) {
                    progressDoalog.dismiss();
                    LoadinView(response.body());
                }

                @Override
                public void onFailure(Call<List<Rp_CampainViewModel>> call, Throwable t) {
                    progressDoalog.dismiss();
                    if (t instanceof SocketTimeoutException) {
                        ErrorView("Slow Connection Detected");
                    } else {
                        ErrorView("Some Error Occurred");
                    }


                }
            });

        } catch (Exception ex) {
            progressDoalog.dismiss();
            ErrorView("Some Error Occurred");

        }

    }

    public void LoadinView(List<Rp_CampainViewModel> aList) {
        if (aList != null) {
            recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
            mAdapter = new _campaing_DataRecyclerview(aList);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(mAdapter);
            recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(),
                    DividerItemDecoration.VERTICAL));
            recyclerView.setItemAnimator(null);
            recyclerView.scrollToPosition(0);
            mAdapter.notifyDataSetChanged();
        }
    }

    public void ErrorView(String msg) {
        Toast.makeText(CampainActivity.this, msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left,
                R.anim.slide_out_right);
    }
}