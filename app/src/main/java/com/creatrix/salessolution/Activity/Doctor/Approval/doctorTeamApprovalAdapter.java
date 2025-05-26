package com.creatrix.salessolution.Activity.Doctor.Approval;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.creatrix.salessolution.Activity.Approval.DCR.DcrApprovalData;
import com.creatrix.salessolution.Activity.Doctor.Approval.Model.DoctorApprovalList;
import com.creatrix.salessolution.Activity.Doctor.Approval.Model.DoctorApproveModel;
import com.creatrix.salessolution.Activity.MileageClaim.MileageClaimViewActivity;
import com.creatrix.salessolution.Activity.MileageClaim.Model.MileageListTeam;
import com.creatrix.salessolution.Model.Doctor.DoctorSM;
import com.creatrix.salessolution.Presenter.AttendancePresenter;
import com.creatrix.salessolution.R;
import com.creatrix.salessolution.UtilityHelper.SessionManagement;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;

public class doctorTeamApprovalAdapter extends RecyclerView.Adapter<doctorTeamApprovalAdapter.mtvh> {
    private Context context;
    private List<DoctorApprovalList> dList;
    approveTDoc approvel;
    private int prev = 0;
    private int current = 0;
    private int next = 0;
    String prev_roleType, next_roleType;
    SessionManagement session;
    HashMap<String, String> userInfo;
    int RoleTypeId;

    public doctorTeamApprovalAdapter(Context context, List<DoctorApprovalList> dList,approveTDoc approvel) {
        this.context = context;
        this.dList = dList;
        this.approvel = approvel;
        session = new SessionManagement(context);
        userInfo = session.getUserDetails();
        RoleTypeId = Integer.parseInt(userInfo.get(SessionManagement.KEY_EmpRoleTypeId));
    }

    @Override
    public mtvh onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rv_approve_doclist, parent, false);
        context = parent.getContext();
        return new mtvh(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull mtvh holder, int position) {
        if (dList != null) {
            DoctorApprovalList elt=dList.get(position);
            DoctorApproveModel doctorSM=elt.getDoctorMasterDao();

            holder.tv_name_code.setText(doctorSM.getDoctorName());
            holder.tv_degree.setText(doctorSM.getDegreeStr());
            holder.tv_category.setText(doctorSM.getCategoryName());
            holder.status.setText(String.valueOf(elt.getApprovalStatus()));
            if (elt.getApprovalStatus().equals("Pending")||elt.getStatus().equals("Posted")) {
                holder.status.setText("Pending");
                holder.status.setTextColor(Color.parseColor("#ff7400"));
            }
            else if(elt.getApprovalStatus().equals("1")||elt.getApprovalStatus().equals("Approved")) {
                holder.status.setText("Verified");
                holder.status.setTextColor(Color.parseColor("#00b248"));
                holder.tv_approve.setVisibility(View.GONE);
            }else if(elt.getApprovalStatus().equals("Rejected"))
            {
                holder.status.setBackgroundResource(R.drawable.shape_reject);
            }


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
            //TODO:Button On off
            if (prev == current) {
                if (elt.getRoleTypeId() == RoleTypeId) {
                    holder.tv_approve.setVisibility(View.GONE);
                }
                else {
                    //new Implementation for vacant
                    if (elt.getToRoleTypeId() == RoleTypeId) {
                     holder.tv_approve.setVisibility(View.VISIBLE);
                    }
                    else {
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



            holder.masterLayout.setOnClickListener(v -> {
                Intent got_details = new Intent(context, DoctorApprovalDetails.class);
                Gson gson = new Gson();
                String myJson = gson.toJson(elt);
                got_details.putExtra("DocTLDetails", myJson);
                got_details.putExtra("Who", "DocTeamApproveAdapter");
                context.startActivity(got_details);

            });
            holder.tv_approve.setOnClickListener(v -> {
                approvel.approveKlick(elt);
            });
        }
    }

    @Override
    public int getItemCount() {
        return dList.size();
    }

    public static class mtvh extends RecyclerView.ViewHolder {
        public TextView tv_name_code,tv_degree,tv_category,tv_approve,status;
        public CardView masterLayout;
        public mtvh(View view) {
            super(view);
            tv_name_code = view.findViewById(R.id.tv_name_code);
            tv_degree = view.findViewById(R.id.tv_degree);
            tv_category = view.findViewById(R.id.tv_category);
            status = view.findViewById(R.id.status);
            tv_approve = view.findViewById(R.id.tv_approve);
            masterLayout = view.findViewById(R.id.master);
        }
    }
    public interface approveTDoc {
        void approveKlick(DoctorApprovalList elt);
    }
}
