package com.creatrix.salessolution.Presenter;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.creatrix.salessolution.Activity.Expense.Model.ApproveExpRQ;
import com.creatrix.salessolution.Activity.Expense.Model.ExpListTeam;
import com.creatrix.salessolution.Interface.ITeamExpClaim;
import com.creatrix.salessolution.Model.ResultInfo;
import com.creatrix.salessolution.Network.CalculationApiCall;
import com.creatrix.salessolution.Network.RetrofitClientInstance;
import com.creatrix.salessolution.UtilityHelper.NetworkInformation;
import com.google.gson.Gson;

import java.net.SocketTimeoutException;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExpenseClaimTeamPresenter implements ITeamExpClaim.Presenter {

    ITeamExpClaim.View view;
    Context context;

    public ExpenseClaimTeamPresenter(ITeamExpClaim.View view, Context context) {
        this.view = view;
        this.context = context;
    }


    @Override
    public void GetExpenseCTeamList(String param, Map<String, String> filter) {
        if (NetworkInformation.isConnected(context)) {
            try {
                CalculationApiCall service = RetrofitClientInstance.getRetrofitInstance().create(CalculationApiCall.class);
                Call<List<ExpListTeam>> call = service.GetExpApproval(param, filter);
                call.enqueue(new Callback<List<ExpListTeam>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<ExpListTeam>> call, @NonNull Response<List<ExpListTeam>> response) {
                        if (response.body() != null) {
                            view.onExpenseCTeamListGet(response.body());
                        } else {
                           // Toast.makeText(context, "Expense Null", Toast.LENGTH_SHORT).show();
                            view.onExpenseCTeamListGet(null);
                        }

                    }

                    @Override
                    public void onFailure(@NonNull Call<List<ExpListTeam>> call, @NonNull Throwable t) {
                        //Toast.makeText(context, "faild " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        // progressDoalog.dismiss();
                        if (t instanceof SocketTimeoutException) {
                            view.onSaveError("Slow Connection Detected. Please try again");
                        } else {
                            view.onSaveError("Something went wrong... Please try again");
                        }
                    }
                });

            } catch (Exception ex) {
                // progressDoalog.dismiss();
                String str = ex.toString();
                System.out.print(str);
                Log.e("Exception", str);
                view.onSaveError(ex.toString());
            }


        } else {
            //TODO:No Internet
        }

    }

    @Override
    public void SaveExpenseCTeam(ApproveExpRQ aInfo) {
        Gson gson=new Gson();
        String data=gson.toJson(aInfo);
        System.out.println("Expense approve rq:"+data);
        if (NetworkInformation.isConnected(context)) {
            try {
                CalculationApiCall service = RetrofitClientInstance.getRetrofitInstance().create(CalculationApiCall.class);
                Call<ResultInfo> call = service.SaveExpApproval(aInfo);
                call.enqueue(new Callback<ResultInfo>() {
                    @Override
                    public void onResponse(Call<ResultInfo> call, Response<ResultInfo> response) {

                        assert response.body() != null;
                        if (response.body().getSuccess()) {
                            view.onSaveSuccess("Approved");
                        } else if (response.body().getValiCheck()) {
                            view.onSaveSuccess("Check");
                        } else {
                            Toast.makeText(context, "Something went wrong!!", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onFailure(Call<ResultInfo> call, Throwable t) {

                        if (t instanceof SocketTimeoutException) {
                            view.onSaveError("Slow Connection Detected. Please try again");
                        } else {
                            view.onSaveError("Something went wrong... Please try again");
                        }
                    }
                });

            } catch (Exception ex) {
                String str = ex.toString();
                Log.e("Exception", str);
                view.onSaveError(ex.toString());
            }


        } else {
            //TODO:No Internet
        }
    }

}
