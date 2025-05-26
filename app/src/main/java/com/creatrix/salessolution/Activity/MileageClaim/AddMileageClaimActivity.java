package com.creatrix.salessolution.Activity.MileageClaim;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
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
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.creatrix.salessolution.Activity.MainDashboardActivity;
import com.creatrix.salessolution.Activity.MileageClaim.Model.MileageListTeam;
import com.creatrix.salessolution.DBAdapter.DBCrudHelper;
import com.creatrix.salessolution.Interface.IMarketStracture;
import com.creatrix.salessolution.Interface.IMileageClaim;
import com.creatrix.salessolution.Model.MarketStructure.StructureTable.Area;
import com.creatrix.salessolution.Model.MarketStructure.StructureTable.Group;
import com.creatrix.salessolution.Model.MarketStructure.StructureTable.Market;
import com.creatrix.salessolution.Model.MarketStructure.StructureTable.Region;
import com.creatrix.salessolution.Model.MarketStructure.StructureTable.SubTeritorry;
import com.creatrix.salessolution.Model.MarketStructure.StructureTable.Teritorry;
import com.creatrix.salessolution.Model.MilageClaimReport;
import com.creatrix.salessolution.Model.MileageClaimSM;
import com.creatrix.salessolution.Model.Transport;
import com.creatrix.salessolution.Presenter.MarketStructurePresenter;
import com.creatrix.salessolution.Presenter.MileageClaimPresenter;
import com.creatrix.salessolution.R;
import com.creatrix.salessolution.Services.Constants;
import com.creatrix.salessolution.UtilityHelper.CameraHelper;
import com.creatrix.salessolution.UtilityHelper.SessionManagement;
import com.creatrix.salessolution.UtilityHelper.SnackBarManagement;
import com.creatrix.salessolution.databinding.ActivityAddMileageClaimBinding;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import id.zelory.compressor.Compressor;

public class AddMileageClaimActivity extends AppCompatActivity implements IMileageClaim.View, IMarketStracture.View {
    private static final int PERMISSION_REQUEST_CODE = 200;
    ActivityAddMileageClaimBinding binding;
    Uri imageuri;
    File imgFile;
    IMileageClaim.Presenter presenter;
    IMarketStracture.Presenter mpresenter;
    SessionManagement session;
    DBCrudHelper dbCrudHelper;

    //dependent spinner
    int selectedRegionId, selectedAreaId, selectedTeritoryId, selectedSTeritoryId, selectedMarketId;
    MileageListTeam mInfo;
    MilageClaimReport smInfo;
    String edit_MName, path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddMileageClaimBinding.inflate(getLayoutInflater());
        //setContentView(R.layout.activity_add_mileage_claim);
        setContentView(binding.getRoot());
        dbCrudHelper = new DBCrudHelper(AddMileageClaimActivity.this);
        binding.toolbarCustom.setNavigationOnClickListener(v -> finish());
        session = new SessionManagement(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        int empId = Integer.parseInt(Objects.requireNonNull(user.get(SessionManagement.KEY_EmpId)));
        String roleType = user.get(SessionManagement.KEY_EmpRoleType);

        String today = new SimpleDateFormat("dd-MMM-yyyy hh:mm a", Locale.getDefault()).format(new Date());
        binding.expdate.setText(today);
        presenter = new MileageClaimPresenter(this, this);
        mpresenter = new MarketStructurePresenter(this, this);
        switch (Constants.WHO) {
            case "MileageViewAC":
                binding.toolbarTitle.setText("Edit Mileage");
                presenter.GetTransportList();
                binding.btnSubmit.setVisibility(View.GONE);
                binding.btnUpdate.setVisibility(View.VISIBLE);
                binding.marketstructureDiv.setVisibility(View.GONE);
                Gson gson = new Gson();
                mInfo = gson.fromJson(getIntent().getStringExtra("mileageEditdata"), MileageListTeam.class);
                setupEdit(empId, mInfo);
                break;
            case "AddMileage":
                switch (Objects.requireNonNull(roleType)) {
                    case "MIO":
                        binding.areadiv.setVisibility(View.GONE);
                        mpresenter.GetTeritoryLocal(0);
                        break;
                    case "AM":
                        binding.areadiv.setVisibility(View.VISIBLE);
                        mpresenter.GetAreaLocal(0);
                        break;
                    case "DZSM":
                        binding.regiondiv.setVisibility(View.VISIBLE);
                        binding.areadiv.setVisibility(View.VISIBLE);
                        mpresenter.GetRegionLocal(0);
                        break;
                }
                presenter.GetTransportList();
                break;
            case "MileageViewSelfAC":
                binding.toolbarTitle.setText("Edit Mileage");
                Gson son = new Gson();
                smInfo = son.fromJson(getIntent().getStringExtra("SelfMileageEditdata"), MilageClaimReport.class);
                presenter.GetTransportList();
                binding.btnSubmit.setVisibility(View.GONE);
                binding.btnUpdate.setVisibility(View.VISIBLE);
                binding.marketstructureDiv.setVisibility(View.GONE);
                setupSelfEdit(empId, smInfo);
                break;
        }


        // imgaeView = findViewById(R.id.imgaeView);
    /*    binding.datePickerExpDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // UtilityHelper._datePickerDialogeForDates(binding.expdate, AddMileageClaimActivity.this);
                UtilityHelper._datePickerDialogeForDates_DisableNextDates(binding.expdate, AddMileageClaimActivity.this);
            }
        });*/
      /*  binding.galleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
            }
        });*/
        binding.camera.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(AddMileageClaimActivity.this,
                    Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED) {
                ImagePicker.Companion.with(AddMileageClaimActivity.this)
                        .crop()                    //Crop image(Optional), Check Customization for more option
                        .compress(1024)            //Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
            /*    Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), CAMERA_PIC_REQUEST);*/
            } else {
                ActivityCompat.requestPermissions(AddMileageClaimActivity.this,
                        new String[]{Manifest.permission.CAMERA},
                        PERMISSION_REQUEST_CODE);
            }

        });
        binding.btnSubmit.setOnClickListener(v -> {
            //  validate();
            String img_str = null;
            try {
                Bitmap bitmap = new Compressor(AddMileageClaimActivity.this).compressToBitmap(imgFile);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] image = stream.toByteArray();
                img_str = Base64.encodeToString(image, 0);
                System.out.println("img : " + img_str);
            } catch (Exception exception) {
                exception.printStackTrace();
            }

            if (TextUtils.isEmpty(binding.expdate.getText().toString())) {
                SnackBarManagement._warning_CustomMessage(binding.milagemaster, "Date is required");
                return;
            }
            if (TextUtils.isEmpty(binding.milageinKm.getText().toString())) {
                SnackBarManagement._warning_CustomMessage(binding.milagemaster, "Mileage (in Km) is required");
                return;
            }
            if (TextUtils.isEmpty(binding.meterReadingId.getText().toString())) {
                binding.meterReadingId.setFocusable(true);
                binding.meterReadingId.setError("Input required");
                SnackBarManagement._warning_CustomMessage(binding.milagemaster, "Meter Reading is required");
                return;
            }
            if (TextUtils.isEmpty(String.valueOf(selectedMarketId)) || selectedMarketId == 0) {
                SnackBarManagement._warning_CustomMessage(binding.milagemaster, "Market is required");
                return;
            }
            if (binding.imgaeView.getDrawable() == null) {
                SnackBarManagement._warning_CustomMessage(binding.milagemaster, "Image is required");
                return;
            }
            SaveData(empId, img_str);
        });
    }

    private int getIndex(Spinner tranportTypeSpinner, String edit_mName) {
        for (int i = 0; i < tranportTypeSpinner.getCount(); i++) {
            if (tranportTypeSpinner.getItemAtPosition(i).toString().equalsIgnoreCase(edit_mName)) {
                return i;

            }
        }
        return 0;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            imageuri = data.getData();
            binding.imgaeView.setImageURI(imageuri);
            if (imageuri != null) {
                path = getUriRealPathAboveKitkat(AddMileageClaimActivity.this, imageuri);
                if (path == null)
                    return;
                imgFile = new File(path);
                try {
                    imgFile = new Compressor(AddMileageClaimActivity.this).compressToFile(imgFile);
                    System.out.println("file" + imgFile);
                } catch (IOException e) {
                    Toast.makeText(AddMileageClaimActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private String getUriRealPathAboveKitkat(AddMileageClaimActivity activity, Uri contentURI) {
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
    public void onTransportListGet(List<Transport> aList) {
        if (aList != null) {
            ArrayAdapter<Transport> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, aList);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            binding.tranportTypeSpinner.setAdapter(dataAdapter);
        }
    }

    public void SaveData(int empId, String img_str) {
        try {
            String selectDate = binding.expdate.getText().toString();
            Transport trp = (Transport) binding.tranportTypeSpinner.getSelectedItem();
            int trpId = trp.getTransportId();


            MileageClaimSM aClaimSM = new MileageClaimSM();
            aClaimSM.setMileageClaimId(0);
            aClaimSM.setEmpInfoId(empId);
            aClaimSM.setMileageDate(selectDate);
            aClaimSM.setTransportId(trpId);
            aClaimSM.setMileageInKM(Double.parseDouble(binding.milageinKm.getText().toString()));
            aClaimSM.setMeterReading(Double.parseDouble(binding.meterReadingId.getText().toString()));
            aClaimSM.setRemarks(binding.remarksTxt.getText().toString());
            aClaimSM.setImageBase64String(img_str);
            aClaimSM.setMarketId(selectedMarketId);
            /*aClaimSM.setSMId(selectedMarketId);
            aClaimSM.setGroupId(selectedGroupId);
            aClaimSM.setRegionId(selectedRegionId);
            aClaimSM.setAreaId(selectedAreaId);
            aClaimSM.setTerritoryId(selectedTeritoryId);
            aClaimSM.setSubTerritoryId(selectedSTeritoryId);*/
            presenter.SaveMileageClaim(aClaimSM);
        } catch (Exception exception) {
            Toast.makeText(getApplicationContext(), "Some error occurred..Please try again", Toast.LENGTH_LONG).show();
            exception.printStackTrace();
        }
    }

    @Override
    public void onSaveSuccess(String message) {
        new androidx.appcompat.app.AlertDialog.Builder(AddMileageClaimActivity.this)
                .setTitle("Success")
                .setMessage(message)
                .setPositiveButton("OK", (dialog, which) -> {
                    dialog.cancel();
                    Intent i = new Intent(AddMileageClaimActivity.this, MainDashboardActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }).setCancelable(false).show();

    }

    @Override
    public void onSaveError(String message) {
        new androidx.appcompat.app.AlertDialog.Builder(AddMileageClaimActivity.this)
                .setTitle("Error")
                .setMessage(message)
                .setPositiveButton("OK", (dialog, which) -> dialog.cancel()).setCancelable(false).show();

    }

    @Override
    public void onMileageListGet(List<MilageClaimReport> aList) {

    }

    @Override
    public void vGroup(List<Group> groupList) {
    }

    @Override
    public void vRegion(List<Region> regionList) {
        try {
            if (regionList != null) {

                ArrayAdapter<Region> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, regionList);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                binding.regionSpinner.setAdapter(dataAdapter);
            }
            binding.regionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    Region trm = (Region) parent.getSelectedItem();
                    selectedRegionId = trm.getRegionId();
                    mpresenter.GetAreaLocal(selectedRegionId);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void vArea(List<Area> areaList) {
        try {
            if (areaList != null) {
                ArrayAdapter<Area> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, areaList);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                binding.areaSpinner.setAdapter(dataAdapter);
            }
            binding.areaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    Area trm = (Area) parent.getSelectedItem();
                    selectedAreaId = trm.getAreaId();
                    mpresenter.GetTeritoryLocal(selectedAreaId);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void vTeritory(List<Teritorry> teritoryList) {
        try {
            if (teritoryList != null) {
                ArrayAdapter<Teritorry> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, teritoryList);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                binding.territorySpinner.setAdapter(dataAdapter);
            }
            binding.territorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    Teritorry teri = (Teritorry) parent.getSelectedItem();
                    selectedTeritoryId = teri.getTerritoryId();
                    mpresenter.GetSTeritoryLocal(selectedTeritoryId);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void vSTeritory(List<SubTeritorry> steritoryList) {
        try {
            if (steritoryList != null) {
                ArrayAdapter<SubTeritorry> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, steritoryList);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                binding.subterritorySpinner.setAdapter(dataAdapter);
            }
            binding.subterritorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    SubTeritorry steri = (SubTeritorry) parent.getSelectedItem();
                    selectedSTeritoryId = steri.getSubTerritoryId();
                    mpresenter.GetMarketLocal(selectedSTeritoryId);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void vMarket(List<Market> marketList) {
        try {
            if (marketList != null) {
                ArrayAdapter<Market> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, marketList);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                binding.marketSpinner.setAdapter(dataAdapter);
            }
            binding.marketSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    Market market = (Market) parent.getSelectedItem();
                    selectedMarketId = market.getMarketId();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private void setupEdit(int empId, MileageListTeam cal) {
        binding.camera.setVisibility(View.GONE);
        Glide.with(AddMileageClaimActivity.this)
                .load(cal.getImageString())
                .fitCenter()
                .into(binding.imgaeView);
        binding.expdate.setText(cal.getMileageDate());
        if (cal.getTransportName() != null) {
            edit_MName = cal.getTransportName();
            binding.tranportTypeSpinner.setSelection(getIndex(binding.tranportTypeSpinner, edit_MName));
        } else {
            presenter.GetTransportList();
        }
        binding.milageinKm.setText(String.valueOf(cal.getMileageInKM()));
        binding.meterReadingId.setText(String.valueOf(cal.getMeterReading()));
        // binding.meterReadingId.setText(String.valueOf(cal.getm()));
        if (cal.getMarketName() != null) {
            edit_MName = cal.getMarketName();
            binding.marketSpinner.setSelection(getIndex(binding.marketSpinner, edit_MName));
        }
        if (cal.getComments() != null) {
            binding.remarksTxt.setText(cal.getComments());
        } else {
            binding.remarksTxt.setText("");
        }
        binding.btnUpdate.setOnClickListener(v -> UpdateMileage(empId, cal.getMileageClaimId(), cal.getImageString()));

    }

    private void setupSelfEdit(int empId, MilageClaimReport cal) {
        binding.camera.setVisibility(View.GONE);
        Glide.with(AddMileageClaimActivity.this)
                .load(cal.getImageString())
                .fitCenter()
                .into(binding.imgaeView);

        binding.expdate.setText(cal.getMileageDate());
        binding.milageinKm.setText(String.valueOf(cal.getMileageInKM()));

        if (smInfo.getTransportName() != null) {
            edit_MName = smInfo.getTransportName();
            binding.tranportTypeSpinner.setSelection(getIndex(binding.tranportTypeSpinner, edit_MName));
        }
        binding.meterReadingId.setText(String.valueOf(cal.getMeterReading()));
        if (cal.getMarketName() != null) {
            edit_MName = cal.getMarketName();
            binding.marketSpinner.setSelection(getIndex(binding.marketSpinner, edit_MName));
        }
        if (cal.getComments() != null) {
            binding.remarksTxt.setText(cal.getComments());
        } else {
            binding.remarksTxt.setText("");
        }
        binding.btnUpdate.setOnClickListener(v -> UpdateMileage(empId, cal.getMileageClaimId(), cal.getImageString()));
    }

    private void UpdateMileage(int empId, int mcid, String img_str) {
        try {
            String selectDate = binding.expdate.getText().toString();
            Transport trp = (Transport) binding.tranportTypeSpinner.getSelectedItem();
            int trpId = trp.getTransportId();

            MileageClaimSM aClaimSM = new MileageClaimSM();
            aClaimSM.setEmpInfoId(empId);
            aClaimSM.setMileageClaimId(mcid);
            aClaimSM.setMileageDate(selectDate);
            aClaimSM.setTransportId(trpId);
            aClaimSM.setMileageInKM(Double.parseDouble(binding.milageinKm.getText().toString()));
            aClaimSM.setMeterReading(Double.parseDouble(binding.meterReadingId.getText().toString()));
            aClaimSM.setRemarks(binding.remarksTxt.getText().toString());
            aClaimSM.setImageBase64String(img_str);
            aClaimSM.setMarketId(selectedMarketId);

           /* Gson gson = new Gson();
            String data=gson.toJson(aClaimSM);
            System.out.println("data: "+data);*/
            presenter.SaveMileageClaim(aClaimSM);


        } catch (Exception exception) {
            Toast.makeText(getApplicationContext(), "Some error occurred..Please try again", Toast.LENGTH_LONG).show();
            exception.printStackTrace();
        }
    }


}