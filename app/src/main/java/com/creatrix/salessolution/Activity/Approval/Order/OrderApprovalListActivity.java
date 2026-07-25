package com.creatrix.salessolution.Activity.Approval.Order;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Toolbar;

import com.creatrix.salessolution.Activity.Approval.DA.ApproveDARQ;
import com.creatrix.salessolution.Activity.Approval.DA.TeamDAViewActivity;
import com.creatrix.salessolution.Activity.Approval.Prescription.PrescriptionApprovalListActivity;
import com.creatrix.salessolution.Activity.DA.DA_ApprovList_Adapter;
import com.creatrix.salessolution.Activity.Expense.Approval.TeamExpClaimReportActivity;
import com.creatrix.salessolution.Activity.MileageClaim.Model.ApproveMilRQ;
import com.creatrix.salessolution.Activity.MileageClaim.Model.MileageListTeam;
import com.creatrix.salessolution.DBAdapter.DBCrudHelper;
import com.creatrix.salessolution.Interface.IMarketStracture;
import com.creatrix.salessolution.Interface.IOrderApproval;
import com.creatrix.salessolution.Model.MarketStructure.AssignEmpTable.ASM;
import com.creatrix.salessolution.Model.MarketStructure.AssignEmpTable.MIO;
import com.creatrix.salessolution.Model.MarketStructure.AssignEmpTable.RSM;
import com.creatrix.salessolution.Model.MarketStructure.StructureTable.Area;
import com.creatrix.salessolution.Model.MarketStructure.StructureTable.Group;
import com.creatrix.salessolution.Model.MarketStructure.StructureTable.Market;
import com.creatrix.salessolution.Model.MarketStructure.StructureTable.Region;
import com.creatrix.salessolution.Model.MarketStructure.StructureTable.SubTeritorry;
import com.creatrix.salessolution.Model.MarketStructure.StructureTable.Teritorry;
import com.creatrix.salessolution.Presenter.MarketStructurePresenter;
import com.creatrix.salessolution.Presenter.OrderApprovalPresenter;
import com.creatrix.salessolution.R;
import com.creatrix.salessolution.Services.Constants;
import com.creatrix.salessolution.UtilityHelper.SessionManagement;
import com.creatrix.salessolution.UtilityHelper.SnackBarManagement;
import com.creatrix.salessolution.UtilityHelper.UtilityHelper;
import com.creatrix.salessolution.databinding.ActivityOrderApprovalBinding;
import com.creatrix.salessolution.databinding.FilterMasterBinding;
import com.github.dewinjm.monthyearpicker.MonthYearPickerDialogFragment;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class OrderApprovalListActivity extends AppCompatActivity implements IOrderApproval.View,IMarketStracture.View,
        OrderApprovalAdapter.approvalTOrder{
ActivityOrderApprovalBinding binding;
    FilterMasterBinding ftm;
    OrderApprovalPresenter presenter;
    DBCrudHelper dbCrudHelper;
    OrderApprovalAdapter oaAdapter;
    BottomSheetDialog bsheetdlg;
    ProgressDialog pd;
    int empid;
    SessionManagement session;
    String fromdat,todate,RoleType, params, tagA, tagR, tagN, Areaid, Regionid, Groupid,selectedTyp="";
    int RoleTypeId;
    String status;

    IMarketStracture.Presenter mkpresenter;
    String role, today;
    int selectedGrpId, selectedZoneId, selectedAreaId, selectedTeriId, selectedSTeriId, selectedMarketId;
    int selectedMonth, selectedyear;

    int next=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  setContentView(R.layout.activity_order_approval);
        binding=ActivityOrderApprovalBinding.inflate(getLayoutInflater());
        presenter=new OrderApprovalPresenter(this,OrderApprovalListActivity.this);
        setContentView(binding.getRoot());
        session = new SessionManagement(getApplicationContext());
        //session.checkLogin();
        HashMap<String, String> user = session.getUserDetails();
        empid = Integer.parseInt(user.get(SessionManagement.KEY_EmpId));
        RoleTypeId = Integer.parseInt(user.get(SessionManagement.KEY_EmpRoleTypeId));
        RoleType = user.get(SessionManagement.KEY_EmpRoleType);
        ftm = FilterMasterBinding.inflate(getLayoutInflater());
        mkpresenter = new MarketStructurePresenter(this, OrderApprovalListActivity.this);

        binding.toolbarCustom.setNavigationOnClickListener(view -> finish());
        pd=new ProgressDialog(OrderApprovalListActivity.this);
        try {
            tagA = ".EmpAreaId=";
            tagR = ".EmpRegionId=";
            tagN = ".EmpGroupId=";
            dbCrudHelper = new DBCrudHelper(this);
            switch (RoleType) {
                case "AM":
                    Areaid = String.valueOf(dbCrudHelper.getCurrentUserAreaId_SQLite());
                    params = "AND View_Webapi_EmployeeFieldForceInfo" + tagA + Areaid;
                    break;
                case "DZSM":
                    Regionid = String.valueOf(dbCrudHelper.getCurrentUserRegionId_SQLite(String.valueOf(empid)));
                    params = "AND View_Webapi_EmployeeFieldForceInfo" + tagR + Regionid;
                    break;
                case "NSM":
                    Groupid = String.valueOf(dbCrudHelper.getCurrentUserGroupId_SQLite());
                    params = "AND View_Webapi_EmployeeFieldForceInfo" + tagN + Groupid;
                    break;
                case "Admin":
                    params = "";
                    break;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        Map<String, String> filter = new HashMap<>();
        filter.put("Role",RoleType);
        filter.put("AppStatus","0");
        filter.put("FromDt","");
        filter.put("ToDt","");
        filter.put("EmpId","");


        filter.put("GroupId", "0");
        filter.put("ZoneId", "0");
        filter.put("AreaId", "0");
        filter.put("TerritoryId", "0");
        filter.put("SubTerritoryId", "0");
        filter.put("MarketId", "0");

        Constants.filterparams=params;
        Constants.filtermap=filter;
        hitApi(params,filter);
        binding.swip.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.getOrderApprovalList(params, filter);
                binding.swip.setRefreshing(false);
            }
        });
       // binding.selectedMonthTxt.setText(UtilityHelper.monthNameArrayFull[month] + ',' + year);
        openFilter();
        binding.atteFilter.setOnClickListener(v -> {
            bsheetdlg.show();
        });
    }
    private void hitApi(String params, Map<String, String> filter) {
        pd.setMessage("Loading...");
        pd.show();
        pd.setCancelable(false);
        presenter.getOrderApprovalList(params, filter);
    }
    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onGetOrderSuccess(List<OrderApprovalData> aList) {
         pd.dismiss();
        if (aList != null) {
            Gson gson=new Gson();
            String data=gson.toJson(aList);
            oaAdapter = new OrderApprovalAdapter(aList,OrderApprovalListActivity.this,this,RoleTypeId);
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            binding.rvOrderlist.setLayoutManager(mLayoutManager);
            binding.rvOrderlist.setItemAnimator(new DefaultItemAnimator());
            binding.rvOrderlist.setAdapter(oaAdapter);
            binding.rvOrderlist.setItemAnimator(null);
            binding.rvOrderlist.scrollToPosition(0);
            oaAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onSaveSuccess(String message) {
        if(pd!=null || pd.isShowing())
        {
            pd.dismiss();
        }
        SnackBarManagement._success_CustomMessage(binding.getRoot(),message);
        Map<String, String> filter = new HashMap<>();
        filter.put("Role",RoleType);
        filter.put("AppStatus","0");
        filter.put("FromDt","");
        filter.put("ToDt","");
        filter.put("EmpId","");


        filter.put("GroupId", "0");
        filter.put("ZoneId", "0");
        filter.put("AreaId", "0");
        filter.put("TerritoryId", "0");
        filter.put("SubTerritoryId", "0");
        filter.put("MarketId", "0");
        pd.setMessage("Loading..");
        pd.show();
        hitApi(Constants.filterparams,Constants.filtermap);
    }

    @Override
    public void onError(String message) {
        pd.dismiss();
    }
   /* public void MonthPicker() {
        int yearSelected;
        int monthSelected;
        Calendar calendar = Calendar.getInstance();
        yearSelected = calendar.get(Calendar.YEAR);
        monthSelected = calendar.get(Calendar.MONTH);
        MonthYearPickerDialogFragment dialogFragment = MonthYearPickerDialogFragment
                .getInstance(monthSelected, yearSelected);
        dialogFragment.show(getSupportFragmentManager(), null);

        dialogFragment.setOnDateSetListener((year, monthOfYear) -> {
            binding.selectedMonthTxt.setText("");
            binding.selectedMonthTxt.setText(UtilityHelper.monthNameArrayFull[monthOfYear] + ',' + year);
            filter = new HashMap<>();
            filter.put("statusTxt", "");
            filter.put("ToDt", "");
            filter.put("FromDt", "");
            presenter.getOrderApprovalList(params, filter);
        });
    }*/
    private void openFilter() {
        bsheetdlg = new BottomSheetDialog(OrderApprovalListActivity.this);
        bsheetdlg.setContentView(ftm.getRoot());
        // bsheetdlg.setContentView(R.layout.filter_master);
        bsheetdlg.setCanceledOnTouchOutside(true);
        ftm.linearLayoutemp.setVisibility(View.GONE);
        ftm.filterStatusLayout.linearLayout.setVisibility(View.VISIBLE);
        ftm.filterTypeLayout.tilDoctype.setVisibility(View.GONE);
        ftm.filterTypeLayout.tilProgtypetype.setVisibility(View.GONE);
        ftm.filterTypeLayout.tilCusttype.setVisibility(View.GONE);
        ftm.filterTypeLayout.tilPharmatype.setVisibility(View.GONE);
        ftm.filterMarketLayout.marketStructure.setVisibility(VISIBLE);

        ftm.filterMonthYearLayout.txtFromDate.setText(UtilityHelper._GetCurrentDate());
        ftm.filterMonthYearLayout.txtToDate.setText(UtilityHelper._GetCurrentDate());

        ftm.filterMonthYearLayout.ivDatePickerFromDate.setOnClickListener(v -> {
            UtilityHelper._datePickerDialogeForDates( ftm.filterMonthYearLayout.txtFromDate, OrderApprovalListActivity.this);
        });
        ftm.filterMonthYearLayout.ivDatePickerToDate.setOnClickListener(v -> {
            UtilityHelper._datePickerDialogeForDates(ftm.filterMonthYearLayout.txtToDate, OrderApprovalListActivity.this);
        });

        switch (RoleType) {
            case "MIO":
                ftm.filterMarketLayout.divGroup.setVisibility(GONE);
                ftm.filterMarketLayout.divRegion.setVisibility(GONE);
                ftm.filterMarketLayout.divArea.setVisibility(GONE);
                ftm.filterMarketLayout.divSteritory.setVisibility(GONE);
                ftm.filterMarketLayout.divMarket.setVisibility(GONE);

                mkpresenter.GetTeritoryLocal(0);

                break;
            case "AM":
                ftm.filterMarketLayout.divGroup.setVisibility(GONE);
                ftm.filterMarketLayout.divRegion.setVisibility(GONE);
                ftm.filterMarketLayout.divSteritory.setVisibility(GONE);
                ftm.filterMarketLayout.divMarket.setVisibility(GONE);
                mkpresenter.GetAreaLocal(0);
                break;
            case "DZSM":
                ftm.filterMarketLayout.divGroup.setVisibility(GONE);
                ftm.filterMarketLayout.divSteritory.setVisibility(GONE);
                ftm.filterMarketLayout.divMarket.setVisibility(GONE);
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
        ftm.filterTopLayout.btnDonefilter.setOnClickListener(v -> {
            switch (selectedTyp)
            {
                case "MIO":
                    MIO mioempid;
                    mioempid= (MIO) ftm.spinnerApprovalMio.getSelectedItem();
                    empid=mioempid.getMIOEmpId();

                    break;
                case "AM":
                    ASM amempid=(ASM) ftm.spinnerApprovalAsm.getSelectedItem();
                    empid=amempid.getASMEmpId();

                    break;
                case "DZSM":
                    RSM dzsmempid=(RSM) ftm.spinnerApprovalRsm.getSelectedItem();
                    empid=dzsmempid.getRSMEmpId();
                    break;
            }
            fromdat=ftm.filterMonthYearLayout.txtFromDate.getText().toString();
            todate=ftm.filterMonthYearLayout.txtToDate.getText().toString();
            status = (String) ftm.filterStatusLayout.spinnerApprovalStatus.getSelectedItem();
            HashMap<String, String> filterz = new HashMap<>();
            if (status.equals("Select")) {
                filterz.put("AppStatus", "");
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
                filterz.put("AppStatus", status);
            }
            filterz.put("Role",RoleType);
            filterz.put("FromDt",fromdat);
            filterz.put("ToDt",todate);
            filterz.put("GroupId", String.valueOf(selectedGrpId));
            filterz.put("ZoneId", String.valueOf(selectedZoneId));
            filterz.put("AreaId", String.valueOf(selectedAreaId));
            filterz.put("TerritoryId", String.valueOf(selectedTeriId));
            filterz.put("SubTerritoryId", String.valueOf(selectedSTeriId));
            filterz.put("MarketId", String.valueOf(selectedMarketId));
            if(String.valueOf(empid)==null)
            {
                filterz.put("EmpId","");
            }else {
                filterz.put("EmpId",String.valueOf(empid));
            }
            Constants.filterparams=params;
            Constants.filtermap=filterz;

            hitApi(params,filterz);
            //presenter.getOrderApprovalList(params, filterz);
            bsheetdlg.cancel();
        });
        ftm.filterTopLayout.ivCancel.setOnClickListener(v -> {
            bsheetdlg.cancel();
        });

    }
    @Override
    public void vGroup(List<Group> groupList) {
        try {
            if (groupList != null) {
                Group g = new Group();
                g.setGroupName("Select");
                groupList.add(0, g);

                ArrayAdapter<Group> dataAdapter = new ArrayAdapter<>(OrderApprovalListActivity.this, android.R.layout.simple_spinner_item, groupList);
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

                ArrayAdapter<Region> dataAdapter = new ArrayAdapter<>(OrderApprovalListActivity.this, android.R.layout.simple_spinner_item, regionList);
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
                ArrayAdapter<Area> dataAdapter = new ArrayAdapter<>(OrderApprovalListActivity.this, android.R.layout.simple_spinner_item, areaList);
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

                ArrayAdapter<Teritorry> dataAdapter = new ArrayAdapter<>(OrderApprovalListActivity.this, android.R.layout.simple_spinner_item, teritoryList);
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

                ArrayAdapter<SubTeritorry> dataAdapter = new ArrayAdapter<>(OrderApprovalListActivity.this, android.R.layout.simple_spinner_item, steritoryList);
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

                ArrayAdapter<Market> dataAdapter = new ArrayAdapter<>(OrderApprovalListActivity.this, android.R.layout.simple_spinner_item, marketList);
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
    public void approvalKlick(OrderApprovalData data) {
        if (RoleTypeId == 2) {
            next = data.getRSMEMPId();
        }
        if (RoleTypeId == 3) {
            next = data.getNSMEMPId();
        }
        if (RoleTypeId == 4) {
            next = 0;
        }

        OrderApprovalSaveBody req = new OrderApprovalSaveBody();
        int step = data.getStep();
        int fstep = step + 1;
        // Toast.makeText(context, "empid : "+String.valueOf(empid), Toast.LENGTH_SHORT).show();
        req.setOrderApprovalId(0);
        req.setFromEmpId(empid);
        req.setToEmpId(next);
        req.setTableId(data.getaOrderMasterDAO().getOrderId());
        req.setStatus("Verified");//Accepted==approve for Admin
        req.setType(data.getType());
        req.setStep(fstep);
        req.setEntryByApp(String.valueOf(empid));
        String entrydate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        String entrytime = new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(new Date());
        req.setEntryDateApp(entrydate);
        req.setEntryTimeApp(entrytime);
        req.setMenuId(377);
        pd=new ProgressDialog(OrderApprovalListActivity.this);
        pd.setMessage("Submitting...");
        pd.show();
        presenter.SaveOrderApproval(req);

    }
}