package com.creatrix.salessolution.DBAdapter.DBDoctor;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.creatrix.salessolution.Activity.Customer.Pending.CustomerARModel;
import com.creatrix.salessolution.Activity.Doctor.Pending.DoctorARModel;
import com.creatrix.salessolution.DBInfo.DBHelperMain;
import com.creatrix.salessolution.Model.Doctor.ContactTypeVM;
import com.creatrix.salessolution.Model.Doctor.Brand;
import com.creatrix.salessolution.Model.Doctor.DoctorBrand;
import com.creatrix.salessolution.Model.Doctor.DoctorCategory;
import com.creatrix.salessolution.Model.Doctor.DoctorChamberName;
import com.creatrix.salessolution.Model.Doctor.DoctorChamberTypeVM;
import com.creatrix.salessolution.Model.Doctor.DoctorDegreeViewModel;
import com.creatrix.salessolution.Model.Doctor.DoctorDesignation;
import com.creatrix.salessolution.Model.Doctor.DoctorListViewModel;
import com.creatrix.salessolution.Model.Doctor.DoctorSM;
import com.creatrix.salessolution.Model.Doctor.DoctorSpecialityViewModel;
import com.creatrix.salessolution.Model.Doctor.DoctorTypeVM;
import com.creatrix.salessolution.Model.Doctor.ProgramType;
import com.creatrix.salessolution.Model.Doctor.SpecialDay;
import com.creatrix.salessolution.Model.InstitutionVM;
import com.creatrix.salessolution.Model.ModelProviderType;
import com.creatrix.salessolution.Model.ModelSMCType;
import com.creatrix.salessolution.Model.TourTypeViewModel;
import com.creatrix.salessolution.Model.UserByRole;
import com.creatrix.salessolution.Model.UserRole;
import com.creatrix.salessolution.UtilityHelper.SessionManagement;

import java.util.ArrayList;

public class DBDoctorHelper {
    DBHelperMain dbHelperMain;
    Context context;
    SessionManagement sessionManagement;

    public DBDoctorHelper(Context context) {
        this.context = context;
        dbHelperMain = new DBHelperMain(context);
    }

    @SuppressLint("Range")
    public ArrayList<DoctorARModel> getDoctorReport_SQLite(String status) {
        ArrayList<DoctorARModel> docList = new ArrayList<>();
        SQLiteDatabase database = dbHelperMain.getWritableDatabase();

        String docMasterQuerynll = "Select * from tblDoctorReport";
        String docMasterQuery = "Select * from tblDoctorReport where ActionStatus='" + status + "'";

        try {
            Cursor cursor;
            if (status.isEmpty()) {
                cursor = database.rawQuery(docMasterQuerynll, null);
            } else {
                cursor = database.rawQuery(docMasterQuery, null);
            }
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    DoctorARModel docinfoInfo = new DoctorARModel();
                    docinfoInfo.setCreatedAt(cursor.getString(cursor.getColumnIndex("createdAt")));
                    docinfoInfo.setDoctorCode(cursor.getString(cursor.getColumnIndex("DoctorCode")));
                    docinfoInfo.setActionStatus(cursor.getString(cursor.getColumnIndex("ActionStatus")));
                    docinfoInfo.setMarketName(cursor.getString(cursor.getColumnIndex("MarketName")));
                    docinfoInfo.setWatingEmployee(cursor.getString(cursor.getColumnIndex("WatingEmployee")));
                    docinfoInfo.setWaitingRole(cursor.getString(cursor.getColumnIndex("WaitingRole")));
                    docList.add(docinfoInfo);
                }
            }

        } catch (Exception exception) {
            Log.e("MIO", exception.toString());
            exception.printStackTrace();
        }
        return docList;
    }

    @SuppressLint("Range")
    public ArrayList<DoctorARModel> getDoctorReportFilter_SQLite(String Gid, String Rid, String Aid, String Tid, String STid, String Mid, String status, String proId, String docId,String docStatus, String smcId) {
        ArrayList<DoctorARModel> docList = new ArrayList<>();
        SQLiteDatabase database = dbHelperMain.getWritableDatabase();
        String docMasterQuerynll = "Select * from tblDoctorReport";

        String parameter = " WHERE DoctorCode is not null ";
        if (!status.equals("Select")) {
            parameter = parameter + " AND  ActionStatus='" + status + "'";
        }
        if (!docStatus.equals("Select")) {
            parameter = parameter + " AND  DoctorStatus='" + docStatus + "'";
        }
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

        if (!proId.equals("0")) {
            parameter = parameter + " AND  MarketId=" + proId;
        }
        if (!docId.equals("0")) {
            parameter = parameter + " AND  MarketId=" + docId;
        }
        if (!smcId.equals("0")) {
            parameter = parameter + " AND  MarketId=" + smcId;
        }

        String mainparam = docMasterQuerynll + parameter;
        try {
            Cursor cursor;
            cursor = database.rawQuery(mainparam, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    DoctorARModel docinfoInfo = new DoctorARModel();
                    docinfoInfo.setCreatedAt(cursor.getString(cursor.getColumnIndex("createdAt")));
                    docinfoInfo.setDoctorCode(cursor.getString(cursor.getColumnIndex("DoctorCode")));
                    docinfoInfo.setActionStatus(cursor.getString(cursor.getColumnIndex("ActionStatus")));
                    docinfoInfo.setStatus(cursor.getString(cursor.getColumnIndex("DoctorStatus")));
                    docinfoInfo.setMarketName(cursor.getString(cursor.getColumnIndex("MarketName")));
                    docinfoInfo.setWatingEmployee(cursor.getString(cursor.getColumnIndex("WatingEmployee")));
                    docinfoInfo.setWaitingRole(cursor.getString(cursor.getColumnIndex("WaitingRole")));
                    docList.add(docinfoInfo);
                }
            }

        } catch (Exception exception) {
            Log.e("MIO", exception.getMessage().toString());
            exception.printStackTrace();
        }
        return docList;
    }

    @SuppressLint("Range")
    public ArrayList<DoctorListViewModel> getDoctorListFilterSQLite(String Gid, String Rid, String Aid, String Tid, String STid, String Mid, String protypeId, String doctypeId, String smctypeId) {

        ArrayList<DoctorListViewModel> docList = new ArrayList<>();
        SQLiteDatabase database = dbHelperMain.getWritableDatabase();
        String docQuery = "Select * from tblDoctorInfo";
        String parameter = " WHERE DoctorCode is not null ";

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
        if (!protypeId.equals("0")) {
            parameter = parameter + " AND  ProgramTypeId=" + protypeId;
        }
        if (!doctypeId.equals("0")) {
            parameter = parameter + " AND  DoctorTypeId=" + doctypeId;
        }
        if (!smctypeId.equals("0")) {
            parameter = parameter + " AND  SMCTypeId=" + smctypeId;
        }

        String mainparam = docQuery + parameter;
        try {
            Cursor cursor;
            cursor = database.rawQuery(mainparam, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    DoctorListViewModel ddInfo = new DoctorListViewModel();
                    ddInfo.setDoctorId(cursor.getInt(cursor.getColumnIndex("DoctorId")));
                    ddInfo.setDoctorName(cursor.getString(cursor.getColumnIndex("DoctorName")));
                    ddInfo.setDoctorCode(cursor.getString(cursor.getColumnIndex("DoctorCode")));
                    ddInfo.setChemberName(cursor.getString(cursor.getColumnIndex("ChemberName")));
                    ddInfo.setDocContact(cursor.getString(cursor.getColumnIndex("DocContact")));
                    ddInfo.setDocTPDetailsId(cursor.getInt(cursor.getColumnIndex("DocTPDetailsId")));
                    ddInfo.setDoctorTypeName(cursor.getString(cursor.getColumnIndex("DoctorTypeName")));
                    ddInfo.setProgramTypeName(cursor.getString(cursor.getColumnIndex("ProgramTypeName")));
                    ddInfo.setMarketName(cursor.getString(cursor.getColumnIndex("MarketName")));
                    ddInfo.setMarketCode(cursor.getString(cursor.getColumnIndex("MarketCode")));
                    ddInfo.setSMCType(cursor.getString(cursor.getColumnIndex("SMCType")));
                    docList.add(ddInfo);
                }
            }

        } catch (Exception exception) {
            Log.e("DoctorDetails", exception.toString());
            exception.printStackTrace();
        }
        return docList;
    }

    @SuppressLint("Range")
    public ArrayList<DoctorListViewModel> getDoctorListFromSQLite() {

        ArrayList<DoctorListViewModel> docList = new ArrayList<>();
        SQLiteDatabase database = dbHelperMain.getWritableDatabase();
        String docQuery = "Select * from tblDoctorInfo";

        try {
            Cursor cursor;
            cursor = database.rawQuery(docQuery, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    DoctorListViewModel ddInfo = new DoctorListViewModel();
                    ddInfo.setDoctorId(cursor.getInt(cursor.getColumnIndex("DoctorId")));
                    ddInfo.setDoctorName(cursor.getString(cursor.getColumnIndex("DoctorName")));
                    ddInfo.setDoctorCode(cursor.getString(cursor.getColumnIndex("DoctorCode")));
                    ddInfo.setChemberName(cursor.getString(cursor.getColumnIndex("ChemberName")));
                    ddInfo.setDocContact(cursor.getString(cursor.getColumnIndex("DocContact")));
                    ddInfo.setDocTPDetailsId(cursor.getInt(cursor.getColumnIndex("DocTPDetailsId")));
                    ddInfo.setDoctorTypeName(cursor.getString(cursor.getColumnIndex("DoctorTypeName")));
                    ddInfo.setProgramTypeName(cursor.getString(cursor.getColumnIndex("ProgramTypeName")));
                    ddInfo.setMarketName(cursor.getString(cursor.getColumnIndex("MarketName")));
                    ddInfo.setMarketCode(cursor.getString(cursor.getColumnIndex("MarketCode")));
                    docList.add(ddInfo);
                }
            }

        } catch (Exception exception) {
            Log.e("DoctorDetails", exception.toString());
            exception.printStackTrace();
        }
        return docList;
    }

    @SuppressLint("Range")
    public ArrayList<DoctorBrand> getDoctorBrandListFromSQLite(int doctorId) {

        ArrayList<DoctorBrand> brandList = new ArrayList<>();
        SQLiteDatabase database = dbHelperMain.getWritableDatabase();
        String docQuery = "Select * from tblDoctorBrand where DoctorId=" + doctorId + "";

        try {
            Cursor cursor;
            cursor = database.rawQuery(docQuery, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    DoctorBrand ddInfo = new DoctorBrand();
                    ddInfo.setDoctorId(cursor.getInt(cursor.getColumnIndex("DoctorId")));
                    ddInfo.setBrandId(cursor.getInt(cursor.getColumnIndex("BrandId")));
                    ddInfo.setBrandName(cursor.getString(cursor.getColumnIndex("BrandName")));
                    brandList.add(ddInfo);
                }
            }

        } catch (Exception exception) {
            Log.e("DoctorDetails", exception.toString());
            exception.printStackTrace();
        }
        return brandList;
    }

    @SuppressLint("Range")
    public ArrayList<UserRole> getRoleListFromSQLite() {

        ArrayList<UserRole> roleList = new ArrayList<>();
        SQLiteDatabase database = dbHelperMain.getWritableDatabase();
        String desigQuery = "Select * from tbl_UserRole";
        try {
            Cursor cursor;
            cursor = database.rawQuery(desigQuery, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    UserRole ddInfo = new UserRole();
                    ddInfo.setUserRoleID(cursor.getInt(cursor.getColumnIndex("UserRoleID")));
                    ddInfo.setRoleName(cursor.getString(cursor.getColumnIndex("RoleName")));
                    roleList.add(ddInfo);
                }
            }

        } catch (Exception exception) {
            Log.e("CampOrderDetails", exception.toString());
            exception.printStackTrace();
        }
        return roleList;
    }

    @SuppressLint("Range")
    public ArrayList<UserByRole> getRolewiseUser(int id) {

        ArrayList<UserByRole> rolebyUserList = new ArrayList<>();
        SQLiteDatabase database = dbHelperMain.getWritableDatabase();
        String oderMasterQuery = "Select * from tbl_UserByRole Where UserRoleID=" + id + "";
        try {
            Cursor cursor;
            cursor = database.rawQuery(oderMasterQuery, null);

            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    UserByRole ddInfo = new UserByRole();
                    ddInfo.setEmpInfoId(cursor.getInt(cursor.getColumnIndex("EmpInfoId")));
                    ddInfo.setEmpName(cursor.getString(cursor.getColumnIndex("EmpName")));
                    ddInfo.setEmpMasterCode(cursor.getString(cursor.getColumnIndex("EmpMasterCode")));
                    ddInfo.setUserRoleID(cursor.getInt(cursor.getColumnIndex("UserRoleID")));
                    rolebyUserList.add(ddInfo);
                }
            }

        } catch (Exception exception) {
            Log.e("CampOrderDetails", exception.toString());
            exception.printStackTrace();
        }
        return rolebyUserList;
    }

    @SuppressLint("Range")
    public ArrayList<DoctorDesignation> getDesigListFromSQLite(int id) {

        ArrayList<DoctorDesignation> deigList = new ArrayList<>();
        SQLiteDatabase database = dbHelperMain.getWritableDatabase();
        String desigQuery = "Select * from tblDoctor_Desig order by DesignationId desc";
        String oderMasterQuery = "Select * from tblDoctor_Desig Where DesignationId=" + id + "";
        try {
            Cursor cursor;

            if (id == 0 || String.valueOf(id).isEmpty()) {
                cursor = database.rawQuery(desigQuery, null);
            } else {
                cursor = database.rawQuery(oderMasterQuery, null);
            }

            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    DoctorDesignation ddInfo = new DoctorDesignation();
                    ddInfo.setDesignationId(cursor.getInt(cursor.getColumnIndex("DesignationId")));
                    ddInfo.setDesignationName(cursor.getString(cursor.getColumnIndex("DesignationName")));
                    deigList.add(ddInfo);
                }
            }

        } catch (Exception exception) {
            Log.e("CampOrderDetails", exception.toString());
            exception.printStackTrace();
        }
        return deigList;
    }

    @SuppressLint("Range")
    public ArrayList<DoctorDegreeViewModel> getDegreeListFromSQLite(int docTypeId) {

        ArrayList<DoctorDegreeViewModel> degList = new ArrayList<>();
        SQLiteDatabase database = dbHelperMain.getWritableDatabase();
        String desigQuery = "Select * from tblDoctor_Degree where DoctorTypeId='" + docTypeId + "' order by DegreeId desc";

        try {
            Cursor cursor;
            cursor = database.rawQuery(desigQuery, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    DoctorDegreeViewModel ddInfo = new DoctorDegreeViewModel();
                    ddInfo.setDegreeId(cursor.getInt(cursor.getColumnIndex("DegreeId")));
                    ddInfo.setDegreeName(cursor.getString(cursor.getColumnIndex("DegreeName")));
                    ddInfo.setDoctorTypeId(cursor.getInt(cursor.getColumnIndex("DoctorTypeId")));
                    degList.add(ddInfo);
                }
            }

        } catch (Exception exception) {
            Log.e("CampOrderDetails", exception.toString());
            exception.printStackTrace();
        }
        return degList;
    }

    @SuppressLint("Range")
    public ArrayList<DoctorSpecialityViewModel> getSpecListFromSQLite() {

        ArrayList<DoctorSpecialityViewModel> spcList = new ArrayList<>();
        SQLiteDatabase database = dbHelperMain.getWritableDatabase();
        String desigQuery = "Select * from tblDoctor_Speciality order by SpecialityId desc";

        try {
            Cursor cursor;
            cursor = database.rawQuery(desigQuery, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    DoctorSpecialityViewModel ddInfo = new DoctorSpecialityViewModel();
                    ddInfo.setSpecialityId(cursor.getInt(cursor.getColumnIndex("SpecialityId")));
                    ddInfo.setSpecialityName(cursor.getString(cursor.getColumnIndex("SpecialityName")));
                    spcList.add(ddInfo);
                }
            }

        } catch (Exception exception) {
            Log.e("CampOrderDetails", exception.toString());
            exception.printStackTrace();
        }
        return spcList;
    }

    @SuppressLint("Range")
    public ArrayList<DoctorTypeVM> getDoctorTypeListFromSQLite(int id) {

        ArrayList<DoctorTypeVM> typeList = new ArrayList<>();
        SQLiteDatabase database = dbHelperMain.getWritableDatabase();
        String desigQuery = "Select * from tblDoctor_Type order by DoctorTypeId desc";
        String oderMasterQuery = "Select * from tblDoctor_Type Where DoctorTypeId=" + id + "";
        try {
            Cursor cursor;
            if (id == 0 || String.valueOf(id).isEmpty()) {
                cursor = database.rawQuery(desigQuery, null);
            } else {
                cursor = database.rawQuery(oderMasterQuery, null);
            }

            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    DoctorTypeVM ddInfo = new DoctorTypeVM();
                    ddInfo.setDoctorTypeId(cursor.getInt(cursor.getColumnIndex("DoctorTypeId")));
                    ddInfo.setDoctorTypeName(cursor.getString(cursor.getColumnIndex("DoctorTypeName")));
                    typeList.add(ddInfo);
                }
            }

        } catch (Exception exception) {
            Log.e("CampOrderDetails", exception.toString());
            exception.printStackTrace();
        }
        return typeList;
    }

    @SuppressLint("Range")
    public ArrayList<ProgramType> getProgramTypeListFromSQLite(int id) {
        ArrayList<ProgramType> typeList = new ArrayList<>();
        SQLiteDatabase database = dbHelperMain.getWritableDatabase();
        String oderMasterQuerynll = "Select * from tblProgram_Type order by ProgramTypeId desc";
        String oderMasterQuery = "Select * from tblProgram_Type Where ProgramTypeId=" + id + "";
        try {
            Cursor cursor;
            if (id == 0 || String.valueOf(id).isEmpty()) {
                //  cursor = database.rawQuery(oderMasterQuerynll, null);
                cursor = database.rawQuery(oderMasterQuerynll, null);
            } else {
                cursor = database.rawQuery(oderMasterQuery, null);
            }
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    ProgramType ddInfo = new ProgramType();
                    ddInfo.setProgramTypeId(cursor.getInt(cursor.getColumnIndex("ProgramTypeId")));
                    ddInfo.setProgramType(cursor.getString(cursor.getColumnIndex("ProgramType")));
                    typeList.add(ddInfo);
                }
            }

        } catch (Exception exception) {
            Log.e("CampOrderDetails", exception.toString());
            exception.printStackTrace();
        }
        return typeList;
    }

    @SuppressLint("Range")
    public ArrayList<ModelProviderType> getProviderTypeListFromSQLite(int id, int fod) {
        // forDoctor
        ArrayList<ModelProviderType> typeList = new ArrayList<>();
        SQLiteDatabase database = dbHelperMain.getWritableDatabase();
        String oderMasterQuerynll = "Select * from tblProvider_Type Where forDoctor =" + fod + "";
        String oderMasterQuery = "Select * from tblProvider_Type Where ProviderTypeId=" + id + "";
        try {
            Cursor cursor;
            if (id == 0 || String.valueOf(id).isEmpty()) {
                //  cursor = database.rawQuery(oderMasterQuerynll, null);
                cursor = database.rawQuery(oderMasterQuerynll, null);
            } else {
                cursor = database.rawQuery(oderMasterQuery, null);
            }
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    ModelProviderType ddInfo = new ModelProviderType();
                    ddInfo.setProviderTypeId(cursor.getInt(cursor.getColumnIndex("ProviderTypeId")));
                    ddInfo.setProviderType(cursor.getString(cursor.getColumnIndex("ProviderType")));
                    typeList.add(ddInfo);
                }
            }

        } catch (Exception exception) {
            Log.e("CampOrderDetails", exception.toString());
            exception.printStackTrace();
        }
        return typeList;
    }

    @SuppressLint("Range")
    public ArrayList<ModelProviderType> getProviderTypeListForCustFromSQLite(int id, int fos) {
        // forCustomer
        ArrayList<ModelProviderType> typeList = new ArrayList<>();
        SQLiteDatabase database = dbHelperMain.getWritableDatabase();
        String oderMasterQuerynll = "Select * from tblProvider_Type Where forCustomer =" + fos + "";
        String oderMasterQuery = "Select * from tblProvider_Type Where ProviderTypeId=" + id + "";
        try {
            Cursor cursor;
            if (id == 0 || String.valueOf(id).isEmpty()) {
                //  cursor = database.rawQuery(oderMasterQuerynll, null);
                cursor = database.rawQuery(oderMasterQuerynll, null);
            } else {
                cursor = database.rawQuery(oderMasterQuery, null);
            }
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    ModelProviderType ddInfo = new ModelProviderType();
                    ddInfo.setProviderTypeId(cursor.getInt(cursor.getColumnIndex("ProviderTypeId")));
                    ddInfo.setProviderType(cursor.getString(cursor.getColumnIndex("ProviderType")));
                    typeList.add(ddInfo);
                }
            }

        } catch (Exception exception) {
            Log.e("CampOrderDetails", exception.toString());
            exception.printStackTrace();
        }
        return typeList;
    }

    @SuppressLint("Range")
    public ArrayList<ModelSMCType> getSMCTypeListForCustFromSQLite(int id, int fos) {
        // forCustomer
        ArrayList<ModelSMCType> typeList = new ArrayList<>();
        SQLiteDatabase database = dbHelperMain.getWritableDatabase();
        String oderMasterQuerynll = "Select * from tblSMCType Where forCustomer =" + fos + "";
        String oderMasterQuery = "Select * from tblSMCType Where SMCTypeId=" + id + "";
        try {
            Cursor cursor;
            if (id == 0 || String.valueOf(id).isEmpty()) {
                //  cursor = database.rawQuery(oderMasterQuerynll, null);
                cursor = database.rawQuery(oderMasterQuerynll, null);
            } else {
                cursor = database.rawQuery(oderMasterQuery, null);
            }
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    ModelSMCType ddInfo = new ModelSMCType();
                    ddInfo.setSMCTypeId(cursor.getInt(cursor.getColumnIndex("SMCTypeId")));
                    ddInfo.setSMCType(cursor.getString(cursor.getColumnIndex("SMCType")));
                    typeList.add(ddInfo);
                }
            }

        } catch (Exception exception) {
            Log.e("CampOrderDetails", exception.toString());
            exception.printStackTrace();
        }
        return typeList;
    }

    @SuppressLint("Range")
    public ArrayList<ModelSMCType> getSMCTypeListForDocFromSQLite(int id, int fos) {
        // forCustomer
        ArrayList<ModelSMCType> typeList = new ArrayList<>();
        SQLiteDatabase database = dbHelperMain.getWritableDatabase();
        String oderMasterQuerynll = "Select * from tblSMCType Where forDoctor =" + fos + "";
        String oderMasterQuery = "Select * from tblSMCType Where SMCTypeId=" + id + "";
        try {
            Cursor cursor;
            if (id == 0 || String.valueOf(id).isEmpty()) {
                //  cursor = database.rawQuery(oderMasterQuerynll, null);
                cursor = database.rawQuery(oderMasterQuerynll, null);
            } else {
                cursor = database.rawQuery(oderMasterQuery, null);
            }
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    ModelSMCType ddInfo = new ModelSMCType();
                    ddInfo.setSMCTypeId(cursor.getInt(cursor.getColumnIndex("SMCTypeId")));
                    ddInfo.setSMCType(cursor.getString(cursor.getColumnIndex("SMCType")));
                    typeList.add(ddInfo);
                }
            }

        } catch (Exception exception) {
            Log.e("CampOrderDetails", exception.toString());
            exception.printStackTrace();
        }
        return typeList;
    }

    @SuppressLint("Range")
    public ArrayList<InstitutionVM> getInstitutionListFromSQLite() {
        ArrayList<InstitutionVM> instList = new ArrayList<>();
        SQLiteDatabase database = dbHelperMain.getWritableDatabase();
        String desigQuery = "Select * from tblDoctor_Institution order by InstitutionId desc";

        try {
            Cursor cursor;
            cursor = database.rawQuery(desigQuery, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    InstitutionVM ddInfo = new InstitutionVM();
                    ddInfo.setInstitutionId(cursor.getInt(cursor.getColumnIndex("InstitutionId")));
                    ddInfo.setInstitution(cursor.getString(cursor.getColumnIndex("Institution")));
                    instList.add(ddInfo);
                }
            }

        } catch (Exception exception) {
            Log.e("CampOrderDetails", exception.toString());
            exception.printStackTrace();
        }
        return instList;
    }

    @SuppressLint("Range")
    public ArrayList<DoctorChamberTypeVM> getChamberListFromSQLite(int id) {
        ArrayList<DoctorChamberTypeVM> chamberList = new ArrayList<>();
        SQLiteDatabase database = dbHelperMain.getWritableDatabase();
        String desigQuery = "Select * from tblDoctor_Chembar order by ChamberTypeId desc";
        String oderMasterQuery = "Select * from tblDoctor_Chembar Where ChamberTypeId=" + id + "";
        try {
            Cursor cursor;
            if (id == 0 || String.valueOf(id).isEmpty()) {
                cursor = database.rawQuery(desigQuery, null);
            } else {
                cursor = database.rawQuery(oderMasterQuery, null);
            }
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    DoctorChamberTypeVM ddInfo = new DoctorChamberTypeVM();
                    ddInfo.setChamberTypeId(cursor.getInt(cursor.getColumnIndex("ChamberTypeId")));
                    ddInfo.setChamberTypeName(cursor.getString(cursor.getColumnIndex("ChamberTypeName")));
                    chamberList.add(ddInfo);
                }
            }

        } catch (Exception exception) {
            Log.e("CampOrderDetails", exception.toString());
            exception.printStackTrace();
        }
        return chamberList;
    }

    @SuppressLint("Range")
    public ArrayList<DoctorChamberName> getChamberIdListFromSQLite(int docId) {
        ArrayList<DoctorChamberName> chamberList = new ArrayList<>();
        SQLiteDatabase database = dbHelperMain.getWritableDatabase();
        String desigQuery = "Select * from tblDoctor_ChembarName where DoctorId='" + docId + "' order by ChemberId desc";

        try {
            Cursor cursor;
            cursor = database.rawQuery(desigQuery, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    DoctorChamberName ddInfo = new DoctorChamberName();
                    ddInfo.setChemberId(cursor.getInt(cursor.getColumnIndex("ChemberId")));
                    ddInfo.setChemberName(cursor.getString(cursor.getColumnIndex("ChemberName")));

                    chamberList.add(ddInfo);
                }
            }

        } catch (Exception exception) {
            Log.e("CampOrderDetails", exception.toString());
            exception.printStackTrace();
        }
        return chamberList;
    }

    @SuppressLint("Range")
    public ArrayList<TourTypeViewModel> getVisitTypeIdListFromSQLite() {

        ArrayList<TourTypeViewModel> visitTypeList = new ArrayList<>();
        SQLiteDatabase database = dbHelperMain.getWritableDatabase();
        String desigQuery = "Select * from tblVisit_Type";

        try {
            Cursor cursor;
            cursor = database.rawQuery(desigQuery, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    TourTypeViewModel ddInfo = new TourTypeViewModel();
                    ddInfo.setTourTypeId(cursor.getInt(cursor.getColumnIndex("TourTypeId")));
                    ddInfo.setTourTypeName(cursor.getString(cursor.getColumnIndex("TourTypeName")));

                    visitTypeList.add(ddInfo);
                }
            }

        } catch (Exception exception) {
            Log.e("visitTypeList", exception.toString());
            exception.printStackTrace();
        }
        return visitTypeList;
    }

    @SuppressLint("Range")
    public ArrayList<Brand> getBrandListFromSQLite() {

        ArrayList<Brand> brandList = new ArrayList<>();
        SQLiteDatabase database = dbHelperMain.getWritableDatabase();
        String desigQuery = "Select * from tblBrandInfo order by ProductBrandId desc";

        try {
            Cursor cursor;
            cursor = database.rawQuery(desigQuery, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    Brand ddInfo = new Brand();
                    ddInfo.setProductBrandId(cursor.getInt(cursor.getColumnIndex("ProductBrandId")));
                    ddInfo.setProductSQName(cursor.getString(cursor.getColumnIndex("ProductSQName")));
                    ddInfo.setMaxValue(cursor.getInt(cursor.getColumnIndex("MaxValue")));
                    brandList.add(ddInfo);
                }
            }

        } catch (Exception exception) {
            Log.e("CampOrderDetails", exception.toString());
            exception.printStackTrace();
        }
        return brandList;
    }

    @SuppressLint("Range")
    public int maxBrandVal() {
        ArrayList<Brand> brandList = new ArrayList<>();
        Brand brandz = new Brand();
        int maxBrandVal = 0;
        SQLiteDatabase database = dbHelperMain.getWritableDatabase();
        //String desigQuery = "Select * from tblBrandInfo order by ProductBrandId desc";
        String brandmaxQuery = "SELECT  DISTINCT MaxValue FROM tblBrandInfo ORDER BY ProductBrandId";
        try {
            Cursor cursor;
            //  cursor = database.rawQuery(desigQuery, null);
            cursor = database.rawQuery(brandmaxQuery, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    // brandz.setMaxValue(cursor.getInt(cursor.getColumnIndex("MaxValue")));
                    maxBrandVal = cursor.getInt(cursor.getColumnIndex("MaxValue"));
                }
            }

        } catch (Exception exception) {
            Log.e("CampOrderDetails", exception.toString());
            exception.printStackTrace();
        }
        return maxBrandVal;
    }

    @SuppressLint("Range")
    public ArrayList<DoctorCategory> getDocCategoryListFromSQLite(int id) {

        ArrayList<DoctorCategory> doccateList = new ArrayList<>();
        SQLiteDatabase database = dbHelperMain.getWritableDatabase();
        String desigQuery = "Select * from tblDoctorCategory order by CategoryId desc";
        String oderMasterQuery = "Select * from tblDoctorCategory Where CategoryId=" + id + "";
        try {
            Cursor cursor;
            if (id == 0 || String.valueOf(id).isEmpty()) {
                cursor = database.rawQuery(desigQuery, null);
            } else {
                cursor = database.rawQuery(oderMasterQuery, null);
            }
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    DoctorCategory ddInfo = new DoctorCategory();
                    ddInfo.setCategoryId(cursor.getInt(cursor.getColumnIndex("CategoryId")));
                    ddInfo.setCategoryName(cursor.getString(cursor.getColumnIndex("CategoryName")));
                    doccateList.add(ddInfo);
                }
            }

        } catch (Exception exception) {
            Log.e("CampOrderDetails", exception.toString());
            exception.printStackTrace();
        }
        return doccateList;
    }

    @SuppressLint("Range")
    public ArrayList<ContactTypeVM> getContactTypeListFromSQLite(int id) {
        ArrayList<ContactTypeVM> conTypeList = new ArrayList<>();
        SQLiteDatabase database = dbHelperMain.getWritableDatabase();
        String desigQuery = "Select * from tblContact_Type order by ContactTypeId desc";
        String oderMasterQuery = "Select * from tblContact_Type Where ContactTypeId=" + id + "";
        try {
            Cursor cursor;
            if (id == 0 || String.valueOf(id).isEmpty()) {
                cursor = database.rawQuery(desigQuery, null);
            } else {
                cursor = database.rawQuery(oderMasterQuery, null);
            }
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    ContactTypeVM ddInfo = new ContactTypeVM();
                    ddInfo.setContactTypeId(cursor.getInt(cursor.getColumnIndex("ContactTypeId")));
                    ddInfo.setContactType(cursor.getString(cursor.getColumnIndex("ContactType")));
                    conTypeList.add(ddInfo);
                }
            }

        } catch (Exception exception) {
            Log.e("CampOrderDetails", exception.toString());
            exception.printStackTrace();
        }
        return conTypeList;
    }

    @SuppressLint("Range")
    public ArrayList<SpecialDay> getSpecialdayTypeListFromSQLite(int id) {

        ArrayList<SpecialDay> sdTypeList = new ArrayList<>();
        SQLiteDatabase database = dbHelperMain.getWritableDatabase();
        String desigQuery = "Select * from tblDoctor_Specialday order by SpecialDayId desc";
        String oderMasterQuery = "Select * from tblDoctor_Specialday Where SpecialDayId=" + id + "";

        try {
            Cursor cursor;
            if (id == 0 || String.valueOf(id).isEmpty()) {
                cursor = database.rawQuery(desigQuery, null);
            } else {
                cursor = database.rawQuery(oderMasterQuery, null);
            }
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    SpecialDay ddInfo = new SpecialDay();
                    ddInfo.setSpecialDayId(cursor.getInt(cursor.getColumnIndex("SpecialDayId")));
                    ddInfo.setSpecialDay(cursor.getString(cursor.getColumnIndex("SpecialDay")));
                    sdTypeList.add(ddInfo);
                }
            }

        } catch (Exception exception) {
            Log.e("CampOrderDetails", exception.toString());
            exception.printStackTrace();
        }
        return sdTypeList;
    }
}
