package com.creatrix.salessolution.RecyclerAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.creatrix.salessolution.Model.Rp_CampainViewModel;
import com.creatrix.salessolution.Model.Rp_TargetAcchivment;
import com.creatrix.salessolution.R;
import com.creatrix.salessolution.UtilityHelper.MathUtil;

import java.util.List;

public class _targetAcchivementProductList_Recycler extends RecyclerView.Adapter<_targetAcchivementProductList_Recycler.BookViewHolder> {
    private Context context;
    private final static int FADE_DURATION = 500; //FADE_DURATION in milliseconds
    private List<Rp_TargetAcchivment> aList;
    int lastposition = -1;

    public _targetAcchivementProductList_Recycler(List<Rp_TargetAcchivment> nList) {
        this.aList = nList;
    }


    public class BookViewHolder extends RecyclerView.ViewHolder {
        public TextView productTxt;
        public TextView targetTxt;
        public TextView acchiveTxt;
        public TextView percenTxt;
        public BookViewHolder(View view) {
            super(view);
            productTxt = (TextView) view.findViewById(R.id.productTxt);
            targetTxt = (TextView) view.findViewById(R.id.targetTxt);
            acchiveTxt = (TextView) view.findViewById(R.id.acchiveTxt);
            percenTxt = (TextView) view.findViewById(R.id.percenTxt);
        }
    }

    @Override
    public _targetAcchivementProductList_Recycler.BookViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.zrv_targetachievement_data, parent, false);
        context = parent.getContext();
        return new _targetAcchivementProductList_Recycler.BookViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(_targetAcchivementProductList_Recycler.BookViewHolder holder, int position) {
        holder.productTxt.setText(aList.get(position).getProductName());
        holder.targetTxt.setText(aList.get(position).getTargetQty());
        holder.acchiveTxt.setText(aList.get(position).getAchivment());

        Double aT  = Double.parseDouble(aList.get(position).getTargetQty());
        Double aA  = Double.parseDouble(aList.get(position).getAchivment());

        Double perQty =(aA * 100)/aT;
        if(aA !=0){
        if(perQty !=null || perQty !=Double.NaN){
            Double AcPercent  = MathUtil.round(perQty,2);
            String withPercentSign = AcPercent.toString() + "%";
            holder.percenTxt.setText(withPercentSign);
        }
        }else{
            holder.percenTxt.setText("0%");

        }



    }

    @Override
    public int getItemCount() {
        return aList.size();
    }
}