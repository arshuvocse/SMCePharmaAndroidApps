package com.creatrix.salessolution.Presenter;

import android.content.Context;


import com.creatrix.salessolution.Activity.Approval.VisitPlan.Model.VPApprovalSaveBody;
import com.creatrix.salessolution.Activity.Approval.VisitPlan.Model.VisitPlanApprovalData;
import com.creatrix.salessolution.Interface.IVPApproval;
import com.creatrix.salessolution.Model.ResultInfo;
import com.creatrix.salessolution.Network.ApiDoctorCall;
import com.creatrix.salessolution.Network.RetrofitClientInstance;


import java.net.SocketTimeoutException;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VPApprovalPresenter implements IVPApproval.Presenter {
    IVPApproval.View view;
    Context context;
    public VPApprovalPresenter(IVPApproval.View view, Context context) {
        this.view = view;
        this.context = context;
    }
    @Override
    public void getVPApprovalList(String pram, Map<String, String> mapparam) {
       // Toast.makeText(context, "param : "+mapparam, Toast.LENGTH_SHORT).show();
        ApiDoctorCall service = RetrofitClientInstance.getRetrofitInstance().create(ApiDoctorCall.class);
        Call<List<VisitPlanApprovalData>> calls = service.GetVPApproval(pram, mapparam);
        calls.enqueue(new Callback<List<VisitPlanApprovalData>>() {
            @Override
            public void onResponse(Call<List<VisitPlanApprovalData>> call, Response<List<VisitPlanApprovalData>> response) {
                if(response!=null && response.body().size()>0)
                {
                    List<VisitPlanApprovalData> data= response.body();
                    view.onGetVPApprovalList(data);
                }
                else {
                    view.onError("No Visit Plan Found");
                }
            }

            @Override
            public void onFailure(Call<List<VisitPlanApprovalData>> call, Throwable t) {

            }
        });
    }


    @Override
    public void SaveVPApproval(VPApprovalSaveBody aInfo) {
        try {
            ApiDoctorCall service = RetrofitClientInstance.getRetrofitInstance().create(ApiDoctorCall.class);
            Call<ResultInfo> call = service.SaveVPApproval(aInfo);
            call.enqueue(new Callback<ResultInfo>() {
                @Override
                public void onResponse(Call<ResultInfo> call, Response<ResultInfo> response) {
                    if(response!=null&&response.body().isSuccess)
                    {
                        view.onSaveSuccess("Verified");
                    }
                    else {
                        view.onSaveSuccess("Not Verified");
                    }
                }

                @Override
                public void onFailure(Call<ResultInfo> call, Throwable t) {
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

