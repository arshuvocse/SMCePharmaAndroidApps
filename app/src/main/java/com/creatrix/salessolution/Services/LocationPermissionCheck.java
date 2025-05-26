package com.creatrix.salessolution.Services;

import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;

import androidx.appcompat.app.AlertDialog;

import static android.content.Context.LOCATION_SERVICE;

public class LocationPermissionCheck {

    public static void CheckPermission(Context context){
        LocationManager locationManagerdd = (LocationManager) context.getSystemService(LOCATION_SERVICE);
        if (locationManagerdd.isProviderEnabled(locationManagerdd.GPS_PROVIDER)){
        }else{
            showGPSDisabledAlertToUser(context);
        }
    }


    private static void showGPSDisabledAlertToUser(Context context){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setTitle("Location is mandatory !");
        alertDialogBuilder.setMessage("Location is disabled in your device. You need to give location permission to use the app")
                .setCancelable(false)
                .setPositiveButton("Enable Location",
                        (dialog, id) -> {
                            Intent callGPSSettingIntent = new Intent(
                                    android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            context.startActivity(callGPSSettingIntent);
                        });
//        alertDialogBuilder.setNegativeButton("Cancel",
//                new DialogInterface.OnClickListener(){
//                    public void onClick(DialogInterface dialog, int id){
////                        dialog.cancel();
////                        context.startActivity(new Intent(context, MainDashboardActivity.class));
//                    }
//                });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

}
