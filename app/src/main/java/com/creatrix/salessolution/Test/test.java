//package com.creatrix.salessolution.RecyclerAdapter;
//
//import android.content.Context;
//import android.content.Intent;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.animation.AlphaAnimation;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.creatrix.salessolution.Activity.OrderStartActivity;
//import com.creatrix.salessolution.DepoStockModel.Customer;
//import com.creatrix.salessolution.DepoStockModel.OrderMaster;
//import com.creatrix.salessolution.R;
//import com.google.gson.Gson;
//
//import java.util.List;
//
//public class _orderList_Recycler_Adapter extends RecyclerView.Adapter<_orderList_Recycler_Adapter.BookViewHolder> {
//    private Context context;
//    private final static int FADE_DURATION = 500; //FADE_DURATION in milliseconds
//    private List<OrderMaster> aOrderlist;
//
//    public _orderList_Recycler_Adapter(List<OrderMaster> aOrderlist) {
//        this.aOrderlist = aOrderlist;
//    }
//
//    @Override
//    public _orderList_Recycler_Adapter.BookViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View itemView = LayoutInflater.from(parent.getContext())
//                .inflate(R.layout._recyclerview_ordermaster, parent, false);
//        context = parent.getContext();
//        return new _orderList_Recycler_Adapter.BookViewHolder(itemView);
//    }
//    @Override
//    public void onBindViewHolder(_orderList_Recycler_Adapter.BookViewHolder holder, int position) {
//
//        OrderMaster ad = aOrderlist.get(position);
//        holder.customerName.setText(aOrderlist.get(position).getCustomerName());
//        setFadeAnimation(holder.itemView);
//    }
//
//    @Override
//    public int getItemCount() {
//        return aOrderlist.size();
//    }
//
//    public class BookViewHolder extends RecyclerView.ViewHolder {
//        public TextView customerName;
//        public LinearLayout clickID;
//
//        public BookViewHolder(View view) {
//            super(view);
//            customerName = (TextView) view.findViewById(R.id.parentCustomerName);
//        }
//    }
//
//
//    private void setFadeAnimation(View view) {
//        AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
//        anim.setDuration(FADE_DURATION);
//        view.startAnimation(anim);
//    }
//
//
//}