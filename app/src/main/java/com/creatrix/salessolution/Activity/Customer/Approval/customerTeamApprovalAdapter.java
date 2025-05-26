package com.creatrix.salessolution.Activity.Customer.Approval;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.creatrix.salessolution.Activity.Customer.Approval.Model.CustomerApprovalList;
import com.creatrix.salessolution.Activity.Customer.CustomerSvModel;
import com.creatrix.salessolution.DBAdapter.DBCrudHelper;
import com.creatrix.salessolution.Presenter.AttendancePresenter;
import com.creatrix.salessolution.R;
import com.creatrix.salessolution.UtilityHelper.SessionManagement;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;

import ozaydin.serkan.com.image_zoom_view.ImageViewZoom;

public class customerTeamApprovalAdapter extends RecyclerView.Adapter<customerTeamApprovalAdapter.mtvh> {
    Context context;
    private List<CustomerApprovalList> cList;
    approvalKlick click;

    private int prev = 0;
    private int current = 0;
    private int next = 0;
    String prev_roleType, next_roleType;
    SessionManagement session;
    HashMap<String, String> userInfo;
    int RoleTypeId;
    public customerTeamApprovalAdapter(Context context, List<CustomerApprovalList> cList,approvalKlick click) {
        this.context = context;
        this.cList = cList;
        this.click = click;
        session = new SessionManagement(context);
        userInfo = session.getUserDetails();
        RoleTypeId = Integer.parseInt(userInfo.get(SessionManagement.KEY_EmpRoleTypeId));
    }

    @Override
    public mtvh onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rv_approve_custlist, parent, false);
        context = parent.getContext();
        return new mtvh(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull mtvh holder, int position) {
        if (cList != null) {
            CustomerApprovalList elt=cList.get(position);
            CustomerSvModel custSM=elt.getCustomerSMListDao();
            holder.tv_name.setText(custSM.getCustomerName());
            holder.tv_address.setText(custSM.getAddress());
            holder.tv_market.setText(custSM.getMarketName());
            holder.tv_market_code.setText(custSM.getMarketCode());
            holder.tv_mobile.setText(custSM.getCellNo());
            holder.tv_type.setText(custSM.getProgramTypeName());
            if (custSM.getImageBase64String() != null) {
                try {
                    Glide.with(context).load(custSM.getImageBase64String()).into(holder.shopimg);

                } catch (Exception exception) {
                    //exception.printStackTrace();
                }
            }else {

            }
            holder.status.setText(String.valueOf(elt.getApprovalStatus()));
            if (elt.getApprovalStatus().equals("0")) {
                holder.status.setText("Pending...");
                holder.status.setBackgroundResource(R.drawable.shape_pending);
            }
            else if(elt.getApprovalStatus().equals("1")) {
                holder.status.setText("Verified");
                holder.status.setBackgroundResource(R.drawable.shape_approved);
            }else if(elt.getApprovalStatus().equals("3"))
            {
                holder.status.setText("Rejected");
                holder.status.setBackgroundResource(R.drawable.shape_reject);
            }
            holder.masterLayout.setOnClickListener(v -> {
                Intent got_details = new Intent(context, CustomerApprovalActivity.class);
                Gson gson = new Gson();
                String myJson = gson.toJson(elt);
                System.out.println("adapter data:"+myJson);
                got_details.putExtra("CustTLDetails", myJson);
                got_details.putExtra("Who", "CustTeamApproveAdapter");
                context.startActivity(got_details);
            });

            if (RoleTypeId == 2) {
                prev = elt.getMIOEmpId();
                prev_roleType = "MIO";
                current = elt.getASMEMPId();
                next = elt.getRSMEMPId();
                next_roleType = "AM";
            }
            if (RoleTypeId == 3) {
                prev = elt.getASMEMPId();
                prev_roleType = "AM";
                current = elt.getRSMEMPId();
                next = elt.getNSMEMPId();
                next_roleType = "DZSM";
            }
            if (RoleTypeId == 4) {
                prev = elt.getRSMEMPId();
                prev_roleType = "DZSM";
                current = elt.getNSMEMPId();
                next_roleType = "ADMIN";
                next = 0;
            }
            if (prev == current) {
                if (elt.getRoleTypeId() == RoleTypeId) {
                    holder.tv_approve.setVisibility(View.GONE);
                } else {
                    //new Implementation
                    if (elt.getToRoleTypeId() == RoleTypeId) {
                        holder.tv_approve.setVisibility(View.VISIBLE);
                    } else {
                        holder.tv_approve.setVisibility(View.GONE);
                        if (elt.getRoleTypeId() >= RoleTypeId) {
                            holder.tv_approve.setVisibility(View.GONE);
                        } else {
                            holder.tv_approve.setVisibility(View.GONE);
                        }
                    }

                }

            } else {
                if (elt.getToRoleTypeId() == RoleTypeId) {
                    holder.tv_approve.setVisibility(View.VISIBLE);
                } else {
                    holder.tv_approve.setVisibility(View.GONE);
                    if (elt.getRoleTypeId() >= RoleTypeId) {
                        holder.tv_approve.setVisibility(View.GONE);
                    } else {
                        holder.tv_approve.setVisibility(View.GONE);
                    }
                }
            }


            holder.tv_approve.setOnClickListener(v -> {
                click.approvalTcust(elt);
            });
        }
    }

    @Override
    public int getItemCount() {
        return cList.size();
    }

    public static class mtvh extends RecyclerView.ViewHolder {
        public TextView tv_name,tv_address,tv_market,tv_mobile,tv_type,status,tv_market_code,tv_approve;
        public CardView masterLayout;
        ImageViewZoom shopimg;


        public mtvh(View view) {
            super(view);
            tv_name = view.findViewById(R.id.tv_name);
            shopimg = view.findViewById(R.id.shopimg);
            tv_address = view.findViewById(R.id.tv_address);
            tv_market = view.findViewById(R.id.tv_market);
            tv_mobile = view.findViewById(R.id.tv_mobile);
            tv_type = view.findViewById(R.id.tv_custtype);
            tv_market_code = view.findViewById(R.id.tv_market_code);
            status = view.findViewById(R.id.status);
            tv_approve = view.findViewById(R.id.tv_approve);
            masterLayout = view.findViewById(R.id.master);
        }
    }

    public interface approvalKlick{
        void approvalTcust(CustomerApprovalList data);
    }
}