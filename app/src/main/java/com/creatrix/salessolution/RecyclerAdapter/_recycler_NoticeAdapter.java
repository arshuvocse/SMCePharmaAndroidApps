package com.creatrix.salessolution.RecyclerAdapter;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.creatrix.salessolution.Activity.Attendance.TeamAttenViewActivity;
import com.creatrix.salessolution.Activity.Notice.NoticeViewActivity;
import com.creatrix.salessolution.Model.Notice;
import com.creatrix.salessolution.R;
import com.creatrix.salessolution.Services.Constants;
import com.creatrix.salessolution.UtilityHelper.SnackBarManagement;
import com.google.gson.Gson;
import com.skyhope.showmoretextview.ShowMoreTextView;

import java.util.List;

import ozaydin.serkan.com.image_zoom_view.ImageViewZoom;

public class _recycler_NoticeAdapter extends RecyclerView.Adapter<_recycler_NoticeAdapter.BookViewHolder> {
    private Activity activity;
    View  v;
    private final static int FADE_DURATION = 500; //FADE_DURATION in milliseconds
    private List<Notice> aList;
    int lastposition = -1;
    public _recycler_NoticeAdapter(Activity activity,List<Notice> nList) {
        this.activity = activity;
        this.aList = nList;
    }
    public static class BookViewHolder extends RecyclerView.ViewHolder {
        public TextView noticeTitle, noticeDesc, notice_seen;
        public TextView fromDate;
        public TextView toDate;
        public ImageViewZoom image;
        public ShowMoreTextView more;
        // public LinearLayout noticeClick;
        public CardView noticeClick;

        public BookViewHolder(View view) {
            super(view);
            noticeTitle = (TextView) view.findViewById(R.id.noticeTitle);
            //noticeDesc = (TextView) view.findViewById(R.id.noticeDesc);
            more = (ShowMoreTextView) view.findViewById(R.id.text_view_show_more);
            image = (ImageViewZoom) view.findViewById(R.id.image);
            //notice_seen = (TextView) view.findViewById(R.id.tv_seen);
            /*fromDate = (TextView) view.findViewById(R.id.fromDate);
            toDate = (TextView) view.findViewById(R.id.toDate);*/
            noticeClick = (CardView) view.findViewById(R.id.noticeClick);
        }
    }

    @Override
    public _recycler_NoticeAdapter.BookViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout._recyclerview_notice, parent, false);
        //activity = parent.getContext();
        return new BookViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(@NonNull _recycler_NoticeAdapter.BookViewHolder holder, int position) {
        if (aList != null) {
            Notice notice = aList.get(position);
            try {
                holder.noticeTitle.setText(aList.get(position).getNoticeTitle());
                holder.more.setText(aList.get(position).getAnnouncement());
                holder.more.setShowingLine(3);
                holder.more.addShowMoreText("Continue");
            } catch (Exception e) {
                e.printStackTrace();
                System.out.print(e.getMessage());
                Log.d(TAG, "problem: "+e.getLocalizedMessage());
                Log.d(TAG, "problem2: "+e.getMessage());
            }
            // holder.noticeDesc.setText(aList.get(position).getAnnouncement());
            //holder.more.setShowingChar(250);
            //holder.fromDate.setText(aList.get(position).getFromDate());
             //holder.toDate.setText(aList.get(position).getToDate());
            // Notice click
            holder.noticeClick.setOnClickListener(v -> {
                Intent notiveview = new Intent(activity, NoticeViewActivity.class);
                //notiveview.putExtra("From", "Adapter");
                /* Gson gson = new Gson();
                String myJson = gson.toJson(notice);
                notiveview.putExtra("myjson", myJson);*/
                //notiveview.putExtra("noticeId", notice.getNoticeId());
                notiveview.putExtra("noId",String.valueOf(notice.getNoticeId()));
                activity.startActivity(notiveview);
                activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            });
          /*  if (notice.getAppCheck()==false) {
                if(Constants.SeenNotice.equals("true"))
                {
                    holder.notice_seen.setVisibility(View.VISIBLE);
                }
                else {
                    holder.notice_seen.setVisibility(View.GONE);
                }

            } else {
                holder.notice_seen.setVisibility(View.VISIBLE);
            }*/
        }
        else {
            SnackBarManagement._warning_CustomMessage(v,"No Notice Available");
        }
        Animation animation = AnimationUtils.loadAnimation(activity, R.anim.slide_in_left);
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