package com.creatrix.salessolution.Activity.Attendance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.creatrix.salessolution.Activity.Attendance.Model.AttenApproval;
import com.creatrix.salessolution.Activity.Attendance.Report.AttenApprovalFragAdapter;
import com.creatrix.salessolution.DBAdapter.DBCrudHelper;
import com.creatrix.salessolution.Network.AttendanceApi;
import com.creatrix.salessolution.Network.RetrofitClientInstance;
import com.creatrix.salessolution.R;
import com.creatrix.salessolution.UtilityHelper.NetworkInformation;
import com.creatrix.salessolution.UtilityHelper.SessionManagement;
import com.creatrix.salessolution.UtilityHelper.SnackBarManagement;
import com.creatrix.salessolution.databinding.ActivityTeamAttendancReportBinding;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TeamAttendancReportActivity extends AppCompatActivity /*implements IAttendance.View*/ {
    SessionManagement session;
    HashMap<String, String> userInfo = new HashMap<>();
    ActivityTeamAttendancReportBinding binding;
    String params, role;
    String tagA, tagR, tagN, Areaid, Regionid, Groupid;
    DBCrudHelper dbCrudHelper;

    int RoleTypeId;
    String RoleType;
    AttenApprovalFragAdapter fadapter;
    FragmentManager fm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTeamAttendancReportBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        session = new SessionManagement(TeamAttendancReportActivity.this);
        userInfo = session.getUserDetails();
        role = userInfo.get(SessionManagement.KEY_EmpRole);
        RoleTypeId = Integer.parseInt(Objects.requireNonNull(userInfo.get(SessionManagement.KEY_EmpRoleTypeId)));
        RoleType = userInfo.get(SessionManagement.KEY_EmpRoleType);

        tagA = ".EmpAreaId=";
        tagR = ".EmpRegionId=";
        tagN = ".EmpGroupId=";

        dbCrudHelper = new DBCrudHelper(TeamAttendancReportActivity.this);
        switch (role) {
            case "AM":
                Areaid = String.valueOf(dbCrudHelper.getCurrentUserAreaId_SQLite());
               // params = "And tblApprovalLog" + tagA + Areaid;
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
        prepareTab();
    }


    @SuppressLint("SetTextI18n")
    public void prepareTab() {
        binding.approvetab.setTabGravity(TabLayout.GRAVITY_FILL);
        fm = getSupportFragmentManager();
        fadapter = new AttenApprovalFragAdapter(fm, getLifecycle(), this/*,tabname,tcc*/);
        binding.approveViewpager.setAdapter(fadapter);
        final List<String> colors = new ArrayList<String>() {
            {
                add("#D6EFF3");
                add("#FFFFFF");
            }
        };

        new TabLayoutMediator(binding.approvetab, binding.approveViewpager,
                (tab, position) -> {
                    LayoutInflater layoutInflater = LayoutInflater.from(TeamAttendancReportActivity.this);
                    //View tabView = layoutInflater.inflate(R.layout.tab_custom_layout, null, false);
                    @SuppressLint("InflateParams") View tabView = layoutInflater.inflate(R.layout.tab_custom_layout, null, false);
                    TextView tabtag = (TextView) tabView.findViewById(R.id.tabtagz);
                    TextView tabitemcounts = (TextView) tabView.findViewById(R.id.tabitemcounts);
                    tab.setCustomView(tabView);
                    tab.view.setBackgroundColor(Color.parseColor(colors.get(position)));
                    switch (position) {
                        case 0:
                            Map<String,String> filter=new HashMap<>();
                            filter.put("Role",role);
                            filter.put("AppStatus","0");
                            filter.put("AttType","1");
                            filter.put("FromDt","");
                            filter.put("ToDt","");
                            filter.put("EmpId","");

                            tabtag.setText("Punch IN");
                            tabitemcounts.setText("0");
                            if (NetworkInformation.isConnected(TeamAttendancReportActivity.this)) {
                                try {
                                    AttendanceApi service = RetrofitClientInstance.getRetrofitInstance().create(AttendanceApi.class);
                                    Call<List<AttenApproval>> call = service.GetAttenInfoNew(params, filter);
                                    call.enqueue(new Callback<List<AttenApproval>>() {
                                        @Override
                                        public void onResponse(@NonNull Call<List<AttenApproval>> call, @NonNull Response<List<AttenApproval>> response) {
                                            if (response.body() != null) {
                                                List<AttenApproval> approval = response.body();
                                                int icountPIN = approval.size();
                                                tabitemcounts.setText(String.valueOf(icountPIN));
                                            } else {
                                                SnackBarManagement._warning_CustomMessage(binding.getRoot(),"Slow Connection Detected. Please try again");
                                            }
                                        }
                                        @Override
                                        public void onFailure(@NonNull Call<List<AttenApproval>> call, @NonNull Throwable t) {
                                            Toast.makeText(TeamAttendancReportActivity.this, "faild " + t.getMessage(), Toast.LENGTH_SHORT).show();
                                           // progressDoalog.dismiss();
                                            if (t instanceof SocketTimeoutException) {
                                                //view.onError("Slow Connection Detected. Please try again", 1);
                                                SnackBarManagement._warning_CustomMessage(binding.getRoot(),"Slow Connection Detected. Please try again");
                                            } else {
                                                // view.onError("Something went wrong... Please try again", 1);
                                                SnackBarManagement._error_CustomMessage(binding.getRoot(),"Something went wrong... Please try again");
                                            }
                                        }
                                    });

                                } catch (Exception ex) {
                                    String str = ex.toString();
                                    Log.e("Exception", str);
                                }

                            } else {
                                SnackBarManagement._error_CustomMessage(binding.getRoot(),"Something went wrong... Please try again");
                            }
                            break;
                        case 1:
                            Map<String,String> filters=new HashMap<>();
                            filters.put("Role",role);
                            filters.put("AppStatus","0");
                            filters.put("AttType","2");
                            filters.put("FromDt","");
                            filters.put("ToDt","");
                            filters.put("EmpId","");
                            tabtag.setText("Punch Out");
                            tabitemcounts.setText("0");
                            if (NetworkInformation.isConnected(TeamAttendancReportActivity.this)) {
                                try {
                                    AttendanceApi service = RetrofitClientInstance.getRetrofitInstance().create(AttendanceApi.class);
                                    Call<List<AttenApproval>> call = service.GetAttenInfoNew(params, filters);
                                    call.enqueue(new Callback<List<AttenApproval>>() {
                                        @Override
                                        public void onResponse(@NonNull Call<List<AttenApproval>> call, @NonNull Response<List<AttenApproval>> response) {
                                            if (response.body() != null) {
                                                List<AttenApproval> approval = response.body();
                                                int icountPOut = approval.size();
                                                tabitemcounts.setText(String.valueOf(icountPOut));
                                            } else {
                                                SnackBarManagement._warning_CustomMessage(binding.getRoot(),"Slow Connection Detected. Please try again");
                                            }
                                        }
                                        @Override
                                        public void onFailure(@NonNull Call<List<AttenApproval>> call, @NonNull Throwable t) {
                                            SnackBarManagement._error_CustomMessage(binding.getRoot(),t.getMessage());
                                           // progressDoalog.dismiss();
                                            if (t instanceof SocketTimeoutException) {
                                                //view.onError("Slow Connection Detected. Please try again", 1);
                                                SnackBarManagement._warning_CustomMessage(binding.getRoot(),"Slow Connection Detected. Please try again");
                                            } else {
                                               // view.onError("Something went wrong... Please try again", 1);
                                                SnackBarManagement._error_CustomMessage(binding.getRoot(),"Something went wrong... Please try again");
                                            }
                                        }
                                    });

                                } catch (Exception ex) {
                                    //progressDoalog.dismiss();
                                    String str = ex.toString();
                                    Log.e("Exception", str);
                                }


                            } else {
                                SnackBarManagement._error_CustomMessage(binding.getRoot(),"No Internet Connection");
                            }

                            break;

                    }
                }).attach();
        prepareViewpager();
    }
    private void prepareViewpager() {
        binding.approvetab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                binding.approveViewpager.setCurrentItem(tab.getPosition());
                final List<String> colors = new ArrayList<String>() {
                    {
                        add("#D6EFF3");
                        add("#F0FBF6");
                    }
                };

                if (tab.getPosition() == 0) {
                    // tab.view.setBackgroundColor(Color.parseColor("#DADADA"));
                    tab.view.setBackgroundColor(Color.parseColor(colors.get(0)));
                } else {
                    tab.view.setBackgroundColor(Color.parseColor(colors.get(tab.getPosition())));
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                final List<String> white = new ArrayList<String>() {
                    {
                        add("#FFFFFF");
                        add("#FFFFFF");
                    }
                };
                //tab.view.setAlpha(1);
                tab.view.setBackgroundColor(Color.parseColor(white.get(tab.getPosition())));
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        binding.approveViewpager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                binding.approvetab.selectTab(binding.approvetab.getTabAt(position));
            }
        });
    }
}