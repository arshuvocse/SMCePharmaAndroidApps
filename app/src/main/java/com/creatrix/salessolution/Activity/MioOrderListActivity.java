package com.creatrix.salessolution.Activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.creatrix.salessolution.Interface.IMarketStracture;
import com.creatrix.salessolution.Model.MarketStructure.StructureTable.Area;
import com.creatrix.salessolution.Model.MarketStructure.StructureTable.Group;
import com.creatrix.salessolution.Model.MarketStructure.StructureTable.Market;
import com.creatrix.salessolution.Model.MarketStructure.StructureTable.Region;
import com.creatrix.salessolution.Model.MarketStructure.StructureTable.SubTeritorry;
import com.creatrix.salessolution.Model.MarketStructure.StructureTable.Teritorry;
import com.creatrix.salessolution.Model.Order.OrderTrackigMaster;
import com.creatrix.salessolution.Model.OrderViewModel;
import com.creatrix.salessolution.Network.GetDataService;
import com.creatrix.salessolution.Network.RetrofitClientInstance;
import com.creatrix.salessolution.Network.RetrofitClientInstanceTracking;
import com.creatrix.salessolution.Presenter.MarketStructurePresenter;
import com.creatrix.salessolution.R;
import com.creatrix.salessolution.RecyclerAdapter._Mio_OrderList_recyclerAdapter;
import com.creatrix.salessolution.UtilityHelper.SessionManagement;
import com.creatrix.salessolution.UtilityHelper.SnackBarManagement;
import com.creatrix.salessolution.UtilityHelper.UtilityHelper;
import com.creatrix.salessolution.databinding.ActivityMioOrderListBinding;
import com.creatrix.salessolution.databinding.FilterMasterBinding;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class MioOrderListActivity extends AppCompatActivity implements IMarketStracture.View {
    ActivityMioOrderListBinding binding;

    ProgressDialog progressDoalog;
    SessionManagement session;
    BottomSheetDialog bsheetdlg;

    String role, empId, today;
    IMarketStracture.Presenter mkpresenter;
    FilterMasterBinding ftm;
    int selectedGrpId, selectedZoneId, selectedAreaId, selectedTeriId, selectedSTeriId, selectedMarketId;
    String status;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMioOrderListBinding.inflate(getLayoutInflater());
        // setContentView(R.layout.activity_mio_order_list);
        setContentView(binding.getRoot());
        session = new SessionManagement(this);
        ftm = FilterMasterBinding.inflate(getLayoutInflater());
        mkpresenter = new MarketStructurePresenter(this, MioOrderListActivity.this);
        HashMap<String, String> user = session.getUserDetails();
        empId = user.get(SessionManagement.KEY_EmpId);
        role = user.get(SessionManagement.KEY_EmpRoleType);
        today = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault()).format(new Date());
        Map<String, String> filter = new HashMap<>();
        filter.put("FromDate", today);
        filter.put("ToDate", today);
        filter.put("AppStatus", "Select");
        filter.put("GroupId", "0");
        filter.put("ZoneId", "0");
        filter.put("AreaId", "0");
        filter.put("TerritoryId", "0");
        filter.put("SubTerritoryId", "0");
        filter.put("MarketId", "0");
        if (empId == null) {
            filter.put("empId", "");
        } else {
            filter.put("empId", empId);
        }
        getMioOrderList(filter);
        openFilter();
        binding.toolbarCustom.setNavigationOnClickListener(v -> finish());
        binding.filter.setOnClickListener(v -> bsheetdlg.show());

    }

    private void getMioOrderList(Map<String, String> filter) {
        progressDoalog = new ProgressDialog(MioOrderListActivity.this);
        progressDoalog.setMessage("Orders are Loading....");
        progressDoalog.show();
        progressDoalog.setCanceledOnTouchOutside(false);
        try {
            GetDataService service = RetrofitClientInstanceTracking.getRetrofitInstance().create(GetDataService.class);
            Call<List<OrderTrackigMaster>> call = service.GetMio_OrderList(filter);
            call.enqueue(new Callback<List<OrderTrackigMaster>>() {
                @Override
                public void onResponse(@NonNull Call<List<OrderTrackigMaster>> call, @NonNull Response<List<OrderTrackigMaster>> response) {
                    bsheetdlg.cancel();
                    if(response.body()!=null)
                    {
                        progressDoalog.dismiss();
                        System.out.println("response"+response.body());
                        LoadRecycler(response.body().get(0).getOrder_Lists());
                        LoadV(response.body().get(0).getTotalNetAmount());
                    }else {
                        progressDoalog.dismiss();
                        LoadRecycler(null);

                        //SnackBarManagement._success_CustomMessage(binding.mainmaster, "No Order Available");
                    }
                }
                @Override
                public void onFailure(@NonNull Call<List<OrderTrackigMaster>> call, @NonNull Throwable t) {
                    progressDoalog.dismiss();
                    bsheetdlg.cancel();
                }
            });

        } catch (Exception ex) {
            progressDoalog.dismiss();
        }

    }
    private void LoadV(String totalNetAmount) {
        binding.rvAmount.setText(totalNetAmount);
    }

    public void LoadRecycler(List<OrderViewModel> aList) {

        if (aList != null ) {
            binding.count.setText(String.valueOf(aList.size()));
            _Mio_OrderList_recyclerAdapter mAdapter = new _Mio_OrderList_recyclerAdapter(aList);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            binding.recyclerView.setLayoutManager(mLayoutManager);
            binding.recyclerView.setItemAnimator(new DefaultItemAnimator());
            binding.recyclerView.setAdapter(mAdapter);
            binding.recyclerView.setItemAnimator(null);
            binding.recyclerView.scrollToPosition(0);
            mAdapter.notifyDataSetChanged();
        } /*else {
            SnackBarManagement._success_CustomMessage(binding.mainmaster, "No Order Available");
            int size = aList.size();
            aList.clear();
            mAdapter.notifyItemRangeRemoved(0,size);

        }*/

    }

    private void openFilter() {
    /*    FilterMasterBinding ftm;
          ftm = FilterMasterBinding.inflate(getLayoutInflater());*/
        bsheetdlg = new BottomSheetDialog(MioOrderListActivity.this);
        bsheetdlg.setContentView(ftm.getRoot());
        bsheetdlg.setCanceledOnTouchOutside(true);
        //mkpresenter.GetGroupLocal();
        ftm.filterStatusLayout.linearLayout.setVisibility(VISIBLE);
        ftm.filterMarketLayout.marketStructure.setVisibility(VISIBLE);

        ftm.linearLayoutemp.setVisibility(GONE);
        ftm.filterTypeLayout.tilDoctype.setVisibility(View.GONE);
        ftm.filterTypeLayout.tilProgtypetype.setVisibility(View.GONE);
        ftm.filterTypeLayout.tilCusttype.setVisibility(View.GONE);
        ftm.filterTypeLayout.tilPharmatype.setVisibility(View.GONE);
        List<String>statu=new ArrayList<>();
        statu.add("Select");
        statu.add("Pending");
        statu.add("Verified");
        statu.add("Approved");
        statu.add("Rejected");
      //  String status=(String)ftm.filterStatusLayout.spinnerApprovalStatus.getSelectedItem();

        ArrayAdapter<String> stadapter=new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item,statu);
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
                /*ftm.filterMarketLayout.territorySpinner.setVisibility(View.VISIBLE);
                ftm.filterMarketLayout.sterritorySpinner.setVisibility(View.VISIBLE);
                ftm.filterMarketLayout.marketSpinner.setVisibility(View.VISIBLE);*/
                break;
            case "AM":
                ftm.filterMarketLayout.divGroup.setVisibility(GONE);
                ftm.filterMarketLayout.divRegion.setVisibility(GONE);
                mkpresenter.GetAreaLocal(0);
                /*ftm.filterMarketLayout.areaSpinner.setVisibility(View.VISIBLE);
                ftm.filterMarketLayout.territorySpinner.setVisibility(View.VISIBLE);
                ftm.filterMarketLayout.sterritorySpinner.setVisibility(View.VISIBLE);
                ftm.filterMarketLayout.marketSpinner.setVisibility(View.VISIBLE);*/
                break;
            case "DZSM":
                ftm.filterMarketLayout.divGroup.setVisibility(GONE);
                mkpresenter.GetRegionLocal(0);
               /* ftm.filterMarketLayout.divRegion.setVisibility(VISIBLE);
                ftm.filterMarketLayout.divArea.setVisibility(VISIBLE);
                ftm.filterMarketLayout.divTeritorry.setVisibility(VISIBLE);
                ftm.filterMarketLayout.divSteritory.setVisibility(VISIBLE);
                ftm.filterMarketLayout.divMarket.setVisibility(VISIBLE);*/
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
        ftm.filterMonthYearLayout.txtFromDate.setText(today);
        ftm.filterMonthYearLayout.txtToDate.setText(today);
        ftm.filterMonthYearLayout.ivDatePickerFromDate.setOnClickListener(v -> UtilityHelper._datePickerDialogeForDates(ftm.filterMonthYearLayout.txtFromDate, MioOrderListActivity.this));

        ftm.filterMonthYearLayout.ivDatePickerToDate.setOnClickListener(v -> {
            if (ftm.filterMonthYearLayout.txtFromDate.getText().toString().isEmpty()) {
                SnackBarManagement._warning_CustomMessage(ftm.filterMaster, "From Date is Require");
            } else {
                UtilityHelper._datePickerDialogeForDates(ftm.filterMonthYearLayout.txtToDate, MioOrderListActivity.this);
            }
        });
        ftm.filterTopLayout.btnDonefilter.setOnClickListener(v -> {
            if (ftm.filterMonthYearLayout.txtToDate.getText().toString().equals("")) {
                // SnackBarManagement._warning_CustomMessage(ftm.filterMaster, "To Date is Require");
                SnackBarManagement._warning_CustomMessage(binding.mainmaster, "To Date is Require");
            }
            Map<String, String> filters = new HashMap<>();
            filters.put("FromDate", ftm.filterMonthYearLayout.txtFromDate.getText().toString());
            filters.put("ToDate", ftm.filterMonthYearLayout.txtToDate.getText().toString());
            if (status.equals("Select")) {
                filters.put("AppStatus", "");
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
            filters.put("GroupId", String.valueOf(selectedGrpId));
            filters.put("ZoneId", String.valueOf(selectedZoneId));
            filters.put("AreaId", String.valueOf(selectedAreaId));
            filters.put("TerritoryId", String.valueOf(selectedTeriId));
            filters.put("SubTerritoryId", String.valueOf(selectedSTeriId));
            filters.put("MarketId", String.valueOf(selectedMarketId));
            if (empId == null) {
                filters.put("empId", "");
            } else {
                filters.put("empId", empId);
            }
           /* if (String.valueOf(selectedGrpId).equals("")) {
                filter.put("GroupId", "");
            } else {
                filter.put("GroupId", String.valueOf(selectedGrpId));
            }
            if (String.valueOf(selectedZoneId).equals("") || String.valueOf(selectedZoneId).equals("Select")) {
                filter.put("ZoneId", "");
            } else {
                filter.put("ZoneId", String.valueOf(selectedZoneId));
            }
            if (String.valueOf(selectedAreaId).equals("") || String.valueOf(selectedAreaId).equals("Select")) {
                filter.put("AreaId", "");
            } else {
                filter.put("AreaId", String.valueOf(selectedAreaId));
            }
            if (String.valueOf(selectedTeriId).equals("") || String.valueOf(selectedTeriId).equals("Select")) {
                filter.put("TerritoryId", "");
            } else {
                filter.put("TerritoryId", String.valueOf(selectedTeriId));
            }
            if (String.valueOf(selectedSTeriId).equals("0") || String.valueOf(selectedSTeriId).equals("Select")) {
                filter.put("SubTerritoryId", "");
            } else {
                filter.put("SubTerritoryId", String.valueOf(selectedSTeriId));
            }
            if (String.valueOf(selectedMarketId).equals("") || String.valueOf(selectedMarketId).equals("Select")) {
                filter.put("MarketId", "");
            } else {
                filter.put("MarketId", String.valueOf(selectedMarketId));
            }*/
            System.out.println("filter :" + filters);

            getMioOrderList(filters);

        });

        ftm.filterTopLayout.ivCancel.setOnClickListener(v -> bsheetdlg.cancel());

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

                ArrayAdapter<Group> dataAdapter = new ArrayAdapter<>(MioOrderListActivity.this, android.R.layout.simple_spinner_item, groupList);
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

                ArrayAdapter<Region> dataAdapter = new ArrayAdapter<>(MioOrderListActivity.this, android.R.layout.simple_spinner_item, regionList);
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
                ArrayAdapter<Area> dataAdapter = new ArrayAdapter<>(MioOrderListActivity.this, android.R.layout.simple_spinner_item, areaList);
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

                ArrayAdapter<Teritorry> dataAdapter = new ArrayAdapter<>(MioOrderListActivity.this, android.R.layout.simple_spinner_item, teritoryList);
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

                ArrayAdapter<SubTeritorry> dataAdapter = new ArrayAdapter<>(MioOrderListActivity.this, android.R.layout.simple_spinner_item, steritoryList);
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

                ArrayAdapter<Market> dataAdapter = new ArrayAdapter<>(MioOrderListActivity.this, android.R.layout.simple_spinner_item, marketList);
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
}