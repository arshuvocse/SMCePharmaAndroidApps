package com.creatrix.salessolution.Presenter;

import android.app.ProgressDialog;

import androidx.annotation.NonNull;

import com.creatrix.salessolution.DBInfo.DBHelperMain;
import com.creatrix.salessolution.Interface.IMioDashboard;
import com.creatrix.salessolution.Model.Dashboard_SummeryVM;
import com.creatrix.salessolution.Network.GetDataService;
import com.creatrix.salessolution.Network.RetrofitClientInstance;
import java.net.SocketTimeoutException;

import okhttp3.HttpUrl;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MioDashboardPresenter implements IMioDashboard.Presenter {
    ProgressDialog progressDoalog;
    IMioDashboard.View view;
    DBHelperMain dbHelper;
    public MioDashboardPresenter(IMioDashboard.View view) {
        this.view = view;
    }
    @Override
    public void getDashboardSummeryData(int empId, String currentDate) {
        try{
            GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
            Call<Dashboard_SummeryVM> call = service.GetMioDashboardSummeryData(empId,currentDate);

            call.enqueue(new Callback<Dashboard_SummeryVM>() {
                @Override
                public void onResponse(@NonNull Call<Dashboard_SummeryVM> call, @NonNull Response<Dashboard_SummeryVM> response) {
                    view.onDashboardSummeryDataBind(response.body());
                }
                @Override
                public void onFailure(@NonNull Call<Dashboard_SummeryVM> call, @NonNull Throwable t) {
                    if(t instanceof SocketTimeoutException){
                        view.onError("Slow Network detected");
                    }else{
                        view.onError("Some Error Occurred");

                    }

                }
            });

        }catch (Exception ex){
            view.onError("Some Error Occurred");
        }
    }
    @Override
    public void getTodaySummery(int empId, String currentDate) {
        try{
            GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
            Call<Dashboard_SummeryVM> call = service.GetTodaySummery(empId,currentDate);

            call.enqueue(new Callback<Dashboard_SummeryVM>() {
                @Override
                public void onResponse(@NonNull Call<Dashboard_SummeryVM> call, @NonNull Response<Dashboard_SummeryVM> response) {
                    view.onTodaySummeryDataBind(response.body());
                }
                @Override
                public void onFailure(@NonNull Call<Dashboard_SummeryVM> call, @NonNull Throwable t) {
                    if(t instanceof SocketTimeoutException){
                        view.onError("Slow Network detected");
                    }else{
                        view.onError("Some Error Occurred");

                    }

                }
            });

        }catch (Exception ex){
           // view.onError("Some Error Occurred");
        }
    }
}
