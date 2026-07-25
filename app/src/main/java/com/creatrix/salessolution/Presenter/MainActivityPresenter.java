package com.creatrix.salessolution.Presenter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.creatrix.salessolution.Activity.DashboardActivity;
import com.creatrix.salessolution.Activity.MainActivity;
import com.creatrix.salessolution.Interface.LoginInterface;
import com.creatrix.salessolution.Model.AppVersionModel;
import com.creatrix.salessolution.Model.User;
import com.creatrix.salessolution.Network.ApiMasterSync;
import com.creatrix.salessolution.Network.GetDataService;
import com.creatrix.salessolution.Network.RetrofitClientInstance;
import com.creatrix.salessolution.UtilityHelper.SessionManagement;
import com.creatrix.salessolution.UtilityHelper.SnackBarManagement;

import java.net.SocketTimeoutException;

import okhttp3.HttpUrl;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivityPresenter implements LoginInterface.Presenter {

    LoginInterface.View loginView;
    SessionManagement session;
    ProgressDialog progressDoalog;
    public MainActivityPresenter(LoginInterface.View loginView) {
        this.loginView = loginView;
    }
    @Override
    public void doLogin(String UserName, String password, Context context,String Imei,String Token,String Device,String AppVersion,String OS, String OS_Version) {
        progressDoalog = new ProgressDialog(context);
        progressDoalog.setMessage("Attempting to Login....");
        progressDoalog.show();
        progressDoalog.setCanceledOnTouchOutside(false);
        try{
            GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
            Call<User> call = service.LoginRequest(UserName,password,Imei,Token,Device,AppVersion,OS,OS_Version);
            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                    if (progressDoalog != null && progressDoalog.isShowing()) {
                        progressDoalog.dismiss();
                    }
                    if (!response.isSuccessful()) {
                        loginView.OnError("Server connection error (" + response.code() + "). Please try again.");
                        return;
                    }
                    User user = response.body();
                    if (user == null) {
                        loginView.OnError("User Not Found...Please try again or contact Admin!");
                    } else if (user.getUserId() == 0) {
                        if (user.getTwoDeviceMsg() != null && !user.getTwoDeviceMsg().equals("")) {
                            loginView.OnError("Maximum Two Device Allowed!!");
                        } else {
                            loginView.OnError("Your Login Name and Password do not match");
                        }
                    } else {
                        int usid = user.getUserId();
                        int EmpInfoId = user.getEmpInfoId();
                        String userName = user.getUserName();
                        String empMasterCo=user.getEmpMasterCode();
                        String loginName = user.getLoginName();
                        String passwrd = user.getPassword();
                        String userCo = user.getUserCo();
                        String versionName = user.getVersionName();
                        String designation=user.getDesigName();
                        int IsImeiMatch = user.getIsImeiMatched();
                        String emprole = user.getEmpRole();

                        //Convert bool to int
                        int isAccepted = user.getisApprove() ? 1 : 0;
                        int isForward = user.getisForward() ? 1 : 0;
                        //Toast.makeText(context, emprole, Toast.LENGTH_SHORT).show();
                        System.out.println(emprole);
                        progressDoalog.dismiss();
                        loginView.OnSuccess(usid,userName,empMasterCo,loginName,passwrd,EmpInfoId,userCo,IsImeiMatch,versionName,emprole,user.getRoleTypeId(), user.getRoleType(),isAccepted,isForward,designation);
                    }

                }
                @Override
                public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                    progressDoalog.dismiss();
                    if(t instanceof SocketTimeoutException){
                        loginView.OnError("Slow Connection Detected. Please try again");
                    }else{
                        loginView.OnError("Slow Connection Detected With Network. Please try again");
                    }
                }
            });

        }catch (Exception ex){
            progressDoalog.dismiss();
            String str = ex.toString();
            loginView.OnError(ex.toString());
        }
    }

  /*  @Override
    public void GetActiveVersion() {

            try{

                GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
                Call<AppVersionModel> call = service.GetAppVersionInfo();
                call.enqueue(new Callback<AppVersionModel>() {
                    @Override
                    public void onResponse(Call<AppVersionModel> call, Response<AppVersionModel> response) {
                        AppVersionModel aInfo = response.body();
                        loginView.onVersionGet(aInfo.getVersionName());
                    }

                    @Override
                    public void onFailure(Call<AppVersionModel> call, Throwable t) {

                    }
                });
            }catch (Exception ex){

            }
    }*/


}
