package siri.project.topnearby.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.util.Log;
import android.widget.RemoteViews;

import java.util.ArrayList;

import siri.project.topnearby.Favorites;
import siri.project.topnearby.MainActivity;
import siri.project.topnearby.Place_Id_Name_and_Image;
import siri.project.topnearby.R;
import siri.project.topnearby.data_actual.FavoritesContract;

/**
 * Implementation of App Widget functionality.
 */
public class FavoriteCityWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        CharSequence widgetText = context.getString(R.string.appwidget_text);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.favorite_city_widget);

        SharedPreferences sharedPreferences = context.getSharedPreferences("WidgetLocation",Context.MODE_PRIVATE);

        views.setTextViewText(R.id.appwidget_text, widgetText);
        views.setTextViewText(R.id.tvFavCityWidget, sharedPreferences.getString("widget_location","ALL_CITIES"));
        views.setTextViewText(R.id.tvFavCityPlacesWidget, getFavouritePlaces(context,sharedPreferences.getString("widget_location","ALL_CITIES")));

        Intent intent = new Intent(context,MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,0,intent,0);
        /*FragmentManager fragmentManager =
        fragmentManager.beginTransaction()
                .replace(R.id.container, new Favorites())
                .commit();*/
        views.setOnClickPendingIntent(R.id.tvFavCityPlacesWidget,pendingIntent);


        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
        Log.d("updateAppWidget", "updated app widget");
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }


    public static void updateAppWidgets(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds)
    {
        for(int appWidgetId: appWidgetIds)
            updateAppWidget(context, appWidgetManager, appWidgetId);
    }



    private static String getFavouritePlaces(Context context, String favoriteCity) {

        ArrayList<String> placesNamesFromDb = new ArrayList<String>();
        String placesNames = "";
        int count = 0;
        Cursor cr = context.getContentResolver().query(FavoritesContract.FavoritesEntry.CONTENT_URI,
                null,
                favoriteCity.equals("ALL_CITIES")?null:FavoritesContract.FavoritesEntry.COLUMN_PLACECITY + " = ?",
                favoriteCity.equals("ALL_CITIES")?null:new String[]{favoriteCity},
                null);
        Log.d("Cursor size: ", String.valueOf(cr.getCount()));
        cr.moveToFirst();
        while (!cr.isAfterLast())
        {
             placesNames = placesNames + "\n" + (++count) + ") " + cr.getString(cr.getColumnIndex(FavoritesContract.FavoritesEntry.COLUMN_PLACENAME));
            //placesNamesFromDb.add(placeName);

            cr.moveToNext();
            Log.d("Place Names",placesNames.toString());
        }
        cr.close();

        return placesNames;
    }
}

