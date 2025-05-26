package com.creatrix.salessolution.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.creatrix.salessolution.DBAdapter.DBCrudHelper;
import com.creatrix.salessolution.Interface.IMore;
import com.creatrix.salessolution.Presenter.MorePresenter;
import com.creatrix.salessolution.R;
import com.creatrix.salessolution.UtilityHelper.SessionManagement;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.HashMap;

public class MoreActivity extends AppCompatActivity implements IMore.View {

    LinearLayout btnCustomerSync;
    LinearLayout btnProductSync;
    Button btnLogout;
    IMore.Presenter presenter;
    SessionManagement session;
    DBCrudHelper dbCrudHelper;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    Intent id = new Intent(MoreActivity.this,MainDashboardActivity.class);
                    id.addFlags(id.FLAG_ACTIVITY_CLEAR_TOP | id.FLAG_ACTIVITY_CLEAR_TASK |id.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(id);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    break;
                case R.id.navigation_reports:
                    Intent i = new Intent(MoreActivity.this,ReportsActivity.class);
                    i.addFlags(i.FLAG_ACTIVITY_CLEAR_TOP | i.FLAG_ACTIVITY_CLEAR_TASK |i.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    break;

                case R.id.navigation_more:

                    break;
            }
            return true;
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setSelectedItemId(R.id.navigation_more);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        dbCrudHelper=new DBCrudHelper(MoreActivity.this);
        btnCustomerSync = findViewById(R.id.btnCustomerSync);
        btnProductSync = findViewById(R.id.btnProductSync);
        btnLogout = findViewById(R.id.btnLogout);
        presenter = new MorePresenter(this,this);
        session = new SessionManagement(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        String userName = user.get(SessionManagement.KEY_UserName);
        String empId = user.get(SessionManagement.KEY_EmpId);
        TextView userTxt = findViewById(R.id.userName);
        userTxt.setText(userName);


        btnCustomerSync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.doCustomerSync(Integer.parseInt(empId));
            }
        });

        btnProductSync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                presenter.doProductSync(Integer.parseInt(empId));
            }
        });



        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(MoreActivity.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Exit App")
                        .setMessage("Are you sure you want to Log out?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                session.logoutUser();
                                dbCrudHelper._deleteAllRecordsFromaTable("tbl_ProductInfo");
                                dbCrudHelper._deleteAllRecordsFromaTable("tbl_ProductSampleInfo");
                                dbCrudHelper._deleteAllRecordsFromaTable("tblCustomerInfo");
                                dbCrudHelper._deleteAllRecordsFromaTable("tblDoctorInfo");
                                dbCrudHelper._deleteAllRecordsFromaTable("tblDoctorBrand");
                            }

                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        });


    }

    @Override
    public void onSuccess(String Message) {
        new AlertDialog.Builder(MoreActivity.this)
                .setTitle("Success")
                .setMessage(Message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).setCancelable(false).show();

    }

    @Override
    public void onError(String Message) {
        new AlertDialog.Builder(MoreActivity.this)
                .setTitle("ERROR")
                .setMessage(Message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).setCancelable(false).show();

    }

    @Override
    public void onCustomerSync(String Message) {

    }

    @Override
    public void onProductSync(String Message) {

    }

    @Override
    public void onDoctorSync(String Message,boolean a) {

    }

    @Override
    public void onOtherSync(String Message) {

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
                        finish();
                    }

                })
                .setNegativeButton("No", null)
                .show();
    }
}