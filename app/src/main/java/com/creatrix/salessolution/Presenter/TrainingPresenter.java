package com.creatrix.salessolution.Presenter;

import android.app.ProgressDialog;
import android.content.Context;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.creatrix.salessolution.DBAdapter.DBCrudHelper;
import com.creatrix.salessolution.DBInfo.DBHelperMain;
import com.creatrix.salessolution.Interface.INotice;
import com.creatrix.salessolution.Interface.ITraining;
import com.creatrix.salessolution.Model.Notice;
import com.creatrix.salessolution.Model.Training;
import com.creatrix.salessolution.Network.GetDataService;
import com.creatrix.salessolution.Network.RetrofitClientInstance;
import com.creatrix.salessolution.Services.Constants;

import java.net.SocketTimeoutException;
import java.util.List;

import okhttp3.HttpUrl;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TrainingPresenter implements ITraining.Presenter {
    ITraining.View view;
    Context context;
    ProgressDialog progressDoalog;
    DBHelperMain dbHelper;
    DBCrudHelper crudHelper;
    SwipeRefreshLayout refreshLayout;

    public TrainingPresenter(ITraining.View view, Context context) {
        this.view = view;
        this.context = context;
        this.dbHelper = dbHelper;
        this.crudHelper = crudHelper;
    }

    @Override
    public void getTraining(int empid) {
        progressDoalog = new ProgressDialog((Context) view);
        progressDoalog.setMessage("Training is Loading....");
        progressDoalog.show();
        progressDoalog.setCanceledOnTouchOutside(false);
        try{
            GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
            Call<List<Training>> call = service.GetTraining(empid);
            HttpUrl ds = call.request().url();
            call.enqueue(new Callback<List<Training>>() {
                @Override
                public void onResponse(Call<List<Training>> call, Response<List<Training>> response) {
                    progressDoalog.dismiss();
                    view.onSuccess(response.body());


                }
                @Override
                public void onFailure(Call<List<Training>> call, Throwable t) {
                    progressDoalog.dismiss();
                    if(t instanceof SocketTimeoutException){
                        view.onError("Slow Connection Detected");
                    }else{
                        view.onError("Some Error Occurred");
                    }


                }
            });

        }catch (Exception ex){
            progressDoalog.dismiss();
            view.onError("Some Error Occurred");

        }
    }

    @Override
    public void seenTraining(int id, int empid) {
        try{
            GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
            Call<Void> call = service.postSeenTraining(id,empid);
            HttpUrl ds = call.request().url();
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    Constants.SeenTraining="true";
                }
                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    if(t instanceof SocketTimeoutException){
                        view.onError("Slow Connection Detected");
                    }else{
                        view.onError("Some Error Occurred");
                    }
                }
            });

        }catch (Exception ex){
            view.onError("Some Error Occurred");

        }
    }
}
