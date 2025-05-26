package com.creatrix.salessolution.Activity.Expense.Approval;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.creatrix.salessolution.Activity.Expense.ExpenceClaimViewActivity;
import com.creatrix.salessolution.Activity.Expense.Model.ApproveExpRQ;
import com.creatrix.salessolution.Activity.Expense.Model.ExpListTeam;
import com.creatrix.salessolution.Activity.Expense.Report.expenseTeamApprovalAdapter;
import com.creatrix.salessolution.DBAdapter.DBCrudHelper;
import com.creatrix.salessolution.Interface.ITeamExpClaim;
import com.creatrix.salessolution.Model.MarketStructure.AssignEmpTable.ASM;
import com.creatrix.salessolution.Model.MarketStructure.AssignEmpTable.MIO;
import com.creatrix.salessolution.Model.MarketStructure.AssignEmpTable.RSM;
import com.creatrix.salessolution.Presenter.ExpenseClaimTeamPresenter;
import com.creatrix.salessolution.R;
import com.creatrix.salessolution.RecyclerAdapter._expenseClaimReport_Recycler;
import com.creatrix.salessolution.Services.Constants;
import com.creatrix.salessolution.UtilityHelper.SessionManagement;
import com.creatrix.salessolution.UtilityHelper.SnackBarManagement;
import com.creatrix.salessolution.UtilityHelper.UtilityHelper;
import com.creatrix.salessolution.databinding.ActivityTeamExpClaimListBinding;
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

public class TeamExpClaimReportActivity extends AppCompatActivity implements ITeamExpClaim.View,expenseTeamApprovalAdapter.approveTE{
    ActivityTeamExpClaimListBinding binding;
    FilterMasterBinding ftm;
    SessionManagement session;
    DBCrudHelper dbCrudHelper;
    HashMap<String, String> userInfo = new HashMap<>();

    ProgressDialog pd;
    ExpenseClaimTeamPresenter presenter;
    expenseTeamApprovalAdapter adapter;
    ProgressDialog progressDoalog;
    _expenseClaimReport_Recycler mAdapter;
    String fromdat, todate,status,empid,RoleType,params, tagA, tagR, tagN, Areaid, Regionid, Groupid;
    int RoleTypeId;
    String prev_roleType, next_roleType;
    private int prev = 0;
    private int current = 0;
    private int next = 0;
    private int myrole = 0;
    int selectedMonth, selectedyear;
    //Filter
    BottomSheetDialog bsheetdlg;
    String selectedTyp="";
    List<MIO> mioList;
    List<ASM> amList;
    List<RSM> dzsmList;
    int empId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityTeamExpClaimListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        pd=new ProgressDialog(this);
        binding.toolbarCustom.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        bsheetdlg = new BottomSheetDialog(TeamExpClaimReportActivity.this);
        session = new SessionManagement(TeamExpClaimReportActivity.this);
        userInfo = session.getUserDetails();
       // role = userInfo.get(SessionManagement.KEY_EmpRole);
        RoleTypeId = Integer.parseInt(userInfo.get(SessionManagement.KEY_EmpRoleTypeId));
        RoleType = userInfo.get(SessionManagement.KEY_EmpRoleType);
        presenter=new ExpenseClaimTeamPresenter(this, TeamExpClaimReportActivity.this);
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        binding.selectedMonthTxt.setText(UtilityHelper.monthNameArrayFull[month]+','+year);

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
                Regionid = String.valueOf(dbCrudHelper.getCurrentUserRegionId_SQLite());
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
        Map<String, String>  filter = new HashMap<>();
        filter.put("Role",RoleType);
        filter.put("AppStatus","0");
        filter.put("FromDt","");
        filter.put("ToDt","");
        filter.put("EmpId","");
       // hitApi(params,filter);
        pd.setMessage("Loading..");
        pd.show();
        pd.setCancelable(false);
        Constants.filtermap=filter;
        Constants.filterparams=params;
        presenter.GetExpenseCTeamList(params, filter);

        binding.swipRl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Map<String, String>  filter1 = new HashMap<>();
                filter1.put("Role",RoleType);
                filter1.put("AppStatus","0");
                filter1.put("FromDt","");
                filter1.put("ToDt","");
                filter1.put("EmpId","");
                pd.setMessage("Loading..");
                pd.show();
                pd.setCancelable(false);
                presenter.GetExpenseCTeamList(params, filter1);

                binding.swipRl.setRefreshing(false);
            }
        });
        openFilter();
        binding.teamExpFilter.setOnClickListener(v -> {
           bsheetdlg.show();
        });

    }
   /* private void hitApi(String params, Map<String, String> filter) {
        pd.setMessage("Loading..");
        pd.show();
        pd.setCancelable(false);
        presenter.GetExpenseCTeamList(params, filter);
    }*/
    @Override
    public void onExpenseCTeamListGet(List<ExpListTeam> aList) {
        System.out.println(aList);
      //  aList.clear();
        pd.dismiss();
        if (aList != null) {
            bsheetdlg.dismiss();
            adapter = new expenseTeamApprovalAdapter(this,aList, RoleTypeId, RoleType,this);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(TeamExpClaimReportActivity.this);
            binding.rvExpteam.setLayoutManager(mLayoutManager);
            binding.rvExpteam.setItemAnimator(new DefaultItemAnimator());
            binding.rvExpteam.setAdapter(adapter);
            binding.rvExpteam.setItemAnimator(null);
            binding.rvExpteam.scrollToPosition(0);
            adapter.notifyDataSetChanged();
            //binding.userCount.setText(String.valueOf(teamlist.size()));
            if (aList.size() == 0) {
                binding.nodta.setVisibility(View.VISIBLE);
                binding.rvExpteam.setVisibility(View.GONE);
            } else {
                binding.nodta.setVisibility(View.GONE);
                binding.rvExpteam.setVisibility(View.VISIBLE);
            }
        } else {
            binding.nodta.setVisibility(View.VISIBLE);
            binding.rvExpteam.setVisibility(View.GONE);
        }
    }

    @Override
    public void onSaveSuccess(String message) {
        if(pd!=null || pd.isShowing())
        {
            pd.dismiss();
        }
        if(message.equals("Approved"))
        {
            new androidx.appcompat.app.AlertDialog.Builder(TeamExpClaimReportActivity.this)
                    .setTitle("Success")
                    .setMessage("You Approved Expense")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                            HashMap<String,String>  filterd = new HashMap<>();
                            filterd.put("Role", RoleType);
                            filterd.put("AppStatus", "0");
                            filterd.put("FromDt", "");
                            filterd.put("ToDt", "");
                            filterd.put("EmpId", "");

                            pd.setMessage("Loading..");
                            pd.show();
                            pd.setCancelable(false);
                           // presenter.GetExpenseCTeamList(params, filterd);
                            presenter.GetExpenseCTeamList(Constants.filterparams, Constants.filtermap);
                            //finish();
                        }

                    }).setCancelable(false).show();


        }else if(message.equals("Check")){
            SnackBarManagement._warning_CustomMessage(binding.masterLayout,"Approved date already expired!!");
        }else {
            SnackBarManagement._warning_CustomMessage(binding.masterLayout,"Something went wrong!!.Try Again");
        }
    }

    @Override
    public void onSaveError(String message) {
        if(pd!=null || pd.isShowing())
        {
            pd.dismiss();
        }

    }


    private void openFilter() {
        ftm = FilterMasterBinding.inflate(getLayoutInflater());
        bsheetdlg.setContentView(ftm.getRoot());
        bsheetdlg.setCanceledOnTouchOutside(false);
        // Employee Type
        List<String> emptype=new ArrayList<>();
        switch (RoleType)
        {
            case "AM":
                emptype.add("Select");
                emptype.add("MIO");
                try {
                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(TeamExpClaimReportActivity.this, android.R.layout.simple_spinner_item, emptype);
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
                ArrayAdapter<String> dataAdapterrsm = new ArrayAdapter<>(TeamExpClaimReportActivity.this, android.R.layout.simple_spinner_item, emptype);
                dataAdapterrsm.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                ftm.spinnerEmployeeType.setAdapter(dataAdapterrsm);
                break;
            case "NSM":
                emptype.add("Select");
                emptype.add("MIO");
                emptype.add("AM");
                emptype.add("DZSM");
                ArrayAdapter<String> dataAdapternsm = new ArrayAdapter<>(TeamExpClaimReportActivity.this, android.R.layout.simple_spinner_item, emptype);
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
                                //exception.printStackTrace();
                            }
                            if (mioList != null) {
                                System.out.println("mlist "+mioList.toString());
                                ArrayAdapter<MIO> dataAdapter = new ArrayAdapter<>(TeamExpClaimReportActivity.this, android.R.layout.simple_spinner_item, mioList);
                                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                ftm.spinnerApprovalMio.setAdapter(dataAdapter);
                                ftm.spinnerApprovalMio.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });
                            }
                            else {
                                SnackBarManagement._warning_CustomMessage(binding.masterLayout,"No MIO Found!!");
                            }
                        } catch (Exception exception) {
                            //exception.printStackTrace();
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
                                ArrayAdapter<ASM> dataAdapter = new ArrayAdapter<>(TeamExpClaimReportActivity.this, android.R.layout.simple_spinner_item, asmList);
                                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                ftm.spinnerApprovalAsm.setAdapter(dataAdapter);

                            }  else {
                                SnackBarManagement._warning_CustomMessage(binding.masterLayout,"No AM Founded!!");
                            }
                        } catch (Exception exception) {
                           // exception.printStackTrace();
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
                                //exception.printStackTrace();
                            }
                            if (rsmList != null) {
                                ArrayAdapter<RSM> dataAdapter = new ArrayAdapter<>(TeamExpClaimReportActivity.this, android.R.layout.simple_spinner_item, rsmList);
                                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                ftm.spinnerApprovalRsm.setAdapter(dataAdapter);
                            }
                            else {
                                SnackBarManagement._warning_CustomMessage(binding.masterLayout,"No AM Founded!!");
                            }
                        } catch (Exception exception) {
                           // exception.printStackTrace();
                        }
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ftm.filterMonthYearLayout.txtFromDate.setText(UtilityHelper._GetCurrentDate());
        ftm.filterMonthYearLayout.txtToDate.setText(UtilityHelper._GetCurrentDate());
        ftm.filterMonthYearLayout.ivDatePickerFromDate.setOnClickListener(v -> {
            UtilityHelper._datePickerDialogeForDates( ftm.filterMonthYearLayout.txtFromDate, TeamExpClaimReportActivity.this);
        });
        ftm.filterMonthYearLayout.ivDatePickerToDate.setOnClickListener(v -> {
            UtilityHelper._datePickerDialogeForDates(ftm.filterMonthYearLayout.txtToDate, TeamExpClaimReportActivity.this);
        });
        ftm.filterTypeLayout.tilDoctype.setVisibility(View.GONE);
        ftm.filterTypeLayout.tilProgtypetype.setVisibility(View.GONE);
        ftm.filterTypeLayout.tilCusttype.setVisibility(View.GONE);
        ftm.filterTypeLayout.tilPharmatype.setVisibility(View.GONE);
        ftm.filterTopLayout.btnDonefilter.setOnClickListener(v -> {
            switch (selectedTyp)
            {
                case "MIO":
                    MIO mioempid;
                    mioempid= (MIO) ftm.spinnerApprovalMio.getSelectedItem();
                    empid=String.valueOf(mioempid.getMIOEmpId());

                    break;
                case "AM":
                    ASM amempid=(ASM) ftm.spinnerApprovalAsm.getSelectedItem();
                    empid=String.valueOf(amempid.getASMEmpId());

                    break;
                case "DZSM":
                    RSM dzsmempid=(RSM) ftm.spinnerApprovalRsm.getSelectedItem();
                    empid=String.valueOf(dzsmempid.getRSMEmpId());
                    break;
            }

            fromdat=ftm.filterMonthYearLayout.txtFromDate.getText().toString();
            todate=ftm.filterMonthYearLayout.txtToDate.getText().toString();

            Map<String,String> filter2=new HashMap<>();
            filter2.put("Role",RoleType);
            status=(String) ftm.filterStatusLayout.spinnerApprovalStatus.getSelectedItem();
            if (status.equals("Select")) {
                filter2.put("AppStatus","");
            }else {
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
                filter2.put("AppStatus",status);
            }
            filter2.put("FromDt",fromdat);
            filter2.put("ToDt",todate);
            if(empid==null)
            {
                filter2.put("EmpId","");
            }else {
                filter2.put("EmpId",empid);
            }

            Constants.filtermap=filter2;
            Constants.filterparams=params;

            pd.setMessage("Loading..");
            pd.show();
            pd.setCancelable(false);
            presenter.GetExpenseCTeamList(params, filter2);

        });
        ftm.filterTopLayout.ivCancel.setOnClickListener(v -> {
            bsheetdlg.cancel();
        });

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Map<String,String> filterz=new HashMap<>();
        filterz.put("Role",RoleType);
        status=(String) ftm.filterStatusLayout.spinnerApprovalStatus.getSelectedItem();
        if (status.equals("Select")) {
            filterz.put("AppStatus","");
        }else {
            filterz.put("AppStatus",status);
        }
        filterz.put("FromDt",fromdat);
        filterz.put("ToDt",todate);
        if(empid==null)
        {
            filterz.put("EmpId","");
        }else {
            filterz.put("EmpId",empid);
        }
       // hitApi(params,filterz);
        pd.setMessage("Loading..");
        pd.show();
        pd.setCancelable(false);
        presenter.GetExpenseCTeamList(params, filterz);
    }

    @Override
    public void approveKlick(ExpListTeam elt) {
        if (RoleTypeId == 2) {
            next = elt.getRSMEMPId();
        }
        if (RoleTypeId == 3) {
            next = elt.getNSMEMPId();
        }
        if (RoleTypeId == 4) {
            next = 0;
        }
        if (RoleTypeId == 5) {
            myrole = 5;
        }

       int empIDS = Integer.parseInt(userInfo.get(SessionManagement.KEY_EmpId));
        try {
            ApproveExpRQ req=new ApproveExpRQ();
            int step = elt.getStep();
            int fstep = step + 1;
            req.setExpanseApprovalId(0);
            req.setFromEmpId(empIDS);
            req.setToEmpId(next);
            req.setTableId(elt.getExpenseClaimID());
            req.setStatus("Verified");//Accepted==approve for Admin
            req.setType(elt.getType());
            req.setStep(fstep);
            req.setEntryByApp(String.valueOf(empIDS));
            String entrydate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
            String entrytime = new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(new Date());
            req.setEntryDateApp(entrydate);
            req.setEntryTimeApp(entrytime);
            req.setMenuId(356);

            pd.setMessage("Loading..");
            pd.show();
            pd.setCancelable(false);
            presenter.SaveExpenseCTeam(req);
        } catch (NumberFormatException e) {
        }
    }
}