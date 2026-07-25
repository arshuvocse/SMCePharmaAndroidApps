package com.creatrix.salessolution.Activity.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.creatrix.salessolution.Activity.Approval.VisitPlan.ActivityDash;
import com.creatrix.salessolution.Activity.Attendance.AttendanceReportsActivity;
import com.creatrix.salessolution.Activity.Customer.Pending.CustomerPendingActivity;
import com.creatrix.salessolution.Activity.Customer.UpdateCustomerActivity;
import com.creatrix.salessolution.Activity.DWSP.DWSPTargetActivity;
import com.creatrix.salessolution.Activity.Doctor.Pending.DoctorPendingActivity;
import com.creatrix.salessolution.Activity.Doctor.UpdateDoctorActivity;
import com.creatrix.salessolution.Activity.Expense.Report.ExpenseClaimReportsActivity;
import com.creatrix.salessolution.Activity.ProductViewActivity;
import com.creatrix.salessolution.Activity.Reports.DWSPReportActivity;
import com.creatrix.salessolution.Activity.Reports.LedgerReportActivity;
import com.creatrix.salessolution.Activity.Reports.ReceiveableReportActivity;
import com.creatrix.salessolution.Activity.SelfReports.ExpenseSummery.ExpenseSummeryActivity;
import com.creatrix.salessolution.Activity.SelfReports.LeaveRecordsActivity;
import com.creatrix.salessolution.Activity.MileageClaim.Report.MileageClaimListActivity;
import com.creatrix.salessolution.Activity.SelfReports.PrescriptionListActivity;
import com.creatrix.salessolution.Activity.SelfReports.ReportTVAActivity;
import com.creatrix.salessolution.Activity.SelfReports.ReportsStockActivity;
import com.creatrix.salessolution.Activity.SelfReports.ReportsTargetAcheiActivity;
import com.creatrix.salessolution.Activity.SelfReports.Reports_TadaActivity;
import com.creatrix.salessolution.Activity.SelfReports.SalesReport.SalesReportActivity;
import com.creatrix.salessolution.Activity.Team.TeamListActivity;
import com.creatrix.salessolution.R;
import com.creatrix.salessolution.Activity.SelfReports.ReportsDcrActivity;
import com.creatrix.salessolution.UtilityHelper.SessionManagement;
import com.creatrix.salessolution.UtilityHelper.SnackBarManagement;
import com.creatrix.salessolution.WebView.SixbyTenReportActivity;
import com.creatrix.salessolution.databinding.FragmentReportBinding;


import java.util.HashMap;

public class ReportFragment extends Fragment {
    //for role
    SessionManagement session;
    HashMap<String, String> userInfo = new HashMap<>();
    String role;
    FragmentReportBinding viewBinding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewBinding = FragmentReportBinding.inflate(getLayoutInflater());
        session = new SessionManagement(getActivity());
        userInfo = session.getUserDetails();
        role = userInfo.get(SessionManagement.KEY_EmpRoleType);
        return viewBinding.getRoot();
    //return inflater.inflate(R.layout.fragment_report, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewBinding.btnReceive.setVisibility(View.GONE);
        switch (role) {
            case "MIO":
                viewBinding.crdmileageClaimList.setVisibility(View.GONE);
                viewBinding.crdTeam.setVisibility(View.GONE);
                viewBinding.crdReportDWSP.setVisibility(View.GONE);
                viewBinding.crdAddDWSPTarget.setVisibility(View.GONE);
                viewBinding.dashboard.setVisibility(View.VISIBLE);
                viewBinding.btnReceive.setVisibility(View.VISIBLE);
                break;
            case "DZSM":
                viewBinding.mileageClaimList.setVisibility(View.GONE);
                viewBinding.crdTeam.setVisibility(View.VISIBLE);
                viewBinding.dashboard.setVisibility(View.VISIBLE);
                viewBinding.crdReportDWSP.setVisibility(View.VISIBLE);
                break;
            case "AM":
                viewBinding.btnUpdateCustomer.setVisibility(View.VISIBLE);
                viewBinding.btnUpdateDoctor.setVisibility(View.VISIBLE);
                viewBinding.crdReportDWSP.setVisibility(View.VISIBLE);
                viewBinding.dashboard.setVisibility(View.VISIBLE);
            case "NSM":
            case "Admin":
                viewBinding.crdTeam.setVisibility(View.VISIBLE);
                viewBinding.mileageClaimList.setVisibility(View.VISIBLE);
                viewBinding.dashboard.setVisibility(View.VISIBLE);
                break;
        }
        viewBinding.btnselfAtteClick.setOnClickListener(v -> {
            Intent i = new Intent(getActivity(), AttendanceReportsActivity.class);
            startActivity(i);
            requireActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });
        viewBinding.btnExpenseClaim.setOnClickListener(v -> {
            Intent i = new Intent(getActivity(), ExpenseClaimReportsActivity.class);
            startActivity(i);
            requireActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });
        //TODO:Expense TeamAtten Approve
        viewBinding.btnTadaClick.setOnClickListener(v -> {
            Intent i = new Intent(getActivity(), Reports_TadaActivity.class);
            startActivity(i);
            requireActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });
        viewBinding.btnOrdersumClick.setOnClickListener(v -> {

            Intent i = new Intent(getActivity(), SalesReportActivity.class);
            i.putExtra("From","Order");
            startActivity(i);
            requireActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });
        viewBinding.crdbtnsixten.setOnClickListener(v -> {


            Intent i = new Intent(getActivity(), SixbyTenReportActivity.class);
            startActivity(i);
            requireActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });
        viewBinding.btnSalesClick.setOnClickListener(v -> {
            Intent i = new Intent(getActivity(), SalesReportActivity.class);
            i.putExtra("From","Sales");
            startActivity(i);
            requireActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });
        viewBinding.btnExpensesumClick.setOnClickListener(v -> {
            Intent i = new Intent(getActivity(), ExpenseSummeryActivity.class);
            startActivity(i);
            requireActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });
        viewBinding.btnProductList.setOnClickListener(v -> {
            Intent i = new Intent(getActivity(), ProductViewActivity.class);
            startActivity(i);
            requireActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });
        viewBinding.btnStockList.setOnClickListener(v -> {
            Intent i = new Intent(getActivity(), ReportsStockActivity.class);
            i.putExtra("From","MainStock");
            startActivity(i);
            requireActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });

        viewBinding.btnSampleStockList.setOnClickListener(v -> {
            Intent i = new Intent(getActivity(), ReportsStockActivity.class);
            i.putExtra("From","SampleStock");
            startActivity(i);
            requireActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });

        viewBinding.btnTva.setOnClickListener(v -> {
            Intent i = new Intent(getActivity(), ReportsTargetAcheiActivity.class);
            startActivity(i);
            requireActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });
        viewBinding.btnTvat.setOnClickListener(v -> {
            Intent i = new Intent(getActivity(), ReportTVAActivity.class);
            startActivity(i);
            requireActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });
        viewBinding.crdReportDWSP.setOnClickListener(v -> {
            Intent i = new Intent(getActivity(), DWSPReportActivity.class);
            startActivity(i);
            requireActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });
        viewBinding.crdAddDWSPTarget.setOnClickListener(v -> {
            Intent i = new Intent(getActivity(), DWSPTargetActivity.class);
            startActivity(i);
            requireActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });

        viewBinding.btnSelfDcrList.setOnClickListener(v -> {
            Intent i = new Intent(getActivity(), ReportsDcrActivity.class);
            startActivity(i);
            requireActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });
        viewBinding.btnSelfprescriptionList.setOnClickListener(v -> {
            Intent i = new Intent(getActivity(), PrescriptionListActivity.class);
            i.putExtra("From", "Prescription");
            startActivity(i);
            requireActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });

        viewBinding.btnSelfcustomerList.setOnClickListener(v -> {
            Intent i = new Intent(getActivity(), CustomerPendingActivity.class);
            startActivity(i);
            requireActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });
        viewBinding.btnSelfDoctorList.setOnClickListener(v -> {
            Intent i = new Intent(getActivity(), DoctorPendingActivity.class);
            startActivity(i);
            requireActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });


        viewBinding.btnLeave.setOnClickListener(v -> {
            //SnackBarManagement._warning_CustomMessage(getView(), "Comming Soon!!");
            Intent i = new Intent(getActivity(), LeaveRecordsActivity.class);
            startActivity(i);
            requireActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });


        viewBinding.btnReceive.setOnClickListener(v -> {
            Intent i = new Intent(getActivity(), ReceiveableReportActivity.class);
            startActivity(i);
            requireActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });



        viewBinding.btnLedger.setOnClickListener(v -> {
            Intent i = new Intent(getActivity(), LedgerReportActivity.class);
            startActivity(i);
            requireActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });



        viewBinding.mileageClaimList.setOnClickListener(v -> {
            Intent i = new Intent(getActivity(), MileageClaimListActivity.class);
            startActivity(i);
            requireActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });

        viewBinding.btnTeam.setOnClickListener(v -> {
            Intent i = new Intent(getActivity(), TeamListActivity.class);
            startActivity(i);
            requireActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });

        viewBinding.btnUpdateCustomer.setOnClickListener(v -> {
            Intent i = new Intent(requireActivity(), UpdateCustomerActivity.class);
            startActivity(i);
            requireActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });
        viewBinding.btnUpdateDoctor.setOnClickListener(v -> {
            Intent i = new Intent(requireActivity(), UpdateDoctorActivity.class);
            startActivity(i);
            requireActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });

        viewBinding.dashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), ActivityDash.class);
                startActivity(i);
                requireActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

//        viewBinding.btnReceivableReport.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(getActivity(), ReceiveableReportActivity.class);
//                startActivity(i);
//                requireActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//            }
//        });

    }
}