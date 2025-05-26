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

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.creatrix.salessolution.Model.Gift;
import com.creatrix.salessolution.R;

import java.util.ArrayList;
import java.util.List;

public class _product_giftAdapter extends RecyclerView.Adapter<_product_giftAdapter.BookViewHolder> {
    private Context context;
    private List<Gift> aList;
    QtyListener qtyListener;
    public _product_giftAdapter(List<Gift> nList,QtyListener qtyListener) {
        this.aList = nList;
        this.qtyListener = qtyListener;
    }

    public class BookViewHolder extends RecyclerView.ViewHolder {
        public TextView productName;
        EditText et_quantity;
        AppCompatButton btn_minus, btn_plus;
        LinearLayout master;

        public BookViewHolder(View view) {
            super(view);
            master = (LinearLayout) view.findViewById(R.id.master);
            productName = (TextView) view.findViewById(R.id.productName);
            et_quantity = (EditText) view.findViewById(R.id.et_quantity);
            btn_minus = (AppCompatButton) view.findViewById(R.id.btn_minus);
            btn_plus = (AppCompatButton) view.findViewById(R.id.btn_plus);

            master.setOnLongClickListener(v -> {
                qtyListener.onLongClick(getAdapterPosition());
                return true;
            });

            et_quantity.addTextChangedListener(new TextWatcher() {
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
                public void afterTextChanged(Editable editable) {
                    boolean id = et_quantity.hasFocus();
                    if (id==true){
                        int postion =getAdapterPosition();
                        if(!String.valueOf(editable).isEmpty()){
                            qtyListener.onEditTextFocusChange(postion,Integer.parseInt(String.valueOf(editable)));
                            int textLength = et_quantity.getText().length();
                            et_quantity.setSelection(textLength, textLength);
                        }else{
                            qtyListener.onEditTextFocusChange(postion,0);
                        }
                    }

                }
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }
            });
        }

    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.zrv_only_productname, parent, false);
        context = parent.getContext();
        return new BookViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        if(aList!=null){
            Gift gift=aList.get(position);
            holder.productName.setText(aList.get(position).getProductName());
            //holder.et_quantity.setText(String.valueOf(qty));
            if (aList.get(position).getQuantity()==0||String.valueOf(aList.get(position).getQuantity())==null)
            {
                aList.get(position).setQuantity(1);
                holder.et_quantity.setText(String.valueOf(1));
            }else {
                holder.et_quantity.setText(String.valueOf(aList.get(position).getQuantity()));
            }


            holder.btn_plus.setOnClickListener(v -> {
              //  int count = Integer.parseInt(String.valueOf(holder.et_quantity.getText()));
                int count = aList.get(holder.getAdapterPosition()).getQuantity();
                count++;
                aList.get(holder.getAdapterPosition()).setQuantity(count);
                notifyDataSetChanged();
                holder.et_quantity.setText("" + count);

            });
            holder.btn_minus.setOnClickListener(v -> {

                Gift gift1 =aList.get(holder.getAdapterPosition());
                int qty = gift1.getQuantity();
                qty--;
                if(qty<1)
                {
                    qty=1;
                }
                gift1.setQuantity(qty);
                notifyDataSetChanged();
                holder.et_quantity.setText("" + qty);
            });


        }

    }

    @Override
    public int getItemCount() {
        return aList.size();
    }



    public interface QtyListener{
        boolean onLongClick(int position);
        void onEditTextFocusChange(int postion, int parseInt);

    }
}
