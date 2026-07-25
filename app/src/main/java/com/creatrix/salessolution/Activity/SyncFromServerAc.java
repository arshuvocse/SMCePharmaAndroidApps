package com.creatrix.salessolution.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.creatrix.salessolution.Activity.Doctor.DCR.NonEffectiveReason;
import com.creatrix.salessolution.DBAdapter.DBCrudHelper;
import com.creatrix.salessolution.DBAdapter.SyncDb_Helper;
import com.creatrix.salessolution.DBInfo.DBHelperMain;
import com.creatrix.salessolution.Interface.ISyncMaster;
import com.creatrix.salessolution.Model.Doctor.ContactTypeVM;
import com.creatrix.salessolution.Model.Customer;
import com.creatrix.salessolution.Model.CustomerType;
import com.creatrix.salessolution.Model.Doctor.Brand;
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
import com.creatrix.salessolution.Model.PrescriptionTYpe;
import com.creatrix.salessolution.Model.Product;
import com.creatrix.salessolution.Model.ProductSample;
import com.creatrix.salessolution.Model.QuotedPrice;
import com.creatrix.salessolution.Model.ResultInfo;
import com.creatrix.salessolution.Model.StationType;
import com.creatrix.salessolution.Model.TerritoryViewModel;
import com.creatrix.salessolution.Model.TourPurposeViewModel;
import com.creatrix.salessolution.Model.TourTypeViewModel;
import com.creatrix.salessolution.Model.Transport;
import com.creatrix.salessolution.Model.UserByRole;
import com.creatrix.salessolution.Model.UserRole;
import com.creatrix.salessolution.Network.ApiCustomerCall;
import com.creatrix.salessolution.Network.ApiDoctorCall;
import com.creatrix.salessolution.Network.ApiMasterSync;
import com.creatrix.salessolution.Network.CalculationApiCall;
import com.creatrix.salessolution.Network.GetDataService;
import com.creatrix.salessolution.Network.OrderProcessAPICALL;
import com.creatrix.salessolution.Network.ProductApi;
import com.creatrix.salessolution.Network.RetrofitClientInstance;
import com.creatrix.salessolution.Network.RetrofitClientOrderProcessInstance;
import com.creatrix.salessolution.Network.TourApiCall;
import com.creatrix.salessolution.Network.UserProcessAPI;
import com.creatrix.salessolution.Network.apiSeedDataCall;
import com.creatrix.salessolution.Presenter.SyncMasterPresenter;
import com.creatrix.salessolution.R;
import com.creatrix.salessolution.ResposeModel.ResponseInfo;
import com.creatrix.salessolution.Services.Constants;
import com.creatrix.salessolution.UtilityHelper.NetworkInformation;
import com.creatrix.salessolution.UtilityHelper.SessionManagement;
import com.creatrix.salessolution.UtilityHelper.ToastManagment;
import com.creatrix.salessolution.databinding.ActivitySyncFromServerBinding;
import com.google.gson.Gson;

import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import okhttp3.HttpUrl;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SyncFromServerAc extends AppCompatActivity implements ISyncMaster.View {
    SyncMasterPresenter spresenter;
    ProgressBar splashProgress;
    int SPLASH_TIME = 1000; //This is 3 seconds
    ActivitySyncFromServerBinding viewBindings;
    DBHelperMain dbHelperMain;
    SyncDb_Helper syncDb_helper;
    DBCrudHelper crudHelper;
    SessionManagement session;
    HashMap<String, String> user = new HashMap<>();

    //    boolean isSymptomDone = false;
    boolean isGroupDone = false;
    boolean isRegionDone = false;
    boolean isAreaDone = false;
    boolean isTeritorryDone = false;
    boolean isSubTeritorryDone = false;
    boolean isMarketDone = false;
    boolean isNSMDone = false;
    boolean isRSMDone = false;
    boolean isASMDone = false;
    boolean isMIODone = false;

    boolean isDoctorDone = false;
    boolean isDesignationDone = false;
    boolean isDoctorDegreeDone = false;
    boolean isDoctorSpecilityDone = false;
    boolean isDoctorSpecilDAyDone = false;
    boolean isDoctorTypeDone = false;
    boolean isContTypeDone = false;
    boolean isCustTypeDone = false;
    boolean isLeaveTypeDone = false;
    boolean isVisitTypeDone = false;
    boolean isDocConType = false;
    boolean isDoctorInstitutionDone = false;
    boolean isChamberType = false;
    boolean isChamberName = false;
    boolean isBrand = false;
    boolean isDocCategory = false;
    boolean isProgramType = false;


    boolean isDivisionDone = false;
    boolean isDistrictDone = false;
    boolean isThanaDone = false;

    boolean isDcrDone = false;

    boolean isTeritoryDone = false;
    //boolean isMarketDone = false;


    boolean isProductDone = false;
    boolean isSampleProductDone = false;
    boolean isGiftProductDone = false;
    boolean isquotedprice = false;

    boolean isExpenseTypeDone = false;
    boolean isCustStationDone = false;
    boolean isTransport = false;
    boolean isTourPurpose = false;
    boolean isPrescType = false;
    boolean isNoneffect = false;
    boolean isCustomerList = false;
    boolean isRoleList = false;
    boolean isRoleUser = false;
    boolean isProviderType=false;
    boolean isSMCType=false;


    String LastSyncTime;
    String CurrentYear;
    String empcode;
    String emprole;
    int empId, year;
    boolean masterDone, areaDone, customerDone, doctorDone, productDone, typeDone;

    // Sequential sync queue: one API call runs at a time, retried once on failure, then the queue moves on.
    private final List<Runnable> syncQueue = new ArrayList<>();
    private final List<String> syncStepNames = new ArrayList<>();
    private int currentStepIndex = 0;
    private int currentStepAttempt = 0;
    private static final int MAX_ATTEMPTS = 2;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_sync_from_server);
        viewBindings = ActivitySyncFromServerBinding.inflate(getLayoutInflater());
        setContentView(viewBindings.getRoot());
        spresenter = new SyncMasterPresenter(this, SyncFromServerAc.this);
        syncDb_helper = new SyncDb_Helper(SyncFromServerAc.this);
        crudHelper = new DBCrudHelper(SyncFromServerAc.this);
        // This is additional feature, used to run a progress bar
        viewBindings.splashProgress.setProgressTintList(ColorStateList.valueOf(Color.WHITE));
      /*  viewBindings.syncview.setMinFrame(20);
        viewBindings.syncview.setMaxFrame(50);
    */
        // playProgress();
        // viewBindings.syncview.playAnimation();
        if (!NetworkInformation.isConnected(this)) {
            NoInternet();
        } else {
            SessionManagement session = new SessionManagement(getApplicationContext());
            HashMap<String, String> user = session.getUserDetails();
            empId = Integer.parseInt(user.get(SessionManagement.KEY_EmpId));
            empcode = user.get(SessionManagement.KEY_EmpMasterCode);
            emprole = user.get(SessionManagement.KEY_EmpRole);
            year = Integer.parseInt(CurrentYear = new SimpleDateFormat("yyyy", Locale.getDefault()).format(new Date()));
            // RunSync(empId, year, empcode, emprole);
            callRunSync(empId, year, empcode, emprole);
        }
    }

    private void addStep(String name, Runnable r) {
        syncStepNames.add(name);
        syncQueue.add(r);
    }

    private void callRunSync(int empId, int year, String empcode, String emprole) {
        syncQueue.clear();
        syncStepNames.clear();

        addStep("Group", () -> spresenter.cllGroup(empcode, emprole));
        addStep("Doctor List", () -> spresenter.cllDoctor(empId));
        addStep("Customer", () -> spresenter.cllCustomer(empId));
        addStep("Customer Station", () -> spresenter.cllCustomerStation(empId));
        addStep("Provider Type", () -> spresenter.callProviderType());

        addStep("NSM", () -> spresenter.cllNSM(empcode, emprole));
        addStep("RSM", () -> spresenter.cllDZSM(empcode, emprole));
        addStep("ASM", () -> spresenter.cllAM(empcode, emprole));
        addStep("MIO", () -> spresenter.cllMIO(empcode, emprole));

        addStep("Region", () -> spresenter.cllZone(empcode, emprole));
        addStep("Area", () -> spresenter.cllArea(empcode, emprole));
        addStep("Territory", () -> spresenter.cllTeritory(empcode, emprole));
        addStep("Sub Territory", () -> spresenter.cllSTeritory(empcode, emprole));
        addStep("Market", () -> spresenter.cllMarket(empcode, emprole));
        addStep("Designation", () -> spresenter.cllDesignation(empId));
        addStep("Degree", () -> spresenter.cllDegree(empId));
        addStep("Speciality", () -> spresenter.cllSpeciality(empId));
        addStep("Special Day", () -> spresenter.cllSpecialday(empId));
        addStep("Institution", () -> spresenter.cllInstitution(empId));

        addStep("Chamber Type", () -> spresenter.cllChamberType(empId));
        addStep("Chamber Name", () -> spresenter.cllChamberName(empId));
        addStep("Brand", () -> spresenter.cllBrand(empId));
        addStep("Doctor Category", () -> spresenter.cllDoccategory(empId));
        addStep("Division", () -> spresenter.cllDivision());
        addStep("District", () -> spresenter.cllDistrict());
        addStep("Thana", () -> spresenter.cllThana());


        addStep("User Role", () -> spresenter.cllUserRole(empId));
        addStep("User By Role", () -> spresenter.cllUserByRole(empId));
        addStep("Product", () -> spresenter.cllProduct(empId));
        addStep("Product Sample", () -> spresenter.cllProductSample(empId));
        addStep("Product Gift", () -> spresenter.cllProductGift(empId));
        addStep("Quoted Price", () -> spresenter.cllQuotedPrice(empId));

        addStep("Doctor Type", () -> spresenter.cllDoctorType(empId, year));
        addStep("Customer Type", () -> spresenter.cllCustomerType(empId, year));

        addStep("Doctor Contact Type", () -> spresenter.cllDoctorContactType(empId, year));
        addStep("Expense Type", () -> spresenter.cllExpenseType(empId, year, emprole));
        addStep("Leave Type", () -> spresenter.cllLeaveType(empId, year));
        addStep("Prescription Type", () -> spresenter.cllPrescriptionType(empId, year));
        addStep("Non Effective Reason", () -> spresenter.cllNonEffectivereason(empId, year));
        addStep("Transport", () -> spresenter.cllTransportList(empId, year));
        addStep("Tour Purpose", () -> spresenter.cllTourPurpose(empId, year));
        addStep("Visit Type", () -> spresenter.cllVisitType(empId, year));
        addStep("SMC Type", () -> spresenter.callSMCType());

        startQueue();
    }

    private void startQueue() {
        currentStepIndex = 0;
        currentStepAttempt = 0;
        viewBindings.splashProgress.setMax(Math.max(syncQueue.size(), 1));
        runCurrentStep();
    }

    private void runCurrentStep() {
        if (currentStepIndex >= syncQueue.size()) {
            AllSyncDone();
            return;
        }
        String name = syncStepNames.get(currentStepIndex);
        int percent = (int) ((currentStepIndex * 100.0f) / syncQueue.size());
        viewBindings.top.setText("Synchronizing " + name + "... (" + (currentStepIndex + 1) + "/" + syncQueue.size() + ") " + percent + "%");
        viewBindings.splashProgress.setProgress(currentStepIndex);
        try {
            syncQueue.get(currentStepIndex).run();
        } catch (Exception ex) {
            Log.e("SyncFromServerAc", "Failed to launch step: " + name, ex);
            advanceQueue();
        }
    }

    // Guarantees that an exception thrown while handling a callback never freezes the queue.
    private void runSafely(Runnable body) {
        try {
            body.run();
        } catch (Exception ex) {
            Log.e("SyncFromServerAc", "Callback handling error", ex);
        }
    }

    private void handleStepResult(boolean success) {
        if (!success && currentStepAttempt < MAX_ATTEMPTS - 1) {
            currentStepAttempt++;
            runCurrentStep();
            return;
        }
        advanceQueue();
    }

    private void advanceQueue() {
        currentStepIndex++;
        currentStepAttempt = 0;
        runCurrentStep();
    }

    //Method to run progress bar for 5 seconds
    private void playProgress() {
        ObjectAnimator.ofInt(splashProgress, "progress", 100)
                .setDuration(25000)
                .start();
    }

    public void AllSyncDone() {
        try {
            String today = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
            SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a");
            String todaytime = dateFormat.format(new Date());
            boolean isTrue = syncDb_helper.Insert_InitTableB(today, todaytime);
            if (!isTrue) {
                Log.e("SyncFromServerAc", "Failed to write final sync marker");
            }
            viewBindings.top.setText("Synchronization complete");
            viewBindings.splashProgress.setProgress(viewBindings.splashProgress.getMax());
            System.out.println("AllSync Done!!!!");
            Intent i = new Intent(SyncFromServerAc.this, MainDashboardActivity.class);
            i.addFlags(i.FLAG_ACTIVITY_CLEAR_TOP | i.FLAG_ACTIVITY_CLEAR_TASK | i.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        } catch (Exception ex) {
            ex.printStackTrace();
            ToastManagment.GetLongToast(SyncFromServerAc.this, "Something went wrong");
        }
    }

    public void NoInternet() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SyncFromServerAc.this);
        alertDialogBuilder.setIcon(R.drawable.ic_warning);
        alertDialogBuilder.setTitle("No Internet Connection!!!");
        alertDialogBuilder.setMessage("Please Connect internet and then press Done");
        alertDialogBuilder.setPositiveButton("Done",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        finish();
                        startActivity(getIntent());
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onGetNSM(String t, boolean a) {
        runSafely(() -> {
            isNSMDone = a;
            if (isNSMDone && isRSMDone && isASMDone
                    && isMIODone && isRoleList && isRoleUser)  {
                masterDone=true;
                viewBindings.masterAlldoneTxt.setText("----Complete");
            }
        });
        handleStepResult(a);
    }

    @Override
    public void onGetDZSM(String t, boolean a) {
        runSafely(() -> {
            isRSMDone = a;
            if (isNSMDone && isRSMDone && isASMDone
                    && isMIODone && isRoleList && isRoleUser) {
                masterDone=true;
                viewBindings.masterAlldoneTxt.setText("----Complete");
            }
        });
        handleStepResult(a);
    }

    @Override
    public void onGetAM(String t, boolean a) {
        runSafely(() -> {
            isASMDone = a;
            if (isNSMDone && isRSMDone && isASMDone
                    && isMIODone && isRoleList && isRoleUser) {
                masterDone=true;
                viewBindings.masterAlldoneTxt.setText("----Complete");
            }
        });
        handleStepResult(a);
    }

    @Override
    public void onGetMIO(String t, boolean a) {
        runSafely(() -> {
            isMIODone = a;
            if (isNSMDone && isRSMDone && isASMDone
                    && isMIODone && isRoleList && isRoleUser) {
                masterDone=true;
                viewBindings.masterAlldoneTxt.setText("----Complete");
            }
        });
        handleStepResult(a);
    }

    @Override
    public void onGetGroup(String t, boolean a) {
        runSafely(() -> {
            viewBindings.groupTxt.setText(t);
            isGroupDone = a;
            if (isGroupDone && isRegionDone && isAreaDone
                    && isTeritorryDone && isSubTeritorryDone && isMarketDone) {
                areaDone=true;
                viewBindings.areaAlldoneTxt.setText("----Complete");
            }
        });
        handleStepResult(a);
    }

    @Override
    public void onGetZone(String t, boolean a) {
        runSafely(() -> {
            viewBindings.regionTxt.setText(t);
            isRegionDone = a;
            if (isGroupDone && isRegionDone && isAreaDone
                    && isTeritorryDone && isSubTeritorryDone && isMarketDone) {
                areaDone=true;
                viewBindings.areaAlldoneTxt.setText("----Complete");
            }
        });
        handleStepResult(a);
    }

    @Override
    public void onGetArea(String t, boolean a) {
        runSafely(() -> {
            viewBindings.areaTxt.setText(t);
            isAreaDone = a;
            if (isGroupDone && isRegionDone && isAreaDone
                    && isTeritorryDone && isSubTeritorryDone && isMarketDone) {
                areaDone=true;
                viewBindings.areaAlldoneTxt.setText("----Complete");
            }
        });
        handleStepResult(a);
    }

    @Override
    public void onGetTeritory(String t, boolean a) {
        runSafely(() -> {
            viewBindings.teritoryTxt.setText(t);
            isTeritorryDone = a;
            if (isGroupDone && isRegionDone && isAreaDone
                    && isTeritorryDone && isSubTeritorryDone && isMarketDone) {
                areaDone=true;
                viewBindings.areaAlldoneTxt.setText("----Complete");
            }
        });
        handleStepResult(a);
    }

    @Override
    public void onGetSTeritory(String t, boolean a) {
        runSafely(() -> {
            viewBindings.steritoryTxt.setText(t);
            isSubTeritorryDone = a;
            if (isGroupDone && isRegionDone && isAreaDone
                    && isTeritorryDone && isSubTeritorryDone && isMarketDone) {
                areaDone=true;
                viewBindings.areaAlldoneTxt.setText("----Complete");
            }
        });
        handleStepResult(a);
    }

    @Override
    public void onGetMarket(String t, boolean a) {
        runSafely(() -> {
            viewBindings.marketTxt.setText(t);
            isMarketDone = a;
            if (isGroupDone && isRegionDone && isAreaDone
                    && isTeritorryDone && isSubTeritorryDone && isMarketDone) {
                areaDone=true;
                viewBindings.areaAlldoneTxt.setText("----Complete");
            }
        });
        handleStepResult(a);
    }

    @Override
    public void onGetCustomer(String t, boolean a) {
        runSafely(() -> {
            viewBindings.custsTxt.setText(t);
            isCustomerList = a;
            if (isCustomerList && isCustStationDone) {
                customerDone=true;
                viewBindings.custAlldoneTxt.setText("----Complete");
            }
        });
        handleStepResult(a);
    }

    @Override
    public void onGetCustomerStation(String t, boolean a) {
        runSafely(() -> {
            viewBindings.custstationTxt.setText(t);
            isCustStationDone = a;
            if (isCustomerList && isCustStationDone) {
                customerDone=true;
                viewBindings.custAlldoneTxt.setText("----Complete");
            }
        });
        handleStepResult(a);
    }

    @Override
    public void onGetDoctor(String t, boolean a) {
        runSafely(() -> {
            viewBindings.docTxt.setText(t);
            isDoctorDone = a;
            System.out.println("1.DoctorDone"+isDoctorDone);

            if (isDoctorDone && isDesignationDone && isDoctorDegreeDone
                    && isDoctorSpecilityDone && isDoctorSpecilDAyDone && isDoctorInstitutionDone && isChamberType &&
                    isChamberName && isBrand && isDocCategory /*&& isProgramType*/) {
                doctorDone=true;
                viewBindings.docAlldoneTxt.setText("----Complete");
            }
        });
        handleStepResult(a);
    }

    @Override
    public void onGetDesignation(String t, boolean a) {
        runSafely(() -> {
            viewBindings.desigTxt.setText(t);
            isDesignationDone = a;
            System.out.println("2.DoctorDone"+isDoctorDone);
            if (isDoctorDone && isDesignationDone && isDoctorDegreeDone
                    && isDoctorSpecilityDone && isDoctorSpecilDAyDone && isDoctorInstitutionDone && isChamberType &&
                    isChamberName && isBrand && isDocCategory /*&& isProgramType*/) {
                doctorDone=true;
                viewBindings.docAlldoneTxt.setText("----Complete");
            }
        });
        handleStepResult(a);
    }

    @Override
    public void onGetDegree(String t, boolean a) {
        runSafely(() -> {
            viewBindings.degTxt.setText(t);
            isDoctorDegreeDone = a;
            if (isDoctorDone && isDesignationDone && isDoctorDegreeDone
                    && isDoctorSpecilityDone && isDoctorSpecilDAyDone && isDoctorInstitutionDone && isChamberType &&
                    isChamberName && isBrand && isDocCategory /*&& isProgramType*/) {
                doctorDone=true;
                viewBindings.docAlldoneTxt.setText("----Complete");
            }
        });
        handleStepResult(a);
    }

    @Override
    public void onGetSpeciality(String t, boolean a) {
        runSafely(() -> {
            viewBindings.spcTxt.setText(t);
            isDoctorSpecilityDone = a;
            if (isDoctorDone && isDesignationDone && isDoctorDegreeDone
                    && isDoctorSpecilityDone && isDoctorSpecilDAyDone && isDoctorInstitutionDone && isChamberType &&
                    isChamberName && isBrand && isDocCategory /*&& isProgramType*/) {
                doctorDone=true;
                viewBindings.docAlldoneTxt.setText("----Complete");
            }
        });
        handleStepResult(a);
    }

    @Override
    public void onGetSpecialday(String t, boolean a) {
        runSafely(() -> {
            viewBindings.spcdayTxt.setText(t);
            isDoctorSpecilDAyDone = a;
            if (isDoctorDone && isDesignationDone && isDoctorDegreeDone
                    && isDoctorSpecilityDone && isDoctorSpecilDAyDone && isDoctorInstitutionDone && isChamberType &&
                    isChamberName && isBrand && isDocCategory /*&& isProgramType*/) {
                doctorDone=true;
                viewBindings.docAlldoneTxt.setText("----Complete");
            }
        });
        handleStepResult(a);
    }

    @Override
    public void onGetInstitution(String t, boolean a) {
        runSafely(() -> {
            viewBindings.institutionTxt.setText(t);
            isDoctorInstitutionDone = a;
            if (isDoctorDone && isDesignationDone && isDoctorDegreeDone
                    && isDoctorSpecilityDone && isDoctorSpecilDAyDone && isDoctorInstitutionDone && isChamberType &&
                    isChamberName && isBrand && isDocCategory /*&& isProgramType*/) {
                doctorDone=true;
                viewBindings.docAlldoneTxt.setText("----Complete");
            }
        });
        handleStepResult(a);
    }

    @Override
    public void onGetChamberType(String t, boolean a) {
        runSafely(() -> {
            viewBindings.ChamberTxt.setText(t);
            isChamberType = a;
            if (isDoctorDone && isDesignationDone && isDoctorDegreeDone
                    && isDoctorSpecilityDone && isDoctorSpecilDAyDone && isDoctorInstitutionDone && isChamberType &&
                    isChamberName && isBrand && isDocCategory /*&& isProgramType*/) {
                doctorDone=true;
                viewBindings.docAlldoneTxt.setText("----Complete");
            }
        });
        handleStepResult(a);
    }

    @Override
    public void onGetChamberName(String t, boolean a) {
        runSafely(() -> {
            viewBindings.ChamberNameTxt.setText(t);
            isChamberName= a;
            if (isDoctorDone && isDesignationDone && isDoctorDegreeDone
                    && isDoctorSpecilityDone && isDoctorSpecilDAyDone && isDoctorInstitutionDone && isChamberType &&
                    isChamberName && isBrand && isDocCategory /*&& isProgramType*/) {
                doctorDone=true;
                viewBindings.docAlldoneTxt.setText("----Complete");
            }
        });
        handleStepResult(a);
    }

    @Override
    public void onGetBrand(String t, boolean a) {
        runSafely(() -> {
            viewBindings.brandTxt.setText(t);
            isBrand= a;
            if (isDoctorDone && isDesignationDone && isDoctorDegreeDone
                    && isDoctorSpecilityDone && isDoctorSpecilDAyDone && isDoctorInstitutionDone && isChamberType &&
                    isChamberName && isBrand && isDocCategory /*&& isProgramType*/) {
                doctorDone=true;
                viewBindings.docAlldoneTxt.setText("----Complete");
            }
        });
        handleStepResult(a);
    }

    @Override
    public void onGetDoccategory(String t, boolean a) {
        runSafely(() -> {
            viewBindings.doccatTxt.setText(t);
            isDocCategory= a;
            if (isDoctorDone && isDesignationDone && isDoctorDegreeDone
                    && isDoctorSpecilityDone && isDoctorSpecilDAyDone && isDoctorInstitutionDone && isChamberType &&
                    isChamberName && isBrand && isDocCategory /*&& isProgramType*/) {
                doctorDone=true;
                viewBindings.docAlldoneTxt.setText("----Complete");
            }
        });
        handleStepResult(a);
    }

    @Override
    public void onGetProgramtypey(String t, boolean a) {
        // Not part of the sequential queue (source API call is currently disabled in the presenter).
        viewBindings.progtypeTxt.setText(t);
        isProgramType= a;
        if (isDoctorDone && isDesignationDone && isDoctorDegreeDone
                && isDoctorSpecilityDone && isDoctorSpecilDAyDone && isDoctorInstitutionDone && isChamberType &&
                isChamberName && isBrand && isDocCategory /*&& isProgramType*/) {
            doctorDone=true;
            viewBindings.docAlldoneTxt.setText("----Complete");
        }
    }


    @Override
    public void onGetUserRole(String t, boolean a) {
        runSafely(() -> {
            viewBindings.roleTxt.setText(t);
            isRoleList= a;
            if (isNSMDone && isRSMDone && isASMDone
                    && isMIODone && isRoleList && isRoleUser) {
                masterDone=true;
                viewBindings.masterAlldoneTxt.setText("----Complete");
            }
        });
        handleStepResult(a);
    }

    @Override
    public void onGetUserByRole(String t, boolean a) {
        runSafely(() -> {
            viewBindings.roleuserTxt.setText(t);
            isRoleUser= a;
            if (isNSMDone && isRSMDone && isASMDone
                    && isMIODone && isRoleList && isRoleUser) {
                masterDone=true;
                viewBindings.masterAlldoneTxt.setText("----Complete");
            }
        });
        handleStepResult(a);
    }

    @Override
    public void onGetProduct(String t, boolean a) {
        runSafely(() -> {
            viewBindings.productsTxt.setText(t);
            isProductDone= a;
            if (isProductDone && isSampleProductDone && isGiftProductDone && isquotedprice) {
                productDone=true;
                viewBindings.productAlldoneTxt.setText("----Complete");
            }
        });
        handleStepResult(a);
    }

    @Override
    public void onGetProductSample(String t, boolean a) {
        runSafely(() -> {
            viewBindings.sampleproductsTxt.setText(t);
            isSampleProductDone= a;
            if (isProductDone && isSampleProductDone && isGiftProductDone && isquotedprice) {
                productDone=true;
                viewBindings.productAlldoneTxt.setText("----Complete");
            }
        });
        handleStepResult(a);
    }

    @Override
    public void onGetProductGift(String t, boolean a) {
        runSafely(() -> {
            viewBindings.giftproductsTxt.setText(t);
            isGiftProductDone= a;
            if (isProductDone && isSampleProductDone && isGiftProductDone && isquotedprice) {
                productDone=true;
                viewBindings.productAlldoneTxt.setText("----Complete");
            }
        });
        handleStepResult(a);
    }

    @Override
    public void onGetQuotedPrice(String t, boolean a) {
        runSafely(() -> {
            isquotedprice= a;
            if (isProductDone && isSampleProductDone && isGiftProductDone && isquotedprice) {
                productDone=true;
                viewBindings.productAlldoneTxt.setText("----Complete");
            }
        });
        handleStepResult(a);
    }
    @Override
    public void onGetDoctorType(String t, boolean a) {
        runSafely(() -> {
            viewBindings.doctypeTxt.setText(t);
            isDoctorTypeDone= a;
            if (isDoctorTypeDone && isCustTypeDone && isDocConType && isExpenseTypeDone
                    && isLeaveTypeDone && isPrescType && isNoneffect && isTransport && isTourPurpose
                    && isVisitTypeDone && isProviderType && isSMCType) {
                typeDone = true;
                viewBindings.typeAlldoneTxt.setText("----Complete");
            }
        });
        handleStepResult(a);
    }

    @Override
    public void onGetCustomerType(String t, boolean a) {
        runSafely(() -> {
            viewBindings.custtypeTxt.setText(t);
            isCustTypeDone= a;
            if (isDoctorTypeDone && isCustTypeDone && isDocConType && isExpenseTypeDone
                    && isLeaveTypeDone && isPrescType && isNoneffect && isTransport && isTourPurpose
                    && isVisitTypeDone && isProviderType && isSMCType) {
                typeDone=true;
                viewBindings.typeAlldoneTxt.setText("----Complete");
            }
        });
        handleStepResult(a);
    }

    @Override
    public void onGetDoctorContactType(String t, boolean a) {
        runSafely(() -> {
            viewBindings.dcontypeTxt.setText(t);
            isDocConType= a;
            if (isDoctorTypeDone && isCustTypeDone && isDocConType && isExpenseTypeDone
                    && isLeaveTypeDone && isPrescType && isNoneffect &&
                    isTransport && isTourPurpose && isVisitTypeDone && isProviderType && isSMCType) {
                typeDone=true;
                viewBindings.typeAlldoneTxt.setText("----Complete");
            }
        });
        handleStepResult(a);
    }

    @Override
    public void onGetExpenseType(String t, boolean a) {
        runSafely(() -> {
            viewBindings.expensetypeTxt.setText(t);
            isExpenseTypeDone= a;
            if (isDoctorTypeDone && isCustTypeDone && isDocConType && isExpenseTypeDone
                    && isLeaveTypeDone && isPrescType && isNoneffect &&
                    isTransport && isTourPurpose && isVisitTypeDone && isProviderType && isSMCType) {
                typeDone=true;
                viewBindings.typeAlldoneTxt.setText("----Complete");
            }
        });
        handleStepResult(a);
    }

    @Override
    public void onGetLeaveType(String t, boolean a) {
        runSafely(() -> {
            viewBindings.leavetypeTxt.setText(t);
            isLeaveTypeDone= a;
            if (isDoctorTypeDone && isCustTypeDone && isDocConType && isExpenseTypeDone
                    && isLeaveTypeDone && isPrescType && isNoneffect &&
                    isTransport && isTourPurpose && isVisitTypeDone && isProviderType && isSMCType) {
                typeDone=true;
                viewBindings.typeAlldoneTxt.setText("----Complete");
            }
        });
        handleStepResult(a);
    }

    @Override
    public void onGetPrescriptionType(String t, boolean a) {
        runSafely(() -> {
            viewBindings.prescriptypeTxt.setText(t);
            isPrescType= a;
            if (isDoctorTypeDone && isCustTypeDone && isDocConType && isExpenseTypeDone
                    && isLeaveTypeDone && isPrescType && isNoneffect &&
                    isTransport && isTourPurpose && isVisitTypeDone && isProviderType && isSMCType) {
                typeDone=true;
                viewBindings.typeAlldoneTxt.setText("----Complete");
            }
        });
        handleStepResult(a);
    }

    @Override
    public void onGetNonEffectivereason(String t, boolean a) {
        runSafely(() -> {
            viewBindings.reasonTxt.setText(t);
            isNoneffect= a;
            if (isDoctorTypeDone && isCustTypeDone && isDocConType && isExpenseTypeDone
                    && isLeaveTypeDone && isPrescType && isNoneffect &&
                    isTransport && isTourPurpose && isVisitTypeDone && isProviderType && isSMCType) {
                typeDone=true;
                viewBindings.typeAlldoneTxt.setText("----Complete");
            }
        });
        handleStepResult(a);
    }

    @Override
    public void onGetTransportList(String t, boolean a) {
        runSafely(() -> {
            viewBindings.transportTxt.setText(t);
            isTransport= a;
            if (isDoctorTypeDone && isCustTypeDone && isDocConType && isExpenseTypeDone
                    && isLeaveTypeDone && isPrescType && isNoneffect &&
                    isTransport && isTourPurpose && isVisitTypeDone && isProviderType && isSMCType) {
                typeDone=true;
                viewBindings.typeAlldoneTxt.setText("----Complete");
            }
        });
        handleStepResult(a);
    }

    @Override
    public void onGetTourPurpose(String t, boolean a) {
        runSafely(() -> {
            viewBindings.tourPurposeTxt.setText(t);
            isTourPurpose= a;
            if (isDoctorTypeDone && isCustTypeDone && isDocConType && isExpenseTypeDone
                    && isLeaveTypeDone && isPrescType && isNoneffect &&
                    isTransport && isTourPurpose && isVisitTypeDone && isProviderType && isSMCType) {
                typeDone=true;
                viewBindings.typeAlldoneTxt.setText("----Complete");
            }
        });
        handleStepResult(a);
    }

    @Override
    public void onGetVisitType(String t, boolean a) {
        runSafely(() -> {
            viewBindings.visittypeTxt.setText(t);
            isVisitTypeDone= a;
            if (isDoctorTypeDone && isCustTypeDone && isDocConType && isExpenseTypeDone
                    && isLeaveTypeDone && isPrescType && isNoneffect &&
                    isTransport && isTourPurpose && isVisitTypeDone && isProviderType && isSMCType) {
                typeDone=true;
                viewBindings.typeAlldoneTxt.setText("----Complete");
            }
        });
        handleStepResult(a);
    }

    @Override
    public void onGetProviderType(String t, boolean a) {
        runSafely(() -> {
            viewBindings.progtypeTxt.setText(t);
            isProviderType= a;
            if (isDoctorTypeDone && isCustTypeDone && isDocConType && isExpenseTypeDone
                    && isLeaveTypeDone && isPrescType && isNoneffect &&
                    isTransport && isTourPurpose && isVisitTypeDone && isProviderType && isSMCType) {
                typeDone=true;
                viewBindings.typeAlldoneTxt.setText("----Complete");
            }
        });
        handleStepResult(a);
    }

    @Override
    public void onGetSMCType(String a, boolean t) {
        runSafely(() -> {
            //viewBindings.progtypeTxt.setText(t);
            isSMCType= t;
            if (isDoctorTypeDone && isCustTypeDone && isDocConType && isExpenseTypeDone
                    && isLeaveTypeDone && isPrescType && isNoneffect &&
                    isTransport && isTourPurpose && isVisitTypeDone && isProviderType && isSMCType) {
                typeDone=true;
                viewBindings.typeAlldoneTxt.setText("----Complete");
            }
        });
        handleStepResult(t);
    }
}
