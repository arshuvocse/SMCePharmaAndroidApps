package com.creatrix.salessolution.RecyclerAdapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.creatrix.salessolution.Interface.RecyclerViewActionListener;
import com.creatrix.salessolution.Interface.RecyclerViewFocusChangeListener;
import com.creatrix.salessolution.Model.Product;
import com.creatrix.salessolution.Model.ProductSample;
import com.creatrix.salessolution.R;

import java.util.List;

public class _product_namewithQty_Recycler extends RecyclerView.Adapter<_product_namewithQty_Recycler.BookViewHolder> {
    private Context context;
    private List<Product> nList;
    private List<ProductSample> sList;

    public _product_namewithQty_Recycler(List<Product> nList) {
        this.nList = nList;

    }
    public _product_namewithQty_Recycler(Context c,List<ProductSample> sList) {
        this.sList = sList;
        this.context = c;

    }
    public class BookViewHolder extends RecyclerView.ViewHolder {
        public TextView productName;
        public EditText productQty;

        public BookViewHolder(View view) {
            super(view);
            productName = (TextView) view.findViewById(R.id.productName);

        }
    }

    @Override
    public _product_namewithQty_Recycler.BookViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.zrv_productname_withqty, parent, false);
        context = parent.getContext();
        return new _product_namewithQty_Recycler.BookViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(_product_namewithQty_Recycler.BookViewHolder holder, int position) {
        holder.productName.setText(nList.get(position).getProductName());


    }

    @Override
    public int getItemCount() {
        return nList.size();
    }
}
