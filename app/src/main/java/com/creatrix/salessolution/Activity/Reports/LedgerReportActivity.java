package com.creatrix.salessolution.Activity.Reports;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.creatrix.salessolution.UtilityHelper.NetworkInformation;
import com.creatrix.salessolution.UtilityHelper.SessionManagement;
import com.creatrix.salessolution.UtilityHelper.SnackBarManagement;
import com.creatrix.salessolution.databinding.ActivityLedgerReportBinding;
import com.creatrix.salessolution.databinding.ActivityReceiveableReportBinding;

import java.util.HashMap;
import java.util.Objects;

public class LedgerReportActivity extends AppCompatActivity {
ActivityLedgerReportBinding  binding;
    SessionManagement session;
    ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLedgerReportBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        session = new SessionManagement(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        int empId = Integer.parseInt(Objects.requireNonNull(user.get(SessionManagement.KEY_EmpId)));
        binding.toolbarCustom.setNavigationOnClickListener(v -> finish());
        pd=new ProgressDialog(LedgerReportActivity.this);
        pd.setTitle("Loading");
        pd.setMessage("Please wait...");

        if (NetworkInformation.isConnected(LedgerReportActivity.this)) {
            viewLoad(empId);
        } else {
            SnackBarManagement._error_CustomMessage(binding.getRoot(), "No Internet!!!");
        }
    }


    @SuppressLint("SetJavaScriptEnabled")
    private void viewLoad(int userId) {
        String urlBuilder = "http://13.76.141.111:456/SInventory_UI/CustomerLedgerReportApps.aspx?EMPID="+userId;
        // progressBar = ProgressDialog.show(DWSPTargetActivity.this, "Loading", "Please wait...");
        binding.webviewDwspTarget.getSettings().setJavaScriptEnabled(true);
        binding.webviewDwspTarget.setWebChromeClient(new WebChromeClient());
        binding.webviewDwspTarget.getSettings().setLoadWithOverviewMode(true);
        binding.webviewDwspTarget.getSettings().setUseWideViewPort(true);
        binding.webviewDwspTarget.getSettings().setBuiltInZoomControls(true);
        binding.webviewDwspTarget.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        binding.webviewDwspTarget.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        binding.webviewDwspTarget.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
            @Override
            public void onPageFinished(WebView view, final String url) {
                if (pd != null || pd.isShowing()) {
                    pd.dismiss();
                }
            }
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                SnackBarManagement._error_CustomMessage(binding.getRoot(), "Something went wrong");
            }
        });
        binding.webviewDwspTarget.loadUrl(urlBuilder);
    }
}