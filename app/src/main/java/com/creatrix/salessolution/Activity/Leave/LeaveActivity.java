package com.creatrix.salessolution.Activity.Leave;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.creatrix.salessolution.Activity.MainDashboardActivity;
import com.creatrix.salessolution.Interface.ILeave;
import com.creatrix.salessolution.Model.LeaveSM;
import com.creatrix.salessolution.Model.LeaveTypeInfo;
import com.creatrix.salessolution.Model.LeaveVM;
import com.creatrix.salessolution.Presenter.LeavePresenter;
import com.creatrix.salessolution.R;
import com.creatrix.salessolution.UtilityHelper.SessionManagement;
import com.creatrix.salessolution.UtilityHelper.SnackBarManagement;
import com.creatrix.salessolution.UtilityHelper.UtilityHelper;
import com.creatrix.salessolution.databinding.ActivityLeaveBinding;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Period;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class LeaveActivity extends AppCompatActivity implements ILeave.View {
    ActivityLeaveBinding viewBinding;
    ILeave.Presenter presenter;
    private static final int PERMISSION_REQUEST_CODE = 200;
    public static final int PICK_IMAGE = 1;
    private static final int CAMERA_REQUEST = 1888;
    private static final int CAMERA_PIC_REQUEST = 22;
    Uri imageuri;
    long diffDays;
    DatePickerDialog picker;
    String startdate, enddate;
    int selectedday;
    private Period period;
    private String monthNameArray[] = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
    public String monthNameArrayFull[] = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_leave);
        viewBinding = com.creatrix.salessolution.databinding.ActivityLeaveBinding.inflate(getLayoutInflater());
        View mainView = viewBinding.getRoot();
        setContentView(mainView);
        //Toolbar toolbar = findViewById(R.id.toolbar_custom);
        viewBinding.toolbarCustom.setNavigationOnClickListener(v -> finish());

        viewBinding.endDate.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0) {
                    String sdate, edate;
                    sdate = viewBinding.startDate.getText().toString().trim();
                    edate = viewBinding.endDate.getText().toString().trim();
                    try {
                        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
                        Date d1 = sdf.parse(sdate);
                        Date d2 = sdf.parse(edate);
                        long oneday = 24 * 60 * 60 * 1000;
                        long diff = d2.getTime() - d1.getTime();
                        diffDays = (diff + oneday) / (24 * 60 * 60 * 1000);
                        viewBinding.duration.setText(String.valueOf(diffDays));

                        LeaveTypeInfo leaveTypeInfo = (LeaveTypeInfo) viewBinding.spinerLeaveType.getSelectedItem();
                        if (sdate == null) {
                            SnackBarManagement._warning_CustomMessage(viewBinding.masterLayout, "Start Date required");
                            return;
                        }
                        if (edate == null) {
                            SnackBarManagement._warning_CustomMessage(viewBinding.masterLayout, "End Date required");
                            return;
                        }
                        if (String.valueOf(leaveTypeInfo.getLeaveBalanceId()).equals("")) {
                            viewBinding.datPickerEndDate.setClickable(false);
                            SnackBarManagement._warning_CustomMessage(viewBinding.masterLayout, "Leave Type Can't be null");
                            return;
                        }

                        if (leaveTypeInfo.getLeaveBalanceId() == 2) {
                            viewBinding.presclay.setVisibility(View.GONE);
                            if (diffDays >= 4 /*&& diffDays == 3*/) {
                                viewBinding.presclay.setVisibility(View.VISIBLE);
                                if (viewBinding.imgaeView.getDrawable() == null) {
                                    SnackBarManagement._warning_CustomMessage(viewBinding.masterLayout, "Prescription required");
                                    return;
                                }
                            } else {
                                viewBinding.presclay.setVisibility(View.GONE);
                            }
                        }

                        if (leaveTypeInfo.getLeaveBalanceId() == 3) {
                            viewBinding.presclay.setVisibility(View.GONE);
                            if (diffDays > 3) {
                                SnackBarManagement._warning_CustomMessage(viewBinding.masterLayout, "Casual Leave Max 3 day");
                                viewBinding.btnSubmit.setEnabled(false);
                                return;
                            } else {
                                viewBinding.btnSubmit.setEnabled(true);
                            }
                        }

                        //  Toast.makeText(LeaveActivity.this, "Balance : "+String.valueOf(leaveTypeInfo.getYearlyLeaveBalance()), Toast.LENGTH_SHORT).show();
                        if (leaveTypeInfo.getYearlyLeaveBalance() != 0) {
                            viewBinding.btnSubmit.setEnabled(true);
                            if (diffDays > leaveTypeInfo.getYearlyLeaveBalance()) {
                                SnackBarManagement._warning_CustomMessage(viewBinding.masterLayout, "You Haven't Enough Leave Balance");
                                viewBinding.btnSubmit.setEnabled(false);
                                return;
                            } else {
                                viewBinding.btnSubmit.setEnabled(true);
                            }
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }


                }
            }
        });
        viewBinding.datePickerStartDate.setOnClickListener(v -> {
            /*  UtilityHelper._datePickerDialogeForDates(viewBinding.startDate, LeaveActivity.this)*/
            Calendar cldr = Calendar.getInstance();
            int day = cldr.get(Calendar.DAY_OF_MONTH);
            int month = cldr.get(Calendar.MONTH);
            int year = cldr.get(Calendar.YEAR);
            picker = new DatePickerDialog(this,
                    (view, year1, monthOfYear, dayOfMonth) -> {
                        int monthNumber = monthOfYear + 1;
                        String attSelectedMonths = Integer.toString(dayOfMonth) + "-" + monthNameArray[monthOfYear] + "-" + year1;
                        startdate = Integer.toString(dayOfMonth) + "-" + monthNameArray[monthOfYear] + "-" + year1;
                        // startdate= monthOfYear + "/" + Integer.toString(dayOfMonth) + "/" + year;
                        selectedday=dayOfMonth;
                        viewBinding.startDate.setText(attSelectedMonths);
                        //  viewBinding.startDate.addTextChangedListener(watcher);
                    }, year, month, day);
            // picker.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
            picker.show();
        });
        viewBinding.datPickerEndDate.setOnClickListener(v -> {
            if (viewBinding.startDate.getText().toString().trim().isEmpty()) {
                SnackBarManagement._warning_CustomMessage(viewBinding.masterLayout, "Select Start Date First");
                return;

            }
            Calendar cldr = Calendar.getInstance();
            int day = cldr.get(Calendar.DAY_OF_MONTH);
            int month = cldr.get(Calendar.MONTH);
            int year = cldr.get(Calendar.YEAR);
            picker = new DatePickerDialog(this,
                    (view, year12, monthOfYear, dayOfMonth) -> {

                        int monthNumber = monthOfYear + 1;
                        String attSelectedMonths = Integer.toString(dayOfMonth) + "-" + monthNameArray[monthOfYear] + "-" + year12;
                        enddate = Integer.toString(dayOfMonth) + "-" + monthNameArray[monthOfYear] + "-" + year12;
                        //enddate= monthOfYear + "/" + Integer.toString(dayOfMonth) + "/" + year;
                        viewBinding.endDate.setText(attSelectedMonths);
                        // viewBinding.endDate.addTextChangedListener(watcher);
                    }, year, month, selectedday+1/*day+1*/);
            //picker.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
            //picker.getDatePicker().setMinDate(System.currentTimeMillis() - 604800000);
            picker.show();


        } /*UtilityHelper._datePickerDialogeForDates(viewBinding.endDate, LeaveActivity.this)*/);
        viewBinding.datPickerReturnDate.setOnClickListener(v -> UtilityHelper._datePickerDialogeForDates(viewBinding.returnDate, LeaveActivity.this));

        presenter = new LeavePresenter(this, this);
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        SessionManagement session = new SessionManagement(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        int empId = Integer.parseInt(user.get(SessionManagement.KEY_EmpId));
        presenter.GetLeaveTyep(empId, year);
        viewBinding.btnSubmit.setOnClickListener(v ->
        {
            String img_str = null;
            try {
                viewBinding.imgaeView.buildDrawingCache();
                Bitmap bitmap = viewBinding.imgaeView.getDrawingCache();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream);
                byte[] image = stream.toByteArray();
                img_str = Base64.encodeToString(image, 0);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            LeaveSM leaveSM = new LeaveSM();
            LeaveTypeInfo leaveTypeInfo = (LeaveTypeInfo) viewBinding.spinerLeaveType.getSelectedItem();
            if (startdate == null) {
                SnackBarManagement._warning_CustomMessage(viewBinding.masterLayout, "Start Date required");
                return;
            }
            if (enddate == null) {
                SnackBarManagement._warning_CustomMessage(viewBinding.masterLayout, "End Date required");
                return;
            }
           /* if (viewBinding.imgaeView.getDrawable() == null) {
                SnackBarManagement._warning_CustomMessage(viewBinding.masterLayout, "Prescription required");
                return;
            }*/
            if (viewBinding.reason.getText().toString().trim().isEmpty()) {
                SnackBarManagement._warning_CustomMessage(viewBinding.masterLayout, "Reason Can't be Empty");
                return;
            }

            if (viewBinding.returnDate.getText().toString().trim().isEmpty()) {
                SnackBarManagement._warning_CustomMessage(viewBinding.masterLayout, "Return Date Can't Empty");
                return;

            }
            if (viewBinding.emContact.getText().toString().trim().isEmpty() || viewBinding.emContact.getText().toString().trim().length() <= 10) {
                SnackBarManagement._warning_CustomMessage(viewBinding.masterLayout, "Emergency Contact Can't Be Empty Or Less Than 11");
                return;
            }
            SaveLeave(leaveSM, leaveTypeInfo, empId, img_str);
        });
        viewBinding.galleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
            }
        });
        viewBinding.camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(LeaveActivity.this,
                        Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), CAMERA_PIC_REQUEST);

                } else {
                    ActivityCompat.requestPermissions(LeaveActivity.this,
                            new String[]{Manifest.permission.CAMERA},
                            PERMISSION_REQUEST_CODE);
                }
            }
        });
    }


    private void SaveLeave(LeaveSM leaveSM, LeaveTypeInfo leaveTypeInfo, int empId, String img_str) {
        //validate();
       /* if (viewBinding.imgaeView.getDrawable() == null) {
            SnackBarManagement._warning_CustomMessage(viewBinding.masterLayout, "Prescription required");
            return;
        }*/
        try {
            leaveSM.setLeaveApplicationId(0);
            leaveSM.setLeaveBalanceId(leaveTypeInfo.getLeaveBalanceId());
            leaveSM.setStartDate(viewBinding.startDate.getText().toString());
            leaveSM.setEndDate(viewBinding.endDate.getText().toString());
            leaveSM.setReason(viewBinding.reason.getText().toString());
            leaveSM.setEmpId(empId);
            leaveSM.setDateOfReturnsToDuty(viewBinding.returnDate.getText().toString());
            leaveSM.setLeaveAddress(viewBinding.leaveAddress.getText().toString());
            leaveSM.setEmergencyContactNo(viewBinding.emContact.getText().toString());
            leaveSM.setImageBase64String(img_str);
            leaveSM.setComments(viewBinding.comments.getText().toString());
            presenter.SaveLeave(leaveSM);
            Gson gson = new Gson();
            String data = gson.toJson(leaveSM);
            System.out.println("json" + data);
            // Toast.makeText(this, "rsp : "+data, Toast.LENGTH_SHORT).show();

        } catch (Exception exception) {

            exception.printStackTrace();
        }

    }


    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left,
                R.anim.slide_out_right);
    }

    @Override
    public void onLeaveTypeGet(List<LeaveTypeInfo> aList) {
        try {
            ArrayAdapter<LeaveTypeInfo> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, aList);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            viewBinding.spinerLeaveType.setAdapter(dataAdapter);
            viewBinding.spinerLeaveType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    // your code here
                    LeaveTypeInfo lt = (LeaveTypeInfo) parentView.getSelectedItem();
                    int lid = lt.getLeaveBalanceId();
                    if (lid == 2) {
                        viewBinding.presclay.setVisibility(View.GONE);
                    }
                    viewBinding.endDate.setText("");
                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                    // your code here
                }

            });
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void onSaveSuccess(String message) {
        new androidx.appcompat.app.AlertDialog.Builder(LeaveActivity.this)
                .setTitle("Success")
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        Intent i = new Intent(LeaveActivity.this, MainDashboardActivity.class);
                        startActivity(i);
                        i.addFlags(i.FLAG_ACTIVITY_CLEAR_TOP | i.FLAG_ACTIVITY_CLEAR_TASK | i.FLAG_ACTIVITY_NEW_TASK);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    }

                }).setCancelable(false).show();
    }

    @Override
    public void onSaveError(String message) {
        new androidx.appcompat.app.AlertDialog.Builder(LeaveActivity.this)
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
    public void onLeaveRecordsGet(List<LeaveVM> aList) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK) {
            imageuri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageuri);
                viewBinding.imgaeView.setImageBitmap(bitmap);

            } catch (IOException exception) {
                exception.printStackTrace();

            }

        }
        if (requestCode == CAMERA_PIC_REQUEST && resultCode == RESULT_OK) {
            try {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                viewBinding.imgaeView.setImageBitmap(photo);
            } catch (Exception ex) {
                Toast.makeText(LeaveActivity.this, ex.toString(), Toast.LENGTH_LONG).show();
            }

        }


    }
}