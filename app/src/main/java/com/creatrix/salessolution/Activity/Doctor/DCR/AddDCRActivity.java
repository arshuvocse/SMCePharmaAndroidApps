package com.creatrix.salessolution.Activity.Doctor.DCR;

import android.Manifest;
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
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.creatrix.salessolution.Activity.Customer.CustomerActivity;
import com.creatrix.salessolution.Activity.Doctor.DoctorDashboardActivity;
import com.creatrix.salessolution.DBAdapter.DBCrudHelper;
import com.creatrix.salessolution.Interface.IDCR;
import com.creatrix.salessolution.Model.DcrSM;
import com.creatrix.salessolution.Model.Doctor.DoctorBrand;
import com.creatrix.salessolution.Model.Doctor.DoctorChamberName;
import com.creatrix.salessolution.Model.Doctor.DoctorListViewModel;
import com.creatrix.salessolution.Model.Gift;
import com.creatrix.salessolution.Model.ProductSample;
import com.creatrix.salessolution.Model.TourTypeViewModel;
import com.creatrix.salessolution.Model.UserByRole;
import com.creatrix.salessolution.Model.UserRole;
import com.creatrix.salessolution.Presenter.DCRPresenter;
import com.creatrix.salessolution.R;
import com.creatrix.salessolution.RecyclerAdapter._VisitedWithRecylcer;
import com.creatrix.salessolution.RecyclerAdapter._product_brandAdapter;
import com.creatrix.salessolution.RecyclerAdapter._product_giftAdapter;
import com.creatrix.salessolution.RecyclerAdapter._product_sampleAdapter;
import com.creatrix.salessolution.Services.Constants;
import com.creatrix.salessolution.UtilityHelper.NetworkInformation;
import com.creatrix.salessolution.UtilityHelper.SessionManagement;
import com.creatrix.salessolution.UtilityHelper.SnackBarManagement;
import com.creatrix.salessolution.databinding.ActivityAddDCRBinding;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static com.creatrix.salessolution.Activity.Attendance.AttendanceActivity.MY_PERMISSIONS_REQUEST_LOCATION;

public class AddDCRActivity extends AppCompatActivity implements LocationListener, IDCR.View, _product_giftAdapter.QtyListener, _product_sampleAdapter.QtyPListener, _VisitedWithRecylcer.DltListener {
    ActivityAddDCRBinding binding;
    private static final String TAG = "AddDCRActivity";
    SessionManagement session;
    IDCR.Presenter presenter;
    DBCrudHelper dbCrudHelper;

    _product_giftAdapter mAdapterPoduct_Gift;
    _product_sampleAdapter mAdapterPoduct_Sample;
    _product_brandAdapter mAdapterPoduct_Brand;
    _VisitedWithRecylcer aVistiedAdapter;

    List<Gift> aProList_Gift = new ArrayList<>();
    String[] listItemProduct_Gift;
    boolean[] checkedItems_Gift;

    ArrayList<Integer> mUserItems_Gift = new ArrayList<>();
    List<Gift> aGiftList = new ArrayList<>();
    List<Gift> aFinalGiftList = new ArrayList<>();


    List<ProductSample> aProList_Sample = new ArrayList<>();
    String[] listItemProduct_Sample;
    boolean[] checkedItems_Sample;

    ArrayList<Integer> mUserItems_Sample = new ArrayList<>();
    List<ProductSample> aFinalProductList_Sample = new ArrayList<>();


    List<DoctorBrand> aProList_Brand = new ArrayList<>();
    String[] listItemBrand;
    boolean[] checkedItems_Brand;
    ArrayList<Integer> mUserItems_Brand = new ArrayList<>();
    List<DoctorBrand> aBrandList = new ArrayList<>();

    List<TourTypeViewModel> aTourTypeLIst;
    List<DoctorChamberName> aChamberList;

    List<UserByRole> aVisitedList = new ArrayList<>();
    String empId, roletypeid, entryTime, empName, empCode;

    int is_noneffectve;
    int selectedreasonId;
    int dcrLocalId;
    String edit_dName;
    String lat, lon, setStreetAddress;
    LocationManager locationManager;
    int giftQty, giftID;

    private final LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(@NonNull LocationResult locationResult) {
            super.onLocationResult(locationResult);
            locationResult.getLastLocation();

            double latitude = locationResult.getLastLocation().getLatitude();
            double longitude = locationResult.getLastLocation().getLongitude();
            setStreetAddress = getCompleteAddressString(latitude, longitude);
            lat = String.valueOf(latitude);
            lon = String.valueOf(longitude);
            binding.latlon.setText(lat + "," + lon);

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddDCRBinding.inflate(getLayoutInflater());
        //setContentView(R.layout.activity_add_d_c_r);
        setContentView(binding.getRoot());
        dbCrudHelper = new DBCrudHelper(AddDCRActivity.this);
        binding.toolbarCustom.setNavigationOnClickListener(v -> finish());
        //imgaeView = findViewById(R.id.imgaeView);
        presenter = new DCRPresenter(this, this);
        session = new SessionManagement(getApplicationContext());
        session.checkLogin();
        HashMap<String, String> user = session.getUserDetails();
        empId = user.get(SessionManagement.KEY_EmpId);
        roletypeid = user.get(SessionManagement.KEY_EmpRoleTypeId);
        empName = user.get(SessionManagement.KEY_UserName);
        empCode = user.get(SessionManagement.KEY_EmpMasterCode);

        if (ContextCompat.checkSelfPermission(AddDCRActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(AddDCRActivity.this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(AddDCRActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
        }
        locationManager = (LocationManager) AddDCRActivity.this.getSystemService(Context.LOCATION_SERVICE);

        if (!NetworkInformation.isConnected(this)) {
            checkLocationPermission();
            locationEnabled();
            getCLocation();
        } else {
        }

        Gson gson = new Gson();
        DoctorListViewModel aInfoData = gson.fromJson(getIntent().getStringExtra("myjson"), DoctorListViewModel.class);
        switch (Constants.WHO) {
            case "PendingDcrAdapter":
                DcrSM pData = gson.fromJson(getIntent().getStringExtra("dcrDtails"), DcrSM.class);
                dcrLocalId = pData.getDcrId();
                binding.dcrdate.setText(pData.getDcrDate());
                binding.Save.setVisibility(View.VISIBLE);
                //  binding.visitedWithAdd.setVisibility(View.GONE);
                //   binding.sampleAdd.setVisibility(View.GONE);
                //   binding.giftAdd.setVisibility(View.GONE);
                binding.noneffectdiv.setVisibility(View.GONE);

                presenter.GetVisitType();
                presenter.GetChamber(aInfoData.getDoctorId());
                presenter.GetGiftProduct(empId);
                presenter.GetSampleProduct(empId);
                presenter.GetDoctorBrand(pData.getDoctorId());

                if (pData.getVisitTypeName() != null) {
                    edit_dName = pData.getVisitTypeName();
                    binding.spinerVisitType.setSelection(getIndex(binding.spinerVisitType, edit_dName));
                } else {
                }
                if (pData.getDoclist().getChemberName() != null) {
                    edit_dName = pData.getDoclist().getChemberName();
                    binding.chamberSpinner.setSelection(getIndex(binding.chamberSpinner, edit_dName));
                } else {
                }
                aVisitedList = pData.getAempList();
                if (aVisitedList != null) {
                    aVistiedAdapter = new _VisitedWithRecylcer(AddDCRActivity.this, aVisitedList, this::onDltClick);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                    binding.recyclerViewVisitedWith.setLayoutManager(mLayoutManager);
                    binding.recyclerViewVisitedWith.setItemAnimator(new DefaultItemAnimator());
                    binding.recyclerViewVisitedWith.setAdapter(aVistiedAdapter);
                    aVistiedAdapter.notifyDataSetChanged();
                }

                aBrandList = pData.getDoctorBrand();
                if (aBrandList != null) {
                    mAdapterPoduct_Brand = new _product_brandAdapter(AddDCRActivity.this, aBrandList);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                    binding.recyclerViewBrand.setLayoutManager(mLayoutManager);
                    binding.recyclerViewBrand.setItemAnimator(new DefaultItemAnimator());
                    binding.recyclerViewBrand.setAdapter(mAdapterPoduct_Brand);
                    mAdapterPoduct_Brand.notifyDataSetChanged();
                }

                aGiftList = pData.getGiftList();
                if (aGiftList != null) {
                    mAdapterPoduct_Gift = new _product_giftAdapter(aGiftList, this);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                    binding.recyclerViewGift.setLayoutManager(mLayoutManager);
                    binding.recyclerViewGift.setItemAnimator(new DefaultItemAnimator());
                    binding.recyclerViewGift.setAdapter(mAdapterPoduct_Gift);
                    mAdapterPoduct_Gift.notifyDataSetChanged();

                    // aProList_Gift = aGiftList;
                  /*  listItemProduct_Gift = new String[aGiftList.size()];
                    try {

                        for(int i=0;i<aGiftList.size();i++)
                        {
                            int pos=aGiftList.get(i).getPosition();
                            Toast.makeText(this, "lp "+pos, Toast.LENGTH_SHORT).show();
                            mUserItems_Gift.add(pos);
                        }
                        checkedItems_Gift = new boolean[listItemProduct_Gift.length];
                        for (int i = 0; i < checkedItems_Gift.length; i++) {
                            checkedItems_Gift[i] = true;
                            //mUserItems_Gift.clear();
                        }
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }*/

                 /*   try {
                            aProList_Gift = aGiftList;
                            listItemProduct_Gift = new String[aProList_Gift.size()];
                            for (int i = 0; i < aProList_Gift.size(); i++) {
                                listItemProduct_Gift[i] = aProList_Gift.get(i).getProductName();
                            }
                            checkedItems_Gift = new boolean[listItemProduct_Gift.length];


                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }*/
                }

                aFinalProductList_Sample = pData.getSampleList();
                if (aFinalProductList_Sample != null) {

                    mAdapterPoduct_Sample = new _product_sampleAdapter(aFinalProductList_Sample, this);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                    binding.recyclerViewSample.setLayoutManager(mLayoutManager);
                    binding.recyclerViewSample.setItemAnimator(new DefaultItemAnimator());
                    binding.recyclerViewSample.setAdapter(mAdapterPoduct_Sample);
                    mAdapterPoduct_Sample.notifyDataSetChanged();
                }
                binding.comment.setEnabled(false);
                binding.comment.setText(pData.getRemarks());

                break;
            case "DoclitAdapter":
                binding.dcrdate.setText(new SimpleDateFormat("dd-MMM-yyyy hh:mm a", Locale.getDefault()).format(new Date()));
                try {
                    presenter.GetVisitType();
                    presenter.GetChamber(aInfoData.getDoctorId());
                    presenter.GetGiftProduct(empId);
                    presenter.GetSampleProduct(empId);
                    presenter.GetDoctorBrand(aInfoData.getDoctorId());
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
                break;
        }
        try {
            binding.dochead.nameTxt.setText(aInfoData.getDoctorName());
            binding.dochead.mobileTxt.setText(aInfoData.getDocContact());
            binding.dochead.doctypeTxt.setText(aInfoData.getDoctorTypeName());
            binding.dochead.programTxt.setText(aInfoData.getProgramTypeName());
            binding.checkbox.setOnClickListener(view -> {
                if (((CheckBox) view).isChecked()) {
                    is_noneffectve = 1;
                    presenter.GetNoneffective();
                    binding.reasondiv.setVisibility(View.VISIBLE);
                    binding.Save.setVisibility(View.GONE);
                } else {
                    binding.reasondiv.setVisibility(View.GONE);
                    is_noneffectve = 0;
                    selectedreasonId = 0;
                    binding.Save.setVisibility(View.VISIBLE);
                }
            });


//            addressTxt.setText(aInfoData.getChamberAddress());
        } catch (Exception ex) {
            Log.e(TAG, "onCreate: ", ex);
        }
        binding.brandAdd.setOnClickListener(v -> showDialog_Brand());
        binding.sampleAdd.setOnClickListener(v -> showDialog_Sample());
        binding.giftAdd.setOnClickListener(v -> showDialog_Gift());
        binding.visitedWithAdd.setOnClickListener(v -> presenter.GetUserRole());

        entryTime = new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(new Date());

        binding.latlon.setText(lat + "," + lon);
        binding.submitbtnClick.setOnClickListener(v -> {
            if (is_noneffectve == 1 && String.valueOf(selectedreasonId).isEmpty()) {
                SnackBarManagement._error_CustomMessage(findViewById(android.R.id.content), "Reason is Mandatory");
                return;
            }
            if (aVisitedList.size() == 0) {
                SnackBarManagement._error_CustomMessage(findViewById(android.R.id.content), "Visited With is Required");
                return;
            }
            SubmitDCR(aInfoData, empId);
        });
        binding.Save.setOnClickListener(v -> {
            if (aVisitedList.size() == 0) {
                SnackBarManagement._error_CustomMessage(findViewById(android.R.id.content), "Visited With is Required");
                return;
            }
            SaveDCR(aInfoData, empId);

        });
        binding.location.setOnClickListener(view -> {
            binding.latlon.setText("Loading...");
            getCLocation();
        });
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
                                ActivityCompat.requestPermissions(AddDCRActivity.this,
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
        LocationManager lm = (LocationManager) AddDCRActivity.this.getSystemService(Context.LOCATION_SERVICE);
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
            new AlertDialog.Builder(AddDCRActivity.this)
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
            LocationRequest locationRequest = new LocationRequest();
            // locationRequest.setInterval(300000);
            locationRequest.setInterval(300000);
            locationRequest.setFastestInterval(180000);
            locationRequest.setSmallestDisplacement(10.2f);
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            LocationServices.getFusedLocationProviderClient(AddDCRActivity.this)
                    .requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
        } catch (Exception e) {
            e.printStackTrace();
        }
      /*  try {
            locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 500, 5, (LocationListener) this);
        } catch (SecurityException e) {
            e.printStackTrace();
        }*/
    }

    public void showDialog_Gift() {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(AddDCRActivity.this);
        mBuilder.setTitle("Select Promo Gift");
        mBuilder.setMultiChoiceItems(listItemProduct_Gift, checkedItems_Gift, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int position, boolean isChecked) {
                if (isChecked) {
                    mUserItems_Gift.add(position);
                } else {
                    mUserItems_Gift.remove((Integer.valueOf(position)));
                }
            }
        });
        mBuilder.setCancelable(false);
        mBuilder.setPositiveButton("Done", (dialogInterface, which) -> {
            String item = "";
            aGiftList.clear();
            try {
                if (mUserItems_Gift != null) {
                    for (int i = 0; i < mUserItems_Gift.size(); i++) {
                        int pos = mUserItems_Gift.get(i);
                        Gift g = new Gift();
                        g.setPosition(pos);
                        g.setProductId(aProList_Gift.get(pos).getProductId());
                        g.setProductName(aProList_Gift.get(pos).getProductName());
                        aGiftList.add(g);

                        item = item + listItemProduct_Gift[mUserItems_Gift.get(i)];
                        if (i != mUserItems_Gift.size() - 1) {
                            item = item + ",";
                        }
                    }

                    if (aGiftList != null) {
                        mAdapterPoduct_Gift = new _product_giftAdapter(aGiftList, AddDCRActivity.this);
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                        binding.recyclerViewGift.setLayoutManager(mLayoutManager);
                        binding.recyclerViewGift.setItemAnimator(new DefaultItemAnimator());
                        binding.recyclerViewGift.setAdapter(mAdapterPoduct_Gift);
                        mAdapterPoduct_Gift.notifyDataSetChanged();
                    }


                }


            } catch (Exception ex) {
                throw ex;
            }


        });

        mBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        mBuilder.setNeutralButton("Clear All", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                for (int i = 0; i < checkedItems_Gift.length; i++) {
                    aGiftList.clear();
                    // mAdapterPoduct_Gift.notifyDataSetChanged();
                    checkedItems_Gift[i] = false;
                    mUserItems_Gift.clear();
                }
            }
        });

        AlertDialog mDialog = mBuilder.create();
        mDialog.show();


    }

    public void showDialog_Sample() {

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(AddDCRActivity.this);
        mBuilder.setTitle("Select Sample ");
        mBuilder.setMultiChoiceItems(listItemProduct_Sample, checkedItems_Sample, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int position, boolean isChecked) {
                if (isChecked) {
                    mUserItems_Sample.add(position);
                } else {
                    mUserItems_Sample.remove((Integer.valueOf(position)));
                }
            }
        });
        mBuilder.setCancelable(false);
        mBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                String item = "";
                aFinalProductList_Sample.clear();
                try {
                    if (mUserItems_Sample != null) {
                        for (int i = 0; i < mUserItems_Sample.size(); i++) {
                            int pos = mUserItems_Sample.get(i);
                            aFinalProductList_Sample.add(aProList_Sample.get(pos));
                            item = item + listItemProduct_Sample[mUserItems_Sample.get(i)];
                            if (i != mUserItems_Sample.size() - 1) {
                                item = item + ",";
                            }
                        }

                        if (aFinalProductList_Sample != null) {

                            mAdapterPoduct_Sample = new _product_sampleAdapter(aFinalProductList_Sample, AddDCRActivity.this);
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                            binding.recyclerViewSample.setLayoutManager(mLayoutManager);
                            binding.recyclerViewSample.setItemAnimator(new DefaultItemAnimator());
                            binding.recyclerViewSample.setAdapter(mAdapterPoduct_Sample);
                            mAdapterPoduct_Sample.notifyDataSetChanged();
                        }


                    }


                } catch (Exception ex) {
                    throw ex;
                }


            }
        });

        mBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        mBuilder.setNeutralButton("Clear All", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                for (int i = 0; i < checkedItems_Sample.length; i++) {
                    aFinalProductList_Sample.clear();
                    mAdapterPoduct_Sample.notifyDataSetChanged();
                    checkedItems_Sample[i] = false;
                    mUserItems_Sample.clear();
                }
            }
        });

        AlertDialog mDialog = mBuilder.create();
        mDialog.show();


    }

    public void showDialog_Brand() {

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(AddDCRActivity.this);
        mBuilder.setTitle("Select Brand ");
        mBuilder.setMultiChoiceItems(listItemBrand, checkedItems_Brand, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int position, boolean isChecked) {
                if (isChecked) {
                    mUserItems_Brand.add(position);
                } else {
                    mUserItems_Brand.remove((Integer.valueOf(position)));
                }
            }
        });
        mBuilder.setCancelable(false);
        mBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                String item = "";
                aBrandList.clear();
                try {
                    if (mUserItems_Brand != null) {
                        for (int i = 0; i < mUserItems_Brand.size(); i++) {
                            int pos = mUserItems_Brand.get(i);
                            aBrandList.add(aProList_Brand.get(pos));
                            item = item + listItemBrand[mUserItems_Brand.get(i)];
                            if (i != mUserItems_Brand.size() - 1) {
                                item = item + ",";
                            }
                        }

                        if (aBrandList != null) {

                            mAdapterPoduct_Brand = new _product_brandAdapter(AddDCRActivity.this, aBrandList);
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                            binding.recyclerViewBrand.setLayoutManager(mLayoutManager);
                            binding.recyclerViewBrand.setItemAnimator(new DefaultItemAnimator());
                            binding.recyclerViewBrand.setAdapter(mAdapterPoduct_Brand);
                            mAdapterPoduct_Brand.notifyDataSetChanged();
                        }


                    }


                } catch (Exception ex) {
                    throw ex;
                }


            }
        });

        mBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        mBuilder.setNeutralButton("Clear All", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                for (int i = 0; i < checkedItems_Brand.length; i++) {
                    aBrandList.clear();
                    /*int pos=0;
                    {
                        pos=aBrandList.size();
                    }*/
                    mAdapterPoduct_Brand.notifyDataSetChanged();
                    checkedItems_Brand[i] = false;
                    mUserItems_Brand.clear();
                }
            }
        });

        AlertDialog mDialog = mBuilder.create();
        mDialog.show();


    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left,
                R.anim.slide_out_right);
    }

    @Override
    public void OnVisitTypeGet(List<TourTypeViewModel> aInfo) {
        try {
            if (aInfo != null) {
                aTourTypeLIst = aInfo;
                ArrayAdapter<TourTypeViewModel> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, aInfo);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                binding.spinerVisitType.setAdapter(dataAdapter);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }


    }

    @Override
    public void OnChamberGet(List<DoctorChamberName> aInfo) {
        try {
            if (aInfo != null) {
                aChamberList = aInfo;
                ArrayAdapter<DoctorChamberName> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, aInfo);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                binding.chamberSpinner.setAdapter(dataAdapter);

            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }


    @Override
    public void OnGiftProductGet(List<Gift> aList) {
        try {
            if (aList != null) {
                aProList_Gift = aList;
                listItemProduct_Gift = new String[aList.size()];
                for (int i = 0; i < aList.size(); i++) {
                    listItemProduct_Gift[i] = aList.get(i).getProductName();
                }
                checkedItems_Gift = new boolean[listItemProduct_Gift.length];

            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }

    }

    @Override
    public void OnSampleProductGet(List<ProductSample> aList) {
        try {
            if (aList != null) {
                aProList_Sample = aList;
                listItemProduct_Sample = new String[aList.size()];
                for (int i = 0; i < aList.size(); i++) {
                    listItemProduct_Sample[i] = aList.get(i).getProductName();
                }
                checkedItems_Sample = new boolean[listItemProduct_Sample.length];

            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }

    }

    @Override
    public void OnDoctorBrandGet(List<DoctorBrand> aInfo) {
        try {
            if (aInfo != null) {
                aProList_Brand = aInfo;
                listItemBrand = new String[aInfo.size()];
                for (int i = 0; i < aInfo.size(); i++) {
                    listItemBrand[i] = aInfo.get(i).getBrandName();
                }
                checkedItems_Brand = new boolean[listItemBrand.length];

            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }


    @Override
    public void onUserRoleGet(List<UserRole> aList) {
        if (aList != null) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(AddDCRActivity.this);
            // Set the alert dialog title
            builder.setTitle("Choose Role");

            // Initializing an array of flowers
            final String[] aRoleList = new String[aList.size()];
            for (int i = 0; i < aList.size(); i++) {
                aRoleList[i] = aList.get(i).getRoleName();
            }
            builder.setSingleChoiceItems(
                    aRoleList, // Items list
                    -1, // Index of checked item (-1 = no selection)
                    new DialogInterface.OnClickListener() // Item click listener
                    {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            String selectedItem = Arrays.asList(aRoleList).get(i);
                            UserRole ads = aList.get(i);
                            if (ads.getRoleName().equals("Self")) {
                                //  List<UserByRole> aSelfList = new ArrayList<>();

                                aVisitedList = new ArrayList<>();
                                UserByRole user = new UserByRole();
                                user.setEmpName(empName);
                                user.setEmpMasterCode(empCode);
                                user.setEmpInfoId(Integer.parseInt(empId));
                                user.setUserRoleID(Integer.parseInt(roletypeid));
                                aVisitedList.add(user);
                                setSelf(aVisitedList);
                            } else {
                                LoadUserByRoleId(ads.getUserRoleID()/*, Integer.parseInt(empId)*/);
                            }
                            dialogInterface.dismiss();
                        }
                    });

            // Set the a;ert dialog positive button
            builder.setPositiveButton("Close", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            });
            // Create the alert dialog
            AlertDialog dialog = builder.create();
            // Finally, display the alert dialog
            dialog.show();
        }
    }

    private void LoadUserByRoleId(int userRoleID/*, int parseInt*/) {
        presenter.GetUserByRoleId(userRoleID/*, parseInt*/);
    }

    private void setSelf(List<UserByRole> aSelfList) {
        try {
            if (aSelfList != null) {
                if (aSelfList != null) {
                    aVistiedAdapter = new _VisitedWithRecylcer(AddDCRActivity.this, aSelfList, AddDCRActivity.this::onDltClick);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                    binding.recyclerViewVisitedWith.setLayoutManager(mLayoutManager);
                    binding.recyclerViewVisitedWith.setItemAnimator(new DefaultItemAnimator());
                    binding.recyclerViewVisitedWith.setAdapter(aVistiedAdapter);
                    aVistiedAdapter.notifyDataSetChanged();
                }
            }


        } catch (Exception ex) {
            throw ex;
        }
    }

    @Override
    public void onUserGet(List<UserByRole> aList) {
        if (aList != null) {
            final ArrayList<Integer> aCheckedList = new ArrayList<>();
            final String[] nameList = new String[aList.size()];
            for (int i = 0; i < aList.size(); i++) {
                nameList[i] = aList.get(i).getEmpName();
            }
            final boolean[] chItems = new boolean[nameList.length];
            AlertDialog.Builder mBuilder = new AlertDialog.Builder(AddDCRActivity.this);
            mBuilder.setTitle("Select");
            mBuilder.setMultiChoiceItems(nameList, chItems, new DialogInterface.OnMultiChoiceClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int position, boolean isChecked) {
                    if (isChecked) {
                        aCheckedList.add(position);
                    } else {
                        aCheckedList.remove((Integer.valueOf(position)));
                    }
                }
            });
            mBuilder.setCancelable(false);
            mBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int which) {
                    String item = "";
                    try {
                        if (aCheckedList != null) {
                            for (int i = 0; i < aCheckedList.size(); i++) {
                                int pos = aCheckedList.get(i);
                                aVisitedList.add(aList.get(pos));
                            }

                            if (aVisitedList != null) {
                                aVistiedAdapter = new _VisitedWithRecylcer(AddDCRActivity.this, aVisitedList, AddDCRActivity.this::onDltClick);
                                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                                binding.recyclerViewVisitedWith.setLayoutManager(mLayoutManager);
                                binding.recyclerViewVisitedWith.setItemAnimator(new DefaultItemAnimator());
                                binding.recyclerViewVisitedWith.setAdapter(aVistiedAdapter);
                                aVistiedAdapter.notifyDataSetChanged();
                            }
                        }


                    } catch (Exception ex) {
                        throw ex;
                    }
                }
            });

            mBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });


            AlertDialog mDialog = mBuilder.create();
            mDialog.show();

        }

    }

    @Override
    public void onNoneffectiveGet(List<NonEffectiveReason> aList) {
        try {
            if (aList != null) {
                // Toast.makeText(this, "data "+aList.toString(), Toast.LENGTH_SHORT).show();
                //aTourTypeLIst = aInfo;
                ArrayAdapter<NonEffectiveReason> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, aList);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                binding.spinerReason.setAdapter(dataAdapter);
                binding.spinerReason.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        NonEffectiveReason resonid = (NonEffectiveReason) parent.getSelectedItem();
                        selectedreasonId = resonid.getReasonId();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void SaveDCR(DoctorListViewModel dvm, String empId) {
        try {

            if (!IsValid()) {
                SnackBarManagement._error_CustomMessage(findViewById(android.R.id.content), "Please fill Date , Visit Type and Chamber to save");
            } else {
                DcrSM aInfo = new DcrSM();
                TourTypeViewModel atpVm;
                atpVm = (TourTypeViewModel) binding.spinerVisitType.getSelectedItem();
                int visityTYpeId = atpVm.getTourTypeId();
                String visityTYpeName = atpVm.getTourTypeName();

                DoctorListViewModel dlvm = new DoctorListViewModel();
                DoctorChamberName chamberVM;
                chamberVM = (DoctorChamberName) binding.chamberSpinner.getSelectedItem();
                int chamberId = chamberVM.getChemberId();
                String chamberName = chamberVM.getChemberName();
                aInfo.setDoctorId(dvm.getDoctorId());
                aInfo.setDoctorName(dvm.getDoctorName());

                dlvm.setDocContact(dvm.getDocContact());
                dlvm.setDoctorTypeName(dvm.getDoctorTypeName());
                dlvm.setChemberName(chamberName);
                dlvm.setProgramTypeName(dvm.getProgramTypeName());

                aInfo.setDoclist(dlvm);
                aInfo.setSessionUser(empId);
                aInfo.setDcrDate(binding.dcrdate.getText().toString().trim());
                aInfo.setEntryTime(entryTime);
                aInfo.setIsNonEffectiveReason(is_noneffectve);
                aInfo.setReasonId(selectedreasonId);
                aInfo.setVisitTypeId(visityTYpeId);
                aInfo.setVisitTypeName(visityTYpeName);
                aInfo.setTourPlanTypeId(visityTYpeId);
                aInfo.setChamberId(chamberId);

                aInfo.getDoclist().setDoctorName(chamberName);
                //  aInfo.set(chamberId);
                aInfo.setRemarks(binding.comment.getText().toString());
                aInfo.setGiftList(aGiftList);
                aInfo.setDoctorBrand(aBrandList);
                aInfo.setSampleList(aFinalProductList_Sample);
                aInfo.setDocTPDetailsId(dvm.getDocTPDetailsId());
                aInfo.setAempList(aVisitedList);
                Gson gson = new Gson();
                String data = gson.toJson(aInfo);
                System.out.println("offline save" + data);

                if (dbCrudHelper.checkDataExistInDCRTable_(dcrLocalId) == true) {
                    boolean isOk;

                    isOk = dbCrudHelper.DeleteLocal_DcrData_SQLite(dcrLocalId);
                    if (isOk == true) {
                        boolean isResult = dbCrudHelper.UpdateDcrInfo_SQLite(aInfo);
                        if (isResult) {
                            RedirectSuccess("The DCR Updated Offline");
                        } else {
                            SnackBarManagement._warning_CustomMessage(binding.masterLayoutId, "Something went wrong.. Please try again");
                        }
                    }
                } else {
                    boolean isResult = dbCrudHelper.SaveDcrInfo_SQLite(aInfo);
                    if (isResult) {
                        RedirectSuccess("The DCR Saved Offline");
                    } else {
                        SnackBarManagement._warning_CustomMessage(binding.masterLayoutId, "Something went wrong.. Please try again");
                    }
                }

            }


        } catch (Exception exception) {
            Log.e(TAG, "SaveDCR: ", exception);
            exception.printStackTrace();
        }
    }

    public void SubmitDCR(DoctorListViewModel dcrSM, String empId) {
        if (!IsValid()) {
            SnackBarManagement._error_CustomMessage(findViewById(android.R.id.content), "Please fill Date , Visit Type and Chamber to save");
        } else {
            DcrSM aInfo = new DcrSM();
            TourTypeViewModel atpVm;
            atpVm = (TourTypeViewModel) binding.spinerVisitType.getSelectedItem();
            int visityTYpeId = atpVm.getTourTypeId();

            DoctorChamberName chamberVM;
            chamberVM = (DoctorChamberName) binding.chamberSpinner.getSelectedItem();
            int chamberId = chamberVM.getChemberId();


            aInfo.setDoctorId(dcrSM.getDoctorId());
            aInfo.setDcrDate(binding.dcrdate.getText().toString().trim());
            aInfo.setEntryTime(entryTime);
            aInfo.setTourPlanTypeId(visityTYpeId);
            aInfo.setDocTPDetailsId(dcrSM.getDocTPDetailsId());
            aInfo.setChamberId(chamberId);
            aInfo.setRemarks(binding.comment.getText().toString());
            aInfo.setSessionUser(empId);
            aInfo.setIsNonEffectiveReason(is_noneffectve);
            aInfo.setReasonId(selectedreasonId);
            //  aInfo.setGiftList(aGiftList);
            aInfo.setGiftList(aGiftList);
            aInfo.setSampleList(aFinalProductList_Sample);
            aInfo.setDoctorBrand(aBrandList);
            aInfo.setAempList(aVisitedList);
            aInfo.setEntryDate_Apps(binding.dcrdate.getText().toString().trim());

            if (lat == null && lon == null) {
                SnackBarManagement._warning_CustomMessage(binding.masterLayoutId, "Your Location Getting Null.Try Again");
                return;
            } else {
                aInfo.setLatitude(lat);
                aInfo.setLongitude(lon);
                aInfo.setStreetAddress(setStreetAddress);
            }

         /*   Gson gson=new Gson();
            String data=gson.toJson(aVisitedList);
            Toast.makeText(this, ""+data, Toast.LENGTH_SHORT).show();*/

            presenter.SaveDCR(aInfo);
        }
        try {

        } catch (Exception exception) {
            Log.e(TAG, "SaveDCR: ", exception);
            exception.printStackTrace();
        }
    }

    public boolean IsValid() {
        boolean isVal = true;

        String presDate = binding.dcrdate.getText().toString();
        if (presDate.equals("") || presDate == null) {
            isVal = false;
        }

        TourTypeViewModel atpVm;
        atpVm = (TourTypeViewModel) binding.spinerVisitType.getSelectedItem();
        int visityTYpeId = atpVm.getTourTypeId();
        if (visityTYpeId == 0) {
            isVal = false;
        }

        DoctorChamberName chamberVM;
        chamberVM = (DoctorChamberName) binding.chamberSpinner.getSelectedItem();
        int chamberId = chamberVM.getChemberId();

        if (chamberId == 0) {
            isVal = false;
        }

        return isVal;
    }

    @Override
    public void OnDcrSaveSuccess(String message) {
        /*new androidx.appcompat.app.AlertDialog.Builder(AddDCRActivity.this)
                .setTitle("Success")
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        Intent i = new Intent(AddDCRActivity.this, MainDashboardActivity.class);
                        startActivity(i);
                        i.addFlags(i.FLAG_ACTIVITY_CLEAR_TOP | i.FLAG_ACTIVITY_CLEAR_TASK | i.FLAG_ACTIVITY_NEW_TASK);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    }

                }).setCancelable(false).show();*/

        if (message.equals("DCR Successfully Submitted")) {
            new AlertDialog.Builder(AddDCRActivity.this)
                    .setTitle("Success")
                    .setMessage(message)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            switch (Constants.WHO) {
                                case "DoclitAdapter":
                                 /*   Intent i = new Intent(AddDCRActivity.this, DoctorDashboardActivity.class);
                                   // i.addFlags(i.FLAG_ACTIVITY_CLEAR_TOP | i.FLAG_ACTIVITY_CLEAR_TASK | i.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(i);
                                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);*/
                                    finish();
                                    break;
                                case "PendingDcrAdapter":
                                    try {
                                        boolean isOk;
                                        isOk = dbCrudHelper.DeleteLocal_DcrTable_SQLite(dcrLocalId);
                                        // Toast.makeText(AddDCRActivity.this, "dcrLocalId inside "+dcrLocalId, Toast.LENGTH_SHORT).show();
                                        if (isOk == true) {
                                            onBackPressed();
                                            /*Intent a = new Intent(AddDCRActivity.this, DoctorDashboardActivity.class);
                                            //a.addFlags(a.FLAG_ACTIVITY_CLEAR_TOP | a.FLAG_ACTIVITY_CLEAR_TASK | a.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(a);*/
                                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                            finish();
                                        }

                                    } catch (Exception exception) {
                                        exception.printStackTrace();
                                    }

                                    break;

                            }


                        }

                    }).setCancelable(false).show();
        } else if (message.equals("Insufficient")) {
            SnackBarManagement._warning_CustomMessage(binding.masterLayoutId, "Insufficient Quantity!!");
        } else {
            SnackBarManagement._warning_CustomMessage(binding.masterLayoutId, "Something went wrong!!.Try Again");
        }


    }

    @Override
    public void OnDcrSaveError(String message) {
        new androidx.appcompat.app.AlertDialog.Builder(AddDCRActivity.this)
                .setTitle("Error")
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }

                }).setCancelable(false).show();

    }

    public void RedirectSuccess(String txt) {
        new AlertDialog.Builder(AddDCRActivity.this)
                .setIcon(R.drawable.tikiconwhite)
                .setTitle("Success")
                .setMessage(txt + " has been " + " successfully.")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        onBackPressed();
                    }
                }).setCancelable(false).show();
    }

    private int getIndex(Spinner designationSpinner, String edit_dname) {
        for (int i = 0; i < designationSpinner.getCount(); i++) {
            if (designationSpinner.getItemAtPosition(i).toString().equalsIgnoreCase(edit_dname)) {
                return i;
            }
        }
        return 0;
    }



   /* @Override
    public void Remove(int a,int b) {
        giftQty=a;
        giftID=b;

        //aFinalGiftList=new ArrayList<>();
       *//* Gift g=new Gift();
        g.setProductId(giftID);
        g.setQuantity(giftQty);
        aFinalGiftList.add(g);*//*
    }*/


    @Override
    public boolean onLongClick(int pos) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage("Are you sure wants to delete the Item ?");
        builder1.setCancelable(true);
        builder1.setPositiveButton(
                "Delete",
                (dialog, id) -> {
                    try {
                        aGiftList.remove(pos);
                        mAdapterPoduct_Gift.notifyItemRemoved(pos);
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
    public void onEditTextFocusChange(int postion, int value) {
        aGiftList.get(postion).setQuantity(value);
     /*   updateProductTotals(productListOrder.get(position));
        setTotals();*/
        if (!binding.recyclerViewGift.isComputingLayout()) {
            try {
                mAdapterPoduct_Gift.notifyItemChanged(postion);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }

    @Override
    public boolean onLongPClick(int position) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage("Are you sure wants to delete the Item ?");
        builder1.setCancelable(true);
        builder1.setPositiveButton(
                "Delete",
                (dialog, id) -> {
                    try {
                        aFinalProductList_Sample.remove(position);
                        mAdapterPoduct_Sample.notifyItemRemoved(position);
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
    public void onEditPTextFocusChange(int postion, int value) {
        aFinalProductList_Sample.get(postion).setQuantity(value);
        if (!binding.recyclerViewSample.isComputingLayout()) {
            try {
                mAdapterPoduct_Sample.notifyItemChanged(postion);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }

    @Override
    public boolean onDltClick(int position) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage("Are you sure wants to delete the Item ?");
        builder1.setCancelable(true);
        builder1.setPositiveButton(
                "Delete",
                (dialog, id) -> {
                    try {
                        aVisitedList.remove(position);
                        aVistiedAdapter.notifyItemRemoved(position);
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
    public void onLocationChanged(@NonNull Location location) {
        try {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            double Lat = addresses.get(0).getLatitude();
            double Lon = addresses.get(0).getLongitude();
            lat = String.valueOf(Lat);
            lon = String.valueOf(Lon);
            setStreetAddress = addresses.get(0).getAddressLine(0);
        } catch (Exception e) {
        }
    }

    private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();
                Log.w("address", strReturnedAddress.toString());
            } else {
                Log.w("address", "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.w("address", "Canont get Address!");
        }
        return strAdd;
    }
}