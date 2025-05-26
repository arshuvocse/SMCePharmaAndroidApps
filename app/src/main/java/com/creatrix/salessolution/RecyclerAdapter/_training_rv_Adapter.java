package com.creatrix.salessolution.RecyclerAdapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.creatrix.salessolution.Activity.Training.TrainingViewActivity;
import com.creatrix.salessolution.Model.Training;
import com.creatrix.salessolution.Presenter.TrainingPresenter;
import com.creatrix.salessolution.R;
import com.creatrix.salessolution.Services.Constants;
import com.google.gson.Gson;

import java.util.List;

public class _training_rv_Adapter extends RecyclerView.Adapter<_training_rv_Adapter.tdvh> {
    Context context;
    List<Training> tList;

    public _training_rv_Adapter(Context context, List<Training> tList) {
        this.context = context;
        this.tList = tList;
    }

    @NonNull
    @Override
    public _training_rv_Adapter.tdvh onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.training_list_item, parent, false);
        tdvh vh = new tdvh(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull _training_rv_Adapter.tdvh holder, int position) {

        if (tList != null) {
            Training training = tList.get(position);
            holder.tv_title.setText(training.getTitle());
           // Toast.makeText(context, Constants.SeenTraining, Toast.LENGTH_SHORT).show();
            if (training.getIsAppCheck() == false) {
                if (Constants.SeenTraining.equals("seentrainingAdapter")) {
                    //Toast.makeText(context, "1", Toast.LENGTH_SHORT).show();

                    holder.tv_seen.setVisibility(View.VISIBLE);
                } else {
                    //Toast.makeText(context, "2", Toast.LENGTH_SHORT).show();
                    holder.tv_seen.setVisibility(View.GONE);

                }
            } else {
                //Toast.makeText(context, "3", Toast.LENGTH_SHORT).show();
                holder.tv_seen.setVisibility(View.VISIBLE);
            }

            holder.training_Click.setOnClickListener(v -> {
                Intent goto_training = new Intent(context, TrainingViewActivity.class);
                Gson gson = new Gson();
                String myJson = gson.toJson(training);
                goto_training.putExtra("From", "trainingAdapter");
                goto_training.putExtra("myjson", myJson);
                context.startActivity(goto_training);

                /*TrainingPresenter presenter;
                presenter.seenTraining();*/

            });
        }
    }

    @Override
    public int getItemCount() {
        if (tList != null) {
            return tList.size();
        } else {
            return 0;
        }

    }

    public class tdvh extends RecyclerView.ViewHolder {
        TextView tv_seen, tv_title;
        CardView training_Click;

        public tdvh(@NonNull View v) {
            super(v);
            tv_title = v.findViewById(R.id.tv_title);
            tv_seen = v.findViewById(R.id.tv_seen);
            training_Click = v.findViewById(R.id.training_Click);
        }
    }
}
