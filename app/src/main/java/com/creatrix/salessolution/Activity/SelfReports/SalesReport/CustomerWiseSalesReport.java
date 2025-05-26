package com.creatrix.salessolution.Activity.SelfReports.SalesReport;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.creatrix.salessolution.Activity.Customer.Approval.CustomerApprovalListActivity;
import com.creatrix.salessolution.Activity.Customer.CustomerListActivity;
import com.creatrix.salessolution.Activity.Customer.CustomerPresenter;
import com.creatrix.salessolution.Activity.Customer.ICustomerAdd;
import com.creatrix.salessolution.Activity.Expense.Report.ExpenseClaimReportsActivity;
import com.creatrix.salessolution.Activity.OrderProcess.Adapter.SalesReportCustAdapter;
import com.creatrix.salessolution.Activity.SelfReports.ExpenseSummery.ExpenseSummeryActivity;
import com.creatrix.salessolution.Activity.SelfReports.SalesReport.Model.CWSData;
import com.creatrix.salessolution.Activity.SelfReports.SalesReport.Model.CWSaleReportModel;
import com.creatrix.salessolution.Activity.SelfReports.SalesReport.Model.PWSaleReportModel;
import com.creatrix.salessolution.DBAdapter.DBCrudHelper;
import com.creatrix.salessolution.Interface.ICustomer;
import com.creatrix.salessolution.Interface.IMarketStracture;
import com.creatrix.salessolution.Model.Customer;
import com.creatrix.salessolution.Model.CustomerType;
import com.creatrix.salessolution.Model.Doctor.ProgramType;
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
import com.creatrix.salessolution.Model.StationType;
import com.creatrix.salessolution.Network.OrderProcessAPICALL;
import com.creatrix.salessolution.Network.RetrofitClientInstance;
import com.creatrix.salessolution.Presenter.MarketStructurePresenter;
import com.creatrix.salessolution.R;
import com.creatrix.salessolution.Services.Constants;
import com.creatrix.salessolution.UtilityHelper.NetworkInformation;
import com.creatrix.salessolution.UtilityHelper.SessionManagement;
import com.creatrix.salessolution.UtilityHelper.SnackBarManagement;
import com.creatrix.salessolution.UtilityHelper.UtilityHelper;
import com.creatrix.salessolution.databinding.FilterMasterBinding;
import com.creatrix.salessolution.databinding.FragmentCustomerWiseSalesReportBinding;
import com.github.dewinjm.monthyearpicker.MonthYearPickerDialogFragment;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomerWiseSalesReport extends Fragment implements IMarketStracture.View, ICustomerAdd.View {
    CustomerPresenter presenterCutstomer;
    FragmentCustomerWiseSalesReportBinding binding;
    SalesReportCustAdapter srAdapter;
    BottomSheetDialog bsheetdlg;
    ProgressDialog progressDoalog;
    IMarketStracture.Presenter mkpresenter;
    FilterMasterBinding ftm;
    String role, empId;
    int selectedGrpId, selectedZoneId, selectedAreaId, selectedTeriId, selectedSTeriId, selectedMarketId;
    String selectedProviderTypeId="",selectedCustTypeID="";
    int selectedMonth, selectedyear;
    SessionManagement session;
    List<CustomerType> ctypeList;
    List<ModelProviderType> providertypeList;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CustomerWiseSalesReport() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static CustomerWiseSalesReport newInstance(String param1, String param2) {
        CustomerWiseSalesReport fragment = new CustomerWiseSalesReport();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    String selectedTyp="";
    List<MIO> mioList;
    DBCrudHelper dbCrudHelper;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCustomerWiseSalesReportBinding.inflate(getLayoutInflater());
        ftm = FilterMasterBinding.inflate(getLayoutInflater());
        mkpresenter = new MarketStructurePresenter(this, getActivity());
        dbCrudHelper = new DBCrudHelper(getActivity());
        session = new SessionManagement(getActivity());
        presenterCutstomer = new CustomerPresenter(this, getActivity());
       // Toast.makeText(requireActivity(), "who : "+ Constants.From, Toast.LENGTH_SHORT).show();
        //return inflater.inflate(R.layout.fragment_customer_wise_sales_report, container, false);
        HashMap<String, String> user = session.getUserDetails();
        empId = user.get(SessionManagement.KEY_EmpId);
        role = user.get(SessionManagement.KEY_EmpRoleType);
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);

        presenterCutstomer.GetCustomerType();
        presenterCutstomer.GetProviderType();

        HashMap<String, String> filter = new HashMap<>();
        switch (Constants.From)
        {
            case "Sales":
                filter.put("type", "Sales");
                break;
            case "Order":
                filter.put("type", "Order");
                break;
        }
        filter.put("Role", role);
        filter.put("empId", empId);
        filter.put("Month", String.valueOf(month + 1));
        filter.put("Year", String.valueOf(year));
        filter.put("ProviderType", "");
        filter.put("CustomerType", "");

        LoadData(filter);
        openFilter();
        binding.cfilter.setOnClickListener(v -> {
            bsheetdlg.show();
        });
        return binding.getRoot();
    }

    private void openFilter() {
    /*    FilterMasterBinding ftm;
        ftm = FilterMasterBinding.inflate(getLayoutInflater());*/
        bsheetdlg = new BottomSheetDialog(getActivity());
        bsheetdlg.setContentView(ftm.getRoot());
        bsheetdlg.setCanceledOnTouchOutside(true);
        switch (role) {
            case "MIO":
                ftm.linearLayoutemp.setVisibility(GONE);
                break;
            case "AM":
            case "DZSM":
            case "NSM":
                ftm.linearLayoutemp.setVisibility(VISIBLE);
                break;
        }
        ftm.filterStatusLayout.getRoot().setVisibility(GONE);
        ftm.filterTypeLayout.tilDoctype.setVisibility(View.GONE);
        ftm.filterTypeLayout.tilProgtypetype.setVisibility(VISIBLE);
        ftm.filterTypeLayout.tilCusttype.setVisibility(VISIBLE);
        ftm.filterTypeLayout.tilPharmatype.setVisibility(View.GONE);
        ftm.filterMonthYearLayout.getRoot().setVisibility(GONE);


        CustomerType r = new CustomerType();
        r.setCustomerType("Select");
        ctypeList.add(0, r);
        ArrayAdapter<CustomerType> customerTypeAdapter = new ArrayAdapter<>(requireActivity(), R.layout._custom_spinner_tv, ctypeList);
        ftm.filterTypeLayout.spinnerCusttype.setAdapter(customerTypeAdapter);
        ftm.filterTypeLayout.spinnerCusttype.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (position == 0) {
                    selectedCustTypeID = "";
                } else {
                    CustomerType cmm = (CustomerType) customerTypeAdapter.getItem(position);
                    selectedCustTypeID = String.valueOf(cmm.getCustomerTypeId());
                }

            }
        });

        ModelProviderType ptype = new ModelProviderType();
        ptype.setProviderType("Select");
        providertypeList.add(0, ptype);
        ArrayAdapter<ModelProviderType> providerAdapter = new ArrayAdapter<>(requireActivity(), R.layout._custom_spinner_tv, providertypeList);
        ftm.filterTypeLayout.spinnerProgramtype.setAdapter(providerAdapter);
        ftm.filterTypeLayout.spinnerProgramtype.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (position == 0) {
                    selectedProviderTypeId = "";
                } else {
                    ModelProviderType cmm = (ModelProviderType) providerAdapter.getItem(position);
                    selectedProviderTypeId = String.valueOf(cmm.getProviderTypeId());
                }
            }
        });


        List<String> emptype=new ArrayList<>();
        switch (role)
        {
            case "AM":
                emptype.add("Select");
                emptype.add("MIO");
                try {
                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, emptype);
                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    ftm.spinnerEmployeeType.setAdapter(dataAdapter);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
                break;
            case "DZSM":
                emptype.add("Select");
                emptype.add("MIO");
                emptype.add("AM");
                ArrayAdapter<String> dataAdapterrsm = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, emptype);
                dataAdapterrsm.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                ftm.spinnerEmployeeType.setAdapter(dataAdapterrsm);
                break;
            case "NSM":
                emptype.add("Select");
                emptype.add("MIO");
                emptype.add("AM");
                emptype.add("DZSM");
                ArrayAdapter<String> dataAdapternsm = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, emptype);
                dataAdapternsm.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                ftm.spinnerEmployeeType.setAdapter(dataAdapternsm);
                break;
        }
        ftm.spinnerEmployeeType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedTyp= String.valueOf(ftm.spinnerEmployeeType.getSelectedItem());
                switch (selectedTyp)
                {
                    case "Select":
                        ftm.miolay.setVisibility(GONE);
                        ftm.asmlay.setVisibility(View.GONE);
                        ftm.rsmlay.setVisibility(View.GONE);
                        break;
                    case "MIO":
                        try {
                            ftm.miolay.setVisibility(View.VISIBLE);
                            ftm.asmlay.setVisibility(View.GONE);
                            ftm.rsmlay.setVisibility(View.GONE);
                            try {
                                mioList = dbCrudHelper.getMIOList_SQLite();
                            } catch (Exception exception) {
                                exception.printStackTrace();
                            }
                            if (mioList != null) {
                                System.out.println("mlist "+mioList);
                                ArrayAdapter<MIO> dataAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, mioList);
                                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                ftm.spinnerApprovalMio.setAdapter(dataAdapter);
                            }
                            else {
                                SnackBarManagement._warning_CustomMessage(binding.getRoot(),"No MIO Found!!");
                            }
                        } catch (Exception exception) {
                            exception.printStackTrace();
                        }
                        break;
                    case "AM":
                        try {
                            ftm.asmlay.setVisibility(View.VISIBLE);
                            ftm.miolay.setVisibility(View.GONE);
                            ftm.rsmlay.setVisibility(View.GONE);
                            List<ASM> asmList = null;
                            try {
                                asmList = dbCrudHelper.getASMList_SQLite();
                            } catch (Exception exception) {
                                exception.printStackTrace();
                            }
                            if (asmList != null) {
                                ArrayAdapter<ASM> dataAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, asmList);
                                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                ftm.spinnerApprovalAsm.setAdapter(dataAdapter);

                            }  else {
                                SnackBarManagement._warning_CustomMessage(binding.getRoot(),"No AM Founded!!");
                            }
                        } catch (Exception exception) {
                            exception.printStackTrace();
                        }
                        break;
                    case "DZSM":
                        try {
                            ftm.rsmlay.setVisibility(View.VISIBLE);
                            ftm.miolay.setVisibility(View.GONE);
                            ftm.asmlay.setVisibility(View.GONE);
                            List<RSM> rsmList = null;
                            try {
                                rsmList = dbCrudHelper.getRSMList_SQLite();
                            } catch (Exception exception) {
                                exception.printStackTrace();
                            }
                            if (rsmList != null) {
                                ArrayAdapter<RSM> dataAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, rsmList);
                                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                ftm.spinnerApprovalRsm.setAdapter(dataAdapter);
                            }
                            else {
                                SnackBarManagement._warning_CustomMessage(binding.getRoot(),"No AM Founded!!");
                            }
                        } catch (Exception exception) {
                            exception.printStackTrace();
                        }
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        ftm.filterMonthYearLayout.ivDatePickerFromDate.setVisibility(GONE);
        ftm.filterMonthYearLayout.ivDatePickerToDate.setVisibility(GONE);

        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        selectedyear=year;
        int month = c.get(Calendar.MONTH);
        selectedMonth=month;
        ftm.llmonthyear.setVisibility(VISIBLE);
        ftm.ivmypicker.setOnClickListener(v -> MonthPicker());
        ftm.montyer.setText(UtilityHelper.monthNameArrayFull[month] + ',' + year);
        ftm.filterTopLayout.btnDonefilter.setOnClickListener(v -> {
            try {
                switch (selectedTyp)
                {
                    case "MIO":
                        MIO mioempid;
                        mioempid= (MIO) ftm.spinnerApprovalMio.getSelectedItem();
                        empId=String.valueOf(mioempid.getMIOEmpId());
                        break;
                    case "AM":
                        ASM amempid=(ASM) ftm.spinnerApprovalAsm.getSelectedItem();
                        empId=String.valueOf(amempid.getASMEmpId());
                        break;
                    case "DZSM":
                        RSM dzsmempid=(RSM) ftm.spinnerApprovalRsm.getSelectedItem();
                        empId=String.valueOf(dzsmempid.getRSMEmpId());
                        break;
                }

                HashMap<String, String> filters = new HashMap<>();
                filters.put("Role", role);
                filters.put("empId", empId);
                filters.put("Month", String.valueOf(selectedMonth+1));
                filters.put("Year", String.valueOf(selectedyear));
                filters.put("ProviderType", selectedProviderTypeId);
                filters.put("CustomerType",selectedCustTypeID);
                switch (Constants.From)
                {
                    case "Sales":
                        filters.put("type", "Sales");
                        break;
                    case "Order":
                        filters.put("type", "Order");
                        break;
                }

                //filters.put("type", "Sales");
                /*   filters.put("GroupId", String.valueOf(selectedGrpId));
                filters.put("RegionId", String.valueOf(selectedZoneId));
                filters.put("AreaId", String.valueOf(selectedAreaId));
                filters.put("TerritoryId", String.valueOf(selectedTeriId));
                filters.put("SubTerritoryId", String.valueOf(selectedSTeriId));
                filters.put("Market", String.valueOf(selectedMarketId));*/
                System.out.println(filters);
                LoadData(filters);

            } catch (Exception exception) {
                //exception.printStackTrace();
            }
            bsheetdlg.cancel();
        });
        ftm.filterTopLayout.ivCancel.setOnClickListener(v -> {
            bsheetdlg.cancel();
        });
    }

    public void MonthPicker() {
        int yearSelected;
        int monthSelected;
        Calendar calendar = Calendar.getInstance();
        yearSelected = calendar.get(Calendar.YEAR);
        monthSelected = calendar.get(Calendar.MONTH);
        MonthYearPickerDialogFragment dialogFragment = MonthYearPickerDialogFragment
                .getInstance(monthSelected, yearSelected);
        dialogFragment.show(getActivity().getSupportFragmentManager(), null);
        dialogFragment.setOnDateSetListener((year, monthOfYear) -> {
            ftm.montyer.setText("");
            ftm.montyer.setText(UtilityHelper.monthNameArrayFull[monthOfYear] + ',' + year);
            selectedMonth = monthOfYear;
            selectedyear = year;
        });
    }
    private void LoadData(HashMap<String, String> filter) {
        if (NetworkInformation.isConnected(getContext())) {
            try {
                progressDoalog = new ProgressDialog(getActivity());
                progressDoalog.setMessage("Report Loading...");
                progressDoalog.show();
                progressDoalog.setCanceledOnTouchOutside(false);

                OrderProcessAPICALL service = RetrofitClientInstance.getRetrofitInstance().create(OrderProcessAPICALL.class);
                Call<CWSaleReportModel> call = service.GetCustWiseOrderReport(filter);
                call.enqueue(new Callback<CWSaleReportModel>() {
                    @Override
                    public void onResponse(@NonNull Call<CWSaleReportModel> call, @NonNull Response<CWSaleReportModel> response) {
                        if(progressDoalog!=null || progressDoalog.isShowing())
                        {
                            progressDoalog.dismiss();
                        }
                        CWSaleReportModel rp = response.body();
                        assert rp != null;
                        if(rp.getCwsDataList().size()>0)
                            {
                                LoadRV(rp);
                            }else {
                                SnackBarManagement._warning_CustomMessage(binding.getRoot(), "Report Data Not Found!!");
                               LoadRV(rp);
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<CWSaleReportModel> call, @NonNull Throwable t) {
                        if(progressDoalog!=null || progressDoalog.isShowing())
                        {
                            progressDoalog.dismiss();
                        }

                        if (t instanceof SocketTimeoutException) {
                            SnackBarManagement._warning_CustomMessage(binding.getRoot(), "Slow Connection Detected. Please try again");
                        }
                    }
                });

            } catch (Exception ex) {
                if(progressDoalog!=null || progressDoalog.isShowing())
                {
                    progressDoalog.dismiss();
                }
            }
        } else {
            SnackBarManagement._error_CustomMessage(binding.getRoot(), "No Internet");
        }
    }
    private void LoadRV(CWSaleReportModel rp) {
        binding.totalAmount.setText(rp.getTotal_amount());
        binding.totalQty.setText(rp.getTotal_qty());

        srAdapter = new SalesReportCustAdapter(rp.getCwsDataList(), getContext());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        binding.recyclerView.setLayoutManager(mLayoutManager);
        binding.recyclerView.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerView.setAdapter(srAdapter);
        binding.recyclerView.setItemAnimator(null);
        binding.recyclerView.scrollToPosition(0);
        srAdapter.notifyDataSetChanged();

    }
    @Override
    public void vGroup(List<Group> groupList) {
        try {
            if (groupList != null) {
                Group g = new Group();
                g.setGroupName("Select");
                groupList.add(0, g);

                ArrayAdapter<Group> dataAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, groupList);
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

                ArrayAdapter<Region> dataAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, regionList);
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
                ArrayAdapter<Area> dataAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, areaList);
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

                ArrayAdapter<Teritorry> dataAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, teritoryList);
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

                ArrayAdapter<SubTeritorry> dataAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, steritoryList);
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

                ArrayAdapter<Market> dataAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, marketList);
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
    public void onProviderType(List<ModelProviderType> ptype) {
        if (ptype.size() > 0) {
            providertypeList = ptype;
        }
    }

    @Override
    public void onSMCType(List<ModelSMCType> ptype) {

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