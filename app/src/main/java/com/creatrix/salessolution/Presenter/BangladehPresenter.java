package com.creatrix.salessolution.Presenter;

import android.content.Context;

import com.creatrix.salessolution.DBAdapter.DBDDTU.DBDDTUHelper;
import com.creatrix.salessolution.Interface.IBangladesh;
import com.creatrix.salessolution.Interface.IMarketStracture;
import com.creatrix.salessolution.Model.DistrictVM;
import com.creatrix.salessolution.Model.DivisionVM;
import com.creatrix.salessolution.Model.ThanaVM;

import java.util.List;

public class BangladehPresenter implements IBangladesh.Presenter {
    IBangladesh.View view;
    Context context;
    DBDDTUHelper dbddtuHelper;

    public BangladehPresenter(IBangladesh.View view, Context context) {
        this.view = view;
        this.context = context;
        dbddtuHelper=new DBDDTUHelper(context);
    }


    @Override
    public void GetDivisionLocal() {
        try {
            view.vDivL(dbddtuHelper.getDivList_SQLite());
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void GetDistrictLocal(int divid) {
        try {
            view.vDisL(dbddtuHelper.getDisByIdList_SQLite(divid));
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void GetThanaLocal(int disid) {
        try {
            view.vThanaL(dbddtuHelper.getThanaByIdList_SQLite(disid));
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
