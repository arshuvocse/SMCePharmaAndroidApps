package com.creatrix.salessolution.Activity.MileageClaim;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.creatrix.salessolution.Activity.Approval.DA.DAListData;
import com.creatrix.salessolution.Activity.MileageClaim.Model.MileageListTeam;
import com.creatrix.salessolution.Presenter.AttendancePresenter;
import com.creatrix.salessolution.R;
import com.creatrix.salessolution.Services.ConstValue;
import com.creatrix.salessolution.Services.Constants;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;

public class mileageTeamApprovalAdapter extends RecyclerView.Adapter<mileageTeamApprovalAdapter.mtvh> {
    private Context context;
    private List<MileageListTeam> aList;
    AttendancePresenter presenter;
    approvalTMil click;
    int RoleTypeId=0;
    private int prev = 0;
    private int current = 0;
    private int next = 0;
    private int role = 0;
    private int myrole = 0;
    String prev_roleType, next_roleType;
    public mileageTeamApprovalAdapter(Context context, List<MileageListTeam> nList, int RoleTypeId,approvalTMil click) {
        this.context = context;
        this.aList = nList;
        this.RoleTypeId = RoleTypeId;
        this.click = click;
    }

    @Override
    public mtvh onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.zrv_mileage_claimlist, parent, false);
        return new mtvh(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull mtvh holder, int position) {
        if (aList != null) {
            MileageListTeam elt=aList.get(position);

            holder.dateTxt.setText(elt.getMileageDate());
            holder.nameTxt.setText(elt.getEmpName());
            holder.transportTxt.setText(elt.getTransportName());

            holder.mileageTxt.setText(String.valueOf(elt.getMileageInKM()));
            holder.meterReadingId.setText(String.valueOf(elt.getMeterReading()));
            holder.submarketTxt.setText(elt.getMarketName());
            holder.statusTxt.setText(elt.getApprovalStatus());

            if (elt.getApprovalStatus().equals("Pending")||elt.getStatus().equals("Posted")) {
                holder.statusTxt.setText("Pending");
                holder.statusTxt.setTextColor(Color.parseColor("#ff7400"));
            }
            else if(elt.getApprovalStatus().equals("1")||elt.getApprovalStatus().equals("Verified")) {
                holder.statusTxt.setText("Verified");
                holder.statusTxt.setTextColor(Color.parseColor("#00b248"));
            }else if(elt.getApprovalStatus().equals("2")||elt.getApprovalStatus().equals("Approved")) {
                holder.statusTxt.setText("Approved");
                holder.statusTxt.setTextColor(Color.GREEN);
            }
            else if(elt.getApprovalStatus().equals("Rejected"))
            {
                holder.statusTxt.setBackgroundResource(R.drawable.shape_reject);
            }

            holder.statusTxt.setOnClickListener(v -> {
                try {
                    Constants.WHO="MilTeamApproveAdapter";
                    Intent got_details = new Intent(context, MileageClaimViewActivity.class);
                    Gson gson = new Gson();
                    String myJson = gson.toJson(elt);
                    got_details.putExtra("MilTLDetails", myJson);
                    context.startActivity(got_details);
                } catch (Exception e) {
                   // e.printStackTrace();
                }
            });


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
            if (prev == current) {
                if (elt.getRoleTypeId() == RoleTypeId) {
                    holder.tv_approve.setVisibility(View.GONE);
                } else {
                    holder.tv_approve.setVisibility(View.VISIBLE);
                }
            } else {
                try {
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
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            holder.tv_approve.setOnClickListener(v -> {
             click.approvalKlick(elt);
            });
        }

    }

    @Override
    public int getItemCount() {
        return aList.size();
    }

    public static class mtvh extends RecyclerView.ViewHolder {
        public TextView dateTxt,nameTxt,transportTxt,mileageTxt,submarketTxt,statusTxt,tv_approve,meterReadingId;
        public LinearLayout masterLayout;

        public mtvh(View view) {
            super(view);
            dateTxt = view.findViewById(R.id.dateTxt);
            nameTxt = view.findViewById(R.id.nameTxt);
            transportTxt = view.findViewById(R.id.transportTxt);
            mileageTxt = view.findViewById(R.id.mileageTxt);
            submarketTxt = view.findViewById(R.id.submarketTxt);
            statusTxt = view.findViewById(R.id.statusTxt);
            meterReadingId = view.findViewById(R.id.meterReadingId);
            tv_approve = view.findViewById(R.id.tv_approve);
            masterLayout = view.findViewById(R.id.masterLayout);
        }
    }
    public interface approvalTMil{
        void approvalKlick(MileageListTeam data);
    }
}
