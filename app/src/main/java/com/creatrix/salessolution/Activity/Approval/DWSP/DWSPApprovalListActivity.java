package com.creatrix.salessolution.Activity.Approval.DWSP;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;


import com.creatrix.salessolution.Activity.Approval.DWSP.Model.DWSPApprovalData;
import com.creatrix.salessolution.Activity.DWSP.Model.DWSPApprovalSaveBody;
import com.creatrix.salessolution.Activity.Team.Model.Team;
import com.creatrix.salessolution.DBAdapter.DBCrudHelper;
import com.creatrix.salessolution.Interface.IDWSPApproval;
import com.creatrix.salessolution.Presenter.DWSPApprovalPresenter;
import com.creatrix.salessolution.R;
import com.creatrix.salessolution.UtilityHelper.SessionManagement;
import com.creatrix.salessolution.UtilityHelper.SnackBarManagement;
import com.creatrix.salessolution.databinding.ActivityDwspapprovalListBinding;
import com.creatrix.salessolution.databinding.BottomSheetDwspstatusBinding;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;
public class DWSPApprovalListActivity extends AppCompatActivity implements IDWSPApproval.View {
    ActivityDwspapprovalListBinding binding;
    DWSPApprovalPresenter presenter;
    String yearList[] = {"2021", "2022", "2023", "2024", "2025", "2026", "2027", "2028", "2029", "2030"};
    String monthNameArray[] = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
    SessionManagement session;
    DBCrudHelper dbCrudHelper;
    int empid,RoleTypeId;
    private int initCount = 0;
    String RoleType,params, tagA, tagR, tagN, Areaid, Regionid, Groupid, selectedTyp = "";
    BottomSheetDialog bsheetdlg;
    BottomSheetDwspstatusBinding btbinding;
    Gson gson = new Gson();
    Team team;
    private int prev = 0;
    private int current = 0;
    private int next = 0;
    private int myrole = 0;
    DWSPApprovalAdapter mAdapter;
    String prev_roleType, next_roleType;
    String cvalue,fcb,general,targetAmount;
    ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDwspapprovalListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.toolbarCustom.setNavigationOnClickListener(view -> finish());
        pd=new ProgressDialog(DWSPApprovalListActivity.this);
        team = gson.fromJson(getIntent().getStringExtra("teamdat"), Team.class);
        session = new SessionManagement(getApplicationContext());
        presenter =new DWSPApprovalPresenter(this,DWSPApprovalListActivity.this);
        HashMap<String, String> user = session.getUserDetails();
        empid = Integer.parseInt(Objects.requireNonNull(user.get(SessionManagement.KEY_EmpId)));
        RoleType = user.get(SessionManagement.KEY_EmpRoleType);
        RoleTypeId = Integer.parseInt(Objects.requireNonNull(user.get(SessionManagement.KEY_EmpRoleTypeId)));

        Calendar mCalendar = Calendar.getInstance();
        int year = mCalendar.get(Calendar.YEAR);
        int month = mCalendar.get(Calendar.MONTH);
        int day = mCalendar.get(Calendar.DAY_OF_MONTH);

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, yearList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.yearSpinner.setAdapter(dataAdapter);
        int yearPos = dataAdapter.getPosition(String.valueOf(year));
        binding.yearSpinner.setSelection(yearPos);
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
            exception.printStackTrace();
        }
        ArrayAdapter<String> dataAdapterMonth = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, monthNameArray);
        dataAdapterMonth.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.monthSpinner.setAdapter(dataAdapterMonth);

        String monName = monthNameArray[month];
        int monthPos = dataAdapterMonth.getPosition(monName);
        binding.monthSpinner.setSelection(monthPos);

        binding.monthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                int yearV = Integer.parseInt(binding.yearSpinner.getSelectedItem().toString());
                int monthV = i + 1;
                HashMap<String,String> filter = new HashMap<>();
                filter.put("Role", RoleType);
                filter.put("AppStatus", "");
                filter.put("FromDt", "");
                filter.put("ToDt", "");
                filter.put("EmpId", String.valueOf(team.getEmpInfoId()));
                filter.put("MonthValue", String.valueOf(monthV));
                filter.put("YearValue", String.valueOf(yearV));
                hitApi(filter);
                //initCount++;
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });
        binding.yearSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (initCount > 0) {
                    //  List<MonthDate> aMondateList = new ArrayList<>();
                    int monthV = binding.monthSpinner.getSelectedItemPosition();
                    int yearV = Integer.parseInt(yearList[position].toString());
                    // aMondateList = printDatesInMonth(yearV, (monthV + 1));
                    HashMap<String,String> filter = new HashMap<>();
                    filter.put("Role", RoleType);
                    filter.put("AppStatus", "");
                    filter.put("FromDt", "");
                    filter.put("ToDt", "");
                    filter.put("EmpId", String.valueOf(team.getEmpInfoId()));
                    filter.put("MonthValue", String.valueOf((monthV + 1)));
                    filter.put("YearValue", String.valueOf(yearV));
                    //presenter.getDWSPApprovalList(params, filter);
                    hitApi(filter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        openDetails();
        binding.statusClick.setOnClickListener(v ->bsheetdlg.show() );

    }

    private void hitApi(HashMap<String, String> filter) {
        pd.setMessage("Loading...");
        pd.setCancelable(false);
        pd.show();
        presenter.getDWSPApprovalList(params, filter);
    }

    private void openDetails() {
        btbinding = BottomSheetDwspstatusBinding.inflate(getLayoutInflater());
        bsheetdlg = new BottomSheetDialog(DWSPApprovalListActivity.this);
        bsheetdlg.setContentView(btbinding.getRoot());
        bsheetdlg.setCanceledOnTouchOutside(true);
        btbinding.dwsptotaltarget.setText(targetAmount);
        btbinding.gvalue.setText(general);
        btbinding.fcbvalue.setText(fcb);
        btbinding.cvalue.setText(cvalue);

    }
    @Override
    public void onGetDWSPApprovalList(DWSPApprovalData aData) {
        if(pd!=null||pd.isShowing())
        {
            pd.dismiss();
        }
        if (aData != null) {
           // dataList = aList;
            binding.rvDwsp.setVisibility(View.VISIBLE);

            if (RoleTypeId == 2) {
                prev = aData.getmIOEmpId();
                prev_roleType = "MIO";
                current = aData.getaSMEMPId();
                next = aData.getrSMEMPId();
                next_roleType = "AM";
                myrole = 2;
            }
            if (RoleTypeId == 3) {
                prev = aData.getaSMEMPId();
                prev_roleType = "AM";
                current = aData.getrSMEMPId();
                next = aData.getnSMEMPId();
                next_roleType = "DZSM";
                myrole = 3;
            }
            if (RoleTypeId == 4) {
                prev = aData.getrSMEMPId();
                prev_roleType = "DZSM";
                current = aData.getnSMEMPId();
                next_roleType = "ADMIN";
                next = 0;
                myrole = 4;
            }
            if (RoleTypeId == 5) {
                myrole = 5;
            }

            btbinding.cvalue.setText(aData.getaDWSPMasterDAO().getCampaignAmount());
            btbinding.fcbvalue.setText(aData.getaDWSPMasterDAO().getfCBAmount());
            btbinding.gvalue.setText(aData.getaDWSPMasterDAO().getGeneralAmount());
            btbinding.dwsptotaltarget.setText(aData.getaDWSPMasterDAO().getTargetAmount());

            cvalue = aData.getaDWSPMasterDAO().getCampaignAmount();
            general = aData.getaDWSPMasterDAO().getGeneralAmount();
            fcb = aData.getaDWSPMasterDAO().getfCBAmount();
            targetAmount = aData.getaDWSPMasterDAO().getTargetAmount();

            mAdapter = new DWSPApprovalAdapter(aData.getaDWSPMasterDAO().getaDWSPDate(), DWSPApprovalListActivity.this);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            binding.rvDwsp.setLayoutManager(mLayoutManager);
            binding.rvDwsp.setItemAnimator(new DefaultItemAnimator());
            binding.rvDwsp.setAdapter(mAdapter);
            binding.rvDwsp.addItemDecoration(new DividerItemDecoration(getApplicationContext(),
                    DividerItemDecoration.VERTICAL));
            binding.rvDwsp.setItemAnimator(null);
            binding.rvDwsp.scrollToPosition(0);
            mAdapter.notifyDataSetChanged();
            if (prev == current) {
                if (aData.getRoleTypeId() == RoleTypeId) {
                    binding.btnApprove.setVisibility(View.GONE);
                    binding.btnReject.setVisibility(View.GONE);

                    binding.warnToast.setVisibility(View.VISIBLE);
                    binding.warnToast.setText("Waiting For Final Approval");
                    binding.warnToast.setBackgroundResource(R.drawable.shape_approved);
                } else {
                    //binding.approveLayout.btnApprove.setVisibility(View.VISIBLE);
                    binding.linearLayout5.setVisibility(View.VISIBLE);
                }
            } else {
                if (aData.getToRoleTypeId() == RoleTypeId) {
                    binding.btnApprove.setVisibility(View.VISIBLE);
                    binding.btnReject.setVisibility(View.VISIBLE);
                } else {
                    binding.btnApprove.setVisibility(View.GONE);
                    binding.btnReject.setVisibility(View.GONE);
                    if (aData.getRoleTypeId() >= RoleTypeId) {
                        binding.warnToast.setVisibility(View.VISIBLE);
                        binding.warnToast.setText("Waiting For Final Approval");
                        // binding.approveLayout.approvemaster.setVisibility(View.GONE);
                        binding.warnToast.setBackgroundResource(R.drawable.shape_approved);
                    } else {
                        binding.warnToast.setVisibility(View.VISIBLE);
                        binding.warnToast.setText("Need To Approved By " + prev_roleType);
                        //binding.approveLayout.approvemaster.setVisibility(View.GONE);
                        binding.warnToast.setBackgroundResource(R.drawable.shape_pending);
                    }
                }

            }
            if (aData.getStatus().equals("Accepted")) {
                binding.warnToast.setVisibility(View.GONE);
                binding.btnApprove.setVisibility(View.GONE);
                binding.btnReject.setVisibility(View.VISIBLE);
            }
            binding.btnApprove.setOnClickListener(v -> {
                DWSPApprovalSaveBody req = new DWSPApprovalSaveBody();
                int step = aData.getStep();
                int fstep = step + 1;
                req.setDWSPApprovalId(0);
                req.setFromEmpId(empid);
                req.setToEmpId(next);
                req.setTableId(aData.getTableId());
                req.setStatus("Verified");//Accepted==approve for Admin
                req.setType(aData.getType());
                req.setStep(fstep);
                req.setEntryByApp(empid);
                String entrydate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
                String entrytime = new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(new Date());
                req.setEntryDateApp(entrydate);
                req.setEntryTimeApp(entrytime);
                req.setMenuId(3022);
                Gson gson=new Gson();
                String data=gson.toJson(req);
                System.out.println("req: "+data);
                presenter.SaveDWSPApproval(req);
            });
            binding.btnReject.setOnClickListener(v -> {
                DWSPApprovalSaveBody req = new DWSPApprovalSaveBody();
                int step = aData.getStep();
                int fstep = step + 1;
                req.setDWSPApprovalId(0);
                req.setFromEmpId(empid);
                req.setToEmpId(next);
                req.setTableId(aData.getTableId());
                req.setStatus("Rejected");//Accepted==approve for Admin
                req.setType(aData.getType());
                req.setStep(fstep);
                req.setEntryByApp(empid);
                String entrydate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
                String entrytime = new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(new Date());
                req.setEntryDateApp(entrydate);
                req.setEntryTimeApp(entrytime);
                req.setMenuId(3022);
                presenter.SaveDWSPApproval(req);
            });
        } else {
            SnackBarManagement._warning_CustomMessage(binding.getRoot(), "No DWSP Found");
        }
    }

    @Override
    public void onSaveSuccess(String message) {
        if (message.equals("Action")) {
            new AlertDialog.Builder(this)
                    .setTitle("Success")
                    .setMessage(message)
                    .setPositiveButton("OK", (dialog, which) -> {
                        session = new SessionManagement(getApplicationContext());
                        ((AlertDialog) dialog).getButton(which).setVisibility(View.INVISIBLE);
                        onBackPressed();
                        finish();
                    }).setCancelable(false).show();
        }
    }

    @Override
    public void onError(String message) {
        if(pd!=null || pd.isShowing())
        {
            pd.dismiss();
        }
        binding.rvDwsp.setVisibility(View.GONE);
        binding.warnToast.setVisibility(View.GONE);
        binding.btnApprove.setVisibility(View.GONE);
        binding.btnReject.setVisibility(View.GONE);
        SnackBarManagement._warning_CustomMessage(binding.getRoot(), message);
    }

}