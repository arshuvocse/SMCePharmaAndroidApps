package com.creatrix.salessolution.RecyclerAdapter;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.creatrix.salessolution.Activity.Approval.TourPlan.Model.TourPlanApprovalData;
import com.creatrix.salessolution.Interface.Rcv_TourPlanListener;
import com.creatrix.salessolution.Model.CustomerViewModel;
import com.creatrix.salessolution.Model.MarketViewModel;
import com.creatrix.salessolution.Model.MonthDate;
import com.creatrix.salessolution.Model.ResultInfo;
import com.creatrix.salessolution.Model.ShiftViewModel;
import com.creatrix.salessolution.Model.SubMarket;
import com.creatrix.salessolution.Model.TerritoryViewModel;
import com.creatrix.salessolution.Model.TourPlanPostModel;
import com.creatrix.salessolution.Model.TourPurposeViewModel;
import com.creatrix.salessolution.Model.TourTypeViewModel;
import com.creatrix.salessolution.Network.RetrofitClientInstance;
import com.creatrix.salessolution.Network.TourApiCall;
import com.creatrix.salessolution.R;
import com.creatrix.salessolution.UtilityHelper.SessionManagement;

import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.List;

import okhttp3.HttpUrl;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class _monthdate_Adapter extends RecyclerView.Adapter<_monthdate_Adapter.BookViewHolder> /*implements ITourplan.View */{
    private Context context;
    private final static int FADE_DURATION = 500; //FADE_DURATION in milliseconds
    private List<MonthDate> aList;
    Rcv_TourPlanListener mListener;
    private TourPlanApprovalData tpdata;
    Activity activity;


    public _monthdate_Adapter(List<MonthDate> aLista,Rcv_TourPlanListener mListener)
    {
        this.aList = aLista;
        this.mListener = mListener;
    }
    public _monthdate_Adapter(TourPlanApprovalData tpdata,Activity activity, Rcv_TourPlanListener mListener)
    {
        this.tpdata = tpdata;
        this.activity = activity;
        this.mListener = mListener;
    }

    @Override
    public _monthdate_Adapter.BookViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.z_reclyview_month_days, parent, false);
               // .inflate(R.layout.doctor_visit_plan_day_item, parent, false);
        context = parent.getContext();
        return new _monthdate_Adapter.BookViewHolder(itemView);
    }

    public class BookViewHolder extends RecyclerView.ViewHolder {
        public TextView dateName,tv_day,tv_date;
        public TextView datevalue;
        public ImageView plusBtn;
        public RecyclerView recyclerView;
        public BookViewHolder(View view) {
            super(view);
            tv_day = (TextView) view.findViewById(R.id.tv_day);
            tv_date = (TextView) view.findViewById(R.id.tv_date);

            dateName = (TextView) view.findViewById(R.id.dateName);
            plusBtn =(ImageView) view.findViewById(R.id.plusBtn);
            recyclerView =(RecyclerView) view.findViewById(R.id.recycler_view_rc);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(_monthdate_Adapter.BookViewHolder holder, int position) {

      //  holder.dateName.setText(aList.get(position).getMonthName());
        holder.dateName.setText(tpdata.getaTourPlanMaster().getaTourPlanDate().get(position).getDayName());
        if(aList.get(position).isFinalSubmit()){
            holder.plusBtn.setVisibility(View.GONE);
        }
        holder.plusBtn.setOnClickListener(v -> {
            MonthDate ord;
            ord = aList.get(holder.getAdapterPosition());


            final Dialog dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(true);
            View popupView = LayoutInflater.from(context).inflate(R.layout.y_popup_turplandata, null);
            dialog.setContentView(popupView);
            dialog.show();
            dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

//                presenter = new TourPlanPresenter();


            TextView marketWiseClick = popupView.findViewById(R.id.marketWiseClick);
            TextView customerWiseClick = popupView.findViewById(R.id.customerWiseClick);
            TextView isMarketWise = popupView.findViewById(R.id.isMarketWise);
            TextView tourDate = popupView.findViewById(R.id.tourDate);
            tourDate.setText(aList.get(holder.getAdapterPosition()).getDateValue());

            TextView typeTxt = popupView.findViewById(R.id.typeTxt);
            LinearLayout customerSection = popupView.findViewById(R.id.customerSection);
            ImageView closeBtn = popupView.findViewById(R.id.closeBtn);


            Spinner territorySpinner=(Spinner)popupView.findViewById(R.id.territorySpinner);
            Spinner marketSpinner=(Spinner)popupView.findViewById(R.id.marketSpinner);

            Spinner submarketSpinner=(Spinner)popupView.findViewById(R.id.subMarketSpinner);

            Spinner shiftSpinner=(Spinner)popupView.findViewById(R.id.shiftSpinner);
            Spinner tourPlanTypeSpinner=(Spinner)popupView.findViewById(R.id.tourPlanTypeSpinner);
            Spinner tourPlanPurposeSpinner=(Spinner)popupView.findViewById(R.id.tourPlanPurposeSpinner);
            Spinner customerSpinner=(Spinner)popupView.findViewById(R.id.customerSpinner);
            SessionManagement session = new SessionManagement(context);
            HashMap<String, String> user = session.getUserDetails();
            int empId = Integer.parseInt(user.get(SessionManagement.KEY_EmpId));
            closeBtn.setOnClickListener(v1 -> dialog.cancel());


            customerWiseClick.setOnClickListener(v12 -> {
                marketWiseClick.setTextColor(context.getResources().getColor(R.color.black));
                customerWiseClick.setBackgroundColor(context.getResources().getColor(R.color.main));
                customerWiseClick.setTextColor(context.getResources().getColor(R.color.white));
                marketWiseClick.setBackground(context.getResources().getDrawable(R.drawable.cell_shape));
                typeTxt.setText("");
                typeTxt.setText("Add Customer Wise Tour Plan");

                customerSection.setVisibility(View.VISIBLE);

                isMarketWise.setText("");
                isMarketWise.setText("No");

                MarketViewModel  marketViewModel = (MarketViewModel) marketSpinner.getSelectedItem();
                try{
                    int id  = marketViewModel.getMarketId();
                    LoadCustomer(id,customerSpinner);
                }catch (Exception ex){
                    Log.e("TAG", "onClick: ",ex);
                }



            });

            marketWiseClick.setOnClickListener(v13 -> {
                marketWiseClick.setBackgroundColor(context.getResources().getColor(R.color.main));
                marketWiseClick.setTextColor(context.getResources().getColor(R.color.white));
                customerWiseClick.setBackground(context.getResources().getDrawable(R.drawable.cell_shape));
                customerWiseClick.setTextColor(context.getResources().getColor(R.color.black));
                typeTxt.setText("");
                typeTxt.setText("Add Market Wise Tour Plan");

                customerSection.setVisibility(View.GONE);

                isMarketWise.setText("");
                isMarketWise.setText("Yes");

            });

            LoadTerritory(empId,territorySpinner);
            LoadShift(empId,shiftSpinner);
            LoadTourTye(empId,tourPlanTypeSpinner);
            LoadTourPurpose(empId,tourPlanPurposeSpinner);
            territorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position1, long id) {
                    TerritoryViewModel trm = (TerritoryViewModel)parent.getSelectedItem();
                    int terrritoryId = trm.getTerritoryId();
                    LoadMarket(terrritoryId,marketSpinner);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });


            marketSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position1, long id) {
                    MarketViewModel trm = (MarketViewModel)parent.getSelectedItem();
                    int marketId = trm.getMarketId();
//                        LoadSubMarket(marketId,submarketSpinner);
                    String isMarketWiseTxt = isMarketWise.getText().toString();
                    if(isMarketWise.equals("No")){
                        LoadCustomer(marketId,customerSpinner);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });


            Button psubmitBnt = popupView.findViewById(R.id.psubmitBnt);
            psubmitBnt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try{
                        TourPlanPostModel tpPost = new TourPlanPostModel();
                        String strTxt = isMarketWise.getText().toString();
                        if(strTxt.equals("Yes")){
                            tpPost.setMarketWise(true);


                        }else{
                            tpPost.setMarketWise(false);
                            CustomerViewModel cuvm = (CustomerViewModel)customerSpinner.getSelectedItem();
                            tpPost.setCustomerMasterId(cuvm.getCustomerMasterId());
                        }

                        TerritoryViewModel tvm = (TerritoryViewModel)territorySpinner.getSelectedItem();
                        tpPost.setTerritoryId(tvm.getTerritoryId());

                        MarketViewModel mvm = (MarketViewModel)marketSpinner.getSelectedItem();
                        tpPost.setMarketId(mvm.getMarketId());

//                          SubMarket subM = (SubMarket)submarketSpinner.getSelectedItem();
                        tpPost.setSMId(0);

                        ShiftViewModel sif = (ShiftViewModel)shiftSpinner.getSelectedItem();
                        tpPost.setShiftId(sif.getShiftId());

                        TourTypeViewModel tpvm = (TourTypeViewModel)tourPlanTypeSpinner.getSelectedItem();
                        tpPost.setTourTypeId(tpvm.getTourTypeId());

                        TourPurposeViewModel tpur = (TourPurposeViewModel) tourPlanPurposeSpinner.getSelectedItem();
                        tpPost.setTPId(tpur.getTPId());

                        EditText remarksTxt = popupView.findViewById(R.id.remarksTxt);
                        tpPost.setComment(remarksTxt.getText().toString());
                        tpPost.setEmpInfoId(empId);
                        tpPost.setTourPlanDate(tourDate.getText().toString());


                        //Saving  Func

                        ProgressDialog   progressDoalog = new ProgressDialog(context);
                        progressDoalog.setMessage("Tour Plan is Saving.... Please wait");
                        progressDoalog.show();
                        progressDoalog.setCanceledOnTouchOutside(false);
                        try{
                            TourApiCall service = RetrofitClientInstance.getRetrofitInstance().create(TourApiCall.class);
                            Call<ResultInfo> call = service.SaveTourPlan(tpPost);
                            call.enqueue(new Callback<ResultInfo>() {
                                @Override
                                public void onResponse(@NonNull Call<ResultInfo> call, @NonNull Response<ResultInfo> response) {
                                    progressDoalog.dismiss();
                                    ResultInfo info =response.body();
                                    if(info.getSuccess() == true){

                                        new androidx.appcompat.app.AlertDialog.Builder(context)
                                                .setTitle("Success")
                                                .setMessage("Tour Plan Added Successfully")
                                                .setPositiveButton("OK", (dialog1, which) -> {
                                                    dialog1.cancel();
                                                    mListener.ReloadCurrentActivity();
                                                }).setCancelable(false).show();


                                    }
                                }
                                @Override
                                public void onFailure(@NonNull Call<ResultInfo> call, @NonNull Throwable t) {
                                    progressDoalog.dismiss();
                                    if(t instanceof SocketTimeoutException){
                                        ShowErrorMsg();
                                    }else{
                                        ShowErrorMsg();
                                    }
                                }
                            });

                        }catch (Exception ex){
                            progressDoalog.dismiss();
                            String str = ex.toString();
                            Log.e("Exception",str);
                            ShowErrorMsg();
                        }
                    }catch (Exception ex){
                        Toast.makeText(context, "Some thing went wrong. Please reload and try again", Toast.LENGTH_SHORT).show();
                        ex.printStackTrace();
                    }
                }
            });



        });
        //nested recycler
        if(aList.get(position).getaTpViewList().size()>0){
            _TourInfoData_List  dpter;
            dpter = new _TourInfoData_List(aList.get(position).getaTpViewList(),context,mListener);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
            holder.recyclerView.setLayoutManager(mLayoutManager);
            holder.recyclerView.setItemAnimator(new DefaultItemAnimator());
            holder.recyclerView.addItemDecoration(new DividerItemDecoration(context,
                    DividerItemDecoration.VERTICAL));
            holder.recyclerView.setAdapter(dpter);
            dpter.notifyDataSetChanged();
        }


        setFadeAnimation(holder.itemView);
    }

    public void ShowErrorMsg(){
        new androidx.appcompat.app.AlertDialog.Builder(context)
                .setTitle("Error")
                .setMessage("Some Error Occurred. Please Try Again")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();

                    }

                }).setCancelable(false).show();
    }



    @Override
    public int getItemCount() {
        return aList.size();
    }




    private void setFadeAnimation(View view) {
        AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(FADE_DURATION);
        view.startAnimation(anim);
    }

    // Data laod

    public void LoadTerritory(int empId,Spinner territorySpinner){

        try{
            TourApiCall service = RetrofitClientInstance.getRetrofitInstance().create(TourApiCall.class);
            Call<List<TerritoryViewModel>> call = service.GetTerritoryByEmpId(empId);
            HttpUrl ds = call.request().url();
            call.enqueue(new Callback<List<TerritoryViewModel>>() {
                @Override
                public void onResponse(Call<List<TerritoryViewModel>> call, Response<List<TerritoryViewModel>> response) {
                    SetTerritoryData(response.body(),territorySpinner);
                }
                @Override
                public void onFailure(Call<List<TerritoryViewModel>> call, Throwable t) {
                    if(t instanceof SocketTimeoutException){
                    }else{
                    }
                }
            });

        }catch (Exception ex){
        }

    }

    public void LoadMarket(int territoryId,Spinner setSpiner){

        try{
            TourApiCall service = RetrofitClientInstance.getRetrofitInstance().create(TourApiCall.class);
            Call<List<MarketViewModel>> call = service.GetMarketByTerritoryId(territoryId);
            call.enqueue(new Callback<List<MarketViewModel>>() {
                @Override
                public void onResponse(Call<List<MarketViewModel>> call, Response<List<MarketViewModel>> response) {
                    SetMarketData(response.body(),setSpiner);
                }
                @Override
                public void onFailure(Call<List<MarketViewModel>> call, Throwable t) {
                    if(t instanceof SocketTimeoutException){
                    }else{
                    }
                }
            });

        }catch (Exception ex){
        }

    }

    public void LoadSubMarket(int marketId,Spinner setSpiner){

        try{
            TourApiCall service = RetrofitClientInstance.getRetrofitInstance().create(TourApiCall.class);
            Call<List<SubMarket>> call = service.GetSubMarketByMarket(marketId);
            call.enqueue(new Callback<List<SubMarket>>() {
                @Override
                public void onResponse(@NonNull Call<List<SubMarket>> call, @NonNull Response<List<SubMarket>> response) {
                    SetSubMarketData(response.body(),setSpiner);
                }
                @Override
                public void onFailure(@NonNull Call<List<SubMarket>> call, @NonNull Throwable t) {
                    if(t instanceof SocketTimeoutException){
                    }else{
                    }
                }
            });

        }catch (Exception ex){
        }

    }

    public void LoadShift(int empId,Spinner setSpiner){

        try{
            TourApiCall service = RetrofitClientInstance.getRetrofitInstance().create(TourApiCall.class);
            Call<List<ShiftViewModel>> call = service.GetShift(empId);
            call.enqueue(new Callback<List<ShiftViewModel>>() {
                @Override
                public void onResponse(Call<List<ShiftViewModel>> call, Response<List<ShiftViewModel>> response) {
                    SetShiftData(response.body(),setSpiner);
                }
                @Override
                public void onFailure(Call<List<ShiftViewModel>> call, Throwable t) {
                    if(t instanceof SocketTimeoutException){
                    }else{
                    }
                }
            });

        }catch (Exception ex){
        }

    }

    public void LoadTourTye(int empId,Spinner setSpiner){

        try{
            TourApiCall service = RetrofitClientInstance.getRetrofitInstance().create(TourApiCall.class);
            Call<List<TourTypeViewModel>> call = service.GetType(empId);
            call.enqueue(new Callback<List<TourTypeViewModel>>() {
                @Override
                public void onResponse(@NonNull Call<List<TourTypeViewModel>> call, @NonNull Response<List<TourTypeViewModel>> response) {
                    SetTourPlanTypeData(response.body(),setSpiner);
                }
                @Override
                public void onFailure(@NonNull Call<List<TourTypeViewModel>> call, @NonNull Throwable t) {
                    if(t instanceof SocketTimeoutException){
                    }else{
                    }
                }
            });

        }catch (Exception ex){
        }

    }
    public void LoadTourPurpose(int empId,Spinner setSpiner){

        try{
            TourApiCall service = RetrofitClientInstance.getRetrofitInstance().create(TourApiCall.class);
            Call<List<TourPurposeViewModel>> call = service.GetPurpose(empId);
            call.enqueue(new Callback<List<TourPurposeViewModel>>() {
                @Override
                public void onResponse(@NonNull Call<List<TourPurposeViewModel>> call, @NonNull Response<List<TourPurposeViewModel>> response) {
                    SetTourPurposeData(response.body(),setSpiner);
                }
                @Override
                public void onFailure(@NonNull Call<List<TourPurposeViewModel>> call, @NonNull Throwable t) {
                    if(t instanceof SocketTimeoutException){
                    }else{
                    }
                }
            });

        }catch (Exception ex){
        }

    }

    public void LoadCustomer(int marketId,Spinner setSpiner){

        try{
            TourApiCall service = RetrofitClientInstance.getRetrofitInstance().create(TourApiCall.class);
            Call<List<CustomerViewModel>> call = service.GetCustomerByMarket(marketId);
            call.enqueue(new Callback<List<CustomerViewModel>>() {
                @Override
                public void onResponse(@NonNull Call<List<CustomerViewModel>> call, @NonNull Response<List<CustomerViewModel>> response) {
                    SetCustomerData(response.body(),setSpiner);
                }
                @Override
                public void onFailure(@NonNull Call<List<CustomerViewModel>> call, @NonNull Throwable t) {
                    if(t instanceof SocketTimeoutException){
                    }else{
                    }
                }
            });

        }catch (Exception ex){
        }

    }


    // Data Set

    public void SetTerritoryData(List<TerritoryViewModel> aList,Spinner territorySpinner){
        ArrayAdapter<TerritoryViewModel> dataAdapter = new ArrayAdapter<TerritoryViewModel>(context, android.R.layout.simple_spinner_item, aList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        territorySpinner.setAdapter(dataAdapter);

    }

    public void SetMarketData(List<MarketViewModel> aList, Spinner selectSpiner){
        ArrayAdapter<MarketViewModel> dataAdapter = new ArrayAdapter<MarketViewModel>(context, android.R.layout.simple_spinner_item, aList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selectSpiner.setAdapter(dataAdapter);

    }

    public void SetSubMarketData(List<SubMarket> aList, Spinner selectSpiner){
        ArrayAdapter<SubMarket> dataAdapter = new ArrayAdapter<SubMarket>(context, android.R.layout.simple_spinner_item, aList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selectSpiner.setAdapter(dataAdapter);

    }

    public void SetShiftData(List<ShiftViewModel> aList, Spinner selectSpiner){
        ArrayAdapter<ShiftViewModel> dataAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, aList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selectSpiner.setAdapter(dataAdapter);

    }

    public void SetTourPlanTypeData(List<TourTypeViewModel> aList, Spinner selectSpiner){
        ArrayAdapter<TourTypeViewModel> dataAdapter = new ArrayAdapter<TourTypeViewModel>(context, android.R.layout.simple_spinner_item, aList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selectSpiner.setAdapter(dataAdapter);

    }

    public void SetTourPurposeData(List<TourPurposeViewModel> aList, Spinner selectSpiner){
        ArrayAdapter<TourPurposeViewModel> dataAdapter = new ArrayAdapter<TourPurposeViewModel>(context, android.R.layout.simple_spinner_item, aList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selectSpiner.setAdapter(dataAdapter);

    }


    public void SetCustomerData(List<CustomerViewModel> aList, Spinner selectSpiner){
        ArrayAdapter<CustomerViewModel> dataAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, aList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selectSpiner.setAdapter(dataAdapter);

    }

}


