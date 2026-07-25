package com.creatrix.salessolution.Activity.Approval.VisitPlan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.creatrix.salessolution.Activity.Approval.VisitPlan.Model.ModelPending;
import com.creatrix.salessolution.Activity.Customer.Approval.Model.CustomerApprovalList;
import com.creatrix.salessolution.Activity.Reports.BonusGift.ActivityBonusGift;
import com.creatrix.salessolution.Activity.Reports.Collection.ActivityCollection;
import com.creatrix.salessolution.DBAdapter.DBCrudHelper;
import com.creatrix.salessolution.Interface.IMioDashboard;
import com.creatrix.salessolution.Model.Dashboard_SummeryVM;
import com.creatrix.salessolution.Network.ApiCustomerCall;
import com.creatrix.salessolution.Network.RetrofitClientInstance;
import com.creatrix.salessolution.Presenter.MioDashboardPresenter;
import com.creatrix.salessolution.R;
import com.creatrix.salessolution.UtilityHelper.NetworkInformation;
import com.creatrix.salessolution.UtilityHelper.SessionManagement;
import com.creatrix.salessolution.databinding.ActivityDashBinding;

import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityDash extends AppCompatActivity implements IMioDashboard.View {
    ActivityDashBinding binding;
    String RoleType = "";
    int empId;
    SessionManagement session;
    HashMap<String, String> userInfo = new HashMap<>();
    DBCrudHelper dbCrudHelper;
    String RoleTypeId, params, tagA, tagR, tagN, Areaid, Regionid, Groupid;
    IMioDashboard.Presenter mioDashboadPresenter;
    ProgressDialog pd;
    String currentDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mioDashboadPresenter = new MioDashboardPresenter(this);
        pd = new ProgressDialog(ActivityDash.this);
        session = new SessionManagement(ActivityDash.this);
        userInfo = session.getUserDetails();
        RoleType = userInfo.get(SessionManagement.KEY_EmpRoleType);
        empId = Integer.parseInt(Objects.requireNonNull(userInfo.get(SessionManagement.KEY_EmpId)));
        RoleTypeId = userInfo.get(SessionManagement.KEY_EmpRoleTypeId);
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        currentDate = df.format(c);

        try {
            tagA = ".EmpAreaId=";
            tagR = ".EmpRegionId=";
            tagN = ".EmpGroupId=";
            dbCrudHelper = new DBCrudHelper(this);
            switch (RoleType) {
                case "MIO":
                  binding.layPendings.setVisibility(View.GONE);
                  binding.menuPending.setVisibility(View.GONE);
                    break;
                case "AM":
                    Areaid = String.valueOf(dbCrudHelper.getCurrentUserAreaId_SQLite());
                    params = "AND View_Webapi_EmployeeFieldForceInfo" + tagA + Areaid;
                    binding.layPendings.setVisibility(View.VISIBLE);
                    binding.menuPending.setVisibility(View.VISIBLE);
                    break;
                case "DZSM":
                    Regionid = String.valueOf(dbCrudHelper.getCurrentUserRegionId_SQLite(String.valueOf(empId)));
                    params = "AND View_Webapi_EmployeeFieldForceInfo" + tagR + Regionid;
                    binding.layPendings.setVisibility(View.VISIBLE);
                    binding.menuPending.setVisibility(View.VISIBLE);
                    break;
                case "NSM":
                    Groupid = String.valueOf(dbCrudHelper.getCurrentUserGroupId_SQLite());
                    params = "AND View_Webapi_EmployeeFieldForceInfo" + tagN + Groupid;
                    binding.layPendings.setVisibility(View.VISIBLE);
                    binding.menuPending.setVisibility(View.VISIBLE);
                    break;

                case "Admin":
                    params = "";
                    binding.layPendings.setVisibility(View.VISIBLE);
                    binding.menuPending.setVisibility(View.VISIBLE);
                    break;

            }
            if (NetworkInformation.isConnected(ActivityDash.this)) {
                pd.setMessage("Please wait..");
                pd.setCancelable(false);
                pd.setCanceledOnTouchOutside(false);
                pd.show();
                mioDashboadPresenter.getTodaySummery(empId, currentDate);
            } else {
                binding.todayOrderAmtTxt.setText("0");
                binding.totalDcrTxt.setText("0");
                binding.totalPrscTxt.setText("0");
            }
        } finally {}


        binding.cadCollection.setOnClickListener(view1 -> {
            Intent i = new Intent(ActivityDash.this, ActivityCollection.class);
            startActivity(i);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });
        binding.cardBonusgift.setOnClickListener(view1 -> {
            Intent i = new Intent(ActivityDash.this, ActivityBonusGift.class);
            startActivity(i);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });
        binding.menuSummarys.setOnClickListener(v -> {
            binding.laySummary.setVisibility(View.VISIBLE);
            binding.layPendings.setVisibility(View.GONE);
            if (NetworkInformation.isConnected(ActivityDash.this)) {
                pd.setMessage("Please wait..");
                pd.setCancelable(false);
                pd.setCanceledOnTouchOutside(false);
                pd.show();
                mioDashboadPresenter.getTodaySummery(empId, currentDate);
            } else {
                binding.todayOrderAmtTxt.setText("0");
                binding.totalDcrTxt.setText("0");
                binding.totalPrscTxt.setText("0");
            }
        });
        binding.menuPending.setOnClickListener(v -> {
            binding.laySummary.setVisibility(View.GONE);
            binding.layPendings.setVisibility(View.VISIBLE);
            pd.setMessage("Please wait..");
            pd.setCancelable(false);
            pd.setCanceledOnTouchOutside(false);
            pd.show();
            hitApi(params, RoleType);
        });
    }

    private void hitApi(String params, String role) {
        try {

            ApiCustomerCall service = RetrofitClientInstance.getRetrofitInstance().create(ApiCustomerCall.class);
            Call<List<ModelPending>> call = service.GetDash(params, role);
            call.enqueue(new Callback<List<ModelPending>>() {
                @Override
                public void onResponse(@NonNull Call<List<ModelPending>> call, @NonNull Response<List<ModelPending>> response) {
                    if (response != null) {
                        showData(response.body());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<List<ModelPending>> call, @NonNull Throwable t) {

                }
            });
        } catch (Exception exception) {

        }
    }

    private void showData(List<ModelPending> body) {
        if (pd != null || pd.isShowing()) {
            pd.dismiss();
        }
        AdapterPending dapter = new AdapterPending(ActivityDash.this, body);
        binding.rvDash.setLayoutManager(new GridLayoutManager(ActivityDash.this, 2));
        binding.rvDash.setAdapter(dapter);
        dapter.notifyDataSetChanged();
    }


    @Override
    public void onDashboardSummeryDataBind(Dashboard_SummeryVM aData) {

    }

    @Override
    public void onTodaySummeryDataBind(Dashboard_SummeryVM aData) {
        if(pd!=null ||pd.isShowing())
        {
            pd.dismiss();
        }
        try {
            if (aData != null) {
                if (aData.getMonthlySaleAmount() != null) {
                    if (!aData.getMonthlySaleAmount().equals("0")) {
                        //binding.monthlySaleTxt.setText(aData.getMonthlySaleAmount());
                    }
                }


                if (aData.getOrderTodayAmt() != null) {
                    if (!aData.getOrderTodayAmt().equals("0")) {
                        binding.todayOrderAmtTxt.setText(aData.getOrderTodayAmt());
                    }

                }

                if (aData.getTotalDcr() != null) {
                    if (!aData.getTotalDcr().equals("0")) {
                        binding.totalDcrTxt.setText(aData.getTotalDcr());
                    }
                }
                if (aData.getTotalPrescription() != null) {
                    if (!aData.getTotalPrescription().equals("0")) {
                        binding.totalPrscTxt.setText(aData.getTotalPrescription());
                    }
                }
                if (aData.getColection() != null) {
                    if (!aData.getColection().equals("0")) {
                        binding.collection.setText(aData.getColection());
                    }
                }
                if (aData.getBonusGift() != null) {
                    if (!aData.getBonusGift().equals("0")) {
                        binding.bonusgift.setText(aData.getBonusGift());
                    }
                }
                if (aData.getTotalvsp() != null) {
                    if (!aData.getBonusGift().equals("0")) {
                        binding.VisitPlanCount.setText(aData.getTotalvsp());
                    }
                }
            }

        } catch (Exception ex) {
            Log.e("MioDashboard", "onDashboardSummeryDataBind: Error on TopBar Summery");
        }
    }

    @Override
    public void onError(String message) {

    }
}