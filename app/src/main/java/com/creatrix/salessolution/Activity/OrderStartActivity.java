package com.creatrix.salessolution.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Pair;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.creatrix.salessolution.Model.Customer;
import com.creatrix.salessolution.R;
import com.creatrix.salessolution.databinding.ActivityMainBinding;
import com.creatrix.salessolution.databinding.ActivityOrderStartBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;


public class OrderStartActivity extends AppCompatActivity {

    LinearLayout addOrderClick;
    ActivityOrderStartBinding viewBinding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_order_start);
        viewBinding = ActivityOrderStartBinding.inflate(getLayoutInflater());
        View mainView = viewBinding.getRoot();
        setContentView(mainView);
        Gson gson = new Gson();
        Customer aInfoData = gson.fromJson(getIntent().getStringExtra("myjson"), Customer.class);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_custom);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        setCutomerDetails(aInfoData);
        addOrderClick = findViewById(R.id.addOrderClick);
        addOrderClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(OrderStartActivity.this,OrderMainActivity.class);
                Gson gson = new Gson();
                String myJson = gson.toJson(aInfoData);
                i.putExtra("myjson", myJson);
                Pair[] pair = new Pair[2];
                pair[0] = new Pair<View,String>(viewBinding.profileImage,"imagetrans");
                pair[1] = new Pair<View,String>(viewBinding.customerName,"titletrans");
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptions activityOptions =  ActivityOptions.makeSceneTransitionAnimation(OrderStartActivity.this,pair);
                    startActivity(i,activityOptions.toBundle());
                }else{
                    startActivity(i);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }
            }


        });



        /// order List click

        viewBinding.orderListClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(OrderStartActivity.this,OrderListActivity.class);
                i.putExtra("customerCode", aInfoData.getCustomerCode());
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

            }
        });


        //mobile number click dialpad open

        viewBinding.customercelltxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + viewBinding.customercelltxt.getText().toString()));
                startActivity(intent);
            }
        });
    }


    // setting customer details
    public void setCutomerDetails(Customer aCustomer){
        viewBinding.customerName.setText(aCustomer.getCustomerName());
        viewBinding.customerCodeTxt.setText(aCustomer.getCustomerCode());
        viewBinding.customerAdressTxt.setText(aCustomer.getAddress());
        viewBinding.customerTypeTxt.setText(aCustomer.getCustomerType());
        viewBinding.customerSubgroupTxt.setText(aCustomer.getCustomerStation());
        viewBinding.customercelltxt.setText(aCustomer.getCellNo());
        viewBinding.customerBalanceTxt.setText(aCustomer.getBalance());
        viewBinding.customerCreditlimitTxt.setText(aCustomer.getCreditLimit());
        viewBinding.customerMarketTxt.setText(aCustomer.getMarketName());




    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left,
                R.anim.slide_out_right);
    }
}