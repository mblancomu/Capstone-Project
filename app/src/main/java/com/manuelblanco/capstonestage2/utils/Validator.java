package com.manuelblanco.capstonestage2.utils;

import android.content.Context;
import android.util.Patterns;
import android.widget.EditText;
import android.widget.Toast;

import com.manuelblanco.capstonestage2.R;
import com.manuelblanco.capstonestage2.model.Trip;

import java.util.List;

/**
 * Provides static methods for different value validators.
 * Shows Toasts with warnings if validation fails.
 */
public class Validator {
    /**
     * Validates user's name: checks whether it is not empty and whether the first letter is in uppercase.
     * Shows Toast with a warning if validation fails.
     *
     * @param currentContext context, in which validation occurs
     * @param name           user's name to be validated
     * @return true if name is valid, false if validation failed
     */
    public static boolean isNameValid(Context currentContext, CharSequence name) {
        if (name.toString().isEmpty()) {
            Toast.makeText(currentContext, currentContext.getString(R.string.warning_name_empty), Toast.LENGTH_LONG).show();
            return false;
        }

   /* if( !Character.isUpperCase( name.charAt( 0 ) ) )
    {
      Toast.makeText( currentContext, currentContext.getString( R.string.warning_name_lowercase ), Toast.LENGTH_LONG ).show();
      return false;
    }*/

        return true;
    }

    /**
     * Validates email: checks whether it is not empty and whether it matches Patterns.EMAIL_ADDRESS regex.
     * Shows Toast with a warning if validation fails.
     *
     * @param currentContext context, in which validation occurs
     * @param email          email to be validated
     * @return true if email is valid, false if validation failed
     */
    public static boolean isEmailValid(Context currentContext, CharSequence email) {
        if (email.toString().isEmpty()) {
            Toast.makeText(currentContext, currentContext.getString(R.string.warning_email_empty), Toast.LENGTH_LONG).show();
            return false;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(currentContext, currentContext.getString(R.string.warning_email_invalid), Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

    /**
     * Validates password: checks whether it is not empty.
     * Shows Toast with a warning if validation fails.
     *
     * @param currentContext context, in which validation occurs
     * @param password       password to be validated
     * @return true if password is valid, false if validation failed
     */
    public static boolean isPasswordValid(Context currentContext, CharSequence password) {
        if (password.toString().isEmpty()) {
            Toast.makeText(currentContext, currentContext.getString(R.string.warning_password_empty), Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

    public static String isIdTripFromListValid(Context context, List<Trip> trip) {
        final String idTrip;

        if (trip == null || trip.size() == 0) {
            idTrip = context.getString(R.string.no_data);
        } else {
            idTrip = trip.get(0).getId_trip();
        }

        return idTrip;
    }

    public static String isIdTripValid(Context context, String trip) {
        final String idTrip;

        if (trip == null || trip.isEmpty() || trip.equals("")) {
            idTrip = context.getString(R.string.no_data);
        } else {
            idTrip = trip;
        }

        return idTrip;
    }

    public static String isCountryValid(Context context, String country) {
        final String idCountry;

        if (country == null || country.isEmpty() || country.equals("")) {
            idCountry = "*";
        } else {
            idCountry = country;
        }

        return idCountry;
    }

    public static boolean validateFieldsRegister(Context context,EditText user, EditText email, EditText password, EditText confirm) {
        if (user.getText().length() == 0) {
            user.setError(context.getString(R.string.empty_field_edit));

            return false;
        }
        if (email.getText().length() == 0) {
            email.setError(context.getString(R.string.empty_field_edit));
            return false;
        }
        if (password.getText().length() == 0) {
            password.setError(context.getString(R.string.empty_field_edit));
            return false;
        }
        if (confirm.getText().length() == 0) {
            confirm.setError(context.getString(R.string.empty_field_edit));
            return false;
        }

        return true;
    }

    public static boolean validateFieldsLogin(Context context, EditText user, EditText password) {
        if (user.getText().length() == 0) {
            user.setError(context.getString(R.string.empty_field_edit));
            return false;
        }
        if (password.getText().length() == 0) {
            password.setError(context.getString(R.string.empty_field_edit));
            return false;
        }

        return true;
    }

}
