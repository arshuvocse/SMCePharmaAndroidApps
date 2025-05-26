package com.creatrix.salessolution.Activity.SelfReports;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.creatrix.salessolution.Activity.Approval.Prescription.PrescriptionApprovalListActivity;
import com.creatrix.salessolution.Activity.Reports.Adapter.AdapterTVAReport;
import com.creatrix.salessolution.Activity.SelfReports.TVAReport.Model.ModelTvaData;
import com.creatrix.salessolution.Model.Rp_TargetAcchivment;
import com.creatrix.salessolution.Network.APICall_Report_i;
import com.creatrix.salessolution.Network.RetrofitClientInstance;
import com.creatrix.salessolution.R;
import com.creatrix.salessolution.RecyclerAdapter._targetAcchivementProductList_Recycler;
import com.creatrix.salessolution.UtilityHelper.SessionManagement;
import com.creatrix.salessolution.UtilityHelper.SnackBarManagement;
import com.creatrix.salessolution.UtilityHelper.UtilityHelper;
import com.creatrix.salessolution.databinding.ActivityReportTvaactivityBinding;

import java.net.SocketTimeoutException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

import okhttp3.HttpUrl;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReportTVAActivity extends AppCompatActivity {
    ActivityReportTvaactivityBinding binding;
    ProgressDialog pd;
    SessionManagement session;
    String RoleType;
    int empid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityReportTvaactivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.toolbarCustom.setNavigationOnClickListener(v -> finish());
        session=new SessionManagement(ReportTVAActivity.this);
        HashMap<String, String> user = session.getUserDetails();
        empid = Integer.parseInt(user.get(SessionManagement.KEY_EmpId));
        RoleType = user.get(SessionManagement.KEY_EmpRoleType);
        /* binding.ivDatePickerFromDate.setOnClickListener(v -> {
            UtilityHelper._datePickerDialogeForDates(binding.txtFromDate, ReportTVAActivity.this);
        });
        binding.ivDatePickerToDate.setOnClickListener(v -> {
            UtilityHelper._datePickerDialogeForDates(binding.txtToDate, ReportTVAActivity.this);
        });*/



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LocalDate todaydate = LocalDate.now();
            System.out.println("Months first date in yyyy-mm-dd: " +todaydate.withDayOfMonth(1));
            todaydate.lengthOfMonth();
            binding.txtFromDate.setText(String.valueOf(todaydate.withDayOfMonth(1)));
            binding.txtToDate.setText(String.valueOf(todaydate.withDayOfMonth(todaydate.lengthOfMonth())));

            /*Toast.makeText(this, "Months first date in yyyy-mm-dd: " +todaydate.withDayOfMonth(1), Toast.LENGTH_SHORT).show();
            Toast.makeText(this, "Months last date in yyyy-mm-dd: " +todaydate.withDayOfMonth(todaydate.lengthOfMonth()), Toast.LENGTH_SHORT).show();*/
        }
        HashMap<String,String> map=new HashMap<>();
        map.put("empId",String.valueOf(empid));
        map.put("FromDate",binding.txtFromDate.getText().toString());
        map.put("ToDate",binding.txtToDate.getText().toString());
        map.put("Role",RoleType);
        pd=new ProgressDialog(ReportTVAActivity.this);
        pd.setCancelable(false);
        pd.setTitle("Report Preparing");
        pd.setMessage("Please wait...");
        pd.show();
        GetReportData(map);

  /*      binding.btnSearch.setOnClickListener(v -> {
           if(!TextUtils.isEmpty(binding.txtFromDate.getText().toString()) && !TextUtils.isEmpty(binding.txtToDate.getText().toString()))
           {
               HashMap<String,String> map=new HashMap<>();
               map.put("empId",String.valueOf(empid));
               map.put("FromDate",binding.txtFromDate.getText().toString());
               map.put("ToDate",binding.txtToDate.getText().toString());
               map.put("Role",RoleType);
               pd=new ProgressDialog(ReportTVAActivity.this);
               pd.setCancelable(false);
               pd.setTitle("Report Preparing");
               pd.setMessage("Please wait...");
               pd.show();
               GetReportData(map);
           }else {
               SnackBarManagement._warning_CustomMessage(binding.getRoot(),"From/To Data Can't be empty");
           }
        });*/
    }

    public void GetReportData(HashMap<String, String> map) {
        /*pd = new ProgressDialog(ReportTVAActivity.this);
        pd.setMessage("Report is Loading....");
        pd.show();
        pd.setCanceledOnTouchOutside(false);*/
        try {
            APICall_Report_i service = RetrofitClientInstance.getRetrofitInstance().create(APICall_Report_i.class);
            Call<ModelTvaData> call = service.GetTeriTarvsAch(map);
            call.enqueue(new Callback<ModelTvaData>() {
                @Override
                public void onResponse(@NonNull Call<ModelTvaData> call, @NonNull Response<ModelTvaData> response) {
                    if (pd != null || pd.isShowing()) {
                        pd.dismiss();
                    }
                    LoadinView(response.body());
                }
                @Override
                public void onFailure(@NonNull Call<ModelTvaData> call, @NonNull Throwable t) {
                    if (pd != null || pd.isShowing()) {
                        pd.dismiss();
                    }
                    SnackBarManagement._error_CustomMessage(binding.getRoot(),"Failed");
                }
            });

        } catch (Exception ex) {
            Log.d("TAG", "GetReportData: "+ex.getMessage());
            Log.d("TAG", "GetReportData: "+ex.getLocalizedMessage());
        }

    }

    public void LoadinView(ModelTvaData data) {
        if (data.getTVADaos() != null) {
            MasterView(data);


            AdapterTVAReport mAdapter = new AdapterTVAReport(data.getTVADaos(), ReportTVAActivity.this);
            binding.rvReport.setLayoutManager(new LinearLayoutManager(ReportTVAActivity.this));
            binding.rvReport.setItemAnimator(new DefaultItemAnimator());
            binding.rvReport.setAdapter(mAdapter);
            binding.rvReport.setItemAnimator(null);
            binding.rvReport.scrollToPosition(0);
            mAdapter.notifyDataSetChanged();
        }
    }

    private void MasterView(ModelTvaData data) {
        binding.target.setText(data.getT_TargetValue());
        binding.order.setText(data.getT_OrderValue());
        binding.orderAch.setText(data.getT_OrderAchiv());
        binding.invoice.setText(data.getT_InvoiceValue());
        binding.invAch.setText(data.getT_InvoiceAchiv());
        binding.sales.setText(data.getT_SalesValue());
        binding.salesAch.setText(data.getT_SalesAchiv());
    }
}