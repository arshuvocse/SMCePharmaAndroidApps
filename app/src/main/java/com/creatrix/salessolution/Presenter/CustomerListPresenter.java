package com.creatrix.salessolution.Presenter;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.creatrix.salessolution.Activity.Customer.Pending.CustomerARModel;
import com.creatrix.salessolution.Activity.Customer.Pending.CustomerPendingActivity;
import com.creatrix.salessolution.DBAdapter.DBCrudHelper;
import com.creatrix.salessolution.DBAdapter.SyncDb_Helper;
import com.creatrix.salessolution.DBInfo.DBHelperMain;
import com.creatrix.salessolution.Interface.ICustomer;
import com.creatrix.salessolution.Model.Customer;
import com.creatrix.salessolution.Model.User;
import com.creatrix.salessolution.Model.mCompanyUnit;
import com.creatrix.salessolution.Network.ApiCustomerCall;
import com.creatrix.salessolution.Network.GetDataService;
import com.creatrix.salessolution.Network.RetrofitClientInstance;
import com.creatrix.salessolution.R;
import com.creatrix.salessolution.UtilityHelper.SnackBarManagement;

import java.net.SocketTimeoutException;
import java.util.List;

import okhttp3.HttpUrl;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomerListPresenter implements ICustomer.Presenter {
    ICustomer.View view;
    ProgressDialog progressDoalog;
    DBHelperMain dbHelper;
    DBCrudHelper crudHelper;
    SyncDb_Helper syncDb_helper;
    public CustomerListPresenter(ICustomer.View view,Context context) {
        this.view = view;
        this.dbHelper = new DBHelperMain(context);
        this.crudHelper = new DBCrudHelper(context);
        this.syncDb_helper = new SyncDb_Helper(context);
    }

    @Override
    public void GetCustomerByUser(int empId) {
        progressDoalog = new ProgressDialog((Context) view);
        progressDoalog.setMessage("Customer is Loading....");
        progressDoalog.show();
        progressDoalog.setCanceledOnTouchOutside(false);
        try{
            ApiCustomerCall service = RetrofitClientInstance.getRetrofitInstance().create(ApiCustomerCall.class);
            Call<List<Customer>> call = service.GetCustomerByUser(empId);
            call.enqueue(new Callback<List<Customer>>() {
                @Override
                public void onResponse(@NonNull Call<List<Customer>> call, @NonNull Response<List<Customer>> response) {
                   boolean isTrue = syncDb_helper.InsertCustomerList(response.body());
                    if(isTrue)
                    {
                        progressDoalog.dismiss();
                        view.OnSuccess(response.body());
                    }

                   crudHelper.InsertCustomerInfo_SQLite(response.body());

                }
                @Override
                public void onFailure(@NonNull Call<List<Customer>> call, @NonNull Throwable t) {
                    progressDoalog.dismiss();
                    if(t instanceof SocketTimeoutException){
                        view.OnError(1);
                    }else{
                        view.OnError(2);
                    }
                }
            });

        }catch (Exception ex){
            progressDoalog.dismiss();
            view.OnError(3);
        }

    }

    @Override
    public void GetCustomerReport(int empId) {
        try{
            ApiCustomerCall service = RetrofitClientInstance.getRetrofitInstance().create(ApiCustomerCall.class);
            Call<List<CustomerARModel>> call = service.GetCustomerApproveRejLists(empId);
            call.enqueue(new Callback<List<CustomerARModel>>() {
                @Override
                public void onResponse(@NonNull Call<List<CustomerARModel>> call, @NonNull Response<List<CustomerARModel>> response) {
                    if(response.body()!=null)
                    {
                       // boolean isInserted=false;
                       // crudHelper._deleteAllRecordsFromaTable("tblCustomerReport");
                        boolean isDeleted= crudHelper._deleteAllRecordsFromaTableBolean("tblCustomerReport");
                        if(isDeleted)
                        {
                            boolean isInserted= crudHelper.InsertCustomerReport_SQLite(response.body());
                            if(isInserted)
                            {
                                view.OnSuccessCustomerReport(crudHelper.getCustomerReport_SQLite("Approved"));
                            }else {
                                view.OnSuccessCustomerReport(response.body());
                            }
                        }

                    }else {
                        view.OnSuccessCustomerReport(null);
                    }



                }
                @Override
                public void onFailure(Call<List<CustomerARModel>> call, Throwable t) {
                    if(t instanceof SocketTimeoutException){
                        view.OnErrorReport("Slow Connection Detected");
                    }else{
                        view.OnErrorReport("Loading Failed!!Try Again");
                    }


                }
            });
        }catch (Exception ex){

        }
    }


}
