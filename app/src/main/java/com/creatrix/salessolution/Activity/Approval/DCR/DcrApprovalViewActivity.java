package com.creatrix.salessolution.Activity.Approval.DCR;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toolbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.creatrix.salessolution.Activity.Approval.MapActivity;
import com.creatrix.salessolution.Interface.IDcrApproval;
import com.creatrix.salessolution.Presenter.DcrApprovalPresenter;
import com.creatrix.salessolution.R;
import com.creatrix.salessolution.UtilityHelper.SessionManagement;
import com.creatrix.salessolution.UtilityHelper.SnackBarManagement;
import com.creatrix.salessolution.databinding.ActivityDcrApprovalViewBinding;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class DcrApprovalViewActivity extends AppCompatActivity implements IDcrApproval.View {
    ActivityDcrApprovalViewBinding binding;
    DcrApprovalPresenter presenter;
    DcrApprovalData aInfoData;
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
        binding = ActivityDcrApprovalViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        presenter = new DcrApprovalPresenter(this, DcrApprovalViewActivity.this);
        binding.toolbarCustom.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });
        session = new SessionManagement(DcrApprovalViewActivity.this);
        userInfo = session.getUserDetails();

        RoleTypeId = Integer.parseInt(userInfo.get(SessionManagement.KEY_EmpRoleTypeId));
        roleType = userInfo.get(SessionManagement.KEY_EmpRoleType);
        empid = Integer.parseInt(userInfo.get(SessionManagement.KEY_EmpId));



        Gson gson = new Gson();
        aInfoData = gson.fromJson(getIntent().getStringExtra("myjson"), DcrApprovalData.class);
        LoadView(aInfoData);
        binding.approveLayout.btnApprove.setOnClickListener(v -> {
            DcrApprovalRQ req = new DcrApprovalRQ();
            int step = aInfoData.getStep();
            int fstep = step + 1;

            req.setDCRApprovalId(0);
            req.setFromEmpId(empid);
            req.setToEmpId(next);
            req.setTableId(aInfoData.getDcrId());
            req.setStatus("Verified");//Accepted==approve for Admin
            req.setType(aInfoData.getType());
            req.setStep(fstep);
            req.setEntryByApp(empid);
            String entrydate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
            String entrytime = new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(new Date());
            req.setEntryDateApp(entrydate);
            req.setEntryTimeApp(entrytime);
            req.setMenuId(382);
            presenter.SaveDcrApproval(req);
        });
        initCommentPop();
        binding.approveLayout.btnReject.setOnClickListener(v -> {
            popComment.show();
        });
    }

    private void LoadView(DcrApprovalData aInfoData) {
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


        binding.dcrDNameTv.setText(aInfoData.getaDCRMasterDAO().getDoctorName());
        binding.dcrCdateTv.setText(aInfoData.getEmpName());
        binding.dcrVisitedTv.setText(aInfoData.getaDCRMasterDAO().getDcrDate());
        binding.dcrVisitTypeTv.setText(aInfoData.getaDCRMasterDAO().getVisitTypeName());
        binding.dcrChamberTv.setText(aInfoData.getaDCRMasterDAO().getChemberName());
        binding.address.setText(aInfoData.getaDCRMasterDAO().getStreetAddress());
        binding.viewmap.setOnClickListener(v -> {
            Intent gotomap = new Intent(DcrApprovalViewActivity.this, MapActivity.class);
            gotomap.putExtra("lat", aInfoData.getaDCRMasterDAO().getLatitude());
            gotomap.putExtra("lon", aInfoData.getaDCRMasterDAO().getLongitude());
            gotomap.putExtra("address", aInfoData.getaDCRMasterDAO().getStreetAddress());
            startActivity(gotomap);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        });
        ArrayList<String> brands=new ArrayList<>();
        for (int i=0;i<aInfoData.getaDCRMasterDAO().getDoctorBrand().size();i++)
        {
            String bname=aInfoData.getaDCRMasterDAO().getDoctorBrand().get(i).getBrandName();
            brands.add(bname);
        }
        ArrayList<String> visit=new ArrayList<>();
        for (int i=0;i<aInfoData.getaDCRMasterDAO().getVisitedWithDAO().size();i++)
        {
            String vname=aInfoData.getaDCRMasterDAO().getVisitedWithDAO().get(i).getEmpName();
            visit.add(vname);
        }


        ArrayList<DcrApplogProduct> productList=new ArrayList<>();
        DcrApplogProduct pd;
        for (int i=0;i<aInfoData.getaDCRMasterDAO().getaDCRProductDAOList().size();i++)
        {
            pd =new DcrApplogProduct();
            String pname=aInfoData.getaDCRMasterDAO().getaDCRProductDAOList().get(i).getProductName();
            int pqty=aInfoData.getaDCRMasterDAO().getaDCRProductDAOList().get(i).getProductQty();
            pd.setProductName(pname);
            pd.setProductQty(pqty);
            productList.add(pd);
        }

        ProductCustomAdapter adapter = new ProductCustomAdapter(DcrApprovalViewActivity.this, R.layout.lv_product, productList);
        binding.productlv.setAdapter(adapter);
        LviewHelper.getListViewSize(binding.productlv);

        ArrayAdapter<String> brand=new ArrayAdapter<>(this,R.layout.lv_dcrbrand,R.id.dcrbrand,brands);
        binding.brandlv.setAdapter(brand);
        LviewHelper.getListViewSize(binding.brandlv);


        ArrayAdapter<String> visited=new ArrayAdapter<>(this,R.layout.lv_dcrbrand,R.id.dcrbrand,visit);
        binding.visitedlv.setAdapter(visited);
        LviewHelper.getListViewSize(binding.visitedlv);

        binding.cmnt.setText(aInfoData.getaDCRMasterDAO().getRemarks());

        binding.dcrMarketTv.setText(aInfoData.getaDCRMasterDAO().getMarketName());
        try {
            if(aInfoData.getApprovalStatus().equals("0"))
            {
                binding.daStatusTv.setText("Pending");
                binding.daStatusTv.setTextColor(Color.parseColor("#ff7400"));
            }else if(aInfoData.getApprovalStatus().equals("1")){
                binding.daStatusTv.setText("Verified");
                binding.daStatusTv.setTextColor(Color.parseColor("#4169e1"));
            }
            else if(aInfoData.getApprovalStatus().equals("2")){
                binding.daStatusTv.setText("Approved");
                binding.daStatusTv.setTextColor(Color.parseColor("#00b248"));
            }
            else if(aInfoData.getApprovalStatus().equals("3")){
                binding.daStatusTv.setText("Rejected");
                binding.daStatusTv.setTextColor(Color.parseColor("#C12222"));
            }
        } catch (Exception exception) {
            exception.printStackTrace();
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
            }
           else if (aInfoData.getToRoleTypeId() == RoleTypeId) {
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
        popComment = new Dialog(DcrApprovalViewActivity.this);
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

                DcrApprovalRQ req = new DcrApprovalRQ();

                int step = aInfoData.getStep();
                int fstep = step + 1;
                req.setDCRApprovalId(0);
                req.setFromEmpId(empid);
                req.setToEmpId(next);
                req.setTableId(aInfoData.getDcrId());
                req.setStatus("Rejected");//Accepted==approve for admin
                req.setType(aInfoData.getType());
                req.setStep(fstep);
                req.setEntryByApp(empid);
                String entrydate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
                String entrytime = new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(new Date());
                req.setEntryDateApp(entrydate);
                req.setEntryTimeApp(entrytime);
                req.setMenuId(382); //Expense
                req.setComments(userComment.getText().toString());
                presenter.SaveDcrApproval(req);
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
    public void onGetDcrApprovalList(List<DcrApprovalData> aList) {

    }

    @Override
    public void onSaveSuccess(String message) {
        new AlertDialog.Builder(this)
                .setTitle("Success")
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        session = new SessionManagement(getApplicationContext());
                        ((AlertDialog) dialog).getButton(which).setVisibility(View.INVISIBLE);
                        dialog.cancel();
                    /*    Intent i = new Intent(SampleOrderActivity.this, CustomerActivity.class);
                        startActivity(i);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);*/
                        onBackPressed();
                        finish();
                    }
                }).setCancelable(false).show();
    }

    @Override
    public void onError(String message) {

    }
  /*  @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent mIntent = new Intent(DcrApprovalViewActivity.this, DcrApprovalListActivity.class);
        // mIntent.addFlags(mIntent.FLAG_ACTIVITY_CLEAR_TOP | mIntent.FLAG_ACTIVITY_CLEAR_TASK | mIntent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mIntent);
        finish();
    }*/
}