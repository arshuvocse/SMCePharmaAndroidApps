package com.creatrix.salessolution.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.creatrix.salessolution.Activity.Attendance.AttendanceReportsActivity;
import com.creatrix.salessolution.Activity.Expense.Report.ExpenseClaimReportsActivity;
import com.creatrix.salessolution.Activity.SelfReports.ReportsStockActivity;
import com.creatrix.salessolution.Activity.SelfReports.ReportsTargetAcheiActivity;
import com.creatrix.salessolution.Activity.SelfReports.Reports_TadaActivity;
import com.creatrix.salessolution.DBAdapter.DBCrudHelper;
import com.creatrix.salessolution.R;
import com.creatrix.salessolution.Activity.SelfReports.ReportsDcrActivity;
import com.creatrix.salessolution.UtilityHelper.SessionManagement;
import com.creatrix.salessolution.databinding.ActivityReportsBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.HashMap;

public class ReportsActivity extends AppCompatActivity {
  //for role
    SessionManagement session;
    HashMap<String,String> userInfo=new HashMap<>();
    DBCrudHelper dbCrudHelper;
    ActivityReportsBinding viewBinding;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    Intent id = new Intent(ReportsActivity.this,MainDashboardActivity.class);
                    id.addFlags(id.FLAG_ACTIVITY_CLEAR_TOP | id.FLAG_ACTIVITY_CLEAR_TASK |id.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(id);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    break;
                case R.id.navigation_reports:
                    break;

                case R.id.navigation_more:
                    Intent i = new Intent(ReportsActivity.this,MoreActivity.class);
                    i.addFlags(i.FLAG_ACTIVITY_CLEAR_TOP | i.FLAG_ACTIVITY_CLEAR_TASK |i.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    break;

            }
            return true;
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_reports);

        viewBinding = com.creatrix.salessolution.databinding.ActivityReportsBinding.inflate(getLayoutInflater());
        View mainView = viewBinding.getRoot();
        setContentView(mainView);
        dbCrudHelper = new DBCrudHelper(ReportsActivity.this);
        session =new SessionManagement(ReportsActivity.this);
        userInfo=session.getUserDetails();
        String role=userInfo.get(SessionManagement.KEY_EmpRoleType);
        //String role=userInfo.get(SessionManagement.Extra);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setSelectedItemId(R.id.navigation_reports);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

      /*  switch (role)
        {
            case "MIO":
                viewBinding.crdTeamAtteClick.setVisibility(View.GONE);
                break;
            case "AM":
                viewBinding.crdTeamAtteClick.setVisibility(View.VISIBLE);
                break;
            case "DZSM":
                break;
            case "Admin":
                viewBinding.crdTeamAtteClick.setVisibility(View.VISIBLE);
                break;
        }*/

      /*  viewBinding.btnAtteClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ReportsActivity.this, AttendanceReportsActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });*/
        viewBinding.btnAtteClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ReportsActivity.this, AttendanceReportsActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });





        viewBinding.btnExpenseClaim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ReportsActivity.this, ExpenseClaimReportsActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        viewBinding.btnTadaClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ReportsActivity.this, Reports_TadaActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        viewBinding.btnStockList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ReportsActivity.this, ReportsStockActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
        viewBinding.btnTva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ReportsActivity.this, ReportsTargetAcheiActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        viewBinding.btnDcr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ReportsActivity.this, ReportsDcrActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });


    }



    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Exit App")
                .setMessage("Are you sure you want to exit?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                  /*      session.logoutUser();
                        dbCrudHelper._deleteAllRecordsFromaTable("tblInitTable");
                        dbCrudHelper._deleteAllRecordsFromaTable("tbl_ProductInfo");
                        dbCrudHelper._deleteAllRecordsFromaTable("tbl_ProductSampleInfo");
                        dbCrudHelper._deleteAllRecordsFromaTable("tblCustomerInfo");
                        dbCrudHelper._deleteAllRecordsFromaTable("tblDoctorInfo");
                        dbCrudHelper._deleteAllRecordsFromaTable("tblDoctorBrand");*/
                        finish();
                    }

                })
                .setNegativeButton("No", null)
                .show();
    }
}