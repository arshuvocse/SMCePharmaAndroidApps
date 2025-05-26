package com.creatrix.salessolution.RecyclerAdapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.creatrix.salessolution.Activity.Doctor.Prescription.AddPrescriptionActivity;
import com.creatrix.salessolution.Activity.Pending.PendingListActivity;
import com.creatrix.salessolution.DBAdapter.DBCrudHelper;
import com.creatrix.salessolution.Model.Doctor.DoctorListViewModel;
import com.creatrix.salessolution.Model.PrescriptionSM;
import com.creatrix.salessolution.Model.PrescriptionVM;
import com.creatrix.salessolution.R;
import com.creatrix.salessolution.Services.Constants;
import com.creatrix.salessolution.UtilityHelper.SnackBarManagement;
import com.google.gson.Gson;

import java.util.List;

public class _prescriptionList_Recycler extends RecyclerView.Adapter<_prescriptionList_Recycler.BookViewHolder> {
    private Context context;
    String who;
    private List<PrescriptionSM> pList;
    DBCrudHelper dbCrudHelper;

    public _prescriptionList_Recycler(List<PrescriptionSM> pList, String who) {
        this.pList = pList;
        this.who = who;
    }

    public _prescriptionList_Recycler(List<PrescriptionSM> pList, Context context, String who) {
        this.pList = pList;
        this.context = context;
        this.who = who;
    }

    public static class BookViewHolder extends RecyclerView.ViewHolder {
        public TextView doctorName, typeTxt, dateTxt, product_name_txt, entrytime, status;
        public TextView docname_tag, typetag;
        CardView bacground, typelay;

        public BookViewHolder(View view) {
            super(view);
            bacground = view.findViewById(R.id.bacground);
            doctorName = (TextView) view.findViewById(R.id.doctorName);
            typeTxt = (TextView) view.findViewById(R.id.typeTxt);
            dateTxt = (TextView) view.findViewById(R.id.dateTxt);
            entrytime = (TextView) view.findViewById(R.id.entrytime);
            status = (TextView) view.findViewById(R.id.status);
            product_name_txt = (TextView) view.findViewById(R.id.product_name_txt);

            docname_tag = (TextView) view.findViewById(R.id.docname_tag);
            typetag = (TextView) view.findViewById(R.id.typetag);
        }
    }

    @Override
    public _prescriptionList_Recycler.BookViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.zrv_prescriptionlist, parent, false);
        context = parent.getContext();
        return new BookViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull _prescriptionList_Recycler.BookViewHolder holder, int position) {
        if (pList != null) {
            if (who.equals("PrescriptionListActivity")) {
                PrescriptionSM ps = pList.get(position);
                DoctorListViewModel dvm=ps.getDoclist();

                holder.entrytime.setVisibility(View.GONE);
                holder.docname_tag.setText("Created By : ");
                holder.docname_tag.setTextColor(Color.parseColor("#ff7400"));
                holder.doctorName.setText(ps.getEmpName());
                holder.doctorName.setTextColor(Color.parseColor("#ff7400"));

                holder.typetag.setText("Doctor NAme : ");
                holder.typeTxt.setText(ps.getDoctorName());
                holder.dateTxt.setText(ps.getPrescriptionDate());

                holder.bacground.setBackgroundResource(R.color.white);

                if (ps.getApprovalStatus().equals("0")) {
                    holder.status.setText("Pending");
                    holder.status.setTextColor(Color.parseColor("#ff7400"));
                } else if (ps.getApprovalStatus().equals("1")) {
                    holder.status.setText("Verified");
                    holder.status.setTextColor(Color.parseColor("#4169e1"));
                } else if (ps.getApprovalStatus().equals("2")) {
                    holder.status.setText("Approved");
                    holder.status.setTextColor(Color.parseColor("#00b248"));
                } else if (ps.getApprovalStatus().equals("3")) {
                    holder.status.setText("Rejected");
                    holder.status.setTextColor(Color.parseColor("#C12222"));
                }
            }

            if (who.equals("PrescriptionListFragment")) {
                Constants.WHO="PrescriptionListAdapter";
                PrescriptionSM ps = pList.get(position);
                DoctorListViewModel dvm=ps.getDoclist();
                holder.entrytime.setText(ps.getEntryTime());
                holder.entrytime.setTextColor(Color.parseColor("#00b248"));
                holder.doctorName.setText(dvm.getDoctorName());
                holder.typeTxt.setText(ps.getPrescTypeName());
                holder.dateTxt.setText(ps.getPrescriptionDate());
                holder.bacground.setBackgroundResource(R.color.green_shadow);
                holder.bacground.setOnLongClickListener(v -> {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                    builder1.setMessage("Are you sure wants to delete the Item ?");
                    builder1.setCancelable(true);
                    builder1.setPositiveButton(

                            "Delete",
                            (dialog, id) -> {
                                try {
                                    //Todo:When Delete any product
                                    ProgressDialog pd=new ProgressDialog(context);
                                    pd.setMessage("Deleting...");
                                    pd.show();
                                    pd.setCancelable(false);
                                    dbCrudHelper=new DBCrudHelper(context);
                                    boolean isOk;
                                    isOk = dbCrudHelper.DeleteLocal_PrescripTable_SQLite(ps.getPrescripId());
                                    if(isOk==true)
                                    {
                                        pd.dismiss();
                                        pList.remove(position);
                                        notifyItemRemoved(position);
                                        //presenter=new PendingCounterPresenter(this,this);
                                      //  PendingListActivity.presenter.totalPresc();
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
                /*dvm.setDocTPDetailsId(ps.getDoctorId());
                dvm.setDoctorName(ps.getDoctorName());
                dvm.setDoctorTypeName(ps.getDoctorName());
                // dvm.setProgramTypeName(ps.setDoctorName());
                dvm.setDocContact(ps.getDoctorName());*/

                holder.bacground.setOnClickListener(v -> {
                    Intent intent = new Intent(context, AddPrescriptionActivity.class);
                    Gson gson = new Gson();
                    String myJson = gson.toJson(dvm);
                    String details = gson.toJson(ps);
                    intent.putExtra("myjson", myJson);
                    intent.putExtra("prescriptionDtails", details);
                    context.startActivity(intent);
                });


            }

        } else {
            SnackBarManagement._warning_CustomMessage(holder.bacground, "Prescription Not Found");
        }

        //holder.product_name_txt.setText(pList.get(position).getaProList().get(position).getProductName());
 /*       if (dcrVM.getApprovalStatus().equals("0")) {
            statusTxt.setText("Pending");
            statusTxt.setTextColor(Color.parseColor("#ff7400"));
        }
        else if (dcrVM.getApprovalStatus().equals("1")) {
            statusTxt.setText("Verified");
            statusTxt.setTextColor(Color.parseColor("#4169e1"));
        }
        else if (dcrVM.getApprovalStatus().equals("2")) {
            statusTxt.setText("Approved");
            statusTxt.setTextColor(Color.parseColor("#00b248"));
        }
        else if (dcrVM.getApprovalStatus().equals("3")) {
            statusTxt.setText("Rejected");
            statusTxt.setTextColor(Color.parseColor("#C12222"));
        }*/
    }

    @Override
    public int getItemCount() {
        return pList.size();
    }
}