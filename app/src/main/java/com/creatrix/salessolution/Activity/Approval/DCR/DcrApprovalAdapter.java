package com.creatrix.salessolution.Activity.Approval.DCR;

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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.creatrix.salessolution.Activity.Expense.Model.ExpListTeam;
import com.creatrix.salessolution.R;
import com.creatrix.salessolution.UtilityHelper.SessionManagement;
import com.creatrix.salessolution.UtilityHelper.SnackBarManagement;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;

public class DcrApprovalAdapter extends RecyclerView.Adapter<DcrApprovalAdapter.ViewHolder> {
    private final static int FADE_DURATION = 500; //FADE_DURATION in milliseconds
    private Context context;
    private List<DcrApprovalData> aList;
    private int prev = 0;
    private int current = 0;
    private int next = 0;
    private int myrole = 0;
    String prev_roleType, next_roleType;
    SessionManagement session;
    HashMap<String, String> userInfo;
    int RoleTypeId;
    approveTDCR listnr;
    public DcrApprovalAdapter(Context context,List<DcrApprovalData> aList,approveTDCR listnr) {
        this.context = context;
        this.aList = aList;
        this.listnr = listnr;

        session = new SessionManagement(context);
        userInfo = session.getUserDetails();
        RoleTypeId = Integer.parseInt(userInfo.get(SessionManagement.KEY_EmpRoleTypeId));

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.zrv_dcr_list_recyclerview, parent, false);
        context = parent.getContext();
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        try {
            if (aList != null) {
                DcrApprovalData data=aList.get(position);
                ((ViewHolder) holder).setdata(aList.get(position));
                holder.masterLayout.setBackgroundResource(R.color.lightBlue);
                holder.masterLayout.setOnClickListener(v -> {
                    Intent intent = new Intent(context, DcrApprovalViewActivity.class);
                    Gson gson = new Gson();
                    String myJson = gson.toJson(data);
                    intent.putExtra("myjson", myJson);
                    context.startActivity(intent);
                    //((Activity)context).finish();
                });
                holder.tv_approve.setOnClickListener(v -> {
                    listnr.approveKlick(data);
                });

            /*    if (RoleTypeId == 2) {
                    prev = data.getMIOEmpId();
                    prev_roleType = "MIO";
                    current = data.getASMEMPId();
                    next = data.getRSMEMPId();
                    next_roleType = "AM";
                    myrole = 2;
                }
                if (RoleTypeId == 3) {
                    prev = data.getASMEMPId();
                    prev_roleType = "AM";
                    current = data.getRSMEMPId();
                    next = data.getNSMEMPId();
                    next_roleType = "DZSM";
                    myrole = 3;
                }
                if (RoleTypeId == 4) {
                    prev = data.getRSMEMPId();
                    prev_roleType = "DZSM";
                    current = data.getNSMEMPId();
                    next_roleType = "ADMIN";
                    next = 0;
                    myrole = 4;
                }
                if (RoleTypeId == 5) {
                    myrole = 5;
                }
                if (prev == current) {
                    if (data.getRoleTypeId() == RoleTypeId) {
                        holder.tv_approve.setVisibility(View.GONE);
                    } else {
                        holder.tv_approve.setVisibility(View.VISIBLE);
                    }
                } else {
                    if (data.getToRoleTypeId() == null) {
                        holder.tv_approve.setVisibility(View.GONE);
                    }
                    else if (data.getToRoleTypeId() == RoleTypeId) {
                        holder.tv_approve.setVisibility(View.VISIBLE);
                    } else {
                        holder.tv_approve.setVisibility(View.GONE);
                        if (data.getRoleTypeId() >= RoleTypeId) {
                            holder.tv_approve.setVisibility(View.GONE);
                        } else {
                            holder.tv_approve.setVisibility(View.GONE);
                        }
                    }

                }*/

            } else {
                SnackBarManagement._warning_CustomMessage(holder.masterLayout,"No DCR Found");
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }


    @Override
    public int getItemCount() {
        if (aList != null) {
            return aList.size();
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
        public TextView docnameTxt, createdby, createddate, statusTxt,tv_approve;
        CardView masterLayout;
        public ViewHolder(View view) {
            super(view);
            docnameTxt = (TextView) view.findViewById(R.id.nameTxt);
            createdby = (TextView) view.findViewById(R.id.createdby);
            createddate = (TextView) view.findViewById(R.id.createddate);
            statusTxt = (TextView) view.findViewById(R.id.statusTxt);
            tv_approve = (TextView) view.findViewById(R.id.tv_approve);
            masterLayout = (CardView) view.findViewById(R.id.masterLayout);
            setFadeAnimation(masterLayout);
        }

        public void setdata(DcrApprovalData dcrVM) {
            docnameTxt.setText(dcrVM.getaDCRMasterDAO().getDoctorName());
            createdby.setText(dcrVM.getEmpName());
            createddate.setText(dcrVM.getaDCRMasterDAO().getDcrDate());
            statusTxt.setVisibility(View.VISIBLE);

            try {
                if(dcrVM.getApprovalStatus().equals("0"))
                {
                    statusTxt.setText("Pending");
                    statusTxt.setTextColor(Color.parseColor("#ff7400"));
                }else if(dcrVM.getApprovalStatus().equals("1")){
                    statusTxt.setText("Verified");
                    statusTxt.setTextColor(Color.parseColor("#4169e1"));
                }
                else if(dcrVM.getApprovalStatus().equals("2")){
                    statusTxt.setText("Approved");
                    statusTxt.setTextColor(Color.parseColor("#00b248"));
                }
                else if(dcrVM.getApprovalStatus().equals("3")){
                    statusTxt.setText("Rejected");
                    statusTxt.setTextColor(Color.parseColor("#C12222"));
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }

            if (RoleTypeId == 2) {
                prev = dcrVM.getMIOEmpId();
                prev_roleType = "MIO";
                current = dcrVM.getASMEMPId();
                next = dcrVM.getRSMEMPId();
                next_roleType = "AM";
                myrole = 2;
            }
            if (RoleTypeId == 3) {
                prev = dcrVM.getASMEMPId();
                prev_roleType = "AM";
                current = dcrVM.getRSMEMPId();
                next = dcrVM.getNSMEMPId();
                next_roleType = "DZSM";
                myrole = 3;
            }
            if (RoleTypeId == 4) {
                prev = dcrVM.getRSMEMPId();
                prev_roleType = "DZSM";
                current = dcrVM.getNSMEMPId();
                next_roleType = "ADMIN";
                next = 0;
                myrole = 4;
            }
            if (RoleTypeId == 5) {
                myrole = 5;
            }
            if (prev == current) {
                if (dcrVM.getRoleTypeId() == RoleTypeId) {
                    tv_approve.setVisibility(View.GONE);
                } else {
                    tv_approve.setVisibility(View.VISIBLE);
                }
            } else {
                if (dcrVM.getToRoleTypeId() == null) {
                     tv_approve.setVisibility(View.GONE);
                }
                else if (dcrVM.getToRoleTypeId() == RoleTypeId) {
                    tv_approve.setVisibility(View.VISIBLE);
                } else {
                    tv_approve.setVisibility(View.GONE);
                    if (dcrVM.getRoleTypeId() >= RoleTypeId) {
                        tv_approve.setVisibility(View.GONE);
                    } else {
                        tv_approve.setVisibility(View.GONE);
                    }
                }
            }
        }
    }
    public interface approveTDCR {
        void approveKlick(DcrApprovalData elt);
    }
}