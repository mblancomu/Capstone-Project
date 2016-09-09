package com.manuelblanco.capstonestage2.widget;

import android.app.IntentService;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.RemoteViews;

import com.manuelblanco.capstonestage2.R;
import com.manuelblanco.capstonestage2.activities.MainActivity;
import com.manuelblanco.capstonestage2.providers.ContractTripsProvider;
import com.manuelblanco.capstonestage2.utils.URLUtils;
import com.manuelblanco.capstonestage2.utils.Utils;
import com.squareup.picasso.Picasso;

import java.io.IOException;

/**
 * Created by Matteo on 25/06/2015.
 */
public class TripsWidgetProvider extends AppWidgetProvider {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        // Is the intent an UPDATE_VIEW intent? Then update the widget
      /*  if (action != null && action.equals(FootballIntentService.ACTION_UPDATE_VIEWS)) {
            context.startService(new Intent(context, FootballWidgetIntentService.class));
        }*/
        super.onReceive(context, intent);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        context.startService(new Intent(context, TripsWidgetIntentService.class));
    }

    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager,
                                          int appWidgetId, Bundle newOptions) {
        context.startService(new Intent(context, TripsWidgetIntentService.class));
    }

    // This is the intent that updates the widget view
    public static class TripsWidgetIntentService extends IntentService {

        public TripsWidgetIntentService() {
            super("TripsWidgetIntentService");
        }

        @Override
        protected void onHandleIntent(Intent intent) {

            // Retrieve all of the Today widget ids: these are the widgets we need to update
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this,
                    TripsWidgetProvider.class));

            Cursor cursor = getContentResolver().query(
                    ContractTripsProvider.CONTENT_URI,
                    null,
                    null,
                    null,
                    null);

            if (cursor == null) {
                return;
            }

            if (!cursor.moveToFirst()) {
                cursor.close();
                return;
            }

            // Perform this loop procedure for each widget that belongs to this provider
            for (int appWidgetId : appWidgetIds) {

                // Create an Intent to launch MainActivity
                Intent mainIntent = new Intent(this, MainActivity.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, mainIntent, 0);

                // Get the layout for the App Widget and attach an on-click listener
                RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.item_list_widget);
                remoteViews.setOnClickPendingIntent(R.id.widget, pendingIntent);

                remoteViews.setTextViewText(R.id.title_card, cursor.getString(3));
                remoteViews.setTextViewText(R.id.description_card, cursor.getString(9));
                remoteViews.setTextViewText(R.id.votes, cursor.getString(4));

                try {
                    Bitmap b = Picasso.with(getApplicationContext()).load(URLUtils.getURLImageBackendless(getApplicationContext(),
                            cursor.getString(5)))
                            .placeholder(R.drawable.trip_placeholder)
                            .resize(250, 200)
                            .onlyScaleDown()
                            .get();

                    remoteViews.setImageViewBitmap(R.id.thumbnail_trip, b);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                remoteViews.setTextViewCompoundDrawables(R.id.title_card, 0, 0, Utils.getFlagIso(getApplicationContext(),
                        cursor.getString(8)), 0);

                // Tell the AppWidgetManager to perform an update on the current app widget
                appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
            }
            cursor.close();
        }
    }
}