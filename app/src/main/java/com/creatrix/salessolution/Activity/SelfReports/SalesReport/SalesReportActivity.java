package com.creatrix.salessolution.Activity.SelfReports.SalesReport;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.creatrix.salessolution.Activity.OrderProcess.Adapter.SalesReportSliderAdapter;
import com.creatrix.salessolution.DBAdapter.DBCrudHelper;
import com.creatrix.salessolution.R;
import com.creatrix.salessolution.UtilityHelper.SessionManagement;
import com.creatrix.salessolution.databinding.ActivitySalesReportBinding;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
public class SalesReportActivity extends AppCompatActivity {
    ActivitySalesReportBinding binding;
    SessionManagement session;
    String params, role;
    String tagA, tagR, tagN, Areaid, Regionid, Groupid;
    DBCrudHelper dbCrudHelper;

    int RoleTypeId;
    String RoleType;
    FragmentManager fm;
    HashMap<String, String> userInfo = new HashMap<>();
    SalesReportSliderAdapter fadapter;
    String from;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySalesReportBinding.inflate(getLayoutInflater());
       // setContentView(R.layout.activity_sales_report);
        setContentView(binding.getRoot());

        session = new SessionManagement(SalesReportActivity.this);
        userInfo = session.getUserDetails();
        role = userInfo.get(SessionManagement.KEY_EmpRole);
        RoleTypeId = Integer.parseInt(userInfo.get(SessionManagement.KEY_EmpRoleTypeId));
        RoleType = userInfo.get(SessionManagement.KEY_EmpRoleType);
        from =getIntent().getStringExtra("From");
        tagA = ".EmpAreaId=";
        tagR = ".EmpRegionId=";
        tagN = ".EmpGroupId=";

        dbCrudHelper = new DBCrudHelper(SalesReportActivity.this);
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
    public void prepareTab() {
        binding.approvetab.setTabGravity(TabLayout.GRAVITY_FILL);
        fm = getSupportFragmentManager();
        fadapter = new SalesReportSliderAdapter(fm, getLifecycle(), this,from/*,tabname,tcc*/);
        binding.salesViewpager.setAdapter(fadapter);
        final List<String> colors = new ArrayList<String>() {
            {
                add("#D6EFF3");
                add("#FFFFFF");
            }
        };
        new TabLayoutMediator(binding.approvetab, binding.salesViewpager,
                new TabLayoutMediator.TabConfigurationStrategy() {
                    @Override
                    public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                        LayoutInflater layoutInflater = LayoutInflater.from(SalesReportActivity.this);
                        View tabView = layoutInflater.inflate(R.layout.tab_custom_layout2, null, false);
                        TextView tabtag = (TextView) tabView.findViewById(R.id.tabtagz);
                        tab.setCustomView(tabView);
                        tab.view.setBackgroundColor(Color.parseColor(colors.get(position)));
                        switch (position) {
                            case 0:
                                tabtag.setText("Customer Wise");
                                break;
                            case 1:
                                tabtag.setText("Product Wise");
                                break;

                        }

                    }
                }).attach();
        prepareViewpager();
    }
    private void prepareViewpager() {
        binding.approvetab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                binding.salesViewpager.setCurrentItem(tab.getPosition());
                final List<String> colors = new ArrayList<String>() {
                    {
                        add("#D6EFF3");
                        add("#F0FBF6");
                    }
                };

                if (tab.getPosition() == 0) {
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
                        //add("#FFFFFF");
                        // add("#FFFFFF");
                    }
                };
                //tab.view.setAlpha(1);
                tab.view.setBackgroundColor(Color.parseColor(white.get(tab.getPosition())));
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        binding.salesViewpager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                binding.approvetab.selectTab(binding.approvetab.getTabAt(position));
            }
        });
    }
}