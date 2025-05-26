package com.creatrix.salessolution.Activity.MileageClaim;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Base64;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.creatrix.salessolution.Activity.Expense.ExpenceClaimViewActivity;
import com.creatrix.salessolution.Activity.MileageClaim.Model.ApproveMilRQ;
import com.creatrix.salessolution.Activity.MileageClaim.Model.MileageListTeam;
import com.creatrix.salessolution.Interface.IMileageTeam;
import com.creatrix.salessolution.Model.MilageClaimReport;
import com.creatrix.salessolution.Presenter.TeamMileagePresenter;
import com.creatrix.salessolution.R;
import com.creatrix.salessolution.Services.Constants;
import com.creatrix.salessolution.UtilityHelper.SessionManagement;
import com.creatrix.salessolution.UtilityHelper.SnackBarManagement;
import com.creatrix.salessolution.databinding.ActivityMileageClaimViewBinding;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class MileageClaimViewActivity extends AppCompatActivity implements IMileageTeam.View {
    ActivityMileageClaimViewBinding binding;
    MilageClaimReport aInfoData;
    Dialog popComment;
    Gson gson = new Gson();
    SessionManagement session;
    HashMap<String, String> userInfo = new HashMap<>();
    MileageListTeam mlt = new MileageListTeam();

    TeamMileagePresenter presenter;
    //For Approve
    private int prev = 0;
    private int current = 0;
    private int next = 0;
    private int myrole = 0;

    int RoleTypeId, empid;
    String roleType;
    String prev_roleType, next_roleType;
    Button submitCmnt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMileageClaimViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        presenter = new TeamMileagePresenter(this, this);
        session = new SessionManagement(MileageClaimViewActivity.this);
        userInfo = session.getUserDetails();

        RoleTypeId = Integer.parseInt(userInfo.get(SessionManagement.KEY_EmpRoleTypeId));
        roleType = userInfo.get(SessionManagement.KEY_EmpRoleType);
        empid = Integer.parseInt(userInfo.get(SessionManagement.KEY_EmpId));

        switch (Constants.WHO) {
            case "Mileageadapter":
                aInfoData = gson.fromJson(getIntent().getStringExtra("MilDetails"), MilageClaimReport.class);
                LoadViewData(aInfoData);
                binding.btnEdit.setOnClickListener(v -> {
                    Constants.WHO = "MileageViewSelfAC";
                    Intent goto_exp = new Intent(MileageClaimViewActivity.this, AddMileageClaimActivity.class);
                    Gson gson = new Gson();
                    String myJson = gson.toJson(aInfoData);
                    goto_exp.putExtra("SelfMileageEditdata", myJson);
                    startActivity(goto_exp);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_in_right);
                });
                break;

            case "MilTeamApproveAdapter":
                mlt = gson.fromJson(getIntent().getStringExtra("MilTLDetails"), MileageListTeam.class);
                //presenter.GetExpenseDetails(getIntent().getIntExtra("ExpenseClaimID", 0));
                LoadViewTeamData(mlt);
                if (mlt.getApprovalStatus().equals("1")) {
                    //binding.approveLayout.approvemaster.setVisibility(View.GONE);
                    binding.approveLayout.btnApprove.setVisibility(View.GONE);
                    binding.approveLayout.btnReject.setVisibility(View.GONE);
                }
                if (RoleTypeId == 2) {
                    prev = mlt.getMIOEmpId();
                    prev_roleType = "MIO";
                    current = mlt.getASMEMPId();
                    next = mlt.getRSMEMPId();
                    next_roleType = "AM";
                    myrole = 2;
                }
                if (RoleTypeId == 3) {
                    prev = mlt.getASMEMPId();
                    prev_roleType = "AM";
                    current = mlt.getRSMEMPId();
                    next = mlt.getNSMEMPId();
                    next_roleType = "DZSM";
                    myrole = 3;
                }
                if (RoleTypeId == 4) {
                    prev = mlt.getRSMEMPId();
                    prev_roleType = "DZSM";
                    current = mlt.getNSMEMPId();
                    next_roleType = "ADMIN";
                    next = 0;
                    myrole = 4;
                }
                if (RoleTypeId == 5) {
                    myrole = 5;
                }
                //TODO:Button On off
                if (prev == current) {
                    if (mlt.getRoleTypeId() == RoleTypeId) {

                        binding.approveLayout.btnApprove.setVisibility(View.GONE);
                        binding.approveLayout.btnReject.setVisibility(View.GONE);

                        binding.warnToast.setVisibility(View.VISIBLE);
                        binding.warnToast.setText("Approved");
                        binding.warnToast.setBackgroundResource(R.drawable.shape_approved);
                    } else {
                        //binding.approveLayout.btnApprove.setVisibility(View.VISIBLE);
                        binding.approveLayout.approvemaster.setVisibility(View.VISIBLE);
                    }
                } else {
                    try {
                        if (mlt.getToRoleTypeId() == RoleTypeId) {
                            // binding.approveLayout.approvemaster.setVisibility(View.VISIBLE);
                            binding.btnEdit.setVisibility(View.VISIBLE);
                            binding.approveLayout.btnApprove.setVisibility(View.VISIBLE);
                            binding.approveLayout.btnReject.setVisibility(View.VISIBLE);
                            binding.btnEdit.setVisibility(View.VISIBLE);
                        } else {
                            binding.approveLayout.btnApprove.setVisibility(View.GONE);
                            binding.approveLayout.btnReject.setVisibility(View.GONE);
                            if (mlt.getRoleTypeId() >= RoleTypeId) {
                                binding.warnToast.setVisibility(View.VISIBLE);
                                binding.warnToast.setText("Approved");
                                binding.warnToast.setBackgroundResource(R.drawable.shape_approved);
                            } else {
                                binding.warnToast.setVisibility(View.VISIBLE);
                                binding.warnToast.setText("Need To Approved By " + prev_roleType);
                                binding.warnToast.setBackgroundResource(R.drawable.shape_pending);
                            }
                        }
                    } catch (Exception e) {
                        //e.printStackTrace();
                    }
                }
                binding.approveLayout.approvemaster.setVisibility(View.VISIBLE);
                binding.approveLayout.btnApprove.setOnClickListener(v -> {
                    ApproveMilRQ req = new ApproveMilRQ();
                    int step = mlt.getStep();
                    int fstep = step + 1;

                    req.setMileageApprovalId(0);
                    req.setFromEmpId(empid);
                    req.setToEmpId(next);
                    req.setTableId(mlt.getMileageClaimId());
                    req.setStatus("Verified");//Accepted==approve for Admin
                    req.setType(mlt.getType());
                    req.setStep(fstep);
                    req.setEntryByApp(empid);
                    String entrydate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
                    String entrytime = new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(new Date());
                    req.setEntryDateApp(entrydate);
                    req.setEntryTimeApp(entrytime);
                    req.setMenuId(372);

                    String data= gson.toJson(req);
                    System.out.println("mileage "+data);
                   presenter.SaveTeamMileageClaim(req);
                });
                initCommentPop();
                binding.approveLayout.btnReject.setOnClickListener(v -> {
                    popComment.show();
                });
                break;
        }
    }

    public void initCommentPop() {
        popComment = new Dialog(MileageClaimViewActivity.this);
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

                ApproveMilRQ req = new ApproveMilRQ();

                int step = mlt.getStep();
                int fstep = step + 1;

                // Toast.makeText(context, "empid : "+String.valueOf(empid), Toast.LENGTH_SHORT).show();

                req.setMileageApprovalId(0);
                req.setFromEmpId(empid);
                req.setToEmpId(next);
                req.setTableId(mlt.getMileageClaimId());
                req.setStatus("Rejected");//Accepted==approve for admin
                req.setType(mlt.getType());
                req.setStep(fstep);
                req.setEntryByApp(empid);
                String entrydate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
                String entrytime = new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(new Date());
                req.setEntryDateApp(entrydate);
                req.setEntryTimeApp(entrytime);
                req.setMenuId(372);
                req.setComments(userComment.getText().toString());
                presenter.SaveTeamMileageClaim(req);
                SnackBarManagement._success_CustomMessage(v1, "Rejected");
                popComment.dismiss();
                onBackPressed();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
            }
        });
    }

    private void LoadViewData(MilageClaimReport aInfoData) {

        try {
           /* byte[] decodedString = Base64.decode(aInfoData.getImageString(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            binding.image.setImageBitmap(decodedByte);*/

            Glide.with(MileageClaimViewActivity.this)
                    .load(aInfoData.getImageString())
                    .fitCenter()
                    .into(binding.image);
        } catch (Exception exception) {
           // exception.printStackTrace();
        }

        binding.milNameTv.setText(aInfoData.getEmpName());
        binding.milDateTv.setText(aInfoData.getMileageDate());
        binding.milTransportTv.setText(aInfoData.getTransportName());
        binding.milMilTv.setText(String.valueOf(aInfoData.getMileageInKM()));
        binding.milMarketTv.setText(aInfoData.getMarketName());


        switch (aInfoData.getApprovalStatus()) {
            case "0":
                binding.milStatusTv.setText("Pending..");
                binding.milStatusTv.setBackgroundResource(R.drawable.shape_prepending);
                //binding.milStatusTv.setTextColor(Color.parseColor("#ff7400"));
                binding.milStatusTv.setTextColor(Color.parseColor("#ffffff"));
                binding.btnEdit.setVisibility(View.VISIBLE);
                break;
            case "1":
                binding.milStatusTv.setText("Verified");
                binding.btnEdit.setVisibility(View.GONE);
                binding.milStatusTv.setTextColor(Color.parseColor("#ffffff"));
                binding.milStatusTv.setBackgroundResource(R.drawable.shape_pending);
                break;
            case "2":
                binding.milStatusTv.setText("Approved");
                binding.btnEdit.setVisibility(View.GONE);
                binding.milStatusTv.setBackgroundResource(R.drawable.shape_approved);
                binding.milStatusTv.setTextColor(Color.parseColor("#ffffff"));
                //binding.milStatusTv.setTextColor(Color.parseColor("#00b248"));
                break;
            case "3":
                binding.milStatusTv.setText("Rejected");
                binding.btnEdit.setVisibility(View.VISIBLE);
                binding.milStatusTv.setBackgroundResource(R.drawable.shape_reject);
                //  binding.milStatusTv.setTextColor(Color.parseColor("#C12222"));
                binding.milStatusTv.setTextColor(Color.parseColor("#ffffff"));

                break;
        }

    }

    private void LoadViewTeamData(MileageListTeam aInfoData) {
/*
        byte[] decodedString = Base64.decode(aInfoData.getImageString(), Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        binding.image.setImageBitmap(decodedByte);*/
        Glide.with(MileageClaimViewActivity.this)
                .load(aInfoData.getImageString())
                .fitCenter()
                .into(binding.image);


        binding.milNameTv.setText(aInfoData.getEmpName());
        binding.milDateTv.setText(aInfoData.getMileageDate());
        binding.milTransportTv.setText(aInfoData.getTransportName());
        binding.milMilTv.setText(String.valueOf(aInfoData.getMileageInKM()));
        binding.milMarketTv.setText(aInfoData.getMarketName());

        if (aInfoData.getApprovalStatus().equals("0")) {
            binding.milStatusTv.setText("Pending..");
            binding.milStatusTv.setTextColor(Color.parseColor("#ff7400"));
        } else if (aInfoData.getApprovalStatus().equals("1")) {
            binding.milStatusTv.setText("Verified");
            binding.milStatusTv.setBackgroundResource(R.drawable.shape_approved);
        } else if (aInfoData.getApprovalStatus().equals("2")) {
            binding.milStatusTv.setText("Approved");
            binding.milStatusTv.setTextColor(Color.parseColor("#00b248"));
        } else if (aInfoData.getApprovalStatus().equals("3")) {
            binding.milStatusTv.setText("Rejected");
            binding.milStatusTv.setTextColor(Color.parseColor("#C12222"));

        }
        binding.btnEdit.setOnClickListener(v -> {
            Constants.WHO = "MileageViewAC";
            Intent goto_exp = new Intent(MileageClaimViewActivity.this, AddMileageClaimActivity.class);
            Gson gson = new Gson();
            String myJson = gson.toJson(aInfoData);
            goto_exp.putExtra("mileageEditdata", myJson);
            startActivity(goto_exp);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_in_right);
        });
        //binding.image.setImageBitmap();
    }


    @Override
    public void onTeamMileageList(List<MileageListTeam> aList) {

    }

    @Override
    public void onSaveSuccess(String message) {
        // SnackBarManagement._success_CustomMessage(v1, "Rejected");
        if(message.equals("Approved"))
        {
            new androidx.appcompat.app.AlertDialog.Builder(MileageClaimViewActivity.this)
                    .setTitle("Success")
                    .setMessage("Mileage Approved")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            onBackPressed();
                            finish();

                        }

                    }).setCancelable(false).show();
        }else if(message.equals("Check")){
            SnackBarManagement._warning_CustomMessage(binding.masterLayout,"Approved date already expired!!");
        }else {
            SnackBarManagement._warning_CustomMessage(binding.masterLayout,"Something went wrong!!.Try Again");
        }
    }



    @Override
    public void onSaveError(String message) {

    }
}