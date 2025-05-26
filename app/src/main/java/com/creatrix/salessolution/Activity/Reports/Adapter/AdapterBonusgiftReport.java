package com.creatrix.salessolution.Activity.Reports.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.creatrix.salessolution.Activity.Reports.BonusGift.ActivityBonusGift;
import com.creatrix.salessolution.Activity.Reports.Model.ModelDWSPReport;
import com.creatrix.salessolution.Activity.Reports.Model.ModelGiftBonus;
import com.creatrix.salessolution.Activity.Reports.Model.ResponseBonusGift;
import com.creatrix.salessolution.R;

import java.util.List;

public class AdapterBonusgiftReport extends RecyclerView.Adapter<AdapterBonusgiftReport.BookViewHolder> {
    public Context context;
    public List<ModelGiftBonus> aList;


    public AdapterBonusgiftReport(List<ModelGiftBonus> aList, Context context) {
        this.aList = aList;
        this.context = context;
    }

    public static class BookViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_orderid,tv_inv,tv_date,tv_bonusqty,tv_giftqty;
        public BookViewHolder(View view) {
            super(view);
            tv_orderid = view.findViewById(R.id.tv_orderid);
            tv_inv = view.findViewById(R.id.tv_inv);
            tv_date = view.findViewById(R.id.tv_date);
            tv_bonusqty = view.findViewById(R.id.tv_bonusqty);
            tv_giftqty = view.findViewById(R.id.tv_giftqty);
        }
    }

    @Override
    public AdapterBonusgiftReport.BookViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rv_report_bonus_gift, parent, false);
        return new BookViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(AdapterBonusgiftReport.BookViewHolder holder, int position) {
        ModelGiftBonus dr= aList.get(holder.getAdapterPosition());
        if(dr!=null)
        {
            holder.tv_orderid.setText(dr.getOrderNo());
            holder.tv_inv.setText(dr.getInvoiceNo());
            holder.tv_date.setText(dr.getInvoiceDate());

            holder.tv_bonusqty.setText(dr.getBounsQty());
            holder.tv_giftqty.setText(dr.getGiftQty());
        }

    }
    @Override
    public int getItemCount() {
        if(aList!=null ||aList.size()>0)
        {
            return aList.size();
        }else {
            return 0;
        }

    }
}
