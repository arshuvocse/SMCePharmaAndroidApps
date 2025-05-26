package com.creatrix.salessolution.RecyclerAdapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.creatrix.salessolution.Activity.Attendance.TeamAttenViewActivity;
import com.creatrix.salessolution.Activity.Expense.ExpenceClaimViewActivity;
import com.creatrix.salessolution.Model.Expense.ADetailListDAO;
import com.creatrix.salessolution.Model.ExpenseReportViewModel;
import com.creatrix.salessolution.Model.ResultInfo;
import com.creatrix.salessolution.Network.CalculationApiCall;
import com.creatrix.salessolution.Network.RetrofitClientInstance;
import com.creatrix.salessolution.R;
import com.creatrix.salessolution.Services.Constants;
import com.google.gson.Gson;

import java.net.SocketTimeoutException;
import java.util.List;

import okhttp3.HttpUrl;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class _expenseClaimReport_Recycler extends RecyclerView.Adapter<_expenseClaimReport_Recycler.BookViewHolder> {
    private Context context;
    private List<ExpenseReportViewModel> aList;
    int lastposition = -1;

    public _expenseClaimReport_Recycler(List<ExpenseReportViewModel> nList,Context context) {
        this.aList = nList;
        this.context = context;
    }


    public class BookViewHolder extends RecyclerView.ViewHolder {
        public TextView dateTxt,nameTxt,typeTxt,statusTxt,amountTxt;
        public LinearLayout masterLayout;

        public BookViewHolder(View view) {
            super(view);
            dateTxt = view.findViewById(R.id.dateTxt);
            nameTxt = view.findViewById(R.id.nameTxt);
            typeTxt = view.findViewById(R.id.typeTxt);
            statusTxt = view.findViewById(R.id.statusTxt);
            amountTxt = view.findViewById(R.id.amountTxt);
            masterLayout = view.findViewById(R.id.masterLayout);

            view.setOnLongClickListener(v -> {
                String appStatus = aList.get(getAdapterPosition()).getApprovalStatus();
                if (appStatus.equals("0")) {
                    onLongClickDelete(getAdapterPosition());
                    return true;
                } else {
                    return false;
                }
            });
        }
    }

    @Override
    public _expenseClaimReport_Recycler.BookViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.zrv_expenseclaim_report, parent, false);
        context = parent.getContext();
        return new _expenseClaimReport_Recycler.BookViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(_expenseClaimReport_Recycler.BookViewHolder holder, int position) {
        holder.dateTxt.setText(aList.get(position).getExpDate());
        holder.nameTxt.setText(aList.get(position).getEmpName());
        holder.typeTxt.setText(aList.get(position).getExpenseTypeName());
       // holder.statusTxt.setText(aList.get(position).getApprovalStatus());
        holder.amountTxt.setText(aList.get(position).getAmount().toString());

        if (aList.get(position).getApprovalStatus().equals("0")) {
            //  holder.statusTxt.setTextColor(Color.parseColor("#ebc51c"));
            holder.statusTxt.setText("Pending..");
            holder.statusTxt.setTextColor(Color.parseColor("#ff7400"));
        } else if (aList.get(position).getApprovalStatus().equals("1")) {
            holder.statusTxt.setText("Verified");
            holder.statusTxt.setBackgroundResource(R.drawable.shape_approved);
        } else if (aList.get(position).getApprovalStatus().equals("2")) {
            holder.statusTxt.setText("Approved");
            holder.statusTxt.setTextColor(Color.parseColor("#00b248"));
        }
        else if (aList.get(position).getApprovalStatus().equals("3")) {
            holder.statusTxt.setText("Rejected");
            holder.statusTxt.setTextColor(Color.parseColor("#C12222"));

        }
        holder.statusTxt.setOnClickListener(v -> {
            Constants.WHO="Reportadapter";
            ExpenseReportViewModel exp = aList.get(position);
            Intent got_details = new Intent(context, ExpenceClaimViewActivity.class);
            Gson gson = new Gson();
            String myJson = gson.toJson(exp);
            got_details.putExtra("ExpDetails", myJson);
            context.startActivity(got_details);
        });


    }

    @Override
    public int getItemCount() {
        return aList.size();
    }


    public void onLongClickDelete(int position) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        builder1.setMessage("Are you sure want to delete ?");
        builder1.setCancelable(true);
        builder1.setPositiveButton(
                "Yes",
                (dialog, id) -> {
                    //Needs to add delete option
                    try{
                       int expid= Integer.parseInt(aList.get(position).getExpenseTypeId());
                        CalculationApiCall service = RetrofitClientInstance.getRetrofitInstance().create(CalculationApiCall.class);
                        Call<ResultInfo> call = service.DelExpenseClaimList(expid);
                        HttpUrl ds = call.request().url();
                        call.enqueue(new Callback<ResultInfo>() {
                            @Override
                            public void onResponse(Call<ResultInfo> call, Response<ResultInfo> response) {
                                ResultInfo info =response.body();
                                if(info !=null){
                                    if(info.getSuccess() == true){
                                        aList.remove(position);
                                    }
                                    else{
                                        Toast.makeText(context, "Delete Not Done", Toast.LENGTH_SHORT).show();
                                    }
                                }else{
                                    Toast.makeText(context, "Some error occurred... Please try again", Toast.LENGTH_SHORT).show();
                                }

                            }
                            @Override
                            public void onFailure(Call<ResultInfo> call, Throwable t) {
                                if(t instanceof SocketTimeoutException){
                                    Toast.makeText(context, "Slow Internet Detected..Please try again", Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(context, "Some error occurred..Please try again", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    }catch (Exception ex){
                        String str = ex.toString();
                        Log.e("Exception",str);
                        Toast.makeText(context, "Some error occurred..Please try again", Toast.LENGTH_SHORT).show();

                    }
                    notifyItemRemoved(position);
                    dialog.cancel();
                });
        builder1.setNegativeButton(
                "Cancel",
                (dialog, id) -> dialog.cancel());

        AlertDialog alert11 = builder1.create();
        alert11.show();

    }


}
