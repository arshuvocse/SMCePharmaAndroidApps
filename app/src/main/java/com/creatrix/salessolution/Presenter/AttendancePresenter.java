package com.creatrix.salessolution.Presenter;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.creatrix.salessolution.Activity.Attendance.Model.ApproveRQ;
import com.creatrix.salessolution.Activity.Attendance.Model.AttenApproval;
import com.creatrix.salessolution.Activity.Attendance.Model.ButtonRP;
import com.creatrix.salessolution.DBAdapter.DBCrudHelper;
import com.creatrix.salessolution.Interface.IAttendance;
import com.creatrix.salessolution.Model.Attendance;
import com.creatrix.salessolution.Model.ResultInfo;
import com.creatrix.salessolution.Network.AttendanceApi;
import com.creatrix.salessolution.Network.RetrofitClientInstance;
import com.creatrix.salessolution.Network.RetrofitClientInstanceAttendance;
import com.creatrix.salessolution.UtilityHelper.NetworkInformation;
import com.creatrix.salessolution.UtilityHelper.SnackBarManagement;

import java.net.SocketTimeoutException;
import java.util.List;
import java.util.Map;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AttendancePresenter implements IAttendance.Presenter {
    IAttendance.View view;
    Context context;
    ProgressDialog progressDoalog;
    DBCrudHelper dbCrudHelper;

    public AttendancePresenter(IAttendance.View view, Context context) {
        this.view = view;
        this.context = context;
        dbCrudHelper = new DBCrudHelper(context);
    }

    @Override
    public void doSavePuncINInfo(Attendance attendance, RelativeLayout masterLayout) {
        if (NetworkInformation.isConnected(context)) {
            progressDoalog = new ProgressDialog((Context) view);
            progressDoalog.setMessage("Punch In is Processing.... Please wait");
            progressDoalog.show();
            progressDoalog.setCanceledOnTouchOutside(false);
            try {
                AttendanceApi service = RetrofitClientInstanceAttendance.getRetrofitInstance().create(AttendanceApi.class);
                Call<ResultInfo> call = service.SavePunInfo(attendance);
                call.enqueue(new Callback<ResultInfo>() {
                    @Override
                    public void onResponse(@NonNull Call<ResultInfo> call, @NonNull Response<ResultInfo> response) {
                        progressDoalog.dismiss();
                        ResultInfo info = response.body();
                        if (response.body() != null) {
                            assert info != null;
                            if (info.getSuccess()) {
                                if (attendance.getAttType() == 1) {
                                   // dbCrudHelper.InsertPunchINInfo_AttendanceTable_SQLite(attendance);
                                   // view.onSuccess("Punch IN Successful : "+info.getMsd() ,true, 1);
                                    view.onSuccess(info.getMsd() ,true, 1);
                                }
                                if (attendance.getAttType() == 2) {
                                   // dbCrudHelper.UpdatePunchOUTInfo_AttendanceTable_SQLite(attendance);
                                    //view.onSuccess("Punch Out Successful : "+info.getMsd(),true, 2);
                                    view.onSuccess(info.getMsd(),true, 2);
                                }
                            } else {
                                SnackBarManagement._warning_CustomMessage(masterLayout,"Already Attendance done");
                            }
                        } else {
                            Toast.makeText(context, "Attendance Null", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onFailure(@NonNull Call<ResultInfo> call, @NonNull Throwable t) {
                        if ((progressDoalog != null) && progressDoalog.isShowing())
                        {
                            progressDoalog.dismiss();
                        }
                        if (t instanceof SocketTimeoutException) {
                            view.onError("Slow Connection Detected. Please try again", 1);
                        } else {
                            view.onError("Something went wrong... Please try again", 1);
                        }
                    }
                });

            } catch (Exception ex) {
                if ((progressDoalog != null) && progressDoalog.isShowing())
                {
                    progressDoalog.dismiss();
                }
                String str = ex.toString();
                Log.e("Exception", str);
                view.onError(ex.toString(), 1);
            }
        }
    }

    @Override
    public void teamAttendanceList(String list, String param, String role) {
        if (NetworkInformation.isConnected(context)) {
            try {
                AttendanceApi service = RetrofitClientInstance.getRetrofitInstance().create(AttendanceApi.class);
                Call<List<AttenApproval>> call = service.GetAttenInfo(param, role);
                call.enqueue(new Callback<List<AttenApproval>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<AttenApproval>> call, @NonNull Response<List<AttenApproval>> response) {
                        Log.d("TAG", "onResponse: " + response.body());
                        System.out.println("resp: " + response.body());
                        if (response.body() != null) {
                            List<AttenApproval> approval = response.body();
                            System.out.println("Response :" + approval);
                            view.onSuccessTeamAtten(approval);

                        } else {
                            view.onSuccessTeamAtten(null);
                            Toast.makeText(context, "Not Approved", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onFailure(@NonNull Call<List<AttenApproval>> call, @NonNull Throwable t) {
                        Toast.makeText(context, "faild " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        if (t instanceof SocketTimeoutException) {
                            view.onError("Slow Connection Detected. Please try again", 1);
                        } else {
                            view.onError("Something went wrong... Please try again", 1);
                        }
                    }
                });

            } catch (Exception ex) {
               // progressDoalog.dismiss();
                String str = ex.toString();
                Log.e("Exception", str);
                view.onError(ex.toString(), 1);
            }
        }
    }

    @Override
    public void teamAttendanceListFilter(String list, String param, Map<String, String> filter) {
        if (NetworkInformation.isConnected(context)) {
            try {
                AttendanceApi service = RetrofitClientInstance.getRetrofitInstance().create(AttendanceApi.class);
                Call<List<AttenApproval>> call = service.GetAttenInfoNew(param, filter);
                call.enqueue(new Callback<List<AttenApproval>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<AttenApproval>> call, @NonNull Response<List<AttenApproval>> response) {
                        Log.d("TAG", "onResponse: " + response.body());
                        System.out.println("resp: " + response.body());
                        if (response.body() != null&&response.body().size()>0) {
                            List<AttenApproval> approval = response.body();
                            System.out.println("Response :" + approval);
                            view.onSuccessTeamAtten(approval);

                        } else {
                            view.onSuccessTeamAtten(null);
                        }

                    }

                    @Override
                    public void onFailure(@NonNull Call<List<AttenApproval>> call, @NonNull Throwable t) {
                        if (t instanceof SocketTimeoutException) {
                            view.onError("Slow Connection Detected. Please try again", 1);
                        } else {
                            view.onError("Something went wrong... Please try again", 1);
                        }
                    }
                });

            } catch (Exception ex) {
                String str = ex.toString();
                Log.e("Exception", str);
                view.onError(ex.toString(), 1);
            }
        }
    }

    //TODO:team Attendance Approve
    @Override
    public void teamAttendanceApprove(ApproveRQ approveRQ) {
        if (NetworkInformation.isConnected(context)) {

            try {
                AttendanceApi service = RetrofitClientInstance.getRetrofitInstance().create(AttendanceApi.class);
                Call<ResultInfo> call = service.ApproveApi(approveRQ);
                call.enqueue(new Callback<ResultInfo>() {
                    @Override
                    public void onResponse(@NonNull Call<ResultInfo> call, @NonNull Response<ResultInfo> response) {
                        ResultInfo rp=response.body();
                        if (rp != null) {
                            if (rp.isSuccess) {
                                try {
                                    view.onSuccessapprove(true);
                                } catch (Exception exception) {
                                    //exception.printStackTrace();
                                }
                            }
                        } else {
                            view.onError("No Response Found",1);
                            //  SnackBarManagement._warning_CustomMessage(v,"No Respone Found");
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ResultInfo> call, @NonNull Throwable t) {
                        if (t instanceof SocketTimeoutException) {
                            view.onError("Slow Connection Detected. Please try again", 1);
                        } else {
                            view.onError("Something went wrong... Please try again", 1);
                        }
                    }
                });

            } catch (Exception ex) {
             //   progressDoalog.dismiss();
                String str = ex.toString();
                Log.e("Exception", str);
                view.onError(ex.toString(), 1);
            }
        }
    }

    @Override
    public void teamAttendanceApproveAll(ApproveRQ approveRQ) {
        if (NetworkInformation.isConnected(context)) {
            try {
                AttendanceApi service = RetrofitClientInstance.getRetrofitInstance().create(AttendanceApi.class);
                Call<ResultInfo> call = service.ApproveApi(approveRQ);
                call.enqueue(new Callback<ResultInfo>() {
                    @Override
                    public void onResponse(@NonNull Call<ResultInfo> call, @NonNull Response<ResultInfo> response) {
                        if (response.body() != null && response.body().isSuccess) {
                            view.onSuccessapprove(true);
                        } else {
                            view.onError("No Response Found",1);
                        }
                    }
                    @Override
                    public void onFailure(@NonNull Call<ResultInfo> call, @NonNull Throwable t) {
                        if (t instanceof SocketTimeoutException) {
                            view.onError("Slow Connection Detected. Please try again", 1);
                        } else {
                            view.onError("Something went wrong... Please try again", 1);
                        }
                    }
                });

            } catch (Exception ex) {
                //   progressDoalog.dismiss();
                String str = ex.toString();
                Log.e("Exception", str);
                view.onError(ex.toString(), 1);
            }
        }
    }

    @Override
    public void button(int empid) {
        if(NetworkInformation.isConnected(context)){
            try{
                AttendanceApi service = RetrofitClientInstanceAttendance.getRetrofitInstance().create(AttendanceApi.class);
                Call<ButtonRP> call = service.GetButtonStatus(empid);
                call.enqueue(new Callback<ButtonRP>() {
                    @Override
                    public void onResponse(@NonNull Call<ButtonRP> call, @NonNull Response<ButtonRP> response) {
                        if(response.body()!=null)
                        {
                            ButtonRP info =response.body();
                            view.onButtonView(info);
                        }else {
                            view.onError("Slow Connection Detected. Please try again",1);
                        }

                    }
                    @Override
                    public void onFailure(@NonNull Call<ButtonRP> call, @NonNull Throwable t) {
                        if(t instanceof SocketTimeoutException){
                            view.onError("Slow Connection Detected. Please try again",1);
                        }else{
                            view.onError("Something went wrong... Please try again",1);
                        }
                    }
                });

            }catch (Exception ex){
                String str = ex.toString();
                Log.e("Exception",str);
                view.onError(str,1);
            }
        }
    }


}
