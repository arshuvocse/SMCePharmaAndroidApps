package com.creatrix.salessolution.Activity.Approval.Prescription;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.creatrix.salessolution.Activity.Approval.DA.DAListData;
import com.creatrix.salessolution.Activity.Approval.DCR.DcrApprovalData;
import com.creatrix.salessolution.Activity.Approval.DCR.DcrApprovalViewActivity;
import com.creatrix.salessolution.Activity.DA.DA_ApprovList_Adapter;
import com.creatrix.salessolution.Activity.Doctor.Prescription.AddPrescriptionActivity;
import com.creatrix.salessolution.DBAdapter.DBCrudHelper;
import com.creatrix.salessolution.Model.Doctor.DoctorListViewModel;
import com.creatrix.salessolution.Model.PrescriptionSM;
import com.creatrix.salessolution.R;
import com.creatrix.salessolution.Services.Constants;
import com.creatrix.salessolution.UtilityHelper.SessionManagement;
import com.creatrix.salessolution.UtilityHelper.SnackBarManagement;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;

public class PrescApprovalAdapter extends RecyclerView.Adapter<PrescApprovalAdapter.ViewHolder> {
    private final static int FADE_DURATION = 500; //FADE_DURATION in milliseconds
    Context context;
    private List<PrescApprovalData> pList;
    DBCrudHelper dbCrudHelper;
    Rxapproval approval;
    int RoleTypeId, empid;

    SessionManagement session;
    private int prev = 0;
    private int current = 0;
    private int next = 0;
    private int myrole = 0;
    String prev_roleType, next_roleType;
    public PrescApprovalAdapter(Context context, List<PrescApprovalData> pList, Rxapproval approval) {
        this.context = context;
        this.pList = pList;
        this.approval = approval;
        dbCrudHelper = new DBCrudHelper(context);
        session = new SessionManagement(context);
        HashMap<String, String> user = session.getUserDetails();
        RoleTypeId = Integer.parseInt(user.get(SessionManagement.KEY_EmpRoleTypeId));
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.zrv_prescriptionlist, parent, false);
        context = parent.getContext();
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (pList != null) {
            PrescApprovalData ps = pList.get(position);
            PrescriptionMaster pm = ps.getaPrescriptionRMasterDAO();
            //pm.setMyImg("");
            holder.entrytime.setVisibility(View.GONE);
            holder.docname_tag.setText("Created By : ");
            //holder.docname_tag.setTextColor(Color.parseColor("#ff7400"));
            holder.doctorName.setText(ps.getEmpName());
            //holder.doctorName.setTextColor(Color.parseColor("#ff7400"));

            holder.typetag.setText("Doctor Name : ");
            holder.typeTxt.setText(pm.getDoctorName());
            holder.dateTxt.setText(pm.getPrescriptionDate());

            holder.bacground.setBackgroundResource(R.color.lightBlue);
            if (RoleTypeId == 2) {
                prev = ps.getMIOEmpId();
                prev_roleType = "MIO";
                current = ps.getASMEMPId();
                next = ps.getRSMEMPId();
                next_roleType = "AM";
                myrole = 2;
            }
            if (RoleTypeId == 3) {
                prev = ps.getASMEMPId();
                prev_roleType = "AM";
                current = ps.getRSMEMPId();
                next = ps.getNSMEMPId();
                next_roleType = "DZSM";
                myrole = 3;
            }
            if (RoleTypeId == 4) {
                prev = ps.getRSMEMPId();
                prev_roleType = "DZSM";
                current = ps.getNSMEMPId();
                next_roleType = "ADMIN";
                next = 0;
                myrole = 4;
            }
            if (RoleTypeId == 5) {
                myrole = 5;
            }


            if (ps.getApprovalStatus().equals("0")) {
                holder.status.setText("Pending");
                holder.status.setTextColor(Color.parseColor("#ff7400"));
            } else if (ps.getApprovalStatus().equals("1")) {
                holder.status.setText("Verified");
                holder.status.setTextColor(Color.parseColor("#4169e1"));
            } else if (ps.getApprovalStatus().equals("2")) {
                holder.status.setText("Approved");
                holder.status.setTextColor(Color.parseColor("#00b248"));
            } else if (ps.getApprovalStatus().equals("3")) {
                holder.status.setText("Rejected");
                holder.status.setTextColor(Color.parseColor("#C12222"));
            }

            if (prev == current) {
                if (ps.getRoleTypeId() == RoleTypeId) {
                    holder.tv_approve.setVisibility(View.GONE);
                } else {
                    holder.tv_approve.setVisibility(View.VISIBLE);
                }
            } else {
                if (ps.getToRoleTypeId() == null) {
                    holder.tv_approve.setVisibility(View.GONE);
                } else if (ps.getToRoleTypeId() == RoleTypeId) {
                    holder.tv_approve.setVisibility(View.VISIBLE);
                } else {
                    holder.tv_approve.setVisibility(View.GONE);
                    if (ps.getRoleTypeId() >= RoleTypeId) {
                        holder.tv_approve.setVisibility(View.GONE);
                    } else {
                        holder.tv_approve.setVisibility(View.GONE);
                    }
                }
            }

            holder.tv_approve.setOnClickListener(v -> {
                    approval.approvalKlick(ps);
            });
            holder.bacground.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {

                        Intent intent = new Intent(context, PrescriptionApprovalViewActivity.class);
                        Gson gson = new Gson();
                        String myJson = gson.toJson(ps);
                        intent.putExtra("myjson", myJson);
                        context.startActivity(intent);
                    } catch (Exception e) {
                    }
                }
            });
        } else {
            SnackBarManagement._warning_CustomMessage(holder.bacground, "Prescription Not Found");
        }

    }


    @Override
    public int getItemCount() {
        if (pList != null) {
            return pList.size();
        }
        return 0;
    }

    private void setFadeAnimation(View view) {
        AnimationSet set = new AnimationSet(true);
        Animation animation = new AlphaAnimation(0.0f, 1.0f);
        animation.setDuration(500);
        view.startAnimation(animation);

        animation = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, -1.0f, Animation.RELATIVE_TO_SELF, 0.0f
        );
        animation.setDuration(100);
        //   view.startAnimation(set);
        view.startAnimation(animation);
    }

    //DCR
    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView doctorName, typeTxt, dateTxt, product_name_txt, entrytime, status, tv_approve;
        public TextView docname_tag, typetag;
        LinearLayout typelay;
        CardView bacground;

        public ViewHolder(View view) {
            super(view);
            bacground = (CardView) view.findViewById(R.id.bacground);
            doctorName = (TextView) view.findViewById(R.id.doctorName);
            typeTxt = (TextView) view.findViewById(R.id.typeTxt);
            dateTxt = (TextView) view.findViewById(R.id.dateTxt);
            entrytime = (TextView) view.findViewById(R.id.entrytime);
            status = (TextView) view.findViewById(R.id.status);
            tv_approve = (TextView) view.findViewById(R.id.tv_approve);
            product_name_txt = (TextView) view.findViewById(R.id.product_name_txt);

            docname_tag = (TextView) view.findViewById(R.id.docname_tag);
            typetag = (TextView) view.findViewById(R.id.typetag);
            setFadeAnimation(bacground);
        }

    }

    public interface Rxapproval{
        void approvalKlick(PrescApprovalData data);
    }
}