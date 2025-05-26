package com.creatrix.salessolution.Presenter;

import android.app.ProgressDialog;
import android.content.Context;

import androidx.annotation.NonNull;

import com.creatrix.salessolution.Activity.Customer.Pending.CustomerARModel;
import com.creatrix.salessolution.Activity.Doctor.Pending.DoctorARModel;
import com.creatrix.salessolution.DBAdapter.DBCrudHelper;
import com.creatrix.salessolution.DBAdapter.DBDoctor.DBDoctorHelper;
import com.creatrix.salessolution.DBAdapter.SyncDb_Helper;
import com.creatrix.salessolution.DBInfo.DBHelperMain;
import com.creatrix.salessolution.Interface.ICustomer;
import com.creatrix.salessolution.Interface.IPendingDoctor;
import com.creatrix.salessolution.Model.Customer;
import com.creatrix.salessolution.Network.ApiCustomerCall;
import com.creatrix.salessolution.Network.ApiDoctorCall;
import com.creatrix.salessolution.Network.RetrofitClientInstance;
import com.creatrix.salessolution.UtilityHelper.SnackBarManagement;

import java.net.SocketTimeoutException;
import java.util.List;

import okhttp3.HttpUrl;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PendingDoctorPresenter implements IPendingDoctor.Presenter {
    IPendingDoctor.View view;
    DBHelperMain dbHelper;
    DBCrudHelper crudHelper;
    SyncDb_Helper syncDb_helper;
    DBDoctorHelper dbDoctorHelper;
    public PendingDoctorPresenter(IPendingDoctor.View view, Context context) {
        this.view = view;
        this.dbHelper = new DBHelperMain(context);
        this.crudHelper = new DBCrudHelper(context);
        this.syncDb_helper = new SyncDb_Helper(context);
        this.dbDoctorHelper = new DBDoctorHelper(context);
    }

    @Override
    public void GetDoctorReport(int empId) {
        try {
            ApiDoctorCall service = RetrofitClientInstance.getRetrofitInstance().create(ApiDoctorCall.class);
            Call<List<DoctorARModel>> call = service.GetDoctorApproveRejLists(empId);
            call.enqueue(new Callback<List<DoctorARModel>>() {
                @Override
                public void onResponse(@NonNull Call<List<DoctorARModel>> call, @NonNull Response<List<DoctorARModel>> response) {
                    if(response.body()!=null){
                        crudHelper._deleteAllRecordsFromaTable("tblDoctorReport");
                        boolean isInserted= crudHelper.InsertCDoctorReport_SQLite(response.body());
                        if(isInserted)
                        {
                            view.OnSuccessDoctorReport(dbDoctorHelper.getDoctorReport_SQLite("Approved"));
                        }else {
                            view.OnSuccessDoctorReport(response.body());
                        }
                    }else {
                        view.OnSuccessDoctorReport(null);
                    }

                }
                @Override
                public void onFailure(@NonNull Call<List<DoctorARModel>> call, @NonNull Throwable t) {
                    if(t instanceof SocketTimeoutException){
                        view.OnErrorReport("Slow Connection Detected");
                    }else{
                        view.OnErrorReport("Loading Failed!!Try Again");
                    }

                }
            });

        } catch (Exception ex) {
        }
    }
}
