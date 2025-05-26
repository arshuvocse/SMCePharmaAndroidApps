package com.creatrix.salessolution.Activity.SelfReports.ExpenseSummery;

import static android.view.View.GONE;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;


import com.creatrix.salessolution.Activity.Expense.Report.ExpenseSummeryData;
import com.creatrix.salessolution.DBAdapter.DBCrudHelper;
import com.creatrix.salessolution.Model.MarketStructure.AssignEmpTable.ASM;
import com.creatrix.salessolution.Model.MarketStructure.AssignEmpTable.MIO;
import com.creatrix.salessolution.Model.MarketStructure.AssignEmpTable.RSM;
import com.creatrix.salessolution.Network.CalculationApiCall;
import com.creatrix.salessolution.Network.RetrofitClientInstance;
import com.creatrix.salessolution.UtilityHelper.SessionManagement;
import com.creatrix.salessolution.UtilityHelper.SnackBarManagement;
import com.creatrix.salessolution.UtilityHelper.UtilityHelper;
import com.creatrix.salessolution.databinding.ActivityExpenseSummeryBinding;
import com.creatrix.salessolution.databinding.FilterMasterBinding;
import com.github.dewinjm.monthyearpicker.MonthYearPickerDialogFragment;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExpenseSummeryActivity extends AppCompatActivity {
    ActivityExpenseSummeryBinding binding;
    ProgressDialog progressDoalog;
    ExpenseSummeryAdapter mAdapter;
    DBCrudHelper dbCrudHelper;
    BottomSheetDialog bsheetdlg;
    FilterMasterBinding ftm;
    String role, empid, selectedempid;
    int selectedMonth, selectedyear;
    String selectedTyp = "";
    List<MIO> mioList;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityExpenseSummeryBinding.inflate(getLayoutInflater());
        // setContentView(R.layout.activity_expense_summery);
        setContentView(binding.getRoot());
        dbCrudHelper = new DBCrudHelper(this);
        binding.toolbarCustom.setNavigationOnClickListener(v -> finish());
        ftm = FilterMasterBinding.inflate(getLayoutInflater());
        SessionManagement session = new SessionManagement(getApplicationContext());
        // session.checkLogin();
        HashMap<String, String> user = session.getUserDetails();
        empid = user.get(SessionManagement.KEY_EmpId);
        role = user.get(SessionManagement.KEY_EmpRoleType);

        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);

        HashMap<String, String> filterz = new HashMap<>();
        filterz.put("Role", role);
        filterz.put("empId", empid);
        filterz.put("Month", String.valueOf(month + 1));
        filterz.put("Year", String.valueOf(year));
        GetReportData(filterz);

        binding.selectedMonthTxt.setText(UtilityHelper.monthNameArrayFull[month] + ',' + year);
        openFilter();
        binding.efilter.setOnClickListener(v -> bsheetdlg.show());
    }

    public void GetReportData(Map<String, String> filter) {
        progressDoalog = new ProgressDialog(ExpenseSummeryActivity.this);
        progressDoalog.setMessage("Report Loading....");
        progressDoalog.show();
        progressDoalog.setCanceledOnTouchOutside(false);
        try {
            CalculationApiCall service = RetrofitClientInstance.getRetrofitInstance().create(CalculationApiCall.class);
            Call<ExpenseSummeryData> call = service.GetExpenseSummery(filter);
            call.enqueue(new Callback<ExpenseSummeryData>() {
                @Override
                public void onResponse(@NonNull Call<ExpenseSummeryData> call, @NonNull Response<ExpenseSummeryData> response) {

                    if (progressDoalog != null && progressDoalog.isShowing()) {
                        progressDoalog.dismiss();
                    }

                    if (response.body() != null) {
                        binding.noData.setVisibility(View.GONE);
                        // LoadinView(response.body().getExpense_Lists());
                        LoadinView(response.body());
                    } else {
                        binding.noData.setVisibility(View.VISIBLE);
                    }

                }

                @Override
                public void onFailure(@NonNull Call<ExpenseSummeryData> call, @NonNull Throwable t) {
                    if (progressDoalog != null && progressDoalog.isShowing()) {
                        progressDoalog.dismiss();
                    }
                    if (t instanceof SocketTimeoutException) {
                        SnackBarManagement._warning_CustomMessage(binding.getRoot(), "Slow Connection Detected");
                    } else {
                        SnackBarManagement._error_CustomMessage(binding.getRoot(), "Some Error Occurred");
                    }
                }
            });

        } catch (Exception ex) {
            if (progressDoalog != null && progressDoalog.isShowing()) {
                progressDoalog.dismiss();
            }
            SnackBarManagement._error_CustomMessage(binding.getRoot(), "Some Error Occurred");

        }
    }

    //@SuppressLint("SetTextI18n")
    private void openFilter() {
        bsheetdlg = new BottomSheetDialog(ExpenseSummeryActivity.this);
        bsheetdlg.setContentView(ftm.getRoot());
        bsheetdlg.setCanceledOnTouchOutside(true);
        switch (role) {
            case "MIO":
                ftm.linearLayoutemp.setVisibility(GONE);
                break;
            case "AM":
            case "DZSM":
            case "NSM":
                ftm.linearLayoutemp.setVisibility(VISIBLE);
                break;
        }
        ftm.filterStatusLayout.getRoot().setVisibility(GONE);
        ftm.filterTypeLayout.tilDoctype.setVisibility(View.GONE);
        ftm.filterTypeLayout.tilProgtypetype.setVisibility(View.GONE);
        ftm.filterTypeLayout.tilCusttype.setVisibility(View.GONE);
        ftm.filterTypeLayout.tilPharmatype.setVisibility(View.GONE);
        ftm.filterMonthYearLayout.getRoot().setVisibility(GONE);



        List<String> emptype = new ArrayList<>();
        switch (role) {
            case "AM":
                emptype.add("Select");
                emptype.add("MIO");
                try {
                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(ExpenseSummeryActivity.this, android.R.layout.simple_spinner_item, emptype);
                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    ftm.spinnerEmployeeType.setAdapter(dataAdapter);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
                break;
            case "DZSM":
                emptype.add("Select");
                emptype.add("MIO");
                emptype.add("AM");
                ArrayAdapter<String> dataAdapterrsm = new ArrayAdapter<>(ExpenseSummeryActivity.this, android.R.layout.simple_spinner_item, emptype);
                dataAdapterrsm.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                ftm.spinnerEmployeeType.setAdapter(dataAdapterrsm);
                break;
            case "NSM":
                emptype.add("Select");
                emptype.add("MIO");
                emptype.add("AM");
                emptype.add("DZSM");
                ArrayAdapter<String> dataAdapternsm = new ArrayAdapter<>(ExpenseSummeryActivity.this, android.R.layout.simple_spinner_item, emptype);
                dataAdapternsm.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                ftm.spinnerEmployeeType.setAdapter(dataAdapternsm);
                break;
        }
        ftm.spinnerEmployeeType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedTyp = String.valueOf(ftm.spinnerEmployeeType.getSelectedItem());
                switch (selectedTyp) {
                    case "MIO":
                        try {
                            ftm.miolay.setVisibility(View.VISIBLE);
                            ftm.asmlay.setVisibility(View.GONE);
                            ftm.rsmlay.setVisibility(View.GONE);
                            role = "MIO";
                            try {
                                mioList = dbCrudHelper.getMIOList_SQLite();
                            } catch (Exception exception) {
                                exception.printStackTrace();
                            }
                            if (mioList != null) {
                                System.out.println("mlist " + mioList);
                                ArrayAdapter<MIO> dataAdapter = new ArrayAdapter<>(ExpenseSummeryActivity.this, android.R.layout.simple_spinner_item, mioList);
                                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                ftm.spinnerApprovalMio.setAdapter(dataAdapter);

                            } else {
                                SnackBarManagement._warning_CustomMessage(binding.masterLayout, "No MIO Found!!");
                            }
                        } catch (Exception exception) {
                            exception.printStackTrace();
                        }
                        break;
                    case "AM":
                        try {
                            ftm.asmlay.setVisibility(View.VISIBLE);
                            ftm.miolay.setVisibility(View.GONE);
                            ftm.rsmlay.setVisibility(View.GONE);
                            role = "AM";
                            List<ASM> asmList = null;
                            try {
                                asmList = dbCrudHelper.getASMList_SQLite();
                            } catch (Exception exception) {
                                exception.printStackTrace();
                            }
                            if (asmList != null) {
                                ArrayAdapter<ASM> dataAdapter = new ArrayAdapter<>(ExpenseSummeryActivity.this, android.R.layout.simple_spinner_item, asmList);
                                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                ftm.spinnerApprovalAsm.setAdapter(dataAdapter);


                            } else {
                                SnackBarManagement._warning_CustomMessage(binding.masterLayout, "No AM Founded!!");
                            }
                        } catch (Exception exception) {
                            exception.printStackTrace();
                        }
                        break;
                    case "DZSM":
                        try {
                            ftm.rsmlay.setVisibility(View.VISIBLE);
                            ftm.miolay.setVisibility(View.GONE);
                            ftm.asmlay.setVisibility(View.GONE);
                            role = "DZSM";
                            List<RSM> rsmList = null;
                            try {
                                rsmList = dbCrudHelper.getRSMList_SQLite();
                            } catch (Exception exception) {
                                exception.printStackTrace();
                            }
                            if (rsmList != null) {
                                ArrayAdapter<RSM> dataAdapter = new ArrayAdapter<>(ExpenseSummeryActivity.this, android.R.layout.simple_spinner_item, rsmList);
                                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                ftm.spinnerApprovalRsm.setAdapter(dataAdapter);


                            } else {
                                SnackBarManagement._warning_CustomMessage(binding.masterLayout, "No AM Founded!!");
                            }
                        } catch (Exception exception) {
                            exception.printStackTrace();
                        }
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        ftm.filterMonthYearLayout.ivDatePickerFromDate.setVisibility(GONE);
        ftm.filterMonthYearLayout.ivDatePickerToDate.setVisibility(GONE);

        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        selectedyear = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        selectedMonth = c.get(Calendar.MONTH);

        ftm.llmonthyear.setVisibility(VISIBLE);
        ftm.ivmypicker.setOnClickListener(v -> MonthPicker());
        ftm.montyer.setText(UtilityHelper.monthNameArrayFull[month] + ',' + year);

        ftm.filterTopLayout.btnDonefilter.setOnClickListener(v -> {
            try {
                switch (role) {
                    case "MIO":
                        MIO mioempid;
                        mioempid = (MIO) ftm.spinnerApprovalMio.getSelectedItem();
                        selectedempid = String.valueOf(mioempid.getMIOEmpId());
                        break;
                    case "AM":
                        ASM amempid = (ASM) ftm.spinnerApprovalAsm.getSelectedItem();
                        selectedempid = String.valueOf(amempid.getASMEmpId());
                        // selectedempId = amempid.getASMEmpId();
                        break;
                    case "DZSM":
                        RSM dzsmempid = (RSM) ftm.spinnerApprovalRsm.getSelectedItem();
                        selectedempid = String.valueOf(dzsmempid.getRSMEmpId());
                        break;
                }
            } catch (Exception ignored) {

            }
            HashMap<String, String> filters = new HashMap<>();
            filters.put("Role", role);
            if (selectedempid == null) {
                filters.put("empId", empid);
            } else {
                filters.put("empId", selectedempid);
            }
            filters.put("Month", String.valueOf(selectedMonth + 1));
            filters.put("Year", String.valueOf(selectedyear));
            Gson gson = new Gson();
            String body = gson.toJson(filters);
            System.out.println(body);
            GetReportData(filters);
            bsheetdlg.cancel();

        });
        ftm.filterTopLayout.ivCancel.setOnClickListener(v -> bsheetdlg.cancel());
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
            selectedMonth = (monthOfYear);
            selectedyear = year;
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    // public void LoadinView(List<ExpenseSummery> aList) {
    public void LoadinView(ExpenseSummeryData body) {
        if (body.getExpense_Lists() != null) {
            binding.grandtotal.setText(body.getGrandTotal());
            binding.allowancesId.setText(body.getAllowences());
            mAdapter = new ExpenseSummeryAdapter(body.getExpense_Lists(), ExpenseSummeryActivity.this);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            binding.rvData.setLayoutManager(mLayoutManager);
            binding.rvData.setItemAnimator(new DefaultItemAnimator());
            binding.rvData.setAdapter(mAdapter);
            binding.rvData.setItemAnimator(null);
            binding.rvData.scrollToPosition(0);
            mAdapter.notifyDataSetChanged();
        }
    }
}