package com.creatrix.salessolution.Activity.Approval.Leave;

import android.app.Dialog;
import android.content.DialogInterface;
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

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.creatrix.salessolution.Interface.ILeaveApproval;
import com.creatrix.salessolution.Presenter.LeaveApprovalPresenter;
import com.creatrix.salessolution.R;
import com.creatrix.salessolution.UtilityHelper.SessionManagement;
import com.creatrix.salessolution.UtilityHelper.SnackBarManagement;
import com.creatrix.salessolution.databinding.ActivityLeaveApprovalViewBinding;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class LeaveApprovalViewActivity extends AppCompatActivity implements ILeaveApproval.View {
    ActivityLeaveApprovalViewBinding binding;
    LeaveApprovalPresenter presenter;
    LeaveApprovalData aInfoData;
    SessionManagement session;
    HashMap<String, String> userInfo;
    int RoleTypeId, empid;
    String roleType;
    Dialog popComment;
    Button submitCmnt;

    private int prev = 0;
    private int current = 0;
    private int next = 0;
    private int myrole = 0;
    String prev_roleType, next_roleType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_leave_approval_view);
        binding = ActivityLeaveApprovalViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        presenter = new LeaveApprovalPresenter(this, LeaveApprovalViewActivity.this);
        binding.toolbarCustom.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        session = new SessionManagement(LeaveApprovalViewActivity.this);
        userInfo = session.getUserDetails();

        RoleTypeId = Integer.parseInt(userInfo.get(SessionManagement.KEY_EmpRoleTypeId));
        roleType = userInfo.get(SessionManagement.KEY_EmpRoleType);
        empid = Integer.parseInt(userInfo.get(SessionManagement.KEY_EmpId));

        Gson gson = new Gson();
        aInfoData = gson.fromJson(getIntent().getStringExtra("myjson"), LeaveApprovalData.class);

        LoadView(aInfoData);
        binding.approveLayout.btnApprove.setOnClickListener(v -> {
            LeaveApprovalRQ req = new LeaveApprovalRQ();
            int step = aInfoData.getStep();
            int fstep = step + 1;
            req.setLeaveApprovalId(0);
            req.setFromEmpId(empid);
            req.setToEmpId(next);
            req.setTableId(aInfoData.getLeaveApplicationId());
            req.setStatus("Verified");//Accepted==approve for Admin
            req.setType(aInfoData.getType());
            req.setStep(fstep);
            req.setEntryByApp(String.valueOf(empid));
            String entrydate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
            String entrytime = new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(new Date());
            req.setEntryDateApp(entrydate);
            req.setEntryTimeApp(entrytime);
            req.setMenuId(1378);
            Gson gson1=new Gson();
            String data=gson1.toJson(req);
            System.out.println(data);
            presenter.SaveLeaveApproval(req);
        });
        initCommentPop();
        binding.approveLayout.btnReject.setOnClickListener(v -> {
            popComment.show();
        });
    }

    private void LoadView(LeaveApprovalData aInfoData) {
        if (RoleTypeId == 2) {
            prev = aInfoData.getMIOEmpId();
            prev_roleType = "MIO";
            current = aInfoData.getASMEMPId();
            next = aInfoData.getRSMEMPId();
            next_roleType = "AM";
            myrole = 2;
        }
        if (RoleTypeId == 3) {
            prev = aInfoData.getASMEMPId();
            prev_roleType = "AM";
            current = aInfoData.getRSMEMPId();
            next = aInfoData.getNSMEMPId();
            next_roleType = "DZSM";
            myrole = 3;
        }
        if (RoleTypeId == 4) {
            prev = aInfoData.getRSMEMPId();
            prev_roleType = "DZSM";
            current = aInfoData.getNSMEMPId();
            next_roleType = "ADMIN";
            next = 0;
            myrole = 4;
        }
        if (RoleTypeId == 5) {
            myrole = 5;
        }

        try {
            binding.prescEnameTv.setText(aInfoData.getEmpName());
            binding.leaveDateTv.setText(aInfoData.getEntryDate());
            binding.leaveDnameTv.setText(aInfoData.getLeaveFromDate());
            binding.leaveReturnTv.setText(aInfoData.getDateOfReturnsToDuty());
            binding.leaveTypeTv.setText(aInfoData.getType());
            binding.leaveAddressTv.setText(aInfoData.getLeaveAddress());
            binding.leaveContactTv.setText(aInfoData.getEmergencyContactNo());
            binding.leaveReason.setText(aInfoData.getReason());
            binding.leaveComment.setText(aInfoData.getRemarks());

            if (aInfoData.getImageString().equals("")) {

            } else {
                byte[] decodedString = Base64.decode(aInfoData.getImageString(), Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                binding.prescImg.setImageBitmap(decodedByte);
            }

        } catch (Exception exception) {
            exception.printStackTrace();
        }

        try {
            if (aInfoData.getApprovalStatus().equals("0")) {
                binding.leaveStatusTv.setText("Pending");
                binding.leaveStatusTv.setTextColor(Color.parseColor("#ff7400"));
            } else if (aInfoData.getApprovalStatus().equals("1")) {
                binding.leaveStatusTv.setText("Verified");
                binding.leaveStatusTv.setTextColor(Color.parseColor("#4169e1"));
            } else if (aInfoData.getApprovalStatus().equals("2")) {
                binding.leaveStatusTv.setText("Approved");
                binding.leaveStatusTv.setTextColor(Color.parseColor("#00b248"));
            } else if (aInfoData.getApprovalStatus().equals("3")) {
                binding.leaveStatusTv.setText("Rejected");
                binding.leaveStatusTv.setTextColor(Color.parseColor("#C12222"));
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        /*if (prev == current) {
            if (aInfoData.getRoleTypeId() == RoleTypeId) {
                binding.approveLayout.btnApprove.setVisibility(View.GONE);
                binding.approveLayout.btnReject.setVisibility(View.GONE);

                binding.warnToast.setVisibility(View.VISIBLE);
                binding.warnToast.setText("Waiting For Final Approval");
                binding.warnToast.setBackgroundResource(R.drawable.shape_approved);
            }
            else {
                binding.approveLayout.approvemaster.setVisibility(View.VISIBLE);
            }
        } else {
            if (aInfoData.getToRoleTypeId() == RoleTypeId) {
                binding.approveLayout.btnApprove.setVisibility(View.VISIBLE);
                binding.approveLayout.btnReject.setVisibility(View.VISIBLE);
            } else {
                binding.approveLayout.btnApprove.setVisibility(View.GONE);
                binding.approveLayout.btnReject.setVisibility(View.GONE);
                if (aInfoData.getRoleTypeId() >= RoleTypeId) {
                    binding.warnToast.setVisibility(View.VISIBLE);
                    binding.warnToast.setText("Waiting For Final Approval");
                    // binding.approveLayout.approvemaster.setVisibility(View.GONE);
                    binding.warnToast.setBackgroundResource(R.drawable.shape_approved);
                } else {
                    binding.warnToast.setVisibility(View.VISIBLE);
                    binding.warnToast.setText("Need To Approved By " + prev_roleType);
                    //binding.approveLayout.approvemaster.setVisibility(View.GONE);
                    binding.warnToast.setBackgroundResource(R.drawable.shape_pending);
                }
            }
        }*/

        if (prev == current) {
            if (aInfoData.getRoleTypeId() == RoleTypeId) {
                binding.approveLayout.btnApprove.setVisibility(View.GONE);
                binding.approveLayout.btnReject.setVisibility(View.GONE);
                binding.warnToast.setVisibility(View.VISIBLE);
                binding.warnToast.setText("Waiting For Final Approval");
                binding.warnToast.setBackgroundResource(R.drawable.shape_approved);
            } else {
                binding.approveLayout.approvemaster.setVisibility(View.VISIBLE);
            }
        } else {
            if (aInfoData.getToRoleTypeId() == null) {
                binding.approveLayout.approvemaster.setVisibility(View.GONE);
            } else if (aInfoData.getToRoleTypeId() == RoleTypeId) {
                binding.approveLayout.btnApprove.setVisibility(View.VISIBLE);
                binding.approveLayout.btnReject.setVisibility(View.VISIBLE);
            } else {
                binding.approveLayout.btnApprove.setVisibility(View.GONE);
                binding.approveLayout.btnReject.setVisibility(View.GONE);
                if (aInfoData.getRoleTypeId() >= RoleTypeId) {
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

    }

    public void initCommentPop() {
        popComment = new Dialog(LeaveApprovalViewActivity.this);
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
                LeaveApprovalRQ req = new LeaveApprovalRQ();

                int step = aInfoData.getStep();
                int fstep = step + 1;
                req.setLeaveApprovalId(0);
                req.setFromEmpId(empid);
                req.setToEmpId(next);
                req.setTableId(aInfoData.getLeaveApplicationId());
                req.setStatus("Rejected");//Accepted==approve for admin
                req.setType(aInfoData.getType());
                req.setStep(fstep);
                req.setEntryByApp(String.valueOf(empid));
                String entrydate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
                String entrytime = new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(new Date());
                req.setEntryDateApp(entrydate);
                req.setEntryTimeApp(entrytime);
                req.setMenuId(1378);
                req.setComments(userComment.getText().toString());
                presenter.SaveLeaveApproval(req);


                SnackBarManagement._success_CustomMessage(v1, "Rejected");
                popComment.dismiss();
                onBackPressed();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
            }
        });
    }

    @Override
    public void onGetLeaveApprovalList(List<LeaveApprovalData> aList) {

    }

    @Override
    public void onSaveSuccess(String message, String what) {
        new AlertDialog.Builder(this)
                .setTitle(what)
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        session = new SessionManagement(getApplicationContext());
                        ((AlertDialog) dialog).getButton(which).setVisibility(View.INVISIBLE);
                        onBackPressed();
                        finish();
                    }
                }).setCancelable(false).show();

    }

    @Override
    public void onError(String message) {
        SnackBarManagement._error_CustomMessage(binding.getRoot(), message);
    }

}