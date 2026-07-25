package com.creatrix.salessolution.RecyclerAdapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.UnderlineSpan;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.creatrix.salessolution.Activity.Customer.CustomerUpdateActivity;
import com.creatrix.salessolution.Activity.OrderMainActivity;
import com.creatrix.salessolution.Activity.OrderProcess.SampleOrderActivity;
import com.creatrix.salessolution.Model.Customer;
import com.creatrix.salessolution.R;
import com.creatrix.salessolution.Services.Constants;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class _customerList_RecyclerAdapter extends RecyclerView.Adapter<_customerList_RecyclerAdapter.BookViewHolder> {
    private Context context;
    private final static int FADE_DURATION = 500; //FADE_DURATION in milliseconds
    private List<Customer> aCustomerList;
    String orderTYpe;


    public _customerList_RecyclerAdapter(List<Customer> customerList,String orderTYpe) {
        this.aCustomerList = customerList;
        this.orderTYpe = orderTYpe;
    }

    @Override
    public _customerList_RecyclerAdapter.BookViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout._recyclerview_customer_list, parent, false);
        context = parent.getContext();
        return new _customerList_RecyclerAdapter.BookViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(_customerList_RecyclerAdapter.BookViewHolder holder, @SuppressLint("RecyclerView") int position) {

        String amame = aCustomerList.get(position).getCustomerName();
        Customer customer = aCustomerList.get(position);
        holder.customerName.setText(aCustomerList.get(position).getCustomerName());
        holder.customer_adress_txt.setText(customer.getAddress());
        holder.customer_market_txt.setText(customer.getMarketName());
        holder.customer_marketcode_txt.setText(customer.getMarketCode());
        holder.customer_code_txt.setText(customer.getCustomerCode());
        holder.invoice_txt.setText(customer.getNote());
        if(customer.getCustomerCheck()==1)
        {
            holder.clickID.setBackgroundResource(R.drawable.shape_reject);
        }
        if(customer.getCustomerType()==null)
        {
            holder.custtypelay.setVisibility(View.GONE);
        }
        else {
            holder.custtypelay.setVisibility(View.VISIBLE);
            holder.customer_type_txt.setText(aCustomerList.get(position).getCustomerType());
        }

        if (orderTYpe.equals("HomeToCustomerEdit")) {
            String bspCode = aCustomerList.get(position).getCustomerBsPCode();
            holder.llinvoice.setVisibility(View.GONE);
            if (!TextUtils.isEmpty(bspCode)) {
                holder.llbspcode.setVisibility(View.VISIBLE);
                holder.txtBspCode.setText(bspCode);
            } else {
                holder.llbspcode.setVisibility(View.GONE);
            }
        } else {
            holder.llbspcode.setVisibility(View.GONE);
        }


        SpannableString content = new SpannableString(aCustomerList.get(position).getCellNo());
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        holder.customerMobile.setText(content);
        holder.llmobile.setOnClickListener(v -> {
            //Intent callIntent=new Intent(Intent.ACTION_CALL);
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_NO_USER_ACTION);
            callIntent.setData(Uri.parse("tel:"+aCustomerList.get(position).getCellNo()));
            context.startActivity(callIntent);
        });



       // String html = "<u></u>";
        //underline textview using HtmlCompat.fromHtml() method
       // holder.customerMobile.setText(HtmlCompat.fromHtml(html, HtmlCompat.FROM_HTML_MODE_LEGACY));


//        holder.customer_subgroup_txt.setText(aCustomerList.get(position).getCustomerType());
        holder.clickID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Customer sCustomer;
                sCustomer = aCustomerList.get(position);
                String ds = "dasd";
                Pair[] pair = new Pair[2];
                pair[0] = new Pair<View,String>(holder.profile_image,"imagetrans");
                pair[1] = new Pair<View,String>(holder.customerName,"titletrans");

                if(orderTYpe.equals("HomeToCustomerEdit")){
                    Intent intent = new Intent(context, CustomerUpdateActivity.class);
                    Gson gson = new Gson();
                    String myJson = gson.toJson(sCustomer);
                    intent.putExtra("myjson", myJson);
                    intent.putExtra("OrderType", orderTYpe);

                   // Toast.makeText(context, "", Toast.LENGTH_SHORT).show();
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                        ActivityOptions activityOptions =  ActivityOptions.makeSceneTransitionAnimation((Activity) context,pair);
                        context.startActivity(intent,activityOptions.toBundle());
                    }else{
                        context.startActivity(intent);
                    }
                }
             else   if(orderTYpe.equals("Sample")){
                    Intent intent = new Intent(context, SampleOrderActivity.class);
                    Gson gson = new Gson();
                    String myJson = gson.toJson(sCustomer);
                    intent.putExtra("myjson", myJson);
                    intent.putExtra("OrderType", orderTYpe);

                   // Toast.makeText(context, "", Toast.LENGTH_SHORT).show();
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                        ActivityOptions activityOptions =  ActivityOptions.makeSceneTransitionAnimation((Activity) context,pair);
                        context.startActivity(intent,activityOptions.toBundle());
                    }else{
                        context.startActivity(intent);
                    }
                }else{
                    Constants.WHO="OrderMainAdapter";
                    Intent intent = new Intent(context, OrderMainActivity.class);
                    Gson gson = new Gson();
                    String myJson = gson.toJson(sCustomer);
                    intent.putExtra("myjson", myJson);
                    intent.putExtra("OrderType", orderTYpe);
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                        ActivityOptions activityOptions =  ActivityOptions.makeSceneTransitionAnimation((Activity) context,pair);
                        context.startActivity(intent,activityOptions.toBundle());
                    }else{
                        context.startActivity(intent);

                    }
                }
            }
        });
        setFadeAnimation(holder.itemView);
    }

    @Override
    public int getItemCount() {
        return aCustomerList.size();
    }


    public void filterList(ArrayList<Customer> aList){
        aCustomerList = aList;
        notifyDataSetChanged();
    }





    public class BookViewHolder extends RecyclerView.ViewHolder {
        public TextView customerName,customerMobile,invoice_txt;
        public TextView customer_code_txt;
        public TextView customer_adress_txt;
        public TextView customer_market_txt,customer_marketcode_txt;
        public TextView customer_type_txt;
        public TextView txtBspCode;
        public ImageView profile_image;
        public TextView customer_subgroup_txt;
        LinearLayout custtypelay,llmobile,llbspcode,llinvoice;



        public LinearLayout clickID;

        public BookViewHolder(View view) {
            super(view);


            customerName = (TextView) view.findViewById(R.id.customerName);
            invoice_txt = (TextView) view.findViewById(R.id.invoice_txt);
            customerMobile = (TextView) view.findViewById(R.id.mobile_txt);
            customer_code_txt = (TextView) view.findViewById(R.id.customer_code_txt);
            customer_adress_txt = (TextView) view.findViewById(R.id.customer_adress_txt);
            customer_market_txt = (TextView) view.findViewById(R.id.customer_market_txt);
            txtBspCode = (TextView) view.findViewById(R.id.txtBspCode);
            customer_marketcode_txt = (TextView) view.findViewById(R.id.customer_marketcode_txt);
            customer_type_txt = (TextView) view.findViewById(R.id.customer_type_txt);
            llmobile = (LinearLayout) view.findViewById(R.id.llmobile);
            llbspcode = (LinearLayout) view.findViewById(R.id.llbspcode);
            llinvoice = (LinearLayout) view.findViewById(R.id.llinvoice);
            custtypelay = (LinearLayout) view.findViewById(R.id.custtypelay);
//            customer_subgroup_txt = (TextView) view.findViewById(R.id.customer_subgroup_txt);
            clickID = (LinearLayout) view.findViewById(R.id.clickID);
            profile_image = (ImageView) view.findViewById(R.id.profile_image);


        }
    }


    private void setFadeAnimation(View view) {
        AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(FADE_DURATION);
        view.startAnimation(anim);
    }


}