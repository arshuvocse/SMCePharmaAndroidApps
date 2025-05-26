package com.creatrix.salessolution.Activity.Doctor.VisitPlan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.creatrix.salessolution.Activity.Doctor.DoctorTourPlanActivity;
import com.creatrix.salessolution.Activity.Doctor.VisitPlan.Model.VisitPlanMaster;
import com.creatrix.salessolution.Activity.Doctor.VisitPlan.Model.VisitplanModel;
import com.creatrix.salessolution.Interface.IVisitPlan;
import com.creatrix.salessolution.Interface.Rcv_TourPlanListener;
import com.creatrix.salessolution.Model.MonthDate;
import com.creatrix.salessolution.Model.ResultInfo;
import com.creatrix.salessolution.Model.TourPlanMasterViewModel;
import com.creatrix.salessolution.Model.TourPlanViewModel;
import com.creatrix.salessolution.Network.ApiDoctorCall;
import com.creatrix.salessolution.Network.RetrofitClientInstance;
import com.creatrix.salessolution.NormalAdapter.DoctorTourStatusBottomSheetDialog;
import com.creatrix.salessolution.NormalAdapter.TourStatusBottomSheetDialog;
import com.creatrix.salessolution.Presenter.VisitPlanPresenter;
import com.creatrix.salessolution.R;
import com.creatrix.salessolution.RecyclerAdapter._doctorMonthSelectRecyclerAdapter;
import com.creatrix.salessolution.Services.Constants;
import com.creatrix.salessolution.UtilityHelper.SessionManagement;
import com.creatrix.salessolution.UtilityHelper.SnackBarManagement;
import com.creatrix.salessolution.databinding.ActivityVisitPlanBinding;

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

public class VisitPlanActivity extends AppCompatActivity implements Rcv_TourPlanListener, DoctorTourStatusBottomSheetDialog.BottomSheetListener, IVisitPlan.View{
  ActivityVisitPlanBinding binding;
   private static final String TAG = "VisitPlanActivity";
    String yearList[] = {"2021", "2022", "2023", "2024", "2025", "2026", "2027", "2028", "2029", "2030"};
    String monthNameArray[] = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
    ActiveListener activeListener;
    VisitPlanPresenter presenter;
    DoctorVisitPlanListAdapter mAdapter;
    ProgressDialog progressDoalog;
    SessionManagement session;
    public TourPlanMasterViewModel aTpMasterData = new TourPlanMasterViewModel();
    private int tourCount = 0;
    private int initCount = 0;
    private int lastPosition;
    private int posdy;
    public boolean isClickable = true;

    int yearPos,monthPos,empId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityVisitPlanBinding.inflate(getLayoutInflater());
        //setContentView(R.layout.activity_visit_plan);
        setContentView(binding.getRoot());
        session = new SessionManagement(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        empId = Integer.parseInt(user.get(SessionManagement.KEY_EmpId));

        presenter = new VisitPlanPresenter(this, VisitPlanActivity.this);
        binding.toolbarCustom.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        binding.refresh.setOnClickListener(view ->CallMasterApi(empId));
        CallMasterApi(empId);


        binding.statusClick.setOnClickListener(v -> {
            int monthValue = binding.monthSpinner.getSelectedItemPosition();
            String monthTxt = binding.monthSpinner.getSelectedItem().toString();
            Integer year1 = Integer.parseInt(binding.yearSpinner.getSelectedItem().toString());
            Bundle args = new Bundle();
            args.putInt("empId", empId);
            args.putInt("monthValue", (monthValue + 1));
            args.putInt("year", year1);
            args.putInt("visitCount", tourCount);
            args.putString("monthTxt", monthTxt);

            DoctorTourStatusBottomSheetDialog bottomSheetDialog = new DoctorTourStatusBottomSheetDialog();
            bottomSheetDialog.setArguments(args);
            bottomSheetDialog.show(getSupportFragmentManager(), "VisitBottomSheetStatus");
        });
    }

    private void CallMasterApi(int empid) {
        Calendar mCalendar = Calendar.getInstance();
        int year = mCalendar.get(Calendar.YEAR);
        int month = mCalendar.get(Calendar.MONTH);
        int day = mCalendar.get(Calendar.DAY_OF_MONTH);

        progressDoalog = new ProgressDialog(VisitPlanActivity.this);
        progressDoalog.setMessage("Visit Plan Loading....");
        progressDoalog.show();
        progressDoalog.setCanceledOnTouchOutside(false);

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, yearList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.yearSpinner.setAdapter(dataAdapter);
        if(Constants.current_vp_selected_year_pos==-1)
        {
            yearPos = dataAdapter.getPosition(String.valueOf(year));
            Constants.current_vp_selected_year_pos=yearPos;

        }else {
            yearPos=Constants.current_vp_selected_year_pos;
        }
        binding.yearSpinner.setSelection(yearPos);

        ArrayAdapter<String> dataAdapterMonth = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, monthNameArray);
        dataAdapterMonth.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.monthSpinner.setAdapter(dataAdapterMonth);
        String monName = monthNameArray[month];
        if(Constants.current_vp_selected_month_pos==-1)
        {
            monthPos = dataAdapterMonth.getPosition(monName);
            Constants.current_vp_selected_month_pos=monthPos;
        }else {
            monthPos=Constants.current_vp_selected_month_pos;
        }
        binding.monthSpinner.setSelection(monthPos);
        binding.monthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                List<MonthDate> aMondateList = new ArrayList<>();
                int yearV = Integer.parseInt(binding.yearSpinner.getSelectedItem().toString());
                int monthV = i + 1;
                aMondateList = printDatesInMonth(yearV, i);
                // GetTourPlanDataFromServer(aMondateList, monthV, yearV, empId);
                Constants.current_vp_selected_month_pos=i;
                GetTourPlanMasterData(aMondateList,monthV,yearV,empid);
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
                    List<MonthDate> aMondateList = new ArrayList<>();
                    int monthV =   binding.monthSpinner.getSelectedItemPosition();
                    int yearV = Integer.parseInt(yearList[position].toString());
                    aMondateList = printDatesInMonth(yearV, (monthV + 1));
                    //GetTourPlanDataFromServer(aMondateList, (monthV + 1), yearV, empId);
                    Constants.current_vp_selected_year_pos=position;
                    GetTourPlanMasterData(aMondateList,monthV+1,yearV,empid);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    public void SetInRecyclerview(List<MonthDate> aMondateList,boolean is_Entry) {
        if(progressDoalog!=null||progressDoalog.isShowing())
        {
            progressDoalog.dismiss();
        }
        mAdapter = new DoctorVisitPlanListAdapter(VisitPlanActivity.this,aMondateList,is_Entry);
        binding.recyclerViewDaylist.setHasFixedSize(true);
        binding.recyclerViewDaylist.setAdapter(mAdapter);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.recyclerViewDaylist.setLayoutManager(layoutManager);
        mAdapter.notifyDataSetChanged();
        binding.recyclerViewDaylist.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                posdy=dy;
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
    @Override
    public void OnArreangList(List<MonthDate> aMondateList,boolean is_Entry, List<VisitplanModel> aTpLIst) {
        binding.recyclerViewDaylist.setVisibility(View.VISIBLE);
        if (aTpLIst != null) {
            tourCount = aTpLIst.size();
            List<MonthDate> aSetList = new ArrayList<>();
            for (int i = 0; i < aMondateList.size(); i++) {
                List<VisitplanModel> aB = new ArrayList<>();
                for (int j = 0; j < aTpLIst.size(); j++) {
                    String baseDate = aMondateList.get(i).getDateValue();
                    String apiDate = aTpLIst.get(j).getTourPlanDate();
                    aMondateList.get(i).setFinalSubmit(aTpLIst.get(j).isFinalSubmit());
                    if (baseDate.equals(apiDate)) {
                        aB.add(aTpLIst.get(j));
                        System.out.println(aB);
                    }
                }
                aSetList.add(aMondateList.get(i));
                aSetList.get(i).setVisitplanList(aB);
            }
            SetInRecyclerview(aSetList,is_Entry);

        } else {
            SetInRecyclerview(aMondateList,is_Entry);
            // Toast.makeText(getActivity(), "visit plan null", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void OnFailour(String msg) {
        if(progressDoalog!=null||progressDoalog.isShowing())
        {
            progressDoalog.dismiss();
        }
        SnackBarManagement._error_CustomMessage(binding.masterLayout,msg);
    }

    @Override
    public void OnSuccessVPPDay(String msg) {
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left,
                R.anim.slide_out_right);
    }
    @Override
    public void ReloadCurrentActivity() {
    }
    @Override
    public void FinalSubmitClick(int month, int year, int empId, String remarks) {
        try {
            progressDoalog = new ProgressDialog(VisitPlanActivity.this);
            progressDoalog.setMessage("Final Submit Processing....");
            progressDoalog.show();
            progressDoalog.setCanceledOnTouchOutside(false);
            ApiDoctorCall service = RetrofitClientInstance.getRetrofitInstance().create(ApiDoctorCall.class);
            Call<ResultInfo> call = service.UpdateDoctorTourPlan(empId,month,year,remarks);
            call.enqueue(new Callback<ResultInfo>() {
                @Override
                public void onResponse(@NonNull Call<ResultInfo> call, @NonNull Response<ResultInfo> response) {
                    progressDoalog.dismiss();
                    ResultInfo info = response.body();
                    if (info.getSuccess() == true) {
                        new AlertDialog.Builder(VisitPlanActivity.this)
                                .setTitle("Success")
                                .setMessage("Final Visit Plan submission done for selected month")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                      /*  Intent mIntent = getIntent();
                                        startActivity(mIntent);*/
                                        finish();

                                    }
                                }).setCancelable(false).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ResultInfo> call, @NonNull Throwable t) {
                    progressDoalog.dismiss();
                    if (t instanceof SocketTimeoutException) {
                        SnackBarManagement._error_CustomMessage(binding.masterLayout, "Slow network detected. Please try again");
                    } else {
                        SnackBarManagement._error_CustomMessage(binding.masterLayout, "Some error occurred. Please try again");
                    }
                }
            });

        } catch (Exception ex) {
            progressDoalog.dismiss();
            SnackBarManagement._error_CustomMessage(binding.masterLayout, "Got an exception. Please try again");
        }
    }
    public void GetTourPlanMasterData(List<MonthDate> aMondateList,int month,int year,int empId){
        try{
            ApiDoctorCall service = RetrofitClientInstance.getRetrofitInstance().create(ApiDoctorCall.class);
            Call<List<VisitPlanMaster>> call = service.GetDoctorVisitPlanMaster(month,year,empId);
            call.enqueue(new Callback<List<VisitPlanMaster>>() {
                @Override
                public void onResponse(@NonNull Call<List<VisitPlanMaster>> call, @NonNull Response<List<VisitPlanMaster>> response) {
                    if (response.body().size() > 0) {
                        SetinView(response.body(),aMondateList,month,year,empId);
                    } else {
                        boolean is_Entrya=true;
                        presenter.getVisitPlanDataByEmpId(aMondateList, is_Entrya, month, year, empId);
                    }
                }
                @Override
                public void onFailure(@NonNull Call<List<VisitPlanMaster>> call, @NonNull Throwable t) {
                    if(t instanceof SocketTimeoutException){
                    }else{
                    }
                }
            });

        }catch (Exception ex){
        }
    }
    public void SetinView(List<VisitPlanMaster> aList,List<MonthDate> aMondateList,int month,int year,int empId){
        try {
            boolean is_Entrynew=false;
            VisitPlanMaster tpMaster = new VisitPlanMaster();
            tpMaster = aList.get(0);
            if (tpMaster != null) {
                if (tpMaster.isFinalSubmit() == false) {
                    is_Entrynew = true;
                    presenter.getVisitPlanDataByEmpId(aMondateList, is_Entrynew, month, year, empId);
                    //  mAdapter.notifyDataSetChanged();
                } else {
                    if (tpMaster.getApprovalStatus().equals("0")) {
                        //Pending
                        is_Entrynew = false;
                    }
                    if (tpMaster.getApprovalStatus().equals("2")) {
                        //Approved
                        is_Entrynew = false;
                    }
                    if (tpMaster.getApprovalStatus().equals("3")) {
                        //Rejected
                        is_Entrynew = true;
                    }
                    presenter.getVisitPlanDataByEmpId(aMondateList, is_Entrynew, month, year, empId);
                }
            }

           /* if(aList!=null){
                tpMaster = aList.get(0);
                if(tpMaster!=null){
                    if(tpMaster.isFinalSubmit()==false){
                        activeListener.getstatus(0);

                    }else{
                        if(tpMaster.getApprovalStatus().equals("Pending")){
                            activeListener.getstatus(1);
                        }
                        if(tpMaster.getApprovalStatus().equals("Approved")){
                            activeListener.getstatus(2);
                        }
                        if(tpMaster.getApprovalStatus().equals("Rejected")){
                            activeListener.getstatus(3);
                        }
                    }


                }
            }*/

        }catch (Exception exception){
            exception.printStackTrace();
        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Constants.current_vp_selected_year_pos=-1;
        Constants.current_vp_selected_month_pos=-1;

    }
    @Override
    protected void onRestart() {
        super.onRestart();
        try {
            CallMasterApi(empId);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}