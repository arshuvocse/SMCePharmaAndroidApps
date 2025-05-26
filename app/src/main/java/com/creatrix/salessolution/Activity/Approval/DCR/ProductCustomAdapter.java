package com.creatrix.salessolution.Activity.Approval.DCR;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.creatrix.salessolution.Model.Product;
import com.creatrix.salessolution.R;

import java.util.ArrayList;
import java.util.List;

public class ProductCustomAdapter extends ArrayAdapter<DcrApplogProduct> {

    private static final String TAG = "PersonListAdapter";

    private Context context;
    private int mResource;
    private int lastPosition = -1;
    List<DcrApplogProduct> products;


    /**
     * Holds variables in a View
     */
    private static class ViewHolder {
        TextView name;
        TextView qty;
    }

    public ProductCustomAdapter(Context context, int mResource, List<DcrApplogProduct> products) {
         super(context, mResource, products);
        this.context = context;
        this.mResource = mResource;
        this.products = products;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //get the persons information
        String name = getItem(position).getProductName();
        int qty = getItem(position).getProductQty();
        //Create the person object with the information
        DcrApplogProduct product = new DcrApplogProduct(name,qty);
        //create the view result for showing the animation
        final View result;
        //ViewHolder object
        ViewHolder holder;
        if(convertView == null){
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(mResource, parent, false);
            holder= new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.dcrproduct);
            holder.qty = (TextView) convertView.findViewById(R.id.dcrqty);

            result = convertView;
            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder) convertView.getTag();
            result = convertView;
        }


        Animation animation = AnimationUtils.loadAnimation(context,
                (position > lastPosition) ? R.anim.down_anim : R.anim.up_anim);
        result.startAnimation(animation);
        lastPosition = position;

        holder.name.setText(product.getProductName());
        holder.qty.setText(String.valueOf(product.getProductQty()));


        return convertView;
    }
}