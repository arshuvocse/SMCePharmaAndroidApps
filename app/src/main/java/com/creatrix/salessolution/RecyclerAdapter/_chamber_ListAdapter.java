package com.creatrix.salessolution.RecyclerAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.creatrix.salessolution.Model.Doctor.DoctorChamberTypeVM;
import com.creatrix.salessolution.R;


import java.util.List;

public class _chamber_ListAdapter extends RecyclerView.Adapter<_chamber_ListAdapter.BookViewHolder> {
    private Context context;
    private final static int FADE_DURATION = 500; //FADE_DURATION in milliseconds
    //private List<DoctorChamberName> aChamberList;
    private List<DoctorChamberTypeVM> aChamberList;


    public _chamber_ListAdapter(Context context, List<DoctorChamberTypeVM> aChamberList) {
        this.context = context;
        this.aChamberList = aChamberList;
    }

    @Override
    public _chamber_ListAdapter.BookViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout._rv_chamber_item, parent, false);
        context = parent.getContext();
        return new _chamber_ListAdapter.BookViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(_chamber_ListAdapter.BookViewHolder holder, int position) {


        DoctorChamberTypeVM ad = aChamberList.get(position);
        holder.tv_chamberTypeName.setText(ad.getChamberTypeName());
        holder.chamberName.setText(ad.getChamberName());
        holder.del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aChamberList.remove(holder.getAdapterPosition());
                notifyDataSetChanged();
            }
        });

        setFadeAnimation(holder.itemView);
    }

    @Override
    public int getItemCount() {
        return aChamberList.size();
    }


    public class BookViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_chamberTypeName, chamberName;
        public ImageView del;


        public BookViewHolder(View view) {
            super(view);
            tv_chamberTypeName = (TextView) view.findViewById(R.id.tv_chamberTypeName);
            chamberName = (TextView) view.findViewById(R.id.tv_chamberName);
            del = (ImageView) view.findViewById(R.id.iv_del);
        }
    }


    private void setFadeAnimation(View view) {
        AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(FADE_DURATION);
        view.startAnimation(anim);
    }


}