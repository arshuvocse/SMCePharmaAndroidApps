package com.creatrix.salessolution.Activity.Doctor.VisitPlan;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.creatrix.salessolution.Activity.Doctor.TourePlan.TourPlanDetailsActivity;
import com.creatrix.salessolution.Activity.Doctor.VisitPlan.Model.VisitplanModel;
import com.creatrix.salessolution.Interface.Rcv_TourPlanListener;
import com.creatrix.salessolution.Model.MonthDate;
import com.creatrix.salessolution.Model.TourPlanViewModel;
import com.creatrix.salessolution.R;
import com.creatrix.salessolution.Services.Constants;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;


public class DoctorVisitPlanListAdapter extends RecyclerView.Adapter<DoctorVisitPlanListAdapter.DVPH> {
    Context c;
    private final static int FADE_DURATION = 500; //FADE_DURATION in milliseconds
    private List<MonthDate> monthList;
    private List<VisitplanModel> vpList = new ArrayList<>();
    boolean is_Entry;


    public DoctorVisitPlanListAdapter(Context c, List<MonthDate> monthList, boolean is_Entry) {
        this.c = c;
        this.monthList = monthList;
        this.is_Entry = is_Entry;
    }
    @NonNull
    @Override
    public DVPH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.doctor_visit_plan_day_item, parent, false);
        c = parent.getContext();
        return new DVPH(v);
    }
    @Override
    public void onBindViewHolder(@NonNull DVPH holder, int position) {
        MonthDate data = monthList.get(position);
        vpList = data.getVisitplanList();
        if (data != null) {
            //Fragment fragment = null;
            holder.tv_day.setText(data.getDateName());
            holder.tv_date.setText(String.valueOf(data.getDateV()));
            if (data.getVisitplanList() != null) {
                if (vpList != null) {
                    String concatenatedDocName = "";
                    List<VisitplanModel> docname = vpList;
                    for (int i = 0; i < docname.size(); i++) {
                        concatenatedDocName += docname.get(i).getDoctorName();
                        if (i < docname.size() - 1) concatenatedDocName += ", ";
                    }
                    holder.tv_docName.setText(concatenatedDocName);
                } else {
                    holder.tv_docName.setText("-----");
                }
            } else {
            }
            if(is_Entry==false)
            {
                holder.visititem.setOnClickListener(null);
            }else {
                holder.visititem.setOnClickListener(v -> {
                    Pair[] pair = new Pair[2];
                    pair[0] = new Pair<View,String>(holder.tv_day,"daytrans");
                    pair[1] = new Pair<View,String>(holder.tv_date,"datetrans");
                    Intent in = new Intent(c, VisitPlanDetailsActivity.class);
                    in.putExtra("Month", data.getMonthName());
                    in.putExtra("Pos", String.valueOf(position));
                    in.putExtra("Year", String.valueOf(data.getYearV()));
                    in.putExtra("Date", String.valueOf(data.getDateV()));
                    in.putExtra("Day", data.getDateName());
                    in.putExtra("VDate", data.getDateValue());
                    Gson gson=new Gson();
                    String item=gson.toJson(data);
                    in.putExtra("itemjson", item);
                    c.startActivity(in);
                    if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                        ActivityOptions activityOptions =  ActivityOptions.makeSceneTransitionAnimation((Activity) c,pair);
                        c.startActivity(in,activityOptions.toBundle());
                      //  ((Activity) c).finish();
                    }else{
                        c.startActivity(in);
                       // ((Activity) c).finish();
                    }
                });
            }
        }
    }
    @Override
    public int getItemCount() {
        return monthList.size();
    }
    public class DVPH extends RecyclerView.ViewHolder {
        TextView tv_day, tv_date, tv_docName;
        CardView visititem;
        public DVPH(@NonNull View v) {
            super(v);
            tv_day = v.findViewById(R.id.tv_day);
            tv_date = v.findViewById(R.id.tv_date);
            tv_docName = v.findViewById(R.id.tv_docName);
            visititem = v.findViewById(R.id.visititem);
        }
    }
}
