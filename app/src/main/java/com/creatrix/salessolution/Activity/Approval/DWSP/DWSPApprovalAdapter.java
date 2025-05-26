package com.creatrix.salessolution.Activity.Approval.DWSP;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.creatrix.salessolution.Activity.Approval.DWSP.Model.ADWSPDate;
import com.creatrix.salessolution.Activity.Approval.TourPlan.Model.ATourPlanDate;
import com.creatrix.salessolution.Activity.Approval.TourPlan.Model.ATourPlanDtlsDAO;
import com.creatrix.salessolution.Activity.Approval.TourPlan.Model.ATourPlanMaster;
import com.creatrix.salessolution.Activity.Approval.TourPlan.Model.TourPlanApprovalData;
import com.creatrix.salessolution.Interface.Rcv_TourPlanListener;
import com.creatrix.salessolution.R;
import com.creatrix.salessolution.RecyclerAdapter._TourInfoData_List;

import java.util.List;

public class DWSPApprovalAdapter extends RecyclerView.Adapter<DWSPApprovalAdapter.BookViewHolder> /*implements ITourplan.View */ {
    private final static int FADE_DURATION = 500; //FADE_DURATION in milliseconds
    Rcv_TourPlanListener mListener;
    private TourPlanApprovalData tpdata;
    private ATourPlanMaster tpM;
    private ATourPlanDate tpdate;
    private ATourPlanDtlsDAO tpdtl;
    List<ADWSPDate> dwspListdata;
    Activity activity;


    public DWSPApprovalAdapter(List<ADWSPDate> dwspListdata, Activity activity) {
        this.dwspListdata = dwspListdata;
        this.activity = activity;
    }


    @Override
    public BookViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rv_dwsp_item, parent, false);
        return new BookViewHolder(itemView);
    }

    public static class BookViewHolder extends RecyclerView.ViewHolder {
        public TextView et_gamount, et_camount, et_famount, tv_day, tv_date;
        public ImageView add;


        public BookViewHolder(View view) {
            super(view);
            tv_day = (TextView) view.findViewById(R.id.tv_day);
            tv_date = (TextView) view.findViewById(R.id.tv_date);

            et_famount = (TextView) view.findViewById(R.id.et_famount);
            et_camount = (TextView) view.findViewById(R.id.et_camount);
            et_gamount = (TextView) view.findViewById(R.id.et_gamount);
            add = (ImageView) view.findViewById(R.id.add);

        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(BookViewHolder holder, int position) {
        ADWSPDate data = dwspListdata.get(position);
       // holder.tv_day.setText(data.getDayValue() + "\n" + data.getDayName());
        holder.tv_day.setText(data.getDayName());
        holder.tv_date.setText(data.getDayValue());
        holder.et_camount.setText(data.getCampaignAmount());
        holder.et_famount.setText(data.getfCBAmount());
        holder.et_gamount.setText(data.getGeneralAmount());

        holder.add.setVisibility(View.GONE);
        //nested recycler

        setFadeAnimation(holder.itemView);
    }

    @Override
    public int getItemCount() {
        if (dwspListdata.size() > 0) {
            return dwspListdata.size();
        } else return 0;

    }

    private void setFadeAnimation(View view) {
        AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(FADE_DURATION);
        view.startAnimation(anim);
    }

}


