package com.creatrix.salessolution.Presenter;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.creatrix.salessolution.Activity.Approval.Order.OrderApprovalData;
import com.creatrix.salessolution.Activity.Approval.Order.OrderApprovalSaveBody;
import com.creatrix.salessolution.Interface.IOrderApproval;
import com.creatrix.salessolution.Model.ExpenseClaimMaster;
import com.creatrix.salessolution.Model.LeaveTypeInfo;
import com.creatrix.salessolution.Model.ResultInfo;
import com.creatrix.salessolution.Network.CalculationApiCall;
import com.creatrix.salessolution.Network.OrderProcessAPICALL;
import com.creatrix.salessolution.Network.RetrofitClientInstance;
import com.creatrix.salessolution.Network.UserProcessAPI;

import java.net.SocketTimeoutException;
import java.util.List;
import java.util.Map;

import okhttp3.HttpUrl;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderApprovalPresenter implements IOrderApproval.Presenter {
    IOrderApproval.View view;
    Context context;


    public OrderApprovalPresenter(IOrderApproval.View view, Context context) {
        this.view = view;
        this.context = context;
    }

    @Override
    public void getOrderApprovalList(String pram, Map<String, String> mapparam) {

        OrderProcessAPICALL service = RetrofitClientInstance.getRetrofitInstance().create(OrderProcessAPICALL.class);
        Call<List<OrderApprovalData>> call = service.GetOrderApproval(pram,mapparam);
        HttpUrl ds = call.request().url();
        call.enqueue(new Callback<List<OrderApprovalData>>() {
            @Override
            public void onResponse(@NonNull Call<List<OrderApprovalData>> call, @NonNull Response<List<OrderApprovalData>> response) {
                if(response!=null)
                {
                    view.onGetOrderSuccess(response.body());
                }
                else {
                    view.onError("Order Not Found");
                }


            }
            @Override
            public void onFailure(@NonNull Call<List<OrderApprovalData>> call, @NonNull Throwable t) {
                if(t instanceof SocketTimeoutException){
                    Toast.makeText(context, "Outside ", Toast.LENGTH_SHORT).show();
                    Log.e("TAG", "onFailure: ", t);
                }else{
                    Log.e("TAG", "onFailure: ", t);
                }
            }
        });

     /*   try{
            OrderProcessAPICALL service = RetrofitClientInstance.getRetrofitInstance().create(OrderProcessAPICALL.class);
            Call<List<OrderApprovalData>> call = service.GetOrderApproval(pram,mapparam);
            HttpUrl ds = call.request().url();
            call.enqueue(new Callback<List<OrderApprovalData>>() {
                @Override
                public void onResponse(Call<List<OrderApprovalData>> call, Response<List<OrderApprovalData>> response) {
                    if(response!=null)
                    {
                        view.onGetOrderSuccess(response.body());
                    }
                    else {
                        view.onError("Order Not Found");
                        Toast.makeText(context, "nnn", Toast.LENGTH_SHORT).show();
                    }


                }
                @Override
                public void onFailure(Call<List<OrderApprovalData>> call, Throwable t) {
                    if(t instanceof SocketTimeoutException){
                        Log.e("TAG", "onFailure: ", t);
                    }else{
                        Log.e("TAG", "onFailure: ", t);
                    }
                }
            });

        }catch (Exception ex){
            Log.e("TAG", "onFailure: ", ex);
        }*/
    }

    @Override
    public void SaveOrderApproval(OrderApprovalSaveBody aInfo) {
        try{
            OrderProcessAPICALL service = RetrofitClientInstance.getRetrofitInstance().create(OrderProcessAPICALL.class);
            Call<ResultInfo> call = service.SaveOrderApproval(aInfo);
            HttpUrl ds = call.request().url();
            call.enqueue(new Callback<ResultInfo>() {
                @Override
                public void onResponse(@NonNull Call<ResultInfo> call, @NonNull Response<ResultInfo> response) {
                    if(response.body().isSuccess)
                    {
                        view.onSaveSuccess("Submitted");
                    }
                    else {
                        view.onError("Something went wrong");
                    }


                }
                @Override
                public void onFailure(@NonNull Call<ResultInfo> call, @NonNull Throwable t) {
                    if(t instanceof SocketTimeoutException){
                        //Log.e("TAG", "onFailure: ", t);
                    }else{
                       // Log.e("TAG", "onFailure: ", t);
                    }
                }
            });

        }catch (Exception ex){
            //Log.e("TAG", "onFailure: ", ex);
        }
    }
}
