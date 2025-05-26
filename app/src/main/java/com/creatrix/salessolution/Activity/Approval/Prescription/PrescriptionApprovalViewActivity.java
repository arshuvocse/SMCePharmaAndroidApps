package com.creatrix.salessolution.Activity.Approval.Prescription;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Base64;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toolbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.creatrix.salessolution.Activity.Approval.DCR.LviewHelper;
import com.creatrix.salessolution.Interface.IPrescApproval;
import com.creatrix.salessolution.Presenter.PrescApprovalPresenter;
import com.creatrix.salessolution.R;
import com.creatrix.salessolution.UtilityHelper.SessionManagement;
import com.creatrix.salessolution.UtilityHelper.SnackBarManagement;
import com.creatrix.salessolution.databinding.ActivityPrescriptionApprovalViewBinding;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class PrescriptionApprovalViewActivity extends AppCompatActivity implements IPrescApproval.View {
    ActivityPrescriptionApprovalViewBinding binding;
    PrescApprovalPresenter presenter;
    PrescApprovalData aInfoData;
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
        //setContentView(R.layout.activity_prescription_approval_view);
        binding = ActivityPrescriptionApprovalViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        presenter = new PrescApprovalPresenter(this, PrescriptionApprovalViewActivity.this);
        binding.toolbarCustom.setNavigationOnClickListener(v -> onBackPressed());
        session = new SessionManagement(PrescriptionApprovalViewActivity.this);
        userInfo = session.getUserDetails();

        RoleTypeId = Integer.parseInt(Objects.requireNonNull(userInfo.get(SessionManagement.KEY_EmpRoleTypeId)));
        roleType = userInfo.get(SessionManagement.KEY_EmpRoleType);
        empid = Integer.parseInt(Objects.requireNonNull(userInfo.get(SessionManagement.KEY_EmpId)));

        Gson gson = new Gson();
        aInfoData = gson.fromJson(getIntent().getStringExtra("myjson"), PrescApprovalData.class);
        LoadView(aInfoData);
        binding.approveLayout.btnApprove.setOnClickListener(v -> {
            PrescApprovalRQ req = new PrescApprovalRQ();
            int step = aInfoData.getStep();
            int fstep = step + 1;

            req.setPrescriptionApprovalId(0);
            req.setFromEmpId(empid);
            req.setToEmpId(next);
            req.setTableId(aInfoData.getPrescriptionId());
            req.setStatus("Verified");//Accepted==approve for Admin
            req.setType(aInfoData.getType());
            req.setStep(fstep);
            req.setEntryByApp(empid);
            String entrydate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
            String entrytime = new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(new Date());
            req.setEntryDateApp(entrydate);
            req.setEntryTimeApp(entrytime);
            req.setMenuId(379);

            Gson gson1=new Gson();
            String dd=gson1.toJson(req);
            System.out.println(dd);
            presenter.SavePrescApproval(req);
        });
        initCommentPop();
        binding.approveLayout.btnReject.setOnClickListener(v -> popComment.show());
    }

    @SuppressLint("SetTextI18n")
    private void LoadView(PrescApprovalData aInfoData) {
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
            binding.prescDateTv.setText(aInfoData.getaPrescriptionRMasterDAO().getPrescriptionDate());
            binding.prescDnameTv.setText(aInfoData.getaPrescriptionRMasterDAO().getDoctorName());
            binding.prescPTypeTv.setText(aInfoData.getaPrescriptionRMasterDAO().getPrescriptionType());
            binding.prescChamberTv.setText(aInfoData.getaPrescriptionRMasterDAO().getChemberName());
            binding.prescMarketTv.setText(aInfoData.getaPrescriptionRMasterDAO().getMarketName());


            ArrayList<String> product = new ArrayList<>();
            for (int i = 0; i < aInfoData.getaPrescriptionRMasterDAO().getaPrescriptionDtlsDAO().size(); i++) {
                String pname = aInfoData.getaPrescriptionRMasterDAO().getaPrescriptionDtlsDAO().get(i).getProductName();
                product.add(pname);
            }
            ArrayAdapter<String> products = new ArrayAdapter<>(this, R.layout.lv_dcrbrand, R.id.dcrbrand, product);
            binding.productlv.setAdapter(products);
            LviewHelper.getListViewSize(binding.productlv);
                    Glide.with(PrescriptionApprovalViewActivity.this)
                    .load(aInfoData.getaPrescriptionRMasterDAO().getImageString())
                    .fitCenter()
                    //.placeholder(R.drawable.loading_spinner)
                    .into(binding.prescImg);
        } catch (Exception exception) {

        }

        try {
            switch (aInfoData.getApprovalStatus()) {
                case "0":
                    binding.daStatusTv.setText("Pending");
                    binding.daStatusTv.setTextColor(Color.parseColor("#ff7400"));
                    break;
                case "1":
                    binding.daStatusTv.setText("Verified");
                    binding.daStatusTv.setTextColor(Color.parseColor("#4169e1"));
                    break;
                case "2":
                    binding.daStatusTv.setText("Approved");
                    binding.daStatusTv.setTextColor(Color.parseColor("#00b248"));
                    break;
                case "3":
                    binding.daStatusTv.setText("Rejected");
                    binding.daStatusTv.setTextColor(Color.parseColor("#C12222"));
                    break;
            }
        } catch (Exception exception) {
        }

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
                    //binding.approveLayout.approvemaster.setVisibility(View.GONE);
                    binding.warnToast.setBackgroundResource(R.drawable.shape_pending);
                }
            }
        }

    }

    public void initCommentPop() {
        popComment = new Dialog(PrescriptionApprovalViewActivity.this);
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
                PrescApprovalRQ req = new PrescApprovalRQ();

                int step = aInfoData.getStep();
                int fstep = step + 1;
                req.setPrescriptionApprovalId(0);
                req.setFromEmpId(empid);
                req.setToEmpId(next);
                req.setTableId(aInfoData.getPrescriptionId());
                req.setStatus("Rejected");//Accepted==approve for admin
                req.setType(aInfoData.getType());
                req.setStep(fstep);
                req.setEntryByApp(empid);
                String entrydate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
                String entrytime = new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(new Date());
                req.setEntryDateApp(entrydate);
                req.setEntryTimeApp(entrytime);
                req.setMenuId(379);
                req.setComments(userComment.getText().toString());
                presenter.SavePrescApproval(req);
                SnackBarManagement._success_CustomMessage(v1, "Rejected");
                popComment.dismiss();
                onBackPressed();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
            }
        });
    }

    @Override
    public void onGetPrescApprovalList(List<PrescApprovalData> aList) {

    }

    @Override
    public void onSaveSuccess(String message) {
        new AlertDialog.Builder(this)
                .setTitle("Success")
                .setMessage(message)
                .setPositiveButton("OK", (dialog, which) -> {
                    session = new SessionManagement(getApplicationContext());
                    ((AlertDialog) dialog).getButton(which).setVisibility(View.INVISIBLE);
                    dialog.cancel();
                    onBackPressed();
                    finish();
                }).setCancelable(false).show();
    }

    @Override
    public void onError(String message) {

    }

/*    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent mIntent = new Intent(PrescriptionApprovalViewActivity.this, PrescriptionApprovalListActivity.class);
        startActivity(mIntent);
        finish();
    }*/
}