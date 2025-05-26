package com.creatrix.salessolution.Activity.Doctor.TourePlan.TP;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.creatrix.salessolution.Activity.Doctor.TourePlan.Adapter.CheckedCustomerItem;
import com.creatrix.salessolution.Activity.Doctor.TourePlan.Adapter.CustomerItemChkAdapter;
import com.creatrix.salessolution.Activity.Doctor.TourePlan.TourPlanDetailsActivity;
import com.creatrix.salessolution.Activity.Doctor.TourePlan.TourPlanDtailsaAdapter;
import com.creatrix.salessolution.DBAdapter.DBCrudHelper;
import com.creatrix.salessolution.Interface.DeleteListener;
import com.creatrix.salessolution.Interface.IMarketStracture;
import com.creatrix.salessolution.Interface.ITourplan;
import com.creatrix.salessolution.Model.Customer;
import com.creatrix.salessolution.Model.MarketStructure.StructureTable.Area;
import com.creatrix.salessolution.Model.MarketStructure.StructureTable.Group;
import com.creatrix.salessolution.Model.MarketStructure.StructureTable.Market;
import com.creatrix.salessolution.Model.MarketStructure.StructureTable.Region;
import com.creatrix.salessolution.Model.MarketStructure.StructureTable.SubTeritorry;
import com.creatrix.salessolution.Model.MarketStructure.StructureTable.Teritorry;
import com.creatrix.salessolution.Model.MonthDate;
import com.creatrix.salessolution.Model.TourPlanViewModel;
import com.creatrix.salessolution.Model.TourPurposeViewModel;
import com.creatrix.salessolution.Presenter.MarketStructurePresenter;
import com.creatrix.salessolution.Presenter.TourPlanPresenter;
import com.creatrix.salessolution.UtilityHelper.SessionManagement;
import com.creatrix.salessolution.databinding.ActivityTpdetailsBinding;
import com.creatrix.salessolution.databinding.PopTourplanAddMarketwiseBinding;
import com.creatrix.salessolution.databinding.PopTourplanEditMarketwiseBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ActivityTPDetails extends AppCompatActivity implements IMarketStracture.View, ITourplan.View, DeleteListener, CheckedCustomerItem {
ActivityTpdetailsBinding binding;
    //Popup Layout
    PopTourplanAddMarketwiseBinding pbinding;
    PopTourplanEditMarketwiseBinding pbindingEdit;
    int selectedRegionId, selectedAreaId, selectedTeriId, selectedSTeri, selectedMarket, selectedTPP, empId, SN;
    String selectedMarketName, selectedTPPName;
    MarketStructurePresenter mpresenter;
    TourPlanPresenter tppresenter;
    String roleType;

    SessionManagement session;
    HashMap<String, String> user;

    TourPlanDtailsaAdapter adapter;
    SimpleDateFormat dateFormat;
    String ddy;
    Dialog popupTPP, popupTPPEdit;
    DBCrudHelper dbCrudHelper;
    List<TourPurposeViewModel> tppList;
    MonthDate monthDatel;
    List<TourPlanViewModel> tpl = new ArrayList<>();


    List<Customer> aCustomerList = new ArrayList<>();
    List<Customer> chkCustomerList = new ArrayList<>();
    String[] listItemCustomer_Customer;
    boolean[] checkedItems_Customer;
    ArrayList<Integer> mUserItems_Cust = new ArrayList<>();


    CustomerItemChkAdapter itemChkAdapter;
    Dialog popupCustomer;
    RecyclerView rv_customer;
    EditText srchview;
    TextView done_cust, cancel_cust, title;
    int a, b, c;
    String currentdate, edit_dName, Tedit_dName;


    private callbackHitApi_TP callback;
    public void setCallback(callbackHitApi_TP callback) {
        this.callback = callback;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityTpdetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        dbCrudHelper = new DBCrudHelper(ActivityTPDetails.this);
        mpresenter = new MarketStructurePresenter(this, this);
        tppresenter = new TourPlanPresenter(this, this);

        session = new SessionManagement(getApplicationContext());
        user = session.getUserDetails();

        empId = Integer.parseInt(user.get(SessionManagement.KEY_EmpId));
        roleType = user.get(SessionManagement.KEY_EmpRoleType);
    }
    public interface callbackHitApi_TP{
        void onCallBackTP(String month,String year);
    }
    @Override
    public void ckdItemName(List<Customer> st, int Pos) {

    }

    @Override
    public void unckdItemName(List<Customer> st, int Pos) {

    }

    @Override
    public void deleteItemFromServer(int pos, int id) {

    }

    @Override
    public void deleteItem(int pos) {

    }

    @Override
    public void editItem(int pos, int id, int rid, int aid, int tid, int stid, int mid, String region, String area, String territory, String subTerritory, String market) {

    }
 @Override
    public void editTourPlanInfo(int pos, int id) {

    }

    @Override
    public void vGroup(List<Group> groupList) {

    }

    @Override
    public void vRegion(List<Region> regionList) {

    }

    @Override
    public void vArea(List<Area> areaList) {

    }

    @Override
    public void vTeritory(List<Teritorry> teritoryList) {

    }

    @Override
    public void vSTeritory(List<SubTeritorry> steritoryList) {

    }

    @Override
    public void vMarket(List<Market> marketList) {

    }

    @Override
    public void OnTourPlanDataGet(List<TourPlanViewModel> aList) {

    }

    @Override
    public void OnTourPlanDailyDataGet(List<MonthDate> aList) {

    }

    @Override
    public void OnArreangList(List<MonthDate> aMondateList, boolean is_Entry, List<TourPlanViewModel> aTpLIst) {

    }

    @Override
    public void OnFailour(String msg) {

    }

    @Override
    public void OnSuccessTPPDay(String msg) {

    }
}