package com.manuelblanco.capstonestage2.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;

public class PermissionsHelper {

    public static final int REQUEST = 1000;

    @TargetApi(Build.VERSION_CODES.M)
    public static boolean requestPermission(Activity activity, String permission) {

        boolean alreadyHas;
        //String locationPermission = Manifest.permission.ACCESS_FINE_LOCATION;
        int hasPermission = activity.checkSelfPermission(permission);
        String[] permissions = new String[]{permission};
        if (hasPermission != PackageManager.PERMISSION_GRANTED) {
            alreadyHas = false;
            activity.requestPermissions(permissions, REQUEST);
        } else {
            alreadyHas = true;
        }
        return alreadyHas;
    }
}