package com.creatrix.salessolution.Activity.OrderProcess;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.creatrix.salessolution.Activity.Customer.CustomerActivity;
import com.creatrix.salessolution.Activity.Customer.CustomerDashboarActivity;
import com.creatrix.salessolution.DBAdapter.DBCrudHelper;
import com.creatrix.salessolution.DBAdapter.ProductSQLiteHelper;
import com.creatrix.salessolution.Interface.IOrderManagement;
import com.creatrix.salessolution.Interface.IProduct;
import com.creatrix.salessolution.Interface.RecyclerViewActionListener;
import com.creatrix.salessolution.Model.Doctor.DoctorListViewModel;
import com.creatrix.salessolution.Model.OrderMaster;
import com.creatrix.salessolution.Model.ProductSample;
import com.creatrix.salessolution.Presenter.OrderManagementPresenter;
import com.creatrix.salessolution.R;
import com.creatrix.salessolution.RecyclerAdapter._product_orderpage_adapter;
import com.creatrix.salessolution.UtilityHelper.NetworkInformation;
import com.creatrix.salessolution.UtilityHelper.SessionManagement;
import com.creatrix.salessolution.UtilityHelper.SnackBarManagement;
import com.creatrix.salessolution.UtilityHelper.UtilityHelper;
import com.creatrix.salessolution.databinding.ActivitySampleOrderBinding;
import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class SampleOrderActivity extends AppCompatActivity implements RecyclerViewActionListener, IOrderManagement.View {

    ActivitySampleOrderBinding viewBinding;
    private _product_orderpage_adapter mAdapter;
    private RecyclerView recyclerView;
    IOrderManagement.Presenter orderPresenter;
    SessionManagement session;
    View aMasterlayout;
    private static DecimalFormat df2 = new DecimalFormat("#.##");
    String orderTYpe;

    //    private ArrayList<Product> productArrayList = new ArrayList<>();
    private List<ProductSample> productSampleListOrder = new ArrayList<>();

    DBCrudHelper crudHelper;
    ProductSQLiteHelper pcrudHelper;
    List<ProductSample> productSampleList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewBinding = ActivitySampleOrderBinding.inflate(getLayoutInflater());
        View mainView = viewBinding.getRoot();
        setContentView(mainView);
        Gson gson = new Gson();
        DoctorListViewModel aInfoData = gson.fromJson(getIntent().getStringExtra("myjson"), DoctorListViewModel.class);
        setDoctorText(aInfoData);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_custom);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        orderTYpe = getIntent().getStringExtra("OrderType");
        orderPresenter = new OrderManagementPresenter(this, this);
        crudHelper = new DBCrudHelper(this);
        pcrudHelper = new ProductSQLiteHelper(this);

        session = new SessionManagement(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        String userName = user.get(SessionManagement.KEY_LoginName);
        String empId = user.get(SessionManagement.KEY_EmpId);
        String extra = user.get(SessionManagement.Extra);

        aMasterlayout = findViewById(R.id.masterLayoutId);
        try {
            productSampleList = pcrudHelper.getProductSampleFromDB();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        if (productSampleList != null) {
            ArrayAdapter<ProductSample> adapter = new ArrayAdapter<>(
                    this, android.R.layout.simple_dropdown_item_1line, productSampleList);
            viewBinding.searchProducttxt.setAdapter(adapter);
            viewBinding.searchProducttxt.setThreshold(1);//will start working from first character
        }
        // product add auto complete
        viewBinding.searchProducttxt.setOnItemClickListener((AdapterView.OnItemClickListener)
                (arg0, arg1, arg2, arg3) -> {
                    ProductSample selected = (ProductSample) arg0.getAdapter().getItem(arg2);
                    double id = selected.getProductId();
                    String Test = Double.toString(id);
                    LayoutInflater li = LayoutInflater.from(SampleOrderActivity.this);
                    View promptsView = li.inflate(R.layout.quantity_modal, null);
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SampleOrderActivity.this);
                    alertDialogBuilder.setView(promptsView);

                    final EditText userInput = (EditText) promptsView
                            .findViewById(R.id.editTextDialogUserInput);
                    userInput.requestFocus();
                    // set dialog message
                    alertDialogBuilder
                            .setCancelable(false)
                            .setPositiveButton("Done",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            ProductSample sp = (ProductSample) arg0.getAdapter().getItem(arg2);
                                            String pQyu = userInput.getText().toString();
                                            if (pQyu.equals("")) {
                                                Toast.makeText(SampleOrderActivity.this, "Please enter a valid amount", Toast.LENGTH_LONG).show();
                                            } else {
                                                int quantity = Integer.parseInt(pQyu);
                                                ProductSample asProduct = new ProductSample(sp.getProductId(), sp.getProductName(), sp.getProductCode(), quantity);
                                                productSampleListOrder.add(asProduct);
                                                if (quantity == 0) {
                                                    Toast.makeText(SampleOrderActivity.this, "Please enter a valid amount", Toast.LENGTH_LONG).show();
                                                    return;
                                                } else {
                                                    mAdapter.notifyItemChanged(productSampleListOrder.size());
                                                    viewBinding.searchProducttxt.setText("");

                                                }

                                            }

                                        }
                                    })
                            .setNegativeButton("Cancel",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            viewBinding.searchProducttxt.setText("");
                                            dialog.cancel();
                                        }
                                    });

                    // create alert dialog
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
                    alertDialog.show();
                });

        // product Adapter for add order
        if (productSampleListOrder != null) {
            recyclerView = (RecyclerView) findViewById(R.id.recycler_view_productsample);
            mAdapter = new _product_orderpage_adapter(productSampleListOrder, this, this, "SampleOrder");
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(mAdapter);
            recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(),
                    DividerItemDecoration.VERTICAL));
            recyclerView.setItemAnimator(null);
            recyclerView.scrollToPosition(0);
            mAdapter.notifyDataSetChanged();
        }

        viewBinding.datePickerDeliveryDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UtilityHelper._datePickerDialogeForDates_DisableOldDates(viewBinding.txtDeliveryDate, SampleOrderActivity.this);
            }
        });

        viewBinding.datePickerCollectionDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UtilityHelper._datePickerDialogeForDates_DisableOldDates(viewBinding.txtCollectionDate, SampleOrderActivity.this);
            }
        });


        // submit button click event
        viewBinding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date c = Calendar.getInstance().getTime();

                SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
                String formattedDate = df.format(c);
                String deliveryDate = formattedDate;
                String collectioDate = formattedDate;

                OrderMaster orderMaster = new OrderMaster();
                orderMaster.setOrderId(0);
                orderMaster.setSubmittedDate(formattedDate);
                orderMaster.setCollectionDate(collectioDate);
                orderMaster.setOrderSampleDetails(productSampleListOrder);
                orderMaster.setEmpId(Integer.parseInt(empId));
                String remarks = viewBinding.remarksTxt.getText().toString();
                orderMaster.setRemarks(remarks);

                boolean isValidateOK = true;
                if (productSampleListOrder.size() == 0) {
                    SnackBarManagement._warning_CustomMessage(aMasterlayout, "Please add atlest one product to submit Order");
                } else {
                    if (productOrder_IsZeroQuantity(productSampleListOrder) == true) {
                        SnackBarManagement._error_CustomMessage(aMasterlayout, "Product Quantity 0 is not Acceptable");
                    } else {
                        if (NetworkInformation.isConnected(SampleOrderActivity.this) == true) {
                            orderPresenter.makeOrder(orderMaster);
                        } else {
                            /// showing user to draft the order
                            AlertDialog.Builder builder1 = new AlertDialog.Builder(SampleOrderActivity.this);
                            builder1.setMessage("You Don't Internet Connection Now. Do you want to draft the order ?");
                            builder1.setCancelable(false);
                            builder1.setPositiveButton(
                                    "Yes",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                            orderPresenter.draftOrder(orderMaster);

                                        }
                                    });

                            builder1.setNegativeButton(
                                    "No",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });

                            AlertDialog alert11 = builder1.create();
                            alert11.show();
                        }
                    }
                }
            }
        });


        viewBinding.btnDraft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date c = Calendar.getInstance().getTime();
                SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
                String formattedDate = df.format(c);
                String deliveryDate = formattedDate;
                String collectioDate = formattedDate;
                int comUnitId = 2;
                HashMap<String, String> user = session.getUserDetails();
                OrderMaster orderMaster = new OrderMaster();
                orderMaster.setMioCode(userName);
                orderMaster.setSubmittedDate(formattedDate);
                orderMaster.setCollectionDate(formattedDate);
                orderMaster.setOrderSampleDetails(productSampleListOrder);
                orderMaster.setEmpId(Integer.parseInt(empId));
                orderMaster.setComunitId(comUnitId);
                String remarks = viewBinding.remarksTxt.getText().toString();
                orderMaster.setRemarks(remarks);
                if (productSampleListOrder.size() == 0) {
                    SnackBarManagement._warning_CustomMessage(aMasterlayout, "Please add atlest one product to submit Order");
                } else {
                    if (productOrder_IsZeroQuantity(productSampleListOrder) == true) {
                        SnackBarManagement._error_CustomMessage(aMasterlayout, "Product Quantity 0 is not Acceptable");
                    } else {
                        orderPresenter.draftOrder(orderMaster);
                    }
                }
            }
        });


    }

    public void setDoctorText(DoctorListViewModel aDoctor) {

        viewBinding.customerName.setText(aDoctor.getDoctorName());
        viewBinding.customerCodeTxt.setText(aDoctor.getDoctorCode());
        viewBinding.customerAdressTxt.setText(aDoctor.getChamberAddress());

    }


    @Override
    public void onEditTextFocusChange(int position, int value) {
        productSampleListOrder.get(position).setQuantity(value);
        if (!recyclerView.isComputingLayout()) {
            mAdapter.notifyItemChanged(position);
        }


    }

    @Override
    public boolean onLongClick(int position) {

        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage("Are you sure wants to delete the Item ?");
        builder1.setCancelable(true);
        builder1.setPositiveButton(
                "Delete",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        productSampleListOrder.remove(position);
                        mAdapter.notifyItemRemoved(position);
                        dialog.cancel();
                    }
                });
        builder1.setNegativeButton(
                "Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();

        return true;

    }

    @Override
    public void OrderSync(OrderMaster orderMaster) {

    }





    @Override
    public void onOrderSuccess(String message,String who) {
        new AlertDialog.Builder(this)
                .setTitle("Order Success")
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        session = new SessionManagement(getApplicationContext());
                        /*HashMap<String, String> user = session.getUserDetails();
                        String extra = user.get(SessionManagement.Extra);*/

                        ((AlertDialog) dialog).getButton(which).setVisibility(View.INVISIBLE);
                    /*    Intent i = new Intent(SampleOrderActivity.this, CustomerActivity.class);
                        startActivity(i);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);*/
                        finish();
//
                    }

                }).setCancelable(false).show();

    }

    @Override
    public void onOrderDraftSuccess(String message) {
        new AlertDialog.Builder(this)
                .setTitle("Order Success")
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        session = new SessionManagement(getApplicationContext());
                        /*HashMap<String, String> user = session.getUserDetails();
                        String extra = user.get(SessionManagement.Extra);*/

                        ((AlertDialog) dialog).getButton(which).setVisibility(View.INVISIBLE);
                    /*    Intent i = new Intent(SampleOrderActivity.this, CustomerActivity.class);
                        startActivity(i);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);*/
                        finish();
//
                    }

                }).setCancelable(false).show();
    }

    @Override
    public void onOrderError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onGenericSuccess(String message) {

    }

    @Override
    public void onGenericError(String message) {

    }


    public boolean productOrder_IsZeroQuantity(List<ProductSample> aList) {
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
}