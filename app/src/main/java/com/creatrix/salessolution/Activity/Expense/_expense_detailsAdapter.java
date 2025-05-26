package com.creatrix.salessolution.Activity.Expense;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import com.creatrix.salessolution.Model.Expense.ADetailListDAO;
import com.creatrix.salessolution.Model.ExpenseReportViewModel;
import com.creatrix.salessolution.Model.ExpenseTypeDetails;
import com.creatrix.salessolution.R;


import java.util.List;

public class _expense_detailsAdapter extends RecyclerView.Adapter<_expense_detailsAdapter.EVHolder> {
    Context context;
    public List<ADetailListDAO> dataList;
    public List<ExpenseTypeDetails> detailList;
    int who;

    public _expense_detailsAdapter(List<ADetailListDAO> dataList, int who) {
        this.dataList = dataList;
        this.who = who;
    }
    public _expense_detailsAdapter(Context context,List<ExpenseTypeDetails> detailList, int who) {
        this.detailList = detailList;
        this.who = who;
    }
    @NonNull
    @Override
    public EVHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rv_expdetaillist, parent, false);
        context = parent.getContext();
        return new EVHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull EVHolder holder, int position) {
        if(who==2){
            if(detailList!=null)
            {
                ExpenseTypeDetails datas=detailList.get(position);
                holder.exp_colname.setText(datas.getFieldName());
                holder.exp_colvalue.setText(datas.getValueText());
            }
        }
        if(who==1) {
            if(dataList!=null)
            {
                ADetailListDAO data=dataList.get(position);
                holder.exp_colname.setText(data.getFieldName());
                holder.exp_colvalue.setText(data.getValueText());
            }
        }


    }

    @Override
    public int getItemCount() {
        int datasize=0;
        if(who==1)
        {
            datasize=dataList.size();
           // return dataList.size();
            return datasize;

        }
        if(who==2)
        {
            datasize=detailList.size();
            //return detailList.size();
            return datasize;
        }
        //return who;
        return datasize;
    }

    public static class EVHolder extends RecyclerView.ViewHolder {
        public TextView exp_colname,exp_colvalue;
        ConstraintLayout llma;
        public EVHolder(@NonNull View view) {
            super(view);
            exp_colname = view.findViewById(R.id.exp_colname);
            exp_colvalue = view.findViewById(R.id.exp_colvalue);
            llma = view.findViewById(R.id.llma);
        }
    }
}