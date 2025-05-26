package com.creatrix.salessolution.Activity.Approval.Order;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.creatrix.salessolution.Activity.Doctor.AddDoctor.DoctorActivity;
import com.creatrix.salessolution.Activity.Doctor.Approval.DoctorApprovalDetails;
import com.creatrix.salessolution.Activity.OrderMainActivity;
import com.creatrix.salessolution.Interface.IOrderApproval;
import com.creatrix.salessolution.Model.Customer;
import com.creatrix.salessolution.Presenter.OrderApprovalPresenter;
import com.creatrix.salessolution.R;
import com.creatrix.salessolution.RecyclerAdapter._product_orderpage_adapter;
import com.creatrix.salessolution.Services.Constants;
import com.creatrix.salessolution.UtilityHelper.SessionManagement;
import com.creatrix.salessolution.UtilityHelper.SnackBarManagement;
import com.creatrix.salessolution.databinding.ActivityOrderApprovalViewBinding;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class OrderApprovalViewActivity extends AppCompatActivity implements IOrderApproval.View {
    OrderApprovalPresenter presenter;
    ActivityOrderApprovalViewBinding binding;
    SessionManagement session;
    OrderApprovalData mlt = new OrderApprovalData();
    Dialog popComment;
    private int prev = 0;
    private int current = 0;
    private int next = 0;
    private int myrole = 0;

    int RoleTypeId, empid;
    String roleType;
    String prev_roleType, next_roleType;
    Button submitCmnt;
    private _product_orderpage_adapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_order_approval_view);
        binding = ActivityOrderApprovalViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        presenter = new OrderApprovalPresenter(this, OrderApprovalViewActivity.this);
        session = new SessionManagement(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        empid = Integer.parseInt(user.get(SessionManagement.KEY_EmpId));
        RoleTypeId = Integer.parseInt(user.get(SessionManagement.KEY_EmpRoleTypeId));
        Gson gson = new Gson();
        binding.toolbarCustom.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        switch (Constants.WHO) {
            case "OrderApproveAdapter":
                mlt = gson.fromJson(getIntent().getStringExtra("OrderApprovalDetails"), OrderApprovalData.class);
                LoadViewData(mlt);
                break;
        }
    }
    private void LoadViewData(OrderApprovalData aInfoData) {
    /*    if (RoleTypeId == 2) {
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
        if (prev == current) {
            if (mlt.getRoleTypeId() == RoleTypeId) {
                binding.approveLayout.btnApprove.setVisibility(View.GONE);
                binding.approveLayout.btnReject.setVisibility(View.GONE);
                binding.btnEdit.setVisibility(View.GONE);

                binding.warnToast.setVisibility(View.VISIBLE);
                binding.warnToast.setText("Approved");
                binding.warnToast.setBackgroundResource(R.drawable.shape_approved);
            } else {
                //binding.approveLayout.btnApprove.setVisibility(View.VISIBLE);
                binding.approveLayout.approvemaster.setVisibility(View.VISIBLE);
                binding.btnEdit.setVisibility(View.VISIBLE);
            }
        } else {
            if (mlt.getToRoleTypeId() == RoleTypeId) {
                // binding.approveLayout.approvemaster.setVisibility(View.VISIBLE);
                binding.approveLayout.btnApprove.setVisibility(View.VISIBLE);
                binding.approveLayout.btnReject.setVisibility(View.VISIBLE);
                binding.btnEdit.setVisibility(View.VISIBLE);
            } else {

                binding.approveLayout.btnApprove.setVisibility(View.GONE);
                binding.approveLayout.btnReject.setVisibility(View.GONE);
                binding.btnEdit.setVisibility(View.GONE);
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

      /*  if (aInfoData.getApprovalStatus().equals("1")) {
            //binding.approveLayout.approvemaster.setVisibility(View.GONE);
            binding.approveLayout.btnApprove.setVisibility(View.GONE);
            binding.approveLayout.btnReject.setVisibility(View.GONE);
        }*/

    /*    if (RoleTypeId == 2) {
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
        //TODO:Button On off
        if (prev == current) {
            if (aInfoData.getRoleTypeId() == RoleTypeId) {

                binding.approveLayout.btnApprove.setVisibility(View.GONE);
                binding.approveLayout.btnReject.setVisibility(View.GONE);
                binding.btnEdit.setVisibility(View.GONE);

                binding.warnToast.setVisibility(View.VISIBLE);
                binding.warnToast.setText("Waiting For Final Approval");
                binding.warnToast.setBackgroundResource(R.drawable.shape_approved);
            } else {
                //binding.approveLayout.btnApprove.setVisibility(View.VISIBLE);
                binding.approveLayout.approvemaster.setVisibility(View.VISIBLE);
            }
        } else {
            if (aInfoData.getToRoleTypeId() == RoleTypeId) {
                // binding.approveLayout.approvemaster.setVisibility(View.VISIBLE);
                binding.btnEdit.setVisibility(View.VISIBLE);
                binding.approveLayout.btnApprove.setVisibility(View.VISIBLE);
                binding.approveLayout.btnReject.setVisibility(View.VISIBLE);
            } else {
                binding.approveLayout.btnApprove.setVisibility(View.GONE);
                binding.approveLayout.btnReject.setVisibility(View.GONE);
                binding.btnEdit.setVisibility(View.GONE);

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
               /* "RoleTypeId": 2,
                "ToRoleTypeId": 3,*/

        //TODO:Button On off
        if (prev == current) {
            if (aInfoData.getRoleTypeId() == RoleTypeId) {
                binding.btnEdit.setVisibility(View.GONE);
                binding.approveLayout.btnApprove.setVisibility(View.GONE);
                binding.approveLayout.btnReject.setVisibility(View.GONE);

                binding.warnToast.setVisibility(View.VISIBLE);
                binding.warnToast.setText("Waiting For Final Approval");
                binding.warnToast.setBackgroundResource(R.drawable.shape_approved);
            } else {
                binding.approveLayout.approvemaster.setVisibility(View.VISIBLE);
                binding.approveLayout.btnApprove.setVisibility(View.VISIBLE);
                binding.approveLayout.btnReject.setVisibility(View.VISIBLE);

                //new Implementation for vacant
                if (aInfoData.getToRoleTypeId() == RoleTypeId) {
                    // binding.approveLayout.approvemaster.setVisibility(View.VISIBLE);
                    binding.btnEdit.setVisibility(View.VISIBLE);
                    binding.approveLayout.approvemaster.setVisibility(View.VISIBLE);
                    binding.approveLayout.btnApprove.setVisibility(View.VISIBLE);
                    binding.approveLayout.btnReject.setVisibility(View.VISIBLE);
                } else {
                    binding.approveLayout.approvemaster.setVisibility(View.GONE);
                    binding.approveLayout.btnApprove.setVisibility(View.GONE);
                    binding.approveLayout.btnReject.setVisibility(View.GONE);
                    binding.btnEdit.setVisibility(View.GONE);
                    if (aInfoData.getRoleTypeId() >= RoleTypeId) {
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
            if (aInfoData.getToRoleTypeId() == RoleTypeId) {
                binding.btnEdit.setVisibility(View.VISIBLE);
                binding.approveLayout.approvemaster.setVisibility(View.VISIBLE);
                binding.approveLayout.btnApprove.setVisibility(View.VISIBLE);
                binding.approveLayout.btnReject.setVisibility(View.VISIBLE);
            } else {
                binding.btnEdit.setVisibility(View.GONE);
                binding.approveLayout.approvemaster.setVisibility(View.GONE);
                binding.approveLayout.btnApprove.setVisibility(View.GONE);
                binding.approveLayout.btnReject.setVisibility(View.GONE);

                if (aInfoData.getRoleTypeId() >= RoleTypeId) {
                    binding.warnToast.setVisibility(View.VISIBLE);
                    binding.warnToast.setText("Waiting For Final Approval");
                    binding.warnToast.setBackgroundResource(R.drawable.shape_approved);
                } else {
                    binding.warnToast.setVisibility(View.VISIBLE);
                    binding.warnToast.setText("Need To Approved By " + prev_roleType);
                    binding.warnToast.setBackgroundResource(R.drawable.shape_prepending);
                }
            }

        }

        binding.btnEdit.setOnClickListener(v->{
            Constants.WHO = "OrderApproveViewActivity";
            //Toast.makeText(this, ""+aInfoData.getaCustMasterDAO().getCustomerName(), Toast.LENGTH_SHORT).show();
            // aInfoData.getaCustMasterDAO().getCustomerName();
            Customer cust=aInfoData.getaCustMasterDAO();

            //cust.setCustomerName();
            Intent got_details = new Intent(OrderApprovalViewActivity.this, OrderMainActivity.class);
            Gson gson = new Gson();
            String myJson = gson.toJson(cust);
            String data = gson.toJson(aInfoData);
            got_details.putExtra("myjson", myJson);
            got_details.putExtra("OrderApprovalEdit", data);
            startActivity(got_details);
            overridePendingTransition(R.anim.slide_in_right,R.anim.slide_in_right);
            finish();
        });
        binding.approveLayout.approvemaster.setVisibility(View.VISIBLE);
        binding.approveLayout.btnApprove.setVisibility(View.VISIBLE);
        binding.approveLayout.btnReject.setVisibility(View.VISIBLE);

        binding.approveLayout.btnApprove.setOnClickListener(v -> {
            OrderApprovalSaveBody req = new OrderApprovalSaveBody();
            int step = mlt.getStep();
            int fstep = step + 1;
            // Toast.makeText(context, "empid : "+String.valueOf(empid), Toast.LENGTH_SHORT).show();
            req.setOrderApprovalId(0);
            req.setFromEmpId(empid);
            req.setToEmpId(next);
            req.setTableId(mlt.getaOrderMasterDAO().getOrderId());
            req.setStatus("Verified");//Accepted==approve for Admin
            req.setType(mlt.getType());
            req.setStep(fstep);
            req.setEntryByApp(String.valueOf(empid));
            String entrydate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
            String entrytime = new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(new Date());
            req.setEntryDateApp(entrydate);
            req.setEntryTimeApp(entrytime);
            req.setMenuId(377);
            presenter.SaveOrderApproval(req);
        });
        initCommentPop();

        binding.approveLayout.btnReject.setOnClickListener(v -> {
            popComment.show();
        });

        try {
            OrderMasterDAO dm = aInfoData.getaOrderMasterDAO();
            Customer cm=aInfoData.getaCustMasterDAO();
            binding.shopName.setText(cm.getCustomerName());
            binding.empName.setText(aInfoData.getEmpName());
            binding.createdDate.setText(aInfoData.getEntryDate());
            binding.marketTv.setText(aInfoData.getMarketName());

            binding.totalTPValue.setText(String.valueOf(aInfoData.getaOrderMasterDAO().getTotalTP()));
            binding.totalVatValue.setText(String.valueOf(aInfoData.getaOrderMasterDAO().getTotalVAT()));
            binding.totalValue.setText(String.valueOf(aInfoData.getaOrderMasterDAO().getTotalAmount()));
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        //Toast.makeText(this, "stts "+aInfoData.getApprovalStatus(), Toast.LENGTH_SHORT).show();

        if (aInfoData.getApprovalStatus().equals("0")) {
            binding.statusTv.setText("Pending..");
            // binding.daStatusTv.setTextColor(Color.parseColor("#ff7400"));
            binding.statusTv.setTextColor(Color.parseColor("#ffffff"));
            binding.statusTv.setBackgroundResource(R.drawable.shape_prepending);
        } else if (aInfoData.getApprovalStatus().equals("1")) {
            binding.statusTv.setText("Verified");
            binding.statusTv.setBackgroundResource(R.drawable.shape_pending);
            binding.statusTv.setTextColor(Color.parseColor("#ffffff"));
        } else if (aInfoData.getApprovalStatus().equals("2")) {
            binding.statusTv.setText("Approved");
            binding.statusTv.setBackgroundResource(R.drawable.shape_approved);
            // binding.daStatusTv.setTextColor(Color.parseColor("#00b248"));
            binding.statusTv.setTextColor(Color.parseColor("#ffffff"));
        } else if (aInfoData.getApprovalStatus().equals("3")) {
            binding.statusTv.setText("Rejected");
            binding.statusTv.setBackgroundResource(R.drawable.shape_reject);
            // binding.daStatusTv.setTextColor(Color.parseColor("#C12222"));
            binding.statusTv.setTextColor(Color.parseColor("#ffffff"));
        }
        if (aInfoData.getaOrderMasterDAO().getaOrderDtls() != null) {
            mAdapter = new _product_orderpage_adapter(aInfoData.getaOrderMasterDAO().getaOrderDtls(), this, "ViewOrder");
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            binding.recyclerViewProduct.setLayoutManager(mLayoutManager);
            binding.recyclerViewProduct.setItemAnimator(new DefaultItemAnimator());
            binding.recyclerViewProduct.setAdapter(mAdapter);
            binding.recyclerViewProduct.addItemDecoration(new DividerItemDecoration(getApplicationContext(),
                    DividerItemDecoration.VERTICAL));
            binding.recyclerViewProduct.setItemAnimator(null);
            binding.recyclerViewProduct.scrollToPosition(0);
            mAdapter.notifyDataSetChanged();
        }

    }

    public void initCommentPop() {
        popComment = new Dialog(OrderApprovalViewActivity.this);
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
                OrderApprovalSaveBody req = new OrderApprovalSaveBody();
                int step = mlt.getStep();
                int fstep = step + 1;
                // Toast.makeText(context, "empid : "+String.valueOf(empid), Toast.LENGTH_SHORT).show();
                req.setOrderApprovalId(0);
                req.setFromEmpId(empid);
                req.setToEmpId(next);
                req.setTableId(mlt.getaOrderMasterDAO().getOrderId());
                req.setStatus("Rejected");//Accepted==approve for Admin
                req.setType(mlt.getType());
                req.setStep(fstep);
                req.setEntryByApp(String.valueOf(empid));
                String entrydate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
                String entrytime = new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(new Date());
                req.setEntryDateApp(entrydate);
                req.setEntryTimeApp(entrytime);
                req.setMenuId(377);
                presenter.SaveOrderApproval(req);
                SnackBarManagement._success_CustomMessage(v1, "Rejected");
                popComment.dismiss();
                onBackPressed();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
            }
        });
    }

    @Override
    public void onGetOrderSuccess(List<OrderApprovalData> aList) {

    }

    @Override
    public void onSaveSuccess(String message) {
        new AlertDialog.Builder(this)
                .setTitle("Success")
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ((AlertDialog) dialog).getButton(which).setVisibility(View.INVISIBLE);
                    /*    Intent i = new Intent(SampleOrderActivity.this, CustomerActivity.class);
                        startActivity(i);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);*/
                        finish();
                    }
                }).setCancelable(false).show();
    }

    @Override
    public void onError(String message) {

    }
}