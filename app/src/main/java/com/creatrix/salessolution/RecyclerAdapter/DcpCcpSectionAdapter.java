package com.creatrix.salessolution.RecyclerAdapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.creatrix.salessolution.Model.DcpCcpData;
import com.creatrix.salessolution.R;

import java.util.ArrayList;
import java.util.List;

public class DcpCcpSectionAdapter extends RecyclerView.Adapter<DcpCcpSectionAdapter.SectionVH> {

    public interface OnAction {
        void onMakeDcp(DcpCcpData item);
        void onMakeCcp(DcpCcpData item);
    }

    private static final int SEC_DCP = 1;
    private static final int SEC_CCP = 2;

    private final List<DcpCcpData> dcpList;
    private final List<DcpCcpData> ccpList;
    private final List<Integer> sections = new ArrayList<>();
    private final OnAction onAction;

    public DcpCcpSectionAdapter(List<DcpCcpData> all, OnAction onAction) {
        this.onAction = onAction;
        // split once
        dcpList = new ArrayList<>();
        ccpList = new ArrayList<>();
        if (all != null) {
            for (DcpCcpData x : all) {
                String t = (x.getTypeName() + "").trim().equalsIgnoreCase("DCP") ? "DCP" : "CVP";
                if ("DCP".equals(t)) dcpList.add(x); else ccpList.add(x);
            }
        }
        if (!dcpList.isEmpty()) sections.add(SEC_DCP);
        if (!ccpList.isEmpty()) sections.add(SEC_CCP);
        setHasStableIds(true);
    }

    @Override public int getItemCount() { return sections.size(); }

    @Override public int getItemViewType(int position) { return sections.get(position); }

    @Override public long getItemId(int position) { return sections.get(position); }

    @NonNull
    @Override
    public SectionVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(com.creatrix.salessolution.R.layout.item_section_card, parent, false);
        return new SectionVH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SectionVH h, int pos) {
        int sec = sections.get(pos);
        boolean isDcp = (sec == SEC_DCP);
        h.txtSectionTitle.setText(isDcp ? "DCP" : "CVP" );
//
//        // setup inner list
//        h.innerList.setLayoutManager(new LinearLayoutManager(h.innerList.getContext()));
//        h.innerList.setNestedScrollingEnabled(false);
//
//        if (isDcp) {
//            h.innerList.setAdapter(new SectionItemAdapter(dcpList, true, item -> {
//                if (onAction != null) onAction.onMakeDcp(item);
//            }));
//        } else {
//            h.innerList.setAdapter(new SectionItemAdapter(ccpList, false, item -> {
//                if (onAction != null) onAction.onMakeCcp(item);
//            }));
//        }
    }

    static class SectionVH extends RecyclerView.ViewHolder {
        TextView txtSectionTitle;
        RecyclerView innerList;
        SectionVH(@NonNull View itemView) {
            super(itemView);
            txtSectionTitle = itemView.findViewById(R.id.txtSectionTitle);
            innerList = itemView.findViewById(R.id.innerList);
        }
    }
}

