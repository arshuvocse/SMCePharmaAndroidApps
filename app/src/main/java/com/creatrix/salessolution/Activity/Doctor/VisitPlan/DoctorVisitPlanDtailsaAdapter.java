package com.creatrix.salessolution.Activity.Doctor.VisitPlan;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.creatrix.salessolution.Activity.Doctor.VisitPlan.Model.VisitplanModel;
import com.creatrix.salessolution.Interface.DeleteListener;
import com.creatrix.salessolution.Interface.Rcv_TourPlanListener;
import com.creatrix.salessolution.Model.MonthDate;
import com.creatrix.salessolution.R;
import com.creatrix.salessolution.UtilityHelper.UtilityHelper;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DoctorVisitPlanDtailsaAdapter extends RecyclerView.Adapter<DoctorVisitPlanDtailsaAdapter.VPDH> {
    Context c;
    private final static int FADE_DURATION = 500; //FADE_DURATION in milliseconds
    private MonthDate monthDate;
    private List<VisitplanModel> vpdocList;
    DeleteListener dListener;


    public DoctorVisitPlanDtailsaAdapter(Context c, MonthDate monthDate,DeleteListener dListener) {
        this.c = c;
        this.monthDate = monthDate;
        this.dListener = dListener;
        notifyDataSetChanged();

    }
   /* public DoctorVisitPlanDtailsaAdapter(Context c, List<VisitplanModel> vpdocList) {
        this.c = c;
        this.vpdocList = vpdocList;

    }*/
    @NonNull
    @Override
    public VPDH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.doctor_visit_plan_details_item, parent, false);
        c = parent.getContext();
        return new VPDH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VPDH holder, int position) {
        try {
            VisitplanModel doclist = monthDate.getVisitplanList().get(position);
            // VisitplanModel doclist = vpdocList.get(position);
            Dialog d = new Dialog(c);
            if (doclist.getDoctorName() != null) {
                //Fragment fragment = null;
               // holder.tv_name.setText(data.getVisitplanList().get(position).getDoctorName());
                holder.tv_name.setText(doclist.getDoctorName());
                holder.brandDiv.setVisibility(View.GONE);
                holder.promoDiv.setVisibility(View.GONE);
                holder.sampleDiv.setVisibility(View.GONE);

                holder.cardView.setOnLongClickListener(v ->{
                    d.setContentView(R.layout.popup_delete);
                    d.getWindow().setLayout(Toolbar.LayoutParams.MATCH_PARENT, Toolbar.LayoutParams.WRAP_CONTENT);
                    //find the aspects
                    TextView yes, no;
                    yes = d.findViewById(R.id.yes);
                    no = d.findViewById(R.id.no);
                    d.show();

                    //if want to delete
                    yes.setOnClickListener(v1 -> {
                        dListener.deleteItemFromServer(position,Integer.parseInt(doclist.getDocTPDetailsId()));
                        d.dismiss();
                       // monthDate.getVisitplanList().remove(position);
                        notifyDataSetChanged();
                    });
                    //if don't want to delete
                    no.setOnClickListener(v1 -> {
                        d.dismiss();
                    });

                    return false;

                });
               // holder.tv_date.setText(String.valueOf(data.getDateV()));
               /* holder.cardView.setOnClickListener(v -> {

                    Intent in = new Intent(c, VisitPlanDetailsActivity.class);
                    in.putExtra("Month", data.getMonthName());
                    in.putExtra("Year", String.valueOf(data.getYearV()));
                    in.putExtra("Date", String.valueOf(data.getDateV()));
                    Gson gson=new Gson();
                    String doclist=gson.toJson(vpList);
                    String item=gson.toJson(monthList);

                    in.putExtra("docjson", doclist);
                    in.putExtra("itemjson", item);
                    c.startActivity(in);
                   *//* Fragment fragment = new VisitPlanListFragment();
                    if (fragment != null) {
                        fragmentManager = ((AppCompatActivity)aca).getSupportFragmentManager();
                        fragmentManager.beginTransaction().setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left).replace(R.id.fragmentContaineplanr, fragment).commit();
                    }*//*
                });*/


            }
            else {

            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }


    }

    @Override
    public int getItemCount() {
        if(monthDate.getVisitplanList()!=null)
        {
            return monthDate.getVisitplanList().size();
        }else {
            return 0;
        }

    }

    public class VPDH extends RecyclerView.ViewHolder {
        TextView tv_name, tv_code, tv_date,tv_brand,tv_gift,tv_sample;
        CardView cardView;
        LinearLayout brandDiv,promoDiv,sampleDiv;

        public VPDH(@NonNull View v) {
            super(v);

            tv_name = v.findViewById(R.id.tv_name);
            tv_code = v.findViewById(R.id.tv_code);
            tv_date = v.findViewById(R.id.tv_date);
            tv_brand = v.findViewById(R.id.tv_brand);
            tv_gift = v.findViewById(R.id.tv_gift);
            tv_sample = v.findViewById(R.id.tv_sample);
            cardView = v.findViewById(R.id.cardView);

            brandDiv = v.findViewById(R.id.brandDiv);
            promoDiv = v.findViewById(R.id.promoDiv);
            sampleDiv = v.findViewById(R.id.sampleDiv);
        }


    }
}
