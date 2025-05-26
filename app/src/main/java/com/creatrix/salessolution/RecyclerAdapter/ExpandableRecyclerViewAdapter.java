package com.creatrix.salessolution.RecyclerAdapter;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.creatrix.salessolution.DBAdapter.DBCrudHelper;
import com.creatrix.salessolution.Interface.RecyclerViewActionListener;
import com.creatrix.salessolution.Model.OrderMaster;
import com.creatrix.salessolution.Model.Product;
import com.creatrix.salessolution.R;
import com.creatrix.salessolution.UtilityHelper.NetworkInformation;
import java.util.ArrayList;

public class ExpandableRecyclerViewAdapter extends RecyclerView.Adapter<ExpandableRecyclerViewAdapter.ViewHolder> {

    ArrayList<OrderMaster> nameList;
    ArrayList<Integer> counter = new ArrayList<>();
    Context context;
    Activity activity;
    DBCrudHelper dbCrudHelper;
    private final RecyclerViewActionListener mListener;
    View v;

    public ExpandableRecyclerViewAdapter(Context context,
                                         ArrayList<OrderMaster> nameList, Activity activity,RecyclerViewActionListener mListener) {
        this.nameList = nameList;
        this.context = context;
        this.activity = activity;
        this.mListener =mListener;
        dbCrudHelper =new DBCrudHelper(context);
        for (int i = 0; i < nameList.size(); i++) {
            counter.add(0);
        }

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView orderSubmitDate;
        TextView orderStatus;
        TextView tpTXTm;
        RecyclerView cardRecyclerView;
        CardView cardView;
        Button btnSync;

        public ViewHolder(View itemView) {
            super(itemView);
            v = itemView;
            name = itemView.findViewById(R.id.parentCustomerName);
          //  parentCustomerCode = itemView.findViewById(R.id.parentCustomerCode);
            orderSubmitDate = itemView.findViewById(R.id.orderSubmitDate);
            orderStatus = itemView.findViewById(R.id.orderStatus);
            cardRecyclerView = itemView.findViewById(R.id.innerRecyclerView);
            cardView = itemView.findViewById(R.id.cardView);
            btnSync = itemView.findViewById(R.id.btnSync);
            tpTXTm = itemView.findViewById(R.id.tpTXTm);
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    onLongClickDelete(getAdapterPosition());
                    return true;
                }
            });
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout._recyclerview_ordermaster, parent, false);
        ExpandableRecyclerViewAdapter.ViewHolder vh = new ExpandableRecyclerViewAdapter.ViewHolder(v);

        return vh;

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.name.setText(nameList.get(position).getCustomerName());
      //  holder.parentCustomerCode.setText(nameList.get(position).getCustomerCode());
        holder.orderSubmitDate.setText(nameList.get(position).getSubmittedDate());

        ArrayList<Product> aPro = new ArrayList<>();
        aPro = (ArrayList<Product>) nameList.get(position).getOrderDetails();

        double tpm = 0;
        for (int i=0;i<aPro.size();i++){
            Product product = new Product();
            product = aPro.get(i);

            int Qty = product.getQuantity();
            Double price = product.getUnitPrice();
            double tp = Qty * price;
            tpm = tpm +tp;


        }
        holder.tpTXTm.setText(Double.toString(tpm));



        if(nameList.get(position).getStatus()==null || nameList.get(position).getStatus().isEmpty()){
            holder.orderStatus.setText("Not Synced");
            holder.orderStatus.setTextColor(context.getResources().getColor(R.color.amber));
            holder.btnSync.setEnabled(true);
        }else{
            holder.orderStatus.setTextColor(context.getResources().getColor(R.color.teal_700));
            holder.orderStatus.setText("Synced");
            holder.btnSync.setVisibility(v.GONE);
            v.setOnLongClickListener(null);
        }


        holder.btnSync.setOnClickListener(v -> {
            OrderMaster orderMaster = nameList.get(holder.getAdapterPosition());
            if(NetworkInformation.isConnected(context)){
                mListener.OrderSync(orderMaster);
            }else{
                AlertDialog.Builder builder1 = new AlertDialog.Builder(activity);
                builder1.setTitle("NO INTERNET CONNECTION !");
                builder1.setMessage("Please connect to internet first to sync the order");
                builder1.setCancelable(true);
                builder1.setPositiveButton(
                        "OK",
                        (dialog, id) -> {

                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();
            }


        });



        _orderList_Recycler_Adapter itemInnerRecyclerView = new _orderList_Recycler_Adapter(nameList.get(position).getOrderDetails());
        holder.cardRecyclerView.setLayoutManager(new GridLayoutManager(context, 2));
        holder.cardView.setOnClickListener(view -> {

            if (counter.get(holder.getAdapterPosition()) % 2 == 0) {
                holder.cardRecyclerView.setVisibility(View.VISIBLE);
            } else {
                holder.cardRecyclerView.setVisibility(View.GONE);
            }
            counter.set(position, counter.get(position) + 1);
        });
        holder.cardRecyclerView.setAdapter(itemInnerRecyclerView);
    }

    @Override
    public int getItemCount() {
        return nameList.size();
    }


    public void onLongClickDelete(int position){


        AlertDialog.Builder builder1 = new AlertDialog.Builder(activity);
        builder1.setMessage("Are You Sure Want to Delete The Order ?");
        builder1.setCancelable(true);
        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if(dbCrudHelper.DeleteOrderMasterDetail(nameList.get(position).getOrderIdLocal()) == true){
                            nameList.remove(position);
                            notifyItemRemoved(position);
                        }else{
                            Toast.makeText(context,"Please Try Again",Toast.LENGTH_SHORT).show();
                        }
                        dialog.cancel();
                    }
                });
        builder1.setNegativeButton(
                "Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();

    }


}