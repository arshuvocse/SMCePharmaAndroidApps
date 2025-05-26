package com.creatrix.salessolution.Activity.DA;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.creatrix.salessolution.Activity.Doctor.Prescription.AddPrescriptionActivity;
import com.creatrix.salessolution.Activity.MainDashboardActivity;
import com.creatrix.salessolution.Activity.MileageClaim.AddMileageClaimActivity;
import com.creatrix.salessolution.Model.ResultInfo;
import com.creatrix.salessolution.Model.TadaClaimSM;
import com.creatrix.salessolution.Model.TourDetailForTADA;
import com.creatrix.salessolution.Network.CalculationApiCall;
import com.creatrix.salessolution.Network.RetrofitClientInstance;
import com.creatrix.salessolution.Network.RetrofitClientInstanceAttendance;
import com.creatrix.salessolution.R;
import com.creatrix.salessolution.RecyclerAdapter._tourForTADAClaim_Recyler;
import com.creatrix.salessolution.UtilityHelper.CameraHelper;
import com.creatrix.salessolution.UtilityHelper.SessionManagement;
import com.creatrix.salessolution.UtilityHelper.SnackBarManagement;
import com.creatrix.salessolution.databinding.ActivityTADAClaimBinding;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import id.zelory.compressor.Compressor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TADAClaimActivity extends AppCompatActivity implements ChkItemListener {
    ActivityTADAClaimBinding binding;

    private static final String monthNameArray[] = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
    _tourForTADAClaim_Recyler mAdapter;


    int item_id, item_pos, nttpid = 0;
    String item_type, RoleType;


    private static final int PERMISSION_REQUEST_CODE = 200;
    private static final int CAMERA_PIC_REQUEST = 22;
    Uri imageuri;
    File imgFile;
    String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTADAClaimBinding.inflate(getLayoutInflater());
        // setContentView(R.layout.activity_t_a_d_a_claim);
        setContentView(binding.getRoot());

        binding.toolbarCustom.setNavigationOnClickListener(v -> finish());
        SessionManagement session = new SessionManagement(getApplicationContext());
        // session.checkLogin();
        HashMap<String, String> user = session.getUserDetails();
        int empId = Integer.parseInt(Objects.requireNonNull(user.get(SessionManagement.KEY_EmpId)));
        RoleType = user.get(SessionManagement.KEY_EmpRoleType);
        binding.datePickerpresDate.setOnClickListener(v -> {
            final Calendar cldr = Calendar.getInstance();
            int day = cldr.get(Calendar.DAY_OF_MONTH);
            int month = cldr.get(Calendar.MONTH);
            int year = cldr.get(Calendar.YEAR);
            DatePickerDialog picker;
            picker = new DatePickerDialog(TADAClaimActivity.this,
                    (view, year1, monthOfYear, dayOfMonth) -> {
                        String attSelectedMonth = dayOfMonth + "-" + monthNameArray[monthOfYear] + "-" + year1;
                        binding.presdate.setText(attSelectedMonth);
                        GetTourForTada(empId, attSelectedMonth);
                    }, year, month, day);
            picker.show();

        });

        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        String formattedDate = df.format(c);
        GetTourForTada(empId, formattedDate);
        // GetTadaAmount(userId);
        binding.presdate.setText(formattedDate);
        binding.rgDa.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.daHQ:
                        nttpid = 1;
                        item_type = "HQ";
                        binding.rlimg.setVisibility(View.GONE);
                        binding.divmandatory.setVisibility(View.GONE);
                        break;
                    case R.id.daXHQ:
                        nttpid = 2;
                        item_type = "ExHQ";
                        binding.rlimg.setVisibility(View.GONE);
                        binding.divmandatory.setVisibility(View.GONE);
                        break;
                }
            }
        });
        binding.rgHqos.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.da2HQ:
                        nttpid = 1;
                        item_type = "HQ";
                        binding.rlimg.setVisibility(View.GONE);
                        binding.divmandatory.setVisibility(View.GONE);
                        break;
                    case R.id.daOS:
                        nttpid = 3;
                        item_type = "OS";
                        if(RoleType.equals("DZSM"))
                        {
                            binding.rlimg.setVisibility(View.GONE);
                            binding.divmandatory.setVisibility(View.GONE);
                        }else {
                            binding.rlimg.setVisibility(View.VISIBLE);
                            binding.divmandatory.setVisibility(View.VISIBLE);
                        }

                        break;
                }
            }
        });
        binding.rgXhqos.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.da3XHQ:
                        nttpid = 1;
                        item_type = "ExHQ";
                        binding.rlimg.setVisibility(View.GONE);
                        binding.divmandatory.setVisibility(View.GONE);
                        break;
                    case R.id.da2OS:
                        nttpid = 3;
                        item_type = "OS";
                        if(RoleType.equals("DZSM"))
                        {
                            binding.rlimg.setVisibility(View.GONE);
                            binding.divmandatory.setVisibility(View.GONE);
                        }else {
                            binding.rlimg.setVisibility(View.VISIBLE);
                            binding.divmandatory.setVisibility(View.VISIBLE);
                        }

                        break;
                }
            }
        });

        binding.camera.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(TADAClaimActivity.this,
                    Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED) {

                ImagePicker.Companion.with(TADAClaimActivity.this)
                        .crop()	    			//Crop image(Optional), Check Customization for more option
                        .compress(1024)			//Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                        .start();

                /*Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), CAMERA_PIC_REQUEST);*/

            } else {
                ActivityCompat.requestPermissions(TADAClaimActivity.this,
                        new String[]{Manifest.permission.CAMERA},
                        PERMISSION_REQUEST_CODE);
            }
        });
        binding.btnSubmit.setOnClickListener(v -> {
                    String img_str = null;
                    try {

                        Bitmap bitmap = new Compressor(TADAClaimActivity.this).compressToBitmap(imgFile);
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream);
                        byte[] image = stream.toByteArray();
                        img_str = Base64.encodeToString(image, 0);

                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                    if (RoleType.equals("MIO") || RoleType.equals("AM") /*|| RoleType.equals("DZSM")*/) {
                        if (item_type.equals("OS")) {
                            if (binding.hotelName.getText().toString().equals("")) {
                                SnackBarManagement._warning_CustomMessage(binding.masterLayoutId, "Hotel Name Mandatory!!");
                                return;
                            }
                            if (binding.damobile.getText().toString().equals("")) {
                                SnackBarManagement._warning_CustomMessage(binding.masterLayoutId, "Mobile Number Mandatory!!");
                                return;
                            }
                            if (binding.damobile.getText().toString().length() > 11 || binding.damobile.getText().toString().length() < 11) {
                                SnackBarManagement._warning_CustomMessage(binding.masterLayoutId, "Mobile Number Must Be 11 Digit");
                                return;
                            }
                        }
                    }
                    SaveDataProcee(empId, img_str);
                }
        );


    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left,
                R.anim.slide_out_right);
    }

    public void GetTourForTada(int empId, String tourDate) {
        ProgressDialog progressDoalog = new ProgressDialog(TADAClaimActivity.this);
        progressDoalog.setMessage("Loading....");
        progressDoalog.show();
        progressDoalog.setCanceledOnTouchOutside(false);
        try {
            CalculationApiCall service = RetrofitClientInstanceAttendance.getRetrofitInstance().create(CalculationApiCall.class);
            Call<List<TourDetailForTADA>> call = service.GetTourPlanForTada(empId, tourDate);
            call.enqueue(new Callback<List<TourDetailForTADA>>() {
                @Override
                public void onResponse(@NonNull Call<List<TourDetailForTADA>> call, @NonNull Response<List<TourDetailForTADA>> response) {
                   // progressDoalog.dismiss();
                    if (progressDoalog != null||progressDoalog.isShowing()) {
                        progressDoalog.dismiss();
                    }
                    Gson gson = new Gson();
                    String dd = gson.toJson(response.body());
                    System.out.println("data :" + dd);
                    LoadinView(response.body());
                }

                @Override
                public void onFailure(@NonNull Call<List<TourDetailForTADA>> call, @NonNull Throwable t) {
                    if (progressDoalog != null||progressDoalog.isShowing()) {
                        progressDoalog.dismiss();
                    }
                    if (t instanceof SocketTimeoutException) {
                        ErrorView("Slow Connection Detected");
                    } else {
                        ErrorView("Some Error Occurred");
                    }
                }
            });

        } catch (Exception ex) {
            progressDoalog.dismiss();
            ErrorView("Some Error Occurred");
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void LoadinView(List<TourDetailForTADA> aList) {
        if (aList != null) {
            if (aList.size() > 0) {
                binding.noRecords.setVisibility(View.GONE);
                binding.btnSubmit.setVisibility(View.VISIBLE);
                mAdapter = new _tourForTADAClaim_Recyler(aList, this);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                binding.recyclerView.setLayoutManager(mLayoutManager);
                binding.recyclerView.setItemAnimator(new DefaultItemAnimator());
                binding.recyclerView.setAdapter(mAdapter);
                binding.recyclerView.setItemAnimator(null);
                binding.recyclerView.scrollToPosition(0);
                mAdapter.notifyDataSetChanged();
            } else {
                binding.noRecords.setVisibility(View.VISIBLE);
                binding.btnSubmit.setVisibility(View.INVISIBLE);
                binding.divmandatory.setVisibility(View.INVISIBLE);
                binding.rlimg.setVisibility(View.INVISIBLE);
            }
        } else {
            binding.noRecords.setVisibility(View.VISIBLE);
            binding.btnSubmit.setVisibility(View.INVISIBLE);
            binding.divmandatory.setVisibility(View.INVISIBLE);
            binding.rlimg.setVisibility(View.INVISIBLE);
        }
    }

    public void ErrorView(String msg) {
        Toast.makeText(TADAClaimActivity.this, msg, Toast.LENGTH_LONG).show();
    }

    public void SaveDataProcee(int empId, String img_str) {
        String presDate = binding.presdate.getText().toString();
        String remarks = binding.remarksTxt.getText().toString();
        TadaClaimSM tadaClaimSM = new TadaClaimSM();
        tadaClaimSM.setTadaDate(presDate);
        tadaClaimSM.setId(item_id);
        tadaClaimSM.setImageString(img_str);
        tadaClaimSM.setRemarks(remarks);
        tadaClaimSM.setEmpInfoId(empId);
        tadaClaimSM.setHotelName(binding.hotelName.getText().toString());
        tadaClaimSM.setHotelPhone(binding.damobile.getText().toString());
        tadaClaimSM.setNewTourTypeId(nttpid);
        Gson gson = new Gson();
        String dd = gson.toJson(tadaClaimSM);
        System.out.println("dd" + dd);
        SaveTADA(tadaClaimSM);
    }

    public void SaveTADA(TadaClaimSM aInfo) {
        ProgressDialog progressDoalog = new ProgressDialog(TADAClaimActivity.this);
        progressDoalog.setMessage("Saving.... Please wait");
        progressDoalog.show();
        progressDoalog.setCanceledOnTouchOutside(false);
        try {
            CalculationApiCall service = RetrofitClientInstanceAttendance.getRetrofitInstance().create(CalculationApiCall.class);
            Call<ResultInfo> call = service.SaveTadaClaim(aInfo);
            call.enqueue(new Callback<ResultInfo>() {
                @Override
                public void onResponse(@NonNull Call<ResultInfo> call, @NonNull Response<ResultInfo> response) {
                    //progressDoalog.dismiss();
                    if (progressDoalog != null||progressDoalog.isShowing()) {
                        progressDoalog.dismiss();
                    }
                    ResultInfo info = response.body();
                    if (info != null) {
                        if (info.getSuccess()) {
                            onSaveSuccess("DA Successfully Submitted");

                        } else {
                            onSaveError(info.getMsd());
                        }

                    } else {
                        onSaveError(info.getMsd());

                    }

                }

                @Override
                public void onFailure(@NonNull Call<ResultInfo> call, @NonNull Throwable t) {
                 //   progressDoalog.dismiss();
                    if (progressDoalog != null||progressDoalog.isShowing()) {
                        progressDoalog.dismiss();
                    }
                    if (t instanceof SocketTimeoutException) {
                        onSaveError("Slow Internet Detected..Please try again");
                    } else {
                        onSaveError("Some error occurred..Please try again");
                    }
                }
            });

        } catch (Exception ex) {
          //  progressDoalog.dismiss();
            if (progressDoalog != null||progressDoalog.isShowing()) {
                progressDoalog.dismiss();
            }
            String str = ex.toString();
            Log.e("Exception", str);
            onSaveError("Some error occurred..Please try again");

        }


    }

    public void onSaveSuccess(String message) {
        new androidx.appcompat.app.AlertDialog.Builder(TADAClaimActivity.this)
                .setTitle("Success")
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        Intent i = new Intent(TADAClaimActivity.this, MainDashboardActivity.class);
                        startActivity(i);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    }

                }).setCancelable(false).show();
    }

    public void onSaveError(String message) {
        new androidx.appcompat.app.AlertDialog.Builder(TADAClaimActivity.this)
                .setTitle("Error")
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }

                }).setCancelable(false).show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_PIC_REQUEST && resultCode == RESULT_OK) {
            try {
                assert data != null;
                if (data != null) {
                    imageuri = data.getData();
                    binding.imgaeView.setImageURI(imageuri);
                    if (imageuri != null) {
                        path = getUriRealPathAboveKitkat(TADAClaimActivity.this, imageuri);
                        if (path == null)
                            return;
                        imgFile = new File(path);
                        try {
                            imgFile = new Compressor(TADAClaimActivity.this).compressToFile(imgFile);
                            System.out.println("file" + imgFile);
                        } catch (IOException e) {
                            Toast.makeText(TADAClaimActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            } catch (Exception ex) {
                Toast.makeText(TADAClaimActivity.this, ex.toString(), Toast.LENGTH_LONG).show();
            }

        }

    }
    private String getUriRealPathAboveKitkat(TADAClaimActivity activity, Uri contentURI) {
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
                    String idArr[] = documentId.split(":");
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
    public void ckdItem(int id, int Pos, String type, int ppid) {
        try {
            item_id = id;
            item_pos = Pos;
            item_type = type;

            // Toast.makeText(this, ""+String.valueOf(id), Toast.LENGTH_SHORT).show();
            if (type.equals(" ")) {
                binding.rlimg.setVisibility(View.GONE);
                binding.divmandatory.setVisibility(View.GONE);

                binding.llDahqos.setVisibility(View.GONE);
                binding.llDaxhqos.setVisibility(View.GONE);
                binding.llDa.setVisibility(View.GONE);
            }

            //TRy new development
            if (ppid == 6 || ppid == 7) {
                if (type.equals("OS")) {
                    switch (RoleType) {
                        case "MIO":
                        case "AM":
                            binding.rlimg.setVisibility(View.VISIBLE);
                            binding.divmandatory.setVisibility(View.VISIBLE);

                            binding.llDa.setVisibility(View.VISIBLE);
                            binding.llDahqos.setVisibility(View.GONE);
                            binding.llDaxhqos.setVisibility(View.GONE);
                            break;
                        case "DZSM":
                            binding.rlimg.setVisibility(View.GONE);
                            binding.divmandatory.setVisibility(View.GONE);

                            binding.llDa.setVisibility(View.VISIBLE);
                            binding.llDahqos.setVisibility(View.GONE);
                            binding.llDaxhqos.setVisibility(View.GONE);
                            case "NSM":
                            binding.rlimg.setVisibility(View.GONE);
                            binding.divmandatory.setVisibility(View.GONE);

                            binding.llDa.setVisibility(View.VISIBLE);
                            binding.llDahqos.setVisibility(View.GONE);
                            binding.llDaxhqos.setVisibility(View.GONE);
                            break;
                    }

                } else if (type.equals("HQ")) {
                    switch (RoleType) {
                        case "MIO":
                        case "AM":
                        case "DZSM":
                            binding.llDahqos.setVisibility(View.GONE);
                            binding.llDaxhqos.setVisibility(View.VISIBLE);
                            binding.llDa.setVisibility(View.GONE);

                            binding.rlimg.setVisibility(View.GONE);
                            binding.divmandatory.setVisibility(View.GONE);
                        case "NSM":
                            binding.llDahqos.setVisibility(View.GONE);
                            binding.llDaxhqos.setVisibility(View.VISIBLE);
                            binding.llDa.setVisibility(View.GONE);

                            binding.rlimg.setVisibility(View.GONE);
                            binding.divmandatory.setVisibility(View.GONE);
                            break;
                    }
                } else {
                    switch (RoleType) {
                        case "MIO":
                        case "AM":
                        case "DZSM":

                            binding.llDahqos.setVisibility(View.VISIBLE);
                            binding.llDaxhqos.setVisibility(View.GONE);
                            binding.llDa.setVisibility(View.GONE);

                            binding.rlimg.setVisibility(View.GONE);
                            binding.divmandatory.setVisibility(View.GONE);
                            binding.llDa.setVisibility(View.GONE);
                        case "NSM":

                            binding.llDahqos.setVisibility(View.VISIBLE);
                            binding.llDaxhqos.setVisibility(View.GONE);
                            binding.llDa.setVisibility(View.GONE);

                            binding.rlimg.setVisibility(View.GONE);
                            binding.divmandatory.setVisibility(View.GONE);
                            binding.llDa.setVisibility(View.GONE);
                            break;
                    }
                }
            }
            else {
                if (type.equals("OS")) {
                    binding.llDahqos.setVisibility(View.GONE);
                    binding.llDaxhqos.setVisibility(View.GONE);
                    switch (RoleType) {

                        case "MIO":
                            binding.llDa.setVisibility(View.GONE);
                            binding.rlimg.setVisibility(View.VISIBLE);
                            binding.divmandatory.setVisibility(View.VISIBLE);
                            break;
                        case "AM":
                            binding.llDa.setVisibility(View.VISIBLE);
                            binding.rlimg.setVisibility(View.VISIBLE);
                            binding.divmandatory.setVisibility(View.VISIBLE);
                            break;
                        case "DZSM":
                            binding.llDa.setVisibility(View.VISIBLE);
                            binding.rlimg.setVisibility(View.GONE);
                            binding.divmandatory.setVisibility(View.GONE);

                        case "NSM":
                            binding.llDa.setVisibility(View.VISIBLE);
                            binding.rlimg.setVisibility(View.GONE);
                            binding.divmandatory.setVisibility(View.GONE);
                            break;
                    }

                } else {
                    binding.llDa.setVisibility(View.GONE);
                    binding.llDahqos.setVisibility(View.GONE);
                    binding.llDaxhqos.setVisibility(View.GONE);
                    switch (RoleType) {
                        case "MIO":
                        case "AM":
                        case "DZSM":
                            binding.rlimg.setVisibility(View.GONE);
                            binding.divmandatory.setVisibility(View.GONE);
                        case "NSM":
                            binding.rlimg.setVisibility(View.GONE);
                            binding.divmandatory.setVisibility(View.GONE);
                            break;
                    }
                }
            }
        } catch (Exception exception) {}
    }

}
