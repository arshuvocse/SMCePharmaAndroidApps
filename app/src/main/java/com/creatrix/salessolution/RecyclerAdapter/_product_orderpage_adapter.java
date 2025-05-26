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
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.creatrix.salessolution.Activity.Approval.Order.OrderApprovalViewActivity;
import com.creatrix.salessolution.Activity.Approval.Order.OrderDtls;
import com.creatrix.salessolution.Activity.OrderMainActivity;
import com.creatrix.salessolution.Activity.OrderProcess.SampleOrderActivity;
import com.creatrix.salessolution.Interface.RecyclerViewActionListener;
import com.creatrix.salessolution.Model.Product;
import com.creatrix.salessolution.Model.ProductSample;
import com.creatrix.salessolution.R;

import java.text.DecimalFormat;
import java.util.List;

public class _product_orderpage_adapter extends RecyclerView.Adapter<_product_orderpage_adapter.BookViewHolder> {
    private Context context;
    private final static int FADE_DURATION = 500; //FADE_DURATION in milliseconds
    private List<Product> aProductList;
    private List<OrderDtls> aProductvList;
    OrderApprovalViewActivity orderAVActivity;
    OrderMainActivity orderMainActivity;
    SampleOrderActivity sampleOrderActivity;
    private List<ProductSample> aProductSampleList;
    TextView totalTP;
    TextView totalVAT;
    TextView total;

    DecimalFormat decimalFormat = new DecimalFormat("##.00");
    private RecyclerViewActionListener mListener;
    String who;


    public _product_orderpage_adapter(List<OrderDtls> aProductvList, OrderApprovalViewActivity orderAVActivity, String ViewOrder) {
        this.context = context;
        this.aProductvList = aProductvList;
        this.orderAVActivity = orderAVActivity;
        this.who = ViewOrder;
    }

    public _product_orderpage_adapter(List<ProductSample> aProductSampleList, SampleOrderActivity sampleOrderActivity, RecyclerViewActionListener mListener, String sampleOrder) {
        this.context = context;
        this.aProductSampleList = aProductSampleList;
        this.sampleOrderActivity = sampleOrderActivity;
        this.mListener = mListener;
        this.who = sampleOrder;
    }

    public _product_orderpage_adapter(List<Product> aProductList, OrderMainActivity orderMainActivity,RecyclerViewActionListener mListener, String mainOrder) {
        this.context = context;
        this.aProductList = aProductList;
        this.orderMainActivity = orderMainActivity;
        this.mListener = mListener;
        this.who = mainOrder;
    }



    @Override
    public _product_orderpage_adapter.BookViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout._recyclerview_product_orderpage, parent, false);
        context = parent.getContext();
        return new _product_orderpage_adapter.BookViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(_product_orderpage_adapter.BookViewHolder holder, int position) {

//        String amame = aProductList.get(position).getProductName();
       // Product ad = aProductList.get(position);

        if(who.equals("MainOrder"))
        {
            if(aProductList!=null)
            {
                holder.tp.setVisibility(View.VISIBLE);
                holder.price.setVisibility(View.VISIBLE);
                holder.unitPrice.setVisibility(View.VISIBLE);
                holder.productName.setText(aProductList.get(position).getProductName());
                holder.unitPrice.setText(Double.toString(aProductList.get(position).getUnitPrice()));
                holder.quantity.setText(String.valueOf(aProductList.get(position).getQuantity()));
                holder.tp.setText(Double.toString(Double.parseDouble(decimalFormat.format(aProductList.get(position).getTp()))));
               // holder.tp.setText(String.valueOf(aProductList.get(position).getTp()));
                holder.price.setText(Double.toString(Double.parseDouble(decimalFormat.format(aProductList.get(position).getPrice()))));
                //holder.price.setText(String.valueOf(aProductList.get(position).getPrice()));
            }else {
                Toast.makeText(orderMainActivity, "No Data", Toast.LENGTH_SHORT).show();
            }
        }
        if(who.equals("SampleOrder")) {
            if(aProductSampleList!=null){
                ProductSample ps = aProductSampleList.get(position);
                holder.productName.setText(ps.getProductName());
                holder.quantity.setText(String.valueOf(ps.getQuantity()));
                LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        5.0f
                );
                LinearLayout.LayoutParams param2 = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        4.0f
                );
                holder.productName.setLayoutParams(param);
                holder.quantity.setLayoutParams(param2);
                holder.unitPrice.setVisibility(View.GONE);
                holder.tp.setVisibility(View.GONE);
                holder.price.setVisibility(View.GONE);
            }
            else {
                Toast.makeText(sampleOrderActivity, "No Data", Toast.LENGTH_SHORT).show();
            }

        }

        if(who.equals("ViewOrder")) {
            if(aProductvList!=null){
                OrderDtls ps = aProductvList.get(position);
                holder.productName.setText(ps.getProductName());
                holder.quantity.setText(String.valueOf(ps.getQuantity()));
                holder.tp.setVisibility(View.VISIBLE);
                holder.price.setVisibility(View.VISIBLE);
                holder.unitPrice.setVisibility(View.VISIBLE);
                holder.unitPrice.setText(Double.toString(ps.getTotalTradePrice()));
                holder.quantity.setText(String.valueOf(ps.getQuantity()));
                holder.quantity.setEnabled(false);
                holder.tp.setText(Double.toString(Double.parseDouble(decimalFormat.format(ps.getUnitPrice()))));
                holder.price.setText(Double.toString(Double.parseDouble(decimalFormat.format(ps.getTotalVatAmount()))));
            }
            else {
                Toast.makeText(orderAVActivity, "No Data", Toast.LENGTH_SHORT).show();
            }

        }

       /* if(!aProductList.get(position).isBonusRow()){

            if(who.equals("MainOrder")){
                holder.tp.setVisibility(View.VISIBLE);
                holder.price.setVisibility(View.VISIBLE);
                holder.unitPrice.setVisibility(View.VISIBLE);
                holder.productName.setText(aProductList.get(position).getProductName());
                holder.unitPrice.setText(Double.toString(aProductList.get(position).getUnitPrice()));
                holder.quantity.setText(String.valueOf(aProductList.get(position).getQuantity()));
                holder.tp.setText(Double.toString(Double.parseDouble(decimalFormat.format(aProductList.get(position).getTp()))));
                holder.price.setText(Double.toString(Double.parseDouble(decimalFormat.format(aProductList.get(position).getPrice()))));
            }else {
              //  if(who.equals("Sample"))
             //   {
                    holder.productName.setText(ps.getProductName());
                    holder.quantity.setText(String.valueOf(ps.getQuantity()));
                    holder.unitPrice.setVisibility(View.GONE);
                    holder.tp.setVisibility(View.GONE);
                    holder.price.setVisibility(View.GONE);

               // }
            }

        }*/

    }

    @Override
    public int getItemCount() {
        if(aProductList!=null)
        {
            return aProductList.size();
            //return aProductList.size();
        }if(aProductSampleList!=null)
        {
            return aProductSampleList.size();
        }
        if(aProductvList!=null)
        {
            return aProductvList.size();
        }
        return 0;
    }

    public class BookViewHolder extends RecyclerView.ViewHolder {
        public TextView productName;
        public TextView productId;
        public TextView unitPrice;
        public EditText quantity;
        public TextView tp;
        public TextView price;

        public LinearLayout clickID;

        public BookViewHolder(View view) {
            super(view);

            productName = (TextView) view.findViewById(R.id.itemNameTxt);
            unitPrice = (TextView) view.findViewById(R.id.unitPriceTxt);
            quantity = (EditText) view.findViewById(R.id.quantityTxt);
            tp = (TextView) view.findViewById(R.id.totalPriceTxt);
            price = (TextView) view.findViewById(R.id.priceTxt);
            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                   mListener.onLongClick(getAdapterPosition());
                   return true;
                }
            });

            quantity.addTextChangedListener(new TextWatcher() {
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
                public void afterTextChanged(Editable editable) {
                    boolean id = quantity.hasFocus();
                    if (id==true){
                        int postion =getAdapterPosition();
                            int values = 0;
                            if(!String.valueOf(editable).isEmpty()){
                                mListener.onEditTextFocusChange(postion,Integer.parseInt(String.valueOf(editable)));
                                int textLength = quantity.getText().length();
                               quantity.setSelection(textLength, textLength);
                            }else{
                                mListener.onEditTextFocusChange(postion,0);
                            }
                    }

                }
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }
            });

        }

    }



}




