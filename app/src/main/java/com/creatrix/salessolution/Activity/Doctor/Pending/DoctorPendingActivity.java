package com.creatrix.salessolution.Activity.Doctor.Pending;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.creatrix.salessolution.Activity.Customer.Pending.CustomerARModel;
import com.creatrix.salessolution.Activity.Customer.Pending.CustomerPendingActivity;
import com.creatrix.salessolution.Activity.SelfReports.ReportsDcrActivity;
import com.creatrix.salessolution.DBAdapter.DBCrudHelper;
import com.creatrix.salessolution.DBAdapter.DBDoctor.DBDoctorHelper;
import com.creatrix.salessolution.Interface.IDoctor;
import com.creatrix.salessolution.Interface.IMarketStracture;
import com.creatrix.salessolution.Interface.IPendingDoctor;
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
import com.creatrix.salessolution.Model.MarketStructure.StructureTable.Area;
import com.creatrix.salessolution.Model.MarketStructure.StructureTable.Group;
import com.creatrix.salessolution.Model.MarketStructure.StructureTable.Market;
import com.creatrix.salessolution.Model.MarketStructure.StructureTable.Region;
import com.creatrix.salessolution.Model.MarketStructure.StructureTable.SubTeritorry;
import com.creatrix.salessolution.Model.MarketStructure.StructureTable.Teritorry;
import com.creatrix.salessolution.Model.ModelProviderType;
import com.creatrix.salessolution.Model.ModelSMCType;
import com.creatrix.salessolution.Network.ApiDoctorCall;
import com.creatrix.salessolution.Network.RetrofitClientInstance;
import com.creatrix.salessolution.Presenter.DoctorPresenter;
import com.creatrix.salessolution.Presenter.MarketStructurePresenter;
import com.creatrix.salessolution.Presenter.PendingDoctorPresenter;
import com.creatrix.salessolution.R;
import com.creatrix.salessolution.UtilityHelper.SessionManagement;
import com.creatrix.salessolution.UtilityHelper.SnackBarManagement;
import com.creatrix.salessolution.databinding.ActivityDoctorPendingBinding;
import com.creatrix.salessolution.databinding.FilterMasterBinding;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DoctorPendingActivity extends AppCompatActivity implements IPendingDoctor.View ,IMarketStracture.View, IDoctor.View{
    PendingDoctorPresenter dpresenter;
    ActivityDoctorPendingBinding binding;
    ProgressDialog progressDoalog;
    DoctorARAdapter dAdapter;
    Dialog popup_status;
    int empId;
    DBCrudHelper crudHelper;
    DBDoctorHelper dbDoctorHelper;

    BottomSheetDialog bsheetdlg;
    IMarketStracture.Presenter mkpresenter;
    DoctorPresenter docpresenter;
    FilterMasterBinding ftm;
    String role, today,docstatus;
    int selectedGrpId, selectedZoneId, selectedAreaId, selectedTeriId, selectedSTeriId, selectedMarketId;
    int selectedMonth, selectedyear;
    SessionManagement session;
    public List<DoctorARModel> doctorArrayList = new ArrayList<>();
    String status;

    List<DoctorTypeVM> dtypeList;
    List<ModelProviderType> providertypeList;
    List<ModelSMCType> pharmatypeList;
    String selecteddoctortypeId="0", selectedprogramId="0",selectedpharmatypeId="0";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDoctorPendingBinding.inflate(getLayoutInflater());
        // setContentView(R.layout.activity_doctor_pending);
        setContentView(binding.getRoot());
        dpresenter = new PendingDoctorPresenter(this, DoctorPendingActivity.this);
        docpresenter = new DoctorPresenter(this, this);

        crudHelper = new DBCrudHelper(DoctorPendingActivity.this);
        dbDoctorHelper = new DBDoctorHelper(DoctorPendingActivity.this);
        binding.toolbarCustom.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        session = new SessionManagement(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        empId = Integer.parseInt(user.get(SessionManagement.KEY_EmpId));
        role = user.get(SessionManagement.KEY_EmpRoleType);
        docpresenter.GetProviderType(0);
        docpresenter.GetDoctorType(0);
        docpresenter.GetSMCType(0);

        ftm = FilterMasterBinding.inflate(getLayoutInflater());
        mkpresenter = new MarketStructurePresenter(this, DoctorPendingActivity.this);
       // GetReportData(empId, "0");
        progressDoalog = new ProgressDialog(DoctorPendingActivity.this);
        progressDoalog.setIcon(R.drawable.ic_email);
        progressDoalog.setMessage("Doctor is Loading....");
        progressDoalog.show();
        progressDoalog.setCanceledOnTouchOutside(false);

        boolean isExist = crudHelper.CheckDataInTable("tblDoctorReport");
        if (!isExist) {
            dpresenter.GetDoctorReport(empId);
        } else {
            LoadinView(dbDoctorHelper.getDoctorReport_SQLite("Approved"));
        }
        binding.refresh.setOnClickListener(v -> {
            progressDoalog = new ProgressDialog(DoctorPendingActivity.this);
            progressDoalog.setIcon(R.drawable.ic_email);
            progressDoalog.setMessage("Doctor is Loading....");
            progressDoalog.show();
            progressDoalog.setCanceledOnTouchOutside(false);
            dpresenter.GetDoctorReport(empId);
        });
        binding.swipdoc.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //GetReportData(empId, "0");
                dpresenter.GetDoctorReport(empId);
                binding.swipdoc.setRefreshing(false);
            }
        });
        openFilterz();
        binding.filter.setOnClickListener(v -> {
            bsheetdlg.show();
        });
    }
    private void openFilterz() {
        bsheetdlg = new BottomSheetDialog(DoctorPendingActivity.this);
        bsheetdlg.setContentView(ftm.getRoot());
        bsheetdlg.setCanceledOnTouchOutside(true);
        ftm.filterMarketLayout.marketStructure.setVisibility(VISIBLE);
        ftm.filterStatusLayout.getRoot().setVisibility(VISIBLE);
        ftm.filterStatusLayout.llcuststats.setVisibility(VISIBLE);
        ftm.filterStatusLayout.tvStatusTag.setText("Doctor Status");

        ftm.filterMonthYearLayout.getRoot().setVisibility(GONE);
        ftm.linearLayoutemp.setVisibility(GONE);
        ftm.filterTypeLayout.tilCusttype.setVisibility(View.GONE);

        List<String>statu=new ArrayList<>();
        statu.add("Select");
        statu.add("Pending");
        statu.add("Verified");
        statu.add("Approved");
        statu.add("Rejected");
        ArrayAdapter<String> stadapter=new ArrayAdapter<String>(DoctorPendingActivity.this,android.R.layout.simple_spinner_dropdown_item,statu);
        ftm.filterStatusLayout.spinnerApprovalStatus.setAdapter(stadapter);
        ftm.filterStatusLayout.spinnerApprovalStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                status=(String)parent.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        switch (role) {
            case "MIO":
                ftm.filterMarketLayout.divGroup.setVisibility(GONE);
                ftm.filterMarketLayout.divRegion.setVisibility(GONE);
                ftm.filterMarketLayout.divArea.setVisibility(GONE);
                mkpresenter.GetTeritoryLocal(0);

                break;
            case "AM":
                ftm.filterMarketLayout.divGroup.setVisibility(GONE);
                ftm.filterMarketLayout.divRegion.setVisibility(GONE);
                mkpresenter.GetAreaLocal(0);
                break;
            case "DZSM":
                ftm.filterMarketLayout.divGroup.setVisibility(GONE);
                mkpresenter.GetRegionLocal(0);
                break;
            case "NSM":
            case "Admin":
                ftm.filterMarketLayout.divGroup.setVisibility(VISIBLE);
                ftm.filterMarketLayout.divRegion.setVisibility(VISIBLE);
                ftm.filterMarketLayout.divArea.setVisibility(VISIBLE);
                ftm.filterMarketLayout.divTeritorry.setVisibility(VISIBLE);
                ftm.filterMarketLayout.divSteritory.setVisibility(VISIBLE);
                ftm.filterMarketLayout.divMarket.setVisibility(VISIBLE);
                mkpresenter.GetGroupLocal();
                break;
        }

        ftm.filterMonthYearLayout.ivDatePickerFromDate.setVisibility(GONE);
        ftm.filterMonthYearLayout.ivDatePickerToDate.setVisibility(GONE);

        DoctorTypeVM r = new DoctorTypeVM();
        r.setDoctorTypeName("Select");
        dtypeList.add(0, r);
        ArrayAdapter<DoctorTypeVM> doctorTypeAdapter = new ArrayAdapter<>(DoctorPendingActivity.this, R.layout._custom_spinner_tv, dtypeList);
        ftm.filterTypeLayout.spinnerDoctortype.setAdapter(doctorTypeAdapter);
        ftm.filterTypeLayout.spinnerDoctortype.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if(position==0)
                {
                    selecteddoctortypeId="0";
                }else{
                    DoctorTypeVM cmm = (DoctorTypeVM) doctorTypeAdapter.getItem(position);
                    selecteddoctortypeId = String.valueOf(cmm.getDoctorTypeId());
                }

            }
        });

        ModelProviderType ptype = new ModelProviderType();
        ptype.setProviderType("Select");
        providertypeList.add(0, ptype);
        ArrayAdapter<ModelProviderType> providerAdapter = new ArrayAdapter<>(DoctorPendingActivity.this, R.layout._custom_spinner_tv, providertypeList);
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

        ArrayAdapter<ModelSMCType> pharmaAdapter = new ArrayAdapter<>(DoctorPendingActivity.this, R.layout._custom_spinner_tv, pharmatypeList);
        ftm.filterTypeLayout.spinnerPharmatype.setAdapter(pharmaAdapter);
        ftm.filterTypeLayout.spinnerPharmatype.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                ModelSMCType cmm = (ModelSMCType) pharmaAdapter.getItem(position);
                selectedpharmatypeId = String.valueOf(cmm.getSMCTypeId());
            }
        });

        String providertype,doctortypeId;
        if (selectedprogramId == null) {
             providertype="0";
        } else {
             providertype= selectedprogramId;
        }
        if (selecteddoctortypeId == null) {
            doctortypeId="0";
        } else {
            doctortypeId=selecteddoctortypeId;
        }


        ftm.filterTopLayout.btnDonefilter.setOnClickListener(v -> {
            docstatus=(String)ftm.filterStatusLayout.spinnerCustomerStatus.getSelectedItem();
            try {
                if (crudHelper.CheckDataInTable("tblDoctorReport")) {
                    doctorArrayList = dbDoctorHelper.getDoctorReportFilter_SQLite(String.valueOf(selectedGrpId), String.valueOf(selectedZoneId), String.valueOf(selectedAreaId)
                            , String.valueOf(selectedTeriId), String.valueOf(selectedSTeriId), String.valueOf(selectedMarketId),status,providertype,
                            doctortypeId,docstatus,String.valueOf(selectedpharmatypeId));
                    LoadinView(doctorArrayList);
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            bsheetdlg.cancel();
        });
        ftm.filterTopLayout.ivCancel.setOnClickListener(v -> {
            ftm.filterTypeLayout.spinnerDoctortype.setSelection(0);
            ftm.filterTypeLayout.spinnerProgramtype.setSelection(0);
            ftm.filterTypeLayout.spinnerPharmatype.setSelection(0);
            bsheetdlg.cancel();
        });
    }
 /*   private void openFilter() {
        popup_status = new Dialog(DoctorPendingActivity.this);
        popup_status.setContentView(R.layout.pop_status);
        popup_status.setCancelable(false);
        TextView btnDonefilter = (TextView) popup_status.findViewById(R.id.btn_donePop);
        TextView btnCancelfilter = (TextView) popup_status.findViewById(R.id.btn_cancelPop);
        RadioGroup rg = (RadioGroup) popup_status.findViewById(R.id.rg_status);

        btnDonefilter.setOnClickListener(v -> {
            int selectedId = rg.getCheckedRadioButtonId();
            RadioButton rb = popup_status.findViewById(selectedId);
            switch (rb.getText().toString()) {
                case "Pending":
                    GetReportData(empId, "0");
                   // dAdapter.notifyDataSetChanged();
                    break;
                case "Verified":
                    GetReportData(empId, "1");
                  //  dAdapter.notifyDataSetChanged();
                    break;
                case "Approved":
                    GetReportData(empId, "2");
                    break;
                case "Rejected":
                    GetReportData(empId, "3");
                    break;
            }
            popup_status.dismiss();
        });


        btnCancelfilter.setOnClickListener(v -> {
            popup_status.cancel();
        });
        popup_status.show();
    }*/

   /* public void GetReportData(int empid, String Status) {
        progressDoalog = new ProgressDialog(DoctorPendingActivity.this);
        progressDoalog.setIcon(R.drawable.ic_email);
        progressDoalog.setMessage("Doctor is Loading....");
        progressDoalog.show();
        progressDoalog.setCanceledOnTouchOutside(false);
        try {
            ApiDoctorCall service = RetrofitClientInstance.getRetrofitInstance().create(ApiDoctorCall.class);
            Call<List<DoctorARModel>> call = service.GetDoctorApproveRejList(empid, Status);
            call.enqueue(new Callback<List<DoctorARModel>>() {
                @Override
                public void onResponse(Call<List<DoctorARModel>> call, Response<List<DoctorARModel>> response) {
                    progressDoalog.dismiss();
                    System.out.println("data : "+response.body());
                    if(response.body()!=null)
                    {
                        LoadinView(response.body());
                        System.out.println("data : "+response.body());
                    }else {
                        LoadinView(null);
                    }
                }
                @Override
                public void onFailure(Call<List<DoctorARModel>> call, Throwable t) {
                    progressDoalog.dismiss();
                    if (t instanceof SocketTimeoutException) {
                        SnackBarManagement._error_CustomMessage(binding.masterLayout, "Slow Connection Detected");
                    } else {
                        SnackBarManagement._error_CustomMessage(binding.masterLayout, "Some Error Occurred");
                    }

                }
            });

        } catch (Exception ex) {
            progressDoalog.dismiss();
            SnackBarManagement._error_CustomMessage(binding.masterLayout, "Some Error Occurred");
        }
    }*/


    public void LoadinView(List<DoctorARModel> aList) {
        if (aList != null) {
            progressDoalog.dismiss();
            binding.count.setText(String.valueOf(aList.size()));
            dAdapter = new DoctorARAdapter(aList, DoctorPendingActivity.this);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            binding.rvDoctorpending.setLayoutManager(mLayoutManager);
            binding.rvDoctorpending.setItemAnimator(new DefaultItemAnimator());
            binding.rvDoctorpending.setAdapter(dAdapter);
            binding.rvDoctorpending.setItemAnimator(null);
            binding.rvDoctorpending.scrollToPosition(0);
            dAdapter.notifyDataSetChanged();
        }else {
            progressDoalog.dismiss();
            SnackBarManagement._error_CustomMessage(binding.masterLayout,"Doctor Not Found");
        }

    }

    @Override
    public void OnSuccessDoctorReport(List<DoctorARModel> aList) {
        if(aList!=null)
        {
            LoadinView(aList);
        }else {
            progressDoalog.dismiss();
            SnackBarManagement._error_CustomMessage(binding.masterLayout,"Doctor Not Found");
        }
    }

    @Override
    public void OnErrorReport(String msg) {

    }
    public void vGroup(List<Group> groupList) {
        try {
            if (groupList != null) {
                Group g = new Group();
                g.setGroupName("Select");
                groupList.add(0, g);

                ArrayAdapter<Group> dataAdapter = new ArrayAdapter<>(DoctorPendingActivity.this, android.R.layout.simple_spinner_item, groupList);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                ftm.filterMarketLayout.groupSpinner.setAdapter(dataAdapter);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        ftm.filterMarketLayout.groupSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Group grp = (Group) parent.getSelectedItem();
                selectedGrpId = grp.getGroupId();
                mkpresenter.GetRegionLocal(selectedGrpId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    @Override
    public void vRegion(List<Region> regionList) {
        try {
            if (regionList != null) {
                Region r = new Region();
                r.setRegionName("Select");
                regionList.add(0, r);

                ArrayAdapter<Region> dataAdapter = new ArrayAdapter<>(DoctorPendingActivity.this, android.R.layout.simple_spinner_item, regionList);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                ftm.filterMarketLayout.regionSpinner.setAdapter(dataAdapter);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        ftm.filterMarketLayout.regionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Region rgn = (Region) parent.getSelectedItem();
                selectedZoneId = rgn.getRegionId();
                mkpresenter.GetAreaLocal(selectedZoneId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void vArea(List<Area> areaList) {
        try {
            if (areaList != null) {
                Area a = new Area();
                a.setAreaName("Select");
                areaList.add(0, a);
                ArrayAdapter<Area> dataAdapter = new ArrayAdapter<>(DoctorPendingActivity.this, android.R.layout.simple_spinner_item, areaList);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                ftm.filterMarketLayout.areaSpinner.setAdapter(dataAdapter);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        ftm.filterMarketLayout.areaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Area area = (Area) parent.getSelectedItem();
                selectedAreaId = area.getAreaId();
                mkpresenter.GetTeritoryLocal(selectedAreaId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void vTeritory(List<Teritorry> teritoryList) {
        try {
            if (teritoryList != null) {
                Teritorry t = new Teritorry();
                t.setTerritoryName("Select");
                teritoryList.add(0, t);

                ArrayAdapter<Teritorry> dataAdapter = new ArrayAdapter<>(DoctorPendingActivity.this, android.R.layout.simple_spinner_item, teritoryList);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                ftm.filterMarketLayout.territorySpinner.setAdapter(dataAdapter);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        ftm.filterMarketLayout.territorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Teritorry teritorry = (Teritorry) parent.getSelectedItem();
                selectedTeriId = teritorry.getTerritoryId();
                mkpresenter.GetSTeritoryLocal(selectedTeriId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void vSTeritory(List<SubTeritorry> steritoryList) {
        try {
            if (steritoryList != null) {
                SubTeritorry st = new SubTeritorry();
                st.setSubTerritoryName("Select");
                steritoryList.add(0, st);

                ArrayAdapter<SubTeritorry> dataAdapter = new ArrayAdapter<>(DoctorPendingActivity.this, android.R.layout.simple_spinner_item, steritoryList);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                ftm.filterMarketLayout.sterritorySpinner.setAdapter(dataAdapter);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        ftm.filterMarketLayout.sterritorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SubTeritorry steritorry = (SubTeritorry) parent.getSelectedItem();
                selectedSTeriId = steritorry.getSubTerritoryId();
                mkpresenter.GetMarketLocal(selectedSTeriId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void vMarket(List<Market> marketList) {
        try {
            if (marketList != null) {
                Market m = new Market();
                m.setMarketName("Select");
                marketList.add(0, m);

                ArrayAdapter<Market> dataAdapter = new ArrayAdapter<>(DoctorPendingActivity.this, android.R.layout.simple_spinner_item, marketList);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                ftm.filterMarketLayout.marketSpinner.setAdapter(dataAdapter);
                ftm.filterMarketLayout.marketSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        Market market = (Market) parent.getSelectedItem();
                        selectedMarketId = market.getMarketId();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }
        } catch (Exception exception) {
            exception.printStackTrace();
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