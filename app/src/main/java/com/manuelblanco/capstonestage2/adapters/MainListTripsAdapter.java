package com.manuelblanco.capstonestage2.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.manuelblanco.capstonestage2.R;
import com.manuelblanco.capstonestage2.activities.TypesActivity;
import com.manuelblanco.capstonestage2.fragments.DetailFragment;
import com.manuelblanco.capstonestage2.utils.Constants;
import com.manuelblanco.capstonestage2.utils.URLUtils;
import com.manuelblanco.capstonestage2.utils.Utils;
import com.squareup.picasso.Picasso;

import java.io.UnsupportedEncodingException;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by manuel on 17/07/16.
 */
public class MainListTripsAdapter extends RecyclerView.Adapter<MainListTripsAdapter.MainViewHolder> {

    private Context context;
    private Cursor items;

    private OnItemClickListener listener;

    public MainListTripsAdapter(Context context, OnItemClickListener listener) {
        this.context = context;
        this.listener = listener;
    }

    public interface OnItemClickListener {
        public void onClick(RecyclerView.ViewHolder holder, String idTrip);
    }

    @Override
    public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_trips_main, parent, false);
        return new MainViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MainViewHolder holder, int position) {
        items.moveToPosition(position);

        String s;

        s = items.getString(FieldsTrips.TITLE);
        holder.viewTitle.setText(s);

        s = items.getString(FieldsTrips.DESCRIPTION);
        holder.viewDescription.setText(s);

        s = items.getString(FieldsTrips.RATE);
        holder.viewVotes.setText(s);

        holder.viewTitle.setCompoundDrawablesWithIntrinsicBounds(0, 0, Utils.getFlagIso(context, items.getString(FieldsTrips.COUNTRY)), 0);

        try {
            Picasso.with(context)
                    .load(URLUtils.getURLImageBackendless(context, items.getString(FieldsTrips.PHOTO)))
                    .placeholder(R.drawable.trip_placeholder)
                    .resize(context.getResources().getInteger(R.integer.width_imageview_list),
                            context.getResources().getInteger(R.integer.height_imageview_list))
                    .onlyScaleDown()
                    .into(holder.viewPhoto);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        if (items != null)
            return items.getCount();
        return 0;
    }

    public String getTripId(int posicion) {
        String trip = "";
        if (items != null) {
            items.moveToPosition(posicion);
            trip = items.getString(FieldsTrips.ID_TRIP);
        }

        return trip;
    }

    public Cursor getCursor() {
        return items;
    }

    public void swapCursor(Cursor newCursor) {
        if (newCursor != null) {
            items = newCursor;
            notifyDataSetChanged();
        }
    }

    interface FieldsTrips {
        int ID_TRIP = 1;
        int TITLE = 3;
        int COUNTRY = 8;
        int DESCRIPTION = 9;
        int RATE = 4;
        int PHOTO = 5;
    }

    public class MainViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        @BindView(R.id.title_card)
        TextView viewTitle;
        @BindView(R.id.votes)
        TextView viewVotes;
        @BindView(R.id.description_card)
        TextView viewDescription;
        @BindView(R.id.thumbnail_trip)
        public ImageView viewPhoto;

        public MainViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.onClick(this, getTripId(getAdapterPosition()));
        }

    }
}
