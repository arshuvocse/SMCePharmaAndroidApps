package com.creatrix.salessolution.Activity.Reports.BonusGift;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.creatrix.salessolution.Activity.CampainActivity;
import com.creatrix.salessolution.Activity.MioOrderListActivity;
import com.creatrix.salessolution.Activity.Reports.Adapter.AdapterBonusgiftReport;
import com.creatrix.salessolution.Activity.Reports.Adapter.AdapterDwspReport;
import com.creatrix.salessolution.Activity.Reports.DWSPReportActivity;
import com.creatrix.salessolution.Activity.Reports.Model.ModelGiftBonus;
import com.creatrix.salessolution.Activity.Reports.Model.ResponseBonusGift;
import com.creatrix.salessolution.Interface.IMarketStracture;
import com.creatrix.salessolution.Model.MarketStructure.StructureTable.Area;
import com.creatrix.salessolution.Model.MarketStructure.StructureTable.Group;
import com.creatrix.salessolution.Model.MarketStructure.StructureTable.Market;
import com.creatrix.salessolution.Model.MarketStructure.StructureTable.Region;
import com.creatrix.salessolution.Model.MarketStructure.StructureTable.SubTeritorry;
import com.creatrix.salessolution.Model.MarketStructure.StructureTable.Teritorry;
import com.creatrix.salessolution.Model.Rp_CampainViewModel;
import com.creatrix.salessolution.Network.APICall_Report_i;
import com.creatrix.salessolution.Network.RetrofitClientInstance;
import com.creatrix.salessolution.Presenter.MarketStructurePresenter;
import com.creatrix.salessolution.R;
import com.creatrix.salessolution.UtilityHelper.SessionManagement;
import com.creatrix.salessolution.UtilityHelper.SnackBarManagement;
import com.creatrix.salessolution.UtilityHelper.UtilityHelper;
import com.creatrix.salessolution.databinding.ActivityBonusGiftBinding;
import com.creatrix.salessolution.databinding.FilterMasterBinding;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import okhttp3.HttpUrl;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityBonusGift extends AppCompatActivity implements IMarketStracture.View{
    ActivityBonusGiftBinding binding;
    ProgressDialog pd;
    SessionManagement session;
    HashMap<String, String> userInfo = new HashMap<>();
    int empid;
    BottomSheetDialog bsheetdlg;
    FilterMasterBinding ftm;
    int selectedGrpId, selectedZoneId, selectedAreaId, selectedTeriId, selectedSTeriId, selectedMarketId;
    String today,role;
    IMarketStracture.Presenter mkpresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityBonusGiftBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ftm = FilterMasterBinding.inflate(getLayoutInflater());
        mkpresenter = new MarketStructurePresenter(this, ActivityBonusGift.this);
        today = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault()).format(new Date());
        pd=new ProgressDialog(ActivityBonusGift.this);
        session = new SessionManagement(getApplicationContext());
        userInfo = session.getUserDetails();
        empid = Integer.parseInt(userInfo.get(SessionManagement.KEY_EmpId));
        role = userInfo.get(SessionManagement.KEY_EmpRoleType);

        Map<String,String> map=new HashMap<>();
        map.put("empId",String.valueOf(empid));
        map.put("FromDate","");
        map.put("ToDate","");
        map.put("GroupId","0");
        map.put("ZoneId","0");
        map.put("AreaId","0");
        map.put("TerritoryId","0");
        map.put("SubTerritoryId","0");
        map.put("MarketId","0");

        GetReportData(map);

        openFilter();
        binding.toolbarCustom.setNavigationOnClickListener(v -> finish());
        binding.filter.setOnClickListener(v -> bsheetdlg.show());
    }

    public void GetReportData(Map<String,String> map) {
        pd.setMessage("Loading....");
        pd.show();

        try {
            APICall_Report_i service = RetrofitClientInstance.getRetrofitInstance().create(APICall_Report_i.class);
            Call<List<ResponseBonusGift>> call = service.GetBonusgift(map);
            call.enqueue(new Callback<List<ResponseBonusGift>>() {
                @Override
                public void onResponse(@NonNull Call<List<ResponseBonusGift>> call, @NonNull Response<List<ResponseBonusGift>> response) {

                    if(pd!=null||pd.isShowing())
                    {
                        pd.dismiss();
                    }
                    if(bsheetdlg!=null||bsheetdlg.isShowing())
                    {
                        bsheetdlg.cancel();
                    }
                    if(response.isSuccessful())
                    {
                        LoadinView(response.body().get(0));

                    }else {
                        bsheetdlg.cancel();
                    }

                }

                @Override
                public void onFailure(@NonNull Call<List<ResponseBonusGift>> call, @NonNull Throwable t) {
                    System.out.println(t.getMessage().toString());
                    if(pd!=null||pd.isShowing())
                    {
                        pd.dismiss();
                    }
                    if(bsheetdlg!=null||bsheetdlg.isShowing())
                    {
                        bsheetdlg.cancel();
                    }
                }
            });

        } catch (Exception ex) {
            pd.dismiss();
            if(bsheetdlg!=null||bsheetdlg.isShowing())
            {
                bsheetdlg.cancel();
            }
            ex.getMessage().toString();
        }

    }




    private void LoadinView(ResponseBonusGift data) {
        binding.tvBonus.setText(data.getTotalBouns());
        binding.tvGift.setText(data.getTotalGift());
        AdapterBonusgiftReport mAdapter = new AdapterBonusgiftReport(data.getLists(), ActivityBonusGift.this);
        binding.rvGift.setHasFixedSize(true);
        binding.rvGift.setAdapter(mAdapter);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.rvGift.setLayoutManager(layoutManager);
        mAdapter.notifyDataSetChanged();
    }

    private void openFilter() {
        bsheetdlg = new BottomSheetDialog(ActivityBonusGift.this);
        bsheetdlg.setContentView(ftm.getRoot());
        bsheetdlg.setCanceledOnTouchOutside(true);
        //mkpresenter.GetGroupLocal();
        ftm.filterStatusLayout.linearLayout.setVisibility(GONE);
        ftm.filterMarketLayout.marketStructure.setVisibility(VISIBLE);

        ftm.linearLayoutemp.setVisibility(GONE);
        ftm.filterTypeLayout.tilDoctype.setVisibility(View.GONE);
        ftm.filterTypeLayout.tilProgtypetype.setVisibility(View.GONE);
        ftm.filterTypeLayout.tilCusttype.setVisibility(View.GONE);
        ftm.filterTypeLayout.tilPharmatype.setVisibility(View.GONE);



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
        ftm.filterMonthYearLayout.ivDatePickerFromDate.setOnClickListener(v -> UtilityHelper._datePickerDialogeForDates(ftm.filterMonthYearLayout.txtFromDate, ActivityBonusGift.this));

        ftm.filterMonthYearLayout.ivDatePickerToDate.setOnClickListener(v -> {
            if (ftm.filterMonthYearLayout.txtFromDate.getText().toString().isEmpty()) {
                SnackBarManagement._warning_CustomMessage(ftm.filterMaster, "From Date is Require");
            } else {
                UtilityHelper._datePickerDialogeForDates(ftm.filterMonthYearLayout.txtToDate, ActivityBonusGift.this);
            }
        });
        ftm.filterTopLayout.btnDonefilter.setOnClickListener(v -> {
            if (ftm.filterMonthYearLayout.txtToDate.getText().toString().equals("")) {
                SnackBarManagement._warning_CustomMessage(binding.mainmaster, "To Date is Require");
            }
            Map<String, String> filters = new HashMap<>();
            filters.put("empId",String.valueOf(empid));
            filters.put("FromDate",ftm.filterMonthYearLayout.txtFromDate.getText().toString());
            filters.put("ToDate",ftm.filterMonthYearLayout.txtToDate.getText().toString());
            filters.put("GroupId",String.valueOf(selectedGrpId));
            filters.put("ZoneId",String.valueOf(selectedZoneId));
            filters.put("AreaId",String.valueOf(selectedAreaId));
            filters.put("TerritoryId",String.valueOf(selectedTeriId));
            filters.put("SubTerritoryId",String.valueOf(selectedSTeriId));
            filters.put("MarketId",String.valueOf(selectedMarketId));
            System.out.println("filter :" + filters);
            GetReportData(filters);

        });

        ftm.filterTopLayout.ivCancel.setOnClickListener(v -> bsheetdlg.cancel());

    }

    @Override
    public void vGroup(List<Group> groupList) {
        try {
            if (groupList != null) {
                Group g = new Group();
                g.setGroupName("Select");
                groupList.add(0, g);

                ArrayAdapter<Group> dataAdapter = new ArrayAdapter<>(ActivityBonusGift.this, android.R.layout.simple_spinner_item, groupList);
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

                ArrayAdapter<Region> dataAdapter = new ArrayAdapter<>(ActivityBonusGift.this, android.R.layout.simple_spinner_item, regionList);
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
                ArrayAdapter<Area> dataAdapter = new ArrayAdapter<>(ActivityBonusGift.this, android.R.layout.simple_spinner_item, areaList);
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

                ArrayAdapter<Teritorry> dataAdapter = new ArrayAdapter<>(ActivityBonusGift.this, android.R.layout.simple_spinner_item, teritoryList);
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

                ArrayAdapter<SubTeritorry> dataAdapter = new ArrayAdapter<>(ActivityBonusGift.this, android.R.layout.simple_spinner_item, steritoryList);
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

                ArrayAdapter<Market> dataAdapter = new ArrayAdapter<>(ActivityBonusGift.this, android.R.layout.simple_spinner_item, marketList);
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