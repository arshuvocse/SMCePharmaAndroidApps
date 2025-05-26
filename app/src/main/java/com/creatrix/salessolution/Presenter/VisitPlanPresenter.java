package com.creatrix.salessolution.Presenter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.creatrix.salessolution.Activity.Doctor.TourePlan.Model.TourPlanReq;
import com.creatrix.salessolution.Activity.Doctor.VisitPlan.Model.VisitplanModel;
import com.creatrix.salessolution.Activity.Doctor.VisitPlan.VisitPlanDetailsActivity;
import com.creatrix.salessolution.DBAdapter.DBCrudHelper;
import com.creatrix.salessolution.Interface.ITourplan;
import com.creatrix.salessolution.Interface.IVisitPlan;
import com.creatrix.salessolution.Model.MonthDate;
import com.creatrix.salessolution.Model.ResultInfo;
import com.creatrix.salessolution.Model.TourPlanViewModel;
import com.creatrix.salessolution.Network.ApiDoctorCall;
import com.creatrix.salessolution.Network.RetrofitClientInstance;
import com.creatrix.salessolution.Network.TourApiCall;
import com.creatrix.salessolution.UtilityHelper.ToastManagment;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.HttpUrl;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VisitPlanPresenter implements IVisitPlan.Presenter {
    Context context;
    ProgressDialog progressDoalog;
    DBCrudHelper dbCrudHelper;
    IVisitPlan.View view;
    public VisitPlanPresenter(IVisitPlan.View view, Context context) {
        this.view = view;
        this.context = context;
        dbCrudHelper=new DBCrudHelper(context);
    }
    @Override
    public void getVisitPlanDataByEmpId(List<MonthDate> aMondateList,boolean is_Entry, int month, int year, int empId) {
        try {
            ApiDoctorCall service = RetrofitClientInstance.getRetrofitInstance().create(ApiDoctorCall.class);
            Call<List<VisitplanModel>> call = service.GetDoctorVisitPlanData(month, year, empId);
            call.enqueue(new Callback<List<VisitplanModel>>() {
                @Override
                public void onResponse(@NonNull Call<List<VisitplanModel>> call, @NonNull Response<List<VisitplanModel>> response) {
                   if(response!=null)
                   {
                       view.OnArreangList(aMondateList,is_Entry,response.body());
                   }
                }
                @Override
                public void onFailure(@NonNull Call<List<VisitplanModel>> call, @NonNull Throwable t) {
                    if (t instanceof SocketTimeoutException) {
                       view.OnFailour("Slow Internet Connection");
                    } else {

                    }
                }
            });

        } catch (Exception ex) {
            view.OnFailour("Got an exception!!!");
        }
    }

    @Override
    public void saveVisitPlanPerdayByEmpId(List<VisitplanModel> vpl) {
        try {
            ApiDoctorCall service = RetrofitClientInstance.getRetrofitInstance().create(ApiDoctorCall.class);
            Call<ResultInfo> call = service.SaveDoctorVisitPlan(vpl);
          //  Toast.makeText(context, "post : "+vpl, Toast.LENGTH_SHORT).show();
            HttpUrl ds = call.request().url();
            call.enqueue(new Callback<ResultInfo>() {
                @Override
                public void onResponse(Call<ResultInfo> call, Response<ResultInfo> response) {
                    // progressDoalog.dismiss();
                    ResultInfo info = response.body();
                    if (info.getSuccess() == true) {
                        view.OnSuccessVPPDay("VisitPlanSave");
                    }
                    else {
                        //  Toast.makeText(context, "Not", Toast.LENGTH_SHORT).show();
                    }
                   /* if(response.body().equals(500))
                    {
                        Toast.makeText(context, "Penguin", Toast.LENGTH_SHORT).show();
                    }else {

                    }*/

                }

                @Override
                public void onFailure(Call<ResultInfo> call, Throwable t) {
                    // progressDoalog.dismiss();
                    if (t instanceof SocketTimeoutException) {
                        //ShowErrorMsg();
                    } else {
                        //ShowErrorMsg();
                    }
                }
            });

        } catch (Exception ex) {
            //progressDoalog.dismiss();
            String str = ex.toString();
            Log.e("Exception", str);
            // ShowErrorMsg();
        }
    }
}
