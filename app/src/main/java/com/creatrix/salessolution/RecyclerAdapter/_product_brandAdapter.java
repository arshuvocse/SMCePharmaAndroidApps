package com.creatrix.salessolution.RecyclerAdapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.creatrix.salessolution.Model.Doctor.DoctorBrand;
import com.creatrix.salessolution.Model.ProductSample;
import com.creatrix.salessolution.R;

import java.util.List;

public class _product_brandAdapter extends RecyclerView.Adapter<_product_brandAdapter.BookViewHolder> {
    private Activity activity;
    private List<DoctorBrand> aList;
    public _product_brandAdapter(Activity activity,List<DoctorBrand> nList) {
        this.activity = activity;
        this.aList = nList;
    }
    public class BookViewHolder extends RecyclerView.ViewHolder {
        public TextView productName;
        AppCompatButton btn_minus,btn_plus;
        EditText et_quantity;
        LinearLayout master;

        public BookViewHolder(View view) {
            super(view);
            master = (LinearLayout) view.findViewById(R.id.master);
            productName = (TextView) view.findViewById(R.id.productName);
            btn_minus = (AppCompatButton) view.findViewById(R.id.btn_minus);
            btn_plus = (AppCompatButton) view.findViewById(R.id.btn_plus);
            et_quantity = (EditText) view.findViewById(R.id.et_quantity);

        }
    }

    @Override
    public BookViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.zrv_only_productname, parent, false);
        //context = parent.getContext();
        return new BookViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(BookViewHolder holder, int position) {
        holder.productName.setText(aList.get(position).getBrandName());
        holder.btn_minus.setVisibility(View.GONE);
        holder.btn_plus.setVisibility(View.GONE);
        holder.et_quantity.setVisibility(View.GONE);

    }

    @Override
    public int getItemCount() {
        return aList.size();
    }
}
