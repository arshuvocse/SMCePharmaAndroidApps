package com.creatrix.salessolution.Activity.Doctor.AddDoctor;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.DigitsKeyListener;
import android.text.method.TextKeyListener;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.creatrix.salessolution.Activity.Doctor.Approval.DoctorApprovalListActivity;
import com.creatrix.salessolution.Activity.Doctor.Approval.Model.ChembarTypeWithName;
import com.creatrix.salessolution.Activity.Doctor.Approval.Model.DoctorApproveModel;
import com.creatrix.salessolution.Activity.MainDashboardActivity;
import com.creatrix.salessolution.DBAdapter.DBCrudHelper;
import com.creatrix.salessolution.DBAdapter.DBDDTU.DBDDTUHelper;
import com.creatrix.salessolution.DBAdapter.DBDoctor.DBDoctorHelper;
import com.creatrix.salessolution.Interface.IDoctor;
import com.creatrix.salessolution.Interface.IMarketStracture;
import com.creatrix.salessolution.Model.Doctor.Brand;
import com.creatrix.salessolution.Model.Doctor.ChembarList;
import com.creatrix.salessolution.Model.Doctor.ContactTypeVM;
import com.creatrix.salessolution.Model.Doctor.DoctorCategory;
import com.creatrix.salessolution.Model.Doctor.DoctorChamberTypeVM;
import com.creatrix.salessolution.Model.Doctor.DoctorContact;
import com.creatrix.salessolution.Model.Doctor.DoctorDegreeViewModel;
import com.creatrix.salessolution.Model.Doctor.DoctorDesignation;
import com.creatrix.salessolution.Model.Doctor.DoctorSM;
import com.creatrix.salessolution.Model.Doctor.DoctorSpecialityViewModel;
import com.creatrix.salessolution.Model.Doctor.DoctorTypeVM;
import com.creatrix.salessolution.Model.Doctor.ProgramType;
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
import com.creatrix.salessolution.Model.StationType;
import com.creatrix.salessolution.Presenter.DoctorPresenter;
import com.creatrix.salessolution.Presenter.MarketStructurePresenter;
import com.creatrix.salessolution.R;
import com.creatrix.salessolution.RecyclerAdapter._chamber_ListAdapter;
import com.creatrix.salessolution.RecyclerAdapter._contact_ListAdapter;
import com.creatrix.salessolution.RecyclerAdapter._specialday_ListAdapter;
import com.creatrix.salessolution.Services.Constants;
import com.creatrix.salessolution.UtilityHelper.NetworkInformation;
import com.creatrix.salessolution.UtilityHelper.SessionManagement;
import com.creatrix.salessolution.UtilityHelper.SnackBarManagement;
import com.creatrix.salessolution.UtilityHelper.UtilityHelper;
import com.creatrix.salessolution.databinding.ActivityDoctorBinding;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class DoctorActivity extends AppCompatActivity implements IDoctor.View, IMarketStracture.View {
    ActivityDoctorBinding viewBinding;
    Dialog popAdd,popContact;
    private static String monthNameArray[] = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
    public static String monthNameArrayFull[] = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
    DatePickerDialog picker;

    IDoctor.Presenter presenter;
    IMarketStracture.Presenter mpresenter;

    String[] listItemsBrand;
    boolean[] checkedItems_Brand;
    ArrayList<Integer> mUserItems_Brand = new ArrayList<>();

    String[] listItemsDegree;
    boolean[] checkedItems;
    ArrayList<Integer> mUserItems = new ArrayList<>();

    String[] listItems_Specility;
    boolean[] checkedItems_Specility;
    ArrayList<Integer> mUserItems_Specility = new ArrayList<>();

    String[] listItems_Type;
    boolean[] checkedItems_Type;
    ArrayList<Integer> mUserItems_Type = new ArrayList<>();

    String[] listItems_ins;
    boolean[] checkedItems_ins;
    ArrayList<Integer> mUserItems_ins = new ArrayList<>();


    DBCrudHelper dbCrudHelper;
    DBDoctorHelper dbDoctorHelper;
    DBDDTUHelper dbddtuHelper;
    _chamber_ListAdapter itemsAdapter;
    _contact_ListAdapter contactAdapter;
    _specialday_ListAdapter spdayAdapter;

    List<DoctorDesignation> doc_desigList;
    List<DoctorDegreeViewModel> doc_degreeList;
    List<DoctorSpecialityViewModel> doc_specilityList;
    List<DoctorTypeVM> doc_typeList;
    List<InstitutionVM> doc_institutionList;
    List<DoctorChamberTypeVM> doc_chamberList;
    List<Brand> brandList;


    List<DoctorChamberTypeVM> itemList = new ArrayList<>();
    List<ChembarList> chembarLists;
    List<SpecialDay> specialdayLists = new ArrayList<>();
    List<DoctorContact> doccontactLists = new ArrayList<>();
    DoctorDesignation desigSelected;

    int selectedRegionId, selectedAreaId, selectedTeriId, selectedSTeri, selectedMarket;
    int selectedDoctypeId, selectedchembarTypeId, selectedsclialdayId,selectedcontactTypeId;
    String selectedchembarTypeName,selectedcontactTypeName,selectedsclialdayName;
    TextView cmbrtype,contacttype;
    EditText chamberName,contactName;
    TextInputLayout til_chamberDD;
    String roleType;
    int brand_count = 0;
    int selectedbrand_count = 0;
    int brand_countValue, empId, doctorid;
    DoctorApproveModel docInfos;

    String edit_dName,spcialday;
    int edit_did;
    public String blockCharacterSet = "@~'#^|$%&*!/?>";
    boolean is_Contact,is_Chamber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //    setContentView(R.layout.activity_doctor);
        viewBinding = com.creatrix.salessolution.databinding.ActivityDoctorBinding.inflate(getLayoutInflater());
        View mainView = viewBinding.getRoot();

        dbCrudHelper = new DBCrudHelper(DoctorActivity.this);
        dbDoctorHelper = new DBDoctorHelper(DoctorActivity.this);
        dbddtuHelper = new DBDDTUHelper(DoctorActivity.this);
        setContentView(mainView);
        viewBinding.docName.setFilters(new InputFilter[]{filter});

        presenter = new DoctorPresenter(this, DoctorActivity.this);
        mpresenter = new MarketStructurePresenter(this, DoctorActivity.this);

        viewBinding.toolbarCustom.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        SessionManagement session = new SessionManagement(getApplicationContext());
        //session.checkLogin();

        HashMap<String, String> user = session.getUserDetails();
        String userName = user.get(SessionManagement.KEY_LoginName);
        empId = Integer.parseInt(user.get(SessionManagement.KEY_EmpId));
        roleType = user.get(SessionManagement.KEY_EmpRoleType);
        switch (Constants.WHO) {
            case "DocApprovalAC":
                viewBinding.toolbarTitle.setText("Edit Doctor");
                viewBinding.psubmitBnt.setVisibility(View.GONE);
                Gson gson = new Gson();
                docInfos = gson.fromJson(getIntent().getStringExtra("DocEditdata"), DoctorApproveModel.class);
                presenter.GetDoctorDesignation(0);
                presenter.GetDoctorType(0);
                presenter.GetSpeciality();
                presenter.GetInstitute();
                presenter.GetBrand();
                presenter.GetDoctorCategory(0);
                presenter.GetChamber(0);
               // presenter.GetProgramType(0);
                presenter.GetProviderType(0);
                presenter.GetSMCType(0);

                presenter.GetContactType(0);
                presenter.GetSpecialType(0);

                mpresenter.GetRegionLocal(0);
                mpresenter.GetAreaLocal(0);
                mpresenter.GetTeritoryLocal(0);
                mpresenter.GetSTeritoryLocal(0);
                mpresenter.GetMarketLocal(0);

                viewBinding.regionSpinner.setBackground(null);
                viewBinding.areaSpinner.setBackground(null);
                viewBinding.territorySpinner.setBackground(null);
                viewBinding.sterritorySpinner.setBackground(null);
                viewBinding.marketSpinner.setBackground(null);

                viewBinding.regionSpinner.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        return true;
                    }
                });
                viewBinding.areaSpinner.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        return true;
                    }
                });
                viewBinding.territorySpinner.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        return true;
                    }
                });
                viewBinding.sterritorySpinner.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        return true;
                    }
                });
                viewBinding.marketSpinner.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        return true;
                    }
                });
                setupEdit(roleType, empId, docInfos);
                break;
            case "AddDoctor":
                if (!NetworkInformation.isConnected(this)) {
                    SnackBarManagement._warning_CustomMessage(viewBinding.master, "Your Internet Connection is Off.\nYou Can't Submit");
                    viewBinding.psubmitBnt.setVisibility(View.GONE);
                } else {
                    viewBinding.psubmitBnt.setVisibility(View.VISIBLE);
                }
                viewBinding.pupdateBnt.setVisibility(View.GONE);
                //Role wise spinner populate
                try {
                    switch (roleType) {
                        case "MIO":
                            viewBinding.areadiv.setVisibility(View.GONE);
                            viewBinding.regiondiv.setVisibility(View.GONE);
                            mpresenter.GetTeritoryLocal(0);
                            break;
                        case "AM":
                            viewBinding.regiondiv.setVisibility(View.GONE);
                            viewBinding.areadiv.setVisibility(View.VISIBLE);
                            mpresenter.GetAreaLocal(0);
                            break;
                        case "DZSM":
                            viewBinding.regiondiv.setVisibility(View.VISIBLE);
                            viewBinding.areadiv.setVisibility(View.VISIBLE);
                            mpresenter.GetRegionLocal(0);
                            break;
                    }
                    viewBinding.doctorTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            DoctorTypeVM doctype = (DoctorTypeVM) parent.getSelectedItem();
                            selectedDoctypeId = doctype.getDoctorTypeId();
                            mUserItems.clear();
                            viewBinding.degreeListStr.setText("");
                            presenter.GetDegree(selectedDoctypeId);
                            //viewBinding.degreeListStr.setText(item);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                    iniPopup();
                    iniPopupContact();


                } catch (Exception exception) {
                    exception.printStackTrace();
                }
                presenter.GetDoctorDesignation(0);
                presenter.GetDoctorType(0);
                presenter.GetSpeciality();
                presenter.GetInstitute();
                presenter.GetBrand();
                presenter.GetDoctorCategory(0);
                presenter.GetChamber(0);
                presenter.GetProviderType(0);
                presenter.GetSMCType(0);
                presenter.GetContactType(0);
                presenter.GetSpecialType(0);
                viewBinding.regionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        Region region = (Region) parent.getSelectedItem();
                        selectedRegionId = region.getRegionId();
                        mpresenter.GetAreaLocal(selectedRegionId);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                viewBinding.areaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        Area area = (Area) parent.getSelectedItem();
                        selectedAreaId = area.getAreaId();
                        mpresenter.GetTeritoryLocal(selectedAreaId);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                viewBinding.territorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        Teritorry teri = (Teritorry) parent.getSelectedItem();
                        selectedTeriId = teri.getTerritoryId();
                        mpresenter.GetSTeritoryLocal(selectedTeriId);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                viewBinding.sterritorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        SubTeritorry steri = (SubTeritorry) parent.getSelectedItem();
                        selectedSTeri = steri.getSubTerritoryId();
                        mpresenter.GetMarketLocal(selectedSTeri);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                viewBinding.marketSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        Market market = (Market) parent.getSelectedItem();
                        selectedMarket = market.getMarketId();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                break;
        }


        viewBinding.datePickerDocDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UtilityHelper._datePickerDialogeForDates_DisableNextDates(viewBinding.scecialdateTxt, DoctorActivity.this);
            }
        });
        LocationManager locationManagerdd = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (locationManagerdd.isProviderEnabled(locationManagerdd.GPS_PROVIDER)) {
        } else {
            showGPSDisabledAlertToUser();
        }
        // viewBinding.docName.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        viewBinding.degreeAdd.setOnClickListener(view -> LoadDegree());
        viewBinding.specialityAdd.setOnClickListener(view -> LoadSpecilityInView());
        //viewBinding.typeAdd.setOnClickListener(view -> LoadDocTypeInView());
        viewBinding.institueAdd.setOnClickListener(view -> LoadInstituteInView());
        viewBinding.brandAdd.setOnClickListener(v -> LoadBrandInView());
        viewBinding.psubmitBnt.setOnClickListener(v -> {
            if (TextUtils.isEmpty(viewBinding.docName.getText().toString())) {
                SnackBarManagement._warning_CustomMessage(viewBinding.masterLayoutId, "Doctor Name is required");
                return;
            }
            if (TextUtils.isEmpty(viewBinding.degreeListStr.getText().toString())) {
                SnackBarManagement._warning_CustomMessage(viewBinding.masterLayoutId, "Degree is required");
                return;
            }
            if (TextUtils.isEmpty(viewBinding.specilityListStr.getText().toString())) {
                SnackBarManagement._warning_CustomMessage(viewBinding.masterLayoutId, "Speciality is required");
                return;
            }
            if (TextUtils.isEmpty(viewBinding.brandListStr.getText().toString())) {
                SnackBarManagement._warning_CustomMessage(viewBinding.masterLayoutId, "Brand is required");
                return;
            }
            if (itemList.size() == 0) {
                SnackBarManagement._warning_CustomMessage(viewBinding.masterLayoutId, "Minimum 1 Chamber  is required");
                return;
            }
            if (doccontactLists.size()==0) {
                SnackBarManagement._warning_CustomMessage(viewBinding.masterLayoutId, "Contact is required");
                return;
            }
            // viewBinding.rvChamberlist.
            SubmitClick(empId);
        });
    }
    public void SubmitClick(int empId) {
        try {
            String docName = viewBinding.docName.getText().toString();
            String docDegree = viewBinding.degreeListStr.getText().toString();
            String docSpeciality = viewBinding.specilityListStr.getText().toString();
            String institute = viewBinding.instituteListStr.getText().toString();
            String docBrand = viewBinding.brandListStr.getText().toString();
            String docAddress = viewBinding.docAddress.getText().toString();


            DoctorTypeVM doctorType = (DoctorTypeVM) viewBinding.doctorTypeSpinner.getSelectedItem();
            int dtypeid = doctorType.getDoctorTypeId();

           /* DoctorChamberTypeVM chamberType = (DoctorChamberTypeVM) viewBinding.chamberSpinner.getSelectedItem();
            int chamberId = chamberType.getChamberId();
            String chamberAddress = viewBinding.chamberAddress.getText().toString();*/

            //TODO:Contact Type
         /*   ContactTypeVM contactType = (ContactTypeVM) viewBinding.contactTypeSpinner.getSelectedItem();
            int contactId = contactType.getContactTypeId();
            String contactDD = viewBinding.contactTxt.getText().toString();*/

            DoctorCategory catType = (DoctorCategory) viewBinding.doctorCategorySpinner.getSelectedItem();
            int catTypeId = catType.getCategoryId();

            //ProgramType progType = (ProgramType) viewBinding.programTypeSpinner.getSelectedItem();
            ModelProviderType progType = (ModelProviderType) viewBinding.programTypeSpinner.getSelectedItem();
            int programTypeId = progType.getProviderTypeId();

            ModelSMCType smcType = (ModelSMCType) viewBinding.smcTypeSpinner.getSelectedItem();
            int smcTypeId = smcType.getSMCTypeId();

           /* SpecialDay spday = (SpecialDay) viewBinding.spcialdayTypeSpinner.getSelectedItem();
            int spdayId = spday.getSpecialDayId();*/

           /* StationType stationType = (StationType) viewBinding.custStationTypeSpinner.getSelectedItem();
            int stationTypeId = stationType.getStationTypeId();*/


            DoctorDesignation docDesi = (DoctorDesignation) viewBinding.designationSpinner.getSelectedItem();
            int designationId = docDesi.getDesignationId();

            chembarLists = new ArrayList<>();
            for (int i = 0; i < itemList.size(); i++) {
                String chamberName = itemList.get(i).getChamberName();
                int chamberTypeId = itemList.get(i).getChamberTypeId();
                ChembarList cl = new ChembarList();
                cl.setChamberTypeId(chamberTypeId);
                cl.setName(chamberName);
                chembarLists.add(cl);
            }
          /*  doccontactLists = new ArrayList<>();
            for (int i = 0; i < doccontactLists.size(); i++) {
                String contactName = doccontactLists.get(i).getContact();
                int contactTypeId = doccontactLists.get(i).getContactTypeId();
                ChembarList cl = new ChembarList();
                cl.setChamberTypeId(chamberTypeId);
                cl.setName(chamberName);
                chembarLists.add(cl);
            }*/

            DoctorSM doctorSM = new DoctorSM();
            doctorSM.setDoctorName(docName);
            doctorSM.setDoctorAddress(docAddress);
            doctorSM.setDesignationId(designationId);
            doctorSM.setDegreeStr(docDegree);

            doctorSM.setSpecialityStr(docSpeciality);
            doctorSM.setInstitutionSTr(institute);
            doctorSM.setBrandStr(docBrand);

            doctorSM.setDoctorTypeId(dtypeid);
            doctorSM.setDoctorTypeStr(String.valueOf(dtypeid));
            doctorSM.setDoctorCategoryId(catTypeId);
            doctorSM.setaChemberListDAO(chembarLists);
            doctorSM.setaDoctorContactDAO(doccontactLists);
            doctorSM.setaDoctorSpecialDAO(specialdayLists);
            doctorSM.setProgramTypeId(programTypeId);
            doctorSM.setSMCTypeId(smcTypeId);

            doctorSM.setAreaId(selectedAreaId);
            doctorSM.setTerritoryId(selectedTeriId);
            doctorSM.setSubTerritoryId(selectedSTeri);
            if(selectedMarket>0)
            {
                doctorSM.setMarketId(selectedMarket);
            }else {
                SnackBarManagement._warning_CustomMessage(viewBinding.getRoot(),"Market Can't Be Empty");
            }

            doctorSM.setSessionUser(empId);
            // doctorSM.setRemarks(viewBinding.remarksTxt.getText().toString());
            Gson gs = new Gson();
            String ss = gs.toJson(doctorSM);
            System.out.println("submit : " + ss);
            presenter.SaveDoctor(doctorSM, "Submit");


        } catch (Exception ex) {
            Log.e("doctorSubmit-Ex", "SubmitClick: ", ex);
        }


    }
    public void LoadDegree() {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(DoctorActivity.this);
        mBuilder.setTitle("Doctor Degree");
        mBuilder.setMultiChoiceItems(listItemsDegree, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int position, boolean isChecked) {
                if (isChecked) {
                    mUserItems.add(position);
                } else {
                    mUserItems.remove((Integer.valueOf(position)));
                }
            }
        });
        mBuilder.setCancelable(false);
        mBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                String item = "";
                for (int i = 0; i < mUserItems.size(); i++) {
                    item = item + listItemsDegree[mUserItems.get(i)];
                    if (i != mUserItems.size() - 1) {
                        item = item + ",";
                    }
                }
                viewBinding.degreeListStr.setText(item);
            }
        });
        mBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        mBuilder.setNeutralButton("Clear All", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                try {
                    for (int i = 0; i < checkedItems.length; i++) {
                        checkedItems[i] = false;
                        mUserItems.clear();
                        viewBinding.degreeListStr.setText("");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            }
        });
        AlertDialog mDialog = mBuilder.create();
        mDialog.show();

    }
    public void LoadSpecilityInView() {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(DoctorActivity.this);
        mBuilder.setTitle("Doctor Specialties");
        mBuilder.setMultiChoiceItems(listItems_Specility, checkedItems_Specility, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int position, boolean isChecked) {
                if (isChecked) {
                    mUserItems_Specility.add(position);
                } else {
                    mUserItems_Specility.remove((Integer.valueOf(position)));
                }
            }
        });
        mBuilder.setCancelable(false);
        mBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                String item = "";
                for (int i = 0; i < mUserItems_Specility.size(); i++) {
                    item = item + listItems_Specility[mUserItems_Specility.get(i)];
                    if (i != mUserItems_Specility.size() - 1) {
                        item = item + ",";
                    }
                }
                viewBinding.specilityListStr.setText(item);
            }
        });
        mBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        mBuilder.setNeutralButton("Clear All", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                try {
                    for (int i = 0; i < checkedItems_Specility.length; i++) {
                        checkedItems_Specility[i] = false;
                        mUserItems_Specility.clear();
                        viewBinding.specilityListStr.setText("");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        AlertDialog mDialog = mBuilder.create();
        mDialog.show();

    }
    public void LoadInstituteInView() {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(DoctorActivity.this);
        mBuilder.setTitle("Institute");
        mBuilder.setMultiChoiceItems(listItems_ins, checkedItems_ins, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int position, boolean isChecked) {
                if (isChecked) {
                    mUserItems_ins.add(position);
                } else {
                    mUserItems_ins.remove((Integer.valueOf(position)));
                }
            }
        });
        mBuilder.setCancelable(false);
        mBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                String item = "";
                for (int i = 0; i < mUserItems_ins.size(); i++) {
                    item = item + listItems_ins[mUserItems_ins.get(i)];
                    if (i != mUserItems_ins.size() - 1) {
                        item = item + ",";
                    }
                }
                viewBinding.instituteListStr.setText(item);
            }
        });

        mBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        mBuilder.setNeutralButton("Clear All", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                try {
                    for (int i = 0; i < checkedItems_ins.length; i++) {
                        checkedItems_ins[i] = false;
                        mUserItems_ins.clear();
                        viewBinding.instituteListStr.setText("");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            }
        });

        AlertDialog mDialog = mBuilder.create();
        mDialog.show();

    }
    public void LoadBrandInView() {
        brand_countValue = dbDoctorHelper.maxBrandVal();
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(DoctorActivity.this);
        mBuilder.setTitle("Brand "+"Maximum Brand Should Be :" + brand_countValue);
                mBuilder.setMultiChoiceItems(listItemsBrand, checkedItems_Brand, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int position, boolean isChecked) {
                        int selectedband = 0;
                        if (isChecked) {
                            //selectedbrand_count++;
                            selectedband++;
                            mUserItems_Brand.add(position);
                            if (brand_count < brand_countValue) {
                                //checkedItems_Brand[position] = isChecked;
                                brand_count++;
                            } else {
                                mUserItems_Brand.remove((Integer.valueOf(position)));
                                brand_count--;
                                // checkedItems_Brand[which]=false;
                              //  SnackBarManagement._error_CustomMessage(viewBinding.masterLayoutId, "Maximum Brand Should Be" + brand_countValue);
                                Toast.makeText(DoctorActivity.this, "Maximum Brand Should Be : " + brand_countValue, Toast.LENGTH_SHORT).show();
                            }
                        } /*else if (checkedItems_Brand.contains(String.valueOf(array[which]))) {
                            selectedgenre.remove(String.valueOf(array[which]));
                            checkedGenres[which] = false;
                        }*/ else {
                            mUserItems_Brand.remove((Integer.valueOf(position)));
                            brand_count--;
                        }

                    }
                });
        mBuilder.setCancelable(false);
        mBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                if (brand_count > brand_countValue) {
                    try {
                        for (int i = 0; i < checkedItems_Brand.length; i++) {
                            checkedItems_Brand[i] = false;
                            mUserItems_Brand.clear();
                            brand_count = 0;
                            viewBinding.brandListStr.setText("");
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
                if (brand_count <= brand_countValue) {
                    String item = "";
                    for (int i = 0; i < mUserItems_Brand.size(); i++) {
                        item = item + listItemsBrand[mUserItems_Brand.get(i)];
                        if (i != mUserItems_Brand.size()) {
                            item = item + ",";
                        }
                    }
                    viewBinding.brandListStr.setText(item);
                }

            }
        });
        mBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        mBuilder.setNeutralButton("Clear All", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                try {
                    for (int i = 0; i < checkedItems_Brand.length; i++) {
                        checkedItems_Brand[i] = false;
                        mUserItems_Brand.clear();
                        brand_count = 0;
                        viewBinding.brandListStr.setText("");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }


            }
        });
        AlertDialog mDialog = mBuilder.create();
        mDialog.show();

    }
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left,
                R.anim.slide_out_right);
    }
    @Override
    public void onDegreeReceived(List<DoctorDegreeViewModel> aList) {
        try {
            //doc_degreeList = dbDoctorHelper.getDegreeListFromSQLite(selectedDoctypeId);
            if (aList != null) {
                listItemsDegree = new String[aList.size()];
                for (int i = 0; i < aList.size(); i++) {
                    listItemsDegree[i] = aList.get(i).getDegreeName();
                }
                checkedItems = new boolean[listItemsDegree.length];

            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }

    }
    @Override
    public void onSpecialityReceived(List<DoctorSpecialityViewModel> aList) {
        try {
            if (aList != null) {
                listItems_Specility = new String[aList.size()];
                for (int i = 0; i < aList.size(); i++) {
                    listItems_Specility[i] = aList.get(i).getSpecialityName();
                }
                checkedItems_Specility = new boolean[listItems_Specility.length];

            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }

    }
    @Override
    public void onDoctorTypeReceived(List<DoctorTypeVM> aList) {
        try {
            if (aList.size()>0) {
                ArrayAdapter<DoctorTypeVM> dataAdapter = new ArrayAdapter<>(DoctorActivity.this, android.R.layout.simple_spinner_item, aList);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                viewBinding.doctorTypeSpinner.setAdapter(dataAdapter);
            }


        } catch (Exception exception) {
            exception.printStackTrace();
        }

    }
    @Override
    public void onInstituteReceived(List<InstitutionVM> aList) {
        try {
            if (aList.size()>0) {
                listItems_ins = new String[aList.size()];
                for (int i = 0; i < aList.size(); i++) {
                    listItems_ins[i] = aList.get(i).getInstitution();
                }
                checkedItems_ins = new boolean[listItems_ins.length];

            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
    @Override
    public void onBrandReceived(List<Brand> aList) {
        try {
            if (aList != null) {
                listItemsBrand = new String[aList.size()];
                for (int i = 0; i < aList.size(); i++) {
                    listItemsBrand[i] = aList.get(i).getProductSQName();
                }
                checkedItems_Brand = new boolean[listItemsBrand.length];

            }
            brandList = new ArrayList<>();
            brandList = dbDoctorHelper.getBrandListFromSQLite();
            if (brandList != null) {
                listItemsBrand = new String[brandList.size()];
                for (int i = 0; i < brandList.size(); i++) {
                    listItemsBrand[i] = brandList.get(i).getProductSQName();
                }
                checkedItems_Brand = new boolean[listItemsBrand.length];
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
    @Override
    public void onDocCategoryReceived(List<DoctorCategory> aList) {
        try {
            if (aList != null) {
                ArrayAdapter<DoctorCategory> dataAdapter = new ArrayAdapter<>(DoctorActivity.this, android.R.layout.simple_spinner_item, aList);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                viewBinding.doctorCategorySpinner.setAdapter(dataAdapter);
            }

        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
    @Override
    public void onChamberReceived(List<DoctorChamberTypeVM> aList) {
        try {
            if (aList != null) {
                ArrayAdapter<DoctorChamberTypeVM> dataAdapter = new ArrayAdapter<>(DoctorActivity.this, R.layout._custom_spinner_tv, aList);
                viewBinding.chamberSpinner.setAdapter(dataAdapter);

                viewBinding.chamberSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        DoctorChamberTypeVM cmm = (DoctorChamberTypeVM) dataAdapter.getItem(position);
                        selectedchembarTypeId = cmm.getChamberTypeId();
                        selectedchembarTypeName = cmm.getChamberTypeName();
                        cmbrtype.setText(selectedchembarTypeName);
                        chamberName.setText("");
                        til_chamberDD.setHint("Chamber Name");
                        is_Chamber=true;
                        popAdd.show();
                    }
                });
            }

        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
    @Override
    public void onContactTypeReceived(List<ContactTypeVM> aList) {
        try {
            if (aList.size()>0) {
                ArrayAdapter<ContactTypeVM> dataAdapter = new ArrayAdapter<>(DoctorActivity.this, R.layout._custom_spinner_tv, aList);
                viewBinding.contactTypeSpinner.setAdapter(dataAdapter);
                viewBinding.contactTypeSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        ContactTypeVM cmm = (ContactTypeVM) dataAdapter.getItem(position);
                        selectedcontactTypeId = cmm.getContactTypeId();
                        selectedcontactTypeName = cmm.getContactType();
                        contacttype.setText(selectedcontactTypeName);
                        contactName.setText("");
                        til_chamberDD.setHint("Contact "+selectedcontactTypeName);
                        if(cmm.getContactType().equals("Mobile"))
                        {
                            contactName.setKeyListener(DigitsKeyListener.getInstance("0123456789"));
                            is_Contact=true;
                            contactName.setFilters(new InputFilter[] { new InputFilter.LengthFilter(11) });
                        }if(cmm.getContactType().equals("Email")) {
                            contactName.setKeyListener(TextKeyListener.getInstance());
                            contactName.setFilters(new InputFilter[] { new InputFilter.LengthFilter(50) });
                            is_Contact=true;
                        }
                        popContact.show();
                    }
                });
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }

    }

  /*  @Override
    public void onProgramTypeReceived(List<ProgramType> aList) {
       *//* try {

            if (aList != null) {
                ArrayAdapter<ProgramType> dataAdapter = new ArrayAdapter<>(DoctorActivity.this, android.R.layout.simple_spinner_item, aList);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                viewBinding.programTypeSpinner.setAdapter(dataAdapter);
            }

        } catch (Exception exception) {
            exception.printStackTrace();
        }*//*

    }*/

    @Override
    public void onProviderTypeReceived(List<ModelProviderType> aList) {
        try {
            if (aList != null) {
                ArrayAdapter<ModelProviderType> dataAdapter = new ArrayAdapter<>(DoctorActivity.this, android.R.layout.simple_spinner_item, aList);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                viewBinding.programTypeSpinner.setAdapter(dataAdapter);
            }

        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
    @Override
    public void onSMCTypeReceived(List<ModelSMCType> aList) {
        try {
            if (aList != null) {
                ArrayAdapter<ModelSMCType> dataAdapter = new ArrayAdapter<>(DoctorActivity.this, android.R.layout.simple_spinner_item, aList);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                viewBinding.smcTypeSpinner.setAdapter(dataAdapter);
            }

        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
    @Override
    public void onSpecialTypeReceived(List<SpecialDay> aList) {
        try {
            if (aList.size()>0) {
                ArrayAdapter<SpecialDay> dataAdapter = new ArrayAdapter<>(DoctorActivity.this, R.layout._custom_spinner_tv, aList);
                viewBinding.spcialdayTypeSpinner.setAdapter(dataAdapter);
                viewBinding.spcialdayTypeSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        SpecialDay cmm = (SpecialDay) dataAdapter.getItem(position);
                        selectedsclialdayId = cmm.getSpecialDayId();
                        selectedsclialdayName = cmm.getSpecialDay();

                        final Calendar cldr = Calendar.getInstance();
                        int day = cldr.get(Calendar.DAY_OF_MONTH);
                        int month = cldr.get(Calendar.MONTH);
                        int year = cldr.get(Calendar.YEAR);
                        picker = new DatePickerDialog(DoctorActivity.this,
                                new DatePickerDialog.OnDateSetListener() {
                                    @Override
                                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                        int monthNumber = monthOfYear + 1;
                                        String docspcialday = Integer.toString(dayOfMonth) + "-" + monthNameArray[monthOfYear] + "-" + year;
                                        SpecialDay isitm = new SpecialDay(selectedsclialdayId, docspcialday,selectedsclialdayName);
                                        specialdayLists.add(isitm);
                                        spdayAdapter = new _specialday_ListAdapter(DoctorActivity.this, specialdayLists);
                                        viewBinding.rvSpecialdaylist.setLayoutManager(new LinearLayoutManager(DoctorActivity.this));
                                        viewBinding.rvSpecialdaylist.setHasFixedSize(true);
                                        viewBinding.rvSpecialdaylist.setAdapter(spdayAdapter);
                                        spdayAdapter.notifyDataSetChanged();
                                    }
                                }, year, month, day);
                        picker.show();


                    }
                });

            }

        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
    @Override
    public void onDoctorDesignationGet(List<DoctorDesignation> aList) {
        try {

            if (aList != null) {
                ArrayAdapter<DoctorDesignation> dataAdapter = new ArrayAdapter<>(DoctorActivity.this, android.R.layout.simple_spinner_item, aList);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                viewBinding.designationSpinner.setAdapter(dataAdapter);
                //viewBinding.designationSpinner.setSelection(2);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
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
    public void onSubmitSuccess(String mesg) {
        if (mesg.equals("Submit")) {
            new androidx.appcompat.app.AlertDialog.Builder(DoctorActivity.this)
                    .setTitle("Success")
                    .setMessage("Doctor Saved Successfully")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            Intent mIntent = getIntent();
                            finish();
                            startActivity(mIntent);

                        }

                    }).setCancelable(false).show();
        }
        if (mesg.equals("Update")) {
            new androidx.appcompat.app.AlertDialog.Builder(DoctorActivity.this)
                    .setTitle("Success")
                    .setMessage("Doctor Update Successfully")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            Intent mIntent = new Intent(DoctorActivity.this, DoctorApprovalListActivity.class);
                            startActivity(mIntent);
                            finish();
                        }
                    }).setCancelable(false).show();
        }
    }
    @Override
    public void onSubmitError(String mesg) {
        new androidx.appcompat.app.AlertDialog.Builder(DoctorActivity.this)
                .setTitle("Error")
                .setMessage(mesg)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }

                }).setCancelable(false).show();

    }
    @Override
    public void vGroup(List<Group> groupList) {
    }
    @Override
    public void vRegion(List<Region> regionList) {
        try {
            if (regionList != null) {
                ArrayAdapter<Region> dataAdapter = new ArrayAdapter<>(DoctorActivity.this, android.R.layout.simple_spinner_item, regionList);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                viewBinding.regionSpinner.setAdapter(dataAdapter);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
    @Override
    public void vArea(List<Area> areaList) {
        try {
            if (areaList != null) {
                ArrayAdapter<Area> dataAdapter = new ArrayAdapter<>(DoctorActivity.this, android.R.layout.simple_spinner_item, areaList);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                viewBinding.areaSpinner.setAdapter(dataAdapter);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
    @Override
    public void vTeritory(List<Teritorry> teritoryList) {
        try {
            if (teritoryList != null) {
                ArrayAdapter<Teritorry> dataAdapter = new ArrayAdapter<>(DoctorActivity.this, android.R.layout.simple_spinner_item, teritoryList);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                viewBinding.territorySpinner.setAdapter(dataAdapter);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
    @Override
    public void vSTeritory(List<SubTeritorry> steritoryList) {
        try {
            if (steritoryList != null) {
                ArrayAdapter<SubTeritorry> dataAdapter = new ArrayAdapter<>(DoctorActivity.this, android.R.layout.simple_spinner_item, steritoryList);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                viewBinding.sterritorySpinner.setAdapter(dataAdapter);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
    @Override
    public void vMarket(List<Market> marketList) {
        try {
            if (marketList != null) {
                ArrayAdapter<Market> dataAdapter = new ArrayAdapter<>(DoctorActivity.this, android.R.layout.simple_spinner_item, marketList);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                viewBinding.marketSpinner.setAdapter(dataAdapter);
            }
           /* viewBinding.marketSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    Market market = (Market) parent.getSelectedItem();
                    selectedMarket = market.getMarketId();

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });*/
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
    private void iniPopup() {
        popAdd = new Dialog(DoctorActivity.this);
        popAdd.setContentView(R.layout.pop_chambername);
        //popAddQty.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popAdd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        popAdd.getWindow().setLayout(Toolbar.LayoutParams.MATCH_PARENT, Toolbar.LayoutParams.WRAP_CONTENT);
        popAdd.getWindow().getAttributes().gravity = Gravity.CENTER;

        // ini popup widgets
        til_chamberDD = popAdd.findViewById(R.id.chamberDD);
        chamberName = popAdd.findViewById(R.id.chamberName);
        cmbrtype = popAdd.findViewById(R.id.cmbrtype);
        Button cmbrBnt = popAdd.findViewById(R.id.cmbrBnt);

        //itemList = new ArrayList<>();
        cmbrBnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!chamberName.getText().toString().isEmpty()) {
                    String chembarName = chamberName.getText().toString();
                    DoctorChamberTypeVM isitm = new DoctorChamberTypeVM(selectedchembarTypeId, selectedchembarTypeName, chembarName);
                    itemList.add(isitm);

                    itemsAdapter = new _chamber_ListAdapter(DoctorActivity.this, itemList);
                    viewBinding.rvChamberlist.setLayoutManager(new LinearLayoutManager(DoctorActivity.this));
                    viewBinding.rvChamberlist.setHasFixedSize(true);
                    viewBinding.rvChamberlist.setAdapter(itemsAdapter);
                    itemsAdapter.notifyDataSetChanged();
                    chamberName.setText("");
                    popAdd.dismiss();
                } else {
                    chamberName.setError("Enter Chamber Name");
                }
            }
        });
    }
    private void iniPopupContact() {
        popContact = new Dialog(DoctorActivity.this);
        popContact.setContentView(R.layout.pop_chambername);
        //popAddQty.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popContact.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        popContact.getWindow().setLayout(Toolbar.LayoutParams.MATCH_PARENT, Toolbar.LayoutParams.WRAP_CONTENT);
        popContact.getWindow().getAttributes().gravity = Gravity.CENTER;

        // ini popup widgets
        til_chamberDD = popContact.findViewById(R.id.chamberDD);
        contactName = popContact.findViewById(R.id.chamberName);
        contacttype = popContact.findViewById(R.id.cmbrtype);
        Button contactBnt = popContact.findViewById(R.id.cmbrBnt);
        contactBnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!contactName.getText().toString().isEmpty()) {
                    String contacName = contactName.getText().toString();
                    DoctorContact isitm = new DoctorContact(selectedcontactTypeId, contacName,selectedcontactTypeName);
                    doccontactLists.add(isitm);

                    contactAdapter = new _contact_ListAdapter(DoctorActivity.this, doccontactLists);
                    viewBinding.rvContactlist.setLayoutManager(new LinearLayoutManager(DoctorActivity.this));
                    viewBinding.rvContactlist.setHasFixedSize(true);
                    viewBinding.rvContactlist.setAdapter(contactAdapter);
                    contactAdapter.notifyDataSetChanged();
                    contactName.setText("");
                    popContact.dismiss();
                } else {
                    contactName.setError("Enter Contact Name");
                }
            }
        });
    }
    private void showGPSDisabledAlertToUser() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("GPS is disabled in your device. Would you like to enable it?")
                .setCancelable(false)
                .setPositiveButton("Goto Settings Page To Enable GPS",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent callGPSSettingIntent = new Intent(
                                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(callGPSSettingIntent);
                            }
                        });
        alertDialogBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        startActivity(new Intent(getApplicationContext(), MainDashboardActivity.class));
                    }
                });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }
    private void setupEdit(String roleType, int empId, DoctorApproveModel cal) {
        iniPopup();
        iniPopupContact();
        viewBinding.pupdateBnt.setVisibility(View.VISIBLE);
        switch (roleType) {
            case "MIO":
                viewBinding.areadiv.setVisibility(View.GONE);
                viewBinding.regiondiv.setVisibility(View.GONE);
                break;
            case "AM":
                viewBinding.areadiv.setVisibility(View.VISIBLE);
                viewBinding.regiondiv.setVisibility(View.GONE);
                break;
            case "DZSM":

                viewBinding.regiondiv.setVisibility(View.VISIBLE);
                viewBinding.areadiv.setVisibility(View.VISIBLE);
                break;

        }

        if (cal.getRegionName() != null) {
            edit_dName = cal.getRegionName();
            viewBinding.regionSpinner.setSelection(getIndex(viewBinding.regionSpinner, edit_dName));
        } else {
        }
        if (cal.getAreaName() != null) {
            edit_dName = cal.getAreaName();
            viewBinding.areaSpinner.setSelection(getIndex(viewBinding.areaSpinner, edit_dName));
        } else {
        }
        if (cal.getTerritoryName() != null) {
            edit_dName = cal.getTerritoryName();
            viewBinding.territorySpinner.setSelection(getIndex(viewBinding.territorySpinner, edit_dName));
        } else {
        }
        if (cal.getSubTerritoryName() != null) {
            edit_dName = cal.getSubTerritoryName();
            viewBinding.sterritorySpinner.setSelection(getIndex(viewBinding.sterritorySpinner, edit_dName));
        } else {
        }
        if (cal.getMarketName() != null) {
            edit_dName = cal.getMarketName();
            viewBinding.marketSpinner.setSelection(getIndex(viewBinding.marketSpinner, edit_dName));
        } else {
        }

        //name
        if (cal.getDoctorName() != null) {
            viewBinding.docName.setText(cal.getDoctorName());
        } else {
            viewBinding.docName.setText("---- ----");
        }
        if (cal.getDoctorAddress() != null) {
            viewBinding.docAddress.setText(cal.getDoctorAddress());
        } else {
            viewBinding.docAddress.setText("---- ----");
        }
        //DoctorType Spinner
        if (cal.getDesignationName() != null) {
            edit_dName = cal.getDesignationName();
            viewBinding.designationSpinner.setSelection(getIndex(viewBinding.designationSpinner, edit_dName));
        } else {
            //presenter.GetDoctorType(0);
        }
        //DoctorType Spinner
        if (cal.getDoctorTypeStr() != null) {
            edit_dName = cal.getDoctorTypeStr();
            viewBinding.doctorTypeSpinner.setSelection(getIndex(viewBinding.doctorTypeSpinner, edit_dName));
        } else {
            //presenter.GetDoctorType(0);
        }
        viewBinding.doctorTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                DoctorTypeVM doctype = (DoctorTypeVM) parent.getSelectedItem();
                selectedDoctypeId = doctype.getDoctorTypeId();
                mUserItems.clear();
                //viewBinding.degreeListStr.setText("");
                presenter.GetDegree(selectedDoctypeId);
                //viewBinding.degreeListStr.setText(item);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //degree
        if (cal.getDegreeStr() != null) {
            viewBinding.degreeListStr.setText(cal.getDegreeStr());
        } else {
            viewBinding.degreeListStr.setText("---- ----");
        }
        //speciality
        if (cal.getSpecialityStr() != null) {
            viewBinding.specilityListStr.setText(cal.getSpecialityStr());
        } else {
            viewBinding.specilityListStr.setText("---- ----");
        }
        //Inistitute
        if (cal.getInstitutionSTr() != null) {
            viewBinding.instituteListStr.setText(cal.getInstitutionSTr());

        } else {
            viewBinding.instituteListStr.setText("----");
        }
        //Brand
        if (cal.getBrandStr() != null) {
            viewBinding.brandListStr.setText(cal.getBrandStr());
        } else {
            viewBinding.brandListStr.setText("----");
        }
        //Doc Category Spinner
        if (cal.getCategoryName() != null) {
            edit_dName = cal.getCategoryName();
            viewBinding.doctorCategorySpinner.setSelection(getIndex(viewBinding.doctorCategorySpinner, edit_dName));
        } else {
            // presenter.GetDoctorCategory(0);
        }

        //Chamber
        if (cal.getaChemberListDAO() != null) {
            List<ChembarTypeWithName> clist = cal.getaChemberListDAO();
            int c;
            for (c = 0; c < cal.getaChemberListDAO().size(); c++) {
                String chembarName = clist.get(c).getChamberName();
                String chembarTypeName = clist.get(c).getChamberTypeName();
                int cmbrtypeid = clist.get(c).getChamberTypeId();
                DoctorChamberTypeVM isitm = new DoctorChamberTypeVM(cmbrtypeid, chembarTypeName, chembarName);
                itemList.add(isitm);
            }
            itemsAdapter = new _chamber_ListAdapter(DoctorActivity.this, itemList);
            viewBinding.rvChamberlist.setLayoutManager(new LinearLayoutManager(DoctorActivity.this));
            viewBinding.rvChamberlist.setHasFixedSize(true);
            viewBinding.rvChamberlist.setAdapter(itemsAdapter);
            itemsAdapter.notifyDataSetChanged();


        } else {
        }
        //Doc Contact Type Spinner
        //TODO:Contact Type
        //Contact
        if (cal.getaDoctorContactDAO() != null) {
            List<DoctorContact> clist = cal.getaDoctorContactDAO();
           /* int c;
            for (c = 0; c < cal.getaDoctorContactDAO().size(); c++) {
                String contactName = clist.get(c).getContact();
                String cntctTypeName = clist.get(c).getContactType();
                int cntcttypeid = clist.get(c).getContactTypeId();
                DoctorChamberTypeVM isitm = new DoctorChamberTypeVM(cmbrtypeid, chembarTypeName, chembarName);
                itemList.add(isitm);
            }*/
            contactAdapter = new _contact_ListAdapter(DoctorActivity.this, clist);
            viewBinding.rvContactlist.setLayoutManager(new LinearLayoutManager(DoctorActivity.this));
            viewBinding.rvContactlist.setHasFixedSize(true);
            viewBinding.rvContactlist.setAdapter(contactAdapter);
            contactAdapter.notifyDataSetChanged();

        } else {
        }

      /*  if (cal.getContactType() != null) {
            edit_dName = cal.getContactType();
            viewBinding.contactTypeSpinner.setSelection(getIndex(viewBinding.contactTypeSpinner, edit_dName));
        } else {
            presenter.GetContactType(0);
        }*/
        //TODO:Contact text
        /*if (cal.getContact() != null) {
            viewBinding.contactTxt.setText(cal.getContact());
        } else {
            viewBinding.contactTxt.setText("----");
        }*/
        //ProgramType Spinner
        if (cal.getProgramTypeName() != null) {
            edit_dName = cal.getProgramTypeName();
            viewBinding.programTypeSpinner.setSelection(getIndex(viewBinding.programTypeSpinner, edit_dName));

        } else {
            // presenter.GetProgramType(0);
        }

        //ProgramType Spinner
        if (cal.getSMCTypeName() != null) {
            edit_dName = cal.getSMCTypeName();
            viewBinding.smcTypeSpinner.setSelection(getIndex(viewBinding.smcTypeSpinner, edit_dName));

        } else {
            // presenter.GetProgramType(0);
        }
        //Special Day Spinner
      /*  if (cal.getSpecialDay() != null) {
            edit_dName = cal.getSpecialDay();
            viewBinding.spcialdayTypeSpinner.setSelection(getIndex(viewBinding.spcialdayTypeSpinner, edit_dName));
        } else {
            // presenter.GetSpecialType(0);
        }*/
        //TODO:Special Day
        //Special date
        if (cal.getaDoctorSpecialDAO() != null) {
            List<SpecialDay> clist = cal.getaDoctorSpecialDAO();
            spdayAdapter = new _specialday_ListAdapter(DoctorActivity.this, clist);
            viewBinding.rvSpecialdaylist.setLayoutManager(new LinearLayoutManager(DoctorActivity.this));
            viewBinding.rvSpecialdaylist.setHasFixedSize(true);
            viewBinding.rvSpecialdaylist.setAdapter(spdayAdapter);
            spdayAdapter.notifyDataSetChanged();

        } else {
        }

        if (cal.getSpeciaDateStr() != null) {
            viewBinding.scecialdateTxt.setText(cal.getSpeciaDateStr());
        } else {
            viewBinding.scecialdateTxt.setText("----");
        }

        viewBinding.pupdateBnt.setOnClickListener(v -> {
            try {
                String docName = viewBinding.docName.getText().toString();
                String docDegree = viewBinding.degreeListStr.getText().toString();
                String docSpeciality = viewBinding.specilityListStr.getText().toString();
                String institute = viewBinding.instituteListStr.getText().toString();
                String docBrand = viewBinding.brandListStr.getText().toString();
                String docAddress = viewBinding.docAddress.getText().toString();


                DoctorTypeVM doctorType = (DoctorTypeVM) viewBinding.doctorTypeSpinner.getSelectedItem();
                int dtypeid = doctorType.getDoctorTypeId();

                //TODO:Contact
                /*ContactTypeVM contactType = (ContactTypeVM) viewBinding.contactTypeSpinner.getSelectedItem();
                int contactId = contactType.getContactTypeId();
                String contactDD = viewBinding.contactTxt.getText().toString();*/

                DoctorCategory catType = (DoctorCategory) viewBinding.doctorCategorySpinner.getSelectedItem();
                int catTypeId = catType.getCategoryId();

               // ProgramType progType = (ProgramType) viewBinding.programTypeSpinner.getSelectedItem();
                ModelProviderType progType = (ModelProviderType) viewBinding.programTypeSpinner.getSelectedItem();
                int programTypeId = progType.getProviderTypeId();

                ModelSMCType smcType = (ModelSMCType) viewBinding.smcTypeSpinner.getSelectedItem();
                int smcTypeId = smcType.getSMCTypeId();
             /*   SpecialDay spday = (SpecialDay) viewBinding.spcialdayTypeSpinner.getSelectedItem();
                int spdayId = spday.getSpecialDayId();*/


                DoctorDesignation docDesi = (DoctorDesignation) viewBinding.designationSpinner.getSelectedItem();
                int designationId = docDesi.getDesignationId();

                chembarLists = new ArrayList<>();
                for (int i = 0; i < itemList.size(); i++) {
                    String chamberName = itemList.get(i).getChamberName();
                    int chamberTypeId = itemList.get(i).getChamberTypeId();
                    ChembarList cl = new ChembarList();
                    cl.setChamberTypeId(chamberTypeId);
                    cl.setName(chamberName);
                    chembarLists.add(cl);
                }

                DoctorSM doctorSM = new DoctorSM();
                doctorSM.setDoctorId(cal.getDoctorId());
                doctorSM.setDoctorName(docName);
                doctorSM.setDoctorAddress(docAddress);
                doctorSM.setDesignationId(designationId);
                doctorSM.setDegreeStr(docDegree);

                doctorSM.setSpecialityStr(docSpeciality);
                doctorSM.setInstitutionSTr(institute);
                doctorSM.setBrandStr(docBrand);

                doctorSM.setDoctorTypeId(dtypeid);
                doctorSM.setDoctorTypeStr(String.valueOf(dtypeid));
                doctorSM.setDoctorCategoryId(catTypeId);
                doctorSM.setaChemberListDAO(chembarLists);
                doctorSM.setaDoctorContactDAO(doccontactLists);
                doctorSM.setaDoctorSpecialDAO(specialdayLists);
               // doctorSM.setContactTypeId(contactId);
                //doctorSM.setContact(contactDD);
                doctorSM.setProgramTypeId(programTypeId);
                doctorSM.setSMCTypeId(smcTypeId);
                //doctorSM.setSpecialDayId(spdayId);
                doctorSM.setSpeciaDateStr(viewBinding.scecialdateTxt.getText().toString());

                Area area = (Area) viewBinding.areaSpinner.getSelectedItem();
                int aeraId = area.getAreaId();
                Teritorry teritorry = (Teritorry) viewBinding.territorySpinner.getSelectedItem();
                int teritoryId = teritorry.getTerritoryId();
                SubTeritorry steri = (SubTeritorry) viewBinding.sterritorySpinner.getSelectedItem();
                int steriId = steri.getSubTerritoryId();
                Market market = (Market) viewBinding.marketSpinner.getSelectedItem();
                int marketId = market.getMarketId();

                doctorSM.setAreaId(aeraId);
                doctorSM.setTerritoryId(teritoryId);
                doctorSM.setSubTerritoryId(steriId);
                doctorSM.setMarketId(marketId);
                doctorSM.setSessionUser(empId);
                // doctorSM.setRemarks(viewBinding.remarksTxt.getText().toString());

                Gson gs = new Gson();
                String ss = gs.toJson(doctorSM);
                System.out.println("post : " + ss);
                // Toast.makeText(this, "data "+ss, Toast.LENGTH_SHORT).show();
                presenter.SaveDoctor(doctorSM, "Update");
            } catch (Exception ex) {
                Log.e("doctorSubmit-Ex", "SubmitClick: ", ex);
            }
        });
    }

    private InputFilter filter = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

            if (source != null && blockCharacterSet.contains(("" + source))) {
                return "";
            }
            return null;
        }
    };
}