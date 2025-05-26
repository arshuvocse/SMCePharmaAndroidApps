package com.creatrix.salessolution.RecyclerAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.creatrix.salessolution.Model.Doctor.SpecialDay;
import com.creatrix.salessolution.R;

import java.util.List;

public class _specialday_ListAdapter extends RecyclerView.Adapter<_specialday_ListAdapter.BookViewHolder> {
    Context context;
    private final static int FADE_DURATION = 500; //FADE_DURATION in milliseconds
    private final List<SpecialDay> aSpecialDayList;
    public _specialday_ListAdapter(Context context, List<SpecialDay> aSpecialDayList) {
        this.context = context;
        this.aSpecialDayList = aSpecialDayList;
    }

    @NonNull
    @Override
    public _specialday_ListAdapter.BookViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout._rv_chamber_item, parent, false);
       // context = parent.getContext();
        return new BookViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(_specialday_ListAdapter.BookViewHolder holder, int position) {
        SpecialDay ad = aSpecialDayList.get(position);
        holder.tv_spdayTypeName.setText(ad.getSpecialDay());
        holder.date.setText(ad.getSpeciaDateStr());
        holder.del.setOnClickListener(v -> {
            aSpecialDayList.remove(holder.getAdapterPosition());
            notifyItemRemoved(holder.getAdapterPosition());
            notifyDataSetChanged();
        });

        setFadeAnimation(holder.itemView);
    }

    @Override
    public int getItemCount() {
        return aSpecialDayList.size();
    }


    public static class BookViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_spdayTypeName, date;
        public ImageView del;


        public BookViewHolder(View view) {
            super(view);
            tv_spdayTypeName = (TextView) view.findViewById(R.id.tv_chamberTypeName);
            date = (TextView) view.findViewById(R.id.tv_chamberName);
            del = (ImageView) view.findViewById(R.id.iv_del);
        }
    }
    private void setFadeAnimation(View view) {
        AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(FADE_DURATION);
        view.startAnimation(anim);
    }


}