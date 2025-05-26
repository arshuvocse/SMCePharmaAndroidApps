package com.creatrix.salessolution.DBAdapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.creatrix.salessolution.Activity.Doctor.DCR.NonEffectiveReason;
import com.creatrix.salessolution.DBInfo.DBHelperMain;
import com.creatrix.salessolution.Model.Doctor.ContactTypeVM;
import com.creatrix.salessolution.Model.Customer;
import com.creatrix.salessolution.Model.CustomerType;
import com.creatrix.salessolution.Model.Doctor.Brand;
import com.creatrix.salessolution.Model.Doctor.DoctorBrand;
import com.creatrix.salessolution.Model.Doctor.DoctorCategory;
import com.creatrix.salessolution.Model.Doctor.DoctorChamberName;
import com.creatrix.salessolution.Model.Doctor.DoctorChamberTypeVM;
import com.creatrix.salessolution.Model.Doctor.DoctorDegreeViewModel;
import com.creatrix.salessolution.Model.Doctor.DoctorDesignation;
import com.creatrix.salessolution.Model.Doctor.DoctorListViewModel;
import com.creatrix.salessolution.Model.Doctor.DoctorSpecialityViewModel;
import com.creatrix.salessolution.Model.Doctor.DoctorTypeVM;
import com.creatrix.salessolution.Model.Doctor.ProgramType;
import com.creatrix.salessolution.Model.Doctor.SpecialDay;
import com.creatrix.salessolution.Model.ExpenseTypeMaster;
import com.creatrix.salessolution.Model.Gift;
import com.creatrix.salessolution.Model.InstitutionVM;
import com.creatrix.salessolution.Model.LeaveTypeInfo;
import com.creatrix.salessolution.Model.MarketStructure.AssignEmpTable.ASM;
import com.creatrix.salessolution.Model.MarketStructure.AssignEmpTable.MIO;
import com.creatrix.salessolution.Model.MarketStructure.AssignEmpTable.NSM;
import com.creatrix.salessolution.Model.MarketStructure.AssignEmpTable.RSM;
import com.creatrix.salessolution.Model.MarketStructure.StructureTable.Area;
import com.creatrix.salessolution.Model.MarketStructure.StructureTable.Group;
import com.creatrix.salessolution.Model.MarketStructure.StructureTable.Market;
import com.creatrix.salessolution.Model.MarketStructure.StructureTable.Region;
import com.creatrix.salessolution.Model.MarketStructure.StructureTable.SubTeritorry;
import com.creatrix.salessolution.Model.MarketStructure.StructureTable.Teritorry;
import com.creatrix.salessolution.Model.ModelProviderType;
import com.creatrix.salessolution.Model.ModelSMCType;
import com.creatrix.salessolution.Model.PrescriptionTYpe;
import com.creatrix.salessolution.Model.Product;
import com.creatrix.salessolution.Model.ProductSample;
import com.creatrix.salessolution.Model.StationType;
import com.creatrix.salessolution.Model.TourPurposeViewModel;
import com.creatrix.salessolution.Model.TourTypeViewModel;
import com.creatrix.salessolution.Model.Transport;
import com.creatrix.salessolution.Model.UserByRole;
import com.creatrix.salessolution.Model.UserRole;

import java.util.List;

public class SyncDb_Helper {
    DBHelperMain dbHelperMain;
    Context context;

    public SyncDb_Helper(Context context) {
        this.context = context;
        dbHelperMain = new DBHelperMain(context);
    }

/*    //Area section Insert
    public boolean InsertDivision(List<DivisionVM> aList) {
        boolean isTrue = true;
        try {
            String tableName = "tblDivisionInfo";
            _deleteAllRecordsFromaTable(tableName);
            SQLiteDatabase database = dbHelperMain.getWritableDatabase();
            for (int i = 0; i < aList.size(); i++) {
                DivisionVM aInfo = aList.get(i);
                String insertQuery = "Insert into tblDivisionInfo(DivisionId,DivisionName) " +
                        "values('" + aInfo.getDivisionId() + "','" + aInfo.getDivisionName() + "')";
                database.execSQL(insertQuery);
            }
        } catch (Exception exception) {
            isTrue = false;

            Log.e("DBEX", exception.toString());
        }
        return isTrue;
    }

    public boolean InsertDistrict(List<DistrictVM> aList) {
        boolean isTrue = true;
        try {
            String tableName = "tblDistrictInfo";
            _deleteAllRecordsFromaTable(tableName);
            SQLiteDatabase database = dbHelperMain.getWritableDatabase();
            for (int i = 0; i < aList.size(); i++) {
                DistrictVM aInfo = aList.get(i);
                // String insertQuery = "Insert into tblDistrictInfo(DistrictId,DistrictName,DivisionId) " +
                String insertQuery = "Insert into tblDistrictInfo(DistrictId,DistrictName,DivisionId) " +
                        //"values('" + aInfo.getDistrictId() + "','" + aInfo.getDistrictName() + "','" + aInfo.getDivisionId() + "')";
                        "values('" + aInfo.getDistrictId() + "','" + aInfo.getDistrictName() + "','" + aInfo.getDivisionId() + "')";
                database.execSQL(insertQuery);
                database.close();
            }
        } catch (Exception exception) {
            isTrue = false;
            Log.e("DBEX", exception.toString());
        }
        return isTrue;
    }

    public boolean InsertThana(List<ThanaVM> aList) {
        boolean isTrue = true;
        try {
            String tableName = "tblThanaInfo";
            _deleteAllRecordsFromaTable(tableName);
            SQLiteDatabase database = dbHelperMain.getWritableDatabase();
            for (int i = 0; i < aList.size(); i++) {
                ThanaVM aInfo = aList.get(i);
                String insertQuery = "Insert into tblThanaInfo(ThanaId,ThanaName,district_id) " +
                        "values('" + aInfo.getThanaId() + "','" + aInfo.getThanaName() + "','" + aInfo.getDistrict_id() + "')";
                database.execSQL(insertQuery);
            }
        } catch (Exception exception) {
            isTrue = false;

            Log.e("DBEX", exception.toString());
        }
        return isTrue;
    }*/

    //TODO:Area
    /*public boolean InsertArea(List<Area> aList){
        boolean isTrue = true;
        try{

            String tableName = "tblProviderInfo";
            DeleteRecordsOfTable(tableName);
            SQLiteDatabase database = dbHelperMain.getWritableDatabase();
            for (int i=0;i<aList.size();i++){
                DoctorListViewModel aInfo = aList.get(i);
                String insertQuery = "Insert into tblProviderInfo(ProviderinfoID,ThanaId,Union_Pourashava,ProviderCode,Village,NameofProvider,MobileNo,NameofOutlet," +
                        "Address,ThanaName,DistrictName,DivisionName,ProviderType) " +
                        "values('"+aInfo.getProviderinfoID()+"','"+aInfo.getThanaId()+"','"+aInfo.getUnion_Pourashava()+"'," +
                        "'"+ aInfo.getProviderCode()+"','"+ aInfo.getVillage()+"','"+ aInfo.getNameofProvider()+"','"+ aInfo.getMobileNo()+"','"+ aInfo.getNameofOutlet()+"'," +
                        "'"+ aInfo.getAddress()+"','"+aInfo.getThanaName()+"','"+aInfo.getDistrictName()+"','"+aInfo.getDivisionName()+"','"+aInfo.getProviderType()+"')";
                database.execSQL(insertQuery);
            }

        }catch (Exception exception){
            isTrue = false;
            Log.e("DBEX",exception.toString());

        }
        return isTrue;
    }*/
    //TODO:Insert Master DATA
    public boolean InsertGroup(List<Group> gList) {
        boolean isTrue = false;
        String tableName = "tbl_Group";
        SQLiteDatabase database = dbHelperMain.getWritableDatabase();
        database.beginTransaction();
        try {
            String query = "delete from '" + tableName + "';";
            database.execSQL(query);
        } catch (Exception ex) {
            Log.e("recordDelError", ex.toString());
        }
        for (Group m : gList) {
            ContentValues cv = new ContentValues();
            cv.put("GroupId", m.getGroupId());
            cv.put("GroupName", m.getGroupName().replace("'", "''"));
            database.insert(tableName, null, cv);
            isTrue =true;
        }
        isTrue = true;
        database.setTransactionSuccessful();
        database.endTransaction();
        database.close();
      /*  try {

            String tableName = "tbl_Group";
            _deleteAllRecordsFromaTable(tableName);
            SQLiteDatabase database = dbHelperMain.getWritableDatabase();
            for (int i = 0; i < gList.size(); i++) {
                Group gInfo = gList.get(i);
                String insertQuery = "Insert into tbl_Group(GroupId,GroupName) " +
                        //"values('" + gInfo.getGroupId() + "','" + gInfo.getGroupName() + "')";
                        "values('" + gInfo.getGroupId() + "','" + gInfo.getGroupName().replace("'", "''") + "')";

                database.execSQL(insertQuery);
            }

        } catch (Exception exception) {
            //  isTrue = false;
            Log.e("DBEX", exception.toString());

        }*/
        return isTrue;
    }

    public boolean InsertRegion(List<Region> rList) {
        boolean isTrue = false;
        String tableName = "tblRegion";
        SQLiteDatabase database = dbHelperMain.getWritableDatabase();
        database.beginTransaction();
        try {
            String query = "delete from '" + tableName + "';";
            database.execSQL(query);
        } catch (Exception ex) {
            Log.e("recordDelError", ex.toString());
        }
        for (Region m : rList) {
            ContentValues cv = new ContentValues();
            cv.put("RegionId", m.getRegionId());
            cv.put("RegionName", m.getRegionName().replace("'", "''"));
            cv.put("GroupId", m.getGroupId());
            database.insert(tableName, null, cv);
        }
        isTrue = true;
        database.setTransactionSuccessful();
        database.endTransaction();
        database.close();

        return isTrue;
    }

    public boolean InsertArea(List<Area> arList) {
        boolean isTrue = false;
        String tableName = "tblArea";
        SQLiteDatabase database = dbHelperMain.getWritableDatabase();
        database.beginTransaction();
        try {
            String query = "delete from '" + tableName + "';";
            database.execSQL(query);
        } catch (Exception ex) {
            Log.e("recordDelError", ex.toString());
        }
        for (Area m : arList) {
            ContentValues cv = new ContentValues();
            cv.put("AreaId", m.getAreaId());
            cv.put("AreaName", m.getAreaName().replace("'", "''"));
            cv.put("RegionId", m.getRegionId());
            database.insert(tableName, null, cv);
        }
        isTrue = true;
        database.setTransactionSuccessful();
        database.endTransaction();
        database.close();
       /* try {

            String tableName = "tblArea";
            _deleteAllRecordsFromaTable(tableName);
            SQLiteDatabase database = dbHelperMain.getWritableDatabase();
            for (int i = 0; i < arList.size(); i++) {
                Area aInfo = arList.get(i);
                String insertQuery = "Insert into tblArea(AreaId,AreaName,RegionId) " +
                        // "values('" + aInfo.getAreaId() + "','" + aInfo.getAreaName() + "','" + aInfo.getRegionId() + "')";
                        "values('" + aInfo.getAreaId() + "','" + aInfo.getAreaName().replace("'", "''") + "','" + aInfo.getRegionId() + "')";
                database.execSQL(insertQuery);
            }

        } catch (Exception exception) {
            // isTrue = false;
            Log.e("DBEX", exception.toString());

        }*/
        return isTrue;
    }

    public boolean InsertTeritorry(List<Teritorry> tList) {
        boolean isTrue = false;
        String tableName = "tblTerritory";
        SQLiteDatabase database = dbHelperMain.getWritableDatabase();
        database.beginTransaction();
        try {
            String query = "delete from '" + tableName + "';";
            database.execSQL(query);
        } catch (Exception ex) {
            Log.e("recordDelError", ex.toString());
        }
        for (Teritorry m : tList) {
            ContentValues cv = new ContentValues();
            cv.put("TerritoryId", m.getTerritoryId());
            cv.put("TerritoryName", m.getTerritoryName().replace("'", "''"));
            cv.put("AreaId", m.getAreaId());
            database.insert(tableName, null, cv);
        }
        isTrue= true;
        database.setTransactionSuccessful();
        database.endTransaction();
        database.close();

        /*try {

            String tableName = "tblTerritory";
            _deleteAllRecordsFromaTable(tableName);
            SQLiteDatabase database = dbHelperMain.getWritableDatabase();
            for (int i = 0; i < tList.size(); i++) {
                Teritorry tInfo = tList.get(i);
                String insertQuery = "Insert into tblTerritory(TerritoryId,TerritoryName,AreaId) " +
                        //  "values('" + tInfo.getTerritoryId() + "','" + tInfo.getTerritoryName() + "','" + tInfo.getAreaId() + "')";
                        "values('" + tInfo.getTerritoryId() + "','" + tInfo.getTerritoryName().replace("'", "''") + "','" + tInfo.getAreaId() + "')";
                database.execSQL(insertQuery);
            }

        } catch (Exception exception) {
            // isTrue = false;
            Log.e("DBEX", exception.toString());

        }*/
        return isTrue;
    }

    public boolean InsertSubTeritorry(List<SubTeritorry> stList) {
        boolean isTrue = false;
        String tableName = "tblSubTerritory";
        SQLiteDatabase database = dbHelperMain.getWritableDatabase();
        database.beginTransaction();
        try {
            String query = "delete from '" + tableName + "';";
            database.execSQL(query);
        } catch (Exception ex) {
            Log.e("recordDelError", ex.toString());
        }
        for (SubTeritorry m : stList) {
            ContentValues cv = new ContentValues();
            cv.put("SubTerritoryId", m.getSubTerritoryId());
            cv.put("SubTerritoryName", m.getSubTerritoryName().replace("'", "''"));
            cv.put("TerritoryId", m.getTerritoryId());
            database.insert(tableName, null, cv);
        }
        isTrue = true;
        database.setTransactionSuccessful();
        database.endTransaction();
        database.close();
    /*    try {
            String tableName = "tblSubTerritory";
            _deleteAllRecordsFromaTable(tableName);
            SQLiteDatabase database = dbHelperMain.getWritableDatabase();
            for (int i = 0; i < stList.size(); i++) {
                SubTeritorry stInfo = stList.get(i);
                String insertQuery = "Insert into tblSubTerritory(SubTerritoryId,SubTerritoryName,TerritoryId) " +
                        // "values('" + stInfo.getSubTerritoryId() + "','" + stInfo.getSubTerritoryName() + "','" + stInfo.getTerritoryId() + "')";
                        "values('" + stInfo.getSubTerritoryId() + "','" + stInfo.getSubTerritoryName().replace("'", "''") + "','" + stInfo.getTerritoryId() + "')";
                database.execSQL(insertQuery);
            }

        } catch (Exception exception) {
            // isTrue = false;
            Log.e("DBEX", exception.toString());

        }*/
        return isTrue;
    }

    public boolean InsertMarket(List<Market> mList) {
        boolean isTrue = false;
        String tableName = "tblMarket";
        SQLiteDatabase database = dbHelperMain.getWritableDatabase();
        database.beginTransaction();
        try {
            String query = "delete from '" + tableName + "';";
            database.execSQL(query);
        } catch (Exception ex) {
            Log.e("recordDelError", ex.toString());
        }
        for (Market m : mList) {
            ContentValues cv = new ContentValues();
            cv.put("MarketId", m.getMarketId());
            cv.put("MarketName", m.getMarketName().replace("'", "''"));
            cv.put("SubTerritoryId", m.getSubTerritoryId());
            database.insert(tableName, null, cv);
        }
        isTrue = true;
        database.setTransactionSuccessful();
        database.endTransaction();
        database.close();
        /*try {

            String tableName = "tblMarket";
            _deleteAllRecordsFromaTable(tableName);
            SQLiteDatabase database = dbHelperMain.getWritableDatabase();
            for (int i = 0; i < mList.size(); i++) {
                Market mInfo = mList.get(i);
                String insertQuery = "Insert into tblMarket(MarketId,MarketName,SubTerritoryId) " +
                        // "values('" + mInfo.getMarketId() + "','" + mInfo.getMarketName() + "','" + mInfo.getSubTerritoryId() + "')";
                        "values('" + mInfo.getMarketId() + "','" + mInfo.getMarketName().replace("'", "''") + "','" + mInfo.getSubTerritoryId() + "')";
                database.execSQL(insertQuery);
            }

        } catch (Exception exception) {
            // isTrue = false;
            Log.e("DBEX", exception.toString());

        }*/
        return isTrue;
    }

    //TODO:Insert MasterEmp
    public boolean InsertNSMInfo(List<NSM> nList) {
        boolean isTrue = true;
        try {

            String tableName = "tblNSMInfo";
            _deleteAllRecordsFromaTable(tableName);
            SQLiteDatabase database = dbHelperMain.getWritableDatabase();
            for (int i = 0; i < nList.size(); i++) {
                NSM nInfo = nList.get(i);
                String insertQuery = "Insert into tblNSMInfo(NSMEmpId,NSMId,EmpMasterCode,EmpName,GroupId) " +
                        "values('" + nInfo.getNSMEmpId() + "','" + nInfo.getNSMId() + "','" + nInfo.getEmpMasterCode() + "','" + nInfo.getEmpName() + "','" + nInfo.getGroupId() + "')";
                database.execSQL(insertQuery);
            }

        } catch (Exception exception) {
            // isTrue = false;
            Log.e("DBEX", exception.toString());

        }
        return isTrue;
    }
    public boolean InsertRSMInfo(List<RSM> rList) {
        boolean isTrue = true;
        try {

            String tableName = "tblRSMInfo";
            _deleteAllRecordsFromaTable(tableName);
            SQLiteDatabase database = dbHelperMain.getWritableDatabase();
            for (int i = 0; i < rList.size(); i++) {
                RSM nInfo = rList.get(i);
                String insertQuery = "Insert into tblRSMInfo(RSMEmpId,RSMId,EmpMasterCode,EmpName,RegionId) " +
                        "values('" + nInfo.getRSMEmpId() + "','" + nInfo.getRSMId() + "','" + nInfo.getEmpMasterCode() + "','" + nInfo.getEmpName().replace("'", "''") + "','" + nInfo.getRegionId() + "')";
                database.execSQL(insertQuery);
            }

        } catch (Exception exception) {
            // isTrue = false;
            Log.e("DBEX", exception.toString());

        }
        return isTrue;
    }
    public boolean InsertASMInfo(List<ASM> aList) {
        boolean isTrue = true;
        try {

            String tableName = "tblASMInfo";
            _deleteAllRecordsFromaTable(tableName);
            SQLiteDatabase database = dbHelperMain.getWritableDatabase();
            for (int i = 0; i < aList.size(); i++) {
                ASM nInfo = aList.get(i);
                String insertQuery = "Insert into tblASMInfo(ASMEmpId,ASMId,EmpMasterCode,EmpName,AreaId) " +
                        "values('" + nInfo.getASMEmpId() + "','" + nInfo.getASMId() + "','" + nInfo.getEmpMasterCode() + "','" + nInfo.getEmpName() + "','" + nInfo.getAreaId() + "')";
                database.execSQL(insertQuery);
            }

        } catch (Exception exception) {
            // isTrue = false;
            Log.e("DBEX", exception.toString());

        }
        return isTrue;
    }
    public boolean InsertMIOInfo(List<MIO> mList) {
        boolean isTrue = true;
        try {
            String tableName = "tblMIOInfo";
            _deleteAllRecordsFromaTable(tableName);
            SQLiteDatabase database = dbHelperMain.getWritableDatabase();
            for (int i = 0; i < mList.size(); i++) {
                MIO nInfo = mList.get(i);
                String insertQuery = "Insert into tblMIOInfo(MIOEmpId,MIOId,EmpMasterCode,EmpName,TerritoryId) " +
                        "values('" + nInfo.getMIOEmpId() + "','" + nInfo.getMIOId() + "','" + nInfo.getEmpMasterCode() + "','" + nInfo.getEmpName() + "','" + nInfo.getTerritoryId() + "')";
                database.execSQL(insertQuery);
            }

        } catch (Exception exception) {
            // isTrue = false;
            Log.e("DBEX", exception.toString());

        }
        return isTrue;
    }

/*    public boolean InsertTeritory(List<TerritoryViewModel> aList) {
        boolean isTrue = true;
        try {

            String tableName = "tblTeritory";
            _deleteAllRecordsFromaTable(tableName);
            SQLiteDatabase database = dbHelperMain.getWritableDatabase();
            String TerritoryName = "";
            for (int i = 0; i < aList.size(); i++) {
                TerritoryViewModel aInfo = aList.get(i);
                try {
                    if (aInfo.getTerritoryName().equals("")) {
                        TerritoryName = "";
                    } else {
                        TerritoryName = aInfo.getTerritoryName().replace("'", "''");
                    }
                } catch (Exception exception) {
                    //exception.printStackTrace();
                }
                String insertQuery = "Insert into tblTeritory(TerritoryId,TerritoryName,TerritoryCode) " +
                        "values('" + aInfo.getTerritoryId() + "','" + TerritoryName*//*aInfo.getTerritoryName().replace("'","''")*//* + "','" + aInfo.getTerritoryCode() + "')";
                database.execSQL(insertQuery);
            }

        } catch (Exception exception) {
            // isTrue = false;
            Log.e("DBEX", exception.toString());

        }
        return isTrue;
    }*/
    //TODO:Market
    /*public boolean InsertMarket(List<DoctorListViewModel> aList){
        boolean isTrue = true;
        try{

            String tableName = "tblProviderInfo";
            _deleteAllRecordsFromaTable(tableName);
            SQLiteDatabase database = dbHelperMain.getWritableDatabase();
            for (int i=0;i<aList.size();i++){
                DoctorListViewModel aInfo = aList.get(i);
                String insertQuery = "Insert into tblProviderInfo(ProviderinfoID,ThanaId,Union_Pourashava,ProviderCode,Village,NameofProvider,MobileNo,NameofOutlet," +
                        "Address,ThanaName,DistrictName,DivisionName,ProviderType) " +
                        "values('"+aInfo.getProviderinfoID()+"','"+aInfo.getThanaId()+"','"+aInfo.getUnion_Pourashava()+"'," +
                        "'"+ aInfo.getProviderCode()+"','"+ aInfo.getVillage()+"','"+ aInfo.getNameofProvider()+"','"+ aInfo.getMobileNo()+"','"+ aInfo.getNameofOutlet()+"'," +
                        "'"+ aInfo.getAddress()+"','"+aInfo.getThanaName()+"','"+aInfo.getDistrictName()+"','"+aInfo.getDivisionName()+"','"+aInfo.getProviderType()+"')";
                database.execSQL(insertQuery);
            }

        }catch (Exception exception){
            isTrue = false;
            Log.e("DBEX",exception.toString());

        }
        return isTrue;
    }*/
    //Customer section Insert
    public boolean InsertCustomerList(List<Customer> aList) {
        boolean isTrue = true;
      /*  try {
            String tableName = "tblCustomerInfo";
            _deleteAllRecordsFromaTable(tableName);

            SQLiteDatabase database = dbHelperMain.getWritableDatabase();
            for (int i = 0; i < aList.size(); i++) {
                Customer aInfo = aList.get(i);
                String insertQuery = "Insert into tblCustomerInfo(CustomerMasterId,CustomerName,CustomerCode,CustomerAdress,CustomerType," +
                        "CustomerSubgroup,CustomerCell,CustomerBalance,CustomerCreditlimit,Market) " +
                        "values('" + aInfo.getCustomerMasterId() + "','" + aInfo.getCustomerName() + "','" + aInfo.getCustomerCode() + "','" + aInfo.getAddress() + "'," +
                        "'" + aInfo.getCustomerType() + "','" + aInfo.getCustomerStation() + "','" + aInfo.getCellNo() + "','" + aInfo.getBalance() + "','" + aInfo.getCreditLimit() + "','" + aInfo.getMarketName() + "')";

                database.execSQL(insertQuery);
            }
        } catch (Exception exception) {
            Log.e("DBEX", exception.toString());
        }*/

        try {
            String tableName = "tblCustomerInfo";
            _deleteAllRecordsFromaTable(tableName);

            SQLiteDatabase database = dbHelperMain.getWritableDatabase();
            String CustomerName = "";
            String CustomerAddress = "";
            String CustomerType = "";
            String MarketName = "";
            for (int i = 0; i < aList.size(); i++) {
                Customer aInfo = aList.get(i);
                try {
                    if (aInfo.getCustomerName().equals("")) {
                        CustomerName = "";
                    } else {
                        CustomerName = aInfo.getCustomerName().replace("'", "''");
                    }
                } catch (Exception exception) {
                    //exception.printStackTrace();
                }
                try {
                    if (aInfo.getAddress().equals("")) {
                        CustomerAddress = "";
                    } else {
                        CustomerAddress = aInfo.getAddress().replace("'", "''");
                    }
                } catch (Exception exception) {
                    //exception.printStackTrace();
                }
                try {
                    if (aInfo.getCustomerType().equals("")) {
                        CustomerType = "";
                    } else {
                        CustomerType = aInfo.getCustomerType().replace("'", "''");
                    }
                } catch (Exception exception) {
                    //exception.printStackTrace();
                }
                try {
                    if (aInfo.getMarketName().equals("")) {
                        MarketName = "";
                    } else {
                        MarketName = aInfo.getMarketName().replace("'", "''");
                    }
                } catch (Exception exception) {
                    //exception.printStackTrace();
                }

                String insertQuery = "Insert into tblCustomerInfo(CustomerMasterId,CustomerName,CustomerCode,CustomerAdress,CustomerType," +
                        "CustomerCell,CustomerBalance,CustomerCreditlimit,Market,MarketCode,GroupId,RegionId,AreaId,TerritoryId,SubTerritoryId,MarketId,Note,CustomerCheck,CustomerTypeId,ProgramTypeId,SMCTypeId) " +
                        "values('" + aInfo.getCustomerMasterId() + "','" + CustomerName + "','" + aInfo.getCustomerCode() + "','" + CustomerAddress + "'," +
                        "'" + CustomerType + "','" + aInfo.getCellNo() + "','" + aInfo.getBalance() + "','" + aInfo.getCreditLimit() + "','" + MarketName + "','" + aInfo.getMarketCode() + "','" + aInfo.getGroupId() + "','" + aInfo.getRegionId() + "','" + aInfo.getAreaId() + "','" + aInfo.getTerritoryId() + "','" + aInfo.getSubTerritoryId() + "','" + aInfo.getMarketId() + "','" + aInfo.getNote() + "'," +
                        "'" + aInfo.getCustomerCheck() + "','" + aInfo.getCustomerTypeId() + "','" + aInfo.getProgramTypeId() + "','" + aInfo.getSMCTypeId() + "')";
                database.execSQL(insertQuery);

            }
            database.close();
        } catch (Exception exception) {
            Log.e("DBEX", exception.toString());
        }
        return isTrue = true;
    }

    public boolean InsertCustomerStation(List<StationType> aList) {
        boolean isTrue = true;
        try {

            String tableName = "tblCustomer_Station";
            _deleteAllRecordsFromaTable(tableName);
            SQLiteDatabase database = dbHelperMain.getWritableDatabase();
            for (int i = 0; i < aList.size(); i++) {
                StationType aInfo = aList.get(i);
                String insertQuery = "Insert into tblCustomer_Station(StationTypeId,StationTypeName) " +
                        "values('" + aInfo.getStationTypeId() + "','" + aInfo.getStationTypeName().replace("'", "''") + "')";
                database.execSQL(insertQuery);
            }

        } catch (Exception exception) {
            isTrue = false;
            Log.e("DBEX", exception.toString());

        }
        return isTrue;
    }

    //TODO:Doctor section Insert
    public boolean InsertDoctorList(List<DoctorListViewModel> aList) {
        boolean isTrue = true;
        try {

            _deleteAllRecordsFromaTable("tblDoctorBrand");
            _deleteAllRecordsFromaTable("tblDoctorInfo");

            SQLiteDatabase database = dbHelperMain.getWritableDatabase();
            String ChemberName = "";
            String MarketName = "";

           /* database.beginTransaction();
            for (DoctorListViewModel e : aList) {
                if(e.getChemberName().equals(""))
                {
                    ChemberName="";
                }else {
                    ChemberName=e.getChemberName().replace("'","''");
                }
                database.insert(e);
            }
            database.setTransactionSuccessful();
            database.endTransaction();*/

            for (int i = 0; i < aList.size(); i++) {
                DoctorListViewModel aInfo = aList.get(i);
                try {
                    if (aInfo.getChemberName().equals("")) {
                        ChemberName = "";
                    } else {
                        ChemberName = aInfo.getChemberName().replace("'", "''");
                    }

                    if (aInfo.getMarketName().equals("")) {
                        MarketName = "";
                    } else {
                        MarketName = aInfo.getMarketName().replace("'", "''");
                    }

                } catch (Exception exception) {

                    //exception.printStackTrace();
                }
                String insertQuery = "Insert into tblDoctorInfo(DoctorId,DoctorCode,DoctorName,ChemberName,DocContact,DocTPDetailsId,DoctorTypeName,ProgramTypeName,GroupId,RegionId,AreaId,TerritoryId,SubTerritoryId,MarketId,MarketName,MarketCode,ProgramTypeId,DoctorTypeId,SMCTypeId,SMCType)" +
                        "values('" + aInfo.getDoctorId() + "','" + aInfo.getDoctorCode() + "','" + aInfo.getDoctorName().replace("'", "''") + "'," +
                        "'" + ChemberName /*aInfo.getChemberName() */ + "','" + aInfo.getDocContact() + "','" + aInfo.getDocTPDetailsId() + "','" + aInfo.getDoctorTypeName() + "','" + aInfo.getProgramTypeName() + "','" + aInfo.getGroupId() + "','" + aInfo.getRegionId() + "','" + aInfo.getAreaId() + "','" + aInfo.getTerritoryId() + "','" + aInfo.getSubTerritoryId()
                        + "','" + aInfo.getMarketId() + "','" + MarketName + "','" + aInfo.getMarketCode() + "','" + aInfo.getProgramTypeId() + "','" + aInfo.getDoctorTypeId() + "','" + aInfo.getSMCTypeId() + "','" + aInfo.getSMCType() + "')";
                // database.beginTransaction();
                database.execSQL(insertQuery);
                String brandname = "";
                for (int j = 0; j < aList.get(i).getDoctorBrand().size(); j++) {
                    DoctorBrand dbi = aList.get(i).getDoctorBrand().get(j);
                    try {
                        if (dbi.getBrandName().equals("")) {
                            brandname = dbi.getBrandName();
                        } else {
                            brandname = dbi.getBrandName().replace("'", "''");
                        }
                    } catch (Exception exception) {

                    }
                    String insertBrand = "Insert into tblDoctorBrand(BrandId,BrandName,DoctorId) " +
                            "values('" + dbi.getBrandId() + "','" + brandname/*dbi.getBrandName().replace("'","''")*/ + "','" + dbi.getDoctorId() + "')";
                    database.execSQL(insertBrand);

                }
           /*      database.setTransactionSuccessful();
                database.endTransaction();
                database.close();*/

            }

        } catch (Exception exception) {
            isTrue = false;
            Log.e("DBEX", exception.toString());

        }
        return isTrue;
    }

    public boolean InsertDoctorDesig(List<DoctorDesignation> aList) {
        boolean isTrue = true;
        try {

            String tableName = "tblDoctor_Desig";
            _deleteAllRecordsFromaTable(tableName);
            SQLiteDatabase database = dbHelperMain.getWritableDatabase();
            for (int i = 0; i < aList.size(); i++) {
                DoctorDesignation aInfo = aList.get(i);
                String insertQuery = "Insert into tblDoctor_Desig(DesignationId,DesignationName) " +
                        "values('" + aInfo.getDesignationId() + "','" + aInfo.getDesignationName().replace("'", "''") + "')";
                database.execSQL(insertQuery);
            }

        } catch (Exception exception) {
            isTrue = false;
            Log.e("DBEX", exception.toString());

        }
        return isTrue;
    }

    public boolean InsertDoctorDegree(List<DoctorDegreeViewModel> aList) {
        boolean isTrue = true;
        try {

            String tableName = "tblDoctor_Degree";
            _deleteAllRecordsFromaTable(tableName);
            SQLiteDatabase database = dbHelperMain.getWritableDatabase();
            for (int i = 0; i < aList.size(); i++) {
                DoctorDegreeViewModel aInfo = aList.get(i);
                String insertQuery = "Insert into tblDoctor_Degree(DegreeId,DegreeName,DoctorTypeId) " +
                        "values('" + aInfo.getDegreeId() + "','" + aInfo.getDegreeName().replace("'", "''") + "','" + aInfo.getDoctorTypeId() + "')";
                database.execSQL(insertQuery);
            }

        } catch (Exception exception) {
            isTrue = false;
            Log.e("DBEX", exception.toString());

        }
        return isTrue;
    }

    public boolean InsertDoctorSpecility(List<DoctorSpecialityViewModel> aList) {
        boolean isTrue = true;
        try {

            String tableName = "tblDoctor_Speciality";
            _deleteAllRecordsFromaTable(tableName);
            SQLiteDatabase database = dbHelperMain.getWritableDatabase();
            for (int i = 0; i < aList.size(); i++) {
                DoctorSpecialityViewModel aInfo = aList.get(i);
                String insertQuery = "Insert into tblDoctor_Speciality(SpecialityId,SpecialityName) " +
                        "values('" + aInfo.getSpecialityId() + "','" + aInfo.getSpecialityName().replace("'", "''") + "')";
                database.execSQL(insertQuery);
            }

        } catch (Exception exception) {
            isTrue = false;
            Log.e("DBEX", exception.toString());

        }
        return isTrue;
    }

    public boolean InsertDoctorSpecialDay(List<SpecialDay> aList) {
        boolean isTrue = true;
        try {

            String tableName = "tblDoctor_Specialday";
            _deleteAllRecordsFromaTable(tableName);
            SQLiteDatabase database = dbHelperMain.getWritableDatabase();
            for (int i = 0; i < aList.size(); i++) {
                SpecialDay aInfo = aList.get(i);
                String insertQuery = "Insert into tblDoctor_Specialday(SpecialDayId,SpecialDay) " +
                        "values('" + aInfo.getSpecialDayId() + "','" + aInfo.getSpecialDay().replace("'", "''") + "')";
                database.execSQL(insertQuery);
            }

        } catch (Exception exception) {
            isTrue = false;
            Log.e("DBEX", exception.toString());

        }
        return isTrue;
    }

    public boolean InsertDoctorInstitution(List<InstitutionVM> aList) {
        boolean isTrue = true;
        try {

            String tableName = "tblDoctor_Institution";
            _deleteAllRecordsFromaTable(tableName);
            SQLiteDatabase database = dbHelperMain.getWritableDatabase();
            for (int i = 0; i < aList.size(); i++) {
                InstitutionVM aInfo = aList.get(i);
                String insertQuery = "Insert into tblDoctor_Institution(InstitutionId,Institution) " +
                        "values('" + aInfo.getInstitutionId() + "','" + aInfo.getInstitution().replace("'", "''") + "')";
                database.execSQL(insertQuery);
            }

        } catch (Exception exception) {
            isTrue = false;
            Log.e("DBEX", exception.toString());

        }
        return isTrue;
    }

    public boolean InsertDoctorChembar(List<DoctorChamberTypeVM> aList) {
        boolean isTrue = true;
        try {

            String tableName = "tblDoctor_Chembar";
            _deleteAllRecordsFromaTable(tableName);
            SQLiteDatabase database = dbHelperMain.getWritableDatabase();
            for (int i = 0; i < aList.size(); i++) {
                DoctorChamberTypeVM aInfo = aList.get(i);
                String insertQuery = "Insert into tblDoctor_Chembar(ChamberTypeId,ChamberTypeName) " +
                        "values('" + aInfo.getChamberTypeId() + "','" + aInfo.getChamberTypeName().replace("'", "''") + "')";
                database.execSQL(insertQuery);
            }
            database.close();

        } catch (Exception exception) {
            isTrue = false;
            Log.e("DBEX", exception.toString());

        }
        return isTrue;
    }

    public boolean InsertDoctorChembarName(List<DoctorChamberName> aList) {
        boolean isTrue = true;
        try {

            String tableName = "tblDoctor_ChembarName";
            _deleteAllRecordsFromaTable(tableName);
            SQLiteDatabase database = dbHelperMain.getWritableDatabase();
            for (int i = 0; i < aList.size(); i++) {
                DoctorChamberName aInfo = aList.get(i);
                String insertQuery = "Insert into tblDoctor_ChembarName(ChemberId,ChemberName,DoctorId) " +
                        "values('" + aInfo.getChemberId() + "','" + aInfo.getChemberName().replace("'", "''") + "','" + aInfo.getDoctorId() + "')";
                database.execSQL(insertQuery);
            }
            database.close();
        } catch (Exception exception) {
            isTrue = false;
            Log.e("DBEX", exception.toString());

        }
        return isTrue;
    }

    //TODO:Type section Insert
    public boolean InsertDoctorType(List<DoctorTypeVM> aList) {
        boolean isTrue = true;
        try {

            String tableName = "tblDoctor_Type";
            _deleteAllRecordsFromaTable(tableName);
            SQLiteDatabase database = dbHelperMain.getWritableDatabase();
            for (int i = 0; i < aList.size(); i++) {
                DoctorTypeVM aInfo = aList.get(i);
                String insertQuery = "Insert into tblDoctor_Type(DoctorTypeId,DoctorTypeName) " +
                        "values('" + aInfo.getDoctorTypeId() + "','" + aInfo.getDoctorTypeName().replace("'", "''") + "')";
                database.execSQL(insertQuery);
            }

        } catch (Exception exception) {
            isTrue = false;
            Log.e("DBEX", exception.toString());

        }
        return isTrue;
    }

    public boolean InsertRole(List<UserRole> aList) {
        boolean isTrue = true;
        try {

            String tableName = "tbl_UserRole";
            _deleteAllRecordsFromaTable(tableName);
            SQLiteDatabase database = dbHelperMain.getWritableDatabase();
            for (int i = 0; i < aList.size(); i++) {
                UserRole aInfo = aList.get(i);
                String insertQuery = "Insert into tbl_UserRole(UserRoleID,RoleName) " +
                        "values('" + aInfo.getUserRoleID() + "','" + aInfo.getRoleName().replace("'", "''") + "')";
                database.execSQL(insertQuery);
            }

        } catch (Exception exception) {
            isTrue = false;
            Log.e("DBEX", exception.toString());

        }
        return isTrue;
    }
    public boolean InsertRoleUser(List<UserByRole> aList) {
        boolean isTrue = true;
        try {


            String tableName = "tbl_UserByRole";
            _deleteAllRecordsFromaTable(tableName);
            SQLiteDatabase database = dbHelperMain.getWritableDatabase();
            for (int i = 0; i < aList.size(); i++) {
                UserByRole aInfo = aList.get(i);
                String insertQuery = "Insert into tbl_UserByRole(EmpInfoId,EmpName,EmpMasterCode,UserRoleID) " +
                        "values('" + aInfo.getEmpInfoId() + "','" + aInfo.getEmpName() + "','" + aInfo.getEmpMasterCode() + "','" + aInfo.getUserRoleID() + "')";
                database.execSQL(insertQuery);
            }
            database.close();
        } catch (Exception exception) {
            isTrue = false;
            Log.e("DBEX", exception.toString());

        }
        return isTrue;
    }

    public boolean InsertProgramType(List<ProgramType> aList) {
        boolean isTrue = true;
        try {

            String tableName = "tblProgram_Type";
            _deleteAllRecordsFromaTable(tableName);
            SQLiteDatabase database = dbHelperMain.getWritableDatabase();
            for (int i = 0; i < aList.size(); i++) {
                ProgramType aInfo = aList.get(i);
                String insertQuery = "Insert into tblProgram_Type(ProgramTypeId,ProgramType) " +
                        "values('" + aInfo.getProgramTypeId() + "','" + aInfo.getProgramType().replace("'", "''") + "')";
                database.execSQL(insertQuery);
            }

        } catch (Exception exception) {
            isTrue = false;
            Log.e("DBEX", exception.toString());

        }
        return isTrue;
    }

    public boolean InsertProviderType(List<ModelProviderType> aList) {
        boolean isTrue = false;
        try {

            String tableName = "tblProvider_Type";
            _deleteAllRecordsFromaTable(tableName);
            SQLiteDatabase database = dbHelperMain.getWritableDatabase();
            for (int i = 0; i < aList.size(); i++) {
                ModelProviderType aInfo = aList.get(i);
                String insertQuery = "Insert into tblProvider_Type(ProviderTypeId,ProviderType,forCustomer,forDoctor) " +
                        "values('" + aInfo.getProviderTypeId() + "','" + aInfo.getProviderType().replace("'", "''") + "','" + aInfo.getForCustomer() + "','" + aInfo.getForDoctor() + "')";
                database.execSQL(insertQuery);
                isTrue = true;
            }

        } catch (Exception exception) {
            isTrue = false;
            Log.e("DBEX", exception.toString());

        }
        return isTrue;
    }

    public boolean InsertSMCType(List<ModelSMCType> aList) {
        boolean isTrue = false;
        try {
            String tableName = "tblSMCType";
            _deleteAllRecordsFromaTable(tableName);
            SQLiteDatabase database = dbHelperMain.getWritableDatabase();
            for (int i = 0; i < aList.size(); i++) {
                ModelSMCType aInfo = aList.get(i);
                String insertQuery = "Insert into tblSMCType(SMCTypeId,SMCType,forCustomer,forDoctor) " +
                        "values('" + aInfo.getSMCTypeId() + "','" + aInfo.getSMCType().replace("'", "''") + "','" + aInfo.getForCustomer() + "','" + aInfo.getForDoctor() + "')";
                database.execSQL(insertQuery);
                isTrue = true;
            }

        } catch (Exception exception) {
            isTrue = false;
            Log.e("DBEX", exception.toString());

        }
        return isTrue;
    }
    public boolean InsertBrand(List<Brand> aList) {
        boolean isTrue = false;
        try {
            String tableName = "tblBrandInfo";
            _deleteAllRecordsFromaTable(tableName);
            SQLiteDatabase database = dbHelperMain.getWritableDatabase();
            for (int i = 0; i < aList.size(); i++) {
                Brand nInfo = aList.get(i);
                String insertQuery = "Insert into tblBrandInfo(ProductBrandId,ProductSQName,MaxValue) " +
                        "values('" + nInfo.getProductBrandId() + "','" + nInfo.getProductSQName().replace("'", "''") + "','" + nInfo.getMaxValue() + "')";
                database.execSQL(insertQuery);
                isTrue = true;
            }

        } catch (Exception exception) {
            isTrue = false;
            Log.e("DBEX", exception.toString());

        }
        return isTrue;
    }
    public boolean InsertDoctorCategory(List<DoctorCategory> aList) {
        boolean isTrue = true;
        try {

            String tableName = "tblDoctorCategory";
            _deleteAllRecordsFromaTable(tableName);
            SQLiteDatabase database = dbHelperMain.getWritableDatabase();
            for (int i = 0; i < aList.size(); i++) {
                DoctorCategory nInfo = aList.get(i);
                String insertQuery = "Insert into tblDoctorCategory(CategoryId,CategoryName) " +
                        "values('" + nInfo.getCategoryId() + "','" + nInfo.getCategoryName().replace("'", "''") + "')";
                database.execSQL(insertQuery);
            }

        } catch (Exception exception) {
            isTrue = false;
            Log.e("DBEX", exception.toString());

        }
        return isTrue;
    }
    public boolean InsertVisitType(List<TourTypeViewModel> aTpp) {
        boolean isTrue = true;
        try {
            String tableName = "tblVisit_Type";
            _deleteAllRecordsFromaTable(tableName);

            SQLiteDatabase databases = dbHelperMain.getWritableDatabase();
            for (int i = 0; i < aTpp.size(); i++) {
                TourTypeViewModel aInfo = aTpp.get(i);
                String insertQuery = "Insert into tblVisit_Type(TourTypeId,TourTypeName) " +
                        "values('" + aInfo.getTourTypeId() + "','" + aInfo.getTourTypeName().replace("'", "''") + "')";
                databases.execSQL(insertQuery);
            }
        } catch (Exception exception) {
            Log.e("DBEX", exception.toString());
        }

        return isTrue;
    }
    public boolean InsertCustomerType(List<CustomerType> aList) {
        boolean isTrue = true;
        try {
            String tableName = "tblCustomer_Type";
            _deleteAllRecordsFromaTable(tableName);
            SQLiteDatabase database = dbHelperMain.getWritableDatabase();
            for (int i = 0; i < aList.size(); i++) {
                CustomerType aInfo = aList.get(i);
                String insertQuery = "Insert into tblCustomer_Type(CustomerTypeId,CustomerType) " +
                        "values('" + aInfo.getCustomerTypeId() + "','" + aInfo.getCustomerType().replace("'", "''") + "')";
                database.execSQL(insertQuery);
            }

        } catch (Exception exception) {
            isTrue = false;
            Log.e("DBEX", exception.toString());

        }
        return isTrue;
    }
    public boolean InsertContType(List<ContactTypeVM> aList) {
        boolean isTrue = true;
        try {

            String tableName = "tblContact_Type";
            _deleteAllRecordsFromaTable(tableName);
            SQLiteDatabase database = dbHelperMain.getWritableDatabase();
            for (int i = 0; i < aList.size(); i++) {
                ContactTypeVM aInfo = aList.get(i);
                String insertQuery = "Insert into tblContact_Type(ContactTypeId,ContactType) " +
                        "values('" + aInfo.getContactTypeId() + "','" + aInfo.getContactType() + "')";
                database.execSQL(insertQuery);
            }

        } catch (Exception exception) {
            isTrue = false;
            Log.e("DBEX", exception.toString());

        }
        return isTrue;
    }
    public boolean InsertExpType(List<ExpenseTypeMaster> aList) {
        boolean isTrue = true;
        try {

            String tableName = "tblExpense_Type";
            _deleteAllRecordsFromaTable(tableName);
            SQLiteDatabase database = dbHelperMain.getWritableDatabase();
            for (int i = 0; i < aList.size(); i++) {
                ExpenseTypeMaster aInfo = aList.get(i);
                String insertQuery = "Insert into tblExpense_Type(ExpenseTypeId,ExpenseTypeName) " +
                        "values('" + aInfo.getExpenseTypeId() + "','" + aInfo.getExpenseTypeName().replace("'", "''") + "')";
                database.execSQL(insertQuery);
            }

        } catch (Exception exception) {
            isTrue = false;
            Log.e("DBEX", exception.toString());

        }
        return isTrue;
    }
    public boolean InsertLeaveType(List<LeaveTypeInfo> aList) {
        boolean isTrue = true;
        try {

            String tableName = "tblLeave_Type";
            _deleteAllRecordsFromaTable(tableName);
            SQLiteDatabase database = dbHelperMain.getWritableDatabase();
            for (int i = 0; i < aList.size(); i++) {
                LeaveTypeInfo aInfo = aList.get(i);
                String insertQuery = "Insert into tblLeave_Type(LeaveBalanceId,LeaveTypeName,YearlyLeaveBalance) " +
                        "values('" + aInfo.getLeaveBalanceId() + "','" + aInfo.getLeaveTypeName().replace("'", "''") + "','" + aInfo.getYearlyLeaveBalance() + "')";
                database.execSQL(insertQuery);
            }
        } catch (Exception exception) {
            Log.e("DBEX", exception.toString());
        }
        return isTrue;
    }
    public boolean InsertPrescripType(List<PrescriptionTYpe> aList) {
        boolean isTrue = true;
        try {
            String tableName = "tblPrescrip_Type";
            _deleteAllRecordsFromaTable(tableName);

            SQLiteDatabase database = dbHelperMain.getWritableDatabase();
            for (int i = 0; i < aList.size(); i++) {
                PrescriptionTYpe aInfo = aList.get(i);
                String insertQuery = "Insert into tblPrescrip_Type(PrescriptionTypeId,PrescriptionType) " + "values('" + aInfo.getPrescriptionTypeId() + "','" + aInfo.getPrescriptionType().replace("'", "''") + "')";
                database.execSQL(insertQuery);
            }
        } catch (Exception exception) {
            Log.e("DBEX", exception.toString());
        }
        return isTrue;
    }
    public boolean InsertNonEffectiveReason(List<NonEffectiveReason> nList) {
        boolean isTrue = true;
        try {
            String tableName = "tblNonEffectiveReason";
            _deleteAllRecordsFromaTable(tableName);

            SQLiteDatabase database = dbHelperMain.getWritableDatabase();
            for (int i = 0; i < nList.size(); i++) {
                NonEffectiveReason ninfo = nList.get(i);
                String insertQuery = "Insert into tblNonEffectiveReason(ReasonId,ReasonName) " + "values('" + ninfo.getReasonId() + "','" + ninfo.getReasonName().replace("'", "''") + "')";
                database.execSQL(insertQuery);
            }
        } catch (Exception exception) {
            Log.e("DBEX", exception.toString());
        }
        return isTrue;
    }
    public boolean InsertTransport(List<Transport> aList) {
        boolean isTrue = true;
        try {
            String tableName = "tblTransportInfo";
            _deleteAllRecordsFromaTable(tableName);

            SQLiteDatabase database = dbHelperMain.getWritableDatabase();
            for (int i = 0; i < aList.size(); i++) {
                Transport aInfo = aList.get(i);
                String insertQuery = "Insert into tblTransportInfo(TransportId,TransportName) " +
                        "values('" + aInfo.getTransportId() + "','" + aInfo.getTransportName().replace("'", "''") + "')";
                database.execSQL(insertQuery);
            }
        } catch (Exception exception) {
            Log.e("DBEX", exception.toString());
        }
        return isTrue;
    }
    public boolean InsertProducts(List<Product> aList) {
        boolean isTrue = true;
        try {
            String tableName = "tbl_ProductInfo";
            _deleteAllRecordsFromaTable(tableName);

            SQLiteDatabase database = dbHelperMain.getWritableDatabase();
            for (int i = 0; i < aList.size(); i++) {
                Product aInfo = aList.get(i);
                String insertQuery = "Insert into tbl_ProductInfo(ProductId,ProductName,ProductCode,ProductDes,PackSize,UnitPrice,QuotedPrice,VatPercentage,VatAmountPerunit,CustomerMasterId) " +
                        "values('" + aInfo.getProductId() + "','" + aInfo.getProductName().replace("'", "''") + "','" + aInfo.getProductCode() + "','" + aInfo.getProductDes().replace("'", "''") + "','" + aInfo.getPackSize() + "','" + aInfo.getUnitPrice() + "','" + aInfo.getQuotedPrice() + "','" + aInfo.getVatPercentage() + "','" + aInfo.getVatAmountPerunit() + "','" + aInfo.getCustomerMasterId() + "')";
                database.execSQL(insertQuery);
            }
        } catch (Exception exception) {
            Log.e("DBEX", exception.toString());
        }
        return isTrue;
    }
    public boolean InsertProductSample(List<ProductSample> aProduct) {
        boolean isTrue = true;
        try {
            String tableName = "tbl_ProductSampleInfo";
            _deleteAllRecordsFromaTable(tableName);

            SQLiteDatabase databases = dbHelperMain.getWritableDatabase();
            for (int i = 0; i < aProduct.size(); i++) {
                ProductSample aInfo = aProduct.get(i);
                String insertQuery = "Insert into tbl_ProductSampleInfo(ProductId,ProductName,ProductCode) " +
                        "values('" + aInfo.getProductId() + "','" + aInfo.getProductName().replace("'", "''") + "','" + aInfo.getProductCode() + "')";
                databases.execSQL(insertQuery);
            }
            databases.close();
        } catch (Exception exception) {
            Log.e("DBEX", exception.toString());
        }

        return isTrue;
    }
    public boolean InsertProductGift(List<Gift> aProduct) {
        boolean isTrue = false;
        try {
            String tableName = "tbl_ProductGiftInfo";
            _deleteAllRecordsFromaTable(tableName);

            SQLiteDatabase databases = dbHelperMain.getWritableDatabase();
            for (int i = 0; i < aProduct.size(); i++) {
                Gift aInfo = aProduct.get(i);
                String insertQuery = "Insert into tbl_ProductGiftInfo(ProductId,ProductName,ProductCode) " +
                        "values('" + aInfo.getProductId() + "','" + aInfo.getProductName().replace("'", "''") + "','" + aInfo.getProductCode() + "')";
                databases.execSQL(insertQuery);
                isTrue = true;
            }
            databases.close();
        } catch (Exception exception) {
            Log.e("DBEX", exception.toString());
        }

        return isTrue;
    }
    public boolean InsertTourPlanPurpose(List<TourPurposeViewModel> aTpp) {
        boolean isTrue = true;
        try {
            String tableName = "tblTourPlanPurpose";
            _deleteAllRecordsFromaTable(tableName);

            SQLiteDatabase databases = dbHelperMain.getWritableDatabase();
            for (int i = 0; i < aTpp.size(); i++) {
                TourPurposeViewModel aInfo = aTpp.get(i);
                String insertQuery = "Insert into tblTourPlanPurpose(TPId,TPName,IsMarketVisit,IsOtherVisit) " +
                        "values('" + aInfo.getTPId() + "','" + aInfo.getTPName().replace("'", "''") + "','" + aInfo.getIsMarketVisit() + "','" + aInfo.getIsOtherVisit() + "')";
                databases.execSQL(insertQuery);
            }
        } catch (Exception exception) {
            Log.e("DBEX", exception.toString());
        }

        return isTrue;
    }

    /*public boolean InsertGift(List<> aProduct) {
        boolean isTrue = true;
        try {
            String tableName = "tbl_ProductSampleInfo";
            _deleteAllRecordsFromaTable(tableName);

            SQLiteDatabase databases = dbHelperMain.getWritableDatabase();
            for (int i = 0; i < aProduct.size(); i++) {
                ProductSample aInfo = aProduct.get(i);
                String insertQuery = "Insert into tbl_ProductSampleInfo(ProductId,ProductName,ProductCode) " +
                        "values('" + aInfo.getProductId() + "','" + aInfo.getProductName() + "','" + aInfo.getProductCode() + "')";
                databases.execSQL(insertQuery);
            }
        } catch (Exception exception) {
            Log.e("DBEX", exception.toString());
        }

        return isTrue;
    }*/

    //Initial Sync
   /* public void Insert_InitTable(String today, String todaytime) {
        try {
            String tableName = "tblInitTable";
            _deleteAllRecordsFromaTable(tableName);
            SQLiteDatabase database = dbHelperMain.getWritableDatabase();

            String insertQuery = "Insert into tblInitTable(IsFirstSyncDone,Date,Time) " +
                    "values(1,'" + today + "','" + todaytime + "')";
            database.execSQL(insertQuery);
        } catch (Exception exception) {
            Log.e("DBEX", exception.toString());
        }
    }*/

    //Initial Sync
    public boolean Insert_InitTableB(String today, String todaytime) {
        try {
            String tableName = "tblInitTable";
            _deleteAllRecordsFromaTable(tableName);
            SQLiteDatabase database = dbHelperMain.getWritableDatabase();

            String insertQuery = "Insert into tblInitTable(IsFirstSyncDone,Date,Time) " +
                    "values(1,'" + today + "','" + todaytime + "')";
            database.execSQL(insertQuery);
            database.close();
        } catch (Exception exception) {
            Log.e("DBEX", exception.toString());
        }
        return true;
    }

    //Check Data in initial Table
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

    //Check backup load or not
   /* public void Insert_InitTableLoadBackup() {
        try {
            String tableName = "tblInitTableLoadBackup";
            _deleteAllRecordsFromaTable(tableName);
            SQLiteDatabase database = dbHelperMain.getWritableDatabase();
            String insertQuery = "Insert into tblInitTableLoadBackup(IsFirstLoadBackupDone) " +
                    "values(1)";
            database.execSQL(insertQuery);
        } catch (Exception exception) {
            Log.e("DBEX", exception.toString());
        }
    }*/

    public void _deleteAllRecordsFromaTable(String tableName) {
        SQLiteDatabase database = dbHelperMain.getWritableDatabase();
        try {
            String query = "delete from '" + tableName + "';";
            database.execSQL(query);
        } catch (Exception ex) {
            Log.e("recordDelError", ex.toString());
        }
        database.close();
    }


}
