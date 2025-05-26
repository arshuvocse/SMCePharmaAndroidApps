package com.creatrix.salessolution.Activity.Doctor.VisitPlan;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.creatrix.salessolution.Activity.Doctor.VisitPlan.Model.VisitplanModel;
import com.creatrix.salessolution.Interface.IVisitPlan;
import com.creatrix.salessolution.Model.MonthDate;
import com.creatrix.salessolution.NormalAdapter.TourStatusBottomSheetDialog;
import com.creatrix.salessolution.Presenter.VisitPlanPresenter;
import com.creatrix.salessolution.Services.Constants;
import com.creatrix.salessolution.UtilityHelper.SessionManagement;
import com.creatrix.salessolution.databinding.FragmentVisitPlanListBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
public class VisitPlanListFragment extends Fragment implements IVisitPlan.View {
    FragmentVisitPlanListBinding binding;
    VisitPlanPresenter presenter;
    SessionManagement session;
    ProgressDialog progressDoalog;
    DoctorVisitPlanListAdapter adapter;
    String yearList[] = {"2021", "2022", "2023", "2024", "2025", "2026", "2027", "2028", "2029", "2030"};
    String monthNameArray[] = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
    String monthNameFull[] = {"January", "February", "March", "April", "May", "Jun", "July", "August", "September", "October", "November", "December"};
    List<MonthDate> aMondateList = new ArrayList<>();
    public VisitPlanListFragment() {
        // Required empty public constructor
    }
    int year,month,current_month,day,daysInMonth,empId,tourCount = 0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
      //return inflater.inflate(R.layout.fragment_visit_plan_list, container, false);
        binding = FragmentVisitPlanListBinding.inflate(inflater,container,false);
        Constants.WHO="VisitPlanListFragment";
        presenter = new VisitPlanPresenter(this, getContext());
        binding.toolbarCustom.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        session = new SessionManagement(getActivity());
        HashMap<String, String> user = session.getUserDetails();
        empId = Integer.parseInt(user.get(SessionManagement.KEY_EmpId));

        Calendar mCalendar = Calendar.getInstance();
         year = mCalendar.get(Calendar.YEAR);
         month = mCalendar.get(Calendar.MONTH);
         current_month = month+1;
         day = mCalendar.get(Calendar.DAY_OF_MONTH);
         daysInMonth = mCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);


        String monName = monthNameFull[month];
        String monWITHyear=monName+","+String.valueOf(year);
        SimpleDateFormat fmt2d = new SimpleDateFormat("MMM,yyyy", Locale.getDefault());
        //fmt2d.format(cal.getTime())
        binding.tvMonthYear.setText(monWITHyear);
        aMondateList= CalculateTotaldayesinMonth(mCalendar,daysInMonth,year,month);


       // presenter.getVisitPlanDataByEmpId(aMondateList, current_month, year, empId);
        binding.statusClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // int monthValue =  monthSpinner.getSelectedItemPosition();
                int monthValue =  current_month;
                // String monthTxt =  monthSpinner.getSelectedItem().toString();
                String monthTxt = String.valueOf(current_month);
                Integer yearz =year;
                Bundle args = new Bundle();
                args.putInt("empId", empId);
                //args.putInt("monthValue", (monthValue+1));
                args.putInt("monthValue", (monthValue));
                args.putInt("year", yearz);
                args.putInt("tourCount", tourCount);
                args.putString("monthTxt", monName);

                TourStatusBottomSheetDialog bottomSheetDialog = new TourStatusBottomSheetDialog();
                bottomSheetDialog.setArguments(args);
                bottomSheetDialog.show(getActivity().getSupportFragmentManager(),"TourBottomSheetStatus");
            }
        });
       // GetTourPlanDataFromServer(aMondateList, current_month, year, empId);

        return binding.getRoot();
    }

    private List<MonthDate> CalculateTotaldayesinMonth(Calendar myCalendar, int maxMonth, int year, int currentmonth) {
        List<MonthDate> aMondateList = new ArrayList<>();
        SimpleDateFormat dateV = new SimpleDateFormat("dd");
        SimpleDateFormat nameV = new SimpleDateFormat("EEEE");
        SimpleDateFormat fmtdate = new SimpleDateFormat("yyyy-MM-dd");
       // SimpleDateFormat monthV = new SimpleDateFormat("MM");
        String monName = monthNameFull[currentmonth];
        myCalendar = Calendar.getInstance();
        myCalendar.clear();
        myCalendar.set(year, currentmonth, 1);
        for (int i = 0; i < maxMonth; i++) {
            MonthDate monthDate = new MonthDate();
            monthDate.setMonthName(monName);
            //date number
            monthDate.setDateV(Integer.parseInt(dateV.format(myCalendar.getTime())));
            //date in formate in check
            monthDate.setDateValue(fmtdate.format(myCalendar.getTime()));
            monthDate.setDateName(nameV.format(myCalendar.getTime()));
            monthDate.setMonthV(currentmonth);
            monthDate.setYearV(year);
            aMondateList.add(monthDate);
            myCalendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        return aMondateList;
    }
    public void SetInRecyclerview(List<MonthDate> aMondateList) {
   /*     adapter = new DoctorVisitPlanListAdapter(getActivity(),aMondateList);
        binding.recyclerViewDaylist.setHasFixedSize(true);
        binding.recyclerViewDaylist.setAdapter(adapter);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        binding.recyclerViewDaylist.setLayoutManager(layoutManager);
        adapter.notifyDataSetChanged();*/

        //retrieve last position on start
       /* SharedPreferences getPrefs = this.getSharedPreferences("position", this.MODE_PRIVATE);
        SharedPreferences.Editor editor = getPrefs.edit();
        editor.commit();
        posdy = getPrefs.getInt("lastPos", 0);
        recyclerView.scrollToPosition(posdy);*/

     /*   binding.recyclerViewDaylist.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //lastPosition = layoutManager.findLastVisibleItemPosition();
                //   Toast.makeText(DoctorTourPlanActivity.this, "Last "+lastPosition, Toast.LENGTH_SHORT).show();
                Toast.makeText(DoctorTourPlanActivity.this, "Fast "+layoutManager.findFirstVisibleItemPosition(), Toast.LENGTH_SHORT).show();
                Toast.makeText(DoctorTourPlanActivity.this, "dy "+dy, Toast.LENGTH_SHORT).show();
                posdy=dy;
            }
        });*/

    }

 /*   @Override
    public void onStart() {
        super.onStart();
        presenter.getVisitPlanDataByEmpId(aMondateList,month,year,empId);
    }*/


    @Override
    public void OnArreangList(List<MonthDate> aMondateList,boolean isentry, List<VisitplanModel> aTpLIst) {
        if (aTpLIst != null) {
           // progressDoalog.dismiss();
            //tourCount = aTpLIst.size();
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

            SetInRecyclerview(aSetList);

        } else {
           // progressDoalog.dismiss();
            SetInRecyclerview(aMondateList);
           // Toast.makeText(getActivity(), "visit plan null", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void OnFailour(String msg) {
    }

    @Override
    public void OnSuccessVPPDay(String msg) {
    }

}