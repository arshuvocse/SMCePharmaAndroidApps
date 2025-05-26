package com.creatrix.salessolution.Activity.DWSP;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.creatrix.salessolution.Activity.DWSP.Model.DWSPDailyModel;
import com.creatrix.salessolution.Activity.DWSP.Model.DWSPListener;
import com.creatrix.salessolution.Activity.DWSP.Model.SaveDWSP;
import com.creatrix.salessolution.Activity.Doctor.TourePlan.TourePlanAC;
import com.creatrix.salessolution.Model.MonthDate;
import com.creatrix.salessolution.Model.ResultInfo;
import com.creatrix.salessolution.Model.TourPlanMasterViewModel;
import com.creatrix.salessolution.Network.CalculationApiCall;
import com.creatrix.salessolution.Network.RetrofitClientInstance;
import com.creatrix.salessolution.Network.TourApiCall;
import com.creatrix.salessolution.NormalAdapter.DWSPStatusBottomSheetDialog;
import com.creatrix.salessolution.NormalAdapter.TourStatusBottomSheetDialog;
import com.creatrix.salessolution.R;
import com.creatrix.salessolution.Services.Constants;
import com.creatrix.salessolution.UtilityHelper.SessionManagement;
import com.creatrix.salessolution.UtilityHelper.SnackBarManagement;
import com.creatrix.salessolution.databinding.ActivityDWSPBinding;
import com.google.gson.Gson;

import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import okhttp3.HttpUrl;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DWSPActivity extends AppCompatActivity implements DWSPListener, DWSPStatusBottomSheetDialog.BottomSheetListener {
    String yearList[] = {"2021", "2022", "2023", "2024", "2025", "2026", "2027", "2028", "2029", "2030"};
    String monthNameArray[] = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
    ActivityDWSPBinding binding;
    Dialog dialog;
    private DWSPAdapter mAdapter;
    ProgressDialog progressDoalog;
    SessionManagement session;
    public TourPlanMasterViewModel aTpMasterData = new TourPlanMasterViewModel();
    private int tourCount = 0;
    private int initCount = 0;
    private int finalsubmit;
    DWSPDailyModel data;
    List<MonthDate> aMondateList = new ArrayList<>();
    boolean is_Entry = false;

    int monthV,yearPos,monthPos,empId;
    int selectedYearpos=0,selectedMonPos=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDWSPBinding.inflate(getLayoutInflater());
        //  setContentView(R.layout.activity_d_w_s_p);
        setContentView(binding.getRoot());
        binding.toolbarCustom.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        session = new SessionManagement(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        empId = Integer.parseInt(user.get(SessionManagement.KEY_EmpId));

        binding.refresh.setOnClickListener(view ->CallMasterApi(empId));
        CallMasterApi(empId);
        binding.detailsClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int monthValue = binding.monthSpinner.getSelectedItemPosition();
                String monthTxt = binding.monthSpinner.getSelectedItem().toString();
                Integer year = Integer.parseInt(binding.yearSpinner.getSelectedItem().toString());
                Bundle args = new Bundle();
                args.putInt("empId", empId);
                args.putInt("monthValue", (monthValue + 1));
                args.putInt("year", year);

                DWSPStatusBottomSheetDialog bottomSheetDialog = new DWSPStatusBottomSheetDialog();
                bottomSheetDialog.setArguments(args);
                bottomSheetDialog.show(getSupportFragmentManager(), "DWSPBottomSheetStatus");
            }
        });
    }

    private void CallMasterApi(int empId) {
        Calendar mCalendar = Calendar.getInstance();
        int year = mCalendar.get(Calendar.YEAR);
        int month = mCalendar.get(Calendar.MONTH);
        int day = mCalendar.get(Calendar.DAY_OF_MONTH);

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, yearList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.yearSpinner.setAdapter(dataAdapter);
        int yearPos = dataAdapter.getPosition(String.valueOf(year));
        selectedYearpos=yearPos;

      /*  if(Constants.current_dwsp_selected_year_pos==-1)
        {
            yearPos = dataAdapter.getPosition(String.valueOf(year));
            Constants.current_dwsp_selected_year_pos=yearPos;

        }else {
            yearPos=Constants.current_dwsp_selected_year_pos;
        }*/
        binding.yearSpinner.setSelection(yearPos);

        ArrayAdapter<String> dataAdapterMonth = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, monthNameArray);
        dataAdapterMonth.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.monthSpinner.setAdapter(dataAdapterMonth);

        String monName = monthNameArray[month];
       /* if(Constants.current_dwsp_selected_month_pos==-1)
        {
            monthPos = dataAdapterMonth.getPosition(monName);
            Constants.current_dwsp_selected_month_pos=monthPos;
        }else {
            monthPos=Constants.current_dwsp_selected_month_pos;
        }*/
        int monthPos = dataAdapterMonth.getPosition(monName);
        binding.monthSpinner.setSelection(monthPos);

        binding.monthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                int yearV = Integer.parseInt(binding.yearSpinner.getSelectedItem().toString());
                monthV = i + 1;
                selectedMonPos=i;
                aMondateList = printDatesInMonth(yearV, i);
                Constants.current_dwsp_selected_month_pos=i;
                GetDWSPMasterData(aMondateList, monthV, yearV, empId);
                initCount++;
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });
        binding.yearSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (initCount > 0) {
                    selectedYearpos=position;
                    List<MonthDate> aMondateList = new ArrayList<>();
                    int monthV = binding.monthSpinner.getSelectedItemPosition();
                    int yearV = Integer.parseInt(yearList[position].toString());
                    aMondateList = printDatesInMonth(yearV, (monthV + 1));
                    Constants.current_dwsp_selected_year_pos=position;
                    GetDWSPMasterData(aMondateList, monthV + 1, yearV, empId);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public List<MonthDate> printDatesInMonth(int year, int month) {
        List<MonthDate> aMondateList = new ArrayList<>();
        SimpleDateFormat dateV = new SimpleDateFormat("dd");
        SimpleDateFormat nameV = new SimpleDateFormat("EEE");
        SimpleDateFormat fmt2 = new SimpleDateFormat("EEE, dd MMM");
        SimpleDateFormat fmtdate = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.set(year, month, 1);
        int daysInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        for (int i = 0; i < daysInMonth; i++) {
            MonthDate monthDate = new MonthDate();
            monthDate.setMonthName(fmt2.format(cal.getTime()));
            monthDate.setDateValue(fmtdate.format(cal.getTime()));
            monthDate.setDateV(Integer.parseInt(dateV.format(cal.getTime())));
            monthDate.setDateName(nameV.format(cal.getTime()));
            monthDate.setMonthV(month + 1);
            monthDate.setYearV(year);
            aMondateList.add(monthDate);
            cal.add(Calendar.DAY_OF_MONTH, 1);
        }

        return aMondateList;

    }

    public void GetDWSPMasterData(List<MonthDate> aMondateList, int month, int year, int empId) {
        try {
            CalculationApiCall service = RetrofitClientInstance.getRetrofitInstance().create(CalculationApiCall.class);
            Call<List<DWSPDailyModel>> call = service.GetDWSP(month, year, empId);
            call.enqueue(new Callback<List<DWSPDailyModel>>() {
                @Override
                public void onResponse(@NonNull Call<List<DWSPDailyModel>> call, @NonNull Response<List<DWSPDailyModel>> response) {
                    if (response.body() != null) {
                        List<DWSPDailyModel> tpMaster = response.body();
                        OnArreangList(aMondateList,tpMaster);
                    } else {

                    }

                }

                @Override
                public void onFailure(@NonNull Call<List<DWSPDailyModel>> call, @NonNull Throwable t) {
                    if (t instanceof SocketTimeoutException) {
                    } else {
                    }
                }
            });

        } catch (Exception ex) {
        }

    }

    public void OnArreangList(List<MonthDate> aMondateList,List<DWSPDailyModel> aTpLIst) {
        binding.rvDwsp.setVisibility(View.VISIBLE);
        if (aTpLIst != null) {
            tourCount = aTpLIst.size();
            List<MonthDate> aSetList = new ArrayList<>();
            for (int i = 0; i < aMondateList.size(); i++) {
                List<DWSPDailyModel> aB = new ArrayList<>();
                for (int j = 0; j < aTpLIst.size(); j++) {
                    String baseDate = aMondateList.get(i).getDateValue();
                    String apiDate = aTpLIst.get(j).getDWSPDate();
                    aMondateList.get(i).setFinalSubmit(aTpLIst.get(j).getIsFinalSubmit());
                    if (baseDate.equals(apiDate)) {
                        aB.add(aTpLIst.get(j));
                        System.out.println(aB);
                    }else {
                    }
                   //
                }
                aSetList.add(aMondateList.get(i));
                aSetList.get(i).setDwspList(aB);
            }
            SetinRView(aSetList);

        } else {
            SetinRView(aMondateList);
        }
    }

    private void SetinRView(List<MonthDate> aMondateList) {

        mAdapter = new DWSPAdapter(DWSPActivity.this, aMondateList, this);
        binding.rvDwsp.setHasFixedSize(true);
        binding.rvDwsp.setAdapter(mAdapter);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.rvDwsp.setLayoutManager(layoutManager);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void dwspAdd(int month,int year,String date, boolean hit, int pos,String fcb,String cam,String gen) {

        SessionManagement sm = new SessionManagement(DWSPActivity.this);
        HashMap<String, String> user = sm.getUserDetails();
        int empId = Integer.parseInt(user.get(SessionManagement.KEY_EmpId));
      //  Toast.makeText(this, "" + String.valueOf(pos), Toast.LENGTH_SHORT).show();
        if (hit == true) {
            dialog = new Dialog(DWSPActivity.this);
            dialog.setContentView(R.layout.popup_dwsp);
            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
            layoutParams.copyFrom(dialog.getWindow().getAttributes());
            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
            layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
            dialog.getWindow().setAttributes(layoutParams);
            dialog.setCanceledOnTouchOutside(false);

            EditText et_famount = dialog.findViewById(R.id.et_fcb);
            EditText et_camount = dialog.findViewById(R.id.et_camount);
            EditText et_gamount = dialog.findViewById(R.id.et_gamount);

            if(fcb.equals(""))
            {
                et_famount.setText("0");
            }else {
                et_famount.setText(fcb);
            }
            if(cam.equals(""))
            {
                et_camount.setText("0");
            }else {
                et_camount.setText(cam);
            }
            if(gen.equals(""))
            {
                et_gamount.setText("0");
            }else {
                et_gamount.setText(gen);
            }
            Button submit = dialog.findViewById(R.id.savedwsp);
            Button cancel = dialog.findViewById(R.id.canceldwsp);
            submit.setOnClickListener(v1 -> {
                ProgressDialog pd = new ProgressDialog(DWSPActivity.this);
                pd.setMessage("Saving...");
                pd.setCancelable(false);
                pd.show();

                SaveDWSP dd=new SaveDWSP();
                dd.setDWSPMasterId(0);
                dd.setMonthValue(month);
                dd.setYearValue(year);
                dd.setEmpInfoId(empId);
                if(et_famount.getText().toString().trim().equals(""))
                {
                    dd.setFCBAmount(0.0);
                }else {
                    dd.setFCBAmount(Double.parseDouble(et_famount.getText().toString().trim()));
                }
                if(et_camount.getText().toString().trim().equals(""))
                {
                    dd.setCampaignAmount(0.0);
                }else {
                    dd.setCampaignAmount(Double.parseDouble(et_camount.getText().toString().trim()));
                }
                if(et_gamount.getText().toString().trim().equals(""))
                {
                    dd.setGeneralAmount(0.0);
                }else {
                    dd.setGeneralAmount(Double.parseDouble(et_gamount.getText().toString().trim()));
                }
                dd.setDWSPDate(date);
                int yearV = Integer.parseInt(binding.yearSpinner.getSelectedItem().toString());
                try{
                    CalculationApiCall service = RetrofitClientInstance.getRetrofitInstance().create(CalculationApiCall.class);
                    Call<ResultInfo> call = service.SaveDWSP(dd);
                    call.enqueue(new Callback<ResultInfo>() {
                        @Override
                        public void onResponse(@NonNull Call<ResultInfo> call, @NonNull Response<ResultInfo> response) {
                            if (response.body().getSuccess()==true) {
                                pd.dismiss();
                                GetDWSPMasterData(aMondateList, monthV, yearV, empId);
                                SnackBarManagement._success_CustomMessage(binding.masterLayout,"Save Successful");
                                dialog.dismiss();
                            } else {
                                pd.dismiss();
                                dialog.dismiss();
                                SnackBarManagement._warning_CustomMessage(binding.getRoot(),response.body().getMsd());
                            }
                        }
                        @Override
                        public void onFailure(@NonNull Call<ResultInfo> call, @NonNull Throwable t) {
                            if(t instanceof SocketTimeoutException){
                                pd.dismiss();
                            }else{
                                pd.dismiss();
                            }
                        }
                    });

                }catch (Exception ex){
                }
            });
            cancel.setOnClickListener(v1 -> {
                dialog.dismiss();
            });
            dialog.show();
        }

    }

    @Override
    public void deleteItem(int pos) {
       // Toast.makeText(this, "" + String.valueOf(pos), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void FinalSubmitClick(int month, int year, int empId, String remarks) {
        try
        {
            progressDoalog = new ProgressDialog(DWSPActivity.this);
            progressDoalog.setMessage("Final Submit Processing....");
            progressDoalog.show();
            progressDoalog.setCanceledOnTouchOutside(false);
            TourApiCall service = RetrofitClientInstance.getRetrofitInstance().create(TourApiCall.class);
            Call<ResultInfo> call = service.DWSPFinalSubmit(empId,month,year,remarks);
            call.enqueue(new Callback<ResultInfo>() {
                @Override
                public void onResponse(@NonNull Call<ResultInfo> call, @NonNull Response<ResultInfo> response) {

                    if(progressDoalog!=null || progressDoalog.isShowing())
                    {
                        progressDoalog.dismiss();
                    }
                    ResultInfo res= response.body();

                    if(response.body().getSuccess()==true){
                        new AlertDialog.Builder(DWSPActivity.this)
                                .setTitle("Success")
                                .setMessage("Final DWSP submission done for selected month")
                                .setPositiveButton("OK", (dialog, which) -> {
                                    Intent mIntent = getIntent();
                                    finish();
                                    startActivity(mIntent);
                                }).setCancelable(false).show();
                    }else {
                        SnackBarManagement._success_CustomMessage(binding.masterLayout,response.body().getMsd());
                    }
                }
                @Override
                public void onFailure(@NonNull Call<ResultInfo> call, @NonNull Throwable t) {
                    if(progressDoalog!=null || progressDoalog.isShowing())
                    {
                        progressDoalog.dismiss();
                    }
                    if(t instanceof SocketTimeoutException){

                        SnackBarManagement._error_CustomMessage(binding.getRoot(),"Some error occurred. Please try again");
                    }
                }
            });

        }catch (Exception ex){
            if(progressDoalog!=null || progressDoalog.isShowing())
            {
                progressDoalog.dismiss();
            }
            SnackBarManagement._error_CustomMessage(binding.getRoot(),"Some error occurred. Please try again");
            //Toast.makeText(TourePlanAC.this,"Some error occurred. Please try again",Toast.LENGTH_LONG).show();

        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();

        Constants.current_dwsp_selected_year_pos=-1;
        Constants.current_dwsp_selected_month_pos=-1;

    }
    @Override
    protected void onRestart() {
        super.onRestart();
        try {
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, yearList);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            binding.yearSpinner.setAdapter(dataAdapter);
            //int yearPos = dataAdapter.getPosition(String.valueOf(Constants.current_selected_year));
            binding.yearSpinner.setSelection(selectedYearpos);


            ArrayAdapter<String> dataAdapterMonth = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, monthNameArray);
            dataAdapterMonth.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            binding.monthSpinner.setAdapter(dataAdapterMonth);
            binding.monthSpinner.setSelection(selectedMonPos);


            binding.monthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    // List<MonthDate> aMondateList = new ArrayList<>();
                    List<MonthDate> aMondateList= new ArrayList<>();
                    int yearV = Integer.parseInt(binding.yearSpinner.getSelectedItem().toString());
                    int monthV = i + 1;

                    aMondateList = printDatesInMonth(yearV, i);
                    Constants.current_selected_month=monthV;
                    Constants.current_selected_year=yearV;
                    Constants.current_selected_month_pos=i;
                    GetDWSPMasterData(aMondateList, monthV, yearV, empId);
                    //   tppresenter.getTourPlanDataByEmpId(aMondateList, finalsubmit, monthV, yearV, empId);
                    initCount++;
                }
                public void onNothingSelected(AdapterView<?> adapterView) {
                    return;
                }
            });
            binding.yearSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (initCount > 0) {

                        int monthV = binding.monthSpinner.getSelectedItemPosition();
                        int yearV = Integer.parseInt(yearList[position].toString());
                        List<MonthDate> aMondateList= printDatesInMonth(yearV, (monthV + 1));
                        Constants.current_selected_month=monthV+1;
                        Constants.current_selected_year=yearV;
                        Constants.current_selected_year_pos=position;
                        GetDWSPMasterData(aMondateList, monthV + 1, yearV, empId);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}