package com.creatrix.salessolution.Activity.Expense.Report;

import static android.view.View.VISIBLE;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import com.creatrix.salessolution.DBAdapter.DBCrudHelper;
import com.creatrix.salessolution.Model.ExpenseReportViewModel;
import com.creatrix.salessolution.Network.CalculationApiCall;
import com.creatrix.salessolution.Network.RetrofitClientInstance;
import com.creatrix.salessolution.R;
import com.creatrix.salessolution.RecyclerAdapter._expenseClaimReport_Recycler;
import com.creatrix.salessolution.UtilityHelper.SessionManagement;
import com.creatrix.salessolution.UtilityHelper.SnackBarManagement;
import com.creatrix.salessolution.UtilityHelper.UtilityHelper;
import com.creatrix.salessolution.databinding.ActivityExpenseClaimReportsBinding;
import com.creatrix.salessolution.databinding.FilterMasterBinding;
import com.github.dewinjm.monthyearpicker.MonthYearPickerDialogFragment;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.net.SocketTimeoutException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.HttpUrl;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExpenseClaimReportsActivity extends AppCompatActivity {
    ActivityExpenseClaimReportsBinding binding;

    ProgressDialog progressDoalog;
    BottomSheetDialog bsheetdlg;
    String role, selectedTyp = "";
    DBCrudHelper dbCrudHelper;
    int empId;
    String fromdat, todate, status;
    String filerTxt;
    FilterMasterBinding ftm;
    int selectedMonth,selectedyear;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityExpenseClaimReportsBinding.inflate(getLayoutInflater());
      //  setContentView(R.layout.activity_expense_claim_reports);
        setContentView(binding.getRoot());
        ftm = FilterMasterBinding.inflate(getLayoutInflater());
        binding.toolbarCustom.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        dbCrudHelper = new DBCrudHelper(this);
        SessionManagement session = new SessionManagement(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        String userName = user.get(SessionManagement.KEY_LoginName);
        empId = Integer.parseInt(user.get(SessionManagement.KEY_EmpId));
        role = user.get(SessionManagement.KEY_EmpRoleType);

        Calendar c = Calendar.getInstance();
       int year = c.get(Calendar.YEAR);
       int month = c.get(Calendar.MONTH);
        HashMap<String,String> filter=new HashMap<>();
        filter.put("monthValue",String.valueOf(month + 1));
        filter.put("yearValue",String.valueOf(year));
        filter.put("statusTxt","All");
        filter.put("empId",String.valueOf(empId));
        GetReportData(filter);
        binding.selectedMonthTxt.setText(UtilityHelper.monthNameArrayFull[month] + ',' + year);
        openFilter();
        binding.atteFilter.setOnClickListener(v ->bsheetdlg.show());
        binding.swipexp.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                binding.swipexp.setRefreshing(false);
                GetReportData(filter);
            }
        });
    }

    private void GetReportData(HashMap<String, String> filter) {
        progressDoalog = new ProgressDialog(ExpenseClaimReportsActivity.this);
        progressDoalog.setMessage("Report Loading....");
        progressDoalog.show();
        progressDoalog.setCanceledOnTouchOutside(false);
        try {
            CalculationApiCall service = RetrofitClientInstance.getRetrofitInstance().create(CalculationApiCall.class);
            Call<List<ExpenseReportViewModel>> call = service.Get_ExpenseClaimList(filter);
            call.enqueue(new Callback<List<ExpenseReportViewModel>>() {
                @Override
                public void onResponse(Call<List<ExpenseReportViewModel>> call, Response<List<ExpenseReportViewModel>> response) {
                    progressDoalog.dismiss();
                    List<ExpenseReportViewModel> expList=response.body();
                    System.out.println(expList);
                    if(expList!=null && expList.size()>0)
                    {
                        LoadinView(response.body());
                    }else {
                        LoadinView(response.body());
                        SnackBarManagement._warning_CustomMessage(binding.getRoot(), "Report Data Not Found!!");
                    }
                }

                @Override
                public void onFailure(Call<List<ExpenseReportViewModel>> call, Throwable t) {
                    progressDoalog.dismiss();
                    if (t instanceof SocketTimeoutException) {
                        ErrorView("Slow Connection Detected");
                    } else {
                        ErrorView("Some Error Occurred");
                    }
                }
            });
         /*   call.enqueue(new Callback<List<ExpenseReportViewModel>>() {
                @Override
                public void onResponse(Call<List<ExpenseReportViewModel>> call, Response<List<ExpenseReportViewModel>> response) {
                    progressDoalog.dismiss();
                    // Toast.makeText(ExpenseClaimReportsActivity.this, "rp", Toast.LENGTH_SHORT).show();
                    System.out.println("rp"+response.body());
                    List<ExpenseReportViewModel> expList=response.body();
                    if(expList!=null && expList.size()>0)
                    {
                        LoadinView(response.body());
                    }
                }
                @Override
                public void onFailure(Call<List<ExpenseReportViewModel>> call, Throwable t) {
                    progressDoalog.dismiss();
                    if (t instanceof SocketTimeoutException) {
                        ErrorView("Slow Connection Detected");
                    } else {
                        ErrorView("Some Error Occurred");
                    }
                }
            });*/
        } catch (Exception ex) {
            progressDoalog.dismiss();
            ErrorView("Some Error Occurred");
            System.out.println("trace : "+ex.getStackTrace());
            System.out.println("msg : "+ex.getMessage());
        }
    }

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

    public void LoadinView(List<ExpenseReportViewModel> aList) {
        _expenseClaimReport_Recycler mAdapter = new _expenseClaimReport_Recycler(aList,ExpenseClaimReportsActivity.this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        binding.recyclerView.setLayoutManager(mLayoutManager);
        binding.recyclerView.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerView.setAdapter(mAdapter);
            /*recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(),
                    DividerItemDecoration.VERTICAL));*/
        binding.recyclerView.setItemAnimator(null);
        // binding.recyclerView.scrollToPosition(0);
        mAdapter.notifyDataSetChanged();
       /* if (aList!=null&&aList.size()>0) {
           *//* Gson gson=new Gson();
            String exp=gson.toJson(aList.get(0).getApprovalStatus());
            System.out.println("exp self :" +exp);*//*

            _expenseClaimReport_Recycler mAdapter = new _expenseClaimReport_Recycler(aList);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            binding.recyclerView.setLayoutManager(mLayoutManager);
            binding.recyclerView.setItemAnimator(new DefaultItemAnimator());
            binding.recyclerView.setAdapter(mAdapter);
            *//*recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(),
                    DividerItemDecoration.VERTICAL));*//*
            binding.recyclerView.setItemAnimator(null);
           // binding.recyclerView.scrollToPosition(0);
            mAdapter.notifyDataSetChanged();
        }*/
    }

    public void ErrorView(String msg) {
        Toast.makeText(ExpenseClaimReportsActivity.this, msg, Toast.LENGTH_LONG).show();
    }

    private void openFilter() {
       // FilterMasterBinding ftm;
        ftm = FilterMasterBinding.inflate(getLayoutInflater());
        bsheetdlg = new BottomSheetDialog(ExpenseClaimReportsActivity.this);
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
        selectedyear = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        selectedMonth = c.get(Calendar.MONTH);

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
            //GetReportData(empId, month + 1, year, filerTxt);

            HashMap<String,String> filters=new HashMap<>();
            filters.put("monthValue",String.valueOf(selectedMonth + 1));
            filters.put("yearValue",String.valueOf(selectedyear));
            filters.put("statusTxt",filerTxt);
            filters.put("empId",String.valueOf(empId));
            GetReportData(filters);
           // Toast.makeText(this, "empId :"+empId+"month :"+(selectedMonth+1)+"year :"+selectedyear+"filter : "+filters, Toast.LENGTH_SHORT).show();
            bsheetdlg.cancel();
        });
        ftm.filterTopLayout.ivCancel.setOnClickListener(v -> {
            bsheetdlg.cancel();
        });


        //bsheetdlg.show();
    }


    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left,
                R.anim.slide_out_right);
    }


}