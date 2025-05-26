package com.creatrix.salessolution.RecyclerAdapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.creatrix.salessolution.Model.DcrVM;
import com.creatrix.salessolution.R;
import com.creatrix.salessolution.UtilityHelper.SnackBarManagement;

import java.util.List;

public class _dcr_ListRecyclerAdapter extends RecyclerView.Adapter<_dcr_ListRecyclerAdapter.ViewHolder> {
    private final static int FADE_DURATION = 500; //FADE_DURATION in milliseconds
    private Context context;
    private List<DcrVM> aList;


    public _dcr_ListRecyclerAdapter(Context context,List<DcrVM> aList) {
        this.context = context;
        this.aList = aList;
    }


    /*  @Override
      public BookViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
          *//*View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.zrv_dcr_list_recyclerview, parent, false);
        context = parent.getContext();
        return new _dcr_ListRecyclerAdapter.BookViewHolder(itemView);*//*
    }*/
   /* @Override
    public int getItemViewType(int position) {
        if (type.equals("Prescription") || type.equals("")) {
            return TYPE_TWO;
        } else {
            return TYPE_ONE;
        }
    }*/


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.zrv_dcr_list_recyclerview, parent, false);
        context = parent.getContext();
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull _dcr_ListRecyclerAdapter.ViewHolder holder, int position) {

        if (aList != null) {
            ((ViewHolder) holder).setdata(aList.get(position));
        } else {
            SnackBarManagement._warning_CustomMessage(holder.masterLayout,"No DCR Found");
        }
    }

 /*   @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        // Toast.makeText(context, "type: " + type, Toast.LENGTH_SHORT).show();
        // Toast.makeText(context, "data: " + aList, Toast.LENGTH_SHORT).show();
        //Toast.makeText(context, "data2: " + sList, Toast.LENGTH_SHORT).show();

      *//*  switch (type)
        {
            case "":

                if(holder instanceof BookViewHolder) {
                    ((BookViewHolder) holder).nameTxt.setText(aList.get(position).getName());
                    ((BookViewHolder) holder).miNameTxt.setText(aList.get(position).getEmpName());
                    ((BookViewHolder) holder).dateTxt.setText(aList.get(position).getDcrDate());
                    if (aList.get(position).getApproved()) {
                        ((BookViewHolder) holder).statusTxt.setText("Approved");
                    } else {
                        ((BookViewHolder) holder).statusTxt.setText("Pending");

                    }
                }
            else {

            }
                break;

            case "DCRLocal":
                if(sList!=null){
                    if(holder instanceof BookViewHolder2){
                        //,miNameTxt,dateTxt,statusTxt
                        ((BookViewHolder2) holder).entrytime.setText(sList.get(position).getEntryTime());
                        ((BookViewHolder2) holder).doctorName.setText(sList.get(position).getDoctorName());
                        ((BookViewHolder2) holder).typeTxt.setText(sList.get(position).getTourPlanTypeId());
                        ((BookViewHolder2) holder).dateTxt.setText(sList.get(position).getDcrDate());
                    }

                }
                else {}
                break;
        }*//*
      *//*  if(aList!=null)
        {
            if(holder instanceof BookViewHolder) {
                ((BookViewHolder) holder).nameTxt.setText(aList.get(position).getName());
                ((BookViewHolder) holder).miNameTxt.setText(aList.get(position).getEmpName());
                ((BookViewHolder) holder).dateTxt.setText(aList.get(position).getDcrDate());
                if (aList.get(position).getApproved()) {
                    ((BookViewHolder) holder).statusTxt.setText("Approved");
                } else {
                    ((BookViewHolder) holder).statusTxt.setText("Pending");

                }
            }
        }else {

        }
        if(sList!=null){
            if(holder instanceof BookViewHolder2){
                //,miNameTxt,dateTxt,statusTxt
                ((BookViewHolder2) holder).entrytime.setText(sList.get(position).getEntryTime());
                ((BookViewHolder2) holder).doctorName.setText(sList.get(position).getDoctorName());
                ((BookViewHolder2) holder).typeTxt.setText(sList.get(position).getTourPlanTypeId());
                ((BookViewHolder2) holder).dateTxt.setText(sList.get(position).getDcrDate());
            }

        }
        else {}*//*

       *//* if (aList == null) {
            if (sList != null) {
                if (holder instanceof BookViewHolder2) {
                    ((BookViewHolder2) holder).entrytime.setText(sList.get(position).getEntryTime());
                    ((BookViewHolder2) holder).doctorName.setText(sList.get(position).getDoctorName());
                    ((BookViewHolder2) holder).typeTxt.setText(sList.get(position).getTourPlanTypeId());
                    ((BookViewHolder2) holder).dateTxt.setText(sList.get(position).getDcrDate());
                }

            } else {
            }
        }
        else {
            if (holder instanceof BookViewHolder) {
                //,miNameTxt,dateTxt,statusTxt
                ((BookViewHolder) holder).nameTxt.setText(aList.get(position).getName());
                ((BookViewHolder) holder).miNameTxt.setText(aList.get(position).getEmpName());
                ((BookViewHolder) holder).dateTxt.setText(aList.get(position).getDcrDate());
                if (aList.get(position).getApproved()) {
                    ((BookViewHolder) holder).statusTxt.setText("Approved");
                } else {
                    ((BookViewHolder) holder).statusTxt.setText("Pending");

                }
            }
        }*//*


    }*/

    @Override
    public int getItemCount() {
        if (aList != null) {
            return aList.size();
        }
        return 0;
    }

    private void setFadeAnimation(View view) {
        AnimationSet set = new AnimationSet(true);
        Animation animation = new AlphaAnimation(0.0f, 1.0f);
        animation.setDuration(500);
        view.startAnimation(animation);

        animation = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, -1.0f, Animation.RELATIVE_TO_SELF, 0.0f
        );
        animation.setDuration(100);

    //   view.startAnimation(set);
        view.startAnimation(animation);
    }

   /* @Override
    public void onBindViewHolder(_dcr_ListRecyclerAdapter.BookViewHolder holder, int position) {


        holder.nameTxt.setText(aList.get(position).getName());
        holder.miNameTxt.setText(aList.get(position).getEmpName());
        holder.dateTxt.setText(aList.get(position).getDcrDate());
        if(aList.get(position).getApproved()){
            holder.statusTxt.setText("Approved");
        }else{
            holder.statusTxt.setText("Pending");

        }

//        Animation animation = AnimationUtils.loadAnimation(context, R.anim.slide_in_left);
//        animation.setStartOffset(30 * position);//Provide delay here
//        holder.itemView.startAnimation(animation);
    }*/

    //DCR
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView docnameTxt, createdby, createddate, statusTxt;
        CardView masterLayout;
        public ViewHolder(View view) {
            super(view);
            docnameTxt = (TextView) view.findViewById(R.id.nameTxt);
            createdby = (TextView) view.findViewById(R.id.createdby);
            createddate = (TextView) view.findViewById(R.id.createddate);
            statusTxt = (TextView) view.findViewById(R.id.statusTxt);
            masterLayout = (CardView) view.findViewById(R.id.masterLayout);
            setFadeAnimation(masterLayout);
        }

        public void setdata(DcrVM dcrVM) {
            docnameTxt.setText(dcrVM.getName());
            createdby.setText(dcrVM.getEmpName());
            createddate.setText(dcrVM.getDcrDate());
            statusTxt.setVisibility(View.VISIBLE);

            try {
                if(dcrVM.getApprovalStatus().equals("0"))
                {
                    statusTxt.setText("Pending");
                    statusTxt.setTextColor(Color.parseColor("#ff7400"));
                }else if(dcrVM.getApprovalStatus().equals("1")){
                    statusTxt.setText("Verified");
                    statusTxt.setTextColor(Color.parseColor("#4169e1"));
                }
                else if(dcrVM.getApprovalStatus().equals("2")){
                    statusTxt.setText("Approved");
                    statusTxt.setTextColor(Color.parseColor("#00b248"));
                }
                else if(dcrVM.getApprovalStatus().equals("3")){
                    statusTxt.setText("Rejected");
                    statusTxt.setTextColor(Color.parseColor("#C12222"));
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }

/* //Prescription
    public class BookViewHolder2 extends RecyclerView.ViewHolder {
        public TextView doctorName, typeTxt, dateTxt, product_name_txt, entrytime;

        public BookViewHolder2(View view) {
            super(view);
            doctorName = (TextView) view.findViewById(R.id.doctorName);
            typeTxt = (TextView) view.findViewById(R.id.typeTxt);
            dateTxt = (TextView) view.findViewById(R.id.dateTxt);
            entrytime = (TextView) view.findViewById(R.id.entrytime);
            product_name_txt = (TextView) view.findViewById(R.id.product_name_txt);
        }
        public void setdata(DcrSM dcrSM) {
            doctorName.setText(dcrSM.getDoctorName());
            dateTxt.setText(dcrSM.getDcrDate());
            entrytime.setText(dcrSM.getEntryTime());
        }
    }*/
}