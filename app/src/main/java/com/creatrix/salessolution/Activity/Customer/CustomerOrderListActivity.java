package com.creatrix.salessolution.Activity.Customer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;

import com.creatrix.salessolution.Model.OrderViewModel;
import com.creatrix.salessolution.Network.GetDataService;
import com.creatrix.salessolution.Network.RetrofitClientInstance;
import com.creatrix.salessolution.R;
import com.creatrix.salessolution.RecyclerAdapter._customer_Order_ListAdapter;
import com.creatrix.salessolution.UtilityHelper.SessionManagement;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomerOrderListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    ProgressDialog progressDoalog;
    private _customer_Order_ListAdapter mAdapter;
    public List<OrderViewModel> OrderList = new ArrayList<>();
    SessionManagement session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_order_list);
        session = new SessionManagement(getApplicationContext());

        HashMap<String, String> user = session.getUserDetails();

        String empName = user.get(SessionManagement.KEY_UserName);
        String cCOde = user.get(SessionManagement.KEY_LoginName);
        getCustomerList(cCOde);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_custom);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    private void getCustomerList(String customerCode){

        progressDoalog = new ProgressDialog(CustomerOrderListActivity.this);
        progressDoalog.setMessage("Order is Loading....");
        progressDoalog.show();
        progressDoalog.setCanceledOnTouchOutside(false);
        List<OrderViewModel> aCusList = new ArrayList<>();
        try{
            GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
            Call<List<OrderViewModel>> call = service.GetCustomer_OrderList(customerCode);

            call.enqueue(new Callback<List<OrderViewModel>>() {
                @Override
                public void onResponse(Call<List<OrderViewModel>> call, Response<List<OrderViewModel>> response) {
                    progressDoalog.dismiss();
                    OrderList =  response.body();
                    LoadRecycler(OrderList);
                }
                @Override
                public void onFailure(Call<List<OrderViewModel>> call, Throwable t) {
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


    public void LoadRecycler(List<OrderViewModel> aList) {

        if(aList !=null){
            recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
            mAdapter = new _customer_Order_ListAdapter(aList);
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


}