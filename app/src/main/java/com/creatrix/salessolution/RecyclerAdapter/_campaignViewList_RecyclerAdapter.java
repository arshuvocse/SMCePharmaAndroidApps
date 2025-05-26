package com.creatrix.salessolution.RecyclerAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.creatrix.salessolution.Model.BonusCampaignViewModel;
import com.creatrix.salessolution.Model.CampaignMasterNew;
import com.creatrix.salessolution.R;

import java.util.ArrayList;
import java.util.List;

public class _campaignViewList_RecyclerAdapter extends RecyclerView.Adapter<_campaignViewList_RecyclerAdapter.SingleViewHolder> {

    private Context context;
    private List<CampaignMasterNew> employees;
    // if checkedPosition = -1, there is no default selection
    // if checkedPosition = 0, 1st item is selected by default
    private int checkedPosition = 0;

    public _campaignViewList_RecyclerAdapter(Context context, List<CampaignMasterNew> employees) {
        this.context = context;
        this.employees = employees;
    }

//    public void setEmployees(ArrayList<CampaignMasterNew> employees) {
//        this.employees = new ArrayList<>();
//        this.employees = employees;
//        notifyDataSetChanged();
//    }

    @NonNull
    @Override
    public SingleViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.zrv_campaign_list, viewGroup, false);
        return new SingleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SingleViewHolder singleViewHolder, int position) {
        singleViewHolder.bind(employees.get(position));
    }

    @Override
    public int getItemCount() {
        return employees.size();
    }

    class SingleViewHolder extends RecyclerView.ViewHolder {

        private TextView textView;
        private RadioButton imageView;
        private RecyclerView rcv;

        SingleViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.campName);
            imageView = itemView.findViewById(R.id.radioCamp);
            rcv = itemView.findViewById(R.id.recycler_viewd);
        }

        void bind(final CampaignMasterNew employee) {
            if (checkedPosition == -1) {
                imageView.setChecked(false);
            } else {
                if (checkedPosition == getAdapterPosition()) {
                    imageView.setChecked(true);
                } else {
                    imageView.setChecked(false);
                }
            }
            textView.setText(employee.getCampaignName());

            _campaignDetail_Recycler mAdapterNested ;
            rcv.setLayoutManager(new LinearLayoutManager(context));
            rcv.addItemDecoration(new DividerItemDecoration(context, LinearLayoutManager.VERTICAL));
            mAdapterNested = new _campaignDetail_Recycler(employee.getCampDetail());
            rcv.setAdapter(mAdapterNested);

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    imageView.setChecked(true);
                    if (checkedPosition != getAdapterPosition()) {
                        notifyItemChanged(checkedPosition);
                        checkedPosition = getAdapterPosition();
                    }
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    imageView.setChecked(true);
                    if (checkedPosition != getAdapterPosition()) {
                        notifyItemChanged(checkedPosition);
                        checkedPosition = getAdapterPosition();
                    }
                }
            });
        }
    }

    public CampaignMasterNew getSelected() {
        if (checkedPosition != -1) {
            return employees.get(checkedPosition);
        }
        return null;
    }
}
