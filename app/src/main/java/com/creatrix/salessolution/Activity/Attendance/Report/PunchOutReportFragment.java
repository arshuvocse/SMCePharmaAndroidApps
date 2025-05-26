package com.creatrix.salessolution.Activity.Attendance.Report;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import com.creatrix.salessolution.Activity.Attendance.Model.ApproveRQ;
import com.creatrix.salessolution.Activity.Attendance.Model.AttenApproval;
import com.creatrix.salessolution.Activity.Attendance.Model.ButtonRP;
import com.creatrix.salessolution.Activity.Attendance.OnClick;
import com.creatrix.salessolution.DBAdapter.DBCrudHelper;
import com.creatrix.salessolution.Interface.IAttendance;
import com.creatrix.salessolution.Model.MarketStructure.AssignEmpTable.ASM;
import com.creatrix.salessolution.Model.MarketStructure.AssignEmpTable.MIO;
import com.creatrix.salessolution.Model.MarketStructure.AssignEmpTable.RSM;
import com.creatrix.salessolution.Presenter.AttendancePresenter;

import com.creatrix.salessolution.RecyclerAdapter._attenApprovalAdapter;
import com.creatrix.salessolution.UtilityHelper.SessionManagement;
import com.creatrix.salessolution.UtilityHelper.SnackBarManagement;
import com.creatrix.salessolution.UtilityHelper.UtilityHelper;
import com.creatrix.salessolution.databinding.FilterMasterBinding;
import com.creatrix.salessolution.databinding.FragmentPunchOutReportBinding;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;


public class PunchOutReportFragment extends Fragment implements IAttendance.View, OnClick {
    FragmentPunchOutReportBinding binding;
    SessionManagement session;
    HashMap<String, String> userInfo = new HashMap<>();
    AttendancePresenter presenter;
    // _teamListAdapter adapter;
    _attenApprovalAdapter adapter;
    String params, role;
    String tagA, tagR, tagN, Areaid, Regionid, Groupid, status;
    DBCrudHelper dbCrudHelper;

    int RoleTypeId;
    String RoleType, selectedTyp;
    List<MIO> mioList;

    BottomSheetDialog bsheetdlg;
    String fromdat, todate, empid;

    ProgressDialog pd;

    List<AttenApproval> allteamlist;
    private int prev = 0;
    private int current = 0;
    private int next = 0;
    private int roles = 0;
    private int myrole = 0;
    String prev_roleType, next_roleType;
    public PunchOutReportFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentPunchOutReportBinding.inflate(getLayoutInflater());
        //View v= inflater.inflate(binding, container, false);
        //return inflater.inflate(R.layout.fragment_punch_out_report, container, false);
        presenter = new AttendancePresenter(this, getActivity());
        session = new SessionManagement(getActivity());
        userInfo = session.getUserDetails();
        role = userInfo.get(SessionManagement.KEY_EmpRole);
        RoleTypeId = Integer.parseInt(userInfo.get(SessionManagement.KEY_EmpRoleTypeId));
        RoleType = userInfo.get(SessionManagement.KEY_EmpRoleType);
        pd = new ProgressDialog(getActivity());

        // tagA = ".AreaId=";
        tagA = ".EmpAreaId=";
        tagR = ".EmpRegionId=";
        tagN = ".EmpGroupId=";

        dbCrudHelper = new DBCrudHelper(getActivity());
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

        }
        Map<String, String> filter = new HashMap<>();
        filter.put("Role", RoleType);
        filter.put("AppStatus", "0");
        filter.put("AttType", "2");
        filter.put("FromDt", "");
        filter.put("ToDt", "");
        filter.put("EmpId", "");
        HitApi(params, filter);
       // presenter.teamAttendanceListFilter("TeamList", params, filter);
        binding.swipTeamatten.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.teamAttendanceListFilter("TeamList", params, filter);
                binding.swipTeamatten.setRefreshing(false);
            }
        });
        openFilter();
        binding.punchoutfilterbtns.setOnClickListener(v -> bsheetdlg.show());
        binding.approveall.setOnClickListener(v -> {
            //  allApproveAtten.onAllApprove(1);
            for (int i = 0; i < allteamlist.size(); i++) {
                if (RoleTypeId == 2) {
                    prev = allteamlist.get(i).getMIOEmpId();
                    prev_roleType = "MIO";
                    current = allteamlist.get(i).getASMEMPId();
                    next = allteamlist.get(i).getRSMEMPId();
                    next_roleType = "AM";
                    myrole = 2;
                }
                if (RoleTypeId == 3) {
                    prev = allteamlist.get(i).getASMEMPId();
                    prev_roleType = "AM";
                    current = allteamlist.get(i).getRSMEMPId();
                    next = allteamlist.get(i).getNSMEMPId();
                    next_roleType = "DZSM";
                    myrole = 3;
                }
                if (RoleTypeId == 4) {
                    prev = allteamlist.get(i).getRSMEMPId();
                    prev_roleType = "DZSM";
                    current = allteamlist.get(i).getNSMEMPId();
                    next_roleType = "ADMIN";
                    next = 0;
                    myrole = 4;
                }
                if (RoleTypeId == 5) {
                    myrole = 5;
                }
                int attenid = allteamlist.get(i).getAttendanceId();
                String step = allteamlist.get(i).getStep();
                approveAll(attenid,step);
            }
        });
        return binding.getRoot();
    }
    public void HitApi(String params, Map<String,String> filter) {
        pd.setMessage("Loading...");
        pd.show();
        presenter.teamAttendanceListFilter("TeamList", params, filter);
    }
    private void approveAll(int attenid, String stp) {
        session = new SessionManagement(getActivity());
        userInfo = session.getUserDetails();
        int empid = Integer.parseInt(Objects.requireNonNull(userInfo.get(SessionManagement.KEY_EmpId)));
        int step = Integer.parseInt(stp);
        int fstep = step + 1;

        // Toast.makeText(context, "empid : "+String.valueOf(empid), Toast.LENGTH_SHORT).show();
        ApproveRQ req = new ApproveRQ();
        req.setApprovalId(0);
        req.setFromEmpId(empid);
        req.setToEmpId(next);
        req.setTableId(attenid);
        req.setStatus("Verified");//Accepted==approve
        req.setType("Attendance");
        req.setStep(fstep);
        req.setEntryByApp(String.valueOf(empid));
        String entrydate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        String entrytime = new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(new Date());
        req.setEntryDateApp(entrydate);
        req.setEntryTimeApp(entrytime);
        req.setMenuId(301);

        //presenter = new AttendancePresenter(context);
        presenter.teamAttendanceApprove(req);

    }
    @Override
    public void onSuccessapprove(boolean t) {
        if(t)
        {
            pd.dismiss();
            new AlertDialog.Builder(requireActivity())
                    .setTitle("Success")
                    .setMessage("Approved")
                    .setPositiveButton("OK", (dialog, which) -> {
                        Map<String, String> filtern = new HashMap<>();
                        filtern.put("Role", RoleType);
                        filtern.put("AppStatus", "0");
                        filtern.put("AttType", "2");
                        filtern.put("FromDt", "");
                        filtern.put("ToDt", "");
                        filtern.put("EmpId", "");
                       HitApi(params, filtern);
                        dialog.cancel();
                    }).setCancelable(false).show();
        }else {
            pd.dismiss();
        }
    }
    @Override
    public void onSuccessapproveAll(String t) {
        if (t.equals("true")) {
            pd.dismiss();
            new AlertDialog.Builder(requireActivity())
                    .setTitle("Success")
                    .setMessage("Approved")
                    .setPositiveButton("OK", (dialog, which) -> {
                        Map<String, String> filter = new HashMap<>();
                        filter.put("Role", RoleType);
                        filter.put("AppStatus", "0");
                        filter.put("AttType", "2");
                        filter.put("FromDt", "");
                        filter.put("ToDt", "");
                        filter.put("EmpId", "");
                        HitApi(params, filter);
                        //presenter.teamAttendanceListFilter("TeamList", params, filter);
                        dialog.cancel();
                        //  getActivity().finish();
                    }).setCancelable(false).show();
        } else {
            pd.dismiss();
        }
    }

    @Override
    public void onSuccess(String message, boolean status, int type) {

    }
    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onSuccessTeamAtten(List<AttenApproval> teamlist) {
        pd.dismiss();
        allteamlist = teamlist;
        _attenApprovalAdapter adapter = new _attenApprovalAdapter(getActivity(), teamlist, RoleTypeId, RoleType, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        binding.rvOut.setLayoutManager(mLayoutManager);
        binding.rvOut.setItemAnimator(new DefaultItemAnimator());
        binding.rvOut.setAdapter(adapter);
        binding.rvOut.setItemAnimator(null);
        binding.rvOut.scrollToPosition(0);
        adapter.notifyDataSetChanged();
        if (teamlist==null || teamlist.size() == 0) {
            binding.nodtas.setVisibility(View.VISIBLE);
            binding.rvOut.setVisibility(View.GONE);
        } else {
            binding.nodtas.setVisibility(View.GONE);
            binding.rvOut.setVisibility(View.VISIBLE);
        }

      /*  if (teamlist != null && teamlist.size()>0) {
            // binding.allaprrovebtn.setVisibility(View.VISIBLE);
            allteamlist = teamlist;
            adapter = new _attenApprovalAdapter(getActivity(), teamlist, RoleTypeId, RoleType, this);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
            binding.rvOut.setLayoutManager(mLayoutManager);
            binding.rvOut.setItemAnimator(new DefaultItemAnimator());
            binding.rvOut.setAdapter(adapter);
            binding.rvOut.setItemAnimator(null);
            binding.rvOut.scrollToPosition(0);
            adapter.notifyDataSetChanged();
            if (teamlist.size() == 0) {
                binding.nodta.setVisibility(View.VISIBLE);
            } else {
                binding.nodta.setVisibility(View.GONE);
            }
        }
        else {
            binding.nodta.setVisibility(View.VISIBLE);
            teamlist.clear();
            adapter.notifyDataSetChanged();
        }*/
    }

    @Override
    public void onError(String message, int type) {
        pd.dismiss();
        if(type==1)
        {
            SnackBarManagement._warning_CustomMessage(getView(),"No Response Found");
        }
    }

    @Override
    public void onButtonView(ButtonRP buttonRP) {

    }
    private void openFilter() {
        FilterMasterBinding ftm;
        ftm = FilterMasterBinding.inflate(getLayoutInflater());
        bsheetdlg = new BottomSheetDialog(requireActivity());
        bsheetdlg.setContentView(ftm.getRoot());
        bsheetdlg.setCanceledOnTouchOutside(true);
        ftm.filterTypeLayout.tilDoctype.setVisibility(View.GONE);
        ftm.filterTypeLayout.tilProgtypetype.setVisibility(View.GONE);
        ftm.filterTypeLayout.tilCusttype.setVisibility(View.GONE);
        ftm.filterTypeLayout.tilPharmatype.setVisibility(View.GONE);
        // Employee Type
        List<String> emptype = new ArrayList<>();
        switch (role) {
            case "AM":
                emptype.add("Select");
                emptype.add("MIO");
                try {
                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, emptype);
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
                ArrayAdapter<String> dataAdapterrsm = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, emptype);
                dataAdapterrsm.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                ftm.spinnerEmployeeType.setAdapter(dataAdapterrsm);
                break;
            case "NSM":
                emptype.add("Select");
                emptype.add("MIO");
                emptype.add("AM");
                emptype.add("DZSM");
                ArrayAdapter<String> dataAdapternsm = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, emptype);
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
                            mioList = dbCrudHelper.getMIOList_SQLite();
                            if (mioList != null) {
                                ArrayAdapter<MIO> dataAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, mioList);
                                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                ftm.spinnerApprovalMio.setAdapter(dataAdapter);

/*                                MIO mioempid;
                                mioempid = (MIO) ftm.spinnerApprovalMio.getSelectedItem();
                                empid = String.valueOf(mioempid.getMIOEmpId());*/
                            } else {
                                SnackBarManagement._warning_CustomMessage(getView(), "No MIO Found!!");
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
                            List<ASM> asmList = dbCrudHelper.getASMList_SQLite();
                            if (asmList != null) {
                                ArrayAdapter<ASM> dataAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, asmList);
                                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                ftm.spinnerApprovalAsm.setAdapter(dataAdapter);

                               /* ASM amempid = (ASM) ftm.spinnerApprovalAsm.getSelectedItem();
                                empid = String.valueOf(amempid.getASMEmpId());*/
                            } else {
                                SnackBarManagement._warning_CustomMessage(getView(), "No AM Founded!!");
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

                            List<RSM> rsmList = dbCrudHelper.getRSMList_SQLite();
                            if (rsmList != null) {
                                ArrayAdapter<RSM> dataAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, rsmList);
                                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                ftm.spinnerApprovalRsm.setAdapter(dataAdapter);

                               /* RSM dzsmempid = (RSM) ftm.spinnerApprovalRsm.getSelectedItem();
                                empid = String.valueOf(dzsmempid.getRSMEmpId());*/
                            } else {
                                SnackBarManagement._warning_CustomMessage(getView(), "No AM Founded!!");
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

        ftm.filterMonthYearLayout.txtFromDate.setText(UtilityHelper._GetCurrentDate());
        ftm.filterMonthYearLayout.txtToDate.setText(UtilityHelper._GetCurrentDate());
        ftm.filterMonthYearLayout.ivDatePickerFromDate.setOnClickListener(v ->
                UtilityHelper._datePickerDialogeForDates(ftm.filterMonthYearLayout.txtFromDate, getActivity()));
        ftm.filterMonthYearLayout.ivDatePickerToDate.setOnClickListener(v ->
                UtilityHelper._datePickerDialogeForDates(ftm.filterMonthYearLayout.txtToDate, getActivity()));
        ftm.filterTopLayout.btnDonefilter.setOnClickListener(v -> {
            switch (selectedTyp) {
                case "MIO":
                    MIO mioempid;
                    mioempid = (MIO) ftm.spinnerApprovalMio.getSelectedItem();
                    empid = String.valueOf(mioempid.getMIOEmpId());

                    break;
                case "AM":
                    ASM amempid = (ASM) ftm.spinnerApprovalAsm.getSelectedItem();
                    empid = String.valueOf(amempid.getASMEmpId());

                    break;
                case "DZSM":
                    RSM dzsmempid = (RSM) ftm.spinnerApprovalRsm.getSelectedItem();
                    empid = String.valueOf(dzsmempid.getRSMEmpId());
                    break;
            }

            status = (String) ftm.filterStatusLayout.spinnerApprovalStatus.getSelectedItem();
            fromdat = ftm.filterMonthYearLayout.txtFromDate.getText().toString();
            todate = ftm.filterMonthYearLayout.txtToDate.getText().toString();

            Map<String, String> filters = new HashMap<>();
            filters.put("Role", RoleType);
            if (status.equals("Select")) {
                filters.put("AppStatus", "0");
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
            filters.put("AttType", "2");
            filters.put("FromDt", fromdat);
            filters.put("ToDt", todate);
            if (empid == null) {
                filters.put("EmpId", "");
            } else {
                filters.put("EmpId", empid);
            }
            HitApi(params, filters);
            bsheetdlg.cancel();
        });
        ftm.filterTopLayout.ivCancel.setOnClickListener(v ->
                bsheetdlg.cancel());
    }

    @Override
    public void clickItem(int Pos, int empId, int next, int tableId, int fstep, boolean type) {
        if(type)
        {
            ApproveRQ req = new ApproveRQ();
            req.setApprovalId(0);
            req.setFromEmpId(empId);
            req.setToEmpId(next);
            req.setTableId(tableId);
            req.setStatus("Verified");//Accepted==approve
            req.setType("Attendance");
            req.setStep(fstep);
            req.setEntryByApp(String.valueOf(empId));
            String entrydate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
            String entrytime = new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(new Date());
            req.setEntryDateApp(entrydate);
            req.setEntryTimeApp(entrytime);
            req.setMenuId(301);
            //presenter = new AttendancePresenter(this,getActivity());
            Gson gson=new Gson();
            String data=gson.toJson(req);
            System.out.println("atten:"+data);
            pd.show();
            presenter.teamAttendanceApprove(req);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Map<String, String> filter = new HashMap<>();
        filter.put("Role", RoleType);
        filter.put("AppStatus", "0");
        filter.put("AttType", "2");
        filter.put("FromDt", "");
        filter.put("ToDt", "");
        filter.put("EmpId", "");
        HitApi(params, filter);
    }

}