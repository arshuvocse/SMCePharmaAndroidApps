package com.creatrix.salessolution.Activity.Team;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.creatrix.salessolution.Activity.Team.Adapter.TeamAdapter;
import com.creatrix.salessolution.Activity.Team.Model.Team;
import com.creatrix.salessolution.Network.RetrofitClientInstance;
import com.creatrix.salessolution.Network.UserProcessAPI;
import com.creatrix.salessolution.UtilityHelper.SessionManagement;
import com.creatrix.salessolution.UtilityHelper.SnackBarManagement;
import com.creatrix.salessolution.databinding.ActivityTeamListBinding;

import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.HttpUrl;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TeamListActivity extends AppCompatActivity {
    ActivityTeamListBinding binding;
    ProgressDialog progressDoalog;
    TeamAdapter mAdapter;
    int empId;
    String RoleType;
    Map<String, String> filter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTeamListBinding.inflate(getLayoutInflater());
        //setContentView(R.layout.activity_team_list);
        setContentView(binding.getRoot());
        binding.toolbarCustom.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        SessionManagement session = new SessionManagement(getApplicationContext());
        session.checkLogin();
        HashMap<String, String> user = session.getUserDetails();
        empId = Integer.parseInt(user.get(SessionManagement.KEY_EmpId));
        RoleType = user.get(SessionManagement.KEY_EmpRoleType);

        filter = new HashMap<>();
        filter.put("role", RoleType);
        filter.put("aparam", " ");
        GetTeamList(empId,filter);
        binding.swipteam.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                binding.swipteam.setRefreshing(false);
                GetTeamList(empId,filter);
            }
        });

    }

    public void GetTeamList(int empId, Map<String, String> filter) {
        progressDoalog = new ProgressDialog(TeamListActivity.this);
        progressDoalog.setMessage("Loading...Team");
        progressDoalog.show();
        progressDoalog.setCanceledOnTouchOutside(false);
        try {
            UserProcessAPI service = RetrofitClientInstance.getRetrofitInstance().create(UserProcessAPI.class);
            Call<List<Team>> call = service.GetTeamList(empId,filter);
            HttpUrl ds = call.request().url();
            call.enqueue(new Callback<List<Team>>() {
                @Override
                public void onResponse(Call<List<Team>> call, Response<List<Team>> response) {
                    progressDoalog.dismiss();
                    LoadinView(response.body());
                }

                @Override
                public void onFailure(Call<List<Team>> call, Throwable t) {
                    progressDoalog.dismiss();
                    if (t instanceof SocketTimeoutException) {
                        SnackBarManagement._error_CustomMessage(binding.masterLayout, "Slow Connection Detected");
                    } else {
                        SnackBarManagement._error_CustomMessage(binding.masterLayout, "Some Error Occurred");
                    }


                }
            });

        } catch (Exception ex) {
            progressDoalog.dismiss();
            SnackBarManagement._error_CustomMessage(binding.masterLayout, "Some Error Occurred");

        }
    }

    public void LoadinView(List<Team> aList) {
        if (aList != null) {
            mAdapter = new TeamAdapter(TeamListActivity.this, aList);
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(TeamListActivity.this);
            binding.rvTeams.setLayoutManager(mLayoutManager);
            binding.rvTeams.setItemAnimator(new DefaultItemAnimator());
            binding.rvTeams.setAdapter(mAdapter);
  /*          binding.rvTeams.addItemDecoration(new DividerItemDecoration(getApplicationContext(),
                    DividerItemDecoration.VERTICAL));*/
            binding.rvTeams.setItemAnimator(null);
            binding.rvTeams.scrollToPosition(0);
            mAdapter.notifyDataSetChanged();
        }
    }
}