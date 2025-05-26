package com.creatrix.salessolution.RecyclerAdapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.creatrix.salessolution.Activity.Doctor.DCR.AddDCRActivity;
import com.creatrix.salessolution.Model.Doctor.DocPlanInfo;
import com.creatrix.salessolution.Model.Doctor.DoctorListViewModel;
import com.creatrix.salessolution.Model.TourDetailForTADA;
import com.creatrix.salessolution.R;
import com.google.gson.Gson;

import java.util.List;

public class _doctorplanTodaysTask_Recycler  extends RecyclerView.Adapter<_doctorplanTodaysTask_Recycler.BookViewHolder> {
    private Context context;
    private final static int FADE_DURATION = 500; //FADE_DURATION in milliseconds
    //private List<DocPlanInfo> aList;
    private List<TourDetailForTADA> aList;
   /* public _doctorplanTodaysTask_Recycler(List<DocPlanInfo> nList) {

        this.aList = nList;
    }*/
    public _doctorplanTodaysTask_Recycler(List<TourDetailForTADA> nList) {

        this.aList = nList;
    }

    public static class BookViewHolder extends RecyclerView.ViewHolder {
        /*public TextView tourDate,nameTxt,nameAcTxt;
        public TextView doctorName;
        public LinearLayout tClick1;*/

        public TextView typeTxt,marketTxt,marketTag,nameAcTxt,nameTxt,purposetitle,purposeTxt;
        RadioButton selectable;
        LinearLayout purposediv;
        public BookViewHolder(View view) {
            super(view);
      /*      tourDate = (TextView) view.findViewById(R.id.tourDate);
            doctorName = (TextView) view.findViewById(R.id.doctorName);
            nameTxt = (TextView) view.findViewById(R.id.nameTxt);
            nameAcTxt = (TextView) view.findViewById(R.id.nameAcTxt);
            tClick1 = (LinearLayout) view.findViewById(R.id.tClick1);*/

            typeTxt = (TextView) view.findViewById(R.id.typeTxt);
            marketTxt = (TextView) view.findViewById(R.id.marketTxt);
            marketTag = (TextView) view.findViewById(R.id.marketTag);
            nameAcTxt = (TextView) view.findViewById(R.id.nameAcTxt);
            nameTxt = (TextView) view.findViewById(R.id.nameTxt);
            selectable = (RadioButton) view.findViewById(R.id.selectable);

            purposetitle = (TextView) view.findViewById(R.id.purposetitle);
            purposeTxt = (TextView) view.findViewById(R.id.purposeTxt);
            purposediv = (LinearLayout) view.findViewById(R.id.purposediv);

        }
    }

    @Override
    public _doctorplanTodaysTask_Recycler.BookViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
               // .inflate(R.layout.zrv_plandate_withdoctorname, parent, false);
                .inflate(R.layout.zrv_tourplan_fortada, parent, false);
        context = parent.getContext();
        return new BookViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(_doctorplanTodaysTask_Recycler.BookViewHolder holder, int position) {
   /*     holder.tourDate.setText(aList.get(position).getTpDate());
        holder.doctorName.setText(aList.get(position).getDoctorName());*/

      /*  holder.doctorName.setText(aList.get(position).getTPName());
        holder.tourDate.setText(aList.get(position).getMarketName());*/

        holder.typeTxt.setText(aList.get(position).getTPName());
        holder.marketTxt.setText(aList.get(position).getMarketName());
        if(aList.get(position).getMName() !=null){
            holder.nameTxt.setText(aList.get(position).getMName());
        }

        if(aList.get(position).getTourType().equals("dtp")){
            holder.nameAcTxt.setText("Doctor : ");
            holder.purposediv.setVisibility(View.GONE);
            holder.marketTag.setText("Doctor Name : ");
            holder.marketTxt.setText(aList.get(position).getMName());

        }else if(aList.get(position).getTourType().equals("mtp")){
            holder.nameAcTxt.setText("Customer : ");
            holder.purposediv.setVisibility(View.GONE);
            holder.purposeTxt.setText(aList.get(position).getSMName());
        }else if(aList.get(position).getTourType().equals("DWSP")){
            holder.nameAcTxt.setText("DWSP : ");
            holder.purposediv.setVisibility(View.GONE);
            holder.marketTag.setText("DWSP Records : ");
            holder.marketTxt.setText(aList.get(position).getMName());
        }

        else {

        }
        holder.selectable.setVisibility(View.GONE);



        /* holder.selectable.setChecked(lastSelectedPosition == position);*/
        /*   holder.tClick1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DoctorListViewModel aInfo =new DoctorListViewModel();

                aInfo.setDoctorId(aList.get(position).getDoctorId());
                aInfo.setDoctorName(aList.get(position).getDoctorName());

                aInfo.setDoctorCode(aList.get(position).getDoctorCode());
                aInfo.setDocTPDetailsId(aList.get(position).getDocTPDetailsId());



             */
        /*   Intent intent = new Intent(context, AddDCRActivity.class);
                Gson gson = new Gson();
                String myJson = gson.toJson(aInfo);
                intent.putExtra("myjson", myJson);
                context.startActivity(intent);*//*

            }
        });*/



//        holder.fromDateTxt.setText(aList.get(position).getFromDate());
//        holder.toDateTxt.setText(aList.get(position).getTodate());
    }

    @Override
    public int getItemCount() {
        return aList.size();
    }
}