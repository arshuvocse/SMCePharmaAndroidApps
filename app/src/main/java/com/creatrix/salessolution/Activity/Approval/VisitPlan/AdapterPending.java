package com.creatrix.salessolution.Activity.Approval.VisitPlan;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.creatrix.salessolution.Activity.Approval.VisitPlan.Model.ModelPending;
import com.creatrix.salessolution.R;

import java.util.List;

public class AdapterPending extends RecyclerView.Adapter<AdapterPending.SingleViewHolder> {

    private Context context;
    boolean isVisible;
    private List<ModelPending> reportsMenus;

    public AdapterPending(Context context, List<ModelPending> reportsMenus) {
        this.context = context;
        this.reportsMenus = reportsMenus;
    }

    @NonNull
    @Override
    public SingleViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.lay_pending, viewGroup, false);
        return new SingleViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull SingleViewHolder holder, int position) {
        ModelPending rm = reportsMenus.get(position);
        holder.menuName.setText(rm.getApp_Title());
        holder.value.setText(rm.getApp_value());
    }

    @Override
    public int getItemCount() {
        return reportsMenus.size();
    }


    static class SingleViewHolder extends RecyclerView.ViewHolder {
        TextView menuName,value;

        SingleViewHolder(@NonNull View itemView) {
            super(itemView);
            menuName = itemView.findViewById(R.id.menuName);
            value = itemView.findViewById(R.id.value);
        }

    }
}
