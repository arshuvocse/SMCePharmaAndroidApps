package com.creatrix.salessolution.RecyclerAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.creatrix.salessolution.Model.Product;
import com.creatrix.salessolution.Model.Rp_CampainViewModel;
import com.creatrix.salessolution.R;

import java.util.List;

public class _product_NameRecycler  extends RecyclerView.Adapter<_product_NameRecycler.BookViewHolder> {
    private Context context;
    private List<Product> aList;
    DeleteListener deleteListener;

    public _product_NameRecycler(List<Product> nList,DeleteListener deleteListener) {
        this.aList = nList;
        this.deleteListener = deleteListener;
    }
    public class BookViewHolder extends RecyclerView.ViewHolder {
        public TextView productName;
        AppCompatButton btn_minus,btn_plus;
        EditText et_quantity;
        LinearLayout master;

        public BookViewHolder(View view) {
            super(view);
            productName = (TextView) view.findViewById(R.id.productName);
            btn_minus = (AppCompatButton) view.findViewById(R.id.btn_minus);
            btn_plus = (AppCompatButton) view.findViewById(R.id.btn_plus);
            et_quantity = (EditText) view.findViewById(R.id.et_quantity);
            master = (LinearLayout) view.findViewById(R.id.master);
            master.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    deleteListener.onLongClick(getAdapterPosition());
                    return true;
                }
            });
        }
    }

    @Override
    public _product_NameRecycler.BookViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.zrv_only_productname, parent, false);
        context = parent.getContext();
        return new _product_NameRecycler.BookViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(_product_NameRecycler.BookViewHolder holder, int position) {
        holder.btn_minus.setVisibility(View.GONE);
        holder.btn_plus.setVisibility(View.GONE);
        holder.et_quantity.setVisibility(View.GONE);
        holder.productName.setText(aList.get(position).getProductName());


    }

    @Override
    public int getItemCount() {
        return aList.size();
    }
    public interface DeleteListener{
        boolean onLongClick(int position);
    }
}
