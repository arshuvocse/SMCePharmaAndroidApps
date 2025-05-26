

package com.creatrix.salessolution.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.creatrix.salessolution.Activity.Attendance.AttendanceActivity;
import com.creatrix.salessolution.Activity.Customer.CustomerListActivity;
import com.creatrix.salessolution.Activity.Leave.LeaveActivity;
import com.creatrix.salessolution.Activity.Notice.NoticeActivity;
import com.creatrix.salessolution.DBAdapter.ProductSQLiteHelper;
import com.creatrix.salessolution.Interface.IProduct;
import com.creatrix.salessolution.Model.Product;
import com.creatrix.salessolution.Model.ProductSample;
import com.creatrix.salessolution.Presenter.ProductPresenter;
import com.creatrix.salessolution.R;
import com.creatrix.salessolution.UtilityHelper.SessionManagement;
import com.creatrix.salessolution.databinding.ActivityDashboardBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.HashMap;
import java.util.List;

public class DashboardActivity extends AppCompatActivity implements IProduct.View {

    ActivityDashboardBinding viewBinding;
    private TextView empName;
    SessionManagement session;
    ProductSQLiteHelper productSQLiteHelper;
    IProduct.Presenter presenter;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    break;
                case R.id.navigation_reports:
                    Intent i = new Intent(DashboardActivity.this,ReportsActivity.class);
                    startActivity(i);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    break;

                case R.id.navigation_more:
                    Intent id = new Intent(DashboardActivity.this,MoreActivity.class);
                    finish();
                    startActivity(id);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

                    break;
            }
            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_dashboard);
        viewBinding = com.creatrix.salessolution.databinding.ActivityDashboardBinding.inflate(getLayoutInflater());
        View mainView = viewBinding.getRoot();
        setContentView(mainView);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setSelectedItemId(R.id.navigation_home);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        session = new SessionManagement(getApplicationContext());
        session.checkLogin();
        HashMap<String, String> user = session.getUserDetails();
        String userName = user.get(SessionManagement.KEY_UserName);
        int empId = Integer.parseInt(user.get(SessionManagement.KEY_EmpId));
        viewBinding.userName.setText(userName);

        // sqlite intialization
        productSQLiteHelper = new ProductSQLiteHelper(getApplicationContext());

        presenter = new ProductPresenter(this,this);

        if(productSQLiteHelper.CheckDataExistinTable() != true){
            presenter.getProductsFromServer(empId);
        }

        if(productSQLiteHelper.CheckDataInTable("tblCompanyUnit") != true){
            presenter.getCompanyUnitFromServer(empId);
        }

        viewBinding.dashboardCustomerCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DashboardActivity.this, CustomerListActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        viewBinding.dashboardAttendanceCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DashboardActivity.this, AttendanceActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        viewBinding.dashboardOrderRecords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DashboardActivity.this,MioOrderListActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        viewBinding.dashboardCustomerCard2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DashboardActivity.this, NoticeActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });


        viewBinding.btnLeave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DashboardActivity.this, LeaveActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });




    }


    @Override
    public void OnError(String message) {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onProductsGet(List<Product> aList) {

    }

    @Override
    public void onProductSampleGet(List<ProductSample> aList) {

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