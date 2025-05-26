package com.creatrix.salessolution.RecyclerAdapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.creatrix.salessolution.Activity.Attendance.TeamAttenViewActivity;
import com.creatrix.salessolution.Model.TeamAtten;
import com.creatrix.salessolution.R;
import com.google.gson.Gson;

import java.util.List;

public class _teamListAdapter extends RecyclerView.Adapter<_teamListAdapter.tlvh> {
    private Context context;
    private List<TeamAtten> tList;
    int lastposition = -1;

    public _teamListAdapter(Context context, List<TeamAtten> nList) {
        this.context = context;
        this.tList = nList;
    }

    @Override
    public tlvh onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout._rv_users_list, parent, false);
        context = parent.getContext();
        return new tlvh(itemView);
    }

    @Override
    public void onBindViewHolder(tlvh holder, int position) {

        if(tList!=null){
        TeamAtten team=tList.get(position);
        CardView viewAtten;

        holder.user_name.setText(team.getUserName());
        holder.user_code.setText("0009");
        holder.user_desig.setText(team.getUserType());
        holder.viewAtten.setOnClickListener(v -> {

            Intent goto_viewAtten=new Intent(context, TeamAttenViewActivity.class);
            Gson gson = new Gson();
            String myJson = gson.toJson(team);
            goto_viewAtten.putExtra("myjson", myJson);
            context.startActivity(goto_viewAtten);

        });


      /*  if (aList.get(position).getApprovalStatus().equals("Pending")) {
            holder.statusTxt.setTextColor(Color.parseColor("#ebc51c"));
        } else if (aList.get(position).getApprovalStatus().equals("Approved")) {
            holder.statusTxt.setTextColor(Color.parseColor("#0f76f5"));
        } else if (aList.get(position).getApprovalStatus().equals("Rejected")) {
            holder.statusTxt.setTextColor(Color.parseColor("#b30c0c"));

        }*/

        }
    }

    @Override
    public int getItemCount() {
        return tList.size();
    }

    public class tlvh extends RecyclerView.ViewHolder {
        public TextView user_name, user_code, user_desig;
        CardView viewAtten;

        public tlvh(View view) {
            super(view);
            user_name = view.findViewById(R.id.user_name);
            user_code = view.findViewById(R.id.user_code);
            user_desig = view.findViewById(R.id.user_desig);
            viewAtten = view.findViewById(R.id.viewAtten);

        }
    }
}
