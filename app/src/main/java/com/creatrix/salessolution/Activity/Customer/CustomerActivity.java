package com.creatrix.salessolution.Activity.Customer;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.creatrix.salessolution.Activity.Approval.DCR.DcrApprovalListActivity;
import com.creatrix.salessolution.Activity.Customer.Approval.CustomerApprovalListActivity;
import com.creatrix.salessolution.Activity.Customer.Approval.Model.CustomerApprovalList;
import com.creatrix.salessolution.Activity.Doctor.TourePlan.TourPlanDetailsActivity;
import com.creatrix.salessolution.Activity.MainDashboardActivity;
import com.creatrix.salessolution.Activity.PersonInfoDAO;
import com.creatrix.salessolution.DBAdapter.DBCrudHelper;
import com.creatrix.salessolution.DBAdapter.DBDoctor.DBDoctorHelper;
import com.creatrix.salessolution.Interface.IBangladesh;
import com.creatrix.salessolution.Interface.IMarketStracture;
import com.creatrix.salessolution.Model.Customer;
import com.creatrix.salessolution.Model.CustomerType;
import com.creatrix.salessolution.Model.DistrictVM;
import com.creatrix.salessolution.Model.DivisionVM;
import com.creatrix.salessolution.Model.Doctor.ProgramType;
import com.creatrix.salessolution.Model.MarketStructure.StructureTable.Area;
import com.creatrix.salessolution.Model.MarketStructure.StructureTable.Group;
import com.creatrix.salessolution.Model.MarketStructure.StructureTable.Market;
import com.creatrix.salessolution.Model.MarketStructure.StructureTable.Region;
import com.creatrix.salessolution.Model.MarketStructure.StructureTable.SubTeritorry;
import com.creatrix.salessolution.Model.MarketStructure.StructureTable.Teritorry;
import com.creatrix.salessolution.Model.ModelProviderType;
import com.creatrix.salessolution.Model.ModelSMCType;
import com.creatrix.salessolution.Model.ResultInfo;
import com.creatrix.salessolution.Model.StationType;
import com.creatrix.salessolution.Model.ThanaVM;
import com.creatrix.salessolution.Model.TourPlanViewModel;
import com.creatrix.salessolution.Network.ApiCustomerCall;
import com.creatrix.salessolution.Network.RetrofitClientInstance;
import com.creatrix.salessolution.Network.RetrofitClientInstanceIntegration;
import com.creatrix.salessolution.Presenter.BangladehPresenter;
import com.creatrix.salessolution.Presenter.MarketStructurePresenter;
import com.creatrix.salessolution.R;
import com.creatrix.salessolution.Services.Constants;
import com.creatrix.salessolution.UtilityHelper.CameraHelper;
import com.creatrix.salessolution.UtilityHelper.NetworkInformation;
import com.creatrix.salessolution.UtilityHelper.SessionManagement;
import com.creatrix.salessolution.UtilityHelper.SnackBarManagement;
import com.creatrix.salessolution.UtilityHelper.ToastManagment;
import com.creatrix.salessolution.databinding.ActivityCustomerBinding;
import com.creatrix.salessolution.databinding.DialogSelectLocationBinding;
import com.creatrix.salessolution.databinding.PopTourplanAddMarketwiseBinding;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.gson.Gson;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static com.creatrix.salessolution.Activity.Attendance.AttendanceActivity.MY_PERMISSIONS_REQUEST_LOCATION;

import id.zelory.compressor.Compressor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomerActivity extends AppCompatActivity implements LocationListener, ICustomerAdd.View, IMarketStracture.View, IBangladesh.View {
    private static final String INTEGRATION_USER_ID = "SMCeAdmin";
    private static final String INTEGRATION_PASS = "Smc@ePharma";
    int   selectedDiviId, selectedDistId, selectedThanaId;
    String selectedThanaName = "";
    DialogSelectLocationBinding selectLocationBinding;
    Dialog popupTPP;
    private static final int PERMISSION_REQUEST_CODE = 200;
    public static final int PICK_IMAGE = 1;
    public static final int PICK_TIMAGE = 2;
    private static final int CAMERA_REQUEST = 1888;
    private static final int CAMERA_PIC_REQUEST = 11;
    private static int CAMERA_PIC_REQUEST2;
    private static int TCAMERA_PIC_REQUEST2;
    ActivityCustomerBinding viewBinding;
    ICustomerAdd.Presenter presenter;
    IMarketStracture.Presenter mkpresenter;
    IBangladesh.Presenter bdpresenter;

    ProgressDialog pd;


    private FusedLocationProviderClient fusedLocationProviderClient;
    LocationManager locationManager;

    DBCrudHelper dbCrudHelper;
    DBDoctorHelper dbDoctor;
    boolean is_Edit = false;


    int seletedRegionId, seletedAreaId, seletedTeritoryId, seletedSTeritoryId, selectedMarketId;
    Bitmap bitmap;
    ByteArrayOutputStream stream;
    Uri imageuri, timageuri;
    File imgFile, timgFile;
    Double lat, lon;
    String address = "";
    CustomerApprovalList cal;
    CustomerSvModel csv;
    Gson gson = new Gson();
    Bitmap decodedByte, decodedByte2;
    int custmasterid;
    String edit_dName, path, tpath;

    public String blockCharacterSet = "@~'#^|$%&*!/?>";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_customer);
        viewBinding = com.creatrix.salessolution.databinding.ActivityCustomerBinding.inflate(getLayoutInflater());
        View mainView = viewBinding.getRoot();
        setContentView(mainView);
        //Blocking Special char
        viewBinding.customerName.setFilters(new InputFilter[]{filter});
        viewBinding.address.setFilters(new InputFilter[]{filter});
        viewBinding.cmistOwnerName.setFilters(new InputFilter[]{filter});
        viewBinding.cmistTradeLicense.setFilters(new InputFilter[]{filter});
        viewBinding.changeptype.setOnClickListener(v -> showSelectDialog());

        // dbCrudHelper = new DBCrudHelper(CustomerActivity.this);
        dbDoctor = new DBDoctorHelper(CustomerActivity.this);
        dbCrudHelper = new DBCrudHelper(CustomerActivity.this);
        presenter = new CustomerPresenter(this, CustomerActivity.this);
        mkpresenter = new MarketStructurePresenter(this, CustomerActivity.this);
        bdpresenter = new BangladehPresenter(this, CustomerActivity.this);



        popup_tpp();
        viewBinding.toolbarCustom.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if (ContextCompat.checkSelfPermission(CustomerActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(CustomerActivity.this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(CustomerActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
        }
        locationManager = (LocationManager) CustomerActivity.this.getSystemService(Context.LOCATION_SERVICE);
        locationEnabled();
        getCLocation();
        SessionManagement session = new SessionManagement(getApplicationContext());
        //session.checkLogin();
        HashMap<String, String> user = session.getUserDetails();
        int empId = Integer.parseInt(user.get(SessionManagement.KEY_EmpId));
        String roleType = user.get(SessionManagement.KEY_EmpRoleType);
        if (!NetworkInformation.isConnected(this)) {
            checkLocationPermission();
        } else {
        }
        switch (Constants.WHO) {
            case "CustApprovalAC":
                viewBinding.btnSubmit.setVisibility(View.GONE);
                viewBinding.btnUpdate.setVisibility(View.VISIBLE);
                viewBinding.tcamera.setVisibility(View.GONE);
                viewBinding.camera.setVisibility(View.GONE);

                cal = gson.fromJson(getIntent().getStringExtra("Editdata"), CustomerApprovalList.class);
                viewBinding.toolbarTitle.setText("Edit Customer");
                mkpresenter.GetRegionLocal(0);
                mkpresenter.GetAreaLocal(0);
                mkpresenter.GetTeritoryLocal(0);
                // mkpresenter.GetSTeritoryLocal(cal.getTerritoryId());
                mkpresenter.GetSTeritoryLocal(0);
                mkpresenter.GetMarketLocal(0);

                viewBinding.regionSpinner.setBackground(null);
                viewBinding.areaSpinner.setBackground(null);
                viewBinding.territorySpinner.setBackground(null);
                viewBinding.sterritorySpinner.setBackground(null);
                viewBinding.marketSpinner.setBackground(null);

                viewBinding.regionSpinner.setOnTouchListener(new View.OnTouchListener() {
                    @SuppressLint("ClickableViewAccessibility")
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        return true;
                    }
                });
                viewBinding.areaSpinner.setOnTouchListener(new View.OnTouchListener() {
                    @SuppressLint("ClickableViewAccessibility")
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        return true;
                    }
                });
                viewBinding.territorySpinner.setOnTouchListener(new View.OnTouchListener() {
                    @SuppressLint("ClickableViewAccessibility")
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        return true;
                    }
                });
                viewBinding.sterritorySpinner.setOnTouchListener(new View.OnTouchListener() {
                    @SuppressLint("ClickableViewAccessibility")
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        return true;
                    }
                });
                viewBinding.marketSpinner.setOnTouchListener(new View.OnTouchListener() {
                    @SuppressLint("ClickableViewAccessibility")
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        return true;
                    }
                });

                setupEdit(roleType, empId, cal/*,csv*/);
                break;

            case "HomeToCustomer":
                viewBinding.btnUpdate.setVisibility(View.GONE);
                viewBinding.btnSubmit.setVisibility(View.VISIBLE);
                viewBinding.tcamera.setVisibility(View.VISIBLE);
                viewBinding.camera.setVisibility(View.VISIBLE);
                // presenter.GetProgramType();
                presenter.GetProviderType();
                presenter.GetSMCType();

                mkpresenter.GetRegionLocal(0);
                mkpresenter.GetAreaLocal(0);
                mkpresenter.GetTeritoryLocal(0);
                mkpresenter.GetSTeritoryLocal(0);
                mkpresenter.GetMarketLocal(0);
                try {
                    switch (roleType) {
                        case "MIO":
                            viewBinding.regiondiv.setVisibility(View.GONE);
                            viewBinding.areadiv.setVisibility(View.GONE);
                            break;
                        case "AM":
                            viewBinding.areadiv.setVisibility(View.VISIBLE);
                            break;
                        case "DZSM":
                            viewBinding.regiondiv.setVisibility(View.VISIBLE);
                            viewBinding.areadiv.setVisibility(View.VISIBLE);

                            break;
                    }
                    viewBinding.regionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            Region region = (Region) parent.getSelectedItem();
                            seletedRegionId = region.getRegionId();
                            mkpresenter.GetAreaLocal(seletedRegionId);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                    viewBinding.areaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            Area area = (Area) parent.getSelectedItem();
                            seletedAreaId = area.getAreaId();
                            mkpresenter.GetTeritoryLocal(seletedAreaId);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                    viewBinding.territorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            Teritorry teri = (Teritorry) parent.getSelectedItem();
                            seletedTeritoryId = teri.getTerritoryId();
                            mkpresenter.GetSTeritoryLocal(seletedTeritoryId);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                    viewBinding.sterritorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            SubTeritorry steri = (SubTeritorry) parent.getSelectedItem();
                            seletedSTeritoryId = steri.getSubTerritoryId();
                            mkpresenter.GetMarketLocal(seletedSTeritoryId);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                    viewBinding.marketSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            Market market = (Market) parent.getSelectedItem();
                            selectedMarketId = market.getMarketId();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });


//                    viewBinding.customerMobile.addTextChangedListener(new TextWatcher() {
//                        @Override
//                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                            // No action needed
//                        }
//
//                        @Override
//                        public void onTextChanged(CharSequence s, int start, int before, int count) {
//                            if (s != null && s.length() == 11) {
//                              //  Toast.makeText(CustomerActivity.this, "১১ ডিজিট দিয়েছেন", Toast.LENGTH_SHORT).show();
//
//                                ApiCustomerCall service = RetrofitClientInstance.getRetrofitInstance()
//                                        .create(ApiCustomerCall.class);
//
//                                Call<Customer> call = service.GetCustomerByUserByMobileNo(
//                                        empId, viewBinding.customerMobile.getText().toString()
//                                );
//
//                                call.enqueue(new Callback<Customer>() {
//                                    @Override
//                                    public void onResponse(Call<Customer> call, Response<Customer> response) {
//                                        if (response.isSuccessful()) {
//                                            Customer customer = response.body();
//                                            if (customer != null) {
//                                                // ✅ Customer found
//                                                Toast.makeText(CustomerActivity.this,
//                                                        "Customer পাওয়া গেছে: " + customer.getCustomerName(),
//                                                        Toast.LENGTH_SHORT).show();
//
//                                                viewBinding.customerName.setText(customer.getCustomerName());
//                                                viewBinding.address.setText(customer.getAddress());
//                                                viewBinding.cmistOwnerName.setText(customer.getRoute());
//
//                                                // TODO: populate UI with customer data
//                                            } else {
//                                                // ✅ No customer found
//                                                Toast.makeText(CustomerActivity.this,
//                                                        "কোনো কাস্টমার পাওয়া যায়নি",
//                                                        Toast.LENGTH_SHORT).show();
//                                            }
//                                        } else {
//                                            Toast.makeText(CustomerActivity.this,
//                                                    "Server response failed",
//                                                    Toast.LENGTH_SHORT).show();
//                                        }
//                                    }
//
//                                    @Override
//                                    public void onFailure(Call<Customer> call, Throwable t) {
//                                        Toast.makeText(CustomerActivity.this,
//                                                "API Call ব্যর্থ: " + t.getMessage(),
//                                                Toast.LENGTH_SHORT).show();
//                                    }
//                                });
//                            }
//                        }
//
//                        @Override
//                        public void afterTextChanged(Editable s) {
//                            // No action needed
//                        }
//                    });


                    viewBinding.btnSubmit.setOnClickListener(v -> {
                        String img_str = null;
                        try {
                            Bitmap bitmap = new Compressor(CustomerActivity.this).compressToBitmap(imgFile);
                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream);
                            byte[] image = stream.toByteArray();
                            img_str = Base64.encodeToString(image, 0);
                            System.out.println("img : " + img_str);

                        } catch (Exception exception) {
                            exception.printStackTrace();
                        }
                        String timg_str = null;
                        try {
                            if (timgFile != null) {
                                Bitmap bitmap2 = new Compressor(this).compressToBitmap(timgFile);
                                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                bitmap2.compress(Bitmap.CompressFormat.JPEG, 90, stream);
                                byte[] image = stream.toByteArray();
                                timg_str = Base64.encodeToString(image, 0);
                                System.out.println("img : " + timg_str);
                            } else {
                                timg_str = "";
                            }

                        } catch (IOException e) {
                            Toast.makeText(CustomerActivity.this, "Image Exception", Toast.LENGTH_SHORT).show();
                        }
                        if (TextUtils.isEmpty(viewBinding.customerMobile.getText().toString())) {
                            SnackBarManagement._warning_CustomMessage(viewBinding.masterLayoutId, "Mobile is required");
                            return;
                        }
                        if (viewBinding.customerMobile.getText().toString().trim().length() < 11) {
                            SnackBarManagement._warning_CustomMessage(viewBinding.masterLayoutId, "Mobile Can't Be Less Than 11");
                            return;
                        }
                        if (TextUtils.isEmpty(viewBinding.customerName.getText().toString())) {
                            SnackBarManagement._warning_CustomMessage(viewBinding.masterLayoutId, "Name is required");
                            return;
                        }
                        if (TextUtils.isEmpty(viewBinding.address.getText().toString())) {
                            SnackBarManagement._warning_CustomMessage(viewBinding.masterLayoutId, "Address is required");
                            return;
                        }

                        if (TextUtils.isEmpty(viewBinding.cmistOwnerName.getText().toString())) {
                            SnackBarManagement._warning_CustomMessage(viewBinding.masterLayoutId, "Owner Name is required");
                            return;
                        }
                        if (viewBinding.imgaeView.getDrawable() == null || img_str==null) {
                            SnackBarManagement._warning_CustomMessage(viewBinding.masterLayoutId, "Shop Image required");
                            return;
                        }
                        if (TextUtils.isEmpty(String.valueOf(lat))) {
                            SnackBarManagement._warning_CustomMessage(viewBinding.masterLayoutId, "Your Latitude Getting Null.Try Again");
                            return;
                        }
                        if (TextUtils.isEmpty(String.valueOf(lon))) {
                            SnackBarManagement._warning_CustomMessage(viewBinding.masterLayoutId, "Your Longitude Getting Null.Try Again");
                            return;
                        }

                        SaveCustomer(custmasterid, empId, img_str, timg_str, "AddCust");
                    });
                } catch (Exception exception) {
                    exception.printStackTrace();
                    Toast.makeText(this, exception.getMessage(), Toast.LENGTH_SHORT).show();
                }

                break;
        }
        viewBinding.camera.setOnClickListener(v -> {
                popupTPP.hide();
            if (ContextCompat.checkSelfPermission(CustomerActivity.this,
                    Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED) {
                /*  Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), CAMERA_PIC_REQUEST);*/
                CAMERA_PIC_REQUEST2 = 78911;
                Constants.From = "78911";
                ImagePicker.Companion.with(CustomerActivity.this)
                        .crop()
                        //Crop image(Optional), Check Customization for more option
                        .compress(1024)            //Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
            } else {
                ActivityCompat.requestPermissions(CustomerActivity.this,
                        new String[]{Manifest.permission.CAMERA},
                        PERMISSION_REQUEST_CODE);
            }
        });
        viewBinding.tcamera.setOnClickListener(v -> {
            popupTPP.hide();
            if (ContextCompat.checkSelfPermission(CustomerActivity.this,
                    Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED) {
               /* Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), TCAMERA_PIC_REQUEST);*/
                TCAMERA_PIC_REQUEST2 = 83411;
                Constants.From = "83411";
                ImagePicker.Companion.with(CustomerActivity.this)
                        .crop()
                        //Crop image(Optional), Check Customization for more option
                        .compress(1024)            //Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                        .start();

            } else {
                ActivityCompat.requestPermissions(CustomerActivity.this,
                        new String[]{Manifest.permission.CAMERA},
                        PERMISSION_REQUEST_CODE);
            }
        });


    }
    private void setupEdit(String roleType, int empId, CustomerApprovalList cal) {
        switch (roleType) {
            case "MIO":
                viewBinding.areadiv.setVisibility(View.GONE);
                viewBinding.regiondiv.setVisibility(View.GONE);
                viewBinding.regionSpinner.setVisibility(View.GONE);
                viewBinding.regionLabel.setVisibility(View.GONE);
                // mkpresenter.GetTeritoryLocal(0);
                break;
            case "AM":
                viewBinding.regiondiv.setVisibility(View.GONE);
                viewBinding.areadiv.setVisibility(View.VISIBLE);
                // mkpresenter.GetAreaLocal(0);
                break;
            case "DZSM":
                viewBinding.regiondiv.setVisibility(View.VISIBLE);
                viewBinding.areadiv.setVisibility(View.VISIBLE);
                // mkpresenter.GetRegionLocal(0);
                break;
        }
        try {
            csv = cal.getCustomerSMListDao();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        Region rname = null;
        try {
            rname = dbCrudHelper.getRegionName_SQLite(csv.getRegionId());
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        Area aname = null;
        try {
            aname = dbCrudHelper.getAreaName_SQLite(csv.getAreaId());
        } catch (Exception exception) {
            exception.printStackTrace();
        }


        Teritorry tname = null;
        try {
            tname = dbCrudHelper.getTerritoryName_SQLite(csv.getTerritoryId());
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        SubTeritorry Stname = null;
        try {
            Stname = dbCrudHelper.getSubTerritoryName_SQLite(csv.getSubTerritoryId());
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        if (String.valueOf(csv.getCustomerMasterId()) != null) {
            custmasterid = csv.getCustomerMasterId();
        } else {
            custmasterid = 0;
        }
        if (csv.getCellNo() != null) {
            viewBinding.customerMobile.setText(csv.getCellNo());
            viewBinding.customerMobile.setEnabled(false);
        } else {
            viewBinding.customerMobile.setText("---- ----");
        }
        if (csv.getCustomerName() != null) {
            viewBinding.customerName.setText(csv.getCustomerName());
        } else {
            viewBinding.customerName.setText("---- ----");
        }
        if (csv.getAddress() != null) {
            viewBinding.address.setText(csv.getAddress());
        } else {
            viewBinding.address.setText("---- ----");
        }
        if (csv.getProgramTypeName() != null) {
            //presenter.GetProgramType();
            presenter.GetProviderType();
            edit_dName = csv.getProgramTypeName();
            viewBinding.ptypeSpinner.setSelection(getIndex(viewBinding.ptypeSpinner, edit_dName));
        } else {
            //presenter.GetProgramType();
            presenter.GetProviderType();
        }

        if (csv.getSMCTypeName() != null) {
            presenter.GetSMCType();
            edit_dName = csv.getSMCTypeName();
            viewBinding.smcTypeSpinner.setSelection(getIndex(viewBinding.smcTypeSpinner, edit_dName));
        } else {
            presenter.GetSMCType();
        }


        //Market
        if (rname.getRegionName() != null) {
            //  mkpresenter.GetTeritoryLocal(0);
            edit_dName = rname.getRegionName();
            mkpresenter.GetAreaLocal(seletedRegionId);
            viewBinding.regionSpinner.setSelection(getIndex(viewBinding.regionSpinner, edit_dName));
        } else {
            mkpresenter.GetRegionLocal(0);
        }
        if (aname.getAreaName() != null) {
            //  mkpresenter.GetTeritoryLocal(0);
            edit_dName = aname.getAreaName();
            mkpresenter.GetTeritoryLocal(seletedAreaId);
            viewBinding.areaSpinner.setSelection(getIndex(viewBinding.areaSpinner, edit_dName));
        } else {
            mkpresenter.GetAreaLocal(0);
        }
        if (tname.getTerritoryName() != null) {
            edit_dName = tname.getTerritoryName();
            mkpresenter.GetSTeritoryLocal(seletedTeritoryId);
            viewBinding.territorySpinner.setSelection(getIndex(viewBinding.territorySpinner, edit_dName));
        } else {
            mkpresenter.GetTeritoryLocal(0);
        }
        if (Stname.getSubTerritoryName() != null) {
            edit_dName = Stname.getSubTerritoryName();
            mkpresenter.GetMarketLocal(seletedSTeritoryId);
            viewBinding.sterritorySpinner.setSelection(getIndex(viewBinding.sterritorySpinner, edit_dName));
        } else {
            mkpresenter.GetSTeritoryLocal(0);
        }
        if (csv.getMarketName() != null) {
            edit_dName = csv.getMarketName();
            viewBinding.marketSpinner.setSelection(getIndex(viewBinding.marketSpinner, edit_dName));
        } else {
            mkpresenter.GetMarketLocal(0);
        }


        if (csv.getConPerson() != null) {
            viewBinding.cmistOwnerName.setText(csv.getConPerson());
        } else {
            viewBinding.cmistOwnerName.setText("---- ----");
        }
        if (csv.getVoterID() != null) {
            viewBinding.cmistNid.setText(csv.getVoterID());
        } else {
            viewBinding.cmistNid.setText("---- ----");
        }
        if (csv.getTradeLicense() != null) {
            viewBinding.cmistTradeLicense.setText(csv.getTradeLicense());
        } else {
            viewBinding.cmistTradeLicense.setText("---- ----");
        }

        if (csv.getImageBase64String() != null) {
          /*  byte[] decodedString = Base64.decode(csv.getImageBase64String(), Base64.DEFAULT);
            decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            viewBinding.imgaeView.setImageBitmap(decodedByte);*/
            Glide.with(CustomerActivity.this).load(csv.getImageBase64String()).into(viewBinding.imgaeView);
        }
        if (csv.getTradeLicenseImg() != null) {
            Glide.with(CustomerActivity.this).load(csv.getTradeLicenseImg()).into(viewBinding.timgaeView);
        }

        viewBinding.btnUpdate.setOnClickListener(v -> {
           /* String img_str = null;
            try {
                viewBinding.imgaeView.buildDrawingCache();
                bitmap = viewBinding.imgaeView.getDrawingCache();
                stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream);
                byte[] image = stream.toByteArray();
                img_str = Base64.encodeToString(image, 0);
            } catch (Exception exception) {
                exception.printStackTrace();
            }*/

            String edit_img_str = null;
            try {

                Bitmap bitmap = new Compressor(CustomerActivity.this).compressToBitmap(imgFile);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream);
                byte[] image = stream.toByteArray();
                edit_img_str = Base64.encodeToString(image, 0);
                System.out.println("img : " + edit_img_str);

            } catch (Exception exception) {
                exception.printStackTrace();
            }

            String edit_timg_str = null;
            try {

                Bitmap bitmap = new Compressor(CustomerActivity.this).compressToBitmap(timgFile);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream);
                byte[] image = stream.toByteArray();
                edit_timg_str = Base64.encodeToString(image, 0);
                System.out.println("img : " + edit_timg_str);

            } catch (Exception exception) {
                exception.printStackTrace();
            }
            if (TextUtils.isEmpty(viewBinding.customerMobile.getText().toString())) {
                SnackBarManagement._warning_CustomMessage(viewBinding.masterLayoutId, "Mobile is required");
                return;
            }
            if (viewBinding.customerMobile.getText().toString().trim().length() < 11) {
                SnackBarManagement._warning_CustomMessage(viewBinding.masterLayoutId, "Mobile Can't Be Less Than 11");
                return;
            }
            if (TextUtils.isEmpty(viewBinding.customerName.getText().toString())) {
                SnackBarManagement._warning_CustomMessage(viewBinding.masterLayoutId, "Name is required");
                return;
            }
            if (TextUtils.isEmpty(viewBinding.address.getText().toString())) {
                SnackBarManagement._warning_CustomMessage(viewBinding.masterLayoutId, "Address is required");
                return;
            }
            if (TextUtils.isEmpty(viewBinding.cmistOwnerName.getText().toString())) {
                SnackBarManagement._warning_CustomMessage(viewBinding.masterLayoutId, "Owner Name is required");
                return;
            }
            if (TextUtils.isEmpty(address)) {
                SnackBarManagement._warning_CustomMessage(viewBinding.masterLayoutId, "Your Address Getting Null.Try Again.Try Again");
                return;
            }
            if (TextUtils.isEmpty(String.valueOf(lat))) {
                SnackBarManagement._warning_CustomMessage(viewBinding.masterLayoutId, "Your Latitude Getting Null.Try Again");
                return;
            }
            if (TextUtils.isEmpty(String.valueOf(lon))) {
                SnackBarManagement._warning_CustomMessage(viewBinding.masterLayoutId, "Your Longitude Getting Null.Try Again");
                return;
            }
            if (lat == null && lon == null) {
                SnackBarManagement._warning_CustomMessage(viewBinding.masterLayoutId, "Your Latitude and Longitude Getting Null.Try Again");
                return;
            }
            SaveCustomer(custmasterid, empId, edit_img_str, edit_timg_str, "CustEdit");
        });
    }



    private void popup_tpp() {
        popupTPP = new Dialog(CustomerActivity.this);
        popupTPP.requestWindowFeature(Window.FEATURE_NO_TITLE);
        popupTPP.setCancelable(true);
        selectLocationBinding = DialogSelectLocationBinding.inflate(LayoutInflater.from(CustomerActivity.this));
        //popupDoctor.setContentView(popupView);
        popupTPP.setContentView(selectLocationBinding.getRoot());
        // popupTPP.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        popupTPP.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        //Role wise spinner populate
        try {
//            switch (roleType) {
//                case "MIO":
//                    pbinding.regiondiv.setVisibility(View.GONE);
//                    pbinding.areadiv.setVisibility(View.GONE);
//                    mpresenter.GetTeritoryLocal(0);
//                    break;
//                case "AM":
//                    pbinding.regiondiv.setVisibility(View.GONE);
//                    pbinding.areadiv.setVisibility(View.VISIBLE);
//                    mpresenter.GetAreaLocal(0);
//                    break;
//                case "DZSM":
//                case "NSM":
//                case "Admin":
//                    pbinding.regiondiv.setVisibility(View.VISIBLE);
//                    pbinding.areadiv.setVisibility(View.VISIBLE);
//                    mpresenter.GetRegionLocal(0);
//                    break;

//            }

        } catch (Exception exception) {
            exception.printStackTrace();
        }
//        selectLocationBinding.tourDate.setText(ddy);
//        LoadTourPurpose(pbinding.tourPlanPurposeSpinner);
        selectLocationBinding.btnSearch.setOnClickListener(v -> searchProviderInfo());
        selectLocationBinding.btnSelect.setOnClickListener(v -> {
            PersonChoiceAdapter adapter = (PersonChoiceAdapter) selectLocationBinding.lvPeople.getAdapter();
            if (adapter == null) {
                Toast.makeText(CustomerActivity.this, "Search and select a provider first", Toast.LENGTH_SHORT).show();
                return;
            }
            PersonInfoDAO selected = adapter.getSelectedPerson(); // নিজের getter দিয়ে selected পাবেন

            if (selected != null) {
                // Text বানিয়ে changeptype এ দেখান
                String showText = selected.oneLine();
                viewBinding.changeptype.setText(showText);
                viewBinding.customerMobile.setText(selected.Mobile);
                viewBinding.customerName.setText(selected.Name);
                viewBinding.address.setText(selected.Address);
                viewBinding.cmistOwnerName.setText(selected.OwnerName);
                if ("BSP".equals(selected.ProviderType)) {


                    viewBinding.ptypeSpinner.setSelection(getIndex(viewBinding.ptypeSpinner, "Blue Star"));
                }

  else if ("GSP".equals(selected.ProviderType)) {


                    viewBinding.ptypeSpinner.setSelection(getIndex(viewBinding.ptypeSpinner, "Green Star"));
                }
                else  if ("PSP".equals(selected.ProviderType)) {


                    viewBinding.ptypeSpinner.setSelection(getIndex(viewBinding.ptypeSpinner, "Pink Star"));
                }
else {
                    viewBinding.ptypeSpinner.setSelection(getIndex(viewBinding.ptypeSpinner, "General"));

                }


            } else {
                // কিছু select না করলে fallback
                viewBinding.changeptype.setText("Nothing selected");
            }
            popupTPP.hide();

        });
        selectLocationBinding.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupTPP.cancel();
            }
        });

        bdpresenter.GetDivisionLocal();
        //popup for tp details recyclerview
//        pbinding.tpsubmitBnt.setOnClickListener(v -> {
//            try {
//                TourPlanViewModel tv = new TourPlanViewModel();
//                tv.setTourPlanId(0);
//                tv.setMarketId(selectedMarket);
//                tv.setMarketName(selectedMarketName);
//                tv.setTPId(selectedTPP);
//                tv.setEmpInfoId(empId);
//                tv.setTourPlanDate(pbinding.tourDate.getText().toString());
//                tv.setTPName(selectedTPPName);
//                if (tpl.size() == 0) {
//                    tv.setSerialNo(1);
//                } else {
//                    tv.setSerialNo(tpl.size() + 1);
//                }
//                tv.setaCustomerMasterList(chkCustomerList);
//                tpl.add(tv);
//                SetInRecyclerviewData(tpl);
//                adapter.notifyDataSetChanged();
//
//                // chkCustomerList.clear();
//
//            } catch (Exception exception) {
//                exception.printStackTrace();
//            }
//            popupTPP.dismiss();
//        });
    }

    private void showSelectDialog() {

        popup_tpp();

        popupTPP.show();
//        View dialogView = getLayoutInflater().inflate(R.layout.dialog_select_location, null);
//
//        Spinner spDivision = dialogView.findViewById(R.id.spDivision);
//        Spinner spDistrict = dialogView.findViewById(R.id.spDistrict);
//        ListView lvPeople = dialogView.findViewById(R.id.lvPeople);
//    Button btnCancel = dialogView.findViewById(R.id.btnCancel);
//      Button btnSelect = dialogView.findViewById(R.id.btnSelect);
////
//        // Demo data — নিজের ডেটা দিন
//        //String[] divisions = {"Dhaka", "Khulna", "Rajshahi"};
//        String[] kushtiaDistricts = {"Kushtia", "Jashore", "Natore"};
//
////        ArrayAdapter<String> divAdapter = new ArrayAdapter<>(this,
////                android.R.layout.simple_spinner_dropdown_item, divisions);
////        spDivision.setAdapter(divAdapter);
//
//
//
//        ArrayAdapter<String> distAdapter = new ArrayAdapter<>(this,
//                android.R.layout.simple_spinner_dropdown_item, kushtiaDistricts);
//        spDistrict.setAdapter(distAdapter);
//
//        List<PersonInfoDAO> people = new ArrayList<>();
//        people.add(new PersonInfoDAO("Hosneara Pervin","Boro Bazar","01705159771","Kushtia Sadar"));
//        people.add(new PersonInfoDAO("Abdul Karim","Station Road","01700000001","Kushtia Sadar"));
//        people.add(new PersonInfoDAO("Rina Akter","Mirpur","01700000002","Mirpur"));
//        people.add(new PersonInfoDAO("Sujon Mia","Kumarkhali","01700000003","Kumarkhali"));
//
//        PersonChoiceAdapter adapter = new PersonChoiceAdapter(this, people);
//        lvPeople.setAdapter(adapter);
//
//        AlertDialog dialog = new AlertDialog.Builder(this)
//                .setView(dialogView)
//                .setCancelable(false)
//                .create();
////
//        btnCancel.setOnClickListener(v -> dialog.dismiss());
////
//        btnSelect.setOnClickListener(v -> {
//            int idx = adapter.getSelectedIndex();
//            if (idx >= 0) {
//                PersonInfoDAO sel = people.get(idx);
//                // এখানে যা দরকার করুন (UI তে দেখানো/ভ্যালু রিটার্ন ইত্যাদি)
//                Toast.makeText(this, "Selected: " + sel.oneLine(), Toast.LENGTH_SHORT).show();
//            } else {
//                Toast.makeText(this, "Please select one", Toast.LENGTH_SHORT).show();
//            }
//            dialog.dismiss();
//        });
//
//        dialog.show();
//        if (dialog.getWindow() != null) {
//            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
//                    ViewGroup.LayoutParams.WRAP_CONTENT);
//        }
//
//         //Division অনুযায়ী District আপডেট করতে চাইলে:
//        spDivision.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                // position দেখে নতুন district list সেট করুন
//                // spDistrict.setAdapter(new ArrayAdapter<>(...));
//            }
//            @Override public void onNothingSelected(AdapterView<?> parent) {}
//        });
    }
    private void SaveCustomer(int custmasterid, int empId, String img_str, String timg_str, String who) {
        CustomerSvModel csm = new CustomerSvModel();
        try {
            if (String.valueOf(custmasterid).isEmpty()) {
                csm.setCustomerMasterId(0);
            } else {
                csm.setCustomerMasterId(custmasterid);
            }
            csm.setImageBase64String(img_str);
           /* viewBinding.timgaeView.buildDrawingCache();
            bitmap = viewBinding.timgaeView.getDrawingCache();
            stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream);
            byte[] timage = stream.toByteArray();
            String timg_str = Base64.encodeToString(timage, 0);*/

            csm.setTradeLicenseImg(timg_str);
            csm.setTermOfPayment("Cash");
            int ptypeId=0;
          try {
              ModelProviderType ptype = (ModelProviderType) viewBinding.ptypeSpinner.getSelectedItem();
              //ProgramType ptype = (ProgramType) viewBinding.ptypeSpinner.getSelectedItem();
                ptypeId = ptype.getProviderTypeId();
          }catch (Exception ex){

          }
            int smctypeId =0;
            try {
            ModelSMCType smctype = (ModelSMCType) viewBinding.smcTypeSpinner.getSelectedItem();
              smctypeId = smctype.getSMCTypeId();
            }catch (Exception ex){

            }

            csm.setProgramTypeId(ptypeId);
            csm.setSMCTypeId(smctypeId);
            csm.setCustomerName(viewBinding.customerName.getText().toString());
            csm.setAddress(viewBinding.address.getText().toString());
            csm.setCustomerBSPCode(viewBinding.changeptype.getText().toString());
            csm.setCellNo(viewBinding.customerMobile.getText().toString());
            csm.setConPerson(viewBinding.cmistOwnerName.getText().toString());
            csm.setVoterID(viewBinding.cmistNid.getText().toString());
            csm.setTradeLicense(viewBinding.cmistTradeLicense.getText().toString());
            if (String.valueOf(selectedMarketId) == null || selectedMarketId == 0) {
                int prevmkId = cal.getCustomerSMListDao().getMarketId();
                csm.setMarketId(prevmkId);
            } else {
                csm.setMarketId(selectedMarketId);
            }
            //csm.setMarketId(selectedMarketId);
            if (lat == null && lon == null) {
                SnackBarManagement._warning_CustomMessage(viewBinding.masterLayoutId, "Your Latitude and Longitude Getting Null.Try Again");
                return;
            } else {
                csm.setLatitude(String.valueOf(lat));
                csm.setLongitude(String.valueOf(lon));
            }
            csm.setStreetAddress(address);
            csm.setEmpId(empId);
            Gson gson = new Gson();
            String data = gson.toJson(csm);
            System.out.println("save " + data);
            // Toast.makeText(this, ""+data, Toast.LENGTH_SHORT).show();
            presenter.SaveCustomer(csm, who);


        } catch (Exception ex) {
            ex.printStackTrace();
            ToastManagment.GetLongToast(CustomerActivity.this, "Some thing went wrong. Try again");
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (Constants.From.equals("78911") && resultCode == RESULT_OK) {
            imageuri = data.getData();
            if (imageuri != null) {
                viewBinding.imgaeView.setImageURI(imageuri);
                viewBinding.imgaeView.setVisibility(View.VISIBLE);
                // viewBinding.imgbg.setVisibility(View.GONE);
                path = getUriRealPathAboveKitkat(CustomerActivity.this, imageuri);
                if (path == null)
                    return;
                imgFile = new File(path);
                try {
                    imgFile = new Compressor(CustomerActivity.this).compressToFile(imgFile);
                    System.out.println("file" + imgFile);
                } catch (IOException e) {
                    Toast.makeText(CustomerActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
          /*  try {
                if (data != null) {
                    imageuri = data.getData();
                    if (imageuri != null) {
                        path = getUriRealPathAboveKitkat(CustomerActivity.this, imageuri);
                        if (path == null)
                            return;
                        imgFile = new File(path);

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }*/

         /*   try {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                viewBinding.imgaeView.setImageBitmap(photo);
                viewBinding.imgaeView.setVisibility(View.VISIBLE);
                viewBinding.imgbg.setVisibility(View.GONE);


            } catch (Exception ex) {
                Toast.makeText(CustomerActivity.this, ex.toString(), Toast.LENGTH_LONG).show();
            }*/

        }
        if (Constants.From.equals("83411") && resultCode == RESULT_OK) {
            timageuri = data.getData();
            if (timageuri != null) {
                viewBinding.timgaeView.setImageURI(timageuri);
                viewBinding.timgaeView.setVisibility(View.VISIBLE);
                // viewBinding.imgbg.setVisibility(View.GONE);
                tpath = getUriRealPathAboveKitkat(CustomerActivity.this, timageuri);
                if (tpath == null)
                    return;
                timgFile = new File(tpath);
                try {
                    timgFile = new Compressor(CustomerActivity.this).compressToFile(timgFile);
                    System.out.println("file" + timgFile);
                } catch (IOException e) {
                    Toast.makeText(CustomerActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }


           /* try {
                if (data != null) {
                    timageuri = data.getData();
                    if (timageuri != null) {
                        tpath = getUriRealPathAboveKitkat(CustomerActivity.this, timageuri);
                        if (tpath == null)
                            return;
                        timgFile = new File(tpath);

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }*/


        }

    }
    private String getUriRealPathAboveKitkat(CustomerActivity activity, Uri contentURI) {
        String ret = "";
        if (activity != null && contentURI != null) {
            if (CameraHelper.isContentUri(contentURI)) {
                //if(isGooglePhotoDoc(contentURI.getAuthority()))
                if (CameraHelper.isGooglePhotoDoc(contentURI.getAuthority())) {
                    ret = contentURI.getLastPathSegment();
                } else {
                    ret = getImageRealPath(getContentResolver(), contentURI, null);
                }
            } else if (CameraHelper.isFileUri(contentURI)) {
                ret = contentURI.getPath();
            } else if (CameraHelper.isDocumentUri(activity, contentURI)) {
                // Get uri related document id.
                String documentId = DocumentsContract.getDocumentId(contentURI);
                // Get uri authority.
                String uriAuthority = contentURI.getAuthority();
                if (CameraHelper.isMediaDoc(uriAuthority)) {
                    String[] idArr = documentId.split(":");
                    if (idArr.length == 2) {
                        // First item is document type.
                        String docType = idArr[0];
                        // Second item is document real id.
                        String realDocId = idArr[1];
                        // Get content uri by document type.
                        Uri mediaContentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                        if ("image".equals(docType)) {
                            mediaContentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                        }
                        // Get where clause with real document id.
                        String whereClause = MediaStore.Images.Media._ID + " = " + realDocId;
                        ret = getImageRealPath(getContentResolver(), mediaContentUri, whereClause);
                    }
                } else if (CameraHelper.isDownloadDoc(uriAuthority)) {
                    // Build download URI.
                    Uri downloadUri = Uri.parse("content://downloads/public_downloads");
                    // Append download document id at URI end.
                    Uri downloadUriAppendId = ContentUris.withAppendedId(downloadUri, Long.valueOf(documentId));
                    ret = getImageRealPath(getContentResolver(), downloadUriAppendId, null);
                } else if (CameraHelper.isExternalStoreDoc(uriAuthority)) {
                    String[] idArr = documentId.split(":");
                    if (idArr.length == 2) {
                        String type = idArr[0];
                        String realDocId = idArr[1];
                        if ("primary".equalsIgnoreCase(type)) {
                            ret = Environment.getExternalStorageDirectory() + "/" + realDocId;
                        }
                    }
                }
            }
        }
        return ret;
    }
    private String getImageRealPath(ContentResolver contentResolver, Uri uri, String whereClause) {
        String ret = "";
        // Query the URI with the condition.
        Cursor cursor = contentResolver.query(uri, null, whereClause, null, null);
        if (cursor != null) {
            boolean moveToFirst = cursor.moveToFirst();
            if (moveToFirst) {
                // Get columns name by URI type.
                String columnName = MediaStore.Images.Media.DATA;
                if (uri == MediaStore.Images.Media.EXTERNAL_CONTENT_URI) {
                    columnName = MediaStore.Images.Media.DATA;
                } else if (uri == MediaStore.Audio.Media.EXTERNAL_CONTENT_URI) {
                    columnName = MediaStore.Audio.Media.DATA;
                } else if (uri == MediaStore.Video.Media.EXTERNAL_CONTENT_URI) {
                    columnName = MediaStore.Video.Media.DATA;
                }
                // Get column index.
                int imageColumnIndex = cursor.getColumnIndex(columnName);
                // Get column value which is the uri related file local path.
                ret = cursor.getString(imageColumnIndex);
            }
        }
        return ret;
    }
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left,
                R.anim.slide_out_right);
    }

    @Override
    public void onProgramType(List<ProgramType> ptype) {
        /*ArrayAdapter<ProgramType> dataAdapter = new ArrayAdapter<>(CustomerActivity.this, android.R.layout.simple_spinner_item, ptype);// dbDoctor.getProgramTypeListFromSQLite(0));
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        viewBinding.ptypeSpinner.setAdapter(dataAdapter);*/
    }

    @Override
    public void onProviderType(List<ModelProviderType> ptype) {

        ModelProviderType a = new ModelProviderType();
        a.setProviderType("Select Provider Type");
        ptype.add(0, a);
        ArrayAdapter<ModelProviderType> dataAdapter = new ArrayAdapter<>(CustomerActivity.this, android.R.layout.simple_spinner_item, ptype);// dbDoctor.getProgramTypeListFromSQLite(0));
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        viewBinding.ptypeSpinner.setAdapter(dataAdapter);
    }

    @Override
    public void onSMCType(List<ModelSMCType> ptype) {
        ArrayAdapter<ModelSMCType> dataAdapter = new ArrayAdapter<>(CustomerActivity.this, android.R.layout.simple_spinner_item, ptype);// dbDoctor.getProgramTypeListFromSQLite(0));
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        viewBinding.smcTypeSpinner.setAdapter(dataAdapter);
    }

    @Override
    public void onCustomerTypeReceived(List<CustomerType> aList) {
      /*  try {
            if (aList != null) {
                ArrayAdapter<CustomerType> dataAdapter = new ArrayAdapter<>(CustomerActivity.this, android.R.layout.simple_spinner_item, aList);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                viewBinding.custTypeSpinner.setAdapter(dataAdapter);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }*/

    }

    @Override
    public void onStationReceived(List<StationType> aList) {
      /*  try {
            if (aList != null) {
                ArrayAdapter<StationType> dataAdapter = new ArrayAdapter<>(CustomerActivity.this, android.R.layout.simple_spinner_item, aList);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                viewBinding.custStationSpinner.setAdapter(dataAdapter);
            }

        } catch (Exception exception) {
            exception.printStackTrace();
        }*/

    }

    @Override
    public void onSubmitSuccess(String mesg, String who) {
        if (who.equals("AddCust")) {
            new androidx.appcompat.app.AlertDialog.Builder(CustomerActivity.this)
                    .setTitle("Success")
                    .setMessage(mesg)
                    .setPositiveButton("OK", (dialog, which) -> {
                        dialog.cancel();
                        Intent i = new Intent(CustomerActivity.this, MainDashboardActivity.class);
                        i.addFlags(i.FLAG_ACTIVITY_CLEAR_TOP | i.FLAG_ACTIVITY_CLEAR_TASK | i.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    }).setCancelable(false).show();
        }
        if (who.equals("CustEdit")) {
            new androidx.appcompat.app.AlertDialog.Builder(CustomerActivity.this)
                    .setTitle("Success")
                    .setMessage("Customer Updated")
                    .setPositiveButton("OK", (dialog, which) -> {
                        dialog.cancel();
                        Intent i = new Intent(CustomerActivity.this, CustomerApprovalListActivity.class);
                        // i.addFlags(i.FLAG_ACTIVITY_CLEAR_TOP | i.FLAG_ACTIVITY_CLEAR_TASK | i.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                        finish();
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    }).setCancelable(false).show();
        }


    }

    @Override
    public void onSubmitError(String mesg) {
        new androidx.appcompat.app.AlertDialog.Builder(CustomerActivity.this)
                .setTitle("Error")
                .setMessage(mesg)
                .setPositiveButton("OK", (dialog, which) -> dialog.cancel()).setCancelable(false).show();
    }

    @Override
    public void vGroup(List<Group> groupList) {
    }

    @Override
    public void vRegion(List<Region> regionList) {
        try {
            if (regionList != null) {
                ArrayAdapter<Region> dataAdapter = new ArrayAdapter<>(CustomerActivity.this, android.R.layout.simple_spinner_item, regionList);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                viewBinding.regionSpinner.setAdapter(dataAdapter);
                // dbDoctor.getProgramTypeListFromSQLite(0);
            }
       /*     viewBinding.regionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    Region region = (Region) parent.getSelectedItem();
                    seletedRegionId = region.getRegionId();
                    mkpresenter.GetAreaLocal(seletedRegionId);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });*/


        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void vArea(List<Area> areaList) {
        try {
            if (areaList != null) {
                ArrayAdapter<Area> dataAdapter = new ArrayAdapter<>(CustomerActivity.this, android.R.layout.simple_spinner_item, areaList);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                viewBinding.areaSpinner.setAdapter(dataAdapter);
                //  dbDoctor.getProgramTypeListFromSQLite(0);
            }
           /* viewBinding.areaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    Area area = (Area) parent.getSelectedItem();
                    seletedAreaId = area.getAreaId();
                    mkpresenter.GetTeritoryLocal(seletedAreaId);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });*/


        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void vTeritory(List<Teritorry> teritoryList) {
        try {
            if (teritoryList != null) {
                ArrayAdapter<Teritorry> dataAdapter = new ArrayAdapter<>(CustomerActivity.this, android.R.layout.simple_spinner_item, teritoryList);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                viewBinding.territorySpinner.setAdapter(dataAdapter);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void vSTeritory(List<SubTeritorry> steritorryList) {
        String steri = gson.toJson(steritorryList);
        System.out.println(steri);
        try {
            if (steritorryList != null) {
                ArrayAdapter<SubTeritorry> dataAdapter = new ArrayAdapter<>(CustomerActivity.this, android.R.layout.simple_spinner_item, steritorryList);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                viewBinding.sterritorySpinner.setAdapter(dataAdapter);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void vMarket(List<Market> marketList) {
        try {
            if (marketList != null) {
                ArrayAdapter<Market> dataAdapter = new ArrayAdapter<>(CustomerActivity.this, android.R.layout.simple_spinner_item, marketList);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                viewBinding.marketSpinner.setAdapter(dataAdapter);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
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
                                ActivityCompat.requestPermissions(CustomerActivity.this,
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
        LocationManager lm = (LocationManager) CustomerActivity.this.getSystemService(Context.LOCATION_SERVICE);
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
            new AlertDialog.Builder(CustomerActivity.this)
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
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 500, 5, (LocationListener) this);
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
            address = addresses.get(0).getAddressLine(0);
        } catch (Exception e) {
        }
    }

    private int getIndex(Spinner designationSpinner, String edit_dname) {
        for (int i = 0; i < designationSpinner.getCount(); i++) {
            if (designationSpinner.getItemAtPosition(i).toString().equalsIgnoreCase(edit_dname)) {
                return i;
            }
        }
        return 0;
    }

    private InputFilter filter = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            if (source != null && blockCharacterSet.contains(("" + source))) {
                return "";
            }
            return null;
        }
    };

    @Override
    public void vDivL(List<DivisionVM> divList) {
        try {
            if (divList != null) {
                DivisionVM a = new DivisionVM();
                a.setDivisionName("Select Division");
                divList.add(0, a);
                ArrayAdapter<DivisionVM> dataAdapter = new ArrayAdapter<>(CustomerActivity.this, android.R.layout.simple_spinner_item, divList);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                selectLocationBinding.spDivision.setAdapter(dataAdapter);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        selectLocationBinding.spDivision.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                DivisionVM area = (DivisionVM) parent.getSelectedItem();
                selectedDiviId = area.getDivisionId();
                selectedDistId = 0;
                selectedThanaId = 0;
                selectedThanaName = "";
                selectLocationBinding.lvPeople.setAdapter(null);
                bdpresenter.GetDistrictLocal(selectedDiviId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void vDisL(List<DistrictVM> disList) {
        try {
            if (disList != null) {
                DistrictVM a = new DistrictVM();
                a.setDistrictName("Select District");
                disList.add(0, a);
                ArrayAdapter<DistrictVM> dataAdapter = new ArrayAdapter<>(CustomerActivity.this, android.R.layout.simple_spinner_item, disList);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                selectLocationBinding.spDistrict.setAdapter(dataAdapter);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        selectLocationBinding.spDistrict.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                DistrictVM area = (DistrictVM) parent.getSelectedItem();
                selectedDistId = area.getDistrictId();
                selectedThanaId = 0;
                selectedThanaName = "";
                selectLocationBinding.lvPeople.setAdapter(null);

                bdpresenter.GetThanaLocal(selectedDistId);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });


    }

    @Override
    public void vThanaL(List<ThanaVM> thanaList) {
        try {
            if (thanaList != null) {
                ThanaVM a = new ThanaVM();
                a.setThanaName("Select Upazila....");
                thanaList.add(0, a);
                ArrayAdapter<ThanaVM> dataAdapter = new ArrayAdapter<>(CustomerActivity.this, android.R.layout.simple_spinner_item, thanaList);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                selectLocationBinding.spThana.setAdapter(dataAdapter);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        selectLocationBinding.spThana.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ThanaVM area = (ThanaVM) parent.getSelectedItem();
                selectedThanaId = area.getThanaId();
                selectedThanaName = selectedThanaId == 0 ? "" : area.getThanaName();
                selectLocationBinding.lvPeople.setAdapter(null);
/*

                
                    @Override
                    public void onResponse(Call<List<PersonInfoDAO>> call, Response<List<PersonInfoDAO>> response) {
                        // ৩ সেকেন্ড দেরি করে হ্যান্ডেল করা হচ্ছে
                        new Handler(Looper.getMainLooper()).postDelayed(() -> {
                            progressDoalog.dismiss();
                            if (response.isSuccessful() && response.body() != null) {
                                List<PersonInfoDAO> people = response.body();
                                if (people.size() > 0) {
                                    PersonChoiceAdapter adapter = new PersonChoiceAdapter(CustomerActivity.this, people);
                                    selectLocationBinding.lvPeople.setAdapter(adapter);
                                } else {
                                    selectLocationBinding.lvPeople.setAdapter(null);
                                    Toast.makeText(CustomerActivity.this, "No data found", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                selectLocationBinding.lvPeople.setAdapter(null);
                                Toast.makeText(CustomerActivity.this, "No data found", Toast.LENGTH_SHORT).show();
                            }
                        }, 3000); // ৩ সেকেন্ড delay
                    }

                    @Override
                    public void onFailure(Call<List<PersonInfoDAO>> call, Throwable t) {
                        progressDoalog.dismiss();
                        Toast.makeText(CustomerActivity.this, "Failed to load data", Toast.LENGTH_SHORT).show();
                        Log.e("API", t.toString());
                    }
                });
*/
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

    }

    private void searchProviderInfo() {
        if (selectedDiviId == 0 || selectedDistId == 0 || selectedThanaId == 0 || TextUtils.isEmpty(selectedThanaName)) {
            Toast.makeText(this, "Please select division, district and upazila", Toast.LENGTH_SHORT).show();
            selectLocationBinding.lvPeople.setAdapter(null);
            return;
        }

        ProgressDialog progressDoalog = new ProgressDialog(CustomerActivity.this);
        progressDoalog.setMessage("Data Loading.... Please wait");
        progressDoalog.show();
        progressDoalog.setCanceledOnTouchOutside(false);

        ApiCustomerCall service = RetrofitClientInstanceIntegration.getRetrofitInstance().create(ApiCustomerCall.class);
        Call<List<PersonInfoDAO>> call = service.GetProviderInfoIntrigration(
                selectedThanaName.trim(),
                INTEGRATION_USER_ID,
                INTEGRATION_PASS
        );

        call.enqueue(new Callback<List<PersonInfoDAO>>() {
            @Override
            public void onResponse(Call<List<PersonInfoDAO>> call, Response<List<PersonInfoDAO>> response) {
                progressDoalog.dismiss();
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    PersonChoiceAdapter adapter = new PersonChoiceAdapter(CustomerActivity.this, response.body());
                    selectLocationBinding.lvPeople.setAdapter(adapter);
                    return;
                }

                selectLocationBinding.lvPeople.setAdapter(null);
                Toast.makeText(CustomerActivity.this, "No data found", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<List<PersonInfoDAO>> call, Throwable t) {
                progressDoalog.dismiss();
                selectLocationBinding.lvPeople.setAdapter(null);
                Toast.makeText(CustomerActivity.this, "Failed to load data", Toast.LENGTH_SHORT).show();
                Log.e("IntegrationAPI", "Provider search failed", t);
            }
        });
    }
}
