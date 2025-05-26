package com.creatrix.salessolution.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.creatrix.salessolution.Model.Notice;
import com.creatrix.salessolution.Model.NotificationViewModel;
import com.creatrix.salessolution.Network.GetDataService;
import com.creatrix.salessolution.Network.RetrofitClientInstance;
import com.creatrix.salessolution.R;
import com.creatrix.salessolution.RecyclerAdapter._notification_List_recyclerAdapter;
import com.creatrix.salessolution.RecyclerAdapter._recycler_NoticeAdapter;
import com.creatrix.salessolution.UtilityHelper.SessionManagement;

import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.List;

import okhttp3.HttpUrl;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationActivity extends AppCompatActivity {
    SessionManagement session;
    ProgressDialog progressDoalog;
    private RecyclerView recyclerView;
    private _notification_List_recyclerAdapter mAdapter;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_custom);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        session = new SessionManagement(getApplicationContext());
        session.checkLogin();
        HashMap<String, String> user = session.getUserDetails();
        String userName = user.get(SessionManagement.KEY_LoginName);
        int empId = Integer.parseInt(user.get(SessionManagement.KEY_EmpId));
        getNotification(empId);
    }


    public void getNotification(int empId) {
        progressDoalog = new ProgressDialog(NotificationActivity.this);
        progressDoalog.setMessage("Notification is Loading....");
        progressDoalog.show();
        progressDoalog.setCanceledOnTouchOutside(false);
        try{
            GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
            Call<List<NotificationViewModel>> call = service.GetNotification(empId);
            HttpUrl ds = call.request().url();
            call.enqueue(new Callback<List<NotificationViewModel>>() {
                @Override
                public void onResponse(Call<List<NotificationViewModel>> call, Response<List<NotificationViewModel>> response) {
                    progressDoalog.dismiss();
                    SetInRecyclerview(response.body());
                }
                @Override
                public void onFailure(Call<List<NotificationViewModel>> call, Throwable t) {
                    progressDoalog.dismiss();
                    if(t instanceof SocketTimeoutException){
                        showError("Slow Connection Detected");
                    }else{
                        showError("Some Error Occurred");
                    }


                }
            });

        }catch (Exception ex){
            progressDoalog.dismiss();
            showError("Some Error Occurred");

        }

    }

    public void SetInRecyclerview(List<NotificationViewModel> arrayList) {
        if(arrayList!=null){
            recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
            mAdapter = new _notification_List_recyclerAdapter(arrayList);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(mAdapter);
            recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(),
                    DividerItemDecoration.VERTICAL));
            recyclerView.setItemAnimator(null);
            recyclerView.scrollToPosition(0);
            mAdapter.notifyDataSetChanged();
        }

    }

    public void showError(String msg){
        Toast.makeText(NotificationActivity.this,msg,Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        session.checkLogin();
        HashMap<String, String> user = session.getUserDetails();
        int empId = Integer.parseInt(user.get(SessionManagement.KEY_EmpId));
        getNotification(empId);
    }
}