package com.manuelblanco.capstonestage2.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fenjuly.mylibrary.SpinnerLoader;
import com.manuelblanco.capstonestage2.R;

/**
 * Created by manuel on 24/08/16.
 */
public class UtilsView {

    public static int setIconType(String type, Context context){

        int icon = 0;

        switch (type){
            case "Cultural":
                icon = R.drawable.ic_cultural;
                break;
            case "Wine":
                icon = R.drawable.ic_wine;
                break;
            case "Nature":
                icon = R.drawable.ic_nature;
                break;
            case "Religion":
                icon = R.drawable.ic_religion;
                break;
            case "Beach":
                icon = R.drawable.ic_beach;
                break;
            case "Mountain":
                icon = R.drawable.ic_mountain;
                break;
            case "Health":
                icon = R.drawable.ic_health;
                break;
            case "Rural":
                icon = R.drawable.ic_rural;
                break;
            case "Gastronomic":
                icon = R.drawable.ic_gastronomic;
                break;
            case "Routes":
                icon = R.drawable.ic_route;
                break;
            case "Diversion":
                icon = R.drawable.ic_diversion;
                break;
            default:
                icon = R.drawable.ic_cultural;
                break;
        }

        return icon;
    }

    public static void showSnackBar(CoordinatorLayout coordinator, String message, int textColor){
        Snackbar snackbar = Snackbar
                .make(coordinator, message, Snackbar.LENGTH_LONG);

        View sbView = snackbar.getView();
        sbView.bringToFront();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(textColor);

        snackbar.show();

    }

    public static void showProgress(SpinnerLoader progress, View back){

        progress.setVisibility(View.VISIBLE);
        back.setVisibility(View.VISIBLE);

    }

    public static void hideProgress(SpinnerLoader progress, View back){

        progress.setVisibility(View.GONE);
        back.setVisibility(View.GONE);

    }
}
