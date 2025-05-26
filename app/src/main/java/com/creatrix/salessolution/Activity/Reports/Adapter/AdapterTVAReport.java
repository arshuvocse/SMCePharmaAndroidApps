package com.creatrix.salessolution.Activity.Reports.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.creatrix.salessolution.Activity.Reports.Model.ModelGiftBonus;
import com.creatrix.salessolution.Activity.SelfReports.TVAReport.Model.ModelTVADao;
import com.creatrix.salessolution.R;

import java.util.List;

public class AdapterTVAReport extends RecyclerView.Adapter<AdapterTVAReport.BookViewHolder> {
    public Context context;
    public List<ModelTVADao> aList;


    public AdapterTVAReport(List<ModelTVADao> aList, Context context) {
        this.aList = aList;
        this.context = context;
    }

    public static class BookViewHolder extends RecyclerView.ViewHolder {
        public TextView tname,target,order,orderAch,invoice,invAch,sales,salesAch;
        public BookViewHolder(View view) {
            super(view);
            tname = view.findViewById(R.id.tname);
            target = view.findViewById(R.id.target);
            order = view.findViewById(R.id.order);
            orderAch = view.findViewById(R.id.orderAch);
            invoice = view.findViewById(R.id.invoice);
            invAch = view.findViewById(R.id.invAch);
            sales = view.findViewById(R.id.sales);
            salesAch = view.findViewById(R.id.salesAch);
        }
    }

    @Override
    public AdapterTVAReport.BookViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rv_tva_new, parent, false);
        return new BookViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(AdapterTVAReport.BookViewHolder holder, int position) {
        ModelTVADao dr= aList.get(holder.getAdapterPosition());
        if(dr!=null)
        {
            holder.tname.setText(dr.getTerritoryName());
            holder.target.setText(dr.getTargetValue());
            holder.order.setText(dr.getOrderValue());
            holder.orderAch.setText(dr.getOrderAchiv());
            holder.invoice.setText(dr.getInvoiceValue());
            holder.invAch.setText(dr.getInvoiceAchiv());
            holder.sales.setText(dr.getSalesValue());
            holder.salesAch.setText(dr.getSalesAchiv());

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
