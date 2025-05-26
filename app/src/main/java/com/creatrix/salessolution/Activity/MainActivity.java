package com.creatrix.salessolution.Activity;

import android.Manifest;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.creatrix.salessolution.Activity.Customer.CustomerDashboarActivity;
import com.creatrix.salessolution.DBAdapter.DBCrudHelper;
import com.creatrix.salessolution.DBAdapter.SyncDb_Helper;
import com.creatrix.salessolution.DBInfo.DBHelperMain;
import com.creatrix.salessolution.Interface.IVersionUpdate;
import com.creatrix.salessolution.Interface.LoginInterface;
import com.creatrix.salessolution.Model.Customer;
import com.creatrix.salessolution.Presenter.MainActivityPresenter;
import com.creatrix.salessolution.Presenter.VersionPresenter;
import com.creatrix.salessolution.R;
import com.creatrix.salessolution.Services.Constants;
import com.creatrix.salessolution.Services.LocationPermissionCheck;
import com.creatrix.salessolution.Services.LocationService;
import com.creatrix.salessolution.Setting.AppSetting;
import com.creatrix.salessolution.Setting.DeviceInfo;
import com.creatrix.salessolution.UtilityHelper.NetworkInformation;
import com.creatrix.salessolution.UtilityHelper.SessionManagement;
import com.creatrix.salessolution.UtilityHelper.SnackBarManagement;
import com.creatrix.salessolution.databinding.ActivityMainBinding;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements LoginInterface.View, IVersionUpdate.View {
    ActivityMainBinding viewBinding;
    LoginInterface.Presenter presenter;
    IVersionUpdate.Presenter vpresenter;
    View aMasterlayout;
    SessionManagement session;
    DBHelperMain dbHelperMain;
    DBCrudHelper crudHelper;
    SyncDb_Helper syncDb_helper;
    List<Customer> aCusListDemo = new ArrayList<>();
    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1;
    String imei="",device="", token;
    private static final int MY_PERMISSIONS_REQUEST_READ_PHONE_STATE = 0;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //setContentView(R.layout.activity_main);
        /*
                int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);
                if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_READ_PHONE_STATE);
                } else {
                    //TODO
                }*/
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        LocationPermissionCheck.CheckPermission(this);
        session = new SessionManagement(getApplicationContext());
        viewBinding = ActivityMainBinding.inflate(getLayoutInflater());
        View mainView = viewBinding.getRoot();
        setContentView(mainView);
        String year=new SimpleDateFormat("yyyy", Locale.getDefault()).format(new Date());
        viewBinding.year.setText(year);
        /* Dexter.withContext(MainActivity.this)
                .withPermission(permission)
                .withListener(listener)
                .check();*/
        Dexter.withContext(this)
                .withPermission(Manifest.permission.READ_PHONE_STATE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {/* ... */}

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {/* ... */}

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {/* ... */}
                }).check();

                FirebaseApp.initializeApp(MainActivity.this);
                FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            return;
                        }
                        // Get new FCM registration token
                        token = task.getResult();
                        System.out.println("device token: " + token);
                    }
                });

//        initPermission();

        dbHelperMain = new DBHelperMain(this);
        crudHelper = new DBCrudHelper(this);
        syncDb_helper = new SyncDb_Helper(this);
        aMasterlayout = findViewById(R.id.login_masterLayoutId);
        presenter = new MainActivityPresenter(this);
        vpresenter = new VersionPresenter(this);
        vpresenter.GetActiveVersion();
        session = new SessionManagement(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        String userName = user.get(SessionManagement.KEY_LoginName);
        String userPass = user.get(SessionManagement.KEY_Password);
        if (session.isLoggedIn()) {
            Intent i = new Intent(MainActivity.this, MainDashboardActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
            finish();
        }
        if (userName != null) {
            viewBinding.inputLoginName.setText(userName);
        }
        viewBinding.textView5.setText(AppSetting.AppName);
        aCusListDemo = GetDemoCustomerData();
        viewBinding.inputPassword.setText(userPass);
        viewBinding.vname.setText(GetVersionName());

        //Creates a button that mimics a crash when pressed
        /*  Button crashButton = new Button(this);
        crashButton.setText("Test Crash");
        crashButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                throw new RuntimeException("Test Crash"); // Force a crash
            }
        });

        addContentView(crashButton, new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));*/

        // Login Button Click Event
        viewBinding.btnLogin.setOnClickListener(v -> {
            String userId = viewBinding.inputLoginName.getText().toString();
            String password = viewBinding.inputPassword.getText().toString();
            if (!userId.equals("") && !password.equals("")) {

                if (NetworkInformation.isConnected(getApplicationContext())) {
                    System.out.println("Imei:" + DeviceInfo.getIMEI(this));
                    if(DeviceInfo.getIMEI(this)==null)
                    {
                        imei="Unknown_imei_"+userId;
                    }else {
                        imei=DeviceInfo.getIMEI(this);
                    }
                    if(DeviceInfo.getDeviceInfo()==null)
                    {
                        device="Unknown"+userId;
                    }else {
                        device=DeviceInfo.getDeviceInfo();
                    }
                    presenter.doLogin(userId, password, MainActivity.this, imei, token, device, GetVersionCode(), "Android", DeviceInfo.getAndroidVersion());
                } else {
                    if (crudHelper.checkLoginProfileUserExist(userId, password) == true) {
                        crudHelper.getLoginProfile_SQLite();
                        Intent i = new Intent(MainActivity.this, MainDashboardActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    } else {
                        SnackBarManagement._error_CustomMessage(aMasterlayout, "Incorrect User Id or Password.");
                    }
                }
            } else {
                SnackBarManagement._warning_CustomMessage(aMasterlayout, "Please provide User Id and Password");
            }

        });
        // Forgot password event
        viewBinding.forgotPassword.setOnClickListener(v -> SnackBarManagement._warning_CustomMessage(viewBinding.loginMasterLayoutId, "Please Contact With Admin for Password Retrieval"));
        viewBinding.site.setOnClickListener(v -> {
            String url = "https://creatrixbd.com/";
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        });
        //  location servics
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_CODE_LOCATION_PERMISSION);
        } else {
            startLocationService();
        }
    }

    /* private void GotoHomaAsAM(String role) {
         session.createLoginSession(99,"AM Monir","AM Monir",99,role,"","AM");
         Intent i = new Intent(MainActivity.this,MainDashboardActivity.class);
         i.addFlags(i.FLAG_ACTIVITY_CLEAR_TOP | i.FLAG_ACTIVITY_CLEAR_TASK |i.FLAG_ACTIVITY_NEW_TASK);
         startActivity(i);
         overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
     }*/
    @Override
    public void OnSuccess(int userId, String userName, String empMasterCo, String loginName, String password, int empId, String userCo, int IsImeiMatch, String versionName, String role, int roleTypeid, String roleType, int isAccepted, int isForward, String DesigName) {
        if (userId > 0) {
            try {
                if (GetVersionName().equals(versionName)) {
                    if (userId > 0) {
                        try {
                            crudHelper.InsertIntoLoginProfile_SQLite(userId, userName, empMasterCo, loginName, empId, userCo, role, roleTypeid, roleType, isForward, isAccepted, DesigName);
                        } catch (Exception exception) {
                            exception.printStackTrace();
                        }
                        if (!syncDb_helper.CheckDataInTable("tblInitTable")) {
                            try {
                                session.createLoginSession(userId, userName, empMasterCo, loginName, password, empId, "", "", role, String.valueOf(roleTypeid), roleType, DesigName);
                                Intent i = new Intent(MainActivity.this, SyncFromServerAc.class);
                                startActivity(i);
                                finish();
                                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                            } catch (Exception exception) {
                                exception.printStackTrace();
                            }
                        } else {
                            session.createLoginSession(userId, userName, empMasterCo, loginName, password, empId, "", "", role, String.valueOf(roleTypeid), roleType, DesigName);
                            Intent i = new Intent(MainActivity.this, MainDashboardActivity.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(i);
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        }
                    }

                } else {
                    UpdateDialog();
                }

            } catch (Exception exception) {
                exception.printStackTrace();
            }


        }

    }

    @Override
    public void OnError(String message) {
        SnackBarManagement._error_CustomMessage(aMasterlayout, message);

    }

    @Override
    public void onVersionGet(String VersionName) {
        try {
            if (!GetVersionName().equals(VersionName)) {
                UpdateDialog();
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }

    }
    private String GetVersionName() {
        String version = "";
        try {
            PackageInfo pInfo = MainActivity.this.getPackageManager().getPackageInfo(getPackageName(), 0);
            version = pInfo.versionName;

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }


        return version;
    }
    private String GetVersionCode() {
        String versioncode = "";
        try {
            PackageInfo pInfo = MainActivity.this.getPackageManager().getPackageInfo(getPackageName(), 0);
            versioncode = String.valueOf(pInfo.versionCode);

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versioncode;
    }
    public void UpdateDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilder.setIcon(R.drawable.ic_warning);
        alertDialogBuilder.setTitle("Update Available!!!");
        alertDialogBuilder.setMessage("An update is required to use the application.\n\nPlease download and update the app first. \nIf You don't get any apk file please contact Admin People");
        alertDialogBuilder.setPositiveButton("Go to Download Page",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        String url = "http://13.76.141.111:456/";
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        startActivity(i);
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelable(false);

        alertDialog.show();
    }


    public List<Customer> GetDemoCustomerData() {
        List<Customer> aList = new ArrayList<>();

        Customer a = new Customer();
        a.setCustomerMasterId(52754);
        a.setCustomerName("M/S Loni Medico");
        a.setCustomerCode("2195231");
        a.setAddress("Taltali Bz,Haji Para Temata,Sandwip,Chattogram");
        a.setCellNo("01878751454");
        aList.add(a);

        Customer b = new Customer();
        b.setCustomerMasterId(52750);
        b.setCustomerName("Mayer Dowa Pharmacy");
        b.setCustomerCode("2195349");
        b.setAddress("south keranigonj,kazi bari");
        b.setCellNo("01911644711");
        aList.add(b);

        Customer v = new Customer();
        v.setCustomerMasterId(52752);
        v.setCustomerName("Mim M Hall");
        v.setCustomerCode("2195315");
        v.setAddress("Baro Kumira,PHP Gate");
        v.setCellNo("01812579670");
        aList.add(v);

        Customer d = new Customer();
        v.setCustomerMasterId(52749);
        v.setCustomerName("Jonota Medicine");
        v.setCustomerCode("2195359");
        v.setAddress("Uttar pangaw,Codda,South Keranigonj");
        v.setCellNo("01911733728");
        aList.add(v);

        return aList;

    }

    public void GotoCustomerDashboard(String userId) {


        for (int p = 0; p < aCusListDemo.size(); p++) {
            if (userId.equals(aCusListDemo.get(p).getCustomerCode())) {

                int userid = aCusListDemo.get(p).getCustomerMasterId();
                String name = aCusListDemo.get(p).getCustomerName();
                String code = aCusListDemo.get(p).getCustomerCode();
                String extra = aCusListDemo.get(p).getAddress();
                String cell = aCusListDemo.get(p).getCellNo();

                session.createLoginSession(userid, name, code, "", "", 0, extra, cell, "Customer", "1", "", "");
                Intent i = new Intent(MainActivity.this, CustomerDashboarActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            }
        }


    }

/*    public void initPermission()
    {
        Dexter.withContext(MainActivity.this).withPermissions(Manifest.permission.CAMERA,Manifest.permission.CALL_PHONE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                        if(Environment.isExternalStorageManager())
                        {

                            *//*try {
                                folder_path = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) + "/" + folderName);
                                mypath = new File(folder_path, "Patient.bin");
                                mypath.listFiles();
                            } catch (Exception exception) {
                                exception.printStackTrace();
                            }*//*
                        }
                        else
                        {
                            Intent permissionIntent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                            startActivity(permissionIntent);
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();
    }*/


    // location servics

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_LOCATION_PERMISSION && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startLocationService();
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_LONG).show();
                moveTaskToBack(true);
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(1);
            }
        }
    }

    /*   private  boolean isLocatioServiceRunning(){
           ActivityManager activityManager = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
           if(activityManager !=null){
               for (ActivityManager.RunningServiceInfo service :
                       activityManager.getRunningServices(Integer.MAX_VALUE)){
                   if(LocationService.class.getName().equals(service.service.getClassName())){
                       if(service.foreground){
                           return true;
                       }
                   }
               }
               return false;

           }
           return false;
       }
       private  void startLocationService(){
           if(!isLocatioServiceRunning()){
               Intent intent = new Intent(getApplicationContext(),LocationService.class);
               intent.setAction(Constants.ACTION_START_LOCATION_SERVICE);
               if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                   startForegroundService(intent);
               } else {
                   startService(intent);
               }
           }
       }*/
    private boolean isLocatioServiceRunning() {
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        if (activityManager != null) {
            for (ActivityManager.RunningServiceInfo service :
                    activityManager.getRunningServices(Integer.MAX_VALUE)) {
                if (LocationService.class.getName().equals(service.service.getClassName())) {
                    if (service.foreground) {
                        return true;
                    }
                }
            }
            return false;

        }
        return false;
    }

    private void startLocationService() {
        if (!isLocatioServiceRunning()) {
            Intent intent = new Intent(MainActivity.this, LocationService.class);
            intent.setAction(Constants.ACTION_START_LOCATION_SERVICE);

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                startForegroundService(intent);
            } else {
                startService(intent);
            }
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        LocationPermissionCheck.CheckPermission(this);
    }

}