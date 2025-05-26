package com.creatrix.salessolution.Activity.DWSP;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.creatrix.salessolution.Activity.DWSP.Model.DWSPDailyModel;
import com.creatrix.salessolution.Activity.DWSP.Model.DWSPListener;
import com.creatrix.salessolution.Model.MonthDate;
import com.creatrix.salessolution.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DWSPAdapter extends RecyclerView.Adapter<DWSPAdapter.DVPH> {
    Activity activity;
    private final static int FADE_DURATION = 500; //FADE_DURATION in milliseconds
    private List<MonthDate> monthList;
    DWSPListener dwspListener;
    boolean is_Entry;
    int submit;
    private List<DWSPDailyModel> dwspList;// = new ArrayList<>();
    public DWSPAdapter(Activity activity, List<MonthDate> monthList,DWSPListener dwspListener/*, boolean is_Entry*/) {
        this.activity = activity;
        this.monthList = monthList;
        this.dwspListener = dwspListener;
    }

    @NonNull
    @Override
    public DVPH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rv_dwsp_item, parent, false);
        return new DVPH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull DVPH holder, int position) {
        MonthDate data = monthList.get(position);
        dwspList = data.getDwspList();
        if (data != null) {
            holder.tv_day.setText(data.getDateName());
            holder.tv_date.setText(String.valueOf(data.getDateV()));
            if (data.getDwspList() != null) {
                if (dwspList != null) {
                    if (dwspList.size() > 0) {
                        holder.llall.setVisibility(View.VISIBLE);
                        holder.add.setVisibility(View.VISIBLE);
                    } else {
                        holder.llall.setVisibility(View.INVISIBLE);
                        holder.add.setVisibility(View.VISIBLE);
                    }
                    String fcb = "";
                    String campaign = "";
                    String general = "";
                    List<DWSPDailyModel> datas = dwspList;
                    for (int i = 0; i < datas.size(); i++) {
                        fcb = String.valueOf(datas.get(i).getFCBAmount());
                        campaign = String.valueOf(datas.get(i).getCampaignAmount());
                        general = String.valueOf(datas.get(i).getGeneralAmount());
                    }
                    holder.tv_famount.setText(fcb);
                    holder.tv_camount.setText(campaign);
                    holder.tv_gamount.setText(general);
                  /*  if(fcb.equals("")&&campaign.equals("")&&general.equals("")){
                        holder.tv_famount.setVisibility(View.GONE);
                        holder.tv_camount.setVisibility(View.GONE);
                        holder.tv_gamount.setVisibility(View.GONE);
                    }else {

                    }*/

                }
            } else {
                holder.llall.setVisibility(View.INVISIBLE);
                holder.add.setVisibility(View.VISIBLE);
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String td=new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
            Date Todaydate = null;
            Date Entrydate = null;
            try {
                Todaydate = sdf.parse(td);
                Entrydate = sdf.parse(data.getDateValue());

            } catch (ParseException e) {
                e.printStackTrace();
            }
            if(Todaydate.before(Entrydate)||Todaydate.equals(Entrydate))
            {
                holder.add.setEnabled(true);
                holder.add.setOnClickListener(v -> {
                    dwspListener.dwspAdd(data.getMonthV(),data.getYearV(),data.getDateValue(),true,position,holder.tv_famount.getText().toString(),holder.tv_camount.getText().toString(),holder.tv_gamount.getText().toString());
                    //dwspListener.deleteItem(position);
                });
            }else {
                holder.add.setEnabled(false);
                holder.add.setVisibility(View.GONE);
            }

        }

    }

    @Override
    public int getItemCount() {
        return monthList.size();
    }
    public class DVPH extends RecyclerView.ViewHolder {
        TextView tv_day, tv_date;
        TextView tv_famount, tv_camount, tv_gamount;
        ImageView add;
        LinearLayout llall;

        public DVPH(@NonNull View v) {
            super(v);
            tv_day = v.findViewById(R.id.tv_day);
            tv_date = v.findViewById(R.id.tv_date);
            tv_famount = v.findViewById(R.id.et_famount);
            tv_camount = v.findViewById(R.id.et_camount);
            tv_gamount = v.findViewById(R.id.et_gamount);
            add = v.findViewById(R.id.add);
            llall = v.findViewById(R.id.llall);
        }


    }
}
