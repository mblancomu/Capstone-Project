package com.manuelblanco.capstonestage2.utils;

import com.backendless.utils.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by manuel on 21/08/16.
 */
public class StringsUtils {

    public static String convertLatLngForDB(String latlng) {
        String coords = latlng;

        Matcher m = Pattern.compile("\\(([^)]+)\\)").matcher(latlng);
        while(m.find()) {
            coords = m.group(1);
        }

        return coords;
    }

    public static List<String> convertStringToArrayList(String commanSeparated) {
        return Arrays.asList(commanSeparated.split("\\s*,\\s*"));
    }

    public static String convertHashMapToString(HashMap<String, String> hash) {
        String coords = "";
        Set<String> set = hash.keySet();
        ArrayList<String> list = new ArrayList<String>();
        list.addAll(set);
        Collections.sort(list);
        int size = list.size();

        for (int i = 0; i < size; i++) {
            if (i == 0) {
                coords = list.get(i).toString() + "," + hash.get(list.get(i));
            } else if(size > 1){
                coords = coords + "~" + list.get(i).toString() + "," + hash.get(list.get(i));
            }
        }

        return coords;
    }

    private static String removeCharAt(String s, int i) {
        StringBuffer buf = new StringBuffer(s.length() - 1);
        buf.append(s.substring(0, i)).append(s.substring(i + 1));
        return buf.toString();
    }

    private static String removeChar(String s, char c) {
        StringBuffer buf = new StringBuffer(s.length());
        buf.setLength(s.length());
        int current = 0;
        for (int i = 0; i < s.length(); i++) {
            char cur = s.charAt(i);
            if (cur != c) buf.setCharAt(current++, cur);
        }
        return buf.toString();
    }

    private static String replaceCharAt(String s, int i, char c) {
        StringBuffer buf = new StringBuffer(s);
        buf.setCharAt(i, c);
        return buf.toString();
    }

    private static String deleteAllNonDigit(String s) {
        String temp = s.replaceAll("\\D", "");
        return temp;
    }

    public static String replaceAllChar(String s, String f, String r) {
        String temp = s.replace(f, r);
        return temp;
    }

    public static boolean specialCharsInString(String text){

        Pattern p = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(text);
        return m.find();
    }

    public static String convertArrayListToString(ArrayList<String> list){
        String join = "";
        int size = list.size();

        for (int i=0;i < size;i++){
            if (i == 0){
                join = list.get(i).toString();
            }else {
                join = join + "," + list.get(i).toString();
            }
        }
    return  join;
    }

    public static ArrayList<String> convertToArrayList(String[] list, String hint){

        List<String> typesList = Arrays.asList(list);
        ArrayList<String> convertTypes = new ArrayList<String>();
        convertTypes.addAll(typesList);
        convertTypes.add(hint);

        return convertTypes;
    }
}
