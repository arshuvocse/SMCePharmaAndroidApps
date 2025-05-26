package com.creatrix.salessolution.RecyclerAdapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Base64;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.creatrix.salessolution.Activity.Approval.Prescription.PrescriptionApprovalViewActivity;
import com.creatrix.salessolution.Activity.Attendance.Model.ApproveRQ;
import com.creatrix.salessolution.Activity.Attendance.Model.AttenApproval;
import com.creatrix.salessolution.Activity.Attendance.Model.ButtonRP;
import com.creatrix.salessolution.Activity.Attendance.OnClick;
import com.creatrix.salessolution.Activity.Attendance.TeamAttenViewActivity;
import com.creatrix.salessolution.Activity.DA.ChkItemListener;
import com.creatrix.salessolution.DBAdapter.DBCrudHelper;
import com.creatrix.salessolution.Interface.AllApproveAtten;
import com.creatrix.salessolution.Interface.IAttendance;
import com.creatrix.salessolution.Presenter.AttendancePresenter;
import com.creatrix.salessolution.R;
import com.creatrix.salessolution.UtilityHelper.SessionManagement;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import ozaydin.serkan.com.image_zoom_view.ImageViewZoom;
import ozaydin.serkan.com.image_zoom_view.ImageViewZoomConfig;

public class _attenApprovalAdapter extends RecyclerView.Adapter<_attenApprovalAdapter.tlvh>{
    private Context context;
    private List<AttenApproval> aList;
    AttendancePresenter presenter;
    int lastposition = -1;
    private int prev = 0;
    private int current = 0;
    private int next = 0;
    private int role = 0;
    private int myrole = 0;
    OnClick onclick;


   // ArrayList<String> arrayList;
   // TextView tvEmpty;
  //  MainViewModel mainViewModel;




    SessionManagement session;
    HashMap<String, String> userInfo = new HashMap<>();

    DBCrudHelper dbCrudHelper;
    int RoleTypeId;
    String roleType;
    String prev_roleType, next_roleType;
    int accepted = 0, forward = 0;
    boolean isSelectAll=false;
    boolean isEnable=false;
    ArrayList<AttenApproval>selectList=new ArrayList<>();


    public _attenApprovalAdapter(Context context, List<AttenApproval> nList, int RoleTypeId, String roleType,OnClick onclick/*,AllApprove allApprove*/) {
        this.context = context;
        //this.allApprove = allApprove;
        this.aList = nList;
        this.RoleTypeId = RoleTypeId;
        this.roleType = roleType;
        this.onclick = onclick;
        dbCrudHelper = new DBCrudHelper(context);
    }

    @Override
    public _attenApprovalAdapter.tlvh onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout._rv_users_list, parent, false);
        context = parent.getContext();
        return new _attenApprovalAdapter.tlvh(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull _attenApprovalAdapter.tlvh holder, int position) {
        session = new SessionManagement(context);
        userInfo = session.getUserDetails();
        String isForward = userInfo.get(SessionManagement.KEY_Forward);
        String isAccepted = userInfo.get(SessionManagement.KEY_Accepted);

        if (aList != null) {
            AttenApproval approval = aList.get(position);
            CardView viewAtten;

            holder.user_name.setText(approval.getEmpName());
            holder.user_code.setText(approval.getEmpMasterCode());
            holder.user_desig.setText(approval.getAttAddress());
            try {
                if(!approval.getImageString().equals(""))
                {
                    Glide.with(context)
                            .load(approval.getImageString())
                            .fitCenter()
                            //.placeholder(R.drawable.loading_spinner)
                            .into( holder.userimg);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


            if (approval.getAttType() == 1) {
                holder.attenType.setText("In");
                holder.attenTime.setText(approval.getPunchInTime());
            } else {
                holder.attenType.setText("Out");
                holder.attenTime.setText(approval.getPunchInTime());
            }
            // holder.user_desig.setText(String.valueOf(approval.getEmpInfoId()));
            if (RoleTypeId == 2) {
                prev = approval.getMIOEmpId();
                prev_roleType = "MIO";
                current = approval.getASMEMPId();
                next = approval.getRSMEMPId();
                next_roleType = "AM";
                myrole = 2;
            }
            if (RoleTypeId == 3) {
                prev = approval.getASMEMPId();
                prev_roleType = "AM";
                current = approval.getRSMEMPId();
                next = approval.getNSMEMPId();
                next_roleType = "DZSM";
                myrole = 3;
            }
            if (RoleTypeId == 4) {
                prev = approval.getRSMEMPId();
                prev_roleType = "DZSM";
                current = approval.getNSMEMPId();
                next_roleType = "ADMIN";
                next = 0;
                myrole = 4;
            }
            if (RoleTypeId == 5) {
                myrole = 5;
            }
            //TODO:Button On off
            if (prev == current) {
                if (approval.getRoleTypeId() == RoleTypeId) {
                    holder.approve.setVisibility(View.GONE);
                    holder.View.setVisibility(View.GONE);
                    holder.warnToast.setVisibility(View.VISIBLE);
                    holder.warnToast.setText("Approved");
                    holder.warnToast.setBackgroundResource(R.drawable.shape_approved);
                } else {
                    holder.approve.setVisibility(View.VISIBLE);
                    holder.View.setVisibility(View.VISIBLE);
                }
            } else {
                if (approval.getToRoleTypeId() == RoleTypeId) {
                    holder.approve.setVisibility(View.VISIBLE);
                    holder.View.setVisibility(View.VISIBLE);
                    //For Admin
                     /*  if (approval.getToRoleTypeId() == myrole) {
                        if (accepted == 1) {
                            holder.forowardpan.setVisibility(View.VISIBLE);
                            holder.approvepan.setVisibility(View.GONE);

                            holder.accepted.setVisibility(View.VISIBLE);
                            holder.forward.setVisibility(View.GONE);
                        }
                        if (forward == 1) {
                            holder.forowardpan.setVisibility(View.VISIBLE);
                            holder.approvepan.setVisibility(View.GONE);

                            holder.accepted.setVisibility(View.GONE);
                            holder.forward.setVisibility(View.VISIBLE);
                        }

                    } else {
                        //For Others
                        holder.approvepan.setVisibility(View.VISIBLE);
                        holder.forowardpan.setVisibility(View.GONE);

                        holder.approve.setVisibility(View.VISIBLE);
                        holder.View.setVisibility(View.VISIBLE);
                    }*/

                } else {
                    holder.approvepan.setVisibility(View.GONE);
                    holder.View.setVisibility(View.GONE);
                    if (approval.getRoleTypeId() >= RoleTypeId) {
                        holder.warnToast.setVisibility(View.VISIBLE);
                        holder.warnToast.setText("Approved");
                        holder.warnToast.setBackgroundResource(R.drawable.shape_approved);
                    } else {
                        holder.warnToast.setVisibility(View.VISIBLE);
                        holder.warnToast.setText("Need To Approved By " + prev_roleType);
                        holder.warnToast.setBackgroundResource(R.drawable.shape_pending);
                    }
                }
            }
            holder.View.setOnClickListener(v -> {

                Intent goto_viewAtten = new Intent(context, TeamAttenViewActivity.class);
                Gson gson = new Gson();
                String myJson = gson.toJson(approval);
                goto_viewAtten.putExtra("myjson", myJson);
                context.startActivity(goto_viewAtten);

            });
            holder.approve.setOnClickListener(v -> {
                session = new SessionManagement(context);
                userInfo = session.getUserDetails();

                int empid = Integer.parseInt(userInfo.get(SessionManagement.KEY_EmpId));
                int step = Integer.parseInt(approval.getStep());
                int fstep = step + 1;
                //Toast.makeText(context, "empid : "+String.valueOf(empid), Toast.LENGTH_SHORT).show();
                /*   ApproveRQ req = new ApproveRQ();
                req.setApprovalId(0);
                req.setFromEmpId(empid);
                req.setToEmpId(next);
                req.setTableId(approval.getAttendanceId());
                req.setStatus("Verified");//Accepted==approve
                req.setType("Attendance");
                req.setStep(fstep);
                req.setEntryByApp(String.valueOf(empid));
                String entrydate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
                String entrytime = new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(new Date());
                req.setEntryDateApp(entrydate);
                req.setEntryTimeApp(entrytime);
                req.setMenuId(301);

                presenter = new AttendancePresenter(this,context);
                Gson gson=new Gson();
                String data=gson.toJson(req);
                System.out.println("atten:"+data);
                presenter.teamAttendanceApprove(req);*/
                onclick.clickItem(position,empid,next,approval.getAttendanceId(),fstep,true);
                notifyDataSetChanged();
            });



      /*  if (aList.get(position).getApprovalStatus().equals("Pending")) {
            holder.statusTxt.setTextColor(Color.parseColor("#ebc51c"));
        } else if (aList.get(position).getApprovalStatus().equals("Approved")) {
            holder.statusTxt.setTextColor(Color.parseColor("#0f76f5"));
        } else if (aList.get(position).getApprovalStatus().equals("Rejected")) {
            holder.statusTxt.setTextColor(Color.parseColor("#b30c0c"));

        }*/

        }
        /*holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                // check condition
                if (!isEnable)
                {// when action mode is not enable
                    // initialize action mode
                    ActionMode.Callback callback=new ActionMode.Callback() {
                        @Override
                        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                            MenuInflater menuInflater= mode.getMenuInflater();
                            menuInflater.inflate(R.menu.menu_selectall,menu);
                            return true;
                        }
                        @Override
                        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                            // when action mode is prepare
                            // set isEnable true
                            isEnable=true;
                            // create method
                            ClickItem(holder);
                            // set observer on getText method
                        *//*    mainViewModel.getText().observe((LifecycleOwner) activity
                                    , new Observer<String>() {
                                        @Override
                                        public void onChanged(String s) {
                                            // when text change
                                            // set text on action mode title
                                            mode.setTitle(String.format("%s Selected",s));
                                        }
                                    });*//*
                            // return true
                            return true;
                        }
                        @Override
                        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                            // when click on action mode item
                            // get item  id
                            int id=item.getItemId();
                            // use switch condition
                            switch(id)
                            {
                                case R.id.menu_send:
                                    // when click on delete
                                    // use for loop
                                    for(AttenApproval s:selectList)
                                    {
                                        // remove selected item list
                                        aList.remove(s);
                                    }
                                    // check condition
                                    if(aList.size()==0)
                                    {
                                        // when array list is empty
                                        // visible text view
                                        //tvEmpty.setVisibility(View.VISIBLE);
                                    }
                                    // finish action mode
                                    mode.finish();
                                    break;

                                case R.id.menu_selectall:
                                    // when click on select all
                                    // check condition
                                    if(selectList.size()==aList.size())
                                    {
                                        // when all item selected
                                        // set isselectall false
                                        isSelectAll=false;
                                        // create select array list
                                        selectList.clear();
                                    }
                                    else
                                    {
                                        // when  all item unselected
                                        // set isSelectALL true
                                        isSelectAll=true;
                                        // clear select array list
                                        selectList.clear();
                                        // add value in select array list
                                        selectList.addAll(aList);
                                    }
                                    // set text on view model
                                    //mainViewModel.setText(String.valueOf(selectList.size()));
                                    // notify adapter
                                    notifyDataSetChanged();
                                    break;
                            }
                            // return true
                            return true;
                        }

                        @Override
                        public void onDestroyActionMode(ActionMode mode) {
                            // when action mode is destroy
                            // set isEnable false
                            isEnable=false;
                            // set isSelectAll false
                            isSelectAll=false;
                            // clear select array list
                            selectList.clear();
                            // notify adapter
                            notifyDataSetChanged();
                        }
                    };
                    // start action mode
                    ((AppCompatActivity) v.getContext()).startActionMode(callback);
                }
                else
                {
                    // when action mode is already enable
                    // call method
                    ClickItem(holder);
                }
                // return true
                return true;
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // check condition
                if(isEnable)
                {
                    // when action mode is enable
                    // call method
                    ClickItem(holder);
                }
                else
                {
                    // when action mode is not enable
                    // display toast
                    Toast.makeText(context,"You Clicked"+aList.get(holder.getAdapterPosition()),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
        // check condition
        if(isSelectAll)
        {
            // when value selected
            // visible all check boc image
            holder.checkbox.setVisibility(View.VISIBLE);
            //set background color
            holder.itemView.setBackgroundColor(Color.LTGRAY);
        }
        else
        {
            // when all value unselected
            // hide all check box image
            holder.checkbox.setVisibility(View.GONE);
            // set background color
            holder.itemView.setBackgroundColor(Color.TRANSPARENT);
        }*/
    }

    private void ClickItem(tlvh holder) {
        // get selected item value
        AttenApproval s=aList.get(holder.getAdapterPosition());
        // check condition
        if(holder.checkbox.getVisibility()==View.GONE)
        {
            // when item not selected
            // visible check box image
            holder.checkbox.setVisibility(View.VISIBLE);
            // set background color
            holder.itemView.setBackgroundColor(Color.LTGRAY);
            // add value in select array list
            selectList.add(s);
        }
        else
        {
            // when item selected
            // hide check box image
            holder.checkbox.setVisibility(View.GONE);
            // set background color
            holder.itemView.setBackgroundColor(Color.TRANSPARENT);
            // remove value from select arrayList
            selectList.remove(s);
        }
        // set text on view model
       // mainViewModel.setText(String.valueOf(selectList.size()));
    }

    @Override
    public int getItemCount() {
        return aList.size();
    }


    public class tlvh extends RecyclerView.ViewHolder implements AllApproveAtten {
        public TextView user_name, user_code, user_desig, attenTime, attenType, warnToast, forward, accepted;
        CardView viewAtten;
        TextView approve, View;
        LinearLayout approvepan, forowardpan;
        ImageView checkbox;
        ImageViewZoom userimg;

        public tlvh(View view) {
            super(view);
            userimg = view.findViewById(R.id.userimg);
            user_name = view.findViewById(R.id.user_name);
            user_code = view.findViewById(R.id.user_code);
            user_desig = view.findViewById(R.id.user_desig);
            viewAtten = view.findViewById(R.id.viewAtten);
            attenTime = view.findViewById(R.id.attenTime);
            attenType = view.findViewById(R.id.attenType);
            checkbox = view.findViewById(R.id.checkbox);

            approve = view.findViewById(R.id.Approved);
            forward = view.findViewById(R.id.forward);
            View = view.findViewById(R.id.View);
            accepted = view.findViewById(R.id.accepted);
            approvepan = view.findViewById(R.id.approvepan);
            forowardpan = view.findViewById(R.id.forowardpan);
            warnToast = view.findViewById(R.id.warnToast);

        }
        @Override
        public boolean onAllApprove(int a) {
            if(a==1)
            {
               // Toast.makeText(context, "Clicked", Toast.LENGTH_SHORT).show();
               /* for(int i=0;i<aList.size();i++)
                {
                    int attenid=aList.get(i).getAttendanceId();
                    String attenStep=aList.get(i).getStep();
                    approveAll(attenid,attenStep);

                }*/
                return true;
            }
            return true;
        }

      /*  private void approveAll(int attenid,String attstep) {
            session = new SessionManagement(context);
            userInfo = session.getUserDetails();

            int empid = Integer.parseInt(userInfo.get(SessionManagement.KEY_EmpId));
            int step = Integer.parseInt(attstep);
            int fstep = step + 1;

            // Toast.makeText(context, "empid : "+String.valueOf(empid), Toast.LENGTH_SHORT).show();
            ApproveRQ req = new ApproveRQ();
            req.setApprovalId(0);
            req.setFromEmpId(empid);
            req.setToEmpId(next);
            req.setTableId(attenid);
            req.setStatus("Verified");//Accepted==approve
            req.setType("Attendance");
            req.setStep(fstep);
            req.setEntryByApp(String.valueOf(empid));
            String entrydate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
            String entrytime = new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(new Date());
            req.setEntryDateApp(entrydate);
            req.setEntryTimeApp(entrytime);
            req.setMenuId(301);

            presenter = new AttendancePresenter(context);
            presenter.teamAttendanceApprove(req);
        }*/
    }
    public interface AllApprove{
        boolean onAllApprove(ApproveRQ deleteQuote);
    }
}
