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

import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;
import com.creatrix.salessolution.Model.ProductSample;
import com.creatrix.salessolution.R;
import java.util.List;

public class _product_sampleAdapter extends RecyclerView.Adapter<_product_sampleAdapter.BookViewHolder> {
    private Context context;
    private final List<ProductSample> aList;
    QtyPListener qtyListener;
    public _product_sampleAdapter(List<ProductSample> nList,QtyPListener qtyListener) {
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

            master.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    qtyListener.onLongPClick(getAdapterPosition());
                    return true;
                }
            });

            et_quantity.addTextChangedListener(new TextWatcher() {
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
                public void afterTextChanged(Editable editable) {
                    boolean id = et_quantity.hasFocus();
                    if (id==true){
                        int postion =getAdapterPosition();
                        int values = 0;
                        if(!String.valueOf(editable).isEmpty()){
                            qtyListener.onEditPTextFocusChange(postion,Integer.parseInt(String.valueOf(editable)));
                            int textLength = et_quantity.getText().length();
                            et_quantity.setSelection(textLength, textLength);
                        }else{
                            qtyListener.onEditPTextFocusChange(postion,0);
                        }
                    }

                }
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }
            });

        }
    }

    @Override
    public BookViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.zrv_only_productname, parent, false);
        //context = parent.getContext();
      /*  aList = new ArrayList<ProductSample>();
        for (int i = 0; i < titles.length; i++) {
            RowItem item = new RowItem(images[i], titles[i], quantities[0]);
            rowItems.add(item);
        }*/
        return new BookViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(BookViewHolder holder, int position) {
        holder.productName.setText(aList.get(position).getProductName());
        if (aList.get(position).getQuantity()==0||String.valueOf(aList.get(position).getQuantity())==null)
        {
            aList.get(position).setQuantity(1);
            holder.et_quantity.setText(String.valueOf(1));
        }else {
            holder.et_quantity.setText(String.valueOf(aList.get(position).getQuantity()));
        }

        holder.btn_plus.setOnClickListener(v -> {
            int count = aList.get(holder.getAdapterPosition()).getQuantity();
            count++;
            aList.get(holder.getAdapterPosition()).setQuantity(count);
            notifyDataSetChanged();
            holder.et_quantity.setText("" + count);
        });
        holder.btn_minus.setOnClickListener(v -> {
          /*  int count = Integer.parseInt(String.valueOf(holder.et_quantity.getText()));
            if (count == 1) {
                holder.et_quantity.setText("1");
            } else {
                count -= 1;
                holder.et_quantity.setText("" + count);
            }*/

            ProductSample sample=aList.get(holder.getAdapterPosition());
            int qty = sample.getQuantity();
            qty--;
            if(qty<1)
            {
                qty=1;
            }
            sample.setQuantity(qty);
            notifyDataSetChanged();
            holder.et_quantity.setText("" + qty);
        });

    }

    @Override
    public int getItemCount() {
        return aList.size();
    }
    public interface QtyPListener{
        boolean onLongPClick(int position);
        void onEditPTextFocusChange(int postion, int parseInt);
    }
}
