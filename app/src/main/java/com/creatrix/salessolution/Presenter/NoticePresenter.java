package com.creatrix.salessolution.Presenter;

import android.app.ProgressDialog;
import android.content.Context;


import androidx.annotation.NonNull;

import com.creatrix.salessolution.DBAdapter.DBCrudHelper;
import com.creatrix.salessolution.DBInfo.DBHelperMain;
import com.creatrix.salessolution.Interface.INotice;

import com.creatrix.salessolution.Model.Notice;
import com.creatrix.salessolution.Network.GetDataService;
import com.creatrix.salessolution.Network.RetrofitClientInstance;
import com.creatrix.salessolution.Services.Constants;

import java.net.SocketTimeoutException;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NoticePresenter implements INotice.Presenter {
    INotice.View view;
    Context context;
    ProgressDialog progressDoalog;
    DBHelperMain dbHelper;
    DBCrudHelper crudHelper;

    public NoticePresenter(INotice.View view, Context context) {
        this.view = view;
        this.context = context;
        this.dbHelper = new DBHelperMain(context);
        this.crudHelper = new DBCrudHelper(context);
    }

    @Override
    public void getNotices(int empId) {
        progressDoalog = new ProgressDialog(context/*(Context)*/ /*view*/);
        progressDoalog.setMessage("Notice is Loading....");
        progressDoalog.show();
        progressDoalog.setCanceledOnTouchOutside(false);
        try{
            GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
            Call<List<Notice>> call = service.GetUpdatedNotice(empId);
            call.enqueue(new Callback<List<Notice>>() {
                @Override
                public void onResponse(@NonNull Call<List<Notice>> call, @NonNull Response<List<Notice>> response) {
                    try {
                        if(response.body()!=null)
                        {
                            progressDoalog.dismiss();
                            view.onSuccess(response.body());
                        }else {
                            progressDoalog.dismiss();
                            view.onSuccess(null);
                        }
                    } catch (Exception exception) {
                        //exception.printStackTrace();
                    }

                }
                @Override
                public void onFailure(@NonNull Call<List<Notice>> call, @NonNull Throwable t) {
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
            System.out.print(ex.getCause());
            System.out.print(ex.getMessage());
            view.onError("Some Error Occurred");

        }

    }

    @Override
    public void getNoticesforPop(int empId) {
        try{
            GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
            Call<List<Notice>> call = service.GetUpdatedNotice(empId);
            call.enqueue(new Callback<List<Notice>>() {
                @Override
                public void onResponse(@NonNull Call<List<Notice>> call, @NonNull Response<List<Notice>> response) {
                    view.onSuccess(response.body());
                }
                @Override
                public void onFailure(@NonNull Call<List<Notice>> call, @NonNull Throwable t) {
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

    @Override
    public void postSeenNotice(int id, int EmpId,String AppSeenDate) {

        try{
            GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
            Call<Void> call = service.postSeenNotice(id,EmpId,AppSeenDate);
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                    Constants.SeenNotice="true";
                }
                @Override
                public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
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
