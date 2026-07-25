package com.creatrix.salessolution.Activity.Approval.VisitPlan;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toolbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.creatrix.salessolution.Activity.Approval.TourPlan.Model.TPApprovalSaveBody;
import com.creatrix.salessolution.Activity.Approval.TourPlan.Model.TourPlanApprovalData;
import com.creatrix.salessolution.Activity.Approval.TourPlan.TourPlanApprovalActivity;
import com.creatrix.salessolution.Activity.Approval.TourPlan.TourPlanApprovalAdapter;
import com.creatrix.salessolution.Activity.Approval.VisitPlan.Model.VPApprovalSaveBody;
import com.creatrix.salessolution.Activity.Approval.VisitPlan.Model.VisitPlanApprovalData;
import com.creatrix.salessolution.Activity.Team.Model.Team;
import com.creatrix.salessolution.DBAdapter.DBCrudHelper;
import com.creatrix.salessolution.Interface.IVPApproval;
import com.creatrix.salessolution.Interface.Rcv_TourPlanListener;
import com.creatrix.salessolution.Presenter.TPApprovalPresenter;
import com.creatrix.salessolution.Presenter.VPApprovalPresenter;
import com.creatrix.salessolution.R;
import com.creatrix.salessolution.UtilityHelper.SessionManagement;
import com.creatrix.salessolution.UtilityHelper.SnackBarManagement;
import com.creatrix.salessolution.databinding.ActivityVisitPlanApprovalBinding;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class VisitPlanApprovalActivity extends AppCompatActivity implements IVPApproval.View , Rcv_TourPlanListener {
    ActivityVisitPlanApprovalBinding binding;
    int empId;
    ProgressDialog pd;
    String yearList[] = {"2021", "2022", "2023", "2024", "2025", "2026", "2027", "2028", "2029", "2030"};
    String monthNameArray[] = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
    VisitPlanApprovalAdapter mAdapter;
    SessionManagement session;
    private int tourCount = 0;
    private int initCount = 0;

    Dialog popComment;
    private int prev = 0;
    private int current = 0;
    private int next = 0;
    private int myrole = 0;
    DBCrudHelper dbCrudHelper;
    int RoleTypeId, empid;
    String roleType;
    String prev_roleType, next_roleType;
    Button submitCmnt;
    Map<String, String> filter;
    String fromdat, todate, RoleType, params, tagA, tagR, tagN, Areaid, Regionid, Groupid, selectedTyp = "";
    Gson gson = new Gson();
    Team team;
    VPApprovalPresenter presenter;
    List<VisitPlanApprovalData> dataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVisitPlanApprovalBinding.inflate(getLayoutInflater());
        //setContentView(R.layout.activity_visit_plan_approval);
        setContentView(binding.getRoot());
        binding.toolbarCustom.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        SessionManagement session = new SessionManagement(getApplicationContext());
        session.checkLogin();
        HashMap<String, String> user = session.getUserDetails();
        empId = Integer.parseInt(user.get(SessionManagement.KEY_EmpId));
        RoleType = user.get(SessionManagement.KEY_EmpRoleType);
        RoleTypeId = Integer.parseInt(user.get(SessionManagement.KEY_EmpRoleTypeId));
        presenter = new VPApprovalPresenter(this, VisitPlanApprovalActivity.this);

        team = gson.fromJson(getIntent().getStringExtra("teamdat"), Team.class);
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
                    Regionid = String.valueOf(dbCrudHelper.getCurrentUserRegionId_SQLite(String.valueOf(empId)));
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

        initCommentPop();
        binding.btnReject.setOnClickListener(v -> {
            popComment.show();
        });

        ArrayAdapter<String> dataAdapterMonth = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, monthNameArray);
        dataAdapterMonth.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.monthSpinner.setAdapter(dataAdapterMonth);

        String monName = monthNameArray[month];
        int monthPos = dataAdapterMonth.getPosition(monName);
        binding.monthSpinner.setSelection(monthPos);
        binding.monthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // List<MonthDate> aMondateList = new ArrayList<>();
                int yearV = Integer.parseInt(binding.yearSpinner.getSelectedItem().toString());
                int monthV = i + 1;
                //  aMondateList = printDatesInMonth(yearV, i);
                filter = new HashMap<>();
                filter.put("Role", RoleType);
                filter.put("AppStatus", "");
                filter.put("FromDt", "");
                filter.put("ToDt", "");
                filter.put("EmpId", String.valueOf(team.getEmpInfoId()));
                filter.put("MonthValue", String.valueOf(monthV));
                filter.put("YearValue", String.valueOf(yearV));

                pd=new ProgressDialog(VisitPlanApprovalActivity.this);
                pd.setMessage("VisitPlan Loading...");
                pd.setCancelable(false);

                presenter.getVPApprovalList(params, filter);
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
                    int monthV = binding.monthSpinner.getSelectedItemPosition();
                    int yearV = Integer.parseInt(yearList[position].toString());
                    // aMondateList = printDatesInMonth(yearV, (monthV + 1));
                    filter = new HashMap<>();
                    filter.put("Role", RoleType);
                    filter.put("AppStatus", "");
                    filter.put("FromDt", "");
                    filter.put("ToDt", "");
                    filter.put("EmpId", String.valueOf(team.getEmpInfoId()));
                    filter.put("MonthValue", String.valueOf((monthV + 1)));
                    filter.put("YearValue", String.valueOf(yearV));
                    pd=new ProgressDialog(VisitPlanApprovalActivity.this);
                    pd.setMessage("VisitPlan Loading...");
                    pd.setCancelable(false);
                    presenter.getVPApprovalList(params, filter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    @Override
    public void onGetVPApprovalList(List<VisitPlanApprovalData> aList) {
        if (aList != null) {
            pd.dismiss();
            dataList = aList;
            binding.rvVisitplan.setVisibility(View.VISIBLE);
            if (RoleTypeId == 2) {
                prev = aList.get(0).getmIOEmpId();
                prev_roleType = "MIO";
                current = aList.get(0).getaSMEMPId();
                next = aList.get(0).getrSMEMPId();
                next_roleType = "AM";
                myrole = 2;
            }
            if (RoleTypeId == 3) {
                prev = aList.get(0).getaSMEMPId();
                prev_roleType = "AM";
                current = aList.get(0).getrSMEMPId();
                next = aList.get(0).getnSMEMPId();
                next_roleType = "DZSM";
                myrole = 3;
            }
            if (RoleTypeId == 4) {
                prev = aList.get(0).getrSMEMPId();
                prev_roleType = "DZSM";
                current = aList.get(0).getnSMEMPId();
                next_roleType = "ADMIN";
                next = 0;
                myrole = 4;
            }
            if (RoleTypeId == 5) {
                myrole = 5;
            }
           // mAdapter = new VisitPlanApprovalAdapter(aList.get(0).getaTourPlanMaster().getaTourPlanDate(), VisitPlanApprovalActivity.this, this);
            mAdapter = new VisitPlanApprovalAdapter(aList.get(0).getaDoctorVisitPlanMaster().getaDoctorVisitPlanDetail(), VisitPlanApprovalActivity.this, this);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            binding.rvVisitplan.setLayoutManager(mLayoutManager);
            binding.rvVisitplan.setItemAnimator(new DefaultItemAnimator());
            binding.rvVisitplan.setAdapter(mAdapter);
            binding.rvVisitplan.addItemDecoration(new DividerItemDecoration(getApplicationContext(),
                    DividerItemDecoration.VERTICAL));
            binding.rvVisitplan.setItemAnimator(null);
            binding.rvVisitplan.scrollToPosition(0);
            mAdapter.notifyDataSetChanged();
            if (prev == current) {
                if (aList.get(0).getRoleTypeId() == RoleTypeId) {
                    binding.btnApprove.setVisibility(View.GONE);
                    binding.btnReject.setVisibility(View.GONE);

                    binding.warnToast.setVisibility(View.VISIBLE);
                    binding.warnToast.setText("Waiting For Final Approval");
                    binding.warnToast.setBackgroundResource(R.drawable.shape_approved);
                } else {
                    //binding.approveLayout.btnApprove.setVisibility(View.VISIBLE);
                  //  binding.approveLayout.approvemaster.setVisibility(View.VISIBLE);
                    binding.linearLayout5.setVisibility(View.VISIBLE);
                }
            } else {
                if (aList.get(0).getToRoleTypeId() == RoleTypeId) {
                    binding.btnApprove.setVisibility(View.VISIBLE);
                    binding.btnReject.setVisibility(View.VISIBLE);
                } else {
                    binding.btnApprove.setVisibility(View.GONE);
                    binding.btnReject.setVisibility(View.GONE);
                    if (aList.get(0).getRoleTypeId() >= RoleTypeId) {
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
            if(aList.get(0).getStatus().equals("Accepted"))
            {
                binding.warnToast.setVisibility(View.GONE);
                binding.btnApprove.setVisibility(View.GONE);
                binding.btnReject.setVisibility(View.VISIBLE);
            }

            binding.btnApprove.setOnClickListener(v -> {
                VPApprovalSaveBody req = new VPApprovalSaveBody();
                int step = aList.get(0).getStep();
                int fstep = step + 1;
                req.setVisitPlanApprovalId(0);
                req.setFromEmpId(empid);
                req.setToEmpId(next);
                req.setTableId(aList.get(0).getTableId());
                req.setStatus("Verified");//Accepted==approve for Admin
                req.setType(aList.get(0).getType());
                req.setStep(fstep);
                req.setEntryByApp(empid);
                String entrydate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
                String entrytime = new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(new Date());
                req.setEntryDateApp(entrydate);
                req.setEntryTimeApp(entrytime);
                req.setMenuId(377);

                pd=new ProgressDialog(VisitPlanApprovalActivity.this);
                pd.setMessage("Verifying...");
                pd.setCancelable(false);
                presenter.SaveVPApproval(req);
            });
        } else {
            SnackBarManagement._warning_CustomMessage(binding.masterLayout, "No Visit Found");
        }
    }

    @Override
    public void onSaveSuccess(String message) {
        if (message.equals("Verified")) {
            pd.dismiss();
            new AlertDialog.Builder(this)
                    .setTitle("Success")
                    .setMessage(message)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            session = new SessionManagement(getApplicationContext());
                            ((AlertDialog) dialog).getButton(which).setVisibility(View.INVISIBLE);
                            onBackPressed();
                            finish();
                        }
                    }).setCancelable(false).show();
        }
    }

    @Override
    public void onError(String message) {
        binding.rvVisitplan.setVisibility(View.GONE);
        binding.warnToast.setVisibility(View.GONE);
        binding.btnApprove.setVisibility(View.GONE);
        binding.btnReject.setVisibility(View.GONE);
        SnackBarManagement._warning_CustomMessage(binding.masterLayout, message);
    }
    public void initCommentPop() {
        popComment = new Dialog(VisitPlanApprovalActivity.this);
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
                VPApprovalSaveBody req = new VPApprovalSaveBody();

                int step = dataList.get(0).getStep();
                int fstep = step + 1;
                req.setVisitPlanApprovalId(0);
                req.setFromEmpId(empid);
                req.setToEmpId(next);
                req.setTableId(dataList.get(0).getTableId());
                req.setStatus("Rejected");//Accepted==approve for admin
                req.setType(dataList.get(0).getType());
                req.setStep(fstep);
                req.setEntryByApp(empid);
                String entrydate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
                String entrytime = new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(new Date());
                req.setEntryDateApp(entrydate);
                req.setEntryTimeApp(entrytime);
                req.setMenuId(377);
                req.setComments(userComment.getText().toString());
                pd=new ProgressDialog(VisitPlanApprovalActivity.this);
                pd.setMessage("Rejecting...");
                pd.setCancelable(false);
                presenter.SaveVPApproval(req);

                SnackBarManagement._success_CustomMessage(v1, "Rejected");
                popComment.dismiss();
                onBackPressed();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
            }
        });
    }

    @Override
    public void ReloadCurrentActivity() {

    }
}