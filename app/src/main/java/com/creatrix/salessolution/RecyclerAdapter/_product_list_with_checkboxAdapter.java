package com.creatrix.salessolution.RecyclerAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.creatrix.salessolution.Model.Product;
import com.creatrix.salessolution.R;

import java.util.List;

public class _product_list_with_checkboxAdapter extends RecyclerView.Adapter<_product_list_with_checkboxAdapter.BookViewHolder> {
    private Context context;
    private final static int FADE_DURATION = 500; //FADE_DURATION in milliseconds
    private List<Product> aCustomerList;


    public _product_list_with_checkboxAdapter(List<Product> customerList) {
        this.aCustomerList = customerList;
    }

    @Override
    public _product_list_with_checkboxAdapter.BookViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout._recyclerview_productlst, parent, false);
        context = parent.getContext();
        return new _product_list_with_checkboxAdapter.BookViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(_product_list_with_checkboxAdapter.BookViewHolder holder, int position) {


        Product ad = aCustomerList.get(position);

        holder.productName.setText(aCustomerList.get(position).getProductName());

        //  setFadeAnimation(holder.itemView);
    }

    @Override
    public int getItemCount() {
        return aCustomerList.size();
    }







    public class BookViewHolder extends RecyclerView.ViewHolder {
        public TextView productName;
        public CheckBox checkProduct;

        public BookViewHolder(View view) {
            super(view);


            productName = (TextView) view.findViewById(R.id.productName);
            checkProduct = (CheckBox) view.findViewById(R.id.checkProduct);


        }
    }


    private void setFadeAnimation(View view) {
        AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(FADE_DURATION);
        view.startAnimation(anim);
    }


}