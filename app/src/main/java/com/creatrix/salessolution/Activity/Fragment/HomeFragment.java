package com.creatrix.salessolution.Activity.Fragment;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.creatrix.salessolution.Activity.Attendance.AttendanceActivity;
import com.creatrix.salessolution.Activity.CampainActivity;
import com.creatrix.salessolution.Activity.Customer.CustomerActivity;
import com.creatrix.salessolution.Activity.Customer.CustomerEditListActivity;
import com.creatrix.salessolution.Activity.Customer.CustomerListActivity;
import com.creatrix.salessolution.Activity.DA.TADAClaimActivity;
import com.creatrix.salessolution.Activity.DWSP.DWSPActivity;
import com.creatrix.salessolution.Activity.Doctor.DoctorDashboardActivity;
import com.creatrix.salessolution.Activity.Doctor.Prescription.AddPrescriptionActivity;
import com.creatrix.salessolution.Activity.Doctor.TourePlan.TP.ActivityTourPlan_;
import com.creatrix.salessolution.Activity.Expense.ExpanseClamActivity;
import com.creatrix.salessolution.Activity.Leave.LeaveActivity;
import com.creatrix.salessolution.Activity.MileageClaim.AddMileageClaimActivity;
import com.creatrix.salessolution.Activity.MioOrderListActivity;
import com.creatrix.salessolution.Activity.Notice.NoticeActivity;
import com.creatrix.salessolution.Activity.Pending.PendingListActivity;
import com.creatrix.salessolution.Activity.TodaysTaskActivity;
import com.creatrix.salessolution.Activity.Training.TrainingListActivity;
import com.creatrix.salessolution.DBAdapter.DBCrudHelper;
import com.creatrix.salessolution.DBAdapter.ProductSQLiteHelper;
import com.creatrix.salessolution.Interface.IMioDashboard;
import com.creatrix.salessolution.Interface.INotice;
import com.creatrix.salessolution.Interface.IPendingCounter;
import com.creatrix.salessolution.Interface.IProduct;
import com.creatrix.salessolution.Interface.IVersionUpdate;
import com.creatrix.salessolution.Model.Dashboard_SummeryVM;
import com.creatrix.salessolution.Model.DcrSM;
import com.creatrix.salessolution.Model.Notice;
import com.creatrix.salessolution.Model.OrderDetailSample;
import com.creatrix.salessolution.Model.OrderDetails;
import com.creatrix.salessolution.Model.OrderMaster;
import com.creatrix.salessolution.Model.PrescriptionSM;
import com.creatrix.salessolution.Model.Product;
import com.creatrix.salessolution.Model.ProductSample;
import com.creatrix.salessolution.Presenter.MioDashboardPresenter;
import com.creatrix.salessolution.Presenter.NoticePresenter;
import com.creatrix.salessolution.Presenter.PendingCounterPresenter;
import com.creatrix.salessolution.Presenter.ProductPresenter;
import com.creatrix.salessolution.Presenter.VersionPresenter;
import com.creatrix.salessolution.R;
import com.creatrix.salessolution.Services.Constants;
import com.creatrix.salessolution.UtilityHelper.NetworkInformation;
import com.creatrix.salessolution.UtilityHelper.SessionManagement;
import com.creatrix.salessolution.UtilityHelper.SnackBarManagement;
import com.creatrix.salessolution.databinding.FragmentHomeBinding;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class HomeFragment extends Fragment implements IProduct.View, IMioDashboard.View, INotice.View, IVersionUpdate.View, IPendingCounter.View {
    FragmentHomeBinding viewBinding;
    SessionManagement session;
    String userName, loginName, role, Designation;
    ProductSQLiteHelper productSQLiteHelper;
    IProduct.Presenter presenter;
    IMioDashboard.Presenter mioDashboadPresenter;
    INotice.Presenter noticePresenter;
    IVersionUpdate.Presenter vPresenter = new VersionPresenter(this);
    PendingCounterPresenter presenterP;
    int empId, count_Total;
    DBCrudHelper crudHelper;
    int dcr, prc, ord;
    String currentDate;
    BroadcastReceiver Checkrcvr;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vPresenter.GetActiveVersion();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewBinding = FragmentHomeBinding.inflate(getLayoutInflater());
        session = new SessionManagement(getContext());
        crudHelper = new DBCrudHelper(requireActivity());
        session.checkLogin();
       // Checkrcvr = new DashboardRefresh();
       // registerBroadcust();

        HashMap<String, String> user = session.getUserDetails();
        userName = user.get(SessionManagement.KEY_UserName);
        loginName = user.get(SessionManagement.KEY_LoginName);
        empId = Integer.parseInt(Objects.requireNonNull(user.get(SessionManagement.KEY_EmpId)));
        role = user.get(SessionManagement.KEY_EmpRoleType);
        Designation = user.get(SessionManagement.KEY_EmpDesigName);
        // viewBinding.designation.setText(role);
        viewBinding.designation.setText(Designation);
        // viewBinding.hscroll.post(() -> viewBinding.hscroll.fullScroll(View.FOCUS_RIGHT));
        viewBinding.refDashboard.setOnClickListener(v -> {
            if (NetworkInformation.isConnected(requireActivity())) {
                mioDashboadPresenter.getDashboardSummeryData(empId, currentDate);
            } else {
                viewBinding.punchInTxt.setText("0");
                viewBinding.orderSubmitedTodayTxt.setText("0");
            }
        });
        return viewBinding.getRoot();
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_home, container, false);
    }

/*    public void registerBroadcust() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {
            requireActivity().registerReceiver(Checkrcvr, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            requireActivity().registerReceiver(Checkrcvr, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
    }
    private void unregisterNetwork() {
        try {
            requireActivity().unregisterReceiver(Checkrcvr);
        } catch (Exception exception) {
            //exception.printStackTrace();
        }
    }*/

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
          //  unregisterNetwork();
        } catch (Exception exception) {
           // exception.printStackTrace();
        }
    }

/*    public void changeTextStatus(boolean isConnected) {
        if (isConnected) {
            mioDashboadPresenter.getDashboardSummeryData(empId, currentDate);
        } else {
            viewBinding.punchInTxt.setText("0");
            viewBinding.orderSubmitedTodayTxt.setText("0");
        }
    }*/

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        switch (role) {
            case "MIO":
                viewBinding.mileageClaim.setVisibility(View.GONE);
                viewBinding.addDwsp.setVisibility(View.VISIBLE);
                break;
            case "AM":
                viewBinding.mileageClaim.setVisibility(View.VISIBLE);
                viewBinding.addDwsp.setVisibility(View.GONE);
                break;
            case "NSM":
            case "Admin":
                viewBinding.addDwsp.setVisibility(View.GONE);
                viewBinding.dashboardOrderCard.setVisibility(View.VISIBLE);
                viewBinding.dashboardAttendanceCard.setVisibility(View.VISIBLE);
                viewBinding.dashboardOrderRecords.setVisibility(View.VISIBLE);
                viewBinding.dashboardNoticeCard.setVisibility(View.VISIBLE);
                // viewBinding.clickProduct.setVisibility(View.VISIBLE);
                viewBinding.clickCampaing.setVisibility(View.VISIBLE);
                viewBinding.clickTraining.setVisibility(View.VISIBLE);
                viewBinding.dashboardTourPlanCard.setVisibility(View.VISIBLE);
                viewBinding.clickTada.setVisibility(View.VISIBLE);
                viewBinding.clickExpenseClaim.setVisibility(View.VISIBLE);
                viewBinding.dashboardDoctorCard.setVisibility(View.VISIBLE);
                viewBinding.dashboardLeaveCard.setVisibility(View.VISIBLE);
                viewBinding.dashboardCustomerCard.setVisibility(View.VISIBLE);
                viewBinding.todaysTaskSec.setVisibility(View.VISIBLE);
                viewBinding.mileageClaim.setVisibility(View.VISIBLE);
                // viewBinding.dashboardSampleOrderCard.setVisibility(View.VISIBLE);
                viewBinding.dashboardPendingmenuCard.setVisibility(View.VISIBLE);
                break;
            case "DZSM":
                viewBinding.addDwsp.setVisibility(View.GONE);
                viewBinding.mileageClaim.setVisibility(View.GONE);
                viewBinding.dashboardOrderCard.setVisibility(View.VISIBLE);
                viewBinding.dashboardAttendanceCard.setVisibility(View.VISIBLE);
                viewBinding.dashboardOrderRecords.setVisibility(View.VISIBLE);
                viewBinding.dashboardNoticeCard.setVisibility(View.VISIBLE);
                // viewBinding.clickProduct.setVisibility(View.VISIBLE);
                viewBinding.clickCampaing.setVisibility(View.VISIBLE);
                viewBinding.clickTraining.setVisibility(View.VISIBLE);
                viewBinding.dashboardTourPlanCard.setVisibility(View.VISIBLE);
                viewBinding.clickTada.setVisibility(View.VISIBLE);
                viewBinding.clickExpenseClaim.setVisibility(View.VISIBLE);
                viewBinding.dashboardDoctorCard.setVisibility(View.VISIBLE);
                viewBinding.dashboardLeaveCard.setVisibility(View.VISIBLE);
                viewBinding.dashboardCustomerCard.setVisibility(View.VISIBLE);
                viewBinding.todaysTaskSec.setVisibility(View.VISIBLE);
                // viewBinding.dashboardSampleOrderCard.setVisibility(View.VISIBLE);
                viewBinding.dashboardPendingmenuCard.setVisibility(View.VISIBLE);
                break;
        }
        viewBinding.userName.setText(userName);
        // sqlite intialization
        productSQLiteHelper = new ProductSQLiteHelper(getContext());
        presenter = new ProductPresenter(this, getContext());
        mioDashboadPresenter = new MioDashboardPresenter(this);
        noticePresenter = new NoticePresenter(this, getContext());
        noticePresenter.getNoticesforPop(empId);
        presenterP = new PendingCounterPresenter(this, getContext());
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        currentDate = df.format(c);
        if (NetworkInformation.isConnected(requireActivity())) {
            mioDashboadPresenter.getDashboardSummeryData(empId, currentDate);
        } else {
            viewBinding.punchInTxt.setText("0");
            viewBinding.orderSubmitedTodayTxt.setText("0");
        }
        presenterP.totalDcr();
        presenterP.totalPresc();
        presenterP.totalOrderMaster();
        try {
            viewBinding.pendingcount.setText(String.valueOf(dcr + prc + ord));
        } catch (Exception exception) {
        }
        viewBinding.rvPendingcount.setOnClickListener(v -> {
            Intent i = new Intent(requireActivity(), PendingListActivity.class);
            startActivity(i);
            requireActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });
        viewBinding.dashboardOrderCard.setOnClickListener(v -> {
            Intent i = new Intent(requireActivity(), CustomerListActivity.class);
            i.putExtra("OrderType", "Regular");
            startActivity(i);
            requireActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });
        viewBinding.dashboardAttendanceCard.setOnClickListener(v -> {
            if (!NetworkInformation.isConnected(requireActivity())) {
                SnackBarManagement.NoInternetSnackbar(viewBinding.masterLayout);
            } else {
                Intent i = new Intent(requireActivity(), AttendanceActivity.class);
                startActivity(i);
                requireActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
        viewBinding.dashboardOrderRecords.setOnClickListener(v -> {
            // Intent i = new Intent(getActivity(), MainTrackingDashboardActivity.class);
            Intent i = new Intent(requireActivity(), MioOrderListActivity.class);
            startActivity(i);
            requireActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });
        viewBinding.dashboardNoticeCard.setOnClickListener(v -> {
            if (NetworkInformation.isConnected(requireActivity())) {
                Intent i = new Intent(requireActivity(), NoticeActivity.class);
                startActivity(i);
                requireActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            } else {
                SnackBarManagement._warning_CustomMessage(viewBinding.masterLayout, "No Internet Connectivity");
            }
        });

         /*  viewBinding.clickProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //SnackBarManagement._warning_CustomMessage(getView(),"Comming Soon!!");
                Intent i = new Intent(getActivity(), ProductViewActivity.class);
                startActivity(i);
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });*/
        viewBinding.clickCampaing.setOnClickListener(v -> {
            if (NetworkInformation.isConnected(requireActivity())) {
                Intent i = new Intent(requireActivity(), CampainActivity.class);
                startActivity(i);
                requireActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            } else {
                SnackBarManagement._warning_CustomMessage(viewBinding.masterLayout, "No Internet Connectivity");
            }
        });
        viewBinding.clickTraining.setOnClickListener(v -> {
            if (NetworkInformation.isConnected(requireActivity())) {
                Intent i = new Intent(requireActivity(), TrainingListActivity.class);
                startActivity(i);
                requireActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            } else {
                SnackBarManagement._warning_CustomMessage(viewBinding.masterLayout, "No Internet Connectivity");
            }
        });
        viewBinding.dashboardTourPlanCard.setOnClickListener(v -> {
            if (NetworkInformation.isConnected(requireActivity())) {
                Intent i = new Intent(requireActivity(), ActivityTourPlan_.class);
                startActivity(i);
                requireActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            } else {
                SnackBarManagement._warning_CustomMessage(viewBinding.masterLayout, "No Internet Connectivity");
            }
        });
        viewBinding.clickTada.setOnClickListener(v -> {
            if (NetworkInformation.isConnected(requireActivity())) {
                Intent i = new Intent(requireActivity(), TADAClaimActivity.class);
                startActivity(i);
                requireActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            } else {
                SnackBarManagement._warning_CustomMessage(viewBinding.masterLayout, "No Internet Connectivity");
            }
        });
        viewBinding.clickExpenseClaim.setOnClickListener(v -> {
            if (NetworkInformation.isConnected(requireActivity())) {
                Constants.WHO = "HomeFragment";
                Intent i = new Intent(requireActivity(), ExpanseClamActivity.class);
                startActivity(i);
                requireActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            } else {
                SnackBarManagement._warning_CustomMessage(viewBinding.masterLayout, "No Internet Connectivity");
            }
        });
         /*   viewBinding.clickDCR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), AddDCRActivity.class);
                startActivity(i);
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });*/
        viewBinding.dashboardDoctorCard.setOnClickListener(v -> {
            Intent i = new Intent(requireActivity(), DoctorDashboardActivity.class);
            startActivity(i);
            requireActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });
        viewBinding.dashboardLeaveCard.setOnClickListener(v -> {
            if (NetworkInformation.isConnected(requireActivity())) {
                Intent i = new Intent(requireActivity(), LeaveActivity.class);
                startActivity(i);
                requireActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            } else {
                SnackBarManagement._warning_CustomMessage(viewBinding.masterLayout, "No Internet Connectivity");
            }
        });
        //extra
        viewBinding.dashboardLeavesCard.setOnClickListener(v -> {
            if (NetworkInformation.isConnected(requireActivity())) {
                Intent i = new Intent(requireActivity(), LeaveActivity.class);
                startActivity(i);
                requireActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            } else {
                SnackBarManagement._warning_CustomMessage(viewBinding.masterLayout, "No Internet Connectivity");
            }
        });
        viewBinding.dashboardNotices.setOnClickListener(v -> {
            if (NetworkInformation.isConnected(requireActivity())) {
                Intent i = new Intent(requireActivity(), NoticeActivity.class);
                startActivity(i);
                requireActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            } else {
                SnackBarManagement._warning_CustomMessage(viewBinding.masterLayout, "No Internet Connectivity");
            }
        });
        viewBinding.dashboardCustomerCard.setOnClickListener(v -> {
            if (NetworkInformation.isConnected(requireActivity())) {
                Constants.WHO = "HomeToCustomer";
                Intent i = new Intent(requireActivity(), CustomerActivity.class);
                startActivity(i);
                requireActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            } else {
                SnackBarManagement._warning_CustomMessage(viewBinding.masterLayout, "No Internet Connectivity");
            }
        });
        viewBinding.dashboardCustomerCardEdit.setOnClickListener(v -> {
            if (NetworkInformation.isConnected(requireActivity())) {
                Constants.WHO = "HomeToCustomerEdit";

                Intent i = new Intent(requireActivity(), CustomerEditListActivity.class);
                i.putExtra("OrderType", "HomeToCustomerEdit");
                startActivity(i);
                requireActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
        viewBinding.prescription.setOnClickListener(v -> {
            Intent i = new Intent(requireActivity(), AddPrescriptionActivity.class);
            startActivity(i);
            requireActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });
        viewBinding.todaysTaskSec.setOnClickListener(v -> {
            Intent i = new Intent(requireActivity(), TodaysTaskActivity.class);
            startActivity(i);
            requireActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });
        viewBinding.mileageClaim.setOnClickListener(v -> {
            if (NetworkInformation.isConnected(requireActivity())) {
                Constants.WHO = "AddMileage";
                Intent i = new Intent(requireActivity(), AddMileageClaimActivity.class);
                startActivity(i);
                requireActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            } else {
                SnackBarManagement._warning_CustomMessage(viewBinding.masterLayout, "No Internet Connectivity");
            }
        });
        viewBinding.addDwsp.setOnClickListener(view1 -> {
            Intent i = new Intent(getActivity(), DWSPActivity.class);
            startActivity(i);
            requireActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });
        /*viewBinding.dashboardSampleOrderCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //SnackBarManagement._warning_CustomMessage(getView(),"Comming Soon!!");
               Intent i = new Intent(getActivity(), DoctorListActivity.class);
                i.putExtra("From", "Samplerequi");
                startActivity(i);
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });*/
        viewBinding.dashboardPendingmenuCard.setOnClickListener(v -> {
            Intent i = new Intent(requireActivity(), PendingListActivity.class);
            startActivity(i);
            requireActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });


    }

    @Override
    public void OnError(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProductsGet(List<Product> aList) {
    }

    @Override
    public void onProductSampleGet(List<ProductSample> aList) {
    }

    @Override
    public void onDashboardSummeryDataBind(Dashboard_SummeryVM aData) {
        try {
            if (aData != null) {
                if (aData.getPunchInTime() != null) {
                    if (!aData.getPunchInTime().equals("0")) {
                        viewBinding.punchInTxt.setText(aData.getPunchInTime());
                    }

                }
                if (aData.getOrderTodayAmt() != null) {
                    if (!aData.getOrderTodayAmt().equals("0")) {
                        viewBinding.orderSubmitedTodayTxt.setText(aData.getOrderTodayAmt());

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
    public void onSuccess(List<Notice> nList) {
        if (nList != null) {
            try {
                int count_True = 0;
                int count_False = 0;
                for (Notice b : nList) {
                    if (b.getAppCheck()) {
                        count_True++;
                    }
                    if (!b.getAppCheck()) {
                        count_False++;
                    }
                    count_Total = count_True + count_False;
                }

            } catch (Exception exception) {
                exception.printStackTrace();
            }

        }
    }

    @Override
    public void onError(String message) {
        Log.e("MioDashboard", "onDashboardSummeryDataBind: Error on TopBar Summery");
    }

    public void NewNoticeDialog(int count_unseen, int count_seen, int count_total, String title) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(requireActivity());
        alertDialogBuilder.setIcon(R.drawable.ic_noticeboard);
        alertDialogBuilder.setTitle("Notice Available!!!");
        //alertDialogBuilder.setMessage(Html.fromHtml("<font color='#ff7400'>"+"Total Unseen Notice : "+count+"\n"+title+"</font>"));
        alertDialogBuilder.setMessage("Total Notice : " + count_total + "\n" + "Seen Notice : " + count_seen + "\n" + "Unseen Notice : " + count_unseen + "\n" + title);
        alertDialogBuilder.setPositiveButton(
                "Yes",
                (arg0, arg1) -> {
                    arg0.dismiss();
                    Intent goto_notice = new Intent(requireActivity(), NoticeActivity.class);
                    requireActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    startActivity(goto_notice);
                });
        alertDialogBuilder.setNegativeButton(
                "No",
                (arg0, arg1) -> arg0.dismiss());
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelable(false);
        alertDialog.show();
        TextView textView = alertDialog.getWindow().findViewById(android.R.id.message);
        Typeface face = Typeface.createFromAsset(requireActivity().getAssets(), String.valueOf(R.font.lato));
        textView.setTypeface(face);

    }

    @Override
    public void onPause() {
        super.onPause();
        noticePresenter.getNoticesforPop(empId);
        vPresenter.GetActiveVersion();
    /*    presenterP.totalDcr();
        presenterP.totalPresc();
        presenterP.totalOrderMaster();*/
    }

    private String GetVersionName() {
        String version = "";
        try {
            PackageInfo pInfo = requireActivity().getPackageManager().getPackageInfo(requireActivity().getPackageName(), 0);
            version = pInfo.versionName;

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }


        return version;
    }

    public void UpdateDialogz() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(requireActivity());
        alertDialogBuilder.setIcon(R.drawable.ic_warning);
        alertDialogBuilder.setTitle("Update Available!!!");
        alertDialogBuilder.setMessage("An update is required to use the application.\n\nPlease download and update the app first. \nIf You don't get any apk file please contact Admin People");
        alertDialogBuilder.setPositiveButton("Go to Download Page",
                (arg0, arg1) -> {
                    String url = "http://103.244.247.179:181/";
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelable(false);

        alertDialog.show();
    }

    @Override
    public void onVersionGet(String VersionName) {
        try {
            //versionNameGlob=VersionName;
            if (!GetVersionName().equals(VersionName)) {
                //TODO:Uncomment this line in live server
                UpdateDialogz();
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Override
    public void totalDcr(List<DcrSM> dcrList) {
        if (dcrList != null) {
            dcr = dcrList.size();
        } else {
            dcr = 0;
        }

    }

    @Override
    public void totalPresc(List<PrescriptionSM> preList) {
        if (preList != null) {
            prc = preList.size();
        } else {
            prc = 0;
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
        try {
            if (oList != null) {
                ord = oList.size();
            } else {
                ord = 0;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
     /*   if (NetworkInformation.isConnected(requireActivity())) {
            presenterP.totalDcr();
            presenterP.totalPresc();
            presenterP.totalOrderMaster();
            mioDashboadPresenter.getDashboardSummeryData(empId, currentDate);
        }*/
        //unregisterNetwork();

    }

    @Override
    public void onStart() {
        super.onStart();
       /* if (NetworkInformation.isConnected(requireActivity())) {
            presenterP.totalDcr();
            presenterP.totalPresc();
            presenterP.totalOrderMaster();
            mioDashboadPresenter.getDashboardSummeryData(empId, currentDate);
        }*/

    }
}