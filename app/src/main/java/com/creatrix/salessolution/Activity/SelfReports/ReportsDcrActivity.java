package com.creatrix.salessolution.Activity.SelfReports;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.creatrix.salessolution.Activity.Approval.DCR.DcrApprovalListActivity;
import com.creatrix.salessolution.Activity.Doctor.DoctorListActivity;
import com.creatrix.salessolution.Interface.IDoctor;
import com.creatrix.salessolution.Model.DcrVM;
import com.creatrix.salessolution.Model.Doctor.Brand;
import com.creatrix.salessolution.Model.Doctor.ContactTypeVM;
import com.creatrix.salessolution.Model.Doctor.DoctorCategory;
import com.creatrix.salessolution.Model.Doctor.DoctorChamberTypeVM;
import com.creatrix.salessolution.Model.Doctor.DoctorDegreeViewModel;
import com.creatrix.salessolution.Model.Doctor.DoctorDesignation;
import com.creatrix.salessolution.Model.Doctor.DoctorSpecialityViewModel;
import com.creatrix.salessolution.Model.Doctor.DoctorTypeVM;
import com.creatrix.salessolution.Model.Doctor.SpecialDay;
import com.creatrix.salessolution.Model.InstitutionVM;
import com.creatrix.salessolution.Model.MarketStructure.AssignEmpTable.ASM;
import com.creatrix.salessolution.Model.MarketStructure.AssignEmpTable.MIO;
import com.creatrix.salessolution.Model.MarketStructure.AssignEmpTable.RSM;
import com.creatrix.salessolution.Model.ModelProviderType;
import com.creatrix.salessolution.Model.ModelSMCType;
import com.creatrix.salessolution.Model.Report_DcrViewModel;
import com.creatrix.salessolution.Network.ApiDoctorCall;
import com.creatrix.salessolution.Network.RetrofitClientInstance;
import com.creatrix.salessolution.Presenter.DoctorPresenter;
import com.creatrix.salessolution.R;
import com.creatrix.salessolution.RecyclerAdapter._dcr_ListRecyclerAdapter;
import com.creatrix.salessolution.UtilityHelper.SessionManagement;
import com.creatrix.salessolution.UtilityHelper.UtilityHelper;
import com.creatrix.salessolution.databinding.ActivityReportsDcrBinding;
import com.creatrix.salessolution.databinding.FilterMasterBinding;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.HttpUrl;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReportsDcrActivity extends AppCompatActivity implements IDoctor.View{
    ActivityReportsDcrBinding binding;
    List<Report_DcrViewModel> aList = new ArrayList<>();
    _dcr_ListRecyclerAdapter mAdapter;
    Map<String, String> filter;
    BottomSheetDialog bsheetdlg;
    String fromdat, todate, RoleType, selectedTyp = "";
    int RoleTypeId,empID;

    DoctorPresenter dpresenter;
    List<DoctorTypeVM> dtypeList;
    List<ModelProviderType> providertypeList;
    List<ModelSMCType> pharmatypeList;
    String selecteddoctortypeId="0", selectedprogramId="0",selectedpharmatypeId="0";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityReportsDcrBinding.inflate(getLayoutInflater());
        //setContentView(R.layout.activity_reports_dcr);
        setContentView(binding.getRoot());
        dpresenter = new DoctorPresenter(this, this);
        binding.toolbarCustom.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        SessionManagement session = new SessionManagement(getApplicationContext());
        session.checkLogin();
        HashMap<String, String> user = session.getUserDetails();
        String empId = user.get(SessionManagement.KEY_EmpId);
        empID=Integer.parseInt(user.get(SessionManagement.KEY_EmpId));
        RoleTypeId = Integer.parseInt(user.get(SessionManagement.KEY_EmpRoleTypeId));
        RoleType = user.get(SessionManagement.KEY_EmpRoleType);
        dpresenter.GetProviderType(0);
        dpresenter.GetDoctorType(0);
        dpresenter.GetSMCType(0);
        filter = new HashMap<>();
        filter.put("FromDt", "");
        filter.put("ToDt", "");
        filter.put("EmpId", empId);
        filter.put("providertype","0");
        filter.put("pharmatype","0");
        filter.put("doctortype","0");
        GetDCRList(filter);

        binding.atteFilter.setOnClickListener(v -> {
            openFilter();
        });
    }

    private void openFilter() {
        FilterMasterBinding ftm;
        ftm = FilterMasterBinding.inflate(getLayoutInflater());
        bsheetdlg = new BottomSheetDialog(ReportsDcrActivity.this);
        bsheetdlg.setContentView(ftm.getRoot());
        // bsheetdlg.setContentView(R.layout.filter_master);
        bsheetdlg.setCanceledOnTouchOutside(true);
        ftm.linearLayoutemp.setVisibility(View.GONE);
        ftm.filterStatusLayout.linearLayout.setVisibility(View.GONE);
        ftm.filterTypeLayout.tilCusttype.setVisibility(View.GONE);
        ftm.filterMonthYearLayout.txtFromDate.setText(UtilityHelper._GetCurrentDate());
        ftm.filterMonthYearLayout.txtToDate.setText(UtilityHelper._GetCurrentDate());
        ftm.filterMonthYearLayout.ivDatePickerFromDate.setOnClickListener(v -> {
            UtilityHelper._datePickerDialogeForDates(ftm.filterMonthYearLayout.txtFromDate, ReportsDcrActivity.this);
        });
        ftm.filterMonthYearLayout.ivDatePickerToDate.setOnClickListener(v -> {
            UtilityHelper._datePickerDialogeForDates(ftm.filterMonthYearLayout.txtToDate, ReportsDcrActivity.this);
        });

        DoctorTypeVM r = new DoctorTypeVM();
        r.setDoctorTypeName("Select");
        dtypeList.add(0, r);
        ArrayAdapter<DoctorTypeVM> doctorTypeAdapter = new ArrayAdapter<>(ReportsDcrActivity.this, R.layout._custom_spinner_tv, dtypeList);
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
        ArrayAdapter<ModelProviderType> providerAdapter = new ArrayAdapter<>(ReportsDcrActivity.this, R.layout._custom_spinner_tv, providertypeList);
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

        ArrayAdapter<ModelSMCType> pharmaAdapter = new ArrayAdapter<>(ReportsDcrActivity.this, R.layout._custom_spinner_tv, pharmatypeList);
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
            switch (selectedTyp) {
                case "MIO":
                    MIO mioempid;
                    mioempid = (MIO) ftm.spinnerApprovalMio.getSelectedItem();
                    empID = mioempid.getMIOEmpId();

                    break;
                case "AM":
                    ASM amempid = (ASM) ftm.spinnerApprovalAsm.getSelectedItem();
                    empID = amempid.getASMEmpId();

                    break;
                case "DZSM":
                    RSM dzsmempid = (RSM) ftm.spinnerApprovalRsm.getSelectedItem();
                    empID = dzsmempid.getRSMEmpId();
                    break;
            }
            fromdat = ftm.filterMonthYearLayout.txtFromDate.getText().toString();
            todate = ftm.filterMonthYearLayout.txtToDate.getText().toString();
            //Map<String,String> filter=new HashMap<>();
            filter = new HashMap<>();
            filter.put("FromDt", fromdat);
            filter.put("ToDt", todate);
            if (String.valueOf(empID) == null) {
                filter.put("EmpId", "");
            } else {
                filter.put("EmpId", String.valueOf(empID));
            }
            if (selectedprogramId == null) {
                filter.put("providertype", "0");
            } else {
                filter.put("providertype", selectedprogramId);
            }
            if (selecteddoctortypeId == null) {
                filter.put("doctortype", "0");
            } else {
                filter.put("doctortype", selecteddoctortypeId);
            }
            filter.put("pharmatype",selectedpharmatypeId);

            GetDCRList(filter);
            bsheetdlg.cancel();
        });
        ftm.filterTopLayout.ivCancel.setOnClickListener(v -> {
            bsheetdlg.cancel();
        });
        bsheetdlg.show();
    }

    public void GetDCRList(Map<String, String> filter) {
        try {
            ApiDoctorCall service = RetrofitClientInstance.getRetrofitInstance().create(ApiDoctorCall.class);
            Call<List<DcrVM>> call = service.GetDcrList(filter);
            //  Call<List<DcrVM>> call = service.GetDcrList(empId);
            HttpUrl ds = call.request().url();
            call.enqueue(new Callback<List<DcrVM>>() {
                @Override
                public void onResponse(Call<List<DcrVM>> call, Response<List<DcrVM>> response) {
                    LoadRecycler(response.body());
                }
                @Override
                public void onFailure(Call<List<DcrVM>> call, Throwable t) {
                    if (t instanceof SocketTimeoutException) {
                    } else {
                    }
                }
            });
        } catch (Exception ex) {
        }
    }

    public void LoadRecycler(List<DcrVM> aList) {
        if (aList != null) {
            binding.count.setText(String.valueOf(aList.size()));
            mAdapter = new _dcr_ListRecyclerAdapter(ReportsDcrActivity.this, aList);
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            binding.recyclerView.setLayoutManager(mLayoutManager);
            binding.recyclerView.setItemAnimator(new DefaultItemAnimator());
            binding.recyclerView.setAdapter(mAdapter);
           /* recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(),
                    DividerItemDecoration.VERTICAL));*/
            binding.recyclerView.setItemAnimator(null);
            binding.recyclerView.scrollToPosition(0);
            mAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public void onDoctorDesignationGet(List<DoctorDesignation> aList) {

    }

    @Override
    public void onDoctorTypeReceived(List<DoctorTypeVM> aList) {
        if (aList.size() > 0) {
            dtypeList = aList;
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
}