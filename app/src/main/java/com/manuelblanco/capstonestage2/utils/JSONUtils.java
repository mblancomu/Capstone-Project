package com.manuelblanco.capstonestage2.utils;

import com.google.gson.Gson;
import com.manuelblanco.capstonestage2.model.Coordinates;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by manuel on 15/08/16.
 */
public class JSONUtils {

    public static String convertCoordsToJson(Coordinates coordinates) {

        Gson gson = new Gson();
        return gson.toJson(coordinates);
    }

    public static Coordinates convertJsonToCoords(String coordinates) {

        Gson gson = new Gson();
        Coordinates coords = gson.fromJson(coordinates, Coordinates.class);

        return coords;
    }
}
