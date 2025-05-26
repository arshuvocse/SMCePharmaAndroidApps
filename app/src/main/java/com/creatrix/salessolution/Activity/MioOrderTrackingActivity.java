package com.creatrix.salessolution.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyachi.stepview.HorizontalStepView;
import com.baoyachi.stepview.bean.StepBean;
import com.creatrix.salessolution.Model.OrderMaster;
import com.creatrix.salessolution.Model.OrderViewModel;
import com.creatrix.salessolution.Model.ResultInfo;
import com.creatrix.salessolution.Network.GetDataService;
import com.creatrix.salessolution.Network.RetrofitClientInstance;
import com.creatrix.salessolution.Network.TourApiCall;
import com.creatrix.salessolution.R;
import com.creatrix.salessolution.RecyclerAdapter._order_tracking_poruduct_infoAdapter;
import com.creatrix.salessolution.UtilityHelper.SessionManagement;
import com.google.gson.Gson;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.HttpUrl;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MioOrderTrackingActivity extends AppCompatActivity {
    ProgressDialog progressDoalog;
    public OrderMaster OrderList = new OrderMaster();
    private RecyclerView recyclerView;
    private _order_tracking_poruduct_infoAdapter mAdapter;
    ImageView attRefresh;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mio_order_tracking);

        int orderIdFromAc = getIntent().getExtras().getInt("myOrderId");
        int notificationId = getIntent().getExtras().getInt("nId");
        String fromPass = getIntent().getExtras().getString("from");


        if(orderIdFromAc > 0){
            GetOrderTracker(orderIdFromAc);

        }


        // This is for updating notification is read
        if(notificationId > 0){
            int readVal = getIntent().getExtras().getInt("readVal");
            if(readVal == 0){
                UpdateNotification(notificationId);
            }


        }


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_custom);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        attRefresh  = findViewById(R.id.attRefresh);
        attRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(orderIdFromAc > 0){
                    GetOrderTracker(orderIdFromAc);

                }
            }
        });

    }

    private void GetOrderTracker(int orderId){

        progressDoalog = new ProgressDialog(MioOrderTrackingActivity.this);
        progressDoalog.setMessage("Order is Loading....");
        progressDoalog.show();
        progressDoalog.setCanceledOnTouchOutside(false);
        try{
            GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
            Call<OrderMaster> call = service.GetOrder_CustomerST(orderId);

            call.enqueue(new Callback<OrderMaster>() {
                @Override
                public void onResponse(@NonNull Call<OrderMaster> call, @NonNull Response<OrderMaster> response) {
                    progressDoalog.dismiss();
                    OrderList =  response.body();
                    SetData_InView(OrderList);
                }
                @Override
                public void onFailure(@NonNull Call<OrderMaster> call, @NonNull Throwable t) {
                    progressDoalog.dismiss();
                    if(t instanceof SocketTimeoutException){

                    }else{

                    }
                }
            });

        }catch (Exception ex){
            progressDoalog.dismiss();

        }

    }
    public void SetData_InView(OrderMaster aList) {
        if(aList !=null){

            TextView hOrderMId = (TextView)findViewById(R.id.hOrderMId);
            TextView order_no_txt = (TextView)findViewById(R.id.order_no_txt);
            TextView customer_name_txt = (TextView)findViewById(R.id.customer_name_txt);
            TextView gross_value_txt = (TextView)findViewById(R.id.gross_value_txt);
            TextView submited_date_txt = (TextView)findViewById(R.id.submited_date_txt);
            TextView mio_name_txt = (TextView)findViewById(R.id.mio_name_txt);
            order_no_txt.setText(aList.getOrderCode());
            customer_name_txt.setText(aList.getCustomerName());
            gross_value_txt.setText(aList.getGrossValue());

            String submDate = aList.getSubmDate();
            submited_date_txt.setText(submDate);
            mio_name_txt.setText(aList.getMIOName());
            hOrderMId.setText(String.valueOf(aList.getOrderId()));


            RelativeLayout btn_section = (RelativeLayout)findViewById(R.id.btn_section);

//            if(aList.getIsFromCustomer().equals("No")){
//
//                btn_section.setVisibility(View.GONE);
//            }else{
//                if(aList.getIsPending()==0){
//                    btn_section.setVisibility(View.VISIBLE);
//                }else{
//                    btn_section.setVisibility(View.GONE);
//                }
//
//            }


            ImageView approve_btn = (ImageView)findViewById(R.id.approve_btn);
            ImageView reject_btn = (ImageView)findViewById(R.id.reject_btn);

            approve_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    AlertDialog.Builder builder1 = new AlertDialog.Builder(MioOrderTrackingActivity.this);
                    builder1.setMessage("Are you sure wants to approve the Order ?");
                    builder1.setCancelable(true);
                    builder1.setPositiveButton(
                            "Ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    ApproveOrder(aList.getOrderId());
                                    dialog.cancel();
                                }
                            });
                    builder1.setNegativeButton(
                            "Cancel",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

                    AlertDialog alert11 = builder1.create();
                    alert11.show();

                }
            });

            reject_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    AlertDialog.Builder builder1 = new AlertDialog.Builder(MioOrderTrackingActivity.this);
                    builder1.setMessage("Are you sure wants to reject the Order ?");
                    builder1.setCancelable(true);
                    builder1.setPositiveButton(
                            "Ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    RejectOrder(aList.getOrderId());

                                    dialog.cancel();
                                }
                            });
                    builder1.setNegativeButton(
                            "Cancel",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                }
            });



             SetTracker_View(aList);








            recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
            mAdapter = new _order_tracking_poruduct_infoAdapter(aList.getOrderDetails());
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(mAdapter);
            recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(),
                    DividerItemDecoration.VERTICAL));
            recyclerView.setItemAnimator(null);
            recyclerView.scrollToPosition(0);
            mAdapter.notifyDataSetChanged();
        }

    }
    public void SetTracker_View(OrderMaster aList){

        HorizontalStepView setpview5 = (HorizontalStepView) findViewById(R.id.step_view);
        List<StepBean> source = new ArrayList<>();
        int pending = aList.getIsPending();
        int confirm = aList.getIsConfirm();
        int inc = aList.getIsInvoiced();
   /*     source.add(new StepBean("Pending",pending));
        source.add(new StepBean("Confirmed",confirm));*/
        source.add(new StepBean("Invoiced",inc));
        source.add(new StepBean("Delivered",aList.getIsDelivered()));
        source.add(new StepBean("Payment",aList.getIsPayment()));

        setpview5.setStepViewTexts(source)
                .setTextSize(10)
                .setStepsViewIndicatorCompletedLineColor(Color.parseColor("#00C853"))//??StepsViewIndicator??????
                .setStepsViewIndicatorUnCompletedLineColor(Color.parseColor("#00C853"))//??StepsViewIndicator???????
                .setStepViewComplectedTextColor(Color.parseColor("#000000"))//??StepsView text??????
                .setStepViewUnComplectedTextColor(Color.parseColor("#000000"))//??StepsView text???????
                .setStepsViewIndicatorCompleteIcon(ContextCompat.getDrawable(this, R.mipmap.ic_completed))//??StepsViewIndicator CompleteIcon
                .setStepsViewIndicatorDefaultIcon(ContextCompat.getDrawable(this, R.drawable.default_icon))//??StepsViewIndicator DefaultIcon
                .setStepsViewIndicatorAttentionIcon(ContextCompat.getDrawable(this, R.drawable.attention));

    }

    public  void ApproveOrder(int orderId){
        progressDoalog = new ProgressDialog(MioOrderTrackingActivity.this);
        progressDoalog.setMessage("Order is Approving....");
        progressDoalog.show();
        progressDoalog.setCanceledOnTouchOutside(false);
        try{
            GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
            Call<ResultInfo> call = service.ApproveOrder_fromMio(orderId);

            call.enqueue(new Callback<ResultInfo>() {
                @Override
                public void onResponse(Call<ResultInfo> call, Response<ResultInfo> response) {
                    progressDoalog.dismiss();
                    ResultInfo aInfo = new ResultInfo();
                    aInfo =  response.body();
                    if(aInfo.isSuccess==true){
                        new AlertDialog.Builder(MioOrderTrackingActivity.this)
                                .setTitle("Order Approved")
                                .setMessage("Approved successfully")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent i = new Intent(MioOrderTrackingActivity.this,MioOrderListActivity.class);
                                        startActivity(i);
                                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                    }

                                }).setCancelable(false).show();

                    }else{
                        Toast.makeText(getApplicationContext(),"Some Error Occurred. Please try again",Toast.LENGTH_LONG).show();
                    }

                }
                @Override
                public void onFailure(Call<ResultInfo> call, Throwable t) {
                    progressDoalog.dismiss();
                    if(t instanceof SocketTimeoutException){

                    }else{

                    }
                }
            });

        }catch (Exception ex){
            progressDoalog.dismiss();

        }

    }

    public  void RejectOrder(int orderId){
        progressDoalog = new ProgressDialog(MioOrderTrackingActivity.this);
        progressDoalog.setMessage("Order is Rejecting....");
        progressDoalog.show();
        progressDoalog.setCanceledOnTouchOutside(false);
        try{
            GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
            Call<ResultInfo> call = service.RejectOrder_fromMio(orderId);

            call.enqueue(new Callback<ResultInfo>() {
                @Override
                public void onResponse(Call<ResultInfo> call, Response<ResultInfo> response) {
                    progressDoalog.dismiss();
                    ResultInfo aInfo = new ResultInfo();
                    aInfo =  response.body();
                    if(aInfo.isSuccess==true){
                        new AlertDialog.Builder(MioOrderTrackingActivity.this)
                                .setTitle("Order Rejected")
                                .setMessage("Rejected successfully")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent i = new Intent(MioOrderTrackingActivity.this,MioOrderListActivity.class);
                                        startActivity(i);
                                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                    }

                                }).setCancelable(false).show();

                    }else{
                        Toast.makeText(getApplicationContext(),"Some Error Occurred. Please try again",Toast.LENGTH_LONG).show();
                    }

                }
                @Override
                public void onFailure(Call<ResultInfo> call, Throwable t) {
                    progressDoalog.dismiss();
                    if(t instanceof SocketTimeoutException){

                    }else{

                    }
                }
            });

        }catch (Exception ex){
            progressDoalog.dismiss();

        }

    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left,
                R.anim.slide_out_right);
    }


    public void UpdateNotification(int id){
        try{
            GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
            Call<ResultInfo> call = service.UpdateNotificationIsRead(id);
            HttpUrl ds = call.request().url();
            call.enqueue(new Callback<ResultInfo>() {
                @Override
                public void onResponse(@NonNull Call<ResultInfo> call, @NonNull Response<ResultInfo> response) {
                    ResultInfo info =response.body();
                    if(info.getSuccess() == true){
                        Log.i("IsreadUpdate", "onResponse: "+info);
                    }
                }
                @Override
                public void onFailure(@NonNull Call<ResultInfo> call, @NonNull Throwable t) {
                    Log.i("IsreadUpdate", "Failur: "+t);
                    if(t instanceof SocketTimeoutException){
                    }else{

                    }
                }
            });

        }catch (Exception ex){
            Log.i("IsreadUpdate", "exception: "+ex);

        }

    }
}