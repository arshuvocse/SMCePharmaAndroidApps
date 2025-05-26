package com.creatrix.salessolution.Activity.Doctor.Pending;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.creatrix.salessolution.Activity.Customer.CustomerPropertyChangeActivity;
import com.creatrix.salessolution.Activity.Customer.Pending.CustomerARModel;
import com.creatrix.salessolution.Activity.Doctor.DoctorPropertyChangeActivity;
import com.creatrix.salessolution.R;
import com.creatrix.salessolution.UtilityHelper.SnackBarManagement;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import ozaydin.serkan.com.image_zoom_view.ImageViewZoom;

public class DoctorUpdatAdapter extends RecyclerView.Adapter<DoctorUpdatAdapter.vh> {
    private Context context;
    List<DoctorARModel> dList;

    public DoctorUpdatAdapter(List<DoctorARModel> dList, Context context) {
        this.dList = dList;
        this.context = context;
    }

    public static class vh extends RecyclerView.ViewHolder {
        public TextView docName, docmarket, docstatus, createdat, docwaiting, txtdocstatus;
        CardView masterLayout;
        LinearLayout llapproval_waiting;

        public vh(View view) {
            super(view);
            createdat = (TextView) view.findViewById(R.id.createdat);
            docName = (TextView) view.findViewById(R.id.docName);
            docmarket = (TextView) view.findViewById(R.id.docmarket);
            txtdocstatus = (TextView) view.findViewById(R.id.txtdocstatus);
            docstatus = (TextView) view.findViewById(R.id.docstatus);
            docwaiting = (TextView) view.findViewById(R.id.docwaiting);
            masterLayout = (CardView) view.findViewById(R.id.masterLayout);
            llapproval_waiting = (LinearLayout) view.findViewById(R.id.llapproval_waiting);

        }
    }

    @NonNull
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
            holder.docwaiting.setVisibility(View.GONE);
            holder.docstatus.setVisibility(View.GONE);
            holder.txtdocstatus.setVisibility(View.GONE);
            holder.llapproval_waiting.setVisibility(View.GONE);


            holder.createdat.setText(ps.getCreatedAt());
            holder.docName.setText(ps.getDoctorCode());
            holder.docmarket.setText(ps.getMarketName());
            holder.masterLayout.setOnClickListener(view -> {
                Intent gotos = new Intent(context, DoctorPropertyChangeActivity.class);
                Gson gson = new Gson();
                String data = gson.toJson(ps);
                System.out.println("data adapter : " + data);
                gotos.putExtra("DoctorData", data);
                context.startActivity(gotos);
            });

        } else {
            SnackBarManagement._warning_CustomMessage(holder.masterLayout, "Customer Not Found");
        }
    }

    @Override
    public int getItemCount() {
        if (dList != null) {
            return dList.size();
        }
        return 0;
    }

    public void filterList(ArrayList<DoctorARModel> aList) {
        dList = aList;
        notifyDataSetChanged();
    }
}