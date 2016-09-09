package com.manuelblanco.capstonestage2.services;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Binder;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.manuelblanco.capstonestage2.R;
import com.manuelblanco.capstonestage2.providers.ContractTripsProvider;
import com.manuelblanco.capstonestage2.utils.URLUtils;
import com.manuelblanco.capstonestage2.utils.Utils;
import com.squareup.picasso.Picasso;

import java.io.IOException;

/**
 * Created by manuel on 25/2/16.
 */

public class WidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new TripsListRemoteViewsFactory(this.getApplicationContext(), intent);
    }
}

class TripsListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private Context mContext;
    private Cursor mCursor;
    private int mAppWidgetId;

    public TripsListRemoteViewsFactory(Context context, Intent intent) {
        mContext = context;
        mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
    }

    @Override
    public int getCount() {
        if (mCursor != null) return mCursor.getCount();
        return 0;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        // Refresh the cursor
        if (mCursor != null) {
            mCursor.close();
        }

        final long token = Binder.clearCallingIdentity();
        try {
            mCursor = mContext.getContentResolver().query(ContractTripsProvider.CONTENT_URI,
                    null, null, null, null);
        } finally {
            Binder.restoreCallingIdentity(token);
        }
    }

    @Override
    public void onDestroy() {
        if (mCursor != null) mCursor.close();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        // Get the data for this position from the content provider

        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.item_list_widget);

        if (mCursor.moveToPosition(position)) {

            rv.setTextViewText(R.id.title_card, mCursor.getString(3));
            rv.setTextViewText(R.id.description_card, mCursor.getString(9));
            rv.setTextViewText(R.id.votes, mCursor.getString(4));

            try {
                Bitmap b = Picasso.with(mContext).load(URLUtils.getURLImageBackendless(mContext,
                        mCursor.getString(5)))
                        .placeholder(R.drawable.trip_placeholder)
                        .resize(250, 200)
                        .onlyScaleDown()
                        .get();

                rv.setImageViewBitmap(R.id.thumbnail_trip, b);
            } catch (IOException e) {
                e.printStackTrace();
            }

            rv.setTextViewCompoundDrawables(R.id.title_card, 0, 0, Utils.getFlagIso(mContext, mCursor.getString(8)), 0);

        }

        // Set the click intent so that we can handle it and show a toast message
        final Intent fillInIntent = new Intent();
        rv.setOnClickFillInIntent(R.id.widget, fillInIntent);

        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
