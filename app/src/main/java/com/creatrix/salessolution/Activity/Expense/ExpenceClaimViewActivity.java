package com.creatrix.salessolution.Activity.Expense;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.creatrix.salessolution.Activity.Approval.Prescription.PrescriptionApprovalViewActivity;
import com.creatrix.salessolution.Activity.Attendance.Model.ApproveRQ;
import com.creatrix.salessolution.Activity.Attendance.TeamAttenViewActivity;
import com.creatrix.salessolution.Activity.Customer.Approval.CustomerApprovalActivity;
import com.creatrix.salessolution.Activity.Expense.Model.ApproveExpRQ;
import com.creatrix.salessolution.Activity.Expense.Model.ExpListTeam;
import com.creatrix.salessolution.Interface.IExpenseClaim;
import com.creatrix.salessolution.Interface.ITeamExpClaim;
import com.creatrix.salessolution.Model.Expense.ADetailListDAO;
import com.creatrix.salessolution.Model.ExpenseReportViewModel;
import com.creatrix.salessolution.Presenter.ExpenseClaimTeamPresenter;
import com.creatrix.salessolution.R;
import com.creatrix.salessolution.Services.Constants;
import com.creatrix.salessolution.UtilityHelper.SessionManagement;
import com.creatrix.salessolution.UtilityHelper.SnackBarManagement;
import com.creatrix.salessolution.databinding.ActivityExpenceClaimViewBinding;
import com.google.gson.Gson;

import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import okhttp3.HttpUrl;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExpenceClaimViewActivity extends AppCompatActivity implements ITeamExpClaim.View {
    ActivityExpenceClaimViewBinding binding;
    ExpenseClaimTeamPresenter presenter;
    Dialog popComment;
    Button submitCmnt;
    List<ADetailListDAO> dataList = new ArrayList<>();
    _expense_detailsAdapter madapter;
    ExpenseReportViewModel aInfoData;
    Gson gson = new Gson();

    SessionManagement session;
    HashMap<String, String> userInfo = new HashMap<>();
//For Approve
    private int prev = 0;
    private int current = 0;
    private int next = 0;
    private int myrole = 0;
    ExpListTeam elt=new ExpListTeam();

    int RoleTypeId,empid;
    String roleType;
    String prev_roleType, next_roleType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityExpenceClaimViewBinding.inflate(getLayoutInflater());
        //setContentView(R.layout.activity_expence_claim_view);
        setContentView(binding.getRoot());
        presenter=new ExpenseClaimTeamPresenter(this,this);

        session = new SessionManagement(ExpenceClaimViewActivity.this);
        userInfo = session.getUserDetails();

        RoleTypeId = Integer.parseInt(userInfo.get(SessionManagement.KEY_EmpRoleTypeId));
        roleType = userInfo.get(SessionManagement.KEY_EmpRoleType);
        empid = Integer.parseInt(userInfo.get(SessionManagement.KEY_EmpId));
        //String android_id = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        // switch (getIntent().getStringExtra("Who")) {
        switch (Constants.WHO) {
            case "Reportadapter":
                aInfoData = gson.fromJson(getIntent().getStringExtra("ExpDetails"), ExpenseReportViewModel.class);
                LoadTypeWiseField(aInfoData);
                binding.btnEdit.setOnClickListener(v -> {
                    Constants.WHO = "ExpViewAC";
                    Intent goto_exp = new Intent(ExpenceClaimViewActivity.this, ExpanseClamActivity.class);
                    Gson gson = new Gson();
                    String myJson = gson.toJson(aInfoData);
                    goto_exp.putExtra("Editdata", myJson);
                    startActivity(goto_exp);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_in_right);
                });
                break;

            case "ExpTeamApproveAdapter":
                elt=gson.fromJson(getIntent().getStringExtra("ExpTLDetails"), ExpListTeam.class);
                LoadTypeWiseFieldTeam(elt);
                if (RoleTypeId == 2) {
                    prev = elt.getMIOEmpId();
                    prev_roleType = "MIO";
                    current = elt.getASMEMPId();
                    next = elt.getRSMEMPId();
                    next_roleType = "AM";
                    myrole = 2;
                }
                if (RoleTypeId == 3) {
                    prev = elt.getASMEMPId();
                    prev_roleType = "AM";
                    current = elt.getRSMEMPId();
                    next = elt.getNSMEMPId();
                    next_roleType = "DZSM";
                    myrole = 3;
                }
                if (RoleTypeId == 4) {
                    prev = elt.getRSMEMPId();
                    prev_roleType = "DZSM";
                    current = elt.getNSMEMPId();
                    next_roleType = "ADMIN";
                    next = 0;
                    myrole = 4;
                }
                if (RoleTypeId == 5) {
                    myrole = 5;
                }
                //TODO:Button On off
                if (prev == current) {
                    if (elt.getRoleTypeId() == RoleTypeId) {

                        binding.approveLayout.btnApprove.setVisibility(View.GONE);
                        binding.approveLayout.btnReject.setVisibility(View.GONE);

                        binding.warnToast.setVisibility(View.VISIBLE);
                        binding.warnToast.setText("Approved");
                        binding.warnToast.setBackgroundResource(R.drawable.shape_approved);
                    } else {
                        //binding.approveLayout.btnApprove.setVisibility(View.VISIBLE);
                       // binding.approveLayout.approvemaster.setVisibility(View.VISIBLE);

                        //new Implementation
                        if (elt.getToRoleTypeId() == RoleTypeId) {
                            // binding.approveLayout.approvemaster.setVisibility(View.VISIBLE);
                            binding.btnEdit.setVisibility(View.VISIBLE);
                            binding.approveLayout.approvemaster.setVisibility(View.VISIBLE);
                           // binding.approveLayout.btnApprove.setVisibility(View.VISIBLE);
                            //binding.approveLayout.btnReject.setVisibility(View.VISIBLE);
                        } else {
                            binding.approveLayout.approvemaster.setVisibility(View.GONE);
                            //binding.approveLayout.btnApprove.setVisibility(View.GONE);
                           // binding.approveLayout.btnReject.setVisibility(View.GONE);
                            binding.btnEdit.setVisibility(View.GONE);
                            if (elt.getRoleTypeId() >= RoleTypeId) {
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
                } else {
                    if (elt.getToRoleTypeId() == RoleTypeId) {
                       // binding.approveLayout.approvemaster.setVisibility(View.VISIBLE);
                        binding.approveLayout.btnApprove.setVisibility(View.VISIBLE);
                        binding.approveLayout.btnReject.setVisibility(View.VISIBLE);
                    } else {

                        binding.approveLayout.btnApprove.setVisibility(View.GONE);
                        binding.approveLayout.btnReject.setVisibility(View.GONE);

                        if (elt.getRoleTypeId() >= RoleTypeId) {
                            binding.warnToast.setVisibility(View.VISIBLE);
                            binding.warnToast.setText("Approved");
                            binding.warnToast.setBackgroundResource(R.drawable.shape_approved);
                        } else {
                            binding.warnToast.setVisibility(View.VISIBLE);
                            binding.warnToast.setText("Need To Approved By " + prev_roleType);
                            binding.warnToast.setBackgroundResource(R.drawable.shape_pending);
                        }
                    }
                }
                binding.approveLayout.approvemaster.setVisibility(View.VISIBLE);
                binding.approveLayout.btnApprove.setOnClickListener(v -> {
                    ApproveExpRQ req=new ApproveExpRQ();
                    int step = elt.getStep();
                    int fstep = step + 1;
                    req.setExpanseApprovalId(0);
                    req.setFromEmpId(empid);
                    req.setToEmpId(next);
                    req.setTableId(elt.getExpenseClaimID());
                    req.setStatus("Verified");//Accepted==approve for Admin
                    req.setType(elt.getType());
                    req.setStep(fstep);
                    req.setEntryByApp(String.valueOf(empid));
                    String entrydate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
                    String entrytime = new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(new Date());
                    req.setEntryDateApp(entrydate);
                    req.setEntryTimeApp(entrytime);
                    req.setMenuId(356);
                    presenter.SaveExpenseCTeam(req);
                });
                initCommentPop();
                binding.approveLayout.btnReject.setOnClickListener(v -> {
                    popComment.show();
                });
                break;
        }

    }

    @SuppressLint("SetTextI18n")
    private void LoadTypeWiseField(ExpenseReportViewModel aInfoDatas) {
        binding.expUsernameTv.setText(aInfoDatas.getEmpName());
        binding.expDateTv.setText(aInfoDatas.getExpDate());
        binding.expExptypeTv.setText(aInfoDatas.getExpenseTypeName());
        binding.expExpenceTv.setText(String.valueOf(aInfoDatas.getAmount()));
        if (aInfoDatas.getRemarks() == null) {
            binding.llcommnt.setVisibility(View.GONE);
        } else {
            binding.llcommnt.setVisibility(View.VISIBLE);
            binding.expCommentTv.setText(aInfoData.getRemarks());
        }

        switch (aInfoDatas.getApprovalStatus()) {
            case "0":
                binding.expStatusTv.setText("Pending..");
                binding.expStatusTv.setTextColor(Color.parseColor("#ffffff"));
                binding.expStatusTv.setBackgroundResource(R.drawable.shape_prepending);
                binding.btnEdit.setVisibility(View.VISIBLE);
                break;
            case "1":
                binding.expStatusTv.setText("Verified");
                binding.btnEdit.setVisibility(View.GONE);
                binding.expStatusTv.setTextColor(Color.parseColor("#ffffff"));
                binding.expStatusTv.setBackgroundResource(R.drawable.shape_pending);
                break;
            case "2":
                binding.expStatusTv.setText("Approved");
                binding.btnEdit.setVisibility(View.GONE);
                binding.expStatusTv.setTextColor(Color.parseColor("#ffffff"));
                binding.expStatusTv.setBackgroundResource(R.drawable.shape_approved);
                break;
            case "3":
                binding.btnEdit.setVisibility(View.GONE);
                binding.expStatusTv.setText("Rejected");
                binding.expStatusTv.setTextColor(Color.parseColor("#ffffff"));
                binding.expStatusTv.setBackgroundResource(R.drawable.shape_reject);
                break;
        }
        try {
           /* byte[] decodedString = Base64.decode(aInfoDatas.getImageString(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            binding.image.setImageBitmap(decodedByte);*/

            Glide.with(ExpenceClaimViewActivity.this)
                    .load(aInfoData.getImageString())
                    .fitCenter()
                    .into(binding.image);
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        madapter = new _expense_detailsAdapter(aInfoDatas.getaDetailListDAO(), 1);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(ExpenceClaimViewActivity.this);
        binding.rvExp.setLayoutManager(mLayoutManager);
        binding.rvExp.setItemAnimator(new DefaultItemAnimator());
        binding.rvExp.setAdapter(madapter);
        madapter.notifyDataSetChanged();
    }
    private void LoadTypeWiseFieldTeam(ExpListTeam aInfoDatas) {
        binding.expUsernameTv.setText(aInfoDatas.getEmpName());
        binding.expDateTv.setText(aInfoDatas.getExpenseDate());
        binding.expExptypeTv.setText(aInfoDatas.getExpenseTypeName());
        binding.expExpenceTv.setText(String.valueOf(aInfoDatas.getAmount()));
        Glide.with(ExpenceClaimViewActivity.this)
                .load(aInfoDatas.getImageString())
                .fitCenter()
                .into(binding.image);
        if (aInfoDatas.getRemarks() == null) {
            binding.llcommnt.setVisibility(View.GONE);
        } else {
            binding.llcommnt.setVisibility(View.VISIBLE);
            binding.expCommentTv.setText(aInfoDatas.getRemarks());
        }
        binding.expStatusTv.setText(aInfoDatas.getStatus());
        switch (aInfoDatas.getApprovalStatus()) {
            case "0":
                binding.expStatusTv.setText("Pending...");
                binding.expStatusTv.setTextColor(Color.parseColor("#ffffff"));
                binding.expStatusTv.setBackgroundResource(R.drawable.shape_prepending);
                binding.btnEdit.setVisibility(View.VISIBLE);
                break;
            case "1":

                binding.expStatusTv.setText("Verified");
                binding.expStatusTv.setTextColor(Color.parseColor("#ffffff"));
                binding.expStatusTv.setBackgroundResource(R.drawable.shape_pending);
                binding.btnEdit.setVisibility(View.VISIBLE);
                binding.approveLayout.btnApprove.setVisibility(View.GONE);
                binding.approveLayout.btnReject.setVisibility(View.GONE);
                break;
            case "2":

                binding.expStatusTv.setText("Approved");
                binding.expStatusTv.setTextColor(Color.parseColor("#ffffff"));
                binding.expStatusTv.setBackgroundResource(R.drawable.shape_approved);
                binding.btnEdit.setVisibility(View.GONE);
                binding.approveLayout.btnApprove.setVisibility(View.GONE);
                binding.approveLayout.btnReject.setVisibility(View.GONE);
                break;
            case "3":
                binding.expStatusTv.setText("Rejected");
                binding.expStatusTv.setTextColor(Color.parseColor("#ffffff"));
                binding.expStatusTv.setBackgroundResource(R.drawable.shape_reject);
                binding.btnEdit.setVisibility(View.GONE);
                binding.approveLayout.btnApprove.setVisibility(View.GONE);
                binding.approveLayout.btnReject.setVisibility(View.GONE);
                break;
        }

        try {
            madapter = new _expense_detailsAdapter(aInfoDatas.getaDetailListDAO(), 1);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(ExpenceClaimViewActivity.this);
            binding.rvExp.setLayoutManager(mLayoutManager);
            binding.rvExp.setItemAnimator(new DefaultItemAnimator());
            binding.rvExp.setAdapter(madapter);
            madapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
            e.getMessage();
        }

        binding.btnEdit.setOnClickListener(v -> {
            Constants.WHO = "TeamExpViewAC";
            Intent goto_exp = new Intent(ExpenceClaimViewActivity.this, ExpanseClamActivity.class);
            Gson gson = new Gson();
            String myJson = gson.toJson(aInfoDatas);
            //goto_exp.putExtra("EditMode","On");
            goto_exp.putExtra("teamEditdata", myJson);
            startActivity(goto_exp);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_in_right);
        });
    }
    public void initCommentPop() {
        popComment = new Dialog(ExpenceClaimViewActivity.this);
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
                ApproveExpRQ req=new ApproveExpRQ();

                int step = elt.getStep();
                int fstep = step + 1;

                req.setExpanseApprovalId(0);
                req.setFromEmpId(empid);
                req.setToEmpId(next);
                req.setTableId(elt.getExpenseClaimID());
                req.setStatus("Rejected");//Accepted==approve
                req.setType(elt.getType());
                req.setStep(fstep);
                req.setEntryByApp(String.valueOf(empid));
                String entrydate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
                String entrytime = new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(new Date());
                req.setEntryDateApp(entrydate);
                req.setEntryTimeApp(entrytime);
                req.setMenuId(356); //Expense
                req.setComments(userComment.getText().toString());
                presenter.SaveExpenseCTeam(req);
                SnackBarManagement._success_CustomMessage(v1, "Rejected");
                popComment.dismiss();
                onBackPressed();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
            }
        });
    }
    @Override
    public void onExpenseCTeamListGet(List<ExpListTeam> aList) {
    }
    @Override
    public void onSaveSuccess(String message) {
        if(message.equals("Approved"))
        {
            new androidx.appcompat.app.AlertDialog.Builder(ExpenceClaimViewActivity.this)
                    .setTitle("Success")
                    .setMessage("You Approved Expense")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            onBackPressed();
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                            //finish();
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