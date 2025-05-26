package com.creatrix.salessolution.RecyclerAdapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.creatrix.salessolution.Activity.MioOrderTrackingActivity;
import com.creatrix.salessolution.Model.NotificationViewModel;
import com.creatrix.salessolution.R;

import java.util.List;

public class _notification_List_recyclerAdapter extends RecyclerView.Adapter<_notification_List_recyclerAdapter.BookViewHolder> {
    private Context context;
    private final List<NotificationViewModel> aList;
    public _notification_List_recyclerAdapter(List<NotificationViewModel> nList) {
        this.aList = nList;
    }

    public static class BookViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout nmasterId;
        public TextView notificationTxt;
        public TextView fromDate;
        public TextView toDate;
        public LinearLayout clickID;

        public BookViewHolder(View view) {
            super(view);


            nmasterId =view.findViewById(R.id.nmasterId);
            notificationTxt = (TextView) view.findViewById(R.id.notificationTxt);


        }
    }

    @NonNull
    @Override
    public _notification_List_recyclerAdapter.BookViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.zrv_notificationlist, parent, false);
        context = parent.getContext();
        return new BookViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(_notification_List_recyclerAdapter.BookViewHolder holder, int position) {

        holder.notificationTxt.setText(aList.get(position).getNotificationRestText());

        if(aList.get(position).isRead()){
            holder.nmasterId.setBackgroundColor(Color.parseColor("#ffffff"));
        }

        holder.nmasterId.setOnClickListener(v -> {
            NotificationViewModel ord;
            ord = aList.get(holder.getAdapterPosition());
            Intent intent = new Intent(context, MioOrderTrackingActivity.class);
            int orderId = ord.getPrimaryId();
            boolean isReda = ord.isRead();
            int readVal =1;
            if(isReda==false){
                readVal = 0;
            }
            intent.putExtra("myOrderId", orderId);
            intent.putExtra("from", "Notification");
            intent.putExtra("nId", ord.getNotificationId());
            intent.putExtra("readVal", readVal);

            context.startActivity(intent);

        });


        Animation animation = AnimationUtils.loadAnimation(context,R.anim.slide_in_left);
        animation.setStartOffset(30 * position);//Provide delay here
        holder.itemView.startAnimation(animation);
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

        view.startAnimation(animation);
    }


}