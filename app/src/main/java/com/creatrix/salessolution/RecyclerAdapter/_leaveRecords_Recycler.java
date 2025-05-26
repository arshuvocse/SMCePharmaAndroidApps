package com.creatrix.salessolution.RecyclerAdapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.creatrix.salessolution.Model.LeaveVM;
import com.creatrix.salessolution.Model.MilageClaimReport;
import com.creatrix.salessolution.R;

import java.util.List;

public class _leaveRecords_Recycler extends RecyclerView.Adapter<_leaveRecords_Recycler.BookViewHolder> {
    private Context context;
    private List<LeaveVM> aList;
    int lastposition = -1;

    public _leaveRecords_Recycler(List<LeaveVM> aList) {
        this.aList = aList;
    }


    public class BookViewHolder extends RecyclerView.ViewHolder {
        public TextView leaveName,createdat,leavetype,startdate,enddate,appStatus;
        private CardView masterLayout;
        public BookViewHolder(View view) {
            super(view);
            masterLayout = view.findViewById(R.id.masterLayout);
            leaveName = view.findViewById(R.id.leaveName);
            createdat = view.findViewById(R.id.createdat);
            leavetype = view.findViewById(R.id.leavetype);
            startdate = view.findViewById(R.id.startdate);
            enddate = view.findViewById(R.id.enddate);
            appStatus = view.findViewById(R.id.leavestatus);
            setFadeAnimation(masterLayout);
        }
    }

    @Override
    public _leaveRecords_Recycler.BookViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.zrv_leaverecords, parent, false);
        context = parent.getContext();
        return new _leaveRecords_Recycler.BookViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(_leaveRecords_Recycler.BookViewHolder holder, int position) {
        if(aList!=null)
        {
            LeaveVM lv=  aList.get(position);
            try {
                holder.leaveName.setText(lv.getUserName());
                holder.createdat.setText(lv.getCreatedAt());
                holder.leavetype.setText(lv.getLeaveTypeName());
                holder.startdate.setText(lv.getLeaveFromDate());
                holder.enddate.setText(lv.getLeaveToDate());
                holder.appStatus.setText(lv.getApprovalStatus());
                try {
                    if(lv.getApprovalStatus().equals("0"))
                    {
                        holder.appStatus.setText("Pending");
                        holder.appStatus.setTextColor(Color.parseColor("#ff7400"));
                    }else if(lv.getApprovalStatus().equals("1")){
                        holder.appStatus.setText("Verified");
                        holder.appStatus.setTextColor(Color.parseColor("#4169e1"));
                    }
                    else if(lv.getApprovalStatus().equals("2")){
                        holder.appStatus.setText("Approved");
                        holder.appStatus.setTextColor(Color.parseColor("#00b248"));
                    }
                    else if(lv.getApprovalStatus().equals("3")){
                        holder.appStatus.setText("Rejected");
                        holder.appStatus.setTextColor(Color.parseColor("#C12222"));
                    }
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }




    }

    @Override
    public int getItemCount() {
        return aList.size();
    }
    private void setFadeAnimation(View view) {
        AnimationSet set = new AnimationSet(true);
        Animation animation = new AlphaAnimation(0.0f, 1.0f);
        animation.setDuration(500);
        view.startAnimation(animation);

        animation = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, -1.0f, Animation.RELATIVE_TO_SELF, 0.0f
        );
        animation.setDuration(100);

        //   view.startAnimation(set);
        view.startAnimation(animation);
    }

}
