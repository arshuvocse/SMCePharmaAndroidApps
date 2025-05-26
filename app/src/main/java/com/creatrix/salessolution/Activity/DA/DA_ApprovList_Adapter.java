package com.creatrix.salessolution.Activity.DA;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.creatrix.salessolution.Activity.Approval.DA.DAListData;
import com.creatrix.salessolution.Activity.Approval.DA.DAMaster;
import com.creatrix.salessolution.Activity.Approval.DA.TeamDAViewActivity;
import com.creatrix.salessolution.Interface.EmployeeWiseTotalCountListener;
import com.creatrix.salessolution.R;
import com.creatrix.salessolution.Services.Constants;
import com.google.gson.Gson;

import java.util.List;

public class DA_ApprovList_Adapter extends RecyclerView.Adapter<DA_ApprovList_Adapter.VH> {
    private Context context;
    private List<DAListData> aList;
    private DAMaster daMaster;
    EmployeeWiseTotalCountListener click;
    approval approval;
    int lastposition = -1;
    private int prev = 0;
    private int current = 0;
    private int next = 0;
    private int role = 0;
    private int myrole = 0;
    int RoleTypeId;
    String prev_roleType, next_roleType;
    public DA_ApprovList_Adapter(List<DAListData> nList,EmployeeWiseTotalCountListener click,approval approval,int RoleTypeId) {
        this.aList = nList;
        this.click = click;
        this.approval = approval;
        this.RoleTypeId = RoleTypeId;
    }
    public static class VH extends RecyclerView.ViewHolder {
        public TextView dateTxt,nameTxt,daTxt,statusTxt,maketTxt,tv_approve,TotalEmpRsltTxt;
        public LinearLayout masterLayout;

        public VH(View view) {
            super(view);
            dateTxt = view.findViewById(R.id.dateTxt);
            nameTxt = view.findViewById(R.id.nameTxt);
            statusTxt = view.findViewById(R.id.statusTxt);
            daTxt = view.findViewById(R.id.daTxt);
            maketTxt = view.findViewById(R.id.marketTxt);
            TotalEmpRsltTxt = view.findViewById(R.id.TotalEmpRsltTxt);
            tv_approve = view.findViewById(R.id.tv_approve);
            masterLayout = view.findViewById(R.id.masterLayout);
        }
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.zrv_tadalist, parent, false);
        context = parent.getContext();
        return new VH(itemView);
    }
    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        if (aList != null) {
            DAListData daList = aList.get(position);
            daMaster = aList.get(position).getaTADAMasterDAO();


            SpannableString ss = new SpannableString(daMaster.getTadaDate());
            ss.setSpan(new UnderlineSpan(), 0, ss.length(), 0);
            holder.dateTxt.setText(daMaster.getTadaDate());
            holder.nameTxt.setText(daMaster.getEmpName());
            holder.statusTxt.setText(daMaster.getApprovalStatus());
            holder.daTxt.setText(String.valueOf(daMaster.getDaAmt()));
            holder.maketTxt.setText(daMaster.getMarketName());
            holder.TotalEmpRsltTxt.setText(daMaster.getTotalEmpRslt());
//            holder.dateTxt.setOnClickListener(view -> {
//              click.Emp_total_count(String.valueOf(daMaster.getEmpInfoId()),daMaster.getTadaDateNewFormat(),true);
//            });


            switch (daMaster.getApprovalStatus()) {
                case "0":
                    holder.statusTxt.setText("Pending");
                    holder.statusTxt.setTextColor(Color.parseColor("#ff7400"));
                    break;
                case "1":
                    holder.statusTxt.setText("Verified");
                    holder.statusTxt.setTextColor(Color.parseColor("#4169e1"));
                    break;
                case "2":
                    holder.statusTxt.setText("Approved");
                    holder.statusTxt.setTextColor(Color.parseColor("#00b248"));

                    break;
                case "3":
                    holder.statusTxt.setText("Rejected");
                    holder.statusTxt.setTextColor(Color.parseColor("#C12222"));
                    break;
            }

            holder.statusTxt.setOnClickListener(v -> {
                Constants.WHO = "DATeamApproveAdapter";
                Intent got_details = new Intent(context, TeamDAViewActivity.class);
                Gson gson = new Gson();
                String myJson = gson.toJson(daList);
                got_details.putExtra("DATLDetails", myJson);
                context.startActivity(got_details);
            });
            if (RoleTypeId == 2) {
                prev = daList.getMIOEmpId();
                prev_roleType = "MIO";
                current = daList.getASMEMPId();
                next = daList.getRSMEMPId();
                next_roleType = "AM";
                myrole = 2;
            }
            if (RoleTypeId == 3) {
                prev = daList.getASMEMPId();
                prev_roleType = "AM";
                current = daList.getRSMEMPId();
                next = daList.getNSMEMPId();
                next_roleType = "DZSM";
                myrole = 3;
            }
            if (RoleTypeId == 4) {
                prev = daList.getRSMEMPId();
                prev_roleType = "DZSM";
                current = daList.getNSMEMPId();
                next_roleType = "ADMIN";
                next = 0;
                myrole = 4;
            }
            if (RoleTypeId == 5) {
                myrole = 5;
            }
            //TODO:Button On off
            if (prev == current) {
                if (daList.getRoleTypeId() == RoleTypeId) {
                    holder.tv_approve.setVisibility(View.GONE);
                } else {
                    if (daList.getToRoleTypeId() == RoleTypeId) {
                        holder.tv_approve.setVisibility(View.VISIBLE);
                    } else {
                        holder.tv_approve.setVisibility(View.GONE);
                    }
                }
            } else {
                if (daList.getToRoleTypeId() == RoleTypeId) {
                    holder.tv_approve.setVisibility(View.VISIBLE);
                } else {
                    holder.tv_approve.setVisibility(View.GONE);
                }
            }

            holder.tv_approve.setOnClickListener(v -> {
                approval.approvalKlick(daList);
            });

        }


    }
    @Override
    public int getItemCount() {
        return aList.size();
    }

    public interface approval{
        void approvalKlick(DAListData data);
    }
}

