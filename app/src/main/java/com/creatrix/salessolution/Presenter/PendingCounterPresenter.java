package com.creatrix.salessolution.Presenter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;

import com.creatrix.salessolution.DBAdapter.DBCrudHelper;
import com.creatrix.salessolution.DBInfo.DBHelperMain;
import com.creatrix.salessolution.Interface.IPendingCounter;
import com.creatrix.salessolution.Model.DcrSM;

public class PendingCounterPresenter implements IPendingCounter.Presenter {
    IPendingCounter.View view;
    Context context;
    Activity activity;
    ProgressDialog progressDoalog;
    DBHelperMain dbHelper;
    DBCrudHelper crudHelper;
    public PendingCounterPresenter(Activity activity) {
        this.activity = activity;
        this.dbHelper = new DBHelperMain(context);
        this.crudHelper = new DBCrudHelper(context);
    }
    public PendingCounterPresenter(IPendingCounter.View view, Context context) {
        this.view = view;
        this.context = context;
        this.dbHelper = new DBHelperMain(context);
        this.crudHelper = new DBCrudHelper(context);
    }

    @Override
    public void totalDcr() {
        try {
            view.totalDcr(crudHelper.GetDcrInfoFromDB());
            dbHelper.close();
        } catch (Exception exception) {
            exception.printStackTrace();
        }

    }

    @Override
    public void totalPresc() {
         view.totalPresc(crudHelper.GetPrescriptionInfoFromDB());
    }

    @Override
    public void totalSample() {

    }

    @Override
    public void totalOrder() {

    }

    @Override
    public void totalOrderMaster() {
        view.totalOrderMaster(crudHelper.getOrderListFromSQLite());
        dbHelper.close();
    }
}
