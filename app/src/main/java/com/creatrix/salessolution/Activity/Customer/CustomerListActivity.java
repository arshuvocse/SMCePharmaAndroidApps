package com.creatrix.salessolution.Activity.Customer;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import com.creatrix.salessolution.Activity.Approval.DCR.DcrApprovalListActivity;
import com.creatrix.salessolution.Activity.Customer.Pending.CustomerARModel;
import com.creatrix.salessolution.Activity.MioOrderListActivity;
import com.creatrix.salessolution.DBAdapter.DBCrudHelper;
import com.creatrix.salessolution.DBAdapter.SyncDb_Helper;
import com.creatrix.salessolution.Interface.ICustomer;
import com.creatrix.salessolution.Interface.IMarketStracture;
import com.creatrix.salessolution.Model.Customer;
import com.creatrix.salessolution.Model.CustomerType;
import com.creatrix.salessolution.Model.Doctor.DoctorTypeVM;
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
import com.creatrix.salessolution.Network.GetDataService;
import com.creatrix.salessolution.Network.RetrofitClientInstance;
import com.creatrix.salessolution.Presenter.CustomerListPresenter;
import com.creatrix.salessolution.Presenter.MarketStructurePresenter;
import com.creatrix.salessolution.R;
import com.creatrix.salessolution.RecyclerAdapter._customerList_RecyclerAdapter;
import com.creatrix.salessolution.UtilityHelper.SessionManagement;
import com.creatrix.salessolution.UtilityHelper.SnackBarManagement;
import com.creatrix.salessolution.UtilityHelper.UtilityHelper;
import com.creatrix.salessolution.databinding.ActivityCustomerListBinding;
import com.creatrix.salessolution.databinding.FilterMasterBinding;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import okhttp3.HttpUrl;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomerListActivity extends AppCompatActivity implements ICustomer.View, IMarketStracture.View, ICustomerAdd.View {
    ActivityCustomerListBinding binding;
    private _customerList_RecyclerAdapter mAdapter;
    ICustomer.Presenter presenter;
    CustomerPresenter presenterCutstomer;
    SessionManagement session;
    ProgressDialog progressDoalog;
    public List<Customer> customerArrayList = new ArrayList<>();
    DBCrudHelper crudHelper;
    SyncDb_Helper syncDbHelper;

    BottomSheetDialog bsheetdlg;
    String role, empId, today;
    IMarketStracture.Presenter mkpresenter;
    FilterMasterBinding ftm;
    int selectedGrpId, selectedZoneId, selectedAreaId, selectedTeriId, selectedSTeriId, selectedMarketId;

    List<CustomerType> ctypeList;
    List<ModelProviderType> providertypeList;
    List<ModelSMCType> pharmatypeList;
    String selectedcustomertypeId = "0", selectedprogramId = "0", selectedpharmatypeId = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCustomerListBinding.inflate(getLayoutInflater());
        // setContentView(R.layout.activity_customer_list);
        setContentView(binding.getRoot());
        presenter = new CustomerListPresenter(this, this);
        presenterCutstomer = new CustomerPresenter(this, this);
        session = new SessionManagement(getApplicationContext());
        ftm = FilterMasterBinding.inflate(getLayoutInflater());
        mkpresenter = new MarketStructurePresenter(this, CustomerListActivity.this);
        //session.checkLogin();
        HashMap<String, String> user = session.getUserDetails();
        int empId = Integer.parseInt(Objects.requireNonNull(user.get(SessionManagement.KEY_EmpId)));
        String orderTYpe = getIntent().getStringExtra("OrderType");
        role = user.get(SessionManagement.KEY_EmpRoleType);

        presenterCutstomer.GetCustomerType();
        presenterCutstomer.GetProviderType();
        presenterCutstomer.GetSMCType();

        binding.toolbarCustom.setNavigationOnClickListener(v -> finish());
        openFilter();
        binding.cfilter.setOnClickListener(v -> {
            bsheetdlg.show();
        });

        //getCustomerList(empId,orderTYpe);
        crudHelper = new DBCrudHelper(this);
        syncDbHelper = new SyncDb_Helper(this);
        try {
            if (crudHelper.CheckDataInTable("tblCustomerInfo") == true) {
                customerArrayList = crudHelper.getCustomerList_SQLite();
                OnSuccessz(customerArrayList, orderTYpe);
            } else {
                getCustomerList(empId, orderTYpe);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
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

    private void openFilter() {
    /*    FilterMasterBinding ftm;
        ftm = FilterMasterBinding.inflate(getLayoutInflater());*/
        bsheetdlg = new BottomSheetDialog(CustomerListActivity.this);
        bsheetdlg.setContentView(ftm.getRoot());
        bsheetdlg.setCanceledOnTouchOutside(true);
        ftm.filterMarketLayout.marketStructure.setVisibility(VISIBLE);
        ftm.filterStatusLayout.getRoot().setVisibility(GONE);
        ftm.filterMonthYearLayout.getRoot().setVisibility(GONE);
        ftm.linearLayoutemp.setVisibility(GONE);
        ftm.filterTypeLayout.tilDoctype.setVisibility(View.GONE);

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
        // ftm.filterMarketLayout.areaSpinner.setVisibility(View.GONE);
/*        ftm.filterMonthYearLayout.txtFromDate.setText(today);
        ftm.filterMonthYearLayout.txtToDate.setText(today);
        ftm.filterMonthYearLayout.ivDatePickerFromDate.setOnClickListener(v -> {
            UtilityHelper._datePickerDialogeForDates(ftm.filterMonthYearLayout.txtFromDate, CustomerListActivity.this);
        });
        ftm.filterMonthYearLayout.ivDatePickerToDate.setOnClickListener(v -> {
            if (ftm.filterMonthYearLayout.txtFromDate.getText().toString().isEmpty()) {
                SnackBarManagement._warning_CustomMessage(ftm.filterMaster, "From Date is Require");
                //ftm.filterMonthYearLayout.txtToDate.setError("First Select From Date");
                // ftm.filterMonthYearLayout.txtToDate.setFocusable(true);
            } else {
                UtilityHelper._datePickerDialogeForDates(ftm.filterMonthYearLayout.txtToDate, CustomerListActivity.this);
            }

        });*/
        ftm.filterMonthYearLayout.ivDatePickerFromDate.setVisibility(GONE);
        ftm.filterMonthYearLayout.ivDatePickerToDate.setVisibility(GONE);

        CustomerType r = new CustomerType();
        r.setCustomerType("Select");
        ctypeList.add(0, r);
        ArrayAdapter<CustomerType> customerTypeAdapter = new ArrayAdapter<>(CustomerListActivity.this, R.layout._custom_spinner_tv, ctypeList);
        ftm.filterTypeLayout.spinnerCusttype.setAdapter(customerTypeAdapter);
        ftm.filterTypeLayout.spinnerCusttype.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (position == 0) {
                    selectedcustomertypeId = "0";
                }else {
                    CustomerType cmm = (CustomerType) customerTypeAdapter.getItem(position);
                    selectedcustomertypeId = String.valueOf(cmm.getCustomerTypeId());
                }

            }
        });

        ModelProviderType ptype = new ModelProviderType();
        ptype.setProviderType("Select");
        providertypeList.add(0, ptype);
        ArrayAdapter<ModelProviderType> providerAdapter = new ArrayAdapter<>(CustomerListActivity.this, R.layout._custom_spinner_tv, providertypeList);
        ftm.filterTypeLayout.spinnerProgramtype.setAdapter(providerAdapter);
        ftm.filterTypeLayout.spinnerProgramtype.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (position == 0) {
                    selectedprogramId = "0";
                } else {
                    ModelProviderType cmm = (ModelProviderType) providerAdapter.getItem(position);
                    selectedprogramId = String.valueOf(cmm.getProviderTypeId());
                }

            }
        });

        ArrayAdapter<ModelSMCType> pharmaAdapter = new ArrayAdapter<>(CustomerListActivity.this, R.layout._custom_spinner_tv, pharmatypeList);
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
           /* if (ftm.filterMonthYearLayout.txtToDate.getText().toString().equals("")) {
               // SnackBarManagement._warning_CustomMessage(binding.aMasterlayout, "To Date is Require");
            }*/
            try {
                if (crudHelper.CheckDataInTable("tblCustomerInfo") == true) {
                    customerArrayList = crudHelper.getCustomerFilter_SQLite(String.valueOf(selectedGrpId), String.valueOf(selectedZoneId), String.valueOf(selectedAreaId)
                            , String.valueOf(selectedTeriId), String.valueOf(selectedSTeriId), String.valueOf(selectedMarketId), String.valueOf(selectedcustomertypeId), String.valueOf(selectedprogramId), String.valueOf(selectedpharmatypeId));
                    OnSuccessz(customerArrayList, "orderTYpe");
                }
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

                ArrayList<Customer> arrayList = new ArrayList<>();
                for (Customer item : customerArrayList) {
                    if ((item.getCustomerName().toLowerCase().contains(customerName.toLowerCase()) || (item.getCustomerCode().contains(customerName)))) {
                        arrayList.add(item);
                    }
                }

                mAdapter.filterList(arrayList);

            }

        } catch (Exception exception) {
            exception.printStackTrace();
        }

    }

    public void OnSuccessz(List<Customer> aList, String orderTYpe) {
        // if (aList.size() > 0) {
        if (aList != null) {
            binding.count.setText(String.valueOf(aList.size()));
            mAdapter = new _customerList_RecyclerAdapter(aList, orderTYpe);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            binding.recyclerView.setLayoutManager(mLayoutManager);
            binding.recyclerView.setItemAnimator(new DefaultItemAnimator());
            binding.recyclerView.setAdapter(mAdapter);
            binding.recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(),
                    DividerItemDecoration.VERTICAL));
            binding.recyclerView.setItemAnimator(null);
            binding.recyclerView.scrollToPosition(0);
            mAdapter.notifyDataSetChanged();
        } else {
            binding.count.setText("0");
        }

    }

    @Override
    public void OnSuccess(List<Customer> aList) {
       /* if(aList.size() > 0){
            recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
            mAdapter = new _customerList_RecyclerAdapter(aList,orderTYpe);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(mAdapter);
            recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(),
                    DividerItemDecoration.VERTICAL));
            recyclerView.setItemAnimator(null);
            recyclerView.scrollToPosition(0);
            mAdapter.notifyDataSetChanged();
        }*/
    }

    @Override
    public void OnSuccessCustomerReport(List<CustomerARModel> aList) {
    }

    @Override
    public void OnError(int type) {
        if (type == 1) {
            SnackBarManagement._error_CustomMessage(binding.aMasterlayout, "Slow connection detected. Make sure you have a working internet connection");
        }
        if (type == 2) {
            SnackBarManagement._error_CustomMessage(binding.aMasterlayout, "Error getting response from server.Make sure you have a working Internet connection");
        }
        if (type == 3) {
            SnackBarManagement._error_CustomMessage(binding.aMasterlayout, "Some Error Occured ! Please try again");
        }
    }

    @Override
    public void OnErrorReport(String msg) {
    }

    private void getCustomerList(int empId, String orderTYpe) {
        progressDoalog = new ProgressDialog(CustomerListActivity.this);
        progressDoalog.setMessage("Customer is Loading....");
        progressDoalog.show();
        progressDoalog.setCanceledOnTouchOutside(false);
        List<Customer> aCusList = new ArrayList<>();
        try {
            ApiCustomerCall service = RetrofitClientInstance.getRetrofitInstance().create(ApiCustomerCall.class);
            Call<List<Customer>> call = service.GetCustomerByUser(empId);
            call.enqueue(new Callback<List<Customer>>() {
                @Override
                public void onResponse(@NonNull Call<List<Customer>> call, @NonNull Response<List<Customer>> response) {
                    if (response != null) {
                        progressDoalog.dismiss();
                        customerArrayList = response.body();

//                    crudHelper.InsertCustomerInfo_SQLite(customerArrayList);
                        OnSuccessz(customerArrayList, orderTYpe);
                    } else {
                        SnackBarManagement._warning_CustomMessage(binding.aMasterlayout, "Customer Not Found!!");
                    }
                }

                @Override
                public void onFailure(@NonNull Call<List<Customer>> call, @NonNull Throwable t) {
                    progressDoalog.dismiss();
                    if (t instanceof SocketTimeoutException) {

                    } else {

                    }
                }
            });

        } catch (Exception ex) {
            progressDoalog.dismiss();

        }

    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left,
                R.anim.slide_out_right);
    }

    @Override
    public void vGroup(List<Group> groupList) {
        try {
            if (groupList != null) {
                Group g = new Group();
                g.setGroupName("Select");
                groupList.add(0, g);

                ArrayAdapter<Group> dataAdapter = new ArrayAdapter<>(CustomerListActivity.this, android.R.layout.simple_spinner_item, groupList);
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

                ArrayAdapter<Region> dataAdapter = new ArrayAdapter<>(CustomerListActivity.this, android.R.layout.simple_spinner_item, regionList);
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
                ArrayAdapter<Area> dataAdapter = new ArrayAdapter<>(CustomerListActivity.this, android.R.layout.simple_spinner_item, areaList);
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

                ArrayAdapter<Teritorry> dataAdapter = new ArrayAdapter<>(CustomerListActivity.this, android.R.layout.simple_spinner_item, teritoryList);
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

                ArrayAdapter<SubTeritorry> dataAdapter = new ArrayAdapter<>(CustomerListActivity.this, android.R.layout.simple_spinner_item, steritoryList);
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

                ArrayAdapter<Market> dataAdapter = new ArrayAdapter<>(CustomerListActivity.this, android.R.layout.simple_spinner_item, marketList);
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