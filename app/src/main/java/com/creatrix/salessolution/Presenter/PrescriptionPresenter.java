package com.creatrix.salessolution.Presenter;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import androidx.annotation.NonNull;

import com.creatrix.salessolution.DBAdapter.DBCrudHelper;
import com.creatrix.salessolution.DBAdapter.DBDoctor.DBDoctorHelper;
import com.creatrix.salessolution.DBAdapter.ProductSQLiteHelper;
import com.creatrix.salessolution.DBInfo.DBHelperMain;
import com.creatrix.salessolution.Interface.IPrescription;
import com.creatrix.salessolution.Model.PrescriptionSM;
import com.creatrix.salessolution.Model.Product;
import com.creatrix.salessolution.Model.ResultInfo;
import com.creatrix.salessolution.Network.ApiDoctorCall;
import com.creatrix.salessolution.Network.RetrofitClientInstance;

import java.net.SocketTimeoutException;

import okhttp3.HttpUrl;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PrescriptionPresenter implements IPrescription.Presenter {
    IPrescription.View view;
    Context context;
    ProgressDialog progressDoalog;
    DBHelperMain dbHelperMain;
    ProductSQLiteHelper pHelper;
    DBCrudHelper dbCrudHelper;
    DBDoctorHelper dbdHelper;

    public PrescriptionPresenter(IPrescription.View view, Context context) {
        this.view = view;
        this.context = context;
        dbHelperMain=new DBHelperMain(context);
        pHelper=new ProductSQLiteHelper(context);
        dbCrudHelper=new DBCrudHelper(context);
        dbdHelper=new DBDoctorHelper(context);
    }

    @Override
    public void GetPrescriptionType(int empId) {
        try {
            view.onPrescriptionTypeGet(dbCrudHelper.getPrescriptionTypeListFromSQLite());
           /* ApiDoctorCall service = RetrofitClientInstance.getRetrofitInstance().create(ApiDoctorCall.class);
            Call<List<PrescriptionTYpe>> call = service.GetPrescriptionType(empId);
            HttpUrl ds = call.request().url();
            call.enqueue(new Callback<List<PrescriptionTYpe>>() {
                @Override
                public void onResponse(Call<List<PrescriptionTYpe>> call, Response<List<PrescriptionTYpe>> response) {
                    view.onPrescriptionTypeGet(response.body());
                    Log.d("TAG", "onResponse: Success");
                }

                @Override
                public void onFailure(Call<List<PrescriptionTYpe>> call, Throwable t) {
                    if (t instanceof SocketTimeoutException) {
                        Log.e("TAG", "onFailure: ", t);
                    } else {
                        Log.e("TAG", "onFailure: ", t);
                    }
                }
            });*/

        } catch (Exception ex) {
            Log.e("TAG", "onFailure: ", ex);
        }

    }

    @Override
    public void GetProducts(int empId) {

        try {

            view.onProductGet(pHelper.getProductFromDB());

            /*ProductApi service = RetrofitClientInstance.getRetrofitInstance().create(ProductApi.class);
            Call<List<Product>> call = service.GetProductList(empId);
            HttpUrl ds = call.request().url();
            call.enqueue(new Callback<List<Product>>() {
                @Override
                public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                    view.onProductGet(response.body());
                }

                @Override
                public void onFailure(Call<List<Product>> call, Throwable t) {
                    if (t instanceof SocketTimeoutException) {
                    } else {
                    }
                }
            });*/

        } catch (Exception ex) {
        }

    }

    @Override
    public void GetChamberId(int docId) {
        try {
            view.onChamberGet(dbdHelper.getChamberIdListFromSQLite(docId));
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
    @SuppressLint("Range")
    @Override
    public void SavePrescription(PrescriptionSM aPres) {
        progressDoalog = new ProgressDialog(context);
        progressDoalog.setMessage("Prescription is Saving.... Please wait");
        progressDoalog.show();
        progressDoalog.setCanceledOnTouchOutside(false);

        SQLiteDatabase database = dbHelperMain.getWritableDatabase();
        try {

            String insertpQuery = "Insert into tblPrescriptionInfo(DoctorId,SessionUser,PrescriptionDate,PrescriptionTypeId,ChemberId,ImageString) " +
                    "values('" + aPres.getDoctorId() + "','" + aPres.getSessionUser() + "','" + aPres.getPrescriptionDate() + "','" + aPres.getPrescriptionTypeId() + "','" + aPres.getChemberId() + "','" + aPres.getImageString() + "')";
            database.execSQL(insertpQuery);
            Cursor cursor= database.rawQuery("SELECT * FROM tblPrescriptionInfo where PrescripId order by  PrescripId desc LIMIT 1", null);
           // Cursor cursor = database.rawQuery("SELECT PrescripId FROM tblPrescriptionMaster ORDER BY PrescripId DESC LIMIT 1", null);
            progressDoalog.dismiss();
            int id = 0;
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    id = cursor.getInt(cursor.getColumnIndex("PrescripId"));
                }
            }

            int ProductId = 0;
            String ProductCode = "";
            String ProductName = "";
            int Quantity = 0;
            String PackSize = "";
            String UnitPrice = "";
            String VatPercentage = "";
            String VatAmountPerunit = "";

            for (int i = 0; i < aPres.getaProList().size(); i++) {
                Product aInfo = aPres.getaProList().get(i);
                ProductId = aInfo.getProductId();

                String insertDetal = "Insert into tblPrescriptionMaster(PrescripId,ProductId,DoctorId) " +
                        "values('" + id + "','" + ProductId + "','" + aPres.getDoctorId() + "')";
                database.execSQL(insertDetal);
                view.onSaveSuccess("Prescription Saved Offline Successfully");
                progressDoalog.dismiss();
            }

            database.close();
        } catch (Exception exception) {
            progressDoalog.dismiss();
            database.close();
            view.onSaveError("Some Error Occurred Drafting Prescription.. Please try again after some times");
            Log.e("EX", exception.toString());

        }


    }


    @Override
    public void SubmitPrescription(PrescriptionSM aPres) {
     /*   ProgressDialog progressDoalog = new ProgressDialog(context);
        progressDoalog.setMessage("Prescription is Saving.... Please wait");
        progressDoalog.show();
        progressDoalog.setCanceledOnTouchOutside(false);*/
        try {
            ApiDoctorCall service = RetrofitClientInstance.getRetrofitInstance().create(ApiDoctorCall.class);
            Call<ResultInfo> call = service.SavePrescription(aPres);
            HttpUrl ds = call.request().url();
            call.enqueue(new Callback<ResultInfo>() {
                @Override
                public void onResponse(@NonNull Call<ResultInfo> call, @NonNull Response<ResultInfo> response) {
                    //progressDoalog.dismiss();
                    ResultInfo info = response.body();
                    if (info != null) {
                        if (info.getSuccess() == true) {
                            view.onSaveSuccess("Prescription Successfully Submitted");

                        } else {
                            view.onSaveError("Some error occurred... Please try again");
                        }

                    } else {
                        view.onSaveError("Slow Internet Detected..Please try again");

                    }

                }

                @Override
                public void onFailure(@NonNull Call<ResultInfo> call, @NonNull Throwable t) {
                    //progressDoalog.dismiss();
                    if (t instanceof SocketTimeoutException) {
                        view.onSaveError("Slow Internet Detected..Please try again");
                    } else {
                        view.onSaveError("Some error occurred..Please try again");
                    }
                }
            });

        } catch (Exception ex) {
           // progressDoalog.dismiss();
            String str = ex.toString();
            Log.e("Exception", str);
            view.onSaveError("Some error occurred..Please try again");

        }
    }

}
