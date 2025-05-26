package com.creatrix.salessolution.HelperActivity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.creatrix.salessolution.Activity.MainActivity;
import com.creatrix.salessolution.Activity.OrderStartActivity;
import com.creatrix.salessolution.R;

public class SplashScreenActivity extends AppCompatActivity {
    ProgressBar splashProgress;
    int SPLASH_TIME = 1000; //This is 3 seconds
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        //This is additional feature, used to run a progress bar
//        splashProgress = findViewById(R.id.splashProgress);
//        splashProgress.setProgressTintList(ColorStateList.valueOf(Color.WHITE));
//        playProgress();

        //Code to start timer and take action after the timer ends
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do any action here. Now we are moving to next page
                try {
                    Intent mySuperIntent = new Intent(SplashScreenActivity.this, MainActivity.class);
                    startActivity(mySuperIntent);
                   /* TextView textView2   = (TextView)findViewById(R.id.textView2);
                    Pair[] pair = new Pair[1];
                    pair[0] = new Pair<View,String>(textView2,"titletrans");
                    ActivityOptions activityOptions =  ActivityOptions.makeSceneTransitionAnimation(SplashScreenActivity.this,pair);
                    startActivity(mySuperIntent,activityOptions.toBundle());*/

                    finish();
                } catch (Exception exception) {
                    exception.printStackTrace();
                }

            }
        }, SPLASH_TIME);
    }

    //Method to run progress bar for 5 seconds
    private void playProgress() {
        ObjectAnimator.ofInt(splashProgress, "progress", 100)
                .setDuration(5000)
                .start();
    }
}