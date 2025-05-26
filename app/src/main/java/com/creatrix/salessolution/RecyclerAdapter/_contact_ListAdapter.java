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
import com.creatrix.salessolution.Model.Doctor.DoctorContact;
import com.creatrix.salessolution.R;
import java.util.List;

public class _contact_ListAdapter extends RecyclerView.Adapter<_contact_ListAdapter.BookViewHolder> {
    public Context context;
    private final static int FADE_DURATION = 500; //FADE_DURATION in milliseconds
    private final List<DoctorContact> aContactList;


    public _contact_ListAdapter(Context context, List<DoctorContact> aContactList) {
        this.context = context;
        this.aContactList = aContactList;
    }

    @NonNull
    @Override
    public _contact_ListAdapter.BookViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout._rv_chamber_item, parent, false);
       // context = parent.getContext();
        return new BookViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(_contact_ListAdapter.BookViewHolder holder, int position) {
        DoctorContact ad = aContactList.get(position);
        holder.tv_contactTypeName.setText(ad.getContactType());
        holder.contactName.setText(ad.getContact());
        holder.del.setOnClickListener(v -> {
            aContactList.remove(holder.getAdapterPosition());
            //notifyDataSetChanged();
            notifyItemChanged(holder.getAdapterPosition());

        });

        setFadeAnimation(holder.itemView);
    }

    @Override
    public int getItemCount() {
        return aContactList.size();
    }


    public static class BookViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_contactTypeName, contactName;
        public ImageView del;


        public BookViewHolder(View view) {
            super(view);
            tv_contactTypeName = (TextView) view.findViewById(R.id.tv_chamberTypeName);
            contactName = (TextView) view.findViewById(R.id.tv_chamberName);
            del = (ImageView) view.findViewById(R.id.iv_del);
        }
    }


    private void setFadeAnimation(View view) {
        AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(FADE_DURATION);
        view.startAnimation(anim);
    }


}