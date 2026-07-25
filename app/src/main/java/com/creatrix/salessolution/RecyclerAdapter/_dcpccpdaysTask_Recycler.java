package com.creatrix.salessolution.RecyclerAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.creatrix.salessolution.Model.DcpCcpData;
import com.creatrix.salessolution.R;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;

public class _dcpccpdaysTask_Recycler extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public interface OnActionClick {
        void onMakeDcp(@NonNull DcpCcpData item);
        void onMakeCcp(@NonNull DcpCcpData item);
    }

    private static final int VIEW_DCP = 1;
    private static final int VIEW_CCP = 2;

    private final List<DcpCcpData> items = new ArrayList<>();
    private final OnActionClick listener;

    public _dcpccpdaysTask_Recycler(@NonNull List<DcpCcpData> data,
                                    @NonNull OnActionClick listener) {
        if (data != null) items.addAll(data);
        this.listener = listener;
        setHasStableIds(true);
    }

    @Override
    public int getItemViewType(int position) {
        String t = (items.get(position).getTypeName() + "").trim().toUpperCase();
        return "DCP".equals(t) ? VIEW_DCP : VIEW_CCP;
    }

    @Override
    public long getItemId(int position) {
        // stable id (DoctorId) helps for animations/binding
        return items.get(position).getDoctorId();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inf = LayoutInflater.from(parent.getContext());
        if (viewType == VIEW_DCP) {
            View v = inf.inflate(R.layout.item_dcp, parent, false);
            return new DcpVH(v);
        } else {
            View v = inf.inflate(R.layout.item_ccp, parent, false);
            return new CcpVH(v);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder h, int position) {
        DcpCcpData item = items.get(position);

        if (h instanceof DcpVH) {
            DcpVH vh = (DcpVH) h;
            vh.txtName.setText(item.getDoctorName());
            vh.txtType.setText("DCP");
            vh.btnMake.setText("Make DCP");
            vh.btnMake.setOnClickListener(v -> {
                if (listener != null) listener.onMakeDcp(item);
            });
        } else {
            CcpVH vh = (CcpVH) h;
            vh.txtName.setText(item.getDoctorName());
            vh.txtType.setText("CCP");
            vh.btnMake.setText("Make CCP");
            vh.btnMake.setOnClickListener(v -> {
                if (listener != null) listener.onMakeCcp(item);
            });
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    // Optional: data refresh
    public void replaceData(@NonNull List<DcpCcpData> data) {
        items.clear();
        items.addAll(data);
        notifyDataSetChanged();
    }

    static class DcpVH extends RecyclerView.ViewHolder {
        MaterialCardView card;
        TextView txtName, txtType;
        Button btnMake;
        DcpVH(@NonNull View itemView) {
            super(itemView);
            card = itemView.findViewById(R.id.cardRoot);
            txtName = itemView.findViewById(R.id.txtName);
            txtType = itemView.findViewById(R.id.txtType);
            btnMake = itemView.findViewById(R.id.btnMake);
        }
    }

    static class CcpVH extends RecyclerView.ViewHolder {
        MaterialCardView card;
        TextView txtName, txtType;
        Button btnMake;
        CcpVH(@NonNull View itemView) {
            super(itemView);
            card = itemView.findViewById(R.id.cardRoot);
            txtName = itemView.findViewById(R.id.txtName);
            txtType = itemView.findViewById(R.id.txtType);
            btnMake = itemView.findViewById(R.id.btnMake);
        }
    }
}