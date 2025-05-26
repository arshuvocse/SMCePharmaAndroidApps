package com.creatrix.salessolution.Activity.SelfReports;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import com.creatrix.salessolution.Model.Rp_StockViewModel;
import com.creatrix.salessolution.Network.APICall_Report_i;
import com.creatrix.salessolution.Network.RetrofitClientInstance;
import com.creatrix.salessolution.R;
import com.creatrix.salessolution.RecyclerAdapter._stock_details_RecyclerAdapter;
import com.creatrix.salessolution.UtilityHelper.SessionManagement;
import com.creatrix.salessolution.databinding.ActivityReportsStockBinding;

import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.List;

import okhttp3.HttpUrl;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReportsStockActivity extends AppCompatActivity {
    ActivityReportsStockBinding binding;
//    public List<Rp_StockViewModel> aList = new ArrayList<>();
    private RecyclerView recyclerView;
    private _stock_details_RecyclerAdapter mAdapter;
    ProgressDialog progressDoalog;
    ImageView attRefresh;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityReportsStockBinding.inflate(getLayoutInflater());
        //setContentView(R.layout.activity_reports_stock);
        setContentView(binding.getRoot());
        String from=getIntent().getStringExtra("From");
        binding.toolbarCustom.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        SessionManagement  session = new SessionManagement(getApplicationContext());
       // session.checkLogin();
        HashMap<String, String> user = session.getUserDetails();
        String userName = user.get(SessionManagement.KEY_LoginName);
        int empId = Integer.parseInt(user.get(SessionManagement.KEY_EmpId));
        switch (from)
        {
            case "SampleStock":
                binding.toolbarTitle.setText("Sample Stock Information");
                GetSampleStockData(empId);
                break;
            case "MainStock":
                GetStockData(empId);
                break;
        }
    }
    public void GetStockData(int empId) {
        progressDoalog = new ProgressDialog(ReportsStockActivity.this);
        progressDoalog.setIcon(R.drawable.ic_email);
        progressDoalog.setMessage("Stock is Loading....");
        progressDoalog.show();
        progressDoalog.setCanceledOnTouchOutside(false);
        try{
            APICall_Report_i service = RetrofitClientInstance.getRetrofitInstance().create(APICall_Report_i.class);
            Call<List<Rp_StockViewModel>> call = service.GetStockData(empId,"Main");
            HttpUrl ds = call.request().url();
            call.enqueue(new Callback<List<Rp_StockViewModel>>() {
                @Override
                public void onResponse(Call<List<Rp_StockViewModel>> call, Response<List<Rp_StockViewModel>> response) {
                    progressDoalog.dismiss();
                    LoadinView(response.body());
                }
                @Override
                public void onFailure(Call<List<Rp_StockViewModel>> call, Throwable t) {
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
    private void GetSampleStockData(int empId) {
        progressDoalog = new ProgressDialog(ReportsStockActivity.this);
        progressDoalog.setIcon(R.drawable.ic_email);
        progressDoalog.setMessage("Sample Stock is Loading....");
        progressDoalog.show();
        progressDoalog.setCanceledOnTouchOutside(false);
        try{
            APICall_Report_i service = RetrofitClientInstance.getRetrofitInstance().create(APICall_Report_i.class);
            Call<List<Rp_StockViewModel>> call = service.GetStockData(empId,"Sample");
            HttpUrl ds = call.request().url();
            call.enqueue(new Callback<List<Rp_StockViewModel>>() {
                @Override
                public void onResponse(Call<List<Rp_StockViewModel>> call, Response<List<Rp_StockViewModel>> response) {
                    progressDoalog.dismiss();
                    LoadinView(response.body());
                }
                @Override
                public void onFailure(Call<List<Rp_StockViewModel>> call, Throwable t) {
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

    public void LoadinView(List<Rp_StockViewModel> aList){
        if(aList !=null){
            recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
            mAdapter = new _stock_details_RecyclerAdapter(aList);
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

    public  void ErrorView(String msg){
        Toast.makeText(ReportsStockActivity.this,msg,Toast.LENGTH_LONG).show();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left,
                R.anim.slide_out_right);
    }
}