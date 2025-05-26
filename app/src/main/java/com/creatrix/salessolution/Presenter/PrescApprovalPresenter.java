package com.creatrix.salessolution.Presenter;

import android.content.Context;

import androidx.annotation.NonNull;

import com.creatrix.salessolution.Activity.Approval.Prescription.PrescApprovalData;
import com.creatrix.salessolution.Activity.Approval.Prescription.PrescApprovalRQ;
import com.creatrix.salessolution.DBAdapter.DBCrudHelper;
import com.creatrix.salessolution.DBInfo.DBHelperMain;
import com.creatrix.salessolution.Interface.IPrescApproval;
import com.creatrix.salessolution.Model.ResultInfo;
import com.creatrix.salessolution.Network.ApiDoctorCall;
import com.creatrix.salessolution.Network.RetrofitClientInstance;

import java.net.SocketTimeoutException;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PrescApprovalPresenter implements IPrescApproval.Presenter {
    IPrescApproval.View view;
    Context context;
    DBHelperMain dbHelperMain;
    DBCrudHelper dbCrudHelper;


    public PrescApprovalPresenter(IPrescApproval.View view, Context context) {
        this.view = view;
        this.context = context;
        dbHelperMain=new DBHelperMain(context);
        dbCrudHelper=new DBCrudHelper(context);
    }


    @Override
    public void getPrescApprovalList(String pram, Map<String, String> mapparam) {
        ApiDoctorCall service = RetrofitClientInstance.getRetrofitInstance().create(ApiDoctorCall.class);
        Call<List<PrescApprovalData>> calls = service.GetPrescListApproval(pram,mapparam);
       /* Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try  {
                    try {
                        List<PrescApprovalData> data=calls.execute().body();
                        if(data!=null)
                        {
                            //List<PrescApprovalData> data= response.body();
                            view.onGetPrescApprovalList(data);
                        }
                        else {
                            view.onError("Prescription Not Found");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();*/
        calls.enqueue(new Callback<List<PrescApprovalData>>() {
            @Override
            public void onResponse(@NonNull Call<List<PrescApprovalData>> call, @NonNull Response<List<PrescApprovalData>> response) {
                if(response!=null)
                {
                    view.onGetPrescApprovalList(response.body());
                   /* List<PrescApprovalData> data= response.body();
                    boolean isInserted=dbCrudHelper.InsertPrescImg_SQLite(data);
                    if(isInserted)
                    {
                        view.onGetPrescApprovalList(data);
                    }*/
                }
                else {
                    view.onError("Prescription Not Found");
                }
            }
            @Override
            public void onFailure(@NonNull Call<List<PrescApprovalData>> call, @NonNull Throwable t) {
                if (t instanceof SocketTimeoutException) {
                    view.onError("Time Out");
                } else {
                }
            }
        });
    }
    @Override
    public void SavePrescApproval(PrescApprovalRQ aInfo) {
        try {
            ApiDoctorCall service = RetrofitClientInstance.getRetrofitInstance().create(ApiDoctorCall.class);
            Call<ResultInfo> call = service.SavePrescApproval(aInfo);
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
           // exception.printStackTrace();
        }
    }

}
