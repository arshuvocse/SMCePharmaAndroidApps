package com.creatrix.salessolution.Presenter;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import com.creatrix.salessolution.DBAdapter.DBDoctor.DBDoctorHelper;
import com.creatrix.salessolution.DBAdapter.SyncDb_Helper;
import com.creatrix.salessolution.Interface.IDoctor;
import com.creatrix.salessolution.Interface.ITypeFilter;
import com.creatrix.salessolution.Model.Doctor.DoctorSM;
import com.creatrix.salessolution.Model.ResultInfo;
import com.creatrix.salessolution.Network.ApiDoctorCall;
import com.creatrix.salessolution.Network.RetrofitClientInstance;

import java.net.SocketTimeoutException;

import okhttp3.HttpUrl;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TypeFilterPresenter implements ITypeFilter.Presenter {
    ITypeFilter.View view;
    Context context;
    DBDoctorHelper dbDoctorHelper;
    SyncDb_Helper syncDbHelper;

    public TypeFilterPresenter(ITypeFilter.View view, Context context) {
        this.view = view;
        this.context = context;
        dbDoctorHelper = new DBDoctorHelper(context);
        syncDbHelper = new SyncDb_Helper(context);
    }



    @Override
    public void GetDoctorType(int id) {
        try {
            view.onDoctorTypeReceived(dbDoctorHelper.getDoctorTypeListFromSQLite(id));

        } catch (Exception ex) {
        }
    }

    @Override
    public void GetCustomerType(int id) {
        try {
           // view.onCustomerTypeReceived(dbDoctorHelper.getDoctorTypeListFromSQLite(id));

        } catch (Exception ex) {
        }
    }

    @Override
    public void GetContactType(int id) {
        try {
            view.onContactTypeReceived(dbDoctorHelper.getContactTypeListFromSQLite(id));
        } catch (Exception ex) {
        }

    }
    @Override
    public void GetProviderType(int id) {
        try {
            view.onProviderTypeReceived(dbDoctorHelper.getProviderTypeListFromSQLite(id,1));
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
    @Override
    public void GetSMCType(int id,String who) {
        try {
            if(who.equals("Doctor"))
            view.onSMCTypeReceived(dbDoctorHelper.getSMCTypeListForDocFromSQLite(0,1));
            else
                view.onSMCTypeReceived(dbDoctorHelper.getSMCTypeListForCustFromSQLite(0,1));

        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
    @Override
    public void GetSpecialType(int id) {
        try {
            view.onSpecialTypeReceived(dbDoctorHelper.getSpecialdayTypeListFromSQLite(id));
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

}
