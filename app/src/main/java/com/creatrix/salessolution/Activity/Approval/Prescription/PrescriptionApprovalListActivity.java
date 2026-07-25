package com.creatrix.salessolution.Activity.Approval.Prescription;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.creatrix.salessolution.Activity.Approval.DA.DAListData;
import com.creatrix.salessolution.Activity.Approval.DCR.DcrApprovalAdapter;
import com.creatrix.salessolution.Activity.Approval.DCR.DcrApprovalData;
import com.creatrix.salessolution.Activity.Approval.DCR.DcrApprovalListActivity;
import com.creatrix.salessolution.DBAdapter.DBCrudHelper;
import com.creatrix.salessolution.Interface.IDoctor;
import com.creatrix.salessolution.Interface.IMarketStracture;
import com.creatrix.salessolution.Interface.IPrescApproval;
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
import com.creatrix.salessolution.Model.MarketStructure.StructureTable.Area;
import com.creatrix.salessolution.Model.MarketStructure.StructureTable.Group;
import com.creatrix.salessolution.Model.MarketStructure.StructureTable.Market;
import com.creatrix.salessolution.Model.MarketStructure.StructureTable.Region;
import com.creatrix.salessolution.Model.MarketStructure.StructureTable.SubTeritorry;
import com.creatrix.salessolution.Model.MarketStructure.StructureTable.Teritorry;
import com.creatrix.salessolution.Model.ModelProviderType;
import com.creatrix.salessolution.Model.ModelSMCType;
import com.creatrix.salessolution.Presenter.DcrApprovalPresenter;
import com.creatrix.salessolution.Presenter.DoctorPresenter;
import com.creatrix.salessolution.Presenter.MarketStructurePresenter;
import com.creatrix.salessolution.Presenter.PrescApprovalPresenter;
import com.creatrix.salessolution.R;
import com.creatrix.salessolution.Services.Constants;
import com.creatrix.salessolution.UtilityHelper.SessionManagement;
import com.creatrix.salessolution.UtilityHelper.SnackBarManagement;
import com.creatrix.salessolution.UtilityHelper.UtilityHelper;
import com.creatrix.salessolution.databinding.ActivityDcrApprovalBinding;
import com.creatrix.salessolution.databinding.ActivityPrescriptionApprovalListBinding;
import com.creatrix.salessolution.databinding.ActivityPrescriptionListBinding;
import com.creatrix.salessolution.databinding.FilterMasterBinding;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class PrescriptionApprovalListActivity extends AppCompatActivity implements IPrescApproval.View, IDoctor.View , IMarketStracture.View,PrescApprovalAdapter.Rxapproval{
    ActivityPrescriptionApprovalListBinding binding;
    PrescApprovalPresenter presenter;
    DoctorPresenter dpresenter;
    BottomSheetDialog bsheetdlg;
    DBCrudHelper dbCrudHelper;
    PrescApprovalAdapter prescAdapter;
    SessionManagement session;
    int empid;
    Map<String, String> filter;
    String fromdat, todate, RoleType, params, tagA, tagR, tagN, Areaid, Regionid, Groupid,status, selectedTyp = "";
    int RoleTypeId;
    ProgressDialog pd;
    FilterMasterBinding ftm;
    IMarketStracture.Presenter mkpresenter;
    int selectedGrpId, selectedZoneId, selectedAreaId, selectedTeriId, selectedSTeriId, selectedMarketId;
    DBCrudHelper crudHelper;
    List<DoctorTypeVM> dtypeList;
    List<ModelProviderType> providertypeList;
    List<ModelSMCType> pharmatypeList;
    String selecteddoctortypeId="0", selectedprogramId="0",selectedpharmatypeId="0";

    private int prev = 0;
    private int current = 0;
    private int next = 0;
    private int myrole = 0;
    String prev_roleType, next_roleType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_prescription_approval_list);
        binding = ActivityPrescriptionApprovalListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        presenter = new PrescApprovalPresenter(this, PrescriptionApprovalListActivity.this);
     //   setContentView(binding.getRoot());
        session = new SessionManagement(PrescriptionApprovalListActivity.this);
        ftm = FilterMasterBinding.inflate(getLayoutInflater());
        crudHelper = new DBCrudHelper(this);
        mkpresenter = new MarketStructurePresenter(this, PrescriptionApprovalListActivity.this);
        //session.checkLogin();
        HashMap<String, String> user = session.getUserDetails();
        empid = Integer.parseInt(user.get(SessionManagement.KEY_EmpId));
        RoleTypeId = Integer.parseInt(user.get(SessionManagement.KEY_EmpRoleTypeId));
        RoleType = user.get(SessionManagement.KEY_EmpRoleType);
        dpresenter = new DoctorPresenter(this, this);
        dpresenter.GetProviderType(0);
        dpresenter.GetDoctorType(0);
        dpresenter.GetSMCType(0);
        binding.toolbarCustom.setNavigationOnClickListener(v -> finish());
        pd=new ProgressDialog(PrescriptionApprovalListActivity.this);

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
       HashMap<String,String> filtera = new HashMap<>();
        filtera.put("Role", RoleType);
        filtera.put("AppStatus", "0");
        filtera.put("FromDt", "");
        filtera.put("ToDt", "");
        filtera.put("EmpId", "");
        filtera.put("GroupId", "0");
        filtera.put("ZoneId", "0");
        filtera.put("AreaId", "0");
        filtera.put("TerritoryId", "0");
        filtera.put("providertype","0");
        filtera.put("pharmatype","0");
        filtera.put("doctortype","0");
        Constants.filtermap=filtera;
        Constants.filterparams=params;
        hitApi(params, filtera);

        binding.swip.setOnRefreshListener(() -> {
            HashMap<String,String> filters = new HashMap<>();
            filters.put("Role", RoleType);
            filters.put("AppStatus", "0");
            filters.put("FromDt", "");
            filters.put("ToDt", "");
            filters.put("EmpId", "");
            filters.put("GroupId", "0");
            filters.put("ZoneId", "0");
            filters.put("AreaId", "0");
            filters.put("TerritoryId", "0");
            filters.put("providertype","0");
            filters.put("pharmatype","0");
            filters.put("doctortype","0");
            hitApi(params, filters);
            binding.swip.setRefreshing(false);
        });
        // binding.selectedMonthTxt.setText(UtilityHelper.monthNameArrayFull[month] + ',' + year);
        openFilter();
        binding.atteFilter.setOnClickListener(v -> {
          bsheetdlg.show();
        });
    }

    private void hitApi(String params, Map<String, String> filter) {
        pd.setMessage("Prescription Loading...");
        pd.show();
        pd.setCancelable(true);
        pd.setCanceledOnTouchOutside(true);
        presenter.getPrescApprovalList(params, filter);
    }

    //Global Filter
    private void openFilter() {
        bsheetdlg = new BottomSheetDialog(PrescriptionApprovalListActivity.this);
        bsheetdlg.setContentView(ftm.getRoot());
        bsheetdlg.setCanceledOnTouchOutside(true);
        ftm.filterMarketLayout.marketStructure.setVisibility(VISIBLE);
        ftm.filterStatusLayout.getRoot().setVisibility(VISIBLE);
        ftm.filterMonthYearLayout.getRoot().setVisibility(VISIBLE);
        ftm.linearLayoutemp.setVisibility(GONE);

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

        ftm.filterMonthYearLayout.ivDatePickerFromDate.setVisibility(VISIBLE);
        ftm.filterMonthYearLayout.ivDatePickerToDate.setVisibility(VISIBLE);
        ftm.filterMonthYearLayout.txtFromDate.setText(UtilityHelper._GetCurrentDate());
        ftm.filterMonthYearLayout.txtToDate.setText(UtilityHelper._GetCurrentDate());
        ftm.filterTypeLayout.tilCusttype.setVisibility(View.GONE);

        ftm.filterMonthYearLayout.ivDatePickerFromDate.setOnClickListener(v -> {
            UtilityHelper._datePickerDialogeForDates(ftm.filterMonthYearLayout.txtFromDate, PrescriptionApprovalListActivity.this);
        });
        ftm.filterMonthYearLayout.ivDatePickerToDate.setOnClickListener(v -> {
            UtilityHelper._datePickerDialogeForDates(ftm.filterMonthYearLayout.txtToDate, PrescriptionApprovalListActivity.this);
        });

        DoctorTypeVM r = new DoctorTypeVM();
        r.setDoctorTypeName("Select");
        dtypeList.add(0, r);
        ArrayAdapter<DoctorTypeVM> doctorTypeAdapter = new ArrayAdapter<>(PrescriptionApprovalListActivity.this, R.layout._custom_spinner_tv, dtypeList);
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
        ArrayAdapter<ModelProviderType> providerAdapter = new ArrayAdapter<>(PrescriptionApprovalListActivity.this, R.layout._custom_spinner_tv, providertypeList);
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

        ArrayAdapter<ModelSMCType> pharmaAdapter = new ArrayAdapter<>(PrescriptionApprovalListActivity.this, R.layout._custom_spinner_tv, pharmatypeList);
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
            fromdat = ftm.filterMonthYearLayout.txtFromDate.getText().toString();
            todate = ftm.filterMonthYearLayout.txtToDate.getText().toString();
            status = (String) ftm.filterStatusLayout.spinnerApprovalStatus.getSelectedItem();
            HashMap<String, String> filters = new HashMap<>();
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
            filters.put("Role", RoleType);
            filters.put("FromDt", fromdat);
            filters.put("ToDt", todate);
            filters.put("EmpId", "");
            filters.put("GroupId", String.valueOf(selectedGrpId));
            filters.put("ZoneId", String.valueOf(selectedZoneId));
            filters.put("AreaId", String.valueOf(selectedAreaId));
            filters.put("TerritoryId", String.valueOf(selectedTeriId));
            filters.put("providertype",selectedprogramId);
            filters.put("pharmatype",selectedpharmatypeId);
            filters.put("doctortype",selecteddoctortypeId);

            Constants.filtermap=filters;
            Constants.filterparams=params;
            hitApi(params, filters);
            bsheetdlg.cancel();
        });
        ftm.filterTopLayout.ivCancel.setOnClickListener(v -> {
            bsheetdlg.cancel();
        });
    }

    @Override
    public void onGetPrescApprovalList(List<PrescApprovalData> aList) {
        pd.dismiss();
        if (aList != null) {
            binding.count.setText(String.valueOf(aList.size()));
            prescAdapter = new PrescApprovalAdapter(PrescriptionApprovalListActivity.this, aList,this);
          //  LinearLayoutManager mLayoutManager = new LinearLayoutManager(PrescriptionApprovalListActivity.this);
          //  binding.rvPresclist.setLayoutManager(mLayoutManager);
            binding.rvPresclist.setLayoutManager(new LinearLayoutManager(PrescriptionApprovalListActivity.this));
            binding.rvPresclist.setItemAnimator(new DefaultItemAnimator());
            binding.rvPresclist.setAdapter(prescAdapter);
            binding.rvPresclist.setItemAnimator(null);
            binding.rvPresclist.scrollToPosition(0);
            prescAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onSaveSuccess(String message) {
     if(pd!=null || pd.isShowing())
     {
         pd.dismiss();
     }
        SnackBarManagement._warning_CustomMessage(binding.getRoot(),message);
        HashMap<String,String> filtera = new HashMap<>();
        filtera.put("Role", RoleType);
        filtera.put("AppStatus", "0");
        filtera.put("FromDt", "");
        filtera.put("ToDt", "");
        filtera.put("EmpId", "");
        filtera.put("GroupId", "0");
        filtera.put("ZoneId", "0");
        filtera.put("AreaId", "0");
        filtera.put("TerritoryId", "0");
        filtera.put("providertype","0");
        filtera.put("pharmatype","0");
        filtera.put("doctortype","0");
        hitApi(Constants.filterparams, Constants.filtermap);
       // hitApi(params, filtera);
    }

    @Override
    public void onError(String message) {
        pd.dismiss();
    }

    @Override
    public void vGroup(List<Group> groupList) {
        try {
            if (groupList != null) {
                Group g = new Group();
                g.setGroupName("Select");
                groupList.add(0, g);

                ArrayAdapter<Group> dataAdapter = new ArrayAdapter<>(PrescriptionApprovalListActivity.this, android.R.layout.simple_spinner_item, groupList);
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

                ArrayAdapter<Region> dataAdapter = new ArrayAdapter<>(PrescriptionApprovalListActivity.this, android.R.layout.simple_spinner_item, regionList);
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
                ArrayAdapter<Area> dataAdapter = new ArrayAdapter<>(PrescriptionApprovalListActivity.this, android.R.layout.simple_spinner_item, areaList);
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

                ArrayAdapter<Teritorry> dataAdapter = new ArrayAdapter<>(PrescriptionApprovalListActivity.this, android.R.layout.simple_spinner_item, teritoryList);
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

    }

    @Override
    public void vMarket(List<Market> marketList) {

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

    @Override
    protected void onRestart() {
        super.onRestart();
        try {
            HashMap<String, String> filterz = new HashMap<>();
            if (status.equals("Select")) {
                filterz.put("AppStatus", "");
            } else {
                filterz.put("AppStatus", status);
            }
            filterz.put("Role", RoleType);
            filterz.put("FromDt", fromdat);
            filterz.put("ToDt", todate);
            filterz.put("EmpId", "");
            filterz.put("GroupId", String.valueOf(selectedGrpId));
            filterz.put("ZoneId", String.valueOf(selectedZoneId));
            filterz.put("AreaId", String.valueOf(selectedAreaId));
            filterz.put("TerritoryId", String.valueOf(selectedTeriId));
            filterz.put("providertype",selectedprogramId);
            filterz.put("pharmatype",selectedpharmatypeId);
            filterz.put("doctortype",selecteddoctortypeId);
            hitApi(params, filterz);
        } catch (Exception exception) {

        }
    }

    @Override
    public void approvalKlick(PrescApprovalData data) {
        if (RoleTypeId == 2) {
            next = data.getRSMEMPId();
        }
        if (RoleTypeId == 3) {
            next = data.getNSMEMPId();
        }
        if (RoleTypeId == 4) {
            next = 0;
        }
        PrescApprovalRQ req = new PrescApprovalRQ();
        int step = data.getStep();
        int fstep = step + 1;

        req.setPrescriptionApprovalId(0);
        req.setFromEmpId(empid);
        req.setToEmpId(next);
        req.setTableId(data.getPrescriptionId());
        req.setStatus("Verified");//Accepted==approve for Admin
        req.setType(data.getType());
        req.setStep(fstep);
        req.setEntryByApp(empid);
        String entrydate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        String entrytime = new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(new Date());
        req.setEntryDateApp(entrydate);
        req.setEntryTimeApp(entrytime);
        req.setMenuId(379);

        pd=new ProgressDialog(PrescriptionApprovalListActivity.this);
        pd.setMessage("Submitting...");
        pd.show();
        Gson gson1=new Gson();
        String dd=gson1.toJson(req);
        System.out.println(dd);
        presenter.SavePrescApproval(req);
    }
}