package com.creatrix.salessolution.Activity.Reports.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.creatrix.salessolution.Activity.Reports.Model.ModelDWSPReport;
import com.creatrix.salessolution.R;
import java.util.List;

public class AdapterDwspReport extends RecyclerView.Adapter<AdapterDwspReport.BookViewHolder> {
    public Context context;
    public List<ModelDWSPReport> aList;
    int lastposition = -1;

    public AdapterDwspReport(List<ModelDWSPReport> aList, Context context) {
        this.aList = aList;
        this.context = context;
    }


    public static class BookViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_teritory,tv_first_week,tv_first_week_prcnt,tv_second_week,tv_second_week_prcnt,tv_third_week
                ,tv_third_week_prcnt,tv_restday,tv_restday_prcnt,total;
        public BookViewHolder(View view) {
            super(view);
            tv_teritory = view.findViewById(R.id.tv_teritory);
            tv_first_week = view.findViewById(R.id.tv_first_week);
            tv_first_week_prcnt = view.findViewById(R.id.tv_first_week_prcnt);
            tv_second_week = view.findViewById(R.id.tv_second_week);
            tv_second_week_prcnt = view.findViewById(R.id.tv_second_week_prcnt);
            tv_third_week = view.findViewById(R.id.tv_third_week);
            tv_third_week_prcnt = view.findViewById(R.id.tv_third_week_prcnt);
            tv_restday = view.findViewById(R.id.tv_restday);
            tv_restday_prcnt = view.findViewById(R.id.tv_restday_prcnt);
            total = view.findViewById(R.id.total);
        }
    }

    @Override
    public AdapterDwspReport.BookViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rv_dwsp_report, parent, false);
        return new BookViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(AdapterDwspReport.BookViewHolder holder, int position) {
        ModelDWSPReport dr= aList.get(holder.getAdapterPosition());
        if(dr!=null)
        {
            holder.tv_teritory.setText(dr.getTerritory());
            holder.tv_first_week.setText(dr.getFirstWeek());
            holder.tv_first_week_prcnt.setText(dr.getFirstWeek_Percent());

            holder.tv_second_week.setText(dr.getSecondWeek());
            holder.tv_second_week_prcnt.setText(dr.getSecondWeek_Percent());
            holder.tv_third_week.setText(dr.getThirdWeek());
            holder.tv_third_week_prcnt.setText(dr.getThirdWeek_Percent());
            holder.tv_restday.setText(dr.getRestDay());
            holder.tv_restday_prcnt.setText(dr.getRestDay_Percent());
            holder.total.setText(dr.getTotal());
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
