package siri.project.topnearby;


import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.CursorLoader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import siri.project.topnearby.data_actual.FavoritesContract;
import siri.project.topnearby.widget.FavoriteCityWidgetService;


/**
 * A simple {@link Fragment} subclass.
 */
public class PlaceDetail extends Fragment implements android.support.v4.app.LoaderManager.LoaderCallbacks<Cursor>{


    static Place_All_Details place_all_details;
    static String place_id, place_city;
    int CURSOR_LOADER_ID = 2;


    //@BindView(R.id.tvPlaceDetail) TextView tvPlaceDetail;
    @BindView(R.id.tvPlaceDetail_ratingValue) TextView tvPlaceDetail_ratingValue;
    @BindView(R.id.tvPlaceDetail_phoneValue) TextView tvPlaceDetail_phoneValue;
    @BindView(R.id.tvPlaceDetail_addressValue) TextView tvPlaceDetail_addressValue;
    @BindView(R.id.tvPlaceDetail_websiteValue) TextView tvPlaceDetail_websiteValue;
    @BindView(R.id.tvPlaceDetail_placeIsLabel) TextView tvPlaceDetail_placeIsLabel;
    @BindView(R.id.tvPlaceDetail_placeIsValue) TextView tvPlaceDetail_placeIsValue;
    @BindView(R.id.tvPlaceDetail_weekdaysValue) TextView tvPlaceDetail_weekdaysValue;




    @BindView(R.id.tvPlaceName_OnMetaToolBar) TextView tvPlaceNameOnMetaToolBar;
    @BindView(R.id.imgPlaceImage) ImageView imgPlaceImage;
    @BindView(R.id.btnFav) Button btnFav;
    @BindView(R.id.btnFabShare) FloatingActionButton btnFabShare;


    /* Butterknife'd
    TextView tvPlaceDetail, tvPlaceNameOnMetaToolBar;
    ImageView imgPlaceImage;
    Button btnFav;
    FloatingActionButton btnFabShare;*/

    public PlaceDetail() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_place_detail, container, false);
        ButterKnife.bind(this, view);

        //Butterknife'd tvPlaceDetail = (TextView)view.findViewById(R.id.tvPlaceDetail);
        //Butterknife'd imgPlaceImage = (ImageView)view.findViewById(R.id.imgPlaceImage);
        //Butterknife'd tvPlaceNameOnMetaToolBar = (TextView)view.findViewById(R.id.tvPlaceName_OnMetaToolBar);
        //Butterknife'd btnFav = (Button)view.findViewById(R.id.btnFav);
        //Butterknife'd btnFabShare = (FloatingActionButton)view.findViewById(R.id.btnFabShare);


        btnFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getContext(),"Make it Fav", Toast.LENGTH_SHORT).show();
                if(isAlreadyFavorite(getArguments().getString("placeId")))
                    makeThePlaceUnFavorite();
                else
                    makeThePlaceFavorite();
            }
        });


        btnFabShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String placeDetailMerge = "RATING: " + tvPlaceDetail_ratingValue.getText() + "\n" + "\n" +

                        "PHONE: " + tvPlaceDetail_phoneValue.getText() + "\n" +
                        "ADDRESS: " + tvPlaceDetail_addressValue.getText() + "\n" + "\n" +

                        "WEBSITE: " + tvPlaceDetail_websiteValue.getText() + "\n" + "\n" +
                        "OPEN ON: " + "\n" + tvPlaceDetail_weekdaysValue.getText();

                sharePlaceDetails(tvPlaceNameOnMetaToolBar.getText() + "\n" + placeDetailMerge);
            }
        });


        if(getArguments()!=null) {
            if (getArguments().getString("callingFrom").equals("Search")) {
                fromSearchPage();
            } else if (getArguments().getString("callingFrom").equals("Favorites")) {
                fromFavoritesPage();
            }
        }


        if(getArguments()!=null) {

            if (isAlreadyFavorite(getArguments().getString("placeId")))
                btnFav.setBackground(getResources().getDrawable(R.drawable.favorite_filled));
            else
                btnFav.setBackground(getResources().getDrawable(R.drawable.favorite_outline));

        }


        return view;
    }



    private void fromSearchPage() {

        String placeId = getArguments().getString("placeId");
        place_id = placeId;
        place_city = getArguments().getString("placeCity");

        final String placeDetailsUrl = getPlaceDetailSearchUrl(placeId);
        Object[] DataTransfer = new Object[1];
        DataTransfer[0] = placeDetailsUrl;
        new GetPlaceDetails(getContext()){
            @Override
            protected void onPostExecute(String result) {
                //super.onPostExecute(result);
                ParsePlaceDetails parsePlaceDetails = new ParsePlaceDetails();
                place_all_details = parsePlaceDetails.parse(result);

                if(place_all_details!=null) {
                    Log.d("Parsed details: ", place_all_details.toString());
                    tvPlaceNameOnMetaToolBar.setText(place_all_details.place_name);


                    tvPlaceDetail_ratingValue.setText(place_all_details.place_rating);
                    tvPlaceDetail_phoneValue.setText(place_all_details.place_phone_number);
                    tvPlaceDetail_addressValue.setText(place_all_details.place_address);
                    tvPlaceDetail_websiteValue.setText(place_all_details.place_website);
                    tvPlaceDetail_placeIsValue.setText(formatOpeningHours_Now(place_all_details.place_opening_hours));
                    tvPlaceDetail_weekdaysValue.setText(formatOpeningHours_Weekdays(place_all_details.place_opening_hours));

                    /* tvPlaceDetail.setText(
                                    *//*place_all_details.place_name + "\n" +
                                    place_all_details.place_address + "\n" +
                                    place_all_details.place_phone_number + "\n" +
                                    place_all_details.place_rating + "\n" +
                                    place_all_details.place_website
                                    + "\n" +
                                    place_all_details.place_name + "\n" +
                                    place_all_details.place_address + "\n" +
                                    place_all_details.place_phone_number + "\n" +
                                    place_all_details.place_rating + "\n" +
                                    place_all_details.place_website
                                    + "\n" +*//*

                             //new
                             "RATING: " + place_all_details.place_rating + "\n" + "\n" +

                             "PHONE: " + place_all_details.place_phone_number + "\n" +
                             "ADDRESS: " + place_all_details.place_address + "\n" + "\n" +

                             "WEBSITE: " + place_all_details.place_website + "\n" + "\n" +

                             "PLACE IS: " + formatOpeningHours_Now(place_all_details.place_opening_hours)+ "\n" + "\n" +
                                    //formatOpeningHours_Today(place_all_details.place_opening_hours) + "\n" +
                             "WEEKDAYS: " + "\n" + formatOpeningHours_Weekdays(place_all_details.place_opening_hours)
                    );*/

                    Log.d("Place Image", place_all_details.place_build_photo.toString());
                    Picasso.with(getContext()).load(place_all_details.place_build_photo).placeholder(R.drawable.brokenimage).into(imgPlaceImage);


                    if(!BuildConfig.isPaid) {
                        //Show interstitial Ad
                        final InterstitialAd interstitialAd = new InterstitialAd(getContext());
                        interstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
                        interstitialAd.loadAd(new AdRequest.Builder().build());

                        interstitialAd.setAdListener(new AdListener() {
                            @Override
                            public void onAdLoaded() {
                                interstitialAd.show();
                                super.onAdLoaded();
                            }
                        });
                    }

                }
            }
        }.execute(DataTransfer);

    }


    private String getPlaceDetailSearchUrl(String placeId) {

        String Url="https://maps.googleapis.com/maps/api/place/details/json?";
        StringBuilder googlePlacesUrl = new StringBuilder(Url);
        googlePlacesUrl.append("placeid=" + placeId);
        googlePlacesUrl.append("&key=" + "AIzaSyBvBsML26knESouhVXITpSoq3vijhFCxN0");
        Log.d("getUrl", googlePlacesUrl.toString());
        return (googlePlacesUrl.toString());
    }


    private void fromFavoritesPage() {
        String placeId = getArguments().getString("placeId");
        place_id = placeId;
        String[] selectionArgs = {placeId};

        Bundle bundle = new Bundle();
        bundle.putString("placeId",place_id);
        getLoaderManager().initLoader(2, bundle, PlaceDetail.this);

       /* Cursor cr = getContext().getContentResolver().query(FavoritesContract.FavoritesEntry.CONTENT_URI,
                    null, "placeId = ?" ,selectionArgs, null);
        if(cr.moveToFirst())
        {
            cr.moveToFirst();

            tvPlaceNameOnMetaToolBar.setText(cr.getString(cr.getColumnIndex(FavoritesContract.FavoritesEntry.COLUMN_PLACENAME)));
            tvPlaceDetail.setText(
                   *//* cr.getString(cr.getColumnIndex(FavoritesContract.FavoritesEntry.COLUMN_PLACENAME)) + "\n" +
                            cr.getString(cr.getColumnIndex(FavoritesContract.FavoritesEntry.COLUMN_PLACEADDRESS)) + "\n" +
                            cr.getString(cr.getColumnIndex(FavoritesContract.FavoritesEntry.COLUMN_PLACEPHONE)) + "\n" +
                            cr.getString(cr.getColumnIndex(FavoritesContract.FavoritesEntry.COLUMN_PLACERATING)) + "\n" +
                            cr.getString(cr.getColumnIndex(FavoritesContract.FavoritesEntry.COLUMN_PLACEWEBSITE))
                            + "\n" +
                            cr.getString(cr.getColumnIndex(FavoritesContract.FavoritesEntry.COLUMN_PLACENAME)) + "\n" +
                    cr.getString(cr.getColumnIndex(FavoritesContract.FavoritesEntry.COLUMN_PLACEADDRESS)) + "\n" +
                    cr.getString(cr.getColumnIndex(FavoritesContract.FavoritesEntry.COLUMN_PLACEPHONE)) + "\n" +
                    cr.getString(cr.getColumnIndex(FavoritesContract.FavoritesEntry.COLUMN_PLACERATING)) + "\n" +
                    cr.getString(cr.getColumnIndex(FavoritesContract.FavoritesEntry.COLUMN_PLACEWEBSITE))
                            + "\n" +*//*
                         //   cr.getString(cr.getColumnIndex(FavoritesContract.FavoritesEntry.COLUMN_PLACENAME)) + "\n" +

                    "RATING: " + cr.getString(cr.getColumnIndex(FavoritesContract.FavoritesEntry.COLUMN_PLACERATING)) + "\n" + "\n" +

              "PHONE: " + cr.getString(cr.getColumnIndex(FavoritesContract.FavoritesEntry.COLUMN_PLACEPHONE)) + "\n" +
              "ADDRESS: " + cr.getString(cr.getColumnIndex(FavoritesContract.FavoritesEntry.COLUMN_PLACEADDRESS)) + "\n" + "\n" +

              "WEBSITE: " + cr.getString(cr.getColumnIndex(FavoritesContract.FavoritesEntry.COLUMN_PLACEWEBSITE)) + "\n" + "\n" +
                    "WEEKDAYS: " + "\n" + cr.getString(cr.getColumnIndex(FavoritesContract.FavoritesEntry.COLUMN_OPENINGHOURS_WEEKDAYS))

            );

            Picasso.with(getContext()).load(cr.getString(cr.getColumnIndex(FavoritesContract.FavoritesEntry.COLUMN_PLACEPHOTO_REFERENCE))).into(imgPlaceImage);

        }
        cr.close();
*/
    }


    private boolean isAlreadyFavorite(String placeId)
    {
        String[] selectionArgs = {placeId};
        Cursor cr = getContext().getContentResolver().query(FavoritesContract.FavoritesEntry.CONTENT_URI,
                null, "placeId = ?" , selectionArgs, null);
        if(cr.getCount()>0)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    private void makeThePlaceFavorite() {

        ContentValues contentValues = new ContentValues();
        contentValues.put(FavoritesContract.FavoritesEntry.COLUMN_PLACEID,place_id);
        contentValues.put(FavoritesContract.FavoritesEntry.COLUMN_PLACENAME,place_all_details.place_name);
        contentValues.put(FavoritesContract.FavoritesEntry.COLUMN_PLACERATING,place_all_details.place_rating);
        contentValues.put(FavoritesContract.FavoritesEntry.COLUMN_PLACEPHONE,place_all_details.place_phone_number);
        contentValues.put(FavoritesContract.FavoritesEntry.COLUMN_PLACEADDRESS,place_all_details.place_address);
        contentValues.put(FavoritesContract.FavoritesEntry.COLUMN_PLACEWEBSITE,place_all_details.place_website);
        contentValues.put(FavoritesContract.FavoritesEntry.COLUMN_PLACEPHOTO_REFERENCE,place_all_details.place_build_photo);
        contentValues.put(FavoritesContract.FavoritesEntry.COLUMN_OPENINGHOURS_WEEKDAYS,formatOpeningHours_Weekdays(place_all_details.place_opening_hours));
        contentValues.put(FavoritesContract.FavoritesEntry.COLUMN_PHOTO_MAXHEIGHT,"");
        contentValues.put(FavoritesContract.FavoritesEntry.COLUMN_PLACECITY,place_city);

        try {

            Uri uri = getContext().getContentResolver().insert(FavoritesContract.FavoritesEntry.CONTENT_URI, contentValues);
            if (uri != null) {
                Toast.makeText(getContext(), "Saved place to the favourites list!", Toast.LENGTH_SHORT).show();
                btnFav.setBackground(getResources().getDrawable(R.drawable.favorite_filled));
                FavoriteCityWidgetService.startActionUpdateWidget(getActivity().getBaseContext());
            }
        }
        catch (Exception e)
        {
            Toast.makeText(getContext(),e.toString(),Toast.LENGTH_LONG).show();
        }
    }


    private void makeThePlaceUnFavorite()
    {
        int deleteStatus = getContext().getContentResolver().delete(FavoritesContract.FavoritesEntry.CONTENT_URI, FavoritesContract.FavoritesEntry.COLUMN_PLACEID + " =? ",new String[]{place_id});
        if(deleteStatus>0)
        {
            Toast.makeText(getContext(),"Place removed from favourites", Toast.LENGTH_LONG).show();
            btnFav.setBackground(getResources().getDrawable(R.drawable.favorite_outline));
            FavoriteCityWidgetService.startActionUpdateWidget(getActivity().getBaseContext());
        }
    }


    private String formatOpeningHours_Now(JSONObject place_opening_hours) {
        try {
            if(place_opening_hours!=null)
                return place_opening_hours.getBoolean("open_now")==true?"Open Now":"CLOSED";
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "N/A";
    }


    private String formatOpeningHours_Today(JSONObject place_opening_hours) {
        return "";
    }

    private String formatOpeningHours_Weekdays(JSONObject jsonPlaceOpeningHours) {

        String opening_hours_weekdays = "";
        try {
            for (int i = 0; i < jsonPlaceOpeningHours.getJSONArray("weekday_text").length(); i++)
                opening_hours_weekdays = opening_hours_weekdays + jsonPlaceOpeningHours.getJSONArray("weekday_text").get(i).toString() + "\n";
        }
        catch (Exception ex)
        {
            Log.d("formatOpeningHours",ex.toString());
        }

        return opening_hours_weekdays.equals("")?"N/A":opening_hours_weekdays;
    }



    private void sharePlaceDetails(String placeDetails) {

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        //whatsappIntent.setPackage("com.whatsapp");
        shareIntent.putExtra(Intent.EXTRA_TEXT, placeDetails);
        try {
            startActivity(shareIntent);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getContext(), "Compatible application have not been installed.", Toast.LENGTH_LONG).show();
        }

    }


    @NonNull
    @Override
    public android.support.v4.content.Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        String[] selectionArgs = {args.getString("placeId")};
        return new CursorLoader(getActivity(),FavoritesContract.FavoritesEntry.CONTENT_URI,
                null, FavoritesContract.FavoritesEntry.COLUMN_PLACEID+" = ?" ,selectionArgs, null);
    }

    @Override
    public void onLoadFinished(@NonNull android.support.v4.content.Loader<Cursor> loader, Cursor data) {
            refreshFragmentWithFavoriteData(data);
    }


    @Override
    public void onLoaderReset(@NonNull android.support.v4.content.Loader<Cursor> loader) {

    }

    private void refreshFragmentWithFavoriteData(Cursor data) {
        Cursor cr = data;
        if(cr.moveToFirst())
        {
            cr.moveToFirst();

            tvPlaceNameOnMetaToolBar.setText(cr.getString(cr.getColumnIndex(FavoritesContract.FavoritesEntry.COLUMN_PLACENAME)));

            tvPlaceDetail_ratingValue.setText(cr.getString(cr.getColumnIndex(FavoritesContract.FavoritesEntry.COLUMN_PLACERATING)));
            tvPlaceDetail_phoneValue.setText(cr.getString(cr.getColumnIndex(FavoritesContract.FavoritesEntry.COLUMN_PLACEPHONE)));
            tvPlaceDetail_addressValue.setText(cr.getString(cr.getColumnIndex(FavoritesContract.FavoritesEntry.COLUMN_PLACEADDRESS)));
            tvPlaceDetail_websiteValue.setText(cr.getString(cr.getColumnIndex(FavoritesContract.FavoritesEntry.COLUMN_PLACEWEBSITE)));
            tvPlaceDetail_placeIsLabel.setVisibility(View.GONE);
            tvPlaceDetail_weekdaysValue.setText(cr.getString(cr.getColumnIndex(FavoritesContract.FavoritesEntry.COLUMN_OPENINGHOURS_WEEKDAYS)));


           /* tvPlaceDetail.setText(
                   *//* cr.getString(cr.getColumnIndex(FavoritesContract.FavoritesEntry.COLUMN_PLACENAME)) + "\n" +
                            cr.getString(cr.getColumnIndex(FavoritesContract.FavoritesEntry.COLUMN_PLACEADDRESS)) + "\n" +
                            cr.getString(cr.getColumnIndex(FavoritesContract.FavoritesEntry.COLUMN_PLACEPHONE)) + "\n" +
                            cr.getString(cr.getColumnIndex(FavoritesContract.FavoritesEntry.COLUMN_PLACERATING)) + "\n" +
                            cr.getString(cr.getColumnIndex(FavoritesContract.FavoritesEntry.COLUMN_PLACEWEBSITE))
                            + "\n" +
                            cr.getString(cr.getColumnIndex(FavoritesContract.FavoritesEntry.COLUMN_PLACENAME)) + "\n" +
                    cr.getString(cr.getColumnIndex(FavoritesContract.FavoritesEntry.COLUMN_PLACEADDRESS)) + "\n" +
                    cr.getString(cr.getColumnIndex(FavoritesContract.FavoritesEntry.COLUMN_PLACEPHONE)) + "\n" +
                    cr.getString(cr.getColumnIndex(FavoritesContract.FavoritesEntry.COLUMN_PLACERATING)) + "\n" +
                    cr.getString(cr.getColumnIndex(FavoritesContract.FavoritesEntry.COLUMN_PLACEWEBSITE))
                            + "\n" +*//*
                    //   cr.getString(cr.getColumnIndex(FavoritesContract.FavoritesEntry.COLUMN_PLACENAME)) + "\n" +

                    //new
                    "RATING: " + cr.getString(cr.getColumnIndex(FavoritesContract.FavoritesEntry.COLUMN_PLACERATING)) + "\n" + "\n" +

                            "PHONE: " + cr.getString(cr.getColumnIndex(FavoritesContract.FavoritesEntry.COLUMN_PLACEPHONE)) + "\n" +
                            "ADDRESS: " + cr.getString(cr.getColumnIndex(FavoritesContract.FavoritesEntry.COLUMN_PLACEADDRESS)) + "\n" + "\n" +

                            "WEBSITE: " + cr.getString(cr.getColumnIndex(FavoritesContract.FavoritesEntry.COLUMN_PLACEWEBSITE)) + "\n" + "\n" +
                            "WEEKDAYS: " + "\n" + cr.getString(cr.getColumnIndex(FavoritesContract.FavoritesEntry.COLUMN_OPENINGHOURS_WEEKDAYS))

            );*/

            Picasso.with(getContext()).load(cr.getString(cr.getColumnIndex(FavoritesContract.FavoritesEntry.COLUMN_PLACEPHOTO_REFERENCE))).into(imgPlaceImage);

        }
        //cr.close();

    }
}
