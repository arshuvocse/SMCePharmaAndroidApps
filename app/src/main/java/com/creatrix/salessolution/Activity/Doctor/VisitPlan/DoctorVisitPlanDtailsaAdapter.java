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
import java.util.Collections;
import java.util.Comparator;
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
            // Sort list by Doctor Name before binding (safe but not efficient if called every time)
            Collections.sort(monthDate.getVisitplanList(), new Comparator<VisitplanModel>() {
                @Override
                public int compare(VisitplanModel o1, VisitplanModel o2) {
                    String name1 = o1.getDoctorName() != null ? o1.getDoctorName() : "";
                    String name2 = o2.getDoctorName() != null ? o2.getDoctorName() : "";
                    return name1.compareToIgnoreCase(name2);
                }
            });

            VisitplanModel doclist = monthDate.getVisitplanList().get(position);

            Dialog d = new Dialog(c);

            if (doclist.getDoctorName() != null) {
                holder.tv_name.setText(doclist.getDoctorName());
                holder.brandDiv.setVisibility(View.GONE);
                holder.promoDiv.setVisibility(View.GONE);
                holder.sampleDiv.setVisibility(View.GONE);

                holder.cardView.setOnLongClickListener(v -> {
                    d.setContentView(R.layout.popup_delete);
                    d.getWindow().setLayout(Toolbar.LayoutParams.MATCH_PARENT, Toolbar.LayoutParams.WRAP_CONTENT);

                    TextView yes, no;
                    yes = d.findViewById(R.id.yes);
                    no = d.findViewById(R.id.no);
                    d.show();

                    yes.setOnClickListener(v1 -> {
                        dListener.deleteItemFromServer(position, Integer.parseInt(doclist.getDocTPDetailsId()));
                        d.dismiss();
                        notifyDataSetChanged();
                    });

                    no.setOnClickListener(v1 -> d.dismiss());

                    return false;
                });

            } else {
                // Optionally handle null doctor name
                holder.tv_name.setText("No Name");
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
