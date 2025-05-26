package com.creatrix.salessolution.Activity.Training;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;


import com.creatrix.salessolution.Interface.ITraining;
import com.creatrix.salessolution.Model.Training;
import com.creatrix.salessolution.Presenter.TrainingPresenter;
import com.creatrix.salessolution.Services.Constants;
import com.creatrix.salessolution.UtilityHelper.SessionManagement;
import com.creatrix.salessolution.databinding.ActivityTrainingViewBinding;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class TrainingViewActivity extends AppCompatActivity implements ITraining.View{
ActivityTrainingViewBinding binding;
    TrainingPresenter presenterTraining;
    SessionManagement session;
    int empId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding =ActivityTrainingViewBinding.inflate(getLayoutInflater());
       // setContentView(R.layout.activity_training_view);
        setContentView(binding.getRoot());
        session = new SessionManagement(TrainingViewActivity.this);
        HashMap<String, String> user = session.getUserDetails();
        empId = Integer.parseInt(Objects.requireNonNull(user.get(SessionManagement.KEY_EmpId)));
        presenterTraining = new TrainingPresenter(this, this);

        Intent getTrainingData = getIntent();
        Gson gson = new Gson();
        Training tInfoData = gson.fromJson(getIntent().getStringExtra("myjson"), Training.class);
        if(getTrainingData.getStringExtra("From").equals("trainingAdapter"))
        {
            Constants.SeenTraining="seentrainingAdapter";
            presenterTraining.seenTraining(tInfoData.getTrainningId(),empId);
        }
        binding.trainingTitle.setText(tInfoData.getTitle());
        binding.trainingCreatedBy.setText(tInfoData.getCreatedBy());
        binding.trainingCreatedAt.setText(tInfoData.getCreateAt());
        binding.trainingDesc.setText(tInfoData.getDescription());
    }

    @Override
    public void onSuccess(List<Training> tList) {

    }

    @Override
    public void onError(String msg) {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Constants.SeenTraining="seentrainingAdapter";
        finish();
    }
}