package com.creatrix.salessolution.Activity.Doctor.Prescription;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.creatrix.salessolution.Activity.Doctor.DoctorDashboardActivity;
import com.creatrix.salessolution.DBAdapter.DBCrudHelper;
import com.creatrix.salessolution.Interface.IPrescription;
import com.creatrix.salessolution.Model.Doctor.DoctorChamberName;
import com.creatrix.salessolution.Model.Doctor.DoctorListViewModel;
import com.creatrix.salessolution.Model.PrescriptionSM;
import com.creatrix.salessolution.Model.PrescriptionTYpe;
import com.creatrix.salessolution.Model.Product;
import com.creatrix.salessolution.Presenter.PrescriptionPresenter;
import com.creatrix.salessolution.R;
import com.creatrix.salessolution.RecyclerAdapter._product_NameRecycler;
import com.creatrix.salessolution.Services.Constants;
import com.creatrix.salessolution.UtilityHelper.CameraHelper;
import com.creatrix.salessolution.UtilityHelper.SessionManagement;
import com.creatrix.salessolution.UtilityHelper.SnackBarManagement;
import com.creatrix.salessolution.UtilityHelper.ToastManagment;
import com.creatrix.salessolution.databinding.ActivityAddPrescriptionBinding;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import id.zelory.compressor.Compressor;

public class AddPrescriptionActivity extends AppCompatActivity implements IPrescription.View, _product_NameRecycler.DeleteListener {
    ActivityAddPrescriptionBinding binding;
    private static final String TAG = "PrescriptionError";
    _product_NameRecycler mAdapterPoduct;
    List<Product> aProList = new ArrayList<>();
    ProgressDialog progressDoalog;
    IPrescription.Presenter presenter;
    String[] listItemProduct;
    boolean[] checkedItems;
    ArrayList<Integer> mUserItems = new ArrayList<>();
    List<Product> aFinalProductList = new ArrayList<>();


    private RecyclerView recyclerView;
    File imgFile;
    Uri imageuri;
    String edit_dName, chemberName, path;
    int prescripLocalId;
    DBCrudHelper dbCrudHelper;
    int cid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddPrescriptionBinding.inflate(getLayoutInflater());
        // setContentView(R.layout.activity_add_prescription);
        setContentView(binding.getRoot());
        dbCrudHelper = new DBCrudHelper(AddPrescriptionActivity.this);
        binding.toolbarCustom.setNavigationOnClickListener(v -> finish());
        SessionManagement session = new SessionManagement(getApplicationContext());
        // session.checkLogin();
        progressDoalog = new ProgressDialog(AddPrescriptionActivity.this);
        HashMap<String, String> user = session.getUserDetails();
        int empId = Integer.parseInt(Objects.requireNonNull(user.get(SessionManagement.KEY_EmpId)));
        binding.presdate.setText(new SimpleDateFormat("dd-MMM-yyyy | hh:mm a", Locale.getDefault()).format(new Date()));
        presenter = new PrescriptionPresenter(this, this);

        Gson gson = new Gson();
        DoctorListViewModel aInfoData = gson.fromJson(getIntent().getStringExtra("myjson"), DoctorListViewModel.class);

        switch (Constants.WHO) {
            case "PrescriptionListAdapter":
                binding.productAdd.setOnClickListener(v -> showDialog());
                PrescriptionSM pData = gson.fromJson(getIntent().getStringExtra("prescriptionDtails"), PrescriptionSM.class);
                prescripLocalId = pData.getPrescripId();
                aProList = pData.getaProList();
                aFinalProductList.addAll(aProList);
               /* for (int i = 0; i < aProList.size(); i++) {
                    aFinalProductList.add(aProList.get(i));
                }*/
                Uri myUri = Uri.parse(pData.getImageString());
                try {

                    binding.imgaeView.setImageURI(myUri);

                } catch (Exception exception) {
                    exception.printStackTrace();
                }
               /* aFinalProductList_Sample = pData.getSampleList();
                if (aFinalProductList_Sample != null) {

                    mAdapterPoduct_Sample = new _product_sampleAdapter(aFinalProductList_Sample,this);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                    binding.recyclerViewSample.setLayoutManager(mLayoutManager);
                    binding.recyclerViewSample.setItemAnimator(new DefaultItemAnimator());
                    binding.recyclerViewSample.setAdapter(mAdapterPoduct_Sample);
                    mAdapterPoduct_Sample.notifyDataSetChanged();
                }*/
                aFinalProductList = pData.getaProList();
                if (aFinalProductList != null) {
                    recyclerView = findViewById(R.id.recycler_view);
                    mAdapterPoduct = new _product_NameRecycler(aFinalProductList, this);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                    recyclerView.setLayoutManager(mLayoutManager);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setAdapter(mAdapterPoduct);
                    mAdapterPoduct.notifyDataSetChanged();
                }

                binding.Save.setVisibility(View.VISIBLE);
                presenter.GetPrescriptionType(empId);
                presenter.GetProducts(empId);
                presenter.GetChamberId(aInfoData.getDoctorId());
                if (pData.getPrescTypeName() != null) {
                    edit_dName = pData.getPrescTypeName();
                    binding.prescriptionSpinner.setSelection(getIndex(binding.prescriptionSpinner, edit_dName));
                }
                if (pData.getDoclist().getChemberName() != null) {
                    edit_dName = pData.getDoclist().getChemberName();
                    binding.chamberSpinner.setSelection(getIndex(binding.chamberSpinner, edit_dName));
                }
                binding.finalSubmit.setOnClickListener(v -> {
                    binding.finalSubmit.setEnabled(false);
                  //  Uri myUri = Uri.parse(pData.getImageString());
                    if (myUri != null) {
                        path = getUriRealPathAboveKitkat(AddPrescriptionActivity.this, myUri);
                        if (path == null)
                            return;
                        imgFile = new File(path);
                        try {
                            imgFile = new Compressor(AddPrescriptionActivity.this).compressToFile(imgFile);
                            System.out.println("file" + imgFile);
                        } catch (IOException e) {
                            Toast.makeText(AddPrescriptionActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                    String img_str = null;
                    try {
                        Bitmap bitmap = new Compressor(AddPrescriptionActivity.this).compressToBitmap(imgFile);
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream);
                        byte[] image = stream.toByteArray();
                        img_str = Base64.encodeToString(image, 0);
                        System.out.println("img : " + img_str);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (binding.imgaeView.getDrawable() == null) {
                        SnackBarManagement._warning_CustomMessage(binding.masterLayout, "Prescription required");
                        return;
                    }
                    if (aFinalProductList.size() == 0) {
                        SnackBarManagement._error_CustomMessage(findViewById(android.R.id.content), "Product is Required");
                        return;
                    }
                    SubmitPrescription(aInfoData.getDoctorId(), empId, img_str);
                });
                break;
            case "DoclitAdapter":
                presenter.GetPrescriptionType(empId);
                presenter.GetProducts(empId);
                presenter.GetChamberId(aInfoData.getDoctorId());
                binding.productAdd.setOnClickListener(v -> showDialog());
                binding.finalSubmit.setOnClickListener(v -> {
                    String img_str = null;
                    try {
                        binding.finalSubmit.setEnabled(false);
                        Bitmap bitmap = new Compressor(AddPrescriptionActivity.this).compressToBitmap(imgFile);
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream);
                        byte[] image = stream.toByteArray();
                        img_str = Base64.encodeToString(image, 0);
                        System.out.println("img : " + img_str);

                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }


                    //TODO:Open
                    if (binding.imgaeView.getDrawable() == null) {
                        SnackBarManagement._warning_CustomMessage(binding.masterLayout, "Prescription required");
                        return;
                    }
                    if (aFinalProductList.size() == 0) {
                        SnackBarManagement._error_CustomMessage(findViewById(android.R.id.content), "Product is Required");
                        return;
                    }
                    SubmitPrescription(aInfoData.getDoctorId(), empId, img_str);

                });
                break;
        }
        try {
            binding.dochead.nameTxt.setText(aInfoData.getDoctorName());
            binding.dochead.mobileTxt.setText(aInfoData.getDocContact());
            binding.dochead.doctypeTxt.setText(aInfoData.getDoctorTypeName());
            binding.dochead.programTxt.setText(aInfoData.getProgramTypeName());

        } catch (Exception ex) {
            Log.e(TAG, "onCreate: ", ex);
        }

        binding.Save.setOnClickListener(v -> SavePrescription(aInfoData, aInfoData.getDoctorId(), aInfoData.getDoctorName(), empId));
        binding.camera.setOnClickListener(view -> ImagePicker.Companion.with(this)
                .crop()                    //Crop image(Optional), Check Customization for more option
                .compress(1024)            //Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                .start());
        binding.presdate.setText(new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault()).format(new Date()));


    }

    public void showDialog() {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(AddPrescriptionActivity.this);
        mBuilder.setTitle("Select Product");
        mBuilder.setMultiChoiceItems(listItemProduct, checkedItems, (dialogInterface, position, isChecked) -> {
            if (isChecked) {
                mUserItems.add(position);
            } else {
                mUserItems.remove((Integer.valueOf(position)));
            }
        });
        mBuilder.setCancelable(false);
        mBuilder.setPositiveButton("Done", (dialogInterface, which) -> {
            String item = "";
            aFinalProductList.clear();
            try {
                if (mUserItems != null) {
                    for (int i = 0; i < mUserItems.size(); i++) {
                        int pos = mUserItems.get(i);
                        aFinalProductList.add(aProList.get(pos));
                        item = item + listItemProduct[mUserItems.get(i)];
                        if (i != mUserItems.size() - 1) {
                            item = item + ",";
                        }
                    }

                    if (aFinalProductList != null) {
                        recyclerView = findViewById(R.id.recycler_view);
                        mAdapterPoduct = new _product_NameRecycler(aFinalProductList, AddPrescriptionActivity.this);
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                        recyclerView.setLayoutManager(mLayoutManager);
                        recyclerView.setItemAnimator(new DefaultItemAnimator());
                        recyclerView.setAdapter(mAdapterPoduct);
                        mAdapterPoduct.notifyDataSetChanged();
                    }

                }


            } catch (Exception ex) {
                throw ex;
            }


        });

        mBuilder.setNegativeButton("Cancel", (dialogInterface, i) -> dialogInterface.dismiss());

        mBuilder.setNeutralButton("Clear All", (dialogInterface, which) -> {
            for (int i = 0; i < checkedItems.length; i++) {
                aFinalProductList.clear();
                mAdapterPoduct.notifyDataSetChanged();
                checkedItems[i] = false;
                mUserItems.clear();
            }
        });

        AlertDialog mDialog = mBuilder.create();
        mDialog.show();


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            imageuri = data.getData();
            binding.imgaeView.setImageURI(imageuri);
            if (imageuri != null) {
                path = getUriRealPathAboveKitkat(AddPrescriptionActivity.this, imageuri);
                if (path == null)
                    return;
                imgFile = new File(path);
                try {
                    imgFile = new Compressor(AddPrescriptionActivity.this).compressToFile(imgFile);
                    System.out.println("file" + imgFile);
                } catch (IOException e) {
                    Toast.makeText(AddPrescriptionActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    //here get file path from uri
    public String getUriRealPathAboveKitkat(AddPrescriptionActivity activity, Uri contentURI) {
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
                    Uri downloadUriAppendId = ContentUris.withAppendedId(downloadUri, Long.parseLong(documentId));
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

    /* private boolean isDocumentUri(AddPrescriptionActivity ctx, Uri uri)
     {
         boolean ret = false;
         if(ctx != null && uri != null) {
             ret = DocumentsContract.isDocumentUri(ctx, uri);
         }
         return ret;
     }
     private boolean isContentUri(Uri uri)
     {
         boolean ret = false;
         if(uri != null) {
             String uriSchema = uri.getScheme();
             if("content".equalsIgnoreCase(uriSchema))
             {
                 ret = true;
             }
         }
         return ret;
     }
     private boolean isFileUri(Uri uri)
     {
         boolean ret = false;
         if(uri != null) {
             String uriSchema = uri.getScheme();
             if("file".equalsIgnoreCase(uriSchema))
             {
                 ret = true;
             }
         }
         return ret;
     }
     *//* Check whether this document is provided by ExternalStorageProvider. Return true means the file is saved in external storage. *//*
    private boolean isExternalStoreDoc(String uriAuthority)
    {
        boolean ret = false;
        if("com.android.externalstorage.documents".equals(uriAuthority))
        {
            ret = true;
        }
        return ret;
    }
    *//* Check whether this document is provided by DownloadsProvider. return true means this file is a downloaded file. *//*
    private boolean isDownloadDoc(String uriAuthority)
    {
        boolean ret = false;
        if("com.android.providers.downloads.documents".equals(uriAuthority))
        {
            ret = true;
        }
        return ret;
    }
    *//*
    Check if MediaProvider provides this document, if true means this image is created in the android media app.
    *//*
    private boolean isMediaDoc(String uriAuthority)
    {
        boolean ret = false;
        if("com.android.providers.media.documents".equals(uriAuthority))
        {
            ret = true;
        }
        return ret;
    }
    *//*
    Check whether google photos provide this document, if true means this image is created in the google photos app.
    *//*
    private boolean isGooglePhotoDoc(String uriAuthority)
    {
        boolean ret = false;
        if("com.google.android.apps.photos.content".equals(uriAuthority))
        {
            ret = true;
        }
        return ret;
    }*/
    /* Return uri represented document file real local path.*/
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
    public void onPrescriptionTypeGet(List<PrescriptionTYpe> aList) {
        ArrayAdapter<PrescriptionTYpe> dataAdapter = new ArrayAdapter<>(AddPrescriptionActivity.this, android.R.layout.simple_spinner_item, aList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.prescriptionSpinner.setAdapter(dataAdapter);

    }

    @Override
    public void onProductGet(List<Product> aList) {
        if (aList != null) {
            aProList = aList;
            listItemProduct = new String[aList.size()];
            for (int i = 0; i < aList.size(); i++) {
                listItemProduct[i] = aList.get(i).getProductName();
            }
            checkedItems = new boolean[listItemProduct.length];

        }
    }

    @Override
    public void onChamberGet(List<DoctorChamberName> cList) {

        if (cList.size() > 0) {
            ArrayAdapter<DoctorChamberName> dataAdapter = new ArrayAdapter<>(AddPrescriptionActivity.this, android.R.layout.simple_spinner_item, cList);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            binding.chamberSpinner.setAdapter(dataAdapter);
            binding.chamberSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    DoctorChamberName pch = (DoctorChamberName) parent.getSelectedItem();
                    cid = pch.getChemberId();
                    chemberName = pch.getChemberName();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        } else {
            cid = 0;
            chemberName = "";
        }


    }

    @Override
    public void onSaveSuccess(String message) {
        progressDoalog.dismiss();
        new androidx.appcompat.app.AlertDialog.Builder(AddPrescriptionActivity.this)
                .setTitle("Success")
                .setMessage(message)
                .setPositiveButton("OK", (dialog, which) -> {
                    dialog.cancel();
                    switch (Constants.WHO) {
                        case "DoclitAdapter":
                            finish();
                            break;
                        case "PrescriptionListAdapter":
                            try {
                                boolean isOk;
                                isOk = dbCrudHelper.DeleteLocal_PrescripTable_SQLite(prescripLocalId);
                                if (isOk) {
                                    Intent a = new Intent(AddPrescriptionActivity.this, DoctorDashboardActivity.class);
                                    startActivity(a);
                                    finish();
                                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                }
                            } catch (Exception exception) {
                                exception.printStackTrace();
                            }

                            break;
                    }


                }).setCancelable(false).show();

    }

    @Override
    public void onSaveError(String message) {
        progressDoalog.dismiss();
        new androidx.appcompat.app.AlertDialog.Builder(AddPrescriptionActivity.this)
                .setTitle("Error")
                .setMessage(message)
                .setPositiveButton("OK", (dialog, which) -> dialog.cancel()).setCancelable(false).show();

    }


    public void SubmitPrescription(int docId, int empId, String img_str) {
        progressDoalog.setMessage("Prescription is Saving.... Please wait");
        progressDoalog.show();
        progressDoalog.setCanceledOnTouchOutside(false);
        try {
            if (!IsValid()) {
                SnackBarManagement._error_CustomMessage(findViewById(android.R.id.content), "Please fill all fields to save");
            } else {
                PrescriptionTYpe dsd = (PrescriptionTYpe) binding.prescriptionSpinner.getSelectedItem();
                int id = dsd.getPrescriptionTypeId();
               /* DoctorChamberName pch = (DoctorChamberName) binding.chamberSpinner.getSelectedItem();
                int cid = pch.getChemberId();*/

              /*  if(String.valueOf(pch.getChemberId())!=null) {

                }else {

                }*/

             /*   binding.imgaeView.buildDrawingCache();
                Bitmap bitmap = binding.imgaeView.getDrawingCache();

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 60, stream);
                byte[] image = stream.toByteArray();
                String img_str = Base64.encodeToString(image, 0);*/

                PrescriptionSM aPres = new PrescriptionSM();
                aPres.setDoctorId(docId);
                aPres.setPrescriptionTypeId(id);

                aPres.setPrescriptionDate(binding.presdate.getText().toString().trim());
                aPres.setImageString(img_str);
                aPres.setaProList(aFinalProductList);
                aPres.setSessionUser(empId);
                aPres.setChemberId(cid);

                System.out.println("new image: " + imageuri);
                Gson gson = new Gson();
                String data = gson.toJson(aPres);
                System.out.println("presc submit" + data);
                presenter.SubmitPrescription(aPres);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void SavePrescription(DoctorListViewModel aInfoData, int docId, String doctorName, int empId) {
        try {

            if (!IsValid()) {
                SnackBarManagement._error_CustomMessage(findViewById(android.R.id.content), "Please fill all fields to save");
            } else {

                PrescriptionTYpe dsd = (PrescriptionTYpe) binding.prescriptionSpinner.getSelectedItem();
                int id = dsd.getPrescriptionTypeId();
                String ptypename = dsd.getPrescriptionType();
               /* DoctorChamberName pch = (DoctorChamberName) binding.chamberSpinner.getSelectedItem();
                int cid = pch.getChemberId();
                String chemberName = pch.getChemberName();*/

                PrescriptionSM aPres = new PrescriptionSM();
                DoctorListViewModel dvm = new DoctorListViewModel();
                // aPres.setDoctorId(docId);
                dvm.setDoctorId(docId);
                dvm.setDoctorName(doctorName);
                // aPres.setDoctorName(doctorName);
                dvm.setDocContact(aInfoData.getDocContact());
                dvm.setDoctorTypeName(aInfoData.getDoctorTypeName());
                dvm.setChemberName(chemberName);//aInfoData.getChemberName());
                dvm.setProgramTypeName(aInfoData.getProgramTypeName());
                aPres.setDoclist(dvm);
                aPres.setPrescriptionTypeId(id);
                aPres.setPrescTypeName(ptypename);
                aPres.setPrescriptionDate(binding.presdate.getText().toString().trim());
                aPres.setEntryTime(new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(new Date()));
                aPres.setImageString(String.valueOf(imageuri));
                aPres.setaProList(aFinalProductList);
                aPres.setSessionUser(empId);

                if (!String.valueOf(cid).isEmpty()) {
                    aPres.setChemberId(cid);
                } else {
                    aPres.setChemberId(0);
                    // Toast.makeText(this, "Chamber Null" + cid, Toast.LENGTH_SHORT).show();
                }

                if (dbCrudHelper.checkDataExistInPrescTable_(prescripLocalId)) {
                    boolean isOk = dbCrudHelper.DeleteLocal_PrescripData_SQLite(prescripLocalId);
                    if (isOk) {
                        boolean isResult = dbCrudHelper.UpdatePrescInfo_SQLite(aPres,prescripLocalId);
                        if (isResult) {
                            RedirectSuccess("The Prescription Updated Offline");
                        } else {
                            // ToastManagment.GetLongToast(AddDCRActivity.this, "Something went wrong.. Please try again");
                            SnackBarManagement._warning_CustomMessage(binding.masterLayout, "Something went wrong.. Please try again");
                        }
                    }
                } else {
                    boolean isResult = dbCrudHelper.SavePrescriptionInfo_SQLite(aPres);
                    if (isResult) {
                        RedirectSuccess("The Prescription Saved Offline");
                    } else {
                        ToastManagment.GetLongToast(AddPrescriptionActivity.this, "Something went wrong.. Please try again");
                    }
                }


            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public boolean IsValid() {
        boolean isVal = true;

        String presDate = binding.presdate.getText().toString();
        if (presDate.equals("")) {
            isVal = false;
        }

        PrescriptionTYpe id = (PrescriptionTYpe) binding.prescriptionSpinner.getSelectedItem();
        if (id.getPrescriptionTypeId() == 0) {
            isVal = false;
        }

        try {
            binding.imgaeView.buildDrawingCache();
            Bitmap bitmap = binding.imgaeView.getDrawingCache();

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] image = stream.toByteArray();
            String img_str = Base64.encodeToString(image, 0);

            if (img_str.equals("")) {
                isVal = false;
            }

        } catch (Exception ex) {
            isVal = false;
        }


        return isVal;
    }

    public void RedirectSuccess(String txt) {
        new AlertDialog.Builder(AddPrescriptionActivity.this)
                .setIcon(R.drawable.tikiconwhite)
                .setTitle("Success")
                .setMessage(txt + " has been " + " successfully.")
                .setPositiveButton("OK", (dialog, which) -> onBackPressed()).setCancelable(false).show();
    }

    private int getIndex(Spinner designationSpinner, String edit_dname) {
        for (int i = 0; i < designationSpinner.getCount(); i++) {
            if (designationSpinner.getItemAtPosition(i).toString().equalsIgnoreCase(edit_dname)) {
                return i;
            }
        }
        return 0;
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
                        aFinalProductList.remove(position);
                        mAdapterPoduct.notifyItemRemoved(position);
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
}