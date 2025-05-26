package com.creatrix.salessolution.Presenter;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.creatrix.salessolution.DBAdapter.DBCrudHelper;
import com.creatrix.salessolution.Interface.IMileageClaim;
import com.creatrix.salessolution.Model.MileageClaimSM;
import com.creatrix.salessolution.Model.ResultInfo;
import com.creatrix.salessolution.Model.Transport;
import com.creatrix.salessolution.Network.CalculationApiCall;
import com.creatrix.salessolution.Network.RetrofitClientInstance;

import java.net.SocketTimeoutException;
import java.util.List;

import okhttp3.HttpUrl;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MileageClaimPresenter implements IMileageClaim.Presenter {
    IMileageClaim.View view;
    Context context;
    DBCrudHelper dbCrudHelper;

    public MileageClaimPresenter(IMileageClaim.View view, Context context) {
        this.view = view;
        this.context = context;
        dbCrudHelper = new DBCrudHelper(context);
    }

    @Override
    public void GetTransportList() {
        try {
            view.onTransportListGet(dbCrudHelper.getTransportTypeListFromSQLite());
         /*   CalculationApiCall service = RetrofitClientInstance.getRetrofitInstance().create(CalculationApiCall.class);
            Call<List<Transport>> call = service.GetTransportList();
            HttpUrl ds = call.request().url();
            call.enqueue(new Callback<List<Transport>>() {
                @Override
                public void onResponse(Call<List<Transport>> call, Response<List<Transport>> response) {
                    view.onTransportListGet(response.body());
                }

                @Override
                public void onFailure(Call<List<Transport>> call, Throwable t) {
                    if (t instanceof SocketTimeoutException) {
                    } else {
                    }
                }
            });*/

        } catch (Exception ex) {
        }
    }

    @Override
    public void SaveMileageClaim(MileageClaimSM aInfo) {

        ProgressDialog progressDoalog = new ProgressDialog(context);
        progressDoalog.setMessage("Saving.... Please wait");
        progressDoalog.show();
        progressDoalog.setCanceledOnTouchOutside(false);
        try {
            CalculationApiCall service = RetrofitClientInstance.getRetrofitInstance().create(CalculationApiCall.class);
            Call<ResultInfo> call = service.SaveMileageClaim(aInfo);
            HttpUrl ds = call.request().url();
            call.enqueue(new Callback<ResultInfo>() {
                @Override
                public void onResponse(Call<ResultInfo> call, Response<ResultInfo> response) {
                    progressDoalog.dismiss();
                    ResultInfo info = response.body();
                    if (info != null) {
                        if (info.getSuccess() == true) {
                            view.onSaveSuccess("Mileage Claim Successfully Submitted");
                        } else if (info.getValiCheck() == true) {
                            view.onSaveError("You Already Claim Your Mileage Today");
                        } else {
                            view.onSaveError("Some error occurred... Please try again");
                        }
                    } else {
                        view.onSaveError("Some error occurred... Please try again");
                    }

                }

                @Override
                public void onFailure(@NonNull Call<ResultInfo> call, @NonNull Throwable t) {
                    progressDoalog.dismiss();
                    if (t instanceof SocketTimeoutException) {
                        view.onSaveError("Slow Internet Detected..Please try again");
                    } else {
                        view.onSaveError("Some error occurred..Please try again");
                    }
                }
            });

        } catch (Exception ex) {
            progressDoalog.dismiss();
            String str = ex.toString();
            Log.e("Exception", str);
            view.onSaveError("Some error occurred..Please try again");

        }

    }

    @Override
    public void GetMileageList(int empId) {

    }
}
