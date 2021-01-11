package siri.project.topnearby;

import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ParsePlaceDetails {

    public Place_All_Details parse(String jsonData)
    {

        try {
            JSONObject jsonResult = new JSONObject(jsonData).getJSONObject("result");

            String stFormattedAddress = jsonResult.optString("formatted_address", "N/A");
            String stInternationalPhoneNumber = jsonResult.optString("international_phone_number", "N/A");
            String stName = jsonResult.optString("name", "N/A");
            String stRating = jsonResult.optString("rating", "N/A");
            String stPhotoReference = jsonResult.getJSONArray("photos").getJSONObject(0).getString("photo_reference");
            String stPhotoWidth = jsonResult.getJSONArray("photos").getJSONObject(0).getString("width");
            String stPhotoHeight = jsonResult.getJSONArray("photos").getJSONObject(0).getString("height");
            String stWebsite = jsonResult.optString("website", "N/A");

            JSONObject jsonOpeningHours = jsonResult.optJSONObject("opening_hours");

            String photoBaseUrl = "https://maps.googleapis.com/maps/api/place/photo?";
            String stMaxHeight = "maxHeight=";
            String stMaxWidth = "&maxwidth=";
            String stPhotoRef = "&photoreference=";
            String key = "&key=AIzaSyBvBsML26knESouhVXITpSoq3vijhFCxN0";

            String buildPhoto = photoBaseUrl + stMaxHeight + stPhotoHeight + stMaxWidth + stPhotoWidth+ stPhotoRef + stPhotoReference + key;


            String parsedDetails = stName + " " + stFormattedAddress + " " + stInternationalPhoneNumber + " " + stRating + " " + stWebsite;
            Place_All_Details place_all_details = new Place_All_Details(stName, stRating, stInternationalPhoneNumber, stFormattedAddress, stWebsite, jsonOpeningHours, buildPhoto);
            return place_all_details;

        } catch (JSONException e) {
            e.printStackTrace();
        }
        /*JSONArray jsonArray = null;
        JSONObject jsonObject, jsonObject2;
        String stPlaceDetail;

        try {
            Log.d("Places", "parse");
            jsonObject = new JSONObject((String) jsonData);
            jsonObject2 = new JSONObject(jsonObject.getString("result"));
            // jsonArray = jsonObject.getJSONArray("result");
            // JSONObject googlePlace=(JSONObject)jsonArray.get(0);
            // String rating = googlePlace.toString();
            //Toast.makeText(c,jsonObject2.toString(),Toast.LENGTH_SHORT).show();
            JSONObject openingHours = new JSONObject(jsonObject2.getString("opening_hours"));
            String is_open_now;
            if(openingHours.getString("open_now").equals("true"))
                is_open_now="Open Now";
            else
                is_open_now="Closed Now";
            JSONArray periods = openingHours.getJSONArray("periods");
            String res="";
            for(int i=0;i<periods.length();i++)
            {
                // Toast.makeText(c,String.valueOf(periods.length()),Toast.LENGTH_SHORT).show();
                JSONObject element = (JSONObject) periods.get(i);
                JSONObject close = new JSONObject(element.getString("close"));
                JSONObject open = new JSONObject(element.getString("open"));
                String day="";
                if(open.getString("day").equals("0")){day="Sun";}
                else if(open.getString("day").equals("1")){day="Mon";}
                else if(open.getString("day").equals("2")){day="Tue";}
                else if(open.getString("day").equals("3")){day="Wed";}
                else if(open.getString("day").equals("4")){day="Thu";}
                else if(open.getString("day").equals("5")){day="Fri";}
                else if(open.getString("day").equals("6")){day="Sat";}


                res=res+day+" : "+open.getString("time")+"-"+close.getString("time")+"\t";
            }

            JSONArray photo= new JSONArray(jsonObject2.getString("photos"));
            JSONObject pic1= (JSONObject)(photo.get(0));

            *//*Toast.makeText(c,"Period: "+res
                    ,Toast.LENGTH_SHORT).show();
            Toast.makeText(c,"Photos: "+pic1.getString("photo_reference"),Toast.LENGTH_SHORT).show();
*//*
             stPlaceDetail = jsonObject2.getString("name")
                    +"\n"+jsonObject2.getString("rating")
                    +"\n"+jsonObject2.getString("international_phone_number")
                    +"\n"+is_open_now
                    +"\n"+res
                    +"\n"+pic1.getString("photo_reference")
                    +"\n"+jsonObject2.getString("formatted_address")
                    +"\n"+jsonObject2.getString("website");

           *//* Intent in = new Intent(c,PlaceDetails.class);
            in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            in.putExtra("data",s);
            c.startActivity(in);
*//*

           //Log.d("Parsed place details: ",s);


        } catch (Exception e) {
            Log.d("Places", "parse error");
            e.printStackTrace();
        }

        return stPlaceDetail;
*/
        return null;
    }

}
