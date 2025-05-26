package com.creatrix.salessolution.Activity.Expense;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.creatrix.salessolution.Activity.Doctor.Prescription.AddPrescriptionActivity;
import com.creatrix.salessolution.Activity.Expense.Model.ExpListTeam;
import com.creatrix.salessolution.Activity.MainDashboardActivity;
import com.creatrix.salessolution.Interface.IExpenseClaim;
import com.creatrix.salessolution.Interface.RecyclerviewExpenseClaimListner;
import com.creatrix.salessolution.Model.ExpenseClaimMaster;
import com.creatrix.salessolution.Model.ExpenseReportViewModel;
import com.creatrix.salessolution.Model.ExpenseTypeDetails;
import com.creatrix.salessolution.Model.ExpenseTypeMaster;
import com.creatrix.salessolution.Presenter.ExpenseClaimPresenter;
import com.creatrix.salessolution.R;
import com.creatrix.salessolution.Services.Constants;
import com.creatrix.salessolution.UtilityHelper.CameraHelper;
import com.creatrix.salessolution.UtilityHelper.SessionManagement;
import com.creatrix.salessolution.UtilityHelper.SnackBarManagement;
import com.creatrix.salessolution.UtilityHelper.UtilityHelper;
import com.creatrix.salessolution.databinding.ActivityExpanseClamBinding;
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

import id.zelory.compressor.Compressor;

public class ExpanseClamActivity extends AppCompatActivity implements IExpenseClaim.View, RecyclerviewExpenseClaimListner {
    ActivityExpanseClamBinding binding;
    private static final int PERMISSION_REQUEST_CODE = 200;
    public static final int PICK_IMAGE = 1;
    //private static final int CAMERA_REQUEST = 1888;
    private static final int CAMERA_PIC_REQUEST = 22;
    Uri imageuri;
    File imgFile;
    //ImageView imgaeView;
    IExpenseClaim.Presenter presenter;
    SessionManagement session;
    private _entrytxtWithValue_Recycler mAdapter;
    List<ExpenseTypeDetails> aSmDetail = new ArrayList<>();
    RecyclerviewExpenseClaimListner mListener;
    String ExpenseClaimID,path;
    int ExpenseTypeId;
    ExpenseReportViewModel expenseReportViewModel;
    ExpListTeam expenseReport;
    List<ExpenseTypeMaster> compareTypeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityExpanseClamBinding.inflate(getLayoutInflater());
        // setContentView(R.layout.activity_expanse_clam);
        setContentView(binding.getRoot());
        binding.toolbarCustom.setNavigationOnClickListener(v -> finish());
        binding.expdate.setText(new SimpleDateFormat("dd-MMM-yyyy hh:mm a", Locale.getDefault()).format(new Date()));
        session = new SessionManagement(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        int empId = Integer.parseInt(user.get(SessionManagement.KEY_EmpId));
        String role  = user.get(SessionManagement.KEY_EmpRole);
        String roleTypeId = user.get(SessionManagement.KEY_EmpRoleTypeId);
        mListener = this::onEditTextFocusChange;
        binding.datePickerExpDate.setOnClickListener(v -> {
            UtilityHelper._datePickerDialogeForOmmit7day_DisableNextDates(binding.expdate, ExpanseClamActivity.this);
        });
        presenter = new ExpenseClaimPresenter(this, this);
        String empIdString = String.valueOf(empId);
        presenter.GetExpenseType(roleTypeId,empIdString);
        //For Edit Expense
        switch (Constants.WHO) {
            case "TeamExpViewAC":
                //edit
                binding.toolbarTitle.setText("Edit Team Expense");
                binding.btnUpdate.setVisibility(View.VISIBLE);
                binding.btnSubmit.setVisibility(View.GONE);
                binding.exptypelay.setVisibility(View.GONE);
                binding.camera.setVisibility(View.GONE);
                binding.datePickerExpDate.setVisibility(View.GONE);
                Gson son = new Gson();
                expenseReport = son.fromJson(getIntent().getStringExtra("teamEditdata"), ExpListTeam.class);
                setTeamDataForEdit(expenseReport, empId);

                break;
            case "ExpViewAC":
                //edit
                binding.toolbarTitle.setText("Edit Expense");
                binding.btnUpdate.setVisibility(View.VISIBLE);
                binding.btnSubmit.setVisibility(View.GONE);
                binding.exptypelay.setVisibility(View.GONE);
                binding.camera.setVisibility(View.GONE);
                binding.datePickerExpDate.setVisibility(View.GONE);
                Gson gson = new Gson();
                expenseReportViewModel = gson.fromJson(getIntent().getStringExtra("Editdata"), ExpenseReportViewModel.class);
                setDataForEdit(expenseReportViewModel, empId);

                break;
            case "HomeFragment":
                binding.btnUpdate.setVisibility(View.GONE);
                binding.exptypelayfixed.setVisibility(View.GONE);
                binding.btnSubmit.setVisibility(View.VISIBLE);
                binding.btnSubmit.setOnClickListener(v -> {
                    String img_str = null;
                    if(imgFile!=null)
                    {
                        try {
                            Bitmap bitmap = new Compressor(ExpanseClamActivity.this).compressToBitmap(imgFile);
                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream);
                            byte[] image = stream.toByteArray();
                            img_str = Base64.encodeToString(image, 0);
                            System.out.println("img : "+img_str);
                        } catch (IOException e) {}
                    }else {
                         img_str = null;
                    }
                    ExpenseTypeMaster typeMaster = (ExpenseTypeMaster) binding.expanseType.getSelectedItem();
                    if(typeMaster.isImageRequired())
                    {
                        if (binding.imgaeView.getDrawable() == null || img_str == null) {
                            SnackBarManagement._warning_CustomMessage(binding.masterLayout, "Bill Image required");
                           return;
                        }
                    }

                        SaveExpenseClaim("submit", empId, ExpenseClaimID, ExpenseTypeId, img_str);
                });
                break;
        }
        binding.camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(ExpanseClamActivity.this,
                        Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_GRANTED) {
                    ImagePicker.Companion.with(ExpanseClamActivity.this)
                            .crop()	    			//Crop image(Optional), Check Customization for more option
                            .compress(1024)			//Final image size will be less than 1 MB(Optional)
                            .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                            .start();

                } else {
                    ActivityCompat.requestPermissions(ExpanseClamActivity.this,
                            new String[]{Manifest.permission.CAMERA},
                            PERMISSION_REQUEST_CODE);
                }
            }
        });
        binding.expanseType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    ExpenseTypeMaster typeMaster = new ExpenseTypeMaster();
                    typeMaster = (ExpenseTypeMaster) binding.expanseType.getSelectedItem();
                    //is_imgReq=typeMaster.isImageRequired();
                    if(typeMaster.isImageRequired())
                    {
                        binding.imgName.setText(R.string.bill_imagerq);
                    }else {
                        binding.imgName.setText(R.string.bill_image);
                    }
                    if(typeMaster.isFixed())
                    {
                        binding.amountValue.setText(typeMaster.getExpenseAmount());
                        binding.amountValue.setEnabled(false);
                    }else {
                        binding.amountValue.setText("");
                        binding.amountValue.setEnabled(true);
                    }

                    presenter.GetExpenseDetails(typeMaster.getExpenseTypeId());
                } catch (Exception ex) {}
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setTeamDataForEdit(ExpListTeam data, int empId) {
        binding.expdate.setText(data.getExpenseDate());
        Glide.with(ExpanseClamActivity.this)
                .load(data.getImageString())
                .fitCenter()
                .into(binding.imgaeView);
        binding.expfixedtypename.setText(data.getExpenseTypeName());
        binding.amountValue.setText(String.valueOf(data.getAmount()));
        if (data.getRemarks() == null) {
            binding.remarksTxt.setText("");
        } else {
            binding.remarksTxt.setText(data.getRemarks());
        }
        aSmDetail.clear();
        for (int i = 0; i < data.getaDetailListDAO().size(); i++) {
            ExpenseTypeDetails dtd = new ExpenseTypeDetails();
            dtd.setFieldName(data.getaDetailListDAO().get(i).getFieldName());
            dtd.setValueText(data.getaDetailListDAO().get(i).getValueText());
            dtd.setExpenseTypDetailsId(data.getaDetailListDAO().get(i).getExpenseTypDetailsId());
            aSmDetail.add(dtd);
        }

        mAdapter = new _entrytxtWithValue_Recycler(aSmDetail, mListener);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        binding.recyclerViewitem.setLayoutManager(mLayoutManager);
        binding.recyclerViewitem.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerViewitem.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        binding.btnUpdate.setOnClickListener(v -> {
            String img_str = null;
            try {
                Bitmap bitmap = new Compressor(ExpanseClamActivity.this).compressToBitmap(imgFile);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream);
                byte[] image = stream.toByteArray();
                img_str = Base64.encodeToString(image, 0);
                System.out.println("img : "+img_str);

                String exDate = binding.expdate.getText().toString();
                String rmrks = binding.remarksTxt.getText().toString();

                if (TextUtils.isEmpty(binding.amountValue.getText().toString())) {
                    SnackBarManagement._warning_CustomMessage(binding.masterLayout, "Amount is required");
                    return;
                }
                if (binding.imgaeView.getDrawable() == null) {
                    SnackBarManagement._warning_CustomMessage(binding.masterLayout, "Bill Image required");
                    return;
                }
                double amount = Double.parseDouble(binding.amountValue.getText().toString());
                ExpenseClaimMaster claimMaster = new ExpenseClaimMaster();
                claimMaster.setExpenseClaimID(String.valueOf(data.getExpenseClaimID()));//ExpenseClaimID
                // claimMaster.setExpenseTypeId(typeMaster.getExpenseTypeId());
                claimMaster.setExpenseTypeId(data.getExpenseTypeId());
                claimMaster.setExpenseDate(exDate);
                claimMaster.setAmount(amount);
                claimMaster.setRemarks(rmrks);
                claimMaster.setaDetailList(aSmDetail);
               // claimMaster.setImageBase64String(img_str);
                claimMaster.setImageBase64String(data.getImageString());
                claimMaster.setEmpInfoId(empId);
                claimMaster.setFromApp(true);
                presenter.SaveExpenseClaim(claimMaster);
            } catch (Exception exception) {}


           /* try {

            } catch (Exception exception) {
                exception.printStackTrace();
            }*/
            //SaveExpenseClaim("update", empId,ExpenseClaimID,ExpenseTypeId,img_str);
        });
    }
    //set data from view
    private void setDataForEdit(ExpenseReportViewModel data, int empId) {
        //set date from view
        binding.expdate.setText(data.getExpDate());
        if (data.getImageString() != null) {
            Glide.with(ExpanseClamActivity.this)
                    .load(data.getImageString())
                    .fitCenter()
                    .into(binding.imgaeView);
          /*  Bitmap decodedByte = null;
            try {
                byte[] decodedString = Base64.decode(data.getImageString(), Base64.DEFAULT);
                decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            //set image from view
            imgaeView.setImageBitmap(decodedByte);*/
        }
        binding.expfixedtypename.setText(data.getExpenseTypeName());
        binding.amountValue.setText(data.getAmount());
        if (data.getRemarks() == null) {
            binding.remarksTxt.setText("");
        } else {
            binding.remarksTxt.setText(data.getRemarks());
        }
        aSmDetail.clear();
        for (int i = 0; i < data.getaDetailListDAO().size(); i++) {
            ExpenseTypeDetails dtd = new ExpenseTypeDetails();
            dtd.setFieldName(data.getaDetailListDAO().get(i).getFieldName());
            dtd.setValueText(data.getaDetailListDAO().get(i).getValueText());
            dtd.setExpenseTypDetailsId(data.getaDetailListDAO().get(i).getExpenseTypDetailsId());
            aSmDetail.add(dtd);
        }

        mAdapter = new _entrytxtWithValue_Recycler(aSmDetail, mListener);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(ExpanseClamActivity.this);
        binding.recyclerViewitem.setLayoutManager(mLayoutManager);
        binding.recyclerViewitem.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerViewitem.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        binding.btnUpdate.setOnClickListener(v -> {
            String img_str = null;
            try {
             /*   binding.imgaeView.buildDrawingCache();
                Bitmap bitmap = binding.imgaeView.getDrawingCache();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream);
                byte[] image = stream.toByteArray();
                img_str = Base64.encodeToString(image, 0);*/
                binding.btnUpdate.setEnabled(false);
                Bitmap bitmap = new Compressor(ExpanseClamActivity.this).compressToBitmap(imgFile);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream);
                byte[] image = stream.toByteArray();
                img_str = Base64.encodeToString(image, 0);
                System.out.println("img : " + img_str);

                String exDate = binding.expdate.getText().toString();
                String rmrks = binding.remarksTxt.getText().toString();

                if (TextUtils.isEmpty(binding.amountValue.getText().toString())) {
                    SnackBarManagement._warning_CustomMessage(binding.masterLayout, "Amount is required");
                    return;
                }
                if (binding.imgaeView.getDrawable() == null) {
                    SnackBarManagement._warning_CustomMessage(binding.masterLayout, "Bill Image required");
                    return;
                }
                double amount = Double.parseDouble(binding.amountValue.getText().toString());
                ExpenseClaimMaster claimMaster = new ExpenseClaimMaster();
                claimMaster.setExpenseClaimID(data.getExpenseClaimID());//ExpenseClaimID
                // claimMaster.setExpenseTypeId(typeMaster.getExpenseTypeId());
                claimMaster.setExpenseTypeId(Integer.parseInt(data.getExpenseTypeId()));
                claimMaster.setExpenseDate(exDate);
                claimMaster.setAmount(amount);
                claimMaster.setRemarks(rmrks);
                claimMaster.setaDetailList(aSmDetail);
                claimMaster.setImageBase64String(img_str);
                claimMaster.setEmpInfoId(empId);
                claimMaster.setFromApp(true);
                presenter.SaveExpenseClaim(claimMaster);


            } catch (Exception exception) {
                //exception.printStackTrace();
            }
        });


    }
    private void SaveExpenseClaim(String btn, int empId, String expenseClaimID, int expenseTypeID, String img_str) {
        try {
            String exDate = binding.expdate.getText().toString();
            String rmrks = binding.remarksTxt.getText().toString();
            if (TextUtils.isEmpty(binding.amountValue.getText().toString())) {
                SnackBarManagement._warning_CustomMessage(binding.masterLayout, "Amount is required");
                return;
            }
            ExpenseTypeMaster typeMaster;
            typeMaster = (ExpenseTypeMaster) binding.expanseType.getSelectedItem();
            double amount = Double.parseDouble(binding.amountValue.getText().toString());
            ExpenseClaimMaster claimMaster = new ExpenseClaimMaster();
            claimMaster.setExpenseClaimID("0");
            claimMaster.setExpenseTypeId(typeMaster.getExpenseTypeId());
            claimMaster.setExpenseDate(exDate);
            claimMaster.setAmount(amount);
            claimMaster.setRemarks(rmrks);
            claimMaster.setaDetailList(aSmDetail);

            boolean hasanyEmptyValue = false;
            for (int i = 0; i < aSmDetail.size(); i++) {
                hasanyEmptyValue = aSmDetail.get(i).getValueText().isEmpty() && aSmDetail.get(i).isRequied() ? true : false;

                if (hasanyEmptyValue) {
                    SnackBarManagement._warning_CustomMessage(binding.masterLayout, "Please Fill up Mandatory Fields");
                    return;
                }

            }

            claimMaster.setImageBase64String(img_str);
            claimMaster.setEmpInfoId(empId);
            claimMaster.setFromApp(true);

            Gson gson = new Gson();
            String expense = gson.toJson(claimMaster);
            System.out.println("offline save" + expense);
            presenter.SaveExpenseClaim(claimMaster);
        } catch (Exception exception) {
          //  exception.printStackTrace();
        }


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        imageuri = data.getData();
        if(imageuri!=null)
        {
            binding.imgaeView.setImageURI(imageuri);
            binding.imgaeView.setVisibility(View.VISIBLE);
            binding.imgbg.setVisibility(View.GONE);
            path= getUriRealPathAboveKitkat(ExpanseClamActivity.this,imageuri);
            if (path == null)
                return;
            imgFile = new File(path);
            try {
                imgFile=new Compressor(ExpanseClamActivity.this).compressToFile(imgFile);
                System.out.println("file"+imgFile);
            } catch (IOException e) {
                Toast.makeText(ExpanseClamActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }
    //here get file path from uri
    private String getUriRealPathAboveKitkat(ExpanseClamActivity activity, Uri contentURI)
    {
        String ret = "";
        if(activity != null && contentURI != null) {
            if(CameraHelper.isContentUri(contentURI))
            {
                //if(isGooglePhotoDoc(contentURI.getAuthority()))
                if(CameraHelper.isGooglePhotoDoc(contentURI.getAuthority()))
                {
                    ret = contentURI.getLastPathSegment();
                }else {
                    ret = getImageRealPath(getContentResolver(), contentURI, null);
                }
            }else if(CameraHelper.isFileUri(contentURI)) {
                ret = contentURI.getPath();
            }else if(CameraHelper.isDocumentUri(activity, contentURI)){
                // Get uri related document id.
                String documentId = DocumentsContract.getDocumentId(contentURI);
                // Get uri authority.
                String uriAuthority = contentURI.getAuthority();
                if(CameraHelper.isMediaDoc(uriAuthority))
                {
                    String idArr[] = documentId.split(":");
                    if(idArr.length == 2)
                    {
                        // First item is document type.
                        String docType = idArr[0];
                        // Second item is document real id.
                        String realDocId = idArr[1];
                        // Get content uri by document type.
                        Uri mediaContentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                        if("image".equals(docType))
                        {
                            mediaContentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                        }
                        // Get where clause with real document id.
                        String whereClause = MediaStore.Images.Media._ID + " = " + realDocId;
                        ret = getImageRealPath(getContentResolver(), mediaContentUri, whereClause);
                    }
                }else if(CameraHelper.isDownloadDoc(uriAuthority))
                {
                    // Build download URI.
                    Uri downloadUri = Uri.parse("content://downloads/public_downloads");
                    // Append download document id at URI end.
                    Uri downloadUriAppendId = ContentUris.withAppendedId(downloadUri, Long.valueOf(documentId));
                    ret = getImageRealPath(getContentResolver(), downloadUriAppendId, null);
                }else if(CameraHelper.isExternalStoreDoc(uriAuthority))
                {
                    String[] idArr = documentId.split(":");
                    if(idArr.length == 2)
                    {
                        String type = idArr[0];
                        String realDocId = idArr[1];
                        if("primary".equalsIgnoreCase(type))
                        {
                            ret = Environment.getExternalStorageDirectory() + "/" + realDocId;
                        }
                    }
                }
            }
        }
        return ret;
    }
    private String getImageRealPath(ContentResolver contentResolver, Uri uri, String whereClause)
    {
        String ret = "";
        // Query the URI with the condition.
        Cursor cursor = contentResolver.query(uri, null, whereClause, null, null);
        if(cursor!=null)
        {
            boolean moveToFirst = cursor.moveToFirst();
            if(moveToFirst)
            {
                // Get columns name by URI type.
                String columnName = MediaStore.Images.Media.DATA;
                if( uri==MediaStore.Images.Media.EXTERNAL_CONTENT_URI )
                {
                    columnName = MediaStore.Images.Media.DATA;
                }else if( uri==MediaStore.Audio.Media.EXTERNAL_CONTENT_URI )
                {
                    columnName = MediaStore.Audio.Media.DATA;
                }else if( uri==MediaStore.Video.Media.EXTERNAL_CONTENT_URI )
                {
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
    public void onExpenseTypeGet(List<ExpenseTypeMaster> aList) {
        if (aList != null && aList.size() > 0) {
            try {
                compareTypeList = aList;
                ArrayAdapter<ExpenseTypeMaster> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, aList);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                binding.expanseType.setAdapter(dataAdapter);
            } catch (Exception exception) {
               // exception.printStackTrace();
            }
        } else {
            SnackBarManagement._warning_CustomMessage(binding.masterLayout, "Expense Not Saved!!");
        }
    }

    @Override
    public void onExpenseTypeDetailsGet(List<ExpenseTypeDetails> aList) {
        if (aList != null) {
            aSmDetail.clear();
            aSmDetail = aList;
            mAdapter = new _entrytxtWithValue_Recycler(aList, mListener);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            binding.recyclerViewitem.setLayoutManager(mLayoutManager);
            binding.recyclerViewitem.setItemAnimator(new DefaultItemAnimator());
            binding.recyclerViewitem.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onSaveSuccess(String message) {
        new androidx.appcompat.app.AlertDialog.Builder(ExpanseClamActivity.this)
                .setTitle("Success")
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        Intent i = new Intent(ExpanseClamActivity.this, MainDashboardActivity.class);
                        startActivity(i);
                        i.addFlags(i.FLAG_ACTIVITY_CLEAR_TOP | i.FLAG_ACTIVITY_CLEAR_TASK | i.FLAG_ACTIVITY_NEW_TASK);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    }
                }).setCancelable(false).show();
    }

    @Override
    public void onSaveError(String message) {
        new androidx.appcompat.app.AlertDialog.Builder(ExpanseClamActivity.this)
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
    public void onEditTextFocusChange(int position, String value) {
        aSmDetail.get(position).setValueText(value);
    }
}