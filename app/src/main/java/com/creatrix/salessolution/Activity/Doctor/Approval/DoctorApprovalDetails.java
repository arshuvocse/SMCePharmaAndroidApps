package com.creatrix.salessolution.Activity.Doctor.Approval;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toolbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.creatrix.salessolution.Activity.Doctor.AddDoctor.DoctorActivity;
import com.creatrix.salessolution.Activity.Doctor.Approval.Model.ChembarTypeWithName;
import com.creatrix.salessolution.Activity.Doctor.Approval.Model.DoctorApprovalList;
import com.creatrix.salessolution.Activity.Doctor.Approval.Model.DoctorApprovalRQ;
import com.creatrix.salessolution.R;
import com.creatrix.salessolution.Services.Constants;
import com.creatrix.salessolution.UtilityHelper.SessionManagement;
import com.creatrix.salessolution.UtilityHelper.SnackBarManagement;
import com.creatrix.salessolution.databinding.ActivityDoctorApprovalDetailsBinding;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class DoctorApprovalDetails extends AppCompatActivity implements IDoctorApproval.View {
    ActivityDoctorApprovalDetailsBinding binding;
    DoctorApprovalList dl;
    Dialog popComment;
    Gson gson = new Gson();
    DoctorApprovalPresenter presenter;
    SessionManagement session;
    HashMap<String, String> userInfo = new HashMap<>();


    private int prev = 0;
    private int current = 0;
    private int next = 0;
    private int myrole = 0;

    int RoleTypeId, empid;
    String roleType;
    String prev_roleType, next_roleType;
    Button submitCmnt;
    ArrayList<String> list;

    ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDoctorApprovalDetailsBinding.inflate(getLayoutInflater());
        //setContentView(R.layout.activity_doctor_approval_details);
        setContentView(binding.getRoot());

        pd=new ProgressDialog(DoctorApprovalDetails.this);
        presenter = new DoctorApprovalPresenter(DoctorApprovalDetails.this, this);
        session = new SessionManagement(DoctorApprovalDetails.this);
        userInfo = session.getUserDetails();

        RoleTypeId = Integer.parseInt(userInfo.get(SessionManagement.KEY_EmpRoleTypeId));
        roleType = userInfo.get(SessionManagement.KEY_EmpRoleType);
        empid = Integer.parseInt(userInfo.get(SessionManagement.KEY_EmpId));
        switch (getIntent().getStringExtra("Who")) {
             /*  case "DocTeamApproveAdapter":
             *//*aInfoData = gson.fromJson(getIntent().getStringExtra("MilDetails"), MilageClaimReport.class);
                LoadViewData(aInfoData);*//*
                break;*/
            case "DocTeamApproveAdapter":
                dl = gson.fromJson(getIntent().getStringExtra("DocTLDetails"), DoctorApprovalList.class);
                //presenter.GetExpenseDetails(getIntent().getIntExtra("ExpenseClaimID", 0));
                LoadViewTeamData(dl);
                binding.edit.setOnClickListener(v -> {
                    Constants.WHO = "DocApprovalAC";
                    Intent goto_exp = new Intent(DoctorApprovalDetails.this, DoctorActivity.class);
                    Gson gson = new Gson();
                    String myJson = gson.toJson(dl.getDoctorMasterDao());
                    //    goto_exp.putExtra("EditMode","On");
                    goto_exp.putExtra("DocEditdata", myJson);
                    startActivity(goto_exp);
                    // overridePendingTransition(R.anim.slide_in_right, R.anim.slide_in_right);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    finish();
                });
                binding.approveLayout.approvemaster.setVisibility(View.VISIBLE);
                binding.approveLayout.btnApprove.setOnClickListener(v -> {
                    DoctorApprovalRQ req = new DoctorApprovalRQ();
                    int step = dl.getStep();
                    int fstep = step + 1;

                    req.setDoctorApprovalId(0);
                    req.setFromEmpId(empid);
                    req.setToEmpId(next);
                    req.setTableId(dl.getDoctorId());
                    req.setStatus("Verified");//Accepted==approve for Admin
                    req.setType(dl.getType());
                    req.setStep(fstep);
                    req.setEntryByApp(empid);
                    String entrydate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
                    String entrytime = new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(new Date());
                    req.setEntryDateApp(entrydate);
                    req.setEntryTimeApp(entrytime);
                    req.setMenuId(303);
                    pd.setTitle("Submitting...");
                    pd.setCancelable(true);
                    pd.show();
                    presenter.SaveDoctorApprovalList(req);
                });
                initCommentPop();
                binding.approveLayout.btnReject.setOnClickListener(v -> {
                    popComment.show();
                });
                break;
        }
    }

    private void LoadViewTeamData(DoctorApprovalList dl) {
        if (dl.getApprovalStatus().equals("1")) {
            binding.approveLayout.btnApprove.setVisibility(View.GONE);
            binding.approveLayout.btnReject.setVisibility(View.GONE);
        }
        if (RoleTypeId == 2) {
            prev = dl.getMIOEmpId();
            prev_roleType = "MIO";
            current = dl.getASMEMPId();
            next = dl.getRSMEMPId();
            next_roleType = "AM";
            myrole = 2;
        }
        if (RoleTypeId == 3) {
            prev = dl.getASMEMPId();
            prev_roleType = "AM";
            current = dl.getRSMEMPId();
            next = dl.getNSMEMPId();
            next_roleType = "DZSM";
            myrole = 3;
        }
        if (RoleTypeId == 4) {
            prev = dl.getRSMEMPId();
            prev_roleType = "DZSM";
            current = dl.getNSMEMPId();
            next_roleType = "ADMIN";
            next = 0;
            myrole = 4;
        }
        if (RoleTypeId == 5) {
            myrole = 5;
        }
        //TODO:Button On off
        if (prev == current) {
            if (dl.getRoleTypeId() == RoleTypeId) {
                binding.approveLayout.btnApprove.setVisibility(View.GONE);
                binding.approveLayout.btnReject.setVisibility(View.GONE);
                binding.edit.setVisibility(View.GONE);
                binding.warnToast.setVisibility(View.VISIBLE);
                binding.warnToast.setText("Waiting For Final Approval");
                binding.warnToast.setBackgroundResource(R.drawable.shape_approved);
            }
            else {
                //new Implementation for vacant
                if (dl.getToRoleTypeId() == RoleTypeId) {
                    binding.edit.setVisibility(View.VISIBLE);
                    binding.approveLayout.btnApprove.setVisibility(View.VISIBLE);
                    binding.approveLayout.btnReject.setVisibility(View.VISIBLE);
                }
                else {
                    binding.approveLayout.btnApprove.setVisibility(View.GONE);
                    binding.approveLayout.btnReject.setVisibility(View.GONE);
                    binding.edit.setVisibility(View.GONE);
                    if (dl.getRoleTypeId() >= RoleTypeId) {
                        binding.warnToast.setVisibility(View.VISIBLE);
                        binding.warnToast.setText("Waiting For Final Approval");
                        binding.warnToast.setBackgroundResource(R.drawable.shape_approved);
                    } else {
                        binding.warnToast.setVisibility(View.VISIBLE);
                        binding.warnToast.setText("Need To Approved By " + prev_roleType);
                        binding.warnToast.setBackgroundResource(R.drawable.shape_pending);
                    }
                }
            }
        } else {
            if (dl.getToRoleTypeId() == RoleTypeId) {
                binding.edit.setVisibility(View.VISIBLE);
                binding.approveLayout.btnApprove.setVisibility(View.VISIBLE);
                binding.approveLayout.btnReject.setVisibility(View.VISIBLE);
            } else {
                binding.approveLayout.btnApprove.setVisibility(View.GONE);
                binding.approveLayout.btnReject.setVisibility(View.GONE);
                binding.edit.setVisibility(View.GONE);
                if (dl.getRoleTypeId() >= RoleTypeId) {
                    binding.warnToast.setVisibility(View.VISIBLE);
                    binding.warnToast.setText("Waiting For Final Approval");
                    binding.warnToast.setBackgroundResource(R.drawable.shape_approved);
                } else {
                    binding.warnToast.setVisibility(View.VISIBLE);
                    binding.warnToast.setText("Need To Approved By " + prev_roleType);
                    binding.warnToast.setBackgroundResource(R.drawable.shape_pending);
                }
            }

        }

        List<ChembarTypeWithName> cl = dl.getDoctorMasterDao().getaChemberListDAO();
        binding.tvName.setText(dl.getDoctorMasterDao().getDoctorName());
        // binding.tvDesig.setText(dl.getDoctorMasterDao().getDesignationName());
        //binding.tvDegree.setText(dl.getDoctorMasterDao().getDegreeStr());
        //binding.tvSpeciality.setText(dl.getDoctorMasterDao().getSpecialityStr());
        if(dl.getDoctorMasterDao().getDesignationName()!=null)
        {
            binding.tvDesig.setText(dl.getDoctorMasterDao().getDesignationName());
        }else {
            binding.tvDesig.setText("--------");
        }
        if(dl.getDoctorMasterDao().getDegreeStr()!=null)
        {
            binding.tvDegree.setText(dl.getDoctorMasterDao().getDegreeStr());
        }else {
            binding.tvDegree.setText("--------");
        }
        if(dl.getDoctorMasterDao().getSpecialityStr()!=null)
        {
            binding.tvSpeciality.setText(dl.getDoctorMasterDao().getSpecialityStr());
        }else {
            binding.tvSpeciality.setText("--------");
        }

       // binding.brandListStr.setText(dl.getDoctorMasterDao().getBrandStr());

        binding.doctype.setText(dl.getDoctorMasterDao().getDoctorTypeStr());
        //--------
        if(dl.getDoctorMasterDao().getInstitutionSTr()!=null)
        {
            binding.tvInsstr.setText(dl.getDoctorMasterDao().getInstitutionSTr());
        }else {
            binding.tvInsstr.setText("--------");
        }
        if(dl.getDoctorMasterDao().getBrandStr()!=null)
        {
            binding.tvBrandstr.setText(dl.getDoctorMasterDao().getBrandStr());
        }else {
            binding.tvBrandstr.setText("--------");
        }
        binding.category.setText(dl.getDoctorMasterDao().getCategoryName());


        if(dl.getDoctorMasterDao().getContact()!=null)
        {
            binding.tvContactstr.setText(dl.getDoctorMasterDao().getContact());
        }else {
            binding.tvContactstr.setText("--------");
        }
        binding.tvProviderstr.setText(dl.getDoctorMasterDao().getProgramTypeName());
        if(dl.getDoctorMasterDao().getSMCTypeName()!=null)
        {
            binding.tvPharmaPlatstr.setText(dl.getDoctorMasterDao().getSMCTypeName());
        }else {
            binding.tvPharmaPlatstr.setText("--------");
        }
        if(dl.getDoctorMasterDao().getSpeciaDateStr()!=null)
        {
            binding.tvSpcldaystr.setText(dl.getDoctorMasterDao().getSpeciaDateStr());
        }else {
            binding.tvSpcldaystr.setText("--------");
        }

        binding.area.setText(dl.getDoctorMasterDao().getAreaName());
        binding.teri.setText(dl.getDoctorMasterDao().getTerritoryName());
        binding.steri.setText(dl.getDoctorMasterDao().getSubTerritoryName());
        binding.market.setText(dl.getDoctorMasterDao().getMarketName());

        list = new ArrayList<String>();
        for (int i = 0; i < cl.size(); i++) {
            String a = cl.get(i).getChamberName();
            list.add(a);
        }
        String listString = "";
        for (String s : list) {
            listString += s + "|";
        }
        binding.tvChamberstr.setText(listString);
    }
    public void initCommentPop() {
        popComment = new Dialog(DoctorApprovalDetails.this);
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
                DoctorApprovalRQ req = new DoctorApprovalRQ();

                int step = dl.getStep();
                int fstep = step + 1;
                req.setDoctorApprovalId(0);
                req.setFromEmpId(empid);
                req.setToEmpId(next);
                req.setTableId(dl.getDoctorId());
                req.setStatus("Rejected");//Accepted==approve for admin
                req.setType(dl.getType());
                req.setStep(fstep);
                req.setEntryByApp(empid);
                String entrydate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
                String entrytime = new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(new Date());
                req.setEntryDateApp(entrydate);
                req.setEntryTimeApp(entrytime);
                req.setMenuId(303); //Expense
                req.setComments(userComment.getText().toString());
                presenter.SaveDoctorApprovalList(req);
                SnackBarManagement._success_CustomMessage(v1, "Rejected");
                popComment.dismiss();
                onBackPressed();
                //overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            }
        });
    }
    @Override
    public void OnRevieveDoctorApproval(List<DoctorApprovalList> aList) {

    }

    @Override
    public void OnSuccess(String msg) {
      /*  if(msg.equals("Verified"))
        {
            onBackPressed();
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            finish();
        }*/
        if(pd!=null || pd.isShowing())
        {
            pd.dismiss();
        }
        new AlertDialog.Builder(this)
                .setTitle("Success")
                .setMessage(msg)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        session = new SessionManagement(getApplicationContext());
                        ((AlertDialog) dialog).getButton(which).setVisibility(View.INVISIBLE);
                        dialog.cancel();
                        onBackPressed();
                        finish();
                    }
                }).setCancelable(false).show();
    }

    @Override
    public void OnError(int type) {

    }

   /* @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent mIntent = new Intent(DoctorApprovalDetails.this, DoctorApprovalListActivity.class);
        // mIntent.addFlags(mIntent.FLAG_ACTIVITY_CLEAR_TOP | mIntent.FLAG_ACTIVITY_CLEAR_TASK | mIntent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mIntent);
        finish();
    }*/
}