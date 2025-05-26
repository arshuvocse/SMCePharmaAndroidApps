package com.creatrix.salessolution.Presenter;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import androidx.annotation.NonNull;

import com.creatrix.salessolution.DBAdapter.ProductSQLiteHelper;
import com.creatrix.salessolution.DBInfo.DBHelperMain;
import com.creatrix.salessolution.Interface.IProduct;
import com.creatrix.salessolution.Model.Product;
import com.creatrix.salessolution.Model.ProductSample;
import com.creatrix.salessolution.Model.mCompanyUnit;
import com.creatrix.salessolution.Network.GetDataService;
import com.creatrix.salessolution.Network.ProductApi;
import com.creatrix.salessolution.Network.RetrofitClientInstance;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.HttpUrl;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductPresenter implements IProduct.Presenter {
    ProgressDialog progressDoalog;
    IProduct.View view;
    ProductSQLiteHelper productSQLiteHelper;
    DBHelperMain dbHelper;
    Context context;


    public ProductPresenter(IProduct.View view,Context context) {
        this.view = view;
        this.productSQLiteHelper = new ProductSQLiteHelper(context);
        this.dbHelper = new DBHelperMain(context);
        this.context = context;
    }
    @Override
    public void getProductsFromServer(int empId) {
        progressDoalog = new ProgressDialog(context);
        progressDoalog.setMessage("Synchronizing....");
        progressDoalog.show();
        progressDoalog.setCanceledOnTouchOutside(false);
        try{
            ProductApi service = RetrofitClientInstance.getRetrofitInstance().create(ProductApi.class);
            Call<List<Product>> call = service.GetProductList(empId);
            call.enqueue(new Callback<List<Product>>() {
                @Override
                public void onResponse(@NonNull Call<List<Product>> call, @NonNull Response<List<Product>> response) {
                    progressDoalog.dismiss();
                    progressDoalog.dismiss();
                    InsertIntoProductTable(response.body());
                }
                @Override
                public void onFailure(@NonNull Call<List<Product>> call, @NonNull Throwable t) {
                    progressDoalog.dismiss();
                    if(t instanceof SocketTimeoutException){
                        view.OnError("Slow Network detected");
                    }else{
                        view.OnError("Some Error occured");

                    }


                }
            });

        }catch (Exception ex){
            view.OnError("Some Error occured");
            progressDoalog.dismiss();
        }

    }

    @Override
    public void getCompanyUnitFromServer(int empId) {
        try{
            GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
            Call<List<mCompanyUnit>> call = service.GetCompanyUnit(empId);
            HttpUrl ds = call.request().url();
            call.enqueue(new Callback<List<mCompanyUnit>>() {
                @Override
                public void onResponse(@NonNull Call<List<mCompanyUnit>> call, @NonNull Response<List<mCompanyUnit>> response) {
                    InsertIntoCompanyUnitTable(response.body());
                }
                @Override
                public void onFailure(@NonNull Call<List<mCompanyUnit>> call, @NonNull Throwable t) {
                    if(t instanceof SocketTimeoutException){
                        view.OnError("Slow Network detected");
                    }else{
                        view.OnError("Some Error occured - ComUnit");
                    }
                }
            });
        }catch (Exception ex){
            view.OnError("Some Error occured - ComUnit");
        }

    }

    @Override
    public void InsertIntoProductTable(List<Product> aProduct) {
        productSQLiteHelper.InsertIntoProductTable(aProduct);
    }

    public void InsertIntoCompanyUnitTable(List<mCompanyUnit> mList){
        try{
            String tableName = "tblCompanyUnit";
            int ComUnitId = 0;
            String ComUnitName = "";
            if(productSQLiteHelper.CheckDataInTable(tableName)){
                productSQLiteHelper._deleteAllRecordsFromaTable(tableName);
            }
            SQLiteDatabase database = dbHelper.getWritableDatabase();
            for (int i=0;i<mList.size();i++){
                mCompanyUnit aInfo = mList.get(i);
                ComUnitId =aInfo.getComUnitId();
                ComUnitName =aInfo.getComUnitName();
                String insertQuery = "Insert into tblCompanyUnit(ComUnitId,ComUnitName) " +
                        "values('"+ComUnitId+"','"+ComUnitName+"')";

                database.execSQL(insertQuery);
            }
        }catch (Exception exception){
            Log.e("DBEX",exception.toString());
        }
    }

    @SuppressLint("Range")
    @Override
    public void getProductFromDB(int empId) {
        List<Product> products = new ArrayList<>();
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        String sqlQuery = "Select * from tbl_ProductInfo";
        Cursor cursor;
        cursor = database.rawQuery(sqlQuery, null);
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                Product aInfo = new Product();
                aInfo.setProductId(cursor.getInt(cursor.getColumnIndex("ProductId")));
                aInfo.setProductName(cursor.getString(cursor.getColumnIndex("ProductName")));
                aInfo.setProductCode(cursor.getString(cursor.getColumnIndex("ProductCode")));
                aInfo.setProductDes(cursor.getString(cursor.getColumnIndex("ProductDes")));
                aInfo.setPackSize(cursor.getString(cursor.getColumnIndex("PackSize")));
                aInfo.setUnitPrice(cursor.getDouble(cursor.getColumnIndex("UnitPrice")));
                aInfo.setVatAmountPerunit(cursor.getDouble(cursor.getColumnIndex("VatAmountPerunit")));
                aInfo.setVatPercentage(cursor.getDouble(cursor.getColumnIndex("VatPercentage")));
                aInfo.setQuotedPrice(cursor.getDouble(cursor.getColumnIndex("QuotedPrice")));
                aInfo.setCustomerMasterId(cursor.getInt(cursor.getColumnIndex("CustomerMasterId")));

                products.add(aInfo);
            }
        }
        view.onProductsGet(products);
    }

    @SuppressLint("Range")
    @Override
    public ArrayList<mCompanyUnit> getCompanyUnitFromDB() {
        ArrayList<mCompanyUnit> aList = new ArrayList<>();

        SQLiteDatabase database = dbHelper.getWritableDatabase();

        mCompanyUnit mCompanyUnit = new mCompanyUnit();
        mCompanyUnit.setComUnitId(0);
        mCompanyUnit.setComUnitName("Select Company Unit");
        aList.add(mCompanyUnit);

        String sqlQuery = "Select * from tblCompanyUnit";
        Cursor cursor;
        cursor = database.rawQuery(sqlQuery, null);
        if (cursor.getCount() > 0) {

            while (cursor.moveToNext()) {
                mCompanyUnit aInfo = new mCompanyUnit();
                aInfo.setComUnitId(cursor.getInt(cursor.getColumnIndex("ComUnitId")));
                aInfo.setComUnitName(cursor.getString(cursor.getColumnIndex("ComUnitName")));
                aList.add(aInfo);
            }
        }
        return aList;
    }
    @SuppressLint("Range")
    @Override
    public void getSampleProducts(int empId) {
        try{
            ProductApi service = RetrofitClientInstance.getRetrofitInstance().create(ProductApi.class);
            Call<List<ProductSample>> call = service.GetSampleProductsList(empId);
            call.enqueue(new Callback<List<ProductSample>>() {
                @Override
                public void onResponse(@NonNull Call<List<ProductSample>> call, @NonNull Response<List<ProductSample>> response) {
                    view.onProductSampleGet(response.body());
                }
                @Override
                public void onFailure(@NonNull Call<List<ProductSample>> call, @NonNull Throwable t) {

                    if(t instanceof SocketTimeoutException){
                        view.OnError("Slow Network detected");
                    }else{
                        view.OnError("Some Error occured");

                    }


                }
            });

        }catch (Exception ex){
            view.OnError("Some Error occured");

        }

    }

    @Override
    public void getRegularProducts(int empId) {
        try{
            ProductApi service = RetrofitClientInstance.getRetrofitInstance().create(ProductApi.class);
            Call<List<Product>> call = service.GetProductList(empId);
            call.enqueue(new Callback<List<Product>>() {
                @Override
                public void onResponse(@NonNull Call<List<Product>> call, @NonNull Response<List<Product>> response) {
                    view.onProductsGet(response.body());

                }
                @Override
                public void onFailure(@NonNull Call<List<Product>> call, @NonNull Throwable t) {

                    if(t instanceof SocketTimeoutException){
                        view.OnError("Slow Network detected");
                    }else{
                        view.OnError("Some Error occured");

                    }


                }
            });

        }catch (Exception ex){
            view.OnError("Some Error occured");

        }

    }


}
