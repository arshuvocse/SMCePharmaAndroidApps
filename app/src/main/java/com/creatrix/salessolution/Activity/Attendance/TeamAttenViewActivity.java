package com.creatrix.salessolution.Activity.Attendance;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Base64;
import android.view.Gravity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.bumptech.glide.Glide;
import com.creatrix.salessolution.Activity.Approval.MapActivity;
import com.creatrix.salessolution.Activity.Attendance.Model.ApproveRQ;
import com.creatrix.salessolution.Activity.Attendance.Model.AttenApproval;
import com.creatrix.salessolution.Activity.Attendance.Model.ButtonRP;
import com.creatrix.salessolution.Interface.IAttendance;
import com.creatrix.salessolution.Presenter.AttendancePresenter;
import com.creatrix.salessolution.R;
import com.creatrix.salessolution.Services.Constants;
import com.creatrix.salessolution.UtilityHelper.SessionManagement;
import com.creatrix.salessolution.UtilityHelper.SnackBarManagement;
import com.creatrix.salessolution.databinding.ActivityTeamAttenViewBinding;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class TeamAttenViewActivity extends AppCompatActivity implements IAttendance.View {
    ActivityTeamAttenViewBinding binding;
    AttendancePresenter presenter;
    ProgressDialog pd;
    String attentype;
    Dialog popComment;
    Button submitCmnt;
    AttenApproval tInfoData;
    FragmentManager fragmentManager;
    Double in_lat, in_lon, out_lat, out_lon;
    SessionManagement session;
    HashMap<String, String> userInfo;

    String address;
    private int prev = 0;
    private int current = 0;
    private int next = 0;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTeamAttenViewBinding.inflate(getLayoutInflater());
        //setContentView(R.layout.activity_team_atten_view);
        setContentView(binding.getRoot());
        initCommentPop();

        pd=new ProgressDialog(TeamAttenViewActivity.this);
        pd.setMessage("Processing..");
        pd.setCancelable(false);
        pd.setCanceledOnTouchOutside(false);

        presenter = new AttendancePresenter(this,TeamAttenViewActivity.this);
        Gson gson = new Gson();
        //TeamAtten tInfoData = gson.fromJson(getIntent().getStringExtra("myjson"), TeamAtten.class);
        tInfoData = gson.fromJson(getIntent().getStringExtra("myjson"), AttenApproval.class);
        binding.usernameshow.setText(tInfoData.getEmpName());
        binding.address.setText(tInfoData.getAttAddress());
        try {
            if(tInfoData.getApprovalStatus().equals("0"))
            {
                binding.teamattenStatus.setText("Pending");
                binding.teamattenStatus.setTextColor(Color.parseColor("#ff7400"));
            }else if(tInfoData.getStatus().equals("1")){
                binding.teamattenStatus.setText("Verified");
                binding.teamattenStatus.setTextColor(Color.parseColor("#4169e1"));
            }
            else if(tInfoData.getStatus().equals("2")){
                binding.teamattenStatus.setText("Approved");
                binding.teamattenStatus.setTextColor(Color.parseColor("#00b248"));
            }
            else if(tInfoData.getStatus().equals("3")){
                binding.teamattenStatus.setText("Rejected");
                binding.teamattenStatus.setTextColor(Color.parseColor("#C12222"));
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        if(tInfoData.getImageString()!=null)
        {
           /* byte[] decodedString = Base64.decode(tInfoData.getImageString(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            binding.image.setImageBitmap(decodedByte);*/
            Glide.with(TeamAttenViewActivity.this)
                    .load(tInfoData.getImageString())
                    .fitCenter()
                    //.placeholder(R.drawable.loading_spinner)
                    .into(binding.image);
        }

        if (tInfoData.getAttType() == 1) {
            binding.attentype.setText("In");
            binding.attendatetime.setText(tInfoData.getPINCreatedDateTime());
            if (tInfoData.getPInLat() != null && tInfoData.getPInLog() != null) {
                in_lat = Double.parseDouble(tInfoData.getPInLat());
                in_lon = Double.parseDouble(tInfoData.getPInLog());
            }

        } else {
            binding.attentype.setText("Out");
            binding.attendatetime.setText(tInfoData.getPINCreatedDateTime());
            if (tInfoData.getPInLat() != null && tInfoData.getPInLog() != null) {
                out_lat = Double.parseDouble(tInfoData.getPInLat());
                out_lon = Double.parseDouble(tInfoData.getPInLog());
            }
        }
        //TODO:Attendance Location View
        binding.viewmap.setOnClickListener(v -> {
            Intent gotomap=new Intent(TeamAttenViewActivity.this, MapActivity.class);
            //gotomap.putExtra("lat",String.valueOf(in_lat));
            gotomap.putExtra("lat",tInfoData.getPInLat());
            gotomap.putExtra("lon",tInfoData.getPInLog());
            gotomap.putExtra("address",tInfoData.getAttAddress());
            startActivity(gotomap);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        });

        //TODO:Attendance Approve btn
        binding.approveLayout.btnApprove.setOnClickListener(v -> {
            pd.show();
            session = new SessionManagement(TeamAttenViewActivity.this);
            userInfo = session.getUserDetails();

            int empid = Integer.parseInt(Objects.requireNonNull(userInfo.get(SessionManagement.KEY_EmpId)));
            int step = Integer.parseInt(tInfoData.getStep());
            int fstep = step + 1;

            // Toast.makeText(context, "empid : "+String.valueOf(empid), Toast.LENGTH_SHORT).show();
            ApproveRQ req = new ApproveRQ();
            req.setApprovalId(0);
            req.setFromEmpId(empid);
            req.setToEmpId(next);
            req.setTableId(tInfoData.getAttendanceId());
            req.setStatus("Verified");//Accepted==approve
            req.setType("Attendance");
            req.setStep(fstep);
            req.setEntryByApp(String.valueOf(empid));
            String entrydate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
            String entrytime = new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(new Date());
            req.setEntryDateApp(entrydate);
            req.setEntryTimeApp(entrytime);
            req.setMenuId(301);

            Gson gsons=new Gson();
            String data=gsons.toJson(req);
            System.out.println("jsons : "+data);

            presenter = new AttendancePresenter(this,TeamAttenViewActivity.this);
            presenter.teamAttendanceApprove(req);
        });
        binding.approveLayout.btnReject.setOnClickListener(v -> {
            popComment.show();
        });
//        binding.ivLocation.setOnClickListener(v -> navigateToMap());
    }

    private void navigateToMap() {
       /* if (attendance != null) {
            if (attendance.getLatitude() != null && attendance.getLongitude() != null) {

            }
        }*/
        Bundle bundle = new Bundle();
        bundle.putString(Constants.LATITUDE, tInfoData.getPInLat());
        bundle.putString(Constants.LONGITUDE, tInfoData.getPInLog());

        fragmentManager = getSupportFragmentManager();

       /* MapsFragment mapFragment = new MapsFragment();
        mapFragment.setArguments(bundle);
        fragmentManager.beginTransaction().setCustomAnimations(R.anim.slide_in_right,R.anim.slide_out_left).replace(R.id.fragmentContainerMap,mapFragment).commit();*/


    }

    public void initCommentPop() {
        popComment = new Dialog(TeamAttenViewActivity.this);
        popComment.setContentView(R.layout.pop_comment);
        //popAddQty.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popComment.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        popComment.getWindow().setLayout(Toolbar.LayoutParams.MATCH_PARENT, Toolbar.LayoutParams.WRAP_CONTENT);
        popComment.getWindow().getAttributes().gravity = Gravity.CENTER;


        final EditText userComment = (EditText) popComment.findViewById(R.id.remarksTxt);
        userComment.requestFocus();
        submitCmnt = popComment.findViewById(R.id.psubmitBnt);
        submitCmnt.setOnClickListener(v1 -> {
            if (userComment.getText().toString().equals("")) {
                userComment.setError("Comment Must");
                userComment.setFocusable(true);
            } else {
                SnackBarManagement._success_CustomMessage(v1, "Rejected");
                popComment.dismiss();
                onBackPressed();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
            }
        });
    }

    @Override
    public void onSuccessapprove(boolean t) {
        if(t)
        {
            pd.dismiss();
            new androidx.appcompat.app.AlertDialog.Builder(TeamAttenViewActivity.this)
                    .setTitle("Success")
                    .setMessage("Approved")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            finish();
                        }
                    }).setCancelable(false).show();
        }
    }

    @Override
    public void onSuccessapproveAll(String t) {

    }

    @Override
    public void onSuccess(String message, boolean status, int type) {

    }

    @Override
    public void onSuccessTeamAtten(List<AttenApproval> teamlist) {
    }

    @Override
    public void onError(String message, int type) {
    }
    @Override
    public void onButtonView(ButtonRP buttonRP) {

    }
}