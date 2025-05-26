package com.creatrix.salessolution.Presenter;

import android.content.Context;

import androidx.annotation.NonNull;

import com.creatrix.salessolution.Activity.Approval.TourPlan.Model.TPApprovalSaveBody;
import com.creatrix.salessolution.Activity.Approval.TourPlan.Model.TourPlanApprovalData;
import com.creatrix.salessolution.Interface.ITPApproval;
import com.creatrix.salessolution.Model.ResultInfo;
import com.creatrix.salessolution.Network.RetrofitClientInstance;
import com.creatrix.salessolution.Network.TourApiCall;

import java.net.SocketTimeoutException;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TPApprovalPresenter implements ITPApproval.Presenter {
    ITPApproval.View view;
    Context context;
    public TPApprovalPresenter(ITPApproval.View view, Context context) {
        this.view = view;
        this.context = context;
    }
    @Override
    public void getTPApprovalList(String pram, Map<String,String> mapparam) {
       // Toast.makeText(context, "param : "+mapparam, Toast.LENGTH_SHORT).show();
        TourApiCall service = RetrofitClientInstance.getRetrofitInstance().create(TourApiCall.class);
        Call<List<TourPlanApprovalData>> calls = service.GetTPApproval(pram, mapparam);
        calls.enqueue(new Callback<List<TourPlanApprovalData>>() {
            @Override
            public void onResponse(@NonNull Call<List<TourPlanApprovalData>> call, @NonNull Response<List<TourPlanApprovalData>> response) {
                if(response!=null && response.body().size()>0)
                {
                    List<TourPlanApprovalData> data= response.body();
                    view.onGetTPApprovalList(data);
                }
                else {
                    view.onError("No Tour Found");
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<TourPlanApprovalData>> call, @NonNull Throwable t) {
                view.onError("No Tour Found");
            }
        });
    }


    @Override
    public void SaveTPApproval(TPApprovalSaveBody aInfo) {
        try {
            TourApiCall service = RetrofitClientInstance.getRetrofitInstance().create(TourApiCall.class);
            Call<ResultInfo> call = service.SaveTPApproval(aInfo);
            call.enqueue(new Callback<ResultInfo>() {
                @Override
                public void onResponse(@NonNull Call<ResultInfo> call, @NonNull Response<ResultInfo> response) {
                    if(response!=null&&response.body().isSuccess)
                    {
                        view.onSaveSuccess("Verified");
                    }
                    else {
                        view.onSaveSuccess("Not Verified");
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ResultInfo> call, @NonNull Throwable t) {
                    if (t instanceof SocketTimeoutException) {
                    } else {
                    }
                }
            });
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}

