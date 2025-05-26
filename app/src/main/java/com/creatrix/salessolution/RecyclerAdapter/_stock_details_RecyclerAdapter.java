package com.creatrix.salessolution.RecyclerAdapter;

import android.content.Context;
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

import androidx.recyclerview.widget.RecyclerView;

import com.creatrix.salessolution.Model.Notice;
import com.creatrix.salessolution.Model.Rp_StockViewModel;
import com.creatrix.salessolution.R;

import java.util.List;

public class _stock_details_RecyclerAdapter extends RecyclerView.Adapter<_stock_details_RecyclerAdapter.BookViewHolder> {
    private Context context;
    private final static int FADE_DURATION = 500; //FADE_DURATION in milliseconds
    private List<Rp_StockViewModel> aList;
    int lastposition = -1;

    public _stock_details_RecyclerAdapter(List<Rp_StockViewModel> nList) {
        this.aList = nList;
    }


    public class BookViewHolder extends RecyclerView.ViewHolder {
        public TextView productName;
        public TextView productQuantity;
        public BookViewHolder(View view) {
            super(view);
            productName = (TextView) view.findViewById(R.id.productName);
            productQuantity = (TextView) view.findViewById(R.id.productQuantity);
        }
    }

    @Override
    public _stock_details_RecyclerAdapter.BookViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.zrv_stock_list_recyclerview, parent, false);
        context = parent.getContext();
        return new _stock_details_RecyclerAdapter.BookViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(_stock_details_RecyclerAdapter.BookViewHolder holder, int position) {


        holder.productName.setText(aList.get(position).getProductName());
        holder.productQuantity.setText(aList.get(position).getStockQty());
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.slide_in_left);
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