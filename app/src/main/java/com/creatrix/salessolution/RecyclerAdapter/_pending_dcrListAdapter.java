package com.creatrix.salessolution.RecyclerAdapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.creatrix.salessolution.Activity.Doctor.DCR.AddDCRActivity;
import com.creatrix.salessolution.Activity.Pending.PendingListActivity;
import com.creatrix.salessolution.DBAdapter.DBCrudHelper;
import com.creatrix.salessolution.Interface.DeleteListeners;
import com.creatrix.salessolution.Interface.NotifyListener;
import com.creatrix.salessolution.Model.DcrSM;
import com.creatrix.salessolution.Model.DcrVM;
import com.creatrix.salessolution.Model.Doctor.DoctorListViewModel;
import com.creatrix.salessolution.Presenter.PendingCounterPresenter;
import com.creatrix.salessolution.R;
import com.creatrix.salessolution.Services.Constants;
import com.google.gson.Gson;

import java.util.List;

public class _pending_dcrListAdapter extends RecyclerView.Adapter<_pending_dcrListAdapter.DCRVH> {
    private Context context;
    private Activity activity;
    private List<DcrVM> aList;
    private List<DcrSM> dList;
    DBCrudHelper dbCrudHelper;
    PendingCounterPresenter presenter;

    public _pending_dcrListAdapter(List<DcrVM> aList) {
        this.aList = aList;
    }
    public _pending_dcrListAdapter(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;

    }
    public _pending_dcrListAdapter(List<DcrSM> dList, Context context) {
        this.dList = dList;
        this.context = context;
    }

    public class DCRVH extends RecyclerView.ViewHolder {
        public TextView doctorName, typeTxt, dateTxt, product_name_txt, entrytime;
        LinearLayout typelay;
        CardView bacground;
        public DCRVH(View view) {
            super(view);
            bacground = view.findViewById(R.id.bacground);
            typelay = view.findViewById(R.id.typelay);
            doctorName = (TextView) view.findViewById(R.id.doctorName);
            typeTxt = (TextView) view.findViewById(R.id.typeTxt);
            dateTxt = (TextView) view.findViewById(R.id.dateTxt);
            entrytime = (TextView) view.findViewById(R.id.entrytime);
            product_name_txt = (TextView) view.findViewById(R.id.product_name_txt);
        }
    }

    @Override
    public DCRVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.zrv_prescriptionlist, parent, false);
        context = parent.getContext();
        return new DCRVH(itemView);
    }

    @Override
    public void onBindViewHolder(DCRVH holder, int position) {

        if (dList != null) {
            DcrSM dsm = dList.get(position);
            DoctorListViewModel doc = dsm.getDoclist();
            holder.entrytime.setText(dsm.getEntryTime());
            holder.entrytime.setTextColor(Color.parseColor("#ff7400"));
            holder.doctorName.setText(doc.getDoctorName());//holder.doctorName.setText(dsm.getDoctorName());
            holder.typelay.setVisibility(View.GONE);
            holder.dateTxt.setText(dsm.getDcrDate());
            //  holder.bacground.setBackgroundColor(Color.parseColor("#FBF1E8"));
            holder.bacground.setBackgroundResource(R.color.mainshadow);
            holder.bacground.setOnClickListener(v -> {
                Constants.WHO = "PendingDcrAdapter";
                Intent intent = new Intent(context, AddDCRActivity.class);
                Gson gson = new Gson();
                String myJson = gson.toJson(doc);
                String details = gson.toJson(dsm);
                intent.putExtra("myjson", myJson);
                intent.putExtra("dcrDtails", details);
                context.startActivity(intent);
            });
            holder.bacground.setOnLongClickListener(v -> {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                builder1.setMessage("Are you sure wants to delete the Item ?");
                builder1.setCancelable(true);
                builder1.setPositiveButton(

                        "Delete",
                        (dialog, id) -> {
                            try {
                                //Todo:When Delete any product
                                dbCrudHelper=new DBCrudHelper(context);
                                boolean isOk;
                                isOk = dbCrudHelper.DeleteLocal_DcrTable_SQLite(dsm.getDcrId());
                                if(isOk==true)
                                {
                                    dList.remove(position);
                                    notifyItemRemoved(position);
                                    presenter=new PendingCounterPresenter(activity);
                                    presenter.totalDcr();
                                    notifyDataSetChanged();
                                    dialog.cancel();
                                }

                            } catch (Exception exception) {
                                exception.printStackTrace();
                            }
                        });
                builder1.setNegativeButton(
                        "Cancel",
                        (dialog, id) -> dialog.cancel());

                AlertDialog alert11 = builder1.create();
                alert11.show();
                return true;
            });
        }

    }

    @Override
    public int getItemCount() {
        return dList.size();
    }
}