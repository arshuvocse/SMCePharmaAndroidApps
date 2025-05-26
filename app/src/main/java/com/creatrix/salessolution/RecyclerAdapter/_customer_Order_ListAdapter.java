package com.creatrix.salessolution.RecyclerAdapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.creatrix.salessolution.Activity.Customer.CustomerOrderTrackingActivity;
import com.creatrix.salessolution.Model.OrderViewModel;
import com.creatrix.salessolution.R;
import com.google.gson.Gson;

import java.util.List;

public class _customer_Order_ListAdapter extends RecyclerView.Adapter<_customer_Order_ListAdapter.BookViewHolder> {
    private Context context;
    //FADE_DURATION in milliseconds
    private final List<OrderViewModel> aCustomerList;


    public _customer_Order_ListAdapter(List<OrderViewModel> customerList) {
        this.aCustomerList = customerList;
    }

    @NonNull
    @Override
    public _customer_Order_ListAdapter.BookViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout._recyclerview_customer_orderlist, parent, false);
        context = parent.getContext();
        return new BookViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(_customer_Order_ListAdapter.BookViewHolder holder, int position) {
        holder.customername.setText(aCustomerList.get(position).getCustomerName());
        holder.orderstatus.setText(aCustomerList.get(position).getOrderStatus());
        holder.orderid.setText(aCustomerList.get(position).getOrderCode());
        holder.grosstxt.setText(aCustomerList.get(position).getGrossValue());
        holder.mioname.setText(aCustomerList.get(position).getMIOName());

        holder.clickID.setOnClickListener(v -> {
            OrderViewModel ord;
            ord = aCustomerList.get(holder.getAdapterPosition());
            Intent intent = new Intent(context, CustomerOrderTrackingActivity.class);
            Gson gson = new Gson();
            String myJson = gson.toJson(ord);
            intent.putExtra("myjson", myJson);
            context.startActivity(intent);


        });



        //  setFadeAnimation(holder.itemView);
    }

    @Override
    public int getItemCount() {
        return aCustomerList.size();
    }

    public static class BookViewHolder extends RecyclerView.ViewHolder {
        public TextView orderid;
        public TextView orderstatus;
        public TextView customername;
        public TextView grosstxt;
        public TextView mioname;
        public LinearLayout clickID;

        public BookViewHolder(View view) {
            super(view);


            orderid = (TextView) view.findViewById(R.id.orderid);
            orderstatus = (TextView) view.findViewById(R.id.orderstatus);
            customername = (TextView) view.findViewById(R.id.customername);
            grosstxt = (TextView) view.findViewById(R.id.grosstxt);
            mioname = (TextView) view.findViewById(R.id.mioname);
            clickID = (LinearLayout) view.findViewById(R.id.clickID);


        }
    }


}