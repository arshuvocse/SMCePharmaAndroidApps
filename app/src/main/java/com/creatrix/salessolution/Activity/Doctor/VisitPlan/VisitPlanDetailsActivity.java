package com.creatrix.salessolution.Activity.Doctor.VisitPlan;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.creatrix.salessolution.Activity.Doctor.VisitPlan.Model.VisitPlanMaster;
import com.creatrix.salessolution.Activity.Doctor.VisitPlan.Model.VisitplanModel;
import com.creatrix.salessolution.DBAdapter.DBCrudHelper;
import com.creatrix.salessolution.DBAdapter.DBDoctor.DBDoctorHelper;
import com.creatrix.salessolution.Interface.DeleteListener;
import com.creatrix.salessolution.Interface.IVisitPlan;
import com.creatrix.salessolution.Model.Doctor.DoctorListViewModel;
import com.creatrix.salessolution.Model.MonthDate;
import com.creatrix.salessolution.Model.ResultInfo;
import com.creatrix.salessolution.Network.ApiDoctorCall;
import com.creatrix.salessolution.Network.RetrofitClientInstance;
import com.creatrix.salessolution.Presenter.VisitPlanPresenter;
import com.creatrix.salessolution.R;
import com.creatrix.salessolution.UtilityHelper.SessionManagement;
import com.creatrix.salessolution.UtilityHelper.SnackBarManagement;
import com.creatrix.salessolution.databinding.ActivityVisitPlanDetailsBinding;
import com.google.gson.Gson;

import java.net.SocketTimeoutException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import okhttp3.HttpUrl;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VisitPlanDetailsActivity extends AppCompatActivity implements ChkedItemListener, IVisitPlan.View, DeleteListener {
    ActivityVisitPlanDetailsBinding binding;
    DoctorVisitPlanDtailsaAdapter adapter;
    SimpleDateFormat dateFormat;
    Dialog popupDoctor;
    DBCrudHelper dbCrudHelper;
    DBDoctorHelper dbDoctorHelper;
    List<DoctorListViewModel> docList = new ArrayList<>();
    MonthDate monthDatel, md;

    RecyclerView rv_doctors;
    EditText srchview;
    TextView done_doc, cancel_doc, label_info;
    ImageView cleare;
    DoctorItemChkAdapter itemChkAdapter;
    VisitplanModel vp;
    List<VisitplanModel> vpl = new ArrayList<>();
    SessionManagement session;
    HashMap<String, String> user;
    String roleType, currentdate;
    int empId;
    String year, date, day;
    VisitPlanPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVisitPlanDetailsBinding.inflate(getLayoutInflater());
        //setContentView(R.layout.activity_visit_plan_details);
        setContentView(binding.getRoot());
        presenter = new VisitPlanPresenter(this, this);
        session = new SessionManagement(getApplicationContext());
        session.checkLogin();
        user = session.getUserDetails();

        empId = Integer.parseInt(user.get(SessionManagement.KEY_EmpId));
        roleType = user.get(SessionManagement.KEY_EmpRoleType);

        dbCrudHelper = new DBCrudHelper(VisitPlanDetailsActivity.this);
        dbDoctorHelper = new DBDoctorHelper(VisitPlanDetailsActivity.this);
        docList = dbDoctorHelper.getCustDoctorListFromSQLite("Doc");


        String month = getIntent().getStringExtra("Month");
        year = getIntent().getStringExtra("Year");
        date = getIntent().getStringExtra("Date");
        day = getIntent().getStringExtra("Day");
        binding.tvMonthdateyear.setText(month /*+ " " + date */ + "," + year);
        SimpleDateFormat nameV = new SimpleDateFormat("EEE");
        // binding.tvOnlyday.setText(day);
        Gson gson = new Gson();
        md = gson.fromJson(getIntent().getStringExtra("itemjson"), MonthDate.class);
        if (md != null) {
            SetInRecyclerview(md);

        } else {
        }
        try {
            String today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
            currentdate = getIntent().getStringExtra("VDate");
            dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date Todate = null;
            Date CurrentDate = null;
            Todate = dateFormat.parse(today);
            CurrentDate = dateFormat.parse(currentdate);
            if (CurrentDate != null && Todate != null) {
                if (CurrentDate.before(Todate)) {
                    binding.fabAdd.setVisibility(View.GONE);
                    binding.btnDone.setVisibility(View.GONE);
                }/* else {
                   // binding.fabAdd.setVisibility(View.VISIBLE);
                    //binding.btnDone.setVisibility(View.VISIBLE);
                }*/
            } else {
               /* binding.fabAdd.setVisibility(View.VISIBLE);
                binding.btnDone.setVisibility(View.VISIBLE);*/
                GetTourPlanMasterData(md.getMonthV(),md.getYearV(),empId);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        binding.btnDone.setOnClickListener(v -> {
            if(vpl.size()>0)
            {
                presenter.saveVisitPlanPerdayByEmpId(vpl);
            }else {
               SnackBarManagement._error_CustomMessage(binding.masterLayout,"Doctor Can not be Empty!!");
            }

        });


        binding.fabAdd.setOnClickListener(v -> {

            AlertDialog.Builder builder = new AlertDialog.Builder(this); // or getContext() for fragment
            builder.setTitle("Select Type");
            String[] options = {"Doctor", "Customer"};

            builder.setItems(options, (dialog, which) -> {
                if (which == 0) {
                    // Doctor selected
                    Toast.makeText(this, "Doctor selected", Toast.LENGTH_SHORT).show();

                    docList = dbDoctorHelper.getCustDoctorListFromSQLite("Doc");
                    popup_Doctor("Doc");
                    popupDoctor.show();
                    // TODO: Navigate to Doctor form or perform action
                } else if (which == 1) {
                    // Customer selected
                    Toast.makeText(this, "Customer selected", Toast.LENGTH_SHORT).show();

                    docList = dbDoctorHelper.getCustDoctorListFromSQLite("Cust");
                    popup_Doctor("Cust");
                    popupDoctor.show();
                    // TODO: Navigate to Customer form or perform action
                }
            });

            builder.show();


        });
    }
    @Override
    public void editTourPlanInfo(int pos, int id) {

    }

    public void popup_Doctor(String Dtype) {
        popupDoctor = new Dialog(VisitPlanDetailsActivity.this);
        popupDoctor.setContentView(R.layout.custdoc_dialog);
        popupDoctor.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        popupDoctor.getWindow().setLayout(Toolbar.LayoutParams.MATCH_PARENT, Toolbar.LayoutParams.MATCH_PARENT);
        popupDoctor.getWindow().getAttributes().gravity = Gravity.CENTER;
        popupDoctor.setCanceledOnTouchOutside(false);

        rv_doctors = popupDoctor.findViewById(R.id.rv_doclist);
        srchview = popupDoctor.findViewById(R.id.srchview);
        done_doc = popupDoctor.findViewById(R.id.btn_done);
        cancel_doc = popupDoctor.findViewById(R.id.btn_cancel);
        label_info = popupDoctor.findViewById(R.id.ff);

        if (Dtype=="Doc"){
            label_info.setText("Select Doctor");
        }
        else {
            label_info.setText("Select Customer");
        }
       /* cleare = popupDoctor.findViewById(R.id.cleare);
        cleare.setOnClickListener(v -> {
            try {
                for (int i = 0; i < monthDatel.getVisitplanList().size(); i++) {
                    rv_doctors.removeItemDecorationAt(i);
                    vpl.clear();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });*/

        done_doc.setOnClickListener(v -> {
            popupDoctor.dismiss();
            adapter = new DoctorVisitPlanDtailsaAdapter(VisitPlanDetailsActivity.this, monthDatel, this);
            //adapter = new DoctorVisitPlanDtailsaAdapter(VisitPlanDetailsActivity.this, vpList);
            binding.rvNewadddoc.setHasFixedSize(true);
            binding.rvNewadddoc.setAdapter(adapter);
            final LinearLayoutManager layoutManager = new LinearLayoutManager(VisitPlanDetailsActivity.this);
            binding.rvNewadddoc.setLayoutManager(layoutManager);
            adapter.notifyDataSetChanged();

        });
        try {
            itemChkAdapter = new DoctorItemChkAdapter(VisitPlanDetailsActivity.this, docList, this);
            rv_doctors.setLayoutManager(new LinearLayoutManager(VisitPlanDetailsActivity.this));
            rv_doctors.setAdapter(itemChkAdapter);
            itemChkAdapter.notifyDataSetChanged();
            cancel_doc.setOnClickListener(v -> {
                popupDoctor.dismiss();
            });
            search();
        } catch (Exception exception) {
            exception.printStackTrace();
        }

    }

    // public void SetInRecyclerview(List<VisitplanModel> vpList) {
    public void SetInRecyclerview(MonthDate monthDateList) {
        adapter = new DoctorVisitPlanDtailsaAdapter(VisitPlanDetailsActivity.this, monthDateList, this);
        binding.rvNewadddoc.setHasFixedSize(true);
        binding.rvNewadddoc.setAdapter(adapter);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(VisitPlanDetailsActivity.this);
        binding.rvNewadddoc.setLayoutManager(layoutManager);
        adapter.notifyDataSetChanged();
    }

    //search
    private void search() {
        srchview.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                rv_doctors.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable editable) {

                filter(editable.toString());
                rv_doctors.setVisibility(View.VISIBLE);
            }
        });
    }
    private void filter(String word) {
        List<DoctorListViewModel> filterwordlist = new ArrayList<>();
        for (DoctorListViewModel words : docList) {
            if (words.getDoctorName() == null) {
                try {

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (words.getDoctorName().toLowerCase().contains(word.toLowerCase())) {
                filterwordlist.add(words);
            }
        }
        itemChkAdapter.filterListFun(filterwordlist);
        itemChkAdapter.notifyDataSetChanged();
    }

    @Override
    public void ckdItemName(List<DoctorListViewModel> st, int pos) {

        if (st != null) {
            vp = new VisitplanModel();
            for (int i = 0; i < st.size(); i++) {
                String name = st.get(i).getDoctorName();
                vp.setDoctorName(name);
                vp.setDoctorId(st.get(i).getDoctorId());
                vp.setYearValue(Integer.parseInt(year));
                vp.setMonthValue(md.getMonthV());
                vp.setTourPlanDate(currentdate);
                vp.setEmpInfoId(empId);
            }
            vpl.add(vp);
            monthDatel = new MonthDate();
            monthDatel.setVisitplanList(vpl);
        } else {
            adapter.notifyItemRangeRemoved(0, st.size());
            vpl.clear();
            rv_doctors.removeItemDecorationAt(pos);

        }
        //  Toast.makeText(this, "No selected"+pos, Toast.LENGTH_SHORT).show();
        // Toast.makeText(this, "doc name "+st.toString(), Toast.LENGTH_SHORT).show();

    }

    @Override
    public void unckdItemName(List<DoctorListViewModel> st, int Pos) {
        vp = new VisitplanModel();
        for (int i = 0; i < st.size(); i++) {
            String name = st.get(i).getDoctorName();
            vp.setDoctorName(name);
            //  Toast.makeText(this, "UnChecked name: "+name, Toast.LENGTH_SHORT).show();
        }

    /*    vpl.add(vp);
        monthDatel=new MonthDate();
        monthDatel.setVisitplanList(vpl);*/
    }

    @Override
    public void deleteItemFromServer(int pos,int id) {
        adapter.notifyItemRemoved(pos);
        try {
            ApiDoctorCall service = RetrofitClientInstance.getRetrofitInstance().create(ApiDoctorCall.class);
            Call<ResultInfo> call = service.DeleteTourPlanData(id);
            call.enqueue(new Callback<ResultInfo>() {
                @Override
                public void onResponse(Call<ResultInfo> call, Response<ResultInfo> response) {
                    ResultInfo info = response.body();
                    Toast.makeText(VisitPlanDetailsActivity.this, ""+String.valueOf(response.body()), Toast.LENGTH_SHORT).show();
                    if (info.getSuccess() == true) {
                        new androidx.appcompat.app.AlertDialog.Builder(VisitPlanDetailsActivity.this)
                                .setTitle("Success")
                                .setMessage("Visit Plan Deleted Successfully")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                      onBackPressed();
                                    }
                                }).setCancelable(false).show();


                    }
                }

                @Override
                public void onFailure(Call<ResultInfo> call, Throwable t) {
                    if (t instanceof SocketTimeoutException) {
                        SnackBarManagement._error_CustomMessage(binding.masterLayout,t.getMessage());
                    } else {
                        SnackBarManagement._error_CustomMessage(binding.masterLayout,t.getMessage());
                    }
                }
            });

        } catch (Exception ex) {
            String str = ex.toString();
            Log.e("Exception", str);
            SnackBarManagement._warning_CustomMessage(binding.masterLayout,str);
        }
        //  rv_doctors.removeItemDecorationAt(pos);
        //adapter.notifyDataSetChanged();

        //presenter.saveVisitPlanPerdayByEmpId();

    }

    @Override
    public void deleteItem(int pos) {

    }

    @Override
    public void editItem(int pos, int id, int rid, int aid, int tid, int stid, int mid, String region, String area, String territory, String subTerritory, String market) {

    }



    @Override
    public void OnArreangList(List<MonthDate> aMondateList,boolean entr, List<VisitplanModel> aTpLIst) {

    }

    @Override
    public void OnFailour(String msg) {

    }

    @Override
    public void OnSuccessVPPDay(String msg) {
        if (msg.equals("VisitPlanSave")) {
            new androidx.appcompat.app.AlertDialog.Builder(VisitPlanDetailsActivity.this)
                    .setTitle("Success")
                    .setMessage("Doctor Visit Plan Added Successfully")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        /*    Intent in = new Intent(VisitPlanDetailsActivity.this, DoctorVisitPlanAC.class);
                            in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            in.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                            startActivity(in);*/
                            onBackPressed();
                            // finish();
                          /*  Intent mIntent = getIntent();
                            finish();
                            startActivity(mIntent);*/
                        }

                    }).setCancelable(false).show();
        }
    }

  /*  @Override
    public void onBackPressed() {
        super.onBackPressed();
  *//*     Intent in = new Intent(VisitPlanDetailsActivity.this, VisitPlanActivity.class);
                            in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            in.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                            startActivity(in);*//*
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
       // finish();
    }*/

    public void SetinView(List<VisitPlanMaster> aList){
        VisitPlanMaster tpMaster = new VisitPlanMaster();
        if(aList!=null){
            try {
                tpMaster = aList.get(0);
                String sd = "Ds";
                if(tpMaster!=null){
                    if(tpMaster.isFinalSubmit()==false){
                       // binding.statusTxt.setText("Final Submit Pending");
                        //binding.lnhide.setVisibility(View.VISIBLE);
                    }else{
                        if(tpMaster.getApprovalStatus().equals("Pending")){
                            /*binding.statusTxt.setText("Final Submit Done - Approval Pending");
                            binding.lnhide.setVisibility(View.GONE);*/

                            binding.fabAdd.setVisibility(View.GONE);
                            binding.btnDone.setVisibility(View.GONE);
                        }
                        if(tpMaster.getApprovalStatus().equals("Approved")){
                            /*binding.statusTxt.setText("Visit Plan Approved");
                            binding.lnhide.setVisibility(View.GONE);*/
                            binding.fabAdd.setVisibility(View.GONE);
                            binding.btnDone.setVisibility(View.GONE);
                        }
                        if(tpMaster.getApprovalStatus().equals("Rejected")){
                            binding.fabAdd.setVisibility(View.VISIBLE);
                            binding.btnDone.setVisibility(View.VISIBLE);
                      /*      if(tpMaster.getApprovalRemarks() !=null){
                                binding.statusTxtRejected.setText(tpMaster.getApprovalRemarks());

                            }
                            binding.lnhide.setVisibility(View.VISIBLE);*/
                        }
                    }


                }
            }catch (Exception exception){
                exception.printStackTrace();
            }

        }

    }
    public void GetTourPlanMasterData(int month,int year,int empId){
        try{
            Toast.makeText(this, "month "+month, Toast.LENGTH_SHORT).show();
            Toast.makeText(this, "year "+year, Toast.LENGTH_SHORT).show();
            Toast.makeText(this, "empId "+empId, Toast.LENGTH_SHORT).show();
            ApiDoctorCall service = RetrofitClientInstance.getRetrofitInstance().create(ApiDoctorCall.class);
            Call<List<VisitPlanMaster>> call = service.GetDoctorVisitPlanMaster(month,year,empId);
            HttpUrl ds = call.request().url();
            call.enqueue(new Callback<List<VisitPlanMaster>>() {
                @Override
                public void onResponse(Call<List<VisitPlanMaster>> call, Response<List<VisitPlanMaster>> response) {
                    SetinView(response.body());
                }
                @Override
                public void onFailure(Call<List<VisitPlanMaster>> call, Throwable t) {
                    if(t instanceof SocketTimeoutException){
                    }else{
                    }
                }
            });

        }catch (Exception ex){
        }

    }
}