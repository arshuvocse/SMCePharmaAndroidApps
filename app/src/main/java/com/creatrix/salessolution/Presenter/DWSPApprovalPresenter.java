package com.creatrix.salessolution.Presenter;

import android.content.Context;

import androidx.annotation.NonNull;

import com.creatrix.salessolution.Activity.Approval.DWSP.Model.DWSPApprovalData;
import com.creatrix.salessolution.Activity.Approval.TourPlan.Model.TPApprovalSaveBody;
import com.creatrix.salessolution.Activity.Approval.TourPlan.Model.TourPlanApprovalData;
import com.creatrix.salessolution.Activity.DWSP.Model.DWSPApprovalSaveBody;
import com.creatrix.salessolution.Interface.IDWSPApproval;
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

public class DWSPApprovalPresenter implements IDWSPApproval.Presenter {
    IDWSPApproval.View view;
    Context context;
    public DWSPApprovalPresenter(IDWSPApproval.View view, Context context) {
        this.view = view;
        this.context = context;
    }

    @Override
    public void getDWSPApprovalList(String pram, Map<String, String> mapparam) {
        TourApiCall service = RetrofitClientInstance.getRetrofitInstance().create(TourApiCall.class);
        Call<DWSPApprovalData> calls = service.GetDWSPApproval(pram,mapparam);
        calls.enqueue(new Callback<DWSPApprovalData>() {
            @Override
            public void onResponse(@NonNull Call<DWSPApprovalData> call, @NonNull Response<DWSPApprovalData> response) {
                if(response.body()!=null)
                {
                    DWSPApprovalData data= response.body();
                    view.onGetDWSPApprovalList(data);
                }
                else {
                    view.onError("No Data Found");
                }
            }

            @Override
            public void onFailure(@NonNull Call <DWSPApprovalData> call, @NonNull Throwable t) {

            }
        });
    }

    @Override
    public void SaveDWSPApproval(DWSPApprovalSaveBody aInfo) {
        try {
            TourApiCall service = RetrofitClientInstance.getRetrofitInstance().create(TourApiCall.class);
            Call<ResultInfo> call = service.SaveDWSPApproval(aInfo);
            call.enqueue(new Callback<ResultInfo>() {
                @Override
                public void onResponse(@NonNull Call<ResultInfo> call, @NonNull Response<ResultInfo> response) {
                    if(response!=null && response.body().isSuccess)
                    {
                        view.onSaveSuccess("Action");
                    }
                    else {
                        view.onError("Not Verified");
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

