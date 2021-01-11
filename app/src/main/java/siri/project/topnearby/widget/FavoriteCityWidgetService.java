package siri.project.topnearby.widget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

public class FavoriteCityWidgetService extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */

    static String TAG = "FavoriteCityWidgetService";

    public static final String ACTION_UPDATE_WIDGET = "siri.project.topnearby.widget.updatefavoritewidget";
    public FavoriteCityWidgetService() {
        super("FavoriteCityWidgetService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        Log.d(TAG,"onHandleIntent");
        if(intent!=null)
        {
            if(intent.getAction().equals(ACTION_UPDATE_WIDGET))
            {
                handleIntent();
            }
        }
    }

    private void handleIntent() {

        Log.d(TAG, "handleIntent");
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, FavoriteCityWidget.class));
        FavoriteCityWidget.updateAppWidgets(this, appWidgetManager, appWidgetIds);


    }

    public static void startActionUpdateWidget(Context context)
    {
        Log.d(TAG,"startActionUpdateWidget");
        Intent intent = new Intent(context, FavoriteCityWidgetService.class);
        intent.setAction(ACTION_UPDATE_WIDGET);
        context.startService(intent);
    }
}
