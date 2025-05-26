package com.creatrix.salessolution.Activity.SelfReports;

import static android.view.View.VISIBLE;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.creatrix.salessolution.Model.TadaList;
import com.creatrix.salessolution.Network.CalculationApiCall;
import com.creatrix.salessolution.Network.RetrofitClientInstance;
import com.creatrix.salessolution.R;
import com.creatrix.salessolution.RecyclerAdapter._tadaListView_Recycler;
import com.creatrix.salessolution.UtilityHelper.SessionManagement;
import com.creatrix.salessolution.UtilityHelper.UtilityHelper;
import com.creatrix.salessolution.databinding.ActivityReportsTadaBinding;
import com.creatrix.salessolution.databinding.FilterMasterBinding;
import com.github.dewinjm.monthyearpicker.MonthYearPickerDialogFragment;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.net.SocketTimeoutException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Reports_TadaActivity extends AppCompatActivity {
    ActivityReportsTadaBinding binding;
    private _tadaListView_Recycler mAdapter;
    ProgressDialog progressDoalog;
    Map<String, String> filter;
    SessionManagement session;


    BottomSheetDialog bsheetdlg;
    FilterMasterBinding ftm;
    String fromdat, todate, status;

    int empid, selectedMonth, selectedyear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityReportsTadaBinding.inflate(getLayoutInflater());
        //setContentView(R.layout.activity_reports__tada);
        setContentView(binding.getRoot());
        binding.toolbarCustom.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        session = new SessionManagement(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        // int userId = Integer.parseInt(user.get(SessionManagement.KEY_UserId));
        empid = Integer.parseInt(user.get(SessionManagement.KEY_EmpId));

        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);

        filter = new HashMap<>();
        filter.put("userId", String.valueOf(empid));
        filter.put("monthValue", String.valueOf(month + 1));
        filter.put("yearValue", String.valueOf(year));
        filter.put("statusTxt", "");
        filter.put("FromDt", "");
        filter.put("ToDt", "");
        // GetReportData(empid,(month + 1),year,filter);
        GetReportData(filter);
        binding.selectedMonthTxt.setText(UtilityHelper.monthNameArrayFull[month] + ',' + year);
        openFilter();
        binding.atteFilter.setOnClickListener(v -> bsheetdlg.show());
    }

    private void openFilter() {
        // FilterMasterBinding ftm;
        ftm = FilterMasterBinding.inflate(getLayoutInflater());
        bsheetdlg = new BottomSheetDialog(Reports_TadaActivity.this);
        bsheetdlg.setContentView(ftm.getRoot());
        // bsheetdlg.setContentView(R.layout.filter_master);
        bsheetdlg.setCanceledOnTouchOutside(true);
        ftm.linearLayoutemp.setVisibility(View.GONE);
        ftm.spinnerEmployeeType.setVisibility(View.GONE);
        ftm.filterMonthYearLayout.fromtomaster.setVisibility(View.GONE);
        ftm.filterTypeLayout.tilProgtypetype.setVisibility(View.GONE);
        ftm.filterTypeLayout.tilDoctype.setVisibility(View.GONE);
        ftm.filterTypeLayout.tilCusttype.setVisibility(View.GONE);
        ftm.filterTypeLayout.tilPharmatype.setVisibility(View.GONE);

        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        selectedyear=year;
        int month = c.get(Calendar.MONTH);
        selectedMonth=month;
        ftm.llmonthyear.setVisibility(VISIBLE);
        ftm.ivmypicker.setOnClickListener(v -> MonthPicker());
        ftm.montyer.setText(UtilityHelper.monthNameArrayFull[month] + ',' + year);
        ftm.filterTopLayout.btnDonefilter.setOnClickListener(v -> {
            status = (String) ftm.filterStatusLayout.spinnerApprovalStatus.getSelectedItem();
            fromdat = ftm.filterMonthYearLayout.txtFromDate.getText().toString();
            todate = ftm.filterMonthYearLayout.txtToDate.getText().toString();
            String filerTxt="";
            if (status.equals("Select")) {
                filerTxt = "";
            } else {
                switch (status) {
                    case "Pending":
                        filerTxt = "0";
                        break;
                    case "Verified":
                        filerTxt = "1";
                        break;
                    case "Approved":
                        filerTxt = "2";
                        break;
                    case "Rejected":
                        filerTxt = "3";
                        break;
                }
            }
            HashMap<String,String> filters = new HashMap<>();
            filters.put("userId", String.valueOf(empid));
            filters.put("monthValue", String.valueOf(selectedMonth+1));
            filters.put("yearValue", String.valueOf(selectedyear));
            filters.put("statusTxt", filerTxt);
            filters.put("FromDt", "");
            filters.put("ToDt", "");
            GetReportData(filters);

            bsheetdlg.cancel();
        });
        ftm.filterTopLayout.ivCancel.setOnClickListener(v ->  bsheetdlg.cancel());
    }

    @SuppressLint("SetTextI18n")
    public void MonthPicker() {
        int yearSelected;
        int monthSelected;
        Calendar calendar = Calendar.getInstance();
        yearSelected = calendar.get(Calendar.YEAR);
        monthSelected = calendar.get(Calendar.MONTH);
        MonthYearPickerDialogFragment dialogFragment = MonthYearPickerDialogFragment
                .getInstance(monthSelected, yearSelected);
        dialogFragment.show(getSupportFragmentManager(), null);

        dialogFragment.setOnDateSetListener((year, monthOfYear) -> {
            ftm.montyer.setText("");
            ftm.montyer.setText(UtilityHelper.monthNameArrayFull[monthOfYear] + ',' + year);
            binding.selectedMonthTxt.setText(UtilityHelper.monthNameArrayFull[monthOfYear] + ',' + year);
            selectedMonth = monthOfYear;
            selectedyear = year;
        });
    }


    public void GetReportData(Map<String, String> filter) {
        // public void GetReportData(Map<String, String> filter) {
        progressDoalog = new ProgressDialog(Reports_TadaActivity.this);
        progressDoalog.setMessage("Report Loading....");
        progressDoalog.show();
        progressDoalog.setCanceledOnTouchOutside(false);
        CalculationApiCall service = RetrofitClientInstance.getRetrofitInstance().create(CalculationApiCall.class);
        Call<List<TadaList>> call = service.GetTadaList(filter);
        call.enqueue(new Callback<List<TadaList>>() {
            @Override
            public void onResponse(Call<List<TadaList>> call, Response<List<TadaList>> response) {
                if(progressDoalog!=null && progressDoalog.isShowing())
                {
                    progressDoalog.dismiss();
                }
                if(response.body()!=null)
                {
                    LoadinView(response.body());
                }

            }

            @Override
            public void onFailure(Call<List<TadaList>> call, Throwable t) {
                if(progressDoalog!=null && progressDoalog.isShowing())
                {
                    progressDoalog.dismiss();
                }
                if (t instanceof SocketTimeoutException) {
                    ErrorView("Slow Connection Detected");
                } else {
                    ErrorView("Some Error Occurred");
                }
            }
        });

    }


    public void LoadinView(List<TadaList> aList) {
        if (aList != null) {
            mAdapter = new _tadaListView_Recycler(Reports_TadaActivity.this,aList);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            binding.recyclerView.setLayoutManager(mLayoutManager);
            binding.recyclerView.setItemAnimator(new DefaultItemAnimator());
            binding.recyclerView.setAdapter(mAdapter);
            binding.recyclerView.setItemAnimator(null);
            binding.recyclerView.scrollToPosition(0);
            mAdapter.notifyDataSetChanged();
        }
    }

    public void ErrorView(String msg) {
        Toast.makeText(Reports_TadaActivity.this, msg, Toast.LENGTH_LONG).show();
    }


    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left,
                R.anim.slide_out_right);
    }
}