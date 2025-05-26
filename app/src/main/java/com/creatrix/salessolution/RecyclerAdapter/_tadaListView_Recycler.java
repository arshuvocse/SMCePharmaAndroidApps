package com.creatrix.salessolution.RecyclerAdapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.creatrix.salessolution.Model.ExpenseReportViewModel;
import com.creatrix.salessolution.Model.TadaList;
import com.creatrix.salessolution.R;

import java.util.List;

public class _tadaListView_Recycler extends RecyclerView.Adapter<_tadaListView_Recycler.BookViewHolder> {
    public Context context;
    private List<TadaList> aList;
    public _tadaListView_Recycler(Context context,List<TadaList> nList) {
        this.context = context;
        this.aList = nList;
    }


    public static class BookViewHolder extends RecyclerView.ViewHolder {
        public TextView dateTxt, nameTxt, daTxt,statusTxt,marketTxt;
        public LinearLayout masterLayout;
        public BookViewHolder(View view) {
            super(view);
            dateTxt = view.findViewById(R.id.dateTxt);
            nameTxt = view.findViewById(R.id.nameTxt);
            statusTxt = view.findViewById(R.id.statusTxt);
            daTxt = view.findViewById(R.id.daTxt);
            marketTxt = view.findViewById(R.id.marketTxt);
            masterLayout = view.findViewById(R.id.masterLayout);
        }
    }

    @NonNull
    @Override
    public _tadaListView_Recycler.BookViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.zrv_tadalist, parent, false);
        //context = parent.getContext();
        return new BookViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(_tadaListView_Recycler.BookViewHolder holder, int position) {
        holder.dateTxt.setText(aList.get(position).getTadaDate());
        holder.nameTxt.setText(aList.get(position).getEmpName());
        holder.statusTxt.setText(aList.get(position).getApprovalStatus());
        holder.daTxt.setText(aList.get(position).getDaAmt());
        holder.marketTxt.setText(aList.get(position).getMarket());

        if(aList.get(position).getApprovalStatus().equals("0")){
            holder.statusTxt.setText("Pending");
            holder.statusTxt.setTextColor(Color.parseColor("#ff7400"));
        }else if(aList.get(position).getApprovalStatus().equals("1")){
            holder.statusTxt.setText("Approved");
            holder.statusTxt.setTextColor(Color.parseColor("#4169e1"));
        }
        else if(aList.get(position).getApprovalStatus().equals("2")){
            holder.statusTxt.setText("Accepted");
            holder.statusTxt.setTextColor(Color.parseColor("#00b248"));
        }
        else if(aList.get(position).getApprovalStatus().equals("3")){
            holder.statusTxt.setText("Rejected");
            holder.statusTxt.setTextColor(Color.parseColor("#C12222"));

        }
    }

    @Override
    public int getItemCount() {
        return aList.size();
    }
}
