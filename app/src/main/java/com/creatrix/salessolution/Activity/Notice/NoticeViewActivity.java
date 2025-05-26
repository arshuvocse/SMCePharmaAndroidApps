package com.creatrix.salessolution.Activity.Notice;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.creatrix.salessolution.Activity.Attendance.Model.AttenApproval;
import com.creatrix.salessolution.Interface.INotice;
import com.creatrix.salessolution.Model.Notice;
import com.creatrix.salessolution.Presenter.NoticePresenter;
import com.creatrix.salessolution.R;
import com.creatrix.salessolution.UtilityHelper.SessionManagement;
import com.creatrix.salessolution.UtilityHelper.SnackBarManagement;
import com.creatrix.salessolution.databinding.ActivityNoticeViewBinding;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class NoticeViewActivity extends AppCompatActivity implements INotice.View{
    ActivityNoticeViewBinding binding;
    NoticePresenter presenterNotice;
    SessionManagement session;
    int empId,noticeId;


    private ProgressDialog progressBar;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNoticeViewBinding.inflate(LayoutInflater.from(this));
        //setContentView(R.layout.activity_notice_view);
        setContentView(binding.getRoot());
        binding.toolbarCustom.setOnClickListener(v -> finish());


        session = new SessionManagement(NoticeViewActivity.this);
        HashMap<String, String> user = session.getUserDetails();
        empId = Integer.parseInt(user.get(SessionManagement.KEY_EmpId));

     /*   presenterNotice = new NoticePresenter(this, this);
        presenterNotice.getNotices(empId);*/
        Intent getNoticeData = getIntent();
        /*Gson gson = new Gson();
        notice = gson.fromJson(getIntent().getStringExtra("myjson"), Notice.class);
        System.out.println("data: "+notice);*/

        Intent in=getIntent();
        int noticeId=Integer.parseInt(in.getStringExtra("noId"));
        viewLoad(noticeId);
       // Toast.makeText(this, "id : "+data, Toast.LENGTH_SHORT).show();
/*
        if(getNoticeData.getStringExtra("From").equals("Adapter"))
        {
            String today=new SimpleDateFormat("dd-MM-yyyy : HH:mm:ss", Locale.getDefault()).format(new Date());
           // presenterNotice.postSeenNotice(notice.getNoticeId(),empId,today);
           // noticeId=Integer.parseInt(getNoticeData.getStringExtra("noticeId"));
            String noticeid=getNoticeData.getStringExtra("noticeId");
           //

        }*/
      /*  binding.noticeTitle.setText(notice.getNoticeTitle());
        binding.noticreatedby.setText(notice.getCreatedBy());
        binding.noticeAnnouncement.setText(notice.getAnnouncement());
        binding.noticeFromdate.setText(notice.getFromDate());*/
        //TODO:Image show from server
        /*if(notice.getImageString()!=null)
        {
            try {
                binding.noticeImage.setVisibility(View.VISIBLE);
                byte[] decodedString = Base64.decode(notice.getImageString(), Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                binding.noticeImage.setImageBitmap(decodedByte);
                binding.noimg.setVisibility(View.GONE);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }else {
            binding.noticeImage.setVisibility(View.GONE);
            binding.noimg.setVisibility(View.VISIBLE);
        }*/

        /*binding.noticeImage.setOnClickListener(v -> {

            Toast.makeText(this, "Show Image Largely", Toast.LENGTH_SHORT).show();
            Intent in=new Intent(NoticeViewActivity.this,ImageFullScreenActivity.class);
            in.putExtra("ImgFull",notice.getImageString());

            startActivity(in);

        });*/

    }

    private void viewLoad(int noticeId) {
        String urlBuilder = "http://13.76.141.111:456//NoticeBoard_UI/NoticeDetailsApp.aspx?MID="+noticeId;
        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        progressBar = ProgressDialog.show(NoticeViewActivity.this, "Loading", "Please wait...");



   /*     binding.webview.getSettings().setJavaScriptEnabled(true);
        binding.webview.getSettings().setBuiltInZoomControls(true);
        binding.webview.setWebChromeClient(new WebChromeClient());
        binding.webview.getSettings().setUseWideViewPort(true);
        binding.webview.getSettings().setLoadWithOverviewMode(true);
        binding.webview.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);*/

        binding.webview.getSettings().setJavaScriptEnabled(true);
        binding.webview.setWebChromeClient(new WebChromeClient());
        binding.webview.getSettings().setLoadWithOverviewMode(true);
        binding.webview.getSettings().setUseWideViewPort(true);
        binding.webview.getSettings().setBuiltInZoomControls(true);
        binding.webview.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        binding.webview.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        binding.webview.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
            @Override
            public void onPageFinished(WebView view, final String url) {
                if (progressBar.isShowing()) {
                    progressBar.dismiss();
                }
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                SnackBarManagement._error_CustomMessage(binding.masterLayout,"Something went wrong");
            }
        });

        binding.webview.loadUrl(urlBuilder);
       /* binding.attRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.webview.loadUrl(urlBuilder);
            }
        });*/
    }

    @Override
    public void onSuccess(List<Notice> arrayList) {

    }

    @Override
    public void onError(String message) {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
}