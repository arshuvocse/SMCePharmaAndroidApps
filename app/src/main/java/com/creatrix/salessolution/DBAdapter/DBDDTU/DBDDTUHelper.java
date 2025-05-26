package com.creatrix.salessolution.DBAdapter.DBDDTU;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import androidx.annotation.IntRange;

import com.creatrix.salessolution.DBInfo.DBHelperMain;
import com.creatrix.salessolution.Model.DistrictVM;
import com.creatrix.salessolution.Model.DivisionVM;
import com.creatrix.salessolution.Model.ThanaVM;
import com.creatrix.salessolution.UtilityHelper.SessionManagement;

import java.util.ArrayList;
import java.util.List;

public class DBDDTUHelper {
    DBHelperMain dbHelperMain;
    Context context;
    SessionManagement sessionManagement;

    public DBDDTUHelper(Context context) {
        this.context = context;
        dbHelperMain = new DBHelperMain(context);
    }

    @SuppressLint("Range")
    public List<DivisionVM> getDivList_SQLite() {

        ArrayList<DivisionVM> divList = new ArrayList<>();
        SQLiteDatabase database = dbHelperMain.getWritableDatabase();
        String oderMasterQuery = "Select * from tblDivisionInfo";

        try {
            Cursor cursor;
            cursor = database.rawQuery(oderMasterQuery, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    DivisionVM divInfo = new DivisionVM();
                    divInfo.setDivisionId(cursor.getInt(cursor.getColumnIndex("DivisionId")));
                    divInfo.setDivisionName(cursor.getString(cursor.getColumnIndex("DivisionName")));
                    divList.add(divInfo);
                }
            }

        } catch (Exception exception) {
            Log.e("MIO", exception.toString());
            exception.printStackTrace();
        }
        return divList;
    }

   // @IntRange(from = 0)
    //int getColumnIndex;
    @SuppressLint("Range")
    public List<DistrictVM> getDisByIdList_SQLite(int divid) {
        List<DistrictVM> disList = new ArrayList<>();
        SQLiteDatabase database = dbHelperMain.getWritableDatabase();
        String oderMasterQuerynll = "Select * from tblDistrictInfo";
        String oderMasterQuery = "Select * from tblDistrictInfo Where DivisionId=" + divid + "";

        try {
            Cursor cursor;
            if (String.valueOf(divid).isEmpty()) {
                cursor = database.rawQuery(oderMasterQuerynll, null);
            } else {
                cursor = database.rawQuery(oderMasterQuery, null);
            }
            // cursor = database.rawQuery(oderMasterQuery, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    DistrictVM disInfo = new DistrictVM();
                    disInfo.setDistrictId(cursor.getInt(cursor.getColumnIndex("DistrictId")));
                    disInfo.setDistrictName(cursor.getString(cursor.getColumnIndex("DistrictName")));
                    disList.add(disInfo);
                }
            }

        } catch (Exception exception) {
            Log.e("MIO", exception.toString());
            exception.printStackTrace();
        }
        return disList;
    }

    @SuppressLint("Range")
    public List<ThanaVM> getThanaByIdList_SQLite(int disid) {
        List<ThanaVM> thanaList = new ArrayList<>();
        SQLiteDatabase database = dbHelperMain.getWritableDatabase();
        String oderMasterQuerynll = "Select * from tblThanaInfo";
        String oderMasterQuery = "Select * from tblThanaInfo Where district_id=" + disid + "";

        try {
            Cursor cursor;
            if (String.valueOf(disid).isEmpty()) {
                cursor = database.rawQuery(oderMasterQuerynll, null);
            } else {
                cursor = database.rawQuery(oderMasterQuery, null);
            }
            // cursor = database.rawQuery(oderMasterQuery, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    ThanaVM thanaInfo = new ThanaVM();
                    thanaInfo.setThanaId(cursor.getInt(cursor.getColumnIndex("ThanaId")));
                    thanaInfo.setThanaName(cursor.getString(cursor.getColumnIndex("ThanaName")));
                    thanaList.add(thanaInfo);
                }
            }

        } catch (Exception exception) {
            Log.e("MIO", exception.toString());
            exception.printStackTrace();
        }
        return thanaList;
    }
}
