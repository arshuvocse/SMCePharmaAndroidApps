package com.creatrix.salessolution.Presenter;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.creatrix.salessolution.Activity.Approval.DA.ApproveDARQ;
import com.creatrix.salessolution.Activity.Approval.DA.DAListData;
import com.creatrix.salessolution.Interface.IDATeam;
import com.creatrix.salessolution.Model.ResultInfo;
import com.creatrix.salessolution.Network.CalculationApiCall;
import com.creatrix.salessolution.Network.RetrofitClientInstance;
import com.google.gson.Gson;

import java.net.SocketTimeoutException;
import java.util.List;
import java.util.Map;

import okhttp3.HttpUrl;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DATeamPresenter implements IDATeam.Presenter {
    IDATeam.View view;
    Context context;

    public DATeamPresenter(IDATeam.View view, Context context) {
        this.view = view;
        this.context = context;
    }

    @Override
    public void SaveTeamDA(ApproveDARQ aInfo) {
        /*ProgressDialog progressDoalog = new ProgressDialog(context);
        progressDoalog.setMessage("Please wait..");
        progressDoalog.show();
        progressDoalog.setCanceledOnTouchOutside(false);*/
        try{
            CalculationApiCall service = RetrofitClientInstance.getRetrofitInstance().create(CalculationApiCall.class);
            Call<ResultInfo> call = service.SaveDAApproval(aInfo);
            Gson gson=new Gson();
            String data=gson.toJson(aInfo);
            System.out.println(data);

            call.enqueue(new Callback<ResultInfo>() {
                @Override
                public void onResponse(@NonNull Call<ResultInfo> call, @NonNull Response<ResultInfo> response) {
                    ResultInfo info =response.body();
                    if(info !=null){
                        if(info.getSuccess()){
                            view.onSaveSuccess("Successfully Submitted");
                        }
                        else if(!info.getSuccess() && info.isValiCheck){
                            view.onSaveError(info.getMsd());
                        }else {
                            view.onSaveError("Some error occurred... Please try again");
                        }
                    }
                    else{
                        view.onSaveError("Some error occurred... Please try again");
                    }

                }
                @Override
                public void onFailure(@NonNull Call<ResultInfo> call, @NonNull Throwable t) {
                   // progressDoalog.dismiss();
                    if(t instanceof SocketTimeoutException){
                        view.onSaveError("Slow Internet Detected..Please try again");
                    }else{
                        view.onSaveError("Some error occurred..Please try again");
                    }
                }
            });

        }catch (Exception ex){
           // progressDoalog.dismiss();
        /*    String str = ex.toString();
            Log.e("Exception",str);
            view.onSaveError("Some error occurred..Please try again");*/

        }
    }

    @Override
    public void GetTeamDAList(String param, Map<String, String> filter) {
        try{
            CalculationApiCall service = RetrofitClientInstance.getRetrofitInstance().create(CalculationApiCall.class);
            Call<List<DAListData>> call = service.GetDAApproval(param,filter);
            HttpUrl ds = call.request().url();
            Log.d("TeamDAList", "GetDAApproval param=" + param + " filter=" + new Gson().toJson(filter) + " url=" + ds);
            call.enqueue(new Callback<List<DAListData>>() {
                @Override
                public void onResponse(@NonNull Call<List<DAListData>> call, @NonNull Response<List<DAListData>> response) {
                    if(response.body()!=null)
                    {
                        view.onTeamDAList(response.body());
                        Log.d("TeamDAList", "GetDAApproval success size=" + (response.body() == null ? 0 : response.body().size()));
                    }else {
                        view.onSaveError("Something went wrong");
                    }

                }
                @Override
                public void onFailure(@NonNull Call<List<DAListData>> call, @NonNull Throwable t) {
                    if(t instanceof SocketTimeoutException){
                        view.onSaveError("Slow Network Detected!!");
                    }
                    Log.e("TeamDAList", "GetDAApproval failed", t);
                }
            });

        }catch (Exception ex){
            Log.e("TeamDAList", "GetDAApproval exception", ex);
        }
    }
}
