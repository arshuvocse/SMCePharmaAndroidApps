package com.creatrix.salessolution.Activity.OrderProcess.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.creatrix.salessolution.Activity.OrderMainActivity;
import com.creatrix.salessolution.DBAdapter.DBCrudHelper;
import com.creatrix.salessolution.Interface.DeleteListeners;
import com.creatrix.salessolution.Interface.NotifyListener;
import com.creatrix.salessolution.Model.Customer;
import com.creatrix.salessolution.Model.OrderMaster;
import com.creatrix.salessolution.Model.Product;
import com.creatrix.salessolution.Presenter.PendingCounterPresenter;
import com.creatrix.salessolution.R;
import com.creatrix.salessolution.Services.Constants;
import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.util.List;


public class MultiOrderAdapter extends RecyclerView.Adapter<MultiOrderAdapter.dvh> {
    Context context;
    Activity activity;
    List<Product> mProductList;
    private final static int FADE_DURATION = 500;
    public MultiOrderAdapter(Context context, Activity activity, List<Product> mProductList) {
        this.context = context;
        this.activity = activity;
        this.mProductList = mProductList;
    }

    @NonNull
    @Override
    public dvh onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rv_multi_item, parent, false);
        return new dvh(itemView);
    }
    @Override
    public void onBindViewHolder(dvh holder, int position) {
        Product ad = mProductList.get(position);

        holder.product_name.setText(ad.getProductName());
        holder.product_code.setText(ad.getProductCode());
        holder.product_qty.setText(String.valueOf(ad.getQuantity()));
        holder.product_stock.setText(String.valueOf(ad.getPackSize()));
       // holder.checkbox.setText(ad.getDeliveryDate());
          //setFadeAnimation(holder.rvmultiitem);
    }

    @Override
    public int getItemCount() {
        return mProductList.size();
    }


    public static class dvh extends RecyclerView.ViewHolder {
        public TextView product_name,product_code,product_stock;
        public EditText product_qty;
        public CheckBox checkbox;
        public ConstraintLayout rvmultiitem;
        public dvh(View view) {
            super(view);
            product_name = (TextView) view.findViewById(R.id.tv_name);
            product_code = (TextView) view.findViewById(R.id.tv_code);
            product_qty = (EditText) view.findViewById(R.id.qty);
            product_stock = (TextView) view.findViewById(R.id.stock);
            checkbox = (CheckBox) view.findViewById(R.id.checkbox);
            rvmultiitem = (ConstraintLayout) view.findViewById(R.id.rvmultiitem);
        }
    }

    private void setFadeAnimation(View view) {
        AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(FADE_DURATION);
        view.startAnimation(anim);
    }


}
