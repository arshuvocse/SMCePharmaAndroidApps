package com.creatrix.salessolution.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Toast;

import com.creatrix.salessolution.Model.Product;
import com.creatrix.salessolution.Network.ProductApi;
import com.creatrix.salessolution.Network.RetrofitClientInstance;
import com.creatrix.salessolution.R;
import com.creatrix.salessolution.RecyclerAdapter._productView_RecyclerAdapter;
import com.creatrix.salessolution.UtilityHelper.SessionManagement;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductViewActivity extends AppCompatActivity {
    private _productView_RecyclerAdapter mAdapter;
    ProgressDialog progressDoalog;
    List<Product> aProList = new ArrayList<>();
    EditText searchEd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_custom);
        toolbar.setNavigationOnClickListener(v ->finish());

        SessionManagement session = new SessionManagement(getApplicationContext());
       // session.checkLogin();
        HashMap<String, String> user = session.getUserDetails();
        int empId = Integer.parseInt(user.get(SessionManagement.KEY_EmpId));
        getProductsFromServer(empId);


        searchEd = findViewById(R.id.searchEd);
        searchEd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());

            }
        });
    }


    public void LoadinView(List<Product> aList){
        aProList.clear();
        aProList = aList;
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mAdapter = new _productView_RecyclerAdapter(aProList);
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

    public  void ErrorView(String msg){
        Toast.makeText(ProductViewActivity.this,msg,Toast.LENGTH_LONG).show();
    }

    private void filter(String productName){
        List<Product> arrayList = new ArrayList<>();
        for (Product item : aProList){
            if((item.getProductName().toLowerCase().contains(productName.toLowerCase()) || (item.getProductCode().contains(productName)))){
                arrayList.add(item);
            }
        }

        mAdapter.filterList(arrayList);

    }




    public void getProductsFromServer(int empId) {
        progressDoalog = new ProgressDialog(ProductViewActivity.this);
        progressDoalog.setMessage("Product is loading....");
        progressDoalog.show();
        progressDoalog.setCanceledOnTouchOutside(false);
        try{
            ProductApi service = RetrofitClientInstance.getRetrofitInstance().create(ProductApi.class);
            Call<List<Product>> call = service.GetProductList(empId);
            call.enqueue(new Callback<List<Product>>() {
                @Override
                public void onResponse(@NonNull Call<List<Product>> call, @NonNull Response<List<Product>> response) {
                    progressDoalog.dismiss();
                    LoadinView(response.body());

                }
                @Override
                public void onFailure(@NonNull Call<List<Product>> call, @NonNull Throwable t) {
                    progressDoalog.dismiss();
                    if(t instanceof SocketTimeoutException){
                        ErrorView("Slow Network detected");
                    }else{
                        ErrorView("Some Error occured");

                    }


                }
            });

        }catch (Exception ex){
            ErrorView("Some Error occured");
            progressDoalog.dismiss();
        }

    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left,
                R.anim.slide_out_right);
    }
}