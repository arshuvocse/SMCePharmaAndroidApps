package com.creatrix.salessolution.RecyclerAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.creatrix.salessolution.Model.DcpCcpData;
import com.creatrix.salessolution.Model.Section;
import com.creatrix.salessolution.R;

import java.util.ArrayList;
import java.util.List;

public class DcpCcpDynamicSectionAdapter extends RecyclerView.Adapter<DcpCcpDynamicSectionAdapter.SectionVH> {

    public interface OnAction {
        void onAction(String typeName, DcpCcpData item); // কোন টাইপের কার্ড থেকে ক্লিক হলো, সেই info সহ
    }

    private final List<Section> sections;
    private final OnAction onAction;

    public DcpCcpDynamicSectionAdapter(List<Section> sections, OnAction onAction) {
        this.sections = sections == null ? new ArrayList<>() : sections;
        this.onAction = onAction;
        setHasStableIds(true);
    }

    @Override public int getItemCount() { return sections.size(); }
    @Override public long getItemId(int position) { return sections.get(position).typeName.hashCode(); }

    @NonNull
    @Override
    public SectionVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_section_card, parent, false);
        return new SectionVH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SectionVH h, int pos) {
        Section sec = sections.get(pos);
        h.txtSectionTitle.setText(sec.typeName);

        h.innerList.setLayoutManager(new LinearLayoutManager(h.innerList.getContext()));
        h.innerList.setNestedScrollingEnabled(false);

        h.innerList.setAdapter(new SectionItemAdapter(sec.items, sec.typeName, (typeName, item) -> {
            if (onAction != null) onAction.onAction(typeName, item);
        }));
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
