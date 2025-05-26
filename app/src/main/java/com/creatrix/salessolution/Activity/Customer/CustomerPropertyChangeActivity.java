package com.creatrix.salessolution.Activity.Customer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.creatrix.salessolution.Activity.Customer.Pending.BtnModel;
import com.creatrix.salessolution.Activity.Customer.Pending.CustomerARModel;
import com.creatrix.salessolution.Activity.Customer.Pending.UpdateCustMaket;
import com.creatrix.salessolution.Interface.IMarketStracture;
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
import com.creatrix.salessolution.Model.ResultInfo;
import com.creatrix.salessolution.Model.StationType;
import com.creatrix.salessolution.Network.ApiCustomerCall;
import com.creatrix.salessolution.Network.RetrofitClientInstance;
import com.creatrix.salessolution.Presenter.MarketStructurePresenter;
import com.creatrix.salessolution.UtilityHelper.SessionManagement;
import com.creatrix.salessolution.UtilityHelper.SnackBarManagement;
import com.creatrix.salessolution.databinding.ActivityCustomerPropertyChangeBinding;
import com.google.gson.Gson;

import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomerPropertyChangeActivity extends AppCompatActivity implements ICustomerAdd.View, IMarketStracture.View {
    ActivityCustomerPropertyChangeBinding binding;
    ProgressDialog pd;
    SessionManagement session;
    int empId, selectedGroupId, selectedRegionId, selectedAreaId, selectedTeriId, selectedSTeri, selectedMarket;
    String Role;
    CustomerARModel customerData;
    ICustomerAdd.Presenter presenter;
    IMarketStracture.Presenter mkpresenter;
    String edit_mName;
    // ArrayAdapter<Market> dataAdapter;
    ArrayAdapter<Group> gdataAdapter;
    ArrayAdapter<Region> rdataAdapter;
    ArrayAdapter<Area> adataAdapter;
    ArrayAdapter<Teritorry> tdataAdapter;
    ArrayAdapter<SubTeritorry> stdataAdapter;
    ArrayAdapter<Market> mdataAdapter;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCustomerPropertyChangeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.toolbarCustom.setNavigationOnClickListener(v -> finish());
        session = new SessionManagement(CustomerPropertyChangeActivity.this);
        HashMap<String, String> user = session.getUserDetails();
        empId = Integer.parseInt(Objects.requireNonNull(user.get(SessionManagement.KEY_EmpId)));
        Role = user.get(SessionManagement.KEY_EmpRoleType);
        presenter = new CustomerPresenter(this, CustomerPropertyChangeActivity.this);
        mkpresenter = new MarketStructurePresenter(this, CustomerPropertyChangeActivity.this);
        presenter.GetProgramType();

        Gson gson = new Gson();
        customerData = gson.fromJson(getIntent().getStringExtra("CustomerData"), CustomerARModel.class);
        initAllMaketLoad();
        System.out.println("data Cust Property : " + customerData);
        pd = new ProgressDialog(CustomerPropertyChangeActivity.this);
        pd.setMessage("Please wait...");
        pd.setCancelable(false);

        hitApi(customerData);
        binding.updateProvider.setOnClickListener(view ->
        {
            pd.show();
            ProgramType provider = (ProgramType) binding.providerSpinner.getSelectedItem();
            int providerId = provider.getProgramTypeId();

            HashMap<String, String> postProvider = new HashMap<>();
            postProvider.put("empId", String.valueOf(empId));
            postProvider.put("CustomerId", String.valueOf(customerData.getCustomerMasterId()));
            postProvider.put("ProviderTypeId", String.valueOf(providerId));
            Gson gsn=new Gson();
            String mm=gsn.toJson(postProvider);
            System.out.println("pp "+mm);
            Log.d("pp",mm);
            //Toast.makeText(CustomerPropertyChangeActivity.this, "Provider: "+mm, Toast.LENGTH_SHORT).show();

           updateProvider(postProvider);
        });
        binding.updateMarket.setOnClickListener(view -> {
            UpdateCustMaket upcmarket = new UpdateCustMaket();
            Group grp=(Group)binding.marketlay.groupSpinner.getSelectedItem();
            int grpId=grp.getGroupId();
            Region rgn=(Region)binding.marketlay.regionSpinner.getSelectedItem();
            int regionId=rgn.getRegionId();
            Area area=(Area)binding.marketlay.areaSpinner.getSelectedItem();
            int areaId=area.getAreaId();
            Teritorry teri=(Teritorry)binding.marketlay.territorySpinner.getSelectedItem();
            int territoryId=teri.getTerritoryId();
            SubTeritorry steri=(SubTeritorry)binding.marketlay.sterritorySpinner.getSelectedItem();
            int sterritoryId=steri.getSubTerritoryId();
            Market market=(Market)binding.marketlay.marketSpinner.getSelectedItem();
            int marketId=market.getMarketId();

            upcmarket.setCustomerId(customerData.getCustomerMasterId());
            upcmarket.setEmpId(empId);
            upcmarket.setGroupId(grpId);
            upcmarket.setZoneId(regionId);
            upcmarket.setAeaId(areaId);
            upcmarket.setTeritoryId(territoryId);
            upcmarket.setSTeritoryId(sterritoryId);
            upcmarket.setMarketId(marketId);
            Gson gsn=new Gson();
            String mm=gsn.toJson(upcmarket);
            System.out.println("vv "+mm);
            Log.d("vv",mm);
           // Toast.makeText(CustomerPropertyChangeActivity.this, "Market: "+mm, Toast.LENGTH_SHORT).show();
            updateMarket(upcmarket);


        });
    }

    private void initAllMaketLoad() {
        binding.marketlay.groupSpinner.setBackground(null);
        binding.marketlay.regionSpinner.setBackground(null);
        binding.marketlay.areaSpinner.setBackground(null);
        binding.marketlay.territorySpinner.setBackground(null);
        binding.marketlay.groupSpinner.setOnTouchListener((v, event) -> true);
        binding.marketlay.regionSpinner.setOnTouchListener((v, event) -> true);
        binding.marketlay.areaSpinner.setOnTouchListener((v, event) -> true);
        binding.marketlay.territorySpinner.setOnTouchListener((v, event) -> true);
        mkpresenter.GetGroupLocal();
        mkpresenter.GetRegionLocal(0);
        mkpresenter.GetAreaLocal(0);
        mkpresenter.GetTeritoryLocal(Integer.parseInt(customerData.getAreaId()));
        edit_mName=customerData.getTerritoryName();
        binding.marketlay.territorySpinner.setSelection(getIndex(binding.marketlay.territorySpinner, edit_mName));
        binding.marketlay.territorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Teritorry teri = (Teritorry) binding.marketlay.territorySpinner.getSelectedItem();
                selectedTeriId = teri.getTerritoryId();
                mkpresenter.GetSTeritoryLocal(selectedTeriId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        binding.marketlay.sterritorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SubTeritorry steri = (SubTeritorry) binding.marketlay.sterritorySpinner.getSelectedItem();
                selectedSTeri = steri.getSubTerritoryId();
                mkpresenter.GetMarketLocal(selectedSTeri);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }
    private void setData(CustomerARModel customerData) {
        if (customerData.getProgramTypeName() != null) {
            String edit_dName = customerData.getProgramTypeName();
            binding.providerSpinner.setSelection(getIndex(binding.providerSpinner, edit_dName));
        }
    }
    private void updateMarket(UpdateCustMaket upmarket) {
        pd.show();
        try {
            ApiCustomerCall service = RetrofitClientInstance.getRetrofitInstance().create(ApiCustomerCall.class);
            Call<ResultInfo> call = service.UpdateCustomerMarket(upmarket);
            call.enqueue(new Callback<ResultInfo>() {
                @Override
                public void onResponse(@NonNull Call<ResultInfo> call, @NonNull Response<ResultInfo> response) {
                    if (pd != null || pd.isShowing()) {
                        pd.dismiss();
                    }
                    assert response.body() != null;
                    if (response.body().getSuccess()) {
                        // SnackBarManagement._success_CustomMessage(binding.getRoot(),"Market Update Successful");
                        new AlertDialog.Builder(CustomerPropertyChangeActivity.this)
                                .setTitle("Success")
                                .setMessage("Market Updated")
                                .setPositiveButton("OK", (dialog, which) -> {
                                    dialog.cancel();
                                    //onBackPressed();
                                    /*  Intent i = new Intent(CustomerPropertyChangeActivity.this, MainDashboardActivity.class);
                                    startActivity(i);
                                    i.addFlags(i.FLAG_ACTIVITY_CLEAR_TOP | i.FLAG_ACTIVITY_CLEAR_TASK | i.FLAG_ACTIVITY_NEW_TASK);
                                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);*/
                                }).setCancelable(false).show();
                    }
                    if(!response.body().getSuccess())
                    {
                        if(response.body().getValiCheck())
                        {
                            SnackBarManagement._error_CustomMessage(binding.getRoot(), response.body().getMsd());
                        }
                    }

                }

                @Override
                public void onFailure(@NonNull Call<ResultInfo> call, @NonNull Throwable t) {
                    if (pd != null || pd.isShowing()) {
                        pd.dismiss();
                    }
                    if (t instanceof SocketTimeoutException) {
                        SnackBarManagement._warning_CustomMessage(binding.getRoot(), "Slow Connection Detected");
                    } else {
                        SnackBarManagement._error_CustomMessage(binding.getRoot(), "Market Update Failed!!Try Again");
                    }


                }
            });
        } catch (Exception ignored) {
        }
    }
    private void updateProvider(HashMap<String, String> postProvider) {

        try {
            ApiCustomerCall service = RetrofitClientInstance.getRetrofitInstance().create(ApiCustomerCall.class);
            Call<ResultInfo> call = service.UpdateCustomerProviderType(postProvider);
            call.enqueue(new Callback<ResultInfo>() {
                @Override
                public void onResponse(@NonNull Call<ResultInfo> call, @NonNull Response<ResultInfo> response) {
                    if (pd != null || pd.isShowing()) {
                        pd.dismiss();
                    }
                    assert response.body() != null;
                    if (response.body().isSuccess) {
                        new AlertDialog.Builder(CustomerPropertyChangeActivity.this)
                                .setTitle("Success")
                                .setMessage("Provider Type Updated")
                                .setPositiveButton("OK", (dialog, which) -> {
                                    dialog.cancel();
                                  //  onBackPressed();
                                  /*  Intent i = new Intent(CustomerPropertyChangeActivity.this, MainDashboardActivity.class);
                                    startActivity(i);
                                    i.addFlags(i.FLAG_ACTIVITY_CLEAR_TOP | i.FLAG_ACTIVITY_CLEAR_TASK | i.FLAG_ACTIVITY_NEW_TASK);
                                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);*/
                                }).setCancelable(false).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ResultInfo> call, @NonNull Throwable t) {
                    if (pd != null || pd.isShowing()) {
                        pd.dismiss();
                    }
                    if (t instanceof SocketTimeoutException) {
                        SnackBarManagement._warning_CustomMessage(binding.getRoot(), "Slow Connection Detected");
                    } else {
                        SnackBarManagement._error_CustomMessage(binding.getRoot(), "Provider Type Update Failed!!Try Again");
                    }


                }
            });
        } catch (Exception ignored) {
        }
    }
    @SuppressLint("ClickableViewAccessibility")
    private void hitApi(CustomerARModel customerData) {
        pd.show();
        try {
            ApiCustomerCall service = RetrofitClientInstance.getRetrofitInstance().create(ApiCustomerCall.class);
            Call<BtnModel> call = service.GetCustomerbtnOnOFF();
            call.enqueue(new Callback<BtnModel>() {
                @Override
                public void onResponse(@NonNull Call<BtnModel> call, @NonNull Response<BtnModel> response) {
                    if (pd != null || pd.isShowing()) {
                        pd.dismiss();
                    }
                    if (response.body() != null) {
                        setupAction(response.body());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<BtnModel> call, @NonNull Throwable t) {
                    if (pd != null || pd.isShowing()) {
                        pd.dismiss();
                    }
                    if (t instanceof SocketTimeoutException) {
                        SnackBarManagement._warning_CustomMessage(binding.getRoot(), "Slow Connection Detected");
                    } else {
                        SnackBarManagement._error_CustomMessage(binding.getRoot(), "Loading Failed!!Try Again");
                    }

                }
            });
        } catch (Exception ignored) {
        }
    }
    @SuppressLint("ClickableViewAccessibility")
    public void setupAction(BtnModel cic) {

        if (cic.getBtnCustProvider() == 1) {

            // binding.divProvider.setVisibility(View.VISIBLE);
            binding.updateProvider.setVisibility(View.VISIBLE);

        } else {
            // binding.divProvider.setVisibility(View.GONE);
            binding.updateProvider.setVisibility(View.GONE);
            binding.updateProvider.setBackground(null);
            binding.providerSpinner.setBackground(null);
            binding.providerSpinner.setOnTouchListener((v, event) -> true);
            if (customerData.getProgramTypeName() != null) {
                String edit_dName = customerData.getProgramTypeName();
                binding.providerSpinner.setSelection(getIndex(binding.providerSpinner, edit_dName));
            }
        }

        if (cic.getBtnCustMS() == 1) {
            binding.updateMarket.setVisibility(View.VISIBLE);
            setData(customerData);

        } else {
            // binding.marketlay.marketStructure.setVisibility(View.GONE);
            binding.updateMarket.setVisibility(View.GONE);
        }
    }
    @Override
    public void onProgramType(List<ProgramType> ptype) {
        try {
            ArrayAdapter<ProgramType> dataAdapter = new ArrayAdapter<>(CustomerPropertyChangeActivity.this, android.R.layout.simple_spinner_item, ptype);// dbDoctor.getProgramTypeListFromSQLite(0));
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            binding.providerSpinner.setAdapter(dataAdapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onProviderType(List<ModelProviderType> ptype) {

    }

    @Override
    public void onSMCType(List<ModelSMCType> ptype) {

    }

    @Override
    public void onCustomerTypeReceived(List<CustomerType> aList) {
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
    @Override
    public void vGroup(List<Group> groupList) {
        try {
            if (groupList != null) {
                gdataAdapter = new ArrayAdapter<>(CustomerPropertyChangeActivity.this, android.R.layout.simple_spinner_item, groupList);
                gdataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                binding.marketlay.groupSpinner.setAdapter(gdataAdapter);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void vRegion(List<Region> regionList) {
        try {
            if (regionList != null) {
                rdataAdapter = new ArrayAdapter<>(CustomerPropertyChangeActivity.this, android.R.layout.simple_spinner_item, regionList);
                rdataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                binding.marketlay.regionSpinner.setAdapter(rdataAdapter);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void vArea(List<Area> areaList) {
        try {
            if (areaList != null) {
                adataAdapter = new ArrayAdapter<>(CustomerPropertyChangeActivity.this, android.R.layout.simple_spinner_item, areaList);
                adataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                binding.marketlay.areaSpinner.setAdapter(adataAdapter);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void vTeritory(List<Teritorry> teritoryList) {
        try {
            if (teritoryList != null) {
                tdataAdapter = new ArrayAdapter<>(CustomerPropertyChangeActivity.this, android.R.layout.simple_spinner_item, teritoryList);
                tdataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                binding.marketlay.territorySpinner.setAdapter(tdataAdapter);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void vSTeritory(List<SubTeritorry> steritoryList) {
        try {
            if (steritoryList != null) {
                stdataAdapter = new ArrayAdapter<>(CustomerPropertyChangeActivity.this, android.R.layout.simple_spinner_item, steritoryList);
                stdataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                binding.marketlay.sterritorySpinner.setAdapter(stdataAdapter);

                edit_mName=customerData.getSubTerritoryName();
                binding.marketlay.sterritorySpinner.setSelection(getIndex(binding.marketlay.sterritorySpinner,edit_mName));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void vMarket(List<Market> marketList) {
        try {
            if (marketList != null) {
                mdataAdapter = new ArrayAdapter<>(CustomerPropertyChangeActivity.this, android.R.layout.simple_spinner_item, marketList);
                mdataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                binding.marketlay.marketSpinner.setAdapter(mdataAdapter);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private int getIndex(Spinner designationSpinner, String edit_dname) {
        for (int i = 0; i < designationSpinner.getCount(); i++) {
            if (designationSpinner.getItemAtPosition(i).toString().equalsIgnoreCase(edit_dname)) {
                return i;
            }
        }
        return 0;
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
    /* for (int i = 0; i < binding.marketlay.territorySpinner.getCount(); i++) {
                Teritorry t = (Teritorry) binding.marketlay.territorySpinner.getItemAtPosition(i);
                if (String.valueOf(t.getTerritoryId()).equalsIgnoreCase(customerData.getTerritoryId())) {
                    binding.marketlay.territorySpinner.setSelection(i);
                    break;
                }
            }*/
}