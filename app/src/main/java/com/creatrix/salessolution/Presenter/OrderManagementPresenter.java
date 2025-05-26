package com.creatrix.salessolution.Presenter;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.creatrix.salessolution.Activity.Approval.Order.OrderApprovalData;
import com.creatrix.salessolution.Activity.OrderProcess.Model.OrderMasterModel;
import com.creatrix.salessolution.DBAdapter.DBCrudHelper;
import com.creatrix.salessolution.DBInfo.DBHelperMain;
import com.creatrix.salessolution.Interface.IOrderManagement;
import com.creatrix.salessolution.Model.CordinateUpdate;
import com.creatrix.salessolution.Model.OrderMaster;
import com.creatrix.salessolution.Model.OrderMasterNew;
import com.creatrix.salessolution.Model.Product;
import com.creatrix.salessolution.Model.ResultInfo;
import com.creatrix.salessolution.Model.User;
import com.creatrix.salessolution.Network.GetDataService;
import com.creatrix.salessolution.Network.OrderProcessAPICALL;
import com.creatrix.salessolution.Network.RetrofitClientInstance;
import com.creatrix.salessolution.Network.RetrofitClientOrderProcessInstance;
import com.creatrix.salessolution.ResposeModel.ResponseInfo;

import java.net.SocketTimeoutException;

import okhttp3.HttpUrl;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderManagementPresenter implements IOrderManagement.Presenter {
    IOrderManagement.View view;
    ProgressDialog progressDoalog;
    DBHelperMain dbHelperMain;
    DBCrudHelper dbCrudHelper;
    Context context;

    public OrderManagementPresenter(IOrderManagement.View view, Context context) {
        this.view = view;
        this.context = context;
        dbHelperMain = new DBHelperMain(context);
        dbCrudHelper = new DBCrudHelper(context);
    }

    @Override
    public void makeOrder(OrderMaster aOrder) {
        progressDoalog = new ProgressDialog((Context) view);
        progressDoalog.setMessage("Requirement is Processing.... Please wait");
        progressDoalog.show();
        progressDoalog.setCanceledOnTouchOutside(false);
        try {
            GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
          //  Call<ResultInfo> call = service.MakeOrder(aOrder);
            Call<ResultInfo> call = service.MakeSampleOrder(aOrder);
            call.enqueue(new Callback<ResultInfo>() {
                @Override
                public void onResponse(@NonNull Call<ResultInfo> call, @NonNull Response<ResultInfo> response) {
                    progressDoalog.dismiss();
                    try {
                        ResultInfo info = response.body();
                        Log.e("Error", response.body().toString());
                        String st = "false";
                        if (info.getSuccess() == true) {
                            view.onOrderSuccess("This Order has been Submitted Successfully","who");
                        }else {
                            view.onOrderError("Order Not Submitted..Try Again");
                        }
                    } catch (Exception exception) {
                        view.onOrderError("Something went wrong... Please try again");
                    }
                }
                @Override
                public void onFailure(@NonNull Call<ResultInfo> call, @NonNull Throwable t) {
                    progressDoalog.dismiss();
                    if (t instanceof SocketTimeoutException) {
                        view.onOrderError("Slow Connection Detected. Please try again");
                    } else {
                        view.onOrderError("Something went wrong... Please try again");
                    }
                }
            });

        } catch (Exception ex) {
            progressDoalog.dismiss();
            String str = ex.toString();
            Log.e("Exception", str);
            view.onOrderError(ex.toString());
        }

    }
    @SuppressLint("Range")
    public void draftOrderWithSynced(OrderMaster aOrder) {
        SQLiteDatabase database = dbHelperMain.getWritableDatabase();
        int orderId = 0;
        String MioCode = "";
        String CustomerCode = "";
        String CustomerName = "";
        String SubmissionDate = "";
        try {

            String insertQuery = "Insert into tblOrderMaster(EmpId,ComUnitId,MIOCode,CustomerCode,CustomerName,SubmissionDate,CollectionDate,Status) " +
                    "values('" + aOrder.getEmpId() + "','" + aOrder.getComunitId() + "','" + aOrder.getMioCode() + "','" + aOrder.getCustomerCode() + "','" + aOrder.getCustomerName() + "','" + aOrder.getSubmittedDate() + "','" + aOrder.getCollectionDate() + "','synced')";
            database.execSQL(insertQuery);
            Cursor cursor = database.rawQuery("SELECT OrderIdLocal FROM tblOrderMaster ORDER BY OrderIdLocal DESC LIMIT 1", null);

            int id = 0;
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    id = cursor.getInt(cursor.getColumnIndex("OrderIdLocal"));
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

            for (int i = 0; i < aOrder.getOrderDetails().size(); i++) {
                Product aInfo = aOrder.getOrderDetails().get(i);
                ProductId = aInfo.getProductId();
                ProductName = aInfo.getProductName();
                Quantity = aInfo.getQuantity();
                UnitPrice = aInfo.getUnitPrice().toString();
                VatPercentage = aInfo.getVatPercentage().toString();
                String insertDetal = "Insert into tblOrderDetails(OrderIdLocal,ProductId,ProductName,Quantity,UnitPrice,VatPercentage) " +
                        "values('" + id + "','" + ProductId + "','" + ProductName + "','" + Quantity + "','" + UnitPrice + "','" + VatPercentage + "')";
                database.execSQL(insertDetal);
                view.onOrderSuccess("This Order has been Submitted Successfully","Draft");
            }

            database.close();
        } catch (Exception exception) {
            database.close();
            view.onOrderError("Some Error Occurred Submitting Current Order.. Please try again after some times");
            Log.e("EX", exception.toString());

        }
    }

    @SuppressLint("Range")
    @Override
    public void draftOrder(OrderMaster aOrder) {
        SQLiteDatabase database = dbHelperMain.getWritableDatabase();
        int orderId = 0;
        String MioCode = "";
        String CustomerCode = "";
        String CustomerName = "";
        String SubmissionDate = "";
        try {
            String insertQuery = "Insert into tblOrderMaster(EmpId,ComUnitId,MIOCode,CustomerCode,CustomerMasterId,CustomerName,CustomerAdress,SubmissionDate,CollectionDate,DeliveryDate,Remarks,PaymentType) " +
                    "values('" + aOrder.getEmpId() + "','" + aOrder.getComunitId() + "','" + aOrder.getMioCode() + "','" + aOrder.getCustomer().getCustomerCode() + "','" + aOrder.getCustomer().getCustomerMasterId() + "','" + aOrder.getCustomer().getCustomerName() + "','" + aOrder.getCustomer().getAddress() + "','" + aOrder.getSubmittedDate() + "','" + aOrder.getCollectionDate() + "','" + aOrder.getDeliveryDate() + "','" + aOrder.getRemarks() + "','" + aOrder.getPaymentType() + "')";
            database.execSQL(insertQuery);
            Cursor cursor = database.rawQuery("SELECT OrderIdLocal FROM tblOrderMaster ORDER BY OrderIdLocal DESC LIMIT 1", null);
            int id = 0;
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    id = cursor.getInt(cursor.getColumnIndex("OrderIdLocal"));
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
            for (int i = 0; i < aOrder.getOrderDetails().size(); i++) {
                Product aInfo = aOrder.getOrderDetails().get(i);
                ProductId = aInfo.getProductId();
                ProductName = aInfo.getProductName();
                Quantity = aInfo.getQuantity();
                UnitPrice = aInfo.getUnitPrice().toString();
                VatPercentage = aInfo.getVatPercentage().toString();
                String insertDetal = "Insert into tblOrderDetails(OrderIdLocal,ProductId,ProductName,Quantity,UnitPrice,VatPercentage) " +
                        "values('" + id + "','" + ProductId + "','" + ProductName + "','" + Quantity + "','" + UnitPrice + "','" + VatPercentage + "')";
                database.execSQL(insertDetal);
               // view.onOrderSuccess("This Order has been Drafted Successfully");
                view.onOrderDraftSuccess("This Order has been Drafted Successfully");
            }
            database.close();
        } catch (Exception exception) {
            database.close();
            view.onOrderError("Some Error Occurred Drafting Current Order.. Please try again after some times");
            Log.e("EX", exception.toString());

        }


    }

    @Override
    public void SyncOrder(OrderMaster aOrder, int localOrderId) {
        progressDoalog = new ProgressDialog((Context) view);
        progressDoalog.setMessage("Order is Processing.... Please wait");
        progressDoalog.show();
        progressDoalog.setCanceledOnTouchOutside(false);
        try {
            GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
            Call<ResultInfo> call = service.MakeOrder(aOrder);
            call.enqueue(new Callback<ResultInfo>() {
                @Override
                public void onResponse(Call<ResultInfo> call, Response<ResultInfo> response) {
                    progressDoalog.dismiss();
                    ResultInfo info = response.body();
                    Log.e("Error", response.body().toString());
                    String st = "false";
                    if (info.getSuccess() == true) {
                        st = "Order Has Been Synced Successfully";
                        dbCrudHelper.UpdateOrderSynchInfo(localOrderId);
                        view.onOrderSuccess(st,"Sync");
                    }
                }

                @Override
                public void onFailure(Call<ResultInfo> call, Throwable t) {
                    progressDoalog.dismiss();
                    if (t instanceof SocketTimeoutException) {
                        view.onOrderError("Slow Connection Detected. Please try again");
                    } else {
                        view.onOrderError("Something went wrong... Please try again");
                    }
                }
            });

        } catch (Exception ex) {
            progressDoalog.dismiss();
            String str = ex.toString();
            Log.e("Exception", str);
            view.onOrderError(ex.toString());
        }
    }

    @Override
    public void UpdateCustomerLocation(CordinateUpdate location) {
        progressDoalog = new ProgressDialog((Context) view);
        progressDoalog.setMessage("Updating Location.... Please wait");
        progressDoalog.show();
        progressDoalog.setCanceledOnTouchOutside(false);
        try {
            OrderProcessAPICALL service = RetrofitClientOrderProcessInstance.getRetrofitInstance().create(OrderProcessAPICALL.class);
            Call<ResponseInfo> call = service.UpdateLocation(location);
            HttpUrl ds = call.request().url();
            call.enqueue(new Callback<ResponseInfo>() {
                @Override
                public void onResponse(Call<ResponseInfo> call, Response<ResponseInfo> response) {
                    progressDoalog.dismiss();
                    try {
                        ResponseInfo info = response.body();
                        if (info != null) {
                            if (info.getMessage().equals("Success")) {
                                view.onGenericSuccess("Customer location updated");
                            }
                        } else {
                            view.onGenericError("Something went wrong... Please try again");

                        }

                    } catch (Exception exception) {
                        view.onGenericError("Something went wrong... Please try again");
                    }

                }

                @Override
                public void onFailure(Call<ResponseInfo> call, Throwable t) {
                    progressDoalog.dismiss();
                    if (t instanceof SocketTimeoutException) {
                        view.onGenericError("Slow Connection Detected. Please try again");
                    } else {
                        view.onGenericError("Something went wrong... Please try again");
                    }
                }
            });

        } catch (Exception ex) {
            progressDoalog.dismiss();
            view.onGenericError(ex.toString());
        }

    }

    @Override
    // public void makeOrder2(OrderMasterNew aOrder) {
    public void makeOrder2(OrderMasterModel aOrder,String Who) {
        progressDoalog = new ProgressDialog((Context) view);
        progressDoalog.setMessage("Order is Processing.... Please wait");
        progressDoalog.show();
        progressDoalog.setCanceledOnTouchOutside(false);
        try {
           // Toast.makeText(context, "master id : "+aOrder.getCampaignMasters(), Toast.LENGTH_SHORT).show();
            OrderProcessAPICALL service = RetrofitClientOrderProcessInstance.getRetrofitInstance().create(OrderProcessAPICALL.class);
            Call<ResultInfo> call = service.SubmitOrder2(aOrder);
            HttpUrl ds = call.request().url();
            call.enqueue(new Callback<ResultInfo>() {
                @Override
                public void onResponse(Call<ResultInfo> call, Response<ResultInfo> response) {
                    try {
                        ResultInfo info = response.body();
                        Log.e("Error", response.body().toString());
                        String st = "false";
                        if (info.getSuccess() == true) {
                            progressDoalog.dismiss();
                            view.onOrderSuccess("This Order has been Submitted Successfully",Who);
                        }else {
                            progressDoalog.dismiss();
                           // view.onOrderError("Something went wrong... Please try again");
                            view.onOrderError(info.getErrormessagenew());
                        }
                    } catch (Exception exception) {
                        progressDoalog.dismiss();
                        view.onOrderError("Something went wrong... Please try again");
                    }

                }

                @Override
                public void onFailure(Call<ResultInfo> call, Throwable t) {
                    progressDoalog.dismiss();
                    if (t instanceof SocketTimeoutException) {
                        view.onOrderError("Slow Connection Detected. Please try again");
                    } else {
                        view.onOrderError("Something went wrong... Please try again");
                    }
                }
            });

        } catch (Exception ex) {
            progressDoalog.dismiss();
            String str = ex.toString();
            Log.e("Exception", str);
            view.onOrderError(ex.toString());
        }

    }

    @Override
    public void draftOrder2(OrderMasterNew aOrder) {
        SQLiteDatabase database = dbHelperMain.getWritableDatabase();
        int orderId = 0;
        String MioCode = "";
        String CustomerCode = "";
        String CustomerName = "";
        String SubmissionDate = "";
//        try {
//
//
//            String insertQuery = "Insert into tblOrderMaster(EmpId,ComUnitId,MIOCode,CustomerCode,CustomerName,SubmissionDate,CollectionDate,Remarks) " +
//                    "values('"+ aOrder.getEmpId()+"','"+aOrder.getComunitId()+"','"+aOrder.getMioCode()+"','"+aOrder.getCustomerCode()+"','"+aOrder.getCustomerName()+"','"+aOrder.getSubmittedDate()+"','"+aOrder.getCollectionDate()+"','"+aOrder.getRemarks()+"')";
//            database.execSQL(insertQuery);
//            Cursor cursor = database.rawQuery("SELECT OrderIdLocal FROM tblOrderMaster ORDER BY OrderIdLocal DESC LIMIT 1",null);
//
//            int id = 0;
//            if (cursor.getCount() > 0) {
//                while (cursor.moveToNext()) {
//                    id = cursor.getInt(cursor.getColumnIndex("OrderIdLocal"));
//                }
//            }
//
//            int ProductId = 0;
//            String ProductCode = "";
//            String ProductName = "";
//            int Quantity = 0;
//            String PackSize = "";
//            String UnitPrice = "";
//            String VatPercentage = "";
//            String VatAmountPerunit = "";
//
//            for (int i=0;i<aOrder.getOrderDetails().size();i++){
//                Product aInfo = aOrder.getOrderDetails().get(i);
//                ProductId =aInfo.getProductId();
//                ProductName =aInfo.getProductName();
//                Quantity =aInfo.getQuantity();
//                UnitPrice = aInfo.getUnitPrice().toString();
//                VatPercentage =aInfo.getVatPercentage().toString();
//                String insertDetal = "Insert into tblOrderDetails(OrderIdLocal,ProductId,ProductName,Quantity,UnitPrice,VatPercentage) " +
//                        "values('"+ id+"','"+ProductId+"','"+ProductName+"','"+Quantity+"','"+UnitPrice+"','"+VatPercentage+"')";
//                database.execSQL(insertDetal);
//                view.onOrderSuccess("This Order has been Drafted Successfully");
//            }
//
//            database.close();
//        }
//        catch (Exception exception){
//            database.close();
//            view.onOrderError("Some Error Occurred Drafting Current Order.. Please try again after some times");
//            Log.e("EX",exception.toString());
//
//        }

    }


}
