package com.creatrix.salessolution.Activity.Customer.Approval;

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

import com.creatrix.salessolution.Activity.Customer.Approval.Model.CustomerApprovalList;
import com.creatrix.salessolution.Activity.Customer.Approval.Model.CustomerApprovalPresenter;
import com.creatrix.salessolution.Activity.Customer.Approval.Model.CustomerApprovalRQ;
import com.creatrix.salessolution.Activity.Customer.Approval.Model.ICustomerApproval;
import com.creatrix.salessolution.Activity.Customer.CustomerListActivity;
import com.creatrix.salessolution.Activity.Customer.CustomerPresenter;
import com.creatrix.salessolution.Activity.Customer.ICustomerAdd;
import com.creatrix.salessolution.DBAdapter.DBCrudHelper;
import com.creatrix.salessolution.DBAdapter.DBDoctor.DBDoctorHelper;
import com.creatrix.salessolution.Interface.ICustomer;
import com.creatrix.salessolution.Model.CustomerType;
import com.creatrix.salessolution.Model.Doctor.ProgramType;
import com.creatrix.salessolution.Model.MarketStructure.StructureTable.Teritorry;
import com.creatrix.salessolution.Model.ModelProviderType;
import com.creatrix.salessolution.Model.ModelSMCType;
import com.creatrix.salessolution.Model.StationType;
import com.creatrix.salessolution.R;
import com.creatrix.salessolution.Services.Constants;
import com.creatrix.salessolution.UtilityHelper.SessionManagement;
import com.creatrix.salessolution.UtilityHelper.SnackBarManagement;
import com.creatrix.salessolution.UtilityHelper.UtilityHelper;
import com.creatrix.salessolution.databinding.ActivityCustomerApprovalListBinding;
import com.creatrix.salessolution.databinding.FilterMasterBinding;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;

import java.security.Provider;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class CustomerApprovalListActivity extends AppCompatActivity implements ICustomerApproval.View, ICustomerAdd.View,customerTeamApprovalAdapter.approvalKlick {
    ActivityCustomerApprovalListBinding binding;
    customerTeamApprovalAdapter adapter;
    CustomerApprovalPresenter presenter;
    CustomerPresenter presenterCutstomer;
    SessionManagement session;
    DBCrudHelper dbCrudHelper;
    HashMap<String, String> userInfo = new HashMap<>();

    String fromdat, todate, RoleType, params, tagA, tagR, tagN, Areaid, Regionid, Groupid;
    int RoleTypeId, empid, pending;
    BottomSheetDialog bsheetdlg;
    DBDoctorHelper dbDoctorHelper;
    FilterMasterBinding ftm;
    ProgressDialog pd;

    List<CustomerType> ctypeList;
    List<ModelProviderType> providertypeList;
    List<ModelSMCType> pharmatypeList;
    String selectedcustomertypeId = "0", selectedprogramId = "0", selectedpharmatypeId = "0", selectedteriId = "0";

    private int next = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCustomerApprovalListBinding.inflate(getLayoutInflater());
        //setContentView(R.layout.activity_customer_approval_list);
        setContentView(binding.getRoot());
        ftm = FilterMasterBinding.inflate(getLayoutInflater());
        binding.toolbarCustom.setOnClickListener(v -> finish());
        pd = new ProgressDialog(CustomerApprovalListActivity.this);

        session = new SessionManagement(getApplicationContext());
        presenter = new CustomerApprovalPresenter(this, this);
        presenterCutstomer = new CustomerPresenter(this, this);

        userInfo = session.getUserDetails();
        empid = Integer.parseInt(userInfo.get(SessionManagement.KEY_EmpId));
        RoleTypeId = Integer.parseInt(userInfo.get(SessionManagement.KEY_EmpRoleTypeId));
        RoleType = userInfo.get(SessionManagement.KEY_EmpRoleType);
        dbDoctorHelper = new DBDoctorHelper(CustomerApprovalListActivity.this);

        presenterCutstomer.GetCustomerType();
        presenterCutstomer.GetSMCType();
        presenterCutstomer.GetProviderType();

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
                    Regionid = String.valueOf(dbCrudHelper.getCurrentUserRegionId_SQLite());
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

            Map<String, String> filter = new HashMap<>();
            filter.put("Role", RoleType);
            filter.put("AppStatus", "");
            filter.put("FromDt", "");
            filter.put("ToDt", "");
            filter.put("TerritoryId", "");
            filter.put("EmpId", "");

            filter.put("CustomerTypeId", selectedcustomertypeId);
            filter.put("ProgramTypeId", selectedprogramId);
            filter.put("SMCTypeId", selectedpharmatypeId);
            Constants.filtermap=filter;
            Constants.filterparams=params;
            hitApi(params, filter);

            openFilter();
            binding.atteFilter.setOnClickListener(v -> bsheetdlg.show());
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        binding.swipteamcustomer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Map<String, String> filters = new HashMap<>();
                filters.put("Role", RoleType);
                filters.put("AppStatus", "");
                filters.put("FromDt", "");
                filters.put("ToDt", "");
                filters.put("TerritoryId", "");
                filters.put("EmpId", "");
//
                filters.put("CustomerTypeId", selectedcustomertypeId);
                filters.put("ProgramTypeId", selectedprogramId);
                filters.put("SMCTypeId", selectedpharmatypeId);
                hitApi(params, filters);
                // presenter.GetCustomerApprovalList(params, filters, binding.master);
                binding.swipteamcustomer.setRefreshing(false);
            }
        });
    }

    private void hitApi(String params, Map<String, String> filter) {
        pd.setMessage("Customer List Loading...");
        pd.show();
        pd.setCanceledOnTouchOutside(false);
        presenter.GetCustomerApprovalList(params, filter, binding.master);
    }
    private void openFilter() {
        bsheetdlg = new BottomSheetDialog(CustomerApprovalListActivity.this);
        bsheetdlg.setContentView(ftm.getRoot());
        //bsheetdlg.setContentView(R.layout.filter_master);
        bsheetdlg.setCanceledOnTouchOutside(true);
        ftm.linearLayoutemp.setVisibility(View.GONE);
        ftm.filterStatusLayout.linearLayout.setVisibility(View.GONE);
        ftm.filterTypeLayout.tilDoctype.setVisibility(View.GONE);
        ftm.filterTypeLayout.tilProgtypetype.setVisibility(View.GONE);
        ftm.filterTypeLayout.spinnerDoctortype.setVisibility(View.GONE);
        ftm.filterTypeLayout.tilTeritorryid.setVisibility(View.VISIBLE);

        CustomerType r = new CustomerType();
        r.setCustomerType("Select");
        ctypeList.add(0, r);
        ArrayAdapter<CustomerType> customerTypeAdapter = new ArrayAdapter<>(CustomerApprovalListActivity.this, R.layout._custom_spinner_tv, ctypeList);
        ftm.filterTypeLayout.spinnerCusttype.setAdapter(customerTypeAdapter);
        ftm.filterTypeLayout.spinnerCusttype.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (position == 0) {
                    selectedcustomertypeId = "0";
                } else {
                    CustomerType cmm = (CustomerType) customerTypeAdapter.getItem(position);
                    selectedcustomertypeId = String.valueOf(cmm.getCustomerTypeId());
                }

            }
        });

        ModelProviderType ptype = new ModelProviderType();
        ptype.setProviderType("Select");
        providertypeList.add(0, ptype);
        ArrayAdapter<ModelProviderType> providerAdapter = new ArrayAdapter<>(CustomerApprovalListActivity.this, R.layout._custom_spinner_tv, providertypeList);
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

        ArrayAdapter<ModelSMCType> pharmaAdapter = new ArrayAdapter<>(CustomerApprovalListActivity.this, R.layout._custom_spinner_tv, pharmatypeList);
        ftm.filterTypeLayout.spinnerPharmatype.setAdapter(pharmaAdapter);
        ftm.filterTypeLayout.spinnerPharmatype.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                ModelSMCType cmm = (ModelSMCType) pharmaAdapter.getItem(position);
                selectedpharmatypeId = String.valueOf(cmm.getSMCTypeId());
            }
        });

        ArrayAdapter<Teritorry> teritoryAdapter = new ArrayAdapter<>(CustomerApprovalListActivity.this, R.layout._custom_spinner_tv, dbCrudHelper.getTerritoryByIdList_SQLite(0));
        ftm.filterTypeLayout.spinnerTeritorryid.setAdapter(teritoryAdapter);
        ftm.filterTypeLayout.spinnerTeritorryid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Teritorry cmm = (Teritorry) teritoryAdapter.getItem(position);
                selectedteriId = String.valueOf(cmm.getTerritoryId());
            }
        });
        ftm.filterMonthYearLayout.txtFromDate.setText(UtilityHelper._GetCurrentDate());
        ftm.filterMonthYearLayout.txtToDate.setText(UtilityHelper._GetCurrentDate());
        ftm.filterMonthYearLayout.ivDatePickerFromDate.setOnClickListener(v -> UtilityHelper._datePickerDialogeForDates(ftm.filterMonthYearLayout.txtFromDate, CustomerApprovalListActivity.this));
        ftm.filterMonthYearLayout.ivDatePickerToDate.setOnClickListener(v -> UtilityHelper._datePickerDialogeForDates(ftm.filterMonthYearLayout.txtToDate, CustomerApprovalListActivity.this)
        );
        ftm.filterTopLayout.btnDonefilter.setOnClickListener(v -> {
            /*switch (selectedTyp) {
                case "MIO":
                    MIO mioempid;
                    mioempid = (MIO) ftm.spinnerApprovalMio.getSelectedItem();
                    empid = mioempid.getMIOEmpId();

                    break;
                case "AM":
                    ASM amempid = (ASM) ftm.spinnerApprovalAsm.getSelectedItem();
                    empid = amempid.getASMEmpId();

                    break;
                case "DZSM":
                    RSM dzsmempid = (RSM) ftm.spinnerApprovalRsm.getSelectedItem();
                    empid = dzsmempid.getRSMEmpId();
                    break;
            }*/
            fromdat = ftm.filterMonthYearLayout.txtFromDate.getText().toString();
            todate = ftm.filterMonthYearLayout.txtToDate.getText().toString();
            Map<String, String> filters = new HashMap<>();
            filters = new HashMap<>();
            filters.put("Role", RoleType);
            filters.put("AppStatus", "");
            filters.put("FromDt", fromdat);
            filters.put("ToDt", todate);
            filters.put("TerritoryId", selectedteriId);
            filters.put("EmpId", "");
            filters.put("CustomerTypeId", selectedcustomertypeId);
            filters.put("ProgramTypeId", selectedprogramId);
            filters.put("SMCTypeId", selectedpharmatypeId);

            Constants.filterparams=params;
            Constants.filtermap=filters;
            hitApi(params, filters);
            bsheetdlg.cancel();
        });
        ftm.filterTopLayout.ivCancel.setOnClickListener(v -> bsheetdlg.cancel());
    }

    @Override
    public void OnRevieveCustomerApproval(List<CustomerApprovalList> aList) {
        if (pd != null || pd.isShowing()) {
            pd.dismiss();
        }
        try {
            if (aList != null) {
                pending = aList.size();
                binding.pending.setText(String.valueOf(pending));
                adapter = new customerTeamApprovalAdapter(this, aList,this);
               /* int pos= 0;
                try {
                    pos = binding.rvCustomerrapproval.getAdapter().getItemCount()-1;
                } catch (Exception exception) {
                    exception.printStackTrace();
                }*/
                LinearLayoutManager mLayoutManager = new LinearLayoutManager(CustomerApprovalListActivity.this);
              /*  mLayoutManager.setReverseLayout(true);
                mLayoutManager.setStackFromEnd(true);*/
                binding.rvCustomerrapproval.setLayoutManager(mLayoutManager);
                binding.rvCustomerrapproval.setItemAnimator(new DefaultItemAnimator());
                binding.rvCustomerrapproval.setAdapter(adapter);
                binding.rvCustomerrapproval.setItemAnimator(null);
                //binding.rvCustomerrapproval.scrollToPosition(0);
                //  binding.rvCustomerrapproval.smoothScrollToPosition(pos);


                adapter.notifyDataSetChanged();
                //binding.userCount.setText(String.valueOf(teamlist.size()));
                if (aList.size() == 0) {
                    binding.nodta.setVisibility(View.VISIBLE);
                    binding.rvCustomerrapproval.setVisibility(View.GONE);
                } else {
                    binding.nodta.setVisibility(View.GONE);
                    binding.rvCustomerrapproval.setVisibility(View.VISIBLE);
                }
            } else {
                binding.nodta.setVisibility(View.VISIBLE);
                binding.rvCustomerrapproval.setVisibility(View.GONE);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void OnSuccess(String msg) {
        if(pd!=null || pd.isShowing())
        {
            pd.dismiss();
        }
        SnackBarManagement._success_CustomMessage(binding.getRoot(),msg);
        Map<String, String> filter = new HashMap<>();
        filter.put("Role", RoleType);
        filter.put("AppStatus", "");
        filter.put("FromDt", "");
        filter.put("ToDt", "");
        filter.put("TerritoryId", "");
        filter.put("EmpId", "");

        filter.put("CustomerTypeId", selectedcustomertypeId);
        filter.put("ProgramTypeId", selectedprogramId);
        filter.put("SMCTypeId", selectedpharmatypeId);
        hitApi(Constants.filterparams, Constants.filtermap);
    }

    @Override
    public void OnError(int type) {
        if (pd != null || pd.isShowing()) {
            pd.dismiss();
        }
        if (type == 1) {
            SnackBarManagement._error_CustomMessage(binding.getRoot(), "NO Data");
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        try {
            Map<String, String> filterz = new HashMap<>();
            filterz.put("Role", RoleType);
            filterz.put("AppStatus", "");
            filterz.put("FromDt", fromdat);
            filterz.put("ToDt", todate);
            filterz.put("TerritoryId", selectedteriId);
            filterz.put("EmpId", "");
            filterz.put("CustomerTypeId", selectedcustomertypeId);
            filterz.put("ProgramTypeId", selectedprogramId);
            filterz.put("SMCTypeId", selectedpharmatypeId);
            hitApi(params, filterz);
            // presenter.GetCustomerApprovalList(params, filterz, binding.master);
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

    @Override
    public void approvalTcust(CustomerApprovalList cl) {
        if (RoleTypeId == 2) {
            next = cl.getRSMEMPId();
        }
        if (RoleTypeId == 3) {
            next = cl.getNSMEMPId();
        }
        if (RoleTypeId == 4) {
            next = 0;
        }

        CustomerApprovalRQ req = new CustomerApprovalRQ();
        int step = cl.getStep();
        int fstep = step + 1;

        req.setCustomerApprovalId(0);
        req.setFromEmpId(empid);
        req.setToEmpId(next);
        req.setTableId(cl.getCustomerSMListDao().getCustomerMasterId());
        req.setStatus("Verified");//Accepted==approve for Admin
        req.setType(cl.getType());
        req.setStep(fstep);
        req.setEntryByApp(empid);
        String entrydate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        String entrytime = new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(new Date());
        req.setEntryDateApp(entrydate);
        req.setEntryTimeApp(entrytime);
        req.setMenuId(302);

        Gson gson=new Gson();
        String approv=gson.toJson(req);
        System.out.println("systen"+approv);
        presenter.SaveCustomerApprovalList(req);
    }
}