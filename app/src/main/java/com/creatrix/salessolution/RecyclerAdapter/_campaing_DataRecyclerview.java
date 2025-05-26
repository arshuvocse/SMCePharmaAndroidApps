package com.creatrix.salessolution.RecyclerAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import com.creatrix.salessolution.Model.Rp_CampainViewModel;
import com.creatrix.salessolution.NormalAdapter.OfferListAdapter;
import com.creatrix.salessolution.R;

import java.util.List;

public class _campaing_DataRecyclerview extends RecyclerView.Adapter<_campaing_DataRecyclerview.BookViewHolder> {
    private Context context;
    private final static int FADE_DURATION = 500; //FADE_DURATION in milliseconds
    private List<Rp_CampainViewModel> aList;
    int lastposition = -1;

    public _campaing_DataRecyclerview(List<Rp_CampainViewModel> nList) {
        this.aList = nList;
    }


    public class BookViewHolder extends RecyclerView.ViewHolder {
        public TextView cmpName;
        public TextView cmpDesc;
        public TextView fromDateTxt;
        public TextView toDateTxt;
        public ListView offerlist;
        public BookViewHolder(View view) {
            super(view);
            cmpName = (TextView) view.findViewById(R.id.cmpName);
            cmpDesc = (TextView) view.findViewById(R.id.cmpDesc);
            fromDateTxt = (TextView) view.findViewById(R.id.fromDateTxt);
            toDateTxt = (TextView) view.findViewById(R.id.toDateTxt);
            offerlist = (ListView) view.findViewById(R.id.listOffer);
        }
    }

    @Override
    public _campaing_DataRecyclerview.BookViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.zrv_campain_dataview, parent, false);
        context = parent.getContext();
        return new _campaing_DataRecyclerview.BookViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(_campaing_DataRecyclerview.BookViewHolder holder, int position) {
        holder.cmpName.setText(aList.get(position).getCampaignName());
        holder.cmpDesc.setText(aList.get(position).getCampaignDesc());
        holder.fromDateTxt.setText(aList.get(position).getFromDate());
        holder.toDateTxt.setText(aList.get(position).getTodate());

        OfferListAdapter offerAdapter = new OfferListAdapter(context, aList.get(position).getCampaingDetails());
        holder.offerlist.setAdapter(offerAdapter);

    }

    @Override
    public int getItemCount() {
        return aList.size();
    }
}