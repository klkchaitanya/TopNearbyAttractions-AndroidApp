package siri.project.topnearby;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

public class GetPlaceDetails extends AsyncTask<Object, String, String>
{

    String googlePlacesData;
    String url;
    Context c;


    public GetPlaceDetails(Context context)
    {
        c=context;
    }


    @Override
    protected String doInBackground(Object... params) {
        try {
            Log.d("GetPlaceDetails", "doInBackground entered");
            url = (String) params[0];
            DownloadUrl downloadUrl = new DownloadUrl();
            googlePlacesData = downloadUrl.readUrl(url);
            Log.d("GooglePlacesReadTask", "doInBackground Exit");
        } catch (Exception e) {
            Log.d("GooglePlacesReadTask", e.toString());
        }
        return googlePlacesData;
    }

}
