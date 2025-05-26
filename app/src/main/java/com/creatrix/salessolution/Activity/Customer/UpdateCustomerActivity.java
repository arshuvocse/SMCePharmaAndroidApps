package com.creatrix.salessolution.Activity.Customer;

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

import com.creatrix.salessolution.Activity.Customer.Pending.CustomerARAdapter;
import com.creatrix.salessolution.Activity.Customer.Pending.CustomerARModel;
import com.creatrix.salessolution.Activity.Customer.Pending.CustomerPendingActivity;
import com.creatrix.salessolution.Activity.Customer.Pending.CustomerUpdatAdapter;
import com.creatrix.salessolution.DBAdapter.DBCrudHelper;
import com.creatrix.salessolution.Interface.ICustomer;
import com.creatrix.salessolution.Interface.IMarketStracture;
import com.creatrix.salessolution.Model.Customer;
import com.creatrix.salessolution.Model.CustomerType;
import com.creatrix.salessolution.Model.Doctor.ProgramType;
import com.creatrix.salessolution.Model.MarketStructure.StructureTable.Area;
import com.creatrix.salessolution.Model.MarketStructure.StructureTable.Group;
import com.creatrix.salessolution.Model.MarketStructure.StructureTable.Market;
import com.creatrix.salessolution.Model.MarketStructure.StructureTable.Region;
import com.creatrix.salessolution.Model.MarketStructure.StructureTable.SubTeritorry;
import com.creatrix.salessolution.Model.MarketStructure.StructureTable.Teritorry;
import com.creatrix.salessolution.Model.ModelProviderType;
import com.creatrix.salessolution.Model.ModelSMCType;
import com.creatrix.salessolution.Model.StationType;
import com.creatrix.salessolution.Network.ApiCustomerCall;
import com.creatrix.salessolution.Network.RetrofitClientInstance;
import com.creatrix.salessolution.Presenter.CustomerListPresenter;
import com.creatrix.salessolution.Presenter.MarketStructurePresenter;
import com.creatrix.salessolution.R;
import com.creatrix.salessolution.UtilityHelper.SessionManagement;
import com.creatrix.salessolution.UtilityHelper.SnackBarManagement;
import com.creatrix.salessolution.databinding.ActivityUpdateCustomerBinding;
import com.creatrix.salessolution.databinding.FilterMasterBinding;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import okhttp3.HttpUrl;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateCustomerActivity extends AppCompatActivity implements ICustomer.View,IMarketStracture.View,ICustomerAdd.View{
ActivityUpdateCustomerBinding binding;
    ProgressDialog progressDoalog;
    CustomerListPresenter custPresenter;
    CustomerPresenter presenterCutstomer;
    String role;
    int empId;
    String empid;
    SessionManagement session;
    DBCrudHelper crudHelper;
    public List<CustomerARModel> customerArrayList = new ArrayList<>();
    CustomerUpdatAdapter cAdapter;
    BottomSheetDialog bsheetdlg;
    IMarketStracture.Presenter mkpresenter;
    FilterMasterBinding ftm;
    int selectedGrpId, selectedZoneId, selectedAreaId, selectedTeriId, selectedSTeriId, selectedMarketId;
    List<CustomerType> ctypeList;
    List<ModelProviderType> providertypeList;
    List<ModelSMCType> pharmatypeList;
    String selectedcustomertypeId="0", selectedprogramId="0",selectedpharmatypeId="0";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityUpdateCustomerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ftm = FilterMasterBinding.inflate(getLayoutInflater());
        mkpresenter = new MarketStructurePresenter(this, UpdateCustomerActivity.this);
        binding.toolbarCustom.setNavigationOnClickListener(v -> finish());
        session = new SessionManagement(UpdateCustomerActivity.this);
        HashMap<String, String> user = session.getUserDetails();
        empid=user.get(SessionManagement.KEY_EmpId);
        empId = Integer.parseInt(Objects.requireNonNull(user.get(SessionManagement.KEY_EmpId)));
        role = user.get(SessionManagement.KEY_EmpRoleType);
        presenterCutstomer = new CustomerPresenter(this, this);
        presenterCutstomer.GetCustomerType();
        presenterCutstomer.GetProviderType();
        presenterCutstomer.GetSMCType();
        progressDoalog = new ProgressDialog(UpdateCustomerActivity.this);

        HashMap<String,String> filterz=new HashMap<>();
        filterz.put("GroupId","0");
        filterz.put("ZoneId","0");
        filterz.put("AreaId","0");
        filterz.put("TerritoryId","0");
        filterz.put("SubTerritoryId","0");
        filterz.put("MarketId","0");
        //hitApi(filterz);

        hitApi(empId,filterz);
        openfiter();
        binding.refresh.setOnClickListener(view -> hitApi(empId,filterz));
        binding.filter.setOnClickListener(view -> bsheetdlg.show());
        binding.searchCustomer.addTextChangedListener(new TextWatcher() {
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
        bsheetdlg = new BottomSheetDialog(UpdateCustomerActivity.this);
        bsheetdlg.setContentView(ftm.getRoot());
        bsheetdlg.setCanceledOnTouchOutside(true);
        ftm.filterMarketLayout.marketStructure.setVisibility(VISIBLE);
        ftm.filterStatusLayout.getRoot().setVisibility(GONE);
        ftm.filterMonthYearLayout.getRoot().setVisibility(GONE);
        ftm.linearLayoutemp.setVisibility(GONE);
        ftm.filterTypeLayout.tilDoctype.setVisibility(View.GONE);

        CustomerType r = new CustomerType();
        r.setCustomerType("Select");
        ctypeList.add(0, r);
        ArrayAdapter<CustomerType> customerTypeAdapter = new ArrayAdapter<>(UpdateCustomerActivity.this, R.layout._custom_spinner_tv, ctypeList);
        ftm.filterTypeLayout.spinnerCusttype.setAdapter(customerTypeAdapter);
        ftm.filterTypeLayout.spinnerCusttype.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if(position==0)
                {
                    selectedcustomertypeId="0";
                }else {
                    CustomerType cmm = (CustomerType) customerTypeAdapter.getItem(position);
                    selectedcustomertypeId = String.valueOf(cmm.getCustomerTypeId());
                }

            }
        });

        ModelProviderType ptype = new ModelProviderType();
        ptype.setProviderType("Select");
        providertypeList.add(0, ptype);
        ArrayAdapter<ModelProviderType> providerAdapter = new ArrayAdapter<>(UpdateCustomerActivity.this, R.layout._custom_spinner_tv, providertypeList);
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

        ArrayAdapter<ModelSMCType> pharmaAdapter = new ArrayAdapter<>(UpdateCustomerActivity.this, R.layout._custom_spinner_tv, pharmatypeList);
        ftm.filterTypeLayout.spinnerPharmatype.setAdapter(pharmaAdapter);
        ftm.filterTypeLayout.spinnerPharmatype.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                ModelSMCType cmm = (ModelSMCType) pharmaAdapter.getItem(position);
                selectedpharmatypeId = String.valueOf(cmm.getSMCTypeId());
            }
        });


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
        ftm.filterTopLayout.btnDonefilter.setOnClickListener(v -> {

            try {
                HashMap<String,String> value=new HashMap<>();
                value.put("GroupId",String.valueOf(selectedGrpId));
                value.put("ZoneId",String.valueOf(selectedZoneId));
                value.put("AreaId",String.valueOf(selectedAreaId));
                value.put("TerritoryId",String.valueOf(selectedTeriId));
                value.put("SubTerritoryId",String.valueOf(selectedSTeriId));
                value.put("MarketId",String.valueOf(selectedMarketId));
                hitApi(empId,value);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            bsheetdlg.cancel();
        });
        ftm.filterTopLayout.ivCancel.setOnClickListener(v -> {
            bsheetdlg.cancel();
        });
    }

    private void filter(String customerName) {
        try {
            if (customerArrayList != null) {

                ArrayList<CustomerARModel> arrayList = new ArrayList<>();
                for (CustomerARModel item : customerArrayList) {
                    if ((item.getCustomerName().toLowerCase().contains(customerName.toLowerCase()) || (item.getCustomerName().contains(customerName)))) {
                        arrayList.add(item);
                    }
                }

                cAdapter.filterList(arrayList);

            }

        } catch (Exception exception) {
            exception.printStackTrace();
        }

    }
    private void hitApi(int empId,HashMap<String, String> filterz) {
        progressDoalog.setIcon(R.drawable.ic_customer);
        progressDoalog.setMessage("Customer is Loading....");
        progressDoalog.show();
        progressDoalog.setCanceledOnTouchOutside(false);
            try{
                ApiCustomerCall service = RetrofitClientInstance.getRetrofitInstance().create(ApiCustomerCall.class);
                Call<List<CustomerARModel>> call = service.GetCustomerUpdate(empId,filterz);
                call.enqueue(new Callback<List<CustomerARModel>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<CustomerARModel>> call, @NonNull Response<List<CustomerARModel>> response) {
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
                    public void onFailure(@NonNull Call<List<CustomerARModel>> call, @NonNull Throwable t) {
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

    @Override
    public void OnSuccess(List<Customer> aList) {

    }

    @Override
    public void OnSuccessCustomerReport(List<CustomerARModel> aList) {
     /*   if(aList!=null)
        {
            LoadinView(aList);
        }else {
            progressDoalog.dismiss();
            // LoadinView(aList);
            SnackBarManagement._error_CustomMessage(binding.getRoot(),"Customer Not Found");
        }*/
    }
    private void LoadinView(List<CustomerARModel> aList) {
        if(progressDoalog!=null||progressDoalog.isShowing())
        {
            progressDoalog.dismiss();
        }
        binding.count.setText(String.valueOf(aList.size()));
        cAdapter = new CustomerUpdatAdapter(aList, UpdateCustomerActivity.this);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        binding.rvCustomerupdate.setLayoutManager(mLayoutManager);
        binding.rvCustomerupdate.setItemAnimator(new DefaultItemAnimator());
        binding.rvCustomerupdate.setAdapter(cAdapter);
        binding.rvCustomerupdate.setItemAnimator(null);
        binding.rvCustomerupdate.scrollToPosition(0);
        cAdapter.notifyDataSetChanged();
        if (aList != null) {
            customerArrayList=aList;
            System.out.println("data area : "+aList);
        }else {
            SnackBarManagement._error_CustomMessage(binding.getRoot(),"Customer Not Found");
        }
    }
    @Override
    public void OnError(int type) {

    }
    @Override
    public void OnErrorReport(String msg) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        HashMap<String,String> filterz=new HashMap<>();
        filterz.put("GroupId","");
        filterz.put("ZoneId","");
        filterz.put("AreaId","");
        filterz.put("TerritoryId","");
        filterz.put("SubTerritoryId","");
        filterz.put("MarketId","");
        hitApi(empId,filterz);
    }

   /* @Override
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

                ArrayAdapter<Group> dataAdapter = new ArrayAdapter<>(UpdateCustomerActivity.this, android.R.layout.simple_spinner_item, groupList);
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

                ArrayAdapter<Region> dataAdapter = new ArrayAdapter<>(UpdateCustomerActivity.this, android.R.layout.simple_spinner_item, regionList);
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
                ArrayAdapter<Area> dataAdapter = new ArrayAdapter<>(UpdateCustomerActivity.this, android.R.layout.simple_spinner_item, areaList);
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

                ArrayAdapter<Teritorry> dataAdapter = new ArrayAdapter<>(UpdateCustomerActivity.this, android.R.layout.simple_spinner_item, teritoryList);
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

                ArrayAdapter<SubTeritorry> dataAdapter = new ArrayAdapter<>(UpdateCustomerActivity.this, android.R.layout.simple_spinner_item, steritoryList);
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

                ArrayAdapter<Market> dataAdapter = new ArrayAdapter<>(UpdateCustomerActivity.this, android.R.layout.simple_spinner_item, marketList);
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
    public void onProgramType(List<ProgramType> ptype) {

    }

    @Override
    public void onProviderType(List<ModelProviderType> ptypeList) {
        if (ptypeList.size() > 0) {
            providertypeList = ptypeList;
        }
    }

    @Override
    public void onSMCType(List<ModelSMCType> smctypeList) {
        if (smctypeList.size() > 0) {
            pharmatypeList = smctypeList;
        }
    }

    @Override
    public void onCustomerTypeReceived(List<CustomerType> aList) {
        if (aList.size() > 0) {
            ctypeList = aList;
        }
    }

    @Override
    public void onStationReceived(List<StationType> aList) {

    }

    @Override
    public void onSubmitSuccess(String mesg, String who) {

    }

    @Override
    public void onSubmitError(String mesg) {

    }
}