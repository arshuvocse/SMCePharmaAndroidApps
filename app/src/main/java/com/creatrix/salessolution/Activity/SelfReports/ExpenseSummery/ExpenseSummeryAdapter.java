package com.creatrix.salessolution.Activity.SelfReports.ExpenseSummery;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.creatrix.salessolution.Activity.SelfReports.ExpenseSummery.Model.ExpenseSummery;
import com.creatrix.salessolution.Activity.SelfReports.SalesReport.Model.CWSData;
import com.creatrix.salessolution.R;

import java.text.DecimalFormat;
import java.util.List;

public class ExpenseSummeryAdapter extends RecyclerView.Adapter<ExpenseSummeryAdapter.dvh> {
    private Context context;
    private Activity activity;
    private DecimalFormat df3 = new DecimalFormat("#.###");
    private final static int FADE_DURATION = 500; //FADE_DURATION in milliseconds
    private List<ExpenseSummery> sumList;
    public ExpenseSummeryAdapter(List<ExpenseSummery> sumList, Context context) {
        this.sumList = sumList;
        this.context = context;
    }

    @Override
    public dvh onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rv_expense_summery, parent, false);
        context = parent.getContext();
        return new dvh(itemView);
    }
    @Override
    public void onBindViewHolder(dvh holder, int position) {
        ExpenseSummery ad = sumList.get(position);
        holder.date.setText(ad.getDate());
        holder.details.setText(ad.getMarketTourType());
        holder.da_amount.setText(ad.getDaAmount());
        holder.mileage_amount.setText(ad.getMileageAmount());
        holder.expense_amount.setText(ad.getExpenseAmount());
        holder.total_amount.setText(ad.getTotalAmount());
    }

    @Override
    public int getItemCount() {
        return sumList.size();
    }


    public class dvh extends RecyclerView.ViewHolder {
        public TextView date,da_amount,mileage_amount,expense_amount,total_amount,details;
        public dvh(View view) {
            super(view);
            date = (TextView) view.findViewById(R.id.date);
            da_amount = (TextView) view.findViewById(R.id.da_amount);
            mileage_amount = (TextView) view.findViewById(R.id.mileage_amount);
            expense_amount = (TextView) view.findViewById(R.id.expense_amount);
            total_amount = (TextView) view.findViewById(R.id.total_amount);
            details = (TextView) view.findViewById(R.id.details);
        }
    }


    private void setFadeAnimation(View view) {
        AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(FADE_DURATION);
        view.startAnimation(anim);
    }


}
