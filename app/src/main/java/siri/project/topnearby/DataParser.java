package siri.project.topnearby;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class DataParser {
    public List<HashMap<String, String>> parse(String jsonData) {
        JSONArray jsonArray = null;
        JSONObject jsonObject;

        try {
            Log.d("Places", "parse");
            jsonObject = new JSONObject((String) jsonData);
            jsonArray = jsonObject.getJSONArray("results");
        } catch (JSONException e) {
            Log.d("Places", "parse error");
            e.printStackTrace();
        }
        return getPlaces(jsonArray);
    }

    private List<HashMap<String, String>> getPlaces(JSONArray jsonArray) {
        int placesCount = jsonArray.length();
        List<HashMap<String, String>> placesList = new ArrayList<>();
        HashMap<String, String> placeMap = null;
        Log.d("Places", "getPlaces");

        for (int i = 0; i < placesCount; i++) {
            try {
                placeMap = getPlace((JSONObject) jsonArray.get(i));
                if(placeMap.get("rating")!=null && !placeMap.get("rating").equalsIgnoreCase("N/A"))
                    placesList.add(placeMap);
                Log.d("Places", "Adding places");

            } catch (JSONException e) {
                Log.d("Places", "Error in Adding places");
                e.printStackTrace();
            }
        }
        return placesList;
    }

    private HashMap<String, String> getPlace(JSONObject googlePlaceJson) {
        HashMap<String, String> googlePlaceMap = new HashMap<String, String>();
        String placeName = "N/A";
        String placeRating = "N/A";
        String vicinity = "N/A";
        String latitude = "";
        String longitude = "";
        String reference = "";
        String placeId = "";
        String icon = "";
        String website = "N/A";
        boolean openNow = false;
        String photoReference = "";
        String photoWidth = "";

        Log.d("getPlace", "Entered");

        try {

            placeName = googlePlaceJson.optString("name","N/A");
            vicinity = googlePlaceJson.optString("vicinity","N/A");
            placeRating = googlePlaceJson.optString("rating", "N/A");
            latitude = googlePlaceJson.getJSONObject("geometry").getJSONObject("location").getString("lat");
            longitude = googlePlaceJson.getJSONObject("geometry").getJSONObject("location").getString("lng");
            reference = googlePlaceJson.getString("reference");
            placeId = googlePlaceJson.getString("place_id");
            icon = googlePlaceJson.getString("icon");
            photoReference = googlePlaceJson.getJSONArray("photos").getJSONObject(0).getString("photo_reference");
            photoWidth = googlePlaceJson.getJSONArray("photos").getJSONObject(0).getString("width");

          //  website = googlePlaceJson.getString("website");
         //   openNow = googlePlaceJson.getJSONObject("opening_hours").getBoolean("open_now");
            googlePlaceMap.put("place_name", placeName);
            googlePlaceMap.put("rating", placeRating);
            googlePlaceMap.put("vicinity", vicinity);
            googlePlaceMap.put("lat", latitude);
            googlePlaceMap.put("lng", longitude);
            googlePlaceMap.put("reference", reference);
            googlePlaceMap.put("place_id", placeId);
            googlePlaceMap.put("icon", icon);
            googlePlaceMap.put("photo_reference",photoReference);
            googlePlaceMap.put("photo_width",photoWidth);

          //  googlePlaceMap.put("website",website);
           // googlePlaceMap.put("open_now", openNow);
            Log.d("getPlace", "Putting Places");
            Log.d("getPlace","pref :"+photoReference);
        } catch (JSONException e) {
            Log.d("getPlace", "Error");
            e.printStackTrace();
        }
        return googlePlaceMap;
    }
}
