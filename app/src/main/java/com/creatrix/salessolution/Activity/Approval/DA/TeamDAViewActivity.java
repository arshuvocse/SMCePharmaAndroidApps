package com.creatrix.salessolution.Activity.Approval.DA;

import androidx.appcompat.app.AppCompatActivity;

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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.creatrix.salessolution.Activity.Approval.Prescription.PrescriptionApprovalViewActivity;
import com.creatrix.salessolution.Activity.Doctor.AddDoctor.DoctorActivity;
import com.creatrix.salessolution.Activity.MileageClaim.MileageClaimViewActivity;
import com.creatrix.salessolution.Activity.MileageClaim.Model.ApproveMilRQ;
import com.creatrix.salessolution.Interface.IDATeam;
import com.creatrix.salessolution.Model.TadaList;
import com.creatrix.salessolution.Presenter.DATeamPresenter;
import com.creatrix.salessolution.R;
import com.creatrix.salessolution.Services.Constants;
import com.creatrix.salessolution.UtilityHelper.SessionManagement;
import com.creatrix.salessolution.UtilityHelper.SnackBarManagement;
import com.creatrix.salessolution.databinding.ActivityTeamDAViewBinding;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class TeamDAViewActivity extends AppCompatActivity implements IDATeam.View {
    ActivityTeamDAViewBinding binding;
    Dialog popComment;
    Gson gson = new Gson();
    SessionManagement session;
    DATeamPresenter presenter;
    HashMap<String, String> userInfo = new HashMap<>();
    DAListData mlt = new DAListData();

    ProgressDialog pd;
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
        binding = ActivityTeamDAViewBinding.inflate(getLayoutInflater());
        //  setContentView(R.layout.activity_team_d_a_view);
        setContentView(binding.getRoot());
        presenter = new DATeamPresenter(this, this);
        binding.toolbarCustom.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        pd = new ProgressDialog(TeamDAViewActivity.this);
        session = new SessionManagement(TeamDAViewActivity.this);
        userInfo = session.getUserDetails();

        RoleTypeId = Integer.parseInt(userInfo.get(SessionManagement.KEY_EmpRoleTypeId));
        roleType = userInfo.get(SessionManagement.KEY_EmpRoleType);
        empid = Integer.parseInt(userInfo.get(SessionManagement.KEY_EmpId));
        switch (Constants.WHO) {
            case "DATeamApproveAdapter":
                mlt = gson.fromJson(getIntent().getStringExtra("DATLDetails"), DAListData.class);
                LoadViewData(mlt);
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
              /*  if (prev == current) {
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
                    if (mlt.getToRoleTypeId() == RoleTypeId) {
                        // binding.approveLayout.approvemaster.setVisibility(View.VISIBLE);
                        binding.approveLayout.btnApprove.setVisibility(View.VISIBLE);
                        binding.approveLayout.btnReject.setVisibility(View.VISIBLE);
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
                }*/
                if (prev == current) {
                    if (mlt.getRoleTypeId() == RoleTypeId) {
                        binding.approveLayout.btnApprove.setVisibility(View.GONE);
                        binding.approveLayout.btnReject.setVisibility(View.GONE);
                        binding.warnToast.setVisibility(View.VISIBLE);
                        binding.warnToast.setText("Waiting For Final Approval");
                        binding.warnToast.setBackgroundResource(R.drawable.shape_approved);
                    } else {
                        binding.approveLayout.approvemaster.setVisibility(View.VISIBLE);
                    }
                } else {
                    if (mlt.getToRoleTypeId() == 0) {
                        binding.approveLayout.approvemaster.setVisibility(View.GONE);
                    } else if (mlt.getToRoleTypeId() == RoleTypeId) {
                        binding.approveLayout.btnApprove.setVisibility(View.VISIBLE);
                        binding.approveLayout.btnReject.setVisibility(View.VISIBLE);
                    } else {
                        binding.approveLayout.btnApprove.setVisibility(View.GONE);
                        binding.approveLayout.btnReject.setVisibility(View.GONE);
                        if (mlt.getRoleTypeId() >= RoleTypeId) {
                            binding.warnToast.setVisibility(View.VISIBLE);
                            binding.warnToast.setText("Waiting For Final Approval");
                            // binding.approveLayout.approvemaster.setVisibility(View.GONE);
                            binding.warnToast.setBackgroundResource(R.drawable.shape_approved);
                        } else {
                            binding.warnToast.setVisibility(View.VISIBLE);
                            binding.warnToast.setText("Need To Approved By " + prev_roleType);
                            binding.warnToast.setBackgroundResource(R.drawable.shape_pending);
                        }
                    }

                }

                // binding.approveLayout.approvemaster.setVisibility(View.VISIBLE);
                binding.approveLayout.btnApprove.setOnClickListener(v -> {
                    ApproveDARQ req = new ApproveDARQ();
                    int step = mlt.getStep();
                    int fstep = step + 1;
                    req.setTADAApprovalId(0);
                    req.setFromEmpId(empid);
                    req.setToEmpId(next);
                    req.setTableId(mlt.getaTADAMasterDAO().getTadaID());
                    req.setStatus("Verified");//Accepted==approve for Admin
                    req.setType(mlt.getType());
                    req.setStep(fstep);
                    req.setEntryByApp(String.valueOf(empid));
                    String entrydate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
                    String entrytime = new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(new Date());
                    req.setEntryDateApp(entrydate);
                    req.setEntryTimeApp(entrytime);
                    req.setMenuId(376);

                    Gson gson = new Gson();
                    String data = gson.toJson(req);
                    System.out.println("value" + data);
                    pd.setMessage("Processing...");
                    pd.show();
                    pd.setCancelable(false);
                    presenter.SaveTeamDA(req);
                });
                initCommentPop();
                binding.approveLayout.btnReject.setOnClickListener(v -> {
                    popComment.show();
                });

                break;
        }
    }

    public void initCommentPop() {
        popComment = new Dialog(TeamDAViewActivity.this);
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
                ApproveDARQ req = new ApproveDARQ();
                int step = mlt.getStep();
                int fstep = step + 1;
                // Toast.makeText(context, "empid : "+String.valueOf(empid), Toast.LENGTH_SHORT).show();
                req.setTADAApprovalId(0);
                req.setFromEmpId(empid);
                req.setToEmpId(next);
                req.setTableId(mlt.getaTADAMasterDAO().getTadaID());
                req.setStatus("Rejected");//Accepted==approve for admin
                req.setType(mlt.getType());
                req.setStep(fstep);
                req.setEntryByApp(String.valueOf(empid));
                String entrydate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
                String entrytime = new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(new Date());
                req.setEntryDateApp(entrydate);
                req.setEntryTimeApp(entrytime);
                req.setMenuId(376);
                req.setComments(userComment.getText().toString());
                Gson gson = new Gson();
                String data = gson.toJson(req);
                System.out.println("reject data: " + data);
                presenter.SaveTeamDA(req);
                SnackBarManagement._success_CustomMessage(v1, "Rejected");
                popComment.dismiss();
                onBackPressed();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
            }
        });
    }

    private void LoadViewData(DAListData aInfoData) {
        DAMaster dm = aInfoData.getaTADAMasterDAO();
        binding.daNameTv.setText(dm.getEmpName());
        binding.daDateTv.setText(dm.getTadaDate());
        binding.daAmountTv.setText(String.valueOf(dm.getDaAmt()));
        binding.daMarketTv.setText(dm.getMarketName());
        if (!aInfoData.getaTADAMasterDAO().getImageString().isEmpty()) {
            Glide.with(TeamDAViewActivity.this)
                    .load(aInfoData.getaTADAMasterDAO().getImageString())
                    .fitCenter()
                    //.placeholder(R.drawable.loading_spinner)
                    .into(binding.imgaeView);
        }
        if (aInfoData.getApprovalStatus().equals("0")) {
            binding.daStatusTv.setText("Pending..");
            // binding.daStatusTv.setTextColor(Color.parseColor("#ff7400"));
            binding.daStatusTv.setTextColor(Color.parseColor("#ffffff"));
            binding.daStatusTv.setBackgroundResource(R.drawable.shape_prepending);
        } else if (aInfoData.getApprovalStatus().equals("1")) {
            binding.daStatusTv.setText("Verified");
            binding.daStatusTv.setBackgroundResource(R.drawable.shape_pending);
            binding.daStatusTv.setTextColor(Color.parseColor("#ffffff"));
        } else if (aInfoData.getApprovalStatus().equals("2")) {
            binding.daStatusTv.setText("Approved");
            binding.daStatusTv.setBackgroundResource(R.drawable.shape_approved);
            // binding.daStatusTv.setTextColor(Color.parseColor("#00b248"));
            binding.daStatusTv.setTextColor(Color.parseColor("#ffffff"));
        } else if (aInfoData.getApprovalStatus().equals("3")) {
            binding.daStatusTv.setText("Rejected");
            binding.daStatusTv.setBackgroundResource(R.drawable.shape_reject);
            // binding.daStatusTv.setTextColor(Color.parseColor("#C12222"));
            binding.daStatusTv.setTextColor(Color.parseColor("#ffffff"));
        }

    }


    @Override
    public void onTeamDAList(List<DAListData> aList) {

    }

    @Override
    public void onSaveSuccess(String message) {
        pd.dismiss();
        if (!message.equals("")) {
            new androidx.appcompat.app.AlertDialog.Builder(TeamDAViewActivity.this)
                    .setTitle("Success")
                    .setMessage(message)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            onBackPressed();
                            finish();
                        }
                    }).setCancelable(false).show();
        }

    }

    @Override
    public void onSaveError(String message) {
        pd.dismiss();
        SnackBarManagement._error_CustomMessage(binding.getRoot(), message);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}