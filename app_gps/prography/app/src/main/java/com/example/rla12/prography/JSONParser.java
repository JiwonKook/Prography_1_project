package com.example.rla12.prography;

/**
 * Created by rla12 on 2018-01-09.
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JSONParser {
    /** Receives a JSONObject and returns a list */
    public static List<HashMap<String,String>> parse(JSONObject jObject){

        JSONArray jStore = null;
        try {
            /** Retrieves all the elements in the 'gps' array */
            jStore = jObject.getJSONArray("sendData");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        /** Invoking getGPS with the array of json object
         * where each json object represent a GPS info
         */
        return getStores(jStore);
    }

    public static List<HashMap<String, String>> getStores(JSONArray jStore){
        int jStoreCount = jStore.length();
        List<HashMap<String, String>> jStoreList = new ArrayList<HashMap<String,String>>();
        HashMap<String, String> gps = null;

        /** Taking each GPS Info, parses and adds to list object */
        for(int i=0; i<jStoreCount;i++){
            try {
                /** Call getGps with gps JSON object to parse the gps */
                gps = getStore((JSONObject)jStore.get(i));
                jStoreList.add(gps);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return jStoreList;
    }

    /** Parsing the GPS JSON object */
    public static HashMap<String, String> getStore(JSONObject jStore){

        HashMap<String, String> storeInfo = new HashMap<String, String>();
        String tvname = "";
        String laitude = "";
        String longitude="";


        try {
            tvname = jStore.getString("tvname");
            laitude = jStore.getString("laitude");
            longitude = jStore.getString("longitude");

            storeInfo.put("tvname", tvname);
            storeInfo.put("laitude", laitude);
            storeInfo.put("longitude", longitude);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return storeInfo;
    }

}