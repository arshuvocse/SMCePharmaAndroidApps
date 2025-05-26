package com.creatrix.salessolution.Activity.Approval.Order;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.creatrix.salessolution.Activity.MileageClaim.Model.MileageListTeam;
import com.creatrix.salessolution.Model.Customer;
import com.creatrix.salessolution.R;
import com.creatrix.salessolution.Services.Constants;
import com.google.gson.Gson;

import java.util.List;

public class OrderApprovalAdapter extends RecyclerView.Adapter<OrderApprovalAdapter.VH> {
    private Context context;
    private List<OrderApprovalData> aList;
    private OrderMasterDAO oMaster;
    private Customer customer;
    private List<OrderDtls> odList;
    int lastposition = -1;

    private int prev = 0;
    private int current = 0;
    private int next = 0;
    private int myrole = 0;
    private int RoleTypeId = 0;
    String prev_roleType, next_roleType;
    approvalTOrder click;
    public OrderApprovalAdapter(List<OrderApprovalData> nList,Context context,approvalTOrder click,int RoleTypeId) {
        this.aList = nList;
        this.context = context;
        this.click = click;
        this.RoleTypeId = RoleTypeId;
    }
    public static class VH extends RecyclerView.ViewHolder {
        public TextView storeName,amount, empName, createdDate, orderstatus,tv_approve;
        CardView masterLayout;

        public VH(View view) {
            super(view);
            storeName = view.findViewById(R.id.storeName);
            amount = view.findViewById(R.id.amount);
            empName = view.findViewById(R.id.empName);
            createdDate = view.findViewById(R.id.createdDate);
            orderstatus = view.findViewById(R.id.orderstatus);
            tv_approve = view.findViewById(R.id.tv_approve);
            masterLayout = view.findViewById(R.id.masterLayout);
        }
    }
    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rv_order_approval, parent, false);
        context = parent.getContext();
        return new VH(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        if (aList != null) {
            OrderApprovalData orderApprovalData = aList.get(position);
            oMaster = aList.get(position).getaOrderMasterDAO();
            odList = aList.get(position).getaOrderMasterDAO().getaOrderDtls();
            customer=aList.get(position).getaCustMasterDAO();
            holder.storeName.setText(customer.getCustomerName());
            holder.amount.setText(String.valueOf(oMaster.getTotalAmount()));
            holder.empName.setText(orderApprovalData.getEmpName());
            holder.createdDate.setText(orderApprovalData.getEntryDate());

            if (orderApprovalData.getApprovalStatus().equals("0")) {
                holder.orderstatus.setText("Pending..");
                holder.orderstatus.setTextColor(Color.parseColor("#ff7400"));
            } else if (orderApprovalData.getApprovalStatus().equals("1")) {
                holder.orderstatus.setText("Verified");
                holder.orderstatus.setTextColor(Color.parseColor("#4169e1"));
            } else if (orderApprovalData.getApprovalStatus().equals("2")) {
                holder.orderstatus.setText("Approved");
                holder.orderstatus.setTextColor(Color.parseColor("#00b248"));

            } else if (orderApprovalData.getApprovalStatus().equals("3")) {
                holder.orderstatus.setText("Rejected");
                holder.orderstatus.setTextColor(Color.parseColor("#C12222"));
            }

            holder.masterLayout.setOnClickListener(v -> {
                Constants.WHO = "OrderApproveAdapter";
                Intent got_details = new Intent(context, OrderApprovalViewActivity.class);
                Gson gson = new Gson();
                String myJson = gson.toJson(orderApprovalData);
                got_details.putExtra("OrderApprovalDetails", myJson);
                context.startActivity(got_details);

            });

            if (RoleTypeId == 2) {
                prev = orderApprovalData.getMIOEmpId();
                prev_roleType = "MIO";
                current = orderApprovalData.getASMEMPId();
                next = orderApprovalData.getRSMEMPId();
                next_roleType = "AM";
                myrole = 2;
            }
            if (RoleTypeId == 3) {
                prev = orderApprovalData.getASMEMPId();
                prev_roleType = "AM";
                current = orderApprovalData.getRSMEMPId();
                next = orderApprovalData.getNSMEMPId();
                next_roleType = "DZSM";
                myrole = 3;
            }
            if (RoleTypeId == 4) {
                prev = orderApprovalData.getRSMEMPId();
                prev_roleType = "DZSM";
                current = orderApprovalData.getNSMEMPId();
                next_roleType = "ADMIN";
                next = 0;
                myrole = 4;
            }
            if (RoleTypeId == 5) {
                myrole = 5;
            }

            if (prev == current) {
                if (orderApprovalData.getRoleTypeId() == RoleTypeId) {
                    holder.tv_approve.setVisibility(View.GONE);
                } else {
                    holder.tv_approve.setVisibility(View.VISIBLE);

                    //new Implementation for vacant
                    if (orderApprovalData.getToRoleTypeId() == RoleTypeId) {
                        holder.tv_approve.setVisibility(View.VISIBLE);
                    } else {
                        holder.tv_approve.setVisibility(View.GONE);
                        if (orderApprovalData.getRoleTypeId() >= RoleTypeId) {
                            holder.tv_approve.setVisibility(View.GONE);
                        } else {
                            holder.tv_approve.setVisibility(View.GONE);
                        }
                    }

                }
            } else {
                if (orderApprovalData.getToRoleTypeId() == RoleTypeId) {
                    holder.tv_approve.setVisibility(View.VISIBLE);
                } else {
                    holder.tv_approve.setVisibility(View.GONE);
                    if (orderApprovalData.getRoleTypeId() >= RoleTypeId) {
                        holder.tv_approve.setVisibility(View.GONE);
                    } else {
                        holder.tv_approve.setVisibility(View.GONE);
                    }
                }
            }

            holder.tv_approve.setOnClickListener(v -> {
                click.approvalKlick(orderApprovalData);
            });
        }


    }

    @Override
    public int getItemCount() {
        return aList.size();
    }

    public interface approvalTOrder{
        void approvalKlick(OrderApprovalData data);
    }
}

