package com.creatrix.salessolution.Presenter;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.creatrix.salessolution.Interface.ILeave;
import com.creatrix.salessolution.Model.ExpenseTypeMaster;
import com.creatrix.salessolution.Model.LeaveSM;
import com.creatrix.salessolution.Model.LeaveTypeInfo;
import com.creatrix.salessolution.Model.LeaveVM;
import com.creatrix.salessolution.Model.ResultInfo;
import com.creatrix.salessolution.Network.CalculationApiCall;
import com.creatrix.salessolution.Network.RetrofitClientInstance;
import com.creatrix.salessolution.Network.UserProcessAPI;

import java.net.SocketTimeoutException;
import java.util.List;

import okhttp3.HttpUrl;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LeavePresenter implements ILeave.Presenter {
    ILeave.View view;
    Context context;


    public LeavePresenter(ILeave.View view, Context context) {
        this.view = view;
        this.context = context;
    }

    @Override
    public void GetLeaveTyep(int empId, int year) {
        try{
            UserProcessAPI service = RetrofitClientInstance.getRetrofitInstance().create(UserProcessAPI.class);
            Call<List<LeaveTypeInfo>> call = service.GetLeaveType(empId,year);
            HttpUrl ds = call.request().url();
            call.enqueue(new Callback<List<LeaveTypeInfo>>() {
                @Override
                public void onResponse(@NonNull Call<List<LeaveTypeInfo>> call, @NonNull Response<List<LeaveTypeInfo>> response) {
                    view.onLeaveTypeGet(response.body());

                }
                @Override
                public void onFailure(@NonNull Call<List<LeaveTypeInfo>> call, @NonNull Throwable t) {
                    if(t instanceof SocketTimeoutException){
                        Log.e("TAG", "onFailure: ", t);
                    }else{
                        Log.e("TAG", "onFailure: ", t);
                    }
                }
            });

        }catch (Exception ex){
            Log.e("TAG", "onFailure: ", ex);
        }
    }

    @Override
    public void SaveLeave(LeaveSM aInfo) {
        ProgressDialog progressDoalog = new ProgressDialog(context);
        progressDoalog.setMessage("Saving.... Please wait");
        progressDoalog.show();
        progressDoalog.setCanceledOnTouchOutside(false);
        try{
            UserProcessAPI service = RetrofitClientInstance.getRetrofitInstance().create(UserProcessAPI.class);
            Call<ResultInfo> call = service.SaveLeave(aInfo);
            call.enqueue(new Callback<ResultInfo>() {
                @Override
                public void onResponse(@NonNull Call<ResultInfo> call, @NonNull Response<ResultInfo> response) {
                    progressDoalog.dismiss();
                    ResultInfo info =response.body();
                    if(info !=null){
                        if(info.getSuccess()){
                            view.onSaveSuccess("Leave Successfully Submitted "+info.getMsd());
                        }else{
                            view.onSaveError(info.getErrorMessage());
                        }
                    }else{
                        view.onSaveError(info.getMsd());

                    }
                }
                @Override
                public void onFailure(@NonNull Call<ResultInfo> call, @NonNull Throwable t) {
                    progressDoalog.dismiss();
                    if(t instanceof SocketTimeoutException){
                        view.onSaveError("Slow Internet Detected..Please try again");
                    }else{
                        view.onSaveError("Some error occurred..Please try again");
                    }
                }
            });

        }catch (Exception ex){
            progressDoalog.dismiss();
            String str = ex.toString();
            Log.e("Exception",str);
            view.onSaveError("Some error occurred..Please try again");

        }

    }

    @Override
    public void GetLeaveRecords(int empId,String Month, int year) {
        try{
            UserProcessAPI service = RetrofitClientInstance.getRetrofitInstance().create(UserProcessAPI.class);
            Call<List<LeaveVM>> call = service.GetLeaveRecords(empId,Month,year);
            HttpUrl ds = call.request().url();
            call.enqueue(new Callback<List<LeaveVM>>() {
                @Override
                public void onResponse(Call<List<LeaveVM>> call, Response<List<LeaveVM>> response) {
                    view.onLeaveRecordsGet(response.body());
                }
                @Override
                public void onFailure(Call<List<LeaveVM>> call, Throwable t) {
                    if(t instanceof SocketTimeoutException){
                        Log.e("TAG", "onFailure: ", t);
                    }else{
                        Log.e("TAG", "onFailure: ", t);
                    }
                }
            });

        }catch (Exception ex){
            Log.e("TAG", "onFailure: ", ex);
        }

    }
}
