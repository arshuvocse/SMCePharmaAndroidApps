package com.creatrix.salessolution.RecyclerAdapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.creatrix.salessolution.Model.UserByRole;
import com.creatrix.salessolution.R;

import java.util.List;

public class _VisitedWithRecylcer extends RecyclerView.Adapter<_VisitedWithRecylcer.BookViewHolder> {
    private Activity activity;
    private List<UserByRole> aList;
    DltListener dltListener;

    public _VisitedWithRecylcer(Activity activity,List<UserByRole> nList,DltListener dltListener) {
        this.activity = activity;
        this.aList = nList;
        this.dltListener = dltListener;

    }

    public class BookViewHolder extends RecyclerView.ViewHolder {
        public TextView empName;
        public LinearLayout clickLay;

        public BookViewHolder(View view) {
            super(view);
            empName = (TextView) view.findViewById(R.id.empName);
            clickLay = view.findViewById(R.id.clickLay);
            clickLay.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    dltListener.onDltClick(getAdapterPosition());
                    //notifyItemRemoved(getAdapterPosition());
                    return true;
                }
            });

        }
    }

    @Override
    public _VisitedWithRecylcer.BookViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.zrv_empname, parent, false);
        //context = parent.getContext();
        return new _VisitedWithRecylcer.BookViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(_VisitedWithRecylcer.BookViewHolder holder, int position) {
        holder.empName.setText(aList.get(position).getEmpName());

    }

    @Override
    public int getItemCount() {
        return aList.size();
    }

    public interface DltListener{
        boolean onDltClick(int position);
    }
}
