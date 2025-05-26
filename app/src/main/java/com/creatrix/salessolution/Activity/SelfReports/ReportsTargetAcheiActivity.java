package com.creatrix.salessolution.Activity.SelfReports;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.creatrix.salessolution.Model.Rp_TargetAcchivment;
import com.creatrix.salessolution.Network.APICall_Report_i;
import com.creatrix.salessolution.Network.RetrofitClientInstance;
import com.creatrix.salessolution.R;
import com.creatrix.salessolution.RecyclerAdapter._targetAcchivementProductList_Recycler;
import com.creatrix.salessolution.UtilityHelper.MathUtil;
import com.creatrix.salessolution.UtilityHelper.SessionManagement;
import com.creatrix.salessolution.UtilityHelper.UtilityHelper;
import com.github.dewinjm.monthyearpicker.MonthYearPickerDialog;
import com.github.dewinjm.monthyearpicker.MonthYearPickerDialogFragment;

import java.net.SocketTimeoutException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import okhttp3.HttpUrl;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReportsTargetAcheiActivity extends AppCompatActivity {
    ProgressDialog progressDoalog;
    private RecyclerView recyclerView;
    private _targetAcchivementProductList_Recycler mAdapter;
    TextView selectedMonthTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports_target_achei);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_custom);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
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
        GetReportData(empId,(month + 1),year);

        selectedMonthTxt = findViewById(R.id.selectedMonthTxt);
        selectedMonthTxt.setText(UtilityHelper.monthNameArrayFull[month]+','+year);


        ImageView datePickerDeliveryDate = (ImageView)findViewById(R.id.atteFilter);
        datePickerDeliveryDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MonthPicker(empId);
            }
        });
    }


    public void GetReportData(int empId,int month,int year) {
        progressDoalog = new ProgressDialog(ReportsTargetAcheiActivity.this);
        progressDoalog.setMessage("Target vs Achievement is Loading....");
        progressDoalog.show();
        progressDoalog.setCanceledOnTouchOutside(false);
        try{
            APICall_Report_i service = RetrofitClientInstance.getRetrofitInstance().create(APICall_Report_i.class);
            Call<List<Rp_TargetAcchivment>> call = service.GetTarvsAcchi(empId,month,year);
            HttpUrl ds = call.request().url();
            call.enqueue(new Callback<List<Rp_TargetAcchivment>>() {
                @Override
                public void onResponse(Call<List<Rp_TargetAcchivment>> call, Response<List<Rp_TargetAcchivment>> response) {
                    progressDoalog.dismiss();
                    LoadinView(response.body());
                }
                @Override
                public void onFailure(Call<List<Rp_TargetAcchivment>> call, Throwable t) {
                    progressDoalog.dismiss();
                    if(t instanceof SocketTimeoutException){
                        ErrorView("Slow Connection Detected");
                    }else{
                        ErrorView("Some Error Occurred");
                    }


                }
            });

        }catch (Exception ex){
            progressDoalog.dismiss();
            ErrorView("Some Error Occurred");

        }

    }

    public void LoadinView(List<Rp_TargetAcchivment> aList){
        if(aList != null){
            MakeTotalTargetAndAcchivement(aList);

            recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mAdapter = new _targetAcchivementProductList_Recycler(aList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(),
                DividerItemDecoration.VERTICAL));
        recyclerView.setItemAnimator(null);
        recyclerView.scrollToPosition(0);
        mAdapter.notifyDataSetChanged();
        }
    }

    public  void ErrorView(String msg){
        Toast.makeText(ReportsTargetAcheiActivity.this,msg,Toast.LENGTH_LONG).show();
    }


    public  void MakeTotalTargetAndAcchivement(List<Rp_TargetAcchivment> aList){

        try {
            Double totalTar = 0.00;
            Double totalAc = 0.00;

            for (int i=0;i<aList.size();i++){
                Double aT  = Double.parseDouble(aList.get(i).getTargetQty());
                Double aA  = Double.parseDouble(aList.get(i).getAchivment());

                totalTar = totalTar + aT;
                totalAc = totalAc + aA;
            }








            TextView mainTarget = findViewById(R.id.mainTarget);
            TextView mainAcciv = findViewById(R.id.mainAcciv);
            TextView mainPerc = findViewById(R.id.mainPerc);

            mainTarget.setText(totalTar.toString());
            mainAcciv.setText(totalAc.toString());

            Double perQty =(totalAc * 100)/totalTar;
            if(totalAc !=0){
                mainPerc.setText("0%");
            }else{
                Double AcPercent  = MathUtil.round(perQty,2);
                String withPercentSign = AcPercent.toString() + "%";
                mainPerc.setText(withPercentSign);
            }



        }catch (Exception ex){
            ex.printStackTrace();
        }

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
                selectedMonthTxt.setText("");
                selectedMonthTxt.setText(UtilityHelper.monthNameArrayFull[monthOfYear]+','+year);

                GetReportData(empId,(monthOfYear+1),year);


            }
        });
    }

    }