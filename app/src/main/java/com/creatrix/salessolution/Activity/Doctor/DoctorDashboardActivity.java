package com.creatrix.salessolution.Activity.Doctor;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.creatrix.salessolution.Activity.Doctor.AddDoctor.DoctorActivity;
import com.creatrix.salessolution.Activity.Doctor.VisitPlan.VisitPlanActivity;
import com.creatrix.salessolution.Interface.IMioDashboard;
import com.creatrix.salessolution.Interface.IPendingCounter;
import com.creatrix.salessolution.Model.Dashboard_SummeryVM;
import com.creatrix.salessolution.Model.DcrSM;
import com.creatrix.salessolution.Model.OrderDetailSample;
import com.creatrix.salessolution.Model.OrderDetails;
import com.creatrix.salessolution.Model.OrderMaster;
import com.creatrix.salessolution.Model.PrescriptionSM;
import com.creatrix.salessolution.Presenter.MioDashboardPresenter;
import com.creatrix.salessolution.Presenter.PendingCounterPresenter;
import com.creatrix.salessolution.R;
import com.creatrix.salessolution.Services.Constants;
import com.creatrix.salessolution.UtilityHelper.SessionManagement;
import com.creatrix.salessolution.databinding.ActivityDoctorDashboardBinding;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class DoctorDashboardActivity extends AppCompatActivity implements IPendingCounter.View , IMioDashboard.View {
    ActivityDoctorDashboardBinding viewBinding;
    PendingCounterPresenter presenter;
    String penDcr, penPresc;
    MioDashboardPresenter mioDashboadPresenter;
    SessionManagement session;
    int empId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_doctor_dashboard);

        viewBinding = com.creatrix.salessolution.databinding.ActivityDoctorDashboardBinding.inflate(getLayoutInflater());
        View mainView = viewBinding.getRoot();
        setContentView(mainView);
        presenter = new PendingCounterPresenter(this, DoctorDashboardActivity.this);
        mioDashboadPresenter = new MioDashboardPresenter(this);

        session = new SessionManagement(DoctorDashboardActivity.this);
        session.checkLogin();
        HashMap<String, String> user = session.getUserDetails();
        empId = Integer.parseInt(user.get(SessionManagement.KEY_EmpId));
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        String formattedDate = df.format(c);
        String currentDate = formattedDate;
        presenter.totalDcr();
        presenter.totalPresc();
        mioDashboadPresenter.getDashboardSummeryData(empId, currentDate);

        viewBinding.toolbarCustom.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        viewBinding.addDoctorClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constants.WHO = "AddDoctor";
                Intent i = new Intent(DoctorDashboardActivity.this, DoctorActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
        viewBinding.prescriptionCLick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  SnackBarManagement._warning_CustomMessage(v, "Comming Soon!!");
                Intent i = new Intent(DoctorDashboardActivity.this, DoctorListActivity.class);
                i.putExtra("From", "Prescription");
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        /*viewBinding.prescriptionListClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DoctorDashboardActivity.this, PrescriptionListActivity.class);
                i.putExtra("From", "Prescription");
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });*/

        viewBinding.DoctorVisitPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent i = new Intent(DoctorDashboardActivity.this, DoctorTourPlanActivity.class);
                //Intent i = new Intent(DoctorDashboardActivity.this, DoctorVisitPlanAC.class);
                Intent i = new Intent(DoctorDashboardActivity.this, VisitPlanActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });


        viewBinding.dcrClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //SnackBarManagement._warning_CustomMessage(v, "Comming Soon!!");
                Intent i = new Intent(DoctorDashboardActivity.this, DoctorListActivity.class);
                i.putExtra("From", "DCR");
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        viewBinding.doctorreqClick.setOnClickListener(v -> {
            Intent i = new Intent(DoctorDashboardActivity.this, DoctorListActivity.class);
            i.putExtra("From", "Samplerequi");
            startActivity(i);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });


      /*  viewBinding.doctorApprovalClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DoctorDashboardActivity.this, DoctorApprovalListActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });*/

    }

    @Override
    public void totalDcr(List<DcrSM> dcrList) {
        try {

            if (dcrList.size() > 0) {
                viewBinding.pendingDcrTxt.setText(String.valueOf(dcrList.size()));
            } else {
                viewBinding.pendingDcrTxt.setText("0");
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void totalPresc(List<PrescriptionSM> preList) {
        try {
            if (preList.size() > 0) {
                viewBinding.pendingPrscTxt.setText(String.valueOf(preList.size()));
            } else {
                viewBinding.pendingPrscTxt.setText("0");
            }

        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void totalSample(List<OrderDetailSample> soList) {

    }

    @Override
    public void totalOrder(List<OrderDetails> oList) {

    }

    @Override
    public void totalOrderMaster(List<OrderMaster> oList) {

    }

    @Override
    public void onDashboardSummeryDataBind(Dashboard_SummeryVM aData) {
        try {
            if (aData != null) {

             /*   if (aData.getPunchInTime() != null) {
                    if (!aData.getPunchInTime().equals("0")) {
                        viewBinding.punchInTxt.setText(aData.getPunchInTime());
                    }

                }
                if (aData.getWeeklySaleAmount() != null) {
                    if (!aData.getWeeklySaleAmount().equals("0")) {
                        viewBinding.weeklySaleTxt.setText(aData.getWeeklySaleAmount());
                    }

                }
                if (aData.getMonthlySaleAmount() != null) {
                    if (!aData.getMonthlySaleAmount().equals("0")) {
                        viewBinding.monthlySaleTxt.setText(aData.getMonthlySaleAmount());

                    }
                }

                if (aData.getOrderSubmitedToday() != null) {
                    if (!aData.getOrderSubmitedToday().equals("0")) {
                        viewBinding.orderSubmitedTodayTxt.setText(aData.getOrderSubmitedToday());

                    }
                }

                if (aData.getOrderTodayAmt() != null) {
                    if (!aData.getOrderTodayAmt().equals("0")) {
                        viewBinding.todayOrderAmtTxt.setText(aData.getOrderTodayAmt());

                    }
                }

                if (aData.getProductTotalOrder() != null) {
                    if (!aData.getProductTotalOrder().equals("0")) {
                        viewBinding.totalProductTxt.setText(aData.getProductTotalOrder());

                    }
                }*/

                if (aData.getTotalDcr() != null) {
                    if (!aData.getTotalDcr().equals("0")) {
                        viewBinding.totalDcrTxt.setText(aData.getTotalDcr());

                    }
                }
                if (aData.getTotalDcr() != null) {
                    if (!aData.getTotalPrescription().equals("0")) {
                        viewBinding.totalPrscTxt.setText(aData.getTotalPrescription());

                    }
                }


            }

        } catch (Exception ex) {
            Log.e("MioDashboard", "onDashboardSummeryDataBind: Error on TopBar Summery");
        }
    }

    @Override
    public void onTodaySummeryDataBind(Dashboard_SummeryVM aData) {

    }

    @Override
    public void onError(String message) {

    }
}