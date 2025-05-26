package com.creatrix.salessolution.Activity.Doctor;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.creatrix.salessolution.Activity.Doctor.Pending.DoctorARModel;
import com.creatrix.salessolution.Activity.Doctor.Pending.DoctorUpdatAdapter;
import com.creatrix.salessolution.Activity.SelfReports.ReportsDcrActivity;
import com.creatrix.salessolution.Interface.IDoctor;
import com.creatrix.salessolution.Interface.IMarketStracture;
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
import com.creatrix.salessolution.Network.ApiCustomerCall;
import com.creatrix.salessolution.Network.RetrofitClientInstance;
import com.creatrix.salessolution.Presenter.DoctorPresenter;
import com.creatrix.salessolution.Presenter.MarketStructurePresenter;
import com.creatrix.salessolution.R;
import com.creatrix.salessolution.UtilityHelper.SessionManagement;
import com.creatrix.salessolution.UtilityHelper.SnackBarManagement;
import com.creatrix.salessolution.databinding.ActivityUpdateDoctorBinding;
import com.creatrix.salessolution.databinding.FilterMasterBinding;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateDoctorActivity extends AppCompatActivity implements IMarketStracture.View, IDoctor.View{
ActivityUpdateDoctorBinding binding;
    ProgressDialog progressDoalog;
    String role;
    int empId;
    String empid;
    SessionManagement session;
    DoctorUpdatAdapter dAdapter;
    public List<DoctorARModel> doctorArrayList = new ArrayList<>();

    BottomSheetDialog bsheetdlg;
    IMarketStracture.Presenter mkpresenter;
    FilterMasterBinding ftm;
    int selectedGrpId, selectedZoneId, selectedAreaId, selectedTeriId, selectedSTeriId, selectedMarketId;
    DoctorPresenter dpresenter;
    List<DoctorTypeVM> dtypeList;
    List<ModelProviderType> providertypeList;
    List<ModelSMCType> pharmatypeList;
    String selecteddoctortypeId="0", selectedprogramId="0",selectedpharmatypeId="0";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityUpdateDoctorBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        dpresenter = new DoctorPresenter(this, this);
        ftm = FilterMasterBinding.inflate(getLayoutInflater());
        mkpresenter = new MarketStructurePresenter(this, UpdateDoctorActivity.this);

        binding.toolbarCustom.setNavigationOnClickListener(v -> finish());
        session = new SessionManagement(UpdateDoctorActivity.this);
        HashMap<String, String> user = session.getUserDetails();
        empid=user.get(SessionManagement.KEY_EmpId);
        empId = Integer.parseInt(Objects.requireNonNull(user.get(SessionManagement.KEY_EmpId)));
        role = user.get(SessionManagement.KEY_EmpRoleType);

        dpresenter.GetProviderType(0);
        dpresenter.GetDoctorType(0);
        dpresenter.GetSMCType(0);

        progressDoalog = new ProgressDialog(UpdateDoctorActivity.this);
        HashMap<String,String> filterz=new HashMap<>();
        filterz.put("GroupId","0");
        filterz.put("ZoneId","0");
        filterz.put("AreaId","0");
        filterz.put("TerritoryId","0");
        filterz.put("SubTerritoryId","0");
        filterz.put("MarketId","0");
        filterz.put("providertype","0");
        filterz.put("pharmatype","0");
        filterz.put("doctortype","0");
        hitApi(empId,filterz);
       // hitApi(empId);
        openfiter();
        binding.refresh.setOnClickListener(view -> hitApi(empId,filterz));
        binding.filter.setOnClickListener(view -> bsheetdlg.show());
        binding.searchDoctor.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());

            }
        });
    }


    private void openfiter() {
        bsheetdlg = new BottomSheetDialog(UpdateDoctorActivity.this);
        bsheetdlg.setContentView(ftm.getRoot());
        bsheetdlg.setCanceledOnTouchOutside(true);
        ftm.filterMarketLayout.marketStructure.setVisibility(VISIBLE);
        ftm.filterStatusLayout.getRoot().setVisibility(GONE);
        ftm.filterMonthYearLayout.getRoot().setVisibility(GONE);
        ftm.linearLayoutemp.setVisibility(GONE);
        ftm.filterTypeLayout.tilCusttype.setVisibility(View.GONE);

        List<String> statu = new ArrayList<>();
        statu.add("Select");
        statu.add("Pending");
        statu.add("Verified");
        statu.add("Approved");
        statu.add("Rejected");
        ArrayAdapter<String> stadapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, statu);
        ftm.filterStatusLayout.spinnerApprovalStatus.setAdapter(stadapter);
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
        ArrayAdapter<DoctorTypeVM> doctorTypeAdapter = new ArrayAdapter<>(UpdateDoctorActivity.this, R.layout._custom_spinner_tv, dtypeList);
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

        ArrayAdapter<ModelProviderType> providerAdapter = new ArrayAdapter<>(UpdateDoctorActivity.this, R.layout._custom_spinner_tv, providertypeList);
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

        ArrayAdapter<ModelSMCType> pharmaAdapter = new ArrayAdapter<>(UpdateDoctorActivity.this, R.layout._custom_spinner_tv, pharmatypeList);
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
            HashMap<String,String> filte=new HashMap<>();
            try {
                filte.put("GroupId",String.valueOf(selectedGrpId));
                filte.put("ZoneId",String.valueOf(selectedZoneId));
                filte.put("AreaId",String.valueOf(selectedAreaId));
                filte.put("TerritoryId",String.valueOf(selectedTeriId));
                filte.put("SubTerritoryId",String.valueOf(selectedSTeriId));
                filte.put("MarketId",String.valueOf(selectedMarketId));
                filte.put("providertype",selectedprogramId);
                filte.put("pharmatype",selectedpharmatypeId);
                filte.put("doctortype",selecteddoctortypeId);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            hitApi(empId,filte);
            bsheetdlg.cancel();
        });
        ftm.filterTopLayout.ivCancel.setOnClickListener(v -> {
            bsheetdlg.cancel();
        });
    }
    private void filter(String doctorName) {
        try {
            if (doctorArrayList != null) {

                ArrayList<DoctorARModel> arrayList = new ArrayList<>();
                for (DoctorARModel item : doctorArrayList) {
                    if ((item.getDoctorCode().toLowerCase().contains(doctorName.toLowerCase()) || (item.getDoctorCode().contains(doctorName)))) {
                        arrayList.add(item);
                    }
                }

                dAdapter.filterList(arrayList);

            }

        } catch (Exception exception) {
            exception.printStackTrace();
        }

    }
    private void hitApi(int empId,HashMap<String, String> filterz) {
        progressDoalog.setIcon(R.drawable.ic_doctor);
        progressDoalog.setMessage("Doctor is Loading....");
        progressDoalog.show();
        progressDoalog.setCanceledOnTouchOutside(false);
        try{
            ApiCustomerCall service = RetrofitClientInstance.getRetrofitInstance().create(ApiCustomerCall.class);
            Call<List<DoctorARModel>> call = service.GetDoctorUpdate(empId,filterz);
            call.enqueue(new Callback<List<DoctorARModel>>() {
                @Override
                public void onResponse(@NonNull Call<List<DoctorARModel>> call, @NonNull Response<List<DoctorARModel>> response) {
                    if(progressDoalog!=null||progressDoalog.isShowing())
                    {
                        progressDoalog.dismiss();
                    }
                    if(response.body()!=null)
                    {
                        LoadinView(response.body());
                    }else {
                        SnackBarManagement._error_CustomMessage(binding.getRoot(),"No Customer At This Moment");
                    }


                }
                @Override
                public void onFailure(@NonNull Call<List<DoctorARModel>> call, @NonNull Throwable t) {
                    if(progressDoalog!=null||progressDoalog.isShowing())
                    {
                        progressDoalog.dismiss();
                    }
                    if(t instanceof SocketTimeoutException){
                        SnackBarManagement._warning_CustomMessage(binding.getRoot(),"Slow Connection Detected");
                    }else{
                        SnackBarManagement._error_CustomMessage(binding.getRoot(),"Loading Failed!!Try Again");
                    }


                }
            });
        }catch (Exception ignored){
        }
    }
    private void LoadinView(List<DoctorARModel> body) {
        if (body != null) {
            doctorArrayList=body;
            System.out.println("data area : "+body);
            if(progressDoalog!=null||progressDoalog.isShowing())
            {
                progressDoalog.dismiss();
            }
            binding.count.setText(String.valueOf(body.size()));
            dAdapter = new DoctorUpdatAdapter(body, UpdateDoctorActivity.this);
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            binding.rvDoctorupdate.setLayoutManager(mLayoutManager);
            binding.rvDoctorupdate.setItemAnimator(new DefaultItemAnimator());
            binding.rvDoctorupdate.setAdapter(dAdapter);
            binding.rvDoctorupdate.setItemAnimator(null);
            binding.rvDoctorupdate.scrollToPosition(0);
            dAdapter.notifyDataSetChanged();
        }else {
            if(progressDoalog!=null||progressDoalog.isShowing())
            {
                progressDoalog.dismiss();
            }
            SnackBarManagement._error_CustomMessage(binding.getRoot(),"Customer Not Found");
        }

    }
    @Override
    protected void onResume() {
        super.onResume();
        HashMap<String,String> filter=new HashMap<>();
        filter.put("GroupId","0");
        filter.put("ZoneId","0");
        filter.put("AreaId","0");
        filter.put("TerritoryId","0");
        filter.put("SubTerritoryId","0");
        filter.put("MarketId","0");
        hitApi(empId,filter);
    }

    /*@Override
    public void onBackPressed() {
        super.onBackPressed();
        hitApi(empId);
    }*/
    @Override
    protected void onDestroy() {
        if(progressDoalog!=null||progressDoalog.isShowing())
        {
            progressDoalog.dismiss();
        }
        super.onDestroy();
    }
    @Override
    public void vGroup(List<Group> groupList) {
        try {
            if (groupList != null) {
                Group g = new Group();
                g.setGroupName("Select");
                groupList.add(0, g);

                ArrayAdapter<Group> dataAdapter = new ArrayAdapter<>(UpdateDoctorActivity.this, android.R.layout.simple_spinner_item, groupList);
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

                ArrayAdapter<Region> dataAdapter = new ArrayAdapter<>(UpdateDoctorActivity.this, android.R.layout.simple_spinner_item, regionList);
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
                ArrayAdapter<Area> dataAdapter = new ArrayAdapter<>(UpdateDoctorActivity.this, android.R.layout.simple_spinner_item, areaList);
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

                ArrayAdapter<Teritorry> dataAdapter = new ArrayAdapter<>(UpdateDoctorActivity.this, android.R.layout.simple_spinner_item, teritoryList);
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

                ArrayAdapter<SubTeritorry> dataAdapter = new ArrayAdapter<>(UpdateDoctorActivity.this, android.R.layout.simple_spinner_item, steritoryList);
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

                ArrayAdapter<Market> dataAdapter = new ArrayAdapter<>(UpdateDoctorActivity.this, android.R.layout.simple_spinner_item, marketList);
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