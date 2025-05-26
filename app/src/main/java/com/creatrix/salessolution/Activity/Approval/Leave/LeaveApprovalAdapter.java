package com.creatrix.salessolution.Activity.Approval.Leave;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;


import com.creatrix.salessolution.Activity.Approval.Prescription.PrescriptionApprovalViewActivity;

import com.creatrix.salessolution.Activity.Customer.Approval.Model.CustomerApprovalList;
import com.creatrix.salessolution.Activity.Customer.Approval.customerTeamApprovalAdapter;
import com.creatrix.salessolution.DBAdapter.DBCrudHelper;
import com.creatrix.salessolution.Model.User;
import com.creatrix.salessolution.R;
import com.creatrix.salessolution.UtilityHelper.SessionManagement;
import com.creatrix.salessolution.UtilityHelper.SnackBarManagement;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;

public class LeaveApprovalAdapter extends RecyclerView.Adapter<LeaveApprovalAdapter.ViewHolder> {
    private final static int FADE_DURATION = 500; //FADE_DURATION in milliseconds
    private Context context;
    private Activity activity;
    private List<LeaveApprovalData> pList;
    approvalKlick click;

    private int prev = 0;
    private int current = 0;
    private int next = 0;
    String prev_roleType, next_roleType;
    SessionManagement session;
    HashMap<String, String> userInfo;
    int RoleTypeId;

    public LeaveApprovalAdapter(Activity activity, List<LeaveApprovalData> pList,approvalKlick click,int RoleTypeId) {
        this.context = context;
        this.activity = activity;
        this.pList = pList;
        this.click = click;
        this.RoleTypeId = RoleTypeId;

      /*  session = new SessionManagement(context);
        userInfo = session.getUserDetails();
        RoleTypeId = Integer.parseInt(userInfo.get(SessionManagement.KEY_EmpRoleTypeId));*/

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rv_leave_approval_list, parent, false);
        context = parent.getContext();
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (pList != null) {
            LeaveApprovalData la = pList.get(position);
            holder.empName.setText(la.getEmpName());
            holder.createdat.setText(la.getEntryDate());
            holder.typeTxt.setText(la.getType());
            holder.dateTxt.setText(la.getLeaveFromDate());
            holder.bacground.setBackgroundResource(R.color.lightBlue);
            if (la.getApprovalStatus().equals("0")) {
                holder.status.setText("Pending");
                holder.status.setTextColor(Color.parseColor("#ff7400"));
            } else if (la.getApprovalStatus().equals("1")) {
                holder.status.setText("Verified");
                holder.status.setTextColor(Color.parseColor("#4169e1"));
            } else if (la.getApprovalStatus().equals("2")) {
                holder.status.setText("Approved");
                holder.status.setTextColor(Color.parseColor("#00b248"));
            } else if (la.getApprovalStatus().equals("3")) {
                holder.status.setText("Rejected");
                holder.status.setTextColor(Color.parseColor("#C12222"));
            }
            holder.bacground.setOnClickListener(v -> {
                Intent intent = new Intent(activity, LeaveApprovalViewActivity.class);
                Gson gson = new Gson();
                String myJson = gson.toJson(la);
                intent.putExtra("myjson", myJson);
                activity.startActivity(intent);
                activity.overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                //activity.finish();
                // ((Activity) context).finish();
            });

            if (RoleTypeId == 2) {
                prev = la.getMIOEmpId();
                prev_roleType = "MIO";
                current = la.getASMEMPId();
                next = la.getRSMEMPId();
                next_roleType = "AM";
            }
            if (RoleTypeId == 3) {
                prev = la.getASMEMPId();
                prev_roleType = "AM";
                current = la.getRSMEMPId();
                next = la.getNSMEMPId();
                next_roleType = "DZSM";
            }
            if (RoleTypeId == 4) {
                prev = la.getRSMEMPId();
                prev_roleType = "DZSM";
                current = la.getNSMEMPId();
                next_roleType = "ADMIN";
                next = 0;
            }
            if (prev == current) {
                if (la.getRoleTypeId() == RoleTypeId) {
                 holder.tv_approve.setVisibility(View.GONE);
                } else {
                    holder.tv_approve.setVisibility(View.VISIBLE);
                }
            } else {
                if (la.getToRoleTypeId() == null) {
                    holder.tv_approve.setVisibility(View.GONE);
                } else if (la.getToRoleTypeId() == RoleTypeId) {
                    holder.tv_approve.setVisibility(View.VISIBLE);
                } else {
                    holder.tv_approve.setVisibility(View.GONE);
                    if (la.getRoleTypeId() >= RoleTypeId) {
                        holder.tv_approve.setVisibility(View.GONE);
                    } else {
                        holder.tv_approve.setVisibility(View.GONE);
                    }
                }
            }

            holder.tv_approve.setOnClickListener(v -> {
                click.approvalTLeave(la);
            });
        }
        else {
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

        public TextView empName,createdat, typeTxt, dateTxt,status,tv_approve;
        CardView bacground;

        public ViewHolder(View view) {
            super(view);
            bacground = view.findViewById(R.id.bacground);
            empName = (TextView) view.findViewById(R.id.empname);
            createdat = (TextView) view.findViewById(R.id.createdat);
            typeTxt = (TextView) view.findViewById(R.id.typeTxt);
            dateTxt = (TextView) view.findViewById(R.id.dateTxt);
            status = (TextView) view.findViewById(R.id.status);
            tv_approve = (TextView) view.findViewById(R.id.tv_approve);

            setFadeAnimation(bacground);
        }

    }
    public interface approvalKlick{
        void approvalTLeave(LeaveApprovalData data);
    }
}