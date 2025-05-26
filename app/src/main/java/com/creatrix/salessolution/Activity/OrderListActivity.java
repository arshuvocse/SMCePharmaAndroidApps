package com.creatrix.salessolution.Activity;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;


import com.creatrix.salessolution.DBAdapter.DBCrudHelper;
import com.creatrix.salessolution.Interface.IOrderManagement;
import com.creatrix.salessolution.Interface.RecyclerViewActionListener;
import com.creatrix.salessolution.Model.OrderMaster;
import com.creatrix.salessolution.Presenter.OrderManagementPresenter;
import com.creatrix.salessolution.R;
import com.creatrix.salessolution.RecyclerAdapter.ExpandableRecyclerViewAdapter;

import java.util.ArrayList;

public class OrderListActivity extends AppCompatActivity implements RecyclerViewActionListener, IOrderManagement.View {

    ArrayList<OrderMaster> orderMasterArrayList = new ArrayList<>();

    DBCrudHelper dbCrudHelper;
    RecyclerView expanderRecyclerView;
    IOrderManagement.Presenter orderPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_custom);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(OrderListActivity.this,MainDashboardActivity.class);
                overridePendingTransition(0, 0);
                startActivity(i);
                overridePendingTransition(0,0);
            }
        });



        dbCrudHelper = new DBCrudHelper(getApplicationContext());


        if (bundle !=null){
            String customerCode="";
            customerCode  = bundle.getString("customerCode");
            orderMasterArrayList = dbCrudHelper.getOrderListFromSQLiteForSingleCustomer(customerCode);
        }else{
            orderMasterArrayList = dbCrudHelper.getOrderListFromSQLite();
        }

        orderPresenter = new OrderManagementPresenter(this,this);




        String str ="Test";

        if(orderMasterArrayList.size() > 0){

            expanderRecyclerView = findViewById(R.id.recycler_view);

            ExpandableRecyclerViewAdapter expandableCategoryRecyclerViewAdapter =
                    new ExpandableRecyclerViewAdapter(getApplicationContext(), orderMasterArrayList,OrderListActivity.this,this);

            expanderRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

            expanderRecyclerView.setAdapter(expandableCategoryRecyclerViewAdapter);

//            recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
//            mAdapter = new _orderList_Recycler_Adapter(orderMasterArrayList);
//            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
//            recyclerView.setLayoutManager(mLayoutManager);
//            recyclerView.setItemAnimator(new DefaultItemAnimator());
//            recyclerView.setAdapter(mAdapter);
//            recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(),
//                    DividerItemDecoration.VERTICAL));
//            recyclerView.setItemAnimator(null);
//            recyclerView.scrollToPosition(0);
//            mAdapter.notifyDataSetChanged();
        }


    }

    @Override
    public void onEditTextFocusChange(int position, int value) {

    }

    @Override
    public boolean onLongClick(int position) {
        return false;
    }

    @Override
    public void OrderSync(OrderMaster orderMaster) {
        orderPresenter.SyncOrder(orderMaster,orderMaster.getOrderIdLocal());
    }

    @Override
    public void onOrderSuccess(String message,String Who) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ((AlertDialog)dialog).getButton(which).setVisibility(View.INVISIBLE);
                        Intent i = new Intent(OrderListActivity.this,OrderListActivity.class);
                        startActivity(i);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    }
                }).setCancelable(false).show();

    }

    @Override
    public void onOrderDraftSuccess(String message) {

    }

    @Override
    public void onOrderError(String message) {

    }

    @Override
    public void onGenericSuccess(String message) {

    }

    @Override
    public void onGenericError(String message) {

    }


    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left,
                R.anim.slide_out_right);
    }
}