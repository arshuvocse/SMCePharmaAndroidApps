package com.creatrix.salessolution.Activity.MileageClaim.Report;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.creatrix.salessolution.Activity.Expense.Approval.TeamExpClaimReportActivity;
import com.creatrix.salessolution.Activity.MileageClaim.Model.ApproveMilRQ;
import com.creatrix.salessolution.Activity.MileageClaim.Model.MileageListTeam;
import com.creatrix.salessolution.Activity.MileageClaim.mileageTeamApprovalAdapter;
import com.creatrix.salessolution.DBAdapter.DBCrudHelper;
import com.creatrix.salessolution.Interface.IMileageTeam;
import com.creatrix.salessolution.Model.MarketStructure.AssignEmpTable.ASM;
import com.creatrix.salessolution.Model.MarketStructure.AssignEmpTable.MIO;
import com.creatrix.salessolution.Model.MarketStructure.AssignEmpTable.RSM;
import com.creatrix.salessolution.Model.MileageClaimSM;
import com.creatrix.salessolution.Presenter.TeamMileagePresenter;
import com.creatrix.salessolution.R;
import com.creatrix.salessolution.Services.Constants;
import com.creatrix.salessolution.UtilityHelper.SessionManagement;
import com.creatrix.salessolution.UtilityHelper.SnackBarManagement;
import com.creatrix.salessolution.UtilityHelper.UtilityHelper;
import com.creatrix.salessolution.databinding.ActivityTeamMileageClaimBinding;
import com.creatrix.salessolution.databinding.FilterMasterBinding;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class TeamMileageClaimActivity extends AppCompatActivity implements IMileageTeam.View, mileageTeamApprovalAdapter.approvalTMil {
    ActivityTeamMileageClaimBinding binding;
    TeamMileagePresenter presenter;
    mileageTeamApprovalAdapter adapter;
    SessionManagement session;
    DBCrudHelper dbCrudHelper;
    HashMap<String, String> userInfo = new HashMap<>();
    Map<String, String> filter;
    String RoleType, params, tagA, tagR, tagN, Areaid, Regionid, Groupid, empid, selectedTyp = "";
    int RoleTypeId;
    FilterMasterBinding ftm;
    //Filter
    BottomSheetDialog bsheetdlg;
    String status, fromdat, todate, empids;
    ProgressDialog pd;
    int next = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTeamMileageClaimBinding.inflate(getLayoutInflater());
        //setContentView(R.layout.activity_team_mileage_claim);
        setContentView(binding.getRoot());
        pd = new ProgressDialog(TeamMileageClaimActivity.this);
        ftm = FilterMasterBinding.inflate(getLayoutInflater());
        binding.toolbarCustom.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        presenter = new TeamMileagePresenter(this, this);
        session = new SessionManagement(getApplicationContext());
        userInfo = session.getUserDetails();
        empid = userInfo.get(SessionManagement.KEY_EmpId);
        RoleTypeId = Integer.parseInt(userInfo.get(SessionManagement.KEY_EmpRoleTypeId));
        RoleType = userInfo.get(SessionManagement.KEY_EmpRoleType);
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        binding.selectedMonthTxt.setText(UtilityHelper.monthNameArrayFull[month] + ',' + year);
        try {
            tagA = ".EmpAreaId=";
            tagR = ".EmpRegionId=";
            tagN = ".EmpGroupId=";
            dbCrudHelper = new DBCrudHelper(this);
            switch (RoleType) {
                case "AM":
                    Areaid = String.valueOf(dbCrudHelper.getCurrentUserAreaId_SQLite());
                    params = "AND View_Webapi_EmployeeFieldForceInfo" + tagA + Areaid;
                    break;
                case "DZSM":
                    Regionid = String.valueOf(dbCrudHelper.getCurrentUserRegionId_SQLite (String.valueOf(empid)));
                    params = "AND View_Webapi_EmployeeFieldForceInfo" + tagR + Regionid;
                    break;
                case "NSM":
                    Groupid = String.valueOf(dbCrudHelper.getCurrentUserGroupId_SQLite());
                    params = "AND View_Webapi_EmployeeFieldForceInfo" + tagN + Groupid;
                    break;

                case "Admin":
                    params = "";
                    break;

            }
            filter = new HashMap<>();
            filter.put("Role", RoleType);
            filter.put("AppStatus", "0");
            filter.put("Month", String.valueOf(month + 1));
            filter.put("Year", String.valueOf(year));
            filter.put("FromDt", "");
            filter.put("ToDt", "");
            filter.put("EmpId", "");
            Constants.filtermap=filter;
            Constants.filterparams=params;
            hitApi(params, filter);

        } catch (Exception exception) {
            exception.printStackTrace();
        }
        openFilter();
        binding.teammileageFilter.setOnClickListener(v -> {
            bsheetdlg.show();
        });
        binding.swipteammileage.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.GetTeamMileageList(params, filter);
                binding.swipteammileage.setRefreshing(false);
            }
        });
    }

    private void hitApi(String params, Map<String, String> filter) {
        pd.setMessage("Loading...");
        pd.setCancelable(false);
        pd.dismiss();
        presenter.GetTeamMileageList(params, filter);
    }


    @Override
    public void onTeamMileageList(List<MileageListTeam> aList) {
        pd.dismiss();
        try {
            if (aList != null) {
                adapter = new mileageTeamApprovalAdapter(this, aList, RoleTypeId, this);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(TeamMileageClaimActivity.this);
                binding.rvMileageteam.setLayoutManager(mLayoutManager);
                binding.rvMileageteam.setItemAnimator(new DefaultItemAnimator());
                binding.rvMileageteam.setAdapter(adapter);
                binding.rvMileageteam.setItemAnimator(null);
                binding.rvMileageteam.scrollToPosition(0);
                adapter.notifyDataSetChanged();
                //binding.userCount.setText(String.valueOf(teamlist.size()));
                if (aList.size() == 0) {
                    binding.nodta.setVisibility(View.VISIBLE);
                    binding.rvMileageteam.setVisibility(View.GONE);
                } else {
                    binding.nodta.setVisibility(View.GONE);
                    binding.rvMileageteam.setVisibility(View.VISIBLE);
                }
            } else {
                binding.nodta.setVisibility(View.VISIBLE);
                binding.rvMileageteam.setVisibility(View.GONE);
            }
        } catch (Exception exception) {
            // exception.printStackTrace();
        }
    }

    private void openFilter() {
        bsheetdlg = new BottomSheetDialog(TeamMileageClaimActivity.this);
        bsheetdlg.setContentView(ftm.getRoot());
        bsheetdlg.setCanceledOnTouchOutside(true);
        ftm.filterTypeLayout.linearLayout.setVisibility(View.GONE);
        ftm.filterTypeLayout.tilProgtypetype.setVisibility(View.GONE);
        ftm.filterTypeLayout.tilDoctype.setVisibility(View.GONE);
        ftm.filterTypeLayout.tilCusttype.setVisibility(View.GONE);
        ftm.filterTypeLayout.tilPharmatype.setVisibility(View.GONE);
        // Employee Type
        List<String> emptype = new ArrayList<>();
        switch (RoleType) {
            case "AM":
                emptype.add("Select");
                emptype.add("MIO");
                try {
                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(TeamMileageClaimActivity.this, android.R.layout.simple_spinner_item, emptype);
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
                ArrayAdapter<String> dataAdapterrsm = new ArrayAdapter<>(TeamMileageClaimActivity.this, android.R.layout.simple_spinner_item, emptype);
                dataAdapterrsm.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                ftm.spinnerEmployeeType.setAdapter(dataAdapterrsm);
                break;
            case "NSM":
                emptype.add("Select");
                emptype.add("MIO");
                emptype.add("AM");
                emptype.add("DZSM");
                ArrayAdapter<String> dataAdapternsm = new ArrayAdapter<>(TeamMileageClaimActivity.this, android.R.layout.simple_spinner_item, emptype);
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
                            List<MIO> mioList = null;
                            try {
                                mioList = dbCrudHelper.getMIOList_SQLite();
                            } catch (Exception exception) {
                                exception.printStackTrace();
                            }
                            if (mioList != null) {
                                System.out.println("mlist " + mioList.toString());
                                ArrayAdapter<MIO> dataAdapter = new ArrayAdapter<>(TeamMileageClaimActivity.this, android.R.layout.simple_spinner_item, mioList);
                                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                ftm.spinnerApprovalMio.setAdapter(dataAdapter);
                            } else {
                                SnackBarManagement._warning_CustomMessage(binding.masterLayout, "No MIO Found!!");
                            }
                        } catch (Exception exception) {
                            // exception.printStackTrace();
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
                                ArrayAdapter<ASM> dataAdapter = new ArrayAdapter<>(TeamMileageClaimActivity.this, android.R.layout.simple_spinner_item, asmList);
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
                            List<RSM> rsmList = null;
                            try {
                                rsmList = dbCrudHelper.getRSMList_SQLite();
                            } catch (Exception exception) {
                                exception.printStackTrace();
                            }
                            if (rsmList != null) {
                                ArrayAdapter<RSM> dataAdapter = new ArrayAdapter<>(TeamMileageClaimActivity.this, android.R.layout.simple_spinner_item, rsmList);
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
        ftm.filterMonthYearLayout.ivDatePickerFromDate.setOnClickListener(v -> {
            UtilityHelper._datePickerDialogeForDates(ftm.filterMonthYearLayout.txtFromDate, TeamMileageClaimActivity.this);
        });
        ftm.filterMonthYearLayout.ivDatePickerToDate.setOnClickListener(v -> {
            UtilityHelper._datePickerDialogeForDates(ftm.filterMonthYearLayout.txtToDate, TeamMileageClaimActivity.this);
        });
        ftm.filterTopLayout.btnDonefilter.setOnClickListener(v -> {
            switch (selectedTyp) {
                case "MIO":
                    MIO mioempid;
                    mioempid = (MIO) ftm.spinnerApprovalMio.getSelectedItem();
                    empids = String.valueOf(mioempid.getMIOEmpId());

                    break;
                case "AM":
                    ASM amempid = (ASM) ftm.spinnerApprovalAsm.getSelectedItem();
                    empids = String.valueOf(amempid.getASMEmpId());

                    break;
                case "DZSM":
                    RSM dzsmempid = (RSM) ftm.spinnerApprovalRsm.getSelectedItem();
                    empids = String.valueOf(dzsmempid.getRSMEmpId());
                    break;
            }
            status = (String) ftm.filterStatusLayout.spinnerApprovalStatus.getSelectedItem();
            fromdat = ftm.filterMonthYearLayout.txtFromDate.getText().toString();
            todate = ftm.filterMonthYearLayout.txtToDate.getText().toString();
            Map<String, String> filters = new HashMap<>();
            filters.put("Role", RoleType);
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

            filters.put("Month", "");
            filters.put("Year", "");
            filters.put("FromDt", fromdat);
            filters.put("ToDt", todate);
            if (empids == null) {
                filters.put("EmpId", "");
            } else {
                filters.put("EmpId", empids);
            }

            Constants.filtermap=filters;
            Constants.filterparams=params;

            hitApi(params, filters);
            bsheetdlg.cancel();
        });

        ftm.filterTopLayout.ivCancel.setOnClickListener(v -> {
            bsheetdlg.cancel();
        });

    }

    @Override
    public void onSaveSuccess(String message) {
        if (pd != null || pd.isShowing()) {
            pd.dismiss();
        }
        SnackBarManagement._success_CustomMessage(binding.getRoot(), message);
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);

        HashMap<String, String> filter = new HashMap<>();
        filter.put("Role", RoleType);
        filter.put("AppStatus", "0");
        filter.put("Month", String.valueOf(month + 1));
        filter.put("Year", String.valueOf(year));
        filter.put("FromDt", "");
        filter.put("ToDt", "");
        filter.put("EmpId", "");
        hitApi(Constants.filterparams, Constants.filtermap);
        //hitApi(params, filter);
    }

    @Override
    public void onSaveError(String message) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.GetTeamMileageList(params, filter);
    }

    @Override
    public void approvalKlick(MileageListTeam data) {
        if (RoleTypeId == 2) {
            next = data.getRSMEMPId();
        }
        if (RoleTypeId == 3) {
            next = data.getNSMEMPId();
        }
        if (RoleTypeId == 4) {
            next = 0;
        }


        ApproveMilRQ req = new ApproveMilRQ();
        int step = data.getStep();
        int fstep = step + 1;

        req.setMileageApprovalId(0);
        req.setFromEmpId(Integer.parseInt(empid));
        req.setToEmpId(next);
        req.setTableId(data.getMileageClaimId());
        req.setStatus("Verified");//Accepted==approve for Admin
        req.setType(data.getType());
        req.setStep(fstep);
        req.setEntryByApp(Integer.parseInt(empid));
        String entrydate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        String entrytime = new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(new Date());
        req.setEntryDateApp(entrydate);
        req.setEntryTimeApp(entrytime);
        req.setMenuId(372);

       /* String datas= gson.toJson(req);
        System.out.println("mileage "+data);*/
        pd.setMessage("Submitting..");
        pd.show();
        presenter.SaveTeamMileageClaim(req);
    }
}