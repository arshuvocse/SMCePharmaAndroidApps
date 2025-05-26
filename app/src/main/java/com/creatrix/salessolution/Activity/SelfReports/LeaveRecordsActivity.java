package com.creatrix.salessolution.Activity.SelfReports;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.creatrix.salessolution.Activity.Leave.LeaveActivity;
import com.creatrix.salessolution.Interface.ILeave;
import com.creatrix.salessolution.Model.LeaveTypeInfo;
import com.creatrix.salessolution.Model.LeaveVM;
import com.creatrix.salessolution.Model.MarketStructure.AssignEmpTable.ASM;
import com.creatrix.salessolution.Model.MarketStructure.AssignEmpTable.MIO;
import com.creatrix.salessolution.Model.MarketStructure.AssignEmpTable.RSM;
import com.creatrix.salessolution.Network.RetrofitClientInstance;
import com.creatrix.salessolution.Network.UserProcessAPI;
import com.creatrix.salessolution.Presenter.LeavePresenter;
import com.creatrix.salessolution.R;
import com.creatrix.salessolution.RecyclerAdapter._leaveRecords_Recycler;
import com.creatrix.salessolution.UtilityHelper.SessionManagement;
import com.creatrix.salessolution.UtilityHelper.UtilityHelper;
import com.creatrix.salessolution.databinding.ActivityLeaveRecordsBinding;
import com.creatrix.salessolution.databinding.FilterMasterBinding;
import com.github.dewinjm.monthyearpicker.MonthYearPickerDialog;
import com.github.dewinjm.monthyearpicker.MonthYearPickerDialogFragment;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.net.SocketTimeoutException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import okhttp3.HttpUrl;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LeaveRecordsActivity extends AppCompatActivity implements ILeave.View{
    ActivityLeaveRecordsBinding binding;
    private _leaveRecords_Recycler mAdapter;
    TextView selectedMonthTxt;
    ProgressDialog progressDoalog;
    LeavePresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityLeaveRecordsBinding.inflate(getLayoutInflater());
        setContentView(R.layout.activity_leave_records);
        setContentView(binding.getRoot());
        presenter=new LeavePresenter(this,this);
        binding.toolbarCustom.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        SessionManagement session = new SessionManagement(getApplicationContext());
        session.checkLogin();
        HashMap<String, String> user = session.getUserDetails();
        String userName = user.get(SessionManagement.KEY_LoginName);
        int empId = Integer.parseInt(user.get(SessionManagement.KEY_EmpId));

        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        presenter.GetLeaveRecords(empId,String.valueOf(month+1), year);
      //  GetReportData(empId,String.valueOf(month), year);

        binding.atteFilter.setOnClickListener(v -> {
           // openFilter();
            MonthPicker(empId);
        });

        FloatingActionButton leadAddBtn = findViewById(R.id.leadAddBtn);
        leadAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LeaveRecordsActivity.this, LeaveActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
    }

    public void MonthPicker(int empId) {
        int yearSelected;
        int monthSelected;
        Calendar calendar = Calendar.getInstance();
        yearSelected = calendar.get(Calendar.YEAR);
        monthSelected = calendar.get(Calendar.MONTH);
        MonthYearPickerDialogFragment dialogFragment = MonthYearPickerDialogFragment
                .getInstance(monthSelected, yearSelected);
        dialogFragment.show(getSupportFragmentManager(), null);
        dialogFragment.setOnDateSetListener(new MonthYearPickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(int year, int monthOfYear) {
                //selectedMonthTxt.setText("");
                //selectedMonthTxt.setText(UtilityHelper.monthNameArrayFull[monthOfYear]+','+year);
                //GetReportData(empId,(monthOfYear+1),year);
                presenter.GetLeaveRecords(empId,String.valueOf(monthOfYear+1),year);
            }
        });
    }
   /* public void GetReportData(int empId,String Month, int year) {
        progressDoalog = new ProgressDialog(LeaveRecordsActivity.this);
        progressDoalog.setMessage("Loading....");
        progressDoalog.show();
        progressDoalog.setCanceledOnTouchOutside(false);
        try {
            UserProcessAPI service = RetrofitClientInstance.getRetrofitInstance().create(UserProcessAPI.class);
            Call<List<LeaveVM>> call = service.GetLeaveRecords(empId,Month,year);
            HttpUrl ds = call.request().url();
            call.enqueue(new Callback<List<LeaveVM>>() {
                @Override
                public void onResponse(Call<List<LeaveVM>> call, Response<List<LeaveVM>> response) {
                    progressDoalog.dismiss();
                    LoadinView(response.body());
                }

                @Override
                public void onFailure(Call<List<LeaveVM>> call, Throwable t) {
                    progressDoalog.dismiss();
                    if (t instanceof SocketTimeoutException) {
                        ErrorView("Slow Connection Detected");
                    } else {
                        ErrorView("Some Error Occurred");
                    }


                }
            });

        } catch (Exception ex) {
            progressDoalog.dismiss();
            ErrorView("Some Error Occurred");

        }

    }*/
  /*  public void LoadinView(List<LeaveVM> aList) {
        if (aList != null) {
            binding.count.setText(String.valueOf(aList.size()));
            mAdapter = new _leaveRecords_Recycler(aList);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            binding.recyclerView.setLayoutManager(mLayoutManager);
            binding.recyclerView.setItemAnimator(new DefaultItemAnimator());
            binding.recyclerView.setAdapter(mAdapter);
            binding.recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(),
                    DividerItemDecoration.VERTICAL));
            binding.recyclerView.setItemAnimator(null);
            binding.recyclerView.scrollToPosition(0);
            mAdapter.notifyDataSetChanged();
        }
    }*/

    public void ErrorView(String msg) {
        Toast.makeText(LeaveRecordsActivity.this, msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onLeaveTypeGet(List<LeaveTypeInfo> aList) {

    }

    @Override
    public void onSaveSuccess(String message) {

    }

    @Override
    public void onSaveError(String message) {

    }

    @Override
    public void onLeaveRecordsGet(List<LeaveVM> aList) {
        if (aList != null) {
            binding.count.setText(String.valueOf(aList.size()));
            mAdapter = new _leaveRecords_Recycler(aList);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            binding.recyclerView.setLayoutManager(mLayoutManager);
            binding.recyclerView.setItemAnimator(new DefaultItemAnimator());
            binding.recyclerView.setAdapter(mAdapter);
            /*binding.recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(),
                    DividerItemDecoration.VERTICAL));*/
            binding.recyclerView.setItemAnimator(null);
            binding.recyclerView.scrollToPosition(0);
            mAdapter.notifyDataSetChanged();
        }
    }
}