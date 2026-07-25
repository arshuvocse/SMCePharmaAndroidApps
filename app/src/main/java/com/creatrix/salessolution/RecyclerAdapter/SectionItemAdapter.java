package com.creatrix.salessolution.RecyclerAdapter;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.creatrix.salessolution.Activity.Doctor.DCR.AddDCRActivity;
import com.creatrix.salessolution.Model.DcpCcpData;
import com.creatrix.salessolution.R;
import com.creatrix.salessolution.Services.Constants;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
public class SectionItemAdapter extends RecyclerView.Adapter<SectionItemAdapter.VH> {
    private Context context;
    public interface OnRowClick {
        void onClick(String typeName, DcpCcpData item);
    }

    private final List<DcpCcpData> data;
    private final String typeName; // e.g., "DCP", "CCP", "XYZ"
    private final OnRowClick onRowClick;

    public SectionItemAdapter(List<DcpCcpData> data, String typeName, OnRowClick onRowClick) {
        this.data = data == null ? new ArrayList<>() : data;
        this.typeName = (typeName == null ? "Others" : typeName);
        this.onRowClick = onRowClick;
        setHasStableIds(true);
    }

    @Override public long getItemId(int position) { return data.get(position).getDoctorId(); }

    @NonNull
    @Override public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_doctor_row, parent, false);
        context = parent.getContext();
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int pos) {
        DcpCcpData item = data.get(pos);
        h.txtName.setText(item.getDoctorName());

        String tn = (typeName == null ? "" : typeName).trim().toUpperCase(java.util.Locale.US);
        Context ctx = h.itemView.getContext();

        if ("DCP".equals(tn)) {
            h.btnMake.setVisibility(View.VISIBLE);
            h.btnMake.setText("Make DCR");

            h.btnMake.setOnClickListener(v -> {
                Toast.makeText(ctx, "Make DCR → " + item.getDoctorName(), Toast.LENGTH_SHORT).show();
          // =helper.getDoctorListFromSQLite();
                // next screen
                Constants.WHO = "ToDayDoclitAdapter";
                Intent intent = new Intent(ctx, AddDCRActivity.class);

                // send current item as JSON (replace with your ViewModel if needed)
                String myJson = new com.google.gson.Gson().toJson(item);
                intent.putExtra("myjson", myJson);

                if (ctx instanceof android.app.Activity
                        && android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    android.app.ActivityOptions options =
                            android.app.ActivityOptions.makeSceneTransitionAnimation((android.app.Activity) ctx);
                    ctx.startActivity(intent, options.toBundle());
                } else {
                    ctx.startActivity(intent);
                }
            });

        }

        else if ("CVP".equals(tn)) {
            h.btnMake.setVisibility(View.VISIBLE);
            h.btnMake.setText("Make CVR");

            h.btnMake.setOnClickListener(v -> {
                Toast.makeText(ctx, "Make CVR → " + item.getDoctorName(), Toast.LENGTH_SHORT).show();
                // =helper.getDoctorListFromSQLite();
                // next screen
                Constants.WHO = "ccrAdapter";
                Intent intent = new Intent(ctx, AddDCRActivity.class);

                // send current item as JSON (replace with your ViewModel if needed)
                String myJson = new com.google.gson.Gson().toJson(item);
                intent.putExtra("myjson", myJson);

                if (ctx instanceof android.app.Activity
                        && android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    android.app.ActivityOptions options =
                            android.app.ActivityOptions.makeSceneTransitionAnimation((android.app.Activity) ctx);
                    ctx.startActivity(intent, options.toBundle());
                } else {
                    ctx.startActivity(intent);
                }
            });


        }

        else {
            // others: hide & clear recycled state
            h.btnMake.setVisibility(View.GONE);
            h.btnMake.setText(null);
            h.btnMake.setOnClickListener(null);
            h.btnMake.setPaintFlags(h.btnMake.getPaintFlags()
                    & ~android.graphics.Paint.UNDERLINE_TEXT_FLAG);
        }
    }




    @Override public int getItemCount() { return data.size(); }

    static class VH extends RecyclerView.ViewHolder {
        TextView txtName, btnMake;
        VH(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtName);
            btnMake = itemView.findViewById(R.id.btnMake);
        }
    }
}
