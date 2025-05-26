package com.creatrix.salessolution.RecyclerAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.creatrix.salessolution.Model.Customer;
import com.creatrix.salessolution.Model.Product;
import com.creatrix.salessolution.R;

import java.util.ArrayList;
import java.util.List;

public class _productView_RecyclerAdapter extends RecyclerView.Adapter<_productView_RecyclerAdapter.BookViewHolder> {
    private Context context;
    private final static int FADE_DURATION = 700; //FADE_DURATION in milliseconds
    private List<Product> aList;


    public _productView_RecyclerAdapter(List<Product> aList) {
        this.aList = aList;
    }

    @Override
    public _productView_RecyclerAdapter.BookViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.zrv_productview, parent, false);
        context = parent.getContext();
        return new _productView_RecyclerAdapter.BookViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(_productView_RecyclerAdapter.BookViewHolder holder, int position) {


        holder.nameTxt.setText(aList.get(position).getProductName());
        holder.codeTxt.setText(aList.get(position).getProductCode());
        if(aList.get(position).getUnitPrice()!=null){
            holder.priceTxt.setText(aList.get(position).getUnitPrice().toString());

        }
//        holder.vatPercenTxt.setText(aList.get(position).getVatPercentage().toString());

        Animation animation = AnimationUtils.loadAnimation(context,R.anim.slide_intop);
        animation.setStartOffset(30 * position);//Provide delay here
        holder.itemView.startAnimation(animation);
    }

    public void filterList(List<Product> aList){
        this.aList = aList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return aList.size();
    }

    public class BookViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTxt;
        public TextView codeTxt;
        public TextView priceTxt;
        public TextView vatPercenTxt;

        public BookViewHolder(View view) {
            super(view);
            nameTxt = view.findViewById(R.id.nameTxt);
            codeTxt = view.findViewById(R.id.codeTxt);
            priceTxt = view.findViewById(R.id.priceTxt);
//            vatPercenTxt = (TextView) view.findViewById(R.id.vatPercenTxt);
        }
    }


    private void setFadeAnimation(View view) {
        AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(FADE_DURATION);
        view.startAnimation(anim);
    }


}
