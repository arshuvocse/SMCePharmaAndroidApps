package com.creatrix.salessolution.Activity.Training;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.creatrix.salessolution.Interface.ITraining;
import com.creatrix.salessolution.Model.Training;
import com.creatrix.salessolution.Presenter.TrainingPresenter;
import com.creatrix.salessolution.R;
import com.creatrix.salessolution.RecyclerAdapter._training_rv_Adapter;
import com.creatrix.salessolution.Services.Constants;
import com.creatrix.salessolution.UtilityHelper.SessionManagement;
import com.creatrix.salessolution.UtilityHelper.SnackBarManagement;
import com.creatrix.salessolution.databinding.ActivityTrainingBinding;

import java.util.HashMap;
import java.util.List;

public class TrainingListActivity extends AppCompatActivity implements ITraining.View {
    ActivityTrainingBinding binding;
    ITraining.Presenter presenter;
    _training_rv_Adapter adapter;
    SessionManagement session;
    HashMap<String, String> user;
    String userName, userId, empId;
    int empid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTrainingBinding.inflate(getLayoutInflater());
        //  setContentView(R.layout.activity_training);
        setContentView(binding.getRoot());
        session = new SessionManagement(TrainingListActivity.this);
        presenter = new TrainingPresenter(this, TrainingListActivity.this);

        session.checkLogin();
        user = session.getUserDetails();
        userName = user.get(SessionManagement.KEY_LoginName);
        userId = user.get(SessionManagement.KEY_UserId);
        empId = user.get(SessionManagement.KEY_EmpId);
        empid = Integer.parseInt(empId);
        presenter.getTraining(empid);

        if (Constants.SeenTraining.equals("seentrainingAdapter")) {
           // Toast.makeText(TrainingListActivity.this, "getTraining call in list", Toast.LENGTH_SHORT).show();
            presenter.getTraining(empid);
        }
        binding.toolbarCustom.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //binding.swiperefresh.setEnabled(false);
        binding.swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //int empid = Integer.parseInt(empId);
                presenter.getTraining(empid);
                binding.swiperefresh.setRefreshing(false);
            }
        });
    }

    @Override
    public void onSuccess(List<Training> tList) {
        if (tList != null) {
            //binding.swiperefresh.setRefreshing(false);
            adapter = new _training_rv_Adapter(TrainingListActivity.this, tList);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            binding.rvTraining.setLayoutManager(mLayoutManager);
            binding.rvTraining.setItemAnimator(new DefaultItemAnimator());
            binding.rvTraining.setAdapter(adapter);
        /*    binding.rvTraining.addItemDecoration(new DividerItemDecoration(getApplicationContext(),
                    DividerItemDecoration.VERTICAL));*/
            binding.rvTraining.setItemAnimator(null);
            binding.rvTraining.scrollToPosition(0);
            adapter.notifyDataSetChanged();
        } else {
            binding.swiperefresh.setRefreshing(false);
        }
    }

    @Override
    public void onError(String msg) {
        SnackBarManagement._error_CustomMessage(binding.trainingMaster, msg);
    }
}