package com.creatrix.salessolution.Activity.Expense.Report;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.creatrix.salessolution.Activity.Expense.ExpenceClaimViewActivity;
import com.creatrix.salessolution.Activity.Expense.Model.ExpListTeam;
import com.creatrix.salessolution.DBAdapter.DBCrudHelper;
import com.creatrix.salessolution.Presenter.AttendancePresenter;
import com.creatrix.salessolution.R;
import com.creatrix.salessolution.Services.Constants;
import com.creatrix.salessolution.UtilityHelper.SessionManagement;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class expenseTeamApprovalAdapter extends RecyclerView.Adapter<expenseTeamApprovalAdapter.etvh> {
    private Context context;
    private List<ExpListTeam> aList;
    private int prev = 0;
    private int current = 0;
    private int next = 0;
    private int role = 0;
    private int myrole = 0;
    SessionManagement session;
    HashMap<String, String> userInfo = new HashMap<>();

    int RoleTypeId;
    String roleType;
    String prev_roleType, next_roleType;
    approveTE click;
    public expenseTeamApprovalAdapter(Context context, List<ExpListTeam> nList, int RoleTypeId, String roleType, approveTE click) {
        this.context = context;
        this.aList = nList;
        this.RoleTypeId = RoleTypeId;
        this.roleType = roleType;
        this.click = click;
    }

    @NonNull
    @Override
    public etvh onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.zrv_expenseclaim_report, parent, false);
        context = parent.getContext();
        return new etvh(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull etvh holder, int position) {
        session = new SessionManagement(context);
        userInfo = session.getUserDetails();

        if (aList != null) {
            ExpListTeam elt = aList.get(position);

            holder.dateTxt.setText(elt.getExpenseDate());
            holder.nameTxt.setText(elt.getEmpName());
            holder.typeTxt.setText(elt.getExpenseTypeName());
            holder.amountTxt.setText(elt.getAmount().toString());
            switch (elt.getApprovalStatus()) {
                case "0":
                    holder.statusTxt.setText("Pending..");
                    holder.statusTxt.setTextColor(Color.BLUE);
                    break;
                case "1":
                    holder.statusTxt.setText("Verified");
                    holder.statusTxt.setTextColor(Color.GREEN);
                    break;
                case "2":
                    holder.statusTxt.setText("Approved");
                    holder.statusTxt.setTextColor(Color.GREEN);
                    break;
                case "3":
                    holder.statusTxt.setText("Rejected");
                    holder.statusTxt.setTextColor(Color.RED);
                    break;
            }
            ((etvh) holder).statusTxt.setOnClickListener(v -> {
                Constants.WHO = "ExpTeamApproveAdapter";
                Intent got_details = new Intent(context, ExpenceClaimViewActivity.class);
                Gson gson = new Gson();
                String myJson = gson.toJson(elt);
                got_details.putExtra("ExpTLDetails", myJson);
                got_details.putExtra("Who", "ExpTeamApproveAdapter");
                got_details.putExtra("ExpenseClaimID", elt.getExpenseClaimID());
                context.startActivity(got_details);
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
            //TODO:Button On off
            if (prev == current) {
                if (Objects.equals(elt.getRoleTypeId(), RoleTypeId)) {
                    holder.tv_approved.setVisibility(View.GONE);
                } else {
                    if (Objects.equals(elt.getToRoleTypeId(), RoleTypeId)) {
                        holder.tv_approved.setVisibility(View.VISIBLE);
                    } else {
                        holder.tv_approved.setVisibility(View.GONE);
                    }
                }
            } else {
                if (Objects.equals(elt.getToRoleTypeId(), RoleTypeId)) {
                    holder.tv_approved.setVisibility(View.VISIBLE);
                } else {
                    holder.tv_approved.setVisibility(View.GONE);
                }
            }
            ((etvh) holder).tv_approved.setOnClickListener(v -> {
                click.approveKlick(elt);
            });
        }

    }

    @Override
    public int getItemCount() {
        return aList.size();
    }
    public static class etvh extends RecyclerView.ViewHolder {
        public TextView dateTxt, nameTxt, typeTxt, statusTxt, amountTxt, tv_approved;
        public LinearLayout masterLayout;


        public etvh(View view) {
            super(view);
            dateTxt = view.findViewById(R.id.dateTxt);
            nameTxt = view.findViewById(R.id.nameTxt);
            typeTxt = view.findViewById(R.id.typeTxt);
            statusTxt = view.findViewById(R.id.statusTxt);
            amountTxt = view.findViewById(R.id.amountTxt);
            tv_approved = view.findViewById(R.id.tv_approves);
            masterLayout = view.findViewById(R.id.masterLayout);

        }
    }
    public interface approveTE {
        void approveKlick(ExpListTeam elt);
    }
}
