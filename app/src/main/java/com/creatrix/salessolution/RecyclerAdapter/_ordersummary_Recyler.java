package com.creatrix.salessolution.RecyclerAdapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.creatrix.salessolution.Activity.OrderProcess.Model.CampaignCalModel;
import com.creatrix.salessolution.Interface.RecyclerViewActionListener;
import com.creatrix.salessolution.Model.Product;
import com.creatrix.salessolution.R;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;


public class _ordersummary_Recyler extends RecyclerView.Adapter<_ordersummary_Recyler.BookViewHolder> {
    private Context context;
   // private List<Product> aProductList;
   List<CampaignCalModel> aProductList;
    DecimalFormat decimalFormat = new DecimalFormat("##.00", new DecimalFormatSymbols(Locale.US));

    private String formatDouble(double val) {
        try {
            return decimalFormat.format(val);
        } catch (Exception e) {
            return String.format(Locale.US, "%.2f", val);
        }
    }

    /*public _ordersummary_Recyler(List<Product> aProductList) {
        this.aProductList = aProductList;
    }*/
    public _ordersummary_Recyler(List<CampaignCalModel> aProductList) {
        this.aProductList = aProductList;
    }

    public void updateData(List<CampaignCalModel> newList) {
        this.aProductList = newList;
        notifyDataSetChanged();
    }
    @Override
    public _ordersummary_Recyler.BookViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.zrv_ordersummery, parent, false);
        context = parent.getContext();
        return new _ordersummary_Recyler.BookViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(_ordersummary_Recyler.BookViewHolder holder, int position) {
        holder.productName.setText(aProductList.get(position).getProductName());
        holder.unitPrice.setText(Double.toString(aProductList.get(position).getUnitPrice()));
        holder.quantity.setText(String.valueOf(aProductList.get(position).getQuantity()));
        holder.tp.setText(formatDouble(aProductList.get(position).getTotalPrice()));
        holder.price.setText(formatDouble(aProductList.get(position).getNetAmount()));
        if(aProductList.get(position) != null){
            holder.discountTxt.setText(formatDouble(aProductList.get(position).getDiscountValue()));
            holder.discountprcntTxt.setText(formatDouble(aProductList.get(position).getDiscountPercentage()));
        }

    }

    @Override
    public int getItemCount() {
        return aProductList.size();
    }

    public class BookViewHolder extends RecyclerView.ViewHolder {
        public TextView productName;
        public TextView unitPrice;
        public TextView quantity;
        public TextView tp;
        public TextView price;
        public TextView discountTxt,discountprcntTxt;
        public BookViewHolder(View view) {
            super(view);
            productName = view.findViewById(R.id.itemNameTxt);
            unitPrice = view.findViewById(R.id.unitPriceTxt);
            quantity = view.findViewById(R.id.quantityTxt);
            tp = view.findViewById(R.id.totalPriceTxt);
            price = view.findViewById(R.id.priceTxt);
            discountTxt = view.findViewById(R.id.discountTxt);
            discountprcntTxt = view.findViewById(R.id.discountprcntTxt);
        }
    }
}




