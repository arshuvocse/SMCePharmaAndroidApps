package com.creatrix.salessolution.Activity.Doctor.Approval;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.creatrix.salessolution.Activity.Doctor.Approval.Model.DoctorApprovalList;
import com.creatrix.salessolution.Activity.Doctor.Approval.Model.DoctorApprovalRQ;
import com.creatrix.salessolution.DBAdapter.DBDoctor.DBDoctorHelper;
import com.creatrix.salessolution.DBAdapter.SyncDb_Helper;
import com.creatrix.salessolution.Model.ResultInfo;
import com.creatrix.salessolution.Network.ApiDoctorCall;
import com.creatrix.salessolution.Network.RetrofitClientInstance;
import com.creatrix.salessolution.UtilityHelper.SnackBarManagement;

import java.net.SocketTimeoutException;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DoctorApprovalPresenter implements IDoctorApproval.Presenter {
    IDoctorApproval.View view;
    Context context;
    DBDoctorHelper dbDoctorHelper;
    SyncDb_Helper syncDbHelper;

    public DoctorApprovalPresenter(IDoctorApproval.View view, Context context) {
        this.view = view;
        this.context = context;
        dbDoctorHelper = new DBDoctorHelper(context);
        syncDbHelper = new SyncDb_Helper(context);
    }

    @Override
    public void GetDoctorApprovalList(String param, Map<String, String> filter, ConstraintLayout layout) {
        try {
            ApiDoctorCall service = RetrofitClientInstance.getRetrofitInstance().create(ApiDoctorCall.class);
            Call<List<DoctorApprovalList>> call = service.GetDoctorApproval(param,filter);
            call.enqueue(new Callback<List<DoctorApprovalList>>() {
                @Override
                public void onResponse(@NonNull Call<List<DoctorApprovalList>> call, @NonNull Response<List<DoctorApprovalList>> response) {

                    if(response!=null)
                    {
                        view.OnRevieveDoctorApproval(response.body());
                    }
                    else {
                        SnackBarManagement._error_CustomMessage(layout,"NO Data");
                    }

                }

                @Override
                public void onFailure(@NonNull Call<List<DoctorApprovalList>> call, @NonNull Throwable t) {
                    if (t instanceof SocketTimeoutException) {
                    } else {
                    }
                }
            });
        } catch (Exception exception) {
            //exception.printStackTrace();
        }

    }

    @Override
    public void SaveDoctorApprovalList(DoctorApprovalRQ doctorApprovalRQ) {
        try {
            ApiDoctorCall service = RetrofitClientInstance.getRetrofitInstance().create(ApiDoctorCall.class);
            Call<ResultInfo> call = service.SaveDoctorApproval(doctorApprovalRQ);
            call.enqueue(new Callback<ResultInfo>() {
                @Override
                public void onResponse(@NonNull Call<ResultInfo> call, @NonNull Response<ResultInfo> response) {
                     if(response!=null&&response.body().isSuccess)
                     {
                         view.OnSuccess("Verified");
                     }
                     else {
                         view.OnSuccess("Not Verified");
                     }
                }

                @Override
                public void onFailure(@NonNull Call<ResultInfo> call, Throwable t) {
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
