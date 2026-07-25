package com.creatrix.salessolution.Activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.datastore.preferences.protobuf.Empty;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.creatrix.salessolution.Activity.Approval.Order.OrderApprovalData;
import com.creatrix.salessolution.Activity.Approval.Order.OrderApprovalListActivity;
import com.creatrix.salessolution.Activity.Approval.Order.OrderMasterDAO;
import com.creatrix.salessolution.Activity.OrderProcess.Adapter.CampaignPopAdapter;
import com.creatrix.salessolution.Activity.OrderProcess.Adapter.MultiOrderAdapter;
import com.creatrix.salessolution.Activity.OrderProcess.Model.CampOrderDetails;
import com.creatrix.salessolution.Activity.OrderProcess.Model.CampaignCalModel;
import com.creatrix.salessolution.Activity.OrderProcess.Model.CampaignGetReq;
import com.creatrix.salessolution.Activity.OrderProcess.Model.CampaignMaster2;
import com.creatrix.salessolution.Activity.OrderProcess.Model.CampaignMasters;
import com.creatrix.salessolution.Activity.OrderProcess.Model.CampaignModel;
import com.creatrix.salessolution.Activity.OrderProcess.Model.CampaignPostReq;
import com.creatrix.salessolution.Activity.OrderProcess.Model.CheckedCampaignListener;
import com.creatrix.salessolution.Activity.OrderProcess.Adapter.DepoStockAdapter;
import com.creatrix.salessolution.Activity.OrderProcess.Model.DepoStockModel;
import com.creatrix.salessolution.Activity.OrderProcess.Model.OrderMasterModel;
import com.creatrix.salessolution.DBAdapter.DBCrudHelper;
import com.creatrix.salessolution.DBAdapter.ProductSQLiteHelper;
import com.creatrix.salessolution.Interface.IOrderManagement;
import com.creatrix.salessolution.Interface.IProduct;
import com.creatrix.salessolution.Interface.RecyclerViewActionListener;
import com.creatrix.salessolution.Model.CampaignMasterNew;
import com.creatrix.salessolution.Model.CordinateUpdate;
import com.creatrix.salessolution.Model.Customer;
import com.creatrix.salessolution.Model.OrderMaster;
import com.creatrix.salessolution.Model.Product;
import com.creatrix.salessolution.Model.ProductSample;
import com.creatrix.salessolution.Model.mCompanyUnit;
import com.creatrix.salessolution.Network.OrderProcessAPICALL;
import com.creatrix.salessolution.Network.RetrofitClientInstance;
import com.creatrix.salessolution.Network.RetrofitClientOrderProcessInstance;
import com.creatrix.salessolution.Network.apiSeedDataCall;
import com.creatrix.salessolution.Presenter.OrderManagementPresenter;
import com.creatrix.salessolution.Presenter.ProductPresenter;
import com.creatrix.salessolution.R;
import com.creatrix.salessolution.RecyclerAdapter._ordersummary_Recyler;
import com.creatrix.salessolution.RecyclerAdapter._product_orderpage_adapter;
import com.creatrix.salessolution.Services.Constants;
import com.creatrix.salessolution.UtilityHelper.LocationGet;
import com.creatrix.salessolution.UtilityHelper.NetworkInformation;
import com.creatrix.salessolution.UtilityHelper.SessionManagement;
import com.creatrix.salessolution.UtilityHelper.SnackBarManagement;
import com.creatrix.salessolution.UtilityHelper.ToastManagment;
import com.creatrix.salessolution.UtilityHelper.UtilityHelper;
import com.creatrix.salessolution.databinding.ActivityOrderMainBinding;
import com.creatrix.salessolution.databinding.PopMultiproductBinding;
import com.creatrix.salessolution.databinding.PopStockBinding;
import com.google.errorprone.annotations.Var;
import com.google.gson.Gson;

import java.net.SocketTimeoutException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import okhttp3.HttpUrl;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.creatrix.salessolution.Activity.Attendance.AttendanceActivity.MY_PERMISSIONS_REQUEST_LOCATION;


public class OrderMainActivity extends AppCompatActivity implements LocationListener, CheckedCampaignListener, IProduct.View, RecyclerViewActionListener, IOrderManagement.View {
    ActivityOrderMainBinding viewBinding;
    PopStockBinding psb;
    PopMultiproductBinding pmpb;
    IProduct.Presenter presenter;
    private _product_orderpage_adapter mAdapter;
    // private RecyclerView recyclerView;
    IOrderManagement.Presenter orderPresenter;
    SessionManagement session;
    View aMasterlayout;
    private static DecimalFormat df2 = new DecimalFormat("#.##");
    String orderTYpe;
    DecimalFormat f = new DecimalFormat("##.00"); // this is for 2 decimal place
    private List<Product> productListOrder = new ArrayList<>();
    private List<Product> productListCampSummary = new ArrayList<>();
    private List<Product> productListCampShowList;
    private List<mCompanyUnit> comUnitList = new ArrayList<mCompanyUnit>();
    DBCrudHelper crudHelper;
    ProductSQLiteHelper pHelper;

    Context defaultContext = OrderMainActivity.this;
    ArrayList<Product> productArrayList;

    List<CampaignModel> cmpaignList;
    List<CampaignGetReq> cmpaignReq = new ArrayList<>();
    CampaignGetReq ca;
    CampaignPostReq cmpaignPReq = new CampaignPostReq();

    List<CampaignMasters> cmpList = new ArrayList<>();
    List<CampaignMaster2> cmp2List = new ArrayList<>();

    CampaignMasters cm;
    CampaignPopAdapter adapter;

    List<CampaignCalModel> cmpaignCalList;
    List<CampOrderDetails> codList;
    _ordersummary_Recyler osAdapter;
    CampaignModel campSelected;
    boolean isSetCampaignAdd = false;

    //Depo Stock
    String CustomerMasterId;
    int campId, CustomerId, bonousId;
    int custId = 0;
    String name = "";
    int pid, c2mastid;
    boolean isSub = false;
    int orderLocalId;
    int cust_reqid, cust_reqpid, cust_reqqty;
    double cust_requp;
    int oid;
    Customer aInfoData;
    String streetAddress = "";
    Double lat, lon;
    LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewBinding = ActivityOrderMainBinding.inflate(getLayoutInflater());
        View mainView = viewBinding.getRoot();
        setContentView(mainView);
        pHelper = new ProductSQLiteHelper(OrderMainActivity.this);
        presenter = new ProductPresenter(this, this);
        orderPresenter = new OrderManagementPresenter(this, this);
        crudHelper = new DBCrudHelper(this);
        if (ContextCompat.checkSelfPermission(OrderMainActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(OrderMainActivity.this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(OrderMainActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
        }
        locationManager = (LocationManager) OrderMainActivity.this.getSystemService(Context.LOCATION_SERVICE);
        locationEnabled();
        getCLocation();
        if (!NetworkInformation.isConnected(this)) {
            checkLocationPermission();
        } else {
            //  SnackBarManagement._warning_CustomMessage(viewBinding.masterLayoutId,"Check Your Internet!!");
        }
        session = new SessionManagement(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        String userName = user.get(SessionManagement.KEY_LoginName);
        String empId = user.get(SessionManagement.KEY_EmpId);
        String extra = user.get(SessionManagement.Extra);

        Gson gson = new Gson();
        //TODO:Need Customer information for edit approval
        aInfoData = gson.fromJson(getIntent().getStringExtra("myjson"), Customer.class);
        setCustomerText(aInfoData);
        CustomerMasterId = String.valueOf(aInfoData.getCustomerMasterId());
        CustomerId = aInfoData.getCustomerMasterId();
        //Check where it comes from
        switch (Constants.WHO) {
            case "OrderMainAdapter":
                productListOrder.clear();
                orderTYpe = getIntent().getStringExtra("OrderType");
                viewBinding.btnDraft.setVisibility(View.VISIBLE);
                Constants.From = "MainOrder";
                String today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
                viewBinding.txtDeliveryDate.setText(today);
                viewBinding.btnDraft.setOnClickListener(v -> OrderDraft(aInfoData, userName, empId, extra));
                viewBinding.btnSubmit.setOnClickListener(v -> OrderSubmit(aInfoData, userName, empId, "SubmitOrder", productListOrder));
                break;
            case "DraftOrderAdapter":
                Constants.From = "DraftOrder";
                OrderMaster orderProduct = gson.fromJson(getIntent().getStringExtra("orderDetails"), OrderMaster.class);
                orderLocalId = orderProduct.getOrderIdLocal();
                viewBinding.txtDeliveryDate.setText(orderProduct.getDeliveryDate());
                viewBinding.remarksTxt.setText(orderProduct.getRemarks());
               String PaymentType=orderProduct.getPaymentType();

                if (PaymentType.equals("COD")) {
                    viewBinding.radioCod.setChecked(true);
                    viewBinding.divPaymentDate.setVisibility(View.GONE);
                } else if (PaymentType.equals("NCOD")) {
                    viewBinding.radioNCOD.setChecked(true);
                    viewBinding.divPaymentDate.setVisibility(View.GONE);
                }


                viewBinding.divPaymentDate.setVisibility(View.VISIBLE);

                productListOrder.clear();
                int i;
                for (i = 0; i < orderProduct.getOrderDetails().size(); i++) {
                    Product pr = new Product();
                    pr = orderProduct.getOrderDetails().get(i);
                    int pids = pr.getProductId();
                    String pnames = pr.getProductName();
                    Double up = pr.getUnitPrice();
                    Double vp = pr.getVatPercentage();
                    int qty = pr.getQuantity();
                    String pcode = pr.getProductCode();
                    Product aProduct = new Product(pids, pnames, up, vp, qty, pcode);
                    cust_reqid = CustomerId;
                    cust_reqpid = pids;
                    cust_reqqty = qty;
                    cust_requp = up;
                    campaignReqSetup(cust_reqid, cust_reqpid, cust_reqqty, cust_requp);
                    productListOrder.add(updateProductTotals(aProduct));
                    setTotals();
                }
                // viewBinding.btnDraft.setVisibility(View.GONE);
                viewBinding.btnDraft.setOnClickListener(v -> OrderDraft(aInfoData, userName, empId, extra));
                viewBinding.btnSubmit.setOnClickListener(v -> OrderSubmit(aInfoData, userName, empId, "DraftOrderAdapter", productListOrder));
                break;

            case "OrderApproveViewActivity":
                viewBinding.toolbarTitle.setText("Update Order");
                viewBinding.btnSubmit.setVisibility(View.GONE);
                viewBinding.btnDraft.setVisibility(View.GONE);
                viewBinding.btnUpdate.setVisibility(View.VISIBLE);
                //Constants.From = "ApprovalOrder";
                OrderApprovalData orderData = gson.fromJson(getIntent().getStringExtra("OrderApprovalEdit"), OrderApprovalData.class);
                OrderMasterDAO oMdao = orderData.getaOrderMasterDAO();
                orderLocalId = orderData.getaOrderMasterDAO().getOrderId();
                productListOrder.clear();
                int oa;
                for (oa = 0; oa < oMdao.getaOrderDtls().size(); oa++) {
                    Product pr = new Product();
                    // pr = orderProduct.getOrderDetails().get(i);
                    int pids = oMdao.getaOrderDtls().get(oa).getProductId();
                    String pnames = oMdao.getaOrderDtls().get(oa).getProductName();// pr.getProductName();
                    Double up = oMdao.getaOrderDtls().get(oa).getUnitPrice();//pr.getUnitPrice();
                    Double vp = oMdao.getaOrderDtls().get(oa).getTotalVatAmount();//pr.getVatPercentage();
                    int qty = oMdao.getaOrderDtls().get(oa).getQuantity();//pr.getQuantity();
                    String pcode = oMdao.getaOrderDtls().get(oa).getProductCode();//pr.getProductCode();
                    Product aProduct = new Product(pids, pnames, up, vp, qty, pcode);
                    cust_reqid = CustomerId;
                    cust_reqpid = pids;
                    cust_reqqty = qty;
                    cust_requp = up;
                    campaignReqSetup(cust_reqid, cust_reqpid, cust_reqqty, cust_requp);
                    productListOrder.add(updateProductTotals(aProduct));
                    setTotals();
                }
               // viewBinding.btnDraft.setVisibility(View.GONE);
                // viewBinding.btnSubmit.setOnClickListener(v -> OrderSubmit(aInfoData, userName, empId, "ApprovalOrder", productListOrder));
                //  viewBinding.btnUpdate.setOnClickListener(v -> OrderSubmit(aInfoData, userName, empId, "ApprovalOrder", productListOrder));
                viewBinding.btnUpdate.setOnClickListener(v -> OrderUpdate(orderData, userName, empId, "ApprovalOrder", productListOrder));

                break;
        }
        if (!NetworkInformation.isConnected(OrderMainActivity.this)) {
            SnackBarManagement._warning_CustomMessage(viewBinding.masterLayoutId, "You are not connected to internet\nYou Can't Submit");
            viewBinding.btnSubmit.setVisibility(View.GONE);
            viewBinding.btnSelectCampaign.setVisibility(View.GONE);
            presenter.getProductFromDB(Integer.parseInt(empId));
            //  return;
        }
        presenter.getProductFromDB(Integer.parseInt(empId));
        viewBinding.toolbarCustom.setNavigationOnClickListener(v -> finish());
        //  orderTYpe = getIntent().getStringExtra("OrderType");
        viewBinding.campaignclick.setOnClickListener(v -> {
            Intent in = new Intent(OrderMainActivity.this, CampainActivity.class);
            startActivity(in);
        });
        aMasterlayout = findViewById(R.id.masterLayoutId);
        // product Adapter for add order
        if (productListOrder != null) {
            //recyclerView = (RecyclerView) findViewById(R.id.recycler_view_product);
            mAdapter = new _product_orderpage_adapter(productListOrder, this, this, "MainOrder");
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            viewBinding.recyclerViewProduct.setLayoutManager(mLayoutManager);
            viewBinding.recyclerViewProduct.setItemAnimator(new DefaultItemAnimator());
            viewBinding.recyclerViewProduct.setAdapter(mAdapter);
            viewBinding.recyclerViewProduct.addItemDecoration(new DividerItemDecoration(getApplicationContext(),
                    DividerItemDecoration.VERTICAL));
            viewBinding.recyclerViewProduct.setItemAnimator(null);
            viewBinding.recyclerViewProduct.scrollToPosition(0);
            mAdapter.notifyDataSetChanged();
        }

        viewBinding.comSpinner.setTitle("Select Company Unit");
        viewBinding.comSpinner.setPositiveButton("OK");

        viewBinding.comSpinner.setAdapter(new ArrayAdapter<>(OrderMainActivity.this, android.R.layout.simple_spinner_dropdown_item, comUnitList));
        viewBinding.comSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                } else {
                    String str = parent.getItemAtPosition(position).toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        viewBinding.datePickerDeliveryDate.setOnClickListener(v -> UtilityHelper._datePickNum_DisableOldDates(viewBinding.txtDeliveryDate, OrderMainActivity.this));
        viewBinding.datePickerPaymentDate.setOnClickListener(v -> UtilityHelper._datePickNum_DisableOldDatesNextMonth(viewBinding.txtPaymentDate, OrderMainActivity.this));
        viewBinding.datePickerCollectionDate.setOnClickListener(v -> UtilityHelper._datePickNum_DisableOldDates(viewBinding.txtCollectionDate, OrderMainActivity.this));


        viewBinding.radioGroupPaymentType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                viewBinding.txtPaymentDate.setText("");
                switch (checkedId) {
                    case R.id.radioCod:
                        viewBinding.divPaymentDate.setVisibility(View.GONE);
                        break;
                    case R.id.radioNCOD:
                        viewBinding.divPaymentDate.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });

        //region Add,Submit,Draft region
        // ca = new CampaignGetReq();
        viewBinding.searchProducttxt.setOnItemClickListener((arg0, arg1, arg2, arg3) -> {
            // ca = new CampaignGetReq();
            Product selected = (Product) arg0.getAdapter().getItem(arg2);
            double id = selected.getUnitPrice();
            String Test = Double.toString(id);
            LayoutInflater li = LayoutInflater.from(OrderMainActivity.this);
            View promptsView = li.inflate(R.layout.quantity_modal, null);
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(OrderMainActivity.this);
            alertDialogBuilder.setView(promptsView);
            final EditText userInput = (EditText) promptsView
                    .findViewById(R.id.editTextDialogUserInput);
            userInput.requestFocus();
            // set dialog message
            alertDialogBuilder
                    .setCancelable(false)
                    .setPositiveButton("Done",
                            (dialog, id1) -> {
                                //isAdd=true;
                                Product sp = (Product) arg0.getAdapter().getItem(arg2);
                                String pQyu = userInput.getText().toString();
                                // pQyu = userInput.getText().toString();
                                int customerId = aInfoData.getCustomerMasterId();
                                if (pQyu.equals("")) {
                                    Toast.makeText(OrderMainActivity.this, "Please enter a valid amount", Toast.LENGTH_LONG).show();
                                } else {
                                    int quantity = Integer.parseInt(userInput.getText().toString());
                                    if (quantity == 0) {
                                        Toast.makeText(OrderMainActivity.this, "Please enter a valid amount", Toast.LENGTH_LONG).show();
                                    } else {
                                        // this is for with customer demo
                                        Double acPrice = 0.00;
                                        Double acVat = 0.00;

                                        double[] quotdPrice = crudHelper.GetQuotedPrice(customerId, sp.getProductId());
                                        if (quotdPrice[0] == 0.00) {
                                            acPrice = sp.getUnitPrice();
                                            acVat = sp.getVatPercentage();
                                        } else {
                                            acPrice = quotdPrice[0];
                                            acVat = sp.getVatPercentage();
                                        }
                                        boolean is_Exist = false;
                                        int proid = sp.getProductId();
                                        for (int i = 0; i < productListOrder.size(); i++) {
                                            int ProductId = productListOrder.get(i).getProductId();
                                            if (proid == ProductId) {
                                                    SnackBarManagement._warning_CustomMessage(viewBinding.masterLayoutId, "Already added this product");
                                                    is_Exist = true;
                                                break;
                                            }
                                        }
                                        if (is_Exist == false) {
                                            Product aProduct = new Product(sp.getProductId(), sp.getProductName(), acPrice, acVat, quantity, sp.getProductCode());
                                            productListOrder.add(updateProductTotals(aProduct));
                                        }
                                        setTotals();
                                        productListCampShowList = new ArrayList<>();
                                        productListCampShowList = productListOrder;
                                        if (cmpaignCalList != null) {
                                            try {
                                                cmpaignCalList.clear();
                                                osAdapter.notifyDataSetChanged();
                                                viewBinding.totalTPValueSummary.setText("0");
                                                viewBinding.totalVatValueSummary.setText("0");
                                                viewBinding.totalDiscountValueSummary.setText("0");
                                                viewBinding.totalValueSummary.setText("0");
                                            } catch (Exception exception) {
                                                exception.printStackTrace();
                                            }
                                        }
                                        mAdapter.notifyItemChanged(productListOrder.size());
                                        mAdapter.notifyDataSetChanged();
                                        viewBinding.searchProducttxt.setText("");
                                    }
                                }

                            })
                    .setNegativeButton("Cancel",
                            (dialog, id12) -> {
                                viewBinding.searchProducttxt.setText("");
                                dialog.cancel();
                            });

            // create alert dialog
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
            alertDialog.show();
        });
        //productListCampShowList
        //   viewBinding.btnSubmit.setOnClickListener(v -> OrderSubmit(aInfoData, userName, empId, extra, productListOrder));

        //endregion
        //TODO:Select Campaign Button Click
        viewBinding.btnSelectCampaign.setOnClickListener(v -> {
            if (!NetworkInformation.isConnected(OrderMainActivity.this)) {
                SnackBarManagement._warning_CustomMessage(viewBinding.masterLayoutId, "You are not connected to Internet!");
                return;
            }

            String _PaymnetType="" ;
            try
            {
                if (viewBinding.radioCod.isChecked()) {
                    _PaymnetType="COD";
                }
            }catch (Exception ex){

            }
            try
            {
                if (viewBinding.radioNCOD.isChecked()) {
                    _PaymnetType="NCOD";
                }  }catch (Exception ex){

            }

            if (_PaymnetType==""){
                SnackBarManagement._warning_CustomMessage(aMasterlayout, "Please Select Payment Type");
                return;
            }

            if (productListOrder != null) {
                List<Product> plist = new ArrayList<>();


                try {
                    for (Product product : productListOrder) {
                        if (product.getQuantity() <= 0) {

                            // Optionally, display an error message
                            System.out.println("Error: Quantity for product " + product.getProductName() + " cannot be 0 or less.");
                            SnackBarManagement._warning_CustomMessage(aMasterlayout, "Quantity for product " + product.getProductName() + " cannot be 0 or less.");
                            return;
                        }
                        plist.add(product); // Add the product to the list if valid
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            fun_SelectCampaignButtonClick();
        });
        viewBinding.updateCustomerLocation.setOnClickListener(view -> {
            int customerId = aInfoData.getCustomerMasterId();
            UpdateCustomerLocation(customerId, Integer.parseInt(empId));
        });
        viewBinding.popStock.setOnClickListener(v -> {
            psb = PopStockBinding.inflate(getLayoutInflater());
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
            ProgressDialog progressDoalog = new ProgressDialog(OrderMainActivity.this);
            progressDoalog.setMessage("loading.... Please wait");
            progressDoalog.show();
            progressDoalog.setCanceledOnTouchOutside(false);
            try {
                apiSeedDataCall service = RetrofitClientInstance.getRetrofitInstance().create(apiSeedDataCall.class);
                Call<List<DepoStockModel>> call = service.GetDepoStock(CustomerMasterId);
                HttpUrl ds = call.request().url();
                call.enqueue(new Callback<List<DepoStockModel>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<DepoStockModel>> call, @NonNull Response<List<DepoStockModel>> response) {
                        progressDoalog.dismiss();
                        if (response != null) {
                            LoadDepoStock(response.body(), psb.rvStock);
                        } else {
                            SnackBarManagement._warning_CustomMessage(psb.rlmaster, "No Product Found");
                        }

                    }

                    @Override
                    public void onFailure(@NonNull Call<List<DepoStockModel>> call, Throwable t) {

                        progressDoalog.dismiss();
                        if (t instanceof SocketTimeoutException) {
                            SnackBarManagement._warning_CustomMessage(viewBinding.masterLayoutId, "Some thing went wrong. Please try again");
                        } else {
                            SnackBarManagement._warning_CustomMessage(viewBinding.masterLayoutId, "Some thing went wrong. Please try again");

                        }
                    }
                });

            } catch (Exception ex) {
                progressDoalog.dismiss();
                SnackBarManagement._warning_CustomMessage(viewBinding.masterLayoutId, "Some thing went wrong. Please try again");
            //    ex.printStackTrace();
            }
            dialogBuilder.setView(psb.getRoot());
            AlertDialog alertDialog = dialogBuilder.create();
            alertDialog.show();
            //  psb.rvStock.setAdapter();
            psb.closeTxt.setOnClickListener(v1 -> {
                alertDialog.dismiss();
            });


        });
        viewBinding.popAddlst.setOnClickListener(view -> {
            pmpb = PopMultiproductBinding.inflate(getLayoutInflater());
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
            /*ProgressDialog progressDoalog = new ProgressDialog(OrderMainActivity.this);
            progressDoalog.setMessage("loading.... Please wait");
            progressDoalog.show();
            progressDoalog.setCanceledOnTouchOutside(false);*/
            /*try {
                apiSeedDataCall service = RetrofitClientInstance.getRetrofitInstance().create(apiSeedDataCall.class);
                Call<List<DepoStockModel>> call = service.GetDepoStock(CustomerMasterId);
                HttpUrl ds = call.request().url();
                call.enqueue(new Callback<List<DepoStockModel>>() {
                    @Override
                    public void onResponse(Call<List<DepoStockModel>> call, Response<List<DepoStockModel>> response) {
                        progressDoalog.dismiss();
                        if (response != null) {
                            LoadDepoStock(response.body(), psb.rvStock);
                        } else {
                            SnackBarManagement._warning_CustomMessage(psb.rlmaster, "No Product Found");
                        }

                    }

                    @Override
                    public void onFailure(Call<List<DepoStockModel>> call, Throwable t) {
                        progressDoalog.dismiss();
                        if (t instanceof SocketTimeoutException) {
                            SnackBarManagement._warning_CustomMessage(viewBinding.masterLayoutId, "Some thing went wrong. Please try again");
                        } else {
                            SnackBarManagement._warning_CustomMessage(viewBinding.masterLayoutId, "Some thing went wrong. Please try again");

                        }
                    }
                });

            } catch (Exception ex) {
                progressDoalog.dismiss();
                SnackBarManagement._warning_CustomMessage(viewBinding.masterLayoutId, "Some thing went wrong. Please try again");
                ex.printStackTrace();
            }*/
            dialogBuilder.setView(pmpb.getRoot());
            AlertDialog alertDialog = dialogBuilder.create();
            alertDialog.show();
            //final EditText userInput = (EditText) pmp.findViewById(R.id.editTextDialogUserInput);
            MultiOrderAdapter madapter = new MultiOrderAdapter(this,this,productArrayList);
            RecyclerView.LayoutManager rm=new LinearLayoutManager(this);
            pmpb.recyclerView.setLayoutManager(rm);
            pmpb.recyclerView.setAdapter(madapter);

            pmpb.btnCancel.setOnClickListener(view1 -> alertDialog.dismiss());
            pmpb.btnDone.setOnClickListener(view1 -> {
               // productArrayList = (ArrayList<Product>) aList;
                alertDialog.dismiss();
            });
             //  psb.rvStock.setAdapter();
            /* pmpb.closeTxt.setOnClickListener(v1 -> {
                alertDialog.dismiss();
            });*/
        });
    }

    private void fun_SelectCampaignButtonClick() {
        if (cmpaignCalList != null) {
            try {
                cmpaignCalList.clear();
                //  viewBinding.recyclerViewProductSummary.removeItemDecorationAt();
                osAdapter.notifyItemRangeRemoved(0, cmpaignCalList.size());
                //cmpaignCalList.clear();
                osAdapter.notifyDataSetChanged();
                viewBinding.totalTPValueSummary.setText("0");
                viewBinding.totalVatValueSummary.setText("0");
                viewBinding.totalDiscountValueSummary.setText("0");
                viewBinding.totalValueSummary.setText("0");
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
        int customerId = aInfoData.getCustomerMasterId();

        String _PaymnetType="" ;
        try
        {
            if (viewBinding.radioCod.isChecked()) {
                _PaymnetType="COD";
            }
        }catch (Exception ex){

        }
        try
        {
            if (viewBinding.radioNCOD.isChecked()) {
                _PaymnetType="NCOD";
            }  }catch (Exception ex){

        }
        if (cmpList != null) {
            try {
                cmpList.clear();
                adapter.notifyItemRangeRemoved(0, cmpList.size());
                adapter.notifyDataSetChanged();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
        cmpaignReq.clear();
        for (int i = 0; i < productListOrder.size(); i++) {

            CampaignGetReq ca = new CampaignGetReq();
            ca.setCustomerId(customerId);
            ca.setProductId(productListOrder.get(i).getProductId());
            ca.setQty(productListOrder.get(i).getQuantity());
            ca.setUnitPrice(productListOrder.get(i).getUnitPrice());
            ca.setTotalPrice(0);
            ca.setPaymentType(_PaymnetType);
            ca.setApplied(false);
            cmpaignReq.add(ca);
            //  getCampaign_PopupList.add(ca);m
        }


        SelectCampaign(customerId, cmpaignReq);
    }

    private void campaignReqSetup(int cust_reqid, int cust_reqpid, int cust_reqqty, double cust_requp) {
        // Toast.makeText(this, "camprq set" + String.valueOf(cust_reqqty), Toast.LENGTH_SHORT).show();
        ca = new CampaignGetReq();
        ca.setCustomerId(cust_reqid);
        ca.setProductId(cust_reqpid);
        ca.setQty(cust_reqqty);
        ca.setUnitPrice(cust_requp);
        ca.setTotalPrice(0);
        ca.setApplied(false);
        cmpaignReq.add(ca);
    }

    public CampaignGetReq updateCampaignRQ(CampaignGetReq ca) {
        // Toast.makeText(this, "camprq set" + String.valueOf(cust_reqqty), Toast.LENGTH_SHORT).show();
        ca = new CampaignGetReq();
        ca.setCustomerId(cust_reqid);
        ca.setProductId(cust_reqpid);
        ca.setQty(cust_reqqty);
        ca.setUnitPrice(cust_requp);
        ca.setTotalPrice(0);
        ca.setApplied(false);
        cmpaignReq.add(ca);
        return ca;
    }

    // Operation Process
    //submit order
    //submit order
    public void OrderSubmit(Customer aInfoData, String userName, String empId, String who, List<Product> productListOrder) {
        int comUnitId = 2;
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String formattedDate = df.format(c);
        String collectioDate = formattedDate;

        OrderMasterModel orderMain = new OrderMasterModel();
        orderMain.setOrderId(oid);
        orderMain.setEmpId(Integer.parseInt(empId));
        orderMain.setComUnitId(String.valueOf(comUnitId));
        orderMain.setOrderType(orderTYpe);
        String remarks = viewBinding.remarksTxt.getText().toString();
        orderMain.setRemarks(remarks);
        orderMain.setTpPercentage(0.0);
        orderMain.setCustomerCode(aInfoData.getCustomerCode());
        orderMain.setSubmittedDate(formattedDate);

        orderMain.setDeliveryDate(viewBinding.txtDeliveryDate.getText().toString().trim());


        String _PaymnetType="" ;
        try
        {
            if (viewBinding.radioCod.isChecked()) {
                _PaymnetType="COD";
            }
        }catch (Exception ex){

        }
        try
        {
            if (viewBinding.radioNCOD.isChecked()) {
                _PaymnetType="NCOD";
            }  }catch (Exception ex){

        }

        if (_PaymnetType==""){
            SnackBarManagement._warning_CustomMessage(aMasterlayout, "Please Select Payment Type");
            return;
        }

        if (_PaymnetType=="NCOD"){

            if(viewBinding.txtPaymentDate.getText()==""){
                SnackBarManagement._warning_CustomMessage(aMasterlayout, "Please Select Payment Date");
                return;
            }

        }
        orderMain.setPaymentDate(viewBinding.txtPaymentDate.getText().toString().trim());
        orderMain.setPaymentType(_PaymnetType);
        if (cmpList.size() > 0) {
            int i, c2mastid = 0;
            for (i = 0; i < cmpList.size(); i++) {
                CampaignMaster2 cam2 = new CampaignMaster2();
                cam2.setCampgainMasterId(cmpList.get(i).getCampgainMasterId());
                cmp2List.add(cam2);
            }
            orderMain.setCampaignMasters(cmp2List);
        } else {
            CampaignMaster2 cam2 = new CampaignMaster2();
            cam2.setCampgainMasterId(0);
            cmp2List = new ArrayList<>();
            cmp2List.add(cam2);
            orderMain.setCampaignMasters(cmp2List);
        }
        if (productListOrder != null) {
            List<Product> plist = null;
            try {
                plist = new ArrayList<>();
                int p;
                for (p = 0; p < productListOrder.size(); p++) {
                    Product pr = new Product();
                    pr.setProductId(productListOrder.get(p).getProductId());
                    pr.setProductName(productListOrder.get(p).getProductName());
                    pr.setQuantity(productListOrder.get(p).getQuantity());
                    pr.setUnitPrice(productListOrder.get(p).getUnitPrice());
                    pr.setTp(productListOrder.get(p).getTp());
                    pr.setVatAmountPerunit(productListOrder.get(p).getVatPercentage());
                    pr.setVatPercentage(productListOrder.get(p).getVatPercentage());

                    pr.setTotalVatAmount(productListOrder.get(p).getTotalVatAmount());
                    pr.setNetAmount(productListOrder.get(p).getNetAmount());
                    pr.setCampaignProduct(false);
                    pr.setDiscountPercentage(0.0);
                    pr.setDiscountValue(0.0);
                    pr.setCampaignMasterId(c2mastid);
                    pr.setCustomerId(aInfoData.getCustomerMasterId());
                    pr.setCustomerTypeId(productListOrder.get(p).getCustomerTypeId());
                    plist.add(pr);
                }
            } catch (Exception exception) {
            }
            //System.out.println(plist);
            orderMain.setOrderDetails(plist);

        }
        if (productListOrder.size() == 0) {
            SnackBarManagement._warning_CustomMessage(aMasterlayout, "Please add at lest one product to submit Order");
        } else {
            //TODO:Checking campaign set or not
            if (productOrder_IsZeroQuantity(productListOrder)) {
                SnackBarManagement._error_CustomMessage(aMasterlayout, "Product Quantity 0 is not Acceptable");
            } else {
                if (NetworkInformation.isConnected(defaultContext)) {
                    Gson gson = new Gson();
                    String model = gson.toJson(orderMain);
                    System.out.println("Make Order " + model);
                    orderPresenter.makeOrder2(orderMain, who);
                } else {
                    /// showing user to draft the order
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(OrderMainActivity.this);
                    builder1.setMessage("You Don't Internet Connection Now. Do you want to draft the order ?");
                    builder1.setCancelable(false);
                    builder1.setPositiveButton(
                            "Yes",
                            (dialog, id) -> {
                                dialog.cancel();
                            });
                    builder1.setNegativeButton(
                            "No",
                            (dialog, id) -> dialog.cancel());

                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                }


            }
        }
    }

    public void OrderUpdate(OrderApprovalData aInfoData, String userName, String empId, String who, List<Product> productListOrder) {
        int comUnitId = 2;
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String formattedDate = df.format(c);
        String collectioDate = formattedDate;

        OrderMasterModel orderMain = new OrderMasterModel();

        orderMain.setOrderId(aInfoData.getTableId());

        orderMain.setEmpId(Integer.parseInt(empId));
        orderMain.setComUnitId(String.valueOf(comUnitId));
        orderMain.setOrderType(orderTYpe);
        String remarks = viewBinding.remarksTxt.getText().toString();
        orderMain.setRemarks(remarks);
        orderMain.setTpPercentage(0.0);
        orderMain.setCustomerCode(aInfoData.getaCustMasterDAO().getCustomerCode());
        orderMain.setSubmittedDate(formattedDate);
        orderMain.setCollectionDate(collectioDate);
        orderMain.setDeliveryDate(viewBinding.txtDeliveryDate.getText().toString().trim());

        if (cmpList.size() > 0) {
            int i, c2mastid = 0;
            for (i = 0; i < cmpList.size(); i++) {
                CampaignMaster2 cam2 = new CampaignMaster2();
                cam2.setCampgainMasterId(cmpList.get(i).getCampgainMasterId());
                cmp2List.add(cam2);
            }
            orderMain.setCampaignMasters(cmp2List);
        } else {
            CampaignMaster2 cam2 = new CampaignMaster2();
            cam2.setCampgainMasterId(0);
            cmp2List = new ArrayList<>();
            cmp2List.add(cam2);
            orderMain.setCampaignMasters(cmp2List);
        }
        if (productListOrder != null) {
            List<Product> plist = null;
            try {
                plist = new ArrayList<>();
                int p;
                for (p = 0; p < productListOrder.size(); p++) {
                    Product pr = new Product();
                    pr.setProductId(productListOrder.get(p).getProductId());
                    pr.setProductName(productListOrder.get(p).getProductName());
                    pr.setQuantity(productListOrder.get(p).getQuantity());
                    pr.setUnitPrice(productListOrder.get(p).getUnitPrice());
                    pr.setTp(productListOrder.get(p).getTp());
                    pr.setVatAmountPerunit(productListOrder.get(p).getVatPercentage());
                    pr.setVatPercentage(productListOrder.get(p).getVatPercentage());

                    pr.setTotalVatAmount(productListOrder.get(p).getTotalVatAmount());
                    pr.setNetAmount(productListOrder.get(p).getNetAmount());
                    pr.setCampaignProduct(false);
                    pr.setDiscountPercentage(0.0);
                    pr.setDiscountValue(0.0);
                    pr.setCampaignMasterId(c2mastid);
                    pr.setCustomerId(aInfoData.getaCustMasterDAO().getCustomerMasterId());
                    pr.setCustomerTypeId(productListOrder.get(p).getCustomerTypeId());
                    plist.add(pr);
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            //System.out.println(plist);
            orderMain.setOrderDetails(plist);
        }
        if (productListOrder.size() == 0) {
            SnackBarManagement._warning_CustomMessage(aMasterlayout, "Please add atlest one product to submit Order");
        } else {
            //TODO:Checking campaign set or not
            if (productOrder_IsZeroQuantity(productListOrder)) {
                SnackBarManagement._error_CustomMessage(aMasterlayout, "Product Quantity 0 is not Acceptable");
            } else {
                if (NetworkInformation.isConnected(defaultContext)) {
                    orderPresenter.makeOrder2(orderMain, who);
                } else {
                    /// showing user to draft the order
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(OrderMainActivity.this);
                    builder1.setMessage("You Don't Internet Connection Now. Do you want to draft the order ?");
                    builder1.setCancelable(false);
                    builder1.setPositiveButton(
                            "Yes",
                            (dialog, id) -> {
                                dialog.cancel();
                            });
                    builder1.setNegativeButton(
                            "No",
                            (dialog, id) -> dialog.cancel());

                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                }


            }
        }
    }

    public void OrderSubmits(Customer aInfoData, String userName, String empId, String extra) {
    /*    int comUnitId = 2;
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        String formattedDate = df.format(c);
        String deliveryDate = formattedDate;
        String collectioDate = formattedDate;

        //selected campaign id
        CampaignMasters cm = new CampaignMasters();
        cm.setCampgainMasterId(campId);
        cmpList.add(cm);

        OrderMasterNew orderMaster = new OrderMasterNew();
        orderMaster.setCustomerCode(aInfoData.getCustomerCode());
        orderMaster.setSubmittedDate(formattedDate);
        orderMaster.setCollectionDate(collectioDate);
        if (productListCampSummary != null) {
            if (productListCampSummary.size() > 0) {
                orderMaster.setOrderDetails(productListCampSummary);
            } else {
                orderMaster.setOrderDetails(productListOrder);
            }
        } else {
            orderMaster.setOrderDetails(productListOrder);
        }

        if (cmpList != null) {
            if (cmpList.size() > 0) {
                orderMaster.setCampaignMasters(cmpList);

            } else {
                orderMaster.setCampaignMasters(cmpList);
            }
        } else {
            orderMaster.setOrderDetails(productListOrder);
        }
        orderMaster.setEmpId(Integer.parseInt(empId));
        orderMaster.setComunitId(comUnitId);
        orderMaster.setOrderType(orderTYpe);
        String remarks = viewBinding.remarksTxt.getText().toString();
        orderMaster.setRemarks(remarks);
        orderMaster.setTpPercentage(discountPercentageTP);
        if (productListOrder.size() == 0) {
            SnackBarManagement._warning_CustomMessage(aMasterlayout, "Please add atlest one product to submit Order");
        } else {
            //TODO:Checking campaign set or not
            if (productOrder_IsZeroQuantity(productListOrder)) {
                SnackBarManagement._error_CustomMessage(aMasterlayout, "Product Quantity 0 is not Acceptable");
            } else {
                if (NetworkInformation.isConnected(defaultContext)) {
                   viewBinding.btnSubmit.setVisibility(View.GONE);
                    orderPresenter.makeOrder2(orderMaster);
                } else {
                    /// showing user to draft the order
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(OrderMainActivity.this);
                    builder1.setMessage("You Don't Internet Connection Now. Do you want to draft the order ?");
                    builder1.setCancelable(false);
                    builder1.setPositiveButton(
                            "Yes",
                            (dialog, id) -> {
                                dialog.cancel();
                                orderPresenter.draftOrder2(orderMaster);
                            });
                    builder1.setNegativeButton(
                            "No",
                            (dialog, id) -> dialog.cancel());

                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                }
               *//* if(isSetCampaignAdd)
                {

                }
                else {

                    SnackBarManagement._warning_CustomMessage(viewBinding.masterLayoutId,"Please Set a Campaign");
                }*//*

            }


        }*/
    }

    public void OrderDraft(Customer aInfoData, String userName, String empId, String extra) {
        //Toast.makeText(this, "inside " + Constants.WHO, Toast.LENGTH_SHORT).show();
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String formattedDate = df.format(c);
        int comUnitId = 2;
        OrderMaster orderMaster = new OrderMaster();
        Customer cust = new Customer(aInfoData.getCustomerMasterId(), aInfoData.getCustomerCode(), aInfoData.getCustomerName(), aInfoData.Address);
        orderMaster.setCustomer(cust);
        orderMaster.setMioCode(userName);
        orderMaster.setSubmittedDate(formattedDate);
        orderMaster.setCollectionDate(formattedDate);
        orderMaster.setDeliveryDate(viewBinding.txtDeliveryDate.getText().toString().trim());
        orderMaster.setOrderDetails(productListOrder);
        orderMaster.setEmpId(Integer.parseInt(empId));
        orderMaster.setComunitId(comUnitId);
        String remarks = viewBinding.remarksTxt.getText().toString();
        String _PaymnetType="" ;
        try
        {
            if (viewBinding.radioCod.isChecked()) {
                _PaymnetType="COD";
            }
        }catch (Exception ex){

        }
        try
        {
        if (viewBinding.radioNCOD.isChecked()) {
            _PaymnetType="NCOD";
        }  }catch (Exception ex){

        }
        orderMaster.setPaymentType(_PaymnetType);
        orderMaster.setRemarks(remarks);
        if (productListOrder.size() == 0) {
            SnackBarManagement._warning_CustomMessage(aMasterlayout, "Please add atlest one product to submit Order");
        } else {
            if (productOrder_IsZeroQuantity(productListOrder)) {
                SnackBarManagement._error_CustomMessage(aMasterlayout, "Product Quantity 0 is not Acceptable");
            } else {
                boolean isOk = crudHelper.DeleteOrderMasterDetail(orderLocalId);
                if (isOk == true) {
                    orderPresenter.draftOrder(orderMaster);
                } else {
                    SnackBarManagement._error_CustomMessage(aMasterlayout, "Order Not Saved");
                }

            }
        }
    }

    public void UpdateCustomerLocation(int customerId, int empId) {
        if (!NetworkInformation.isConnected(defaultContext)) {
            ToastManagment.GetLongToast(defaultContext, "You are not connected to internet");
            return;
        }
        try {

            LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }

            Location location = LocationGet.getLastKnownLocation(defaultContext);
            if (location != null) {
                CordinateUpdate cordinateUpdate = new CordinateUpdate();
                cordinateUpdate.setId(customerId);
                cordinateUpdate.setEmpId(empId);
                cordinateUpdate.setLatitudeValue(String.valueOf(lat));
                cordinateUpdate.setLongitudeValue(String.valueOf(lon));
                /*cordinateUpdate.setLatitudeValue(String.valueOf(location.getLatitude()));
                cordinateUpdate.setLongitudeValue(String.valueOf(location.getLongitude()));*/
                //cordinateUpdate.setLatitudeValue(Double.toString(location.getLatitude()));
                //cordinateUpdate.setLongitudeValue(Double.toString(location.getLongitude()));
                cordinateUpdate.setStreetaddress(streetAddress);

               /* Toast.makeText(defaultContext, "lat"+lat, Toast.LENGTH_SHORT).show();
                Toast.makeText(defaultContext, "lon "+lon, Toast.LENGTH_SHORT).show();
                Toast.makeText(defaultContext, ""+streetAddress, Toast.LENGTH_SHORT).show();*/
                orderPresenter.UpdateCustomerLocation(cordinateUpdate);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    //region Campaign Segment
    private void SelectCampaign(int customerId, /*, int empId*/List<CampaignGetReq> cmpaignReq) {
        try {
            if (productListOrder.size() == 0) {
                SnackBarManagement._warning_CustomMessage(viewBinding.masterLayoutId, "Please select product first");
                return;
            }
            getCampaign(cmpaignReq);

        } catch (Exception exception) {
        }
    }
    private void getCampaign(List<CampaignGetReq> campaignGetReq) {
        ProgressDialog progressDoalog = new ProgressDialog(defaultContext);
        progressDoalog.setMessage("Campaign is loading.... Please wait");
        progressDoalog.show();
        progressDoalog.setCanceledOnTouchOutside(false);

        Gson gson = new Gson();
        String data = gson.toJson(campaignGetReq);
        System.out.println("Get Campaign " + data);

        try {
            OrderProcessAPICALL service = RetrofitClientOrderProcessInstance.getRetrofitInstance().create(OrderProcessAPICALL.class);
            Call<List<CampaignModel>> call = service.GetCampaign(campaignGetReq);
            call.enqueue(new Callback<List<CampaignModel>>() {
                @Override
                public void onResponse(@NonNull Call<List<CampaignModel>> call, @NonNull Response<List<CampaignModel>> response) {
                    progressDoalog.dismiss();
                    if (response != null && response.isSuccessful() && response.body() != null) {
                        isSetCampaignAdd = true;
                        cmpaignList = response.body();
                        SetCampaignShow(cmpaignList);
                    } else {
                        SnackBarManagement._warning_CustomMessage(viewBinding.masterLayoutId, "No Campaign found for selected product");
                    }
                }

                @Override
                public void onFailure(@NonNull Call<List<CampaignModel>> call, @NonNull Throwable t) {
                    progressDoalog.dismiss();
                    if (t instanceof SocketTimeoutException) {
                        ToastManagment.GetLongToast(OrderMainActivity.this, "Connection Timeout. Please check network and try again");
                    } else {
                        ToastManagment.GetLongToast(OrderMainActivity.this, "Network Error: " + t.getLocalizedMessage());
                    }
                }
            });

        } catch (Exception ex) {
            progressDoalog.dismiss();
            ToastManagment.GetLongToast(OrderMainActivity.this, "Something went wrong. Please try again");
            ex.printStackTrace();
        }

    }
    //TODO:Herer Campaign Show into POPUP
    private void SetCampaignShow(List<CampaignModel> cmpaignList) {
        if (cmpaignList != null && cmpaignList.size() > 0) {
            // CampaignPopAdapter adapter;
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
            LayoutInflater inflater = this.getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.pop_campaignshow, null);
            dialogBuilder.setView(dialogView);

            Button btn_done = (Button) dialogView.findViewById(R.id.btn_done);
            Button btn_cancel = (Button) dialogView.findViewById(R.id.btn_cancel);
            RecyclerView recyclerView = (RecyclerView) dialogView.findViewById(R.id.rv_campaigns);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

            adapter = new CampaignPopAdapter(this, cmpaignList, this);
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            AlertDialog alertDialog = dialogBuilder.create();
            alertDialog.show();
            alertDialog.setCancelable(false);

            btn_cancel.setOnClickListener(view -> {
                cmpList.clear();
                alertDialog.dismiss();
            });
            btn_done.setOnClickListener(view -> {
                //TODO:Herer Selected Campaign Show into POPUP
                campSelected = adapter.getSelected();
                int customerTypeId = campSelected.getCustomerTypeId();
                codList = new ArrayList<>();
                Product product = new Product();
                for (int i = 0; i < productListOrder.size(); i++) {
                    product = productListOrder.get(i);
                    custId = product.getCustomerMasterId();
                    pid = product.getProductId();
                    name = product.getProductName();
                    int qty = product.getQuantity();
                    double up = product.getUnitPrice();
                    double vatAmount = product.getVatPercentage();

                    double TP = (qty * up);
                    double totalVat = (vatAmount * qty);
                    double netAmount = TP + totalVat;
                    // totalFinal = totalFinal + totalVat;
                    CampOrderDetails cod = new CampOrderDetails(pid, name, qty, up, TP, vatAmount, vatAmount, totalVat
                            , netAmount, false, 0.0, 0.0, campId, CustomerId, customerTypeId);
                    codList.add(cod);
                }
                cmpaignPReq.setCampaignMasters(cmpList);
                cmpaignPReq.setOrderDetails(codList);
                //Send req for order details
                PostSelectedCampaign(cmpaignPReq/*, discountPercentageTP*/);
                Gson gson = new Gson();
                String cmpListd = gson.toJson(cmpList);
                String codListd = gson.toJson(codList);
              /*  System.out.println("cmpList"+cmpListd);
                System.out.println("codListd"+codListd);*/
                alertDialog.dismiss();
            });
        } else {
            // SetCampaign(null, discountPercentageTP);
            SnackBarManagement._success_CustomMessage(viewBinding.masterLayoutId, "No Campaign for this product");
        }
    }
    private void PostSelectedCampaign(CampaignPostReq cmpaignPost) {
        Gson gson = new Gson();
        String data = gson.toJson(cmpaignPost);
        System.out.println("Get Campaign Popup Request" + data);

        ProgressDialog progressDoalog = new ProgressDialog(this);
        progressDoalog.setMessage("Please wait...");
        progressDoalog.show();
        progressDoalog.setCanceledOnTouchOutside(false);
        try {
            OrderProcessAPICALL service = RetrofitClientOrderProcessInstance.getRetrofitInstance().create(OrderProcessAPICALL.class);
            Call<List<CampaignCalModel>> call = service.GetCampaignProductWise(cmpaignPost);
            call.enqueue(new Callback<List<CampaignCalModel>>() {
                @Override
                public void onResponse(@NonNull Call<List<CampaignCalModel>> call, @NonNull Response<List<CampaignCalModel>> response) {
                    progressDoalog.dismiss();
                    if (response != null) {
                        viewBinding.cardOrderSummary.setVisibility(View.VISIBLE);
                        viewBinding.campaignDiv.setVisibility(View.VISIBLE);
                        SetCampaignShowToCustomer(response.body());
                    }

                }

                @Override
                public void onFailure(Call<List<CampaignCalModel>> call, Throwable t) {
                    progressDoalog.dismiss();
                    if (t instanceof SocketTimeoutException) {
                        ToastManagment.GetLongToast(OrderMainActivity.this, "Some thing went wrong. Please try again");
                    } else {
                        ToastManagment.GetLongToast(OrderMainActivity.this, "Some thing went wrong. Please try again");

                    }
                }
            });

        } catch (Exception ex) {
            progressDoalog.dismiss();
            SnackBarManagement._error_CustomMessage(viewBinding.masterLayoutId, "Some thing went wrong. Please try again");
            ex.printStackTrace();
        }
    }
    private void SetCampaignShowToCustomer(List<CampaignCalModel> cmpaigncalList) {
        if (cmpaigncalList != null) {
            cmpaignCalList = cmpaigncalList;

            setCAmpaignTotals(cmpaignCalList);
            RecyclerView recycler_view_product_Summary = findViewById(R.id.recycler_view_product_Summary);
            osAdapter = new _ordersummary_Recyler(cmpaignCalList);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            recycler_view_product_Summary.setLayoutManager(mLayoutManager);
            recycler_view_product_Summary.setItemAnimator(new DefaultItemAnimator());
            recycler_view_product_Summary.setAdapter(osAdapter);
            recycler_view_product_Summary.addItemDecoration(new DividerItemDecoration(getApplicationContext(),
                    DividerItemDecoration.VERTICAL));
            recycler_view_product_Summary.setItemAnimator(null);
            recycler_view_product_Summary.scrollToPosition(0);
            osAdapter.notifyDataSetChanged();
        }
    }

    //endregion
//endregion
    public void setCustomerText(Customer aCustomer) {
        viewBinding.customerName.setText(aCustomer.getCustomerName());
        viewBinding.customerCodeTxt.setText(aCustomer.getCustomerCode());
        viewBinding.customerAdressTxt.setText(aCustomer.getAddress());
    }
    private void LoadDepoStock(List<DepoStockModel> stocklist, RecyclerView rvStock) {
        DepoStockAdapter mAdapter = new DepoStockAdapter(stocklist, OrderMainActivity.this);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(OrderMainActivity.this);
        rvStock.setLayoutManager(mLayoutManager);
        rvStock.setItemAnimator(new DefaultItemAnimator());
        rvStock.setAdapter(mAdapter);
      /*  psb.rvStock.addItemDecoration(new DividerItemDecoration(getApplicationContext(),
                DividerItemDecoration.VERTICAL));*/
        rvStock.setItemAnimator(null);
        rvStock.scrollToPosition(0);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void OnError(String message) {
        Toast.makeText(this, "error: " + message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProductsGet(List<Product> aList) {
        if (aList != null) {
            productArrayList = (ArrayList<Product>) aList;
            ArrayAdapter<Product> adapter = new ArrayAdapter<>(
                    this, android.R.layout.simple_dropdown_item_1line, productArrayList);

            viewBinding.searchProducttxt.setAdapter(adapter);
            viewBinding.searchProducttxt.setThreshold(1);//will start working from first character
        }

     /*   if(NetworkInformation.isConnected(OrderMainActivity.this))
        {
            if (aList != null) {
                productArrayList = (ArrayList<Product>) aList;
                ArrayAdapter<Product> adapter = new ArrayAdapter<>(
                        this, android.R.layout.simple_dropdown_item_1line, productArrayList);
                viewBinding.searchProducttxt.setAdapter(adapter);
                viewBinding.searchProducttxt.setThreshold(1);//will start working from first character
            }
        }else {
            try {
                productArrayList = (ArrayList<Product>) pHelper.getProductFromDB();
                Toast.makeText(this, ""+productArrayList.toString(), Toast.LENGTH_SHORT).show();

            } catch (Exception exception) {
                exception.printStackTrace();
            }
            ArrayAdapter<Product> adapter = new ArrayAdapter<>(
                    this, android.R.layout.simple_dropdown_item_1line, productArrayList);
            viewBinding.searchProducttxt.setAdapter(adapter);
            viewBinding.searchProducttxt.setThreshold(1);//will start working from first character
        }*/
    }

    @Override
    public void onProductSampleGet(List<ProductSample> aList) {

    }

    @Override
    public void onEditTextFocusChange(int position, int value) {
        productListOrder.get(position).setQuantity(value);
        updateProductTotals(productListOrder.get(position));
        setTotals();
        if (!viewBinding.recyclerViewProduct.isComputingLayout()) {
            try {
                mAdapter.notifyItemChanged(position);
                osAdapter.notifyItemChanged(position);
                osAdapter.notifyDataSetChanged();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }


    @Override
    public boolean onLongClick(int position) {

        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage("Are you sure wants to delete the Item ?");
        builder1.setCancelable(true);
        builder1.setPositiveButton(
                "Delete",
                (dialog, id) -> {
                    try {
                        //Todo:When Delete any product


                        // productListCampShowList.remove(position);
                        productListOrder.remove(position);
                        setTotals();
                        mAdapter.notifyItemRemoved(position);

                        cmpaignCalList.clear();
                        cmpList.clear();
                        osAdapter.notifyItemRangeRemoved(0, cmpaignCalList.size());
                        osAdapter.notifyDataSetChanged();
                        viewBinding.totalTPValueSummary.setText("");
                        viewBinding.totalVatValueSummary.setText("");
                        viewBinding.totalDiscountValueSummary.setText("");
                        viewBinding.totalValueSummary.setText("");
                        dialog.cancel();
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

    }

    @Override
    public void OrderSync(OrderMaster orderMaster) {

    }


    public Product updateProductTotals(Product aProduct) {
        int totalQn = aProduct.getQuantity();
        double unitPrice = aProduct.getUnitPrice();
        double vatPer = aProduct.getVatPercentage();
        double tp = (totalQn * unitPrice);
        double vatamount = aProduct.getVatPercentage();
        double TP = (unitPrice * totalQn);
        double TotalVat = (vatamount * totalQn);
        double tpvat = (TP + TotalVat);
        aProduct.setTp(TP);
        aProduct.setPrice(Double.valueOf(df2.format(tpvat)));
        return aProduct;

    }

    public Product updateProductDetails(Product aProduct) {
        int qun = aProduct.getQuantity();
        int totalQn = aProduct.getQuantity();
        double unitPrice = aProduct.getUnitPrice();
        double vatPer = aProduct.getVatPercentage();
        double tp = (totalQn * unitPrice);
        double vatamount = aProduct.getVatPercentage();
        double TP = (unitPrice * totalQn);
        double TotalVat = (vatamount * totalQn);
        double tpvat = (TP + TotalVat);

        aProduct.setTp(TP);
        aProduct.setPrice(Double.valueOf(df2.format(tpvat)));

        return aProduct;

    }

    public CampaignCalModel updateCampaignTotals(CampaignCalModel aProduct) {
        int totalQn = aProduct.getQuantity();
        double unitPrice = aProduct.getUnitPrice();
        double vatPer = aProduct.getVatPercentage();
        double tp = (totalQn * unitPrice);
        double vatAMount = ((Double.valueOf(df2.format(tp)) * vatPer) / 100);
        double priceT = tp + vatAMount;
        aProduct.setNetAmount(Double.valueOf(df2.format(priceT)));
        aProduct.setTotalPrice(tp);
        /*aProduct.setTp(tp);
        aProduct.setPrice(Double.valueOf(df2.format(priceT)));*/
        return aProduct;

    }

    //TODO:SET TOTAL Calculation in product Selection
    public void setTotals() {
        double totaltp = 0;
        double totalVat = 0;
        double totalFinal = 0;
        for (int i = 0; i < productListOrder.size(); i++) {
            Product product = new Product();
            product = productListOrder.get(i);
            int totalQn = product.getQuantity();
            double unitPrice = product.getUnitPrice();
            double vatAmaount = product.getVatPercentage();
            double TP = (unitPrice * totalQn);

            double totalvat = (vatAmaount * totalQn);

            totaltp = (totaltp + TP);
            totalVat = totalVat + totalvat;
            totalFinal = (totaltp + totalVat);
        }
        viewBinding.totalTPValue.setText(df2.format(totaltp));
        viewBinding.totalVatValue.setText(df2.format(totalVat));
        viewBinding.totalValue.setText(df2.format(totalFinal));
    }

    public void setCAmpaignTotals(List<CampaignCalModel> cmpaignCalList) {
        double totaltp = 0;
        double totalVat = 0;
        double totalFinal = 0;
        double totalDiscount = 0;
        for (int i = 0; i < cmpaignCalList.size(); i++) {

            CampaignCalModel product = new CampaignCalModel();
            product = cmpaignCalList.get(i);
            int totalQn = product.getQuantity();
            double unitPrice = product.getUnitPrice();
            double vatPer = product.getUnitVatAmount();
            double discount = product.getDiscountValue();

            double TP = (unitPrice * totalQn);
            double pervat = (vatPer * totalQn);

            totalDiscount = totalDiscount + discount;
            totaltp = totaltp + TP;
            totalVat = totalVat + pervat;
            totalFinal = (totaltp + totalVat) - totalDiscount;
        }
        viewBinding.totalTPValueSummary.setText(df2.format(totaltp));
        viewBinding.totalVatValueSummary.setText(df2.format(totalVat));
        viewBinding.totalDiscountValueSummary.setText(df2.format(totalDiscount));
        viewBinding.totalValueSummary.setText(df2.format(totalFinal));
    }

    @Override
    public void onOrderSuccess(String message, String Who) {
        if (!TextUtils.isEmpty(message)) {
            viewBinding.btnSubmit.setVisibility(View.VISIBLE);
            new AlertDialog.Builder(this)
                    .setTitle("Order Success")
                    .setMessage(message)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                         /*   session = new SessionManagement(getApplicationContext());
                            HashMap<String, String> user = session.getUserDetails();
                            String extra = user.get(SessionManagement.Extra);*/
                         /*   if (!extra.equals("")) {
                                ((AlertDialog) dialog).getButton(which).setVisibility(View.INVISIBLE);
                                Intent i = new Intent(OrderMainActivity.this, CustomerDashboarActivity.class);
                                startActivity(i);
                                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                            } else {
                                crudHelper.DeleteOldOrder_OrderTable_SQLite();
                                ((AlertDialog) dialog).getButton(which).setVisibility(View.INVISIBLE);
                                Intent i = new Intent(OrderMainActivity.this, MainDashboardActivity.class);
                                i.addFlags(i.FLAG_ACTIVITY_CLEAR_TOP | i.FLAG_ACTIVITY_CLEAR_TASK | i.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(i);
                                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                            }*/

                            //   switch (Constants.WHO) {
                            switch (Who) {
                                case "SubmitOrder":
                                    ((AlertDialog) dialog).getButton(which).setVisibility(View.INVISIBLE);
                                    Intent i = new Intent(OrderMainActivity.this, MainDashboardActivity.class);
                                    startActivity(i);
                                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                    break;
                                case "DraftOrderAdapter":
                                    try {
                                        crudHelper.DeleteOldOrder_OrderTable_SQLite(orderLocalId);
                                    } catch (Exception exception) {
                                        exception.printStackTrace();
                                    }
                                    ((AlertDialog) dialog).getButton(which).setVisibility(View.INVISIBLE);
                                    Intent idr = new Intent(OrderMainActivity.this, MainDashboardActivity.class);
                                    idr.addFlags(idr.FLAG_ACTIVITY_CLEAR_TOP | idr.FLAG_ACTIVITY_CLEAR_TASK | idr.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(idr);
                                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                    break;
                                case "ApprovalOrder":
                                    ((AlertDialog) dialog).getButton(which).setVisibility(View.INVISIBLE);
                                    Intent idro = new Intent(OrderMainActivity.this, OrderApprovalListActivity.class);
                                    idro.addFlags(idro.FLAG_ACTIVITY_CLEAR_TOP | idro.FLAG_ACTIVITY_CLEAR_TASK | idro.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(idro);
                                    finish();
                                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                    break;
                            }
                        }
                    }).setCancelable(false).show();
        } else {
            viewBinding.btnSubmit.setVisibility(View.GONE);
        }

    }

    @Override
    public void onOrderDraftSuccess(String message) {
        if (!TextUtils.isEmpty(message)) {
            // viewBinding.btnSubmit.setVisibility(View.VISIBLE);
          /*  AlertDialog optionDialog = new AlertDialog.Builder(this)
                    .setTitle("Draft Success")
                    .setMessage(message)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            session = new SessionManagement(getApplicationContext());
                            HashMap<String, String> user = session.getUserDetails();
                            String extra = user.get(SessionManagement.Extra);
                            productListOrder.clear();
                            viewBinding.recyclerViewProduct.getAdapter().notifyDataSetChanged();
                            viewBinding.totalTPValue.setText("0.00");
                            viewBinding.totalValue.setText("0.00");
                            if(cmpaignCalList!=null)
                            {
                                cmpaignCalList.clear();
                                osAdapter.notifyDataSetChanged();
                            }
                            viewBinding.cardOrderSummary.setVisibility(View.GONE);
                            viewBinding.totalTPValueSummary.setText("0");
                            viewBinding.totalVatValueSummary.setText("0");
                            viewBinding.totalDiscountValueSummary.setText("0");
                            viewBinding.totalValueSummary.setText("0");
                            optionDialog.cancel();
                        }
                    })
                    .create();*/
            /*     AlertDialog.Builder bd = new AlertDialog.Builder(this);
            bd.setPositiveButton("OK", new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface d, int arg1) {
                    d.cancel();
                    //here db.cancel will dismiss the builder

                };
            });*/
            new AlertDialog.Builder(this)
                    .setTitle("Draft Success")
                    .setMessage(message)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            session = new SessionManagement(getApplicationContext());
                            HashMap<String, String> user = session.getUserDetails();
                            String extra = user.get(SessionManagement.Extra);
                            productListOrder.clear();
                            viewBinding.recyclerViewProduct.getAdapter().notifyDataSetChanged();
                            viewBinding.totalTPValue.setText("0.00");
                            viewBinding.totalValue.setText("0.00");
                            if (cmpaignCalList != null) {
                                cmpaignCalList.clear();
                                osAdapter.notifyDataSetChanged();
                            }
                            viewBinding.cardOrderSummary.setVisibility(View.GONE);
                            viewBinding.totalTPValueSummary.setText("0");
                            viewBinding.totalVatValueSummary.setText("0");
                            viewBinding.totalDiscountValueSummary.setText("0");
                            viewBinding.totalValueSummary.setText("0");
                            dialog.cancel();
                            //((AlertDialog) dialog).getButton(which).setVisibility(View.INVISIBLE);
                            //((AlertDialog) dialog).cancel();
                            //  ((AlertDialog) dialog).dismiss();
                            /*Intent i = new Intent(OrderMainActivity.this, MainDashboardActivity.class);
                            startActivity(i);
                            finish();
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);*/
                         /*   if (!extra.equals("")) {
                                ((AlertDialog) dialog).getButton(which).setVisibility(View.INVISIBLE);
                                Intent i = new Intent(OrderMainActivity.this, CustomerDashboarActivity.class);
                                startActivity(i);
                                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                            } else {
                                crudHelper.DeleteOldOrder_OrderTable_SQLite();
                                ((AlertDialog) dialog).getButton(which).setVisibility(View.INVISIBLE);
                                Intent i = new Intent(OrderMainActivity.this, MainDashboardActivity.class);
                                i.addFlags(i.FLAG_ACTIVITY_CLEAR_TOP | i.FLAG_ACTIVITY_CLEAR_TASK | i.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(i);
                                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                            }*/
                            /*switch (Constants.WHO)
                            {
                                case "DraftOrderAdapter":
                                    try {
                                        crudHelper.DeleteOldOrder_OrderTable_SQLite(orderLocalId);
                                    } catch (Exception exception) {
                                        exception.printStackTrace();
                                    }
                                    ((AlertDialog) dialog).getButton(which).setVisibility(View.INVISIBLE);
                                    Intent i = new Intent(OrderMainActivity.this, MainDashboardActivity.class);
                                    i.addFlags(i.FLAG_ACTIVITY_CLEAR_TOP | i.FLAG_ACTIVITY_CLEAR_TASK | i.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(i);
                                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                    break;
                            }*/
                        }
                    })
                    .setCancelable(false)
                    .show();
        } else {
            viewBinding.btnSubmit.setVisibility(View.GONE);
        }
    }

    @Override
    public void onOrderError(String message) {
        //  Toast.makeText(this, "Error : " + message, Toast.LENGTH_SHORT).show();

        SnackBarManagement._error_CustomMessage(viewBinding.masterLayoutId, message);
        //  fun_SelectCampaignButtonClick();
    }

    @Override
    public void onGenericSuccess(String message) {
        SnackBarManagement._success_CustomMessage(viewBinding.masterLayoutId, message);
    }

    @Override
    public void onGenericError(String message) {
        SnackBarManagement._error_CustomMessage(viewBinding.masterLayoutId, message);
    }


    public boolean productOrder_IsZeroQuantity(List<Product> aList) {
        boolean isZero = false;
        for (int i = 0; i < aList.size(); i++) {
            if (aList.get(i).getQuantity() == 0) {
                isZero = true;
            }

        }

        return isZero;
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left,
                R.anim.slide_out_right);
    }

    public Product GetSingleProductByCode(int id) {
        Product product = new Product();
        for (int i = 0; i < productArrayList.size(); i++) {
            if (productArrayList.get(i).getProductId() == id) {
                product = productArrayList.get(i);
                break;
            }
        }
        return product;
    }

    @Override
    public void ckdItem(List<CampaignModel> camp, int Pos) {
        if (camp != null) {
            cmpList.clear();
            for (int i = 0; i < camp.size(); i++) {
                cm = new CampaignMasters();
                campId = camp.get(i).getCampgainMasterId();
                bonousId = camp.get(i).getBonusProductId();
                cm.setCampaignMasterId(campId);
                int custtypeId = camp.get(i).getCustomerTypeId();
                cm.setCampgainMasterId(campId);
                cmpList.add(cm);

                // Toast.makeText(this, "chk : "+String.valueOf(bonousId), Toast.LENGTH_SHORT).show();
            }

        } else {

            adapter.notifyItemRangeRemoved(0, camp.size());
            cmpList.clear();
            //viewBinding.recyclerCampaignDetail.removeItemDecorationAt(po);
            // rv_doctors.removeItemDecorationAt(pos);

        }
    }

    @Override
    public void ckdItemid2(List<CampaignMaster2> ids2, int Pos) {

    }

    @Override
    public void ckdItemId(List<CampaignMasters> ids, int Pos) {
    }


    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                new AlertDialog.Builder(this)
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(OrderMainActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
        }
    }

    private void locationEnabled() {
        LocationManager lm = (LocationManager) OrderMainActivity.this.getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;
        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!gps_enabled && !network_enabled) {
            new AlertDialog.Builder(OrderMainActivity.this)
                    .setTitle("Enable GPS Service")
                    .setMessage("We need your GPS location to show Near Places around you.")
                    .setCancelable(false)
                    .setPositiveButton("Enable", new
                            DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                                    startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                                    //turnGPSOn();
                                }
                            })

                    // .setNegativeButton("Cancel", null)
                    .show();
        }
    }


    public void getCLocation() {
        try {
            locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 500, 5, (android.location.LocationListener) this);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        try {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            lat = addresses.get(0).getLatitude();
            lon = addresses.get(0).getLongitude();
            streetAddress = addresses.get(0).getAddressLine(0);
        } catch (Exception e) {
        }
    }
}