package com.creatrix.salessolution.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.creatrix.salessolution.Activity.Approval.ApprovalPanelFragment;
import com.creatrix.salessolution.Activity.Fragment.HomeFragment;
import com.creatrix.salessolution.Activity.Fragment.MoreFragment;
import com.creatrix.salessolution.Activity.Fragment.ReportFragment;
import com.creatrix.salessolution.DBAdapter.DBCrudHelper;
import com.creatrix.salessolution.DBAdapter.SyncDb_Helper;
import com.creatrix.salessolution.R;
import com.creatrix.salessolution.UtilityHelper.SessionManagement;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;
import java.util.HashMap;
public class MainDashboardActivity extends AppCompatActivity {

    ChipNavigationBar bottomNav;
    FragmentManager fragmentManager;
    //ImageBadgeView badgeView;
    SessionManagement session;
    SyncDb_Helper syncDb_helper;
    DBCrudHelper dbCrudHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_dashboard);
        bottomNav = findViewById(R.id.bottomNav);
        session = new SessionManagement(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        String empId = user.get(SessionManagement.KEY_EmpId);
        syncDb_helper = new SyncDb_Helper(this);
        dbCrudHelper = new DBCrudHelper(this);
        //TODO:
      /*  if(!syncDb_helper.CheckDataInTable("tblInitTable")){
            try {
                Intent i = new Intent(MainDashboardActivity.this, SyncFromServerAc.class);
                startActivity(i);
                finish();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }*/

//        GetNotificationCount(Integer.parseInt(empId));

//        final Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            public void run() {
//              GetNotificationCount(Integer.parseInt(empId));
////                Toast.makeText(MainDashboardActivity.this,"After 30 Sec ",Toast.LENGTH_LONG).show();
//                handler.postDelayed(this, 20000); //now is every 1 minutes
//            }
//        }, 20000); //Every 120000 ms (1 minutes)


//        badgeView = findViewById(R.id.notifiationBadge);
//        badgeView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(MainDashboardActivity.this, NotificationActivity.class);
//                startActivity(i);
//                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//            }
//        });
        FirebaseMessaging.getInstance().subscribeToTopic("mychannel")
                .addOnCompleteListener(task -> {
                    //String msg = getString(R.string.msg_subscribed);
                    if (!task.isSuccessful()) {
                       // msg = getString(R.string.msg_subscribe_failed);
                    }

                });
        if (savedInstanceState==null){
            bottomNav.setItemSelected(R.id.navigation_home,true);
            fragmentManager = getSupportFragmentManager();
            HomeFragment homeFragment = new HomeFragment();
            fragmentManager.beginTransaction().setCustomAnimations(R.anim.slide_in_right,R.anim.slide_out_left).replace(R.id.fragmentContainer,homeFragment).commit();
        }
        bottomNav.setOnItemSelectedListener(id -> {
            Fragment fragment = null;
            switch (id){
                case R.id.navigation_home:
                    fragment = new HomeFragment();
                    break;
                case R.id.navigation_approval:
                    fragment = new ApprovalPanelFragment();
                    break;
                case R.id.navigation_reports:
                    fragment = new ReportFragment();
                    break;
                case R.id.navigation_more:
                    fragment = new MoreFragment();
                    break;
            }
            if (fragment !=null){
                fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().setCustomAnimations(R.anim.slide_in_right,R.anim.slide_out_left).replace(R.id.fragmentContainer,fragment).commit();
            }
        });
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Exit App")
                .setMessage("Are you sure you want to exit?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            //TODO:Exit hole login Dorkar Porbe na
                           /* session.logoutUser();
                            dbCrudHelper._deleteAllRecordsFromaTable("tblInitTable");
                            dbCrudHelper._deleteAllRecordsFromaTable("tbl_ProductInfo");
                            dbCrudHelper._deleteAllRecordsFromaTable("tbl_ProductSampleInfo");
                            dbCrudHelper._deleteAllRecordsFromaTable("tblCustomerInfo");
                            dbCrudHelper._deleteAllRecordsFromaTable("tblDoctorInfo");
                            dbCrudHelper._deleteAllRecordsFromaTable("tblDoctorBrand");*/
                            finish();
                        } catch (Exception exception) {
                            exception.printStackTrace();
                        }
                    }

                })
                .setNegativeButton("No", null)
                .show();
    }
    /*public void GetNotificationCount(int empId){

        try{
            GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
            Call<Integer> call = service.GetNotificationCount(empId);
            HttpUrl ds = call.request().url();
            call.enqueue(new Callback<Integer>() {
                @Override
                public void onResponse(Call<Integer> call, Response<Integer> response) {
                    if(response.body() !=null){
                     Integer nCount = response.body();
                     if(nCount > 0){
                         badgeView.startAnimation(AnimationUtils.loadAnimation(MainDashboardActivity.this,R.anim.shake));
                         badgeView.setBadgeValue(nCount);
                    }
                    }


                }
                @Override
                public void onFailure(Call<Integer> call, Throwable t) {
                    Log.d("zxc", "f: "+t);

                    if(t instanceof SocketTimeoutException){
                    }else{
                    }
                }
            });

        }catch (Exception ex){
            Log.d("zxc", "e: "+ex);

        }

    }*/


    @Override
    protected void onRestart() {
        super.onRestart();
        HashMap<String, String> user = session.getUserDetails();
        String empId = user.get(SessionManagement.KEY_EmpId);

    }
}