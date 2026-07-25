package com.creatrix.salessolution.DBAdapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.creatrix.salessolution.Activity.Doctor.DCR.NonEffectiveReason;
import com.creatrix.salessolution.DBInfo.DBHelperMain;
import com.creatrix.salessolution.Model.DistrictVM;
import com.creatrix.salessolution.Model.DivisionVM;
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
import com.creatrix.salessolution.Model.ThanaVM;
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

  //Area section Insert
    public boolean InsertDivision(List<DivisionVM> aList) {
        boolean isTrue = true;
        try {
            String tableName = "tblDivisionInfo";
            _deleteAllRecordsFromaTable(tableName);
            SQLiteDatabase database = dbHelperMain.getWritableDatabase();
            
            database.beginTransaction();
            try {
for (int i = 0; i < aList.size(); i++) {
                DivisionVM aInfo = aList.get(i);
                android.content.ContentValues values = new android.content.ContentValues();
values.put("DivisionId", aInfo.getDivisionId());
values.put("DivisionName", aInfo.getDivisionName());
database.insert("tblDivisionInfo", null, values);
            }
        
                database.setTransactionSuccessful();
            } finally {
                database.endTransaction();
            }} catch (Exception exception) {
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
            
            database.beginTransaction();
            try {
for (int i = 0; i < aList.size(); i++) {
                DistrictVM aInfo = aList.get(i);
                android.content.ContentValues values = new android.content.ContentValues();
values.put("DistrictId", aInfo.getDistrictId());
values.put("DistrictName", aInfo.getDistrictName());
values.put("DivisionId", aInfo.getDivisionId());
database.insert("tblDistrictInfo", null, values);
                // << database.close(); এখানে নয় >>
            }
            
                database.setTransactionSuccessful();
            } finally {
                database.endTransaction();
            }database.close(); // লুপের বাইরে, সব শেষ হলে ক্লোজ করুন
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
            
            database.beginTransaction();
            try {
for (int i = 0; i < aList.size(); i++) {
                ThanaVM aInfo = aList.get(i);
                android.content.ContentValues values = new android.content.ContentValues();
values.put("ThanaId", aInfo.getThanaId());
values.put("ThanaName", aInfo.getThanaName());
values.put("district_id", aInfo.getDistrict_id());
database.insert("tblThanaInfo", null, values);
            }
        
                database.setTransactionSuccessful();
            } finally {
                database.endTransaction();
            }} catch (Exception exception) {
            isTrue = false;

            Log.e("DBEX", exception.toString());
        }
        return isTrue;
    }

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

    public void deleteAllFromTable(String tableName, SQLiteDatabase database) {
        try {
            String query = "DELETE FROM '" + tableName + "';";
            database.execSQL(query);
        } catch (Exception ex) {
            Log.e("recordDelError", ex.toString());
        }
    }

    public void deleteAllMasterTables() {
        SQLiteDatabase database = dbHelperMain.getWritableDatabase();
        database.beginTransaction();

        try {
            deleteAllFromTable("tbl_Group", database);
            deleteAllFromTable("tblRegion", database);
            deleteAllFromTable("tblArea", database);
            deleteAllFromTable("tblTerritory", database);
            deleteAllFromTable("tblSubTerritory", database);
            deleteAllFromTable("tblMarket", database);
            deleteAllFromTable("tblNSMInfo", database);
            deleteAllFromTable("tblRSMInfo", database);
            deleteAllFromTable("tblASMInfo", database);
            deleteAllFromTable("tblMIOInfo", database);

            database.setTransactionSuccessful();
        } catch (Exception ex) {
            Log.e("deleteAllError", ex.toString());
        } finally {
            database.endTransaction();
            database.close();
        }
    }

    public boolean InsertGroup(List<Group> gList) {
        boolean isTrue = false;
        String tableName = "tbl_Group";
        SQLiteDatabase database = dbHelperMain.getWritableDatabase();
        database.beginTransaction();
        try {
            for (Group m : gList) {
                try {
                    ContentValues cv = new ContentValues();
                    cv.put("GroupId", m.getGroupId());
                    cv.put("GroupName", m.getGroupName() != null ? m.getGroupName().replace("'", "''") : "");
                    database.insert(tableName, null, cv);
                } catch (Exception rowEx) {
                    Log.e("DBEX", rowEx.toString());
                }
            }
            isTrue = true;
            database.setTransactionSuccessful();
        } catch (Exception ex) {
            Log.e("DBEX", ex.toString());
        } finally {
            database.endTransaction();
            database.close();
        }
        return isTrue;
    }

    public boolean InsertRegion(List<Region> rList) {
        boolean isTrue = false;
        String tableName = "tblRegion";
        SQLiteDatabase database = dbHelperMain.getWritableDatabase();
        database.beginTransaction();
        try {
            for (Region m : rList) {
                try {
                    ContentValues cv = new ContentValues();
                    cv.put("RegionId", m.getRegionId());
                    cv.put("RegionName", m.getRegionName() != null ? m.getRegionName().replace("'", "''") : "");
                    cv.put("GroupId", m.getGroupId());
                    database.insert(tableName, null, cv);
                } catch (Exception rowEx) {
                    Log.e("DBEX", rowEx.toString());
                }
            }
            isTrue = true;
            database.setTransactionSuccessful();
        } catch (Exception ex) {
            Log.e("DBEX", ex.toString());
        } finally {
            database.endTransaction();
            database.close();
        }
        return isTrue;
    }

    public boolean InsertArea(List<Area> arList) {
        boolean isTrue = false;
        String tableName = "tblArea";
        SQLiteDatabase database = dbHelperMain.getWritableDatabase();
        database.beginTransaction();
        try {
            for (Area m : arList) {
                try {
                    ContentValues cv = new ContentValues();
                    cv.put("AreaId", m.getAreaId());
                    cv.put("AreaName", m.getAreaName() != null ? m.getAreaName().replace("'", "''") : "");
                    cv.put("RegionId", m.getRegionId());
                    database.insert(tableName, null, cv);
                } catch (Exception rowEx) {
                    Log.e("DBEX", rowEx.toString());
                }
            }
            isTrue = true;
            database.setTransactionSuccessful();
        } catch (Exception ex) {
            Log.e("DBEX", ex.toString());
        } finally {
            database.endTransaction();
            database.close();
        }
        return isTrue;
    }

    public boolean InsertTeritorry(List<Teritorry> tList) {
        boolean isTrue = false;
        String tableName = "tblTerritory";
        SQLiteDatabase database = dbHelperMain.getWritableDatabase();
        database.beginTransaction();
        try {
            for (Teritorry m : tList) {
                try {
                    ContentValues cv = new ContentValues();
                    cv.put("TerritoryId", m.getTerritoryId());
                    cv.put("TerritoryName", m.getTerritoryName() != null ? m.getTerritoryName().replace("'", "''") : "");
                    cv.put("AreaId", m.getAreaId());
                    database.insert(tableName, null, cv);
                } catch (Exception rowEx) {
                    Log.e("DBEX", rowEx.toString());
                }
            }
            isTrue = true;
            database.setTransactionSuccessful();
        } catch (Exception ex) {
            Log.e("DBEX", ex.toString());
        } finally {
            database.endTransaction();
            database.close();
        }
        return isTrue;
    }

    public boolean InsertSubTeritorry(List<SubTeritorry> stList) {
        boolean isTrue = false;
        String tableName = "tblSubTerritory";
        SQLiteDatabase database = dbHelperMain.getWritableDatabase();
        database.beginTransaction();
        try {
            for (SubTeritorry m : stList) {
                try {
                    ContentValues cv = new ContentValues();
                    cv.put("SubTerritoryId", m.getSubTerritoryId());
                    cv.put("SubTerritoryName", m.getSubTerritoryName() != null ? m.getSubTerritoryName().replace("'", "''") : "");
                    cv.put("TerritoryId", m.getTerritoryId());
                    database.insert(tableName, null, cv);
                } catch (Exception rowEx) {
                    Log.e("DBEX", rowEx.toString());
                }
            }
            isTrue = true;
            database.setTransactionSuccessful();
        } catch (Exception ex) {
            Log.e("DBEX", ex.toString());
        } finally {
            database.endTransaction();
            database.close();
        }
        return isTrue;
    }

    public boolean InsertMarket(List<Market> mList) {
        boolean isTrue = false;
        String tableName = "tblMarket";
        SQLiteDatabase database = dbHelperMain.getWritableDatabase();
        database.beginTransaction();
        try {
            for (Market m : mList) {
                try {
                    ContentValues cv = new ContentValues();
                    cv.put("MarketId", m.getMarketId());
                    cv.put("MarketName", m.getMarketName() != null ? m.getMarketName().replace("'", "''") : "");
                    cv.put("SubTerritoryId", m.getSubTerritoryId());
                    database.insert(tableName, null, cv);
                } catch (Exception rowEx) {
                    Log.e("DBEX", rowEx.toString());
                }
            }
            isTrue = true;
            database.setTransactionSuccessful();
        } catch (Exception ex) {
            Log.e("DBEX", ex.toString());
        } finally {
            database.endTransaction();
            database.close();
        }
        return isTrue;
    }

    //TODO:Insert MasterEmp
    public boolean InsertNSMInfo(List<NSM> nList) {
        boolean isTrue = true;
        try {

            String tableName = "tblNSMInfo";
           // _deleteAllRecordsFromaTable(tableName);
            SQLiteDatabase database = dbHelperMain.getWritableDatabase();
            
            database.beginTransaction();
            try {
for (int i = 0; i < nList.size(); i++) {
                NSM nInfo = nList.get(i);
                android.content.ContentValues values = new android.content.ContentValues();
values.put("NSMEmpId", nInfo.getNSMEmpId());
values.put("NSMId", nInfo.getNSMId());
values.put("EmpMasterCode", nInfo.getEmpMasterCode());
values.put("EmpName", nInfo.getEmpName());
values.put("GroupId", nInfo.getGroupId());
database.insert("tblNSMInfo", null, values);
            }


                database.setTransactionSuccessful();
            } finally {
                database.endTransaction();
            }} catch (Exception exception) {
            isTrue = false;
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
            
            database.beginTransaction();
            try {
for (int i = 0; i < rList.size(); i++) {
                RSM nInfo = rList.get(i);
                android.content.ContentValues values = new android.content.ContentValues();
values.put("RSMEmpId", nInfo.getRSMEmpId());
values.put("RSMId", nInfo.getRSMId());
values.put("EmpMasterCode", nInfo.getEmpMasterCode());
values.put("EmpName", nInfo.getEmpName().replace("'", "''"));
values.put("RegionId", nInfo.getRegionId());
database.insert("tblRSMInfo", null, values);
            }


                database.setTransactionSuccessful();
            } finally {
                database.endTransaction();
            }} catch (Exception exception) {
            isTrue = false;
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
            
            database.beginTransaction();
            try {
for (int i = 0; i < aList.size(); i++) {
                ASM nInfo = aList.get(i);
                android.content.ContentValues values = new android.content.ContentValues();
values.put("ASMEmpId", nInfo.getASMEmpId());
values.put("ASMId", nInfo.getASMId());
values.put("EmpMasterCode", nInfo.getEmpMasterCode());
values.put("EmpName", nInfo.getEmpName());
values.put("AreaId", nInfo.getAreaId());
database.insert("tblASMInfo", null, values);
            }


                database.setTransactionSuccessful();
            } finally {
                database.endTransaction();
            }} catch (Exception exception) {
            isTrue = false;
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
            
            database.beginTransaction();
            try {
for (int i = 0; i < mList.size(); i++) {
                MIO nInfo = mList.get(i);
                android.content.ContentValues values = new android.content.ContentValues();
values.put("MIOEmpId", nInfo.getMIOEmpId());
values.put("MIOId", nInfo.getMIOId());
values.put("EmpMasterCode", nInfo.getEmpMasterCode());
values.put("EmpName", nInfo.getEmpName());
values.put("TerritoryId", nInfo.getTerritoryId());
database.insert("tblMIOInfo", null, values);
            }


                database.setTransactionSuccessful();
            } finally {
                database.endTransaction();
            }} catch (Exception exception) {
            isTrue = false;
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
                android.content.ContentValues values = new android.content.ContentValues();
values.put("TerritoryId", aInfo.getTerritoryId());
values.put("TerritoryName", TerritoryName*//*aInfo.getTerritoryName().replace("'","''")*//*);
values.put("TerritoryCode", aInfo.getTerritoryCode());
database.insert("tblTeritory", null, values);
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
            database.beginTransaction();
            try {
                for (int i = 0; i < aList.size(); i++) {
                    Customer aInfo = aList.get(i);
                    android.content.ContentValues values = new android.content.ContentValues();
                    values.put("CustomerMasterId", aInfo.getCustomerMasterId());
                    values.put("CustomerName", aInfo.getCustomerName());
                    values.put("CustomerCode", aInfo.getCustomerCode());
                    values.put("CustomerAdress", aInfo.getAddress());
                    values.put("CustomerType", aInfo.getCustomerType());
                    values.put("CustomerCell", aInfo.getCellNo());
                    values.put("CustomerBalance", aInfo.getBalance());
                    values.put("CustomerCreditlimit", aInfo.getCreditLimit());
                    values.put("Market", aInfo.getMarketName());
                    values.put("MarketCode", aInfo.getMarketCode());
                    values.put("GroupId", aInfo.getGroupId());
                    values.put("RegionId", aInfo.getRegionId());
                    values.put("AreaId", aInfo.getAreaId());
                    values.put("TerritoryId", aInfo.getTerritoryId());
                    values.put("SubTerritoryId", aInfo.getSubTerritoryId());
                    values.put("MarketId", aInfo.getMarketId());
                    values.put("Note", aInfo.getNote());
                    values.put("CustomerCheck", aInfo.getCustomerCheck());
                    values.put("CustomerTypeId", aInfo.getCustomerTypeId());
                    values.put("ProgramTypeId", aInfo.getProgramTypeId());
                    values.put("SMCTypeId", aInfo.getSMCTypeId());

                    database.insert(tableName, null, values);
                }
                database.setTransactionSuccessful();
            } finally {
                database.endTransaction();
            }
            database.close();
        } catch (Exception exception) {
            isTrue = false;
            Log.e("DBEX", exception.toString());
        }
        return isTrue;
    }

    public boolean InsertCustomerStation(List<StationType> aList) {
        boolean isTrue = true;
        try {

            String tableName = "tblCustomer_Station";
            _deleteAllRecordsFromaTable(tableName);
            SQLiteDatabase database = dbHelperMain.getWritableDatabase();
            
            database.beginTransaction();
            try {
for (int i = 0; i < aList.size(); i++) {
                StationType aInfo = aList.get(i);
                android.content.ContentValues values = new android.content.ContentValues();
values.put("StationTypeId", aInfo.getStationTypeId());
values.put("StationTypeName", aInfo.getStationTypeName().replace("'", "''"));
database.insert("tblCustomer_Station", null, values);
            }

        
                database.setTransactionSuccessful();
            } finally {
                database.endTransaction();
            }} catch (Exception exception) {
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
            database.beginTransaction();
            try {
                for (int i = 0; i < aList.size(); i++) {
                    DoctorListViewModel aInfo = aList.get(i);
                    
                    android.content.ContentValues values = new android.content.ContentValues();
                    values.put("DoctorId", aInfo.getDoctorId());
                    values.put("DoctorCode", aInfo.getDoctorCode());
                    values.put("DoctorName", aInfo.getDoctorName());
                    values.put("ChemberName", aInfo.getChemberName());
                    values.put("DocContact", aInfo.getDocContact());
                    values.put("DocTPDetailsId", aInfo.getDocTPDetailsId());
                    values.put("DoctorTypeName", aInfo.getDoctorTypeName());
                    values.put("ProgramTypeName", aInfo.getProgramTypeName());
                    values.put("GroupId", aInfo.getGroupId());
                    values.put("RegionId", aInfo.getRegionId());
                    values.put("AreaId", aInfo.getAreaId());
                    values.put("TerritoryId", aInfo.getTerritoryId());
                    values.put("SubTerritoryId", aInfo.getSubTerritoryId());
                    values.put("MarketId", aInfo.getMarketId());
                    values.put("MarketName", aInfo.getMarketName());
                    values.put("MarketCode", aInfo.getMarketCode());
                    values.put("ProgramTypeId", aInfo.getProgramTypeId());
                    values.put("DoctorTypeId", aInfo.getDoctorTypeId());
                    values.put("SMCTypeId", aInfo.getSMCTypeId());
                    values.put("SMCType", aInfo.getSMCType());

                    database.insert("tblDoctorInfo", null, values);

                    if (aInfo.getDoctorBrand() != null) {
                        for (int j = 0; j < aInfo.getDoctorBrand().size(); j++) {
                            DoctorBrand dbi = aInfo.getDoctorBrand().get(j);
                            android.content.ContentValues brandValues = new android.content.ContentValues();
                            brandValues.put("BrandId", dbi.getBrandId());
                            brandValues.put("BrandName", dbi.getBrandName());
                            brandValues.put("DoctorId", dbi.getDoctorId());
                            database.insert("tblDoctorBrand", null, brandValues);
                        }
                    }
                }
                database.setTransactionSuccessful();
            } finally {
                database.endTransaction();
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
            
            database.beginTransaction();
            try {
for (int i = 0; i < aList.size(); i++) {
                DoctorDesignation aInfo = aList.get(i);
                android.content.ContentValues values = new android.content.ContentValues();
values.put("DesignationId", aInfo.getDesignationId());
values.put("DesignationName", aInfo.getDesignationName().replace("'", "''"));
database.insert("tblDoctor_Desig", null, values);
            }

        
                database.setTransactionSuccessful();
            } finally {
                database.endTransaction();
            }} catch (Exception exception) {
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
            
            database.beginTransaction();
            try {
for (int i = 0; i < aList.size(); i++) {
                DoctorDegreeViewModel aInfo = aList.get(i);
                android.content.ContentValues values = new android.content.ContentValues();
values.put("DegreeId", aInfo.getDegreeId());
values.put("DegreeName", aInfo.getDegreeName().replace("'", "''"));
values.put("DoctorTypeId", aInfo.getDoctorTypeId());
database.insert("tblDoctor_Degree", null, values);
            }

        
                database.setTransactionSuccessful();
            } finally {
                database.endTransaction();
            }} catch (Exception exception) {
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
            
            database.beginTransaction();
            try {
for (int i = 0; i < aList.size(); i++) {
                DoctorSpecialityViewModel aInfo = aList.get(i);
                android.content.ContentValues values = new android.content.ContentValues();
values.put("SpecialityId", aInfo.getSpecialityId());
values.put("SpecialityName", aInfo.getSpecialityName().replace("'", "''"));
database.insert("tblDoctor_Speciality", null, values);
            }

        
                database.setTransactionSuccessful();
            } finally {
                database.endTransaction();
            }} catch (Exception exception) {
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
            
            database.beginTransaction();
            try {
for (int i = 0; i < aList.size(); i++) {
                SpecialDay aInfo = aList.get(i);
                android.content.ContentValues values = new android.content.ContentValues();
values.put("SpecialDayId", aInfo.getSpecialDayId());
values.put("SpecialDay", aInfo.getSpecialDay().replace("'", "''"));
database.insert("tblDoctor_Specialday", null, values);
            }

        
                database.setTransactionSuccessful();
            } finally {
                database.endTransaction();
            }} catch (Exception exception) {
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
            
            database.beginTransaction();
            try {
for (int i = 0; i < aList.size(); i++) {
                InstitutionVM aInfo = aList.get(i);
                android.content.ContentValues values = new android.content.ContentValues();
values.put("InstitutionId", aInfo.getInstitutionId());
values.put("Institution", aInfo.getInstitution().replace("'", "''"));
database.insert("tblDoctor_Institution", null, values);
            }

        
                database.setTransactionSuccessful();
            } finally {
                database.endTransaction();
            }} catch (Exception exception) {
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
            
            database.beginTransaction();
            try {
for (int i = 0; i < aList.size(); i++) {
                DoctorChamberTypeVM aInfo = aList.get(i);
                android.content.ContentValues values = new android.content.ContentValues();
values.put("ChamberTypeId", aInfo.getChamberTypeId());
values.put("ChamberTypeName", aInfo.getChamberTypeName().replace("'", "''"));
database.insert("tblDoctor_Chembar", null, values);
            }
            
                database.setTransactionSuccessful();
            } finally {
                database.endTransaction();
            }database.close();

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
            
            database.beginTransaction();
            try {
for (int i = 0; i < aList.size(); i++) {
                DoctorChamberName aInfo = aList.get(i);
                android.content.ContentValues values = new android.content.ContentValues();
values.put("ChemberId", aInfo.getChemberId());
values.put("ChemberName", aInfo.getChemberName().replace("'", "''"));
values.put("DoctorId", aInfo.getDoctorId());
database.insert("tblDoctor_ChembarName", null, values);
            }
            
                database.setTransactionSuccessful();
            } finally {
                database.endTransaction();
            }database.close();
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
            
            database.beginTransaction();
            try {
for (int i = 0; i < aList.size(); i++) {
                DoctorTypeVM aInfo = aList.get(i);
                android.content.ContentValues values = new android.content.ContentValues();
values.put("DoctorTypeId", aInfo.getDoctorTypeId());
values.put("DoctorTypeName", aInfo.getDoctorTypeName().replace("'", "''"));
database.insert("tblDoctor_Type", null, values);
            }

        
                database.setTransactionSuccessful();
            } finally {
                database.endTransaction();
            }} catch (Exception exception) {
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
            
            database.beginTransaction();
            try {
for (int i = 0; i < aList.size(); i++) {
                UserRole aInfo = aList.get(i);
                android.content.ContentValues values = new android.content.ContentValues();
values.put("UserRoleID", aInfo.getUserRoleID());
values.put("RoleName", aInfo.getRoleName().replace("'", "''"));
database.insert("tbl_UserRole", null, values);
            }

        
                database.setTransactionSuccessful();
            } finally {
                database.endTransaction();
            }} catch (Exception exception) {
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
            
            database.beginTransaction();
            try {
for (int i = 0; i < aList.size(); i++) {
                UserByRole aInfo = aList.get(i);
                android.content.ContentValues values = new android.content.ContentValues();
values.put("EmpInfoId", aInfo.getEmpInfoId());
values.put("EmpName", aInfo.getEmpName());
values.put("EmpMasterCode", aInfo.getEmpMasterCode());
values.put("UserRoleID", aInfo.getUserRoleID());
database.insert("tbl_UserByRole", null, values);
            }
            
                database.setTransactionSuccessful();
            } finally {
                database.endTransaction();
            }database.close();
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
            
            database.beginTransaction();
            try {
for (int i = 0; i < aList.size(); i++) {
                ProgramType aInfo = aList.get(i);
                android.content.ContentValues values = new android.content.ContentValues();
values.put("ProgramTypeId", aInfo.getProgramTypeId());
values.put("ProgramType", aInfo.getProgramType().replace("'", "''"));
database.insert("tblProgram_Type", null, values);
            }

        
                database.setTransactionSuccessful();
            } finally {
                database.endTransaction();
            }} catch (Exception exception) {
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
            
            database.beginTransaction();
            try {
for (int i = 0; i < aList.size(); i++) {
                ModelProviderType aInfo = aList.get(i);
                android.content.ContentValues values = new android.content.ContentValues();
values.put("ProviderTypeId", aInfo.getProviderTypeId());
values.put("ProviderType", aInfo.getProviderType().replace("'", "''"));
values.put("forCustomer", aInfo.getForCustomer());
values.put("forDoctor", aInfo.getForDoctor());
database.insert("tblProvider_Type", null, values);
                isTrue = true;
            }

        
                database.setTransactionSuccessful();
            } finally {
                database.endTransaction();
            }} catch (Exception exception) {
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
            
            database.beginTransaction();
            try {
for (int i = 0; i < aList.size(); i++) {
                ModelSMCType aInfo = aList.get(i);
                android.content.ContentValues values = new android.content.ContentValues();
values.put("SMCTypeId", aInfo.getSMCTypeId());
values.put("SMCType", aInfo.getSMCType().replace("'", "''"));
values.put("forCustomer", aInfo.getForCustomer());
values.put("forDoctor", aInfo.getForDoctor());
database.insert("tblSMCType", null, values);
                isTrue = true;
            }

        
                database.setTransactionSuccessful();
            } finally {
                database.endTransaction();
            }} catch (Exception exception) {
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
            
            database.beginTransaction();
            try {
for (int i = 0; i < aList.size(); i++) {
                Brand nInfo = aList.get(i);
                android.content.ContentValues values = new android.content.ContentValues();
values.put("ProductBrandId", nInfo.getProductBrandId());
values.put("ProductSQName", nInfo.getProductSQName().replace("'", "''"));
values.put("MaxValue", nInfo.getMaxValue());
database.insert("tblBrandInfo", null, values);
                isTrue = true;
            }

        
                database.setTransactionSuccessful();
            } finally {
                database.endTransaction();
            }} catch (Exception exception) {
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
            
            database.beginTransaction();
            try {
for (int i = 0; i < aList.size(); i++) {
                DoctorCategory nInfo = aList.get(i);
                android.content.ContentValues values = new android.content.ContentValues();
values.put("CategoryId", nInfo.getCategoryId());
values.put("CategoryName", nInfo.getCategoryName().replace("'", "''"));
database.insert("tblDoctorCategory", null, values);
            }

        
                database.setTransactionSuccessful();
            } finally {
                database.endTransaction();
            }} catch (Exception exception) {
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

            SQLiteDatabase database = dbHelperMain.getWritableDatabase();
            
            database.beginTransaction();
            try {
for (int i = 0; i < aTpp.size(); i++) {
                TourTypeViewModel aInfo = aTpp.get(i);
                android.content.ContentValues values = new android.content.ContentValues();
values.put("TourTypeId", aInfo.getTourTypeId());
values.put("TourTypeName", aInfo.getTourTypeName().replace("'", "''"));
database.insert("tblVisit_Type", null, values);
            }

                database.setTransactionSuccessful();
            } finally {
                database.endTransaction();
            }} catch (Exception exception) {
            isTrue = false;
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
            
            database.beginTransaction();
            try {
for (int i = 0; i < aList.size(); i++) {
                CustomerType aInfo = aList.get(i);
                android.content.ContentValues values = new android.content.ContentValues();
values.put("CustomerTypeId", aInfo.getCustomerTypeId());
values.put("CustomerType", aInfo.getCustomerType().replace("'", "''"));
database.insert("tblCustomer_Type", null, values);
            }

        
                database.setTransactionSuccessful();
            } finally {
                database.endTransaction();
            }} catch (Exception exception) {
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
            
            database.beginTransaction();
            try {
for (int i = 0; i < aList.size(); i++) {
                ContactTypeVM aInfo = aList.get(i);
                android.content.ContentValues values = new android.content.ContentValues();
values.put("ContactTypeId", aInfo.getContactTypeId());
values.put("ContactType", aInfo.getContactType());
database.insert("tblContact_Type", null, values);
            }

        
                database.setTransactionSuccessful();
            } finally {
                database.endTransaction();
            }} catch (Exception exception) {
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
            
            database.beginTransaction();
            try {
for (int i = 0; i < aList.size(); i++) {
                ExpenseTypeMaster aInfo = aList.get(i);
                android.content.ContentValues values = new android.content.ContentValues();
values.put("ExpenseTypeId", aInfo.getExpenseTypeId());
values.put("ExpenseTypeName", aInfo.getExpenseTypeName().replace("'", "''"));
database.insert("tblExpense_Type", null, values);
            }

        
                database.setTransactionSuccessful();
            } finally {
                database.endTransaction();
            }} catch (Exception exception) {
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
            
            database.beginTransaction();
            try {
for (int i = 0; i < aList.size(); i++) {
                LeaveTypeInfo aInfo = aList.get(i);
                android.content.ContentValues values = new android.content.ContentValues();
values.put("LeaveBalanceId", aInfo.getLeaveBalanceId());
values.put("LeaveTypeName", aInfo.getLeaveTypeName().replace("'", "''"));
values.put("YearlyLeaveBalance", aInfo.getYearlyLeaveBalance());
database.insert("tblLeave_Type", null, values);
            }

                database.setTransactionSuccessful();
            } finally {
                database.endTransaction();
            }} catch (Exception exception) {
            isTrue = false;
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
            
            database.beginTransaction();
            try {
for (int i = 0; i < aList.size(); i++) {
                PrescriptionTYpe aInfo = aList.get(i);
                android.content.ContentValues values = new android.content.ContentValues();
values.put("PrescriptionTypeId", aInfo.getPrescriptionTypeId());
values.put("PrescriptionType", aInfo.getPrescriptionType().replace("'", "''"));
database.insert("tblPrescrip_Type", null, values);
            }

                database.setTransactionSuccessful();
            } finally {
                database.endTransaction();
            }} catch (Exception exception) {
            isTrue = false;
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
            
            database.beginTransaction();
            try {
for (int i = 0; i < nList.size(); i++) {
                NonEffectiveReason ninfo = nList.get(i);
                android.content.ContentValues values = new android.content.ContentValues();
values.put("ReasonId", ninfo.getReasonId());
values.put("ReasonName", ninfo.getReasonName().replace("'", "''"));
database.insert("tblNonEffectiveReason", null, values);
            }

                database.setTransactionSuccessful();
            } finally {
                database.endTransaction();
            }} catch (Exception exception) {
            isTrue = false;
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
            
            database.beginTransaction();
            try {
for (int i = 0; i < aList.size(); i++) {
                Transport aInfo = aList.get(i);
                android.content.ContentValues values = new android.content.ContentValues();
values.put("TransportId", aInfo.getTransportId());
values.put("TransportName", aInfo.getTransportName().replace("'", "''"));
database.insert("tblTransportInfo", null, values);
            }

                database.setTransactionSuccessful();
            } finally {
                database.endTransaction();
            }} catch (Exception exception) {
            isTrue = false;
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
            
            database.beginTransaction();
            try {
for (int i = 0; i < aList.size(); i++) {
                Product aInfo = aList.get(i);
                android.content.ContentValues values = new android.content.ContentValues();
values.put("ProductId", aInfo.getProductId());
values.put("ProductName", aInfo.getProductName().replace("'", "''"));
values.put("ProductCode", aInfo.getProductCode());
values.put("ProductDes", aInfo.getProductDes().replace("'", "''"));
values.put("PackSize", aInfo.getPackSize());
values.put("UnitPrice", aInfo.getUnitPrice());
values.put("QuotedPrice", aInfo.getQuotedPrice());
values.put("VatPercentage", aInfo.getVatPercentage());
values.put("VatAmountPerunit", aInfo.getVatAmountPerunit());
values.put("CustomerMasterId", aInfo.getCustomerMasterId());
database.insert("tbl_ProductInfo", null, values);
            }

                database.setTransactionSuccessful();
            } finally {
                database.endTransaction();
            }} catch (Exception exception) {
            isTrue = false;
            Log.e("DBEX", exception.toString());
        }
        return isTrue;
    }
    public boolean InsertProductSample(List<ProductSample> aProduct) {
        boolean isTrue = true;
        try {
            String tableName = "tbl_ProductSampleInfo";
            _deleteAllRecordsFromaTable(tableName);

            SQLiteDatabase database = dbHelperMain.getWritableDatabase();
            
            database.beginTransaction();
            try {
for (int i = 0; i < aProduct.size(); i++) {
                ProductSample aInfo = aProduct.get(i);
                android.content.ContentValues values = new android.content.ContentValues();
values.put("ProductId", aInfo.getProductId());
values.put("ProductName", aInfo.getProductName().replace("'", "''"));
values.put("ProductCode", aInfo.getProductCode());
database.insert("tbl_ProductSampleInfo", null, values);
            }

                database.setTransactionSuccessful();
            } finally {
                database.endTransaction();
            }database.close();
        } catch (Exception exception) {
            isTrue = false;
            Log.e("DBEX", exception.toString());
        }

        return isTrue;
    }
    public boolean InsertProductGift(List<Gift> aProduct) {
        boolean isTrue = false;
        try {
            String tableName = "tbl_ProductGiftInfo";
            _deleteAllRecordsFromaTable(tableName);

            SQLiteDatabase database = dbHelperMain.getWritableDatabase();
            
            database.beginTransaction();
            try {
for (int i = 0; i < aProduct.size(); i++) {
                Gift aInfo = aProduct.get(i);
                android.content.ContentValues values = new android.content.ContentValues();
values.put("ProductId", aInfo.getProductId());
values.put("ProductName", aInfo.getProductName().replace("'", "''"));
values.put("ProductCode", aInfo.getProductCode());
database.insert("tbl_ProductGiftInfo", null, values);
                isTrue = true;
            }

                database.setTransactionSuccessful();
            } finally {
                database.endTransaction();
            }database.close();
        } catch (Exception exception) {
            isTrue = false;
            Log.e("DBEX", exception.toString());
        }

        return isTrue;
    }
    public boolean InsertTourPlanPurpose(List<TourPurposeViewModel> aTpp) {
        boolean isTrue = true;
        try {
            String tableName = "tblTourPlanPurpose";
            _deleteAllRecordsFromaTable(tableName);

            SQLiteDatabase database = dbHelperMain.getWritableDatabase();
            
            database.beginTransaction();
            try {
for (int i = 0; i < aTpp.size(); i++) {
                TourPurposeViewModel aInfo = aTpp.get(i);
                android.content.ContentValues values = new android.content.ContentValues();
values.put("TPId", aInfo.getTPId());
values.put("TPName", aInfo.getTPName().replace("'", "''"));
values.put("IsMarketVisit", aInfo.getIsMarketVisit());
values.put("IsOtherVisit", aInfo.getIsOtherVisit());
database.insert("tblTourPlanPurpose", null, values);
            }

                database.setTransactionSuccessful();
            } finally {
                database.endTransaction();
            }} catch (Exception exception) {
            isTrue = false;
            Log.e("DBEX", exception.toString());
        }

        return isTrue;
    }

    /*public boolean InsertGift(List<> aProduct) {
        boolean isTrue = true;
        try {
            String tableName = "tbl_ProductSampleInfo";
            _deleteAllRecordsFromaTable(tableName);

            SQLiteDatabase database = dbHelperMain.getWritableDatabase();
            
            database.beginTransaction();
            try {
for (int i = 0; i < aProduct.size(); i++) {
                ProductSample aInfo = aProduct.get(i);
                android.content.ContentValues values = new android.content.ContentValues();
values.put("ProductId", aInfo.getProductId());
values.put("ProductName", aInfo.getProductName());
values.put("ProductCode", aInfo.getProductCode());
database.insert("tbl_ProductSampleInfo", null, values);
            }
        
                database.setTransactionSuccessful();
            } finally {
                database.endTransaction();
            }} catch (Exception exception) {
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
        boolean isTrue = false;
        try {
            String tableName = "tblInitTable";
            _deleteAllRecordsFromaTable(tableName);
            SQLiteDatabase database = dbHelperMain.getWritableDatabase();

            String insertQuery = "Insert into tblInitTable(IsFirstSyncDone,Date,Time) " +
                    "values(1,'" + today + "','" + todaytime + "')";
            database.execSQL(insertQuery);
            database.close();
            isTrue = true;
        } catch (Exception exception) {
            Log.e("DBEX", exception.toString());
        }
        return isTrue;
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
