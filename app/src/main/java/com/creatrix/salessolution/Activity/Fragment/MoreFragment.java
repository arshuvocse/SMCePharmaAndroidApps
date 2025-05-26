package com.creatrix.salessolution.Activity.Fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Looper;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.creatrix.salessolution.Activity.ChangePassword;
import com.creatrix.salessolution.Activity.SyncFromServerAc;
import com.creatrix.salessolution.BuildConfig;
import com.creatrix.salessolution.DBAdapter.DBCrudHelper;
import com.creatrix.salessolution.Interface.IMore;
import com.creatrix.salessolution.Interface.ISyncMaster;
import com.creatrix.salessolution.Presenter.MorePresenter;
import com.creatrix.salessolution.Presenter.SyncMasterPresenter;
import com.creatrix.salessolution.R;
import com.creatrix.salessolution.Services.Constants;
import com.creatrix.salessolution.UtilityHelper.NetworkInformation;
import com.creatrix.salessolution.UtilityHelper.SessionManagement;
import com.creatrix.salessolution.UtilityHelper.SnackBarManagement;
import com.creatrix.salessolution.databinding.FragmentMoreBinding;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import static android.content.Context.LOCATION_SERVICE;
import static android.content.Context.TELEPHONY_SERVICE;
import static android.telephony.TelephonyManager.NETWORK_TYPE_1xRTT;
import static android.telephony.TelephonyManager.NETWORK_TYPE_CDMA;
import static android.telephony.TelephonyManager.NETWORK_TYPE_EDGE;
import static android.telephony.TelephonyManager.NETWORK_TYPE_EVDO_0;
import static android.telephony.TelephonyManager.NETWORK_TYPE_EVDO_A;
import static android.telephony.TelephonyManager.NETWORK_TYPE_EVDO_B;
import static android.telephony.TelephonyManager.NETWORK_TYPE_GPRS;
import static android.telephony.TelephonyManager.NETWORK_TYPE_HSDPA;
import static android.telephony.TelephonyManager.NETWORK_TYPE_HSPA;
import static android.telephony.TelephonyManager.NETWORK_TYPE_HSPAP;
import static android.telephony.TelephonyManager.NETWORK_TYPE_IDEN;
import static android.telephony.TelephonyManager.NETWORK_TYPE_LTE;
import static android.telephony.TelephonyManager.NETWORK_TYPE_NR;
import static android.telephony.TelephonyManager.NETWORK_TYPE_UMTS;
import static com.creatrix.salessolution.Activity.Attendance.AttendanceActivity.MY_PERMISSIONS_REQUEST_LOCATION;

public class MoreFragment extends Fragment implements IMore.View, OnMapReadyCallback ,ISyncMaster.View{
    FragmentMoreBinding viewBinding;
    IMore.Presenter presenter;
    ISyncMaster.Presenter spresenter;
    SessionManagement session;
    DBCrudHelper dbCrudHelper;

    boolean isWifiConn = false;
    boolean isMobileConn = false;
    FusedLocationProviderClient mFusedLocationClient;
    String lat, lng;
    GoogleMap mGoogleMap;
    SupportMapFragment mapFrag;
    LocationRequest mLocationRequest;
    Location mLastLocation;
    Marker mCurrLocationMarker;


    boolean isDoctorDone = false;
    boolean isDesignationDone = false;
    boolean isDoctorDegreeDone = false;
    boolean isDoctorInstitutionDone = false;
    boolean isChamberType = false;
    boolean isChamberName = false;
    boolean isBrand = false;
    boolean isDocCategory = false;
    boolean isProgramType = false;
    boolean isDoctorSpecilityDone = false;
    boolean isDoctorSpecilDAyDone = false;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewBinding = FragmentMoreBinding.inflate(getLayoutInflater());
        /* mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        lat = null;
        lng = null;
       LocationManager locationManagerdd = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);

        if (locationManagerdd.isProviderEnabled(locationManagerdd.GPS_PROVIDER)) {
        } else {
            showGPSDisabledAlertToUser();
        }*/

        return viewBinding.getRoot();
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_more, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mapFrag = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map_more);
        //mapFrag.getMapAsync(this);

        /*networkStatus();*/


        dbCrudHelper = new DBCrudHelper(getContext());
        presenter = new MorePresenter(this, getContext());
        spresenter = new SyncMasterPresenter(this, getActivity());

        session = new SessionManagement(getContext());
        HashMap<String, String> user = session.getUserDetails();
        String userName = user.get(SessionManagement.KEY_UserName);
        String empId = user.get(SessionManagement.KEY_EmpId);
        int empIds = Integer.parseInt(Objects.requireNonNull(user.get(SessionManagement.KEY_EmpId)));
        String empcode = user.get(SessionManagement.KEY_EmpMasterCode);
        String emprole = user.get(SessionManagement.KEY_EmpRole);

        viewBinding.userName.setText(userName);
        viewBinding.userCode.setText(empcode);
        viewBinding.tvVersionname.setText(BuildConfig.VERSION_NAME);
        viewBinding.tvLastsynctime.setText(Constants.LastSyncTime);

        viewBinding.btnAllSync.setOnClickListener(v -> {
            Intent i = new Intent(getActivity(), SyncFromServerAc.class);
            startActivity(i);
            requireActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });
        viewBinding.btnCustomerSync.setOnClickListener(v -> {

            try {
                assert empId != null;
                presenter.doCustomerSync(Integer.parseInt(empId));
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });
        viewBinding.btnProductSyncs.setOnClickListener(v -> {
            try {
                assert empId != null;
                presenter.doProductSync(Integer.parseInt(empId));
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });
        viewBinding.btnDoctorSyncs.setOnClickListener(v -> {
            try {
                assert empId != null;
                presenter.doDoctorSync(Integer.parseInt(empId));
                spresenter.cllDesignation(empIds);
                spresenter.cllDegree(empIds);
                spresenter.cllSpeciality(empIds);
                spresenter.cllInstitution(empIds);
                spresenter.cllBrand(empIds);
                spresenter.cllDoccategory(empIds);

                spresenter.cllProgramtypey(empIds);
                spresenter.cllSpecialday(empIds);
                spresenter.cllChamberType(empIds);
                spresenter.cllChamberName(empIds);
               /*

              */

            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });
        viewBinding.btnOtherSyncs.setOnClickListener(v -> {
            try {
                presenter.doOtherSync(empcode,emprole);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });

       /* viewBinding.tbnStoretoMemory.setOnClickListener(v -> {
            SnackBarManagement._warning_CustomMessage(getView(),"Comming Soon!!");
          *//*  try {
                List<PatientInformation_storage> pl =mainDataLayerDB.GetPatientInformationAllFromLocal();
                saveObjectToFileList(pl,mypath);
            } catch (IOException e) {
                e.printStackTrace();
            }*//*

        });
        viewBinding.tbnLoadfromLocal.setOnClickListener(v -> {
            SnackBarManagement._warning_CustomMessage(getView(),"Comming Soon!!");
           *//* try {
                FileInputStream fis = new FileInputStream(mypath);
                ObjectInputStream ois = new ObjectInputStream(fis);
                ArrayList<PatientInformation_storage> pi = new ArrayList<>();
                pi= (ArrayList<PatientInformation_storage>) ois.readObject();


                if(!dbLayerSqlite.CheckDataInTable("tblInitTableLoadBackup")){
                    for(PatientInformation_storage pmi:pi)
                    {
                        mainDataLayerDB.SavePatientInfoBackup(pmi);
                        dbLayerSqlite.Insert_InitTableLoadBackup();
                    }

                }else {

                    try {
                        Toast.makeText(getActivity(), "You Already Load Your Latest Backup", Toast.LENGTH_SHORT).show();
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                }


            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }*//*

        });*/
        viewBinding.settingdown.setOnClickListener(v -> {
            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());
            lat = null;
            lng = null;
            LocationManager locationManagerdd = (LocationManager) requireActivity().getSystemService(LOCATION_SERVICE);

            if (!locationManagerdd.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                showGPSDisabledAlertToUser();
            }
            mapFrag.getMapAsync(this);
            networkStatus();

            viewBinding.settingLayout.setVisibility(View.VISIBLE);
            viewBinding.settingdown.setVisibility(View.GONE);
            viewBinding.settingup.setVisibility(View.VISIBLE);
        });
        viewBinding.settingup.setOnClickListener(v -> {
            viewBinding.settingLayout.setVisibility(View.GONE);
            viewBinding.settingup.setVisibility(View.GONE);
            viewBinding.settingdown.setVisibility(View.VISIBLE);
            if (mFusedLocationClient != null) {
                mFusedLocationClient.removeLocationUpdates(mLocationCallback);
            }
        });
        viewBinding.btnChangepw.setOnClickListener(v -> {
            Intent gotocpw = new Intent(getActivity(), ChangePassword.class);
            startActivity(gotocpw);
            requireActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

        });
        viewBinding.btnLogout.setOnClickListener(v ->
                new AlertDialog.Builder(requireActivity())
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Exit App")
                .setMessage("Are you sure you want to Log out?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    session.logoutUser();
                    dbCrudHelper._deleteAllRecordsFromaTable("tblInitTable");
                    dbCrudHelper._deleteAllRecordsFromaTable("tbl_ProductInfo");
                    dbCrudHelper._deleteAllRecordsFromaTable("tbl_ProductSampleInfo");
                    dbCrudHelper._deleteAllRecordsFromaTable("tblCustomerInfo");
                    dbCrudHelper._deleteAllRecordsFromaTable("tblCustomerReport");
                    dbCrudHelper._deleteAllRecordsFromaTable("tblDoctorInfo");
                    dbCrudHelper._deleteAllRecordsFromaTable("tblDoctorBrand");
                    dbCrudHelper._deleteAllRecordsFromaTable("tblLoginProfile");

                    dbCrudHelper._deleteAllRecordsFromaTable("tbl_Group");
                    dbCrudHelper._deleteAllRecordsFromaTable("tblRegion");
                    dbCrudHelper._deleteAllRecordsFromaTable("tblArea");
                    dbCrudHelper._deleteAllRecordsFromaTable("tblTerritory");
                    dbCrudHelper._deleteAllRecordsFromaTable("tblSubTerritory");
                    dbCrudHelper._deleteAllRecordsFromaTable("tblMarket");

                    dbCrudHelper._deleteAllRecordsFromaTable("tblNSMInfo");
                    dbCrudHelper._deleteAllRecordsFromaTable("tblRSMInfo");
                    dbCrudHelper._deleteAllRecordsFromaTable("tblASMInfo");
                    dbCrudHelper._deleteAllRecordsFromaTable("tblMIOInfo");

                })
                .setNegativeButton("No", null)
                .show());
    }

    @SuppressLint({"SetTextI18n"})
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void networkStatus() {
        ActivityCompat.requestPermissions(requireActivity(),new String[]{Manifest.permission.READ_PHONE_STATE}, PackageManager.PERMISSION_GRANTED);
        TelephonyManager telephonyManager = (TelephonyManager) requireActivity().getSystemService(TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            return;
        }
        ConnectivityManager connectivityManager = (ConnectivityManager) requireActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        for (Network network : connectivityManager.getAllNetworks()) {
            NetworkInfo networkInfo = connectivityManager.getNetworkInfo(network);
            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                isWifiConn |= networkInfo.isConnected();
                viewBinding.tvNetwork.setText("Wifi");
            }
            if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                isMobileConn |= networkInfo.isConnected();
                switch (telephonyManager.getDataNetworkType()) {
                    case NETWORK_TYPE_EDGE:
                    case NETWORK_TYPE_GPRS:
                    case NETWORK_TYPE_CDMA:
                    case NETWORK_TYPE_IDEN:
                    case NETWORK_TYPE_1xRTT:
                        viewBinding.tvNetwork.setText("2G");
                        break;
                    case NETWORK_TYPE_UMTS:
                    case NETWORK_TYPE_HSDPA:
                    case NETWORK_TYPE_HSPA:
                    case NETWORK_TYPE_HSPAP:
                    case NETWORK_TYPE_EVDO_0:
                    case NETWORK_TYPE_EVDO_A:
                    case NETWORK_TYPE_EVDO_B:
                        viewBinding.tvNetwork.setText("3G");
                        break;
                    case NETWORK_TYPE_LTE:
                        viewBinding.tvNetwork.setText("4G");
                        break;
                    case NETWORK_TYPE_NR:
                        viewBinding.tvNetwork.setText("5G");
                        break;

                    default:
                        viewBinding.tvNetwork.setText("Unknown");
                    case TelephonyManager.NETWORK_TYPE_EHRPD:
                        break;
                    case TelephonyManager.NETWORK_TYPE_GSM:
                        break;
                    case TelephonyManager.NETWORK_TYPE_HSUPA:
                        break;
                    case TelephonyManager.NETWORK_TYPE_IWLAN:
                        break;
                    case TelephonyManager.NETWORK_TYPE_TD_SCDMA:
                        break;
                    case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                        break;
                }
            }
        }

     /*   connectivityManager.registerDefaultNetworkCallback(new ConnectivityManager.NetworkCallback() {
            @Override
            public void onAvailable(Network network) {
                Log.d("TAG", "The default network is now: " + network);
            }

            @Override
            public void onLost(Network network) {
                Log.d("TAG", "The application no longer has a default network. The last default network was " + network);
            }

            @Override
            public void onCapabilitiesChanged(Network network, NetworkCapabilities networkCapabilities) {
                Log.d("TAG", "The default network changed capabilities: " + networkCapabilities);
            }

            @Override
            public void onLinkPropertiesChanged(Network network, LinkProperties linkProperties) {
                Log.d("TAG", "The default network changed link properties: " + linkProperties);
            }
        });*/
    }

    @Override
    public void onSuccess(String Message) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Success")
                .setMessage(Message)
                .setPositiveButton("OK", (dialog, which) -> {

                }).setCancelable(false).show();

    }

    @Override
    public void onError(String Message) {
        new AlertDialog.Builder(requireContext())
                .setTitle("ERROR")
                .setMessage(Message)
                .setPositiveButton("OK", (dialog, which) -> {

                }).setCancelable(false).show();

    }

    @Override
    public void onCustomerSync(String Message) {
     SnackBarManagement._success_CustomMessage(viewBinding.masterLayout,Message);
    }

    @Override
    public void onProductSync(String Message) {
        SnackBarManagement._success_CustomMessage(viewBinding.masterLayout,Message);
    }

    @Override
    public void onDoctorSync(String Message,boolean a) {
        isDoctorDone=a;
        hitMain();
        //SnackBarManagement._success_CustomMessage(viewBinding.masterLayout,Message);
    }

    @Override
    public void onOtherSync(String Message) {
        SnackBarManagement._success_CustomMessage(viewBinding.masterLayout,Message);
    }
    private void showGPSDisabledAlertToUser() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(requireActivity());
        alertDialogBuilder.setMessage("GPS is disabled in your device. Would you like to enable it?")
                .setCancelable(false)
                .setPositiveButton("Goto Settings Page To Enable GPS",
                        (dialog, id) -> {
                            Intent callGPSSettingIntent = new Intent(
                                    android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(callGPSSettingIntent);
                        });


        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }
    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        //mGoogleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        //TODO:Street View from SAT View
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(120000); // two minute interval
        mLocationRequest.setFastestInterval(120000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(requireActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                //Location Permission already granted
                mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                mGoogleMap.setMyLocationEnabled(true);
            } else {
                //Request Location Permission
                checkLocationPermission();
            }
        } else {
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
            mGoogleMap.setMyLocationEnabled(true);
        }
    }
    LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            List<Location> locationList = locationResult.getLocations();
            if (locationList.size() > 0) {
                //The last location in the list is the newest
                Location location = locationList.get(locationList.size() - 1);

                //Log.i("MapsActivity", "Location: " + location.getLatitude() + " " + location.getLongitude());
                mLastLocation = location;
                if (mCurrLocationMarker != null) {
                    mCurrLocationMarker.remove();
                }
                try {
                    //Place current location marker
                    lat = String.valueOf(location.getLatitude());
                    lng = String.valueOf(location.getLongitude());
                    Geocoder geocoder = new Geocoder(requireActivity());
                    List<Address> addresses;

                    addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 5);
                    LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(latLng);

                    if(NetworkInformation.isConnected(requireActivity())){
                        markerOptions.title(addresses.get(0).getAddressLine(0));
                    }
                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
                    mCurrLocationMarker = mGoogleMap.addMarker(markerOptions);

                    //move map camera
                    mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    };
    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(requireActivity())
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", (dialogInterface, i) -> {
                            //Prompt the user once explanation has been shown
                            ActivityCompat.requestPermissions(requireActivity(),
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                    MY_PERMISSIONS_REQUEST_LOCATION);
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(requireActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
        }
    }

    @Override
    public void onGetNSM(String a, boolean t) {

    }

    @Override
    public void onGetDZSM(String a, boolean t) {

    }

    @Override
    public void onGetAM(String a, boolean t) {

    }

    @Override
    public void onGetMIO(String a, boolean t) {

    }

    @Override
    public void onGetGroup(String a, boolean t) {

    }

    @Override
    public void onGetZone(String a, boolean t) {

    }

    @Override
    public void onGetArea(String a, boolean t) {

    }

    @Override
    public void onGetTeritory(String a, boolean t) {

    }

    @Override
    public void onGetSTeritory(String a, boolean t) {

    }

    @Override
    public void onGetMarket(String a, boolean t) {

    }

    @Override
    public void onGetCustomer(String a, boolean t) {

    }

    @Override
    public void onGetCustomerStation(String a, boolean t) {

    }

    @Override
    public void onGetDoctor(String a, boolean t) {

    }

    @Override
    public void onGetDesignation(String a, boolean t) {
        isDesignationDone= t;
        hitMain();
    }

    @Override
    public void onGetDegree(String a, boolean t) {
        isDoctorDegreeDone= t;
        hitMain();
    }

    @Override
    public void onGetSpeciality(String a, boolean t) {
        isDoctorSpecilityDone= t;
        hitMain();
    }

    @Override
    public void onGetSpecialday(String a, boolean t) {
        isDoctorSpecilDAyDone= t;
        hitMain();
    }

    @Override
    public void onGetInstitution(String a, boolean t) {
        isDoctorInstitutionDone= t;
        hitMain();
    }

    @Override
    public void onGetChamberType(String a, boolean t) {
        isChamberType= t;
        hitMain();
    }

    @Override
    public void onGetChamberName(String a, boolean t) {
        isChamberName= t;
        hitMain();
    }

    @Override
    public void onGetBrand(String a, boolean t) {
        isBrand= t;
        hitMain();
    }

    @Override
    public void onGetDoccategory(String a, boolean t) {
        isDocCategory= t;
        hitMain();

    }

    private void hitMain() {
        if (isDoctorDone && isDesignationDone && isDoctorDegreeDone
                && isDoctorSpecilityDone  && isDoctorInstitutionDone  && isBrand && isDocCategory && isProgramType
                && isDoctorSpecilDAyDone && isChamberType && isChamberName  ) {
            SnackBarManagement._success_CustomMessage(viewBinding.masterLayout,"Doctor Sync is Successful");
            //viewBindings.docAlldoneTxt.setText("----Complete");
        }
    }

    @Override
    public void onGetProgramtypey(String a, boolean t) {
        isProgramType= t;
        hitMain();
    }

    @Override
    public void onGetUserRole(String a, boolean t) {

    }

    @Override
    public void onGetUserByRole(String a, boolean t) {

    }

    @Override
    public void onGetProduct(String a, boolean t) {

    }

    @Override
    public void onGetProductSample(String a, boolean t) {

    }

    @Override
    public void onGetProductGift(String a, boolean t) {

    }

    @Override
    public void onGetQuotedPrice(String a, boolean t) {

    }

    @Override
    public void onGetDoctorType(String a, boolean t) {

    }

    @Override
    public void onGetCustomerType(String a, boolean t) {

    }

    @Override
    public void onGetDoctorContactType(String a, boolean t) {

    }

    @Override
    public void onGetExpenseType(String a, boolean t) {

    }

    @Override
    public void onGetLeaveType(String a, boolean t) {

    }

    @Override
    public void onGetPrescriptionType(String a, boolean t) {

    }

    @Override
    public void onGetNonEffectivereason(String a, boolean t) {

    }

    @Override
    public void onGetTransportList(String a, boolean t) {

    }

    @Override
    public void onGetTourPurpose(String a, boolean t) {

    }

    @Override
    public void onGetVisitType(String a, boolean t) {

    }

    @Override
    public void onGetProviderType(String a, boolean t) {

    }

    @Override
    public void onGetSMCType(String a, boolean t) {

    }
}