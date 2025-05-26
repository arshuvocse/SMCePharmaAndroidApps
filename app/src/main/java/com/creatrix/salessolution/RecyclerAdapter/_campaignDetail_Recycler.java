package com.creatrix.salessolution.RecyclerAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.creatrix.salessolution.Model.BonusCampaignViewModel;
import com.creatrix.salessolution.Model.CampaignDetailNew;
import com.creatrix.salessolution.Model.Product;
import com.creatrix.salessolution.R;

import java.util.List;

public class _campaignDetail_Recycler extends RecyclerView.Adapter<_campaignDetail_Recycler.BookViewHolder> {
    private Context context;
    private List<CampaignDetailNew> aList;

    public _campaignDetail_Recycler(List<CampaignDetailNew> nList) {
        this.aList = nList;

    }
    public class BookViewHolder extends RecyclerView.ViewHolder {
        public TextView camPaignName;

        public BookViewHolder(View view) {
            super(view);
            camPaignName = (TextView) view.findViewById(R.id.camPaignName);

        }
    }

    @Override
    public _campaignDetail_Recycler.BookViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.zrv_campaigndetail, parent, false);
        context = parent.getContext();
        return new _campaignDetail_Recycler.BookViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(_campaignDetail_Recycler.BookViewHolder holder, int position) {
        holder.camPaignName.setText(aList.get(position).getTypeName());
    }

    @Override
    public int getItemCount() {
        return aList.size();
    }
}
