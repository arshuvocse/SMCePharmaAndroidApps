package com.creatrix.salessolution.Activity.Doctor.Approval;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.creatrix.salessolution.Activity.Doctor.Approval.Model.DoctorApprovalList;
import com.creatrix.salessolution.Activity.Doctor.Approval.Model.DoctorApprovalRQ;
import com.creatrix.salessolution.DBAdapter.DBCrudHelper;
import com.creatrix.salessolution.Interface.IDoctor;
import com.creatrix.salessolution.Model.Doctor.Brand;
import com.creatrix.salessolution.Model.Doctor.ContactTypeVM;
import com.creatrix.salessolution.Model.Doctor.DoctorCategory;
import com.creatrix.salessolution.Model.Doctor.DoctorChamberTypeVM;
import com.creatrix.salessolution.Model.Doctor.DoctorDegreeViewModel;
import com.creatrix.salessolution.Model.Doctor.DoctorDesignation;
import com.creatrix.salessolution.Model.Doctor.DoctorSpecialityViewModel;
import com.creatrix.salessolution.Model.Doctor.DoctorTypeVM;
import com.creatrix.salessolution.Model.Doctor.ProgramType;
import com.creatrix.salessolution.Model.Doctor.SpecialDay;
import com.creatrix.salessolution.Model.InstitutionVM;
import com.creatrix.salessolution.Model.ModelProviderType;
import com.creatrix.salessolution.Model.ModelSMCType;
import com.creatrix.salessolution.Presenter.DoctorPresenter;
import com.creatrix.salessolution.R;
import com.creatrix.salessolution.Services.Constants;
import com.creatrix.salessolution.UtilityHelper.SessionManagement;
import com.creatrix.salessolution.UtilityHelper.SnackBarManagement;
import com.creatrix.salessolution.UtilityHelper.UtilityHelper;
import com.creatrix.salessolution.databinding.ActivityDoctorApprovalBinding;
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
import java.util.Objects;

public class DoctorApprovalListActivity extends AppCompatActivity implements IDoctorApproval.View, IDoctor.View,doctorTeamApprovalAdapter.approveTDoc {
    ActivityDoctorApprovalBinding binding;
    doctorTeamApprovalAdapter adapter;
    DoctorApprovalPresenter presenter;
    DoctorPresenter dpresenter;
    FilterMasterBinding ftm;
    SessionManagement session;
    DBCrudHelper dbCrudHelper;
    HashMap<String, String> userInfo = new HashMap<>();
    
    String fromdat, todate, RoleType, params, tagA, tagR, tagN, Areaid, Regionid, Groupid, selectedTyp = "";
    int RoleTypeId, empid, pending;
    List<DoctorTypeVM> dtypeList;
    List<ModelProviderType> providertypeList;
    List<ModelSMCType> pharmatypeList;
    BottomSheetDialog bsheetdlg;
    String selecteddoctortypeId="0", selectedprogramId="0",selectedpharmatypeId="0";

    private int next = 0;
    ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDoctorApprovalBinding.inflate(getLayoutInflater());
        //setContentView(R.layout.activity_doctor_approval);
        setContentView(binding.getRoot());
        pd=new ProgressDialog(DoctorApprovalListActivity.this);
        ftm = FilterMasterBinding.inflate(getLayoutInflater());
        binding.toolbarCustom.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        session = new SessionManagement(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        int empId = Integer.parseInt(user.get(SessionManagement.KEY_EmpId));
        presenter = new DoctorApprovalPresenter(this, this);
        dpresenter = new DoctorPresenter(this, this);
        session = new SessionManagement(getApplicationContext());
        userInfo = session.getUserDetails();
        empid = Integer.parseInt(Objects.requireNonNull(userInfo.get(SessionManagement.KEY_EmpId)));
        RoleTypeId = Integer.parseInt(Objects.requireNonNull(userInfo.get(SessionManagement.KEY_EmpRoleTypeId)));
        RoleType = userInfo.get(SessionManagement.KEY_EmpRoleType);

        //dpresenter.GetProgramType(0);
        dpresenter.GetProviderType(0);
        dpresenter.GetDoctorType(0);
        dpresenter.GetSMCType(0);

        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);

        //binding.selectedMonthTxt.setText(UtilityHelper.monthNameArrayFull[month] + ',' + year);
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
            Map<String, String> filter = new HashMap<>();
            filter.put("Role", RoleType);
            filter.put("AppStatus", "");
            filter.put("FromDt", "");
            filter.put("ToDt", "");
            filter.put("ProgramTypeId","");
            filter.put("DoctorTypeId", "");
            filter.put("EmpId", "");

            filter.put("providertype","0");
            filter.put("pharmatype","0");
            filter.put("doctortype","0");
            Constants.filtermap=filter;
            Constants.filterparams=params;
            hitApi(params,filter);
           // presenter.GetDoctorApprovalList(params, filter, binding.master);
            openFilter();
            binding.atteFilter.setOnClickListener(v -> bsheetdlg.show());
        } catch (Exception exception) {
           // exception.printStackTrace();
        }
        binding.swipteamdoctor.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Map<String, String> filters = new HashMap<>();
                filters.put("Role", RoleType);
                filters.put("AppStatus", "");
                filters.put("FromDt", "");
                filters.put("ToDt", "");
                filters.put("ProgramTypeId","");
                filters.put("DoctorTypeId", "");
                filters.put("EmpId", "");
                filters.put("providertype","0");
                filters.put("pharmatype","0");
                filters.put("doctortype","0");
                presenter.GetDoctorApprovalList(params, filters, binding.master);
                binding.swipteamdoctor.setRefreshing(false);
            }
        });


        //openFilter();
    }

    private void openFilter() {
        //ftm = FilterMasterBinding.inflate(getLayoutInflater());
        bsheetdlg = new BottomSheetDialog(DoctorApprovalListActivity.this);
        bsheetdlg.setContentView(ftm.getRoot());
        // bsheetdlg.setContentView(R.layout.filter_master);
        bsheetdlg.setCanceledOnTouchOutside(true);
        ftm.linearLayoutemp.setVisibility(View.GONE);
        ftm.filterStatusLayout.linearLayout.setVisibility(View.GONE);
        ftm.filterTypeLayout.tilCusttype.setVisibility(View.GONE);

        ftm.filterMonthYearLayout.txtFromDate.setText(UtilityHelper._GetCurrentDate());
        ftm.filterMonthYearLayout.txtToDate.setText(UtilityHelper._GetCurrentDate());
        ftm.filterMonthYearLayout.ivDatePickerFromDate.setOnClickListener(v -> {
            UtilityHelper._datePickerDialogeForDates(ftm.filterMonthYearLayout.txtFromDate, DoctorApprovalListActivity.this);
        });
        ftm.filterMonthYearLayout.ivDatePickerToDate.setOnClickListener(v -> {
            UtilityHelper._datePickerDialogeForDates(ftm.filterMonthYearLayout.txtToDate, DoctorApprovalListActivity.this);
        });

        DoctorTypeVM r = new DoctorTypeVM();
        r.setDoctorTypeName("Select");
        dtypeList.add(0, r);
        ArrayAdapter<DoctorTypeVM> doctorTypeAdapter = new ArrayAdapter<>(DoctorApprovalListActivity.this, R.layout._custom_spinner_tv, dtypeList);
        ftm.filterTypeLayout.spinnerDoctortype.setAdapter(doctorTypeAdapter);
        ftm.filterTypeLayout.spinnerDoctortype.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if(position==0)
                {
                    selecteddoctortypeId="0";
                }else {
                    DoctorTypeVM cmm = (DoctorTypeVM) doctorTypeAdapter.getItem(position);
                    selecteddoctortypeId = String.valueOf(cmm.getDoctorTypeId());
                }
            }
        });

        ModelProviderType ptype = new ModelProviderType();
        ptype.setProviderType("Select");
        providertypeList.add(0, ptype);
        ArrayAdapter<ModelProviderType> providerAdapter = new ArrayAdapter<>(DoctorApprovalListActivity.this, R.layout._custom_spinner_tv, providertypeList);
        ftm.filterTypeLayout.spinnerProgramtype.setAdapter(providerAdapter);
        ftm.filterTypeLayout.spinnerProgramtype.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if(position==0)
                {
                    selectedprogramId="0";
                }else {
                    ModelProviderType cmm = (ModelProviderType) providerAdapter.getItem(position);
                    selectedprogramId = String.valueOf(cmm.getProviderTypeId());
                }
            }
        });

        ArrayAdapter<ModelSMCType> pharmaAdapter = new ArrayAdapter<>(DoctorApprovalListActivity.this, R.layout._custom_spinner_tv, pharmatypeList);
        ftm.filterTypeLayout.spinnerPharmatype.setAdapter(pharmaAdapter);
        ftm.filterTypeLayout.spinnerPharmatype.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                ModelSMCType cmm = (ModelSMCType) pharmaAdapter.getItem(position);
                selectedpharmatypeId = String.valueOf(cmm.getSMCTypeId());
            }
        });


        ftm.filterTopLayout.btnDonefilter.setOnClickListener(v -> {
            fromdat = ftm.filterMonthYearLayout.txtFromDate.getText().toString();
            todate = ftm.filterMonthYearLayout.txtToDate.getText().toString();
            HashMap<String, String> filter = new HashMap<>();
            filter.put("Role", RoleType);
            filter.put("AppStatus", "");
            filter.put("FromDt", fromdat);
            filter.put("ToDt", todate);
            filter.put("ProgramTypeId", selectedprogramId);
            filter.put("DoctorTypeId", selecteddoctortypeId);
            filter.put("EmpId", "");
            filter.put("providertype",selectedprogramId);
            filter.put("pharmatype",selectedpharmatypeId);
            filter.put("doctortype",selecteddoctortypeId);
            Constants.filterparams=params;
            Constants.filtermap=filter;
            hitApi(params,filter);
           // presenter.GetDoctorApprovalList(params, filter, binding.master);
            bsheetdlg.cancel();
        });
        ftm.filterTopLayout.ivCancel.setOnClickListener(v -> {
            bsheetdlg.dismiss();
        });
    }
    private void hitApi(String params, Map<String, String> filter) {
        presenter.GetDoctorApprovalList(params, filter, binding.master);
    }
    @Override
    public void OnRevieveDoctorApproval(List<DoctorApprovalList> aList) {
        if(pd!=null || pd.isShowing())
        {
            pd.dismiss();
        }
        try {
            if (aList != null) {
                pending = aList.size();
                binding.pending.setText(String.valueOf(pending));
                adapter = new doctorTeamApprovalAdapter(this, aList,this);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(DoctorApprovalListActivity.this);
                binding.rvDoctorapproval.setLayoutManager(mLayoutManager);
                binding.rvDoctorapproval.setItemAnimator(new DefaultItemAnimator());
                binding.rvDoctorapproval.setAdapter(adapter);
                binding.rvDoctorapproval.setItemAnimator(null);
                binding.rvDoctorapproval.scrollToPosition(0);
                adapter.notifyDataSetChanged();
                //binding.userCount.setText(String.valueOf(teamlist.size()));
                if (aList.size() == 0) {
                    binding.nodta.setVisibility(View.VISIBLE);
                    binding.rvDoctorapproval.setVisibility(View.GONE);
                } else {
                    binding.nodta.setVisibility(View.GONE);
                    binding.rvDoctorapproval.setVisibility(View.VISIBLE);
                }
            } else {
                binding.nodta.setVisibility(View.VISIBLE);
                binding.rvDoctorapproval.setVisibility(View.GONE);
            }
        } catch (Exception exception) {
            //exception.printStackTrace();
        }
    }

    @Override
    public void OnSuccess(String msg) {
        if(pd!=null || pd.isShowing())
        {
            pd.dismiss();
        }

        SnackBarManagement._success_CustomMessage(binding.getRoot(),msg);

        pd.setTitle("Loading...");
        pd.show();
        Map<String, String> filter = new HashMap<>();
        filter.put("Role", RoleType);
        filter.put("AppStatus", "");
        filter.put("FromDt", "");
        filter.put("ToDt", "");
        filter.put("ProgramTypeId","");
        filter.put("DoctorTypeId", "");
        filter.put("EmpId", "");

        filter.put("providertype","0");
        filter.put("pharmatype","0");
        filter.put("doctortype","0");
        hitApi(Constants.filterparams,Constants.filtermap);
    }

    @Override
    public void OnError(int type) {

    }

    @Override
    public void onDoctorDesignationGet(List<DoctorDesignation> aList) {

    }

    @Override
    public void onDoctorTypeReceived(List<DoctorTypeVM> aList) {
        if (aList.size() > 0) {
            dtypeList = aList;
          /*  ArrayAdapter<DoctorTypeVM> doctorTypeAdapter = new ArrayAdapter<>(DoctorApprovalListActivity.this, R.layout._custom_spinner_tv, aList);
            ftm.filterTypeLayout.spinnerDoctortype.setAdapter(doctorTypeAdapter);
            ftm.filterTypeLayout.spinnerDoctortype.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    DoctorTypeVM cmm = (DoctorTypeVM) doctorTypeAdapter.getItem(position);
                    selecteddoctortypeId = cmm.getDoctorTypeId();
                }
            });*/
        }
    }

    @Override
    public void onDegreeReceived(List<DoctorDegreeViewModel> aList) {

    }

    @Override
    public void onSpecialityReceived(List<DoctorSpecialityViewModel> aList) {

    }

    @Override
    public void onInstituteReceived(List<InstitutionVM> aList) {

    }

    @Override
    public void onBrandReceived(List<Brand> aList) {

    }

    @Override
    public void onDocCategoryReceived(List<DoctorCategory> aList) {

    }

    @Override
    public void onChamberReceived(List<DoctorChamberTypeVM> aList) {

    }

    @Override
    public void onContactTypeReceived(List<ContactTypeVM> aList) {

    }

   /* @Override
    public void onProgramTypeReceived(List<ProgramType> aList) {
        if (aList.size() > 0) {
            ptypeList = aList;

        }
    }*/

    @Override
    public void onProviderTypeReceived(List<ModelProviderType> aList) {
        if (aList.size() > 0) {
            providertypeList = aList;
        }
    }

    @Override
    public void onSMCTypeReceived(List<ModelSMCType> aList) {
        if (aList.size() > 0) {
            pharmatypeList = aList;
        }
    }

    @Override
    public void onSpecialTypeReceived(List<SpecialDay> aList) {

    }

    @Override
    public void onSubmitSuccess(String mesg) {

    }

    @Override
    public void onSubmitError(String mesg) {

    }


    @Override
    protected void onRestart() {
        super.onRestart();
        try {
            Map<String, String> filterz = new HashMap<>();
            filterz.put("Role", RoleType);
            filterz.put("AppStatus", "");
            filterz.put("FromDt", fromdat);
            filterz.put("ToDt", todate);
            filterz.put("ProgramTypeId", selectedprogramId);
            filterz.put("DoctorTypeId", selecteddoctortypeId);
            filterz.put("EmpId", "");
            filterz.put("providertype",selectedprogramId);
            filterz.put("pharmatype",selectedpharmatypeId);
            filterz.put("doctortype",selecteddoctortypeId);
            hitApi(params,filterz);

        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void approveKlick(DoctorApprovalList dl) {
        if (RoleTypeId == 2) {
            next = dl.getRSMEMPId();
        }
        if (RoleTypeId == 3) {
            next = dl.getNSMEMPId();
        }
        if (RoleTypeId == 4) {
            next = 0;
        }

        DoctorApprovalRQ req = new DoctorApprovalRQ();
        int step = dl.getStep();
        int fstep = step + 1;

        req.setDoctorApprovalId(0);
        req.setFromEmpId(empid);
        req.setToEmpId(next);
        req.setTableId(dl.getDoctorId());
        req.setStatus("Verified");//Accepted==approve for Admin
        req.setType(dl.getType());
        req.setStep(fstep);
        req.setEntryByApp(empid);
        String entrydate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        String entrytime = new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(new Date());
        req.setEntryDateApp(entrydate);
        req.setEntryTimeApp(entrytime);
        req.setMenuId(303);
        presenter.SaveDoctorApprovalList(req);
    }
}