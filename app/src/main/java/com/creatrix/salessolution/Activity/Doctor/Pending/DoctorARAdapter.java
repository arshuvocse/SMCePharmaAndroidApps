package com.creatrix.salessolution.Activity.Doctor.Pending;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.creatrix.salessolution.Activity.Customer.Pending.CustomerARModel;
import com.creatrix.salessolution.R;
import com.creatrix.salessolution.UtilityHelper.SnackBarManagement;

import java.util.List;

public class DoctorARAdapter extends RecyclerView.Adapter<DoctorARAdapter.vh> {
    private Context context;
    private List<DoctorARModel> dList;

    public DoctorARAdapter(List<DoctorARModel> dList, Context context) {
        this.dList = dList;
        this.context = context;
    }


    public static class vh extends RecyclerView.ViewHolder {
        public TextView docName, docmarket, docstatus,createdat,docwaiting;
        CardView masterLayout;
        public vh(View view) {
            super(view);
            createdat = (TextView) view.findViewById(R.id.createdat);
            docName = (TextView) view.findViewById(R.id.docName);
            docmarket = (TextView) view.findViewById(R.id.docmarket);
            docstatus = (TextView) view.findViewById(R.id.docstatus);
            docwaiting = (TextView) view.findViewById(R.id.docwaiting);
            masterLayout = (CardView) view.findViewById(R.id.masterLayout);

        }
    }
    @Override
    public vh onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rv_doctor_pending, parent, false);
        context = parent.getContext();
        return new vh(itemView);
    }
    @Override
    public void onBindViewHolder(@NonNull vh holder, int position) {
        if (dList != null) {
            DoctorARModel ps = dList.get(position);
            holder.createdat.setText(ps.getCreatedAt());
            holder.docName.setText(ps.getDoctorCode() +"("+ps.getStatus()+")");
            holder.docmarket.setText(ps.getMarketName());
            holder.docwaiting.setText(ps.getWaitingRole());
            //holder.docstatus.setText(ps.getActionStatus());
            if (ps.getActionStatus().equals("Pending")) {
                holder.docstatus.setText(ps.getActionStatus());
                holder.docstatus.setTextColor(Color.parseColor("#ffffff"));
                holder.docstatus.setBackgroundResource(R.drawable.shape_prepending);

            } else if (ps.getActionStatus().equals("Verified")) {
                holder.docstatus.setText(ps.getActionStatus());
                holder.docstatus.setTextColor(Color.parseColor("#ffffff"));
                holder.docstatus.setBackgroundResource(R.drawable.shape_pending);

            } else if (ps.getActionStatus().equals("Approved")) {
                holder.docstatus.setText(ps.getActionStatus());
                holder.docstatus.setTextColor(Color.parseColor("#ffffff"));
                holder.docstatus.setBackgroundResource(R.drawable.shape_approved);

            } else if (ps.getActionStatus().equals("Rejected")) {
                holder.docstatus.setText(ps.getActionStatus());
                holder.docstatus.setTextColor(Color.parseColor("#ffffff"));
                holder.docstatus.setBackgroundResource(R.drawable.shape_reject);
            }

        } else {
            SnackBarManagement._warning_CustomMessage(holder.masterLayout, "Doctor Not Found");
        }

    }
    @Override
    public int getItemCount() {

        if(dList!=null)
        {
            return dList.size();
        }
        return 0;
    }
}