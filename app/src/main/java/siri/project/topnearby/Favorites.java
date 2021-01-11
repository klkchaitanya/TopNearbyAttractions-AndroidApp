package siri.project.topnearby;


import android.database.Cursor;
import android.database.DatabaseUtils;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import siri.project.topnearby.R;
import siri.project.topnearby.data_actual.FavoritesContract;

/**
 * A simple {@link Fragment} subclass.
 */
public class Favorites extends Fragment implements PlaceListAdapter.ListItemClickListener, LoaderManager.LoaderCallbacks<Cursor> {


    @BindView(R.id.recycler) RecyclerView recyclerView;
    @BindView(R.id.spinnerCities) Spinner spinnerCities;

    /*
    RecyclerView recyclerView;
    Spinner spinnerCities;
    */

    PlaceListAdapter imageAdapter;
    ArrayList<Place_Id_Name_and_Image> listFavoritePlacesIdNameImage;
    static  ArrayList<String> placeCities;
    static boolean updateSpinnerAdapter = true;
    Cursor placeDataCursor;

    int CURSOR_LOADER_ID = 1;

    public Favorites() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View view = inflater.inflate(R.layout.fragment_favorites, container, false);
        ButterKnife.bind(this, view);

       //Butterknife'd recyclerView = (RecyclerView) view.findViewById(R.id.recycler);
       //Butterknife'd spinnerCities = (Spinner) view.findViewById(R.id.spinnerCities);

        placeCities = new ArrayList<>();
        updateSpinnerAdapter = true;

        Bundle bundle = new Bundle();
        bundle.putString("filter_by_city", "All_Cities");


        Log.d("On Create View", "On create view");

        getLoaderManager().initLoader(CURSOR_LOADER_ID, bundle, Favorites.this);

        spinnerCities.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getContext(), spinnerCities.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();

                //--listFavoritePlacesIdNameImage = getFavouritePlaces(spinnerCities.getSelectedItem().toString(),false);
                updateSpinnerAdapter = false;
                Bundle bundle = new Bundle();
                bundle.putString("filter_by_city", spinnerCities.getSelectedItem().toString());
                getLoaderManager().restartLoader(CURSOR_LOADER_ID, bundle, Favorites.this);
                Log.d("List city fav place", String.valueOf(listFavoritePlacesIdNameImage.size()));
                loadRecyclerView(listFavoritePlacesIdNameImage);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

/*
        listFavoritePlacesIdNameImage = getFavouritePlaces("All_Cities",true);

        final ArrayAdapter spinnerCitiesAdapter = new ArrayAdapter(this.getContext(),R.layout.support_simple_spinner_dropdown_item, placeCities);
        spinnerCities.setAdapter(spinnerCitiesAdapter);
        spinnerCities.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getContext(), spinnerCities.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();

                listFavoritePlacesIdNameImage = getFavouritePlaces(spinnerCities.getSelectedItem().toString(),false);
                Log.d("List city fav place", String.valueOf(listFavoritePlacesIdNameImage.size()));
                loadRecyclerView(listFavoritePlacesIdNameImage);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        loadRecyclerView(listFavoritePlacesIdNameImage);*/
        return view;
    }


    public void loadRecyclerView(ArrayList<Place_Id_Name_and_Image> listFavoritePlacesIdNameImage_arg)
    {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        gridLayoutManager.setAutoMeasureEnabled(true);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setHasFixedSize(true);
        imageAdapter = new PlaceListAdapter(getContext(), listFavoritePlacesIdNameImage_arg, listFavoritePlacesIdNameImage_arg == null ?0:listFavoritePlacesIdNameImage_arg.size(), this);
        imageAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(imageAdapter);
    }


    private ArrayList<Place_Id_Name_and_Image> getFavouritePlaces(/*String filter_by_city, boolean updateSpinnerAdapter*/Cursor cr) {

        ArrayList<Place_Id_Name_and_Image> placesIdNameImageFromDb = new ArrayList<Place_Id_Name_and_Image>();
        /*Cursor cr = getContext().getContentResolver().query(FavoritesContract.FavoritesEntry.CONTENT_URI,
                            null,
                            (!filter_by_city.equalsIgnoreCase("All_Cities"))?FavoritesContract.FavoritesEntry.COLUMN_PLACECITY + " = ?":null,
                            (!filter_by_city.equalsIgnoreCase("All_Cities"))?new String[]{filter_by_city}:null,
                            null);*/
        //Cursor cr = cursor;
        Log.d("Cursor size: ", String.valueOf(cr.getCount()));
        if(cr.getCount()==0 && !placeCities.contains("No Favorites added"))
        {
            placeCities.add("No Favorites added");
        }
        cr.moveToFirst();
        while (!cr.isAfterLast())
        {
            Place_Id_Name_and_Image place_Id_Name_and_Image = new Place_Id_Name_and_Image(
                    cr.getString(cr.getColumnIndex(FavoritesContract.FavoritesEntry.COLUMN_PLACEID)),
                    cr.getString(cr.getColumnIndex(FavoritesContract.FavoritesEntry.COLUMN_PLACENAME)),
                    cr.getString(cr.getColumnIndex(FavoritesContract.FavoritesEntry.COLUMN_PLACEPHOTO_REFERENCE)));
            placesIdNameImageFromDb.add(place_Id_Name_and_Image);

            if(updateSpinnerAdapter && !placeCities.contains(cr.getString(cr.getColumnIndex(FavoritesContract.FavoritesEntry.COLUMN_PLACECITY))))
                placeCities.add(cr.getString(cr.getColumnIndex(FavoritesContract.FavoritesEntry.COLUMN_PLACECITY)));

            cr.moveToNext();
            Log.d("Place Id name Image",place_Id_Name_and_Image.placeTitle.toString());
        }
/*
        if(cr != null && !cr.isClosed())
            cr.close();*/

        return placesIdNameImageFromDb;
    }

    @Override
    public void onItemClick(int position) {
        Log.d("onItemClick", "Item at position "+ position+" clicked");

        PlaceDetail fragPlaceDetail = new PlaceDetail();
        Bundle bundle = new Bundle();
        bundle.putString("callingFrom","Favorites");
        bundle.putString("placeId",listFavoritePlacesIdNameImage.get(position).placeId.toString());
        fragPlaceDetail.setArguments(bundle);
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, fragPlaceDetail)
                .addToBackStack(null)
                .commit();

    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        String filter_by_city = args.getString("filter_by_city");
        return new CursorLoader(getActivity(), FavoritesContract.FavoritesEntry.CONTENT_URI,
                                null,
                                (!filter_by_city.equalsIgnoreCase("All_Cities"))?FavoritesContract.FavoritesEntry.COLUMN_PLACECITY + " = ?":null,
                                (!filter_by_city.equalsIgnoreCase("All_Cities"))?new String[]{filter_by_city}:null,
                                null);

    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
       Log.d("Cursor length", String.valueOf(data.getCount()));
       //placeDataCursor = data;
       refreshFragment(data);

       //data.moveToFirst();
       //Log.d("First cursor item", data.getString(data.getColumnIndex(FavoritesContract.FavoritesEntry.COLUMN_PLACECITY)));
    }


    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
    }



    private void refreshFragment(Cursor data) {

        listFavoritePlacesIdNameImage = getFavouritePlaces(data);
        if(updateSpinnerAdapter)
        {
            final ArrayAdapter spinnerCitiesAdapter = new ArrayAdapter(this.getContext(),R.layout.support_simple_spinner_dropdown_item, placeCities);
            spinnerCities.setAdapter(spinnerCitiesAdapter);

        }
        loadRecyclerView(listFavoritePlacesIdNameImage);

    }


}
