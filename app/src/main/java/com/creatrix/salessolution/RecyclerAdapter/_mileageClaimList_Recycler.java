package com.creatrix.salessolution.RecyclerAdapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.creatrix.salessolution.Activity.Expense.ExpenceClaimViewActivity;
import com.creatrix.salessolution.Activity.MileageClaim.MileageClaimViewActivity;
import com.creatrix.salessolution.Model.ExpenseReportViewModel;
import com.creatrix.salessolution.Model.MilageClaimReport;
import com.creatrix.salessolution.R;
import com.creatrix.salessolution.Services.Constants;
import com.google.gson.Gson;

import java.util.List;

public class _mileageClaimList_Recycler extends RecyclerView.Adapter<_mileageClaimList_Recycler.BookViewHolder> {
    private Context context;
    private List<MilageClaimReport> aList;
    int lastposition = -1;

    public _mileageClaimList_Recycler(List<MilageClaimReport> nList) {
        this.aList = nList;
    }


    public class BookViewHolder extends RecyclerView.ViewHolder {
        public TextView dateTxt,nameTxt,transportTxt,statusTxt,mileageTxt,submarketTxt,meterReadingId;
        public LinearLayout masterLayout;

        public BookViewHolder(View view) {
            super(view);
            dateTxt = view.findViewById(R.id.dateTxt);
            nameTxt = view.findViewById(R.id.nameTxt);
            transportTxt = view.findViewById(R.id.transportTxt);
            statusTxt = view.findViewById(R.id.statusTxt);
            mileageTxt = view.findViewById(R.id.mileageTxt);
            submarketTxt = view.findViewById(R.id.submarketTxt);
            masterLayout = view.findViewById(R.id.masterLayout);
            meterReadingId = view.findViewById(R.id.meterReadingId);

//            view.setOnLongClickListener(v -> {
//                String  appStatus = aList.get(getAdapterPosition()).getApprovalStatus();
//                if(appStatus.equals("Pending")){
//                    onLongClickDelete(getAdapterPosition());
//                    return true;
//                }else {
//                    return false;
//                }
//
//            });
        }
    }

    @Override
    public _mileageClaimList_Recycler.BookViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.zrv_mileage_claimlist, parent, false);
        context = parent.getContext();
        return new _mileageClaimList_Recycler.BookViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(_mileageClaimList_Recycler.BookViewHolder holder, int position) {
        if (aList != null) {
            MilageClaimReport mcr = aList.get(position);
            holder.dateTxt.setText(mcr.getMileageDate());
            holder.nameTxt.setText(mcr.getEmpName());
            holder.transportTxt.setText(mcr.getTransportName());
            holder.meterReadingId.setText(String.valueOf(mcr.getMeterReading()));

            // holder.statusTxt.setText(mcr.getApprovalStatus());
            if (mcr.getApprovalStatus().equals("0")) {
                holder.statusTxt.setText("Pending..");
                holder.statusTxt.setTextColor(Color.parseColor("#ff7400"));
            } else if (mcr.getApprovalStatus().equals("1")) {
                holder.statusTxt.setText("Verified");
                holder.statusTxt.setBackgroundResource(R.drawable.shape_approved);
            } else if (mcr.getApprovalStatus().equals("2")) {
                holder.statusTxt.setText("Approved");
                holder.statusTxt.setTextColor(Color.parseColor("#00b248"));
            }
            else if (mcr.getApprovalStatus().equals("3")) {
                holder.statusTxt.setText("Rejected");
                holder.statusTxt.setTextColor(Color.parseColor("#C12222"));

            }
            holder.statusTxt.setOnClickListener(v -> {
                Constants.WHO="Mileageadapter";
                Intent got_details = new Intent(context, MileageClaimViewActivity.class);
                Gson gson = new Gson();
                String myJson = gson.toJson(mcr);
                got_details.putExtra("MilDetails", myJson);
                context.startActivity(got_details);

            });
            holder.mileageTxt.setText(Double.toString(mcr.getMileageInKM()));
            holder.submarketTxt.setText(mcr.getMarketName().toString());
        }
    }

    @Override
    public int getItemCount() {
        return aList.size();
    }


}
