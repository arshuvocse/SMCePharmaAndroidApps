package com.creatrix.salessolution.Activity.SelfReports;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.creatrix.salessolution.DBAdapter.DBCrudHelper;
import com.creatrix.salessolution.DBInfo.DBHelperMain;
import com.creatrix.salessolution.Interface.IDoctor;
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
import com.creatrix.salessolution.Model.PrescriptionSM;
import com.creatrix.salessolution.Network.RetrofitClientInstance;
import com.creatrix.salessolution.Network.ApiDoctorCall;
import com.creatrix.salessolution.Presenter.DoctorPresenter;
import com.creatrix.salessolution.R;
import com.creatrix.salessolution.RecyclerAdapter._prescriptionList_Recycler;
import com.creatrix.salessolution.UtilityHelper.SessionManagement;
import com.creatrix.salessolution.UtilityHelper.SnackBarManagement;
import com.creatrix.salessolution.UtilityHelper.UtilityHelper;
import com.creatrix.salessolution.databinding.ActivityPrescriptionListBinding;
import com.creatrix.salessolution.databinding.FilterMasterBinding;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.HttpUrl;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PrescriptionListActivity extends AppCompatActivity implements IDoctor.View{
    ActivityPrescriptionListBinding binding;
    ProgressDialog progressDoalog;

    private _prescriptionList_Recycler mAdapter;
    TextView syncAllClick;
    DBCrudHelper dbCrudHelper;
    DBHelperMain dbHelperMain;
    Map<String, String> filter;
    //Filter
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
        binding=ActivityPrescriptionListBinding.inflate(getLayoutInflater());
       // setContentView(R.layout.activity_prescription_list);
        setContentView(binding.getRoot());
        dpresenter = new DoctorPresenter(this, this);
        dbCrudHelper=new DBCrudHelper(PrescriptionListActivity.this);
        dbHelperMain=new DBHelperMain(PrescriptionListActivity.this);
        binding.toolbarCustom.setNavigationOnClickListener(v -> finish());
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
        GetReportData(filter);
        binding.atteFilter.setOnClickListener(v -> {
            openFilter();
        });
    }
    private void openFilter() {
        FilterMasterBinding ftm;
        ftm = FilterMasterBinding.inflate(getLayoutInflater());
        bsheetdlg = new BottomSheetDialog(PrescriptionListActivity.this);
        bsheetdlg.setContentView(ftm.getRoot());
        // bsheetdlg.setContentView(R.layout.filter_master);
        bsheetdlg.setCanceledOnTouchOutside(true);
        ftm.linearLayoutemp.setVisibility(View.GONE);
        ftm.filterStatusLayout.linearLayout.setVisibility(View.GONE);
        ftm.filterTypeLayout.tilCusttype.setVisibility(View.GONE);

        ftm.filterMonthYearLayout.txtFromDate.setText(UtilityHelper._GetCurrentDate());
        ftm.filterMonthYearLayout.txtToDate.setText(UtilityHelper._GetCurrentDate());
        ftm.filterMonthYearLayout.ivDatePickerFromDate.setOnClickListener(v -> {
            UtilityHelper._datePickerDialogeForDates(ftm.filterMonthYearLayout.txtFromDate, PrescriptionListActivity.this);
        });
        ftm.filterMonthYearLayout.ivDatePickerToDate.setOnClickListener(v -> {
            UtilityHelper._datePickerDialogeForDates(ftm.filterMonthYearLayout.txtToDate, PrescriptionListActivity.this);
        });

        DoctorTypeVM r = new DoctorTypeVM();
        r.setDoctorTypeName("Select");
        dtypeList.add(0, r);
        ArrayAdapter<DoctorTypeVM> doctorTypeAdapter = new ArrayAdapter<>(PrescriptionListActivity.this, R.layout._custom_spinner_tv, dtypeList);
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
        ArrayAdapter<ModelProviderType> providerAdapter = new ArrayAdapter<>(PrescriptionListActivity.this, R.layout._custom_spinner_tv, providertypeList);
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

        ArrayAdapter<ModelSMCType> pharmaAdapter = new ArrayAdapter<>(PrescriptionListActivity.this, R.layout._custom_spinner_tv, pharmatypeList);
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
            filter.put("providertype",selectedprogramId);
            filter.put("pharmatype",selectedpharmatypeId);
            filter.put("doctortype",selecteddoctortypeId);


            GetReportData(filter);
            bsheetdlg.cancel();
        });
        ftm.filterTopLayout.ivCancel.setOnClickListener(v -> {
            bsheetdlg.cancel();
        });
        bsheetdlg.show();
    }
    public void GetReportData(Map<String, String> map) {
        progressDoalog = new ProgressDialog(PrescriptionListActivity.this);
        progressDoalog.setIcon(R.drawable.ic_email);
        progressDoalog.setMessage("Prescription is Loading....");
        progressDoalog.show();
        progressDoalog.setCanceledOnTouchOutside(false);
        try{
            ApiDoctorCall service = RetrofitClientInstance.getRetrofitInstance().create(ApiDoctorCall.class);
            Call<List<PrescriptionSM>> call = service.GetPrescriptionList(map);
            call.enqueue(new Callback<List<PrescriptionSM>>() {
                @Override
                public void onResponse(@NonNull Call<List<PrescriptionSM>> call, @NonNull Response<List<PrescriptionSM>> response) {
                    if(progressDoalog!=null && progressDoalog.isShowing())
                    {
                        progressDoalog.dismiss();
                    }
                    LoadinView(response.body());

                }
                @Override
                public void onFailure(@NonNull Call<List<PrescriptionSM>> call, @NonNull Throwable t) {
                    if(progressDoalog!=null && progressDoalog.isShowing())
                    {
                        progressDoalog.dismiss();
                    }
                    if(t instanceof SocketTimeoutException){
                        SnackBarManagement._error_CustomMessage(binding.masterLayout,"Slow Connection Detected");
                    }else{
                        SnackBarManagement._error_CustomMessage(binding.masterLayout,"Some Error Occurred");
                    }


                }
            });

        }catch (Exception ex){
            if(progressDoalog!=null && progressDoalog.isShowing())
            {
                progressDoalog.dismiss();
            }
            ErrorView("Some Error Occurred");
            SnackBarManagement._error_CustomMessage(binding.masterLayout,"Some Error Occurred");
        }
    }
   // public void LoadinView(List<PrescriptionVM> aList){
    public void LoadinView(List<PrescriptionSM> aList){
        if(aList != null){
            binding.count.setText(String.valueOf(aList.size()));
            mAdapter = new _prescriptionList_Recycler(aList,PrescriptionListActivity.this,"PrescriptionListActivity");
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            binding.recyclerView.setLayoutManager(mLayoutManager);
            binding.recyclerView.setItemAnimator(new DefaultItemAnimator());
            binding.recyclerView.setAdapter(mAdapter);
          /*  recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(),
                    DividerItemDecoration.VERTICAL));*/
            binding.recyclerView.setItemAnimator(null);
            binding.recyclerView.scrollToPosition(0);
            mAdapter.notifyDataSetChanged();
        }
    }

    public  void ErrorView(String msg){
        Toast.makeText(PrescriptionListActivity.this,msg,Toast.LENGTH_LONG).show();

    }
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left,
                R.anim.slide_out_right);
    }

    public void LoadRecyclerView(List<PrescriptionSM> aList, String type) {

        if (aList != null) {
            if(type.equals("Local")){
                syncAllClick.setVisibility(View.VISIBLE);
            }
/*
            recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
            mAdapter = new _prescriptionList_Recycler(aList, this,"PrescList");
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(mAdapter);
            recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(),
                    DividerItemDecoration.VERTICAL));
            recyclerView.setItemAnimator(null);
            recyclerView.scrollToPosition(0);
            mAdapter.notifyDataSetChanged();*/

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