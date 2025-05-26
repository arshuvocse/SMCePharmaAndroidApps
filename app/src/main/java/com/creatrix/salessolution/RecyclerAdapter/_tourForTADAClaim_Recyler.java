package com.creatrix.salessolution.RecyclerAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.creatrix.salessolution.Activity.DA.ChkItemListener;
import com.creatrix.salessolution.Activity.OrderProcess.Model.CheckedCampaignListener;
import com.creatrix.salessolution.Model.Rp_CampainViewModel;
import com.creatrix.salessolution.Model.TourDetailForTADA;
import com.creatrix.salessolution.R;

import java.util.List;

public class _tourForTADAClaim_Recyler  extends RecyclerView.Adapter<_tourForTADAClaim_Recyler.BookViewHolder> {
    private Context context;
    private final static int FADE_DURATION = 500; //FADE_DURATION in milliseconds
    private List<TourDetailForTADA> aList;
    private int lastSelectedPosition = -1;
    ChkItemListener chkItemListener;
    //CheckedCampaignListener

    public _tourForTADAClaim_Recyler(List<TourDetailForTADA> aList,ChkItemListener chkItemListener) {
        this.aList = aList;
        this.chkItemListener = chkItemListener;
    }

    public class BookViewHolder extends RecyclerView.ViewHolder {
        public TextView typeTxt;
        public TextView marketTxt;
        public TextView nameAcTxt;
        public TextView nameTxt;
        public TextView purposetitle,purposeTxt;
        RadioButton selectable;
        LinearLayout purposediv;
        public BookViewHolder(View view) {
            super(view);
            typeTxt = (TextView) view.findViewById(R.id.typeTxt);
            marketTxt = (TextView) view.findViewById(R.id.marketTxt);
            nameAcTxt = (TextView) view.findViewById(R.id.nameAcTxt);
            nameTxt = (TextView) view.findViewById(R.id.nameTxt);
            selectable = (RadioButton) view.findViewById(R.id.selectable);
            purposetitle = (TextView) view.findViewById(R.id.purposetitle);
            purposeTxt = (TextView) view.findViewById(R.id.purposeTxt);
            purposediv = (LinearLayout) view.findViewById(R.id.purposediv);
            /*selectable.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lastSelectedPosition = getAdapterPosition();
                    notifyDataSetChanged();

              */
            /*      Toast.makeText(_tourForTADAClaim_Recyler.this.context,
                            "selected DA is " + aList.get(lastSelectedPosition).getTPName(),
                            Toast.LENGTH_LONG).show();*//*
                    chkItemListener.ckdItem(aList.get(lastSelectedPosition).getId(),lastSelectedPosition,aList.get(lastSelectedPosition).getTourType());

                 *//*   if(aList.get(lastSelectedPosition).getTourType().equals("mtp"))
                    {
                        chkItemListener.ckdItem(aList.get(lastSelectedPosition).getId(),lastSelectedPosition,"mtp");
                    }
                    if(aList.get(lastSelectedPosition).getTourType().equals("dtp"))
                    {
                        chkItemListener.ckdItem(aList.get(lastSelectedPosition).getId(),lastSelectedPosition,"dtp");
                    }*//*


                }
            });*/
        }

    }

    @Override
    public _tourForTADAClaim_Recyler.BookViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.zrv_tourplan_fortada, parent, false);
        context = parent.getContext();
        return new _tourForTADAClaim_Recyler.BookViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(_tourForTADAClaim_Recyler.BookViewHolder holder, int position) {
           if(position==0)
           {
               try {
                   holder.selectable.setVisibility(View.VISIBLE);
                   holder.selectable.setChecked(true);
                   chkItemListener.ckdItem(aList.get(position).getId(),position,aList.get(position).getTPName(),aList.get(position).getTourPurposeId());
                 //  notifyDataSetChanged();
               } catch (Exception exception) {
                   exception.printStackTrace();
               }
           }else {
               holder.selectable.setVisibility(View.GONE);
           }
        holder.typeTxt.setText(aList.get(position).getTPName());
        holder.marketTxt.setText(aList.get(position).getMarketName());
        holder.purposeTxt.setText(aList.get(position).getSMName());

        if(aList.get(position).getMName() !=null){
            holder.nameTxt.setText(aList.get(position).getMName());
        }
      /*  if(aList.get(position).getTourType().equals("dtp")){
            holder.nameAcTxt.setText("Doctor : ");
            holder.purposediv.setVisibility(View.GONE);

        }else{
            holder.nameAcTxt.setText("Customer : ");
            holder.purposeTxt.setText(aList.get(position).getSMName());
        }*/
      //  holder.selectable.setChecked(lastSelectedPosition == 0);
        //lastSelectedPosition = getAdapterPosition();

       // holder.selectable.setChecked(true);
       // chkItemListener.ckdItem(aList.get(lastSelectedPosition).getId(),lastSelectedPosition,aList.get(lastSelectedPosition).getTourType());
        //chkItemListener.ckdItem(aList.get(position).getId(),position,aList.get(position).getTPName());
//        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return aList.size();
    }
}