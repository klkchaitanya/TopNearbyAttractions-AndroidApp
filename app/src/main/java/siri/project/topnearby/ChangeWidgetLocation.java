package siri.project.topnearby;


import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.test.espresso.remote.EspressoRemoteMessage;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import siri.project.topnearby.R;
import siri.project.topnearby.data_actual.FavoritesContract;
import siri.project.topnearby.widget.FavoriteCityWidgetService;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChangeWidgetLocation extends Fragment {


    @BindView(R.id.spinnerWidgetLocation) Spinner spinnerWidgetLocation;
    @BindView(R.id.btnSaveWidgetLocation) Button btnSaveWidgetLocation;

    static ArrayList<String> placeCities;
    /* Butterknife'd
    Spinner spinnerWidgetLocation;
    Button btnSaveWidgetLocation;*/

    public ChangeWidgetLocation() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_change_widget_location, container, false);
        ButterKnife.bind(this, view);

        //Butterknife'd spinnerWidgetLocation = (Spinner)view.findViewById(R.id.spinnerWidgetLocation);
        //Butterknife'd btnSaveWidgetLocation = (Button)view.findViewById(R.id.btnSaveWidgetLocation);

        placeCities = new ArrayList<>();
        loadPlaceCitiesToSpinner();
        final ArrayAdapter spinnerCitiesAdapter = new ArrayAdapter(this.getContext(),R.layout.support_simple_spinner_dropdown_item, placeCities);
        spinnerWidgetLocation.setAdapter(spinnerCitiesAdapter);
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("WidgetLocation", Context.MODE_PRIVATE);
        spinnerWidgetLocation.setSelection(sharedPreferences.contains("widget_location")?spinnerCitiesAdapter.getPosition(sharedPreferences.getString("widget_location","")):0);



        btnSaveWidgetLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences sharedPreferences = getContext().getSharedPreferences("WidgetLocation", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("widget_location", spinnerWidgetLocation.getSelectedItem().toString());
                editor.commit();

                FavoriteCityWidgetService.startActionUpdateWidget(getActivity().getBaseContext());
                Toast.makeText(getContext(), "Widget Location Updated.", Toast.LENGTH_LONG).show();
            }
        });

        return view;

    }


    public void loadPlaceCitiesToSpinner()
    {
        Cursor cr = getContext().getContentResolver().query(FavoritesContract.FavoritesEntry.CONTENT_URI,
                null, null, null, null);
        cr.moveToFirst();
        placeCities.add("ALL_CITIES");
        while (!cr.isAfterLast())
        {
            if(!placeCities.contains(cr.getString(cr.getColumnIndex(FavoritesContract.FavoritesEntry.COLUMN_PLACECITY))))
            {
                placeCities.add(cr.getString(cr.getColumnIndex(FavoritesContract.FavoritesEntry.COLUMN_PLACECITY)));
            }

            cr.moveToNext();

        }
        cr.close();

    }

}
