package com.creatrix.salessolution.DBAdapter;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;

import com.creatrix.salessolution.Activity.Approval.DA.DAListData;
import com.creatrix.salessolution.Activity.Approval.Prescription.PrescApprovalData;
import com.creatrix.salessolution.Activity.Customer.Pending.CustomerARModel;
import com.creatrix.salessolution.Activity.Doctor.DCR.NonEffectiveReason;
import com.creatrix.salessolution.Activity.Doctor.Pending.DoctorARModel;
import com.creatrix.salessolution.Activity.Expense.Model.ExpListTeam;
import com.creatrix.salessolution.Activity.MileageClaim.Model.MileageListTeam;
import com.creatrix.salessolution.DBInfo.DBHelperMain;
import com.creatrix.salessolution.Model.Attendance;
import com.creatrix.salessolution.Model.Customer;
import com.creatrix.salessolution.Model.CustomerType;
import com.creatrix.salessolution.Model.DcrSM;
import com.creatrix.salessolution.Model.Doctor.DoctorBrand;
import com.creatrix.salessolution.Model.Doctor.DoctorListViewModel;
import com.creatrix.salessolution.Model.Gift;
import com.creatrix.salessolution.Model.MarketStructure.AssignEmpTable.ASM;
import com.creatrix.salessolution.Model.MarketStructure.AssignEmpTable.MIO;
import com.creatrix.salessolution.Model.MarketStructure.AssignEmpTable.RSM;
import com.creatrix.salessolution.Model.MarketStructure.EmpInfoListModels;
import com.creatrix.salessolution.Model.MarketStructure.StructureTable.Area;
import com.creatrix.salessolution.Model.MarketStructure.StructureTable.Group;
import com.creatrix.salessolution.Model.MarketStructure.StructureTable.Market;
import com.creatrix.salessolution.Model.MarketStructure.StructureTable.Region;
import com.creatrix.salessolution.Model.MarketStructure.StructureTable.SubTeritorry;
import com.creatrix.salessolution.Model.MarketStructure.StructureTable.Teritorry;
import com.creatrix.salessolution.Model.OrderMaster;
import com.creatrix.salessolution.Model.PrescriptionSM;
import com.creatrix.salessolution.Model.PrescriptionTYpe;
import com.creatrix.salessolution.Model.Product;
import com.creatrix.salessolution.Model.ProductSample;
import com.creatrix.salessolution.Model.QuotedPrice;
import com.creatrix.salessolution.Model.StationType;
import com.creatrix.salessolution.Model.TourPurposeViewModel;
import com.creatrix.salessolution.Model.Transport;
import com.creatrix.salessolution.Model.User;
import com.creatrix.salessolution.Model.UserByRole;
import com.creatrix.salessolution.UtilityHelper.SessionManagement;
import com.creatrix.salessolution.UtilityHelper.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class DBCrudHelper {
    DBHelperMain dbHelperMain;
    Context context;
    SessionManagement sessionManagement;

    public DBCrudHelper(Context context) {
        this.context = context;
        dbHelperMain = new DBHelperMain(context);
    }

    @SuppressLint("Range")
    public ArrayList<OrderMaster> getOrderListFromSQLite() {
        ArrayList<OrderMaster> orderList = new ArrayList<>();
        SQLiteDatabase database = dbHelperMain.getWritableDatabase();
        String oderMasterQuery = "Select * from tblOrderMaster order by OrderIdLocal desc";
        try {
            Cursor cursor;
            cursor = database.rawQuery(oderMasterQuery, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    OrderMaster aInfo = new OrderMaster();
                    Customer cInfo = new Customer();
                    aInfo.setOrderIdLocal(cursor.getInt(cursor.getColumnIndex("OrderIdLocal")));
                    aInfo.setEmpId(cursor.getInt(cursor.getColumnIndex("EmpId")));
                    aInfo.setComunitId(cursor.getInt(cursor.getColumnIndex("ComUnitId")));
                    //new style to get cust info
                    cInfo.setCustomerMasterId(cursor.getInt(cursor.getColumnIndex("CustomerMasterId")));
                    cInfo.setCustomerName(cursor.getString(cursor.getColumnIndex("CustomerName")));
                    cInfo.setCustomerCode(cursor.getString(cursor.getColumnIndex("CustomerCode")));
                    cInfo.setAddress(cursor.getString(cursor.getColumnIndex("CustomerAdress")));
                    aInfo.setCustomer(cInfo);

                    aInfo.setSubmittedDate(cursor.getString(cursor.getColumnIndex("SubmissionDate")));
                    aInfo.setCollectionDate(cursor.getString(cursor.getColumnIndex("CollectionDate")));
                    aInfo.setDeliveryDate(cursor.getString(cursor.getColumnIndex("DeliveryDate")));
                    aInfo.setMioCode(cursor.getString(cursor.getColumnIndex("MIOCode")));
                    aInfo.setStatus(cursor.getString(cursor.getColumnIndex("Status")));
                    aInfo.setRemarks(cursor.getString(cursor.getColumnIndex("Remarks")));
                    aInfo.setPaymentType(cursor.getString(cursor.getColumnIndex("PaymentType")));
                    orderList.add(aInfo);
                    int OrdeId = cursor.getInt(cursor.getColumnIndex("OrderIdLocal"));
                    String oderDetailsQuery = "Select * from tblOrderDetails where OrderIdLocal = " + OrdeId + "";
                    Cursor cursor2Details;
                    cursor2Details = database.rawQuery(oderDetailsQuery, null);

                    ArrayList<Product> productArrayList = new ArrayList<>();
                    if (cursor2Details.getCount() > 0) {
                        while (cursor2Details.moveToNext()) {
                            Product aProduct = new Product();
                            aProduct.setProductId(cursor2Details.getInt(cursor2Details.getColumnIndex("ProductId")));
                            aProduct.setProductName(cursor2Details.getString(cursor2Details.getColumnIndex("ProductName")));
                            aProduct.setQuantity(cursor2Details.getInt(cursor2Details.getColumnIndex("Quantity")));
                            aProduct.setUnitPrice(Double.parseDouble(cursor2Details.getString(cursor2Details.getColumnIndex("UnitPrice"))));
                            aProduct.setVatPercentage(Double.parseDouble(cursor2Details.getString(cursor2Details.getColumnIndex("VatPercentage"))));
                            productArrayList.add(aProduct);
                        }
                    }
                    aInfo.setOrderDetails(productArrayList);

                }
            }

        } catch (Exception exception) {
            Log.e("CampOrderDetails", exception.toString());
            exception.printStackTrace();
        }
        return orderList;
    }
    //Order Main
    @SuppressLint("Range")
    public ArrayList<OrderMaster> getOrderListFromSQLiteForSingleCustomer(String customerCode) {
        ArrayList<OrderMaster> orderList = new ArrayList<>();
        SQLiteDatabase database = dbHelperMain.getWritableDatabase();
        String oderMasterQuery = "Select * from tblOrderMaster where CustomerCode = '" + customerCode + "' order by OrderIdLocal desc";
        try {
            Cursor cursor;
            cursor = database.rawQuery(oderMasterQuery, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    OrderMaster aInfo = new OrderMaster();
                    aInfo.setOrderIdLocal(cursor.getInt(cursor.getColumnIndex("OrderIdLocal")));
                    aInfo.setEmpId(cursor.getInt(cursor.getColumnIndex("EmpId")));
                    aInfo.setComunitId(cursor.getInt(cursor.getColumnIndex("ComUnitId")));
                    aInfo.setCustomerCode(cursor.getString(cursor.getColumnIndex("CustomerCode")));
                    aInfo.setCustomerName(cursor.getString(cursor.getColumnIndex("CustomerName")));
                    aInfo.setSubmittedDate(cursor.getString(cursor.getColumnIndex("SubmissionDate")));
                    aInfo.setCollectionDate(cursor.getString(cursor.getColumnIndex("CollectionDate")));
                    aInfo.setMioCode(cursor.getString(cursor.getColumnIndex("MIOCode")));
                    aInfo.setStatus(cursor.getString(cursor.getColumnIndex("Status")));
                    aInfo.setRemarks(cursor.getString(cursor.getColumnIndex("Remarks")));
                    orderList.add(aInfo);
                    int OrdeId = cursor.getInt(cursor.getColumnIndex("OrderIdLocal"));
                    String oderDetailsQuery = "Select * from tblOrderDetails where OrderIdLocal = " + OrdeId + "";
                    Cursor cursor2Details;
                    cursor2Details = database.rawQuery(oderDetailsQuery, null);
                    ArrayList<Product> productArrayList = new ArrayList<>();
                    if (cursor2Details.getCount() > 0) {
                        while (cursor2Details.moveToNext()) {

                            Product aProduct = new Product();

                            aProduct.setProductId(cursor2Details.getInt(cursor2Details.getColumnIndex("ProductId")));
                            aProduct.setProductName(cursor2Details.getString(cursor2Details.getColumnIndex("ProductName")));
                            aProduct.setQuantity(cursor2Details.getInt(cursor2Details.getColumnIndex("Quantity")));
                            aProduct.setUnitPrice(Double.parseDouble(cursor2Details.getString(cursor2Details.getColumnIndex("UnitPrice"))));
                            aProduct.setVatPercentage(Double.parseDouble(cursor2Details.getString(cursor2Details.getColumnIndex("VatPercentage"))));
                            productArrayList.add(aProduct);

                        }
                    }
                    aInfo.setOrderDetails(productArrayList);
                }
            }

        } catch (Exception exception) {
            Log.e("CampOrderDetails", exception.toString());
            exception.printStackTrace();
        }
        return orderList;
    }

    //get prescription from local
    @SuppressLint("Range")
    public ArrayList<PrescriptionSM> GetPrescriptionInfoFromDB() {
        ArrayList<PrescriptionSM> prescriptionList = new ArrayList<>();
        SQLiteDatabase database = dbHelperMain.getWritableDatabase();
        String prescriptionQuery = "Select * from tblPrescriptionInfo";
        // String prescriptionproductQuery = "Select * from tblPrescriptionInfo where CustomerCode = '" + customerCode + "' order by OrderIdLocal desc";
        try {
            Cursor cursor;
            cursor = database.rawQuery(prescriptionQuery, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    PrescriptionSM aInfo = new PrescriptionSM();
                    DoctorListViewModel dvm = new DoctorListViewModel();
                    //aInfo.setDoctorId(cursor.getInt(cursor.getColumnIndex("DoctorId")));
                    // aInfo.setDoctorName(cursor.getString(cursor.getColumnIndex("DoctorName")));
                    dvm.setDoctorId(cursor.getInt(cursor.getColumnIndex("DoctorId")));
                    dvm.setDoctorName(cursor.getString(cursor.getColumnIndex("DoctorName")));//setDoctorName(cursor.getString(cursor.getColumnIndex("DoctorName")));
                    dvm.setDocContact(cursor.getString(cursor.getColumnIndex("DocContact")));
                    dvm.setDoctorTypeName(cursor.getString(cursor.getColumnIndex("DoctorTypeName")));
                    dvm.setChemberName(cursor.getString(cursor.getColumnIndex("ChemberName")));
                    dvm.setProgramTypeName(cursor.getString(cursor.getColumnIndex("ProgramTypeName")));
                    aInfo.setDoclist(dvm);
                    aInfo.setSessionUser(cursor.getInt(cursor.getColumnIndex("SessionUser")));
                    aInfo.setPrescriptionDate(cursor.getString(cursor.getColumnIndex("PrescriptionDate")));
                    aInfo.setEntryTime(cursor.getString(cursor.getColumnIndex("EntryTime")));
                    aInfo.setPrescripId(cursor.getInt(cursor.getColumnIndex("PrescripId")));
                    aInfo.setPrescTypeName(cursor.getString(cursor.getColumnIndex("PrescTypeName")));
                    aInfo.setChemberId(cursor.getInt(cursor.getColumnIndex("ChemberId")));
                    aInfo.setImageString(cursor.getString(cursor.getColumnIndex("ImageString")));
                    prescriptionList.add(aInfo);

                    int prscId = cursor.getInt(cursor.getColumnIndex("PrescripId"));
                    String prescProductQuery = "Select * from tblPrescriptionMaster where PrescripId = " + prscId + "";

                    Cursor cursor2Details;
                    cursor2Details = database.rawQuery(prescProductQuery, null);
                    ArrayList<Product> productArrayList = new ArrayList<>();
                    if (cursor2Details.getCount() > 0) {
                        while (cursor2Details.moveToNext()) {

                            Product aProduct = new Product();

                            aProduct.setProductId(cursor2Details.getInt(cursor2Details.getColumnIndex("ProductId")));
                            aProduct.setProductName(cursor2Details.getString(cursor2Details.getColumnIndex("ProductName")));
                            productArrayList.add(aProduct);

                        }
                    }
                    aInfo.setaProList(productArrayList);
                }
            }

        } catch (Exception exception) {
            Log.e("CampOrderDetails", exception.toString());
            exception.printStackTrace();
        }
        return prescriptionList;
    }
    //DCR
    @SuppressLint("Range")
    public ArrayList<DcrSM> GetDcrInfoFromDB() {
        ArrayList<DcrSM> dcrList = new ArrayList<>();
        SQLiteDatabase database = dbHelperMain.getWritableDatabase();
        String prescriptionQuery = "Select * from tblDcrInfo";

        // String prescriptionproductQuery = "Select * from tblPrescriptionInfo where CustomerCode = '" + customerCode + "' order by OrderIdLocal desc";
        try {
            Cursor cursor;
            cursor = database.rawQuery(prescriptionQuery, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    DcrSM aInfo = new DcrSM();
                    DoctorListViewModel dvm = new DoctorListViewModel();
                    dvm.setDoctorId(cursor.getInt(cursor.getColumnIndex("DoctorId")));
                    dvm.setDoctorName(cursor.getString(cursor.getColumnIndex("DoctorName")));//setDoctorName(cursor.getString(cursor.getColumnIndex("DoctorName")));
                    dvm.setDocContact(cursor.getString(cursor.getColumnIndex("DocContact")));
                    dvm.setDoctorTypeName(cursor.getString(cursor.getColumnIndex("DoctorTypeName")));
                    dvm.setChemberName(cursor.getString(cursor.getColumnIndex("ChemberName")));
                    dvm.setProgramTypeName(cursor.getString(cursor.getColumnIndex("ProgramTypeName")));

                    aInfo.setDoclist(dvm);
                    aInfo.setDcrId(cursor.getInt(cursor.getColumnIndex("DcrId")));
                    aInfo.setSessionUser(cursor.getString(cursor.getColumnIndex("SessionUser")));
                    aInfo.setDcrDate(cursor.getString(cursor.getColumnIndex("DcrDate")));
                    aInfo.setEntryTime(cursor.getString(cursor.getColumnIndex("EntryTime")));
                    aInfo.setVisitTypeId(cursor.getInt(cursor.getColumnIndex("VisitTypeId")));
                    aInfo.setVisitTypeName(cursor.getString(cursor.getColumnIndex("VisitTypeName")));
                    aInfo.setChamberId(cursor.getInt(cursor.getColumnIndex("ChemberId")));
                    aInfo.setRemarks(cursor.getString(cursor.getColumnIndex("Remarks")));
                    dcrList.add(aInfo);

                    int dcrId = cursor.getInt(cursor.getColumnIndex("DcrId"));
                    String dcrSampleQuery = "Select * from tblDcrSample where DcrId = " + dcrId + "";
                    String dcrBrandQuery = "Select * from tblDcrBrand where DcrId = " + dcrId + "";
                    String dcrGiftQuery = "Select * from tblDcrGift where DcrId = " + dcrId + "";
                    String dcrVisitedwithQuery = "Select * from tblDcrVisitedwith where DcrId = " + dcrId + "";

                    Cursor cursorSampleDetails, cursorBrandDetails, cursorGifteDetails, cursorVisitedDetails;
                    cursorSampleDetails = database.rawQuery(dcrSampleQuery, null);
                    cursorBrandDetails = database.rawQuery(dcrBrandQuery, null);
                    cursorGifteDetails = database.rawQuery(dcrGiftQuery, null);
                    cursorVisitedDetails = database.rawQuery(dcrVisitedwithQuery, null);

                    ArrayList<ProductSample> productArrayList = new ArrayList<>();
                    ArrayList<DoctorBrand> brandArrayList = new ArrayList<>();
                    ArrayList<Gift> giftArrayList = new ArrayList<>();
                    ArrayList<UserByRole> visitedwithArrayList = new ArrayList<>();
                    //Retrive Sample Product
                    if (cursorSampleDetails.getCount() > 0) {
                        while (cursorSampleDetails.moveToNext()) {

                            ProductSample aProduct = new ProductSample();

                            aProduct.setProductId(cursorSampleDetails.getInt(cursorSampleDetails.getColumnIndex("ProductId")));
                            aProduct.setProductName(cursorSampleDetails.getString(cursorSampleDetails.getColumnIndex("ProductName")));
                            aProduct.setQuantity(cursorSampleDetails.getInt(cursorSampleDetails.getColumnIndex("Quantity")));
                            productArrayList.add(aProduct);

                        }
                    }
                    aInfo.setSampleList(productArrayList);

                    //Retrive Brand
                    if (cursorBrandDetails.getCount() > 0) {
                        while (cursorBrandDetails.moveToNext()) {
                            DoctorBrand aProduct = new DoctorBrand();
                            aProduct.setBrandId(cursorBrandDetails.getInt(cursorBrandDetails.getColumnIndex("BrandId")));
                            aProduct.setBrandName(cursorBrandDetails.getString(cursorBrandDetails.getColumnIndex("BrandName")));
                            brandArrayList.add(aProduct);
                        }
                    }
                    aInfo.setDoctorBrand(brandArrayList);

                    //Retrive Gift Product
                    if (cursorGifteDetails.getCount() > 0) {
                        while (cursorGifteDetails.moveToNext()) {

                            Gift aProduct = new Gift();
                            aProduct.setProductId(cursorGifteDetails.getInt(cursorGifteDetails.getColumnIndex("ProductId")));
                            aProduct.setProductName(cursorGifteDetails.getString(cursorGifteDetails.getColumnIndex("ProductName")));
                            aProduct.setQuantity(cursorGifteDetails.getInt(cursorGifteDetails.getColumnIndex("Quantity")));
                            aProduct.setPosition(cursorGifteDetails.getInt(cursorGifteDetails.getColumnIndex("Position")));
                            giftArrayList.add(aProduct);

                        }
                    }
                    aInfo.setGiftList(giftArrayList);
                    //Retrive Visitedwith
                    if (cursorVisitedDetails.getCount() > 0) {
                        while (cursorVisitedDetails.moveToNext()) {
                            UserByRole aUser = new UserByRole();

                            aUser.setEmpInfoId(cursorVisitedDetails.getInt(cursorVisitedDetails.getColumnIndex("EmpInfoId")));
                            aUser.setEmpName(cursorVisitedDetails.getString(cursorVisitedDetails.getColumnIndex("EmpName")));
                            aUser.setEmpMasterCode(cursorVisitedDetails.getString(cursorVisitedDetails.getColumnIndex("EmpMasterCode")));
                            visitedwithArrayList.add(aUser);

                        }
                    }
                    aInfo.setAempList(visitedwithArrayList);
                }
            }

            database.close();
        } catch (Exception exception) {
            Log.e("CampOrderDetails", exception.toString());
            exception.printStackTrace();
        }
        return dcrList;
    }

    @SuppressLint("Range")
    public boolean DeleteOrderMasterDetail(int orderId) {
        boolean isSuc = true;
        try {
            SQLiteDatabase database = dbHelperMain.getWritableDatabase();
            String query = "delete from tblOrderMaster where OrderIdLocal=" + orderId + "";
            String query2 = "delete from tblOrderDetails where OrderIdLocal=" + orderId + "";
            database.execSQL(query);
            database.execSQL(query2);
            isSuc = true;
        } catch (Exception ex) {
            isSuc = false;
            Log.e("Delete", ex.toString());
        }
        return isSuc;
    }

    public boolean UpdateOrderSynchInfo(int orderId) {
        boolean isSuc = true;
        try {
            SQLiteDatabase database = dbHelperMain.getWritableDatabase();
            String query = "update tblOrderMaster set Status='synced' where OrderIdLocal=" + orderId + "";
            database.execSQL(query);
            isSuc = true;
        } catch (Exception ex) {
            isSuc = false;
            Log.e("sync", ex.toString());
        }
        return isSuc;
    }

    public void _deleteAllRecordsFromaTable(String tableName) {
        SQLiteDatabase database = dbHelperMain.getWritableDatabase();
        try {
            String query = "delete from '" + tableName + "';";
            database.execSQL(query);

        } catch (Exception ex) {
            Log.e("recordDelError", ex.toString());
        }
    }

    public boolean _deleteAllRecordsFromaTableBolean(String tableName) {
        SQLiteDatabase database = dbHelperMain.getWritableDatabase();
        try {
            String query = "delete from '" + tableName + "';";
            database.execSQL(query);

        } catch (Exception ex) {
            Log.e("recordDelError", ex.toString());
        }
        return true;
    }

    //TODO:Customer Info
    public void InsertCustomerInfo_SQLite(List<Customer> mList) {
        try {
            String tableName = "tblCustomerInfo";
            _deleteAllRecordsFromaTable(tableName);

            SQLiteDatabase database = dbHelperMain.getWritableDatabase();
            for (int i = 0; i < mList.size(); i++) {
                Customer aInfo = mList.get(i);
                String insertQuery = "Insert into tblCustomerInfo(CustomerMasterId,CustomerName,CustomerCode,CustomerAdress,CustomerType," +
                        "CustomerCell,CustomerBalance,CustomerCreditlimit,Market) " +
                        "values('" + aInfo.getCustomerMasterId() + "','" + aInfo.getCustomerName() + "','" + aInfo.getCustomerCode() + "','" + aInfo.getAddress() + "'," +
                        "'" + aInfo.getCustomerType() + "','" + aInfo.getCustomerStation() + "','" + aInfo.getCellNo() + "','" + aInfo.getBalance() + "','" + aInfo.getCreditLimit() + "','" + aInfo.getMarketName() + "')";

                database.execSQL(insertQuery);
            }
        } catch (Exception exception) {
            Log.e("DBEX", exception.toString());
        }
    }

    //TODO:Customer Info
    public boolean InsertCustomerReport_SQLite(List<CustomerARModel> mList) {
        boolean inserted = false;
        try {
            String tableName = "tblCustomerReport";
            _deleteAllRecordsFromaTable(tableName);

            SQLiteDatabase database = dbHelperMain.getWritableDatabase();
            for (int i = 0; i < mList.size(); i++) {
                CustomerARModel aInfo = mList.get(i);
                String CustomerName = "";
                String MarketName = "";
                String OwnerName = "";
                String Address = "";
                String Customerstatus = "";

                String GroupName = "";
                String RegionName = "";
                String AreaName = "";
                String TerritoryName = "";
                String SubTerritoryName = "";

                if (!aInfo.getCustomerName().isEmpty()) {
                    CustomerName = aInfo.getCustomerName().replace("'", "''");
                } else {
                    CustomerName = "";
                }
                if (!aInfo.getMarketName().isEmpty()) {
                    MarketName = aInfo.getMarketName().replace("'", "''");
                } else {
                    MarketName = "";
                }
                if (!aInfo.getOwnerName().isEmpty()) {
                    OwnerName = aInfo.getOwnerName().replace("'", "''");
                } else {
                    OwnerName = "";
                }
                if (!aInfo.getAddress().isEmpty()) {
                    Address = aInfo.getAddress().replace("'", "''");
                } else {
                    Address = "";
                }
                if (!aInfo.getCustomerStatus().isEmpty()) {
                    Customerstatus = aInfo.getCustomerStatus().replace("'", "''");
                } else {
                    Customerstatus = "";
                }
                // if (!aInfo.getGroupName().isEmpty()) {
                if (!TextUtils.isEmpty(aInfo.getGroupName())) {
                    GroupName = aInfo.getGroupName().replace("'", "''");
                } else {
                    GroupName = "";
                }
                // if (!aInfo.getRegionName().isEmpty()) {
                if (!TextUtils.isEmpty(aInfo.getRegionName())) {
                    RegionName = aInfo.getRegionName().replace("'", "''");
                } else {
                    RegionName = "";
                }
                // if (!aInfo.getAreaName().isEmpty()) {
                if (!TextUtils.isEmpty(aInfo.getAreaName())) {
                    AreaName = aInfo.getAreaName().replace("'", "''");
                } else {
                    AreaName = "";
                }
                //if (!aInfo.getTerritoryName().isEmpty()) {
                if (!TextUtils.isEmpty(aInfo.getTerritoryName())) {
                    TerritoryName = aInfo.getTerritoryName().replace("'", "''");
                } else {
                    TerritoryName = "";
                }
                // if (!aInfo.getSubTerritoryName().isEmpty()) {
                if (!TextUtils.isEmpty(aInfo.getSubTerritoryName())) {
                    SubTerritoryName = aInfo.getSubTerritoryName().replace("'", "''");
                } else {
                    SubTerritoryName = "";
                }

                String insertQuery = "Insert into tblCustomerReport(CustomerName,MarketName,MarketCode,ActionStatus,CellNo,OwnerName," +
                        "Address,ImageBase64String,ProgramTypeName,WatingEmployee,WaitingRole,GroupId,RegionId,AreaId,TerritoryId,SubTerritoryId,MarketId,CustomerStatus,CustomerTypeId,ProgramTypeId,SMCTypeId) " +
                        "values('" + CustomerName + "','" + MarketName + "','" + aInfo.getMarketCode() + "','" + aInfo.getActionStatus() + "','" + aInfo.getCellNo() + "'," +
                        "'" + OwnerName + "','" + Address + "','" + aInfo.getImageBase64String() + "','" + aInfo.getProgramTypeName() + "','" + aInfo.getWatingEmployee() + "','" + aInfo.getWaitingRole() + "'" +
                        ",'" + aInfo.getGroupId() + "','" + aInfo.getRegionId() + "','" + aInfo.getAreaId() + "','" + aInfo.getTerritoryId() + "'" +
                        ",'" + aInfo.getSubTerritoryId() + "','" + aInfo.getMarketId() + "','" + Customerstatus + "','" + aInfo.getCustomerTypeId() + "','" + aInfo.getProgramTypeId() + "','" + aInfo.getSMCTypeId() + "')";


                database.execSQL(insertQuery);
                inserted = true;
            }

        } catch (Exception exception) {
            Log.e("DBEX", exception.toString());
        }
        return inserted;
    }

    //TODO:Customer Info
    public boolean InsertCDoctorReport_SQLite(List<DoctorARModel> mList) {
        try {
            String tableName = "tblDoctorReport";
            _deleteAllRecordsFromaTable(tableName);

            SQLiteDatabase database = dbHelperMain.getWritableDatabase();
            for (int i = 0; i < mList.size(); i++) {
                DoctorARModel aInfo = mList.get(i);
                String MarketName = "";
                if (!aInfo.getMarketName().isEmpty()) {
                    MarketName = aInfo.getMarketName().replace("'", "''");
                } else {
                    MarketName = "";
                }
                String insertQuery = "Insert into tblDoctorReport(createdAt,DoctorCode,MarketName,ActionStatus,WaitingRole,WatingEmployee,GroupId,RegionId,AreaId,TerritoryId,SubTerritoryId,MarketId,ProgramTypeId,DoctorTypeId,DoctorStatus,SMCTypeId) " +
                        "values('" + aInfo.getCreatedAt() + "','" + aInfo.getDoctorCode() + "','" + MarketName + "','" + aInfo.getActionStatus() + "'," +
                        "'" + aInfo.getWaitingRole() + "','" + aInfo.getWatingEmployee() + "','" + aInfo.getGroupId() + "','" + aInfo.getRegionId() + "'" +
                        ",'" + aInfo.getAreaId() + "','" + aInfo.getTerritoryId() + "','" + aInfo.getSubTerritoryId() + "','" + aInfo.getMarketId() + "','" + aInfo.getProgramTypeId() + "','" + aInfo.getDoctorTypeId() + "','" + aInfo.getStatus() + "','" + aInfo.getSMCTypeId() + "')";

                database.execSQL(insertQuery);
            }

        } catch (Exception exception) {
            Log.e("DBEX", exception.toString());
            System.out.println(exception.getMessage().toString());
        }
        return true;
    }

    /* public void InsertProductInfo_SQLite(List<Product> mList) {
         try {
             String tableName = "tblProductInfo";
             _deleteAllRecordsFromaTable(tableName);

             SQLiteDatabase database = dbHelperMain.getWritableDatabase();
             for (int i = 0; i < mList.size(); i++) {
                 Product aInfo = mList.get(i);
                 String insertQuery = "Insert into tbl_ProductInfo(ProductId,ProductName,ProductCode,ProductDes,PackSize,UnitPrice,QuotedPrice,VatPercentage,VatAmountPerunit,CustomerMasterId) " +
                         "values('" + aInfo.getProductId() + "','" + aInfo.getProductName() + "','" + aInfo.getProductCode() + "','" + aInfo.getProductDes() + "','" + aInfo.getPackSize() + "','" + aInfo.getUnitPrice() + "','" + aInfo.getQuotedPrice() + "','" + aInfo.getVatPercentage() + "','" + aInfo.getVatAmountPerunit() + "','" + aInfo.getCustomerMasterId() + "')";
                 database.execSQL(insertQuery);
             }
         } catch (Exception exception) {
             Log.e("DBEX", exception.toString());
         }
     }*/
    public boolean InsertQuotedPriceInfo_SQLite(List<QuotedPrice> mList) {
        boolean isTrue = true;
        try {
            String tableName = "tblQuotedPrice";
            _deleteAllRecordsFromaTable(tableName);

            SQLiteDatabase database = dbHelperMain.getWritableDatabase();
            for (int i = 0; i < mList.size(); i++) {
                QuotedPrice aInfo = mList.get(i);
                String insertQuery = "Insert into tblQuotedPrice(description,quotedPriceDetailId,policy,customerMasterId,activeFromDate,activeToDate,productId,unitPrice,vat) " +
                        "values('" + aInfo.getDescription() + "','" + aInfo.getQuotedPriceDetailId() + "','" + aInfo.getPolicy() + "','" + aInfo.getCustomerMasterId() + "','" + aInfo.getActiveFromDate() + "','" + aInfo.getActiveToDate() + "'," +
                        "'" + aInfo.getProductId() + "','" + aInfo.getUnitPrice() + "','" + aInfo.getVat() + "')";
                database.execSQL(insertQuery);
            }
        } catch (Exception exception) {
            isTrue = false;
            Log.e("DBEX", exception.toString());
        }

        return isTrue;
    }

    public boolean InsertPrescImg_SQLite(List<PrescApprovalData> mList) {
        boolean isTrue = false;
        try {
            String tableName = "tblprescimg";
            _deleteAllRecordsFromaTable(tableName);

            SQLiteDatabase database = dbHelperMain.getWritableDatabase();
            for (int i = 0; i < mList.size(); i++) {
                PrescApprovalData aInfo = mList.get(i);
                String insertQuery = "Insert into tblprescimg(prescimg_id,prescimg) " +
                        "values('" + aInfo.getPrescriptionId() + "','" + aInfo.getaPrescriptionRMasterDAO().getImageString() + "')";
                database.execSQL(insertQuery);
                isTrue = true;
            }
            database.close();
        } catch (Exception exception) {
            isTrue = false;
            Log.e("DBEX", exception.toString());
        }

        return isTrue;
    }

    public boolean InsertMileagImg_SQLite(List<MileageListTeam> mList) {
        boolean isTrue = false;
        try {
            String tableName = "tblmileageimg";
            _deleteAllRecordsFromaTable(tableName);

            SQLiteDatabase database = dbHelperMain.getWritableDatabase();
            for (int i = 0; i < mList.size(); i++) {
                MileageListTeam aInfo = mList.get(i);
                String insertQuery = "Insert into tblmileageimg(mileageimg_id,mileageimg) " +
                        "values('" + aInfo.getMileageClaimId() + "','" + aInfo.getImageString() + "')";
                database.execSQL(insertQuery);
                isTrue = true;
            }
        } catch (Exception exception) {
            isTrue = false;
            Log.e("DBEX", exception.toString());
        }

        return isTrue;
    }

    public boolean InsertExpenseImg_SQLite(List<ExpListTeam> mList) {
        boolean isTrue = false;
        try {
            String tableName = "tblexpensimg";
            _deleteAllRecordsFromaTable(tableName);

            SQLiteDatabase database = dbHelperMain.getWritableDatabase();
            for (int i = 0; i < mList.size(); i++) {
                ExpListTeam aInfo = mList.get(i);
                String insertQuery = "Insert into tblexpensimg(expenseimg_id,expenseimg) " +
                        "values('" + aInfo.getExpenseClaimID() + "','" + aInfo.getImageString() + "')";
                database.execSQL(insertQuery);
                isTrue = true;
            }
        } catch (Exception exception) {
            isTrue = false;
            Log.e("DBEX", exception.toString());
        }

        return isTrue;
    }

    public boolean InsertDAImg_SQLite(List<DAListData> mList) {
        boolean isTrue = false;
        try {
            String tableName = "tbldaimg";
            _deleteAllRecordsFromaTable(tableName);

            SQLiteDatabase database = dbHelperMain.getWritableDatabase();
            for (int i = 0; i < mList.size(); i++) {
                DAListData aInfo = mList.get(i);
                String insertQuery = "Insert into tbldaimg(daimg_id,daimg) " +
                        "values('" + aInfo.getaTADAMasterDAO().getTadaID() + "','" + aInfo.getaTADAMasterDAO().getEmpName() + "')";
                database.execSQL(insertQuery);
                isTrue = true;
            }
        } catch (Exception exception) {
            isTrue = false;
            Log.e("DBEX", exception.toString());
        }

        return isTrue;
    }

    @SuppressLint("Range")
    public PrescApprovalData getPrescription_Image(int id) {
        PrescApprovalData presscName = new PrescApprovalData();
        SQLiteDatabase database = dbHelperMain.getWritableDatabase();
        String oderMasterQuerynll = "Select * from tblprescimg";
        String oderMasterQuery = "Select * from tblprescimg Where prescimg_id=" + id + "";

        try {
            Cursor cursor;
           /* if (id == 0 || String.valueOf(id).isEmpty()) {
                cursor = database.rawQuery(oderMasterQuerynll, null);
            } else {
                cursor = database.rawQuery(oderMasterQuery, null);
            }*/
            cursor = database.rawQuery(oderMasterQuery, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    presscName.setTerritoryName(cursor.getString(cursor.getColumnIndex("prescimg")));
                }
            }
            cursor.close();
        } catch (Exception exception) {
            Log.e("MIO", exception.toString());
            exception.printStackTrace();
        }
        return presscName;
    }

    @SuppressLint("Range")
    public MileageListTeam getMileage_Image(int id) {
        MileageListTeam MileageName = new MileageListTeam();
        SQLiteDatabase database = dbHelperMain.getWritableDatabase();
        String oderMasterQuery = "Select * from tblmileageimg Where mileageimg_id=" + id + "";
        try {
            Cursor cursor;
            cursor = database.rawQuery(oderMasterQuery, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    MileageName.setImageString(cursor.getString(cursor.getColumnIndex("mileageimg")));
                }
            }
            cursor.close();
        } catch (Exception exception) {
            Log.e("MIO", exception.toString());
            exception.printStackTrace();
        }
        return MileageName;
    }

    @SuppressLint("Range")
    public ExpListTeam getExpense_Image(int id) {
        ExpListTeam ExpenseName = new ExpListTeam();
        SQLiteDatabase database = dbHelperMain.getWritableDatabase();
        String oderMasterQuerynll = "Select * from tblexpensimg";
        String oderMasterQuery = "Select * from tblexpensimg Where expenseimg_id=" + id + "";

        try {
            Cursor cursor;
            cursor = database.rawQuery(oderMasterQuery, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    ExpenseName.setImageString(cursor.getString(cursor.getColumnIndex("expenseimg")));
                }
            }
            cursor.close();
        } catch (Exception exception) {
            Log.e("MIO", exception.toString());
            exception.printStackTrace();
        }
        return ExpenseName;
    }

    public DAListData getDA_Image(int id) {
        DAListData DaName = new DAListData();
        SQLiteDatabase database = dbHelperMain.getWritableDatabase();
        String oderMasterQuerynll = "Select * from tbldaimg";
        String oderMasterQuery = "Select * from tbldaimg Where daimg_id=" + id + "";

        try {
            Cursor cursor;
            cursor = database.rawQuery(oderMasterQuery, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    //  DaName.(cursor.getString(cursor.getColumnIndex("daimg")));
                }
            }
            cursor.close();
        } catch (Exception exception) {
            Log.e("MIO", exception.toString());
            exception.printStackTrace();
        }
        return DaName;
    }

    @SuppressLint("Range")
    public boolean SavePrescriptionInfo_SQLite(PrescriptionSM aPres) {
        try {
            SQLiteDatabase database = dbHelperMain.getWritableDatabase();
            int prescripId = 0;
            String DoctorName = "";
            String ChemberName = "";
            String PrescTypeName = "";
            if (!aPres.getDoclist().getDoctorName().isEmpty()) {
                DoctorName = aPres.getDoclist().getDoctorName().replace("'", "''");
            } else {
                DoctorName = "";
            }
            if (!aPres.getDoclist().getDoctorName().isEmpty()) {
                ChemberName = aPres.getDoclist().getChemberName().replace("'", "''");
            } else {
                ChemberName = "";
            }
            if (!aPres.getPrescTypeName().isEmpty()) {
                PrescTypeName = aPres.getPrescTypeName().replace("'", "''");
            } else {
                PrescTypeName = "";
            }
            String insertpQuery = "Insert into tblPrescriptionInfo(DoctorId,DoctorName,DocContact,DoctorTypeName,ChemberName,ProgramTypeName,SessionUser,PrescriptionDate,EntryTime,PrescriptionTypeId,PrescTypeName,ChemberId,ImageString) " +
                    "values('" + aPres.getDoclist().getDoctorId() + "','" + DoctorName + "','" + aPres.getDoclist().getDocContact() + "','" + aPres.getDoclist().getDoctorTypeName() + "','" + ChemberName + "','" + aPres.getDoclist().getProgramTypeName() + "','" + aPres.getSessionUser() + "','" + aPres.getPrescriptionDate() + "','" + aPres.getEntryTime() + "','" + aPres.getPrescriptionTypeId() + "','" + PrescTypeName + "','" + aPres.getChemberId() + "','" + aPres.getImageString() + "')";
            database.execSQL(insertpQuery);
            // Cursor cursor = database.rawQuery("SELECT PrescripProductId FROM tblPrescriptionMaster ORDER BY PrescripProductId DESC LIMIT 1", null);
            // progressDoalog.dismiss();
            Cursor c1 = database.rawQuery("SELECT * FROM tblPrescriptionInfo where PrescripId order by  PrescripId desc LIMIT 1", null);
            if (c1.getCount() > 0) {
                while (c1.moveToNext()) {
                    prescripId = c1.getInt(c1.getColumnIndex("PrescripId"));
                }
            }
            int ProductId = 0;
            for (int i = 0; i < aPres.getaProList().size(); i++) {
                Product aInfo = aPres.getaProList().get(i);
                ProductId = aInfo.getProductId();
                String ProductName = "";
                if (!aInfo.getProductName().isEmpty()) {
                    ProductName = aInfo.getProductName().replace("'", "''");
                } else {
                    ProductName = "";
                }
                String insertDetal = "Insert into tblPrescriptionMaster(PrescripId,ProductId,ProductName,DoctorId) " +
                        "values('" + prescripId + "','" + ProductId + "','" + ProductName + "','" + aPres.getDoctorId() + "')";
                database.execSQL(insertDetal);
            }
            database.close();
            return true;
        } catch (Exception exception) {
            Log.e("EX", exception.toString());
            return false;
        }

    }

    @SuppressLint("Range")
    public boolean UpdatePrescInfo_SQLite(PrescriptionSM aPres, int prescripLocalId) {
        try {
            SQLiteDatabase database = dbHelperMain.getWritableDatabase();
            int prescripId = 0;
            Cursor c1 = database.rawQuery("SELECT * FROM tblPrescriptionInfo where PrescripId order by  PrescripId desc LIMIT 1", null);
            if (c1.getCount() > 0) {
                while (c1.moveToNext()) {
                    prescripId = c1.getInt(c1.getColumnIndex("PrescripId"));
                    ContentValues cv = new ContentValues();
                    if (!aPres.getImageString().isEmpty()) {
                        cv.put("ImageString", aPres.getImageString()); //These Fields should be your String values of actual column names
                        database.update("tblPrescriptionInfo", cv, "PrescripId = " + prescripLocalId, null);
                    }

                }
            }

            int ProductId = 0;
            for (int i = 0; i < aPres.getaProList().size(); i++) {
                Product aInfo = aPres.getaProList().get(i);
                ProductId = aInfo.getProductId();
                String ProductName = "";
                if (!aInfo.getProductName().isEmpty()) {
                    ProductName = aInfo.getProductName().replace("'", "''");
                } else {
                    ProductName = "";
                }
                String insertDetal = "Insert into tblPrescriptionMaster(PrescripId,ProductId,ProductName,DoctorId) " +
                        "values('" + prescripLocalId + "','" + ProductId + "','" + ProductName + "','" + aPres.getDoctorId() + "')";
                database.execSQL(insertDetal);
            }
            database.close();
            return true;
        } catch (Exception exception) {
            Log.e("EX", exception.toString());
            return false;
        }
    }

    @SuppressLint("Range")
    public boolean SaveDcrInfo_SQLite(DcrSM aPres) {
        try {
            SQLiteDatabase database = dbHelperMain.getWritableDatabase();
            int dcrId = 0;
            String ChemberName = "";
            String ProgramTypeName = "";
            String VisitTypeName = "";
            String Remarks = "";
            try {
                if (!aPres.getDoclist().getChemberName().isEmpty()) {
                    ChemberName = aPres.getDoclist().getChemberName().replace("'", "''");
                } else {
                    ChemberName = "";
                }
            } catch (Exception exception) {

            }
            try {
                if (!aPres.getDoclist().getProgramTypeName().isEmpty()) {
                    ProgramTypeName = aPres.getDoclist().getProgramTypeName().replace("'", "''");
                } else {
                    ProgramTypeName = "";
                }
            } catch (Exception exception) {

            }
            try {
                if (!aPres.getVisitTypeName().isEmpty()) {
                    VisitTypeName = aPres.getVisitTypeName().replace("'", "''");
                } else {
                    VisitTypeName = "";
                }
            } catch (Exception exception) {

            }
            try {
                if (!aPres.getRemarks().isEmpty()) {
                    Remarks = aPres.getRemarks().replace("'", "''");
                } else {
                    Remarks = "";
                }
            } catch (Exception exception) {

            }

            String insertpQuery = "Insert into tblDcrInfo(DoctorId,DoctorName,DocContact,DoctorTypeName,ChemberName,ProgramTypeName,SessionUser,DcrDate,EntryTime,VisitTypeId,VisitTypeName,ChemberId,Remarks) " +
                    //"values('" + aPres.getDoctorId() + "','" + aPres.getDoctorName() + "','" + aPres.getDoclist().getDocContact() + "','" + aPres.getDoclist().getDoctorTypeName() + "','" + aPres.getDoclist().getChemberName() + "','" + aPres.getDoclist().getProgramTypeName() + "','" + aPres.getSessionUser() + "','" + aPres.getDcrDate() + "','" + aPres.getEntryTime() + "','" + aPres.getVisitTypeId() + "','" + aPres.getVisitTypeName() + "','" + aPres.getChamberId() + "','" +  ncomment +"')";
                    "values('" + aPres.getDoctorId() + "','" + aPres.getDoctorName() + "','" + aPres.getDoclist().getDocContact() + "','" + aPres.getDoclist().getDoctorTypeName() + "','" + ChemberName + "','" + ProgramTypeName + "','" + aPres.getSessionUser() + "','" + aPres.getDcrDate() + "','" + aPres.getEntryTime() + "','" + aPres.getVisitTypeId() + "','" + VisitTypeName + "','" + aPres.getChamberId() + "', '" + Remarks + "')";
            database.execSQL(insertpQuery);

            Cursor c1 = database.rawQuery("SELECT * FROM tblDcrInfo where DcrId order by  DcrId desc LIMIT 1", null);
            if (c1.getCount() > 0) {
                while (c1.moveToNext()) {
                    dcrId = c1.getInt(c1.getColumnIndex("DcrId"));
                }
            }


            int ProductId = 0;
            for (int i = 0; i < aPres.getSampleList().size(); i++) {
                ProductSample aInfo = aPres.getSampleList().get(i);
                ProductId = aInfo.getProductId();
                //String ProductName = aInfo.getProductName();
                int Quantity = aInfo.getQuantity();

                String ProductName = "";
                if (!aInfo.getProductName().isEmpty()) {
                    ProductName = aInfo.getProductName().replace("'", "''");
                } else {
                    ProductName = "";
                }
                String insertDetal = "Insert into tblDcrSample(DcrId,ProductId,ProductName,Quantity,DoctorId) " +
                        "values('" + dcrId + "','" + ProductId + "','" + ProductName/*.replace("'","''") */ + "','" + Quantity + "','" + aPres.getDoctorId() + "')";
                database.execSQL(insertDetal);


            }
            int BrandId = 0;
            for (int i = 0; i < aPres.getDoctorBrand().size(); i++) {
                DoctorBrand aInfo = aPres.getDoctorBrand().get(i);
                BrandId = aInfo.getBrandId();
                // String BrandName = aInfo.getBrandName();
                String BrandName = "";
                try {
                    if (!aInfo.getBrandName().isEmpty()) {
                        BrandName = aInfo.getBrandName().replace("'", "''");
                    } else {
                        BrandName = "";
                    }
                } catch (Exception exception) {

                }
                String insertBDetal = "Insert into tblDcrBrand(DcrId,BrandId,BrandName,DoctorId) " +
                        "values('" + dcrId + "','" + BrandId + "','" + BrandName/*.replace("'","''")*/ + "','" + aPres.getDoctorId() + "')";
                database.execSQL(insertBDetal);
            }

            int GiftId = 0;
            for (int i = 0; i < aPres.getGiftList().size(); i++) {
                Gift aInfo = aPres.getGiftList().get(i);
                GiftId = aInfo.getProductId();
                String ProductName = "";// aInfo.getProductName();
                int Quantity = aInfo.getQuantity();

                try {
                    if (!aInfo.getProductName().isEmpty()) {
                        ProductName = aInfo.getProductName().replace("'", "''");
                    } else {
                        ProductName = "";
                    }
                } catch (Exception exception) {
                }
                String insertGDetal = "Insert into tblDcrGift(DcrId,ProductId,ProductName,Quantity,Position,DoctorId) " +
                        "values('" + dcrId + "','" + GiftId + "','" + ProductName/*.replace("'","''")*/ + "','" + Quantity + "','" + aInfo.getPosition() + "','" + aPres.getDoctorId() + "')";
                database.execSQL(insertGDetal);
            }
            int VisitedwithId = 0;
            for (int i = 0; i < aPres.getAempList().size(); i++) {
                UserByRole aInfo = aPres.getAempList().get(i);
                VisitedwithId = aInfo.getEmpInfoId();
                String EmpName = aInfo.getEmpName();
                String EmpMasterCode = aInfo.getEmpMasterCode();
                String insertVDetal = "Insert into tblDcrVisitedwith(DcrId,EmpInfoId,EmpName,EmpMasterCode,DoctorId) " +
                        "values('" + dcrId + "','" + VisitedwithId + "','" + EmpName + "','" + EmpMasterCode + "','" + aPres.getDoctorId() + "')";
                database.execSQL(insertVDetal);
            }
          /*  view.onSaveSuccess("This Order has been Drafted Successfully");
            progressDoalog.dismiss();*/
            database.close();
            return true;
        } catch (Exception exception) {
          /*  progressDoalog.dismiss();
            database.close();
            view.onSaveError("Some Error Occurred Drafting Current Order.. Please try again after some times");*/
            Log.e("EX", exception.toString());
            return false;
        }

    }

    @SuppressLint("Range")
    public boolean UpdateDcrInfo_SQLite(DcrSM aPres) {
        try {
            SQLiteDatabase database = dbHelperMain.getWritableDatabase();
            int dcrId = 0;
            Cursor c1 = database.rawQuery("SELECT * FROM tblDcrInfo where DcrId order by  DcrId desc LIMIT 1", null);
            if (c1.getCount() > 0) {
                while (c1.moveToNext()) {
                    dcrId = c1.getInt(c1.getColumnIndex("DcrId"));
                }
            }

            int ProductId = 0;
            for (int i = 0; i < aPres.getSampleList().size(); i++) {
                ProductSample aInfo = aPres.getSampleList().get(i);
                ProductId = aInfo.getProductId();
                String ProductName = aInfo.getProductName();
                int Quantity = aInfo.getQuantity();

                String insertDetal = "Insert into tblDcrSample(DcrId,ProductId,ProductName,Quantity,DoctorId) " +
                        "values('" + dcrId + "','" + ProductId + "','" + ProductName.replace("'", "''") + "','" + Quantity + "','" + aPres.getDoctorId() + "')";
                database.execSQL(insertDetal);


            }

            int BrandId = 0;
            for (int i = 0; i < aPres.getDoctorBrand().size(); i++) {
                DoctorBrand aInfo = aPres.getDoctorBrand().get(i);
                BrandId = aInfo.getBrandId();
                String BrandName = aInfo.getBrandName();

                String insertBDetal = "Insert into tblDcrBrand(DcrId,BrandId,BrandName,DoctorId) " +
                        "values('" + dcrId + "','" + BrandId + "','" + BrandName.replace("'", "''") + "','" + aPres.getDoctorId() + "')";
                database.execSQL(insertBDetal);


            }

            int GiftId = 0;
            for (int i = 0; i < aPres.getGiftList().size(); i++) {
                Gift aInfo = aPres.getGiftList().get(i);
                GiftId = aInfo.getProductId();
                String ProductName = aInfo.getProductName();
                int Quantity = aInfo.getQuantity();

                String insertGDetal = "Insert into tblDcrGift(DcrId,ProductId,ProductName,Quantity,Position,DoctorId) " +
                        "values('" + dcrId + "','" + GiftId + "','" + ProductName.replace("'", "''") + "','" + Quantity + "','" + aInfo.getPosition() + "','" + aPres.getDoctorId() + "')";
                database.execSQL(insertGDetal);


            }

            int VisitedwithId = 0;
            for (int i = 0; i < aPres.getAempList().size(); i++) {
                UserByRole aInfo = aPres.getAempList().get(i);
                VisitedwithId = aInfo.getEmpInfoId();
                String EmpName = aInfo.getEmpName();
                String EmpMasterCode = aInfo.getEmpMasterCode();
                String insertVDetal = "Insert into tblDcrVisitedwith(DcrId,EmpInfoId,EmpName,EmpMasterCode,DoctorId) " +
                        "values('" + dcrId + "','" + VisitedwithId + "','" + EmpName + "','" + EmpMasterCode + "','" + aPres.getDoctorId() + "')";
                database.execSQL(insertVDetal);
            }
            database.close();
            return true;
        } catch (Exception exception) {
            Log.e("EX", exception.toString());
            return false;
        }

    }

    @SuppressLint("Range")
    public ArrayList<MIO> getMIOList_SQLite() {
        ArrayList<MIO> MIOList = new ArrayList<>();
        SQLiteDatabase database = dbHelperMain.getWritableDatabase();
        String oderMasterQuery = "Select * from tblMIOInfo";
        try {
            Cursor cursor;
            cursor = database.rawQuery(oderMasterQuery, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    MIO mioInfo = new MIO();
                    mioInfo.setMIOEmpId(cursor.getInt(cursor.getColumnIndex("MIOEmpId")));
                    mioInfo.setEmpName(cursor.getString(cursor.getColumnIndex("EmpName")));
                    mioInfo.setEmpMasterCode(cursor.getString(cursor.getColumnIndex("EmpMasterCode")));
                    MIOList.add(mioInfo);
                }
            }
            cursor.close();
        } catch (Exception exception) {
            Log.e("MIO", exception.toString());
            exception.printStackTrace();
        }
        return MIOList;
    }


    @SuppressLint("Range")
    public ArrayList<EmpInfoListModels> getMIOListWithSelf_SQLite(int empId, String roleType) {
        ArrayList<EmpInfoListModels> MIOList = new ArrayList<>();
        SQLiteDatabase database = dbHelperMain.getWritableDatabase();
         String oderMasterQuery = "";

        if (roleType.equals("AM")) {
            oderMasterQuery = "select * from(\n" +
                    " Select ASMEmpId MIOEmpId , EmpName ||  ' (Self)' EmpName  from tblASMInfo \n" +
                    "                                  where ASMEmpId  ="+ empId +
                    "                                    union all \n" +
                    "            SELECT RSMEmpId AS MIOEmpId, EmpName || ' (DZSM)' AS EmpName    FROM tblRSMInfo    union all   Select MIOEmpId, EmpName || ' (MIO)' EmpName  from tblMIOInfo  \n" +
                    ") gg\n" +
                    " \n " ;
        } else if (roleType.equals("DZSM")) {
            oderMasterQuery =         "SELECT * FROM (\n" +
                    "    SELECT RSMEmpId AS MIOEmpId, EmpName || ' (Self)' AS EmpName  \n" +
                    "    FROM tblRSMInfo \n" +
                    "    WHERE RSMEmpId ="+ empId +
                    "    union all\n" +
                    "    SELECT ASMEmpId AS MIOEmpId, EmpName || ' (AM)' AS EmpName  \n" +
                    "    FROM tblASMInfo    union all  Select MIOEmpId, EmpName || ' (MIO)' EmpName from tblMIOInfo   \n" +
                     ") gg"   ;
        } else if (roleType.equals("NSM")) {
            oderMasterQuery =  "select * from(\n" +
                    "                     Select NSMEmpId MIOEmpId, EmpName   || ' (Self)' EmpName  from tblNSMInfo \n" +
                    "                    where NSMEmpId  ="+ empId +
                    "                    union all\n" +
                    "                    Select RSMEmpId MIOEmpId, EmpName || ' (DZSM)' EmpName  from tblRSMInfo \n" +
                    ") gg"  ;
        }

        try {
            Cursor cursor;
            cursor = database.rawQuery(oderMasterQuery, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    EmpInfoListModels mioInfo = new EmpInfoListModels();
                    mioInfo.setMIOEmpId(cursor.getInt(cursor.getColumnIndex("MIOEmpId")));
                    mioInfo.setEmpName(cursor.getString(cursor.getColumnIndex("EmpName")));
                //    mioInfo.setEmpMasterCode(cursor.getString(cursor.getColumnIndex("EmpMasterCode")));
                    MIOList.add(mioInfo);
                }
            }
            cursor.close();
        } catch (Exception exception) {
            Log.e("MIO", exception.toString());
            exception.printStackTrace();
        }
        return MIOList;
    }

    public boolean CheckDataInTable(String tableName) {
        SQLiteDatabase database = dbHelperMain.getWritableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM '" + tableName + "'", null);
        int count = cursor.getCount();
        if (count > 0) {
            return true;
        } else {
            return false;
        }
    }

    //TODO:GET Customer List
    @SuppressLint("Range")
    public ArrayList<Customer> getCustomerFilter_SQLite(String Gid, String Rid, String Aid, String Tid, String STid, String Mid, String Cid, String ptypeId, String SmcTypeId) {
        ArrayList<Customer> custList = new ArrayList<>();
        SQLiteDatabase database = dbHelperMain.getWritableDatabase();
        String oderMasterQuery = "Select * from tblCustomerInfo";
        String parameter = " WHERE CustomerCode is not null ";

        if (!Gid.equals("0")) {
            parameter = parameter + " AND  GroupId=" + Gid;
        }
        if (!Rid.equals("0")) {
            parameter = parameter + " AND  RegionId=" + Rid;
        }
        if (!Aid.equals("0")) {
            parameter = parameter + " AND AreaId=" + Aid;
        }
        if (!Tid.equals("0")) {
            parameter = parameter + " AND  TerritoryId=" + Tid;
        }
        if (!STid.equals("0")) {
            parameter = parameter + " AND  SubTerritoryId=" + STid;
        }
        if (!Mid.equals("0")) {
            parameter = parameter + " AND  MarketId=" + Mid;
        }
        if (!Cid.equals("0")) {
            parameter = parameter + " AND  CustomerTypeId=" + Cid;
        }
        if (!ptypeId.equals("0")) {
            parameter = parameter + " AND  ProgramTypeId=" + ptypeId;
        }
        if (!SmcTypeId.equals("0")) {
            parameter = parameter + " AND  SMCTypeId=" + SmcTypeId;
        }

        String mainparam = oderMasterQuery + parameter;
        try {
            Cursor cursor;
            cursor = database.rawQuery(mainparam, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    Customer aInfo = new Customer();
                    aInfo.setCustomerMasterId(cursor.getInt(cursor.getColumnIndex("CustomerMasterId")));
                    aInfo.setCustomerName(cursor.getString(cursor.getColumnIndex("CustomerName")));
                    aInfo.setCustomerCode(cursor.getString(cursor.getColumnIndex("CustomerCode")));
                    aInfo.setAddress(cursor.getString(cursor.getColumnIndex("CustomerAdress")));
                    aInfo.setCustomerType(cursor.getString(cursor.getColumnIndex("CustomerType")));
                    aInfo.setCellNo(cursor.getString(cursor.getColumnIndex("CustomerCell")));
                    aInfo.setBalance(cursor.getString(cursor.getColumnIndex("CustomerBalance")));
                    aInfo.setCreditLimit(cursor.getString(cursor.getColumnIndex("CustomerCreditlimit")));
                    aInfo.setMarketName(cursor.getString(cursor.getColumnIndex("Market")));
                    aInfo.setMarketCode(cursor.getString(cursor.getColumnIndex("MarketCode")));
                    aInfo.setNote(cursor.getString(cursor.getColumnIndex("Note")));
                    aInfo.setCustomerCheck(cursor.getInt(cursor.getColumnIndex("CustomerCheck")));
                    custList.add(aInfo);
                }
            }
            cursor.close();
        } catch (Exception exception) {
            Log.e("Customer", exception.toString());
            exception.printStackTrace();
        }
        return custList;
    }

    @SuppressLint("Range")
    public ArrayList<Customer> getCustomerList_SQLite() {
        ArrayList<Customer> orderList = new ArrayList<>();
        SQLiteDatabase database = dbHelperMain.getWritableDatabase();
        String oderMasterQuery = "Select * from tblCustomerInfo";

        try {
            Cursor cursor;
            cursor = database.rawQuery(oderMasterQuery, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    Customer aInfo = new Customer();
                    aInfo.setCustomerMasterId(cursor.getInt(cursor.getColumnIndex("CustomerMasterId")));
                    aInfo.setCustomerName(cursor.getString(cursor.getColumnIndex("CustomerName")));
                    aInfo.setCustomerCode(cursor.getString(cursor.getColumnIndex("CustomerCode")));
                    aInfo.setAddress(cursor.getString(cursor.getColumnIndex("CustomerAdress")));
                    aInfo.setCustomerType(cursor.getString(cursor.getColumnIndex("CustomerType")));
                    aInfo.setCellNo(cursor.getString(cursor.getColumnIndex("CustomerCell")));
                    aInfo.setBalance(cursor.getString(cursor.getColumnIndex("CustomerBalance")));
                    aInfo.setCreditLimit(cursor.getString(cursor.getColumnIndex("CustomerCreditlimit")));
                    aInfo.setMarketName(cursor.getString(cursor.getColumnIndex("Market")));
                    aInfo.setMarketCode(cursor.getString(cursor.getColumnIndex("MarketCode")));
                    aInfo.setCustomerCheck(cursor.getInt(cursor.getColumnIndex("CustomerCheck")));
                    orderList.add(aInfo);
                }
            }
            cursor.close();
        } catch (Exception exception) {
            Log.e("Customer", exception.toString());
            exception.printStackTrace();
        }
        return orderList;
    }


    @SuppressLint("Range")
    public ArrayList<CustomerARModel> getPendingCustomerFilter_SQLite(String Gid, String Rid, String Aid, String Tid, String STid, String Mid, String Status, String cusStatus, String Cid, String ptypeId, String SmcTypeId) {
        ArrayList<CustomerARModel> custList = new ArrayList<>();
        SQLiteDatabase database = dbHelperMain.getWritableDatabase();
        String oderMasterQuery = "Select * from tblCustomerReport";
        String parameter = " WHERE CustomerName is not null ";
        if (!Status.equals("Select")) {
            parameter = parameter + " AND  ActionStatus='" + Status + "'";
        }
        if (!cusStatus.equals("Select")) {
            parameter = parameter + " AND  CustomerStatus='" + cusStatus + "'";
        }
        if (!Gid.equals("0")) {
            parameter = parameter + " AND  GroupId='" + Gid + "'";
        }
        if (!Rid.equals("0")) {
            parameter = parameter + " AND  RegionId='" + Rid + "'";
        }
        if (!Aid.equals("0")) {
            parameter = parameter + " AND  AreaId='" + Aid + "'";
        }
        if (!Tid.equals("0")) {
            parameter = parameter + " AND  TerritoryId='" + Tid + "'";
        }
        if (!STid.equals("0")) {
            parameter = parameter + " AND  SubTerritoryId='" + STid + "'";
        }
        if (!Mid.equals("0")) {
            // parameter=parameter+" AND  MarketId="+Mid;
            parameter = parameter + " AND  MarketId='" + Mid + "'";
        }
        if (!Cid.equals("0")) {
            parameter = parameter + " AND  CustomerTypeId=" + Cid;
        }
        if (!ptypeId.equals("0")) {
            parameter = parameter + " AND  ProgramTypeId=" + ptypeId;
        }
        if (!SmcTypeId.equals("0")) {
            parameter = parameter + " AND  SMCTypeId=" + SmcTypeId;
        }

        String mainparam = oderMasterQuery + parameter;
        try {
            Cursor cursor;
            cursor = database.rawQuery(mainparam, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    CustomerARModel custinfoInfo = new CustomerARModel();
                    custinfoInfo.setCustomerName(cursor.getString(cursor.getColumnIndex("CustomerName")));
                    custinfoInfo.setMarketName(cursor.getString(cursor.getColumnIndex("MarketName")));
                    custinfoInfo.setMarketCode(cursor.getString(cursor.getColumnIndex("MarketCode")));
                    custinfoInfo.setActionStatus(cursor.getString(cursor.getColumnIndex("ActionStatus")));
                    custinfoInfo.setCellNo(cursor.getString(cursor.getColumnIndex("CellNo")));
                    custinfoInfo.setOwnerName(cursor.getString(cursor.getColumnIndex("OwnerName")));
                    custinfoInfo.setAddress(cursor.getString(cursor.getColumnIndex("Address")));
                    custinfoInfo.setImageBase64String(cursor.getString(cursor.getColumnIndex("ImageBase64String")));
                    custinfoInfo.setProgramTypeName(cursor.getString(cursor.getColumnIndex("ProgramTypeName")));
                    custinfoInfo.setWatingEmployee(cursor.getString(cursor.getColumnIndex("WatingEmployee")));
                    custinfoInfo.setWaitingRole(cursor.getString(cursor.getColumnIndex("WaitingRole")));
                    custinfoInfo.setCustomerStatus(cursor.getString(cursor.getColumnIndex("CustomerStatus")));
                    custList.add(custinfoInfo);
                }
            }
            cursor.close();
        } catch (Exception exception) {
            Log.e("Customer", exception.toString());
            exception.printStackTrace();
        }
        return custList;
    }

    @SuppressLint("Range")
    public ArrayList<CustomerARModel> getCustomerReport_SQLite(String status) {
        ArrayList<CustomerARModel> custList = new ArrayList<>();
        SQLiteDatabase database = dbHelperMain.getWritableDatabase();

        String custMasterQuerynll = "Select * from tblCustomerReport";
        String custMasterQuery = "Select * from tblCustomerReport where ActionStatus='" + status + "'";

        try {
            Cursor cursor;
            if (status.isEmpty()) {
                cursor = database.rawQuery(custMasterQuerynll, null);
            } else {
                cursor = database.rawQuery(custMasterQuery, null);
            }
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    CustomerARModel custinfoInfo = new CustomerARModel();
                    custinfoInfo.setCustomerName(cursor.getString(cursor.getColumnIndex("CustomerName")));
                    custinfoInfo.setMarketName(cursor.getString(cursor.getColumnIndex("MarketName")));
                    custinfoInfo.setActionStatus(cursor.getString(cursor.getColumnIndex("ActionStatus")));
                    custinfoInfo.setCellNo(cursor.getString(cursor.getColumnIndex("CellNo")));
                    custinfoInfo.setOwnerName(cursor.getString(cursor.getColumnIndex("OwnerName")));
                    custinfoInfo.setAddress(cursor.getString(cursor.getColumnIndex("Address")));
                    custinfoInfo.setImageBase64String(cursor.getString(cursor.getColumnIndex("ImageBase64String")));
                    custinfoInfo.setProgramTypeName(cursor.getString(cursor.getColumnIndex("ProgramTypeName")));
                    custinfoInfo.setWatingEmployee(cursor.getString(cursor.getColumnIndex("WatingEmployee")));
                    custinfoInfo.setWaitingRole(cursor.getString(cursor.getColumnIndex("WaitingRole")));
                    custinfoInfo.setCustomerStatus(cursor.getString(cursor.getColumnIndex("CustomerStatus")));
                    /*    custinfoInfo.setGroupName(cursor.getString(cursor.getColumnIndex("GroupName")));
                    custinfoInfo.setRegionName(cursor.getString(cursor.getColumnIndex("RegionName")));
                    custinfoInfo.setAreaName(cursor.getString(cursor.getColumnIndex("AreaName")));
                    custinfoInfo.setTerritoryName(cursor.getString(cursor.getColumnIndex("TerritoryName")));
                    custinfoInfo.setSubTerritoryName(cursor.getString(cursor.getColumnIndex("SubTerritoryName")));*/

                    custList.add(custinfoInfo);
                }
            }
            cursor.close();
        } catch (Exception exception) {
            Log.e("MIO", exception.toString());
            exception.printStackTrace();
        }
        return custList;
    }

    @SuppressLint("Range")
    public ArrayList<NonEffectiveReason> getNoneffective_SQLite() {
        ArrayList<NonEffectiveReason> nerList = new ArrayList<>();
        SQLiteDatabase database = dbHelperMain.getWritableDatabase();
        String oderMasterQuery = "Select * from tblNonEffectiveReason";

        try {
            Cursor cursor;
            cursor = database.rawQuery(oderMasterQuery, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    NonEffectiveReason nerInfo = new NonEffectiveReason();
                    nerInfo.setReasonId(cursor.getInt(cursor.getColumnIndex("ReasonId")));
                    nerInfo.setReasonName(cursor.getString(cursor.getColumnIndex("ReasonName")));
                    nerList.add(nerInfo);
                }
            }
            cursor.close();
        } catch (Exception exception) {
            Log.e("MIO", exception.toString());
            exception.printStackTrace();
        }
        return nerList;
    }

    @SuppressLint("Range")
    public ArrayList<ASM> getASMList_SQLite() {

        ArrayList<ASM> ASMList = new ArrayList<>();
        SQLiteDatabase database = dbHelperMain.getWritableDatabase();
        String oderMasterQuery = "Select * from tblASMInfo";

        try {
            Cursor cursor;
            cursor = database.rawQuery(oderMasterQuery, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    ASM asmInfo = new ASM();
                    asmInfo.setASMEmpId(cursor.getInt(cursor.getColumnIndex("ASMEmpId")));
                    asmInfo.setEmpName(cursor.getString(cursor.getColumnIndex("EmpName")));
                    asmInfo.setEmpMasterCode(cursor.getString(cursor.getColumnIndex("EmpMasterCode")));
                    ASMList.add(asmInfo);
                }
            }
            cursor.close();
        } catch (Exception exception) {
            Log.e("MIO", exception.toString());
            exception.printStackTrace();
        }
        return ASMList;
    }

    @SuppressLint("Range")
    public ArrayList<RSM> getRSMList_SQLite() {

        ArrayList<RSM> RSMList = new ArrayList<>();
        SQLiteDatabase database = dbHelperMain.getWritableDatabase();
        String oderMasterQuery = "Select * from tblRSMInfo";

        try {
            Cursor cursor;
            cursor = database.rawQuery(oderMasterQuery, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    RSM rsmInfo = new RSM();
                    rsmInfo.setRSMEmpId(cursor.getInt(cursor.getColumnIndex("RSMEmpId")));
                    rsmInfo.setEmpName(cursor.getString(cursor.getColumnIndex("EmpName")));
                    rsmInfo.setEmpMasterCode(cursor.getString(cursor.getColumnIndex("EmpMasterCode")));
                    RSMList.add(rsmInfo);
                }
            }
            cursor.close();
        } catch (Exception exception) {
            Log.e("MIO", exception.toString());
            exception.printStackTrace();
        }
        return RSMList;
    }

    @SuppressLint("Range")
    public ArrayList<Group> getGroupByIdList_SQLite() {

        ArrayList<Group> GroupList = new ArrayList<>();
        SQLiteDatabase database = dbHelperMain.getWritableDatabase();
        String oderMasterQuery = "Select * from tbl_Group";

        try {
            Cursor cursor;
            cursor = database.rawQuery(oderMasterQuery, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    Group groupInfo = new Group();
                    groupInfo.setGroupId(cursor.getInt(cursor.getColumnIndex("GroupId")));
                    groupInfo.setGroupName(cursor.getString(cursor.getColumnIndex("GroupName")));
                    GroupList.add(groupInfo);
                }
            }
            cursor.close();
        } catch (Exception exception) {
            Log.e("MIO", exception.toString());
            exception.printStackTrace();
        }
        return GroupList;
    }

    @SuppressLint("Range")
    public List<Region> getRegionByIdList_SQLite(int gid) {
        List<Region> RegionList = new ArrayList<>();
        SQLiteDatabase database = dbHelperMain.getWritableDatabase();
        String oderMasterQuerynll = "Select * from tblRegion";
        String oderMasterQuery = "Select * from tblRegion Where GroupId=" + gid + "";

        try {
            Cursor cursor;
            if (gid == 0 || String.valueOf(gid).isEmpty()) {
                cursor = database.rawQuery(oderMasterQuerynll, null);
            } else {
                cursor = database.rawQuery(oderMasterQuery, null);
            }
            // cursor = database.rawQuery(oderMasterQuery, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    Region regionInfo = new Region();
                    regionInfo.setRegionId(cursor.getInt(cursor.getColumnIndex("RegionId")));
                    regionInfo.setRegionName(cursor.getString(cursor.getColumnIndex("RegionName")));
                    RegionList.add(regionInfo);
                }
            }
            cursor.close();
        } catch (Exception exception) {
            Log.e("MIO", exception.toString());
            exception.printStackTrace();
        }
        return RegionList;
    }
 @SuppressLint("Range")
    public List<Region> getRegionByIdList_SQLiteEdit(int gid) {
        List<Region> RegionList = new ArrayList<>();
        SQLiteDatabase database = dbHelperMain.getWritableDatabase();
        String oderMasterQuerynll = "Select * from tblRegion";
        String oderMasterQuery = "Select * from tblRegion Where RegionId=" + gid + "";

        try {
            Cursor cursor;
            if (gid == 0 || String.valueOf(gid).isEmpty()) {
                cursor = database.rawQuery(oderMasterQuerynll, null);
            } else {
                cursor = database.rawQuery(oderMasterQuery, null);
            }
            // cursor = database.rawQuery(oderMasterQuery, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    Region regionInfo = new Region();
                    regionInfo.setRegionId(cursor.getInt(cursor.getColumnIndex("RegionId")));
                    regionInfo.setRegionName(cursor.getString(cursor.getColumnIndex("RegionName")));
                    RegionList.add(regionInfo);
                }
            }
            cursor.close();
        } catch (Exception exception) {
            Log.e("MIO", exception.toString());
            exception.printStackTrace();
        }
        return RegionList;
    }

    @SuppressLint("Range")
    public Region getRegionName_SQLite(int id) {
        Region RegionName = new Region();
        SQLiteDatabase database = dbHelperMain.getWritableDatabase();
        String oderMasterQuerynll = "Select * from tblRegion";
        String oderMasterQuery = "Select * from tblRegion Where RegionId=" + id + "";
        try {
            Cursor cursor;
            if (id == 0 || String.valueOf(id).isEmpty()) {
                cursor = database.rawQuery(oderMasterQuerynll, null);
            } else {
                cursor = database.rawQuery(oderMasterQuery, null);
            }
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    RegionName.setRegionName(cursor.getString(cursor.getColumnIndex("RegionName")));

                }
            }
            cursor.close();
        } catch (Exception exception) {
            Log.e("MIO", exception.toString());
            exception.printStackTrace();
        }
        return RegionName;
    }

    @SuppressLint("Range")
    public ArrayList<Area> getAreaByIdList_SQLite(int id) {
        ArrayList<Area> AreaList = new ArrayList<>();
        SQLiteDatabase database = dbHelperMain.getWritableDatabase();
        String oderMasterQuerynll = "Select * from tblArea";
        String oderMasterQuery = "Select * from tblArea where RegionId=" + id + "";

        try {

            Cursor cursor;
            if (id == 0 || String.valueOf(id).isEmpty()) {
                cursor = database.rawQuery(oderMasterQuerynll, null);
            } else {
                cursor = database.rawQuery(oderMasterQuery, null);
            }
            //   cursor = database.rawQuery(oderMasterQuery, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    Area areaInfo = new Area();
                    areaInfo.setAreaId(cursor.getInt(cursor.getColumnIndex("AreaId")));
                    areaInfo.setAreaName(cursor.getString(cursor.getColumnIndex("AreaName")));
                    AreaList.add(areaInfo);
                }
            }
            cursor.close();
        } catch (Exception exception) {
            Log.e("MIO", exception.toString());
            exception.printStackTrace();
        }
        return AreaList;
    }
    @SuppressLint("Range")
    public ArrayList<Area> getAreaByIdList_SQLiteEdit(int id) {
        ArrayList<Area> AreaList = new ArrayList<>();
        SQLiteDatabase database = dbHelperMain.getWritableDatabase();
        String oderMasterQuerynll = "Select * from tblArea";
        String oderMasterQuery = "Select * from tblArea where AreaId=" + id + "";

        try {

            Cursor cursor;
            if (id == 0 || String.valueOf(id).isEmpty()) {
                cursor = database.rawQuery(oderMasterQuerynll, null);
            } else {
                cursor = database.rawQuery(oderMasterQuery, null);
            }
            //   cursor = database.rawQuery(oderMasterQuery, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    Area areaInfo = new Area();
                    areaInfo.setAreaId(cursor.getInt(cursor.getColumnIndex("AreaId")));
                    areaInfo.setAreaName(cursor.getString(cursor.getColumnIndex("AreaName")));
                    AreaList.add(areaInfo);
                }
            }
            cursor.close();
        } catch (Exception exception) {
            Log.e("MIO", exception.toString());
            exception.printStackTrace();
        }
        return AreaList;
    }

    @SuppressLint("Range")
    public Area getAreaName_SQLite(int id) {
        Area AreaName = new Area();
        SQLiteDatabase database = dbHelperMain.getWritableDatabase();
        String oderMasterQuerynll = "Select * from tblArea";
        String oderMasterQuery = "Select * from tblArea Where AreaId=" + id + "";

        try {
            Cursor cursor;
            if (id == 0 || String.valueOf(id).isEmpty()) {
                cursor = database.rawQuery(oderMasterQuerynll, null);
            } else {
                cursor = database.rawQuery(oderMasterQuery, null);
            }
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    // Region regionInfo = new Region();
                    AreaName.setAreaName(cursor.getString(cursor.getColumnIndex("AreaName")));
                }
            }
            cursor.close();
        } catch (Exception exception) {
            Log.e("MIO", exception.toString());
            exception.printStackTrace();
        }
        return AreaName;
    }

    @SuppressLint("Range")
    public ArrayList<Teritorry> getTerritoryByIdList_SQLite(int id) {
        ArrayList<Teritorry> TeritorryList = new ArrayList<>();
        SQLiteDatabase database = dbHelperMain.getWritableDatabase();
        String oderMasterQuerynll = "Select * from tblTerritory";
        String oderMasterQuery = "Select * from tblTerritory where AreaId=" + id + "";

        try {
            Cursor cursor;
            if (id == 0 || String.valueOf(id).isEmpty()) {
                cursor = database.rawQuery(oderMasterQuerynll, null);
            } else {
                cursor = database.rawQuery(oderMasterQuery, null);
            }

            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    Teritorry teritorryInfo = new Teritorry();
                    teritorryInfo.setTerritoryId(cursor.getInt(cursor.getColumnIndex("TerritoryId")));
                    teritorryInfo.setTerritoryName(cursor.getString(cursor.getColumnIndex("TerritoryName")));
                    TeritorryList.add(teritorryInfo);
                }
            }
            cursor.close();
        } catch (Exception exception) {
            Log.e("MIO", exception.toString());
            exception.printStackTrace();
        }
        return TeritorryList;
    }
    @SuppressLint("Range")
    public ArrayList<Teritorry> getTerritoryByIdList_SQLiteEdit(int id) {
        ArrayList<Teritorry> TeritorryList = new ArrayList<>();
        SQLiteDatabase database = dbHelperMain.getWritableDatabase();
        String oderMasterQuerynll = "Select * from tblTerritory";
        String oderMasterQuery = "Select * from tblTerritory where TerritoryId=" + id + "";

        try {
            Cursor cursor;
            if (id == 0 || String.valueOf(id).isEmpty()) {
                cursor = database.rawQuery(oderMasterQuerynll, null);
            } else {
                cursor = database.rawQuery(oderMasterQuery, null);
            }

            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    Teritorry teritorryInfo = new Teritorry();
                    teritorryInfo.setTerritoryId(cursor.getInt(cursor.getColumnIndex("TerritoryId")));
                    teritorryInfo.setTerritoryName(cursor.getString(cursor.getColumnIndex("TerritoryName")));
                    TeritorryList.add(teritorryInfo);
                }
            }
            cursor.close();
        } catch (Exception exception) {
            Log.e("MIO", exception.toString());
            exception.printStackTrace();
        }
        return TeritorryList;
    }
    @SuppressLint("Range")
    public Teritorry getTerritoryName_SQLite(int id) {
        Teritorry TeritorryName = new Teritorry();
        SQLiteDatabase database = dbHelperMain.getWritableDatabase();
        String oderMasterQuerynll = "Select * from tblTerritory";
        String oderMasterQuery = "Select * from tblTerritory Where TerritoryId=" + id + "";

        try {
            Cursor cursor;
            if (id == 0 || String.valueOf(id).isEmpty()) {
                cursor = database.rawQuery(oderMasterQuerynll, null);
            } else {
                cursor = database.rawQuery(oderMasterQuery, null);
            }
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    TeritorryName.setTerritoryName(cursor.getString(cursor.getColumnIndex("TerritoryName")));
                }
            }
            cursor.close();
        } catch (Exception exception) {
            Log.e("MIO", exception.toString());
            exception.printStackTrace();
        }
        return TeritorryName;
    }

    @SuppressLint("Range")
    public ArrayList<SubTeritorry> getSubTerritoryByIdList_SQLite(int id) {
        ArrayList<SubTeritorry> SubTeritorryList = new ArrayList<>();
        SQLiteDatabase database = dbHelperMain.getWritableDatabase();
        String oderMasterQuerynll = "Select * from tblSubTerritory";
        String oderMasterQuery = "Select * from tblSubTerritory where TerritoryId= " + id + "";
        try {
            Cursor cursor;
            if (id == 0 || String.valueOf(id).isEmpty()) {
                cursor = database.rawQuery(oderMasterQuerynll, null);
            } else {
                cursor = database.rawQuery(oderMasterQuery, null);
            }

            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    SubTeritorry subteritorryInfo = new SubTeritorry();
                    subteritorryInfo.setSubTerritoryId(cursor.getInt(cursor.getColumnIndex("SubTerritoryId")));
                    subteritorryInfo.setSubTerritoryName(cursor.getString(cursor.getColumnIndex("SubTerritoryName")));
                    SubTeritorryList.add(subteritorryInfo);
                }
            }
            cursor.close();
        } catch (Exception exception) {
            Log.e("MIO", exception.toString());
            exception.printStackTrace();
        }
        return SubTeritorryList;
    }

    @SuppressLint("Range")
    public ArrayList<SubTeritorry> getSubTerritoryByIdList_SQLiteEdit(int id) {
        ArrayList<SubTeritorry> SubTeritorryList = new ArrayList<>();
        SQLiteDatabase database = dbHelperMain.getWritableDatabase();
        String oderMasterQuerynll = "Select * from tblSubTerritory";
        String oderMasterQuery = "Select * from tblSubTerritory where SubTerritoryId= " + id + "";
        try {
            Cursor cursor;
            if (id == 0 || String.valueOf(id).isEmpty()) {
                cursor = database.rawQuery(oderMasterQuerynll, null);
            } else {
                cursor = database.rawQuery(oderMasterQuery, null);
            }

            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    SubTeritorry subteritorryInfo = new SubTeritorry();
                    subteritorryInfo.setSubTerritoryId(cursor.getInt(cursor.getColumnIndex("SubTerritoryId")));
                    subteritorryInfo.setSubTerritoryName(cursor.getString(cursor.getColumnIndex("SubTerritoryName")));
                    SubTeritorryList.add(subteritorryInfo);
                }
            }
            cursor.close();
        } catch (Exception exception) {
            Log.e("MIO", exception.toString());
            exception.printStackTrace();
        }
        return SubTeritorryList;
    }

    @SuppressLint("Range")
    public SubTeritorry getSubTerritoryName_SQLite(int id) {
        SubTeritorry SubTeritorryName = new SubTeritorry();
        SQLiteDatabase database = dbHelperMain.getWritableDatabase();
        String oderMasterQuerynll = "Select * from tblSubTerritory";
        String oderMasterQuery = "Select * from tblSubTerritory Where SubTerritoryId=" + id + "";

        try {
            Cursor cursor;
            if (id == 0 || String.valueOf(id).isEmpty()) {
                cursor = database.rawQuery(oderMasterQuerynll, null);
            } else {
                cursor = database.rawQuery(oderMasterQuery, null);
            }
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    // Region regionInfo = new Region();
                    SubTeritorryName.setSubTerritoryName(cursor.getString(cursor.getColumnIndex("SubTerritoryName")));
                }
            }
            cursor.close();
        } catch (Exception exception) {
            Log.e("MIO", exception.toString());
            exception.printStackTrace();
        }
        return SubTeritorryName;
    }

    @SuppressLint("Range")
    public ArrayList<Market> getMarketByIdList_SQLite(int id) {

        ArrayList<Market> MarketList = new ArrayList<>();
        SQLiteDatabase database = dbHelperMain.getWritableDatabase();
        String oderMasterQuery = "Select * from tblMarket where SubTerritoryId=" + id + "";
        String oderMasterQuerynll = "Select * from tblMarket";


        try {
            Cursor cursor;
            if (id == 0 || String.valueOf(id).isEmpty()) {
                cursor = database.rawQuery(oderMasterQuerynll, null);
            } else {
                cursor = database.rawQuery(oderMasterQuery, null);
            }
            //cursor = database.rawQuery(oderMasterQuery, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    Market marketInfo = new Market();
                    marketInfo.setMarketId(cursor.getInt(cursor.getColumnIndex("MarketId")));
                    marketInfo.setMarketName(cursor.getString(cursor.getColumnIndex("MarketName")));
                    MarketList.add(marketInfo);
                }
            }
            cursor.close();
        } catch (Exception exception) {
            Log.e("MIO", exception.toString());
            exception.printStackTrace();
        }
        return MarketList;
    }

    @SuppressLint("Range")
    public ArrayList<Market> getMarketByIdList_SQLiteEdit(int id) {

        ArrayList<Market> MarketList = new ArrayList<>();
        SQLiteDatabase database = dbHelperMain.getWritableDatabase();
        String oderMasterQuery = "Select * from tblMarket where MarketId=" + id + "";
        String oderMasterQuerynll = "Select * from tblMarket";


        try {
            Cursor cursor;
            if (id == 0 || String.valueOf(id).isEmpty()) {
                cursor = database.rawQuery(oderMasterQuerynll, null);
            } else {
                cursor = database.rawQuery(oderMasterQuery, null);
            }
            //cursor = database.rawQuery(oderMasterQuery, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    Market marketInfo = new Market();
                    marketInfo.setMarketId(cursor.getInt(cursor.getColumnIndex("MarketId")));
                    marketInfo.setMarketName(cursor.getString(cursor.getColumnIndex("MarketName")));
                    MarketList.add(marketInfo);
                }
            }
            cursor.close();
        } catch (Exception exception) {
            Log.e("MIO", exception.toString());
            exception.printStackTrace();
        }
        return MarketList;
    }

    @SuppressLint("Range")
    public ArrayList<Market> getMarketListAll_SQLite() {

        ArrayList<Market> MarketList = new ArrayList<>();
        SQLiteDatabase database = dbHelperMain.getWritableDatabase();
     // String oderMasterQuery = "Select * from tblMarket";
        String oderMasterQueryall = "Select * from tblMarket";


        try {
            Cursor cursor;

                cursor = database.rawQuery(oderMasterQueryall, null);

            //cursor = database.rawQuery(oderMasterQuery, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    Market marketInfo = new Market();
                    marketInfo.setMarketId(cursor.getInt(cursor.getColumnIndex("MarketId")));
                    marketInfo.setMarketName(cursor.getString(cursor.getColumnIndex("MarketName")));
                    MarketList.add(marketInfo);
                }
            }
            cursor.close();
        } catch (Exception exception) {
            Log.e("MIO", exception.toString());
            exception.printStackTrace();
        }
        return MarketList;
    }

    @SuppressLint("Range")
    public ArrayList<Market> getMarketListAll_SQLiteByTerritoryId(String TerritoryId) {

        ArrayList<Market> MarketList = new ArrayList<>();
        SQLiteDatabase database = dbHelperMain.getWritableDatabase();
     // String oderMasterQuery = "Select * from tblMarket";
        String oderMasterQueryall = "Select distinct mr.* from tblMarket mr \n" +
                "inner join tblSubTerritory sub on mr.SubTerritoryId=sub.SubTerritoryId\n" +
                "where sub.TerritoryId in ("+TerritoryId+") order by mr.MarketName asc";


        try {
            Cursor cursor;

                cursor = database.rawQuery(oderMasterQueryall, null);

            //cursor = database.rawQuery(oderMasterQuery, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    Market marketInfo = new Market();
                    marketInfo.setMarketId(cursor.getInt(cursor.getColumnIndex("MarketId")));
                    marketInfo.setMarketName(cursor.getString(cursor.getColumnIndex("MarketName")));
                    MarketList.add(marketInfo);
                }
            }
            cursor.close();
        } catch (Exception exception) {
            Log.e("MIO", exception.toString());
            exception.printStackTrace();
        }
        return MarketList;
    }

    @SuppressLint("Range")
    public ArrayList<TourPurposeViewModel> getTPPList_SQLite() {
        ArrayList<TourPurposeViewModel> TPPList = new ArrayList<>();
        SQLiteDatabase database = dbHelperMain.getWritableDatabase();
        String oderMasterQuery = "Select * from tblTourPlanPurpose";
        try {
            Cursor cursor;
            cursor = database.rawQuery(oderMasterQuery, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    TourPurposeViewModel tppInfo = new TourPurposeViewModel();
                    tppInfo.setTPId(cursor.getInt(cursor.getColumnIndex("TPId")));
                    tppInfo.setTPName(cursor.getString(cursor.getColumnIndex("TPName")));
                    TPPList.add(tppInfo);
                }
            }
            cursor.close();
        } catch (Exception exception) {
            Log.e("MIO", exception.toString());
            exception.printStackTrace();
        }
        return TPPList;
    }



    @SuppressLint("Range")
    public ArrayList<TourPurposeViewModel>  getTPPListForMarketVisit_SQLite  () {
        ArrayList<TourPurposeViewModel> TPPList = new ArrayList<>();
        SQLiteDatabase database = dbHelperMain.getWritableDatabase();
        String oderMasterQuery = "Select * from tblTourPlanPurpose where IsMarketVisit=1";
        try {
            Cursor cursor;
            cursor = database.rawQuery(oderMasterQuery, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    TourPurposeViewModel tppInfo = new TourPurposeViewModel();
                    tppInfo.setTPId(cursor.getInt(cursor.getColumnIndex("TPId")));
                    tppInfo.setTPName(cursor.getString(cursor.getColumnIndex("TPName")));
                    TPPList.add(tppInfo);
                }
            }
            cursor.close();
        } catch (Exception exception) {
            Log.e("MIO", exception.toString());
            exception.printStackTrace();
        }
        return TPPList;
    }

    @SuppressLint("Range")
    public ArrayList<TourPurposeViewModel> getTPPListForOtherVisit_SQLite () {
        ArrayList<TourPurposeViewModel> TPPList = new ArrayList<>();
        SQLiteDatabase database = dbHelperMain.getWritableDatabase();
        String oderMasterQuery = "Select * from tblTourPlanPurpose where IsOtherVisit=1";
        try {
            Cursor cursor;
            cursor = database.rawQuery(oderMasterQuery, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    TourPurposeViewModel tppInfo = new TourPurposeViewModel();
                    tppInfo.setTPId(cursor.getInt(cursor.getColumnIndex("TPId")));
                    tppInfo.setTPName(cursor.getString(cursor.getColumnIndex("TPName")));
                    TPPList.add(tppInfo);
                }
            }
            cursor.close();
        } catch (Exception exception) {
            Log.e("MIO", exception.toString());
            exception.printStackTrace();
        }
        return TPPList;
    }

    @SuppressLint("Range")
    public void InsertIntoLoginProfile_SQLite(int userId, String userName, String EmpMasterCode, String loginName, int empId, String userCo, String EmpRole, int RoleTypeId, String RoleType, int IsApprove, int IsForward, String DesigName) {
        try {
            if (CheckIfDifferentUser(loginName)) {
                _deleteAllRecordsFromaTable("tblAttendance");
            }

            String tableName = "tblLoginProfile";
            _deleteAllRecordsFromaTable(tableName);
            SQLiteDatabase database = dbHelperMain.getWritableDatabase();
            String insertQuery = "Insert into tblLoginProfile(UserId,UserName,EmpMasterCode,LoginName,empId,UserCo,EmpRole,RoleTypeId,RoleType,IsApprove,IsForward,DesigName) " +
                    "values('" + userId + "','" + userName + "','" + EmpMasterCode + "','" + loginName + "'," +
                    "'" + empId + "','" + userCo + "','" + EmpRole + "','" + RoleTypeId + "','" + RoleType + "','" + IsApprove + "','" + IsForward + "','" + DesigName.replace("'", "''") + "')";
            database.execSQL(insertQuery);

        } catch (Exception exception) {
            Log.e("DBEX", exception.toString());
        }
    }

    @SuppressLint("Range")
    public boolean checkLoginProfileUserExist(String loginName, String password) {
        SQLiteDatabase database = dbHelperMain.getWritableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM  tblLoginProfile where LoginName ='" + loginName + "' and UserCo ='" + password + "'", null);
        int count = cursor.getCount();
        if (count > 0) {
            return true;
        } else {
            return false;
        }
    }

    @SuppressLint("Range")
    public boolean CheckIfDifferentUser(String loginName) {
        SQLiteDatabase database = dbHelperMain.getWritableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM  tblLoginProfile where LoginName ='" + loginName + "' ", null);
        int count = cursor.getCount();
        if (count > 0) {
            return true;
        } else {
            return false;
        }
    }

    @SuppressLint("Range")
    public void getLoginProfile_SQLite() {

        sessionManagement = new SessionManagement(context);
        SQLiteDatabase database = dbHelperMain.getWritableDatabase();
        String oderMasterQuery = "Select * from tblLoginProfile";
        try {
            Cursor cursor;
            cursor = database.rawQuery(oderMasterQuery, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {

                    int userId = 0;
                    // String userName = "";
                    //String empMastercode = "";
                    // String loginName = "";
                    // String password = "";
                    // int empId = 0;
                    //  int roleTypeId = 0;
                    //String role, roleType,designation;

                    userId = cursor.getInt(cursor.getColumnIndex("UserId"));
                    String userName = cursor.getString(cursor.getColumnIndex("UserName"));
                    String empMastercode = cursor.getString(cursor.getColumnIndex("EmpMasterCode"));
                    String loginName = cursor.getString(cursor.getColumnIndex("LoginName"));
                    int empId = cursor.getInt(cursor.getColumnIndex("empId"));
                    String role = cursor.getString(cursor.getColumnIndex("EmpRole"));
                    int roleTypeId = cursor.getInt(cursor.getColumnIndex("RoleTypeId"));
                    String roleType = cursor.getString(cursor.getColumnIndex("RoleType"));
                    String designation = cursor.getString(cursor.getColumnIndex("DesigName"));
                    String password = cursor.getString(cursor.getColumnIndex("Password"));
                    sessionManagement.createLoginSession(userId, userName, empMastercode, loginName, password, empId, "", "", role, String.valueOf(roleTypeId), roleType, designation);
                }
            }
            cursor.close();
        } catch (Exception exception) {
            Log.e("LOginProfile", exception.toString());
            exception.printStackTrace();
        }
    }

    @SuppressLint("Range")
    public ArrayList<User> getIsForoward_SQLite() {
        ArrayList<User> isList = new ArrayList<>();
        SQLiteDatabase database = dbHelperMain.getWritableDatabase();
        String MasterQuery = "Select * from tblLoginProfile";

        try {
            Cursor cursor;
            cursor = database.rawQuery(MasterQuery, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    User userinf = new User();
                    userinf.setIsApprovei(cursor.getInt(cursor.getColumnIndex("IsApprove")));
                    userinf.setIsForwardi(cursor.getInt(cursor.getColumnIndex("IsForward")));
                    isList.add(userinf);
                }
            }

        } catch (Exception exception) {
            Log.e("MIO", exception.toString());
            exception.printStackTrace();
        }
        return isList;
    }

    public boolean checkDataExistInAttendanceTable_withCurrentDate(String attDate) {

        SQLiteDatabase database = dbHelperMain.getWritableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM  tblAttendance where AttendanceDate ='" + attDate + "'", null);
        int count = cursor.getCount();
        if (count > 0) {
            return true;
        } else {
            return false;
        }


    }

    public boolean checkDataExistInDCRTable_(int dcrId) {

        SQLiteDatabase database = dbHelperMain.getWritableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM  tblDcrInfo where DcrId ='" + dcrId + "'", null);
        int count = cursor.getCount();

        if (count > 0) {
            return true;
        } else {
            return false;
        }


    }

    public boolean checkDataExistInPrescTable_(int prescId) {

        SQLiteDatabase database = dbHelperMain.getWritableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM  tblPrescriptionInfo where PrescripId ='" + prescId + "'", null);
        int count = cursor.getCount();
        if (count > 0) {
            return true;
        } else {
            return false;
        }


    }

    public void InsertPunchINInfo_AttendanceTable_SQLite(Attendance aInfo) {
        try {
            String tableName = "tblAttendance";
            _deleteAllRecordsFromaTable(tableName);
            SQLiteDatabase database = dbHelperMain.getWritableDatabase();
            String insertQuery = "Insert into tblAttendance(EmpId,PInTime,PInLat,PInLong,AttendanceDate,AttImg) " +
                    "values('" + aInfo.getEmpInfoId() + "','" + aInfo.getPunchInTime() + "','" + aInfo.getPInLat() + "'," +
                    "'" + aInfo.getPInLog() + "','" + aInfo.getAttendanceDate() + "','" + aInfo.getAttImg() + "')";
            database.execSQL(insertQuery);
        } catch (Exception exception) {
            Log.e("DBEX", exception.toString());
        }
    }

    public void UpdatePunchOUTInfo_AttendanceTable_SQLite(Attendance aInfo) {
        try {
            String tableName = "tblAttendance";
            SQLiteDatabase database = dbHelperMain.getWritableDatabase();
            String insertQuery = "update tblAttendance set completeStatus = 'Completed' where  EmpId ='" + aInfo.getEmpInfoId() + "' and AttendanceDate = '" + aInfo.getAttendanceDate() + "'";
            database.execSQL(insertQuery);
        } catch (Exception exception) {
            Log.e("DBEX", exception.toString());
        }
    }

    public boolean CheckPuncOUTComplete_AttendanceTable_SQLite(String attDate) {
        SQLiteDatabase database = dbHelperMain.getWritableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM  tblAttendance where AttendanceDate ='" + attDate + "' and completeStatus='Completed'", null);
        int count = cursor.getCount();
        if (count > 0) {
            return true;
        } else {
            return false;
        }
    }

    public void DeleteOldOrder_OrderTable_SQLite(int orderIdLocal) {
        try {
            SQLiteDatabase database = dbHelperMain.getWritableDatabase();
            String deletOrderMatserQUERY = "DELETE from tblOrderMaster where OrderIdLocal not in (select OrderIdLocal from tblOrderMaster order by OrderIdLocal desc limit 25)";
            String deletOrderDetailsQUERY = "DELETE from tblOrderDetails where OrderIdLocal not in (select OrderIdLocal from tblOrderMaster order by OrderIdLocal desc limit 25)";

            String deleteOrederMaster = "DELETE from tblOrderMaster where OrderIdLocal=" + orderIdLocal;
            String deleteOrederDetails = "DELETE from tblOrderDetails where OrderIdLocal=" + orderIdLocal;

            database.execSQL(deleteOrederMaster);
            database.execSQL(deleteOrederDetails);
            // database.execSQL(deletOrderMatserQUERY);
            //database.execSQL(deletOrderDetailsQUERY);

        } catch (Exception exception) {
            Log.d("sqlite", "DeleteOldOrder_OrderTable_SQLite: ");
        }
    }

    public boolean DeleteOldOrder_SQLite(int orderIdLocal) {
        try {
            SQLiteDatabase database = dbHelperMain.getWritableDatabase();
            String deletOrderMatserQUERY = "DELETE from tblOrderMaster where OrderIdLocal not in (select OrderIdLocal from tblOrderMaster order by OrderIdLocal desc limit 25)";
            String deletOrderDetailsQUERY = "DELETE from tblOrderDetails where OrderIdLocal not in (select OrderIdLocal from tblOrderMaster order by OrderIdLocal desc limit 25)";

            String deleteOrederMaster = "DELETE from tblOrderMaster where OrderIdLocal=" + orderIdLocal;
            String deleteOrederDetails = "DELETE from tblOrderDetails where OrderIdLocal=" + orderIdLocal;

            database.execSQL(deleteOrederMaster);
            database.execSQL(deleteOrederDetails);
            // database.execSQL(deletOrderMatserQUERY);
            //database.execSQL(deletOrderDetailsQUERY);

        } catch (Exception exception) {
            Log.d("sqlite", "DeleteOldOrder_OrderTable_SQLite: ");
        }
        return true;
    }

    public boolean DeleteLocal_PrescripTable_SQLite(int prescrip) {
        try {
            SQLiteDatabase database = dbHelperMain.getWritableDatabase();
            String deleteOrederMaster = "DELETE from tblPrescriptionInfo where PrescripId=" + prescrip;
            String deleteOrederDetails = "DELETE from tblPrescriptionMaster where PrescripId=" + prescrip;

            database.execSQL(deleteOrederMaster);
            database.execSQL(deleteOrederDetails);

        } catch (Exception exception) {
            Log.d("sqlite", "DeletePrescrip Table_SQLite: ");
        }
        return true;
    }

    public boolean DeleteLocal_PrescripData_SQLite(int prescrip) {
        try {
            SQLiteDatabase database = dbHelperMain.getWritableDatabase();
            String deleteOrederDetails = "DELETE from tblPrescriptionMaster where PrescripId=" + prescrip;
            database.execSQL(deleteOrederDetails);
        } catch (Exception exception) {
            Log.d("sqlite", "DeletePrescrip Table_SQLite: ");
        }
        return true;
    }

    public boolean DeleteLocal_DcrTable_SQLite(int dcrid) {
        try {
            SQLiteDatabase database = dbHelperMain.getWritableDatabase();
            String deleteOrederMaster = "DELETE from tblDcrInfo where DcrId=" + dcrid;
            String deleteGift = "DELETE from tblDcrGift where DcrId=" + dcrid;
            String deleteSample = "DELETE from tblDcrSample where DcrId=" + dcrid;
            String deleteVisited = "DELETE from tblDcrVisitedwith where DcrId=" + dcrid;

            database.execSQL(deleteOrederMaster);
            database.execSQL(deleteGift);
            database.execSQL(deleteSample);
            database.execSQL(deleteVisited);

        } catch (Exception exception) {
            Log.d("sqlite", "DeleteDcr Table_SQLite: ");
        }
        return true;
    }

    public boolean DeleteLocal_DcrData_SQLite(int dcrid) {
        try {
            SQLiteDatabase database = dbHelperMain.getWritableDatabase();
            String deleteGift = "DELETE from tblDcrGift where DcrId=" + dcrid + "";
            String deleteBrand = "DELETE from tblDcrBrand where BrandId=" + dcrid + "";
            String deleteSample = "DELETE from tblDcrSample where DcrId=" + dcrid + "";
            String deleteVisited = "DELETE from tblDcrVisitedwith where DcrId=" + dcrid + "";

            database.execSQL(deleteGift);
            database.execSQL(deleteBrand);
            database.execSQL(deleteSample);
            database.execSQL(deleteVisited);

        } catch (Exception exception) {
            Log.d("sqlite", "DeleteDcr Table_SQLite: ");
        }
        return true;
    }

    @SuppressLint("Range")
    public int getCurrentUserGroupId_SQLite() {

        SQLiteDatabase database = dbHelperMain.getWritableDatabase();
        String oderMasterQuery = "Select * from tbl_Group";
        int groupId = 0;
        try {
            Cursor cursor;
            cursor = database.rawQuery(oderMasterQuery, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {

                    groupId = cursor.getInt(cursor.getColumnIndex("GroupId"));
                }
            }
            cursor.close();
        } catch (Exception exception) {
            Log.e("LOginProfile", exception.toString());
            exception.printStackTrace();
        }
        return groupId;
    }

    @SuppressLint("Range")
    public int getCurrentUserRegionId_SQLite() {

        SQLiteDatabase database = dbHelperMain.getWritableDatabase();
        String oderMasterQuery = "Select * from tblRegion";
        int regionId = 0;
        try {
            Cursor cursor;
            cursor = database.rawQuery(oderMasterQuery, null);

            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {


                    regionId = cursor.getInt(cursor.getColumnIndex("RegionId"));
                }
            }
            cursor.close();
        } catch (Exception exception) {
            Log.e("LOginProfile", exception.toString());
            exception.printStackTrace();
        }
        return regionId;
    }

    @SuppressLint("Range")
    public int getCurrentUserAreaId_SQLite() {

        SQLiteDatabase database = dbHelperMain.getWritableDatabase();
        String oderMasterQuery = "Select * from tblArea";
        int areaId = 0;
        try {
            Cursor cursor;
            cursor = database.rawQuery(oderMasterQuery, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    areaId = cursor.getInt(cursor.getColumnIndex("AreaId"));
                }
            }
            cursor.close();
        } catch (Exception exception) {
            Log.e("LOginProfile", exception.toString());
            exception.printStackTrace();
        }
        return areaId;
    }

    @SuppressLint("Range")
    public double[] GetQuotedPrice(int customerId, int productId) {
        double[] quoted = new double[2];
        quoted[0] = 0.00;
        quoted[1] = 0.00;
        try {
            SQLiteDatabase database = dbHelperMain.getWritableDatabase();
            String query = "Select * from tblQuotedPrice where customerMasterId='" + customerId + "' and productId=='" + productId + "'";
            Cursor cursor;
            cursor = database.rawQuery(query, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    String price = cursor.getString(cursor.getColumnIndex("unitPrice"));
                    String vat = cursor.getString(cursor.getColumnIndex("vat"));
                    quoted[0] = Double.parseDouble(price);
                    quoted[1] = Double.parseDouble(vat);
                }
            }
            cursor.close();
        } catch (Exception ex) {
            quoted[0] = 0.00;
            quoted[1] = 0.00;
        }
        return quoted;
    }

    @SuppressLint("Range")
    public ArrayList<StationType> getStationTypeListFromSQLite() {
        ArrayList<StationType> stationTypeList = new ArrayList<>();
        SQLiteDatabase database = dbHelperMain.getWritableDatabase();
        String desigQuery = "Select * from tblCustomer_Station order by StationTypeId desc";

        try {
            Cursor cursor;
            cursor = database.rawQuery(desigQuery, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    StationType ddInfo = new StationType();
                    ddInfo.setStationTypeId(cursor.getInt(cursor.getColumnIndex("StationTypeId")));
                    ddInfo.setStationTypeName(cursor.getString(cursor.getColumnIndex("StationTypeName")));
                    stationTypeList.add(ddInfo);
                }
            }
            cursor.close();
        } catch (Exception exception) {
            Log.e("CampOrderDetails", exception.toString());
            exception.printStackTrace();
        }
        return stationTypeList;
    }

    @SuppressLint("Range")
    public ArrayList<Transport> getTransportTypeListFromSQLite() {

        ArrayList<Transport> transportTypeList = new ArrayList<>();
        SQLiteDatabase database = dbHelperMain.getWritableDatabase();
        String desigQuery = "Select * from tblTransportInfo";

        try {
            Cursor cursor;
            cursor = database.rawQuery(desigQuery, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    Transport ddInfo = new Transport();
                    ddInfo.setTransportId(cursor.getInt(cursor.getColumnIndex("TransportId")));
                    ddInfo.setTransportName(cursor.getString(cursor.getColumnIndex("TransportName")));
                    transportTypeList.add(ddInfo);
                }
            }
            cursor.close();
        } catch (Exception exception) {
            Log.e("CampOrderDetails", exception.toString());
            exception.printStackTrace();
        }
        return transportTypeList;
    }

    @SuppressLint("Range")
    public ArrayList<CustomerType> getCustTypeListFromSQLite() {

        ArrayList<CustomerType> custTypeList = new ArrayList<>();
        SQLiteDatabase database = dbHelperMain.getWritableDatabase();
        String desigQuery = "Select * from tblCustomer_Type order by CustomerTypeId desc";

        try {
            Cursor cursor;
            cursor = database.rawQuery(desigQuery, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    CustomerType ddInfo = new CustomerType();
                    ddInfo.setCustomerTypeId(cursor.getInt(cursor.getColumnIndex("CustomerTypeId")));
                    ddInfo.setCustomerType(cursor.getString(cursor.getColumnIndex("CustomerType")));
                    custTypeList.add(ddInfo);
                }
            }
            cursor.close();
        } catch (Exception exception) {
            Log.e("CampOrderDetails", exception.toString());
            exception.printStackTrace();
        }
        return custTypeList;
    }

    @SuppressLint("Range")
    public ArrayList<PrescriptionTYpe> getPrescriptionTypeListFromSQLite() {

        ArrayList<PrescriptionTYpe> presTypeList = new ArrayList<>();
        SQLiteDatabase database = dbHelperMain.getWritableDatabase();
        String desigQuery = "Select * from tblPrescrip_Type order by PrescriptionTypeId desc";

        try {
            Cursor cursor;
            cursor = database.rawQuery(desigQuery, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    PrescriptionTYpe ddInfo = new PrescriptionTYpe();
                    ddInfo.setPrescriptionTypeId(cursor.getInt(cursor.getColumnIndex("PrescriptionTypeId")));
                    ddInfo.setPrescriptionType(cursor.getString(cursor.getColumnIndex("PrescriptionType")));
                    presTypeList.add(ddInfo);
                }
            }
            cursor.close();
        } catch (Exception exception) {
            Log.e("CampOrderDetails", exception.toString());
            exception.printStackTrace();
        }
        return presTypeList;
    }

}
