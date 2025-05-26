package com.creatrix.salessolution.Activity.Customer.Pending;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.creatrix.salessolution.Activity.Customer.Approval.CustomerApprovalListActivity;
import com.creatrix.salessolution.Activity.Customer.CustomerPresenter;
import com.creatrix.salessolution.Activity.Customer.ICustomerAdd;
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
import com.creatrix.salessolution.databinding.ActivityCustomerPendingBinding;
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

public class CustomerPendingActivity extends AppCompatActivity implements ICustomer.View,IMarketStracture.View, ICustomerAdd.View {
    ActivityCustomerPendingBinding binding;
    ProgressDialog progressDoalog;
    CustomerARAdapter cAdapter;
    int empId;
    CustomerListPresenter custPresenter;
    CustomerPresenter presenterCutstomer;
    DBCrudHelper crudHelper;

    BottomSheetDialog bsheetdlg;
    IMarketStracture.Presenter mkpresenter;
    FilterMasterBinding ftm;
    String role;
    int selectedGrpId, selectedZoneId, selectedAreaId, selectedTeriId, selectedSTeriId, selectedMarketId;
    SessionManagement session;
    public List<CustomerARModel> customerArrayList = new ArrayList<>();
    String status,cusStats;

    List<CustomerType> ctypeList;
    List<ModelProviderType> providertypeList;
    List<ModelSMCType> pharmatypeList;
    String selectedcustomertypeId="0", selectedprogramId="0",selectedpharmatypeId="0";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCustomerPendingBinding.inflate(getLayoutInflater());
        //setContentView(R.layout.activity_customer_pending);
        setContentView(binding.getRoot());
        custPresenter = new CustomerListPresenter(this, CustomerPendingActivity.this);
        presenterCutstomer = new CustomerPresenter(this, CustomerPendingActivity.this);
        crudHelper = new DBCrudHelper(CustomerPendingActivity.this);
        binding.toolbarCustom.setNavigationOnClickListener(v -> finish());
        session = new SessionManagement(CustomerPendingActivity.this);
        HashMap<String, String> user = session.getUserDetails();
        empId = Integer.parseInt(Objects.requireNonNull(user.get(SessionManagement.KEY_EmpId)));
        role = user.get(SessionManagement.KEY_EmpRoleType);
        ftm = FilterMasterBinding.inflate(getLayoutInflater());
        mkpresenter = new MarketStructurePresenter(this, CustomerPendingActivity.this);
        presenterCutstomer.GetCustomerType();
        presenterCutstomer.GetSMCType();
        presenterCutstomer.GetProviderType();
       // GetReportData(empId, "0");
        progressDoalog = new ProgressDialog(CustomerPendingActivity.this);
        progressDoalog.setIcon(R.drawable.ic_email);
        progressDoalog.setMessage("Customer is Loading....");
        progressDoalog.show();
        progressDoalog.setCanceledOnTouchOutside(false);

        boolean isExist = crudHelper.CheckDataInTable("tblCustomerReport");
        if (!isExist) {
            custPresenter.GetCustomerReport(empId);
        } else {
            LoadinView(crudHelper.getCustomerReport_SQLite("Approved"));
        }
        binding.swipcust.setOnRefreshListener(() -> {
           // GetReportData(empId, "0");
            custPresenter.GetCustomerReport(empId);
            binding.swipcust.setRefreshing(false);
        });
        binding.refresh.setOnClickListener(v -> {
            progressDoalog = new ProgressDialog(CustomerPendingActivity.this);
            progressDoalog.setIcon(R.drawable.ic_email);
            progressDoalog.setMessage("Customer is Loading....");
            progressDoalog.show();
            progressDoalog.setCanceledOnTouchOutside(false);
            custPresenter.GetCustomerReport(empId);
        });
        openFilterz();
        binding.filter.setOnClickListener(v -> bsheetdlg.show());

    }
    private void openFilterz() {
        bsheetdlg = new BottomSheetDialog(CustomerPendingActivity.this);
        bsheetdlg.setContentView(ftm.getRoot());
        bsheetdlg.setCanceledOnTouchOutside(true);
        ftm.filterMarketLayout.marketStructure.setVisibility(VISIBLE);

        ftm.filterStatusLayout.getRoot().setVisibility(VISIBLE);
        ftm.filterStatusLayout.llcuststats.setVisibility(VISIBLE);
        ftm.filterStatusLayout.tvStatusTag.setText("Customer Status");

        ftm.filterMonthYearLayout.getRoot().setVisibility(GONE);
        ftm.linearLayoutemp.setVisibility(GONE);
        ftm.filterTypeLayout.tilDoctype.setVisibility(View.GONE);

        CustomerType r = new CustomerType();
        r.setCustomerType("Select");
        ctypeList.add(0, r);
        ArrayAdapter<CustomerType> customerTypeAdapter = new ArrayAdapter<>(CustomerPendingActivity.this, R.layout._custom_spinner_tv, ctypeList);
        ftm.filterTypeLayout.spinnerCusttype.setAdapter(customerTypeAdapter);
        ftm.filterTypeLayout.spinnerCusttype.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if(position==0)
                {
                    selectedcustomertypeId="0";
                }else
                {
                    CustomerType cmm = (CustomerType) customerTypeAdapter.getItem(position);
                    selectedcustomertypeId = String.valueOf(cmm.getCustomerTypeId());
                }
            }
        });

        ModelProviderType ptype = new ModelProviderType();
        ptype.setProviderType("Select");
        providertypeList.add(0, ptype);
        ArrayAdapter<ModelProviderType> providerAdapter = new ArrayAdapter<>(CustomerPendingActivity.this, R.layout._custom_spinner_tv, providertypeList);
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

        ArrayAdapter<ModelSMCType> pharmaAdapter = new ArrayAdapter<>(CustomerPendingActivity.this, R.layout._custom_spinner_tv, pharmatypeList);
        ftm.filterTypeLayout.spinnerPharmatype.setAdapter(pharmaAdapter);
        ftm.filterTypeLayout.spinnerPharmatype.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                ModelSMCType cmm = (ModelSMCType) pharmaAdapter.getItem(position);
                selectedpharmatypeId = String.valueOf(cmm.getSMCTypeId());
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
        ftm.filterTopLayout.btnDonefilter.setOnClickListener(v -> {
            status=(String)ftm.filterStatusLayout.spinnerApprovalStatus.getSelectedItem();
            cusStats=(String)ftm.filterStatusLayout.spinnerCustomerStatus.getSelectedItem();
            try {
                if (crudHelper.CheckDataInTable("tblCustomerReport")) {
                    customerArrayList = crudHelper.getPendingCustomerFilter_SQLite(String.valueOf(selectedGrpId), String.valueOf(selectedZoneId), String.valueOf(selectedAreaId)
                            , String.valueOf(selectedTeriId), String.valueOf(selectedSTeriId), String.valueOf(selectedMarketId),status,cusStats,String.valueOf(selectedcustomertypeId),String.valueOf(selectedprogramId),String.valueOf(selectedpharmatypeId));
                    LoadinView(customerArrayList);
                }else {

                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            bsheetdlg.cancel();
        });
        ftm.filterTopLayout.ivCancel.setOnClickListener(v -> bsheetdlg.cancel());
    }
 /*   public void GetReportData(int empid, String Status) {
        progressDoalog = new ProgressDialog(CustomerPendingActivity.this);
        progressDoalog.setIcon(R.drawable.ic_email);
        progressDoalog.setMessage("Customer is Loading....");
        progressDoalog.show();
        progressDoalog.setCanceledOnTouchOutside(false);
        try {
            ApiCustomerCall service = RetrofitClientInstance.getRetrofitInstance().create(ApiCustomerCall.class);
            Call<List<CustomerARModel>> call = service.GetCustomerApproveRejList(empid, Status);
            call.enqueue(new Callback<List<CustomerARModel>>() {
                @Override
                public void onResponse(@NonNull Call<List<CustomerARModel>> call, @NonNull Response<List<CustomerARModel>> response) {
                    progressDoalog.dismiss();
                    LoadinView(response.body());
                }

                @Override
                public void onFailure(@NonNull Call<List<CustomerARModel>> call, @NonNull Throwable t) {
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
    public void LoadinView(List<CustomerARModel> aList) {
        if (aList != null) {
            System.out.println("data area : "+aList);
            progressDoalog.dismiss();
            binding.count.setText(String.valueOf(aList.size()));
            cAdapter = new CustomerARAdapter(aList, CustomerPendingActivity.this);
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            binding.rvCustomerpending.setLayoutManager(mLayoutManager);
            binding.rvCustomerpending.setItemAnimator(new DefaultItemAnimator());
            binding.rvCustomerpending.setAdapter(cAdapter);
            binding.rvCustomerpending.setItemAnimator(null);
            binding.rvCustomerpending.scrollToPosition(0);
            cAdapter.notifyDataSetChanged();
        }else {
            progressDoalog.dismiss();
            cAdapter.notifyDataSetChanged();
            SnackBarManagement._error_CustomMessage(binding.masterLayout,"Customer Not Found");
        }
    }

    @Override
    public void OnSuccess(List<Customer> aList) {

    }
    @Override
    public void OnSuccessCustomerReport(List<CustomerARModel> aList) {
    if(aList!=null)
    {
        LoadinView(aList);
    }else {
        progressDoalog.dismiss();
       // LoadinView(aList);
        SnackBarManagement._error_CustomMessage(binding.masterLayout,"Customer Not Found");
    }
    }
    @Override
    public void OnError(int type) {
    }
    @Override
    public void OnErrorReport(String msg) {

    }
    @Override
    public void vGroup(List<Group> groupList) {
        try {
            if (groupList != null) {
                Group g = new Group();
                g.setGroupName("Select");
                groupList.add(0, g);

                ArrayAdapter<Group> dataAdapter = new ArrayAdapter<>(CustomerPendingActivity.this, android.R.layout.simple_spinner_item, groupList);
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

                ArrayAdapter<Region> dataAdapter = new ArrayAdapter<>(CustomerPendingActivity.this, android.R.layout.simple_spinner_item, regionList);
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
                ArrayAdapter<Area> dataAdapter = new ArrayAdapter<>(CustomerPendingActivity.this, android.R.layout.simple_spinner_item, areaList);
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

                ArrayAdapter<Teritorry> dataAdapter = new ArrayAdapter<>(CustomerPendingActivity.this, android.R.layout.simple_spinner_item, teritoryList);
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

                ArrayAdapter<SubTeritorry> dataAdapter = new ArrayAdapter<>(CustomerPendingActivity.this, android.R.layout.simple_spinner_item, steritoryList);
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

                ArrayAdapter<Market> dataAdapter = new ArrayAdapter<>(CustomerPendingActivity.this, android.R.layout.simple_spinner_item, marketList);
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