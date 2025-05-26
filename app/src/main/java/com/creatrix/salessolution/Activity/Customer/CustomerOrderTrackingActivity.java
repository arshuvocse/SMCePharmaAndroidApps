package com.creatrix.salessolution.Activity.Customer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.baoyachi.stepview.HorizontalStepView;
import com.baoyachi.stepview.bean.StepBean;
import com.creatrix.salessolution.Model.OrderMaster;
import com.creatrix.salessolution.Model.OrderViewModel;
import com.creatrix.salessolution.Network.GetDataService;
import com.creatrix.salessolution.Network.RetrofitClientInstance;
import com.creatrix.salessolution.R;
import com.creatrix.salessolution.RecyclerAdapter._order_tracking_poruduct_infoAdapter;
import com.creatrix.salessolution.databinding.ActivityCustomerOrderTrackingBinding;
import com.google.gson.Gson;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomerOrderTrackingActivity extends AppCompatActivity {
    ActivityCustomerOrderTrackingBinding binding;
    ProgressDialog progressDoalog;
    public OrderMaster OrderList = new OrderMaster();
    private _order_tracking_poruduct_infoAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityCustomerOrderTrackingBinding.inflate(getLayoutInflater());
      //  setContentView(R.layout.activity_customer_order_tracking);
        setContentView(binding.getRoot());
        Gson gson = new Gson();
        OrderViewModel aInfoData = gson.fromJson(getIntent().getStringExtra("myjson"), OrderViewModel.class);
        GetOrderTracker(aInfoData.getOrderId());

        binding.toolbarCustom.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private void GetOrderTracker(int orderId){
        progressDoalog = new ProgressDialog(CustomerOrderTrackingActivity.this);
        progressDoalog.setMessage("Order is Loading....");
        progressDoalog.show();
        progressDoalog.setCanceledOnTouchOutside(false);
        try{
            GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
            Call<OrderMaster> call = service.GetOrder_CustomerST(orderId);
            call.enqueue(new Callback<OrderMaster>() {
                @Override
                public void onResponse(Call<OrderMaster> call, Response<OrderMaster> response) {
                    progressDoalog.dismiss();
                    OrderList =  response.body();
                    SetData_InView(OrderList);
                }
                @Override
                public void onFailure(Call<OrderMaster> call, Throwable t) {
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
            TextView order_no_txt = (TextView)findViewById(R.id.order_no_txt);
            TextView customer_name_txt = (TextView)findViewById(R.id.customer_name_txt);


            TextView gross_value_txt = (TextView)findViewById(R.id.gross_value_txt);
            TextView submited_date_txt = (TextView)findViewById(R.id.submited_date_txt);
            TextView mio_name_txt = (TextView)findViewById(R.id.mio_name_txt);


            binding.orderNoTxt.setText(aList.getOrderCode());
            binding.customerNameTxt.setText(aList.getCustomerName());
            binding.grossValueTxt.setText(aList.getGrossValue());
        //    binding.discValueTxt.setText(aList.getOrderDetailsMain().get(0).get);

            String submDate = aList.getSubmDate();
            submited_date_txt.setText(submDate);
            mio_name_txt.setText(aList.getMIOName());
            SetTracker_View(aList);

            mAdapter = new _order_tracking_poruduct_infoAdapter(aList.getOrderDetails());
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            binding.recyclerView.setLayoutManager(mLayoutManager);
            binding.recyclerView.setItemAnimator(new DefaultItemAnimator());
            binding.recyclerView.setAdapter(mAdapter);
            binding.recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(),
                    DividerItemDecoration.VERTICAL));
            binding.recyclerView.setItemAnimator(null);
            binding.recyclerView.scrollToPosition(0);
            mAdapter.notifyDataSetChanged();
        }

    }


    public void SetTracker_View(OrderMaster aList){

        HorizontalStepView setpview5 = (HorizontalStepView) findViewById(R.id.step_view);
        List<StepBean> source = new ArrayList<>();
//        int pending = aList.getIsPending();
//        int confirm = aList.getIsConfirm();
//        int inc = aList.getIsInvoiced();
        int pending = 0;
        int confirm = 0;
        int inc = 0;


        source.add(new StepBean("Pending",pending));
        source.add(new StepBean("Confirmed",confirm));
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
}