package com.manuelblanco.capstonestage2.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.appwidget.AppWidgetProviderInfo;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.manuelblanco.capstonestage2.R;
import com.manuelblanco.capstonestage2.activities.MainActivity;
import com.manuelblanco.capstonestage2.providers.ContractTripsProvider;
import com.manuelblanco.capstonestage2.services.WidgetService;

/**
 * Created by manuel on 22/08/16.
 */
public class TripTopCollectionWidget extends AppWidgetProvider {

    public static String CLICK_ACTION = "com.manuelblanco.capstonestage2.widget.LIST_CLICK";
    private static HandlerThread sWorkerThread;
    private static Handler sWorkerQueue;
    private static TripsDataProviderObserver sDataObserver;

    public TripTopCollectionWidget() {
        sWorkerThread = new HandlerThread("TripTopCollectionWidget-worker");
        sWorkerThread.start();
        sWorkerQueue = new Handler(sWorkerThread.getLooper());
    }

    @Override
    public void onEnabled(Context context) {
        final ContentResolver r = context.getContentResolver();
        if (sDataObserver == null) {
            final AppWidgetManager mgr = AppWidgetManager.getInstance(context);
            final ComponentName cn = new ComponentName(context, TripTopCollectionWidget.class);
            sDataObserver = new TripsDataProviderObserver(mgr, cn, sWorkerQueue);
            r.registerContentObserver(ContractTripsProvider.CONTENT_URI, true, sDataObserver);
        }
    }

    @Override
    public void onReceive(Context ctx, Intent intent) {
        final String action = intent.getAction();

        if (action.equals(CLICK_ACTION)) {
            intent.setClass(ctx, MainActivity.class);
            ctx.startActivity(intent);
        }

        super.onReceive(ctx, intent);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Toast.makeText(context, "widget onupdate", Toast.LENGTH_SHORT).show();
        // Update each of the widgets with the remote adapter

        for (int i = 0; i < appWidgetIds.length; ++i) {
            final Intent intent = new Intent(context, WidgetService.class);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);
            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
            final RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.widget);
            if(android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH){
                rv.setRemoteAdapter(R.id.recycler_widget, intent);
            } else {
                rv.setRemoteAdapter(appWidgetIds[i], R.id.recycler_widget, intent);
            }

            rv.setEmptyView(R.id.recycler_widget, R.id.empty_text);

            Bundle options = appWidgetManager.getAppWidgetOptions(appWidgetIds[i]);

            int category = options.getInt(AppWidgetManager.OPTION_APPWIDGET_HOST_CATEGORY, -1);
            boolean isLockScreen = category == AppWidgetProviderInfo.WIDGET_CATEGORY_KEYGUARD;

            final Intent onClickIntent = new Intent(context, TripTopCollectionWidget.class);
            onClickIntent.setAction(TripTopCollectionWidget.CLICK_ACTION);
            onClickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);
            onClickIntent.setData(Uri.parse(onClickIntent.toUri(Intent.URI_INTENT_SCHEME)));
            final PendingIntent onClickPendingIntent = PendingIntent.getBroadcast(context, 0,
                    onClickIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            rv.setPendingIntentTemplate(R.id.recycler_widget, onClickPendingIntent);

            appWidgetManager.updateAppWidget(appWidgetIds[i], rv);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);

        // Build the intent to call the service
        Intent intent = new Intent(context.getApplicationContext(),
                WidgetService.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);

        // Update the widgets via the service
        context.startService(intent);
    }
}

class TripsDataProviderObserver extends ContentObserver {
    private AppWidgetManager mAppWidgetManager;
    private ComponentName mComponentName;

    TripsDataProviderObserver(AppWidgetManager mgr, ComponentName cn, Handler h) {
        super(h);
        mAppWidgetManager = mgr;
        mComponentName = cn;
    }

    @Override
    public void onChange(boolean selfChange) {
        mAppWidgetManager.notifyAppWidgetViewDataChanged(
                mAppWidgetManager.getAppWidgetIds(mComponentName), R.id.recycler_widget);
    }
}
