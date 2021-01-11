package siri.project.topnearby;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.test.espresso.remote.EspressoRemoteMessage;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class Search extends Fragment implements PlaceListAdapter.ListItemClickListener {


     @BindView(R.id.etSearch) EditText etSearch;
     @BindView(R.id.imgBtnSearch) ImageButton imgBtnSearch;
     @BindView(R.id.imgBtnLocate) ImageButton imgBtnLocate;
     @BindView(R.id.spinnerCategories) Spinner spinnerCategories;
     @BindView(R.id.recycler) RecyclerView recyclerView;


    Context context;

/*  Butterknife'd

    RecyclerView recyclerView;
    Spinner spinnerCategories;
    EditText etSearch;
    ImageButton imgBtnSearch, imgBtnLocate;*/

    PlaceListAdapter imageAdapter;
    List<String> list_images;
    List<Place_Id_Name_and_Image> listPlacecard;



    static String city;


    String[] categoriesArray = {"all", "airport", "amusement_park", "atm", "bank", "bar", "cafe", "car_rental", "church", "gas_station",
            "gym", "library", "museum", "night_club", "park", "pharmacy", "restaurant", "shopping_mall"};

    private int PROXIMITY_RADIUS = 10000;
    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;

    public Search() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d("On Create View", "Search fragment");

        View view = inflater.inflate(R.layout.fragment_search, container, false);
        ButterKnife.bind(this,view);

       //Butterknife'd etSearch = (EditText) view.findViewById(R.id.etSearch);
       //Butterknife'd imgBtnSearch = (ImageButton) view.findViewById(R.id.imgBtnSearch);
       //Butterknife'd imgBtnLocate = (ImageButton) view.findViewById(R.id.imgBtnLocate);
       //Butterknife'd spinnerCategories = (Spinner) view.findViewById(R.id.spinnerCategories);
       //Butterknife'd recyclerView = (RecyclerView) view.findViewById(R.id.recycler);


        etSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent =
                            new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                                    .build(getActivity());
                    startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
                } catch (GooglePlayServicesRepairableException e) {
                    // TODO: Handle the error.
                } catch (GooglePlayServicesNotAvailableException e) {
                    // TODO: Handle the error.
                }
            }
        });



        imgBtnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isConnectedToInternet())
                    imgBtnSearch_Click();
                else
                    Toast.makeText(getContext(),"No Internet! Plese try again!", Toast.LENGTH_SHORT).show();
            }
        });

        imgBtnLocate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isConnectedToInternet())
                    imgBtnLocate_Click();
                else
                    Toast.makeText(getContext(),"No Internet! Plese try again!", Toast.LENGTH_SHORT).show();
            }
        });


        //ArrayAdapter categoriesAdapter = new ArrayAdapter<String>(this.getContext(), R.layout.support_simple_spinner_dropdown_item/*, categoriesArray*/);
        //spinnerCategories.setAdapter(categoriesAdapter);
        spinnerCategories.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(isConnectedToInternet())
                    imgBtnSearch_Click();
                else
                    Toast.makeText(getContext(),"No Internet! Plese try again!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        return view;
    }



    //On result from place auto complete view.
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(getContext(), data);
                etSearch.setText(place.getName());
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(getContext(), data);
                // TODO: Handle the error.
                Log.d("onActivityResult", status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        this.context = context;
        super.onAttach(context);
    }

    private void imgBtnSearch_Click() {
        if (!etSearch.getText().toString().equals("")) {
            new Thread(new Runnable() {
                @Override
                public void run() {

                    Geocoder coder = new Geocoder(getActivity());
                    List<Address> address;

                    try {
                        address = coder.getFromLocationName(etSearch.getText().toString(), 5);
                        if (address == null) {
                            Toast.makeText(getActivity(), "NULL", Toast.LENGTH_SHORT).show();
                        }
                        Address location = address.get(0);
                        city = location.getAddressLine(0);
                        String lat = String.valueOf(location.getLatitude());
                        String lon = String.valueOf(location.getLongitude());
                        //Toast.makeText(getActivity(),lat+" "+lon,Toast.LENGTH_SHORT).show();
                        Log.d("LAT LONG", lat + " " + lon);


                        String searchUrl = getSearchUrl(Double.valueOf(lat), Double.valueOf(lon), categoriesArray[spinnerCategories.getSelectedItemPosition()]);
                        Object[] DataTransfer = new Object[1];
                        DataTransfer[0] = searchUrl;
                        Log.d("onClick", searchUrl);
                       /* GetNearbyPlacesData getNearbyPlacesData = new GetNearbyPlacesData(getContext());
                        getNearbyPlacesData.execute(DataTransfer);*/
                        //Toast.makeText(MapsActivity.this,"Top spots in the Place", Toast.LENGTH_LONG).show();

                        new GetNearbyPlacesData(getContext()) {
                            @Override
                            protected void onPostExecute(String result) {
                                //super.onPostExecute(result);

                                List<HashMap<String, String>> nearbyPlacesList = null;

                                DataParser dataParser = new DataParser();
                                nearbyPlacesList = dataParser.parse(result);

                                addAdapterToRecyclerView(nearbyPlacesList);
                            }
                        }.execute(DataTransfer);


                    } catch (Exception e) {
                        //Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
                        Log.d("img btn click", e.toString());
                    }

                }
            }).start();

        } else {
            Toast.makeText(getContext(), "Please enter any place!", Toast.LENGTH_SHORT).show();
        }
    }

    private void imgBtnLocate_Click() {
        checkPermissionsAndLocatePlace();
    }

    private void checkPermissionsAndLocatePlace()
    {
        final String FUNC_TAG = "checkPermissionsAndLocatePlace";

        final LocationManager locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        if (locationManager != null) {
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

               /* if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)) {

                }
                else {*/
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
                //}
                Log.d("Permission check", "No permission granted");
                return;

            }
            else {

                Log.d("Permission check", "Permission granted");

                locationManager.requestSingleUpdate(LocationManager.NETWORK_PROVIDER, new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        Log.d("OnLocationChanged", "Location changed");
                        getAddress(location.getLatitude(), location.getLongitude());
                    }

                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras) {
                        Log.d("onStatusChanged", "onStatusChanged");
                    }

                    @Override
                    public void onProviderEnabled(String provider) {
                        Log.d("onProviderEnabled", "onProviderEnabled");
                    }

                    @Override
                    public void onProviderDisabled(String provider) {
                        Log.d("onProviderDisabled", "onProviderDisabled");
                    }
                }, null);
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                boolean locationPermissionAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                if (locationPermissionAccepted)
                    checkPermissionsAndLocatePlace();
                break;

        }
    }



    public void getAddress(Double latitude, Double longitude)
    {
        List<Address> addresses;
        String address="";
        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
            address = addresses.get(0).getAddressLine(0);
            Log.d("Address got", address);

        } catch (IOException e) {
            e.printStackTrace();
            Log.d("Exception", e.toString());
        }

        etSearch.setText(address);


    }


    private void addAdapterToRecyclerView(List<HashMap<String, String>> nearbyPlacesList) {

        listPlacecard = new ArrayList<Place_Id_Name_and_Image>();
        String photoBaseUrl = "https://maps.googleapis.com/maps/api/place/photo?";
        String stMaxWidth = "maxwidth=";
        String stPhotoRef = "&photoreference=";
        String key = "&key=AIzaSyBvBsML26knESouhVXITpSoq3vijhFCxN0";

        for(int i=0;i<nearbyPlacesList.size();i++)
        {
            String buildPhoto = photoBaseUrl + stMaxWidth + nearbyPlacesList.get(i).get("photo_width")+ stPhotoRef +nearbyPlacesList.get(i).get("photo_reference") + key;

                    listPlacecard.add(new Place_Id_Name_and_Image(nearbyPlacesList.get(i).get("place_id"),nearbyPlacesList.get(i).get("place_name")
                    ,buildPhoto));
        }

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        gridLayoutManager.setAutoMeasureEnabled(true);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setHasFixedSize(true);
        imageAdapter = new PlaceListAdapter(getContext(), listPlacecard, listPlacecard == null ?0:listPlacecard.size(), this);
        imageAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(imageAdapter);
    }

    @Override
    public void onItemClick(final int position) {
        Log.d("onItemClick", "Item clicked at position "+position +" "+ listPlacecard.get(position).placeTitle.toString());

        if(isConnectedToInternet()) {
            PlaceDetail fragPlaceDetail = new PlaceDetail();
            Bundle bundle = new Bundle();
            bundle.putString("callingFrom", "Search");
            bundle.putString("placeId", listPlacecard.get(position).placeId.toString());
            bundle.putString("placeCity", city);
            fragPlaceDetail.setArguments(bundle);
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.container, fragPlaceDetail)
                    .addToBackStack(null)
                    .commit();
        }
        else
        {
            Toast.makeText(getContext(),"No Internet! Plese try again!", Toast.LENGTH_SHORT).show();
        }
    }


    private String getSearchUrl(double latitude, double longitude, String category) {

        String nearbySearchUrl="https://maps.googleapis.com/maps/api/place/nearbysearch/json?";
        //String radarSearchUrl="https://maps.googleapis.com/maps/api/place/radarsearch/json?";
        StringBuilder googlePlacesUrl = new StringBuilder(nearbySearchUrl);
        googlePlacesUrl.append("location=" + latitude + "," + longitude);
        googlePlacesUrl.append("&radius=" + PROXIMITY_RADIUS);

        Log.d("Category", category);
        if(!category.equalsIgnoreCase("all"))
          googlePlacesUrl.append("&type=" + category);

        googlePlacesUrl.append("&rankby=prominence");
        googlePlacesUrl.append("&sensor=false");
        //googlePlacesUrl.append("&key=" + "AIzaSyAeiNTUItTqvSC1c_sJheL-LkyVoRNW-nk");
        googlePlacesUrl.append("&key="+"AIzaSyBvBsML26knESouhVXITpSoq3vijhFCxN0");
        Log.d("getUrl", googlePlacesUrl.toString());
        return (googlePlacesUrl.toString());
    }



    public boolean isConnectedToInternet() {
        ConnectivityManager cm = (ConnectivityManager)getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }


}
