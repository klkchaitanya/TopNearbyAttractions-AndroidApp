package siri.project.topnearby;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GetNearbyPlacesData extends AsyncTask<Object, String, String> /*implements GoogleMap.OnMarkerClickListener*/ {

    String googlePlacesData;
    GoogleMap mMap;
    String url;
    Context c;
    String urlToList;
    static ArrayList<String> placesList=new ArrayList<String>();
    static ArrayList<String> placesIdList=new ArrayList<String>();

    public GetNearbyPlacesData(Context context)
    {
        c=context;
    }

    @Override
    protected String doInBackground(Object... params) {
        try {
            Log.d("GetNearbyPlacesData", "doInBackground entered");
            //mMap = (GoogleMap) params[0];
            url = (String) params[0];
            DownloadUrl downloadUrl = new DownloadUrl();
            googlePlacesData = downloadUrl.readUrl(url);
            Log.d("GooglePlacesReadTask", "doInBackground Exit");
        } catch (Exception e) {
            Log.d("GooglePlacesReadTask", e.toString());
        }
        return googlePlacesData;
    }

   /* @Override
    protected void onPostExecute(String result) {
        Log.d("GooglePlacesReadTask", "onPostExecute Entered");
        List<HashMap<String, String>> nearbyPlacesList = null;

        DataParser dataParser = new DataParser();
        nearbyPlacesList =  dataParser.parse(result);

        ShowNearbyPlaces(nearbyPlacesList);
        Log.d("GooglePlacesReadTask", "onPostExecute Exit");
    }*/

    private void ShowNearbyPlaces(List<HashMap<String, String>> nearbyPlacesList) {

        placesList.clear();
        placesIdList.clear();

        for (int i = 0; i < nearbyPlacesList.size(); i++) {
            Log.d("onPostExecute","Entered into showing locations");
            MarkerOptions markerOptions = new MarkerOptions();
            HashMap<String, String> googlePlace = nearbyPlacesList.get(i);
            double lat = Double.parseDouble(googlePlace.get("lat"));
            double lng = Double.parseDouble(googlePlace.get("lng"));
            String placeName = googlePlace.get("place_name");
            String place_id = googlePlace.get("place_id");
          //  String open_now = googlePlace.get("open_now");
            String vicinity = googlePlace.get("vicinity");
            String reference = googlePlace.get("reference");
           // String website = googlePlace.get("website");
            String icon = googlePlace.get("icon");

            String photoReference = googlePlace.get("photo_reference");

            Log.d("PhotoReference", photoReference);
           /* LatLng latLng = new LatLng(lat, lng);
            markerOptions.position(latLng);
            markerOptions.title(placeName + "\n" + vicinity + "\n" + place_id + "\n" + icon);

            mMap.addMarker(markerOptions);
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
            //move map camera
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(11));
            mMap.setOnMarkerClickListener(this);*/

            //placesList.add(place_id);
            placesList.add(placeName);
            placesIdList.add(place_id);

        }

        Log.d("Places List",placesList.toString());
        Log.d("Places Id List",placesIdList.toString());
    }

 /*   @Override
    public boolean onMarkerClick(Marker marker) {

        String[] split=marker.getTitle().split("\n");
        String url = getUrl(split[2]);
        Object[] DataTransfer = new Object[1];
        DataTransfer[0] = url;
        Log.d("onClick", url);
        GetPlaceDetails PlaceDetails = new GetPlaceDetails(c);
        PlaceDetails.execute(DataTransfer);
        return false;
    }*/

   /* private String getUrl(String placeId) {

        String Url="https://maps.googleapis.com/maps/api/place/details/json?";
        StringBuilder googlePlacesUrl = new StringBuilder(Url);
        googlePlacesUrl.append("placeid=" + placeId);
        googlePlacesUrl.append("&key=" + "AIzaSyAeiNTUItTqvSC1c_sJheL-LkyVoRNW-nk");
        Log.d("getUrl", googlePlacesUrl.toString());
        return (googlePlacesUrl.toString());
    }*/

   /* public void displayAsList()
    {
      Intent in=new Intent(c,SearchResults.class);
      in.putExtra("placesList",placesList);
      in.putExtra("placesIdList",placesIdList);
        //Toast.makeText(c, "disp"+String.valueOf(placesList.size()), Toast.LENGTH_SHORT).show();

      in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
      c.startActivity(in);

    }*/

}
