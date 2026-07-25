package com.creatrix.salessolution.Activity.Customer;

import static com.creatrix.salessolution.Activity.Attendance.AttendanceActivity.MY_PERMISSIONS_REQUEST_LOCATION;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

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
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.creatrix.salessolution.Activity.Customer.Approval.CustomerApprovalListActivity;
import com.creatrix.salessolution.Activity.Customer.Approval.Model.CustomerApprovalList;
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
import com.creatrix.salessolution.Model.StationType;
import com.creatrix.salessolution.Model.ThanaVM;
import com.creatrix.salessolution.Network.ApiCustomerCall;
import com.creatrix.salessolution.Network.RetrofitClientInstance;
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
import com.creatrix.salessolution.databinding.ActivityCustomerUpdateBinding;
import com.creatrix.salessolution.databinding.DialogSelectLocationBinding;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import id.zelory.compressor.Compressor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomerUpdateActivity extends AppCompatActivity implements LocationListener, ICustomerAdd.View, IMarketStracture.View, IBangladesh.View {
    int   selectedDiviId, selectedDistId, selectedThanaId;
    Dialog popupTPP;
    private static final int PERMISSION_REQUEST_CODE = 200;
    public static final int PICK_IMAGE = 1;
    public static final int PICK_TIMAGE = 2;
    private static final int CAMERA_REQUEST = 1888;
    private static final int CAMERA_PIC_REQUEST = 11;
    private static int CAMERA_PIC_REQUEST2;
    private static int TCAMERA_PIC_REQUEST2;
    ActivityCustomerUpdateBinding viewBinding;
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
    Customer aInfoData;
    public String blockCharacterSet = "@~'#^|$%&*!/?>";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_customer);
        viewBinding = com.creatrix.salessolution.databinding.ActivityCustomerUpdateBinding.inflate(getLayoutInflater());
        View mainView = viewBinding.getRoot();
        setContentView(mainView);
        //Blocking Special char
//        viewBinding.customerName.setFilters(new InputFilter[]{filter});
//        viewBinding.address.setFilters(new InputFilter[]{filter});
//        viewBinding.cmistOwnerName.setFilters(new InputFilter[]{filter});
//        viewBinding.cmistTradeLicense.setFilters(new InputFilter[]{filter});
//        viewBinding.changeptype.setOnClickListener(v -> showSelectDialog());

        // dbCrudHelper = new DBCrudHelper(CustomerActivity.this);
        dbDoctor = new DBDoctorHelper(CustomerUpdateActivity.this);
        dbCrudHelper = new DBCrudHelper(CustomerUpdateActivity.this);
        presenter = new CustomerPresenter(this, CustomerUpdateActivity.this);
        mkpresenter = new MarketStructurePresenter(this, CustomerUpdateActivity.this);
        bdpresenter = new BangladehPresenter(this, CustomerUpdateActivity.this);

        bdpresenter.GetDivisionLocal();
        Gson gson = new Gson();
        //TODO:Need Customer information for edit approval
        aInfoData = gson.fromJson(getIntent().getStringExtra("myjson"), Customer.class);
        setCustomerText(aInfoData);



        if (ContextCompat.checkSelfPermission(CustomerUpdateActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(CustomerUpdateActivity.this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(CustomerUpdateActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
        }
        locationManager = (LocationManager) CustomerUpdateActivity.this.getSystemService(Context.LOCATION_SERVICE);
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
                viewBinding.btnUpdate.setVisibility(View.GONE);
                viewBinding.btnUpdate.setVisibility(View.VISIBLE);

                cal = gson.fromJson(getIntent().getStringExtra("Editdata"), CustomerApprovalList.class);
                viewBinding.toolbarTitle.setText("Edit Customer");




              //  setupEdit(roleType, empId, cal/*,csv*/);
                break;

            case "HomeToCustomer":
                viewBinding.btnUpdate.setVisibility(View.GONE);
                viewBinding.btnUpdate.setVisibility(View.VISIBLE);
                // presenter.GetProgramType();


                break;
        }

        viewBinding.btnUpdate.setOnClickListener(v -> {

            PersonChoiceAdapter adapter = (PersonChoiceAdapter) viewBinding.lvPeople.getAdapter();
            if (adapter == null) {
                SnackBarManagement._warning_CustomMessage(viewBinding.masterLayoutId, "Select Code!");
                return;
            }
            PersonInfoDAO selected = adapter.getSelectedPerson(); // নিজের getter দিয়ে selected পাবেন
            String bspCode="";
            if (selected != null) {
                // Text বানিয়ে changeptype এ দেখান
                bspCode = selected.oneLine();
                //viewBinding.changeptype.setText(showText);
            } else {
                // কিছু select না করলে fallback
              //  viewBinding.changeptype.setText("Nothing selected");
            }

            if (TextUtils.isEmpty(bspCode.toString())) {
                SnackBarManagement._warning_CustomMessage(viewBinding.masterLayoutId, "Select Code!");
                return;
            }

            SaveCustomer(custmasterid, empId,bspCode);
        });

        viewBinding.toolbarCustom.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private void SaveCustomer(int custmasterid, int empId, String bspCode) {
        CustomerSvModel csm = new CustomerSvModel();
        try {
            if (String.valueOf(custmasterid).isEmpty()) {
                csm.setCustomerMasterId(0);
            } else {
                csm.setCustomerMasterId(custmasterid);
            }



            csm.setCustomerBSPCode(bspCode);
            csm.setEmpId(empId);
            Gson gson = new Gson();
            String data = gson.toJson(csm);
            System.out.println("save " + data);
            // Toast.makeText(this, ""+data, Toast.LENGTH_SHORT).show();

            presenter.SaveCustomer(csm, "CustBSPUpdate");


        } catch (Exception ex) {
            ex.printStackTrace();
            ToastManagment.GetLongToast(CustomerUpdateActivity.this, "Some thing went wrong. Try again");
        }
    }

    public void setCustomerText(Customer aCustomer) {
        viewBinding.customerName.setText(aCustomer.getCustomerName());
        viewBinding.customerCodeTxt.setText(aCustomer.getCustomerCode());
        viewBinding.customerAdressTxt.setText(aCustomer.getAddress());
        viewBinding.custmarket.setText(aCustomer.getMarketName());
        viewBinding.custmoblie.setText(aCustomer.getCellNo());
        custmasterid=aCustomer.getCustomerMasterId();
    }

    private void showSelectDialog() {


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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (Constants.From.equals("78911") && resultCode == RESULT_OK) {
            imageuri = data.getData();
            if (imageuri != null) {

                // viewBinding.imgbg.setVisibility(View.GONE);
                path = getUriRealPathAboveKitkat(CustomerUpdateActivity.this, imageuri);
                if (path == null)
                    return;
                imgFile = new File(path);
                try {
                    imgFile = new Compressor(CustomerUpdateActivity.this).compressToFile(imgFile);
                    System.out.println("file" + imgFile);
                } catch (IOException e) {
                    Toast.makeText(CustomerUpdateActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
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
                // viewBinding.imgbg.setVisibility(View.GONE);
                tpath = getUriRealPathAboveKitkat(CustomerUpdateActivity.this, timageuri);
                if (tpath == null)
                    return;
                timgFile = new File(tpath);
                try {
                    timgFile = new Compressor(CustomerUpdateActivity.this).compressToFile(timgFile);
                    System.out.println("file" + timgFile);
                } catch (IOException e) {
                    Toast.makeText(CustomerUpdateActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
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
    private String getUriRealPathAboveKitkat(CustomerUpdateActivity activity, Uri contentURI) {
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

    }

    @Override
    public void onSMCType(List<ModelSMCType> ptype) {

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
            new androidx.appcompat.app.AlertDialog.Builder(CustomerUpdateActivity.this)
                    .setTitle("Success")
                    .setMessage(mesg)
                    .setPositiveButton("OK", (dialog, which) -> {
                        dialog.cancel();
                        Intent i = new Intent(CustomerUpdateActivity.this, MainDashboardActivity.class);
                        i.addFlags(i.FLAG_ACTIVITY_CLEAR_TOP | i.FLAG_ACTIVITY_CLEAR_TASK | i.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    }).setCancelable(false).show();
        }
        if (who.equals("CustEdit")) {
            new androidx.appcompat.app.AlertDialog.Builder(CustomerUpdateActivity.this)
                    .setTitle("Success")
                    .setMessage("Customer Updated")
                    .setPositiveButton("OK", (dialog, which) -> {
                        dialog.cancel();
                        Intent i = new Intent(CustomerUpdateActivity.this, CustomerApprovalListActivity.class);
                        // i.addFlags(i.FLAG_ACTIVITY_CLEAR_TOP | i.FLAG_ACTIVITY_CLEAR_TASK | i.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                        finish();
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    }).setCancelable(false).show();
        }

        if (who.equals("CustBSPUpdate")) {
            new androidx.appcompat.app.AlertDialog.Builder(CustomerUpdateActivity.this)
                    .setTitle("Success")
                    .setMessage("Customer Updated")
                    .setPositiveButton("OK", (dialog, which) -> {
                        dialog.cancel();
                        Intent i = new Intent(CustomerUpdateActivity.this, CustomerEditListActivity.class);

                        i.putExtra("OrderType", "HomeToCustomerEdit");
                     i.addFlags(i.FLAG_ACTIVITY_CLEAR_TOP | i.FLAG_ACTIVITY_CLEAR_TASK | i.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                        finish();
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    }).setCancelable(false).show();
        }


    }

    @Override
    public void onSubmitError(String mesg) {
        new androidx.appcompat.app.AlertDialog.Builder(CustomerUpdateActivity.this)
                .setTitle("Error")
                .setMessage(mesg)
                .setPositiveButton("OK", (dialog, which) -> dialog.cancel()).setCancelable(false).show();
    }

    @Override
    public void vGroup(List<Group> groupList) {
    }

    @Override
    public void vRegion(List<Region> regionList) {

    }

    @Override
    public void vArea(List<Area> areaList) {

    }

    @Override
    public void vTeritory(List<Teritorry> teritoryList) {

    }

    @Override
    public void vSTeritory(List<SubTeritorry> steritoryList) {

    }

    @Override
    public void vMarket(List<Market> marketList) {

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
                                ActivityCompat.requestPermissions(CustomerUpdateActivity.this,
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
        LocationManager lm = (LocationManager) CustomerUpdateActivity.this.getSystemService(Context.LOCATION_SERVICE);
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
            new AlertDialog.Builder(CustomerUpdateActivity.this)
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
                a.setDivisionName("Select Division....");
                divList.add(0, a);
                ArrayAdapter<DivisionVM> dataAdapter = new ArrayAdapter<>(CustomerUpdateActivity.this, android.R.layout.simple_spinner_item, divList);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                viewBinding.spDivision.setAdapter(dataAdapter);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        viewBinding.spDivision.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                DivisionVM area = (DivisionVM) parent.getSelectedItem();
                selectedDiviId = area.getDivisionId();
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
                a.setDistrictName("Select District....");
                disList.add(0, a);
                ArrayAdapter<DistrictVM> dataAdapter = new ArrayAdapter<>(CustomerUpdateActivity.this, android.R.layout.simple_spinner_item, disList);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                viewBinding.spDistrict.setAdapter(dataAdapter);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        viewBinding.spDistrict.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                DistrictVM area = (DistrictVM) parent.getSelectedItem();
                selectedDistId = area.getDistrictId();


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
                ArrayAdapter<ThanaVM> dataAdapter = new ArrayAdapter<>(CustomerUpdateActivity.this, android.R.layout.simple_spinner_item, thanaList);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                viewBinding.spThana.setAdapter(dataAdapter);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        viewBinding.spThana.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ThanaVM area = (ThanaVM) parent.getSelectedItem();
                selectedThanaId = area.getThanaId();

                if (selectedDiviId == 0 || selectedDistId == 0 || selectedThanaId == 0) {
                    viewBinding.lvPeople.setAdapter(null); // clear list
                    return;
                }

                ProgressDialog progressDoalog = new ProgressDialog(CustomerUpdateActivity.this);
                progressDoalog.setMessage("Data Loading.... Please wait");
                progressDoalog.show();
                progressDoalog.setCanceledOnTouchOutside(false);

                ApiCustomerCall service = RetrofitClientInstance.getRetrofitInstance().create(ApiCustomerCall.class);
                Call<List<PersonInfoDAO>> call = service.GetPersonByDivisionDistrict(
                        selectedDiviId, selectedDistId, selectedThanaId,"Update");

                call.enqueue(new Callback<List<PersonInfoDAO>>() {
                    @Override
                    public void onResponse(Call<List<PersonInfoDAO>> call, Response<List<PersonInfoDAO>> response) {
                        // ৩ সেকেন্ড delay দিয়ে result হ্যান্ডেল করবো
                        new Handler(Looper.getMainLooper()).postDelayed(() -> {
                            progressDoalog.dismiss();
                            if (response.isSuccessful() && response.body() != null) {
                                List<PersonInfoDAO> people = response.body();
                                if (people.size() > 0) {
                                    PersonChoiceAdapter adapter =
                                            new PersonChoiceAdapter(CustomerUpdateActivity.this, people);
                                    viewBinding.lvPeople.setAdapter(adapter);
                                } else {
                                    viewBinding.lvPeople.setAdapter(null);
                                    Toast.makeText(CustomerUpdateActivity.this,
                                            "No data found", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                viewBinding.lvPeople.setAdapter(null);
                                Toast.makeText(CustomerUpdateActivity.this,
                                        "No data found", Toast.LENGTH_SHORT).show();
                            }
                        }, 3000); // ৩ সেকেন্ড delay
                    }

                    @Override
                    public void onFailure(Call<List<PersonInfoDAO>> call, Throwable t) {
                        progressDoalog.dismiss();
                        Toast.makeText(CustomerUpdateActivity.this,
                                "Failed to load data", Toast.LENGTH_SHORT).show();
                        Log.e("API", t.toString());
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });


    }
}