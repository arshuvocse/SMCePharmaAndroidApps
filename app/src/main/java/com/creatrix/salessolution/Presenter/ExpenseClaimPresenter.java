package com.creatrix.salessolution.Presenter;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.creatrix.salessolution.Interface.IExpenseClaim;
import com.creatrix.salessolution.Model.ExpenseClaimMaster;
import com.creatrix.salessolution.Model.ExpenseTypeDetails;
import com.creatrix.salessolution.Model.ExpenseTypeMaster;
import com.creatrix.salessolution.Model.ResultInfo;
import com.creatrix.salessolution.Network.CalculationApiCall;
import com.creatrix.salessolution.Network.RetrofitClientInstance;

import java.net.SocketTimeoutException;
import java.util.List;

import okhttp3.HttpUrl;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExpenseClaimPresenter implements IExpenseClaim.Presenter {

    IExpenseClaim.View view;
    Context context;
    public ExpenseClaimPresenter(IExpenseClaim.View view,Context context) {
        this.view = view;
        this.context = context;
    }

    @Override
    public void GetExpenseType(String roleType, String empId) {
        try{
            CalculationApiCall service = RetrofitClientInstance.getRetrofitInstance().create(CalculationApiCall.class);
            Call<List<ExpenseTypeMaster>> call = service.Get_ExpenseType(roleType,empId);
            HttpUrl ds = call.request().url();
            call.enqueue(new Callback<List<ExpenseTypeMaster>>() {
                @Override
                public void onResponse(@NonNull Call<List<ExpenseTypeMaster>> call, @NonNull Response<List<ExpenseTypeMaster>> response) {
                    view.onExpenseTypeGet(response.body());

                }
                @Override
                public void onFailure(@NonNull Call<List<ExpenseTypeMaster>> call, @NonNull Throwable t) {
                    if(t instanceof SocketTimeoutException){
                        Log.e("TAG", "onFailure: ", t);
                    }else{
                        Log.e("TAG", "onFailure: ", t);
                    }
                }
            });

        }catch (Exception ex){
           // Log.e("TAG", "onFailure: ", ex);
        }
    }

    @Override
    public void GetExpenseDetails(int id) {

        try{
            CalculationApiCall service = RetrofitClientInstance.getRetrofitInstance().create(CalculationApiCall.class);
            Call<List<ExpenseTypeDetails>> call = service.Get_ExpenseTypeDetails(id);
            HttpUrl ds = call.request().url();
            call.enqueue(new Callback<List<ExpenseTypeDetails>>() {
                @Override
                public void onResponse(@NonNull Call<List<ExpenseTypeDetails>> call, @NonNull Response<List<ExpenseTypeDetails>> response) {
                    view.onExpenseTypeDetailsGet(response.body());
                }
                @Override
                public void onFailure(@NonNull Call<List<ExpenseTypeDetails>> call, @NonNull Throwable t) {
                    if(t instanceof SocketTimeoutException){
                        Log.e("TAG", "onFailure: ", t);
                    }else{
                        Log.e("TAG", "onFailure: ", t);
                    }
                }
            });

        }catch (Exception ex){
           // Log.e("TAG", "onFailure: ", ex);
        }

    }

    @Override
    public void SaveExpenseClaim(ExpenseClaimMaster aInfo) {
        ProgressDialog progressDoalog = new ProgressDialog(context);
        progressDoalog.setMessage("Saving.... Please wait");
        progressDoalog.show();
        progressDoalog.setCanceledOnTouchOutside(false);
        try{
            CalculationApiCall service = RetrofitClientInstance.getRetrofitInstance().create(CalculationApiCall.class);
            Call<ResultInfo> call = service.SaveExpenseClaim(aInfo);
            HttpUrl ds = call.request().url();
            call.enqueue(new Callback<ResultInfo>() {
                @Override
                public void onResponse(@NonNull Call<ResultInfo> call, @NonNull Response<ResultInfo> response) {
                    progressDoalog.dismiss();
                    ResultInfo info =response.body();
                    if(info !=null){
                        if(info.getSuccess() == true){
                            view.onSaveSuccess("Expense Claim Successfully Submitted");
                        }
                        else if(info.getValiCheck()==true)
                        {
                            view.onSaveError("You Already Claim Your Expense");
                        }
                        /*else{
                            view.onSaveError("Some error occurred... Please try again");
                        }*/
                    }else{
                        view.onSaveError("Some error occurred... Please try again");

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
          /*  progressDoalog.dismiss();
            String str = ex.toString();
            Log.e("Exception",str);*/
            view.onSaveError("Some error occurred..Please try again");

        }
    }
}
