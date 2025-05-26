package com.creatrix.salessolution.Activity.Customer.Approval;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.creatrix.salessolution.Activity.Customer.Approval.Model.CustomerApprovalList;
import com.creatrix.salessolution.Activity.Customer.Approval.Model.CustomerApprovalPresenter;
import com.creatrix.salessolution.Activity.Customer.Approval.Model.CustomerApprovalRQ;
import com.creatrix.salessolution.Activity.Customer.Approval.Model.ICustomerApproval;
import com.creatrix.salessolution.Activity.Customer.CustomerActivity;
import com.creatrix.salessolution.Activity.Customer.CustomerSvModel;
import com.creatrix.salessolution.R;
import com.creatrix.salessolution.Services.Constants;
import com.creatrix.salessolution.UtilityHelper.SessionManagement;
import com.creatrix.salessolution.UtilityHelper.SnackBarManagement;
import com.creatrix.salessolution.databinding.ActivityCustomerApprovalBinding;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class CustomerApprovalActivity extends AppCompatActivity implements ICustomerApproval.View {
    ActivityCustomerApprovalBinding binding;
    CustomerApprovalList cl;
    Dialog popComment;
    Gson gson = new Gson();
    CustomerApprovalPresenter presenter;
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
    ArrayList<String> list ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCustomerApprovalBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        presenter = new CustomerApprovalPresenter(CustomerApprovalActivity.this, this);
        session = new SessionManagement(CustomerApprovalActivity.this);
        userInfo = session.getUserDetails();

        RoleTypeId = Integer.parseInt(Objects.requireNonNull(userInfo.get(SessionManagement.KEY_EmpRoleTypeId)));
        roleType = userInfo.get(SessionManagement.KEY_EmpRoleType);
        empid = Integer.parseInt(Objects.requireNonNull(userInfo.get(SessionManagement.KEY_EmpId)));
        switch (getIntent().getStringExtra("Who")) {
            /*  case "DocTeamApproveAdapter":
             *//*aInfoData = gson.fromJson(getIntent().getStringExtra("MilDetails"), MilageClaimReport.class);
                LoadViewData(aInfoData);*//*
                break;*/

            case "CustTeamApproveAdapter":
                cl = gson.fromJson(getIntent().getStringExtra("CustTLDetails"), CustomerApprovalList.class);
                LoadViewTeamData(cl);
                if (cl.getApprovalStatus().equals("1")) {
                    binding.approveLayouts.btnApprove.setVisibility(View.GONE);
                    binding.approveLayouts.btnReject.setVisibility(View.GONE);
                }
                if (RoleTypeId == 2) {
                    prev = cl.getMIOEmpId();
                    prev_roleType = "MIO";
                    current = cl.getASMEMPId();
                    next = cl.getRSMEMPId();
                    next_roleType = "AM";
                    myrole = 2;
                }
                if (RoleTypeId == 3) {
                    prev = cl.getASMEMPId();
                    prev_roleType = "AM";
                    current = cl.getRSMEMPId();
                    next = cl.getNSMEMPId();
                    next_roleType = "DZSM";
                    myrole = 3;
                }
                if (RoleTypeId == 4) {
                    prev = cl.getRSMEMPId();
                    prev_roleType = "DZSM";
                    current = cl.getNSMEMPId();
                    next_roleType = "ADMIN";
                    next = 0;
                    myrole = 4;
                }
                if (RoleTypeId == 5) {
                    myrole = 5;
                }
                //TODO:Button On off
                if (prev == current) {
                    if (cl.getRoleTypeId() == RoleTypeId) {
                        binding.approveLayouts.btnApprove.setVisibility(View.GONE);
                        binding.approveLayouts.btnReject.setVisibility(View.GONE);
                        binding.btnEdit.setVisibility(View.GONE);
                        binding.warnToast.setVisibility(View.VISIBLE);
                        binding.warnToast.setText("Waiting For Final Approval");
                        binding.warnToast.setBackgroundResource(R.drawable.shape_approved);
                    } else {
                        //new Implementation
                        if (cl.getToRoleTypeId() == RoleTypeId) {
                            binding.btnEdit.setVisibility(View.VISIBLE);
                            binding.approveLayouts.btnApprove.setVisibility(View.VISIBLE);
                            binding.approveLayouts.btnReject.setVisibility(View.VISIBLE);
                        } else {
                            binding.approveLayouts.btnApprove.setVisibility(View.GONE);
                            binding.approveLayouts.btnReject.setVisibility(View.GONE);
                            binding.btnEdit.setVisibility(View.GONE);
                            if (cl.getRoleTypeId() >= RoleTypeId) {
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
                    if (cl.getToRoleTypeId() == RoleTypeId) {
                        // binding.approveLayout.approvemaster.setVisibility(View.VISIBLE);
                        binding.btnEdit.setVisibility(View.VISIBLE);
                        binding.approveLayouts.btnApprove.setVisibility(View.VISIBLE);
                        binding.approveLayouts.btnReject.setVisibility(View.VISIBLE);
                    } else {
                        binding.approveLayouts.btnApprove.setVisibility(View.GONE);
                        binding.approveLayouts.btnReject.setVisibility(View.GONE);
                        binding.btnEdit.setVisibility(View.GONE);
                        if (cl.getRoleTypeId() >= RoleTypeId) {
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
                binding.btnEdit.setOnClickListener(v -> {
                    Constants.WHO = "CustApprovalAC";
                    Intent goto_exp = new Intent(CustomerApprovalActivity.this, CustomerActivity.class);
                    Gson gson = new Gson();
                    String myJson = gson.toJson(cl);
                    goto_exp.putExtra("Editdata", myJson);
                    startActivity(goto_exp);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_in_right);
                });
                binding.approveLayouts.approvemaster.setVisibility(View.VISIBLE);
                binding.approveLayouts.btnApprove.setOnClickListener(v -> {
                    CustomerApprovalRQ req = new CustomerApprovalRQ();
                    int step = cl.getStep();
                    int fstep = step + 1;

                    req.setCustomerApprovalId(0);
                    req.setFromEmpId(empid);
                    req.setToEmpId(next);
                    req.setTableId(cl.getCustomerSMListDao().getCustomerMasterId());
                    req.setStatus("Verified");//Accepted==approve for Admin
                    req.setType(cl.getType());
                    req.setStep(fstep);
                    req.setEntryByApp(empid);
                    String entrydate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
                    String entrytime = new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(new Date());
                    req.setEntryDateApp(entrydate);
                    req.setEntryTimeApp(entrytime);
                    req.setMenuId(302);

                    Gson gson=new Gson();
                    String approv=gson.toJson(req);
                    System.out.println("systen"+approv);
                    presenter.SaveCustomerApprovalList(req);
                });
                initCommentPop();
                binding.approveLayouts.btnReject.setOnClickListener(v -> {
                    popComment.show();
                });

                break;
        }
    }
    private void LoadViewTeamData(CustomerApprovalList cl) {
        CustomerSvModel csl =  cl.getCustomerSMListDao();
        binding.custDetailsLayout.tvName.setText(csl.getCustomerName());
        binding.custDetailsLayout.tvAddress.setText(csl.getAddress());
        binding.custDetailsLayout.tvMobile.setText(csl.getCellNo());
        binding.custDetailsLayout.tvMarket.setText(csl.getMarketName());

        if(!csl.getImageBase64String().equals("")||!csl.getTradeLicenseImg().equals(""))
        {
            binding.custDetailsLayout.llimages.setVisibility(View.VISIBLE);
        }else {
            binding.custDetailsLayout.llimages.setVisibility(View.GONE);
        }
        if(!csl.getImageBase64String().equals(""))
        {
            binding.custDetailsLayout.ivShop.setVisibility(View.VISIBLE);
            Glide.with(CustomerApprovalActivity.this).load(csl.getImageBase64String())
                    .into(binding.custDetailsLayout.ivShop);
        }else {
            binding.custDetailsLayout.ivShop.setVisibility(View.GONE);
        }
        if(!csl.getTradeLicenseImg().equals(""))
        {
            binding.custDetailsLayout.ivTrade.setVisibility(View.VISIBLE);
            Glide.with(CustomerApprovalActivity.this).load(csl.getTradeLicenseImg())
                    .into(binding.custDetailsLayout.ivTrade);
        }else {
            binding.custDetailsLayout.ivTrade.setVisibility(View.GONE);
        }

        binding.tvPharmaPlatstr.setText(cl.getCustomerSMListDao().getSMCTypeName());
        binding.ownName.setText(cl.getCustomerSMListDao().getConPerson());
        binding.tvNid.setText(cl.getCustomerSMListDao().getVoterID());
        binding.tvTradelc.setText(cl.getCustomerSMListDao().getTradeLicense());
        binding.tvProvidertype.setText(cl.getCustomerSMListDao().getProgramTypeName());

        binding.zone.setText(cl.getCustomerSMListDao().getZoneName());
        binding.area.setText(cl.getCustomerSMListDao().getAreaName());
        binding.teri.setText(cl.getCustomerSMListDao().getTerritory());
        binding.steri.setText(cl.getCustomerSMListDao().getSubterritory());
        binding.market.setText(cl.getCustomerSMListDao().getMarketName());
      /*  if(cl.getCustomerSMListDao().getImageBase64String()!=null)
        {
            byte[] decodedString = Base64.decode(cl.getCustomerSMListDao().getImageBase64String(), Base64.DEFAULT);
            decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            binding.custDetailsLayout.ivShop.setImageBitmap(decodedByte);
        }
        if(cl.getCustomerSMListDao().getTradeLicenseImg()!=null)
        {
            byte[] decodedString = Base64.decode(cl.getCustomerSMListDao().getTradeLicenseImg(), Base64.DEFAULT);
            decodedByte2 = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            binding.custDetailsLayout.ivTrade.setImageBitmap(decodedByte);
        }*/
       /* list = new ArrayList<String>();
        for(int i=0;i<cl.size();i++)
        {
            String a= cl.get(i).getChamberName();
            list.add(a);
        }
        String listString = "";
        for (String s : list) {
            listString += s + "|";
        }
        binding.tvChamberstr.setText(listString);*/

    }

    public void initCommentPop() {
        popComment = new Dialog(CustomerApprovalActivity.this);
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
                CustomerApprovalRQ req = new CustomerApprovalRQ();
                int step = cl.getStep();
                int fstep = step + 1;
                req.setCustomerApprovalId(0);
                req.setFromEmpId(empid);
                req.setToEmpId(next);
                req.setTableId(cl.getCustomerMasterId());
                req.setStatus("Rejected");//Accepted==approve for admin
                req.setType(cl.getType());
                req.setStep(fstep);
                req.setEntryByApp(empid);
                String entrydate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
                String entrytime = new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(new Date());
                req.setEntryDateApp(entrydate);
                req.setEntryTimeApp(entrytime);
                req.setMenuId(6); //Customer
                req.setComments(userComment.getText().toString());
                presenter.SaveCustomerApprovalList(req);
                SnackBarManagement._success_CustomMessage(v1, "Rejected");
                popComment.dismiss();
                onBackPressed();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
            }
        });
    }
    @Override
    public void OnRevieveCustomerApproval(List<CustomerApprovalList> aList) {

    }

    @Override
    public void OnSuccess(String msg) {
          if(msg.equals("Verified"))
          {
              new androidx.appcompat.app.AlertDialog.Builder(CustomerApprovalActivity.this)
                      .setTitle("Success")
                      .setMessage("You Approved Customer")
                      .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                          @Override
                          public void onClick(DialogInterface dialog, int which) {
                              dialog.cancel();
                              onBackPressed();
                              finish();
                             // finishAffinity();
                          }

                      }).setCancelable(false).show();
          }
    }

    @Override
    public void OnError(int type) {

    }
}