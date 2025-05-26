package com.creatrix.salessolution.Activity.Customer;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;


import com.creatrix.salessolution.DBAdapter.DBCrudHelper;
import com.creatrix.salessolution.DBAdapter.DBDoctor.DBDoctorHelper;
import com.creatrix.salessolution.Model.ResultInfo;
import com.creatrix.salessolution.Model.StationType;
import com.creatrix.salessolution.Network.ApiCustomerCall;
import com.creatrix.salessolution.Network.RetrofitClientInstance;
import com.creatrix.salessolution.Network.apiSeedDataCall;

import java.net.SocketTimeoutException;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomerPresenter implements ICustomerAdd.Presenter {
    ICustomerAdd.View view;
    Context context;
    DBCrudHelper dbCrudHelper;
    DBDoctorHelper dbDoctor;
    public CustomerPresenter(ICustomerAdd.View view,Context context) {
        this.view = view;
        this.context = context;
        dbCrudHelper=new DBCrudHelper(context);
        dbDoctor = new DBDoctorHelper(context);
    }

    @Override
    public void GetProgramType() {
       /* try {

            view.onProgramType(dbDoctor.getProgramTypeListFromSQLite(0));
        } catch (Exception exception) {
            exception.printStackTrace();
        }*/
    }

    @Override
    public void GetProviderType() {
        try {
            view.onProviderType(dbDoctor.getProviderTypeListForCustFromSQLite(0,1));
        } catch (Exception exception) {
            exception.printStackTrace();
        }

    }

    @Override
    public void GetSMCType() {
        try {
            view.onSMCType(dbDoctor.getSMCTypeListForCustFromSQLite(0,1));
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void GetCustomerType() {

        try{
           /* apiSeedDataCall service = RetrofitClientInstance.getRetrofitInstance().create(apiSeedDataCall.class);
            Call<List<CustomerType>> call = service.GetCustomerType();
            call.enqueue(new Callback<List<CustomerType>>() {
                @Override
                public void onResponse(Call<List<CustomerType>> call, Response<List<CustomerType>> response) {
                    view.onCustomerTypeReceived(response.body());
                }
                @Override
                public void onFailure(Call<List<CustomerType>> call, Throwable t) {
                    if(t instanceof SocketTimeoutException){
                    }else{
                    }
                }
            });*/

            view.onCustomerTypeReceived(dbCrudHelper.getCustTypeListFromSQLite());
        }catch (Exception ex){
        }

    }

    @Override
    public void GetStationType() {
        try{
            apiSeedDataCall service = RetrofitClientInstance.getRetrofitInstance().create(apiSeedDataCall.class);
            Call<List<StationType>> call = service.GetStation();
            call.enqueue(new Callback<List<StationType>>() {
                @Override
                public void onResponse(Call<List<StationType>> call, Response<List<StationType>> response) {
                    view.onStationReceived(response.body());
                }
                @Override
                public void onFailure(Call<List<StationType>> call, Throwable t) {
                    if(t instanceof SocketTimeoutException){
                    }else{
                    }
                }
            });

        }catch (Exception ex){
        }

    }

    @Override
    public void SaveCustomer(CustomerSvModel aInfo,String who) {
        ProgressDialog progressDoalog = new ProgressDialog(context);
        progressDoalog.setMessage("Customer is Saving.... Please wait");
        progressDoalog.show();
        progressDoalog.setCanceledOnTouchOutside(false);
        try{
            ApiCustomerCall service = RetrofitClientInstance.getRetrofitInstance().create(ApiCustomerCall.class);
            Call<ResultInfo> call = service.SaveCustomer(aInfo);
            call.enqueue(new Callback<ResultInfo>() {
                @Override
                public void onResponse(Call<ResultInfo> call, Response<ResultInfo> response) {

                    ResultInfo info =response.body();
                    if(info !=null){
                        if(info.getSuccess() == true){
                            progressDoalog.dismiss();
                            view.onSubmitSuccess("Customer Successfully Submitted",who);
                        }
                        else if(info.getValiCheck()==true)
                        {
                            progressDoalog.dismiss();
                            view.onSubmitError("This Customer Already Exist");
                        }
                        else{
                            progressDoalog.dismiss();
                            view.onSubmitError("Some error occurred... Please try again");
                        }
                    }else{
                        progressDoalog.dismiss();
                        view.onSubmitError("Some error occurred... Please try again");

                    }
                    /*if(info !=null){
                        if(info.getSuccess() == true){
                            view.onSubmitSuccess("Customer Saved Successfully");

                        }else{
                            view.onSubmitError("Some error occurred..Please try again");
                        }

                    }else{
                        view.onSubmitError("Some error occurred..Please try again");

                    }*/

                }
                @Override
                public void onFailure(Call<ResultInfo> call, Throwable t) {
                    progressDoalog.dismiss();
                    if(t instanceof SocketTimeoutException){
                        progressDoalog.dismiss();
                        view.onSubmitError("Slow Internet Detected..Please try again");
                    }else{
                        progressDoalog.dismiss();
                        view.onSubmitError("Some error occurred..Please try again");
                    }
                }
            });
        }catch (Exception ex){
            progressDoalog.dismiss();
            String str = ex.toString();
            Log.e("Exception",str);
            view.onSubmitError("Some error occurred..Please try again");
        }


    }
}
