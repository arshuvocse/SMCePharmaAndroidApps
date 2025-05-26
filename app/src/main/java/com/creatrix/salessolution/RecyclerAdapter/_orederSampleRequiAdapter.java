package com.creatrix.salessolution.RecyclerAdapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.creatrix.salessolution.Activity.OrderProcess.SampleOrderActivity;
import com.creatrix.salessolution.Model.Product;
import com.creatrix.salessolution.R;

import java.util.List;

public class _orederSampleRequiAdapter extends RecyclerView.Adapter<_orederSampleRequiAdapter.BookViewHolder> {
    private Context context;
    private final static int FADE_DURATION = 500; //FADE_DURATION in milliseconds
    private List<Product> aOrderlist;

    public _orederSampleRequiAdapter(Context context,List<Product> aOrderlist) {
        this.context = context;
        this.aOrderlist = aOrderlist;
    }



    @Override
    public _orederSampleRequiAdapter.BookViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rv_sampleorderlist, parent, false);
        context = parent.getContext();
        return new _orederSampleRequiAdapter.BookViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(_orederSampleRequiAdapter.BookViewHolder holder, int position) {
        if(aOrderlist!=null)
        {
            holder.sampleProductName.setText(aOrderlist.get(position).getProductName());
            holder.sampleProductQty.setText(Integer.toString(aOrderlist.get(position).getQuantity()));
            holder.clickSample.setOnClickListener(v -> {
                Toast.makeText(context, "Goto sample requisition Activity", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, SampleOrderActivity.class);
                context.startActivity(intent);
            });
        }
        else {
            Toast.makeText(context, "No Data", Toast.LENGTH_SHORT).show();

        }


    }

    @Override
    public int getItemCount() {
        return aOrderlist.size();
    }
    public class BookViewHolder extends RecyclerView.ViewHolder {
        public TextView sampleProductName;
        public TextView sampleProductQty;
        public CardView clickSample;

        public BookViewHolder(View view) {
            super(view);
            sampleProductName = (TextView) view.findViewById(R.id.sampleProductName);
            sampleProductQty = (TextView) view.findViewById(R.id.sampleProductQty);
            clickSample = (CardView) view.findViewById(R.id.clickSample);
        }
    }
}
