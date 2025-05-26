package com.creatrix.salessolution.Activity.Team.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.creatrix.salessolution.Activity.Approval.DWSP.DWSPApprovalListActivity;
import com.creatrix.salessolution.Activity.Approval.TourPlan.TourPlanApprovalActivity;
import com.creatrix.salessolution.Activity.Approval.VisitPlan.VisitPlanApprovalActivity;
import com.creatrix.salessolution.Activity.Doctor.TourePlan.TourePlanAC;
import com.creatrix.salessolution.Activity.Team.Model.Team;
import com.creatrix.salessolution.R;
import com.creatrix.salessolution.Services.Constants;
import com.creatrix.salessolution.UtilityHelper.SnackBarManagement;
import com.google.gson.Gson;
import java.util.List;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class TeamAdapter extends RecyclerView.Adapter<TeamAdapter.tAvh> {
    private Context context;
    private List<Team> tList;
    int lastposition = -1;

    public TeamAdapter(Context context, List<Team> tList) {
        this.context = context;
        this.tList = tList;
    }

    @Override
    public tAvh onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rv_teamlist, parent, false);
        context = parent.getContext();
        return new tAvh(itemView);
    }

    @Override
    public void onBindViewHolder(tAvh holder, int position) {
        if(tList!=null){
            Team team=tList.get(position);

            holder.user_name.setText(team.getEmpName());
            holder.user_code.setText(team.getEmpMasterCode());
            holder.user_desig.setText(team.getDesignation());
            holder.popupmenu.setOnClickListener(v -> {

                /*Intent goto_viewAtten=new Intent(context, TeamAttenViewActivity.class);
                Gson gson = new Gson();
                String myJson = gson.toJson(team);
                goto_viewAtten.putExtra("myjson", myJson);
                context.startActivity(goto_viewAtten);*/

                PopupMenu popupMenu=new PopupMenu(context, v);
                popupMenu.getMenuInflater().inflate(R.menu.pop_vt_menu,popupMenu.getMenu());
                popupMenu.show();
                /*int position = holder.getAdapterPosition();
                Team pvm=new Team();*/

                Gson gson=new Gson();
                String teamdata=gson.toJson(team);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.navigation_visit:
                                Constants.WHO="teamvisitadapter";


                                Intent intent = new Intent(context, VisitPlanApprovalActivity.class);
                                intent.putExtra("teamdat", teamdata);
                                intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(intent);

                                break;
                                case R.id.navigation_tour:
                                    Constants.WHO="teamtouradapter";
                                   // Intent tpi = new Intent(context, TourePlanAC.class);
                                    Intent tpi = new Intent(context, TourPlanApprovalActivity.class);
                                    tpi.putExtra("teamdat", teamdata);
                                    tpi.addFlags(FLAG_ACTIVITY_NEW_TASK);
                                    context.startActivity(tpi);


                                /*    SessionM sm=new SessionM(context);
                                    HashMap<String,String> map=new HashMap<>();
                                    map=sm.getloginUserInfo();
                                    String Auth=map.get(SessionM.UserToken);
                                    pvm.DeleteCustApiCall(Auth,ct.getCustomerID());
                                    customerList.remove(position);
                                    notifyItemRemoved(position);
                                    if(onDeletListener!=null)
                                    {
                                        onDeletListener.OnDeletListener(ct);
                                    }*/
                                    break;
                            case R.id.navigation_dwsp:
                                Constants.WHO="teamtouradapter";
                                // Intent tpi = new Intent(context, TourePlanAC.class);
                                Intent dwsp = new Intent(context, DWSPApprovalListActivity.class);
                                dwsp.putExtra("teamdat", teamdata);
                                dwsp.addFlags(FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(dwsp);
                                break;
                        }
                        return false;
                    }
                });

            });
        setFadeAnimation(holder.master);

        }else {
            SnackBarManagement._warning_CustomMessage(holder.master,"Member Not Found");
        }
    }

    @Override
    public int getItemCount() {
        if(tList!=null)
        {
            return tList.size();
        }else {
            return 0;
        }

    }

    public class tAvh extends RecyclerView.ViewHolder {
        public TextView user_name, user_code, user_desig;
        CardView viewAtten;
        ImageView popupmenu;
        ConstraintLayout master;

        public tAvh(View view) {
            super(view);
            user_name = view.findViewById(R.id.tv_name);
            user_code = view.findViewById(R.id.tv_code);
            user_desig = view.findViewById(R.id.tv_desig);
            popupmenu = view.findViewById(R.id.popupmenu);
            master = view.findViewById(R.id.master);
        }
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

        view.startAnimation(animation);
    }
}
