package com.creatrix.salessolution.Presenter;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.creatrix.salessolution.DBAdapter.DBCrudHelper;
import com.creatrix.salessolution.DBAdapter.DBDoctor.DBDoctorHelper;
import com.creatrix.salessolution.DBAdapter.ProductSQLiteHelper;
import com.creatrix.salessolution.Interface.IDCR;
import com.creatrix.salessolution.Model.DcrSM;
import com.creatrix.salessolution.Model.ResultInfo;
import com.creatrix.salessolution.Model.TourTypeViewModel;
import com.creatrix.salessolution.Model.UserByRole;
import com.creatrix.salessolution.Model.UserRole;
import com.creatrix.salessolution.Network.ApiDoctorCall;
import com.creatrix.salessolution.Network.RetrofitClientInstance;
import com.creatrix.salessolution.Services.Constants;

import java.net.SocketTimeoutException;
import java.util.List;

import okhttp3.HttpUrl;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DCRPresenter implements IDCR.Presenter {
    IDCR.View view;
    Context context;
    DBCrudHelper dbCrudHelper;
    ProductSQLiteHelper phelper;
    DBDoctorHelper dhelper;

    public DCRPresenter(IDCR.View view,Context context) {
        this.view = view;
        this.context = context;

        dbCrudHelper=new DBCrudHelper(context);
        phelper=new ProductSQLiteHelper(context);
        dhelper=new DBDoctorHelper(context);
    }

    @Override
    public void GetVisitType() {
        try{
            view.OnVisitTypeGet(dhelper.getVisitTypeIdListFromSQLite());

        }catch (Exception ex){
        }
    }

    @Override
    public void GetChamber(int doctorId) {
        try{
            view.OnChamberGet(dhelper.getChamberIdListFromSQLite(doctorId));

        }catch (Exception ex){
        }

    }

    @Override
    public void GetGiftProduct(String empId) {
        try{
            view.OnGiftProductGet(phelper.getProductGiftFromDB());

        }catch (Exception ex){
        }

    }

    @Override
    public void GetDoctorBrand(int docId) {
        try{
            view.OnDoctorBrandGet(dhelper.getDoctorBrandListFromSQLite(docId));

        }catch (Exception ex){
        }
    }

    @Override
    public void GetSampleProduct(String empId) {
        try{
            view.OnSampleProductGet(phelper.getProductSampleFromDB());
        }catch (Exception ex){
        }

    }

    @Override
    public void SaveDCR(DcrSM aInfo) {
        ProgressDialog progressDoalog = new ProgressDialog(context);
        progressDoalog.setMessage("DCR is Saving.... Please wait");
        progressDoalog.show();
        progressDoalog.setCanceledOnTouchOutside(false);
        try{
            ApiDoctorCall service = RetrofitClientInstance.getRetrofitInstance().create(ApiDoctorCall.class);
            Call<ResultInfo> call = service.SaveDcr(aInfo);
            call.enqueue(new Callback<ResultInfo>() {
                @Override
                public void onResponse(@NonNull Call<ResultInfo> call, @NonNull Response<ResultInfo> response) {
                    progressDoalog.dismiss();
                    ResultInfo info =response.body();
                    if(info !=null){
                        if(info.getSuccess() == true){
                            view.OnDcrSaveSuccess("DCR Successfully Submitted");
                        }else if(info.getValiCheck()==true)
                        {
                            view.OnDcrSaveSuccess("Insufficient");
                        }
                        else {
                           // view.OnDcrSaveError("Some error occurred... Please try again");
                            Toast.makeText(context, "Some error occurred... Please try again", Toast.LENGTH_SHORT).show();
                        }

                    }else{
                        view.OnDcrSaveError("Some error occurred..Please try again");

                    }

                }
                @Override
                public void onFailure(@NonNull Call<ResultInfo> call, @NonNull Throwable t) {
                    progressDoalog.dismiss();
                    if(t instanceof SocketTimeoutException){
                        view.OnDcrSaveError("Slow Internet Detected..Please try again");

                    }else{
                        view.OnDcrSaveError("Failed to submit..Please try again");
                    }
                }
            });

        }catch (Exception ex){
            progressDoalog.dismiss();
            String str = ex.toString();
            Log.e("Exception",str);
            view.OnDcrSaveError("Exception occurred :"+str);

        }
    }

    @Override
    public void GetUserRole() {
        try{
            view.onUserRoleGet(dhelper.getRoleListFromSQLite());

        }catch (Exception ex){
        }
       /* try{

            ApiDoctorCall service = RetrofitClientInstance.getRetrofitInstance().create(ApiDoctorCall.class);
            Call<List<UserRole>> call = service.GetUserRole();
            call.enqueue(new Callback<List<UserRole>>() {
                @Override
                public void onResponse(Call<List<UserRole>> call, Response<List<UserRole>> response) {
                    view.onUserRoleGet(response.body());
                }
                @Override
                public void onFailure(Call<List<UserRole>> call, Throwable t) {
                    if(t instanceof SocketTimeoutException){
                    }else{
                    }
                }
            });

        }catch (Exception ex){
        }*/
    }

    @Override
    public void GetUserByRoleId(int roleid/*, int empId*/) {
        try{
            view.onUserGet(dhelper.getRolewiseUser(roleid));

        }catch (Exception ex){
        }
       /* try{
            ApiDoctorCall service = RetrofitClientInstance.getRetrofitInstance().create(ApiDoctorCall.class);
            Call<List<UserByRole>> call = service.GetUserRoleByRoleId(id,empId);
            HttpUrl ds = call.request().url();

            call.enqueue(new Callback<List<UserByRole>>() {
                @Override
                public void onResponse(Call<List<UserByRole>> call, Response<List<UserByRole>> response) {
                    view.onUserGet(response.body());
                }
                @Override
                public void onFailure(Call<List<UserByRole>> call, Throwable t) {
                    if(t instanceof SocketTimeoutException){
                    }else{
                    }
                }
            });

        }catch (Exception ex){
        }*/

    }

    @Override
    public void GetNoneffective() {
        try {
            view.onNoneffectiveGet(dbCrudHelper.getNoneffective_SQLite());
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }


}
