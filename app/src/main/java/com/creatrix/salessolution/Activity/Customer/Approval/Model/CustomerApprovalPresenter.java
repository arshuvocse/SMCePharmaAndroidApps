package com.creatrix.salessolution.Activity.Customer.Approval.Model;

import android.app.ProgressDialog;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.creatrix.salessolution.DBAdapter.DBDoctor.DBDoctorHelper;
import com.creatrix.salessolution.DBAdapter.SyncDb_Helper;
import com.creatrix.salessolution.Model.ResultInfo;
import com.creatrix.salessolution.Network.ApiCustomerCall;
import com.creatrix.salessolution.Network.RetrofitClientInstance;
import com.creatrix.salessolution.UtilityHelper.SnackBarManagement;

import java.net.SocketTimeoutException;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomerApprovalPresenter implements ICustomerApproval.Presenter {
    ICustomerApproval.View view;
    Context context;
    DBDoctorHelper dbDoctorHelper;
    SyncDb_Helper syncDbHelper;

    public CustomerApprovalPresenter(ICustomerApproval.View view, Context context) {
        this.view = view;
        this.context = context;
        dbDoctorHelper = new DBDoctorHelper(context);
        syncDbHelper = new SyncDb_Helper(context);
    }


    @Override
    public void GetCustomerApprovalList(String param, Map<String, String> filter, ConstraintLayout layout) {
        try {

            ApiCustomerCall service = RetrofitClientInstance.getRetrofitInstance().create(ApiCustomerCall.class);
            Call<List<CustomerApprovalList>> call = service.GetCustomerApproval(param,filter);
            call.enqueue(new Callback<List<CustomerApprovalList>>() {
                @Override
                public void onResponse(@NonNull Call<List<CustomerApprovalList>> call, @NonNull Response<List<CustomerApprovalList>> response) {
                    if(response!=null)
                    {
                        view.OnRevieveCustomerApproval(response.body());
                    }
                    else {
                        view.OnError(1);
                    }

                }

                @Override
                public void onFailure(@NonNull Call<List<CustomerApprovalList>> call, @NonNull Throwable t) {
                    if (t instanceof SocketTimeoutException) {
                        view.OnError(2);
                    } else {
                        view.OnError(2);
                    }
                }
            });
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void SaveCustomerApprovalList(CustomerApprovalRQ customerApprovalRQ) {
        try {
            ProgressDialog progressDoalog = new ProgressDialog(context);
            progressDoalog.setMessage("Submitting...");
            progressDoalog.show();
            progressDoalog.setCanceledOnTouchOutside(false);

            ApiCustomerCall service = RetrofitClientInstance.getRetrofitInstance().create(ApiCustomerCall.class);
            Call<ResultInfo> call = service.SaveCustomerApproval(customerApprovalRQ);
            call.enqueue(new Callback<ResultInfo>() {
                @Override
                public void onResponse(@NonNull Call<ResultInfo> call, @NonNull Response<ResultInfo> response) {
                    if(response.body().isSuccess)
                    {
                        progressDoalog.dismiss();
                        view.OnSuccess("Verified");
                    }
                    else {
                        view.OnSuccess("Not Verified");
                        progressDoalog.dismiss();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ResultInfo> call, @NonNull Throwable t) {
                    if (t instanceof SocketTimeoutException) {
                        progressDoalog.dismiss();
                    } else {
                        progressDoalog.dismiss();
                    }
                }
            });
        } catch (Exception exception) {
            //exception.printStackTrace();
        }
    }
}
