package com.creatrix.salessolution.Activity.Customer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.creatrix.salessolution.Activity.MainActivity;
import com.creatrix.salessolution.Activity.OrderMainActivity;
import com.creatrix.salessolution.DBAdapter.ProductSQLiteHelper;
import com.creatrix.salessolution.Interface.IProduct;
import com.creatrix.salessolution.Model.Customer;
import com.creatrix.salessolution.Model.Product;
import com.creatrix.salessolution.Model.ProductSample;
import com.creatrix.salessolution.Presenter.ProductPresenter;
import com.creatrix.salessolution.R;
import com.creatrix.salessolution.UtilityHelper.SessionManagement;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;

public class CustomerDashboarActivity extends AppCompatActivity implements IProduct.View  {
    SessionManagement session;
    TextView customerName;
    TextView customerCode;
    TextView adress;
    TextView mobileNo;
    IProduct.Presenter presenter;
    ProductSQLiteHelper productSQLiteHelper;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    break;
                case R.id.navigation_more:
                    Intent i = new Intent(CustomerDashboarActivity.this, MainActivity.class);
                    i.addFlags(i.FLAG_ACTIVITY_CLEAR_TOP | i.FLAG_ACTIVITY_CLEAR_TASK |i.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);

                    break;
            }
            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_dashboar);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setSelectedItemId(R.id.navigation_home);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        session = new SessionManagement(getApplicationContext());
        session.checkLogin();
        HashMap<String, String> user = session.getUserDetails();

        String empName = user.get(SessionManagement.KEY_UserName);
        String cCOde = user.get(SessionManagement.KEY_LoginName);
        String address = user.get(SessionManagement.Extra);
        String cell = user.get(SessionManagement.CustomerCell);

        customerName = (TextView) findViewById(R.id.customername) ;
        adress = (TextView) findViewById(R.id.customeraddress) ;
        customerCode = (TextView) findViewById(R.id.customerCode) ;
        mobileNo = (TextView) findViewById(R.id.mobileNo) ;

        customerName.setText(empName);
        adress.setText(address);
        customerCode.setText(cCOde);
        mobileNo.setText(cell);


        productSQLiteHelper = new ProductSQLiteHelper(getApplicationContext());
        presenter = new ProductPresenter(this,this);
        if(productSQLiteHelper.CheckDataExistinTable() != true){
            presenter.getProductsFromServer(151);
        }






        Button orderTracking = (Button)findViewById(R.id.orderTracking);
        Button orderBtn = (Button)findViewById(R.id.orderBtn);
        orderTracking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CustomerDashboarActivity.this,CustomerOrderListActivity.class);
//                i.addFlags(i.FLAG_ACTIVITY_CLEAR_TOP | i.FLAG_ACTIVITY_CLEAR_TASK |i.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        orderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                Intent i = new Intent(CustomerDashboarActivity.this, OrderMainActivity.class);
                Customer aInfoData = new Customer();
                aInfoData.setCustomerName(empName);
                aInfoData.setCustomerCode(cCOde);
                aInfoData.setAddress(address);
                Gson gson = new Gson();
                String myJson = gson.toJson(aInfoData);
                i.putExtra("myjson", myJson);
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
                        finish();
                    }

                })
                .setNegativeButton("No", null)
                .show();
    }


    @Override
    public void OnError(String message) {

    }

    @Override
    public void onProductsGet(List<Product> aList) {

    }

    @Override
    public void onProductSampleGet(List<ProductSample> aList) {

    }
}