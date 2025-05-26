package com.creatrix.salessolution.Activity.MileageClaim.Report;

import static android.view.View.VISIBLE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.creatrix.salessolution.Model.MilageClaimReport;
import com.creatrix.salessolution.Network.CalculationApiCall;
import com.creatrix.salessolution.Network.RetrofitClientInstance;
import com.creatrix.salessolution.R;
import com.creatrix.salessolution.RecyclerAdapter._mileageClaimList_Recycler;
import com.creatrix.salessolution.UtilityHelper.SessionManagement;
import com.creatrix.salessolution.UtilityHelper.SnackBarManagement;
import com.creatrix.salessolution.UtilityHelper.UtilityHelper;
import com.creatrix.salessolution.databinding.ActivityMileageClaimListBinding;
import com.creatrix.salessolution.databinding.FilterMasterBinding;
import com.github.dewinjm.monthyearpicker.MonthYearPickerDialogFragment;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.net.SocketTimeoutException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Query;

public class MileageClaimListActivity extends AppCompatActivity {
    ActivityMileageClaimListBinding binding;
    TextView selectedMonthTxt;
    ProgressDialog progressDoalog;
    int month, year, empId;
    //Filter
    BottomSheetDialog bsheetdlg;
    FilterMasterBinding ftm;
    String fromdat, todate, status;
    String filerTxt;
    int selectedMonth, selectedyear;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMileageClaimListBinding.inflate(getLayoutInflater());
        //setContentView(R.layout.activity_mileage_claim_list);
        setContentView(binding.getRoot());
        binding.toolbarCustom.setNavigationOnClickListener(v -> finish());
        ftm = FilterMasterBinding.inflate(getLayoutInflater());
        SessionManagement session = new SessionManagement(getApplicationContext());
        //session.checkLogin();
        HashMap<String, String> user = session.getUserDetails();
        empId = Integer.parseInt(Objects.requireNonNull(user.get(SessionManagement.KEY_EmpId)));

        Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);


        HashMap<String, String> filter = new HashMap<>();
        filter.put("monthValue", String.valueOf(month + 1));
        filter.put("yearValue", String.valueOf(year));
        filter.put("statusTxt", "0");
        filter.put("empId", String.valueOf(empId));
        GetReportData(filter);
        binding.selectedMonthTxt.setText(UtilityHelper.monthNameArrayFull[month] + ',' + year);
        openFilter();
        binding.atteFilter.setOnClickListener(v -> bsheetdlg.show());
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

    // public void GetReportData(int empId,int month,int year,String filerTxt) {
    public void GetReportData(HashMap<String, String> map) {
        progressDoalog = new ProgressDialog(MileageClaimListActivity.this);
        progressDoalog.setMessage("Report Loading....");
        progressDoalog.show();
        progressDoalog.setCanceledOnTouchOutside(false);
        try {
            CalculationApiCall service = RetrofitClientInstance.getRetrofitInstance().create(CalculationApiCall.class);
            // Call<List<MilageClaimReport>> call = service.GetMileageCLaimList(month,year,filerTxt,empId);
            Call<List<MilageClaimReport>> call = service.GetMileageCLaimList(map);
            call.enqueue(new Callback<List<MilageClaimReport>>() {
                @Override
                public void onResponse(@NonNull Call<List<MilageClaimReport>> call, @NonNull Response<List<MilageClaimReport>> response) {
                    progressDoalog.dismiss();
                    LoadinView(response.body());
                }

                @Override
                public void onFailure(@NonNull Call<List<MilageClaimReport>> call, @NonNull Throwable t) {
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

    public void LoadinView(List<MilageClaimReport> aList) {
        if (aList != null) {
            _mileageClaimList_Recycler mAdapter = new _mileageClaimList_Recycler(aList);
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
        SnackBarManagement._error_CustomMessage(binding.masterLayout, msg);
    }

    private void openFilter() {
       /* FilterMasterBinding ftm;
        ftm = FilterMasterBinding.inflate(getLayoutInflater());*/
        bsheetdlg = new BottomSheetDialog(MileageClaimListActivity.this);
        bsheetdlg.setContentView(ftm.getRoot());
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
        selectedyear = year;
        int month = c.get(Calendar.MONTH);
        selectedMonth = month;
        ftm.llmonthyear.setVisibility(VISIBLE);
        ftm.ivmypicker.setOnClickListener(v -> MonthPicker());
        ftm.montyer.setText(UtilityHelper.monthNameArrayFull[month] + ',' + year);

        ftm.filterTopLayout.btnDonefilter.setOnClickListener(v -> {
            status = (String) ftm.filterStatusLayout.spinnerApprovalStatus.getSelectedItem();
            fromdat = ftm.filterMonthYearLayout.txtFromDate.getText().toString();
            todate = ftm.filterMonthYearLayout.txtToDate.getText().toString();
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
            HashMap<String, String> search = new HashMap<>();
            search.put("monthValue", String.valueOf(selectedMonth + 1));
            search.put("yearValue", String.valueOf(selectedyear));
            search.put("statusTxt", filerTxt);
            search.put("empId", String.valueOf(empId));
            GetReportData(search);

            bsheetdlg.cancel();
        });

        ftm.filterTopLayout.ivCancel.setOnClickListener(v ->
                bsheetdlg.cancel());


    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left,
                R.anim.slide_out_right);
    }
}