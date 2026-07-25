package com.creatrix.salessolution.Activity.Approval.Leave;

import static android.view.View.GONE;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.creatrix.salessolution.Activity.Approval.DA.TeamDAListActivity;
import com.creatrix.salessolution.Activity.Approval.Prescription.PrescApprovalAdapter;
import com.creatrix.salessolution.Activity.Approval.Prescription.PrescriptionApprovalListActivity;
import com.creatrix.salessolution.DBAdapter.DBCrudHelper;
import com.creatrix.salessolution.Interface.ILeaveApproval;
import com.creatrix.salessolution.Model.MarketStructure.AssignEmpTable.ASM;
import com.creatrix.salessolution.Model.MarketStructure.AssignEmpTable.MIO;
import com.creatrix.salessolution.Model.MarketStructure.AssignEmpTable.RSM;
import com.creatrix.salessolution.Presenter.LeaveApprovalPresenter;
import com.creatrix.salessolution.Presenter.PrescApprovalPresenter;
import com.creatrix.salessolution.R;
import com.creatrix.salessolution.Services.Constants;
import com.creatrix.salessolution.UtilityHelper.SessionManagement;
import com.creatrix.salessolution.UtilityHelper.SnackBarManagement;
import com.creatrix.salessolution.UtilityHelper.UtilityHelper;
import com.creatrix.salessolution.databinding.ActivityLeaveApprovalListBinding;
import com.creatrix.salessolution.databinding.FilterMasterBinding;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class LeaveApprovalListActivity extends AppCompatActivity implements ILeaveApproval.View,LeaveApprovalAdapter.approvalKlick {
    ActivityLeaveApprovalListBinding binding;
    LeaveApprovalPresenter presenter;
    BottomSheetDialog bsheetdlg;
    DBCrudHelper dbCrudHelper;
    LeaveApprovalAdapter leaveAdapter;
    SessionManagement session;
    String empid;
    Map<String, String> filter;
    String fromdat, todate, RoleType, params, tagA, tagR, tagN, Areaid, Regionid, Groupid, status, selectedTyp = "";
    int RoleTypeId;
    List<MIO> mioList;
    String empidS;

    private int next = 0;
    ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_leave_approval_list);
        binding = ActivityLeaveApprovalListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        pd=new ProgressDialog(LeaveApprovalListActivity.this);
        presenter = new LeaveApprovalPresenter(this, LeaveApprovalListActivity.this);
        setContentView(binding.getRoot());
        session = new SessionManagement(LeaveApprovalListActivity.this);
        session.checkLogin();
        HashMap<String, String> user = session.getUserDetails();
        empid = user.get(SessionManagement.KEY_EmpId);
        RoleTypeId = Integer.parseInt(user.get(SessionManagement.KEY_EmpRoleTypeId));
        RoleType = user.get(SessionManagement.KEY_EmpRoleType);
        binding.toolbarCustom.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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
                    Regionid = String.valueOf(dbCrudHelper.getCurrentUserRegionId_SQLite(String.valueOf(empid)));
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
        } catch (Exception exception) {
        }
        filter = new HashMap<>();
        filter.put("Role", RoleType);
        if(RoleType.equals("AM"))
        {
            filter.put("AppStatus", "0");
        }else {
            filter.put("AppStatus", "0,1");
        }
        filter.put("FromDt", "");
        filter.put("ToDt", "");
        filter.put("EmpId", "");
        pd.setMessage("Loading...");
        pd.show();
        pd.setCancelable(false);
        pd.setCanceledOnTouchOutside(false);
        Constants.filtermap=filter;
        Constants.filterparams=params;
        presenter.getLeaveApprovalList(params, filter);
        binding.swip.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.getLeaveApprovalList(params, filter);
                binding.swip.setRefreshing(false);
            }
        });
        // binding.selectedMonthTxt.setText(UtilityHelper.monthNameArrayFull[month] + ',' + year);
        openFilter();
        binding.atteFilter.setOnClickListener(v -> {
            bsheetdlg.show();
        });
    }

    private void openFilter() {
        FilterMasterBinding ftm = FilterMasterBinding.inflate(getLayoutInflater());
        bsheetdlg = new BottomSheetDialog(LeaveApprovalListActivity.this);
        bsheetdlg.setContentView(ftm.getRoot());
        // bsheetdlg.setContentView(R.layout.filter_master);
        bsheetdlg.setCanceledOnTouchOutside(true);

        ftm.filterStatusLayout.getRoot().setVisibility(View.VISIBLE);
        ftm.filterTypeLayout.tilDoctype.setVisibility(View.GONE);
        ftm.filterTypeLayout.tilProgtypetype.setVisibility(View.GONE);
        ftm.filterTypeLayout.tilCusttype.setVisibility(View.GONE);
        ftm.filterTypeLayout.tilPharmatype.setVisibility(View.GONE);

        ftm.filterMonthYearLayout.getRoot().setVisibility(View.VISIBLE);

        List<String> emptype = new ArrayList<>();
        switch (RoleType) {
            case "AM":
                emptype.add("Select");
                emptype.add("MIO");
                try {
                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(LeaveApprovalListActivity.this, android.R.layout.simple_spinner_item, emptype);
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
                ArrayAdapter<String> dataAdapterrsm = new ArrayAdapter<>(LeaveApprovalListActivity.this, android.R.layout.simple_spinner_item, emptype);
                dataAdapterrsm.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                ftm.spinnerEmployeeType.setAdapter(dataAdapterrsm);
                break;
            case "NSM":
                emptype.add("Select");
                emptype.add("MIO");
                emptype.add("AM");
                emptype.add("DZSM");
                ArrayAdapter<String> dataAdapternsm = new ArrayAdapter<>(LeaveApprovalListActivity.this, android.R.layout.simple_spinner_item, emptype);
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
                            try {
                                mioList = dbCrudHelper.getMIOList_SQLite();
                            } catch (Exception exception) {
                                exception.printStackTrace();
                            }
                            if (mioList != null) {
                                System.out.println("mlist " + mioList);
                                ArrayAdapter<MIO> dataAdapter = new ArrayAdapter<>(LeaveApprovalListActivity.this, android.R.layout.simple_spinner_item, mioList);
                                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                ftm.spinnerApprovalMio.setAdapter(dataAdapter);
                            } else {
                                SnackBarManagement._warning_CustomMessage(binding.getRoot(), "No MIO Found!!");
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
                                ArrayAdapter<ASM> dataAdapter = new ArrayAdapter<>(LeaveApprovalListActivity.this, android.R.layout.simple_spinner_item, asmList);
                                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                ftm.spinnerApprovalAsm.setAdapter(dataAdapter);

                            } else {
                                SnackBarManagement._warning_CustomMessage(binding.getRoot(), "No AM Founded!!");
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
                                ArrayAdapter<RSM> dataAdapter = new ArrayAdapter<>(LeaveApprovalListActivity.this, android.R.layout.simple_spinner_item, rsmList);
                                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                ftm.spinnerApprovalRsm.setAdapter(dataAdapter);
                            } else {
                                SnackBarManagement._warning_CustomMessage(binding.getRoot(), "No DZSM Founded!!");
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
        ftm.filterMonthYearLayout.ivDatePickerFromDate.setVisibility(View.VISIBLE);
        ftm.filterMonthYearLayout.ivDatePickerToDate.setVisibility(View.VISIBLE);

        ftm.filterMonthYearLayout.txtFromDate.setText(UtilityHelper._GetCurrentDate());
        ftm.filterMonthYearLayout.txtToDate.setText(UtilityHelper._GetCurrentDate());
        ftm.filterMonthYearLayout.ivDatePickerFromDate.setOnClickListener(v -> {
            UtilityHelper._datePickerDialogeForDates(ftm.filterMonthYearLayout.txtFromDate, LeaveApprovalListActivity.this);
        });
        ftm.filterMonthYearLayout.ivDatePickerToDate.setOnClickListener(v -> {
            UtilityHelper._datePickerDialogeForDates(ftm.filterMonthYearLayout.txtToDate, LeaveApprovalListActivity.this);
        });

        ftm.filterTopLayout.btnDonefilter.setOnClickListener(v -> {
            try {
                switch (selectedTyp) {
                    case "MIO":
                        MIO mioempid;
                        mioempid = (MIO) ftm.spinnerApprovalMio.getSelectedItem();
                        empidS = String.valueOf(mioempid.getMIOEmpId());
                        break;
                    case "AM":
                        ASM amempid = (ASM) ftm.spinnerApprovalAsm.getSelectedItem();
                        empidS = String.valueOf(amempid.getASMEmpId());
                        break;
                    case "DZSM":
                        RSM dzsmempid = (RSM) ftm.spinnerApprovalRsm.getSelectedItem();
                        empidS = String.valueOf(dzsmempid.getRSMEmpId());
                        break;
                }
                fromdat = ftm.filterMonthYearLayout.txtFromDate.getText().toString();
                todate = ftm.filterMonthYearLayout.txtToDate.getText().toString();
                status = (String) ftm.filterStatusLayout.spinnerApprovalStatus.getSelectedItem();
                Map<String, String> filters = new HashMap<>();
                filters.put("Role", RoleType);
                if (status.equals("Select"))
                {
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
                filters.put("FromDt", fromdat);
                filters.put("ToDt", todate);
                if(empidS==null)
                {
                    filters.put("EmpId","");
                }else {
                    filters.put("EmpId",empidS);
                }
                Constants.filtermap=filters;
                Constants.filterparams=params;
                presenter.getLeaveApprovalList(params, filters);
            } catch (Exception e) {
                e.printStackTrace();
            }
            bsheetdlg.cancel();
        });
        ftm.filterTopLayout.ivCancel.setOnClickListener(v -> {
            bsheetdlg.cancel();
        });

    }

    @Override
    public void onGetLeaveApprovalList(List<LeaveApprovalData> aList) {
        if(pd!=null || pd.isShowing())
        {
            pd.dismiss();
        }
        if (aList != null) {
            binding.count.setText(String.valueOf(aList.size()));
            leaveAdapter = new LeaveApprovalAdapter(LeaveApprovalListActivity.this, aList,this,RoleTypeId);
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            binding.rvLeavelist.setLayoutManager(mLayoutManager);
            binding.rvLeavelist.setItemAnimator(new DefaultItemAnimator());
            binding.rvLeavelist.setAdapter(leaveAdapter);
            binding.rvLeavelist.setItemAnimator(null);
            binding.rvLeavelist.scrollToPosition(0);
            leaveAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onSaveSuccess(String message, String what) {
        if(pd!=null || pd.isShowing())
        {
            pd.dismiss();
        }
        if(what.equals("Success"))
        {
            SnackBarManagement._success_CustomMessage(binding.getRoot(),message);
        }
        if(what.equals("Info")){
            SnackBarManagement._warning_CustomMessage(binding.getRoot(),message);
        }


        HashMap<String,String> filter = new HashMap<>();
        filter.put("Role", RoleType);
        filter.put("AppStatus", "0");
        filter.put("FromDt", "");
        filter.put("ToDt", "");
        filter.put("EmpId", "");
        pd.setMessage("Loading...");
        pd.show();
        presenter.getLeaveApprovalList(Constants.filterparams, Constants.filtermap);
       // presenter.getLeaveApprovalList(params, filter);
    }



    @Override
    public void onError(String message) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.getLeaveApprovalList(params, filter);
    }

    @Override
    public void approvalTLeave(LeaveApprovalData data) {

        if (RoleTypeId == 2) {
            next = data.getRSMEMPId();
        }
        if (RoleTypeId == 3) {
            next = data.getNSMEMPId();
        }
        if (RoleTypeId == 4) {
            next = 0;
        }

        LeaveApprovalRQ req = new LeaveApprovalRQ();
        int step = data.getStep();
        int fstep = step + 1;
        req.setLeaveApprovalId(0);
        req.setFromEmpId(Integer.parseInt(empid));
        req.setToEmpId(next);
        req.setTableId(data.getLeaveApplicationId());
        req.setStatus("Verified");//Accepted==approve for Admin
        req.setType(data.getType());
        req.setStep(fstep);
        req.setEntryByApp(String.valueOf(empid));
        String entrydate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        String entrytime = new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(new Date());
        req.setEntryDateApp(entrydate);
        req.setEntryTimeApp(entrytime);
        req.setMenuId(1378);
        Gson gson1=new Gson();
        String datas=gson1.toJson(req);
        System.out.println(datas);
        pd.setMessage("Submitting...");
        pd.show();
        presenter.SaveLeaveApproval(req);
    }
}