package com.creatrix.salessolution.Activity.Approval.DA;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.creatrix.salessolution.Activity.DA.DA_ApprovList_Adapter;
import com.creatrix.salessolution.Activity.DA.EmpTotalModel;
import com.creatrix.salessolution.DBAdapter.DBCrudHelper;
import com.creatrix.salessolution.Interface.EmployeeWiseTotalCountListener;
import com.creatrix.salessolution.Interface.IDATeam;
import com.creatrix.salessolution.Model.EmpTotalCountModel;
import com.creatrix.salessolution.Model.MarketStructure.AssignEmpTable.ASM;
import com.creatrix.salessolution.Model.MarketStructure.AssignEmpTable.MIO;
import com.creatrix.salessolution.Model.MarketStructure.AssignEmpTable.RSM;
import com.creatrix.salessolution.Model.Order.OrderTrackigMaster;
import com.creatrix.salessolution.Network.APICall_Report_i;
import com.creatrix.salessolution.Network.ApiCustomerCall;
import com.creatrix.salessolution.Network.CalculationApiCall;
import com.creatrix.salessolution.Network.GetDataService;
import com.creatrix.salessolution.Network.RetrofitClientInstance;
import com.creatrix.salessolution.Network.RetrofitClientInstanceTracking;
import com.creatrix.salessolution.Presenter.DATeamPresenter;
import com.creatrix.salessolution.R;
import com.creatrix.salessolution.Services.Constants;
import com.creatrix.salessolution.UtilityHelper.SessionManagement;
import com.creatrix.salessolution.UtilityHelper.SnackBarManagement;
import com.creatrix.salessolution.UtilityHelper.UtilityHelper;
import com.creatrix.salessolution.databinding.ActivityTeamDAListBinding;
import com.creatrix.salessolution.databinding.FilterMasterBinding;
import com.github.dewinjm.monthyearpicker.MonthYearPickerDialogFragment;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;

import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import okhttp3.HttpUrl;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TeamDAListActivity extends AppCompatActivity implements IDATeam.View, EmployeeWiseTotalCountListener, DA_ApprovList_Adapter.approval {
    ActivityTeamDAListBinding binding;
    FilterMasterBinding ftm;
    SessionManagement session;
    DATeamPresenter presenter;
    DBCrudHelper dbCrudHelper;
    DA_ApprovList_Adapter daAdapter;
    ProgressDialog pd,pd1,pd2;
    String empid,status="";
    String fromdat, todate,role,RoleType, params, tagA, tagR, tagN, Areaid, Regionid, Groupid,selectedTyp="", empId="";
    int RoleTypeId;
    int selectedMonth, selectedyear;
    BottomSheetDialog bsheetdlg;
    List<MIO> mioList;
    int month,year;
    Dialog popup,popComment;
    TextView torder,tdcr,trx;
    String selectedMonthz="";
    Button submitCmnt;
    private int next = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTeamDAListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        pd=new ProgressDialog(this);
        popup=new Dialog(TeamDAListActivity.this);
        pd2=new ProgressDialog(TeamDAListActivity.this);
        //Toast.makeText(this, "Create", Toast.LENGTH_SHORT).show();
        ftm = FilterMasterBinding.inflate(getLayoutInflater());
        presenter=new DATeamPresenter(this,TeamDAListActivity.this);
        binding.toolbarCustom.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        session = new SessionManagement(getApplicationContext());
       // session.checkLogin();
        HashMap<String, String> user = session.getUserDetails();
        empid = user.get(SessionManagement.KEY_EmpId);
        RoleTypeId = Integer.parseInt(user.get(SessionManagement.KEY_EmpRoleTypeId));

        //RoleType = user.get(SessionManagement.KEY_EmpRoleType);
        role = user.get(SessionManagement.KEY_EmpRoleType);
        Calendar c = Calendar.getInstance();
         year = c.get(Calendar.YEAR);
         month = c.get(Calendar.MONTH);
       popup_empTotal();
        binding.selectedMonthTxt.setText(UtilityHelper.monthNameArrayFull[month] + ',' + year);
        try {
            tagA = ".EmpAreaId=";
            tagR = ".EmpRegionId=";
            tagN = ".EmpGroupId=";
            dbCrudHelper = new DBCrudHelper(this);
            switch (role) {
                case "AM":
                    Areaid = String.valueOf(dbCrudHelper.getCurrentUserAreaId_SQLite());
                    params = "AND View_Webapi_EmployeeFieldForceInfo" + tagA + Areaid;
                    break;
                case "DZSM":
                    Regionid = dbCrudHelper.getCurrentUserRegionIds_SQLite(String.valueOf(empid));
//                    if (!TextUtils.isEmpty(Regionid) && Regionid.contains(",")) {
//                        params = "AND View_Webapi_EmployeeFieldForceInfo.EmpRegionId in (" + Regionid + ")";
//                    } else
                    {
                        params = "AND View_Webapi_EmployeeFieldForceInfo" + tagR + Regionid;
                    }
                    break;
                case "NSM":
                    Groupid = String.valueOf(dbCrudHelper.getCurrentUserGroupId_SQLite());
                    params = "AND View_Webapi_EmployeeFieldForceInfo" + tagN + Groupid;
                    break;
                case "Admin":
                    params = "";
                    break;
            }
            selectedMonthz=String.valueOf(month + 1);
            HashMap<String,String > filter = new HashMap<>();
            filter.put("Role", role);
            filter.put("AppStatus", "0");
            filter.put("FromDt", "");
            filter.put("ToDt", "");
            filter.put("EmpId", "");
            filter.put("Month", String.valueOf(month + 1));
            filter.put("Year", String.valueOf(year));

            Constants.filtermap=filter;
            Constants.filterparams=params;

            hitApi(params,filter);
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        binding.swip.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                HashMap<String,String > filter = new HashMap<>();
                filter.put("Role", role);
                filter.put("AppStatus", "0");
                filter.put("FromDt", "");
                filter.put("ToDt", "");
                filter.put("EmpId", "");
                filter.put("Month", String.valueOf(month + 1));
                filter.put("Year", String.valueOf(year));
                hitApi(params,filter);
               // presenter.GetTeamDAList(params,filter);
                binding.swip.setRefreshing(false);
            }
        });
        openFilter();
        binding.teamdaFilter.setOnClickListener(v -> {
            bsheetdlg.show();
        });
    }

    private void hitApi(String params, Map<String, String> filter) {
        pd.setMessage("Loading..");
        pd.show();
        presenter.GetTeamDAList(params, filter);
    }

    /*    public void MonthPicker() {
            int yearSelected;
            int monthSelected;
            Calendar calendar = Calendar.getInstance();
            yearSelected = calendar.get(Calendar.YEAR);
            monthSelected = calendar.get(Calendar.MONTH);
            MonthYearPickerDialogFragment dialogFragment = MonthYearPickerDialogFragment
                    .getInstance(monthSelected, yearSelected);
            dialogFragment.show(getSupportFragmentManager(), null);

            dialogFragment.setOnDateSetListener((year, monthOfYear) -> {
                binding.selectedMonthTxt.setText("");
                binding.selectedMonthTxt.setText(UtilityHelper.monthNameArrayFull[monthOfYear] + ',' + year);
                filter = new HashMap<>();
                filter.put("statusTxt", "");
                filter.put("ToDt", "");
                filter.put("FromDt", "");
                presenter.GetTeamDAList(params, filter);
            });
        }
        private void openFilter() {
            FilterMasterBinding ftm;
            ftm = FilterMasterBinding.inflate(getLayoutInflater());
            bsheetdlg = new BottomSheetDialog(TeamDAListActivity.this);
            bsheetdlg.setContentView(ftm.getRoot());
            // bsheetdlg.setContentView(R.layout.filter_master);
            bsheetdlg.setCanceledOnTouchOutside(true);
            ftm.linearLayoutemp.setVisibility(View.GONE);
            ftm.filterStatusLayout.linearLayout.setVisibility(View.GONE);
            ftm.filterTypeLayout.tilDoctype.setVisibility(View.GONE);
            ftm.filterTypeLayout.tilProgtypetype.setVisibility(View.GONE);
            ftm.filterTypeLayout.spinnerDoctortype.setVisibility(View.GONE);

            ftm.filterMonthYearLayout.ivDatePickerFromDate.setOnClickListener(v -> {
                UtilityHelper._datePickerDialogeForDates(ftm.filterMonthYearLayout.txtFromDate, TeamDAListActivity.this);
            });
            ftm.filterMonthYearLayout.ivDatePickerToDate.setOnClickListener(v -> {
                UtilityHelper._datePickerDialogeForDates(ftm.filterMonthYearLayout.txtToDate, TeamDAListActivity.this);
            });
            ftm.filterTopLayout.btnDonefilter.setOnClickListener(v -> {
                switch (selectedTyp) {
                    case "MIO":
                        MIO mioempid;
                        mioempid = (MIO) ftm.spinnerApprovalMio.getSelectedItem();
                        empid = mioempid.getMIOEmpId();

                        break;
                    case "AM":
                        ASM amempid = (ASM) ftm.spinnerApprovalAsm.getSelectedItem();
                        empid = amempid.getASMEmpId();

                        break;
                    case "DZSM":
                        RSM dzsmempid = (RSM) ftm.spinnerApprovalRsm.getSelectedItem();
                        empid = dzsmempid.getRSMEmpId();
                        break;

                }
                fromdat = ftm.filterMonthYearLayout.txtFromDate.getText().toString();
                todate = ftm.filterMonthYearLayout.txtToDate.getText().toString();
                Map<String,String> filters=new HashMap<>();
                filters.put("Role", RoleType);
                filters.put("AppStatus", "");
                filters.put("FromDt", fromdat);
                filters.put("ToDt", todate);
                filters.put("EmpId", "");

                filters.put("Month", String.valueOf(month + 1));
                filters.put("Year", String.valueOf(year));
                presenter.GetTeamDAList(params, filter);
                bsheetdlg.cancel();
            });
            ftm.filterTopLayout.ivCancel.setOnClickListener(v -> {
                bsheetdlg.cancel();
            });

        }*/
    private void openFilter() {
        bsheetdlg = new BottomSheetDialog(TeamDAListActivity.this);
        bsheetdlg.setContentView(ftm.getRoot());
        bsheetdlg.setCanceledOnTouchOutside(true);
        ftm.filterStatusLayout.getRoot().setVisibility(VISIBLE);
        ftm.filterTypeLayout.tilDoctype.setVisibility(View.GONE);
        ftm.filterTypeLayout.tilProgtypetype.setVisibility(View.GONE);
        ftm.filterTypeLayout.tilCusttype.setVisibility(View.GONE);
        ftm.filterTypeLayout.tilPharmatype.setVisibility(View.GONE);
        ftm.filterMonthYearLayout.getRoot().setVisibility(GONE);
        List<String> emptype=new ArrayList<>();
        switch (role)
        {
            case "AM":
                emptype.add("Select");
                emptype.add("MIO");
                try {
                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(TeamDAListActivity.this, android.R.layout.simple_spinner_item, emptype);
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
                ArrayAdapter<String> dataAdapterrsm = new ArrayAdapter<>(TeamDAListActivity.this, android.R.layout.simple_spinner_item, emptype);
                dataAdapterrsm.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                ftm.spinnerEmployeeType.setAdapter(dataAdapterrsm);
                break;
            case "NSM":
                emptype.add("Select");
                emptype.add("MIO");
                emptype.add("AM");
                emptype.add("DZSM");
                ArrayAdapter<String> dataAdapternsm = new ArrayAdapter<>(TeamDAListActivity.this, android.R.layout.simple_spinner_item, emptype);
                dataAdapternsm.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                ftm.spinnerEmployeeType.setAdapter(dataAdapternsm);
                break;
        }
        ftm.spinnerEmployeeType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedTyp= String.valueOf(ftm.spinnerEmployeeType.getSelectedItem());
                switch (selectedTyp)
                {
                    case "MIO":
                        try {
                            ftm.miolay.setVisibility(View.VISIBLE);
                            ftm.asmlay.setVisibility(View.GONE);
                            ftm.rsmlay.setVisibility(View.GONE);
                            try {
                                mioList = dbCrudHelper.getMIOList_SQLite();
                            } catch (Exception exception) {
                                exception.printStackTrace();
                            }
                            if (mioList != null) {
                                System.out.println("mlist "+mioList);
                                ArrayAdapter<MIO> dataAdapter = new ArrayAdapter<>(TeamDAListActivity.this, android.R.layout.simple_spinner_item, mioList);
                                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                ftm.spinnerApprovalMio.setAdapter(dataAdapter);
                            }
                            else {
                                SnackBarManagement._warning_CustomMessage(binding.getRoot(),"No MIO Found!!");
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
                            List<ASM> asmList = null;
                            try {
                                asmList = dbCrudHelper.getASMList_SQLite();
                            } catch (Exception exception) {
                                exception.printStackTrace();
                            }
                            if (asmList != null) {
                                ArrayAdapter<ASM> dataAdapter = new ArrayAdapter<>(TeamDAListActivity.this, android.R.layout.simple_spinner_item, asmList);
                                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                ftm.spinnerApprovalAsm.setAdapter(dataAdapter);

                            }  else {
                                SnackBarManagement._warning_CustomMessage(binding.getRoot(),"No AM Founded!!");
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
                            List<RSM> rsmList = null;
                            try {
                                rsmList = dbCrudHelper.getRSMList_SQLite();
                            } catch (Exception exception) {
                                exception.printStackTrace();
                            }
                            if (rsmList != null) {
                                ArrayAdapter<RSM> dataAdapter = new ArrayAdapter<>(TeamDAListActivity.this, android.R.layout.simple_spinner_item, rsmList);
                                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                ftm.spinnerApprovalRsm.setAdapter(dataAdapter);
                            }
                            else {
                                SnackBarManagement._warning_CustomMessage(binding.getRoot(),"No DZSM Founded!!");
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
        selectedyear=year;
        int month = c.get(Calendar.MONTH);
        selectedMonth=month;
        ftm.llmonthyear.setVisibility(VISIBLE);
        ftm.ivmypicker.setOnClickListener(v -> MonthPicker());
        ftm.montyer.setText(UtilityHelper.monthNameArrayFull[month] + ',' + year);
        ftm.filterTopLayout.btnDonefilter.setOnClickListener(v -> {
            try {
                switch (selectedTyp)
                {
                    case "MIO":
                        MIO mioempid;
                        mioempid= (MIO) ftm.spinnerApprovalMio.getSelectedItem();
                        empId=String.valueOf(mioempid.getMIOEmpId());
                        break;
                    case "AM":
                        ASM amempid=(ASM) ftm.spinnerApprovalAsm.getSelectedItem();
                        empId=String.valueOf(amempid.getASMEmpId());
                        break;
                    case "DZSM":
                        RSM dzsmempid=(RSM) ftm.spinnerApprovalRsm.getSelectedItem();
                        empId=String.valueOf(dzsmempid.getRSMEmpId());
                        break;
                }
                status = (String) ftm.filterStatusLayout.spinnerApprovalStatus.getSelectedItem();
                HashMap<String, String> filters = new HashMap<>();
                if (status.equals("Select")) {
                    filters.put("AppStatus", "");
                } else {
                    switch (status) {
                        case "Pending":
                            status = "0";
                            break;
                        case "Verified":
                            status = "1";
                            break;
                        case "Approved":
                            status = "2";
                            break;
                        case "Rejected":
                            status = "3";
                            break;
                    }
                    filters.put("AppStatus", status);
                }
                filters.put("Role", role);
                filters.put("FromDt", "");
                filters.put("ToDt", "");
                filters.put("EmpId", empId);
                filters.put("Month", String.valueOf(selectedMonth + 1));
                filters.put("Year", String.valueOf(selectedyear));

                Constants.filtermap=filters;
                Constants.filterparams=params;
                hitApi(params,filters);
               // presenter.GetTeamDAList(params, filters);
                System.out.println(filters);

            } catch (Exception exception) {
                exception.printStackTrace();
            }
            bsheetdlg.cancel();
        });
        ftm.filterTopLayout.ivCancel.setOnClickListener(v -> {
            bsheetdlg.cancel();
        });
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

    @Override
    public void onTeamDAList(List<DAListData> aList) {
        try{
            pd.dismiss();
            daAdapter = new DA_ApprovList_Adapter(aList,this,this,RoleTypeId);
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            binding.rvDa.setLayoutManager(mLayoutManager);
            binding.rvDa.setItemAnimator(new DefaultItemAnimator());
            binding.rvDa.setAdapter(daAdapter);
            binding.rvDa.setItemAnimator(null);
            binding.rvDa.scrollToPosition(0);
            daAdapter.notifyDataSetChanged();
        }
        catch (Exception ex){

        }

    }

    @Override
    public void onSaveSuccess(String message) {
      if(pd2!=null || pd2.isShowing())
      {
          pd2.dismiss();
      }
      SnackBarManagement._success_CustomMessage(binding.getRoot(),message);
        HashMap<String,String > filter = new HashMap<>();
        filter.put("Role", role);
        filter.put("AppStatus", "0");
        filter.put("FromDt", "");
        filter.put("ToDt", "");
        filter.put("EmpId", "");
        filter.put("Month", String.valueOf(month + 1));
        filter.put("Year", String.valueOf(year));

        hitApi(Constants.filterparams,Constants.filtermap);
       // hitApi(params,filter);
    }

    @Override
    public void onSaveError(String message) {
        pd.dismiss();
        SnackBarManagement._error_CustomMessage(binding.getRoot(),message);
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        status = (String) ftm.filterStatusLayout.spinnerApprovalStatus.getSelectedItem();
        HashMap<String, String> filters = new HashMap<>();
        if (status.equals("Select")) {
            filters.put("AppStatus", "0");
        } else {
            filters.put("AppStatus", status);
        }
        filters.put("Role", role);
        filters.put("FromDt", "");
        filters.put("ToDt", "");
        filters.put("EmpId", empId);
        filters.put("Month", String.valueOf(selectedMonthz));
        filters.put("Year", String.valueOf(selectedyear));
        hitApi(params,filters);


        //Toast.makeText(TeamDAListActivity.this, "Restart", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void Emp_total_count(String id, String date,boolean ok) {
        if(ok)
        {
            pd1=new ProgressDialog(TeamDAListActivity.this);
            pd1.setMessage("Calculating...");
            pd1.show();
            popup.show();


            try {
                torder.setText("0");
                tdcr.setText("0");
                trx.setText("0");
                APICall_Report_i service = RetrofitClientInstance.getRetrofitInstance().create(APICall_Report_i.class);
                Call<EmpTotalModel> call = service.GetEmpTotal(id,date);
                call.enqueue(new Callback<EmpTotalModel>() {
                    @Override
                    public void onResponse(@NonNull Call<EmpTotalModel> call, @NonNull Response<EmpTotalModel> response) {
                        pd1.dismiss();
                        if(response.body()!=null)
                        {

                            torder.setText(response.body().getTotalOrder());
                            tdcr.setText(response.body().getTotalDcr());
                            trx.setText(response.body().getTotalRX());
                            //viewPopup(response.body());
                        }

                    }

                    @Override
                    public void onFailure(@NonNull Call<EmpTotalModel> call, @NonNull Throwable t) {
                        pd1.dismiss();
                        popup.show();
                    }
                });

            } catch (Exception ex) {
                pd1.dismiss();
                popup.show();
            }

         //   hitApi2(id,date);
        }
    }
    public void popup_empTotal() {
        popup = new Dialog(TeamDAListActivity.this);
        popup.setContentView(R.layout.popup_emptotal);
        popup.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        popup.getWindow().setLayout(Toolbar.LayoutParams.MATCH_PARENT, Toolbar.LayoutParams.WRAP_CONTENT);
        popup.getWindow().getAttributes().gravity = Gravity.CENTER;
        popup.setCanceledOnTouchOutside(false);

        torder = popup.findViewById(R.id.ordervalue);
        tdcr = popup.findViewById(R.id.dcrvalue);
        trx = popup.findViewById(R.id.rxvalue);
        //cancel_doc = popupDoctor.findViewById(R.id.btn_cancel);
    //    torder.setText("10");
       // http://13.76.141.111:165/api/Reports/GetEmployeeWiseTotalCount?empId=346&Date=11-Sep-2024
//        try {
//            ApiCustomerCall service = RetrofitClientInstance.getRetrofitInstance().create(ApiCustomerCall.class);
//
//            Call<EmpTotalCountModel> call = service.GetEmployeeWiseTotalCount(empId, "11-Sep-2024");
//            call.enqueue(new Callback<EmpTotalCountModel>() {
//                @Override
//                public void onResponse(@NonNull Call<EmpTotalCountModel> call, @NonNull Response<EmpTotalCountModel> response) {
//
//                    if (response.body() != null) {
//                        EmpTotalCountModel data = response.body();
//                        torder.setText(data.getTotalOrder());
//                        tdcr.setText(data.getTotalDcr());
//                        trx.setText(data.getTotalRX());
//                    } else {
//                        // Handle the case where response body is null
//                        // SnackBarManagement._success_CustomMessage(binding.mainmaster, "No Order Available");
//                    }
//                }
//
//                @Override
//                public void onFailure(@NonNull Call<EmpTotalCountModel> call, @NonNull Throwable t) {
//                    // Handle failure
//                    t.printStackTrace();
//                    // SnackBarManagement._error_CustomMessage(binding.mainmaster, "Request failed");
//                }
//            });
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            // SnackBarManagement._error_CustomMessage(binding.mainmaster, "An error occurred");
//        }

    }

    private void hitApi2(String id, String date) {
        try {
            torder.setText("0");
            tdcr.setText("0");
            trx.setText("0");
            APICall_Report_i service = RetrofitClientInstance.getRetrofitInstance().create(APICall_Report_i.class);
            Call<EmpTotalModel> call = service.GetEmpTotal(id,date);
            call.enqueue(new Callback<EmpTotalModel>() {
                @Override
                public void onResponse(@NonNull Call<EmpTotalModel> call, @NonNull Response<EmpTotalModel> response) {
                  pd1.dismiss();
                    if(response.body()!=null)
                  {
                   //   viewPopup(response.body());
                  }

                }

                @Override
                public void onFailure(@NonNull Call<EmpTotalModel> call, @NonNull Throwable t) {
                    pd1.dismiss();
                    popup.show();
                }
            });

        } catch (Exception ex) {
            pd1.dismiss();
            popup.show();
        }
    }
//
//    private void viewPopup(EmpTotalModel body) {
//        popup.show();
//        torder.setText(body.getTotalOrder());
//        tdcr.setText(body.getTotalDcr());
//        trx.setText(body.getTotalRX());
//    }

    @Override
    public void approvalKlick(DAListData data) {
        if (RoleTypeId == 2) {
            next = data.getRSMEMPId();

        }
        if (RoleTypeId == 3) {
            next = data.getNSMEMPId();

        }
        if (RoleTypeId == 4) {
            next = 0;
        }

        ApproveDARQ req = new ApproveDARQ();
        int step = data.getStep();
        int fstep = step + 1;
        req.setTADAApprovalId(0);
        req.setFromEmpId(Integer.parseInt(empid));
        req.setToEmpId(next);
        req.setTableId(data.getaTADAMasterDAO().getTadaID());
        req.setStatus("Verified");//Accepted==approve for Admin
        req.setType(data.getType());
        req.setStep(fstep);
        req.setEntryByApp(String.valueOf(empid));
        String entrydate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        String entrytime = new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(new Date());
        req.setEntryDateApp(entrydate);
        req.setEntryTimeApp(entrytime);
        req.setMenuId(376);

        Gson gson=new Gson();
        String dataz=gson.toJson(req);
        System.out.println("value"+dataz);

        pd2.setMessage("Processing...");
        pd2.show();
        pd2.setCancelable(false);
        presenter.SaveTeamDA(req);

    }

    public void initCommentPop(DAListData data) {
        popComment = new Dialog(TeamDAListActivity.this);
        popComment.setContentView(R.layout.pop_comment);
        //popAddQty.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popComment.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        popComment.getWindow().setLayout(Toolbar.LayoutParams.MATCH_PARENT, Toolbar.LayoutParams.WRAP_CONTENT);
        popComment.getWindow().getAttributes().gravity = Gravity.CENTER;

        final EditText userComment = (EditText) popComment.findViewById(R.id.remarksTxt);
        userComment.requestFocus();
        submitCmnt = popComment.findViewById(R.id.psubmitBnt);
        submitCmnt.setOnClickListener(v1 -> {
            if (userComment.getText().toString().equals("")) {
                userComment.setError("Comment Must");
                userComment.setFocusable(true);
            } else {
                ApproveDARQ req = new ApproveDARQ();
                int step = data.getStep();
                int fstep = step + 1;
                // Toast.makeText(context, "empid : "+String.valueOf(empid), Toast.LENGTH_SHORT).show();
                req.setTADAApprovalId(0);
                req.setFromEmpId(Integer.parseInt(empid));
                req.setToEmpId(data.getToEmpId());
                req.setTableId(data.getaTADAMasterDAO().getTadaID());
                req.setStatus("Rejected");//Accepted==approve for admin
                req.setType(data.getType());
                req.setStep(fstep);
                req.setEntryByApp(String.valueOf(empid));
                String entrydate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
                String entrytime = new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(new Date());
                req.setEntryDateApp(entrydate);
                req.setEntryTimeApp(entrytime);
                req.setMenuId(376);
                req.setComments(userComment.getText().toString());
                Gson gson=new Gson();
                String datas=gson.toJson(req);
                System.out.println("reject data: "+datas);
                presenter.SaveTeamDA(req);
                SnackBarManagement._success_CustomMessage(v1, "Rejected");
                popComment.dismiss();
                onBackPressed();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
            }
        });
    }
}
