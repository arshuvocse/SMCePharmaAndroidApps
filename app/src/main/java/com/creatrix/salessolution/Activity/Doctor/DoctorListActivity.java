package com.creatrix.salessolution.Activity.Doctor;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.creatrix.salessolution.Activity.Customer.CustomerListActivity;
import com.creatrix.salessolution.Activity.Doctor.Approval.DoctorApprovalListActivity;
import com.creatrix.salessolution.DBAdapter.DBDoctor.DBDoctorHelper;
import com.creatrix.salessolution.DBInfo.DBHelperMain;
import com.creatrix.salessolution.Interface.IDoctor;
import com.creatrix.salessolution.Interface.IMarketStracture;
import com.creatrix.salessolution.Model.Doctor.Brand;
import com.creatrix.salessolution.Model.Doctor.ContactTypeVM;
import com.creatrix.salessolution.Model.Doctor.DoctorCategory;
import com.creatrix.salessolution.Model.Doctor.DoctorChamberTypeVM;
import com.creatrix.salessolution.Model.Doctor.DoctorDegreeViewModel;
import com.creatrix.salessolution.Model.Doctor.DoctorDesignation;
import com.creatrix.salessolution.Model.Doctor.DoctorListViewModel;
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
import com.creatrix.salessolution.R;
import com.creatrix.salessolution.RecyclerAdapter._doctor_listRecyclerAdapter;
import com.creatrix.salessolution.UtilityHelper.NetworkInformation;
import com.creatrix.salessolution.UtilityHelper.SessionManagement;
import com.creatrix.salessolution.UtilityHelper.SnackBarManagement;
import com.creatrix.salessolution.UtilityHelper.UtilityHelper;
import com.creatrix.salessolution.databinding.ActivityDoctorListBinding;
import com.creatrix.salessolution.databinding.FilterMasterBinding;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.math.BigInteger;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.HttpUrl;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class DoctorListActivity extends AppCompatActivity implements IMarketStracture.View, IDoctor.View{
    ActivityDoctorListBinding binding;
    private _doctor_listRecyclerAdapter mAdapter;
    List<DoctorListViewModel> aList = new ArrayList<>();

    DBDoctorHelper helper;
    DBHelperMain dbHelperMain;
    DoctorPresenter dpresenter;

    BottomSheetDialog bsheetdlg;
    String role, empId, today,fromType="";
    IMarketStracture.Presenter mkpresenter;
    FilterMasterBinding ftm;
    int selectedGrpId, selectedZoneId, selectedAreaId, selectedTeriId, selectedSTeriId, selectedMarketId;

    List<DoctorTypeVM> dtypeList;
    List<ModelProviderType> providertypeList;
    List<ModelSMCType> pharmatypeList;
    String selecteddoctortypeId="0", selectedprogramId="0",selectedpharmatypeId="0";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDoctorListBinding.inflate(getLayoutInflater());
        //  setContentView(R.layout.activity_doctor_list);
        setContentView(binding.getRoot());
        helper = new DBDoctorHelper(DoctorListActivity.this);
        dbHelperMain = new DBHelperMain(DoctorListActivity.this);
        ftm = FilterMasterBinding.inflate(getLayoutInflater());
        mkpresenter = new MarketStructurePresenter(this, DoctorListActivity.this);

        binding.toolbarCustom.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        fromType = getIntent().getStringExtra("From");
        dpresenter = new DoctorPresenter(this, this);
        SessionManagement session = new SessionManagement(getApplicationContext());
        session.checkLogin();
        HashMap<String, String> user = session.getUserDetails();
        String userName = user.get(SessionManagement.KEY_LoginName);
        int empId = Integer.parseInt(user.get(SessionManagement.KEY_EmpId));
        role = user.get(SessionManagement.KEY_EmpRoleType);

        dpresenter.GetProviderType(0);
        dpresenter.GetDoctorType(0);
        dpresenter.GetSMCType(0);
        getDocFrom(empId, fromType);

        binding.searchEd.addTextChangedListener(new TextWatcher() {
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
        openFilter();
        binding.cfilter.setOnClickListener(v -> {
            bsheetdlg.show();
        });
    }
    private void openFilter() {
    /*    FilterMasterBinding ftm;
        ftm = FilterMasterBinding.inflate(getLayoutInflater());*/
        bsheetdlg = new BottomSheetDialog(DoctorListActivity.this);
        bsheetdlg.setContentView(ftm.getRoot());
        bsheetdlg.setCanceledOnTouchOutside(true);
        //   mkpresenter.GetGroupLocal();
        ftm.filterStatusLayout.linearLayout.setVisibility(GONE);
        ftm.filterMarketLayout.marketStructure.setVisibility(VISIBLE);

        ftm.linearLayoutemp.setVisibility(GONE);
        ftm.filterTypeLayout.tilDoctype.setVisibility(VISIBLE);
        ftm.filterTypeLayout.tilProgtypetype.setVisibility(View.VISIBLE);
        ftm.filterTypeLayout.tilCusttype.setVisibility(GONE);
        ftm.filterTypeLayout.tilPharmatype.setVisibility(View.VISIBLE);
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
        ftm.filterMonthYearLayout.getRoot().setVisibility(GONE);

        DoctorTypeVM r = new DoctorTypeVM();
        r.setDoctorTypeName("Select");
        dtypeList.add(0, r);
        ArrayAdapter<DoctorTypeVM> doctorTypeAdapter = new ArrayAdapter<>(DoctorListActivity.this, R.layout._custom_spinner_tv, dtypeList);
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

        // ArrayAdapter<ProgramType> programAdapter = new ArrayAdapter<>(DoctorApprovalListActivity.this, R.layout._custom_spinner_tv, ptypeList);
        ModelProviderType ptype = new ModelProviderType();
        ptype.setProviderType("Select");
        providertypeList.add(0, ptype);
        ArrayAdapter<ModelProviderType> providerAdapter = new ArrayAdapter<>(DoctorListActivity.this, R.layout._custom_spinner_tv, providertypeList);
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

        ArrayAdapter<ModelSMCType> pharmaAdapter = new ArrayAdapter<>(DoctorListActivity.this, R.layout._custom_spinner_tv, pharmatypeList);
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
            try {
                List<DoctorListViewModel> data=helper.getDoctorListFilterSQLite(String.valueOf(selectedGrpId), String.valueOf(selectedZoneId), String.valueOf(selectedAreaId)
                        , String.valueOf(selectedTeriId), String.valueOf(selectedSTeriId), String.valueOf(selectedMarketId),String.valueOf(selectedprogramId),String.valueOf(selecteddoctortypeId),String.valueOf(selectedpharmatypeId));
                LoadinView(data,fromType,"Offline");
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
    private void filter(String aName) {
        try {
            List<DoctorListViewModel> arrayList = new ArrayList<>();
            for (DoctorListViewModel item : aList) {
                if ((item.getDoctorName().toLowerCase().contains(aName.toLowerCase()) || (item.getDoctorCode().contains(aName)))) {
                    arrayList.add(item);
                }
            }

            mAdapter.filterList(arrayList);
        } catch (Exception ex) {
            Log.e("TAG", "filter: ", ex);
        }

    }
    public void getDocFrom(int empId, String fromType) {
        try {
            List<DoctorListViewModel> data=helper.getDoctorListFromSQLite();
            LoadinView(data,fromType,"Offline");
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    /*    if (!NetworkInformation.isConnected(DoctorListActivity.this)) {
            List<DoctorListViewModel> data=helper.getDoctorListFromSQLite();
            LoadinView(data,fromType,"Offline");

        } else {

            try {
                progressDoalog = new ProgressDialog(DoctorListActivity.this);
                progressDoalog.setMessage("Doctor is loading....");
                progressDoalog.show();
                progressDoalog.setCanceledOnTouchOutside(false);
                try {
                    ApiDoctorCall service = RetrofitClientInstance.getRetrofitInstance().create(ApiDoctorCall.class);
                    Call<List<DoctorListViewModel>> call = service.GetDoctorList(empId);
                    HttpUrl ds = call.request().url();
                    call.enqueue(new Callback<List<DoctorListViewModel>>() {
                        @Override
                        public void onResponse(Call<List<DoctorListViewModel>> call, Response<List<DoctorListViewModel>> response) {
                            progressDoalog.dismiss();
                            LoadinView(response.body(),fromType,"Online");
                        }

                        @Override
                        public void onFailure(Call<List<DoctorListViewModel>> call, Throwable t) {
                            progressDoalog.dismiss();
                            if (t instanceof SocketTimeoutException) {
                                ErrorView("Slow Network detected");
                            } else {
                                ErrorView("Some Error occured");
                            }
                        }
                    });

                } catch (Exception ex) {
                    ErrorView("Some Error occured");
                    progressDoalog.dismiss();
                }

               *//* new AlertDialog.Builder(DoctorListActivity.this)
                        .setIcon(R.drawable.ic_warning)
                        .setTitle("No Internet Connection ")
                        .setMessage("Turn it on ")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                onBackPressed();
                            }
                        }).setCancelable(false).show();*//*
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }*/
    }
    public long getDoctorCount() {
        SQLiteDatabase db = dbHelperMain.getReadableDatabase();
        long count = DatabaseUtils.queryNumEntries(db, "tblDoctorInfo");
        db.close();
        return count;
    }
    public void LoadinView(List<DoctorListViewModel> mList, String fromType,String nettype) {
        if (mList != null) {
            if (mList.size()>0) {
                binding.docCountTxt.setText(String.valueOf(mList.size()));
            } else {
                binding.docCountTxt.setText("0");
            }
           /* if(nettype.equals("Online"))
            {
                int countd = 0;
                if (mList.size() > 0) {
                    countd = mList.size();
                    binding.docCountTxt.setText(String.valueOf(countd));
                    //Toast.makeText(this, "data "+String.valueOf(countd), Toast.LENGTH_SHORT).show();
                } else {
                    binding.docCountTxt.setText("0");
                }
            }else */
/*                if(nettype.equals("Offline")){
                long countd = 0;
                try {
                    countd = 0;
                    if (getDoctorCount()>0) {
                        countd = getDoctorCount();
                        binding.docCountTxt.setText(String.valueOf(countd));
                    } else {
                        binding.docCountTxt.setText("0");
                    }
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }*/

            aList.clear();
            aList = mList;
            mAdapter = new _doctor_listRecyclerAdapter(mList, fromType);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            binding.recyclerView.setLayoutManager(mLayoutManager);
            binding.recyclerView.setItemAnimator(new DefaultItemAnimator());
            binding.recyclerView.setAdapter(mAdapter);
            binding.recyclerView.setItemAnimator(null);
            binding.recyclerView.scrollToPosition(0);
            mAdapter.notifyDataSetChanged();
        }

    }
    public void ErrorView(String msg) {
        Toast.makeText(DoctorListActivity.this, msg, Toast.LENGTH_LONG).show();
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

                ArrayAdapter<Group> dataAdapter = new ArrayAdapter<>(DoctorListActivity.this, android.R.layout.simple_spinner_item, groupList);
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

                ArrayAdapter<Region> dataAdapter = new ArrayAdapter<>(DoctorListActivity.this, android.R.layout.simple_spinner_item, regionList);
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
                ArrayAdapter<Area> dataAdapter = new ArrayAdapter<>(DoctorListActivity.this, android.R.layout.simple_spinner_item, areaList);
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

                ArrayAdapter<Teritorry> dataAdapter = new ArrayAdapter<>(DoctorListActivity.this, android.R.layout.simple_spinner_item, teritoryList);
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

                ArrayAdapter<SubTeritorry> dataAdapter = new ArrayAdapter<>(DoctorListActivity.this, android.R.layout.simple_spinner_item, steritoryList);
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

                ArrayAdapter<Market> dataAdapter = new ArrayAdapter<>(DoctorListActivity.this, android.R.layout.simple_spinner_item, marketList);
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