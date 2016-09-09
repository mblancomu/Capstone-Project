package com.manuelblanco.capstonestage2.utils;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;

import com.manuelblanco.capstonestage2.R;

import java.util.ArrayList;

/**
 * Created by manuel on 17/07/16.
 */
public class Utils {

    public static int getFlagIso(Context context, String code){

        return context.getResources().getIdentifier(code, "drawable", context.getPackageName());
    }

    public static int getIconType(Context context, String type){

        return context.getResources().getIdentifier("ic_" + type, "drawable", context.getPackageName());
    }

    public static int getMarkerType(Context context, String type){

        return context.getResources().getIdentifier("marker_" + type.toLowerCase(), "drawable", context.getPackageName());
    }

    public static String typesForSqlite(ArrayList<String> types){
        String typesToString = "";
        int size = types.size();

        for (int i = 0; i < size;i++){

            if (i == 0 && size == 1){
                typesToString = "" + types.get(i).toString() + "";
            }else if(i == 0 && size > 1){
                typesToString = "" + types.get(i).toString() + "',";
            }else if (i > 0 && i < size-1){
                typesToString = typesToString + "'" + types.get(i).toString() + "',";
            }else if (i == size-1){
                typesToString = typesToString + "'" + types.get(i).toString() + "";
            }
        }

        return typesToString;
    }

    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static boolean checkPermission(final Context context)
    {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if(currentAPIVersion>=android.os.Build.VERSION_CODES.M)
        {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle("Permission necessary");
                    alertBuilder.setMessage("External storage permission is necessary");
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();

                } else {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static boolean isTablet(Context context){
        return context.getResources().getBoolean(R.bool.isTablet);
    }
}
