package com.creatrix.salessolution.Presenter;


import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.creatrix.salessolution.Activity.MileageClaim.Model.ApproveMilRQ;
import com.creatrix.salessolution.Activity.MileageClaim.Model.MileageListTeam;
import com.creatrix.salessolution.Interface.IMileageTeam;
import com.creatrix.salessolution.Model.ResultInfo;
import com.creatrix.salessolution.Network.CalculationApiCall;
import com.creatrix.salessolution.Network.RetrofitClientInstance;
import com.creatrix.salessolution.UtilityHelper.NetworkInformation;

import java.net.SocketTimeoutException;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TeamMileagePresenter implements IMileageTeam.Presenter {
    IMileageTeam.View view;
    Context context;

    public TeamMileagePresenter(IMileageTeam.View view, Context context) {
        this.view = view;
        this.context = context;
    }

    @Override
    public void SaveTeamMileageClaim(ApproveMilRQ aInfo) {
        if (NetworkInformation.isConnected(context)) {
            try {
                CalculationApiCall service = RetrofitClientInstance.getRetrofitInstance().create(CalculationApiCall.class);
                Call<ResultInfo> call = service.SaveTeamMileageClaim(aInfo);
                call.enqueue(new Callback<ResultInfo>() {
                    @Override
                    public void onResponse(@NonNull Call<ResultInfo> call, @NonNull Response<ResultInfo> response) {
                      /*  if (response.body() != null || response.body().isSuccess) {
                            view.onSaveSuccess("Mileage Approved");
                        } else {
                            Toast.makeText(context, "Mileage Null", Toast.LENGTH_SHORT).show();
                        }*/

                        if (response.body().getSuccess() ==true) {
                            view.onSaveSuccess("Approved");

                        }else if(response.body().getValiCheck()==true)
                        {
                            view.onSaveSuccess("Check");
                        }
                        else {
                            Toast.makeText(context, "Something went wrong!!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ResultInfo> call, @NonNull Throwable t) {
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
                //String str = ex.toString();
                //Log.e("Exception", str);
                view.onSaveError(ex.toString());
            }


        } else {
            //TODO:No Internet
        }
    }

    @Override
    public void GetTeamMileageList(String param, Map<String, String> filter) {
        if (NetworkInformation.isConnected(context)) {
            try {
                CalculationApiCall service = RetrofitClientInstance.getRetrofitInstance().create(CalculationApiCall.class);
                Call<List<MileageListTeam>> call = service.GetMileageApproval(param, filter);
                call.enqueue(new Callback<List<MileageListTeam>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<MileageListTeam>> call, @NonNull Response<List<MileageListTeam>> response) {
                        if (response.body() != null) {
                           view.onTeamMileageList(response.body());
                        } else {
                            Toast.makeText(context, "No Mileage Data", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<MileageListTeam>> call, @NonNull Throwable t) {
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
                Log.e("Exception", str);
                view.onSaveError(ex.toString());
            }


        } else {
            //TODO:No Internet
        }
    }
}