package com.manuelblanco.capstonestage2.utils;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.manuelblanco.capstonestage2.R;

import java.io.UnsupportedEncodingException;

/**
 * Created by manuel on 6/08/16.
 */
public class URLUtils {

    private static final String TAG = URLUtils.class.getSimpleName();

    public static String getURLFlags(String country) throws UnsupportedEncodingException {

        Uri.Builder builder = new Uri.Builder();
        builder.scheme(Constants.SCHEME_URL)
                .authority(Constants.AUTHORITY_URL)
                .appendPath(Constants.PATH_FLAGS_ISO)
                .appendPath(Constants.PATH_FLAGS_ISO_RESOL)
                .appendPath(country + Constants.PNG);

        return builder.build().toString();
    }

    public static String getURLTypes(String type) throws UnsupportedEncodingException {

        Uri.Builder builder = new Uri.Builder();
        builder.scheme(Constants.SCHEME_URL)
                .authority(Constants.AUTHORITY_URL)
                .appendPath(Constants.PATH_TYPES)
                .appendPath(type  + Constants.PNG);

        return builder.build().toString();
    }

    public static String getURLImageTest(String trip) throws UnsupportedEncodingException {

        Uri.Builder builder = new Uri.Builder();
        builder.scheme(Constants.SCHEME_URL)
                .authority(Constants.AUTHORITY_URL)
                .appendPath(Constants.PATH_TEST)
                .appendPath(trip);

        return builder.build().toString();
    }

    public static String getURLImageBackendless(Context context,String photo) throws UnsupportedEncodingException {

        Uri.Builder builder = new Uri.Builder();
        builder.scheme(Constants.SCHEME_URL_HTTPS)
                .authority(Constants.AUTHORITY_BACKENDLESS)
                .appendPath(context.getString(R.string.app_id_backendless))
                .appendPath(context.getString(R.string.version_app))
                .appendPath(Constants.PATH_FILES)
                .appendPath(Constants.PATH_PHOTOS_FOLDER)
                .appendPath(photo);

        return builder.build().toString();
    }

    public static String getURLFacebook() throws UnsupportedEncodingException {

        Uri.Builder builder = new Uri.Builder();
        builder.scheme(Constants.SCHEME_URL_HTTPS)
                .authority(Constants.AUTHORITY_URL_FACEBOOK)
                .appendPath(Constants.PATH_FACEBOOK_ID);

        return builder.build().toString();
    }


}
