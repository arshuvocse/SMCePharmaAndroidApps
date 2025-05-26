package com.creatrix.salessolution.Activity.DWSP.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.creatrix.salessolution.Activity.DWSP.Model.ModelDWSPTargetArea;
import com.creatrix.salessolution.R;

import java.util.List;

public class DWSP_TargetAdapter extends RecyclerView.Adapter<DWSP_TargetAdapter.VH> {
    Context context;
    List<ModelDWSPTargetArea> dList;

    public DWSP_TargetAdapter(Context context, List<ModelDWSPTargetArea> dList) {
        this.context = context;
        this.dList = dList;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemview = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rv_dwsptarget_table, parent, false);
        return new DWSP_TargetAdapter.VH(itemview);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        ModelDWSPTargetArea md=dList.get(position);
        holder.rv_sn.setText(String.valueOf(holder.getAdapterPosition()+1));
        holder.rv_name.setText(md.getName());
        holder.rv_amount.setText(md.getAmount());
    }

    @Override
    public int getItemCount() {
        if(dList!=null|| dList.size()>0)
        {
            return dList.size();
        }
        return 0;
    }

    public static class VH extends RecyclerView.ViewHolder {
        TextView rv_sn,rv_name;
        EditText rv_amount;
        public VH(@NonNull View itemView) {
            super(itemView);
            rv_sn =itemView.findViewById(R.id.rv_sn);
            rv_name =itemView.findViewById(R.id.rv_name);
            rv_amount =itemView.findViewById(R.id.rv_amount);
        }
    }
}
