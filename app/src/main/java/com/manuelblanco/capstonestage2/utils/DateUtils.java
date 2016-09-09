package com.manuelblanco.capstonestage2.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by manuel on 24/08/16.
 */
public class DateUtils {

    public static String getOnlyDate(String created){

        String newDate = "";
        String oldFormat = "MM/dd/yyyy HH:mm:ss";
        String newFormat = "dd/MM/yyyy";

        SimpleDateFormat sdf1 = new SimpleDateFormat(oldFormat);
        SimpleDateFormat sdf2 = new SimpleDateFormat(newFormat);


        try {
            newDate = sdf2.format(sdf1.parse(created));

        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return newDate;
    }

    public static String getActualDate(){

        Calendar c = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        return dateFormat.format(c.getTime());
    }
}
