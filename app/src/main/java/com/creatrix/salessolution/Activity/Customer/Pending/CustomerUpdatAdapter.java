package com.creatrix.salessolution.Activity.Customer.Pending;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.creatrix.salessolution.Activity.Customer.CustomerPropertyChangeActivity;
import com.creatrix.salessolution.Model.Customer;
import com.creatrix.salessolution.R;
import com.creatrix.salessolution.UtilityHelper.SnackBarManagement;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import ozaydin.serkan.com.image_zoom_view.ImageViewZoom;

public class CustomerUpdatAdapter extends RecyclerView.Adapter<CustomerUpdatAdapter.vh> {
    private Context context;
    List<CustomerARModel> cList;
    public CustomerUpdatAdapter(List<CustomerARModel> cList, Context context) {
        this.cList = cList;
        this.context = context;
    }

    public static class vh extends RecyclerView.ViewHolder {
        public TextView custName, custmarket, custmobile, custstatus, custowner, custaddress, custptype, custpwaiting;
        CardView masterLayout;
        LinearLayout ll_master,llapprovalwaiting,llstatus;
        ImageViewZoom shopimg;

        public vh(View view) {
            super(view);
            custName = (TextView) view.findViewById(R.id.custName);
            custmarket = (TextView) view.findViewById(R.id.custmarket);
            custmobile = (TextView) view.findViewById(R.id.custmobile);
            custstatus = (TextView) view.findViewById(R.id.custstatus);
            custowner = (TextView) view.findViewById(R.id.custowner);
            custaddress = (TextView) view.findViewById(R.id.custaddress);
            custptype = (TextView) view.findViewById(R.id.custptype);
            custpwaiting = (TextView) view.findViewById(R.id.custpwaiting);
            shopimg = (ImageViewZoom) view.findViewById(R.id.shopimg);
            masterLayout = (CardView) view.findViewById(R.id.masterLayout);
            ll_master = (LinearLayout) view.findViewById(R.id.ll_master);
            llapprovalwaiting = (LinearLayout) view.findViewById(R.id.llapprovalwaiting);
            llstatus = (LinearLayout) view.findViewById(R.id.llstatus);

        }
    }

    @NonNull
    @Override
    public vh onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rv_customer_pending, parent, false);
        context = parent.getContext();
        return new vh(itemView);
    }
    @Override
    public void onBindViewHolder(@NonNull vh holder, int position) {
        if (cList != null) {
            CustomerARModel ps = cList.get(position);
            holder.llapprovalwaiting.setVisibility(View.GONE);
            holder.llstatus.setVisibility(View.GONE);

            holder.custName.setText(ps.getCustomerName());
            holder.custmarket.setText(ps.getMarketName());
            holder.custmobile.setText(ps.getCellNo());
            holder.custowner.setText(ps.getOwnerName());
            holder.custptype.setText(ps.getProgramTypeName());
            holder.custpwaiting.setText(ps.getWaitingRole());
            holder.custaddress.setText(ps.getAddress());
            if (ps.getImageBase64String() != null) {
                Glide.with(context)
                        .load(ps.getImageBase64String())
                        .fitCenter()
                        .placeholder(R.drawable.userm)
                        .into(holder.shopimg);
              /*  try {
                    byte[] decodedString = Base64.decode(ps.getImageBase64String(), Base64.DEFAULT);
                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    holder.shopimg.setImageBitmap(decodedByte);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }*/
            }
           /*  if (ps.getActionStatus() != null) {
                if (ps.getActionStatus().equals("Pending")) {
                    holder.custstatus.setText(ps.getActionStatus());
                    holder.custstatus.setTextColor(Color.parseColor("#ffffff"));
                    holder.custstatus.setBackgroundResource(R.drawable.shape_prepending);
                } else if (ps.getActionStatus().equals("Verified")) {
                    holder.custstatus.setText(ps.getActionStatus());
                    holder.custstatus.setTextColor(Color.parseColor("#ffffff"));
                    holder.custstatus.setBackgroundResource(R.drawable.shape_pending);
                } else if (ps.getActionStatus().equals("Approved")) {
                    holder.custstatus.setText(ps.getActionStatus());
                    holder.custstatus.setTextColor(Color.parseColor("#ffffff"));
                    holder.custstatus.setBackgroundResource(R.drawable.shape_approved);
                } else if (ps.getActionStatus().equals("Rejected")) {
                    holder.custstatus.setText(ps.getActionStatus());
                    holder.custstatus.setTextColor(Color.parseColor("#ffffff"));
                    holder.custstatus.setBackgroundResource(R.drawable.shape_reject);
                }
            }*/
           /* if (ps.getBtnupdateInfo()==1) {
                holder.ll_master.setVisibility(View.VISIBLE);
            }else {
                holder.updateInfo.setVisibility(View.GONE);
            }*/
            holder.ll_master.setOnClickListener(view -> {
                Intent gotos = new Intent(context, CustomerPropertyChangeActivity.class);
                Gson gson=new Gson();
                String data=gson.toJson(ps);
                System.out.println("data adapter : "+data);
                gotos.putExtra("CustomerData",data);
                context.startActivity(gotos);
               // ((Activity) context).finish();
            });

        } else {
            SnackBarManagement._warning_CustomMessage(holder.masterLayout, "Customer Not Found");
        }
    }
    @Override
    public int getItemCount() {
        if (cList != null) {
            return cList.size();
        }
        return 0;
    }
    public void filterList(ArrayList<CustomerARModel> aList){
        cList = aList;
        notifyDataSetChanged();
    }
}