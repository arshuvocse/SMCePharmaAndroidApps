package com.creatrix.salessolution.Presenter;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.creatrix.salessolution.Activity.Approval.DCR.DcrApprovalData;
import com.creatrix.salessolution.Activity.Approval.DCR.DcrApprovalRQ;
import com.creatrix.salessolution.Activity.Approval.Order.OrderApprovalSaveBody;
import com.creatrix.salessolution.Interface.IDcrApproval;
import com.creatrix.salessolution.Model.ResultInfo;
import com.creatrix.salessolution.Network.ApiDoctorCall;
import com.creatrix.salessolution.Network.RetrofitClientInstance;

import java.net.SocketTimeoutException;
import java.util.List;
import java.util.Map;

import okhttp3.HttpUrl;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DcrApprovalPresenter implements IDcrApproval.Presenter {
    IDcrApproval.View view;
    Context context;


    public DcrApprovalPresenter(IDcrApproval.View view, Context context) {
        this.view = view;
        this.context = context;
    }


    @Override
    public void getDcrApprovalList(String pram, Map<String, String> mapparam) {

        ApiDoctorCall service = RetrofitClientInstance.getRetrofitInstance().create(ApiDoctorCall.class);
        Call<List<DcrApprovalData>> calls = service.GetDcrListApproval(pram,mapparam);
        calls.enqueue(new Callback<List<DcrApprovalData>>() {
            @Override
            public void onResponse(@NonNull Call<List<DcrApprovalData>> call, @NonNull Response<List<DcrApprovalData>> response) {
                if(response!=null)
                {
                    List<DcrApprovalData> data= response.body();
                    view.onGetDcrApprovalList(data);
                }
                else {
                    view.onError("DCR Not Found");
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<DcrApprovalData>> call, @NonNull Throwable t) {

            }
        });

    }

    @Override
    public void SaveDcrApproval(DcrApprovalRQ aInfo) {
        try {
            ApiDoctorCall service = RetrofitClientInstance.getRetrofitInstance().create(ApiDoctorCall.class);
            Call<ResultInfo> call = service.SaveDcrApproval(aInfo);
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
