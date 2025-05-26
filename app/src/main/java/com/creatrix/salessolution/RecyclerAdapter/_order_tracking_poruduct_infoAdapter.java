package com.creatrix.salessolution.RecyclerAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.creatrix.salessolution.Model.Product;
import com.creatrix.salessolution.R;

import java.util.List;

public class _order_tracking_poruduct_infoAdapter extends RecyclerView.Adapter<_order_tracking_poruduct_infoAdapter.BookViewHolder> {
    private Context context;
    private final static int FADE_DURATION = 500; //FADE_DURATION in milliseconds
    private List<Product> aList;


    public _order_tracking_poruduct_infoAdapter(List<Product> aList) {
        this.aList = aList;
    }

    @Override
    public _order_tracking_poruduct_infoAdapter.BookViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout._recyclerview_order_details_product, parent, false);
        context = parent.getContext();
        return new _order_tracking_poruduct_infoAdapter.BookViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(_order_tracking_poruduct_infoAdapter.BookViewHolder holder, int position) {



        holder.product_name_txt.setText(aList.get(position).getProductName());
        holder.quantity_txt.setText(String.valueOf(aList.get(position).getQuantity()));
        holder.price_txt.setText(String.valueOf(aList.get(position).getUnitPrice()));
        holder.total_vat_txt.setText(String.valueOf(aList.get(position).getTotalVatAmount()));
        holder.total_amt_txt.setText(String.valueOf(aList.get(position).getNetAmount()));
    }

    @Override
    public int getItemCount() {
        return aList.size();
    }


    public class BookViewHolder extends RecyclerView.ViewHolder {
        public TextView product_name_txt;
        public TextView quantity_txt;
        public TextView price_txt;
        public TextView total_vat_txt;
        public TextView total_amt_txt;


        public BookViewHolder(View view) {
            super(view);


            product_name_txt = (TextView) view.findViewById(R.id.product_name_txt);
            quantity_txt = (TextView) view.findViewById(R.id.quantity_txt);
            price_txt = (TextView) view.findViewById(R.id.price_txt);
            total_vat_txt = (TextView) view.findViewById(R.id.total_vat_txt);
            total_amt_txt = (TextView) view.findViewById(R.id.total_amt_txt);



        }
    }


    private void setFadeAnimation(View view) {
        AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(FADE_DURATION);
        view.startAnimation(anim);
    }


}